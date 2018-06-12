package com.cd.uap.service;

import java.security.NoSuchAlgorithmException;
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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cd.uap.bean.Administrator;
import com.cd.uap.bean.User;
import com.cd.uap.bean.UserInfo;
import com.cd.uap.dao.AdministratorDao;
import com.cd.uap.dao.RoleUserDao;
import com.cd.uap.dao.UserDao;
import com.cd.uap.exception.AdminNotExistException;
import com.cd.uap.exception.PhoneNumberHasExistException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.exception.UserHasExistException;
import com.cd.uap.exception.UserNotCreateByAdminException;
import com.cd.uap.exception.UserNotExistException;
import com.cd.uap.pojo.FieldModel;
import com.cd.uap.pojo.PageBean;
import com.cd.uap.utils.AESUtils;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.MD5Utils;

@Service
public class UserService {
	private static Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private AdministratorService administratorservice;
	@Autowired
	private AdministratorDao administratorDao;
	@Autowired
	private RoleUserDao roleUserDao;

	/**
	 * 新增
	 * 
	 * @param user
	 * @return
	 * @throws AdminNotExistException
	 * @throws SQLException
	 * @throws SecurityUserException
	 * @throws UserHasExistException
	 * @throws NoSuchAlgorithmException
	 * @throws PhoneNumberHasExistException 
	 */
	@Transactional
	public Long addUser(User user) throws AdminNotExistException, SQLException, SecurityUserException,
			UserHasExistException, NoSuchAlgorithmException, PhoneNumberHasExistException {
		// 用户名唯一
		userHasExist(user);
		// 手机号唯一
		phoneHasExist(user);
		// 获取当前登录管理员id
		Integer adminId = administratorservice.getAdminId();
		user.setCreatedAdminId(adminId);
		user.setCreatedDatetime(new Date());
		user.setStatus(1);
		user.setPassword(MD5Utils.md5Digest(user.getPassword()));
		userDao.save(user);
		return user.getId();
	}

	/**
	 * 全字段修改
	 * 
	 * @param id
	 * @param user
	 * @return
	 * @throws AdminNotExistException,SQLException
	 * @throws UserNotCreateByAdminException
	 * @throws UserNotExistException
	 * @throws NoSuchAlgorithmException
	 * @throws SecurityUserException
	 * @throws UserHasExistException
	 * @throws PhoneNumberHasExistException 
	 */
	@Transactional
	public void updateUser(User user) throws SQLException, AdminNotExistException, UserNotExistException,
			UserNotCreateByAdminException, NoSuchAlgorithmException, SecurityUserException, UserHasExistException, PhoneNumberHasExistException,Exception {
		// 修改的管理员和创建的是否为同一个或为超级管理员
		isCreateAdmin(user.getId());
		// 用户名唯一
		userHasExist(user);
		// 手机号唯一
		phoneHasExist(user);
		// 如果传入的密码与数据库中读取的不一致，视为修改了密码
		User oldUser = userDao.findOne(user.getId());
		// 如果前端没有传创建者id，把旧的赋上
		if (StringUtils.isEmpty(user.getCreatedAdminId())) {
			user.setCreatedAdminId(oldUser.getCreatedAdminId());
		}
		if (!oldUser.getPassword().equals(user.getPassword())) {
			//对前台传过来的密码进行解密操作
			String newPassword = AESUtils.desEncrypt(user.getPassword());
			user.setPassword(MD5Utils.md5Digest(newPassword));
		}

		userDao.save(user);
	}
	/**
	 * 重置密码
	 */
	@Transactional
	public void resetPassword(Long id) throws SQLException, NoSuchAlgorithmException, UserNotExistException,
			UserNotCreateByAdminException, SecurityUserException {
		// 修改的管理员和创建的是否为同一个或为超级管理员
		isCreateAdmin(id);
		User user = userDao.findOne(id);
		user.setPassword(MD5Utils.md5Digest("cd123456"));
		userDao.save(user);
	}

