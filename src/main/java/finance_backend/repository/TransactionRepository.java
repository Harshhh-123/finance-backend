package finance_backend.repository;

import finance_backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByDeletedFalse();
    List<Transaction> findByCategoryAndDeletedFalse(String category);
    List<Transaction> findByTypeAndDeletedFalse(Transaction.Type type);
    List<Transaction> findByDateBetweenAndDeletedFalse(LocalDate start, LocalDate end);
}