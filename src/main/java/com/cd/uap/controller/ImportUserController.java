package com.cd.uap.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cd.uap.exception.ExcelFormException;
import com.cd.uap.exception.SecurityUserException;
import com.cd.uap.pojo.Response;
import com.cd.uap.service.ImportUserService;
import com.cd.uap.utils.CodeMessage;

@RestController
@RequestMapping("/api/uap/v1/userIO")
public class ImportUserController extends BaseController {
	private static Logger log = LoggerFactory.getLogger(ImportUserController.class);
	@Autowired
	private ImportUserService importUserService;

	/**
	 * 用户导入模板下载
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/templateDownload", method = RequestMethod.GET)
	public Response templateDownload(HttpServletRequest request, HttpServletResponse response) {
		Response result = null;
		String fileName = "user.xls";
		result = super.downloadFile(fileName, request, response);
		return result;
	}

	/**
	 * 导入Excel
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public Response importFile(@RequestParam(value = "file") MultipartFile file) {
		Response response = null;
		String message = "";
		try {
			message = importUserService.importFile(file);
		} catch (SecurityUserException e) {
			response = new Response(1, CodeMessage.VALIDATE_FAILED.getCode(), e.getMessage());
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (ExcelFormException e) {
			response = new Response(1, CodeMessage.EXCEL_FORM_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		} catch (IOException e) {
			response = new Response(1, CodeMessage.INSERT_FAILED);
			log.error(response.getError().getCode() + ":" + response.getError().getMessage());
			return response;
		}
		Map<String,String> messageMap = new HashMap<String,String>();
		String success = message.substring(4, 5);
		String fail = message.substring(9, 10);
		if(fail.equals("0"))message="成功导入"+success+"条,无失败记录";
		messageMap.put("success", success);
		messageMap.put("message", message);
		messageMap.put("fail", fail);
		return new Response(0,CodeMessage.OPERATION_SUCCESS,messageMap);
	}
}
