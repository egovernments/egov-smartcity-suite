package org.egov.ptis.domain.dao.property;

import java.util.List;

import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.ptis.domain.entity.property.StructureClassification;

/**
 * Structure Classification DAO Test
 * 
 * @author Srikanth
 **/
public class StructureClassificationHibernateDAOTest extends EgovHibernateTest {

	private StructureClassificationDAO structureClassificationDAO;

	public void setUp() throws Exception {
		structureClassificationDAO = PropertyDAOFactory.getDAOFactory()
				.getStructureClassificationDAO();
	}

	public void tearDown() throws Exception {
		structureClassificationDAO = null;
	}

	public final void testGetAllStructuralClassification() {
		List<StructureClassification> strucClzzList = structureClassificationDAO
				.getAllStructureClassification();
		assertNotNull(strucClzzList);
	}

}
