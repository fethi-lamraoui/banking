package com.banking.test.validator;

import com.banking.test.form.TransactionForm;
import com.banking.test.form.UserForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TransactionFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TransactionForm.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        TransactionForm transactionForm = (TransactionForm) obj;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "transactionType", "transaction.validation.transactionType", "Transaction type is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", "transaction.validation.amount", "Amount is required");

    }
}