package org.egov.ptis.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.utils.EgovInfrastrUtil;
import org.egov.commons.utils.EgovInfrastrUtilInteface;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.DepreciationMasterDao;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Address;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.ptis.domain.dao.property.CategoryDao;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyOccupationDAO;
import org.egov.ptis.domain.dao.property.PropertySourceDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusDAO;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyUsageDAO;
import org.egov.ptis.domain.dao.property.StructureClassificationDAO;
import org.egov.ptis.domain.dao.property.TaxPercDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.ConstructionTypeImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyCreationReason;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author deepak YN
 * 
 */

public class PTISCacheManager implements PTISCacheManagerInteface {

	private static final Logger LOGGER = Logger
			.getLogger(PTISCacheManager.class);
	private static boolean reset = true;
	private static ArrayList propertyCreationReasonsList = new ArrayList();
	private static ArrayList structuralFactorslist = new ArrayList();
	private static ArrayList constructionTypeslist = new ArrayList();
	private static ArrayList allPropUsagelist = new ArrayList();
	private static ArrayList allPropOccTypeslist = new ArrayList();
	private static ArrayList allPropertyStatuslist = new ArrayList();
	private static ArrayList allPropertySourcelist = new ArrayList();
	private static ArrayList allAllTaxRatelist = new ArrayList();
	private static ArrayList allCategorieslist = new ArrayList();
	private static ArrayList allDepreciationRates = new ArrayList();
	private static HashMap allPropertySourceMap = new HashMap();
	private static HashMap allPropertyStatusMap = new HashMap();
	private static HashMap allPropUsageMap = new HashMap();
	private static HashMap allPropOccTypesMap = new HashMap();
	private static HashMap structuralFactorsMap = new HashMap();
	private static HashMap constructionTypesMap = new HashMap();
	private static HashMap propertyCreationReasonsMap = new HashMap();
	private static HashMap boundaryMap = new HashMap();
	public String cityName = EGovConfig.getProperty("CITY", "", "PT");
	public String block = EGovConfig.getProperty("ptis_egov_config.xml",
			"BNDRYTYPNAME3", "", "PT");
	public String street = EGovConfig.getProperty("ptis_egov_config.xml",
			"BNDRYTYPNAME4", "", "PT");
	@Autowired
	private static BoundaryService boundaryService;

	@Override
	public List getAllDepreciationRates() {
		if (reset) {
			update();
		}
		return allDepreciationRates;
	}

	@Override
	public List getPropertyCreationReasons() {
		if (reset) {
			update();
		}
		return propertyCreationReasonsList;
	}

	@Override
	public List getAllTaxRates() {
		if (reset) {
			update();
		}
		return allAllTaxRatelist;
	}

	@Override
	public List getAllStructuralFactors() {
		if (reset) {
			update();
		}
		return structuralFactorslist;
	}

	@Override
	public List getAllCategories() {
		if (reset) {
			update();
		}
		return allCategorieslist;
	}

	@Override
	public List getAllConstructionTypes() {
		if (reset) {
			update();
		}
		return constructionTypeslist;
	}

	@Override
	public List getAllPropUsage() {
		if (reset) {
			update();
		}
		return allPropUsagelist;
	}

	@Override
	public List getAllPropOccTypes() {
		if (reset) {
			update();
		}
		return allPropOccTypeslist;
	}

	@Override
	public List getAllPropertyStatus() {
		if (reset) {
			update();
		}
		return allPropertyStatuslist;
	}

	@Override
	public List getAllPropertySource() {
		if (reset) {
			update();
		}
		return allPropertySourcelist;
	}

	@Override
	public PropertySource getPropertySourceById(Integer propSrcId) {
		if (reset) {
			update();
		}
		return (PropertySource) allPropertySourceMap.get(propSrcId);
	}

	@Override
	public PropertyStatus getPropertyStatusById(Integer propStatusId) {
		if (reset) {
			update();
		}
		return (PropertyStatus) allPropertyStatusMap.get(propStatusId);
	}

	@Override
	public PropertyUsage getPropertyUsageById(Integer propUsageId) {
		if (reset) {
			update();
		}
		return (PropertyUsage) allPropUsageMap.get(propUsageId);
	}

	@Override
	public PropertyOccupation getPropertyOccupationById(Integer propOccId) {
		if (reset) {
			update();
		}
		return (PropertyOccupation) allPropOccTypesMap.get(propOccId);
	}

