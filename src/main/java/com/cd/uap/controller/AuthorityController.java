package com.cd.uap.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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

import com.cd.uap.bean.Authority;
import com.cd.uap.bean.AuthorityInfo;
import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.exception.ValidateException;
import com.cd.uap.pojo.PageBean;
import com.cd.uap.pojo.Response;
import com.cd.uap.service.AuthorityService;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.ValidatedUtils;


@RestController
@RequestMapping("/api/uap/v1/authority")
public class AuthorityController {
	
	private static Logger log = LoggerFactory.getLogger(AuthorityController.class);
	
	@Autowired 
	private AuthorityService authorityService;
	
	/**
	 * 新增
	 * 
	 * @param authority
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Response addAuthority(@RequestBody @Validated Authority authority, BindingResult bindingResult) {
		
		Response response = null;
		Integer id = null;
		try {
			ValidatedUtils.validate(bindingResult);//实体类校验
			id = authorityService.addAuthority(authority);
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
	 * @param authority
	 * @return
	 */
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Response updateAuthority(@PathVariable Integer id,@RequestBody @Validated Authority authority, BindingResult bindingResult) {
		
		Response response = null;
		try {
			ValidatedUtils.validate(bindingResult);//实体类校验
			authorityService.updateAuthority(id,authority);
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
	 * 批量删除(真删)
	 * 
	 * @param ids	要删除的ID数组
	 * @return
	 */
	@RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
	public Response deleteAuthority(@PathVariable("ids") Integer[] ids) {
		
		Response response = new Response();
		HashMap<String, List<Integer>> resultMap = authorityService.deleteAuthority(ids);
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
	public Response findAuthorityById(@PathVariable("id") Integer id) {
		
		Response response = new Response();
		AuthorityInfo authority = null;
		try {
			authority = authorityService.findAuthorityById(id);
		} catch (SQLException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SELECT_FAILED.getCode(), CodeMessage.SELECT_FAILED.getMsg());
			log.error(CodeMessage.SELECT_FAILED.getCode() + ":" + CodeMessage.SELECT_FAILED.getMsg());
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(authority);
		return response;
	}

	
	/**
	 * 条件分页查询(只传条件则是条件查询，只传分页则是分页查询)
	 * 
	 * @param queryModels
	 * @return
	 */
	@RequestMapping(value="/conditions",method = RequestMethod.GET)
	public Response findAuthorityByConditions (Authority authority,@RequestParam(defaultValue="0")Integer page, @RequestParam(defaultValue="0")Integer size){
		
		Response response = new Response();
		PageBean<AuthorityInfo> pageBean = null;
		try {
			pageBean = authorityService.findAuthorityByPageAndConditions(authority,page,size);
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
	
}







