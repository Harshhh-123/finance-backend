package finance_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String category;

    private LocalDate date;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    private boolean deleted = false;

    public enum Type {
        INCOME, EXPENSE
    }
}