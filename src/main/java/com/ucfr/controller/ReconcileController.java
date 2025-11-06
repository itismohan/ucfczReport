package com.ucfr.controller;

import com.ucfr.model.ReconciliationResult;
import com.ucfr.model.ReportBundle;
import com.ucfr.service.ReconcileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ReconcileController {

    private final ReconcileService service;

    public ReconcileController(ReconcileService service) {
        this.service = service;
    }

    @PostMapping("/reconcile")
    public ResponseEntity<ReconciliationResult> reconcile(@RequestBody ReportBundle bundle) {
        ReconciliationResult result = service.reconcile(bundle);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/consumer/{token}/canonical")
    public ResponseEntity<ReconciliationResult> getCanonical(@PathVariable String token) {
        ReconciliationResult result = service.getByToken(token);
        if (result == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }
}
