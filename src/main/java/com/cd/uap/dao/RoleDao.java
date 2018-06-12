package com.cd.uap.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cd.uap.bean.Role;

public interface RoleDao extends JpaRepository<Role, Integer>,JpaSpecificationExecutor<Role> {
	
	public List<Role> findByAppId(Integer appId);
	
	public Role findByRoleName(String roleName);
	
	public List<Role> findByCreatedAdminId(Integer adminId);
}
