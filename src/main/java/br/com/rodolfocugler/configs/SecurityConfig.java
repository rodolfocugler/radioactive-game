package br.com.rodolfocugler.configs;

import br.com.rodolfocugler.filters.JWTAuthenticationFilter;
import br.com.rodolfocugler.filters.JWTAuthorizationFilter;
import br.com.rodolfocugler.services.AccountService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static br.com.rodolfocugler.configs.AuthenticationConfig.SIGN_UP_URL;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final AccountService accountService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public SecurityConfig(AccountService accountService,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.accountService = accountService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(new JWTAuthorizationFilter(authenticationManager()))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(accountService).passwordEncoder(bCryptPasswordEncoder);
  }
}