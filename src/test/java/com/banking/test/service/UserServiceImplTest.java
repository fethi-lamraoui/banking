package com.banking.test.service;

import com.banking.test.dto.UserDto;
import com.banking.test.enums.RoleEnum;
import com.banking.test.exception.UserExistException;
import com.banking.test.exception.UserNotSavedException;
import com.banking.test.model.Account;
import com.banking.test.model.Role;
import com.banking.test.model.User;
import com.banking.test.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserServiceImpl.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private User user;
    private UserDto userDto;

    @Before
    public void setUp() throws Exception {
        user = User
                .builder()
                .id(1L)
                .username("username")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .roles(Collections.singleton(Role
                    .builder()
                    .id(1L)
                    .code(RoleEnum.ROLE_USER.getValue())
                    .build()))
                .account(Account
                    .builder()
                    .username("username")
                    .balance(5000D)
                    .build())
                .build();

        userDto = UserDto
                .builder()
                .username("username")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();
    }

    @Test
    public void loadUserByUsername() {
    }

    @Test
    public void save() {
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(user)).thenReturn(user);
        UserDto userDtoResult = userService.save(this.userDto);

        assertEquals(userDto.getUsername(), userDtoResult.getUsername());
        assertEquals(userDto.getPassword(), userDtoResult.getPassword());
        assertEquals(userDto.getFirstname(), userDtoResult.getFirstname());
        assertEquals(userDto.getLastname(), userDtoResult.getLastname());
    }

    @Test(expected = UserExistException.class)
    public void save_existed_username_should_throw_UserExistException() {
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        userService.save(userDto);
    }

    @Test(expected = UserNotSavedException.class)
    public void save_with_exception_should_throw_UserNotSavedException() {
        Mockito.when(userRepository.findByUsername("username")).thenThrow(new RuntimeException());
        userService.save(userDto);
    }
}