package finance_backend.service;

import finance_backend.model.Transaction;
import finance_backend.model.User;
import finance_backend.repository.TransactionRepository;
import finance_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public Transaction create(Transaction t, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        t.setCreatedBy(user);
        return transactionRepository.save(t);
    }

    public List<Transaction> getAll() {
        return transactionRepository.findByDeletedFalse();
    }

    public List<Transaction> getByCategory(String category) {
        return transactionRepository.findByCategoryAndDeletedFalse(category);
    }

    public List<Transaction> getByType(Transaction.Type type) {
        return transactionRepository.findByTypeAndDeletedFalse(type);
    }

    public List<Transaction> getByDateRange(LocalDate start, LocalDate end) {
        return transactionRepository.findByDateBetweenAndDeletedFalse(start, end);
    }

    public Transaction update(Long id, Transaction updated) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        t.setAmount(updated.getAmount());
        t.setType(updated.getType());
        t.setCategory(updated.getCategory());
        t.setDate(updated.getDate());
        t.setNotes(updated.getNotes());
        return transactionRepository.save(t);
    }

    public void delete(Long id) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        t.setDeleted(true);
        transactionRepository.save(t);
    }

    public Map<String, Object> getSummary() {
        List<Transaction> all = transactionRepository.findByDeletedFalse();
        double income = all.stream().filter(t -> t.getType() == Transaction.Type.INCOME)
                .mapToDouble(Transaction::getAmount).sum();
        double expense = all.stream().filter(t -> t.getType() == Transaction.Type.EXPENSE)
                .mapToDouble(Transaction::getAmount).sum();
        Map<String, Double> byCategory = all.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)));
        return Map.of(
                "totalIncome", income,
                "totalExpense", expense,
                "netBalance", income - expense,
                "byCategory", byCategory,
                "recentTransactions", all.stream().limit(5).collect(Collectors.toList())
        );
    }
}