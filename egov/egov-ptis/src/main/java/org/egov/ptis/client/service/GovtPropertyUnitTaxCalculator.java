/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.client.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infstr.utils.DateUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.client.model.UnitTaxCalculationInfo;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;

public class GovtPropertyUnitTaxCalculator {

	private static final Logger LOGGER = Logger.getLogger(GovtPropertyUnitTaxCalculator.class);
	private PropertyTaxUtil propertyTaxUtil;
	private UnitTaxCalculationInfo unitTaxCalculationInfo;

	public UnitTaxCalculationInfo calculateUnitTax(PropertyDetail propertyDetail, Boundary propertyArea,
			Installment installment, Long categoryId, Property property) {

		BigDecimal annualLettingValue = BigDecimal.ZERO;

		List<String> applicableTaxes = PropertyTaxUtil.prepareApplicableTaxes(property);
		LOGGER.debug("calculatePropertyTax - Govt. Property, applicableTaxes = " + applicableTaxes);

		// Add Unit Info
		unitTaxCalculationInfo = new UnitTaxCalculationInfo();
		unitTaxCalculationInfo.setFloorNumber(propertyTaxUtil.getFloorStr(0));
		unitTaxCalculationInfo.setFloorNumberInteger(new Integer(0));
		unitTaxCalculationInfo.setUnitNumber(1);

		if (PropertyTaxUtil.isPropertyModified(property)) {
			unitTaxCalculationInfo.setOccpancyDate(new Date(propertyDetail.getEffective_date().getTime()));
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
		if (propertyDetail.getPropertyTypeMaster().getCode().equals(PropertyTaxConstants.PROPTYPE_STATE_GOVT)) {
			annualLettingValue = buildingCost.multiply(new BigDecimal(
					PropertyTaxConstants.STATEGOVT_BUILDING_ALV_PERCENTAGE).divide(new BigDecimal("100")));
			// Set ALV
			unitTaxCalculationInfo
					.setAnnualRentAfterDeduction(annualLettingValue.setScale(0, BigDecimal.ROUND_HALF_UP));

			unitTaxCalculationInfo = propertyTaxUtil.calculateApplicableTaxes(applicableTaxes, null,
					unitTaxCalculationInfo, installment, propertyDetail.getPropertyTypeMaster().getCode(),
					propertyDetail.getExtra_field5(), propertyDetail.getExtra_field4(), property, null, null);
		}
		/**
		 * Calculate Tax for Central Government Buildings
		 */
		else {
			annualLettingValue = buildingCost.multiply(new BigDecimal(
					PropertyTaxConstants.CENTRALGOVT_BUILDING_ALV_PERCENTAGE).divide(new BigDecimal("100")));
			// Set ALV
			unitTaxCalculationInfo
					.setAnnualRentAfterDeduction(annualLettingValue.setScale(0, BigDecimal.ROUND_HALF_UP));

			unitTaxCalculationInfo = propertyTaxUtil.calculateApplicableTaxes(applicableTaxes, null,
					unitTaxCalculationInfo, installment, propertyDetail.getPropertyTypeMaster().getCode(),
					propertyDetail.getExtra_field5(), propertyDetail.getExtra_field4(), property, null, null);
		}
		unitTaxCalculationInfo.setInstDate(DateUtils.getDefaultFormattedDate(propertyDetail.getProperty()
				.getBasicProperty().getPropCreateDate()));
		return unitTaxCalculationInfo;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

}
