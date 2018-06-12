package com.cd.uap.controller;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.pojo.Response;
import com.cd.uap.pojo.ValidateModel;
import com.cd.uap.service.RegisterService;
import com.cd.uap.utils.CodeMessage;

@RestController
@RequestMapping("/api/uap/v1/regist")
public class RegisterController {
	
	private static Logger log = LoggerFactory.getLogger(RegisterController.class);
	
	@Autowired 
	private RegisterService registerService;
	
	/**
	 * 注册时，验证手机号规则（1、手机号格式；2、手机号没有注册）
	 * @param validateModel
	 * @return
	 */
	@PostMapping("/checkRegistPhoneNum")
	public Response checkRegistPhoneNum(@RequestBody ValidateModel validateModel) {
		boolean isPhoneNumRule = registerService.checkRegistPhoneNum(validateModel);
		return new Response(0, CodeMessage.OPERATION_SUCCESS, isPhoneNumRule);
	}
	
	/**
	 * 注册用户
	 * @param validateModel
	 * @return
	 */
	@PostMapping
	public Response regist(@RequestBody ValidateModel validateModel) {
		Response response = null;
		try {
			registerService.regist(validateModel);
		} catch (NoSuchAlgorithmException e) {//md5加密异常
			response = new Response(1, CodeMessage.ADMIN_MD5_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (LogicCheckException e) {
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {//SQL异常
			response = new Response(1, CodeMessage.INSERT_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS);
	}
	
	/**
	 * 第一次分配用户角色
	 * @param validateModel 
	 * @return
	 */
	@PostMapping("/roleUser")
	public Response updateRoleUser(@RequestBody ValidateModel validateModel) {
		Response response = null;
		try {
			registerService.updateRoleUser(validateModel);
		} catch (LogicCheckException e) {
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {//SQL异常
			response = new Response(1, CodeMessage.INSERT_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS);
	}
	
	/**
	 * 找回密码时，验证手机号规则（1、手机号格式；2、手机号已注册）
	 * @param validateModel
	 * @return
	 */
	@PostMapping("/checkBackPhoneNum")
	public Response checkBackPhoneNum(@RequestBody ValidateModel validateModel) {
		boolean isPhoneNumRule = registerService.checkBackPhoneNum(validateModel);
		return new Response(0, CodeMessage.OPERATION_SUCCESS, isPhoneNumRule);
	}
	
	/**
	 * 修改密码
	 * @param validateModel
	 * @return
	 */
	@PutMapping("/password")
	public Response updatePassword(@RequestBody ValidateModel validateModel) {
		Response response = null;
		try {
			registerService.updatePassword(validateModel);
		} catch (NoSuchAlgorithmException e) {//md5加密异常
			response = new Response(1, CodeMessage.ADMIN_MD5_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (LogicCheckException e) {
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {
			response = new Response(1, CodeMessage.UPDATE_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
			return new Response(0, CodeMessage.OPERATION_SUCCESS);
	}
	
}
