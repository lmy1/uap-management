package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

public class LogicCheckException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CodeMessage codeMessage;
	
	public LogicCheckException(CodeMessage codeMessage) {
		super();
		this.codeMessage = codeMessage;
	}

	public LogicCheckException() {
		// TODO Auto-generated constructor stub
	}

	public LogicCheckException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public LogicCheckException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public LogicCheckException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public LogicCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public CodeMessage getCodeMessage() {
		return codeMessage;
	}

	public void setCodeMessage(CodeMessage codeMessage) {
		this.codeMessage = codeMessage;
	}

}
