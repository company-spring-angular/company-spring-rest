package com.company.rest.controller;

import com.company.dao.CompanyDAO;
import com.company.model.Company;
import com.company.rest.exceptions.CompanyNotFoundException;
import com.company.rest.exceptions.ValidationException;
import com.company.rest.validation.CompanyValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/v1/companies")
public class CompanyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class.getName());

    private CompanyDAO companyDAO;

    @Autowired
    public CompanyController(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Company> create(@RequestBody final Company company) throws ValidationException {
        LOGGER.info("create: company={}", company);

        CompanyValidator.validate(company);

        Company savedCompany = companyDAO.save(company);

        HttpHeaders headers = createHttpHeaders();

        return new ResponseEntity<Company>(savedCompany, headers, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<Company>> list() {
        LOGGER.info("list");
        Collection<Company> allCompanies = companyDAO.findAll();
        HttpHeaders headers = createHttpHeaders();

        return new ResponseEntity<Collection<Company>>(allCompanies, headers, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/{companyId}", method = RequestMethod.GET)
    public ResponseEntity<Company> find(@PathVariable String companyId) throws CompanyNotFoundException {
        LOGGER.info("find: companyId={}", companyId);

        if (StringUtils.isEmpty(companyId)) {
            throw new CompanyNotFoundException(companyId);
        }

        Company company = companyDAO.findById(companyId);

        if (company == null) {
            throw new CompanyNotFoundException(companyId);
        }
        HttpHeaders headers = createHttpHeaders();

        return new ResponseEntity<Company>(company, headers, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/{companyId}", method = RequestMethod.PUT)
    public ResponseEntity<Company> update(@PathVariable final String companyId, @RequestBody(required = true) final Company company) throws CompanyNotFoundException, ValidationException {
        LOGGER.info("update: companyId={}, company={}", companyId, company);

        if (StringUtils.isEmpty(companyId)) {
            throw new CompanyNotFoundException(companyId);
        }

        CompanyValidator.validate(company);
        Company found = companyDAO.findById(companyId);

        if (found == null) {
            throw new CompanyNotFoundException(companyId);
        }

        company.setCid(companyId);
        companyDAO.update(companyId, company);
        HttpHeaders headers = createHttpHeaders();

        return new ResponseEntity<Company>(company, headers, HttpStatus.OK);
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//        headers.setAccessControlAllowOrigin("localhost");
//        headers.setAccessControlAllowOrigin("*");
        return headers;
    }

}
