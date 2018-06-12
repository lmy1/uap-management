package com.cd.uap.service;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.cd.uap.exception.ExcelFormException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.utils.CodeMessage;

public abstract class BaseImportService {
	public String excelImport(MultipartFile file, int col)
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
				resultStr.append(addObj(row, i + 1));
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

	/**
	 * 对象保存
	 * 
	 * @param row
	 * @return
	 * @throws SecurityUserException
	 */
	public abstract StringBuilder addObj(HSSFRow row, int i) throws SecurityUserException;

}
