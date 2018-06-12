package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class PhoneNumberHasExistException extends LogicCheckException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PhoneNumberHasExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhoneNumberHasExistException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}

	public PhoneNumberHasExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PhoneNumberHasExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PhoneNumberHasExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PhoneNumberHasExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
