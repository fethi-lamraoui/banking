package com.banking.test.service;

import com.banking.test.dto.UserDto;
import com.banking.test.enums.RoleEnum;
import com.banking.test.exception.UserExistException;
import com.banking.test.exception.UserNotSavedException;
import com.banking.test.mapper.UserMapper;
import com.banking.test.model.Account;
import com.banking.test.model.Role;
import com.banking.test.model.User;
import com.banking.test.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() ->
				new UsernameNotFoundException("could not find the user '" + username + "'"));

		return org.springframework.security.core.userdetails.User
				.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.disabled(false)
				.accountExpired(false)
				.accountLocked(false)
				.authorities(user
						.getRoles()
						.stream()
						.map(Role::getCode)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList()))
				.build();
	}

	@Override
	public UserDto save(UserDto userDto) {
		try {
			logger.debug("Persisting user: '" + userDto + "'");

			if (!userRepository.findByUsername(userDto.getUsername()).isPresent()) {
				User user = UserMapper
						.INSTANCE
						.userDtoToUser(userDto);

				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setRoles(Collections.singleton(Role
						.builder()
						.id(1L)
						.code(RoleEnum.ROLE_USER.getValue())
						.build()));
				user.setAccount(Account
						.builder()
						.username(userDto.getUsername())
						.balance(0.0)
						.build());

				User savedUser = userRepository.save(user);
				return UserMapper
						.INSTANCE
						.userToUserDto(savedUser);
			} else {
				throw new UserExistException();
			}

		} catch (UserExistException ex)	{
			logger.info("user already exists");
			throw new UserExistException();
		} catch (Exception ex) {
			throw new UserNotSavedException();
		}
	}

}
