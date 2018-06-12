package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class UserNotCreateByAdminException extends LogicCheckException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotCreateByAdminException() {
		// TODO Auto-generated constructor stub
	}

	public UserNotCreateByAdminException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserNotCreateByAdminException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotCreateByAdminException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotCreateByAdminException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UserNotCreateByAdminException(CodeMessage userNotCreateByAdminFailed) {
		super(userNotCreateByAdminFailed);
	}

}
