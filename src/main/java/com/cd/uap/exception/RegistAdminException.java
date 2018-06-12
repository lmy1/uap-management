package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class RegistAdminException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public RegistAdminException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RegistAdminException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public RegistAdminException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RegistAdminException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RegistAdminException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public RegistAdminException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
