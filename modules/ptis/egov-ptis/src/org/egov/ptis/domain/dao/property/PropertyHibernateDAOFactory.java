package org.egov.ptis.domain.dao.property;

import org.apache.log4j.Logger;
import org.egov.demand.dao.EgDemandDao;
import org.egov.demand.dao.EgDemandDetailsDao;
import org.egov.demand.dao.EgDemandDetailsHibDao;
import org.egov.demand.dao.EgDemandHibernateDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.demand.PtDemandHibernateDao;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.ConstructionTypeImpl;
import org.egov.ptis.domain.entity.property.ConstructionTypeSet;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.domain.entity.property.PropertyCreationReason;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyIntegration;
import org.egov.ptis.domain.entity.property.PropertyModifyReason;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyReference;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.TaxPerc;
import org.egov.ptis.domain.entity.property.TaxRates;
import org.hibernate.Session;

/**
 * Returns Hibernate-specific instances of DAOs.
 * <p>
 * One of the responsiblities of the factory is to inject a Hibernate Session
 * into the DAOs. You can customize the getCurrentSession() method if you are
 * not using the default strategy, which simply delegates to
 * <tt>HibernateUtil.getCurrentSession()</tt>, and also starts a transaction
 * lazily, if none exists already for the current thread or current EJB.
 * <p>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * an inner class to implement the interface in a generic way. This allows clean
 * refactoring later on, should the interface implement business data access
 * methods at some later time. Then, we would externalize the implementation
 * into its own first-level class. We can't use anonymous inner classes for this
 * trick because they can't extend or implement an interface and they can't
 * include constructors.
 * 
 * @author christian.bauer@jboss.com
 */
public class PropertyHibernateDAOFactory extends PropertyDAOFactory {
	protected Session getCurrentSession() {
		// Get a Session and begin a database transaction. If the current
		// thread/EJB already has an open Sessio n and an ongoing Transaction,
		// this is a no-op and only returns a reference to the current Session.
		HibernateUtil.beginTransaction();
		return HibernateUtil.getCurrentSession();
	}

	public BasicPropertyDAO getBasicPropertyDAO() {
		return new BasicPropertyHibernateDAO(BasicPropertyImpl.class, getCurrentSession());
	}

	public ConstructionTypeSetDAO getConstructionTypeSetDAO() {
		return new ConstructionTypeSetHibernateDAO(ConstructionTypeSet.class, getCurrentSession());
	}

	public ConstructionTypeDAO getConstructionTypeDAO() {
		return new ConstructionTypeHibernateDAO(ConstructionTypeImpl.class, getCurrentSession());
	}

	public FloorDAO getFloorDAO() {
		return new FloorHibernateDAO(FloorImpl.class, getCurrentSession());
	}

	public PropertyDAO getPropertyDAO() {
		return new PropertyHibernateDAO(PropertyImpl.class, getCurrentSession());
	}

	public PropertyDetailDAO getPropertyDetailDAO() {
		return new PropertyDetailHibernateDAO(PropertyDetail.class, getCurrentSession());
	}

	public PropertyOccupationDAO getPropertyOccupationDAO() {
		return new PropertyOccupationHibernateDAO(PropertyOccupation.class, getCurrentSession());
	}

	public PropertySourceDAO getPropertySourceDAO() {
		return new PropertySourceHibernateDAO(PropertySource.class, getCurrentSession());
	}

	public PropertyStatusDAO getPropertyStatusDAO() {
		return new PropertyStatusHibernateDAO(PropertyStatus.class, getCurrentSession());
	}

	public PropertyUsageDAO getPropertyUsageDAO() {
		return new PropertyUsageHibernateDAO(PropertyUsage.class, getCurrentSession());
	}

	public StructureClassificationDAO getStructureClassificationDAO() {
		return new StructureClassificationHibernateDAO(StructureClassification.class,
				getCurrentSession());
	}

	public FloorDAO getFloorImplDAO() {
		return new FloorHibernateDAO(FloorImpl.class, getCurrentSession());
	}

	public PropertyCreationReasonsDAO getPropertyCreationReasonsDAO() {
		return new PropertyCreationReasonsHibernateDAO(PropertyCreationReason.class,
				getCurrentSession());
	}

	public PropertyIDDAO getPropertyIDDAO() {
		return new PropertyIDHibernateDAO(PropertyID.class, getCurrentSession());
	}

	public PropertyTypeMasterDAO getPropertyTypeMasterDAO() {
		return new PropertyTypeMasterHibernateDAO(PropertyTypeMaster.class, getCurrentSession());
	}

	public CategoryDao getCategoryDao() {
		return new CategoryHibDao(Category.class, getCurrentSession());
	}

	public BoundaryCategoryDao getBoundaryCategoryDao() {
		return new BoundaryCategoryHibDao(BoundaryCategory.class, getCurrentSession());
	}

	public TaxPercDAO getTaxPercDao() {
		return new TaxPercHibernateDAO(TaxPerc.class, getCurrentSession());
	}

	public PropertyModifyReasonsDAO getPropertyModifyReasonsDAO() {
		return new PropertyModifyReasonsHibernateDAO(PropertyModifyReason.class,
				getCurrentSession());
	}

	public PropertyMutationMasterDAO getPropertyMutationMstrDAO() {
		return new PropertyMutationMasterHibDAO(PropertyMutationMaster.class, getCurrentSession());
	}

	public PropertyReferenceHibDAO getPropertyReferenceDAO() {
		return new PropertyReferenceHibDAO(PropertyReference.class, getCurrentSession());
	}

	public TaxPercentageforDatesDAO getTaxPercentageforDatesDAO() {
		return new TaxPercentageforDatesHibernateDAO(TaxRates.class, getCurrentSession());
	}

	public PropertyMutationDAO getPropertyMutationDAO() {

		return new PropertyMutationHibDAO(PropertyMutation.class, getCurrentSession());
	}

	public PtDemandDao getPtDemandDao() {
		return new PtDemandHibernateDao(EgDemand.class, getCurrentSession());
	}

	public PropertyStatusValuesDAO getPropertyStatusValuesDAO() {
		return new PropertyStatusValuesHibernateDAO(PropertyStatusValues.class, getCurrentSession());
	}

	public PropertyIntegrationDAO getPropertyIntegrationDAO() {
		return new PropertyIntegrationHibDAO(PropertyIntegration.class, getCurrentSession());
	}

	public EgDemandDao getEgDemandDao() {
		return new EgDemandHibernateDao(EgDemand.class, getCurrentSession());
	}

	public EgDemandDetailsDao getEgDemandDetailsDao() {
		return new EgDemandDetailsHibDao(EgDemandDetails.class, getCurrentSession());
	}
	
}
