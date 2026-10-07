package com.example.finance.transaction.dto;

import com.example.finance.category.entity.CategoryType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateTransactionRequest {

    @NotNull(message = "收支类型不能为空")
    private CategoryType type;

    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于 0")
    @Digits(integer = 10, fraction = 2, message = "金额最多保留两位小数")
    private BigDecimal amount;

    @NotNull(message = "交易日期不能为空")
    private LocalDate transactionDate;

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    @Size(max = 255, message = "备注最多 255 个字符")
    private String note;

    public CreateTransactionRequest() {
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

    public String getNote() {
        return note;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setNote(String note) {
        this.note = note;
    }
}