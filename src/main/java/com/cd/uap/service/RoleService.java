package com.cd.uap.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cd.uap.bean.AdminApp;
import com.cd.uap.bean.Administrator;
import com.cd.uap.bean.Role;
import com.cd.uap.bean.RoleInfo;
import com.cd.uap.dao.AdminAppDao;
import com.cd.uap.dao.AdministratorDao;
import com.cd.uap.dao.ApplicationDao;
import com.cd.uap.dao.RoleAuthorityDao;
import com.cd.uap.dao.RoleDao;
import com.cd.uap.dao.RoleUserDao;
import com.cd.uap.dao.UserDao;
import com.cd.uap.exception.AdministratorNotExistException;
import com.cd.uap.exception.ApplicationNotExistException;
import com.cd.uap.exception.CurrentAdminNotOperateAppException;
import com.cd.uap.exception.RoleHasExistException;
import com.cd.uap.exception.RoleNotExistException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.pojo.PageBean;
import com.cd.uap.utils.CodeMessage;


@Service
public class RoleService {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private ApplicationDao applicationDao;
	
	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdminAppDao adminAppDao;
	
	@Autowired
	private AdministratorDao administratorDao;
	
	@Autowired
	private RoleAuthorityDao roleAuthorityDao;
	
	@Autowired
	private RoleUserDao roleUserDao;
	
