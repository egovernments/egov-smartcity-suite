package org.egov.ptis.domain.dao.property;

import java.util.ArrayList;
import java.util.List;

import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;

/**
 * PropertyStatusValuesHibDAOTest
 * 
 * @author Evlyn
 **/

public class PropertyStatusValuesHibDAOTest extends EgovHibernateTest {
	private PropertyStatusValuesDAO propStatusValueDAO;
	private PropertyStatusValues propStatusValues;
	private BasicPropertyDAO basicPropDAO;

	public void setUp() throws Exception {
		propStatusValueDAO = PropertyDAOFactory.getDAOFactory().getPropertyStatusValuesDAO();
		basicPropDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
	}

	public void tearDown() throws Exception {
		propStatusValueDAO = null;
		propStatusValues = null;
		basicPropDAO = null;
	}

	public void testGetLatestPropertyStatusValuesByPropertyIdAndCodeInputNullId() {
		List<String> code = new ArrayList<String>();
		code.add("WRONGCODE");
		propStatusValues = propStatusValueDAO.getLatestPropertyStatusValuesByPropertyIdAndCode(
				null, code);
		assertNull(propStatusValues);
	}

	public void testGetLatestPropertyStatusValuesByPropertyIdAndCode() {
		List<String> code = new ArrayList<String>();
		code.add("CREATE");
		code.add("MODIFY");
		propStatusValues = propStatusValueDAO.getLatestPropertyStatusValuesByPropertyIdAndCode(
				"08-119-0000-000", code);
		assertNotNull(propStatusValues);
	}

	public void testGetParentBasicPropsForChildWithNullBasicProp() {
		List<PropertyStatusValues> propStatusValueList;
		propStatusValueList = propStatusValueDAO.getParentBasicPropsForChild(null);
		assertEquals(0, propStatusValueList.size());
	}

	public void testGetParentBasicPropsForChildWithWrongBasicProp() {
		List<PropertyStatusValues> propStatusValueList;
		BasicProperty basicProperty = basicPropDAO.getAllBasicPropertyByPropertyID("Wrong Bill No");
		propStatusValueList = propStatusValueDAO.getParentBasicPropsForChild(basicProperty);
		assertEquals(0, propStatusValueList.size());
	}

	public void testGetParentBasicPropsForChildWithRelavantBasicProp() {
		List<PropertyStatusValues> propStatusValueList;
		BasicProperty basicProperty = basicPropDAO
				.getAllBasicPropertyByPropertyID("08-119-0000-000");
		propStatusValueList = propStatusValueDAO.getParentBasicPropsForChild(basicProperty);
		assertEquals(1, propStatusValueList.size());
	}
}
