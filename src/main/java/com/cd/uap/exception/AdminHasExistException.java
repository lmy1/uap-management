package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminHasExistException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AdminHasExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminHasExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdminHasExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminHasExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminHasExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AdminHasExistException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
