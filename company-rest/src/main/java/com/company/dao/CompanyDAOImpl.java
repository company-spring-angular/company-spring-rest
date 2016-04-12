package com.company.dao;

import com.company.model.Company;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * WARNING This class returns muttable objects
 */
@Service
public class CompanyDAOImpl implements CompanyDAO {

	private Map<String, Company> companyMap = new ConcurrentSkipListMap<String, Company>();

	private final AtomicLong counter = new AtomicLong();

	/*
	 * (non-Javadoc)
	 * 
	 * @see CompanyDAO#save(com.viabill.company.model.
	 * Company)
	 */
	public Company save(Company company) {
		company.setCid(generateId());
		companyMap.put(company.getCid(), company);
		return company;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see CompanyDAO#find(java.lang.String)
	 */
	public Company findById(String companyId) {
		return companyMap.get(companyId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see CompanyDAO#update(java.lang.String,
	 * Company)
	 */
	public void update(String companyId, Company company) {
		companyMap.put(companyId, company);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see CompanyDAO#findAll()
	 */
	public Collection<Company> findAll() {
		return companyMap.values();
	}

	private String generateId() {
		return String.valueOf(counter.incrementAndGet());
	}
}
