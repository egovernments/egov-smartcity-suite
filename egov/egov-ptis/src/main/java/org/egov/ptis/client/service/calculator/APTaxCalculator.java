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
package org.egov.ptis.client.service.calculator;

import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASERATE_BY_OCCUPANCY_ZONE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.handler.TaxCalculationInfoXmlHandler;
import org.egov.ptis.client.model.calculator.APMiscellaneousTax;
import org.egov.ptis.client.model.calculator.APTaxCalculationInfo;
import org.egov.ptis.client.model.calculator.APUnitTaxCalculationInfo;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.domain.service.calculator.PropertyTaxCalculator;
import org.springframework.beans.factory.annotation.Autowired;

//TODO name class as client specific
public class APTaxCalculator implements PropertyTaxCalculator {
	private static final Logger LOGGER = Logger.getLogger(APTaxCalculator.class);
	@Autowired
	private PersistenceService persistenceService;
	private BigDecimal totalTaxPayable = BigDecimal.ZERO;
	private HashMap<Installment, TaxCalculationInfo> taxCalculationMap = new HashMap<Installment, TaxCalculationInfo>();

	/**
	 *
	 * @param property
	 *            Property Object
	 * @param applicableTaxes
	 *            List of Applicable Taxes
	 * @param occupationDate
	 *            Minimum Occupancy Date among all the units
	 * @return
	 */
	@Override
	public HashMap<Installment, TaxCalculationInfo> calculatePropertyTax(Property property, Date occupationDate) {

		Boundary propertyZone = null;
		BigDecimal totalNetArv = BigDecimal.ZERO;
		BoundaryCategory boundaryCategory = null;
		// TODO move method prepareApplicableTaxes to tax calculator
		List<String> applicableTaxes = prepareApplicableTaxes(property);
		List<Installment> taxInstallments = getInstallmentListByStartDate(occupationDate);
		propertyZone = property.getBasicProperty().getPropertyID().getZone();

		for (Installment installment : taxInstallments) {
			totalTaxPayable = BigDecimal.ZERO;
			APTaxCalculationInfo taxCalculationInfo = this.addPropertyInfo(property);

			if (betweenOrBefore(occupationDate, installment.getFromDate(), installment.getToDate())) {
				for (Floor floorIF : property.getPropertyDetail().getFloorDetails()) {
					if (floorIF != null) {
						// TODO think about, these beans to be client specific
						boundaryCategory = getBoundaryCategory(propertyZone, installment, floorIF.getPropertyUsage()
								.getId(), occupationDate);
						APUnitTaxCalculationInfo unitTaxCalculationInfo = prepareUnitCalcInfo(floorIF, boundaryCategory);
						totalNetArv = totalNetArv.add(unitTaxCalculationInfo.getNetARV());

						calculateApplicableTaxes(applicableTaxes, unitTaxCalculationInfo, installment, property,
								boundaryCategory, taxCalculationInfo);

						totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
						taxCalculationInfo.addUnitTaxCalculationInfo(unitTaxCalculationInfo);
					}
				}
				taxCalculationInfo.setTotalNetARV(totalNetArv);
				taxCalculationInfo.setTotalTaxPayable(totalTaxPayable);
				taxCalculationInfo.setTaxCalculationInfoXML(generateTaxCalculationXML(taxCalculationInfo));
				taxCalculationMap.put(installment, taxCalculationInfo);
			}
		}
		return taxCalculationMap;
	}

