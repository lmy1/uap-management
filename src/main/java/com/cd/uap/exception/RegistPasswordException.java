package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class RegistPasswordException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public RegistPasswordException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RegistPasswordException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public RegistPasswordException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RegistPasswordException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RegistPasswordException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public RegistPasswordException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
