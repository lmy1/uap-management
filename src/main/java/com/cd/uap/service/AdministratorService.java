package com.cd.uap.service;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.util.StringUtils;

import com.cd.uap.bean.Administrator;
import com.cd.uap.bean.AdministratorInfo;
import com.cd.uap.bean.Application;
import com.cd.uap.bean.Authority;
import com.cd.uap.bean.Role;
import com.cd.uap.bean.User;
import com.cd.uap.dao.AdministratorDao;
import com.cd.uap.dao.ApplicationDao;
import com.cd.uap.dao.AuthorityDao;
import com.cd.uap.dao.RoleDao;
import com.cd.uap.dao.UserDao;
import com.cd.uap.exception.AdminHasExistException;
import com.cd.uap.exception.AdminNoCreatAdminException;
import com.cd.uap.exception.AdminNoEditException;
import com.cd.uap.exception.AdminNoUpdatePasswordException;
import com.cd.uap.exception.AdminNullUpdateException;
import com.cd.uap.exception.AdminPasswordRuleException;
import com.cd.uap.exception.AdminPhoneNumberException;
import com.cd.uap.exception.AdminStatusException;
import com.cd.uap.exception.AdminTypeException;
import com.cd.uap.exception.AdminTypeLowException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.pojo.FieldModel;
import com.cd.uap.pojo.PageBean;
import com.cd.uap.utils.AESUtils;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.MD5Utils;
import com.cd.uap.utils.ObjectUtil;
import com.cd.uap.utils.SecurityUserUtils;


@Service
public class AdministratorService {

	private static Logger log = LoggerFactory.getLogger(AdministratorService.class);
	
	@Autowired
	private AdministratorDao administratorDao;
	
	@Autowired
	private ApplicationDao applicationDao;
	
	@Autowired
	private AuthorityDao authorityDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserDao userDao;

	/**
	 * 新增
	 * 
	 * @param administrator
	 * @return
	 * @throws
	 */
	@Transactional(rollbackFor=Exception.class)
	public Integer addAdministrator(Administrator administrator) throws SQLException,AdminHasExistException,AdminNoCreatAdminException,
	AdminTypeLowException,AdminTypeException,AdminPasswordRuleException,NoSuchAlgorithmException,SecurityUserException,AdminPhoneNumberException {
		
		//获取当前登录管理员ID
		Integer loginAdminId = getAdminId();
		//1、给新建的管理员的createdAdminId赋值
		administrator.setCreatedAdminId(loginAdminId);
		//2、给status赋值，默认1为可用，0为冻结
		administrator.setStatus(1);
		//3、给createdDatetime赋值，默认创建时间为系统当时时间
		administrator.setCreatedDatetime(new Date());
		
		//判断管理员用户名唯一性
		Administrator adminInfo = administratorDao.findByUsername(administrator.getUsername());
		if(adminInfo != null) {
			throw new AdminHasExistException(CodeMessage.ADMIN_HASEXIST_FAILED);
		}
		
		//判断创建者ID是否存在
		Administrator createdAdminInfo = administratorDao.findOne(administrator.getCreatedAdminId());
		if(createdAdminInfo == null) {
			throw new AdminNoCreatAdminException(CodeMessage.ADMIN_NOCREATADMIN_FAILED);
		}
		
		//创建者等级是否有创建下级用户的权限，四级用户无法创建下级
		if(createdAdminInfo.getType()!=null && createdAdminInfo.getType().toString().length()>0 && createdAdminInfo.getType()==4){
			throw new AdminTypeLowException(CodeMessage.ADMIN_TYPELOW_FAILED);
		}
		
		//判断管理员等级是否为创建者的下一级别
		if(createdAdminInfo.getType()!=null && createdAdminInfo.getType().toString().length()>0 
			&& administrator.getType()!=null && administrator.getType().toString().length()>0 
			&& (createdAdminInfo.getType()+1) != administrator.getType()){
			throw new AdminTypeException(CodeMessage.ADMIN_TYPE_FAILED);
		}
		
		//首先判断密码复杂度，再对密码进行MD5的加密
		if(administrator.getPassword() != null && administrator.getPassword().length() > 0) {
			if(checkPasswordRule(administrator.getPassword())) {
				String password = MD5Utils.md5Digest(administrator.getPassword());
				administrator.setPassword(password);
			}else {
				throw new AdminPasswordRuleException(CodeMessage.ADMIN_PASSWORDRULE_FAILED);
			}
		}
		
		//校验管理员电话
		String phoneNumber = administrator.getPhoneNumber();
		if(phoneNumber!=null && phoneNumber.length()>0 && !checkPhoneNumber(phoneNumber)) {
			throw new AdminPhoneNumberException(CodeMessage.ADMIN_PHONENUMBER_FAILED);
		}
		
		//保存
		administratorDao.save(administrator);
		return administrator.getId();
	}
	