	private APUnitTaxCalculationInfo prepareUnitCalcInfo(Floor floor, BoundaryCategory boundaryCategory) {
		APUnitTaxCalculationInfo unitTaxCalculationInfo = new APUnitTaxCalculationInfo();
		BigDecimal builtUpArea = BigDecimal.ZERO;
		BigDecimal floorMrv = BigDecimal.ZERO;
		BigDecimal floorBuildingValue = BigDecimal.ZERO;
		BigDecimal floorSiteValue = BigDecimal.ZERO;
		BigDecimal floorGrossArv = BigDecimal.ZERO;
		BigDecimal floorDepreciation = BigDecimal.ZERO;
		BigDecimal floorNetArv = BigDecimal.ZERO;

		builtUpArea = BigDecimal.valueOf(floor.getBuiltUpArea().getArea());
		floorMrv = calculateFloorMrv(builtUpArea, boundaryCategory);
		floorBuildingValue = calculateFloorBuildingValue(floorMrv);
		floorSiteValue = calculateFloorSiteValue(floorMrv);
		floorGrossArv = floorBuildingValue.multiply(new BigDecimal(12));
		floorDepreciation = calculateFloorDepreciation(floorGrossArv, floor);
		floorNetArv = floorSiteValue.multiply(new BigDecimal(12)).add(floorGrossArv.subtract(floorDepreciation));

		unitTaxCalculationInfo.setFloorNumber(FLOOR_MAP.get(floor.getFloorNo()));
		unitTaxCalculationInfo.setBaseRateEffectiveDate(boundaryCategory.getFromDate());
		unitTaxCalculationInfo.setBaseRate(BigDecimal.valueOf(boundaryCategory.getCategory().getCategoryAmount()));
		unitTaxCalculationInfo.setMrv(floorMrv);
		unitTaxCalculationInfo.setBuildingValue(floorBuildingValue);
		unitTaxCalculationInfo.setSiteValue(floorSiteValue);
		unitTaxCalculationInfo.setGrossARV(floorGrossArv);
		unitTaxCalculationInfo.setDepreciation(floorDepreciation);
		unitTaxCalculationInfo.setNetARV(floorNetArv);
		return unitTaxCalculationInfo;
	}

	private APTaxCalculationInfo addPropertyInfo(Property property) {
		APTaxCalculationInfo taxCalculationInfo = new APTaxCalculationInfo();
		// Add Property Info
		taxCalculationInfo.setPropertyOwnerName(property.getBasicProperty().getFullOwnerName());
		taxCalculationInfo.setPropertyAddress(property.getBasicProperty()
				.getAddress().toString());
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

	public APUnitTaxCalculationInfo calculateApplicableTaxes(List<String> applicableTaxes,
			APUnitTaxCalculationInfo unitTaxCalculationInfo, Installment installment, Property property,
			BoundaryCategory category, APTaxCalculationInfo currentTaxCalc) {

		BigDecimal totalTaxPayable = BigDecimal.ZERO;
		EgDemandReasonDetails reasonDetail = null;
		BigDecimal alv = unitTaxCalculationInfo.getNetARV();
		BigDecimal calculatedTax = BigDecimal.ZERO;
		LOGGER.debug("calculateApplicableTaxes - ALV: " + alv);
		LOGGER.debug("calculateApplicableTaxes - applicableTaxes: " + applicableTaxes);

		for (String applicableTax : applicableTaxes) {
			reasonDetail = getDemandReasonDetails(applicableTax, alv, installment).get(0);
			calculatedTax = (alv.multiply(reasonDetail.getPercentage().divide(new BigDecimal("100")))).setScale(0,
					BigDecimal.ROUND_HALF_UP);
			APMiscellaneousTax miscellaneousTax = new APMiscellaneousTax();
			miscellaneousTax.setTaxName(applicableTax);
			miscellaneousTax.setTotalCalculatedTax(calculatedTax);
			totalTaxPayable = totalTaxPayable.add(calculatedTax);
			unitTaxCalculationInfo.addMiscellaneousTaxes(miscellaneousTax);
		}
		unitTaxCalculationInfo.setTotalTaxPayable(totalTaxPayable);
		return unitTaxCalculationInfo;
	}

	/**
	 * Returns the applicable taxes for the given property
	 *
	 * @param property
	 * @return List of taxes
	 */
	private List<String> prepareApplicableTaxes(Property property) {
		LOGGER.debug("Entered into prepareApplTaxes");
		LOGGER.debug("prepareApplTaxes: property: " + property);
		List<String> applicableTaxes = new ArrayList<String>();
		String propType = property.getPropertyDetail().getPropertyTypeMaster().getCode();

		applicableTaxes.add(DEMANDRSN_CODE_GENERAL_TAX);
		applicableTaxes.add(DEMANDRSN_CODE_LIBRARY_CESS);
		applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS);
		applicableTaxes.add(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY);

		LOGGER.debug("prepareApplTaxes: applicableTaxes: " + applicableTaxes);
		LOGGER.debug("Exiting from prepareApplTaxes");
		return applicableTaxes;
	}

