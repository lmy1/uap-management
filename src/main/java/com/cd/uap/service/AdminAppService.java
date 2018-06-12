package com.cd.uap.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cd.uap.bean.AdminApp;
import com.cd.uap.bean.Administrator;
import com.cd.uap.bean.Application;
import com.cd.uap.bean.Role;
import com.cd.uap.bean.User;
import com.cd.uap.dao.AdminAppDao;
import com.cd.uap.dao.AdministratorDao;
import com.cd.uap.dao.ApplicationDao;
import com.cd.uap.dao.RoleDao;
import com.cd.uap.dao.UserDao;
import com.cd.uap.exception.AdminNotExistException;
import com.cd.uap.exception.ApplicationNotExistException;
import com.cd.uap.exception.RoleNotExistException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.utils.CodeMessage;

@Service

public class AdminAppService {

	@Autowired
	private AdminAppDao adminAppDao;

	@Autowired
	private AdministratorDao administratorDao;
	
	@Autowired
	private ApplicationDao applicationDao;
	
	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private RoleUserService roleUserService;
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * 
	 * @param adminId	当前管理员id(必须是1级或者0级管理员)
	 * @param appIds
	 * @throws SQLException
	 * @throws AdminNotExistException
	 * @throws RoleNotExistException
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addAdminApp(Integer adminId, Integer appId) throws SQLException, AdminNotExistException, ApplicationNotExistException {

		//1.该管理员必须存在
		if ((!administratorDao.exists(adminId)) || adminId == null) {
			throw new AdminNotExistException(CodeMessage.ADMIN_NOTEXIST_FAILED);
		}
		//2.应用必须存在
		if (!applicationDao.exists(appId)) {
			throw new ApplicationNotExistException(CodeMessage.APP_NOT_EXIST_FAILED);
		} else {
			AdminApp adminApp = new AdminApp(adminId, appId);
			adminAppDao.save(adminApp);
		}
		
	}

	/**
	 * 查询当前登录管理员可用的应用及其角色和已选中角色的信息
	 * @return
	 * @throws SecurityUserException 
	 */
	public List<Map<String, Object>> findAbleApplications(Long userId) throws SecurityUserException,SQLException {

		List<Application> applications = null;
		
		//1、查看用户的创建者级别
		User user = userDao.findOne(userId);
		Integer createdAdminId = user.getCreatedAdminId();
		Administrator createdAdmin = administratorDao.findOne(createdAdminId);
		
		//2、如果用户创建者是超级管理员，则显示所有角色，并可以分配
		if (createdAdmin.getType() == 0) {
			applications = applicationDao.findAll();
		} else {
			//3、如果用户创建者不是超级管理员，则只显示该创建管理员家族的角色，并分配
			Integer adminId = administratorService.getAdminId();
			Integer topAdminId = administratorService.findTopAdminId(adminId);
			Administrator admin = administratorDao.findOne(topAdminId);
			
			if (null != admin && admin.getType() == 0) {
				//查询用户的创建管理员的家族应用`
				Integer myCreatedAdminId = user.getCreatedAdminId();
				topAdminId = administratorService.findTopAdminId(myCreatedAdminId);
				
			}
			applications = adminAppDao.findApplicationsByAdminId(topAdminId);
		}
		
		List<Map<String, Object>> appArray = new LinkedList<>();		//用于封装应用及其角色和已选中角色的信息
		for (Application application : applications) {
			Map<String, Object> app = new HashMap<>();
			app.put("id", application.getId().toString());
			app.put("appName", application.getAppName());
			List<Role> roles = roleDao.findByAppId(application.getId());
			app.put("roles", roles);
			List<Role> existRoles = roleUserService.findRolesByUserIdAndAppId(userId, application.getId());
			List<Integer> existRolesIds = new ArrayList<>();
			if (null != existRoles) {
				for (Role roleInfo : existRoles) {
					existRolesIds.add(roleInfo.getId());
				}
			}
			app.put("existRoles", existRolesIds);
			appArray.add(app);
		}
		return appArray;
	}


}

