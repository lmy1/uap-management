package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminStatusException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AdminStatusException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminStatusException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdminStatusException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminStatusException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminStatusException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	public AdminStatusException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
