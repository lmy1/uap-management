package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminTypeLowException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AdminTypeLowException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminTypeLowException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdminTypeLowException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminTypeLowException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminTypeLowException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AdminTypeLowException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