	@Override
	public StructureClassification getStructureClassificationById(
			Integer strucClssfnId) {
		if (reset) {
			update();
		}
		return (StructureClassification) structuralFactorsMap
				.get(strucClssfnId);
	}

	@Override
	public ConstructionTypeImpl getConstructionTypeById(Integer id) {
		if (reset) {
			update();
		}
		return (ConstructionTypeImpl) constructionTypesMap.get(id);
	}

	@Override
	public PropertyCreationReason getReasonById(Integer id) {
		if (reset) {
			update();
		}
		return (PropertyCreationReason) propertyCreationReasonsMap.get(id);
	}

	@Override
	public Boundary getBoundary(Integer id) {
		if (reset) {
			update();
		}
		return (Boundary) boundaryMap.get(id);
	}

	private static synchronized void update() {
		LOGGER.debug("Starting update in PTISCacheManager util.........."
				+ reset);
		String cessLibId = EGovConfig.getProperty("ptis_egov_config.xml",
				"LIB_CESSID", "", "PT");
		LOGGER.info("cessLibId" + cessLibId);
		String cessBegId = EGovConfig.getProperty("ptis_egov_config.xml",
				"BEG_CESSID", "", "PT");
		LOGGER.info("cessBegId" + cessBegId);
		String cessHelthId = EGovConfig.getProperty("ptis_egov_config.xml",
				"HLTH_CESSID", "", "PT");
		LOGGER.info("cessHelthId" + cessHelthId);
		String totalCessId = EGovConfig.getProperty("ptis_egov_config.xml",
				"TOTAL_CESSID", "", "PT");
		LOGGER.info("totalCessId" + totalCessId);
		EgovInfrastrUtilInteface inteface = new EgovInfrastrUtil();
		propertyCreationReasonsList = new ArrayList();
		structuralFactorslist = new ArrayList();
		constructionTypeslist = new ArrayList();
		allPropUsagelist = new ArrayList();
		allPropOccTypeslist = new ArrayList();
		allPropertyStatuslist = new ArrayList();
		allPropertySourcelist = new ArrayList();
		allPropertySourceMap = new HashMap();
		allPropertyStatusMap = new HashMap();
		allPropUsageMap = new HashMap();
		allPropOccTypesMap = new HashMap();
		structuralFactorsMap = new HashMap();
		constructionTypesMap = new HashMap();
		propertyCreationReasonsMap = new HashMap();
		boundaryMap = new HashMap();
		allAllTaxRatelist = new ArrayList();
		allCategorieslist = new ArrayList();
		allDepreciationRates = new ArrayList();
		try {
			PropertyTypeMasterDAO propTypeMstrDao = PropertyDAOFactory
					.getDAOFactory().getPropertyTypeMasterDAO();
			PropertyUsageDAO propertyUsageDAO = PropertyDAOFactory
					.getDAOFactory().getPropertyUsageDAO();
			StructureClassificationDAO strucClsfnDao = PropertyDAOFactory
					.getDAOFactory().getStructureClassificationDAO();
			PropertyMutationMasterDAO propMutMstrDao = PropertyDAOFactory
					.getDAOFactory().getPropertyMutationMstrDAO();
			PropertySourceDAO propSrcDao = PropertyDAOFactory.getDAOFactory()
					.getPropertySourceDAO();
			PropertyStatusDAO propStatusDao = PropertyDAOFactory
					.getDAOFactory().getPropertyStatusDAO();
			PropertyUsageDAO propUsageDAO = PropertyDAOFactory.getDAOFactory()
					.getPropertyUsageDAO();
			PropertyOccupationDAO propOccDAO = PropertyDAOFactory
					.getDAOFactory().getPropertyOccupationDAO();
			TaxPercDAO taxPercDAO = PropertyDAOFactory.getDAOFactory()
					.getTaxPercDao();
			CategoryDao categoryDao = PropertyDAOFactory.getDAOFactory()
					.getCategoryDao();

			DepreciationMasterDao deprMstrDao = DCBDaoFactory.getDaoFactory()
					.getDepreciationMasterDao();

			allPropertySourcelist = (ArrayList) propSrcDao.findAll();
			Iterator allPropertySourcelistIter = allPropertySourcelist
					.iterator();
			while (allPropertySourcelistIter.hasNext()) {
				PropertySource obj = (PropertySource) allPropertySourcelistIter
						.next();
				PropertySource propertySource = (PropertySource) propSrcDao
						.findById(obj.getID(), false);
				allPropertySourceMap.put(obj.getID(), propertySource);
			}
			allPropertyStatuslist = (ArrayList) propStatusDao.findAll();
			Iterator allPropertyStatuslistIter = allPropertyStatuslist
					.iterator();
			while (allPropertyStatuslistIter.hasNext()) {
				PropertyStatus obj = (PropertyStatus) allPropertyStatuslistIter
						.next();
				PropertyStatus propertyStatus = (PropertyStatus) propStatusDao
						.findById(obj.getID(), false);
				allPropertyStatusMap.put(obj.getID(), propertyStatus);
			}
			allPropUsagelist = (ArrayList) propUsageDAO.findAll();
			Iterator allPropUsagelistIter = allPropUsagelist.iterator();
			while (allPropUsagelistIter.hasNext()) {
				PropertyUsage obj = (PropertyUsage) allPropUsagelistIter.next();
				PropertyUsage propertyUsage = (PropertyUsage) propUsageDAO
						.findById(obj.getId(), false);
				allPropUsageMap.put(obj.getId(), propertyUsage);
			}
			allPropOccTypeslist = (ArrayList) propOccDAO.findAll();
			Iterator allPropOccTypeslistIter = allPropOccTypeslist.iterator();
			while (allPropOccTypeslistIter.hasNext()) {
				PropertyOccupation obj = (PropertyOccupation) allPropOccTypeslistIter
						.next();
				PropertyOccupation propertyOccupation = (PropertyOccupation) propOccDAO
						.findById(obj.getId(), false);
				allPropOccTypesMap.put(obj.getId(), propertyOccupation);
			}
			structuralFactorslist = (ArrayList) strucClsfnDao.findAll();
			Iterator structuralFactorslistIter = structuralFactorslist
					.iterator();
			while (structuralFactorslistIter.hasNext()) {
				StructureClassification obj = (StructureClassification) structuralFactorslistIter
						.next();
				StructureClassification structureClassification = (StructureClassification) strucClsfnDao
						.findById(obj.getId(), false);
				structuralFactorsMap.put(obj.getId(), structureClassification);
			}
			/*
			 * constructionTypeslist =
			 * (ArrayList)getPropertyManager().getAllConstructionTypes();
			 * Iterator constructionTypesIter =constructionTypeslist.iterator();
			 * while(constructionTypesIter.hasNext()) { ConstructionTypeImpl obj
			 * = (ConstructionTypeImpl)constructionTypesIter.next();
			 * ConstructionTypeImpl constructionTypeImpl =
			 * getPropertyManager().getConstructionTypeById(obj.getID());
			 * constructionTypesMap.put(obj.getID(),constructionTypeImpl); }
			 */
			List<Boundary> boundaries = boundaryService.getAllBoundaries();
			for (Boundary boundary : boundaries) {
				boundaryMap.put(boundary.getId(), boundary);
			}

			allAllTaxRatelist = (ArrayList) taxPercDAO.findAll();
			allCategorieslist = (ArrayList) categoryDao.findAll();
			allDepreciationRates = (ArrayList) deprMstrDao.findAll();

		} catch (Exception sqe) {
			LOGGER.info("Exception in update()-----PTISCacheManager----"
					+ sqe.getMessage());
			throw new EGOVRuntimeException(sqe.getMessage());
		}
		reset = false;
	}

