package com.cd.uap.controller;

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

import com.cd.uap.exception.ValidateException;
import com.cd.uap.pojo.Response;
import com.cd.uap.service.ImportAdministratorService;
import com.cd.uap.utils.CodeMessage;

@RestController
@RequestMapping("/api/uap/v1/administratorIO")
public class ImportAdministratorController extends BaseController{
	
	private static Logger log = LoggerFactory.getLogger(ImportAdministratorController.class);
	
	@Autowired 
	private ImportAdministratorService importAdministratorService;

	/**
	 * 应用管理员导入模板下载
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/templateDownload", method = RequestMethod.GET)
	public Response downloadFile2(HttpServletRequest request, HttpServletResponse response) {
		Response result = new Response();
		String fileName = "adminList.xls";
		try {
			super.downloadFile(fileName, request, response);
		} catch (Exception e) {
			result.setStatus(1);
			result.setError(CodeMessage.TEMPLATEDOWNLOAD_FAILED.getCode(), e.getMessage());
			log.error(CodeMessage.TEMPLATEDOWNLOAD_FAILED.getCode()+","+e.getMessage());
			return result;
		}
		
		result.setStatus(0);
		result.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		return result;
	}
	
	/**
	 * 导入Excel
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public Response importFile(@RequestParam(value = "file") MultipartFile file){
		Response response = new Response();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = importAdministratorService.importFile(file);
		} catch (ValidateException e) {//注解校验异常
			response.setStatus(1);
			response.setError(CodeMessage.VALIDATE_FAILED.getCode(), CodeMessage.VALIDATE_FAILED.getMsg());
			log.error(CodeMessage.VALIDATE_FAILED.getCode()+","+CodeMessage.VALIDATE_FAILED.getMsg());
			return response;
		} catch (Exception e) {
			response.setStatus(1);
			response.setError(CodeMessage.INSERT_FAILED.getCode(), CodeMessage.INSERT_FAILED.getMsg());
			log.error(CodeMessage.INSERT_FAILED.getCode()+","+CodeMessage.INSERT_FAILED.getMsg());
			return response;
		}
		response.setStatus(0);
		response.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		response.setData(map);
		return response;
	}

}
