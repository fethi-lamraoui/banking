package com.banking.test.service;

import com.banking.test.dto.TransactionDto;
import com.banking.test.enums.TransactionTypeEnum;
import com.banking.test.exception.TransactionException;
import com.banking.test.model.Account;
import com.banking.test.model.Transaction;
import com.banking.test.repository.AccountRepository;
import com.banking.test.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TransactionServiceImpl.class, AccountServiceImpl.class})
public class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private AccountRepository accountRepository;

    private List<Transaction> transactions;
    private List<TransactionDto> transactionDtos;
    private Account account;
    TransactionDto transactionDto1, transactionDto2;
    Transaction transaction1, transaction2;

    @Before
    public void setUp() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        transactionDto1 = TransactionDto
                .builder()
                .transactionType(TransactionTypeEnum.DEPOSIT)
                .dateTime(now)
                .amount(1000D)
                .balance(17000D)
                .build();

        transactionDto2 = TransactionDto
                .builder()
                .transactionType(TransactionTypeEnum.WITHDRAW)
                .dateTime(now)
                .amount(500D)
                .balance(18000D)
                .build();

        transaction1 = Transaction
                .builder()
                .id(1L)
                .username("username")
                .transactionType(TransactionTypeEnum.DEPOSIT)
                .dateTime(now)
                .amount(1000D)
                .balance(17000D)
                .build();

        transaction2 = Transaction
                .builder()
                .id(1L)
                .username("username")
                .transactionType(TransactionTypeEnum.WITHDRAW)
                .dateTime(now)
                .amount(500D)
                .balance(18000D)
                .build();

        account = Account
                .builder()
                .id(1L)
                .username("username")
                .balance(17000D)
                .build();

        transactionDtos = Arrays.asList(transactionDto1, transactionDto2);
        transactions = Arrays.asList(transaction1, transaction2);
    }

    @Test
    public void findByUsername() {
        Mockito.when(transactionRepository.findByUsername("username")).thenReturn(transactions);
        List<TransactionDto> transactionDtosResult = transactionService.findByUsername("username");

        assertEquals(transactions.size(), transactionDtosResult.size());

        TransactionDto transactionDtoResult = transactionDtosResult.get(0);
        TransactionDto transactionDto = transactionDtos.get(0);

        assertEquals(transactionDto.getTransactionType(), transactionDtoResult.getTransactionType());
        assertEquals(transactionDto.getDateTime(), transactionDtoResult.getDateTime());
        assertEquals(transactionDto.getAmount(), transactionDtoResult.getAmount());
        assertEquals(transactionDto.getBalance(), transactionDtoResult.getBalance());
    }

    @Test(expected = TransactionException.class)
    public void makeTransaction_with_not_existed_user_should_throw_TransactionException() {
        Mockito.when(accountRepository.findByUsername("username")).thenReturn(Optional.empty());
        transactionService.makeTransaction(TransactionTypeEnum.DEPOSIT, "username", 1000);
    }

    @Test(expected = TransactionException.class)
    public void makeTransaction_with_withdraw_more_than_user_balance_should_throw_TransactionException() {
        Mockito.when(accountRepository.findByUsername("username")).thenReturn(Optional.of(account));
        transactionService.makeTransaction(TransactionTypeEnum.WITHDRAW, "username", 10000000);
    }

    @Test
    public void makeTransaction_with_deposit() {
        Mockito.when(accountRepository.findByUsername("username")).thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.save(transaction1)).thenReturn(transaction1);
        Mockito.when(transactionRepository.findByUsername("username")).thenReturn(Collections.singletonList(transaction1));

        transactionService.makeTransaction(TransactionTypeEnum.DEPOSIT, "username", 1000);

        List<TransactionDto> transactionDtosResult = transactionService.findByUsername("username");
        TransactionDto transactionDtoResult = transactionDtosResult.get(0);
        TransactionDto transactionDto = transactionDto1;

        assertEquals(18000, accountService.findAccountBalanceByUsername("username"), 0);
        assertEquals(1, transactionDtosResult.size());
        assertEquals(transactionDto.getTransactionType(), transactionDtoResult.getTransactionType());
        assertEquals(transactionDto.getDateTime(), transactionDtoResult.getDateTime());
        assertEquals(transactionDto.getAmount(), transactionDtoResult.getAmount());
        assertEquals(transactionDto.getBalance(), transactionDtoResult.getBalance());
    }

    @Test
    public void makeTransaction_with_withdraw() {
        Mockito.when(accountRepository.findByUsername("username")).thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.save(transaction2)).thenReturn(transaction2);
        Mockito.when(transactionRepository.findByUsername("username")).thenReturn(Collections.singletonList(transaction2));

        transactionService.makeTransaction(TransactionTypeEnum.WITHDRAW, "username", 500);

        List<TransactionDto> transactionDtosResult = transactionService.findByUsername("username");
        TransactionDto transactionDtoResult = transactionDtosResult.get(0);
        TransactionDto transactionDto = transactionDto2;

        assertEquals(16500, accountService.findAccountBalanceByUsername("username"), 0);
        assertEquals(1, transactionDtosResult.size());
        assertEquals(transactionDto.getTransactionType(), transactionDtoResult.getTransactionType());
        assertEquals(transactionDto.getDateTime(), transactionDtoResult.getDateTime());
        assertEquals(transactionDto.getAmount(), transactionDtoResult.getAmount());
        assertEquals(transactionDto.getBalance(), transactionDtoResult.getBalance());
    }

}