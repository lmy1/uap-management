package com.cd.uap.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.pojo.Response;
import com.cd.uap.service.RoleUserService;
import com.cd.uap.utils.CodeMessage;


@RestController
@RequestMapping("/api/uap/v1/roleUser")
public class RoleUserController {
	
	@Autowired 
	private RoleUserService roleUserService;
	
	private static Logger log = LoggerFactory.getLogger(RoleUserController.class);
	
	/**
	 * 新增
	 * 
	 * @param roleUser
	 * @return
	 */
	@RequestMapping(value = "/{userId}", method = RequestMethod.POST)
	public Response addRoleUser(@PathVariable(name="userId",required = true)Long userId, @RequestBody List<Integer> roleIds) {
		
		Response response = null;
		
		try {
			roleUserService.addRoleUser(userId,roleIds);
		} catch (LogicCheckException e) {//业务逻辑校验
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
	 * 根据id全字段修改
	 * 
	 * @param id
	 * @param roleUser
	 * @return
	 */
	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	public Response updateRoleUser(@PathVariable(name="userId",required = true)Long userId, @RequestBody List<Integer> roleIds) {
		
		Response response = null;
		try {
			roleUserService.updateRoleUser(userId,roleIds);
		} catch (LogicCheckException e) {//业务逻辑校验
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
	 * 根据用户id查询已经关联的角色
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public Response getRolesByUserId(@PathVariable(name="userId",required = true)Long userId) {
		
		Response response = null;
		List<Map<String,Object>> appList = null;
		try {
			appList = roleUserService.findRolesByUserId(userId);
		} catch (SQLException e) {
			response = new Response(1, CodeMessage.SELECT_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS,appList);
	}
}







