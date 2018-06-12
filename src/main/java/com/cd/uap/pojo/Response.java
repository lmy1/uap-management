package com.cd.uap.pojo;

import com.cd.uap.utils.CodeMessage;

public class Response {

	private int status;

	private Error error;

	private Object data;

	public Response() {
		super();
	}
	public Response(int status, CodeMessage codeMessage) {
		super();
		this.status = status;
		this.error = new Error(codeMessage.getCode(),codeMessage.getMsg());
	}
	public Response(int status, CodeMessage codeMessage, Object data) {
		super();
		this.status = status;
		this.error = new Error(codeMessage.getCode(),codeMessage.getMsg());
		this.data = data;
	}
	public Response(int status, String code, String message, Object data) {
		super();
		this.status = status;
		this.error = new Error(code,message);
		this.data = data;
	}
	public Response(int status, String code, String message) {
		super();
		this.status = status;
		this.error = new Error(code,message);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Error getError() {
		return error;
	}

	public void setError(String code, String message) {
		this.error = new Error(code, message);
	}
	
	/**
	 * 错误信息类
	 * @author li.mingyang
	 *
	 */
	public class Error {
		
		private String code;
		
		private String message;
		
		public Error() {
			super();
		}
		
		public Error(String code, String message) {
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
		
	}

}
