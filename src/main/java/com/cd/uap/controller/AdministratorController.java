package com.cd.uap.controller;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.cd.uap.bean.Administrator;
import com.cd.uap.bean.AdministratorInfo;
import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.exception.ValidateException;
import com.cd.uap.pojo.FieldModel;
import com.cd.uap.pojo.PageBean;
import com.cd.uap.pojo.Response;
import com.cd.uap.service.AdministratorService;
import com.cd.uap.utils.AESUtils;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.ValidatedUtils;


@RestController
@RequestMapping("/api/uap/v1/administrator")
public class AdministratorController {
	
	private static Logger log = LoggerFactory.getLogger(AdministratorController.class);
	
	@Autowired 
	private AdministratorService administratorService;
	
	/**
	 * 新增
	 * 
	 * @param administrator
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Response addAdministrator(@RequestBody @Validated Administrator administrator, BindingResult bindingResult) {
		
		Response response = null;
		Integer id = null;
		try {
			//对前台传过来的密码进行解密操作
			String password = administrator.getPassword();
			administrator.setPassword(AESUtils.desEncrypt(password));
			
			ValidatedUtils.validate(bindingResult);//实体类校验
			id = administratorService.addAdministrator(administrator);
		} catch (NoSuchAlgorithmException e) {//md5加密异常
			response = new Response(1, CodeMessage.ADMIN_MD5_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
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
		} catch (Exception e) {
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
	 * @param administrator
	 * @return
	 */
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Response updateAdministrator(@PathVariable Integer id,@RequestBody @Validated Administrator administrator, BindingResult bindingResult) {
		
		Response response = null;
		try {
			ValidatedUtils.validate(bindingResult);//实体类校验
			administratorService.updateAdministrator(id,administrator);
		} catch (NoSuchAlgorithmException e) {//md5加密异常
			response = new Response(1, CodeMessage.ADMIN_MD5_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
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
		} catch (Exception e) {
			response = new Response(1, CodeMessage.UPDATE_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS, id);
	}
	
	/**
	 * 冻结、可用
	 * 
	 * @param ids
	 * @param fieldModel
	 * @return
	 */
	@RequestMapping(value = "/{id}/logic", method = RequestMethod.PUT)
	public Response deleteAdministratorByLogic(@PathVariable("id") Integer id, @RequestBody FieldModel fieldModel) {
		
		Response response = null;
		try {
			administratorService.updateFieldById(id,fieldModel);
		} catch (LogicCheckException e) {//业务逻辑校验
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {//SQL异常
			response = new Response(1, CodeMessage.INSERT_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (Exception e) {
			response = new Response(1, CodeMessage.UPDATE_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS);
	}
	
	/**
	 * 批量删除(真删)
	 * 
	 * @param ids	要删除的ID数组
	 * @return
	 */
	@RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
	public Response deleteAdministrator(@PathVariable("ids") Integer[] ids) {
		
		Response response = new Response();
		HashMap<String, List<Integer>> resultMap = administratorService.deleteAdministrator(ids);
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(resultMap);
		return response;
	}

	/**
	 * 根据id查询
	 * 
	 * @param id	需要查询的ID
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Response findAdministratorById(@PathVariable("id") Integer id) {
		
		Response response = new Response();
		AdministratorInfo administrator = null;
		try {
			administrator = administratorService.findAdministratorById(id);
		} catch (SQLException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SELECT_FAILED.getCode(), CodeMessage.SELECT_FAILED.getMsg());
			log.error(CodeMessage.SELECT_FAILED.getCode() + ":" + CodeMessage.SELECT_FAILED.getMsg());
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(administrator);
		return response;
	}

	
	/**
	 * 条件分页查询(只传条件则是条件查询，只传分页则是分页查询)
	 * 
	 * @param queryModels
	 * @return
	 */
	@RequestMapping(value="/conditions",method = RequestMethod.GET)
	public Response findAdministratorByConditions (Administrator administrator,@RequestParam(defaultValue="0")Integer page, @RequestParam(defaultValue="0")Integer size){
		
		Response response = new Response();
		PageBean<AdministratorInfo> pageBean = null;
		try {
			pageBean = administratorService.findAdministratorByPageAndConditions(administrator,page,size);
		} catch (SQLException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SELECT_FAILED.getCode(), CodeMessage.SELECT_FAILED.getMsg());
			log.error(CodeMessage.SELECT_FAILED.getCode() + ":" + CodeMessage.SELECT_FAILED.getMsg());
			return response;
		} catch (SecurityUserException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SECURITY_USER_FAILED.getCode(), CodeMessage.SECURITY_USER_FAILED.getMsg());
			log.error(CodeMessage.SECURITY_USER_FAILED.getCode() + ":" + CodeMessage.SECURITY_USER_FAILED.getMsg());
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(pageBean);
		return response;
		
	}
	
	/**
	 * 获得当前登录管理员的下一级别
	 * @return
	 */
	@RequestMapping(value = "/type", method = RequestMethod.GET)
	public Response getType() {
		Response response = new Response();
		Map<Integer, String> typeMap = null;
		try {
			typeMap = administratorService.getType();
		} catch (SecurityUserException e) {//获取当前登录管理员失败
			response.setStatus(1);
			response.setError(CodeMessage.SECURITY_USER_FAILED.getCode(), CodeMessage.SECURITY_USER_FAILED.getMsg());
			log.error(CodeMessage.SECURITY_USER_FAILED.getCode() + ":" + CodeMessage.SECURITY_USER_FAILED.getMsg());
			return response;
		} catch (SQLException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SELECT_FAILED.getCode(), CodeMessage.SELECT_FAILED.getMsg());
			log.error(CodeMessage.SELECT_FAILED.getCode() + ":" + CodeMessage.SELECT_FAILED.getMsg());
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(typeMap);
		return response;
	}
	
	/**
	 * 获取当前登录管理员所有信息
	 * @return
	 */
	@RequestMapping(value = "/loginAdminInfo", method = RequestMethod.GET)
	public Response getLoginAdminInfo() {
		
		Response response = new Response();
		Administrator administrator = null;
		try {
			administrator = administratorService.getLoginAdminInfo();
		}catch (SecurityUserException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SECURITY_USER_FAILED.getCode(), CodeMessage.SECURITY_USER_FAILED.getMsg());
			log.error(CodeMessage.SECURITY_USER_FAILED.getCode() + ":" + CodeMessage.SECURITY_USER_FAILED.getMsg());
			return response;
		} catch (SQLException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SELECT_FAILED.getCode(), CodeMessage.SELECT_FAILED.getMsg());
			log.error(CodeMessage.SELECT_FAILED.getCode() + ":" + CodeMessage.SELECT_FAILED.getMsg());
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(administrator);
		return response;
	}
	
	/**
	 * 重置密码
	 * @param id
	 * @return
	 */
	@RequestMapping(value = {"/resetPassword/{id}"}, method = RequestMethod.PUT)
	public Response resetPassword(@PathVariable("id") Integer id) {
		Response response = null;
		try {
			administratorService.resetPassword(id);
		} catch (LogicCheckException e) { //业务逻辑校验
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (NoSuchAlgorithmException e) { //md5加密异常
			response = new Response(1, CodeMessage.MD5_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {
			response = new Response(1, CodeMessage.UPDATE_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS, id);
	}
}






















