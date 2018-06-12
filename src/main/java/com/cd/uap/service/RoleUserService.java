package com.cd.uap.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cd.uap.bean.Administrator;
import com.cd.uap.bean.Application;
import com.cd.uap.bean.Role;
import com.cd.uap.bean.RoleUser;
import com.cd.uap.bean.User;
import com.cd.uap.dao.AdministratorDao;
import com.cd.uap.dao.ApplicationDao;
import com.cd.uap.dao.RoleDao;
import com.cd.uap.dao.RoleUserDao;
import com.cd.uap.dao.UserDao;
import com.cd.uap.exception.RoleNotExistException;
import com.cd.uap.exception.UserNotAddRoleException;
import com.cd.uap.exception.UserNotExistException;
import com.cd.uap.utils.CodeMessage;


@Service
public class RoleUserService {

	@Autowired
	private RoleUserDao roleUserDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ApplicationDao applicationDao;
	
	@Autowired
	private AdministratorDao administratorDao;
	
	/**
	 * 为用户赋予角色
	 * 
	 * @param roleUser
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addRoleUser(Long userId, List<Integer> roleIds) throws SQLException, UserNotExistException, RoleNotExistException, UserNotAddRoleException {
		
		//1.用户必须存在
		if ((!userDao.exists(userId)) || userId == null) {
			throw new UserNotExistException(CodeMessage.USER_NOT_EXIST_FAILED);
		}
		//2.角色必须存在
		//用于封装该用户要添加的角色
		Set<Role> roles = new HashSet<>();
		for (Integer roleId : roleIds) {
			if (!roleDao.exists(roleId)) {
				throw new RoleNotExistException(CodeMessage.ROLE_NOT_EXIST_FAILED);
			} else {
				Role role = roleDao.findOne(roleId);
				roles.add(role);
			}
		}
		
		//3.该角色必须是该用户可用的
		//先查询出来用户的创建管理员ID，
		User user = userDao.findOne(userId);
		Administrator createdAdmin = administratorDao.findOne(user.getCreatedAdminId());
		//可用的角色
		Set<Role> ableRoles = new HashSet<>();
		if ((createdAdmin != null) &&  (createdAdmin.getType() == 0)) {
			//说明是超级管理员创建的用户   他可以分配所有角色
			List<Role> findAll = roleDao.findAll();
			ableRoles.addAll(findAll);
		}else {
			ableRoles = roleService.findAvailableRolesByUserId(userId);
		}
		//若全部包含，则可以添加
		if (ableRoles.containsAll(roles)) {
			for (Role role : roles) {
				RoleUser roleUser = new RoleUser(role.getId(), userId);
				roleUserDao.save(roleUser);
			}
		} else {
			//否则，用户没有添加该角色的权限
			throw new UserNotAddRoleException(CodeMessage.USER_NOT_ADD_ROLE_FAILED);
		}
	}

	/**
	 * 全字段修改
	 * 
	 * @param id
	 * @param roleUser
	 * @return
	 * @throws UserNotAddRoleException 
	 * @throws RoleNotExistException 
	 * @throws UserNotExistException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateRoleUser(Long userId, List<Integer> roleIds) throws SQLException, UserNotExistException, RoleNotExistException, UserNotAddRoleException {
		
		//要更新，先删除
		roleUserDao.deleteByUserId(userId);
		//再更新 
		addRoleUser(userId,roleIds);
	}

	/**
	 * 查询用户已经关联的角色
	 * @param id
	 * @return
	 */
	public List<Role> findRolesByUserIdAndAppId(Long userId, Integer appId) throws SQLException{

		List<Role> roles = roleUserDao.findRolesByUserIdAndAppId(userId, appId);
		return roles;
	}
	
	/**
	 * 查询用户已经关联的角色
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> findRolesByUserId(Long userId) throws SQLException{
		
		List<Role> roles = roleUserDao.findRolesByUserId(userId);
		Set<Integer> appSet = new TreeSet<>();
		for (Role role : roles) {
			appSet.add(role.getAppId());
		}
		List<Map<String, Object>> appArray = new LinkedList<>();
		for (Integer appId : appSet) {
			List<Role> existRoles = roleUserDao.findRolesByUserIdAndAppId(userId, appId);
			HashMap<String, Object> hashMap = new HashMap<>();
			Application application = applicationDao.findOne(appId);
			hashMap.put("appName", application.getAppName());
			hashMap.put("roles", existRoles);
			appArray.add(hashMap);
		}
		return appArray;
		

		
		
		
		
		
/*		
		

		List<Role> roles = roleUserDao.findRolesByUserId(userId);
		List<Map<String,Object>> appList = new LinkedList<>();

		Map<Application,List<Role>> map = new TreeMap<>();
		if (null != roles) {
			for (Role role : roles) {
				Integer appId = role.getAppId();
				Application application = applicationDao.findOne(appId);
				
				if (!map.containsKey(application)) {
					map.put(application, new ArrayList<>());
				}
				map.get(application).add(role);
				
			}
		}
		
		Set<Entry<Application, List<Role>>> entrySet = map.entrySet();
		for (Entry<Application, List<Role>> entry : entrySet) {
			Application key = entry.getKey();
			List<Role> value = entry.getValue();
			Map<String, Object> appMap = new TreeMap<>();
			
			appMap.put("id", key.getId());
			appMap.put("appName", key.getAppName());
			appMap.put("roles", value);
			appList.add(appMap);
		}
		
		return appList;*/
	}

}

