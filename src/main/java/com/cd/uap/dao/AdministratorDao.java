package com.cd.uap.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.cd.uap.bean.Administrator;

public interface AdministratorDao extends JpaRepository<Administrator, Integer>,JpaSpecificationExecutor<Administrator> {
	public Administrator findByUsername(String username);
	
	public List<Administrator> findByCreatedAdminId(Integer createdAdminId);
	
	@Query(value="SELECT t2.* FROM tbl_administrator t1 , tbl_administrator t2 WHERE t1.ID = ? AND t1.CREATED_ADMIN_ID = t2.ID",nativeQuery=true)
	public Administrator findParentAdminById(Integer id);
	
	
	@Query(value="SELECT t1.ID FROM tbl_administrator t1 WHERE t1.CREATED_ADMIN_ID = ?",nativeQuery=true)
	public Set<Integer> findChildrenAdminId(Integer adminId);
}
