package org.egov.ptis.domain.dao.property;

//TODO -- Uncomment this once demand code is available
/*import org.egov.demand.dao.EgDemandDao;
import org.egov.demand.dao.EgDemandDetailsDao;*/
import org.egov.ptis.domain.dao.demand.PtDemandDao;

/**
 * Defines all DAOs and the concrete factories to get the conrecte DAOs.
 * <p>
 * Either use the <tt>DEFAULT</tt> to get the same concrete RNDDAOFactory
 * throughout your application, or a concrete factory by name, e.g.
 * <tt>RNDDAOFactory.HIBERNATE</tt> is a concrete
 * <tt>RNDHibernateDAOFactory</tt>.
 * <p>
 * Implementation: If you write a new DAO, this class has to know about it. If
 * you add a new persistence mechanism, add an additional concrete factory for
 * it to the enumeration of factories.
 * <p>
 * It probably wouldn't be a bad idea to move the <tt>DEFAULT</tt> setting into
 * external configuration.
 * 
 * @author christian.bauer@jboss.com
 */
public abstract class PropertyDAOFactory {

	private static final PropertyDAOFactory HIBERNATE = new PropertyHibernateDAOFactory();

	public static PropertyDAOFactory getDAOFactory() {
		return HIBERNATE;
	}

	public abstract BasicPropertyDAO getBasicPropertyDAO();

	public abstract ConstructionTypeSetDAO getConstructionTypeSetDAO();

	public abstract ConstructionTypeDAO getConstructionTypeDAO();

	public abstract FloorDAO getFloorDAO();

	public abstract PropertyDAO getPropertyDAO();

	public abstract PropertyDetailDAO getPropertyDetailDAO();

	public abstract PropertyOccupationDAO getPropertyOccupationDAO();

	public abstract PropertySourceDAO getPropertySourceDAO();

	public abstract PropertyStatusDAO getPropertyStatusDAO();

	public abstract PropertyUsageDAO getPropertyUsageDAO();

	public abstract StructureClassificationDAO getStructureClassificationDAO();

	public abstract FloorDAO getFloorImplDAO();

	public abstract PropertyCreationReasonsDAO getPropertyCreationReasonsDAO();

	public abstract PropertyIDDAO getPropertyIDDAO();

	public abstract PropertyTypeMasterDAO getPropertyTypeMasterDAO();

	public abstract CategoryDao getCategoryDao();

	public abstract BoundaryCategoryDao getBoundaryCategoryDao();

	public abstract TaxPercDAO getTaxPercDao();

	public abstract PropertyModifyReasonsDAO getPropertyModifyReasonsDAO();

	public abstract TaxPercentageforDatesDAO getTaxPercentageforDatesDAO();

	public abstract PropertyMutationMasterDAO getPropertyMutationMstrDAO();

	public abstract PropertyReferenceDAO getPropertyReferenceDAO();

	public abstract PropertyMutationDAO getPropertyMutationDAO();

	public abstract PtDemandDao getPtDemandDao();

	public abstract PropertyStatusValuesDAO getPropertyStatusValuesDAO();

	public abstract PropertyIntegrationDAO getPropertyIntegrationDAO();

	//TODO -- Uncomment this once demand code is available
	/*public abstract EgDemandDao getEgDemandDao();

	public abstract EgDemandDetailsDao getEgDemandDetailsDao();*/
}
