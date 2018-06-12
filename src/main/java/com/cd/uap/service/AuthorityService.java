package com.cd.uap.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cd.uap.bean.AdminApp;
import com.cd.uap.bean.Administrator;
import com.cd.uap.bean.Application;
import com.cd.uap.bean.Authority;
import com.cd.uap.bean.AuthorityInfo;
import com.cd.uap.dao.AdminAppDao;
import com.cd.uap.dao.AdministratorDao;
import com.cd.uap.dao.ApplicationDao;
import com.cd.uap.dao.AuthorityDao;
import com.cd.uap.dao.RoleAuthorityDao;
import com.cd.uap.exception.AuthorityHasExistException;
import com.cd.uap.exception.AuthorityNoAppException;
import com.cd.uap.exception.AuthorityNoCreatAdminException;
import com.cd.uap.exception.AuthorityNoEditException;
import com.cd.uap.exception.AuthorityNoPowerException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.pojo.PageBean;
import com.cd.uap.utils.CodeMessage;


@Service
public class AuthorityService {
	
	private static Logger log = LoggerFactory.getLogger(AuthorityService.class);

	@Autowired
	private AuthorityDao authorityDao;
	@Autowired
	private AdministratorDao administratorDao;
	@Autowired
	private ApplicationDao applicationDao;
	@Autowired
	private AdminAppDao adminAppDao;
	@Autowired
	AdministratorService administratorService;
	@Autowired
	private RoleAuthorityDao roleAuthorityDao;

	/**
	 * 新增
	 * 
	 * @param authority
	 * @return
	 * @throws
	 */
	@Transactional(rollbackFor=Exception.class)
	public Integer addAuthority(Authority authority) throws SQLException,AuthorityNoCreatAdminException,AuthorityNoAppException,
		AuthorityNoPowerException,SecurityUserException,AuthorityHasExistException {

		//获取当前登录管理员ID
		Integer loginAdminId = administratorService.getAdminId();
		Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);
		//1、给新建的权限的createdAdminId赋值
		authority.setCreatedAdminId(loginAdminId);
		//2、给createdDatetime赋值，默认创建时间为系统当时时间
		authority.setCreatedDatetime(new Date());
		
		//判断权限名是否存在
		Authority authorityInfo = authorityDao.findByAuthorityName(authority.getAuthorityName());
		if(authorityInfo != null) {
			throw new AuthorityHasExistException(CodeMessage.AUTHORITY_HAS_EXIST_FAILED);
		}
		
		//判断当前创建者ID是否存在
		Administrator createdAdminInfo = administratorDao.findOne(authority.getCreatedAdminId());
		if(createdAdminInfo == null) {
			throw new AuthorityNoCreatAdminException(CodeMessage.AUTHORITY_NOCREATADMIN_FAILED);
		}
		
		//判断该权限所属的应用ID是否存在
		Application application = applicationDao.findOne(authority.getAppId());
		if(application == null) {
			throw new AuthorityNoAppException(CodeMessage.AUTHORITY_NOAPP_FAILED);
		}
		
		//判断当前创建者是否有创建该应用权限的权利
		Integer topAdminId = administratorService.findTopAdminId(authority.getCreatedAdminId());
		AdminApp adminApp = adminAppDao.findByAdminIdAndAppId(topAdminId, authority.getAppId());
		if(adminApp == null || loginAdminInfo.getType() != 0) {
			throw new AuthorityNoPowerException(CodeMessage.AUTHORITY_NOPOWER_FAILED);
		}
		
