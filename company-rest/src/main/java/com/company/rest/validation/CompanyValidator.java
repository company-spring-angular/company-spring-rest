package com.company.rest.validation;

import java.util.List;

import com.company.model.Address;
import com.company.model.Beneficial;
import com.company.model.Company;
import com.company.rest.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;

public class CompanyValidator {

	public static void validate(Company company) throws ValidationException {

		if (company == null)
			throw new ValidationException("Company cannot be null");
		if (StringUtils.isEmpty(company.getName()))
			throw new ValidationException("Company 'name' property cannot be null nor empty");

		validate(company.getAddress());
		validate(company.getOwners());
	}

	public static void validate(Address address) throws ValidationException {

		if (address == null)
			throw new ValidationException("Address cannot be null");
		if (StringUtils.isEmpty(address.getCity()))
			throw new ValidationException("Address 'city' property cannot be null nor empty");
		if (StringUtils.isEmpty(address.getCountry()))
			throw new ValidationException("Address 'country' property cannot be null nor empty");
	}

	public static void validate(List<Beneficial> owners) throws ValidationException {

		if (owners == null)
			throw new ValidationException("Owners list cannot be null");
		if (owners.size() == 0)
			throw new ValidationException("Owers list cannot be empty");

		for (Beneficial beneficial : owners) {
			validate(beneficial);
		}
	}

	public static void validate(Beneficial beneficial) throws ValidationException {
		if (beneficial == null)
			throw new ValidationException("Beneficial cannot be null");
		if (StringUtils.isEmpty(beneficial.getFirstName()))
			throw new ValidationException("Beneficial 'firstName' cannot be null nor empty");
		if (StringUtils.isEmpty(beneficial.getLastName()))
			throw new ValidationException("Beneficial 'lastName' cannot be null nor empty");
	}
}
