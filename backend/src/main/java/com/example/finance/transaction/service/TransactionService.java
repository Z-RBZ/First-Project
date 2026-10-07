package com.example.finance.transaction.service;

import com.example.finance.category.entity.Category;
import com.example.finance.category.repository.CategoryRepository;
import com.example.finance.common.exception.CategoryNotFoundException;
import com.example.finance.common.exception.InvalidTransactionException;
import com.example.finance.transaction.dto.CreateTransactionRequest;
import com.example.finance.transaction.dto.TransactionResponse;
import com.example.finance.transaction.entity.Transaction;
import com.example.finance.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<TransactionResponse> getTransactions() {
        return transactionRepository.findAllByOrderByTransactionDateDescIdDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

        if (!category.isActive()) {
            throw new InvalidTransactionException(
                    "CATEGORY_INACTIVE",
                    "分类已停用，不能用于记账"
            );
        }

        if (category.getType() != request.getType()) {
            throw new InvalidTransactionException(
                    "TRANSACTION_TYPE_MISMATCH",
                    "收支类型必须与所选分类类型一致"
            );
        }

        String note = request.getNote();
        if (note != null) {
            note = note.trim();
            if (note.isEmpty()) {
                note = null;
            }
        }

        Transaction transaction = new Transaction(
                request.getType(),
                request.getAmount(),
                request.getTransactionDate(),
                category,
                note
        );

        Transaction savedTransaction = transactionRepository.save(transaction);
        return toResponse(savedTransaction);
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getCategory().getId(),
                transaction.getCategory().getName(),
                transaction.getNote()
        );
    }
}