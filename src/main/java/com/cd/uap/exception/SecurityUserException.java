package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class SecurityUserException extends LogicCheckException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SecurityUserException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SecurityUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public SecurityUserException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SecurityUserException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SecurityUserException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public SecurityUserException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}

}
