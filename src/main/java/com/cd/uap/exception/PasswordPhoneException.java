package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class PasswordPhoneException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public PasswordPhoneException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PasswordPhoneException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PasswordPhoneException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PasswordPhoneException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PasswordPhoneException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public PasswordPhoneException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
