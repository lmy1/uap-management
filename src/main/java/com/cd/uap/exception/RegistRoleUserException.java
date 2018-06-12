package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class RegistRoleUserException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public RegistRoleUserException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RegistRoleUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public RegistRoleUserException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RegistRoleUserException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RegistRoleUserException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public RegistRoleUserException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
