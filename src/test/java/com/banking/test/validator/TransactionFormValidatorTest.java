package com.banking.test.validator;

import com.banking.test.enums.TransactionTypeEnum;
import com.banking.test.form.TransactionForm;
import com.banking.test.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.Assert.*;

public class TransactionFormValidatorTest {

    private TransactionFormValidator transactionFormValidator;
    private Errors errors;

    @Before
    public void setUp() throws Exception {
        transactionFormValidator = new TransactionFormValidator();
    }

    @Test
    public void supports() {
        assertTrue(transactionFormValidator.supports(TransactionForm.class));
        assertFalse(transactionFormValidator.supports(Transaction.class));
    }

    @Test
    public void validate() {
        TransactionForm transactionForm = TransactionForm
                .builder()
                .transactionType(TransactionTypeEnum.DEPOSIT.name())
                .amount(1000)
                .build();

        errors = new BeanPropertyBindingResult(transactionForm, "transactionForm");
        transactionFormValidator.validate(transactionForm, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void validate_transactionType_empty_should_return_error() {
        TransactionForm transactionForm = TransactionForm
                .builder()
                .transactionType("")
                .amount(0)
                .build();

        errors = new BeanPropertyBindingResult(transactionForm, "transactionForm");
        transactionFormValidator.validate(transactionForm, errors);
        assertTrue(errors.hasErrors());
        assertEquals(errors.getErrorCount(), 1);
        assertEquals(errors.getFieldError().getField(), "transactionType");
    }
}