	/**
	 * 批量删除(真删)
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional
	public HashMap<String, List<Long>> deleteUser(Long[] ids) {

		// 创建集合，用于封装
		List<Long> failList = new ArrayList<>();
		// 将数组中内容转入到集合中
		List<Long> idList = Arrays.asList(ids);
		// 遍历要修改的数组，封装删除失败的数据ID
		for (Long id : ids) {
			try {
				// 修改的管理员和创建的是否为同一个或为超级管理员
				isCreateAdmin(id);
				userDao.delete(id);
				//删除关联表数据
				roleUserDao.deleteByUserId(id);
			} catch (Exception e) {
				log.error("删除失败:" + id);
				failList.add(id);
			}
		}
		// 创建集合，用于封装success和fail的集合
		HashMap<String, List<Long>> map = new HashMap<>();
		// 如果有删除失败数据
		if (failList.size() > 0) {

			map.put("fail", failList);
			ArrayList<Long> allId = new ArrayList<>();
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
	 * 
	 * @param id
	 * @return
	 */
	public UserInfo findUserById(Long id) throws Exception {
		User user = userDao.findOne(id);
		if (user == null) {
			return null;
		}
		UserInfo userInfo = new UserInfo();
		// 复制user对象到userInfo对象
		BeanUtils.copyProperties(user, userInfo);
		// 获得创建者的adminName，赋值到新对象中
		String adminName = "";
		Integer createdAdminId = user.getCreatedAdminId();
		if (createdAdminId != null && createdAdminId.toString().length() > 0) {
			adminName = administratorDao.getOne(createdAdminId).getUsername();
		}
		userInfo.setCreatedAdminName(adminName);

		return userInfo;
	}

