package org.egov.ptis.nmc.model;

import static org.egov.ptis.constants.PropertyTaxConstants.PROP_ADDR_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROP_SOURCE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.egov.commons.Area;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.demand.model.DepreciationMaster;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.address.model.AddressTypeMaster;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryImpl;
import org.egov.infra.admin.master.entity.BoundaryTypeImpl;
import org.egov.lib.admbndry.HeirarchyTypeImpl;
import org.egov.infra.admin.master.entity.UserImpl;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.notice.PtNotice;
import org.hibernate.Session;

public class NMCObjectFactory {

	private final Session session;
	private PersistenceService service;

	public NMCObjectFactory() {
		this.session = null;
	}

	public NMCObjectFactory(Session session) {
		this.session = session;
	}

	public NMCObjectFactory(Session session, PersistenceService service) {
		this.session = session;
		this.service = service;
	}

	public List<String> createApplicableTaxesResidential() {
		List<String> applicableTaxes = new ArrayList<String>();
		applicableTaxes.add("GENERALTAX");
		applicableTaxes.add("SEWERAGETAX");
		applicableTaxes.add("LIGHTINGTAX");
		applicableTaxes.add("FIRESERVICETAX");
		applicableTaxes.add("GENERALWATERTAX");
		applicableTaxes.add("EC_RESIDENTIAL");
		return applicableTaxes;
	}

	public List<String> createApplicableTaxesNonResidential() {
		List<String> applicableTaxes = new ArrayList<String>();
		applicableTaxes.add("GENERALTAX");
		applicableTaxes.add("SEWERAGETAX");
		applicableTaxes.add("LIGHTINGTAX");
		applicableTaxes.add("FIRESERVICETAX");
		applicableTaxes.add("GENERALWATERTAX");
		applicableTaxes.add("EC_N-RESIDENTIAL");
		applicableTaxes.add("EGCESS");
		return applicableTaxes;
	}

	public List<String> createApplicableTaxesOpenPlot() {
		List<String> applicableTaxes = new ArrayList<String>();
		applicableTaxes.add("GENERALTAX");
		applicableTaxes.add("SEWERAGETAX");
		applicableTaxes.add("LIGHTINGTAX");
		applicableTaxes.add("FIRESERVICETAX");
		applicableTaxes.add("EC_OPENPLOT");
		return applicableTaxes;
	}

	public Property createProperty(String propertyType, Boundary boundary, String occupancyCode, String occupany,
			String occupancyIndex, String structureIndex, String usageIndex, String usageCode, String usageName,
			DepreciationMaster depreciationMaster, String propertyArea, Date installmentStartDate,
			Date installmentEndDate) {
		Property property = new PropertyImpl();

		Installment installment = (Installment) service.find(
				"from Installment I where I.module.moduleName=? and (I.fromDate <= ? and I.toDate >= ?) ",
				PTMODULENAME, installmentStartDate, installmentEndDate);
		PropertySource propertySource = (PropertySource) service.find("from PropertySource where propSrcCode = ?",
				PROP_SOURCE);

		property.setCreatedDate(new Date());
		property.setIsChecked('Y');
		property.setBasicProperty(createBasicProperty(property, boundary));

		property.setStatus('N');
		property.setIsDefaultProperty('Y');
		property.setInstallment(installment);
		property.setEffectiveDate(installment.getFromDate());
		property.setPropertySource(propertySource);
		property.setPropertyDetail(createPropertyDetail(propertyType, property, boundary, occupancyCode, occupany,
				occupancyIndex, structureIndex, usageIndex, usageCode, usageName, depreciationMaster, propertyArea,
				installmentStartDate, installmentEndDate));
		return property;
	}