	/*
	 * buiding property address from basic property's old municipal no and
	 * basicProperty's address object(i.e streetaddress1,citytownvillage,pincode
	 * from db columns
	 */
	@Override
	public String buildAddress(BasicProperty basicProperty) {
		if (basicProperty == null) {
			throw new EGOVRuntimeException(
					"Internal Server Error  BasicProperty is Null!!");
		}
		Address address = basicProperty.getAddress();
		String addressStr = "";
		if (address == null) {
			throw new EGOVRuntimeException(
					"Internal Server Error in Searching Property Address is Null!!");
		}
		// logger.info("address id"+address.getAddressID());
		if (basicProperty.getOldMuncipalNum() != null) {
			addressStr = "(Old No. " + basicProperty.getOldMuncipalNum() + " )";
		}

		// logger.info("::::::::addressStr "+addressStr);
		return buildAddressFromAddress(address);

	}

	/*
	 * this method used to build owner name by taking ownerset as parameter.if
	 * owner firstname exists then bulid ownername else take firnamelocal and
	 * build ownername
	 */
	@Override
	public String buildOwnerFullName(Set<PropertyOwner> ownerSet) {
		if (ownerSet == null) {
			throw new EGOVRuntimeException("Property Owner set is null...");
		}
		String ownerFullName = "";
		Set<String> ownerNameSet = new HashSet<String>();
		for (PropertyOwner propOwner : ownerSet) {

			// TODO -- Commented to fix Phoenix Compilation issues
			/*
			 * LOGGER.debug("buildOwnerFullName : Owner id " +
			 * propOwner.getCitizenID()); if (propOwner.getFirstName() != null
			 * && !propOwner.getFirstName().trim().equals("")) { if
			 * (!ownerNameSet.contains(propOwner.getFirstName().trim())) { if
			 * (!ownerFullName.trim().equals("")) { if
			 * (!ownerFullName.equals("")) { ownerFullName += ", "; } }
			 * ownerNameSet.add(propOwner.getFirstName().trim()); ownerFullName
			 * += (propOwner.getFirstName() == null ? "" :
			 * propOwner.getFirstName()) + (propOwner.getMiddleName() == null ?
			 * "" : " " + propOwner.getMiddleName()) + (propOwner.getLastName()
			 * == null ? "" : " " + propOwner.getLastName());
			 * LOGGER.debug("buildOwnerFullName : ownerFullNameEnglish : " +
			 * ownerFullName); } }
			 */
		}
		LOGGER.debug("buildOwnerFullName : ownerFullName" + ownerFullName);
		return ownerFullName;
	}