	private static Logger log = LoggerFactory.getLogger(RoleService.class);
	/**
	 * 新增
	 * 
	 * @param role
	 * @return
	 * @throws ApplicationNotExistException 
	 * @throws SecurityUserException 
	 * @throws RoleHasExistException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public Integer addRole(Role role) throws SQLException, ApplicationNotExistException, AdministratorNotExistException, SecurityUserException, RoleHasExistException {
		
		//校验role对应的应用是否已经存在
		Integer appId = role.getAppId();
		if (!applicationDao.exists(appId)) {
			throw new ApplicationNotExistException(CodeMessage.APP_NOT_EXIST_FAILED);
		}
		//校验角色名是否存在
		Role roleName = roleDao.findByRoleName(role.getRoleName());
		if (null != roleName) {
			throw new RoleHasExistException(CodeMessage.ROLE_HAS_EXIST_FAILED);
		}
		
		//获取当前登录者ID
		Integer adminId = administratorService.getAdminId();

		//设置创建时间及创建者
		role.setCreatedDatetime(new Date());
		role.setCreatedAdminId(adminId);
		roleDao.save(role);
		return role.getId();
	}

	/**
	 * 全字段修改
	 * 
	 * @param id
	 * @param role
	 * @return
	 * @throws ApplicationNotExistException 
	 * @throws AdministratorNotExistException 
	 * @throws SecurityUserException 
	 * @throws RoleHasExistException 
	 * @throws CurrentAdminNotOperateAppException 
	 * @throws RoleNotExistException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateRole(Integer id, Role role) throws SQLException, ApplicationNotExistException, AdministratorNotExistException, SecurityUserException, RoleHasExistException, CurrentAdminNotOperateAppException, RoleNotExistException {

		Role oldRole = roleDao.findOne(id);
		if (null == oldRole) {
			throw new RoleNotExistException(CodeMessage.ADMIN_NOTEXIST_FAILED);
		}
		//获取当前登录管理员ID
		Integer adminId = administratorService.getAdminId();
		Administrator administrator = administratorService.findAdministratorById(adminId);
		//如果当前用户不是0级管理员创建该角色的的1级管理员，驳回请求
		if (!(administrator.getType() == 0 || (oldRole.getCreatedAdminId().equals(adminId)))) {
			throw new CurrentAdminNotOperateAppException(CodeMessage.APP_CURRENT_ADMIN_NOT_OPERATE_FAILED);
		}
		
		//校验角色名是否存在，并且不是自己
		Role existRole = roleDao.findByRoleName(role.getRoleName());
		if ((existRole != null) && !(existRole.getId().equals(id))) {
			throw new RoleHasExistException(CodeMessage.ROLE_HAS_EXIST_FAILED);
		}
		
		//防止窜改初始化日期
		role.setCreatedDatetime(oldRole.getCreatedDatetime());
		role.setCreatedAdminId(oldRole.getCreatedAdminId());
		role.setId(id);
		roleDao.save(role);
		
		
	}

	/**
	 * 批量删除(真删)
	 * 
	 * @param ids
	 * @return
	 * @throws SecurityUserException 
	 * @throws SQLException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public HashMap<String, List<Integer>> deleteRole(Integer[] ids) throws SecurityUserException, SQLException {

		//获取当前登录管理员ID
		Integer adminId = administratorService.getAdminId();
		Administrator administrator = administratorService.findAdministratorById(adminId);
		
		// 创建集合，用于封装
		List<Integer> failList = new ArrayList<>();
		// 将数组中内容转入到集合中
		List<Integer> successList = new ArrayList<>();
		// 遍历要修改的数组，封装删除失败的数据ID
		for (Integer id : ids) {
			try {
				Role oldRole = roleDao.findOne(id);
				//如果当前用户不是0级管理员创建该角色的的1级管理员，驳回请求
				if (!(administrator.getType() == 0 || (oldRole.getCreatedAdminId().equals(adminId)))) {
					throw new CurrentAdminNotOperateAppException(CodeMessage.APP_CURRENT_ADMIN_NOT_OPERATE_FAILED);
				}
				roleDao.delete(id);
				//删除关联表数据
				if (roleUserDao.existsByRoleId(id)) {
					roleUserDao.deleteByRoleId(id);
				}
				if (roleAuthorityDao.existsByRoleId(id)) {
					roleAuthorityDao.deleteByRoleId(id);
				}
				successList.add(id);
			} catch (Exception e) {
				log.error(CodeMessage.DELETE_FAILED.getCode()+ ":" + CodeMessage.DELETE_FAILED.getMsg());
				failList.add(id);
			}
		}
		// 创建集合，用于封装success和fail的集合
		HashMap<String, List<Integer>> map = new HashMap<>();
		// 如果有删除失败数据
		map.put("success", successList);
		map.put("fail", failList);
		
		return map;

	}

	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	public RoleInfo findRoleById(Integer id) throws SQLException{

		Role role = roleDao.findOne(id);
		if(role == null) {
			return null;
		}
		RoleInfo roleInfo = new RoleInfo();
		//复制role对象到roleInfo对象
		BeanUtils.copyProperties(role, roleInfo);
		
		//获得创建者的adminName，赋值到新对象中
		String adminName = "";
		Integer createdAdminId = role.getCreatedAdminId();
		if(createdAdminId != null && createdAdminId.toString().length() > 0) {
			adminName = administratorDao.getOne(createdAdminId).getUsername();
		}
		roleInfo.setCreatedAdminName(adminName);
		
		//获得所属应用的appname，赋值到新对象中
		String appName = "";
		Integer appId = role.getAppId();
		if(appId != null && appId.toString().length() > 0) {
			appName = applicationDao.getOne(appId).getAppName();
		}
		roleInfo.setAppName(appName);
		
		return roleInfo;
	}

	/**
	 * 分页条件查询
	 * 
	 * @param role 条件封装对象
	 * @param page
	 * @param size
	 * @return
	 * @throws SecurityUserException 
	 */
	public PageBean<RoleInfo> findRoleByPageAndConditions(Role role, Integer page, Integer size) throws SQLException, SecurityUserException{
		
		//创建分页对象
		PageRequest pageRequest = null;
		//创建查询结果集对象
		PageBean<RoleInfo> pageBean = new PageBean<>();
		//获取当前登录管理员ID
		Integer loginAdminId = administratorService.getAdminId();
		Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);

