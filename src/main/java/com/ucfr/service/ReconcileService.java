package com.ucfr.service;

import com.ucfr.entity.AuditEvent;
import com.ucfr.model.*;
import com.ucfr.repo.AuditRepository;
import com.ucfr.util.SimilarityUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ReconcileService {

    private final AuditRepository auditRepo;
    // simple in-memory canonical store for demo
    private final Map<String, ReconciliationResult> canonicalStore = new ConcurrentHashMap<>();

    public ReconcileService(AuditRepository auditRepo) {
        this.auditRepo = auditRepo;
    }

    public ReconciliationResult reconcile(ReportBundle bundle) {
        // Normalize
        List<Report> reports = bundle.getReports();
        String consumerToken = bundle.getConsumerToken();

        List<AccountRecord> all = new ArrayList<>();
        for (Report r : reports) {
            for (AccountRecord ar : r.getAccounts()) {
                ar.setSource(r.getSource());
                all.add(ar);
            }
        }

        // Simple clustering by issuer+last4 or fuzzy match
        List<List<AccountRecord>> clusters = clusterAccounts(all);

        List<CanonicalRecord> canonicals = clusters.stream().map(this::buildCanonical).collect(Collectors.toList());

        ReconciliationResult result = new ReconciliationResult();
        result.setConsumerToken(consumerToken);
        result.setCanonicalRecords(canonicals);
        result.setTimestamp(Instant.now().toString());

        // store audit
        AuditEvent ev = new AuditEvent();
        ev.setConsumerToken(consumerToken);
        ev.setEventType("RECONCILE");
        ev.setPayload(result.toString());
        ev.setCreatedAt(Instant.now());
        auditRepo.save(ev);

        canonicalStore.put(consumerToken, result);

        return result;
    }

    public ReconciliationResult getByToken(String token) {
        return canonicalStore.get(token);
    }

    private List<List<AccountRecord>> clusterAccounts(List<AccountRecord> all) {
        List<List<AccountRecord>> clusters = new ArrayList<>();
        boolean[] used = new boolean[all.size()];
        for (int i = 0; i < all.size(); i++) {
            if (used[i]) continue;
            AccountRecord a = all.get(i);
            List<AccountRecord> group = new ArrayList<>();
            group.add(a); used[i] = true;
            for (int j = i+1; j < all.size(); j++) {
                if (used[j]) continue;
                AccountRecord b = all.get(j);
                if (isSameAccount(a,b)) { group.add(b); used[j]=true; }
            }
            clusters.add(group);
        }
        return clusters;
    }

    private boolean isSameAccount(AccountRecord a, AccountRecord b) {
        // exact issuer + last4
        if (a.getIssuer() != null && b.getIssuer() != null && a.getIssuer().equalsIgnoreCase(b.getIssuer())) {
            if (a.getAccountId() != null && b.getAccountId() != null) {
                String la = a.getAccountId(); String lb = b.getAccountId();
                if (la.length()>=4 && lb.length()>=4) {
                    if (la.substring(la.length()-4).equals(lb.substring(lb.length()-4))) return true;
                }
            }
        }
        // fuzzy compare name + account type + last reported amount similarity
        double nameSim = SimilarityUtil.levenshteinSimilarity(a.getAccountName(), b.getAccountName());
        if (nameSim > 0.85 && a.getType()!=null && a.getType().equalsIgnoreCase(b.getType())) return true;
        return false;
    }

    private CanonicalRecord buildCanonical(List<AccountRecord> group) {
        CanonicalRecord c = new CanonicalRecord();
        c.setLinkedRecords(group);
        // simple median balance
        List<Double> balances = group.stream().map(AccountRecord::getBalance).filter(Objects::nonNull).collect(Collectors.toList());
        if (!balances.isEmpty()) {
            Collections.sort(balances);
            c.setCanonicalBalance(balances.get(balances.size()/2));
        }
        // status consensus
        Map<String, Long> freq = group.stream().map(AccountRecord::getStatus).filter(Objects::nonNull).collect(Collectors.groupingBy(x->x, Collectors.counting()));
        c.setCanonicalStatus(freq.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("unknown"));
        // confidence = simple heuristic
        double conf = 0.6 + (Math.min(group.size(),3) * 0.1);
        c.setConfidence(Math.min(conf, 0.99));

        // mock explanation via evidence
        List<String> reasons = new ArrayList<>();
        if (balances.size()>1) reasons.add("Balance variance across bureaus; median used.");
        reasons.add("Matched by issuer/last4 or high name similarity.");
        c.setExplain(String.join("; ", reasons));

        return c;
    }

    public byte[] generateDisputePdf(String consumerToken, String canonicalAccountId) throws Exception {
        // simple PDF with summary
        ReconciliationResult r = canonicalStore.get(consumerToken);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            PDPageContentStream cs = new PDPageContentStream(doc, page);
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
            cs.setLeading(14.5f);
            cs.newLineAtOffset(50,700);
            cs.showText("Dispute Package - Consumer: " + consumerToken);
            cs.newLine();
            cs.showText("Account: " + canonicalAccountId);
            cs.newLine();
            cs.showText("Generated at: " + Instant.now().toString());
            cs.endText();
            cs.close();
            doc.save(out);
        }
        return out.toByteArray();
    }
}
