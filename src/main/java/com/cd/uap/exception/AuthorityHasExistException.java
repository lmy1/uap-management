package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AuthorityHasExistException extends LogicCheckException{

	private static final long serialVersionUID = 1L;

	public AuthorityHasExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthorityHasExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AuthorityHasExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AuthorityHasExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AuthorityHasExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AuthorityHasExistException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
