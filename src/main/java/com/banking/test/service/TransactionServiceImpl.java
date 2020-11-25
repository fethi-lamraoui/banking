package com.banking.test.service;

import com.banking.test.dto.TransactionDto;
import com.banking.test.enums.TransactionTypeEnum;
import com.banking.test.exception.TransactionException;
import com.banking.test.mapper.TransactionMapper;
import com.banking.test.model.Account;
import com.banking.test.model.Transaction;
import com.banking.test.repository.AccountRepository;
import com.banking.test.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class TransactionServiceImpl implements TransactionService {

	final static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

	private TransactionRepository transactionRepository;
	private AccountRepository accountRepository;

	@Autowired
	public TransactionServiceImpl(TransactionRepository transactionRepository,
								  AccountRepository accountRepository) {
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
	}

	@Override
	public List<TransactionDto> findByUsername(String username) {
		return TransactionMapper.INSTANCE.transactionTotransactionDto(transactionRepository.findByUsername(username));
	}

	// The transaction by default (rollbackFor = { RuntimeException.class, Error.class })
	@Override
	public void makeTransaction(TransactionTypeEnum transactionType, String username, double amount) {

		synchronized (accountRepository.findByUsername(username)) {
			try {
				Account userAccount = accountRepository.findByUsername(username).orElseThrow(()
						-> new TransactionException(TransactionException.TRANSACTION_EXCEPTION));

				double balance = userAccount.getBalance();

				switch (transactionType) {
					case DEPOSIT:
						balance = deposit(balance, amount);
						break;
					case WITHDRAW:
						balance = withdraw(balance, amount);
						break;
				}

				transactionRepository.save(Transaction
						.builder()
						.username(username)
						.transactionType(transactionType)
						.dateTime(LocalDateTime.now())
						.amount(amount)
						.balance(userAccount.getBalance())
						.build());

				userAccount.setBalance(balance);
				accountRepository.save(userAccount);
			} catch (Exception ex) {
				throw new TransactionException(ex.getMessage());
			}
		}
	}

	private double deposit(double balance, double amount) {
		logger.info("Deposit " + amount);
		return balance + amount;
	}

	private double withdraw(double balance, double amount) {
		if(amount <= balance) {
			logger.info("Withdrawing " + amount);
			return balance - amount;
		} else {
			throw new TransactionException(TransactionException.WITHDRAW_EXCEPTION);
		}
	}
}
