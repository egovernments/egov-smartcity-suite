package org.egov.commons.service;

import org.egov.commons.CFinancialYear;
import org.egov.commons.Functionary;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.junit.Before;
import org.junit.Ignore;

import java.math.BigDecimal;
import java.util.Date;

@Ignore
public class CommonsManagerBeanTest extends EgovHibernateTest {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
/*
	public void testGetFunctionaryByCode() {
		final Functionary functionary = new CommonsServiceImpl()
				.getFunctionaryByCode(createFunctionary().getCode());
		assertEquals("-3000", functionary.getCode().toString());
	}

	public void testGetFunctionaryByCodeforInvalidCode() {
		final Functionary functionary = new CommonsServiceImpl()
				.getFunctionaryByCode(BigDecimal.valueOf(-1000));
		assertNull(functionary);
	}
	*/

	public Functionary createFunctionary() {
		PersistenceService<Functionary, Long> functionaryService;
		functionaryService = new PersistenceService<Functionary, Long>();
		functionaryService.setSessionFactory(new SessionFactory());
		functionaryService.setType(Functionary.class);

		final Functionary functionary = new Functionary();
		functionary.setCode(new BigDecimal(-3000));
		functionary.setName("Functionary");
		functionary.setIsactive(true);
		functionaryService.persist(functionary);
		return functionary;
	}
/*
	public void testGetFinancialYearByDate() {
		final CFinancialYear cFinancialYear = new CommonsServiceImpl()
				.getFinancialYearByDate(new Date());
		assertNotNull(cFinancialYear);
	}
	*/
}
