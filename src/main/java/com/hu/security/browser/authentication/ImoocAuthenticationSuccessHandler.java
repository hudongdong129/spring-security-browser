/**
 * @author huDong
 */
package com.hu.security.browser.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hu.security.core.properties.LoginType;
import com.hu.security.core.properties.SecurityProperties;

/**
 * @author Administrator
 *
 */
@Component("imoocAuthenticationSuccessHandler")
public class ImoocAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private SecurityProperties securityProperties;
	
	/**
	 * Authentication 封装认证信息（认证请求的信息，及返回的认证信息）
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		logger.info("登录成功");
		if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
			
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(authentication));
			
		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
		
	}
	
	
//	@Autowired
//	private ObjectMapper objectMapper;
//	
//	@Autowired
//	private SecurityProperties securityProperties;
//	
//	/* (non-Javadoc)
//	 * Authentication 封装的用户信息
//	 * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
//	 */
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		logger.info("登录成功");
//		
//		if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
//			response.setContentType("application/json;charset=UTF-8");
//			response.getWriter().write(objectMapper.writeValueAsString(authentication));
//		}else {
//			super.onAuthenticationSuccess(request, response, authentication);
//		}
//		
//		
//	}
	

}
