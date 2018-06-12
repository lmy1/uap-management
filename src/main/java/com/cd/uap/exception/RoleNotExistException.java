package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class RoleNotExistException extends LogicCheckException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoleNotExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoleNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public RoleNotExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RoleNotExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RoleNotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public RoleNotExistException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
	
}
