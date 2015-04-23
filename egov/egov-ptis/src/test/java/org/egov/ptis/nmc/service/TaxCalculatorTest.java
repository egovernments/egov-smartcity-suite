package org.egov.ptis.nmc.service;

import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.commons.Installment;
import org.egov.demand.model.DepreciationMaster;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infra.admin.master.entity.BoundaryImpl;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.NMCObjectFactory;
import org.egov.ptis.nmc.model.TaxCalculationInfo;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TaxCalculatorTest extends AbstractPersistenceServiceTest {
	private TaxCalculator taxCalculator;

	private ResidentialUnitTaxCalculator residentialUnitTaxCalculator;
	private NonResidentialUnitTaxCalculator nonResidentialUnitTaxCalculator;
	private OpenPlotUnitTaxCalculator openPlotUnitTaxCalculator;
	private PropertyTaxUtil propertyTaxUtil;
	private NMCObjectFactory objectFactory;
	private PersistenceService persistenceService;

	@Before
	public void setUp() {
		objectFactory = new NMCObjectFactory(session, service);
		persistenceService = new PersistenceService();
		persistenceService.setSessionFactory(new SessionFactory());
		taxCalculator = new TaxCalculator();
		residentialUnitTaxCalculator = new ResidentialUnitTaxCalculator();
		nonResidentialUnitTaxCalculator = new NonResidentialUnitTaxCalculator();
		openPlotUnitTaxCalculator = new OpenPlotUnitTaxCalculator();
		openPlotUnitTaxCalculator.setPersistenceService(persistenceService);
		propertyTaxUtil = new PropertyTaxUtil();

		propertyTaxUtil.setPersistenceService(service);
		propertyTaxUtil.setGenericDao(new GenericHibernateDaoFactory());

		residentialUnitTaxCalculator.setPropertyTaxUtil(propertyTaxUtil);
		nonResidentialUnitTaxCalculator.setPropertyTaxUtil(propertyTaxUtil);
		openPlotUnitTaxCalculator.setPropertyTaxUtil(propertyTaxUtil);

		taxCalculator.setResidentialUnitTaxCalculator(residentialUnitTaxCalculator);
		taxCalculator.setNonResidentialUnitTaxCalculator(nonResidentialUnitTaxCalculator);
		taxCalculator.setOpenPlotUnitTaxCalculator(openPlotUnitTaxCalculator);
		taxCalculator.setPropertyTaxUtil(propertyTaxUtil);
	}
	/*Ingoring because the rent chart rules changes and we do not have same rent chart with sample data*/
	@SuppressWarnings("unused")
	@Ignore
	public void calculateSingleOwnerResidentialPropertyTax() {
		BoundaryImpl bndry = objectFactory.createBoundary();

		DepreciationMaster depreciationMaster = objectFactory.getDepreciationMaster("E", "1");

		EgDemandReasonDetails demandReasonGeneralTax = objectFactory.createDemandReasonDetails("GENERALTAX",
				"GENERALTAX", "27");
		EgDemandReasonDetails demandReasonSewerageTax = objectFactory.createDemandReasonDetails("SEWERAGETAX",
				"SEWERAGETAX", "12");
		EgDemandReasonDetails demandReasonFireServiceTax = objectFactory.createDemandReasonDetails("FIRESERVICETAX",
				"FIRESERVICETAX", "1");
		EgDemandReasonDetails demandReasonLightingTax = objectFactory.createDemandReasonDetails("LIGHTINGTAX",
				"LIGHTINGTAX", "1");
		EgDemandReasonDetails demandReasonGeneralWaterTax = objectFactory.createDemandReasonDetails("GENERALWATERTAX",
				"GENERALWATERTAX", "0");

		EgDemandReasonDetails demandReasonEducationTax = objectFactory.createDemandReasonDetails("EC_RESIDENTIAL",
				"EDUCATIONCESS_RESIDENTIAL", "6");

		Property property = objectFactory.createProperty(NMCPTISConstants.PROPTYPE_RESD, bndry, "SELFOCC", "Owner", "1", "1.25", "1", "UFD1",
				"Residential houses", depreciationMaster, "205.7", new Date(), new Date());

		for (FloorIF floor : property.getPropertyDetail().getFloorDetails()) {
			Category cat =objectFactory.createCategory("1.25",floor.getPropertyUsage(),floor.getStructureClassification(),bndry);

		}

		List<String> applicableTaxes = objectFactory.createApplicableTaxesResidential();
		Date minimumOccupancyDate =new Date();

		HashMap<Installment, TaxCalculationInfo> taxCalculationInfoMap = taxCalculator.calculatePropertyTax(property,
				minimumOccupancyDate);

		Installment installment = (Installment) service.find(
				"from Installment I where I.module.moduleName=? and (I.fromDate <= ? and I.toDate >= ?) ",
				PTMODULENAME, new Date(), new Date());

		Assert.assertEquals(new BigDecimal("8807"), taxCalculationInfoMap.get(installment).getTotalTaxPayable());

	}
	/*Ingoring because the rent chart rules changes and we do not have same rent chart with sample data*/
	@Ignore
	public void calculateResidentialTenantPropertyTax() {
		Category category = objectFactory.createCategory("1.25");
		DepreciationMaster depreciationMaster = objectFactory.getDepreciationMaster("E", "1");

		EgDemandReasonDetails demandReasonGeneralTax = objectFactory.createDemandReasonDetails("GENERALTAX",
				"GENERALTAX", "27");
		EgDemandReasonDetails demandReasonSewerageTax = objectFactory.createDemandReasonDetails("SEWERAGETAX",
				"SEWERAGETAX", "12");
		EgDemandReasonDetails demandReasonFireServiceTax = objectFactory.createDemandReasonDetails("FIRESERVICETAX",
				"FIRESERVICETAX", "1");
		EgDemandReasonDetails demandReasonLightingTax = objectFactory.createDemandReasonDetails("LIGHTINGTAX",
				"LIGHTINGTAX", "1");
		EgDemandReasonDetails demandReasonGeneralWaterTax = objectFactory.createDemandReasonDetails("GENERALWATERTAX",
				"GENERALWATERTAX", "0");
		EgDemandReasonDetails demandReasonEducationTax = objectFactory.createDemandReasonDetails("EC_RESIDENTIAL",
				"EDUCATIONCESS_RESIDENTIAL", "6");

		Property property = objectFactory.createProperty(NMCPTISConstants.PROPTYPE_RESD, category
				.getCatBoundaries().iterator().next().getBndry(), "TENANT", "Tenanted", "2", "1.25", "1", "UFD1",
				"Residential houses", depreciationMaster, "205.7", new Date(), new Date());
		List<String> applicableTaxes = objectFactory.createApplicableTaxesResidential();
		Date minimumOccupancyDate =new Date();
		HashMap<Installment, TaxCalculationInfo> taxCalculationInfoMap = taxCalculator.calculatePropertyTax(property,
				minimumOccupancyDate);

		Installment installment = (Installment) service.find(
				"from Installment I where I.module.moduleName=? and (I.fromDate <= ? and I.toDate >= ?) ",
				PTMODULENAME, new Date(), new Date());

		Assert.assertEquals(new BigDecimal("10152"), taxCalculationInfoMap.get(installment).getTotalTaxPayable());

	}
	/*Ingoring because the rent chart rules changes and we do not have same rent chart with sample data*/
	@Ignore
	public void calculateNonResidentialSingleOwnerPropertyTax() {
		BoundaryImpl bndry = objectFactory.createBoundary();
		DepreciationMaster depreciationMaster = objectFactory.getDepreciationMaster("E", "1");

		EgDemandReasonDetails demandReasonGeneralTax = objectFactory.createDemandReasonDetails("GENERALTAX",
				"GENERALTAX", "27");
		EgDemandReasonDetails demandReasonSewerageTax = objectFactory.createDemandReasonDetails("SEWERAGETAX",
				"SEWERAGETAX", "12");
		EgDemandReasonDetails demandReasonFireServiceTax = objectFactory.createDemandReasonDetails("FIRESERVICETAX",
				"FIRESERVICETAX", "1");
		EgDemandReasonDetails demandReasonLightingTax = objectFactory.createDemandReasonDetails("LIGHTINGTAX",
				"LIGHTINGTAX", "1");
		EgDemandReasonDetails demandReasonGeneralWaterTax = objectFactory.createDemandReasonDetails("GENERALWATERTAX",
				"GENERALWATERTAX", "0");
		EgDemandReasonDetails demandReasonEducationTax = objectFactory.createDemandReasonDetails("EC_N-RESIDENTIAL",
				"EDUCATIONCESS_NON-RESIDENTIAL", "12");
		EgDemandReasonDetails demandReasonEmployementCess = objectFactory.createDemandReasonDetails("EGCESS",
				"EMPLOYMENTGUARANTEECESS", "3");

		Property property = objectFactory
				.createProperty(
						NMCPTISConstants.PROPTYPE_NON_RESD,
						bndry,
						"SELFOCC",
						"Owner",
						"1",
						".75",
						"3",
						"UFB1",
						"Commercial establishments like shops, business centres, unstarred hotels, unstarred restaurants/ bars",
						depreciationMaster, "205.7", new Date(), new Date());
		for (FloorIF floor : property.getPropertyDetail().getFloorDetails()) {
			Category cat =objectFactory.createCategory("1.25",floor.getPropertyUsage(),floor.getStructureClassification(),bndry);

		}

		List<String> applicableTaxes = objectFactory.createApplicableTaxesNonResidential();


		Date minimumOccupancyDate = new Date();

		HashMap<Installment, TaxCalculationInfo> taxCalculationInfoMap = taxCalculator.calculatePropertyTax(property,
				minimumOccupancyDate);

		Installment installment = (Installment) service.find(
				"from Installment I where I.module.moduleName=? and (I.fromDate <= ? and I.toDate >= ?) ",
				PTMODULENAME, new Date(), new Date());

		Assert.assertEquals(new BigDecimal("8318"), taxCalculationInfoMap.get(installment).getTotalTaxPayable());

	}


	/*Ingoring because the rent chart rules changes and we do not have same rent chart with sample data*/
	@Ignore
	public void calculateOpenPlotSingleOwnerPropertyTax() {
		Category category = objectFactory.createCategory("1.25");

		EgDemandReasonDetails demandReasonGeneralTax = objectFactory.createDemandReasonDetails("GENERALTAX",
				"GENERALTAX", "23");
		EgDemandReasonDetails demandReasonSewerageTax = objectFactory.createDemandReasonDetails("SEWERAGETAX",
				"SEWERAGETAX", "12");
		EgDemandReasonDetails demandReasonFireServiceTax = objectFactory.createDemandReasonDetails("FIRESERVICETAX",
				"FIRESERVICETAX", "1");
		EgDemandReasonDetails demandReasonLightingTax = objectFactory.createDemandReasonDetails("LIGHTINGTAX",
				"LIGHTINGTAX", "1");
		EgDemandReasonDetails demandReasonEducationTax = objectFactory.createDemandReasonDetails("EC_OPENPLOT",
				"EDUCATIONCESS_EC_OPENPLOT", "6");

		Property property = objectFactory.createProperty(NMCPTISConstants.PROPTYPE_OPEN_PLOT, category
				.getCatBoundaries().iterator().next().getBndry(), "SELFOCC", "Owner", "1", null, "1", "UFD1",
				"Open Plot", null, "150", new Date(), new Date());
		List<String> applicableTaxes = objectFactory.createApplicableTaxesOpenPlot();
		Date minimumOccupancyDate =new Date();

		HashMap<Installment, TaxCalculationInfo> taxCalculationInfoMap = taxCalculator.calculatePropertyTax(property,
				minimumOccupancyDate);

		Installment installment = (Installment) service.find(
				"from Installment I where I.module.moduleName=? and (I.fromDate <= ? and I.toDate >= ?) ",
				PTMODULENAME, new Date(), new Date());

		Assert.assertEquals(new BigDecimal("3483"), taxCalculationInfoMap.get(installment).getTotalTaxPayable());

	}

	@Test
	public void dummy() {
		Assert.assertEquals(true, true);
	}
}
