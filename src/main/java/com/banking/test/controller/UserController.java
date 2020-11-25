package com.banking.test.controller;

import com.banking.test.constant.RequestMappingConstants;
import com.banking.test.constant.ViewConstants;
import com.banking.test.enums.TransactionTypeEnum;
import com.banking.test.exception.TransactionException;
import com.banking.test.form.TransactionForm;
import com.banking.test.service.AccountService;
import com.banking.test.service.TransactionService;
import com.banking.test.validator.TransactionFormValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = RequestMappingConstants.USER)
public class UserController {

    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    private TransactionFormValidator transactionFormValidator;
    private TransactionService transactionService;
    private AccountService accountService;

    @Autowired
    public UserController(TransactionFormValidator transactionFormValidator,
                          TransactionService transactionService,
                          AccountService accountService) {
        this.transactionFormValidator = transactionFormValidator;
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @GetMapping
    public ModelAndView getUserPage() {
        ModelAndView modelAndView = new ModelAndView(ViewConstants.USER);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        modelAndView.addObject("user", username);
        modelAndView.addObject("userBalance", accountService.findAccountBalanceByUsername(username));
        modelAndView.addObject("userTransactions", transactionService.findByUsername(username));
        modelAndView.addObject("transactionForm", TransactionForm
                .builder()
                .transactionType(TransactionTypeEnum.DEPOSIT.name())
                .build());
        return modelAndView;
    }

    @PostMapping(RequestMappingConstants.TRANSACTION)
    public ModelAndView makeTransaction(@ModelAttribute TransactionForm transactionForm, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView(RequestMappingConstants.REDIRECT + RequestMappingConstants.USER);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        transactionFormValidator.validate(transactionForm, bindingResult);

        if(!bindingResult.hasErrors()) {
            try {
                transactionService.makeTransaction(TransactionTypeEnum.valueOf(transactionForm.getTransactionType()), username, transactionForm.getAmount());
            } catch (TransactionException ex) {
                modelAndView.addObject("error", ex.getMessage());
                logger.info("Error when making transaction", ex);
            } catch (Exception ex) {

            }
        }

        return modelAndView;
    }

}
