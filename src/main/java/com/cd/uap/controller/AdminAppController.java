package com.cd.uap.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.pojo.Response;
import com.cd.uap.service.AdminAppService;
import com.cd.uap.utils.CodeMessage;


@RestController
@RequestMapping("/api/uap/v1/adminApp")
public class AdminAppController {
	
	@Autowired 
	private AdminAppService adminAppService;
	
	private static Logger log = LoggerFactory.getLogger(AdminAppController.class);
	
	/**
	 * 查询当前登录管理员可用的应用（其实是对应父管理员1级或0级可用的应用）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ableapp/{userId}", method = RequestMethod.GET)
	public Response findAdminAppById(@PathVariable("userId")Long userId) {
		
		Response response = null;
		List<Map<String, Object>> appMap = null;
		try {
			appMap = adminAppService.findAbleApplications(userId);
		}catch (LogicCheckException e) {//业务逻辑校验
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {
			response = new Response(1, CodeMessage.SELECT_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} 
		return new Response(0, CodeMessage.OPERATION_SUCCESS, appMap);
	}
	
}







