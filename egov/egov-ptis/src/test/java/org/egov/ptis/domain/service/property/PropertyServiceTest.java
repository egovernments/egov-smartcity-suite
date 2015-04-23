package org.egov.ptis.domain.service.property;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.NMCObjectFactory;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class PropertyServiceTest extends AbstractPersistenceServiceTest {
	private PropertyTaxUtil propertyTaxUtil;
	private PropertyService propertyService;
	private NMCObjectFactory objectFactory;
	//private Map<String, String> demandNameByCode;

	private Map<Installment, Map<String, BigDecimal>> excessCollectionsByInstallmet = new LinkedHashMap<Installment, Map<String, BigDecimal>>();
	private Map<Installment, Set<EgDemandDetails>> demandDetailsByInstallment = new LinkedHashMap<Installment, Set<EgDemandDetails>>();

	@Before
	public void setUp() {
		//propertyTaxUtil = new PropertyTaxUtil();
		//propertyTaxUtil.setPersistenceService(genericService);
		//propertyService = new PropertyService();
		//objectFactory = new NMCObjectFactory();
		//demandNameByCode = prepareDemandNameByCode();
	}
	
	@SuppressWarnings("serial")
	@Test
	public void dummy() {
		Assert.assertEquals(true, true);
	}
	@SuppressWarnings("serial")
	@Ignore
	public void testAdjustExcessCollectionAmount() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2010, 04, 01, 0, 0, 0);
		List<Installment> installments = propertyTaxUtil.getInstallmentListByStartDate(calendar.getTime());

		Module module = GenericDaoFactory.getDAOFactory().getModuleDao().getModuleByName(NMCPTISConstants.PTMODULENAME);

		calendar.set(2012, 04, 01);
		Installment installment = CommonsDaoFactory.getDAOFactory().getInstallmentDao()
				.getInsatllmentByModuleForGivenDate(module, calendar.getTime());

		Map<String, BigDecimal> excessCollections = new LinkedHashMap<String, BigDecimal>() {
			{
				put("GEN_TAX", new BigDecimal("118782"));
				put("SEWERAGETAX", new BigDecimal("45703"));
				put("FIRE_SER_TAX", new BigDecimal("3809"));
				put("LIGHTINGTAX", new BigDecimal("3809"));
				put("EDU_CESS_RESD", new BigDecimal("57844"));
			}
		};

		excessCollectionsByInstallmet.put(installment, excessCollections);

		List<String> reasons = Arrays.asList(DEMANDRSN_CODE_GENERAL_TAX,
				DEMANDRSN_CODE_SEWERAGE_TAX, DEMANDRSN_CODE_FIRE_SERVICE_TAX, DEMANDRSN_CODE_LIGHTINGTAX,
				DEMANDRSN_CODE_GENERAL_WATER_TAX, DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);


		Map<String, BigDecimal> detailsCommonToAllInstallments = new HashMap<String, BigDecimal>() {
			{
				put("GEN_TAX", new BigDecimal("1746"));
				put("SEWERAGETAX", new BigDecimal("953"));
				put("FIRE_SER_TAX", new BigDecimal("79"));
				put("LIGHTINGTAX", new BigDecimal("79"));
				put("EDU_CESS_RESD", new BigDecimal("476"));
				put("BIG_RESD_TAX", new BigDecimal("794"));
			}
		};

		Map<String, BigDecimal> detailsWithCollection = new HashMap<String, BigDecimal>() {
			{
				put("GEN_TAX", new BigDecimal("1746"));
				put("SEWERAGETAX", new BigDecimal("953"));
				put("FIRE_SER_TAX", new BigDecimal("79"));
				put("LIGHTINGTAX", new BigDecimal("79"));
				put("EDU_CESS_RESD", new BigDecimal("476"));
				put("BIG_RESD_TAX", new BigDecimal("0"));
			}
		};

		Map<String, BigDecimal> detailsWithExpectedCollection = new HashMap<String, BigDecimal>() {
			{
				put("GEN_TAX", new BigDecimal("103386"));
				put("SEWERAGETAX", new BigDecimal("46656"));
				put("FIRE_SER_TAX", new BigDecimal("3888"));
				put("LIGHTINGTAX", new BigDecimal("3888"));
				put("EDU_CESS_RESD", new BigDecimal("53716"));
				put("BIG_RESD_TAX", new BigDecimal("794"));
			}
		};


		for (Installment inst : installments) {

			if (installment.equals(inst)) {
				demandDetailsByInstallment.put(inst,
						objectFactory.createDemandDetails(detailsCommonToAllInstallments, inst, detailsWithCollection));
			} else {
				demandDetailsByInstallment.put(inst,
						objectFactory.createDemandDetails(detailsCommonToAllInstallments, inst, null));
			}
		}


		// Verifying
		propertyService.setExcessCollAmtMap(excessCollectionsByInstallmet);
		propertyService.adjustExcessCollectionAmount(installments, demandDetailsByInstallment, null);


		Installment currentInstallment = PropertyTaxUtil.getCurrentInstallment();

		Set<EgDemandDetails> adjustedDemandDetails = demandDetailsByInstallment.get(currentInstallment);

		for (EgDemandDetails demandDetail : adjustedDemandDetails) {
			Assert.assertEquals(
					detailsWithExpectedCollection.get(demandDetail.getEgDemandReason().getEgDemandReasonMaster()
							.getCode()), demandDetail.getAmtCollected());
		}

	}

	@SuppressWarnings("serial")
	@Ignore
	public void testAdjustExcessCollectionAmountToDemandDetailsOf2010_11() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2010, 04, 01, 0, 0, 0);
		List<Installment> installments = propertyTaxUtil.getInstallmentListByStartDate(calendar.getTime());

		Module module = GenericDaoFactory.getDAOFactory().getModuleDao().getModuleByName(NMCPTISConstants.PTMODULENAME);

		calendar.set(2012, 04, 01);
		Installment installment = CommonsDaoFactory.getDAOFactory().getInstallmentDao()
				.getInsatllmentByModuleForGivenDate(module, calendar.getTime());

		Map<String, BigDecimal> excessCollections = new LinkedHashMap<String, BigDecimal>() {
			{
				put("GEN_TAX", new BigDecimal("3274"));
				put("SEWERAGETAX", new BigDecimal("1239"));
				put("FIRE_SER_TAX", new BigDecimal("103"));
				put("LIGHTINGTAX", new BigDecimal("103"));
				put("EMP_GUA_CESS", new BigDecimal("310"));
				put("EDU_CESS_NONRESD", new BigDecimal("1239"));
			}
		};

		excessCollectionsByInstallmet.put(installment, excessCollections);

		Map<String, BigDecimal> demandDetailsCommonToAllInstallments = new HashMap<String, BigDecimal>() {
			{
				put("GEN_TAX", new BigDecimal("3888"));
				put("SEWERAGETAX", new BigDecimal("1944"));
				put("FIRE_SER_TAX", new BigDecimal("162"));
				put("LIGHTINGTAX", new BigDecimal("162"));
				put("EMP_GUA_CESS", new BigDecimal("486"));
				put("EDU_CESS_NONRESD", new BigDecimal("1944"));
			}
		};

		Map<String, BigDecimal> detailsWithExpectedCollection = new HashMap<String, BigDecimal>() {
			{
				put("GEN_TAX", new BigDecimal("3274"));
				put("SEWERAGETAX", new BigDecimal("1239"));
				put("FIRE_SER_TAX", new BigDecimal("103"));
				put("LIGHTINGTAX", new BigDecimal("103"));
				put("EMP_GUA_CESS", new BigDecimal("310"));
				put("EDU_CESS_NONRESD", new BigDecimal("1239"));
			}
		};


		for (Installment inst : installments) {
			demandDetailsByInstallment.put(inst,
					objectFactory.createDemandDetails(demandDetailsCommonToAllInstallments, inst, null));
		}


		// Verifying
		propertyService.setExcessCollAmtMap(excessCollectionsByInstallmet);
		propertyService.adjustExcessCollectionAmount(installments, demandDetailsByInstallment, null);


		calendar.set(2010, 04, 01);
		installment = CommonsDaoFactory.getDAOFactory().getInstallmentDao()
				.getInsatllmentByModuleForGivenDate(module, calendar.getTime());

		Set<EgDemandDetails> adjustedDemandDetails = demandDetailsByInstallment.get(installment);

		for (EgDemandDetails demandDetail : adjustedDemandDetails) {
			Assert.assertEquals(
					detailsWithExpectedCollection.get(demandDetail.getEgDemandReason().getEgDemandReasonMaster()
							.getCode()), demandDetail.getAmtCollected());
		}

	}


	@SuppressWarnings("serial")
	@Ignore
	public void testAdjustExcessCollectionAmountWhenNoWaterTax() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2002, 04, 01, 0, 0, 0);
		List<Installment> installments = propertyTaxUtil.getInstallmentListByStartDate(calendar.getTime());


		Map<String, BigDecimal> excessCollections = new LinkedHashMap<String, BigDecimal>();
		excessCollections.put("GEN_WATER_TAX", new BigDecimal("2079"));
		excessCollectionsByInstallmet.put(installments.get(4), excessCollections);

		excessCollections = new LinkedHashMap<String, BigDecimal>();
		excessCollections.put("GEN_WATER_TAX", new BigDecimal("297"));
		excessCollectionsByInstallmet.put(installments.get(5), excessCollections);

		excessCollections = new LinkedHashMap<String, BigDecimal>();
		excessCollections.put("GEN_WATER_TAX", new BigDecimal("297"));
		excessCollectionsByInstallmet.put(installments.get(6), excessCollections);

		excessCollections = new LinkedHashMap<String, BigDecimal>();
		excessCollections.put("GEN_WATER_TAX", new BigDecimal("800"));
		excessCollectionsByInstallmet.put(installments.get(7), excessCollections);

		excessCollections = new LinkedHashMap<String, BigDecimal>();
		excessCollections.put("GEN_WATER_TAX", new BigDecimal("800"));
		excessCollectionsByInstallmet.put(installments.get(8), excessCollections);

		excessCollections = new LinkedHashMap<String, BigDecimal>();
		excessCollections.put("GEN_WATER_TAX", new BigDecimal("800"));
		excessCollectionsByInstallmet.put(installments.get(9), excessCollections);

		excessCollections = new LinkedHashMap<String, BigDecimal>();
		excessCollections.put("GEN_WATER_TAX", new BigDecimal("800"));
		excessCollectionsByInstallmet.put(installments.get(10), excessCollections);

		excessCollections = new LinkedHashMap<String, BigDecimal>();
		excessCollections.put("GEN_WATER_TAX", new BigDecimal("800"));
		excessCollectionsByInstallmet.put(installments.get(11), excessCollections);

		Map<String, BigDecimal> demandDetailsCommonToAllInstallments = new HashMap<String, BigDecimal>() {
			{
				put("GEN_TAX", new BigDecimal("1331"));
				put("SEWERAGETAX", new BigDecimal("798"));
				put("FIRE_SER_TAX", new BigDecimal("67"));
				put("LIGHTINGTAX", new BigDecimal("67"));
				put("EDU_CESS_RESD", new BigDecimal("399"));
			}
		};

		Map<String, BigDecimal> detailsWithExpectedCollectionForCurrentInstallment = new HashMap<String, BigDecimal>() {
			{
				put("GEN_TAX", new BigDecimal("6673"));
				put("SEWERAGETAX", new BigDecimal("0"));
				put("FIRE_SER_TAX", new BigDecimal("0"));
				put("LIGHTINGTAX", new BigDecimal("0"));
				put("EDU_CESS_RESD", new BigDecimal("0"));
			}
		};


		for (Installment inst : installments) {
			demandDetailsByInstallment.put(inst,
					objectFactory.createDemandDetails(demandDetailsCommonToAllInstallments, inst, null));
		}


		// Verifying
		propertyService.setExcessCollAmtMap(excessCollectionsByInstallmet);
		propertyService.adjustExcessCollectionAmount(installments, demandDetailsByInstallment, null);


		Installment currentInstallment = PropertyTaxUtil.getCurrentInstallment();

		Set<EgDemandDetails> adjustedDemandDetails = demandDetailsByInstallment.get(currentInstallment);

		for (EgDemandDetails demandDetail : adjustedDemandDetails) {
			Assert.assertEquals(detailsWithExpectedCollectionForCurrentInstallment.get(demandDetail.getEgDemandReason().getEgDemandReasonMaster()
							.getCode()), demandDetail.getAmtCollected());
		}

	}

}
