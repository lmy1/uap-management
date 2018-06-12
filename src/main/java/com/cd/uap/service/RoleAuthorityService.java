package com.cd.uap.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cd.uap.bean.Authority;
import com.cd.uap.bean.Role;
import com.cd.uap.bean.RoleAuthority;
import com.cd.uap.dao.AuthorityDao;
import com.cd.uap.dao.RoleAuthorityDao;
import com.cd.uap.dao.RoleDao;
import com.cd.uap.exception.RoleNotAddAuthorityException;
import com.cd.uap.exception.RoleNotExistException;
import com.cd.uap.utils.CodeMessage;


@Service
public class RoleAuthorityService {

	@Autowired
	private RoleAuthorityDao roleAuthorityDao;
	
	@Autowired
	private AuthorityDao authorityDao;
	
	@Autowired
	private RoleDao roleDao;
	
	/**
	 * 角色关联权限
	 * 
	 * @param roleAuthority
	 * @return
	 * @throws RoleNotExistException 
	 * @throws RoleNotAddAuthorityException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addRoleAuthority(Integer roleId, List<Integer> authorityIds) throws SQLException, RoleNotExistException, RoleNotAddAuthorityException {
		
		Role role = roleDao.findOne(roleId);
		//判断传入的角色id是否存在
		if (null == role) {
			throw new RoleNotExistException(CodeMessage.ROLE_NOT_EXIST_FAILED);
		}
		Integer appId = role.getAppId();
		//查询当前可添加的所有权限
		List<Authority> ableAuthorities = authorityDao.findByAppId(appId);
		
		//要添加的所有对象
		List<Authority> authorities = authorityDao.findAll(authorityIds);
		
		if (ableAuthorities.containsAll(authorities)) {
			for (Authority authority : authorities) {
				RoleAuthority roleAuthority = new RoleAuthority(roleId, authority.getId());
				roleAuthorityDao.save(roleAuthority);
			}
			
		} else {
			//否则，角色没有添加该权限的权利
			throw new RoleNotAddAuthorityException(CodeMessage.ROLE_NOT_ADD_AUTHORITY_FAILED);
		}
		
	}

	/**
	 * 全字段修改
	 * 
	 * @param id
	 * @param roleAuthority
	 * @return
	 * @throws RoleNotAddAuthorityException 
	 * @throws RoleNotExistException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateRoleAuthority(Integer roleId, List<Integer> authorityIds) throws SQLException, RoleNotExistException, RoleNotAddAuthorityException {

		//要更新，先删除
		roleAuthorityDao.deleteByRoleId(roleId);
		//再更新 
		addRoleAuthority(roleId,authorityIds);
	}

	/**
	 * 根据角色ID查找已经关联的权限
	 * 
	 * @param id
	 * @return
	 */
	public List<Authority> findAuthoritiesByRoleId(Integer roleId) throws SQLException{

		List<Authority> authorities = roleAuthorityDao.findAuthoritiesByRoleId(roleId);
		return authorities;
	}

	
}

