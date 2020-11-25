package com.banking.test.mapper;

import com.banking.test.dto.UserDto;
import com.banking.test.form.UserForm;
import com.banking.test.model.Account;
import com.banking.test.model.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;

public class UserMapperTest {

    private User user;
    private UserDto userDto;
    private UserForm userForm;

    @Before
    public void setUp() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        user = User
                .builder()
                .id(1L)
                .username("username")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .roles(Collections.emptySet())
                .account(Account
                        .builder()
                        .id(1L)
                        .username("username")
                        .balance(3000D)
                        .build())
                .transactions(Collections.emptySet())
                .build();

        userDto = UserDto
                .builder()
                .username("username")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();

        userForm = UserForm
                .builder()
                .username("username")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();
    }

    @Test
    public void userToUserDto() {
        UserDto userDtoResult = UserMapper.INSTANCE.userToUserDto(user);

        assertEquals(userDto.getUsername(), userDtoResult.getUsername());
        assertEquals(userDto.getPassword(), userDtoResult.getPassword());
        assertEquals(userDto.getFirstname(), userDtoResult.getFirstname());
        assertEquals(userDto.getLastname(), userDtoResult.getLastname());
    }

    @Test
    public void userDtoToUser() {
        User userResult = UserMapper.INSTANCE.userDtoToUser(userDto);

        assertEquals(user.getUsername(), userResult.getUsername());
        assertEquals(user.getPassword(), userResult.getPassword());
        assertEquals(user.getFirstname(), userResult.getFirstname());
        assertEquals(user.getLastname(), userResult.getLastname());
    }

    @Test
    public void userFormToUserDto() {
        UserDto userDtoResult = UserMapper.INSTANCE.userFormToUserDto(userForm);

        assertEquals(userDto.getUsername(), userDtoResult.getUsername());
        assertEquals(userDto.getPassword(), userDtoResult.getPassword());
        assertEquals(userDto.getFirstname(), userDtoResult.getFirstname());
        assertEquals(userDto.getLastname(), userDtoResult.getLastname());
    }
}