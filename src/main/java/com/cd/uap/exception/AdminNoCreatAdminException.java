package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminNoCreatAdminException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AdminNoCreatAdminException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminNoCreatAdminException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdminNoCreatAdminException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminNoCreatAdminException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminNoCreatAdminException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AdminNoCreatAdminException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
