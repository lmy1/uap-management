package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class ApplicationHasExistException extends LogicCheckException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ApplicationHasExistException() {
		// TODO Auto-generated constructor stub
	}

	public ApplicationHasExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ApplicationHasExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ApplicationHasExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ApplicationHasExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ApplicationHasExistException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
