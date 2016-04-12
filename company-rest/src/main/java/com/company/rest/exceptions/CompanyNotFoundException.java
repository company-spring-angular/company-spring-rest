package com.company.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Company not found")
public class CompanyNotFoundException extends Exception {
	
	private static final long serialVersionUID = 6930510463346420085L;

	public CompanyNotFoundException(String companyId) {
		super(companyId);
	}

}