	/**
	 * 分页条件查询
	 * 
	 * @param user
	 *            条件封装对象
	 * @param page
	 * @param size
	 * @return
	 */
	public PageBean<UserInfo> findUserByPageAndConditions(User user, Integer page, Integer size) throws Exception {

		// 创建分页对象
		PageRequest pageRequest = null;
		// 创建查询结果集对象
		PageBean<UserInfo> pageBean = new PageBean<>();

		// 创建条件查询对象
		Specification<User> specification = new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 创建一个集合，将所有创建好的条件存进集合，一次查询。
				List<Predicate> predicateList = new ArrayList<Predicate>();

				// 条件查询
				if (user.getId() != null) {
					Predicate p0 = cb.equal(root.get("id"), user.getId());
					predicateList.add(p0);
				}
				if (user.getUsername() != null) {
					Predicate p1 = cb.like(root.get("username"), "%" + user.getUsername() + "%");
					predicateList.add(p1);
				}
				if (!StringUtils.isEmpty(user.getNickname())) {
					Predicate p2 = cb.like(root.get("nickname"), "%" + user.getNickname() + "%");
					predicateList.add(p2);
				}
				if (user.getPhoneNumber() != null) {
					Predicate p3 = cb.like(root.get("phoneNumber"), "%" + user.getPhoneNumber() + "%");
					predicateList.add(p3);
				}
				if (user.getCreatedAdminId() != null) {
					Predicate p4 = cb.equal(root.get("createdAdminId"), user.getCreatedAdminId());
					predicateList.add(p4);
				}
				if (user.getStatus() != null) {
					Predicate p5 = cb.equal(root.get("status"), user.getStatus());
					predicateList.add(p5);
				}
				// 将条件集合转换为数组并返回
				Predicate[] p = new Predicate[predicateList.size()];
				return cb.and(predicateList.toArray(p));
			}
		};

		Sort sort = new Sort(Direction.DESC, "id");
		if (page > 0 && size > 0) {
			// 如果是分页条件查询
			pageRequest = new PageRequest(page - 1, size, sort);
			Page<User> pageInfo = userDao.findAll(specification, pageRequest);
			List<User> content = pageInfo.getContent();
			// 转换list集合对象，用于封装需要转换的值
			List<UserInfo> infoList = changeList(content);

			pageBean.setItems(infoList);
			pageBean.setTotalNum(pageInfo.getTotalElements());
		} else {
			// 如果是条件查询
			List<User> findAll = userDao.findAll(specification, sort);
			// 转换list集合对象，用于封装需要转换的值
			List<UserInfo> infoList = changeList(findAll);

			pageBean.setItems(infoList);
			pageBean.setTotalNum((long) findAll.size());
		}

		return pageBean;

	}

	/**
	 * 校验修改的管理员和创建的管理员是否为同一个，或为超级管理员
	 * 
	 * @param user
	 * @return
	 * @throws SecurityUserException
	 */
	public void isCreateAdmin(Long id)
			throws UserNotExistException, UserNotCreateByAdminException, SecurityUserException {
		// 获取当前登录管理员id
		Integer adminId = administratorservice.getAdminId();
		// 查询当前管理员
		Administrator admin = administratorDao.findOne(adminId);
		// 获取数据库中原数据创建者id
		User oldUser = userDao.findOne(id);
		if (oldUser == null)
			throw new UserNotExistException(CodeMessage.USER_NOT_EXIST_FAILED);
		if (!(oldUser.getCreatedAdminId().equals(adminId) || admin.getType() == 0))
			throw new UserNotCreateByAdminException(CodeMessage.USER_NOT_CREATE_BY_ADMIN_FAILED);
	}

	/**
	 * 冻结用户
	 * 
	 * @param id
	 * @throws UserNotExistException
	 * @throws UserNotCreateByAdminException
	 * @throws SecurityUserException
	 */
	@Transactional
	public void freezeUserByLogic(Long id, FieldModel fieldModel)
			throws UserNotExistException, UserNotCreateByAdminException, SecurityUserException {
		isCreateAdmin(id);
		User user = userDao.findOne(id);
		if ("status".equals(fieldModel.getFieldName())
				&& ("1".equals(fieldModel.getFieldValue()) || "0".equals(fieldModel.getFieldValue()))) {
			user.setStatus(Integer.valueOf(fieldModel.getFieldValue()));
			userDao.save(user);
		}
	}

	/**
	 * 用户名是否已存在
	 * 
	 * @param username
	 * @throws UserHasExistException
	 */
	private void userHasExist(User user) throws UserHasExistException {
		List<User> userList = userDao.findByUsername(user.getUsername());
		if (userList.size() > 0) {
			for (User oldUser : userList) {
				if (!oldUser.getId().equals(user.getId()))
					throw new UserHasExistException(CodeMessage.USER_HAS_EXIST_FAILED);
			}
		}
	}
	
	public void phoneHasExist(User user) throws PhoneNumberHasExistException {
		List<User> userList = userDao.findByPhoneNumber(user.getPhoneNumber());
		if (userList.size() > 0) {
			for (User oldUser : userList) {
				if (!oldUser.getId().equals(user.getId()))
					throw new PhoneNumberHasExistException(CodeMessage.PHONE_NUMBER_HAS_EXIST_FAILED);
			}
		}
	}

	/**
	 * 转换list集合对象
	 * 
	 * @param list
	 * @return
	 */
	private List<UserInfo> changeList(List<User> userList) {

		List<UserInfo> infoList = new ArrayList<>();
		// 循环遍历
		for (User user : userList) {
			UserInfo userInfo = new UserInfo();
			// 复制user对象到userInfo对象
			BeanUtils.copyProperties(user, userInfo);
			// 获得创建者的adminName，赋值到新对象中
			String adminName = "";
			Integer createdAdminId = user.getCreatedAdminId();
			if (createdAdminId != null && createdAdminId.toString().length() > 0) {
				adminName = administratorDao.getOne(createdAdminId).getUsername();
			}
			userInfo.setCreatedAdminName(adminName);
			infoList.add(userInfo);
		}
		return infoList;
	}

}
