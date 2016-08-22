package com.yy.jp.javaserver.exceptions;


public class ServerInitException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2587928275840202930L;

	public ServerInitException() {
		super();
	}

	public ServerInitException(String message) {
		super(message);
	}

	public ServerInitException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerInitException(Throwable cause) {
		super(cause);
	}

	protected ServerInitException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
