package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class ValidateCodeException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public ValidateCodeException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ValidateCodeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ValidateCodeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ValidateCodeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ValidateCodeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ValidateCodeException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
