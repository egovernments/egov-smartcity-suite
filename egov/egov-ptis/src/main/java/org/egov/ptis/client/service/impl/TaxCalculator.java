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
package org.egov.ptis.client.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.client.model.MiscellaneousTax;
import org.egov.ptis.client.model.TaxCalculationInfo;
import org.egov.ptis.client.model.UnitTaxCalculationInfo;
import org.egov.ptis.client.service.TaxCalculatorInterface;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;

public class TaxCalculator implements TaxCalculatorInterface {

	private PropertyTaxUtil propertyTaxUtil;
	private PTISCacheManagerInteface ptisCachMgr = new PTISCacheManager();
	private BigDecimal totalTaxPayable = BigDecimal.ZERO;
	private static final Logger LOGGER = Logger.getLogger(TaxCalculator.class);
	private HashMap<Installment, TaxCalculationInfo> taxCalculationMap = new HashMap<Installment, TaxCalculationInfo>();
	SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	private static final BigDecimal BUILDING_VALUE_FACTOR = new BigDecimal(2 / 3);
	private static final BigDecimal SITE_VALUE_FACTOR = new BigDecimal(2 / 3);

	/**
	 *
	 * @param property
	 *            Property Object
	 * @param applicableTaxes
	 *            List of Applicable Taxes
	 * @param occDate
	 *            Minimum Occupancy Date among all the units
	 * @return
	 */
	@Override
	public HashMap<Installment, TaxCalculationInfo> calculatePropertyTax(Property property, Date occupationDate) {

		List<Installment> taxInstallments = propertyTaxUtil.getInstallmentListByStartDate(occupationDate);
		Boundary propertyZone = null;
		BigDecimal builtUpArea = BigDecimal.ZERO;
		BigDecimal mrv = BigDecimal.ZERO;
		BigDecimal buildingValue = BigDecimal.ZERO;
		BigDecimal siteValue = BigDecimal.ZERO;
		BigDecimal grossArv = BigDecimal.ZERO;
		BigDecimal depreciation = BigDecimal.ZERO;
		BigDecimal netArv = BigDecimal.ZERO;
		BigDecimal totalNetArv = BigDecimal.ZERO;

		List<String> applicableTaxes = propertyTaxUtil.prepareApplicableTaxes(property);

		propertyZone = property.getBasicProperty().getPropertyID().getZone();

		for (Installment installment : taxInstallments) {
			totalTaxPayable = BigDecimal.ZERO;
			TaxCalculationInfo taxCalculationInfo = this.addPropertyInfo(property);

			BigDecimal installmentWiseTaxPayable = BigDecimal.ZERO;
			BoundaryCategory boundaryCategory = null;

			Set<FloorIF> newFloorSet = property.getPropertyDetail().getFloorDetails();
			for (FloorIF floorIF : newFloorSet) {
				if (floorIF != null) {
					FloorImpl floorImpl = (FloorImpl) floorIF;
					builtUpArea = BigDecimal.ZERO;
					mrv = BigDecimal.ZERO;
					buildingValue = BigDecimal.ZERO;
					siteValue = BigDecimal.ZERO;
					grossArv = BigDecimal.ZERO;
					depreciation = BigDecimal.ZERO;
					netArv = BigDecimal.ZERO;
					totalNetArv = BigDecimal.ZERO;

					builtUpArea = BigDecimal.valueOf(floorImpl.getBuiltUpArea().getArea());

					mrv = builtUpArea.multiply(BigDecimal.valueOf(boundaryCategory.getCategory().getCategoryAmount()));
					buildingValue = mrv.multiply(BUILDING_VALUE_FACTOR);
					siteValue = mrv.multiply(SITE_VALUE_FACTOR);
					grossArv = buildingValue.multiply(new BigDecimal(12));
					depreciation = grossArv.multiply(
							BigDecimal.valueOf(floorImpl.getDepreciationMaster().getDepreciationPct())).divide(
							BigDecimal.valueOf(100));
					netArv = siteValue.multiply(new BigDecimal(12)).add(grossArv.subtract(depreciation));

					totalNetArv = totalNetArv.add(netArv);

					if (propertyTaxUtil.betweenOrBefore(occupationDate, installment.getFromDate(),
							installment.getToDate())) {

						boundaryCategory = propertyTaxUtil.getBoundaryCategory(propertyZone, installment, floorImpl
								.getPropertyUsage().getId(), occupationDate);
						List<UnitTaxCalculationInfo> calculationInfos = new ArrayList<UnitTaxCalculationInfo>();

						UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();
						unitTaxCalculationInfo.setBaseRateEffectiveDate(boundaryCategory.getFromDate());
						unitTaxCalculationInfo.setBaseRate(BigDecimal.valueOf(boundaryCategory.getCategory()
								.getCategoryAmount()));
						unitTaxCalculationInfo.setBuildingValue(buildingValue);
						unitTaxCalculationInfo.setSiteValue(siteValue);
						unitTaxCalculationInfo.setGrossARV(grossArv);
						unitTaxCalculationInfo.setDepreciation(depreciation);
						unitTaxCalculationInfo.setNetARV(netArv);
						calculationInfos.add(unitTaxCalculationInfo);

						calculateApplicableTaxes(applicableTaxes, unitTaxCalculationInfo, installment, property,
								boundaryCategory, taxCalculationInfo);

						installmentWiseTaxPayable = installmentWiseTaxPayable.add(unitTaxCalculationInfo
								.getTotalTaxPayable());

						totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
						taxCalculationInfo.addUnitTaxCalculationInfo(calculationInfos);
					}
				}
				taxCalculationMap.put(installment, taxCalculationInfo);
			}
		}
		return taxCalculationMap;
	}

