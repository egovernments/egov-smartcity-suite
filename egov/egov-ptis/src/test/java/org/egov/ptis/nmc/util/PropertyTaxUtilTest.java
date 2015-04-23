package org.egov.ptis.nmc.util;

import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.BASERENT_FROM_APRIL2008_BUILDINGS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_RESD;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.nmc.model.ApplicableFactor;
import org.egov.ptis.nmc.model.NMCObjectFactory;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class PropertyTaxUtilTest extends AbstractPersistenceServiceTest {

	private PropertyTaxUtil propertyTaxUtil;
	private NMCObjectFactory objectFactory;

	@Before
	public void setUp() {
		objectFactory = new NMCObjectFactory(session, genericService);
		propertyTaxUtil = new PropertyTaxUtil();
		propertyTaxUtil.setPersistenceService(service);
	}

	@Ignore
	public void testCalculateApplicableTaxes() {
		List<String> applicableTaxesMap = objectFactory.createApplicableTaxesResidential();

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

		Installment installment = (Installment) service.find(
				"from Installment I where I.module.moduleName=? and (I.fromDate <= ? and I.toDate >= ?) ",
				PTMODULENAME, new Date(), new Date());

		UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();
		unitTaxCalculationInfo.setAnnualRentAfterDeduction(new BigDecimal("29281.5"));
		unitTaxCalculationInfo.setOccpancyDate(new Date());

		/*unitTaxCalculationInfo = propertyTaxUtil.calculateApplicableTaxes(applicableTaxesMap, null, unitTaxCalculationInfo,
				installment, null, null, null, null);*/
		unitTaxCalculationInfo = propertyTaxUtil.calculateApplicableTaxes(applicableTaxesMap, null, unitTaxCalculationInfo,
				installment, null, null, null, null, null, null);

		Assert.assertEquals(Boolean.TRUE,
				unitTaxCalculationInfo.getTotalTaxPayable().compareTo(BigDecimal.ZERO) == 1 ? Boolean.TRUE
						: Boolean.FALSE);

	}

	@Test
	public void testmultiplicativeFactorAreaWise() {
		FloorImpl floorImpl = new FloorImpl();
		floorImpl.setFloorNo(Integer.valueOf(0));
		Assert.assertEquals("1", propertyTaxUtil.multiplicativeFactorAreaWise(PROPTYPE_RESD, floorImpl, "46.45", null));
		Assert.assertEquals(".8", propertyTaxUtil.multiplicativeFactorAreaWise(PROPTYPE_RESD, floorImpl, "93", null));

		floorImpl.setFloorNo(Integer.valueOf(1));
		Assert.assertEquals(".9", propertyTaxUtil.multiplicativeFactorAreaWise(PROPTYPE_RESD, floorImpl, "46.45", null));

		floorImpl.setFloorNo(Integer.valueOf(1));
		Assert.assertEquals(".7", propertyTaxUtil.multiplicativeFactorAreaWise(PROPTYPE_NON_RESD, floorImpl, "1", null));
	}

	@Test
	public void testCalculateBaseRentPerSqMt() {

		List<ApplicableFactor> applicableFactors = objectFactory.createResidentialApplicableFactors();

		BigDecimal baseRate = BASERENT_FROM_APRIL2008_BUILDINGS;

		Assert.assertEquals(new BigDecimal("15.63"),
				propertyTaxUtil.calculateBaseRentPerSqMtPerMonth(applicableFactors, baseRate));

	}
}
