package finance_backend.controller;

import finance_backend.model.Transaction;
import finance_backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        if (category != null)
            return ResponseEntity.ok(transactionService.getByCategory(category));
        if (type != null)
            return ResponseEntity.ok(transactionService.getByType(
                    Transaction.Type.valueOf(type.toUpperCase())));
        if (from != null && to != null)
            return ResponseEntity.ok(transactionService.getByDateRange(
                    LocalDate.parse(from), LocalDate.parse(to)));
        return ResponseEntity.ok(transactionService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody Transaction t,
                                    Authentication auth) {
        try {
            return ResponseEntity.ok(transactionService.create(t, auth.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Transaction t) {
        try {
            return ResponseEntity.ok(transactionService.update(id, t));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            transactionService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}