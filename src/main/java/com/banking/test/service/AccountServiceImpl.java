package com.banking.test.service;

import com.banking.test.exception.AccountNotFoundException;
import com.banking.test.model.Account;
import com.banking.test.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

	final static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private AccountRepository accountRepository;

	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public double findAccountBalanceByUsername(String username) {
		logger.info("Getting balance for " + username);
		Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException());
		return account.getBalance();
	}
}
