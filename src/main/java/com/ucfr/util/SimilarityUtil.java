package com.ucfr.util;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class SimilarityUtil {
    public static double levenshteinSimilarity(String a, String b) {
        if (a == null || b == null) return 0.0;
        a = a.toLowerCase().trim(); b = b.toLowerCase().trim();
        LevenshteinDistance ld = new LevenshteinDistance();
        int dist = ld.apply(a,b);
        int max = Math.max(a.length(), b.length());
        if (max == 0) return 1.0;
        return 1.0 - ((double) dist / max);
    }
}
