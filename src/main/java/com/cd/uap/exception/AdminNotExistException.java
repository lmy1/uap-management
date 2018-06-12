package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminNotExistException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AdminNotExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdminNotExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminNotExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminNotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AdminNotExistException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
	
}
