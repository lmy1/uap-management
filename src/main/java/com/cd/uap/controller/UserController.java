package com.cd.uap.controller;

import java.security.NoSuchAlgorithmException;
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

import com.cd.uap.bean.User;
import com.cd.uap.bean.UserInfo;
import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.exception.ValidateException;
import com.cd.uap.pojo.FieldModel;
import com.cd.uap.pojo.PageBean;
import com.cd.uap.pojo.Response;
import com.cd.uap.service.UserService;
import com.cd.uap.utils.AESUtils;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.ValidatedUtils;

@RestController
@RequestMapping("/api/uap/v1/user")
public class UserController{
	private static Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	/**
	 * 新增
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Response addUser(@RequestBody @Validated User user, BindingResult result) {
		Response response = null;
		Long id = null;
		try {
			//对前台传过来的密码进行解密操作
			String password = user.getPassword();
			user.setPassword(AESUtils.desEncrypt(password));
			
			ValidatedUtils.validate(result);
			id = userService.addUser(user);
		} catch (NoSuchAlgorithmException e) {//MD5转换错误
			response = new Response(1, CodeMessage.MD5_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (ValidateException e) {
			response = new Response(1, CodeMessage.VALIDATE_FAILED.getCode(), e.getMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (LogicCheckException e) {
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {
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
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.PUT)
	public Response updateUser(@PathVariable Long id, @RequestBody @Validated User user, BindingResult result) {
		Response response = null;
		user.setId(id);
		try {
			// 保存时校验
			ValidatedUtils.validate(result);
			userService.updateUser(user);
		} catch (ValidateException e) {// 校验异常
			response = new Response(1, CodeMessage.VALIDATE_FAILED.getCode(), e.getMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (LogicCheckException e) {
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {
			response = new Response(1, CodeMessage.INSERT_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (NoSuchAlgorithmException e) {//MD5转换错误
			response = new Response(1, CodeMessage.MD5_FAILED);
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
	 * 重置密码
	 */
	@RequestMapping(value = { "/resetPassword/{id}" }, method = RequestMethod.PUT)
	public Response resetPassword(@PathVariable Long id) {
		Response response = null;
		try {
			userService.resetPassword(id);
		} catch (LogicCheckException e) {
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (SQLException e) {
			response = new Response(1, CodeMessage.INSERT_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (NoSuchAlgorithmException e) {//MD5转换错误
			response = new Response(1, CodeMessage.MD5_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS);
	}
	/**
	 * 批量删除(真删)
	 * 
	 * @param ids
	 *            要删除的ID数组
	 * @return
	 */
	@RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
	public Response deleteUser(@PathVariable("ids") Long[] ids) {
		Response response = new Response();
		HashMap<String, List<Long>> resultMap = userService.deleteUser(ids);
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(resultMap);
		return response;
	}
	
	/**
	 * 冻结/解冻单个用户
	 * 
	 * @param ids
	 * @param fieldModel
	 * @return
	 */
	@RequestMapping(value = "/{id}/logic", method = RequestMethod.PUT)
	public Response deleteUserByLogic(@PathVariable("id") Long id, @RequestBody FieldModel fieldModel) {
		Response response = null;
		try {
			userService.freezeUserByLogic(id,fieldModel);
		} catch (LogicCheckException e) {
			response = new Response(1, e.getCodeMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		return new Response(0, CodeMessage.OPERATION_SUCCESS);
	}

	/**
	 * 根据id查询
	 * 
	 * @param id
	 *            需要查询的ID
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Response findUserById(@PathVariable("id") Long id) {
		
		Response response = new Response();
		UserInfo user = null;
		try {
			user = userService.findUserById(id);
		} catch (Exception e) {
			response.setStatus(1);
			response.setError(CodeMessage.SELECT_FAILED.getCode(), CodeMessage.SELECT_FAILED.getMsg());
			e.printStackTrace();
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(user);
		return response;
	}

	/**
	 * 条件分页查询(只传条件则是条件查询，只传分页则是分页查询)
	 * 
	 * @param queryModels
	 * @return
	 */
	@RequestMapping(value = "/conditions", method = RequestMethod.GET)
	public Response findUserByConditions(User user, @RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "0") Integer size) {

		Response response = new Response();
		PageBean<UserInfo> pageBean = null;
		try {
			pageBean = userService.findUserByPageAndConditions(user, page, size);
		} catch (Exception e) {
			response.setStatus(1);
			response.setError(CodeMessage.SELECT_FAILED.getCode(), CodeMessage.SELECT_FAILED.getMsg());
			e.printStackTrace();
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(pageBean);
		return response;

	}
	

}
