package com.cd.uap.service;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cd.uap.bean.Authority;
import com.cd.uap.exception.ExcelFormException;
import com.cd.uap.exception.LogicCheckException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.exception.ValidateException;
import com.cd.uap.utils.CodeMessage;
import com.cd.uap.utils.ValidatedUtils;

@Service
public class ImportAuthorityService {
	private static Logger log = LoggerFactory.getLogger(ImportAuthorityService.class);
	@Autowired
	private AuthorityService authorityService;

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
	public String importFile(MultipartFile file,Integer appId) throws SecurityUserException, IOException, ExcelFormException {
		int col = 3;// 表格列数
		return excelImport(file, col,appId);
	}

	/**
	 * 用户新增保存
	 */
	public StringBuilder addObj(HSSFRow row, int i, Integer appId) throws SecurityUserException {
		// 获取当前登录管理员ID
		StringBuilder result = new StringBuilder();// 记录错误信息
		// ---------------------------------获取用户--------------------------------
		Authority auth = new Authority(
				StringUtils.isEmpty(row.getCell(0)) ? null : row.getCell(0).getStringCellValue(),
				StringUtils.isEmpty(row.getCell(1)) ? null : row.getCell(1).getStringCellValue(),
				appId,
				StringUtils.isEmpty(row.getCell(3)) ? null : row.getCell(3).getStringCellValue()
			);
		try {
			ValidatedUtils.validateObj(auth);
			authorityService.addAuthority(auth);
		} catch (ValidateException e) {
			result.append("第" + i + "行：记录的错误信息是：" + e.getMessage());
			log.error("第" + i + "行：记录的错误信息是：" + e.getMessage());
			return result;
		} catch (LogicCheckException e) {
			result.append("第" + i + "行：记录的错误信息是：" + e.getCodeMessage().getMsg());
			log.error("第" + i + "行：记录的错误信息是：" + e.getCodeMessage().getMsg());
			return result;
		} catch (SQLException e) {
			result.append("第" + i + "行：记录的错误信息是：sql异常");
			log.error("第" + i + "行：记录的错误信息是：sql异常");
			return result;
		}
		return result;
	}
	
	
	public String excelImport(MultipartFile file, int col,Integer appId)
			throws ExcelFormException, IOException, SecurityUserException {
		HSSFWorkbook book = new HSSFWorkbook(file.getInputStream());
		HSSFSheet sheet = book.getSheetAt(0);
		int rowCount = sheet.getLastRowNum();// 行数
		int colCount = sheet.getRow(0).getPhysicalNumberOfCells();// 列数
		StringBuilder result = new StringBuilder();// 记录错误信息
		String message = "";// 记录导入返回信息
		if (colCount >= col) {// 导入的表格，必须列
			int suNum = 0;// 记录成功的行数
			int errorNum = 0;// 记录失败的行数

			for (int i = 2; i < rowCount + 1; i++) {
				HSSFRow row = sheet.getRow(i);
				// -----------------------------判断是否有空行数据-----------------------------
				if (row == null) {
					continue;
				}
				StringBuilder resultStr = new StringBuilder();// 记录错误信息
				resultStr.append(addObj(row, i + 1,appId));
				// 保存对象
				if (resultStr.length() > 0) {
					result = result.append(resultStr + "   ");
					errorNum++;
				} else
					suNum++;
			}
			message = "成功导入" + suNum + "条！失败" + errorNum + "条！失败记录详细信息如下：" + result;
		} else
			throw new ExcelFormException(CodeMessage.EXCEL_FORM_FAILED);
		return message;
	}
}
