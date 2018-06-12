package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class UserNotExistException  extends LogicCheckException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}

}
