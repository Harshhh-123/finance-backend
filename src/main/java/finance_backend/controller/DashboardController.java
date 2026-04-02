package finance_backend.controller;

import finance_backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final TransactionService transactionService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<?> getSummary() {
        return ResponseEntity.ok(transactionService.getSummary());
    }
}