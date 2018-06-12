package com.cd.uap.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cd.uap.bean.Role;
import com.cd.uap.bean.RoleUser;

public interface RoleUserDao extends JpaRepository<RoleUser, Integer>,JpaSpecificationExecutor<RoleUser> {
	
	/**
	 * 根据用户ID删除其关联的角色
	 * @param userId
	 */
	@Modifying
	@Query(value="DELETE FROM tbl_role_user where USER_ID = ?",nativeQuery=true)
	public void deleteByUserId(Long userId);
	
	/**
	 * 根据用户id和应用id查询关联角色
	 * @param Id
	 * @return
	 */
	@Query("select new Role(t2.id,t2.roleName,t2.introduction,t2.createdAdminId,t2.createdDatetime,t2.appId,t2.remarker) from RoleUser as t1, Role as t2 where t1.roleId = t2.id and t1.userId = ? and t2.appId = ? order by t2.id")
	public List<Role> findRolesByUserIdAndAppId(Long Id, Integer appId);
	
	/**
	 * 根据角色ID删除关联表数据
	 * @param roleId
	 */
	@Modifying
	@Query(value="DELETE FROM tbl_role_user where ROLE_ID = ?",nativeQuery=true)
	public void deleteByRoleId(Integer roleId);
	
	public boolean existsByRoleId(Integer roleId);
	
	/**
	 * 根据用户id查询关联角色
	 * @param Id
	 * @return
	 */
	@Query("select new Role(t2.id,t2.roleName,t2.introduction,t2.createdAdminId,t2.createdDatetime,t2.appId,t2.remarker) from RoleUser as t1, Role as t2 where t1.roleId = t2.id and t1.userId = ? order by t2.id")
	public List<Role> findRolesByUserId(Long userId);
	
	public boolean existsByRoleIdAndUserId(Integer roleId, Long userId);
}