	/*
	 * getTxPercWithUsg will take lstTaxRates as parameter and returns Map
	 * (key-Integer,business meaning id of TaxPerc, value-String ,business
	 * meaning taxperc-usg)
	 */
	@Override
	public Map getTxPercWithUsg(List lstTaxRates) {

		Iterator ItrlstTaxRates = lstTaxRates.iterator();
		Map taxRatesMap = new HashMap();
		while (ItrlstTaxRates.hasNext()) {
			org.egov.ptis.domain.entity.property.TaxPerc txPerc = (org.egov.ptis.domain.entity.property.TaxPerc) ItrlstTaxRates
					.next();
			if (txPerc != null) {
				txPerc.getTax_perc();
				txPerc.getPropertyUsage().getUsageName();
				String usgwithTx = txPerc.getTax_perc().toString() + "-"
						+ txPerc.getPropertyUsage().getUsageName();
				LOGGER.info("usgwithTx " + usgwithTx);
				taxRatesMap.put(txPerc.getId(), usgwithTx);

			}
		}
		return taxRatesMap;
	}

	/*
	 * buildOwnerFullName this API returns String of ownerName by passing
	 * BasicProperty as parameter and get all properties owners and add all
	 * those to a set and calls overloaded method buildOwnerFullName(ownerSet);
	 */
	@Override
	public String buildOwnerFullName(BasicProperty bp) {
		Property property = bp.getProperty();
		return buildOwnerFullName(property.getPropertyOwnerSet());
	}

	@Override
	public String buildAddressFromAddress(Address address) {
		String addressStr = "";
		if (address != null) {
			addressStr = (address.getHouseNoBldgApt() == null ? " " : address
					.getHouseNoBldgApt());
			if (!addressStr.trim().equals("")) {
				addressStr = addressStr
						+ (address.getStreetRoadLine() == null ? " " : ", "
								+ address.getStreetRoadLine());
			} else {
				addressStr = (address.getStreetRoadLine() == null ? " "
						: address.getStreetRoadLine());
			}

			if (!addressStr.trim().equals("")) {
				addressStr = addressStr
						+ (address.getAreaLocalitySector() == null ? "" : ", "
								+ address.getAreaLocalitySector());
				addressStr = addressStr
						+ (address.getCityTownVillage() == null ? "" : ", "
								+ address.getCityTownVillage());
				addressStr = addressStr
						+ (address.getPinCode() == null ? "" : ", "
								+ address.getPinCode().toString());
			} else {
				addressStr = addressStr
						+ (address.getAreaLocalitySector() == null ? "" : ", "
								+ address.getAreaLocalitySector());
				addressStr = (address.getCityTownVillage() == null ? ""
						: address.getCityTownVillage());

				if (!addressStr.trim().equals("")) {
					addressStr = addressStr
							+ (address.getPinCode() == null ? "" : ", "
									+ address.getPinCode().toString());
				} else {
					addressStr = addressStr
							+ (address.getPinCode() == null ? "" : address
									.getPinCode().toString());
				}
			}
		}
		return addressStr;

	}

