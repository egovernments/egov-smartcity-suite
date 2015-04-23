/**
 * 
 */
package org.egov.ptis.domain.dao.property;

import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.ptis.domain.entity.property.PropertySource;

/**
 * PropertySourceHibernateDAOTest
 * 
 * @author Ramakrishna
 **/
public class PropertySourceHibernateDAOTest extends EgovHibernateTest {

	private PropertySourceDAO propertySourceDao;
	private PropertySource propertySource;

	public void setUp() throws Exception {
		propertySourceDao = PropertyDAOFactory.getDAOFactory().getPropertySourceDAO();
	}

	public void tearDown() throws Exception {
		propertySourceDao = null;
		propertySource = null;
	}

	public void testGetPropertySourceByCodeInputEmptyCode() {
		propertySource = propertySourceDao.getPropertySourceByCode("");
		assertNull(propertySource);
	}

	public void testGetPropertySourceByCodeInputNullCode() {
		propertySource = propertySourceDao.getPropertySourceByCode(null);
		assertNull(propertySource);
	}

	public void testGetPropertySourceByCode() {
		propertySource = propertySourceDao.getPropertySourceByCode("MNCPL-RECORDS");
		assertNotNull(propertySource);
	}
}
