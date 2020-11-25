package com.banking.test.service;

import com.banking.test.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
	UserDto save(UserDto user);
}
