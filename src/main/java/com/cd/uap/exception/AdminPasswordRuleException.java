package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class AdminPasswordRuleException extends LogicCheckException {
	
	private static final long serialVersionUID = 1L;

	public AdminPasswordRuleException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminPasswordRuleException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AdminPasswordRuleException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AdminPasswordRuleException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AdminPasswordRuleException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AdminPasswordRuleException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
