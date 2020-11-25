package com.banking.test.controller;

import com.banking.test.BankingApplication;
import com.banking.test.constant.RequestMappingConstants;
import com.banking.test.constant.ViewConstants;
import com.banking.test.dto.UserDto;
import com.banking.test.exception.UserNotSavedException;
import com.banking.test.form.UserForm;
import com.banking.test.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ConcurrentModel;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.ModelAndView;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = BankingApplication.class)
public class HomeControllerTest {

    @Autowired
    HomeController homeController;

    @MockBean
    private UserService userService;

    private UserForm userForm;

    @Before
    public void setUp() throws Exception {
        userForm = UserForm.builder().build();
    }

    @Test
    public void getHomePage() {
        ModelAndView home = homeController.getHomePage();
        assertEquals(ViewConstants.HOME, home.getViewName());
    }

    @WithMockUser("anonymousUser")
    @Test
    public void getSigninPageNotConnected() {
        ModelAndView signin = homeController.getSigninPage(new ConcurrentModel(), null);
        assertEquals(ViewConstants.SIGNIN, signin.getViewName());
        assertFalse(signin.getModel().containsKey("error"));
    }

    @WithMockUser("anonymousUser")
    @Test
    public void getSigninPageErrorWithUsernameAndOrPassword() {
        ModelAndView signin = homeController.getSigninPage(new ConcurrentModel(), "true");
        assertEquals(ViewConstants.SIGNIN, signin.getViewName());
        assertTrue(signin.getModel().containsKey("error"));
    }

    @WithMockUser("username")
    @Test
    public void getSigninPageConnected() {
        ModelAndView signin = homeController.getSigninPage(new ConcurrentModel(), "");
        assertEquals(RequestMappingConstants.REDIRECT + RequestMappingConstants.USER, signin.getViewName());
    }

    @Test
    public void getSignupPage() {
        ModelAndView signup = homeController.getSignupPage(userForm);
        assertEquals(ViewConstants.SIGNUP, signup.getViewName());
        assertEquals(signup.getModel().get("userForm"), userForm);
    }

    @Test
    public void testPostSignupPage() {
        ModelAndView signup = homeController.postSignupPage(this.userForm, new BeanPropertyBindingResult(this.userForm, "userForm"));
        assertEquals(ViewConstants.SIGNUP, signup.getViewName());

        this.userForm = UserForm
                .builder()
                .username("username")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();

        Mockito.when(userService.save(Mockito.any(UserDto.class))).thenThrow(UserNotSavedException.class);
        signup = homeController.postSignupPage(this.userForm, new BeanPropertyBindingResult(this.userForm, "userForm"));
        assertEquals(ViewConstants.SIGNUP, signup.getViewName());
        assertTrue(signup.getModel().containsKey("error"));

        Mockito.when(userService.save(Mockito.any(UserDto.class))).thenReturn(UserDto.builder().build());
        signup = homeController.postSignupPage(this.userForm, new BeanPropertyBindingResult(this.userForm, "userForm"));
        assertEquals(RequestMappingConstants.REDIRECT + RequestMappingConstants.USER, signup.getViewName());
    }
}