	public PropertyDetail createPropertyDetail(String propertyType, Property property, Boundary boundary,
			String occupancyCode, String occupancy, String occupancyIndex, String structureIndex, String usageIndex,
			String usageCode, String usageName, DepreciationMaster depreciationMaster, String propertyArea,
			Date installmentStartDate, Date installmentEndDate) {

		PropertyDetail propDetail = new BuiltUpProperty();
		propDetail.setProperty(property);
		propDetail.setIsChecked('Y');
		propDetail.setPropertyType("testPropertyType");
		PropertyMutationMaster propMutMstr = (PropertyMutationMaster) service.find(
				"from PropertyMutationMaster PM where upper(PM.code) = ?", "NEW");
		propDetail.setPropertyMutationMaster(propMutMstr);
		propDetail.setUpdatedTime(new Date());
		propDetail.setSitalArea(createArea(propertyArea));
		propDetail.setPropertyTypeMaster(createPropertyType(propertyType));
		if (propertyType.equals(PROPTYPE_RESD) && occupancyCode.equals("SELFOCC")) {
			propDetail.addFloor(createFloor("0", "0", "61.2", boundary, occupancyCode, occupancy, occupancyIndex, null,
					structureIndex, usageIndex, usageCode, usageName + "1", depreciationMaster, installmentStartDate,
					installmentEndDate));
			propDetail.addFloor(createFloor("0", "1", "55.25", boundary, occupancyCode, occupancy, occupancyIndex,
					null, structureIndex, usageIndex, usageCode, usageName + "2", depreciationMaster,
					installmentStartDate, installmentEndDate));
			propDetail.addFloor(createFloor("0", "2", "46.75", boundary, occupancyCode, occupancy, occupancyIndex,
					null, structureIndex, usageIndex, usageCode, usageName + "3", depreciationMaster,
					installmentStartDate, installmentEndDate));
			propDetail.addFloor(createFloor("0", "3", "42.5", boundary, occupancyCode, occupancy, occupancyIndex, null,
					structureIndex, usageIndex, usageCode, usageName + "4", depreciationMaster, installmentStartDate,
					installmentEndDate));
		} else if (propertyType.equals(PROPTYPE_RESD) && occupancyCode.equals("TENANT")) {
			propDetail.addFloor(createFloor("0", "0", "76.5", boundary, occupancyCode, occupancy, occupancyIndex, null,
					structureIndex, usageIndex, usageCode, usageName, depreciationMaster, installmentStartDate,
					installmentEndDate));
		} else if (propertyType.equals(NMCPTISConstants.PROPTYPE_NON_RESD) && occupancyCode.equals("SELFOCC")) {
			propDetail.addFloor(createFloor("0", "0", "183.6", boundary, occupancyCode, occupancy, occupancyIndex,
					"91.44", structureIndex, usageIndex, usageCode, usageName, depreciationMaster,
					installmentStartDate, installmentEndDate));
		} else if (propertyType.equals(NMCPTISConstants.PROPTYPE_OPEN_PLOT)) {
			PropertyUsage propertyUsage = createPropertyUsage(usageCode, usageName, usageIndex);
			PropertyOccupation propertyOccupation = createPropertyOccupation(occupancyCode, occupancy, occupancyIndex);
			propDetail.setPropertyUsage(propertyUsage);
			propDetail.setPropertyOccupation(propertyOccupation);
		}

		return propDetail;
	}

	public PropertyTypeMaster createPropertyType(String propertyType) {
		UserImpl userImpl = createUser("test");
		PropertyTypeMaster propertyTypeMaster = new PropertyTypeMaster();
		propertyTypeMaster.setCode(propertyType);
		propertyTypeMaster.setCreatedBy(userImpl);
		propertyTypeMaster.setCreatedDate(new Date());
		propertyTypeMaster.setFactor(Float.valueOf("1"));
		propertyTypeMaster.setModifiedBy(userImpl);
		propertyTypeMaster.setModifiedDate(new Date());
		propertyTypeMaster.setType(propertyType);
		return propertyTypeMaster;
	}

	public Area createArea(String areaString) {
		Area area = new Area();
		area.setArea(Float.valueOf(areaString));

		return area;
	}

