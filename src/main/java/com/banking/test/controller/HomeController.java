package com.banking.test.controller;

import com.banking.test.constant.RequestMappingConstants;
import com.banking.test.constant.ViewConstants;
import com.banking.test.exception.UserNotSavedException;
import com.banking.test.form.UserForm;
import com.banking.test.mapper.UserMapper;
import com.banking.test.service.UserService;
import com.banking.test.validator.UserFormValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController {

    final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    private UserFormValidator userFormValidator;
    private UserService userService;

    @Autowired
    public HomeController(UserFormValidator userFormValidator, UserService userService) {
        this.userFormValidator = userFormValidator;
        this.userService = userService;
    }

    @GetMapping(RequestMappingConstants.HOME)
    public ModelAndView getHomePage() {
        ModelAndView modelAndView = new ModelAndView(ViewConstants.HOME);
        return modelAndView;
    }

    @GetMapping(RequestMappingConstants.SIGNIN)
    public ModelAndView getSigninPage(Model model, String error) {

        ModelAndView modelAndView = new ModelAndView(ViewConstants.SIGNIN);

        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            modelAndView.setViewName(RequestMappingConstants.REDIRECT + RequestMappingConstants.USER);
        } else if (error != null) {
            modelAndView.addObject("error", "Your username and password is invalid.");
        }

        return modelAndView;
    }

    @GetMapping(RequestMappingConstants.SIGNUP)
    public ModelAndView getSignupPage(@ModelAttribute UserForm userForm) {
        ModelAndView modelAndView = new ModelAndView(ViewConstants.SIGNUP, "userForm", UserForm.builder().build());
        return modelAndView;
    }

    @PostMapping(RequestMappingConstants.SIGNUP)
    public ModelAndView postSignupPage(@ModelAttribute UserForm userForm, BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView(ViewConstants.SIGNUP);
        userFormValidator.validate(userForm, bindingResult);

        if(!bindingResult.hasErrors()) {
            try {
                userService.save(UserMapper.INSTANCE.userFormToUserDto(userForm));
                modelAndView.setViewName(RequestMappingConstants.REDIRECT + RequestMappingConstants.USER);
            } catch (UserNotSavedException ex) {
                modelAndView.addObject("error", ex.getMessage());
                logger.info("Error when saving user", ex);
            }
        }

        return modelAndView;
    }

}
