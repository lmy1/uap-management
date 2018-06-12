package com.cd.uap.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cd.uap.pojo.Response;
import com.cd.uap.utils.CodeMessage;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LoginController {
	/**
	 * login接口必须定义为get方式，security默认校验用户的接口为post方式
	 * 
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public Response login(String state, HttpSession session) {
		// 登录失败返回
		if ("error".equals(state))
			return new Response(0, CodeMessage.LOGIN_ERROR, "login error");
		// 登录成功返回
		if ("success".equals(state)) {
			String sessionId = session.getId();
			return new Response(0, CodeMessage.LOGIN_SUCCESS, "JSESSIONID=" + sessionId);
		}
		// 注销后返回
		if ("logout".equals(state))
			return new Response(0, CodeMessage.LOGOUT_SUCCESS, "logout success");

		return new Response(0, CodeMessage.LOGIN_CODE, "login");
	}
}
