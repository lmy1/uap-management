package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AuthorityNoPowerException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AuthorityNoPowerException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoPowerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoPowerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoPowerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoPowerException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoPowerException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
