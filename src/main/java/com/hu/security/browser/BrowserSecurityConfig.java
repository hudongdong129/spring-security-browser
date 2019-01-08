/**
 * @author huDong
 */
package com.hu.security.browser;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.hu.security.core.properties.SecurityProperties;
import com.hu.security.core.validate.code.ValidateCodeFilter;

//import com.hu.security.core.properties.SecurityProperties;
//import com.hu.security.core.validate.code.ValidateCodeFilter;

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
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private DataSource dataSource;
	/**
	 * 注入password
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * 记住我的功能需要
	 * @return
	 */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
//		tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
		validateCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
		validateCodeFilter.setSecurityProperties(securityProperties);
		validateCodeFilter.afterPropertiesSet();
		
		
		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
		.formLogin()
			.loginPage("/authentication/require")
			.loginProcessingUrl("/authentication/form")
			.successHandler(imoocAuthenticationSuccessHandler) //替换成功处理器的行为
			.failureHandler(imoocAuthenticationFailureHandler)
			.and()
		.rememberMe()
			.tokenRepository(persistentTokenRepository())
			.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
			.userDetailsService(userDetailsService)
			.and()
			.authorizeRequests()
			.antMatchers("/authentication/require",
					securityProperties.getBrowser().getLoginPage(),
					"/code/*").permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.csrf().disable();
	}
//	@Autowired
//	private SecurityProperties securityProperties;
//	
//	@Autowired
//	private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
//	
//	@Autowired
//	private AuthenticationFailureHandler imoocAuthenticationFailureHandler;
//	
//	
//	@Autowired
//	private DataSource dataSource;
//	
//	@Autowired
//	private UserDetailsService userDetailsService;
//	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Bean
//	public PersistentTokenRepository persistentTokenRepository() {
//		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
//		tokenRepository.setDataSource(dataSource);
////		tokenRepository.setCreateTableOnStartup(true);
//		return tokenRepository;
//	}
//	
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		
//		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
//		validateCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
//		validateCodeFilter.setSecurityProperties(securityProperties);
//		validateCodeFilter.afterPropertiesSet();
//		
//		
//		
//		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
//			.formLogin()
//				.loginPage("/authentication/require")
//				.loginProcessingUrl("/authentication/form")
//				.successHandler(imoocAuthenticationSuccessHandler)
//				.failureHandler(imoocAuthenticationFailureHandler)
//				.and()
//			.rememberMe()
//				.tokenRepository(persistentTokenRepository())
//				.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
//				.userDetailsService(userDetailsService)
////		http.httpBasic()
//			.and()
//			.authorizeRequests()
//			.antMatchers("/authentication/require",
//					securityProperties.getBrowser().getLoginPage(),
//					"/code/*").permitAll()
//			.anyRequest()
//			.authenticated()
//			.and()
//			.csrf().disable();
//	}
	

}
