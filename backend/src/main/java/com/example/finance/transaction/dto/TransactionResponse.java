package com.example.finance.transaction.dto;

import com.example.finance.category.entity.CategoryType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionResponse {

    private final Long id;
    private final CategoryType type;
    private final BigDecimal amount;
    private final LocalDate transactionDate;
    private final Long categoryId;
    private final String categoryName;
    private final String note;

    public TransactionResponse(
            Long id,
            CategoryType type,
            BigDecimal amount,
            LocalDate transactionDate,
            Long categoryId,
            String categoryName,
            String note
    ) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getNote() {
        return note;
    }
}