package com.tech.app.exception;

public class HTTPRequestException extends Exception {


	private static final long serialVersionUID = 6183056916667521455L;
	
	public HTTPRequestException() {
		super();
	}
	public HTTPRequestException(String message) {
		super(message);
	}
	public HTTPRequestException(String message, Throwable cause) {
		super(message, cause);
	}
	

}
