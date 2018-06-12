package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class LoginPasswordErrorException extends LogicCheckException {

	public LoginPasswordErrorException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginPasswordErrorException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}

	public LoginPasswordErrorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public LoginPasswordErrorException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public LoginPasswordErrorException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public LoginPasswordErrorException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