	// changes done by Evlyn for Proper Property Address Format
	/**
	 * houseno/sunno(old door num), street1, street2, locality, block - pincode
	 */
	@Override
	public String buildAddressByImplemetation(Address address) {
		String impName = EGovConfig.getProperty("ptis_egov_config.xml",
				"IMPLEMENTATION_NAME", "", "PT");
		if (impName == null || impName.trim().equals("")) {
			throw new EGOVRuntimeException("Implementation Name is null");
		}

		String addressStr = "";
		if (address != null) {
			if (impName.equals("CHENNAI")) {
				addressStr = (address.getHouseNoBldgApt() == null ? " "
						: address.getHouseNoBldgApt());
				CharSequence cs = "PropertyAddress";
				if (address.getClass().getName().contains(cs)) {
					PropertyAddress propAddr = (PropertyAddress) address;
					if (propAddr.getSubNumber() != null
							&& !propAddr.getSubNumber().equals("")) {
						addressStr = addressStr + "/" + propAddr.getSubNumber();
					}
					if (propAddr.getDoorNumOld() != null
							&& !propAddr.getDoorNumOld().equals("")) {
						addressStr = addressStr + "("
								+ propAddr.getDoorNumOld() + ")";
					}
				}
				if (!addressStr.trim().equals("")) {
					addressStr = addressStr
							+ (address.getStreetRoadLine() == null ? " " : ", "
									+ address.getStreetRoadLine());
				} else {
					addressStr = (address.getStreetRoadLine() == null ? " "
							: address.getStreetRoadLine());
				}

				addressStr = addressStr
						+ (address.getAreaLocalitySector() == null ? "" : ", "
								+ address.getAreaLocalitySector());
				addressStr = addressStr
						+ (address.getCityTownVillage() == null ? "" : ", "
								+ address.getCityTownVillage());
				addressStr = addressStr
						+ (address.getPinCode() == null ? "" : " -  "
								+ address.getPinCode().toString());
			}
		}
		if (address != null) {
			if (impName.equals("NAGPUR")) {
				addressStr = (address.getHouseNoBldgApt() == null ? " "
						: address.getHouseNoBldgApt());
				CharSequence cs = "PropertyAddress";
				if (address.getClass().getName().contains(cs)) {
					PropertyAddress propAddr = (PropertyAddress) address;
					if (propAddr.getSubNumber() != null
							&& !propAddr.getSubNumber().equals("")) {
						addressStr = addressStr + "/" + propAddr.getSubNumber();
					}
					if (propAddr.getDoorNumOld() != null
							&& !propAddr.getDoorNumOld().equals("")) {
						addressStr = addressStr + "("
								+ propAddr.getDoorNumOld() + ")";
					}
				}
				if (!addressStr.trim().equals("")) {
					addressStr = addressStr
							+ (StringUtils.isBlank(address.getStreetRoadLine()) ? ""
									: ", " + address.getStreetRoadLine());
				} else {
					addressStr = (StringUtils.isBlank(address
							.getStreetRoadLine()) ? "" : address
							.getStreetRoadLine());
				}

				if (address.getClass().getName().contains(cs)) {
					PropertyAddress propAddr = (PropertyAddress) address;
					addressStr = addressStr
							+ (StringUtils.isBlank(propAddr.getExtraField1()) ? ""
									: ", " + propAddr.getExtraField1());

					addressStr = addressStr
							+ (StringUtils.isBlank(propAddr.getExtraField2()) ? ""
									: ", " + propAddr.getExtraField2());

					addressStr = addressStr
							+ (StringUtils.isBlank(propAddr.getExtraField3()) ? ""
									: ", " + propAddr.getExtraField3());

					addressStr = addressStr
							+ (StringUtils.isBlank(propAddr.getExtraField4()) ? ""
									: ", " + propAddr.getExtraField4());
				}

				addressStr = addressStr
						+ (address.getPinCode() == null ? "" : " -  "
								+ address.getPinCode().toString());
			}
		}

		return addressStr;
	}

}