	/**
	 * 验证密码复杂度
	 * 规则：英文+数字+特殊字符8位以上
	 * @param password
	 * @return
	 */
	public static boolean checkPasswordRule(String password) {
		String regex = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,30}";
		String regexForChinese = "[^\u4e00-\u9fa5]+";
		boolean isPasswordRule = password.matches(regex);
		if(isPasswordRule) {
			isPasswordRule = password.matches(regexForChinese);
		}
		return isPasswordRule;
	}
	
	/**
	 * 验证密码复杂度
	 * 规则：英文+数字+特殊字符16位以上
	 * @param password
	 * @return
	 */
	public static boolean checkSuperPasswordRule(String password) {
		String regex = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{16,30}";
		String regexForChinese = "[^\u4e00-\u9fa5]+";
		boolean isPasswordRule = password.matches(regex);
		if(isPasswordRule) {
			isPasswordRule = password.matches(regexForChinese);
		}
		return isPasswordRule;
	}
	
	/**
	 * 验证电话号码
	 * @param phoneNumber
	 * @return
	 */
	public static boolean checkPhoneNumber(String phoneNumber) {
		String regex = "^(1[345789]{1})\\d{9}$";
		boolean isPhoneNumber = phoneNumber.matches(regex);
		return isPhoneNumber;
	}
	
	/**
	 * 全字段修改
	 * 
	 * @param id
	 * @param administrator
	 * @return
	 * @throws
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateAdministrator(Integer id, Administrator administrator) throws SQLException,AdminHasExistException,AdminNoCreatAdminException,
	AdminTypeLowException,AdminTypeException,AdminPasswordRuleException,NoSuchAlgorithmException,SecurityUserException,AdminNoEditException,AdminPhoneNumberException,Exception {
		
		//获取当前登录管理员ID
		Integer loginAdminId = getAdminId();
		Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);
		
		//判断当前登录管理员是否为超级管理员，或是否为该管理员的创建者，以及是否为自己,否则没有修改该管理员信息的权利
		if(loginAdminInfo.getType()!=0 && administrator.getCreatedAdminId()!=null && !loginAdminId.equals(administrator.getCreatedAdminId()) 
				&& !loginAdminId.equals(id)) {
			throw new AdminNoEditException(CodeMessage.ADMIN_NOEDIT_FAILED);
		}
		
		//判断修改后的管理员用户名唯一性
		Administrator adminInfo = administratorDao.findByUsername(administrator.getUsername());
		if(adminInfo != null && !id.equals(adminInfo.getId())) {
			throw new AdminHasExistException(CodeMessage.ADMIN_HASEXIST_FAILED);
		}
		
		//判断创建者ID是否为空（除超级管理员外）
		if((administrator.getType() != 0) && (administrator.getCreatedAdminId()==null || administrator.getCreatedAdminId().toString().length()<=0)) {
			throw new AdminNoCreatAdminException(CodeMessage.ADMIN_NOCREATADMIN_FAILED);
		}
		
		//修改非超级管理员信息
		if(administrator.getCreatedAdminId() != null) {
			Administrator createdAdminInfo = administratorDao.findOne(administrator.getCreatedAdminId());
			
			//判断创建者ID是否存在
			if(createdAdminInfo == null) {
				throw new AdminNoCreatAdminException(CodeMessage.ADMIN_NOCREATADMIN_FAILED);
			}
			
			//创建者等级是否有创建下级用户的权限，四级用户无法创建下级
			if(createdAdminInfo.getType()!=null && createdAdminInfo.getType().toString().length()>0 && createdAdminInfo.getType()==4){
				throw new AdminTypeLowException(CodeMessage.ADMIN_TYPELOW_FAILED);
			}
			
			//判断管理员等级是否为创建者的下一级别
			if(createdAdminInfo.getType()!=null && createdAdminInfo.getType().toString().length()>0 
				&& administrator.getType()!=null && administrator.getType().toString().length()>0 
				&& (createdAdminInfo.getType()+1) != administrator.getType()){
				throw new AdminTypeException(CodeMessage.ADMIN_TYPE_FAILED);
			}
		}
		
		String oldPassword = administratorDao.findOne(id).getPassword();
		String newPassword = administrator.getPassword();
		//加密前判断是否为修改密码
		if(newPassword!=null && newPassword.length()>0 && !oldPassword.equals(newPassword)) {
			//对前台传过来的密码进行解密操作
			newPassword = AESUtils.desEncrypt(newPassword);
			boolean checkPassword = true;
			//1、超级管理员和普通管理员使用不同的密码复杂度校验
			if(administrator.getType() == 0) {
				checkPassword = checkSuperPasswordRule(newPassword);
			}else {
				checkPassword = checkPasswordRule(newPassword);
			}
			//2、对密码进行MD5的加密
			if(checkPassword) {
				String password = MD5Utils.md5Digest(newPassword);
				administrator.setPassword(password);
			}else {
				throw new AdminPasswordRuleException(CodeMessage.ADMIN_PASSWORDRULE_FAILED);
			}
		}
		
		//校验管理员电话
		String phoneNumber = administrator.getPhoneNumber();
		if(phoneNumber!=null && phoneNumber.length()>0 && !checkPhoneNumber(phoneNumber)) {
			throw new AdminPhoneNumberException(CodeMessage.ADMIN_PHONENUMBER_FAILED);
		}
		
		administrator.setId(id);
		administratorDao.save(administrator);
	}

	/**
	 * 批量删除(真删)
	 * 
	 * @param ids
	 * @return
	 */
	public HashMap<String, List<Integer>> deleteAdministrator(Integer[] ids){

		// 创建集合，用于封装
		List<Integer> failList = new ArrayList<>();
		// 将数组中内容转入到集合中
		List<Integer> idList = Arrays.asList(ids);
		// 遍历要修改的数组，封装删除失败的数据ID
		for (Integer id : ids) {
			try {
				//获取当前登录管理员ID
				Integer loginAdminId = getAdminId();
				Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);
				Administrator delAdminInfo = administratorDao.findOne(id);
				//1、判断当前用户是否为超级管理员，或者是否为被删除者的创建者，才有删除权限
				if(loginAdminInfo.getType()==0 || (delAdminInfo.getCreatedAdminId()!=null && loginAdminId.equals(delAdminInfo.getCreatedAdminId()))) {
					//2、判断被删除管理员是否存在子级管理员
					List<Administrator> childrenAdminList = administratorDao.findByCreatedAdminId(id);
					//3、判断是否存在该管理员创建的应用
					List<Application> appList = applicationDao.findByCreatedAdminId(id);
					//4、判断是否存在该管理员创建的权限
					List<Authority> authorityList = authorityDao.findByCreatedAdminId(id);
					//5、判断是否存在该管理员创建的角色
					List<Role> roleList = roleDao.findByCreatedAdminId(id);
					//6、判断是否存在该管理员创建的用户
					List<User> userList = userDao.findByCreatedAdminId(id);
					
					if(childrenAdminList.size()<=0 && appList.size()<=0 && authorityList.size()<=0 && roleList.size()<=0 && userList.size()<=0) {
						administratorDao.delete(id);
					}else {//该管理员存在子级管理员
						failList.add(id);
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
	 * 修改单个字段值（冻结、可用）
	 * 
	 * @param id
	 * @param fieldModel
	 * @throws
	 */
	public void updateFieldById(Integer id, FieldModel fieldModel) 
			throws AdminNullUpdateException,AdminStatusException,AdminNoEditException,SecurityUserException,SQLException,Exception {

		// 获取修改条件
		String fieldName = fieldModel.getFieldName();
		String fieldValue = fieldModel.getFieldValue();
		
		// 如果修改条件为空
		if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(fieldValue)) {
			throw new AdminNullUpdateException(CodeMessage.ADMIN_NULLUPDATE_FAILED);
		}
		
		//修改可用状态
		if("status".equals(fieldName)){
			if(!"0".equals(fieldValue) && !"1".equals(fieldValue)) {
				throw new AdminStatusException(CodeMessage.ADMIN_STATUS_FAILED);
			}
		}else {
			throw new AdminStatusException(CodeMessage.ADMIN_STATUS_FAILED);
		}
		
		//获取当前登录管理员ID
		Integer loginAdminId = getAdminId();
		Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);
		//被冻结管理员信息
		Administrator adminInfo = administratorDao.findOne(id);
		
		//判断当前登录管理员是否为超级管理员，或是否为该管理员的创建者，否则没有冻结该管理员的权利
		if(loginAdminInfo.getType()!=0 && adminInfo.getCreatedAdminId()!=null && !loginAdminId.equals(adminInfo.getCreatedAdminId())) {
			throw new AdminNoEditException(CodeMessage.ADMIN_NOEDIT_FAILED);
		}
		
		Administrator administrator = administratorDao.findOne(id);
		String methodName = "set" + StringUtils.capitalize(fieldName);

		ObjectUtil.invokeMethod(fieldName, fieldValue, administrator, methodName);
		administratorDao.save(administrator);

	}

	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	public AdministratorInfo findAdministratorById(Integer id) throws SQLException{

		Administrator administrator = administratorDao.findOne(id);
		if(administrator == null) {
			return null;
		}
		AdministratorInfo administratorInfo = new AdministratorInfo();
		//复制administrator对象到administratorInfo对象
		BeanUtils.copyProperties(administrator, administratorInfo);
		//获得创建者的adminName，赋值到新对象中
		String adminName = "";
		Integer createdAdminId = administrator.getCreatedAdminId();
		if(createdAdminId != null && createdAdminId.toString().length() > 0) {
			adminName = administratorDao.getOne(createdAdminId).getUsername();
		}
		administratorInfo.setCreatedAdminName(adminName);
		return administratorInfo;
	}

	/**
	 * 分页条件查询
	 * 
	 * @param administrator
	 *            条件封装对象
	 * @param page
	 * @param size
	 * @return
	 * @throws
	 */
	public PageBean<AdministratorInfo> findAdministratorByPageAndConditions(Administrator administrator, Integer page, Integer size) throws SQLException,SecurityUserException{
		
		//创建分页对象
		PageRequest pageRequest = null;
		//创建查询结果集对象
		PageBean<AdministratorInfo> pageBean = new PageBean<>();
		//获取当前登录管理员ID
		Integer loginAdminId = getAdminId();
		Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);

		//创建条件查询对象
		Specification<Administrator> specification = new Specification<Administrator>() {

			@Override
			public Predicate toPredicate(Root<Administrator> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 创建一个集合，将所有创建好的条件存进集合，一次查询。
				List<Predicate> predicateList = new ArrayList<Predicate>();
				
				//管理员用户名
				if(administrator.getUsername() != null && administrator.getUsername().length() > 0) {
					Predicate username = cb.like(root.get("username"), "%"+administrator.getUsername()+"%");
					predicateList.add(username);
				}
				
				//管理员昵称
				if(administrator.getNickname() != null && administrator.getNickname().length() > 0) {
					Predicate nickname = cb.like(root.get("nickname"), "%"+administrator.getNickname()+"%");
					predicateList.add(nickname);
				}
				
				//管理员电话
				if(administrator.getPhoneNumber()!= null && administrator.getPhoneNumber().length() > 0) {
					Predicate phoneNumber = cb.like(root.get("phoneNumber"), "%"+administrator.getPhoneNumber()+"%");
					predicateList.add(phoneNumber);
				}
				
				//管理员是否可用
				if(administrator.getStatus()!= null && administrator.getStatus().toString().length() > 0) {
					Predicate status = cb.equal(root.get("status"), administrator.getStatus());
					predicateList.add(status);
				}
				
				//管理员等级
				if(administrator.getType() != null && administrator.getType().toString().length() > 0) {
					Predicate type = cb.equal(root.get("type"), administrator.getType());
					predicateList.add(type);
				}
				
				//当前登录管理员所能看到的所有子级管理员信息（除：超级管理员看到所有信息）
				Set<Integer> childrenAdminIds = findAllChildrenAdmin(loginAdminId);
				List<Predicate> adminIdsList = new ArrayList<Predicate>();
				for(Integer id:childrenAdminIds) {
					Predicate adminId = cb.equal(root.get("id"), id);
					adminIdsList.add(adminId);
				}
				Predicate[] adminIdsPre = new Predicate[adminIdsList.size()];
				Predicate adminIds = cb.or(adminIdsList.toArray(adminIdsPre));
				predicateList.add(adminIds);
				
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
			Page<Administrator> pageInfo = administratorDao.findAll(specification, pageRequest);
			List<Administrator> content = pageInfo.getContent();
			//转换list集合对象，用于封装需要转换的值
			List<AdministratorInfo> infoList = changeList(content);
			
			pageBean.setItems(infoList);
			pageBean.setTotalNum(pageInfo.getTotalElements());
		} else {
			//如果是条件查询
			List<Administrator> findAll = administratorDao.findAll(specification, sort);
			//转换list集合对象，用于封装需要转换的值
			List<AdministratorInfo> infoList = changeList(findAll);
			
			pageBean.setItems(infoList);
			pageBean.setTotalNum((long) findAll.size());
			
		}
		
		return pageBean;

	}

	/**
	 * 获取当前登录管理员id
	 * @return
	 * @throws SecurityUserException
	 */
	public Integer getAdminId() throws SecurityUserException {
		//获取当前登录管理员id
		String username = SecurityUserUtils.getSecurityUser().getUsername();
		Administrator admin = administratorDao.findByUsername(username);
		return admin.getId();
	}
	
	/**
	 * 获取当前登录管理员所有信息
	 * @return
	 * @throws SecurityUserException
	 */
	public Administrator getLoginAdminInfo() throws SecurityUserException,SQLException {
		Integer adminId = getAdminId();
		if(adminId != null) {
			Administrator administrator = administratorDao.findOne(adminId);
			return administrator;
		}else {
			return null;
		}
	}
	
	/**
	 * 查找当前管理员的一级管理员（超级管理员和一级管理员，返回自己本身ID）
	 * @param adminId 当前管理员ID
	 * @return 返回最高级管理员ID
	 */
	public Integer findTopAdminId(Integer adminId) {
		//1、定义当前管理员的最高级管理员的ID
		Integer topAdminId = null;
		//2、当前管理员对象
		Administrator adminInfo = administratorDao.findOne(adminId);
		Integer adminInfoId = adminInfo.getId();
		//3、判断当前管理员等级是否为0或1
		if (adminInfo.getType() == 0 || adminInfo.getType() == 1) {
			topAdminId = adminInfoId;
		}else {
			//4、如果是一级以下管理员
			while (topAdminId == null) {
				//查找出当前管理员的直系管理员
				Administrator createdAdmin = administratorDao.findParentAdminById(adminInfoId);
				//查看直系管理员等级
				Integer createdType = createdAdmin.getType();
				//如果等级是1，结束循环，返回该管理员ID
				if (createdType == 1) {
					topAdminId = createdAdmin.getId();
					break;
				} else {
					//如果等级大于1，那么进行下一次循环，并将管理员id(adminInfoId)改为当前管理员的id
					adminInfoId = createdAdmin.getId();
				}
			}
		}
		return topAdminId;
	}
	
	/**
	 * 根据当前管理员id查询其所有子管理员Id
	 * @param adminId
	 * @return	返回所有的直接子管理员Id
	 */
	public Set<Integer> findAllChildrenAdmin(Integer parentAdminId){
		
		Set<Integer> result = new HashSet<>();
		findChildrenAdmin(result, parentAdminId);
		return result;
	}
	
	/**
	 * 根据当前管理员id查询其直接子管理员Id
	 * @param adminId
	 * @return	返回所有的直接子管理员Id
	 */
	public void findChildrenAdmin(Set<Integer> result, Integer parentAdminId){
		
		//查询直接子类的ID数组
		Set<Integer> ids = administratorDao.findChildrenAdminId(parentAdminId);
		if (ids.size() == 0) {
			return;
		}
		result.addAll(ids);
		//遍历子类的数组
		for (Integer childrenId : ids) {
			//如果
			findChildrenAdmin(result, childrenId);
		}
	}
	
	/**
	 * 获得当前登录管理员的下一级别
	 * @return
	 * @throws SecurityUserException
	 */
	public Map<Integer, String> getType() throws SecurityUserException,SQLException{
		Map<Integer, String> typeMap = new HashMap<Integer, String>();
		Integer typeValue = null;//级别值
		String typeName = "";//级别名称
		
		//获取当前登录管理员ID
		Integer loginAdminId = getAdminId();
		Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);
		//下一级别
		typeValue = loginAdminInfo.getType()+1;
		
		if(typeValue == 1) {
			typeName = "一级";
		}else if(typeValue == 2) {
			typeName = "二级";
		}else if(typeValue == 3) {
			typeName = "三级";
		}else if(typeValue == 4) {
			typeName = "四级";
		}
		
		typeMap.put(typeValue, typeName);
		return typeMap;
	}
	
	/**
	 * 转换list集合对象
	 * @param list
	 * @return
	 */
	private List<AdministratorInfo> changeList(List<Administrator> adminList) {
		
		List<AdministratorInfo> infoList = new ArrayList<>();
		//循环遍历
		for (Administrator admin : adminList) {
			AdministratorInfo adminInfo = new AdministratorInfo();
			//复制administrator对象到administratorInfo对象
			BeanUtils.copyProperties(admin, adminInfo);
			//获得创建者的adminName，赋值到新对象中
			String adminName = "";
			Integer createdAdminId = admin.getCreatedAdminId();
			if(createdAdminId != null && createdAdminId.toString().length() > 0) {
				adminName = administratorDao.getOne(createdAdminId).getUsername();
			}
			adminInfo.setCreatedAdminName(adminName);
			infoList.add(adminInfo);
		}
		return infoList;
	}
	
	/**
	 * 重置密码（只有超级管理员 和 创建该管理员的管理员才能重置密码）
	 * @param id
	 * @throws
	 */
	public void resetPassword(Integer id) throws SecurityUserException, AdminNoUpdatePasswordException, NoSuchAlgorithmException, SQLException {
		
		//获取当前登录管理员ID
		Integer loginAdminId = getAdminId();
		Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);
		//被重置的管理员信息
		Administrator adminInfo = administratorDao.findOne(id);
		
		//判断当前登录管理员是否为超级管理员，或是否为该管理员的创建者，否则没有修改该管理员密码的权利
		if(loginAdminInfo.getType()!=0 && adminInfo.getCreatedAdminId()!=null && !loginAdminId.equals(adminInfo.getCreatedAdminId())) {
			throw new AdminNoUpdatePasswordException(CodeMessage.ADMIN_NO_UPDATE_PASSWORD_FAILED);
		}
		
		//保存重置的新密码
		adminInfo.setPassword(MD5Utils.md5Digest("#cd123456"));
		administratorDao.save(adminInfo);
	}
	
	
}

