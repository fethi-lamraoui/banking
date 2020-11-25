package com.banking.test.validator;

import com.banking.test.form.UserForm;
import com.banking.test.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.Assert.*;

public class UserFormValidatorTest {

    private UserFormValidator userFormValidator;
    private Errors errors;

    @Before
    public void setUp() throws Exception {
        userFormValidator = new UserFormValidator();
    }

    @Test
    public void supports() {
        assertTrue(userFormValidator.supports(UserForm.class));
        assertFalse(userFormValidator.supports(Transaction.class));
    }

    @Test
    public void validate() {
        UserForm userForm = UserForm
                .builder()
                .username("username")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();

        errors = new BeanPropertyBindingResult(userForm, "userForm");
        userFormValidator.validate(userForm, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void validate_transactionType_empty_should_return_error() {
        UserForm userForm = UserForm
                .builder()
                .username("")
                .password("")
                .firstname("")
                .lastname("")
                .build();

        errors = new BeanPropertyBindingResult(userForm, "userForm");
        userFormValidator.validate(userForm, errors);
        assertTrue(errors.hasErrors());
        assertEquals(errors.getErrorCount(), 4);
    }
}