package com.banking.test.mapper;

import com.banking.test.dto.UserDto;
import com.banking.test.form.UserForm;
import com.banking.test.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);

    UserDto userFormToUserDto(UserForm userForm);
}
