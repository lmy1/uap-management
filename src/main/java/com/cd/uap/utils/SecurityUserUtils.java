package com.cd.uap.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.cd.uap.exception.SecurityUserException;

public class SecurityUserUtils {
	/**
	 * security读取当前登录用户
	 * @throws SecurityUserException 
	 */
	public static User getSecurityUser() throws SecurityUserException{
		User securityUser = null;
		Object authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = null;
		
		if(authentication!=null&&authentication instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken upat = (UsernamePasswordAuthenticationToken) authentication;
			principal = upat.getPrincipal();
		}else 
			throw new SecurityUserException(CodeMessage.SECURITY_USER_FAILED);
		
		//转为userdetails.User对象
		if(principal!=null&&principal instanceof User) {
			securityUser = (User)principal;
		}else 
			throw new SecurityUserException(CodeMessage.SECURITY_USER_FAILED);
		return securityUser;
	}
}
