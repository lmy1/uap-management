package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminNoUpdatePasswordException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AdminNoUpdatePasswordException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminNoUpdatePasswordException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdminNoUpdatePasswordException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminNoUpdatePasswordException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminNoUpdatePasswordException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AdminNoUpdatePasswordException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}