		//创建条件查询对象
		Specification<Role> specification = new Specification<Role>() {

			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 创建一个集合，将所有创建好的条件存进集合，一次查询。
				List<Predicate> predicateList = new ArrayList<Predicate>();
				
				if (!StringUtils.isEmpty(role.getRoleName())) {
					//角色名模糊查询
					Predicate predicate = cb.like(root.get("roleName"), "%" + role.getRoleName() + "%");
					predicateList.add(predicate);
				}
				
				if (!StringUtils.isEmpty(role.getAppId())) {
					//应用id查询
					Predicate predicate = cb.equal(root.get("appId"), role.getAppId());
					predicateList.add(predicate);
				}
				
				//当前登录管理员所能看到的所有角色信息（同一家族管理员创建的角色，以及自己创建的角色；除：超级管理员看到所有信息）
				if(loginAdminInfo.getType() != 0) {
					List<Predicate> appIdsList = new ArrayList<Predicate>();
					//1、查找当前登录管理员的一级管理员
					Integer topAdminId = administratorService.findTopAdminId(loginAdminId);
					//2、根据一级管理员id查找所能操作的所有应用id
					List<AdminApp> adminAppList = adminAppDao.findByAdminId(topAdminId);
					for (AdminApp adminApp : adminAppList) {
						Integer appId = adminApp.getAppId();
						//3、根据应用id查找相应角色信息
						Predicate appIdPre = cb.equal(root.get("appId"), appId);
						appIdsList.add(appIdPre);
					}
					Predicate[] appIdsPre = new Predicate[appIdsList.size()];
					Predicate appIds = cb.or(appIdsList.toArray(appIdsPre));
					predicateList.add(appIds);
				}
				
				//将条件集合转换为数组并返回
				Predicate[] p = new Predicate[predicateList.size()];
				return cb.and(predicateList.toArray(p));
			}
		};
		
		//倒序排序
		Sort sort = new Sort(Sort.Direction.DESC,"id");
		
		if (page > 0 && size > 0) {
			//如果是分页条件查询
			pageRequest = new PageRequest(page - 1, size, sort);
			Page<Role> pageInfo = roleDao.findAll(specification, pageRequest);
			List<Role> content = pageInfo.getContent();
			//转换list集合对象，用于封装需要转换的值
			List<RoleInfo> infoList = changeList(content);
			
			pageBean.setItems(infoList);
			pageBean.setTotalNum(pageInfo.getTotalElements());
		} else {	
			//如果是条件查询
			List<Role> findAll = roleDao.findAll(specification, sort);
			//转换list集合对象，用于封装需要转换的值
			List<RoleInfo> infoList = changeList(findAll);
			
			pageBean.setItems(infoList);
			pageBean.setTotalNum((long) findAll.size());
			
		}
		
		return pageBean;

	}

	/**
	 * 根据用户id查询可用的角色信息
	 * @param userId
	 * @return
	 */
	public Set<Role> findAvailableRolesByUserId(Long userId) throws SQLException{
		
		Integer adminId = userDao.findOne(userId).getCreatedAdminId();
		//根据管理员id查询对应的上级管理员Type,如果是1 ，那就是1级管理员，返回1级管理员的ID
		
		Integer topAdminId = administratorService.findTopAdminId(adminId);
			
		//然后看该管理员的1级可以操作什么应用,
		List<AdminApp> findByAdminId = adminAppDao.findByAdminId(topAdminId);
		
		Set<Role> ableRoles = new HashSet<>();
		
		for (AdminApp adminApp : findByAdminId) {
			Integer appId = adminApp.getAppId();
			List<Role> roles = roleDao.findByAppId(appId);
			ableRoles.addAll(roles);
		}
		return ableRoles;
		
	}

	/**
	 * 转换list集合对象
	 * @param list
	 * @return
	 */
	private List<RoleInfo> changeList(List<Role> roleList) {
		
		List<RoleInfo> infoList = new ArrayList<>();
		//循环遍历
		for (Role role : roleList) {
			RoleInfo roleInfo = new RoleInfo();
			//复制role对象到roleInfo对象
			BeanUtils.copyProperties(role, roleInfo);
			
			//获得创建者的adminName，赋值到新对象中
			String adminName = "";
			Integer createdAdminId = role.getCreatedAdminId();
			if(createdAdminId != null && createdAdminId.toString().length() > 0) {
				adminName = administratorDao.getOne(createdAdminId).getUsername();
			}
			roleInfo.setCreatedAdminName(adminName);
			
			//获得所属应用的appname，赋值到新对象中
			String appName = "";
			Integer appId = role.getAppId();
			if(appId != null && appId.toString().length() > 0) {
				appName = applicationDao.getOne(appId).getAppName();
			}
			roleInfo.setAppName(appName);
			
			infoList.add(roleInfo);
		}
		return infoList;
	}
}

