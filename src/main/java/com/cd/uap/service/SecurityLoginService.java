package com.cd.uap.service;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cd.uap.bean.Administrator;
import com.cd.uap.dao.AdministratorDao;
import com.cd.uap.exception.AdminFrozenException;
import com.cd.uap.exception.AdministratorNotExistException;
import com.cd.uap.exception.LoginPasswordErrorException;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.MD5Utils;

@Service
public class SecurityLoginService {
	@Autowired
	private AdministratorDao adminDao;

	public UserDetails loadUser(Administrator user) throws NoSuchAlgorithmException, LoginPasswordErrorException, AdminFrozenException, AdministratorNotExistException {
		Administrator uapUser = adminDao.findByUsername(user.getUsername());
		User userDetails = null;
		if(uapUser==null) {
			throw new AdministratorNotExistException(CodeMessage.ADMIN_NOTEXIST_FAILED);
		}
		if(uapUser.getStatus()==0) {
			throw new AdminFrozenException(CodeMessage.ADMIN_FROZEN_FAILED);
		}
		if(!uapUser.getPassword().equals(MD5Utils.md5Digest(user.getPassword()))) {
			throw new LoginPasswordErrorException(CodeMessage.LOGIN_PASSWORD_ERROR);
		}
		 //数据库中查询权限写死了。
        Set<GrantedAuthority> authoritySet = new HashSet<GrantedAuthority>();
        authoritySet.add(new SimpleGrantedAuthority("NORMALLY"));
        userDetails=new User(user.getUsername(),"pwd",authoritySet);
		return userDetails;
	}

}
