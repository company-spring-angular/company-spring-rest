package com.company.dao;

import java.util.Collection;

import com.company.model.Company;

public interface CompanyDAO {

	Company save(Company company);

	Company findById(String companyId);

	void update(String companyId, Company company);

	Collection<Company> findAll();

}