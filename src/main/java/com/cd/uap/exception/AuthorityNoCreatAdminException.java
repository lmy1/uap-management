package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AuthorityNoCreatAdminException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AuthorityNoCreatAdminException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoCreatAdminException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoCreatAdminException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoCreatAdminException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoCreatAdminException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AuthorityNoCreatAdminException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
