package com.cd.uap.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cd.uap.bean.User;
import com.cd.uap.exception.ExcelFormException;
import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.exception.ValidateException;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.ValidatedUtils;

@Service
public class ImportUserService extends BaseImportService {
	private static Logger log = LoggerFactory.getLogger(ImportUserService.class);
	@Autowired
	private UserService userService;
	@Autowired
	private AdministratorService administratorService;

	/**
	 * 导入Excel
	 * 
	 * @param file
	 * @return
	 * @throws SecurityUserException
	 * @throws IOException
	 * @throws ExcelFormException
	 * @throws Exception
	 */
	public String importFile(MultipartFile file) throws SecurityUserException, IOException, ExcelFormException {
		int col = 5;// 表格列数
		return super.excelImport(file, col);
	}

	/**
	 * 用户新增保存
	 */
	public StringBuilder addObj(HSSFRow row, int i) throws SecurityUserException {
		// 获取当前登录管理员ID
		Integer loginAdminId = administratorService.getAdminId();
		StringBuilder result = new StringBuilder();// 记录错误信息
		// ---------------------------------获取用户--------------------------------
		User user = new User(StringUtils.isEmpty(row.getCell(0)) ? null : row.getCell(0).getStringCellValue(),
				StringUtils.isEmpty(row.getCell(1)) ? null : row.getCell(1).getStringCellValue(),
				StringUtils.isEmpty(row.getCell(2)) ? null : row.getCell(2).getStringCellValue(),
				StringUtils.isEmpty(row.getCell(3)) ? null : row.getCell(3).getStringCellValue(), 1, loginAdminId,
				new Date(), StringUtils.isEmpty(row.getCell(4)) ? null : row.getCell(4).getStringCellValue());
		try {
			ValidatedUtils.validateObj(user);
			userService.addUser(user);
		} catch (ValidateException e) {
			result.append("第" + i + "行：记录的错误信息是：" + e.getMessage());
			log.error("第" + i + "行：记录的错误信息是：" + e.getMessage());
			return result;
		} catch (LogicCheckException e) {
			result.append("第" + i + "行：记录的错误信息是：" + e.getCodeMessage().getMsg());
			log.error("第" + i + "行：记录的错误信息是：" + e.getCodeMessage().getMsg());
			return result;
		} catch (NoSuchAlgorithmException e) {// MD5转换错误
			result.append("第" + i + "行：记录的错误信息是：" + CodeMessage.MD5_FAILED.getMsg());
			log.error("第" + i + "行：记录的错误信息是：" + CodeMessage.MD5_FAILED.getMsg());
			return result;
		} catch (SQLException e) {
			result.append("第" + i + "行：记录的错误信息是：sql异常");
			log.error("第" + i + "行：记录的错误信息是：sql异常");
			return result;
		}
		return result;
	}
}
