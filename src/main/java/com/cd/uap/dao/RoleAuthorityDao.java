package com.cd.uap.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cd.uap.bean.Authority;
import com.cd.uap.bean.RoleAuthority;

public interface RoleAuthorityDao extends JpaRepository<RoleAuthority, Integer>,JpaSpecificationExecutor<RoleAuthority> {

	/**
	 * 根据角色ID删除关联表数据
	 * @param roleId
	 */
	@Modifying
	@Query(value="DELETE FROM tbl_role_authority where ROLE_ID = ?", nativeQuery=true)
	public void deleteByRoleId(Integer roleId); 
	
	@Query("select new Authority(t2.id,t2.authorityName,t2.introduction,t2.createdAdminId,t2.createdDatetime,t2.appId,t2.remarker) from RoleAuthority as t1,Authority as t2 where t2.id = t1.authorityId AND t1.roleId = ?")
	public List<Authority> findAuthoritiesByRoleId(Integer roleId);
	
	/**
	 * 根据权限ID删除关联表数据
	 * @param authorityId
	 */
	@Modifying
	@Query(value="DELETE FROM tbl_role_authority where AUTHORITY_ID = ?", nativeQuery=true)
	public void deleteByAuthorityId(Integer authorityId);
	
	public boolean existsByRoleId(Integer roleId);
	
	public boolean existsByAuthorityId(Integer authorityId);
}
