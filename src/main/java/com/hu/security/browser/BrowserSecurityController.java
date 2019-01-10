/**
 * @author huDong
 */
package com.hu.security.browser;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hu.security.browser.support.SocialUserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hu.security.browser.support.SimpleResponse;
import com.hu.security.core.properties.SecurityProperties;
import org.springframework.web.context.request.ServletWebRequest;

//import com.hu.security.browser.support.SimpleResponse;
//import com.hu.security.core.properties.SecurityProperties;

/**
 * @author Administrator
 *
 */
@RestController
public class BrowserSecurityController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private RequestCache requestCache = new HttpSessionRequestCache(); 
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/authentication/require")
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();
			logger.info("请求的路径为："+targetUrl);
			if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
				redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
			}
					
		}
		return new SimpleResponse("访问的服务需要身份认证，请引导用户到登陆页");
		
	}

	@GetMapping("/social/user")
	public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
		SocialUserInfo userInfo = new SocialUserInfo();
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
		userInfo.setProviderUserId(connection.getKey().getProviderUserId());
		userInfo.setProviderId(connection.getKey().getProviderId());
		userInfo.setNickName(connection.getDisplayName());
		userInfo.setHeadimg(connection.getImageUrl());
		return userInfo;
	}


	
//	private RequestCache requestCache = new HttpSessionRequestCache();
//	
//	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//	
//	@Autowired
//	private SecurityProperties securityProperties;
//	
//	
//	/**
//	 *  当需要身份认证时，跳转到这里
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws IOException
//	 */
//	@RequestMapping("/authentication/require")
//	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
//	public SimpleResponse requireAuthentication(HttpServletRequest request,HttpServletResponse response) throws IOException {
//		
//		SavedRequest savedRequest = requestCache.getRequest(request, response);
//		
//		
//		
//		if (savedRequest != null) {
//			String targetUrl = savedRequest.getRedirectUrl();
//			logger.info("引发跳转的请求:" + targetUrl);
//			if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
//				
//				redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
//			}
//		}
//		
//		return new SimpleResponse("访问的服务需要身份认证，请引导用户到登陆页");
//		
//	}
}