	private TaxCalculationInfo addPropertyInfo(Property property) {
		TaxCalculationInfo taxCalculationInfo = new TaxCalculationInfo();
		// Add Property Info
		taxCalculationInfo.setPropertyOwnerName(ptisCachMgr.buildOwnerFullName(property.getPropertyOwnerSet()));
		taxCalculationInfo.setPropertyAddress(ptisCachMgr.buildAddressByImplemetation(property.getBasicProperty()
				.getAddress()));
		taxCalculationInfo.setHouseNumber(property.getBasicProperty().getAddress().getHouseNoBldgApt());
		taxCalculationInfo.setZone(property.getBasicProperty().getPropertyID().getZone().getName());
		taxCalculationInfo.setWard(property.getBasicProperty().getPropertyID().getWard().getName());
		taxCalculationInfo.setBlock(property.getBasicProperty().getPropertyID().getArea().getName());
		taxCalculationInfo.setLocality(property.getBasicProperty().getPropertyID().getLocality().getName());
		if (property.getPropertyDetail().getSitalArea() != null) {
			taxCalculationInfo.setPropertyArea(new BigDecimal(property.getPropertyDetail().getSitalArea().getArea()
					.toString()));
		}
		taxCalculationInfo.setPropertyType(property.getPropertyDetail().getPropertyTypeMaster().getType());
		taxCalculationInfo.setPropertyId(property.getBasicProperty().getUpicNo());
		return taxCalculationInfo;
	}

	/**
	 * Calculates applicable taxes
	 *
	 * <p>
	 * This method is used both to calculate taxes on unconsolidated ALV and to
	 * calculate taxes on consolidated ALV's. If taxNameAndALV map is null then
	 * use will be to calculate taxes on unconsolidated ALV else this map
	 * contains the ALV to be used to calculate respective tax
	 * </p>
	 *
	 * @param applicableTaxes
	 *            The DemandReasonMaster codes
	 * @param taxNameAndALV
	 *            a map containing taxName and ALV pair, determines ALV to be
	 *            used in calculating tax taxName
	 * @param unitTaxCalculationInfo
	 *            calculated tax details are stored in
	 *            <code> MiscellaneousTax <code>, this wil be addedd to
	 * 							<code> unitTaxCalculationInfo </code>
	 * @param installment
	 *            The installment for which tax has to be calculated
	 * @param propType
	 *            The property type code
	 * @param propTypeCategory
	 *            The property type category
	 * @param amenities
	 *            Amenties in case of Central Govt. property
	 * @param property
	 *            The property containing the property details
	 *
	 * @return {@link UnitTaxCalculationInfo}
	 */

	public UnitTaxCalculationInfo calculateApplicableTaxes(List<String> applicableTaxes,
			UnitTaxCalculationInfo unitTaxCalculationInfo, Installment installment, Property property,
			BoundaryCategory category, TaxCalculationInfo currentTaxCalc) {

		BigDecimal totalTaxPayable = BigDecimal.ZERO;
		EgDemandReasonDetails reasonDetail = null;
		BigDecimal alv = unitTaxCalculationInfo.getNetARV();
		BigDecimal calculatedTax = BigDecimal.ZERO;
		LOGGER.debug("calculateApplicableTaxes - ALV: " + alv);
		LOGGER.debug("calculateApplicableTaxes - applicableTaxes: " + applicableTaxes);

		for (String applicableTax : applicableTaxes) {
			reasonDetail = propertyTaxUtil.getDemandReasonDetails(applicableTax, alv, installment).get(0);
			calculatedTax = alv.multiply(reasonDetail.getPercentage().divide(new BigDecimal("100")));
			MiscellaneousTax miscellaneousTax = new MiscellaneousTax();
			miscellaneousTax.setTaxName(applicableTax);
			miscellaneousTax.setTotalCalculatedTax(calculatedTax);
			totalTaxPayable = totalTaxPayable.add(calculatedTax);
			unitTaxCalculationInfo.addMiscellaneousTaxes(miscellaneousTax);
		}
		unitTaxCalculationInfo.setTotalTaxPayable(totalTaxPayable);
		return unitTaxCalculationInfo;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}
	
	

}