		//保存
		authorityDao.save(authority);
		return authority.getId();
	}

	/**
	 * 全字段修改
	 * 
	 * @param id
	 * @param authority
	 * @return
	 * @throws
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateAuthority(Integer id, Authority authority) throws SQLException,AuthorityNoCreatAdminException,AuthorityNoAppException,
		AuthorityNoPowerException,SecurityUserException,AuthorityNoEditException,AuthorityHasExistException {

		//获取当前登录管理员ID
		Integer loginAdminId = administratorService.getAdminId();
		Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);
		
		//判断当前登录管理员是否为超级管理员，或是否为该权限的创建者，否则没有修改该权限信息的权利
		if(loginAdminInfo.getType()!=0 && authority.getCreatedAdminId()!=null && !loginAdminId.equals(authority.getCreatedAdminId())) {
			throw new AuthorityNoEditException(CodeMessage.AUTHORITY_NOEDIT_FAILED);
		}
		
		//判断权限名是否存在
		Authority authorityInfo = authorityDao.findByAuthorityName(authority.getAuthorityName());
		if(authorityInfo != null && !id.equals(authorityInfo.getId())) {
			throw new AuthorityHasExistException(CodeMessage.AUTHORITY_HAS_EXIST_FAILED);
		}
		
		//判断当前创建者ID是否为空
		if(authority.getCreatedAdminId()==null || authority.getCreatedAdminId().toString().length()<=0) {
			throw new AuthorityNoCreatAdminException(CodeMessage.AUTHORITY_NOCREATADMIN_FAILED);
		}
		
		//判断当前创建者ID是否存在
		Administrator createdAdminInfo = administratorDao.findOne(authority.getCreatedAdminId());
		if(createdAdminInfo == null) {
			throw new AuthorityNoCreatAdminException(CodeMessage.AUTHORITY_NOCREATADMIN_FAILED);
		}
		
		//判断该权限所属的应用ID是否存在
		Application application = applicationDao.findOne(authority.getAppId());
		if(application == null) {
			throw new AuthorityNoAppException(CodeMessage.AUTHORITY_NOAPP_FAILED);
		}
		
		authority.setId(id);
		authorityDao.save(authority);
	}

	/**
	 * 批量删除(真删)
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public HashMap<String, List<Integer>> deleteAuthority(Integer[] ids) {

		// 创建集合，用于封装
		List<Integer> failList = new ArrayList<>();
		// 将数组中内容转入到集合中
		List<Integer> idList = Arrays.asList(ids);
		// 遍历要修改的数组，封装删除失败的数据ID
		for (Integer id : ids) {
			try {
				//获取当前登录管理员ID
				Integer loginAdminId = administratorService.getAdminId();
				Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);
				Authority delAuthorityInfo = authorityDao.findOne(id);
				//1、判断当前用户是否为超级管理员，或者是否为被删除者的创建者，才有删除权限
				if(loginAdminInfo.getType()==0 || (delAuthorityInfo.getCreatedAdminId()!=null && loginAdminId.equals(delAuthorityInfo.getCreatedAdminId()))) {
					authorityDao.delete(id);
					//删除关联表数据
					if (roleAuthorityDao.existsByAuthorityId(id)) {
						roleAuthorityDao.deleteByAuthorityId(id);
					}
				}else {
					failList.add(id);
				}
			} catch (Exception e) {
				log.error(CodeMessage.DELETE_FAILED.getCode()+","+CodeMessage.DELETE_FAILED.getMsg());
				failList.add(id);
			}
		}
		// 创建集合，用于封装success和fail的集合
		HashMap<String, List<Integer>> map = new HashMap<>();
		// 如果有删除失败数据
		if (failList.size() > 0) {

			map.put("fail", failList);
			ArrayList<Integer> allId = new ArrayList<>();
			allId.addAll(idList);
			// 如果部分失败
			if (allId.size() > failList.size()) {
				allId.removeAll(failList);
				// 将成功的数据封装到成功的集合里
				map.put("success", allId);
			}
		} else {
			// 如果全部删除成功
			map.put("success", idList);
		}
		return map;

	}

	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	public AuthorityInfo findAuthorityById(Integer id) throws SQLException{

		Authority authority = authorityDao.findOne(id);
		if(authority == null) {
			return null;
		}
		AuthorityInfo authorityInfo = new AuthorityInfo();
		//复制authority对象到authorityInfo对象
		BeanUtils.copyProperties(authority, authorityInfo);
		
		//获得创建者的adminName，赋值到新对象中
		String adminName = "";
		Integer createdAdminId = authority.getCreatedAdminId();
		if(createdAdminId != null && createdAdminId.toString().length() > 0) {
			adminName = administratorDao.getOne(createdAdminId).getUsername();
		}
		authorityInfo.setCreatedAdminName(adminName);
		
		//获得所属应用的appname，赋值到新对象中
		String appName = "";
		Integer appId = authority.getAppId();
		if(appId != null && appId.toString().length() > 0) {
			appName = applicationDao.getOne(appId).getAppName();
		}
		authorityInfo.setAppName(appName);
		
		return authorityInfo;
	}

	/**
	 * 分页条件查询
	 * 
	 * @param authority
	 *            条件封装对象
	 * @param page
	 * @param size
	 * @return
	 * @throws
	 */
	public PageBean<AuthorityInfo> findAuthorityByPageAndConditions(Authority authority, Integer page, Integer size) throws SQLException,SecurityUserException{
		
		//创建分页对象
		PageRequest pageRequest = null;
		//创建查询结果集对象
		PageBean<AuthorityInfo> pageBean = new PageBean<>();
		//获取当前登录管理员ID
		Integer loginAdminId = administratorService.getAdminId();
		Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);

		//创建条件查询对象
		Specification<Authority> specification = new Specification<Authority>() {

			@Override
			public Predicate toPredicate(Root<Authority> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 创建一个集合，将所有创建好的条件存进集合，一次查询。
				List<Predicate> predicateList = new ArrayList<Predicate>();
				
				//权限名称
				if(authority.getAuthorityName() != null && authority.getAuthorityName().length() > 0) {
					Predicate authorityName = cb.like(root.get("authorityName"), "%"+authority.getAuthorityName()+"%");
					predicateList.add(authorityName);
				}
				
				//该权限所属的应用
				if(authority.getAppId() != null && authority.getAppId().toString().length() > 0) {
					Predicate appId = cb.equal(root.get("appId"), authority.getAppId());
					predicateList.add(appId);
				}
				
				//当前登录管理员所能看到的所有权限信息（同一家族管理员创建的权限，以及自己创建的权限；除：超级管理员看到所有信息）
				if(loginAdminInfo.getType() != 0) {
					List<Predicate> appIdsList = new ArrayList<Predicate>();
					//1、查找当前登录管理员的一级管理员
					Integer topAdminId = administratorService.findTopAdminId(loginAdminId);
					//2、根据一级管理员id查找所能操作的所以应用id
					List<AdminApp> adminAppList = adminAppDao.findByAdminId(topAdminId);
					for (AdminApp adminApp : adminAppList) {
						Integer appId = adminApp.getAppId();
						//3、根据应用id查找相应权限信息
						Predicate appIdPre = cb.equal(root.get("appId"), appId);
						appIdsList.add(appIdPre);
					}
					Predicate[] appIdsPre = new Predicate[appIdsList.size()];
					Predicate appIds = cb.or(appIdsList.toArray(appIdsPre));
					predicateList.add(appIds);
				}
				
				//将条件集合转换为数组并返回
				Predicate[] p = new Predicate[predicateList.size()];
				return cb.and(predicateList.toArray(p));
			}
		};
		
		//倒序排序
		Sort sort = new Sort(Sort.Direction.DESC,"id");
		
		if (page > 0 && size > 0) {
			//如果是分页条件查询
			pageRequest = new PageRequest(page - 1, size, sort);
			Page<Authority> pageInfo = authorityDao.findAll(specification, pageRequest);
			List<Authority> content = pageInfo.getContent();
			//转换list集合对象，用于封装需要转换的值
			List<AuthorityInfo> infoList = changeList(content);
			
			pageBean.setItems(infoList);
			pageBean.setTotalNum(pageInfo.getTotalElements());
		} else {
			//如果是条件查询
			List<Authority> findAll = authorityDao.findAll(specification, sort);
			//转换list集合对象，用于封装需要转换的值
			List<AuthorityInfo> infoList = changeList(findAll);
			
			pageBean.setItems(infoList);
			pageBean.setTotalNum((long) findAll.size());
			
		}
		
		return pageBean;

	}
	
	/**
	 * 转换list集合对象
	 * @param list
	 * @return
	 */
	private List<AuthorityInfo> changeList(List<Authority> authorityList) {
		
		List<AuthorityInfo> infoList = new ArrayList<>();
		//循环遍历
		for (Authority authority : authorityList) {
			AuthorityInfo authorityInfo = new AuthorityInfo();
			//复制authority对象到authorityInfo对象
			BeanUtils.copyProperties(authority, authorityInfo);
			
			//获得创建者的adminName，赋值到新对象中
			String adminName = "";
			Integer createdAdminId = authority.getCreatedAdminId();
			if(createdAdminId != null && createdAdminId.toString().length() > 0) {
				adminName = administratorDao.getOne(createdAdminId).getUsername();
			}
			authorityInfo.setCreatedAdminName(adminName);
			
			//获得所属应用的appname，赋值到新对象中
			String appName = "";
			Integer appId = authority.getAppId();
			if(appId != null && appId.toString().length() > 0) {
				appName = applicationDao.getOne(appId).getAppName();
			}
			authorityInfo.setAppName(appName);
			
			infoList.add(authorityInfo);
		}
		return infoList;
	}

}

