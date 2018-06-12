/**
 * 
 */
package com.cd.uap.exception;

import com.cd.uap.utils.CodeMessage;

/**
 * @author li.mingyang
 *
 */
public class CurrentAdminNotOperateAppException extends LogicCheckException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public CurrentAdminNotOperateAppException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public CurrentAdminNotOperateAppException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public CurrentAdminNotOperateAppException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CurrentAdminNotOperateAppException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public CurrentAdminNotOperateAppException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public CurrentAdminNotOperateAppException(CodeMessage codeMessage) {
		super(codeMessage);
		// TODO Auto-generated constructor stub
	}
}