	public List<Installment> getInstallmentListByStartDate(Date startDate) {
		return persistenceService.findAllByNamedQuery(QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR, startDate,
				startDate, PTMODULENAME);
	}

	public BoundaryCategory getBoundaryCategory(Boundary zone, Installment installment, Long usageId, Date occupancyDate) {
		List<BoundaryCategory> categories = new ArrayList<BoundaryCategory>();

		categories = persistenceService.findAllByNamedQuery(QUERY_BASERATE_BY_OCCUPANCY_ZONE, zone.getId(), usageId,
				occupancyDate, installment.getToDate());

		LOGGER.debug("baseRentOfUnit - Installment : " + installment);
		return categories.get(0);
	}

	public List<EgDemandReasonDetails> getDemandReasonDetails(String demandReasonCode,
			BigDecimal grossAnnualRentAfterDeduction, Installment installment) {
		return persistenceService.findAllByNamedQuery(QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT,
				demandReasonCode, grossAnnualRentAfterDeduction, installment.getFromDate(), installment.getToDate());
	}

	public Map<String, BigDecimal> getMiscTaxesForProp(List<UnitTaxCalculationInfo> unitTaxCalcInfos) {

		Map<String, BigDecimal> taxMap = new HashMap<String, BigDecimal>();
		for (UnitTaxCalculationInfo unitTaxCalcInfo : unitTaxCalcInfos) {
			for (MiscellaneousTax miscTax : unitTaxCalcInfo.getMiscellaneousTaxes()) {
				if (taxMap.get(miscTax.getTaxName()) == null) {
					taxMap.put(miscTax.getTaxName(), miscTax.getTotalCalculatedTax());
				} else {
					taxMap.put(miscTax.getTaxName(),
							taxMap.get(miscTax.getTaxName()).add(miscTax.getTotalCalculatedTax()));
				}
			}
		}

		return taxMap;
	}

	public String generateTaxCalculationXML(TaxCalculationInfo taxCalculationInfo) {
		TaxCalculationInfoXmlHandler handler = new TaxCalculationInfoXmlHandler();
		String taxCalculationInfoXML = "";

		if (taxCalculationInfo != null) {

			taxCalculationInfoXML = handler.toXML(taxCalculationInfo);

		}

		return taxCalculationInfoXML;

	}

	public Boolean between(Date date, Date fromDate, Date toDate) {
		return ((date.after(fromDate) || date.equals(fromDate)) && date.before(toDate) || date.equals(toDate));
	}

	public Boolean betweenOrBefore(Date date, Date fromDate, Date toDate) {
		Boolean result = between(date, fromDate, toDate) || date.before(fromDate);
		return result;
	}

	private BigDecimal calculateFloorMrv(BigDecimal builtUpArea, BoundaryCategory boundaryCategory) {
		return builtUpArea.multiply(BigDecimal.valueOf(boundaryCategory.getCategory().getCategoryAmount()));
	}

	private BigDecimal calculateFloorBuildingValue(BigDecimal floorMrv) {
		return floorMrv.multiply(new BigDecimal(0.66));
	}

	private BigDecimal calculateFloorSiteValue(BigDecimal floorMrv) {
		return floorMrv.multiply(new BigDecimal(0.33));
	}

	private BigDecimal calculateFloorDepreciation(BigDecimal grossArv, Floor floor) {
		return grossArv.multiply(BigDecimal.valueOf(floor.getDepreciationMaster().getDepreciationPct())).divide(
				BigDecimal.valueOf(100));
	}

}
