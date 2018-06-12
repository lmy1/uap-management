package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AuthorityNoAppException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AuthorityNoAppException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoAppException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoAppException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoAppException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoAppException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoAppException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
