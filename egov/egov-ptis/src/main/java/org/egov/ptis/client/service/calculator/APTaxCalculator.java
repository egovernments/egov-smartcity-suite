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

import static org.egov.ptis.constants.PropertyTaxConstants.BPA_DEVIATION_TAXPERC_11_25;
import static org.egov.ptis.constants.PropertyTaxConstants.BPA_DEVIATION_TAXPERC_1_10;
import static org.egov.ptis.constants.PropertyTaxConstants.BPA_DEVIATION_TAXPERC_26_100;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PRIMARY_SERVICE_CHARGES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASERATE_BY_OCCUPANCY_ZONE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR;
import static org.egov.ptis.constants.PropertyTaxConstants.SQUARE_YARD_TO_SQUARE_METER_VALUE;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGE_RESIDENTIAL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.egov.commons.Area;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.ModuleService;
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
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.domain.service.calculator.PropertyTaxCalculator;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

//TODO name class as client specific
public class APTaxCalculator implements PropertyTaxCalculator {
    private static final Logger LOGGER = Logger.getLogger(APTaxCalculator.class);
    private static BigDecimal RESD_OWNER_DEPRECIATION = new BigDecimal(40);
    private static BigDecimal SEASHORE_RESD_OWNER_DEPRECIATION = new BigDecimal(45);
    // private static BigDecimal BUIULDING_VALUE = new BigDecimal(0.67);
    // private static BigDecimal SITE_VALUE = new BigDecimal(0.33);

    private BigDecimal BUIULDING_VALUE = new BigDecimal(2).divide(new BigDecimal(3), 5, BigDecimal.ROUND_HALF_UP);
    private BigDecimal SITE_VALUE = new BigDecimal(1).divide(new BigDecimal(3), 5, BigDecimal.ROUND_HALF_UP);
    private BigDecimal totalTaxPayable = BigDecimal.ZERO;
    private Boolean isCorporation = Boolean.FALSE;
    private Boolean isSeaShoreULB = Boolean.FALSE;
    private Boolean isPrimaryServiceChrApplicable = Boolean.FALSE;
    private String unAuthDeviationPerc = "";
    private HashMap<Installment, TaxCalculationInfo> taxCalculationMap = new HashMap<Installment, TaxCalculationInfo>();
    private Properties taxRateProps = null;
    private Installment currInstallment = null;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private InstallmentDao installmentDAO;

    @Autowired
    private ModuleService moduleService;

