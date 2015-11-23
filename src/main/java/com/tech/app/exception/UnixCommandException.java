package com.tech.app.exception;

public class UnixCommandException extends Exception {


	private static final long serialVersionUID = 6183056916667521455L;
	
	public UnixCommandException() {
		super();
	}
	public UnixCommandException(String message) {
		super(message);
	}
	public UnixCommandException(String message, Throwable cause) {
		super(message, cause);
	}
	

}
