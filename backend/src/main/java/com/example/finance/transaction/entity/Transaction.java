package com.example.finance.transaction.entity;

import com.example.finance.category.entity.Category;
import com.example.finance.category.entity.CategoryType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoryType type;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(length = 255)
    private String note;

    protected Transaction() {
    }

    public Transaction(
            CategoryType type,
            BigDecimal amount,
            LocalDate transactionDate,
            Category category,
            String note
    ) {
        this.type = type;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.category = category;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public CategoryType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public Category getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }
}