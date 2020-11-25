package com.banking.test.controller;

import com.banking.test.BankingApplication;
import com.banking.test.constant.RequestMappingConstants;
import com.banking.test.constant.ViewConstants;
import com.banking.test.dto.TransactionDto;
import com.banking.test.enums.TransactionTypeEnum;
import com.banking.test.exception.TransactionException;
import com.banking.test.form.TransactionForm;
import com.banking.test.service.AccountService;
import com.banking.test.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = BankingApplication.class)
public class UserControllerTest {

    @Autowired
    UserController userController;

    @MockBean
    AccountService accountService;

    @MockBean
    TransactionService transactionService;

    private TransactionForm transactionForm;

    @Before
    public void setUp() throws Exception {
        transactionForm = TransactionForm
                .builder()
                .transactionType("")
                .amount(1000)
                .build();
    }

    @WithMockUser("username")
    @Test
    public void getUserPage() {
        List<TransactionDto> transactionDtos = Collections.singletonList(TransactionDto.builder().build());
        when(accountService.findAccountBalanceByUsername("username")).thenReturn(5000D);
        when(transactionService.findByUsername("username")).thenReturn(transactionDtos);
        ModelAndView user = userController.getUserPage();
        assertEquals(ViewConstants.USER, user.getViewName());
    }

    @WithMockUser("username")
    @Test
    public void makeTransaction_with_validation_error_should_redirect_to_user() {
        ModelAndView transaction = userController.makeTransaction(this.transactionForm, new BeanPropertyBindingResult(this.transactionForm, "transactionForm"));
        assertEquals(RequestMappingConstants.REDIRECT + RequestMappingConstants.USER, transaction.getViewName());
        assertFalse(transaction.getModel().containsKey("error"));
    }

    @WithMockUser("username")
    @Test
    public void makeTransaction_with_validation_error_should_redirect_to_user_with_error() {
        this.transactionForm.setTransactionType(TransactionTypeEnum.DEPOSIT.name());
        doThrow(TransactionException.class).when(transactionService).makeTransaction(TransactionTypeEnum.DEPOSIT, "username", 1000);
        ModelAndView transaction = userController.makeTransaction(this.transactionForm, new BeanPropertyBindingResult(this.transactionForm, "transactionForm"));
        assertEquals(RequestMappingConstants.REDIRECT + RequestMappingConstants.USER, transaction.getViewName());
        assertTrue(transaction.getModel().containsKey("error"));
    }

}