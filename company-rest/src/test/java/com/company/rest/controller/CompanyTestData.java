package com.company.rest.controller;

import java.util.ArrayList;
import java.util.List;

import com.company.model.Beneficial;
import com.company.model.Address;
import com.company.model.Company;

public class CompanyTestData {

	public static final String COMPANY_ID = "id";
	public static final String COMPANY_NAME = "name";
	public static final String BENEFICIAL_FIRST_NAME = "firstName";
	public static final String BENEFICIAL_LAST_NAME = "lastName";
	public static final String ADDRESS_CITY = "city";
	public static final String ADDRESS_COUNTRY = "country";
	public static final String ADDRESS_FLAT_NUMBER = "flatNumber";
	public static final String ADDRESS_HOUSE_NUMBER = "houseNumber";
	public static final String ADDRESS_POSTAL_CODE = "postalCode";
	public static final String ADDRESS_STREET = "street";

	public static Company createCompany() {
		Address address = createAddress();
		List<Beneficial> owners = createBeneficials();

		return new Company(COMPANY_ID, COMPANY_NAME, address, owners);
	}

	public static List<Beneficial> createBeneficials() {
		List<Beneficial> owners = new ArrayList<Beneficial>();
		Beneficial beneficial = createBeneficial();

		owners.add(beneficial);

		return owners;
	}

	public static Beneficial createBeneficial() {
		Beneficial beneficial = new Beneficial();

		beneficial.setFirstName(BENEFICIAL_FIRST_NAME);
		beneficial.setLastName(BENEFICIAL_LAST_NAME);

		return beneficial;
	}

	public static Address createAddress() {
		Address address = new Address();
		address.setCity(ADDRESS_CITY);
		address.setCountry(ADDRESS_COUNTRY);
		address.setFlatNumber(ADDRESS_FLAT_NUMBER);
		address.setHouseNumber(ADDRESS_HOUSE_NUMBER);
		address.setPostalCode(ADDRESS_POSTAL_CODE);
		address.setStreet(ADDRESS_STREET);
		return address;
	}
}
