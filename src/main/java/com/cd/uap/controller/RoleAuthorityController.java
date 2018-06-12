package com.cd.uap.controller;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cd.uap.bean.Authority;
import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.pojo.Response;
import com.cd.uap.service.RoleAuthorityService;
import com.cd.uap.utils.CodeMessage;


@RestController
@RequestMapping("/api/uap/v1/roleAuthority")
public class RoleAuthorityController {
	
	@Autowired 
	private RoleAuthorityService roleAuthorityService;
	
	private static Logger log = LoggerFactory.getLogger(RoleUserController.class);
	/**
	 * 为角色关联多个权限
	 * 
	 * @param roleUser
	 * @return
	 */
	@RequestMapping(value = "/{roleId}", method = RequestMethod.POST)
	public Response addRoleAuthority(@PathVariable(name="roleId",required = true)Integer roleId, @RequestBody List<Integer> authorityIds) {
		
		Response response = null;
		
		try {
			roleAuthorityService.addRoleAuthority(roleId,authorityIds);
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
	 * 全字段修改
	 * 
	 * @param id
	 * @param roleUser
	 * @return
	 */
	@RequestMapping(value = "/{roleId}", method = RequestMethod.PUT)
	public Response updateRoleUser(@PathVariable(name="roleId",required = true)Integer roleId, @RequestBody List<Integer> authorityIds) {
		
		Response response = null;
	
		try {
			roleAuthorityService.updateRoleAuthority(roleId,authorityIds);
		} catch (LogicCheckException e) {//业务逻辑校验
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {//SQL异常
			response = new Response(1, CodeMessage.UPDATE_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS);
	}
	
	/**
	 * 根据角色查询已经关联的权限
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/{roleId}", method = RequestMethod.GET)
	public Response updateRoleUser(@PathVariable(name="roleId",required = true)Integer roleId) {
		
		Response response = null;
		List<Authority> authorities = null;
		try {
			authorities = roleAuthorityService.findAuthoritiesByRoleId(roleId);
		} catch (SQLException e) {
			response = new Response(1, CodeMessage.SELECT_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS,authorities);
	}
}







