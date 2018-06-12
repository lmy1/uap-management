package com.cd.uap.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

import com.cd.uap.bean.Role;
import com.cd.uap.bean.RoleInfo;
import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.exception.ValidateException;
import com.cd.uap.pojo.PageBean;
import com.cd.uap.pojo.Response;
import com.cd.uap.service.RoleService;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.ValidatedUtils;


@RestController
@RequestMapping("/api/uap/v1/role")
public class RoleController {
	
	@Autowired 
	private RoleService roleService;
	
	private static Logger log = LoggerFactory.getLogger(RoleController.class);
	
	/**
	 * 新增
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Response addRole(@RequestBody @Validated Role role, BindingResult bindResult) {
		
		Response response = null;
		Integer id = null;
		try {
			ValidatedUtils.validate(bindResult);
			id = roleService.addRole(role);
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
	 * @param role
	 * @return
	 */
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Response updateRole(@PathVariable Integer id,@RequestBody @Validated Role role, BindingResult bindResult) {
		
		Response response = new Response();
		try {
			ValidatedUtils.validate(bindResult);
			roleService.updateRole(id,role);
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
	public Response deleteRole(@PathVariable("ids") Integer[] ids) {
		
		Response response = new Response();
		HashMap<String, List<Integer>> resultMap = null;
		try {
			resultMap = roleService.deleteRole(ids);
		} catch (LogicCheckException e) {
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {//SQL异常
			response = new Response(1, CodeMessage.DELETE_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
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
	public Response findRoleById(@PathVariable("id") Integer id) {
		
		Response response = new Response();
		RoleInfo role = null;
		try {
			role = roleService.findRoleById(id);
		} catch (SQLException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SELECT_FAILED.getCode(), CodeMessage.SELECT_FAILED.getMsg());
			log.error(CodeMessage.SELECT_FAILED.getCode() + ":" + CodeMessage.SELECT_FAILED.getMsg());
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(role);
		return response;
	}

	
	/**
	 * 条件分页查询(只传条件则是条件查询，只传分页则是分页查询)
	 * 
	 * @param queryModels
	 * @return
	 */
	@RequestMapping(value="/conditions",method = RequestMethod.GET)
	public Response findRoleByConditions (Role role,@RequestParam(defaultValue="0")Integer page, @RequestParam(defaultValue="0")Integer size){
		
		Response response = new Response();
		PageBean<RoleInfo> pageBean = null;
		try {
			pageBean = roleService.findRoleByPageAndConditions(role,page,size);
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
	 * 根据用户id查询可用的角色信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/ableroles/{userId}",method = RequestMethod.GET)
	public Response findAvailableRolesByUserId (Long userId){
		
		Response response = new Response();
		Set<Role> ableRoles = null;
		try {
			ableRoles = roleService.findAvailableRolesByUserId(userId);
		} catch (SQLException e) {
			response.setStatus(1);
			response.setError(CodeMessage.SELECT_FAILED.getCode(), CodeMessage.SELECT_FAILED.getMsg());
			log.error(CodeMessage.SELECT_FAILED.getCode() + ":" + CodeMessage.SELECT_FAILED.getMsg());
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(ableRoles);
		return response;
	}
	
}







