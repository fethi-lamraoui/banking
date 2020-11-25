package com.banking.test.validator;

import com.banking.test.form.UserForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserForm.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        UserForm userForm = (UserForm) obj;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "signup.validation.username", "username is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "signup.validation.password", "password is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "signup.validation.firstname", "firstname is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "signup.validation.lastname", "lastname is required");

    }
}