package com.banking.test.service;

import com.banking.test.dto.TransactionDto;
import com.banking.test.enums.TransactionTypeEnum;

import java.util.List;

public interface TransactionService {
	void makeTransaction(TransactionTypeEnum transactionType, String username, double amount);
	List<TransactionDto> findByUsername(String username);
}