    /**
     * @param property
     *            Property Object
     * @param applicableTaxes
     *            List of Applicable Taxes
     * @param occupationDate
     *            Minimum Occupancy Date among all the units
     * @return
     * @throws TaxCalculatorExeption
     */
    @Override
    public HashMap<Installment, TaxCalculationInfo> calculatePropertyTax(final Property property,
            final Date occupationDate) throws TaxCalculatorExeption {
        Boundary propertyZone = null;
        BigDecimal totalNetArv = BigDecimal.ZERO;
        BoundaryCategory boundaryCategory = null;
        taxRateProps = propertyTaxUtil.loadTaxRates();
        isCorporation = propertyTaxUtil.isCorporation();
        isSeaShoreULB = propertyTaxUtil.isSeaShoreULB();
        currInstallment = propertyTaxUtil.getCurrentInstallment();
        if (isCorporation)
            isPrimaryServiceChrApplicable = propertyTaxUtil.isPrimaryServiceApplicable();

        final List<String> applicableTaxes = prepareApplicableTaxes(property);
        final List<Installment> taxInstallments = getInstallmentListByStartDate(occupationDate);
        propertyZone = property.getBasicProperty().getPropertyID().getZone();

        for (final Installment installment : taxInstallments) {
            totalTaxPayable = BigDecimal.ZERO;
            totalNetArv = BigDecimal.ZERO;
            final APTaxCalculationInfo taxCalculationInfo = addPropertyInfo(property);

            if (betweenOrBefore(occupationDate, installment.getFromDate(), installment.getToDate())) {
                if (property.getPropertyDetail().getPropertyTypeMaster().getCode().equals(OWNERSHIP_TYPE_VAC_LAND)
                        || (property.getPropertyDetail().isAppurtenantLandChecked() != null && property
                                .getPropertyDetail().isAppurtenantLandChecked())) {
                    final APUnitTaxCalculationInfo unitTaxCalculationInfo = calculateVacantLandTax(property,
                            occupationDate, applicableTaxes, installment);
                    totalNetArv = totalNetArv.add(unitTaxCalculationInfo.getNetARV());
                    taxCalculationInfo.addUnitTaxCalculationInfo(unitTaxCalculationInfo);
                    totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
                }

                for (final Floor floorIF : property.getPropertyDetail().getFloorDetails()) {
                    // TODO think about, these beans to be client
                    // specific
                    if (betweenOrBefore(floorIF.getOccupancyDate(), installment.getFromDate(), installment.getToDate())) {
                        boundaryCategory = getBoundaryCategory(propertyZone, installment, floorIF.getPropertyUsage()
                                .getId(), occupationDate, floorIF.getStructureClassification().getId());
                        final APUnitTaxCalculationInfo unitTaxCalculationInfo = calculateNonVacantTax(property,
                                floorIF, boundaryCategory, applicableTaxes, installment);
                        totalNetArv = totalNetArv.add(unitTaxCalculationInfo.getNetARV());

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

    private APUnitTaxCalculationInfo calculateNonVacantTax(final Property property, final Floor floor,
            final BoundaryCategory boundaryCategory, final List<String> applicableTaxes, final Installment installment) {
        final APUnitTaxCalculationInfo unitTaxCalculationInfo = new APUnitTaxCalculationInfo();
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
        if (property.getPropertyDetail().isStructure())
            floorNetArv = floorGrossArv.subtract(floorDepreciation);
        else
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
        BigDecimal unAuthDeviationPerc = getUnAuthDeviationPerc(floor);
        calculateApplicableTaxes(applicableTaxes, unitTaxCalculationInfo, installment, property.getPropertyDetail()
                .getPropertyTypeMaster().getCode(), floor, unAuthDeviationPerc);

        return unitTaxCalculationInfo;
    }

    private APTaxCalculationInfo addPropertyInfo(final Property property) {
        final APTaxCalculationInfo taxCalculationInfo = new APTaxCalculationInfo();
        // Add Property Info
        PropertyID propertyId = property.getBasicProperty().getPropertyID();
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
        BigDecimal halfYearHeadTax = BigDecimal.ZERO;
        BigDecimal taxRatePerc = BigDecimal.ZERO;
        LOGGER.debug("calculateApplicableTaxes - ALV: " + alv);
        LOGGER.debug("calculateApplicableTaxes - applicableTaxes: " + applicableTaxes);

        for (final String applicableTax : applicableTaxes) {
            halfYearHeadTax = BigDecimal.ZERO;
            if (applicableTax.equals(DEMANDRSN_CODE_GENERAL_TAX) || applicableTax.equals(DEMANDRSN_CODE_VACANT_TAX)) {
                if (applicableTax.equals(DEMANDRSN_CODE_VACANT_TAX)) {
                    taxRatePerc = getTaxRate(DEMANDRSN_CODE_VACANT_TAX);
                    halfYearHeadTax = getHalfYearTax(alv.multiply(taxRatePerc.divide(new BigDecimal("100"))).setScale(
                            0, BigDecimal.ROUND_HALF_UP));
                } else {
                    if (floor != null && floor.getPropertyUsage().getIsResidential()) {
                        taxRatePerc = getTaxRate(DEMANDRSN_CODE_GENERAL_TAX + "_RESD");
                    } else {
                        taxRatePerc = getTaxRate(DEMANDRSN_CODE_GENERAL_TAX + "_NR");
                    }
                    halfYearHeadTax = alv.multiply(taxRatePerc.divide(new BigDecimal("100"))).setScale(0,
                            BigDecimal.ROUND_HALF_UP);
                }
                halfYearHeadTax = taxIfGovtProperty(propTypeCode, halfYearHeadTax);
                generalTax = halfYearHeadTax;
            }
            if (applicableTax.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS)) {
                educationTax = alv.multiply(getTaxRate(DEMANDRSN_CODE_EDUCATIONAL_CESS).divide(new BigDecimal("100")))
                        .setScale(0, BigDecimal.ROUND_HALF_UP);
                halfYearHeadTax = educationTax;
            }
            if (applicableTax.equals(DEMANDRSN_CODE_LIBRARY_CESS)) {
                halfYearHeadTax = generalTax.add(educationTax)
                        .multiply(getTaxRate(DEMANDRSN_CODE_LIBRARY_CESS).divide(new BigDecimal("100")))
                        .setScale(0, BigDecimal.ROUND_HALF_UP);
            }

            if (halfYearHeadTax.compareTo(BigDecimal.ZERO) > 0) {
                halfYearHeadTax = roundOffToNearestEven(halfYearHeadTax);
                totalHalfTaxPayable = totalHalfTaxPayable.add(halfYearHeadTax);
                createMiscTax(applicableTax, halfYearHeadTax, unitTaxCalculationInfo);
            }
        }
        // calculating Un Authorized Penalty
        if (!propTypeCode.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
            if (!(unAuthDeviationPerc == null || "0".equals(unAuthDeviationPerc) || "-1".equals(unAuthDeviationPerc))) {
                halfYearHeadTax = BigDecimal.ZERO;
                halfYearHeadTax = roundOffToNearestEven(calculateUnAuthPenalty(unAuthDeviationPerc, totalHalfTaxPayable));
                totalHalfTaxPayable = totalHalfTaxPayable.add(halfYearHeadTax);
                createMiscTax(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, halfYearHeadTax, unitTaxCalculationInfo);
            }
        }
        // calculating Public Service Charges
        if (isPrimaryServiceChrApplicable) {
            halfYearHeadTax = BigDecimal.ZERO;
            halfYearHeadTax = roundOffToNearestEven(calcPublicServiceCharges(totalHalfTaxPayable));
            totalHalfTaxPayable = totalHalfTaxPayable.add(halfYearHeadTax);
            createMiscTax(DEMANDRSN_CODE_PRIMARY_SERVICE_CHARGES, halfYearHeadTax, unitTaxCalculationInfo);
        }
        unitTaxCalculationInfo.setTotalTaxPayable(totalHalfTaxPayable);
        return unitTaxCalculationInfo;
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
        final List<String> applicableTaxes = new ArrayList<String>();
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equals(OWNERSHIP_TYPE_VAC_LAND)) {
            applicableTaxes.add(DEMANDRSN_CODE_GENERAL_TAX);
            applicableTaxes.add(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY);
            applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS);
        } else {
            applicableTaxes.add(DEMANDRSN_CODE_VACANT_TAX);
        }
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
    public List<Installment> getInstallmentListByStartDate(final Date startDate) {
        if (startDate.after(new Date())) {
            return installmentDAO.getInsatllmentByModule(moduleService.getModuleByName(PTMODULENAME), startDate);
        } else {
            return persistenceService.findAllByNamedQuery(QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR, startDate,
                    startDate, PTMODULENAME);
        }
    }

    private BoundaryCategory getBoundaryCategory(final Boundary zone, final Installment installment,
            final Long usageId, final Date occupancyDate, final Long classification) throws TaxCalculatorExeption {
        List<BoundaryCategory> categories = new ArrayList<BoundaryCategory>();
        categories = persistenceService.findAllByNamedQuery(QUERY_BASERATE_BY_OCCUPANCY_ZONE, zone.getId(), usageId,
                classification, occupancyDate, installment.getToDate());

        LOGGER.debug("baseRentOfUnit - Installment : " + installment);

        if (categories.isEmpty()) {
            throw new TaxCalculatorExeption("There are no Unit rates defined for chosen combinations, zone : "
                    + zone.getName() + " usageId : " + usageId + " classification : " + classification
                    + " occupancyDate : " + occupancyDate);
        } else {
            return categories.get(0);
        }
    }

    private List<EgDemandReasonDetails> getDemandReasonDetails(final String demandReasonCode,
            final BigDecimal grossAnnualRentAfterDeduction, final Installment installment) {
        return persistenceService.findAllByNamedQuery(QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT,
                demandReasonCode, grossAnnualRentAfterDeduction, installment.getFromDate(), installment.getToDate());
    }

    public Map<String, BigDecimal> getMiscTaxesForProp(final List<UnitTaxCalculationInfo> unitTaxCalcInfos) {

        final Map<String, BigDecimal> taxMap = new HashMap<String, BigDecimal>();
        for (final UnitTaxCalculationInfo unitTaxCalcInfo : unitTaxCalcInfos)
            for (final MiscellaneousTax miscTax : unitTaxCalcInfo.getMiscellaneousTaxes())
                if (taxMap.get(miscTax.getTaxName()) == null)
                    taxMap.put(miscTax.getTaxName(), miscTax.getTotalCalculatedTax());
                else
                    taxMap.put(miscTax.getTaxName(),
                            taxMap.get(miscTax.getTaxName()).add(miscTax.getTotalCalculatedTax()));

        return taxMap;
    }

    private APUnitTaxCalculationInfo calculateVacantLandTax(final Property property, final Date occupancyDate,
            final List<String> applicableTaxes, final Installment installment) {
        final APUnitTaxCalculationInfo unitTaxCalculationInfo = new APUnitTaxCalculationInfo();

        unitTaxCalculationInfo.setFloorNumber("VACANT");
        unitTaxCalculationInfo.setBaseRateEffectiveDate(occupancyDate);
        unitTaxCalculationInfo.setCapitalValue(new BigDecimal(property.getPropertyDetail().getCurrentCapitalValue()));
        unitTaxCalculationInfo.setNetARV(unitTaxCalculationInfo.getCapitalValue());

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
        final Boolean result = between(date, fromDate, toDate) || date.before(fromDate);
        return result;
    }

    private BigDecimal calculateFloorMrv(final BigDecimal builtUpArea, final BoundaryCategory boundaryCategory) {
        return builtUpArea.multiply(BigDecimal.valueOf(boundaryCategory.getCategory().getCategoryAmount()));
    }

    private BigDecimal calculateFloorBuildingValue(final BigDecimal floorMrv) {
        return floorMrv.multiply(BUIULDING_VALUE).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculateFloorSiteValue(final BigDecimal floorMrv) {
        return floorMrv.multiply(SITE_VALUE).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculateFloorDepreciation(final BigDecimal grossArv, final Floor floor) {
        BigDecimal depreciationPct = BigDecimal.ZERO;
        if (isSeaShoreULB) {
            if (floor.getPropertyOccupation().getOccupancyCode().equals(PropertyTaxConstants.OCC_OWNER)) {
                depreciationPct = SEASHORE_RESD_OWNER_DEPRECIATION;
            } else {
                depreciationPct = BigDecimal.valueOf(floor.getDepreciationMaster().getDepreciationPct());
            }
        } else {
            if (floor.getPropertyOccupation().getOccupancyCode().equals(PropertyTaxConstants.OCC_OWNER)) {
                depreciationPct = RESD_OWNER_DEPRECIATION;
            } else {
                depreciationPct = BigDecimal.valueOf(floor.getDepreciationMaster().getDepreciationPct());
            }
        }
        return grossArv.multiply(depreciationPct).divide(BigDecimal.valueOf(100));
    }

    public BigDecimal roundOffToNearestEven(final BigDecimal amount) {
        BigDecimal roundedAmt;
        final BigDecimal remainder = amount.remainder(new BigDecimal(2));
        /*
         * if reminder is less than 1, subtract reminder amount from passed
         * amount else reminder is greater than 1, subtract reminder amount from
         * passed amount and add 5 to result amount
         */
        if (remainder.compareTo(new BigDecimal("1")) == 1)
            roundedAmt = amount.subtract(remainder).add(new BigDecimal(2));
        else
            roundedAmt = amount.subtract(remainder);
        return roundedAmt;
    }

    public BigDecimal convertYardToSquareMeters(final Float vacantLandArea) {
        Float areaInSqMts = null;
        areaInSqMts = new Float(vacantLandArea) * new Float(SQUARE_YARD_TO_SQUARE_METER_VALUE);
        return new BigDecimal(areaInSqMts).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal taxIfGovtProperty(String propTypeCode, BigDecimal tax) {
        if (propTypeCode.equals(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_335))
            tax = tax.multiply(new BigDecimal(0.335));
        if (propTypeCode.equals(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_50))
            tax = tax.multiply(new BigDecimal(0.5));
        if (propTypeCode.equals(PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_75))
            tax = tax.multiply(new BigDecimal(0.75));
        return tax;
    }


    private BigDecimal getUnAuthDeviationPerc(Floor floor) {
        BigDecimal deviationPerc = null;
        BigDecimal diffArea = null;
        BigDecimal plinthArea = new BigDecimal(floor.getBuiltUpArea().getArea());
        BigDecimal buildingPlanPlinthArea = (floor.getBuildingPlanPlinthArea() != null && floor.getBuildingPlanPlinthArea().getArea() != null) ? new BigDecimal(floor
                .getBuildingPlanPlinthArea().getArea()) : BigDecimal.ZERO;
        if (buildingPlanPlinthArea.compareTo(BigDecimal.ZERO) == 0) {
            deviationPerc = new BigDecimal(100);
        } else if (plinthArea.compareTo(buildingPlanPlinthArea) == 1) {
            diffArea = plinthArea.subtract(buildingPlanPlinthArea);
            deviationPerc = diffArea.divide(plinthArea, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
        }
        return deviationPerc;
    }

    private BigDecimal calculateUnAuthPenalty(BigDecimal deviationPerc, BigDecimal totalPropertyTax) {
        BigDecimal unAuthPenalty = BigDecimal.ZERO;
        if (deviationPerc != null && !deviationPerc.equals("0")) {
            if (deviationPerc.compareTo(BigDecimal.ZERO) == 1 && deviationPerc.compareTo(BigDecimal.TEN) == -1) {
                unAuthPenalty = totalPropertyTax.multiply(BPA_DEVIATION_TAXPERC_1_10);
            } else if (deviationPerc.compareTo(new BigDecimal(11)) == 1
                    && deviationPerc.compareTo(new BigDecimal(25)) == -1) {
                unAuthPenalty = totalPropertyTax.multiply(BPA_DEVIATION_TAXPERC_11_25);
            } else {
                unAuthPenalty = totalPropertyTax.multiply(BPA_DEVIATION_TAXPERC_26_100);
            }
        }
        return unAuthPenalty;
    }

    private BigDecimal calcPublicServiceCharges(BigDecimal totalPropertyTax) {
        BigDecimal publicServiceCharge = BigDecimal.ZERO;
        return publicServiceCharge;
    }

    private void createMiscTax(String taxHead, BigDecimal tax, APUnitTaxCalculationInfo unitTaxCalculationInfo) {
        APMiscellaneousTax miscellaneousTax = new APMiscellaneousTax();
        miscellaneousTax.setTaxName(taxHead);
        miscellaneousTax.setTotalCalculatedTax(tax);
        unitTaxCalculationInfo.addMiscellaneousTaxes(miscellaneousTax);
    }

    private BigDecimal getTaxRate(String taxHead) {
        BigDecimal taxRate = BigDecimal.ZERO;
        if (taxRateProps != null) {
            taxRate = new BigDecimal(taxRateProps.getProperty(taxHead));
        }
        return taxRate;
    }

    private BigDecimal getHalfYearTax(BigDecimal annualTax) {
        return annualTax.divide(new BigDecimal(2)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
