package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdministratorNotExistException extends LogicCheckException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdministratorNotExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdministratorNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdministratorNotExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdministratorNotExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdministratorNotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AdministratorNotExistException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
