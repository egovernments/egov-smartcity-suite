/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.client.service.calculator;

import static org.egov.ptis.constants.PropertyTaxConstants.BIGDECIMAL_100;
import static org.egov.ptis.constants.PropertyTaxConstants.BPA_DEVIATION_TAXPERC_1_10;
import static org.egov.ptis.constants.PropertyTaxConstants.BPA_DEVIATION_TAXPERC_ABOVE_11;
import static org.egov.ptis.constants.PropertyTaxConstants.BPA_DEVIATION_TAXPERC_NOT_DEFINED;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_DDMMYYY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PRIMARY_SERVICE_CHARGES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASERATE_BY_OCCUPANCY_ZONE;
import static org.egov.ptis.constants.PropertyTaxConstants.SQUARE_YARD_TO_SQUARE_METER_VALUE;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.handler.TaxCalculationInfoXmlHandler;
import org.egov.ptis.client.model.calculator.APMiscellaneousTax;
import org.egov.ptis.client.model.calculator.APTaxCalculationInfo;
import org.egov.ptis.client.model.calculator.APUnitTaxCalculationInfo;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.domain.service.calculator.PropertyTaxCalculator;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class APTaxCalculator implements PropertyTaxCalculator {
    private static final Logger LOGGER = Logger.getLogger(APTaxCalculator.class);
    private static final BigDecimal RESD_OWNER_DEPRECIATION = new BigDecimal(40);
    private static final BigDecimal SEASHORE_ADDITIONAL_DEPRECIATION = new BigDecimal(5);
    private static final String UNAUTHPENALTY_STARTDATE = "01/04/1987";
    private static final BigDecimal BUILDING_VALUE = new BigDecimal(2).divide(new BigDecimal(3), 5, BigDecimal.ROUND_HALF_UP);
    private static final BigDecimal SITE_VALUE = new BigDecimal(1).divide(new BigDecimal(3), 5, BigDecimal.ROUND_HALF_UP);
    private Boolean isCorporation = Boolean.FALSE;
    private Boolean isSeaShoreULB = Boolean.FALSE;
    private Boolean isPrimaryServiceChrApplicable = Boolean.FALSE;
    private final HashMap<Installment, TaxCalculationInfo> taxCalculationMap = new HashMap<>();
    private Properties taxRateProps = null;

    @SuppressWarnings("rawtypes")
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    /**
     * @param property Property Object
     * @param applicableTaxes List of Applicable Taxes
     * @param occupationDate Minimum Occupancy Date among all the units
     * @return
     * @throws TaxCalculatorExeption
     */
    @Override
    public HashMap<Installment, TaxCalculationInfo> calculatePropertyTax(final Property property,
            final Date occupationDate) throws TaxCalculatorExeption {
        Boundary propertyZone;
        BigDecimal totalNetArv;
        BoundaryCategory boundaryCategory;
        taxRateProps = propertyTaxUtil.loadTaxRates();
        isCorporation = propertyTaxUtil.isCorporation();
        isSeaShoreULB = propertyTaxUtil.isSeaShoreULB();
        if (isCorporation)
            isPrimaryServiceChrApplicable = propertyTaxUtil.isPrimaryServiceApplicable();

        final List<String> applicableTaxes = prepareApplicableTaxes(property);
        final List<Installment> taxInstallments = propertyTaxUtil.getInstallmentsListByEffectiveDate(occupationDate);
        propertyZone = property.getBasicProperty().getPropertyID().getZone();

        for (final Installment installment : taxInstallments) {
            BigDecimal totalTaxPayable = BigDecimal.ZERO;
            totalNetArv = BigDecimal.ZERO;
            final APTaxCalculationInfo taxCalculationInfo = addPropertyInfo(property);

            if (betweenOrBefore(occupationDate, installment.getFromDate(), installment.getToDate())) {
                if (property.getPropertyDetail().getPropertyTypeMaster().getCode().equals(OWNERSHIP_TYPE_VAC_LAND)
                        || property.getPropertyDetail().isAppurtenantLandChecked() != null && property
                                .getPropertyDetail().isAppurtenantLandChecked()) {
                    final APUnitTaxCalculationInfo unitTaxCalculationInfo = calculateVacantLandTax(property,
                            occupationDate, applicableTaxes, installment);
                    totalNetArv = totalNetArv.add(unitTaxCalculationInfo.getNetARV());
                    taxCalculationInfo.addUnitTaxCalculationInfo(unitTaxCalculationInfo);
                    totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
                }

                for (final Floor floorIF : property.getPropertyDetail().getFloorDetails())
                    if (betweenOrBefore(floorIF.getOccupancyDate(), installment.getFromDate(), installment.getToDate())) {
                        boundaryCategory = getBoundaryCategory(propertyZone, installment, floorIF.getPropertyUsage()
                                .getId(), occupationDate, floorIF.getStructureClassification().getId());
                        final APUnitTaxCalculationInfo unitTaxCalculationInfo = calculateNonVacantTax(property,
                                floorIF, boundaryCategory, applicableTaxes, installment);
                        totalNetArv = totalNetArv.add(unitTaxCalculationInfo.getNetARV());

                        totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
                        taxCalculationInfo.addUnitTaxCalculationInfo(unitTaxCalculationInfo);
                    }
                taxCalculationInfo.setTotalNetARV(totalNetArv);
                taxCalculationInfo.setTotalTaxPayable(totalTaxPayable);
                taxCalculationInfo.setTaxCalculationInfoXML(generateTaxCalculationXML(taxCalculationInfo));
                taxCalculationMap.put(installment, taxCalculationInfo);
            }
        }
        return taxCalculationMap;
    }

    private APUnitTaxCalculationInfo calculateNonVacantTax(final Property property, final Floor floor,
            final BoundaryCategory boundaryCategory, final List<String> applicableTaxes, final Installment installment) {
        final APUnitTaxCalculationInfo unitTaxCalculationInfo = new APUnitTaxCalculationInfo();
        BigDecimal builtUpArea;
        BigDecimal floorMrv;
        BigDecimal floorBuildingValue;
        BigDecimal floorSiteValue;
        BigDecimal floorGrossArv;
        BigDecimal floorDepreciation;
        BigDecimal floorNetArv;

        builtUpArea = BigDecimal.valueOf(floor.getBuiltUpArea().getArea());
        floorMrv = calculateFloorMrv(builtUpArea, boundaryCategory);
        floorBuildingValue = calculateFloorBuildingValue(floorMrv);
        floorSiteValue = calculateFloorSiteValue(floorMrv);
        floorGrossArv = floorBuildingValue.multiply(new BigDecimal(12));
        floorDepreciation = calculateFloorDepreciation(floorGrossArv, floor);
        floorNetArv = floorSiteValue.multiply(new BigDecimal(12)).add(floorGrossArv.subtract(floorDepreciation));
        unitTaxCalculationInfo.setFloorNumber(FLOOR_MAP.get(floor.getFloorNo()));
        unitTaxCalculationInfo.setFloorArea(builtUpArea);
        unitTaxCalculationInfo.setBaseRateEffectiveDate(boundaryCategory.getFromDate());
        unitTaxCalculationInfo.setBaseRate(BigDecimal.valueOf(boundaryCategory.getCategory().getCategoryAmount()));
        unitTaxCalculationInfo.setMrv(floorMrv);
        unitTaxCalculationInfo.setBuildingValue(floorBuildingValue);
        unitTaxCalculationInfo.setSiteValue(floorSiteValue);
        unitTaxCalculationInfo.setGrossARV(floorGrossArv);
        unitTaxCalculationInfo.setDepreciation(floorDepreciation);
        unitTaxCalculationInfo.setUnitUsage(floor.getPropertyUsage().getUsageCode());
        unitTaxCalculationInfo.setUnitOccupation(floor.getPropertyOccupation().getOccupancyCode());
        unitTaxCalculationInfo.setUnitStructure(floor.getStructureClassification().getConstrTypeCode());
        unitTaxCalculationInfo.setNetARV(floorNetArv.setScale(0, BigDecimal.ROUND_HALF_UP));
        final BigDecimal unAuthDeviationPerc = getUnAuthDeviationPerc(floor);
        calculateApplicableTaxes(applicableTaxes, unitTaxCalculationInfo, installment, property.getPropertyDetail()
                .getPropertyTypeMaster().getCode(), floor, unAuthDeviationPerc);

        return unitTaxCalculationInfo;
    }

    private APTaxCalculationInfo addPropertyInfo(final Property property) {
        final APTaxCalculationInfo taxCalculationInfo = new APTaxCalculationInfo();
        // Add Property Info
        final PropertyID propertyId = property.getBasicProperty().getPropertyID();
        taxCalculationInfo.setPropertyOwnerName(property.getBasicProperty().getFullOwnerName());
        taxCalculationInfo.setPropertyAddress(property.getBasicProperty().getAddress().toString());
        taxCalculationInfo.setHouseNumber(property.getBasicProperty().getAddress().getHouseNoBldgApt());
        taxCalculationInfo.setZone(propertyId.getZone().getName());
        taxCalculationInfo.setWard(propertyId.getWard().getName());
        taxCalculationInfo.setBlock(propertyId.getArea() != null ? propertyId.getArea().getName() : "");
        taxCalculationInfo.setLocality(property.getBasicProperty().getPropertyID().getLocality().getName());
        if (property.getPropertyDetail().getSitalArea().getArea() != null)
            taxCalculationInfo.setPropertyArea(new BigDecimal(property.getPropertyDetail().getSitalArea().getArea()
                    .toString()));
        taxCalculationInfo.setPropertyType(property.getPropertyDetail().getPropertyTypeMaster().getType());
        taxCalculationInfo.setPropertyId(property.getBasicProperty().getUpicNo());
        return taxCalculationInfo;
    }

    public APUnitTaxCalculationInfo calculateApplicableTaxes(final List<String> applicableTaxes,
            final APUnitTaxCalculationInfo unitTaxCalculationInfo, final Installment installment,
            final String propTypeCode, final Floor floor, final BigDecimal unAuthDeviationPerc) {

        BigDecimal totalHalfTaxPayable = BigDecimal.ZERO;
        final BigDecimal alv = unitTaxCalculationInfo.getNetARV();
        BigDecimal generalTax = BigDecimal.ZERO;
        BigDecimal educationTax = BigDecimal.ZERO;
        BigDecimal halfYearHeadTax;
        BigDecimal taxRatePerc;
        LOGGER.debug("calculateApplicableTaxes - ALV: " + alv);
        LOGGER.debug("calculateApplicableTaxes - applicableTaxes: " + applicableTaxes);

        for (final String applicableTax : applicableTaxes) {
            halfYearHeadTax = BigDecimal.ZERO;
            if (applicableTax.equals(DEMANDRSN_CODE_GENERAL_TAX) || applicableTax.equals(DEMANDRSN_CODE_VACANT_TAX)) {
                if (applicableTax.equals(DEMANDRSN_CODE_VACANT_TAX)) {
                    taxRatePerc = getTaxRate(DEMANDRSN_CODE_VACANT_TAX);
                    halfYearHeadTax = getHalfYearTax(alv.multiply(taxRatePerc.divide(new BigDecimal("100"))).setScale(
                            2, BigDecimal.ROUND_HALF_UP));
                } else {
                    if (floor != null && floor.getPropertyUsage().getIsResidential())
                        taxRatePerc = getTaxRate(DEMANDRSN_CODE_GENERAL_TAX + "_RESD");
                    else
                        taxRatePerc = getTaxRate(DEMANDRSN_CODE_GENERAL_TAX + "_NR");
                    halfYearHeadTax = alv.multiply(taxRatePerc.divide(new BigDecimal("100"))).setScale(2,
                            BigDecimal.ROUND_HALF_UP);
                }
                halfYearHeadTax = taxIfGovtProperty(propTypeCode, halfYearHeadTax);
                generalTax = halfYearHeadTax;
            }
            if (applicableTax.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS)) {
                educationTax = alv.multiply(getTaxRate(DEMANDRSN_CODE_EDUCATIONAL_CESS).divide(new BigDecimal("100")))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                halfYearHeadTax = educationTax;
            }
            if (applicableTax.equals(DEMANDRSN_CODE_LIBRARY_CESS))
                halfYearHeadTax = generalTax.add(educationTax)
                        .multiply(getTaxRate(DEMANDRSN_CODE_LIBRARY_CESS).divide(new BigDecimal("100")))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);

            if (halfYearHeadTax.compareTo(BigDecimal.ZERO) > 0) {
                totalHalfTaxPayable = totalHalfTaxPayable.add(halfYearHeadTax);
                createMiscTax(applicableTax, halfYearHeadTax, unitTaxCalculationInfo);
            }
        }
        // calculating Un Authorized Penalty
        if (!propTypeCode.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            try {
                if (floor != null && floor.getConstructionDate()
                        .after(new SimpleDateFormat(DATE_FORMAT_DDMMYYY).parse(UNAUTHPENALTY_STARTDATE)))
                    if (!(checkUnAuthDeviationPerc(unAuthDeviationPerc)
                            || unAuthDeviationPerc != null && unAuthDeviationPerc.intValue() == 0)) {
                        halfYearHeadTax = calculateUnAuthPenalty(unAuthDeviationPerc, totalHalfTaxPayable);
                        totalHalfTaxPayable = totalHalfTaxPayable.add(halfYearHeadTax);
                        createMiscTax(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, halfYearHeadTax, unitTaxCalculationInfo);
                    }
            } catch (final ParseException e) {
                LOGGER.error(e);
            }
        // calculating Public Service Charges
        if (isPrimaryServiceChrApplicable) {
            halfYearHeadTax = roundOffToNearestEven(calcPublicServiceCharges(totalHalfTaxPayable));
            totalHalfTaxPayable = totalHalfTaxPayable.add(halfYearHeadTax);
            createMiscTax(DEMANDRSN_CODE_PRIMARY_SERVICE_CHARGES, halfYearHeadTax, unitTaxCalculationInfo);
        }
        unitTaxCalculationInfo.setTotalTaxPayable(totalHalfTaxPayable);
        return unitTaxCalculationInfo;
    }

    private Boolean checkUnAuthDeviationPerc(final BigDecimal unAuthDeviationPerc) {
        return unAuthDeviationPerc == null || "0".equals(unAuthDeviationPerc.toString())
                || "-1".equals(unAuthDeviationPerc.toString());
    }

    /**
     * Returns the applicable taxes for the given property
     *
     * @param property
     * @return List of taxes
     */
    private List<String> prepareApplicableTaxes(final Property property) {
        LOGGER.debug("Entered into prepareApplTaxes");
        LOGGER.debug("prepareApplTaxes: property: " + property);
        final List<String> applicableTaxes = new ArrayList<>();
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equals(OWNERSHIP_TYPE_VAC_LAND)) {
            applicableTaxes.add(DEMANDRSN_CODE_GENERAL_TAX);
            applicableTaxes.add(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY);
            applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS);
        } else
            applicableTaxes.add(DEMANDRSN_CODE_VACANT_TAX);
        applicableTaxes.add(DEMANDRSN_CODE_LIBRARY_CESS);
        if (isCorporation)
            applicableTaxes.add(DEMANDRSN_CODE_SEWERAGE_TAX);
        if (isPrimaryServiceChrApplicable)
            applicableTaxes.add(DEMANDRSN_CODE_PRIMARY_SERVICE_CHARGES);
        LOGGER.debug("prepareApplTaxes: applicableTaxes: " + applicableTaxes);
        LOGGER.debug("Exiting from prepareApplTaxes");
        return applicableTaxes;
    }

    @SuppressWarnings("unchecked")
    private BoundaryCategory getBoundaryCategory(final Boundary zone, final Installment installment,
            final Long usageId, final Date occupancyDate, final Long classification) throws TaxCalculatorExeption {
        final List<BoundaryCategory> categories = persistenceService.findAllByNamedQuery(QUERY_BASERATE_BY_OCCUPANCY_ZONE,
                zone.getId(), usageId,
                classification, occupancyDate, installment.getToDate());

        LOGGER.debug("baseRentOfUnit - Installment : " + installment);

        if (categories.isEmpty())
            throw new TaxCalculatorExeption("There are no Unit rates defined for chosen combinations, zone : "
                    + zone.getName() + " usageId : " + usageId + " classification : " + classification
                    + " occupancyDate : " + occupancyDate);
        else
            return categories.get(0);
    }

    public Map<String, BigDecimal> getMiscTaxesForProp(final List<UnitTaxCalculationInfo> unitTaxCalcInfos) {

        final Map<String, BigDecimal> taxMap = new HashMap<>();
        for (final UnitTaxCalculationInfo unitTaxCalcInfo : unitTaxCalcInfos)
            for (final MiscellaneousTax miscTax : unitTaxCalcInfo.getMiscellaneousTaxes())
                if (taxMap.get(miscTax.getTaxName()) == null)
                    taxMap.put(miscTax.getTaxName(), miscTax.getTotalCalculatedTax());
                else
                    taxMap.put(miscTax.getTaxName(),
                            taxMap.get(miscTax.getTaxName()).add(miscTax.getTotalCalculatedTax()));

        for (final Map.Entry<String, BigDecimal> entry : taxMap.entrySet())
            entry.setValue(entry.getValue().setScale(0, BigDecimal.ROUND_HALF_UP));
        return taxMap;
    }

    private APUnitTaxCalculationInfo calculateVacantLandTax(final Property property, final Date occupancyDate,
            final List<String> applicableTaxes, final Installment installment) {
        final APUnitTaxCalculationInfo unitTaxCalculationInfo = new APUnitTaxCalculationInfo();

        unitTaxCalculationInfo.setFloorNumber("VACANT");
        unitTaxCalculationInfo.setBaseRateEffectiveDate(occupancyDate);
        unitTaxCalculationInfo.setCapitalValue(new BigDecimal(property.getPropertyDetail().getCurrentCapitalValue()));
        unitTaxCalculationInfo.setMarketValue(property.getPropertyDetail().getMarketValue() != null
                ? new BigDecimal(property.getPropertyDetail().getMarketValue()) : BigDecimal.ZERO);
        unitTaxCalculationInfo.setNetARV(
                unitTaxCalculationInfo.getCapitalValue().compareTo(unitTaxCalculationInfo.getMarketValue()) < 0
                        ? unitTaxCalculationInfo.getMarketValue() : unitTaxCalculationInfo.getCapitalValue());

        calculateApplicableTaxes(applicableTaxes, unitTaxCalculationInfo, installment, property.getPropertyDetail()
                .getPropertyTypeMaster().getCode(), null, null);

        return unitTaxCalculationInfo;
    }

    public String generateTaxCalculationXML(final TaxCalculationInfo taxCalculationInfo) {
        final TaxCalculationInfoXmlHandler handler = new TaxCalculationInfoXmlHandler();
        String taxCalculationInfoXML = "";

        if (taxCalculationInfo != null)
            taxCalculationInfoXML = handler.toXML(taxCalculationInfo);
        return taxCalculationInfoXML;
    }

    private Boolean between(final Date date, final Date fromDate, final Date toDate) {
        return (date.after(fromDate) || date.equals(fromDate)) && date.before(toDate) || date.equals(toDate);
    }

    private Boolean betweenOrBefore(final Date date, final Date fromDate, final Date toDate) {
        return between(date, fromDate, toDate) || date.before(fromDate);
    }

    private BigDecimal calculateFloorMrv(final BigDecimal builtUpArea, final BoundaryCategory boundaryCategory) {
        return builtUpArea.multiply(BigDecimal.valueOf(boundaryCategory.getCategory().getCategoryAmount()));
    }

    private BigDecimal calculateFloorBuildingValue(final BigDecimal floorMrv) {
        return floorMrv.multiply(BUILDING_VALUE).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculateFloorSiteValue(final BigDecimal floorMrv) {
        return floorMrv.multiply(SITE_VALUE).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculateFloorDepreciation(final BigDecimal grossArv, final Floor floor) {
        BigDecimal depreciationPct;
        if (floor.getPropertyOccupation().getOccupancyCode().equals(PropertyTaxConstants.OCC_OWNER)
                && floor.getPropertyUsage().getIsResidential())
            depreciationPct = RESD_OWNER_DEPRECIATION;
        else
            depreciationPct = BigDecimal.valueOf(floor.getDepreciationMaster().getDepreciationPct());
        if (isSeaShoreULB)
            depreciationPct = depreciationPct.add(SEASHORE_ADDITIONAL_DEPRECIATION);
        return grossArv.multiply(depreciationPct).divide(BigDecimal.valueOf(100));
    }

    public BigDecimal roundOffToNearestEven(final BigDecimal amount) {
        BigDecimal roundedAmt;
        final BigDecimal remainder = amount.remainder(new BigDecimal(2));
        /*
         * if reminder is less than 1, subtract reminder amount from passed amount else reminder is greater than 1, subtract
         * reminder amount from passed amount and add 5 to result amount
         */
        if (remainder.compareTo(new BigDecimal("1")) == 1)
            roundedAmt = amount.subtract(remainder).add(new BigDecimal(2));
        else
            roundedAmt = amount.subtract(remainder);
        return roundedAmt;
    }

    public BigDecimal convertYardToSquareMeters(final Float vacantLandArea) {
        final Float areaInSqMts = vacantLandArea * new Float(SQUARE_YARD_TO_SQUARE_METER_VALUE);
        return new BigDecimal(areaInSqMts).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal taxIfGovtProperty(final String propTypeCode, final BigDecimal tax) {
        BigDecimal amount = BigDecimal.ZERO;
        if (propTypeCode.equals(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_335))
            amount = tax.multiply(BigDecimal.valueOf(0.335));
        if (propTypeCode.equals(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_50))
            amount = tax.multiply(BigDecimal.valueOf(0.5));
        if (propTypeCode.equals(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_75))
            amount = tax.multiply(BigDecimal.valueOf(0.75));
        return amount;
    }

    private BigDecimal getUnAuthDeviationPerc(final Floor floor) {
        BigDecimal deviationPerc = null;
        BigDecimal diffArea;
        final BigDecimal plinthArea = new BigDecimal(floor.getBuiltUpArea().getArea()).setScale(2, BigDecimal.ROUND_HALF_UP);
        final BigDecimal buildingPlanPlinthArea = floor.getBuildingPlanPlinthArea() != null && floor
                .getBuildingPlanPlinthArea().getArea() != null ? new BigDecimal(floor.getBuildingPlanPlinthArea()
                        .getArea()).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        if (buildingPlanPlinthArea.compareTo(BigDecimal.ZERO) == 0)
            deviationPerc = new BigDecimal(100);
        else if (plinthArea.compareTo(buildingPlanPlinthArea) > 0) {
            diffArea = plinthArea.subtract(buildingPlanPlinthArea);
            deviationPerc = diffArea.divide(buildingPlanPlinthArea, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
        }
        return deviationPerc;
    }

    private BigDecimal calculateUnAuthPenalty(final BigDecimal deviationPerc, final BigDecimal totalPropertyTax) {
        BigDecimal unAuthPenalty = BigDecimal.ZERO;
        /*
         * deviationPerc between 1-10, Penalty perc = 25%, deviationPerc > 10, Penalty perc = 50%, no Building plan details,
         * Penalty perc = 100%
         */
        if (deviationPerc != null && deviationPerc.compareTo(BigDecimal.ZERO) > 0)
            if (deviationPerc.compareTo(BigDecimal.ZERO) > 0 && deviationPerc.compareTo(BigDecimal.TEN) <= 0)
                unAuthPenalty = totalPropertyTax.multiply(BPA_DEVIATION_TAXPERC_1_10);
            else if (deviationPerc.compareTo(BigDecimal.TEN) > 0
                    && deviationPerc.compareTo(BIGDECIMAL_100) != 0)
                unAuthPenalty = totalPropertyTax.multiply(BPA_DEVIATION_TAXPERC_ABOVE_11);
            else
                unAuthPenalty = totalPropertyTax.multiply(BPA_DEVIATION_TAXPERC_NOT_DEFINED);
        return unAuthPenalty;
    }

    private BigDecimal calcPublicServiceCharges(final BigDecimal totalPropertyTax) {
        return BigDecimal.ZERO;
    }

    private void createMiscTax(final String taxHead, final BigDecimal tax,
            final APUnitTaxCalculationInfo unitTaxCalculationInfo) {
        final APMiscellaneousTax miscellaneousTax = new APMiscellaneousTax();
        miscellaneousTax.setTaxName(taxHead);
        miscellaneousTax.setTotalCalculatedTax(tax);
        unitTaxCalculationInfo.addMiscellaneousTaxes(miscellaneousTax);
    }

    private BigDecimal getTaxRate(final String taxHead) {
        BigDecimal taxRate = BigDecimal.ZERO;
        if (taxRateProps != null)
            taxRate = new BigDecimal(taxRateProps.getProperty(taxHead));
        return taxRate;
    }

    private BigDecimal getHalfYearTax(final BigDecimal annualTax) {
        return annualTax.divide(new BigDecimal(2)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}