package org.egov.ptis.nmc.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.admbndry.Boundary;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.util.PropertyTaxUtil;

public class GovtPropertyUnitTaxCalculator {

	private static final Logger LOGGER = Logger.getLogger(GovtPropertyUnitTaxCalculator.class);
	private PropertyTaxUtil propertyTaxUtil;
	private UnitTaxCalculationInfo unitTaxCalculationInfo;

	public UnitTaxCalculationInfo calculateUnitTax(PropertyDetail propertyDetail, Boundary propertyArea,
			List<String> applicableTaxes, Installment installment, Long categoryId, Property property) {

		BigDecimal annualLettingValue = BigDecimal.ZERO;

		// Add Unit Info
		unitTaxCalculationInfo = new UnitTaxCalculationInfo();
		unitTaxCalculationInfo.setFloorNumber(propertyTaxUtil.getFloorStr(0));
		unitTaxCalculationInfo.setFloorNumberInteger(new Integer(0));
		unitTaxCalculationInfo.setUnitNumber(1);
		
		if (PropertyTaxUtil.isPropertyModified(property)) {
			unitTaxCalculationInfo.setOccpancyDate(new Date(propertyDetail.getProperty().getEffectiveDate().getTime()));
		} else {
			unitTaxCalculationInfo.setOccpancyDate(new Date(property.getBasicProperty().getPropCreateDate().getTime()));
		}

		if (propertyDetail.getSitalArea() != null)
			unitTaxCalculationInfo.setUnitArea(new BigDecimal(propertyDetail.getSitalArea().getArea().toString()));

		BigDecimal buildingCost = new BigDecimal(propertyDetail.getExtra_field3());
		LOGGER.debug("Cost of Govt Building: " + buildingCost);

		/**
		 * Calculate Tax for State Government Buildings
		 */
		if (propertyDetail.getPropertyTypeMaster().getCode().equals(NMCPTISConstants.PROPTYPE_STATE_GOVT)) {
			annualLettingValue = buildingCost.multiply(new BigDecimal(
					NMCPTISConstants.STATEGOVT_BUILDING_ALV_PERCENTAGE).divide(new BigDecimal("100")));
			// Set ALV
			unitTaxCalculationInfo
					.setAnnualRentAfterDeduction(annualLettingValue.setScale(0, BigDecimal.ROUND_HALF_UP));

			unitTaxCalculationInfo = propertyTaxUtil.calculateApplicableTaxes(applicableTaxes, null,
					unitTaxCalculationInfo, installment, propertyDetail.getPropertyTypeMaster().getCode(),
					propertyDetail.getExtra_field5(), propertyDetail.getExtra_field4(), property);
		}
		/**
		 * Calculate Tax for Central Government Buildings
		 */
		else {
			annualLettingValue = buildingCost.multiply(new BigDecimal(
					NMCPTISConstants.CENTRALGOVT_BUILDING_ALV_PERCENTAGE).divide(new BigDecimal("100")));
			// Set ALV
			unitTaxCalculationInfo
					.setAnnualRentAfterDeduction(annualLettingValue.setScale(0, BigDecimal.ROUND_HALF_UP));

			unitTaxCalculationInfo = propertyTaxUtil.calculateApplicableTaxes(applicableTaxes, null,
					unitTaxCalculationInfo, installment, propertyDetail.getPropertyTypeMaster().getCode(),
					propertyDetail.getExtra_field5(), propertyDetail.getExtra_field4(), property);
		}
		unitTaxCalculationInfo.setInstDate(DateUtils.getDefaultFormattedDate(propertyDetail.getProperty()
				.getBasicProperty().getPropCreateDate()));
		return unitTaxCalculationInfo;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

}
