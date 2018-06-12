package com.cd.uap.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cd.uap.bean.Administrator;
import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.jwt.JWTUtils;
import com.cd.uap.pojo.CacheConfModel;
import com.cd.uap.pojo.CacheMgr;
import com.cd.uap.pojo.Response;
import com.cd.uap.service.SecurityLoginService;
import com.cd.uap.utils.AESUtils;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.SecurityUserUtils;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/uap/v1")
public class SecurityLoginController {
	private static Logger log = LoggerFactory.getLogger(SecurityLoginController.class);
	@Autowired
	JWTUtils jwtUtils;  
	@Autowired
	SecurityLoginService securityLoginService;
	@Autowired
	CacheConfModel cModel;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response createAuthenticationToken(@RequestBody Administrator user) {
    	Response response=null;
        UserDetails userDetails;
		try {
			//对前台传过来的密码进行解密操作
			String password = user.getPassword();
			user.setPassword(AESUtils.desEncrypt(password));
			
			userDetails = securityLoginService.loadUser(user);
		} catch (LogicCheckException e) {
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (NoSuchAlgorithmException e) {
			response = new Response(1, CodeMessage.MD5_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (Exception e) {
			response = new Response(1, CodeMessage.LOGIN_ERROR);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		//缓存用户token
		CacheMgr cm=CacheMgr.getInstance();
		//查询用户记录是否存在，若已存在token不更新
		Object token = cm.getValue(user.getUsername());
		if(!StringUtils.isEmpty(token)) {
			cm.removeCache(user.getUsername());
		}
		
		cModel.setBeginTime(new Date().getTime());
		token = jwtUtils.generateAccessToken(userDetails);
		cm.addCache(user.getUsername(), token, cModel);
        return new Response(0,CodeMessage.OPERATION_SUCCESS,token);
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Response logout() {
    	CacheMgr cm=CacheMgr.getInstance();
    	try {
			cm.removeCache(SecurityUserUtils.getSecurityUser().getUsername());
		} catch (SecurityUserException e) {
			 return new Response(0,CodeMessage.LOGOUT_ERROR,null);
		}
        return new Response(0,CodeMessage.LOGOUT_SUCCESS,"logout success");
    }
    
}
