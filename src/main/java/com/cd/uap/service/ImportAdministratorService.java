package com.cd.uap.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cd.uap.bean.Administrator;
import com.cd.uap.dao.AdministratorDao;
import com.cd.uap.utils.ValidatedUtils;

@Service
@Transactional(rollbackFor=Exception.class)
public class ImportAdministratorService {
	
	@Autowired
	private AdministratorDao administratorDao;
	@Autowired 
	private AdministratorService administratorService;
	
	/**
	 * 导入Excel
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> importFile(MultipartFile file) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		//获取当前登录管理员ID
		Integer loginAdminId = administratorService.getAdminId();
		Administrator loginAdminInfo = administratorDao.findOne(loginAdminId);
		
		HSSFWorkbook book = new HSSFWorkbook(file.getInputStream());
		HSSFSheet sheet = book.getSheetAt(0);
		int rowCount = sheet.getLastRowNum();//行数
		int colCount = sheet.getRow(0).getPhysicalNumberOfCells();//列数
		StringBuilder result = new StringBuilder();//记录错误信息
		String message = "";//记录导入返回信息
		int suNum = 0;//记录成功的行数
		int errorNum = 0;//记录失败的行数

		if(loginAdminInfo.getType()!=null && loginAdminInfo.getType()<4) {
			if(colCount == 6){//导入的表格，必须列
				for(int i=2; i<rowCount+1; i++) {
					HSSFRow row = sheet.getRow(i);
					//-----------------------------判断是否有空行数据-----------------------------
					if(row == null) {
						break;
					}
					int check = 0;
					//---------------------------------判断用户名--------------------------------
					String username = "";
					if(row.getCell(0) != null && row.getCell(0).toString().trim().length() > 0) {
						username = row.getCell(0).toString().trim();
						Administrator adminInfo = administratorDao.findByUsername(username);
						if(adminInfo != null) {
							result.append("第"+(i-1)+"条导入失败，用户名已存在；");
							check = 1;
						}
					}else {
						result.append("第"+(i-1)+"条导入失败，用户名不能为空；");
						check = 1;
					}
					//---------------------------------判断密码---------------------------------
					String password = "";
					if(row.getCell(1) != null && row.getCell(1).toString().trim().length() > 0) {
						password = row.getCell(1).toString().trim();
						if(!administratorService.checkPasswordRule(password)) {
							result.append("第"+(i-1)+"条导入失败，密码复杂度不符合要求，规则：英文+数字+特殊字符8位以上；");
							check = 1;
						}
					}else {
						result.append("第"+(i-1)+"条导入失败，密码不能为空；");
						check = 1;
					}
					//---------------------------------判断昵称---------------------------------
					String nickname = "";
					if(row.getCell(2) != null && row.getCell(2).toString().trim().length() > 0) {
						nickname = row.getCell(2).toString().trim();
					}
					//---------------------------------判断电话---------------------------------
					String phoneNumber = "";
					if(row.getCell(3) != null && row.getCell(3).toString().trim().length() > 0) {
						if(row.getCell(3).getCellType() == row.getCell(3).CELL_TYPE_NUMERIC) {
							int phoneNumberInt = (int) row.getCell(3).getNumericCellValue();
							phoneNumber = Integer.toString(phoneNumberInt);
						}else {
							phoneNumber = row.getCell(3).toString().trim();
						}
						if(phoneNumber.length() > 12) {
							result.append("第"+(i-1)+"条导入失败，电话长度不能大于11位；");
							check = 1;
						}
						if(!administratorService.checkPhoneNumber(phoneNumber)) {
							result.append("第"+(i-1)+"条导入失败，管理员电话格式错误；");
							check = 1;
						}
					}else {
						result.append("第"+(i-1)+"条导入失败，电话不能为空；");
						check = 1;
					}
					//-------------------------------判断管理员等级-------------------------------
					int type = 0;
					if(row.getCell(4) != null && row.getCell(4).toString().trim().length() > 0) {
						String typestr = row.getCell(4).toString().trim();
						if("一级".equals(typestr)) {
							type = 1;
						}else if ("二级".equals(typestr)) {
							type = 2;
						}else if ("三级".equals(typestr)) {
							type = 3;
						}else if ("四级".equals(typestr)) {
							type = 4;
						}else {
							result.append("第"+(i-1)+"条导入失败，管理员等级填写错误；");
							check = 1;
						}
					}else {
						result.append("第"+(i-1)+"条导入失败，管理员等级不能为空；");
						check = 1;
					}
					//--------------------------判断创建者等级与管理员等级--------------------------
					Administrator createdAdminInfo = administratorDao.findOne(loginAdminInfo.getId());
					if(createdAdminInfo == null) {
						result.append("第"+(i-1)+"条导入失败，创建者不存在；");
						check = 1;
					}else {
						if(createdAdminInfo.getType()!=null && createdAdminInfo.getType().toString().length()>0 
								&& (createdAdminInfo.getType()+1) != type) {
							result.append("第"+(i-1)+"条导入失败，该管理员等级不符合要求，应为创建者的下级；");
							check = 1;
						}
					}
					
					//----------------------------------判断备注------------------------------------
					String remarker = "";
					if(row.getCell(5) != null && row.getCell(5).toString().trim().length() > 0) {
						remarker = row.getCell(5).toString().trim();
					}
					
					if(check == 0){
						Administrator admin = new Administrator(username, password, nickname, phoneNumber, 1, type, loginAdminInfo.getId(), new Date(), remarker);
						ValidatedUtils.validateObj(admin);//实体类注解校验
						administratorService.addAdministrator(admin);//保存数据到数据库
						suNum++;// 记录成功的条数
					}else{
						errorNum++;//记录错误的条数
					}
					
					if (result == null || result.toString().length() <= 0) {
						message = "导入成功！成功导入" + suNum + "条！";
					} else {
						message = "成功导入" + suNum + "条！失败" + errorNum + "条！失败记录详细信息如下：" + result.toString();
					}
				}
			}else {
				message = "导入失败！失败详细信息如下：该表格不符合规则！";
			}
		}else {
			message = "导入失败！当前登录管理员为四级，没有新增下级管理员的权限！";
		}
		
		map.put("message", message);
		map.put("sucess", suNum);
		map.put("fail", errorNum);
		return map;
	}

}
