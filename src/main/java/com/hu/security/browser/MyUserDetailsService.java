/**
 * @author huDong
 */
package com.hu.security.browser;

//import org.hibernate.validator.internal.util.logging.Log_.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 *
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("登录用户名："+ username);
		//根据用户名查找用户信息
		//根据查找到的用户信息判断是否用户被冻结
		return new User(username,passwordEncoder.encode("123456"),
				true,true,true,true,
				AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}

//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	
//	private Logger logger = LoggerFactory.getLogger(getClass());
//	
//	
//	/* (non-Javadoc)
//	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
//	 */
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		logger.info("登录用户名:"+username);
//		//根据用户名查找用户信息
//		String password = passwordEncoder.encode("123456");
//		logger.info("数据库密码为:"+password);
//		return new User(username, passwordEncoder.encode("123456"),
//				true,true,true,true,
//				AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
//	}

}
