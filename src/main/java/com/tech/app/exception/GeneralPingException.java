package com.tech.app.exception;

public class GeneralPingException extends Exception {


	private static final long serialVersionUID = 6183056916667521455L;
	
	public GeneralPingException() {
		super();
	}
	public GeneralPingException(String message) {
		super(message);
	}
	public GeneralPingException(String message, Throwable cause) {
		super(message, cause);
	}
	

}
