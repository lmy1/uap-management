package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminPhoneNumberException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AdminPhoneNumberException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminPhoneNumberException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdminPhoneNumberException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminPhoneNumberException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminPhoneNumberException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AdminPhoneNumberException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
