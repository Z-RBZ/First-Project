package com.example.finance.transaction.controller;

import com.example.finance.transaction.dto.CreateTransactionRequest;
import com.example.finance.transaction.dto.TransactionResponse;
import com.example.finance.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<TransactionResponse> getTransactions() {
        return transactionService.getTransactions();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        return transactionService.createTransaction(request);
    }
}