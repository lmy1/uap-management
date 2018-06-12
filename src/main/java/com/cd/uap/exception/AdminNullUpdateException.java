package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminNullUpdateException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AdminNullUpdateException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminNullUpdateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdminNullUpdateException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminNullUpdateException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminNullUpdateException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	public AdminNullUpdateException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
