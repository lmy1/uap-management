package com.cd.uap.controller;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cd.uap.bean.Application;
import com.cd.uap.bean.ApplicationInfo;
import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.exception.ValidateException;
import com.cd.uap.pojo.PageBean;
import com.cd.uap.pojo.Response;
import com.cd.uap.service.ApplicationService;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.ValidatedUtils;


@RestController
@RequestMapping("/api/uap/v1/application")
public class ApplicationController {
	
	@Autowired 
	private ApplicationService applicationService;
	
	private static Logger log = LoggerFactory.getLogger(ApplicationController.class);
	
	/**
	 * 新增
	 * 
	 * @param application
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Response addApplication(@RequestBody @Validated Application application,BindingResult bindResult) {
		
		Response response = null;
		Integer id = null;
		
		//如何数据不符合规范，会抛出统一异常	
		try {
			ValidatedUtils.validate(bindResult);
			id = applicationService.addApplication(application);
		} catch (ValidateException e) {//校验异常
			response = new Response(1, CodeMessage.VALIDATE_FAILED.getCode(), e.getMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (LogicCheckException e) {//业务逻辑校验
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {//SQL异常
			response = new Response(1, CodeMessage.INSERT_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS, id);
	}
	
	/**
	 * 根据id全字段修改
	 * 
	 * @param id
	 * @param application
	 * @return
	 */
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Response updateApplication(@PathVariable Integer id,@RequestBody @Validated Application application,BindingResult bindResult) {
		
		Response response = null;
		//如何数据不符合规范，会抛出统一异常
		try {
			ValidatedUtils.validate(bindResult);
			application.setId(id);
			applicationService.updateApplication(application);
		} catch (ValidateException e) {//校验异常
			response = new Response(1, CodeMessage.VALIDATE_FAILED.getCode(), e.getMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (LogicCheckException e) {//业务逻辑校验
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {//SQL异常
			response = new Response(1, CodeMessage.UPDATE_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS, id);
	}
	
	/**
	 * 删除
	 * 
	 * @param ids	要删除的ID数组
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Response deleteApplication(@PathVariable("id") Integer id) {
		
		Response response = new Response();
		try {
			applicationService.deleteApplication(id);
		} catch (SQLException  e) {
			response.setStatus(1);
			response.setError(CodeMessage.DELETE_FAILED.getCode(), CodeMessage.DELETE_FAILED.getMsg());
			log.error(CodeMessage.DELETE_FAILED.getCode() + ":" + CodeMessage.DELETE_FAILED.getMsg());
			return response;
		} catch (LogicCheckException e) {
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} 
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		return response;
	}
  	
	/**
	 * 根据id查询
	 * 
	 * @param id	需要查询的ID
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Response findApplicationById(@PathVariable("id") Integer id) {
		
		Response response = new Response();
		ApplicationInfo application = null;
		try {
			application = applicationService.findApplicationById(id);
		} catch (SQLException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SELECT_FAILED.getCode(), CodeMessage.SELECT_FAILED.getMsg());
			log.error(CodeMessage.SELECT_FAILED.getCode() + ":" + CodeMessage.SELECT_FAILED.getMsg());
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(application);
		return response;
	}

	
	/**
	 * 条件分页查询(只传条件则是条件查询，只传分页则是分页查询)
	 * 
	 * @param queryModels
	 * @return
	 */
	@RequestMapping(value="/conditions",method = RequestMethod.GET)
	public Response findApplicationByConditions (Application application,@RequestParam(defaultValue="0")Integer page, @RequestParam(defaultValue="0")Integer size){
		
		Response response = new Response();
		PageBean<ApplicationInfo> pageBean = null;
		try {
			pageBean = applicationService.findApplicationByPageAndConditions(application,page,size);
		} catch (SQLException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SELECT_FAILED.getCode(), CodeMessage.SELECT_FAILED.getMsg());
			log.error(CodeMessage.SELECT_FAILED.getCode() + ":" + CodeMessage.SELECT_FAILED.getMsg());
			return response;
		} catch (SecurityUserException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SECURITY_USER_FAILED.getCode(), CodeMessage.SECURITY_USER_FAILED.getMsg());
			log.error(CodeMessage.SECURITY_USER_FAILED.getCode()+","+CodeMessage.SECURITY_USER_FAILED.getMsg());
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(pageBean);
		return response;
		
	}
	
}












