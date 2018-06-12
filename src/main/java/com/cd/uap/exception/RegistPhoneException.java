package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class RegistPhoneException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public RegistPhoneException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RegistPhoneException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public RegistPhoneException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RegistPhoneException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RegistPhoneException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public RegistPhoneException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
