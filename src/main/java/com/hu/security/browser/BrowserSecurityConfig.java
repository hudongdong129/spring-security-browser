/**
 * @author huDong
 */
package com.hu.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.hu.security.core.properties.SecurityProperties;

/**
 * @author Administrator
 *
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler imoocAuthenticationFailureHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.formLogin()
			.loginPage("/authentication/require")
			.loginProcessingUrl("/authentication/form")
			.successHandler(imoocAuthenticationSuccessHandler)
			.failureHandler(imoocAuthenticationFailureHandler)
//		http.httpBasic()
			.and()
			.authorizeRequests()
			.antMatchers("/authentication/require",
					securityProperties.getBrowser().getLoginPage()).permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.csrf().disable();
	}
	

}
