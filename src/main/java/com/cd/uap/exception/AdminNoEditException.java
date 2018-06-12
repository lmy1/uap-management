package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminNoEditException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AdminNoEditException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminNoEditException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdminNoEditException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminNoEditException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminNoEditException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AdminNoEditException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
