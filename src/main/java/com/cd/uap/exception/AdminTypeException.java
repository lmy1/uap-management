package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminTypeException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AdminTypeException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdminTypeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminTypeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminTypeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AdminTypeException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
