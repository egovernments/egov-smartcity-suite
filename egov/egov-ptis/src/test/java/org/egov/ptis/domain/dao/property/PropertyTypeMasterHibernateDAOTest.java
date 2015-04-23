package org.egov.ptis.domain.dao.property;

import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;

public class PropertyTypeMasterHibernateDAOTest extends EgovHibernateTest {

	private PropertyTypeMasterDAO propertyTypeMasterDAO;
	private PropertyTypeMaster propertyTypeMaster;

	public void setUp() throws Exception {
		propertyTypeMasterDAO = PropertyDAOFactory.getDAOFactory().getPropertyTypeMasterDAO();
	}

	public void tearDown() throws Exception {
		propertyTypeMasterDAO = null;
		propertyTypeMaster = null;
	}

	public void testGetPropertyTypeMasterByCodeInputEmptyCode() {
		propertyTypeMaster = propertyTypeMasterDAO.getPropertyTypeMasterByCode("");
		assertNull(propertyTypeMaster);
	}

	public void testGetPropertyTypeMasterByCodeInputNullCode() {
		propertyTypeMaster = propertyTypeMasterDAO.getPropertyTypeMasterByCode(null);
		assertNull(propertyTypeMaster);
	}

	public void testGetPropertyTypeMasterByWrongCode() {
		propertyTypeMaster = propertyTypeMasterDAO.getPropertyTypeMasterByCode("WRONG_CODE");
		assertNull(propertyTypeMaster);
	}

	public void testGetPropertyTypeMasterByCode() {
		propertyTypeMaster = propertyTypeMasterDAO.getPropertyTypeMasterByCode("SUP_STRUC");
		assertNotNull(propertyTypeMaster);
	}
}
