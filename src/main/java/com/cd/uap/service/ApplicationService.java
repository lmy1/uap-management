package com.cd.uap.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cd.uap.bean.Administrator;
import com.cd.uap.bean.Application;
import com.cd.uap.bean.ApplicationInfo;
import com.cd.uap.dao.AdminAppDao;
import com.cd.uap.dao.AdministratorDao;
import com.cd.uap.dao.ApplicationDao;
import com.cd.uap.exception.AdminNotExistException;
import com.cd.uap.exception.ApplicationHasExistException;
import com.cd.uap.exception.ApplicationNotExistException;
import com.cd.uap.exception.CurrentAdminNotOperateAppException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.pojo.PageBean;
import com.cd.uap.utils.CodeMessage;


@Service
public class ApplicationService {

	@Autowired
	private ApplicationDao applicationDao;
	
	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private AdminAppService adminAppService;
	
	@Autowired
	private AdminAppDao adminAppDao;
	
	@Autowired
	private AdministratorDao administratorDao;
	
	/**
	 * 管理员新增应用
	 * 
	 * 1 获取当前登录管理员ID
	 * 2 校验应用名是否重复 
	 * 3 获取前端传来的信息 set创建日期 set创建者管理员管理员
	 * 4 向数据库保存 向管理员应用中间表保存数据 
	 * 
	 * @param application
	 * @return
	 * @throws ApplicationHasExistException 
	 * @throws AdminNotExistException 
	 * @throws SecurityUserException 
	 * @throws CurrentAdminNotOperateAppException 
	 * @throws ApplicationNotExistException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public Integer addApplication(Application application) throws SQLException, ApplicationHasExistException, AdminNotExistException, SecurityUserException, CurrentAdminNotOperateAppException, ApplicationNotExistException {

		//如果应用名已经存在
		if (null != applicationDao.findByAppName(application.getAppName())) {
			throw new ApplicationHasExistException(CodeMessage.APP_HAS_EXIST_FAILED);
		}
		//获取当前登录管理员ID
		Integer adminId = administratorService.getAdminId();
		Administrator administrator = administratorService.findAdministratorById(adminId);
		
		//如果当前用户不是0/1级管理员，驳回请求
		Integer type = administrator.getType();
		if (!(type == 0 || type == 1)) {
			throw new CurrentAdminNotOperateAppException(CodeMessage.APP_CURRENT_ADMIN_NOT_OPERATE_FAILED);
		}
		application.setCreatedAdminId(adminId);
		application.setCreatedDatetime(new Date());
		applicationDao.save(application);
		
		//获取创建管理员ID
		Integer topAdminId = administratorService.findTopAdminId(adminId);
		//向关联表存储数据
		Integer appId = application.getId();
		adminAppService.addAdminApp(topAdminId, appId);
		return appId;
	}

	/**
	 * 全字段修改
	 * 
	 * @param id
	 * @param application
	 * @return
	 * @throws ApplicationHasExistException 
	 * @throws AdminNotExistException 
	 * @throws SecurityUserException 
	 * @throws CurrentAdminNotOperateAppException 
	 * @throws ApplicationNotExistException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateApplication(Application application) throws SQLException, ApplicationHasExistException, AdminNotExistException, SecurityUserException, CurrentAdminNotOperateAppException, ApplicationNotExistException {

		//获取修改前信息
		Application oldApplication = applicationDao.findOne(application.getId());
		if (null == oldApplication) {
			throw new ApplicationNotExistException(CodeMessage.APP_NOT_EXIST_FAILED);
		}
		
		//获取当前登录管理员ID
		Integer adminId = administratorService.getAdminId();
		Administrator administrator = administratorService.findAdministratorById(adminId);
		//如果当前用户不是0级管理员创建该应用的的1级管理员，驳回请求
		if (!(administrator.getType() == 0 || (oldApplication.getCreatedAdminId().equals(adminId)))) {
			throw new CurrentAdminNotOperateAppException(CodeMessage.APP_CURRENT_ADMIN_NOT_OPERATE_FAILED);
		}
		
		//如果应用名已经存在，并且不是自己
		Application existApp = applicationDao.findByAppName(application.getAppName());
		if ((null != existApp) && !(existApp.getId().equals(application.getId()))) {
			throw new ApplicationHasExistException(CodeMessage.APP_HAS_EXIST_FAILED);
		}
		//防止修改初始信息
		application.setCreatedAdminId(oldApplication.getCreatedAdminId());
		application.setCreatedDatetime(oldApplication.getCreatedDatetime());
		
		applicationDao.save(application);
	}

	/**
	 * 删除(真删)
	 * 
	 * @param ids
	 * @return
	 * @throws ApplicationNotExistException 
	 * @throws SecurityUserException 
	 * @throws CurrentAdminNotOperateAppException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void deleteApplication(Integer id) throws SQLException, ApplicationNotExistException, SecurityUserException, CurrentAdminNotOperateAppException {
		//获取修改前信息
		Application oldApplication = applicationDao.findOne(id);
		if (null == oldApplication) {
			throw new ApplicationNotExistException(CodeMessage.APP_NOT_EXIST_FAILED);
		}
		//获取当前登录管理员ID
		Integer adminId = administratorService.getAdminId();
		Administrator administrator = administratorService.findAdministratorById(adminId);
		//如果当前用户不是0级管理员创建该应用的的1级管理员，驳回请求
		if (!(administrator.getType() == 0 || (oldApplication.getCreatedAdminId().equals(adminId)))) {
			throw new CurrentAdminNotOperateAppException(CodeMessage.APP_CURRENT_ADMIN_NOT_OPERATE_FAILED);
		}
		applicationDao.delete(id);
		adminAppDao.deleteByAppId(id);
	}

	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	public ApplicationInfo findApplicationById(Integer id) throws SQLException {

		Application application = applicationDao.findOne(id);
		if(application == null) {
			return null;
		}
		ApplicationInfo applicationInfo = new ApplicationInfo();
		//复制application对象到applicationInfo对象
		BeanUtils.copyProperties(application, applicationInfo);
		//获得创建者的adminName，赋值到新对象中
		String adminName = "";
		Integer createdAdminId = application.getCreatedAdminId();
		if(createdAdminId != null && createdAdminId.toString().length() > 0) {
			adminName = administratorDao.getOne(createdAdminId).getUsername();
		}
		applicationInfo.setCreatedAdminName(adminName);
		return applicationInfo;
	}

	/**
	 * 分页条件查询
	 * 
	 * @param application
	 *            条件封装对象
	 * @param page
	 * @param size
	 * @return
	 * @throws SecurityUserException 
	 */
	public PageBean<ApplicationInfo> findApplicationByPageAndConditions(Application application, Integer page, Integer size) throws SQLException, SecurityUserException {
		
		//创建分页对象
		PageRequest pageRequest = null;
		//创建查询结果集对象
		PageBean<ApplicationInfo> pageBean = new PageBean<>();
		//获取当前登录管理员ID
		Integer adminId = administratorService.getAdminId();
		Administrator loginAdminInfo = administratorDao.findOne(adminId);
		//创建条件查询对象
		Specification<Application> specification = new Specification<Application>() {

			@Override
			public Predicate toPredicate(Root<Application> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 创建一个集合，将所有创建好的条件存进集合，一次查询。
				List<Predicate> predicateList = new ArrayList<Predicate>();
				
				if (!StringUtils.isEmpty(application.getAppName())) {
					//应用名模糊查询
					Predicate predicate = cb.like(root.get("appName"), "%" + application.getAppName() + "%");
					predicateList.add(predicate);
				}
				
				//当前登录管理员所能看到的所有应用信息（即当前登录管理员的一级管理员所创建的应用）
				if(loginAdminInfo.getType() != 0) {
					//1、查找当前登录管理员的一级管理员
					Integer topAdminId = administratorService.findTopAdminId(adminId);
					Predicate createdAdminId = cb.equal(root.get("createdAdminId"), topAdminId);
					predicateList.add(createdAdminId);
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
			Page<Application> pageInfo = applicationDao.findAll(specification, pageRequest);
			List<Application> content = pageInfo.getContent();
			//转换list集合对象，用于封装需要转换的值
			List<ApplicationInfo> infoList = changeList(content);
			
			pageBean.setItems(infoList);
			pageBean.setTotalNum(pageInfo.getTotalElements());
		} else {
			//如果是条件查询
			List<Application> findAll = applicationDao.findAll(specification, sort);
			//转换list集合对象，用于封装需要转换的值
			List<ApplicationInfo> infoList = changeList(findAll);
			
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
	private List<ApplicationInfo> changeList(List<Application> appList) {
		
		List<ApplicationInfo> infoList = new ArrayList<>();
		//循环遍历
		for (Application app : appList) {
			ApplicationInfo appInfo = new ApplicationInfo();
			//复制application对象到applicationInfo对象
			BeanUtils.copyProperties(app, appInfo);
			//获得创建者的adminName，赋值到新对象中
			String adminName = "";
			Integer createdAdminId = app.getCreatedAdminId();
			if(createdAdminId != null && createdAdminId.toString().length() > 0) {
				adminName = administratorDao.getOne(createdAdminId).getUsername();
			}
			appInfo.setCreatedAdminName(adminName);
			infoList.add(appInfo);
		}
		return infoList;
	}

}

