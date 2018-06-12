package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminFrozenException extends LogicCheckException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdminFrozenException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}

	public AdminFrozenException() {
		// TODO Auto-generated constructor stub
	}

	public AdminFrozenException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminFrozenException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AdminFrozenException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminFrozenException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
