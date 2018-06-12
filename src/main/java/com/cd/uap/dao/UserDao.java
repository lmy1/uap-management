package com.cd.uap.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cd.uap.bean.User;

public interface UserDao extends JpaRepository<User, Long>,JpaSpecificationExecutor<User> {

	List<User> findByUsername(String username);
	List<User> findByPhoneNumber(String phoneNumber);
	
	public List<User> findByCreatedAdminId(Integer adminId);

}
