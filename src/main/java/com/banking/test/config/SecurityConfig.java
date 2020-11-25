package com.banking.test.config;


import com.banking.test.constant.RequestMappingConstants;
import com.banking.test.enums.RoleEnum;
import com.banking.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String JSESSIONID = "JSESSIONID";

  @Autowired
  private UserService userService;

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(RequestMappingConstants.USER + "/**")
        .hasRole(RoleEnum.ROLE_USER.getRoleValue())
        .antMatchers(RequestMappingConstants.HOME).permitAll()
        .and()
        .formLogin()
        .loginPage(RequestMappingConstants.SIGNIN)
        .failureUrl(RequestMappingConstants.SIGNIN + "?error=true")
        .defaultSuccessUrl(RequestMappingConstants.USER)
        .permitAll()
        .and()
        .logout()
        .logoutSuccessUrl(RequestMappingConstants.HOME)
        .invalidateHttpSession(true)
        .deleteCookies(JSESSIONID)
        .permitAll()
        .and()
        .exceptionHandling().accessDeniedPage(RequestMappingConstants.FORBIDDEN)
        .and()
        .csrf().disable()
        .headers().frameOptions().sameOrigin();
  }

}
