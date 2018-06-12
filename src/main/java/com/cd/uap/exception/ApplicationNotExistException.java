package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class ApplicationNotExistException extends LogicCheckException{

	private static final long serialVersionUID = 1L;

	public ApplicationNotExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApplicationNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ApplicationNotExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ApplicationNotExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ApplicationNotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ApplicationNotExistException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
