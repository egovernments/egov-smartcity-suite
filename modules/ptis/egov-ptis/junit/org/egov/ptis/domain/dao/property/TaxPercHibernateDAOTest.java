package org.egov.ptis.domain.dao.property;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.ptis.domain.entity.property.TaxPerc;

public class TaxPercHibernateDAOTest extends EgovHibernateTest {
	public static final Logger LOGGER = Logger.getLogger(TaxPercHibernateDAOTest.class);

	public void testGetTaxPercInputNull() throws Exception, org.egov.DuplicateElementException {
		TaxPercDAO taxPercDao = PropertyDAOFactory.getDAOFactory().getTaxPercDao();
		TaxPerc taxPerc = taxPercDao.getTaxPerc(null, null, null, null);
		assertNull(taxPerc);
	}

	public void testGetTaxPercInputAmountAndDate() throws Exception,
			org.egov.DuplicateElementException {
		TaxPercDAO taxPercDao = PropertyDAOFactory.getDAOFactory().getTaxPercDao();
		TaxPerc taxPerc = taxPercDao.getTaxPerc(null, null, new BigDecimal(650), new Date());
		assertNotNull(taxPerc);
	}

	public void testGetTaxPercInputAmount() throws Exception, org.egov.DuplicateElementException {
		TaxPercDAO taxPercDao = PropertyDAOFactory.getDAOFactory().getTaxPercDao();
		TaxPerc taxPerc = taxPercDao.getTaxPerc(null, null, new BigDecimal(650), null);
		assertNotNull(taxPerc);
	}

}
