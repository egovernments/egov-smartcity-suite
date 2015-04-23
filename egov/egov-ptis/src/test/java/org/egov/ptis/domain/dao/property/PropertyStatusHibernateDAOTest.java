/**
 * 
 */
package org.egov.ptis.domain.dao.property;

import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.ptis.domain.entity.property.PropertyStatus;

/**
 * PropertyStatusHibernateDAOTest
 * 
 * @author Ramakrishna
 **/
public class PropertyStatusHibernateDAOTest extends EgovHibernateTest {

	private PropertyStatusDAO propertyStatusDao;
	private PropertyStatus propertyStatus;

	public void setUp() throws Exception {
		propertyStatusDao = PropertyDAOFactory.getDAOFactory().getPropertyStatusDAO();
	}

	public void tearDown() throws Exception {
		propertyStatusDao = null;
		propertyStatus = null;
	}

	public void testGetPropertyStatusByNameInputEmptyName() {
		propertyStatus = propertyStatusDao.getPropertyStatusByName("");
		assertNull(propertyStatus);
	}

	public void testGetPropertyStatusByNameInputNullName() {
		propertyStatus = propertyStatusDao.getPropertyStatusByName(null);
		assertNull(propertyStatus);
	}

	public void testGetPropertyStatusByName() {
		propertyStatus = propertyStatusDao.getPropertyStatusByName("Create");
		assertNotNull(propertyStatus);
	}

	public void testGetPropertyStatusByCodeInputEmptyCode() {
		propertyStatus = propertyStatusDao.getPropertyStatusByCode("");
		assertNull(propertyStatus);
	}

	public void testGetPropertyStatusByCodeInputNullCode() {
		propertyStatus = propertyStatusDao.getPropertyStatusByCode(null);
		assertNull(propertyStatus);
	}

	public void testGetPropertyStatusByCode() {
		propertyStatus = propertyStatusDao.getPropertyStatusByCode("CREATE");
		assertNotNull(propertyStatus);
	}
}
