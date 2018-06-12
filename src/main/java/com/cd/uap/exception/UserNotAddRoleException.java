package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class UserNotAddRoleException extends LogicCheckException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotAddRoleException() {
		// TODO Auto-generated constructor stub
	}

	public UserNotAddRoleException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserNotAddRoleException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotAddRoleException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotAddRoleException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	
	public UserNotAddRoleException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
