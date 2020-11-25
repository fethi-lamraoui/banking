package com.banking.test.service;

import com.banking.test.exception.AccountNotFoundException;
import com.banking.test.model.Account;
import com.banking.test.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AccountServiceImpl.class)
public class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void findAcountBalanceByUsername() {
        Optional<Account> account = Optional.of(Account
                .builder()
                .id(1L)
                .username("username")
                .balance(5000D)
                .build());
        Mockito.when(accountRepository.findByUsername(Mockito.anyString())).thenReturn(account);
        double balance = accountService.findAccountBalanceByUsername("username");
        assertEquals(balance, 5000, 0);
    }

    @Test(expected = AccountNotFoundException.class)
    public void findAcountBalanceByUsername_with_not_existed_user_should_throw_AccountNotFoundException() {
        Optional<Account> account = Optional.empty();
        Mockito.when(accountRepository.findByUsername(Mockito.anyString())).thenReturn(account);
        double balance = accountService.findAccountBalanceByUsername("username");
        assertEquals(balance, 5000, 0);
    }
}