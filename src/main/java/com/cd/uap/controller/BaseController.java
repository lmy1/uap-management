package com.cd.uap.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;

import com.cd.uap.pojo.Response;
import com.cd.uap.utils.CodeMessage;

@RestController
public class BaseController {
	
	/**
	 * 模板下载
	 * @param fileName 模板名称
	 * @param request
	 * @param response
	 * @return
	 */
	public Response downloadFile(String fileName, HttpServletRequest request, HttpServletResponse response) {
		Response result = new Response();
		try {
			response.addHeader("Content-Disposition",
					"attachment; filename=" + new String((fileName).getBytes("GB2312"), "iso8859-1") + ".xls");
			InputStream input = null;
			input = this.getClass().getResourceAsStream("/templates/" + fileName);
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				response.getOutputStream().write(buffer, 0, n);
			}
			result.setStatus(0);
			result.setError(CodeMessage.OPERATION_SUCCESS.getCode(), CodeMessage.OPERATION_SUCCESS.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(1);
			result.setError(CodeMessage.TEMPLATEDOWNLOAD_FAILED.getCode(), CodeMessage.TEMPLATEDOWNLOAD_FAILED.getMsg());
		} finally {
			try {
				if (response.getOutputStream() != null)
					response.getOutputStream().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 关闭流
		}
		return result;
	}
}
