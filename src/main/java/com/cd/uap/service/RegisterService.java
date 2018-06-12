package com.cd.uap.service;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd.uap.bean.Administrator;
import com.cd.uap.bean.Role;
import com.cd.uap.bean.RoleUser;
import com.cd.uap.bean.User;
import com.cd.uap.dao.AdministratorDao;
import com.cd.uap.dao.RoleDao;
import com.cd.uap.dao.RoleUserDao;
import com.cd.uap.dao.UserDao;
import com.cd.uap.exception.PasswordPhoneException;
import com.cd.uap.exception.RegistAdminException;
import com.cd.uap.exception.RegistPasswordException;
import com.cd.uap.exception.RegistPhoneException;
import com.cd.uap.exception.RegistRoleUserException;
import com.cd.uap.exception.ValidateCodeException;
import com.cd.uap.pojo.ValidateModel;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.MD5Utils;

@Service
public class RegisterService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private RoleUserDao roleUserDao;
	
	@Autowired
	private AdministratorDao administratorDao;
	
	@Autowired 
	private SmsService smsService;
	
	/**
	 * 注册时，验证手机号规则（1、手机号格式；2、手机号没有注册）
	 * @param validateModel
	 * @return
	 */
	public boolean checkRegistPhoneNum(ValidateModel validateModel) {
		String phoneNum = (validateModel.getPhoneNum()==null?"":validateModel.getPhoneNum());
		//1、手机号格式验证
		String regex = "^(1[345789]{1})\\d{9}$";
		boolean isPhoneNumRule = phoneNum.matches(regex);
		//2、手机号没有注册验证
		List<User> userList = userDao.findByPhoneNumber(phoneNum);
		
		if(isPhoneNumRule && userList.size() <= 0) {
			isPhoneNumRule = true;
		}else {
			isPhoneNumRule = false;
		}
		
		return isPhoneNumRule;
	}
	
	/**
	 * 注册用户
	 * @param validateModel
	 * @throws RegistPhoneException 
	 * @throws RegistPasswordException 
	 * @throws ValidateCodeException 
	 * @throws NoSuchAlgorithmException 
	 * @throws SQLException 
	 * @throws RegistRoleUserException 
	 * @throws RegistAdminException 
	 */
	@Transactional(rollbackOn=Exception.class)
	public void regist(ValidateModel validateModel) throws RegistPhoneException, RegistPasswordException, ValidateCodeException, NoSuchAlgorithmException, SQLException, RegistRoleUserException, RegistAdminException {
		//1、校验手机号规则
		boolean isPhoneNumRule = checkRegistPhoneNum(validateModel);
		if(!isPhoneNumRule) {
			throw new RegistPhoneException(CodeMessage.REGIST_PHONE_ERROR);
		}
		
		//2、校验密码规则
		String password = (validateModel.getPassword()==null?"":validateModel.getPassword());
		boolean isPasswordRule = checkPasswordRule(password);
		if(!isPasswordRule) {
			throw new RegistPasswordException(CodeMessage.REGIST_PASSWORD_ERROR);
		}
		
		//3、校验短信验证码
		Boolean isValidateCode = smsService.checkValidateCode(validateModel.getValidateCode(), validateModel.getPhoneNum());
		if(!isValidateCode) {
			throw new ValidateCodeException(CodeMessage.VALIDATE_CODE_ERROR);
		}
		
		//注册用户，保存数据
		User user = new User();
		user.setUsername("uap" + validateModel.getPhoneNum());
		user.setPassword(MD5Utils.md5Digest(validateModel.getPassword()));
		user.setPhoneNumber(validateModel.getPhoneNum());
		user.setStatus(1);
		//注册用户创建者id分配
		Administrator administrator = administratorDao.findByUsername("registAdmin");
		if(administrator == null) {
			throw new RegistAdminException(CodeMessage.REGIST_ADMIN_ERROR);
		}
		user.setCreatedAdminId(administrator.getId());
		
		user.setCreatedDatetime(new Date());
		userDao.save(user);
		
		//第一次分配用户角色
		updateRoleUser(validateModel);
	}
	
	/**
	 * 第一次分配用户角色
	 * @param validateModel
	 * @throws SQLException 
	 * @throws RegistRoleUserException 
	 */
	public void updateRoleUser(ValidateModel validateModel) throws SQLException, RegistRoleUserException {
		List<User> userList = userDao.findByPhoneNumber(validateModel.getPhoneNum());
		Role role = roleDao.findByRoleName(validateModel.getRoleName());
		
		if(userList == null || userList.size() <= 0 || role == null) {
			throw new RegistRoleUserException(CodeMessage.REGIST_ROLEUSER_ERROR);
		}
		
		User user = userList.get(0);
		boolean isRoleUser = roleUserDao.existsByRoleIdAndUserId(role.getId(), user.getId());
		if(isRoleUser) {
			throw new RegistRoleUserException(CodeMessage.REGIST_ROLEUSER_ERROR);
		}
		
		RoleUser roleUser = new RoleUser();
		roleUser.setUserId(user.getId());
		roleUser.setRoleId(role.getId());
		roleUserDao.save(roleUser);
	}
	
	/**
	 * 验证密码规则
	 * @param password
	 * @return
	 */
	public boolean checkPasswordRule(String password) {
		String regex = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\\\\\\\\\\\\\[\\\\\\\\\\\\\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？])+$";
		boolean isPasswordRule = password.matches(regex);
		if(password.length() >= 8 && password.length() <= 32 && isPasswordRule) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 找回密码时，验证手机号规则（1、手机号格式；2、手机号已注册）
	 * @param validateModel
	 * @return
	 */
	public boolean checkBackPhoneNum(ValidateModel validateModel) {
		String phoneNum = (validateModel.getPhoneNum()==null?"":validateModel.getPhoneNum());
		//1、手机号格式验证
		String regex = "^(1[345789]{1})\\d{9}$";
		boolean isPhoneNumRule = phoneNum.matches(regex);
		//2、手机号已注册验证
		List<User> userList = userDao.findByPhoneNumber(phoneNum);
		
		if(isPhoneNumRule && userList.size() > 0) {
			isPhoneNumRule = true;
		}else {
			isPhoneNumRule = false;
		}
		
		return isPhoneNumRule;
	}
	
	/**
	 * 通过手机号，修改密码
	 * @param validateModel
	 * @throws RegistPasswordException 
	 * @throws NoSuchAlgorithmException 
	 * @throws SQLException 
	 * @throws PasswordPhoneException 
	 * @throws ValidateCodeException 
	 */
	public void updatePassword(ValidateModel validateModel) throws RegistPasswordException, NoSuchAlgorithmException, SQLException, PasswordPhoneException, ValidateCodeException {
		//校验短信验证码
		Boolean isValidateCode = smsService.checkValidateCode(validateModel.getValidateCode(), validateModel.getPhoneNum());
		if(!isValidateCode) {
			throw new ValidateCodeException(CodeMessage.VALIDATE_CODE_ERROR);
		}
		
		//校验密码规则
		String password = (validateModel.getPassword()==null?"":validateModel.getPassword());
		boolean isPasswordRule = checkPasswordRule(password);
		if(!isPasswordRule) {
			throw new RegistPasswordException(CodeMessage.REGIST_PASSWORD_ERROR);
		}
		
		//校验手机号规则
		boolean isPhoneNumRule = checkBackPhoneNum(validateModel);
		if(!isPhoneNumRule) {
			throw new PasswordPhoneException(CodeMessage.PASSWORD_PHONE_ERROR);
		}
		
		//通过手机号获得user对象
		User user = userDao.findByPhoneNumber(validateModel.getPhoneNum()).get(0);
		
		//修改密码
		user.setPassword(MD5Utils.md5Digest(password));
		userDao.save(user);
	}
	
}
