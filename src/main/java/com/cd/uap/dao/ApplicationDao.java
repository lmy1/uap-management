package com.cd.uap.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cd.uap.bean.Application;

public interface ApplicationDao extends JpaRepository<Application, Integer>,JpaSpecificationExecutor<Application> {
	
	public Application findByAppName(String appName);
	
	public List<Application> findByCreatedAdminId(Integer adminId);
	
}
