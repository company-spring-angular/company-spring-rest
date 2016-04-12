package com.company.rest.controller;

import com.company.dao.CompanyDAO;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestContext {

	@Bean
	@Primary
	public CompanyDAO companyDAOMock() {
		return Mockito.mock(CompanyDAO.class);
	}
}
