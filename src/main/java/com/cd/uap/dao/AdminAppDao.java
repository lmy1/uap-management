package com.cd.uap.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cd.uap.bean.AdminApp;
import com.cd.uap.bean.Application;

public interface AdminAppDao extends JpaRepository<AdminApp, Integer>,JpaSpecificationExecutor<AdminApp> {
	
	public AdminApp findByAdminIdAndAppId(Integer adminId, Integer appId);
	
	public List<AdminApp> findByAdminId(Integer adminId);
	
	@Query("SELECT new Application(t2.id, t2.appName, t2.clientId, t2.secret, t2.appUrl, t2.introduction,t2.createdAdminId, t2.createdDatetime, t2.remarker) FROM AdminApp t1, Application t2 WHERE t1.appId = t2.id AND t1.adminId = ? order by t2.id")
	public List<Application> findApplicationsByAdminId(Integer adminId);
	
	@Modifying
	@Query(value="DELETE FROM tbl_admin_app where APP_ID = ?",nativeQuery=true)
	public void deleteByAppId(Integer appId);
	
}
