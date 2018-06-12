package com.cd.uap.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cd.uap.bean.Authority;

public interface AuthorityDao extends JpaRepository<Authority, Integer>,JpaSpecificationExecutor<Authority> {
	
	public List<Authority> findByAppId(Integer appId);
	
	public Authority findByAuthorityName(String authorityName);
	
	public List<Authority> findByCreatedAdminId(Integer adminId);
}
