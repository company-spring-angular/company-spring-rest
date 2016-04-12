package com.company.rest.exceptions;

public class ValidationException extends Exception {

	private static final long serialVersionUID = -41987596514581597L;
	
	public ValidationException(String message) {
		super(message);
	}

}