	public BasicProperty createBasicProperty(Property property, Boundary boundary) {
		BasicProperty basicProperty = new BasicPropertyImpl();

		PropertyStatus propStatus = (PropertyStatus) service.find("from PropertyStatus where statusCode=?", "ASSESSED");

		basicProperty.setActive(Boolean.TRUE);
		basicProperty.setGisReferenceNo("1243563");
		basicProperty.setAddress(createPropertyAddress());
		basicProperty.setPropertyID(createPropertyID(basicProperty, boundary));
		basicProperty.setUpicNo("223223");
		basicProperty.setStatus(propStatus);
		basicProperty.addPropertyStatusValues(createPropStatVal(basicProperty));
		basicProperty.setBoundary(boundary);
		try {
			basicProperty.setPropCreateDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		basicProperty.addProperty(property);

		return basicProperty;
	}

	public PropertyStatusValues createPropStatVal(BasicProperty basicProperty) {
		PropertyStatusValues propStatVal = new PropertyStatusValues();
		PropertyStatus propertyStatus = (PropertyStatus) service.find("from PropertyStatus where statusCode=?",
				"CREATE");
		propStatVal.setIsActive("Y");
		propStatVal.setPropertyStatus(propertyStatus);
		propStatVal.setReferenceDate(new Date());
		propStatVal.setReferenceNo("0001");// There should be rule to create
		// order number, client has to give
		// it
		propStatVal.setBasicProperty(basicProperty);
		return propStatVal;
	}

	public PropertyID createPropertyID(BasicProperty basicProperty, Boundary boundary) {
		PropertyID propertyId = new PropertyID();

		propertyId.setZone(boundary);
		propertyId.setWard(boundary);

		propertyId.setCreatedDate(new Date());
		propertyId.setModifiedDate(new Date());
		propertyId.setArea(boundary);
		propertyId.setRightBndryStreet(boundary);
		propertyId.setLeftBndryStreet(boundary);
		propertyId.setBasicProperty(basicProperty);
		propertyId.setBackBndryStreet(boundary);

		return propertyId;
	}

	public PropertyAddress createPropertyAddress() {
		PropertyAddress propAddr = new PropertyAddress();
		propAddr.setAddTypeMaster((AddressTypeMaster) service.find("from AddressTypeMaster where addressTypeName = ?",
				PROP_ADDR_TYPE));
		propAddr.setBlock(createBoundary().getName());
		propAddr.setHouseNo("101");
		propAddr.setDoorNumOld("101");
		propAddr.setStreetAddress1("100 Ft Road");
		propAddr.setMobileNo("9008292922");
		propAddr.setEmailAddress("test@gmail.com");
		propAddr.setPinCode(560038);
		return propAddr;
	}

	public UserImpl createUser(String userName) {
		UserImpl user = new UserImpl();
		user.setUserName(userName + getRandomNumber());
		user.setFirstName(userName);
		user.setPwd("testpassword");
		user.setIsActive(1);
		session.saveOrUpdate(user);
		return user;
	}

	public int getRandomNumber() {
		Random ran = new Random();
		return ran.nextInt();
	}

	public BoundaryImpl createBoundary() {
		BoundaryImpl boundaryImpl = new BoundaryImpl();
		boundaryImpl.setBoundaryNum(BigInteger.valueOf(123232));
		boundaryImpl.setName("Bangalore");
		boundaryImpl.setBoundaryType(createBoundaryType());
		boundaryImpl.setIsHistory('N');
		session.saveOrUpdate(boundaryImpl);
		return boundaryImpl;
	}

	public BoundaryTypeImpl createBoundaryType() {
		BoundaryTypeImpl boundaryTypeImpl = new BoundaryTypeImpl();
		boundaryTypeImpl.setHeirarchy(Short.valueOf(1 + ""));
		boundaryTypeImpl.setName("karnataka");
		boundaryTypeImpl.setUpdatedTime(new Date());
		boundaryTypeImpl.setHeirarchyType(createHierarchy("testHierarchyName", "testHierarchyCode"));
		session.saveOrUpdate(boundaryTypeImpl);
		return boundaryTypeImpl;
	}

	public HeirarchyTypeImpl createHierarchy(String name, String code) {

		HeirarchyTypeImpl hierarchyType = new HeirarchyTypeImpl();
		hierarchyType.setName(name + getRandomNumber());
		hierarchyType.setCode(code + getRandomNumber());
		session.saveOrUpdate(hierarchyType);
		return hierarchyType;
	}

	public FloorImpl createFloor(String unitNo, String floorNo, String areaStr, Boundary boundary,
			String occupancyCode, String occpancy, String occupancyIndex, String areaBeforeInterceptingWall,
			String structureIndex, String usageIndex, String usageCode, String usageName,
			DepreciationMaster depreciationMaster, Date installmentStartDate, Date installmentEndDate) {
		FloorImpl floorImpl = new FloorImpl();

		PropertyUsage propertyUsage = createPropertyUsage(usageCode, usageName, usageIndex);

		floorImpl.setExtraField1(unitNo);
		floorImpl.setFloorNo(Integer.valueOf(floorNo));
		floorImpl.setStructureClassification(createStructureClassification(structureIndex, installmentStartDate,
				installmentEndDate));
		floorImpl.setPropertyUsage(propertyUsage);
		floorImpl.setPropertyOccupation(createPropertyOccupation(occupancyCode, occpancy, occupancyIndex));
		floorImpl.setExtraField3("01/04/2011");
		floorImpl.setDepreciationMaster(depreciationMaster);
		floorImpl.setBuiltUpArea(createArea(areaStr));
		if (occupancyCode.equals("TENANT")) {
			floorImpl.setRentPerMonth(new BigDecimal("2000"));
		}
		if (areaBeforeInterceptingWall != null) {
			floorImpl.setExtraField6(areaBeforeInterceptingWall);
		}
		return floorImpl;
	}

	public StructureClassification createStructureClassification(String structureIndex, Date installmentStartDate,
			Date installmentEndDate) {
		StructureClassification structureClassification = new StructureClassification();
		Installment installment = (Installment) service.find(
				"from Installment I where I.module.moduleName=? and (I.fromDate <= ? and I.toDate >= ?) ",
				PTMODULENAME, new Date(), new Date());

		UserImpl userImpl = createUser("test");

		structureClassification.setConstrTypeCode(UUID.randomUUID().toString().substring(0, 10));
		structureClassification.setCreatedBy(userImpl);
		structureClassification.setCreatedDate(new Date());
		structureClassification.setDescription("test Description");
		structureClassification.setFactor(Float.valueOf(structureIndex));
		structureClassification.setFloorNum(Integer.valueOf(1));
		try {
			structureClassification.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
			structureClassification.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/03/2012"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		structureClassification.setIsHistory('N');
		structureClassification.setModifiedBy(userImpl);
		structureClassification.setModifiedDate(new Date());
		structureClassification.setNumber(Integer.valueOf(1));
		structureClassification.setOrderId(Integer.valueOf(1));
		structureClassification.setStartInstallment(installment);
		structureClassification.setTypeName("I-A Posh");
		session.saveOrUpdate(structureClassification);
		return structureClassification;
	}

	/*
	 * public Installment createInstallment() { Installment installment = new
	 * Installment(); Module module = (Module)
	 * service.find("from Module where moduleName = ?", "Property Tax");
	 * 
	 * installment.setDescription("2011-12"); try { installment.setFromDate(new
	 * SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
	 * installment.setToDate(new
	 * SimpleDateFormat("dd/MM/yyyy").parse("31/03/2012"));
	 * installment.setInstallmentYear(new
	 * SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011")); } catch
	 * (ParseException e) { e.printStackTrace(); }
	 * installment.setInstallmentNumber(Integer.valueOf(1));
	 * installment.setLastUpdatedTimeStamp(new Date());
	 * installment.setModule(module); session.saveOrUpdate(installment); return
	 * installment; }
	 */

	public PropertyUsage createPropertyUsage(String usageCode, String usageName, String usageIndex) {
		PropertyUsage propertyUsage = new PropertyUsage();

		UserImpl userImpl = createUser("test");
		propertyUsage.setCreatedBy(userImpl);
		propertyUsage.setCreatedDate(new Date());
		try {
			propertyUsage.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
			propertyUsage.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/03/2012"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		propertyUsage.setIsEnabled(Integer.valueOf(1));
		propertyUsage.setLastUpdatedTimeStamp(new Date());
		propertyUsage.setModifiedBy(userImpl);
		propertyUsage.setModifiedDate(new Date());
		propertyUsage.setOrderId(Integer.valueOf(1));
		propertyUsage.setUsageCode(usageCode);
		propertyUsage.setUsageName(usageName);
		propertyUsage.setUsagePercentage(Float.valueOf(usageIndex));
		session.saveOrUpdate(propertyUsage);
		return propertyUsage;

	}

	public PropertyOccupation createPropertyOccupation(String occupancyCode, String occupation, String index) {
		PropertyOccupation propertyOccupation = new PropertyOccupation();
		UserImpl userImpl = createUser("test");

		propertyOccupation.setCreatedBy(userImpl);
		propertyOccupation.setCreatedDate(new Date());
		try {
			propertyOccupation.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
			propertyOccupation.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/03/2012"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		propertyOccupation.setModifiedBy(userImpl);
		propertyOccupation.setModifiedDate(new Date());
		propertyOccupation.setOccupancyCode(occupancyCode);
		propertyOccupation.setOccupancyFactor(Float.valueOf(index));
		propertyOccupation.setOccupation(occupation);
		propertyOccupation.setPropertyUsage(null);
		return propertyOccupation;
	}

	public DepreciationMaster getDepreciationMaster(String depName, String percentage) {
		DepreciationMaster depreciationMaster = (DepreciationMaster) service.find(
				"select dp from org.egov.demand.model.DepreciationMaster dp where dp.year=?", 2010);
		depreciationMaster.setDepreciationName(depName);
		depreciationMaster.setDepreciationPct(Float.valueOf(percentage));
		session.saveOrUpdate(depreciationMaster);
		return depreciationMaster;
	}

	public PtNotice createNotice() {
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		Boundary boundary = createBoundary();
		DepreciationMaster depreciationMaster = getDepreciationMaster("A", "1");
		BasicProperty basicProperty = createProperty(PROPTYPE_RESD, boundary, "SELFOCC", "Owner", "1", "1.25", "1",
				"UFD1", "Residential houses", depreciationMaster, "205.7", new Date(), new Date()).getBasicProperty();
		PtNotice notice = new PtNotice();

		notice.setModuleId(module.getId());
		notice.setNoticeDate(new Date());
		notice.setNoticeNo(basicProperty.getUpicNo());
		basicProperty.addNotice(notice);
		notice.setBasicProperty(basicProperty);

		session.saveOrUpdate(notice);
		return notice;
	}

	public CFinancialYear getFinancialYearForDate(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		Date startingDate;
		Date endingDate;
		Calendar start;
		Calendar end;
		CFinancialYear financialYear = new CFinancialYear();

		if (now.get(Calendar.MONTH) > Calendar.APRIL) {
			start = (Calendar) now.clone();
			start.set(now.get(Calendar.YEAR), Calendar.APRIL, 1);
			startingDate = start.getTime();
			end = (Calendar) now.clone();
			end.set(now.get(Calendar.YEAR) + 1, Calendar.MARCH, 31);
			endingDate = end.getTime();
		} else {
			start = (Calendar) now.clone();
			start.set(now.get(Calendar.YEAR) - 1, Calendar.APRIL, 1);
			startingDate = start.getTime();
			end = (Calendar) now.clone();
			end.set(now.get(Calendar.YEAR), Calendar.MARCH, 31);
			endingDate = end.getTime();
		}
		String finYrRange = start.get(Calendar.YEAR) + "_" + String.valueOf(end.get(Calendar.YEAR)).substring(2);

		financialYear.setStartingDate(startingDate);
		financialYear.setEndingDate(endingDate);
		financialYear.setFinYearRange(finYrRange);
		session.saveOrUpdate(financialYear);
		return financialYear;
	}

	public List<ApplicableFactor> createResidentialApplicableFactors() {
		List<ApplicableFactor> applicableFactors = new ArrayList<ApplicableFactor>();

		// Add Structural Factor
		ApplicableFactor applicableStructuralFactor = new ApplicableFactor();
		applicableStructuralFactor.setFactorName("SF");
		applicableStructuralFactor.setFactorValue(new BigDecimal("1.25"));
		applicableFactors.add(applicableStructuralFactor);

		// Add Usage Factor
		ApplicableFactor applicableUsageFactor = new ApplicableFactor();
		applicableUsageFactor.setFactorName("UF");
		applicableUsageFactor.setFactorValue(new BigDecimal("1"));
		applicableFactors.add(applicableUsageFactor);

		// Add Occupancy Factor
		ApplicableFactor applicableOccupancyFactor = new ApplicableFactor();
		applicableOccupancyFactor.setFactorName("OF");
		applicableOccupancyFactor.setFactorValue(new BigDecimal("1"));
		applicableFactors.add(applicableOccupancyFactor);

		// Add Age Factor
		ApplicableFactor applicableAgeFactor = new ApplicableFactor();
		applicableAgeFactor.setFactorName("AF");
		applicableAgeFactor.setFactorValue(new BigDecimal("1"));
		applicableFactors.add(applicableAgeFactor);

		// Add Location Factor
		ApplicableFactor applicableLocationFactor = new ApplicableFactor();
		applicableLocationFactor.setFactorName("LF");
		applicableLocationFactor.setFactorValue(new BigDecimal("1.25"));
		applicableFactors.add(applicableLocationFactor);
		return applicableFactors;
	}

	public EgDemandReasonDetails createDemandReasonDetails(String demandcode, String reasonMaster, String percentage) {
		EgDemandReasonDetails demandReasonDetails = new EgDemandReasonDetails();

		demandReasonDetails.setCreateTimestamp(new Date());
		demandReasonDetails.setEgDemandReason(createDemandReason(demandcode, reasonMaster));
		demandReasonDetails.setFlatAmount(BigDecimal.ZERO);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			demandReasonDetails.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
			Module module = GenericDaoFactory.getDAOFactory().getModuleDao().getModuleByName(NMCPTISConstants.PTMODULENAME);
			Installment currentInstallment = CommonsDaoFactory.getDAOFactory().getInstallmentDao().getInsatllmentByModuleForGivenDate(module, new Date());
			String toDate = dateFormat.format(currentInstallment.getToDate());
			demandReasonDetails.setToDate(dateFormat.parse(toDate));
		} catch (ParseException exp) {
			exp.printStackTrace();
		}
		demandReasonDetails.setHighLimit(new BigDecimal("100000000"));
		demandReasonDetails.setLastUpdatedTimeStamp(new Date());
		demandReasonDetails.setLowLimit(new BigDecimal("0"));
		demandReasonDetails.setPercentage(new BigDecimal(percentage));
		demandReasonDetails.setIsFlatAmntMax(0);
		session.saveOrUpdate(demandReasonDetails);
		return demandReasonDetails;
	}

	public EgDemandReasonDetails createDemandReasonDetailsLightingTax(String percentage) {
		EgDemandReasonDetails demandReasonDetails = new EgDemandReasonDetails();

		demandReasonDetails.setCreateTimestamp(new Date());
		demandReasonDetails.setEgDemandReason(createDemandReason("LIGHTINGTAX", "LIGHTINGTAX"));
		demandReasonDetails.setFlatAmount(BigDecimal.ZERO);
		try {
			demandReasonDetails.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
			demandReasonDetails.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/03/2012"));
		} catch (ParseException exp) {
			exp.printStackTrace();
		}
		demandReasonDetails.setHighLimit(new BigDecimal("100000000"));
		demandReasonDetails.setLastUpdatedTimeStamp(new Date());
		demandReasonDetails.setLowLimit(new BigDecimal("0"));
		demandReasonDetails.setPercentage(new BigDecimal(percentage));
		demandReasonDetails.setIsFlatAmntMax(0);

		session.saveOrUpdate(demandReasonDetails);
		return demandReasonDetails;
	}

	public EgDemandReasonDetails createDemandReasonDetailsGeneralWaterTax(String percentage) {
		EgDemandReasonDetails demandReasonDetails = new EgDemandReasonDetails();

		demandReasonDetails.setCreateTimestamp(new Date());
		demandReasonDetails.setEgDemandReason(createDemandReason("GENERALWATERTAX", "GENERALWATERTAX"));
		demandReasonDetails.setFlatAmount(BigDecimal.ZERO);
		try {
			demandReasonDetails.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
			demandReasonDetails.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/03/2012"));
		} catch (ParseException exp) {
			exp.printStackTrace();
		}
		demandReasonDetails.setHighLimit(new BigDecimal("100000000"));
		demandReasonDetails.setLastUpdatedTimeStamp(new Date());
		demandReasonDetails.setLowLimit(new BigDecimal("0"));
		demandReasonDetails.setPercentage(new BigDecimal(percentage));
		demandReasonDetails.setIsFlatAmntMax(0);
		session.saveOrUpdate(demandReasonDetails);
		return demandReasonDetails;
	}

	public EgDemandReasonDetails createDemandReasonDetailsEmployementCess(String percentage) {
		EgDemandReasonDetails demandReasonDetails = new EgDemandReasonDetails();

		demandReasonDetails.setCreateTimestamp(new Date());
		demandReasonDetails.setEgDemandReason(createDemandReason("EGCESS", "EMPLOYMENTGUARANTEECESS"));
		demandReasonDetails.setFlatAmount(BigDecimal.ZERO);
		try {
			demandReasonDetails.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
			demandReasonDetails.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/03/2012"));
		} catch (ParseException exp) {
			exp.printStackTrace();
		}
		demandReasonDetails.setHighLimit(new BigDecimal("100000000"));
		demandReasonDetails.setLastUpdatedTimeStamp(new Date());
		demandReasonDetails.setLowLimit(new BigDecimal("0"));
		demandReasonDetails.setPercentage(new BigDecimal(percentage));
		demandReasonDetails.setIsFlatAmntMax(0);
		session.saveOrUpdate(demandReasonDetails);
		return demandReasonDetails;
	}
	//TODO [Ramki] change method signature to accept installment and return EgDemandReason for given installment
	public EgDemandReason createDemandReason(String code, String reasonMaster) {
		EgDemandReason demandReason = new EgDemandReason();
		Installment installment = (Installment) service.find(
				"from Installment I where I.module.moduleName=? and (I.fromDate <= ? and I.toDate >= ?) ",
				PTMODULENAME, new Date(), new Date());

		demandReason.setCreateTimestamp(new Date());
		demandReason.setEgDemandReasonMaster(createDemandReasonMaster(code, reasonMaster));
		demandReason.setEgInstallmentMaster(installment);
		demandReason.setLastUpdatedTimestamp(new Date());
		demandReason.setPercentageBasis(null);
		demandReason.setPurposeCode(null);
		session.saveOrUpdate(demandReason);
		return demandReason;

	}

	public EgDemandReasonMaster createDemandReasonMaster(String code, String reasonMaster) {
		Module module = (Module) service.find("from Module where moduleName = ?", "Property Tax");
		EgReasonCategory reasonCategory = (EgReasonCategory) service
				.find("from EgReasonCategory where code = ?", "TAX");

		EgDemandReasonMaster demandReasonMaster = new EgDemandReasonMaster();
		demandReasonMaster.setCode(code);
		demandReasonMaster.setCreateTimeStamp(new Date());
		demandReasonMaster.setEgModule(module);
		demandReasonMaster.setEgReasonCategory(reasonCategory);
		demandReasonMaster.setIsDebit("N");
		demandReasonMaster.setLastUpdatedTimeStamp(new Date());
		demandReasonMaster.setOrderId(Long.valueOf("1"));
		demandReasonMaster.setReasonMaster(reasonMaster);
		session.saveOrUpdate(demandReasonMaster);
		return demandReasonMaster;
	}

	public Category createCategory(String categoryAmount) {
		UserImpl userImpl = createUser("test");
		Category category = new Category();
		category.setCategoryAmount(Float.valueOf(categoryAmount));
		category.setCategoryName("test");
		category.setCreatedBy(userImpl);
		category.setCreatedDate(new Date());
		try {
			category.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
			category.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/03/2012"));
		} catch (ParseException exp) {
			exp.printStackTrace();
		}
		category.setIsHistory('N');
		category.setModifiedBy(userImpl);
		category.setModifiedDate(new Date());
		BoundaryCategory boundaryCategory = createBoundaryCategory(category);
		Set<BoundaryCategory> boundaryCategories = new HashSet<BoundaryCategory>();
		boundaryCategories.add(boundaryCategory);
		category.setCatBoundaries(boundaryCategories);
		session.saveOrUpdate(category);
		return category;
	}

	public Category createCategory(String categoryAmount,PropertyUsage propUsage,StructureClassification structureClass,BoundaryImpl bndry) {
		UserImpl userImpl = createUser("test");
		Category category = new Category();
		category.setCategoryAmount(Float.valueOf(categoryAmount));
		category.setCategoryName(UUID.randomUUID().toString().substring(0, 10));
		category.setCreatedBy(userImpl);
		category.setCreatedDate(new Date());
		try {
			category.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
			category.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/03/2012"));
		} catch (ParseException exp) {
			exp.printStackTrace();
		}
		category.setIsHistory('N');
		category.setModifiedBy(userImpl);
		category.setModifiedDate(new Date());
		category.setPropUsage(propUsage);
		category.setStructureClass(structureClass);
		BoundaryCategory boundaryCategory = createBoundaryCategory(category,bndry);
		Set<BoundaryCategory> boundaryCategories = new HashSet<BoundaryCategory>();
		boundaryCategories.add(boundaryCategory);
		category.setCatBoundaries(boundaryCategories);
		
		session.saveOrUpdate(category);
		return category;
	}

	public BoundaryCategory createBoundaryCategory(Category category,BoundaryImpl bndry) {
		UserImpl userImpl = createUser("test");
		BoundaryCategory boundaryCategory = new BoundaryCategory();
		boundaryCategory.setBndry(bndry);
		boundaryCategory.setCategory(category);
		boundaryCategory.setCreatedBy(userImpl);
		boundaryCategory.setCreatedDate(new Date());
		try {
			boundaryCategory.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
			boundaryCategory.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/03/2012"));
		} catch (ParseException exp) {
			exp.printStackTrace();
		}
		boundaryCategory.setModifiedBy(userImpl);
		boundaryCategory.setModifiedDate(new Date());
		return boundaryCategory;
	}

	
	public BoundaryCategory createBoundaryCategory(Category category) {
		UserImpl userImpl = createUser("test");
		BoundaryCategory boundaryCategory = new BoundaryCategory();
		boundaryCategory.setBndry(createBoundary());
		boundaryCategory.setCategory(category);
		boundaryCategory.setCreatedBy(userImpl);
		boundaryCategory.setCreatedDate(new Date());
		try {
			boundaryCategory.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2011"));
			boundaryCategory.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse("31/03/2012"));
		} catch (ParseException exp) {
			exp.printStackTrace();
		}
		boundaryCategory.setModifiedBy(userImpl);
		boundaryCategory.setModifiedDate(new Date());
		return boundaryCategory;
	}

	public Installment createCurrInstallment() {
		Installment installment = new Installment();
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);

		Date today = new Date();
		Calendar instStartCal = Calendar.getInstance();
		Calendar instEndCal = Calendar.getInstance();
		Calendar currCal = Calendar.getInstance();
		instStartCal.setTime(today);
		instEndCal.setTime(today);
		currCal.setTime(today);
		int currMonth = currCal.get(Calendar.MONTH);
		String instDesc = "";
		String currYear = Integer.valueOf(currCal.get(Calendar.YEAR)).toString();
		String currYearP1 = Integer.valueOf(currCal.get(Calendar.YEAR) + 1).toString();
		String currYearM1 = Integer.valueOf(currCal.get(Calendar.YEAR) - 1).toString();
		if (currMonth == Calendar.JANUARY || currMonth == Calendar.FEBRUARY || currMonth == Calendar.MARCH) {
			instDesc = currYearM1 + "-" + currYear;
			instStartCal.set(Integer.valueOf(currYearM1).intValue(), Calendar.APRIL, 0);
			instEndCal.set(Integer.valueOf(currYear).intValue(), Calendar.MARCH, 31);
		} else {
			instDesc = currYear + "-" + currYearP1;
			instStartCal.set(Integer.valueOf(currYear).intValue(), Calendar.APRIL, 0);
			instEndCal.set(Integer.valueOf(currYearP1).intValue(), Calendar.MARCH, 31);
		}
		installment.setFromDate(instStartCal.getTime());
		installment.setInstallmentYear(instStartCal.getTime());
		installment.setToDate(instEndCal.getTime());
		installment.setInstallmentNumber(Integer.valueOf(1));

		currCal.set(Calendar.MONTH, Calendar.APRIL);
		currCal.set(Calendar.DAY_OF_MONTH, 0);
		installment.setModule(module);
		installment.setDescription(instDesc);
		session.saveOrUpdate(installment);
		return installment;
	}
	//TODO [Ramki] use createDemandReason, remove this method
	public EgDemandReason createEgDemandReason(String reasonMasterCode, String reasonMaster, Installment installment) {
		EgDemandReason demandReason = new EgDemandReason();
		demandReason.setCreateTimestamp(new Date());
		demandReason.setEgInstallmentMaster(installment);
		demandReason.setEgDemandReasonMaster(createEgDemandReasonMaster(reasonMasterCode, reasonMaster));
		return demandReason;
	}
	//TODO [Ramki] cross check with createDemandReasonMaster(String code, String reasonMaster), if no diff use the same and remove this mothod
	public EgDemandReasonMaster createEgDemandReasonMaster(String reasonMasterCode, String reasonMaster) {
		EgDemandReasonMaster demandReasonMaster = new EgDemandReasonMaster();
		
		EgReasonCategory reasonCategory = new EgReasonCategory();
		reasonCategory.setCode("TAX");
		reasonCategory.setName("TAX");
		reasonCategory.setOrderId(1L);
		reasonCategory.setLastUpdatedTimeStamp(new Date());
		
		demandReasonMaster.setCode(reasonMasterCode);
		demandReasonMaster.setReasonMaster(reasonMaster);
		demandReasonMaster.setEgReasonCategory(reasonCategory);		
		demandReasonMaster.setIsDebit("N");		
		demandReasonMaster.setEgModule(GenericHibernateDaoFactory.getDAOFactory().getModuleDao()
				.getModuleByName(NMCPTISConstants.PTMODULENAME));
		demandReasonMaster.setOrderId(1L);
		demandReasonMaster.setCreateTimeStamp(new Date());
		demandReasonMaster.setLastUpdatedTimeStamp(new Date());
		
		return demandReasonMaster;
	}
	
	public Set<EgDemandDetails> createDemandDetails(Map<String, BigDecimal> demandByReason, Installment inst, Map<String, BigDecimal> collectionByReason) {
		Set<EgDemandDetails> demandDetails = new HashSet<EgDemandDetails>();
		
		Map<String, String> demandNameByCode = prepareDemandNameByCode();
		for (Map.Entry<String, BigDecimal> entry : demandByReason.entrySet()) {
			
			if (collectionByReason == null) {
				demandDetails.add(EgDemandDetails.fromReasonAndAmounts(entry.getValue(),
						createEgDemandReason(entry.getKey(), demandNameByCode.get(entry.getKey()), inst),
						new BigDecimal("0")));
			} else {
				demandDetails.add(EgDemandDetails.fromReasonAndAmounts(entry.getValue(),
						createEgDemandReason(entry.getKey(), demandNameByCode.get(entry.getKey()), inst),
						collectionByReason.get(entry.getKey())));
			}
		}
		
		return demandDetails;
	}
	
	private Map<String, String> prepareDemandNameByCode() {
		Map<String, String> demandNameByCode = new HashMap<String, String>() {
			{
				put(DEMANDRSN_CODE_GENERAL_TAX, DEMANDRSN_STR_GENERAL_TAX);
				put(DEMANDRSN_CODE_SEWERAGE_TAX, DEMANDRSN_STR_SEWERAGE_TAX);
				put(DEMANDRSN_CODE_LIGHTINGTAX, DEMANDRSN_STR_LIGHTINGTAX);
				put(DEMANDRSN_CODE_FIRE_SERVICE_TAX, DEMANDRSN_STR_FIRE_SERVICE_TAX);
				put(DEMANDRSN_CODE_GENERAL_WATER_TAX, DEMANDRSN_STR_GENERAL_WATER_TAX);
				put(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD, DEMANDRSN_STR_EDUCATIONAL_CESS_RESD);
				put(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD, DEMANDRSN_STR_EDUCATIONAL_CESS_NONRESD);
				put(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX, DEMANDRSN_STR_EMPLOYEE_GUARANTEE_TAX);
				put(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX, DEMANDRSN_STR_BIG_RESIDENTIAL_BLDG_TAX);
			}
		};
		return demandNameByCode;
	}
}
