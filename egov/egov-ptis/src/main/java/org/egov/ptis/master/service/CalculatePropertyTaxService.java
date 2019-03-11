/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.ptis.master.service;

import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PRIMARY_SERVICE_CHARGES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VACANT_TAX_DEMAND_CODES;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.ptis.bean.FloorDetailsInfo;
import org.egov.ptis.client.service.calculator.APTaxCalculator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@Transactional
public class CalculatePropertyTaxService {

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private StructureClassificationService structureClassificationService;

    @Autowired
    private PropertyUsageService propertyUsageService;

    @Autowired
    private PropertyOccupationService propertyOccupationService;

    @Autowired
    PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private APTaxCalculator apTaxCalculator;

    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    public void addModelAttributes(final Model model) {
        final List<Boundary> zoneList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE,
                REVENUE_HIERARCHY_TYPE);
        final List<StructureClassification> buildingClassifications = structureClassificationService.getAllActiveStructureTypes();
        final List<PropertyUsage> usageList = propertyUsageService.getAllActiveMixedPropertyUsages();
        Map<Long, String> zones = new ConcurrentHashMap<>();
        Map<Long, String> classifications = new ConcurrentHashMap<>();
        Map<Long, String> usages = new ConcurrentHashMap<>();
        Map<Long, String> occupations = new ConcurrentHashMap<>();
        for (Boundary zone : zoneList) {
            zones.put(zone.getId(), zone.getName());
        }

        for (StructureClassification classification : buildingClassifications) {
            classifications.put(classification.getId(), classification.getTypeName());
        }
        for (PropertyUsage usage : usageList) {
            usages.put(usage.getId(), usage.getUsageName());
        }
        for (PropertyOccupation occupation : propertyOccupationService.getAllPropertyOccupations()) {
            occupations.put(occupation.getId(), occupation.getOccupation());
        }
        model.addAttribute("floorDetails", new FloorDetailsInfo());
        model.addAttribute("zoneId", zones);
        model.addAttribute("classificationId", classifications);
        model.addAttribute("usageId", usages);
        model.addAttribute("occupationId", occupations);
        model.addAttribute("floorId", FLOOR_MAP);
        model.addAttribute("occupancyId", occupations);
    }

    public Map<String, String> calculateTaxes(FloorDetailsInfo taxCalculatorRequest) throws TaxCalculatorExeption {
        PropertyTypeMaster propertyTypeMaster = propertyTypeMasterDAO.getPropertyTypeMasterByCode(OWNERSHIP_TYPE_PRIVATE);
        Map<Installment, TaxCalculationInfo> instTaxMap = apTaxCalculator.calculatePropertyTax(
                boundaryService.getBoundaryById(taxCalculatorRequest.getZoneId()), taxCalculatorRequest.getFloorTemp(),
                propertyTypeMaster, new Date());
        return getCurrentHalfyearTaxRates(instTaxMap, propertyTypeMaster);
    }

    public Map<String, String> getCurrentHalfyearTaxRates(final Map<Installment, TaxCalculationInfo> instTaxMap,
            final PropertyTypeMaster propTypeMstr) {
        final Map<String, String> taxDetails = new LinkedHashMap<>();
        final Installment currentInstall = propertyTaxCommonUtils.getCurrentPeriodInstallment();
        final TaxCalculationInfo calculationInfo = instTaxMap.get(currentInstall);
        final BigDecimal annualValue = calculationInfo.getTotalNetARV();
        BigDecimal genTax = BigDecimal.ZERO;
        BigDecimal libCess = BigDecimal.ZERO;
        BigDecimal eduTax = BigDecimal.ZERO;
        BigDecimal unAuthPenalty = BigDecimal.ZERO;
        BigDecimal vacLandTax = BigDecimal.ZERO;
        BigDecimal sewrageTax = BigDecimal.ZERO;
        BigDecimal serviceCharges = BigDecimal.ZERO;
        for (final UnitTaxCalculationInfo unitTaxCalcInfo : calculationInfo.getUnitTaxCalculationInfos())
            for (final MiscellaneousTax miscTax : unitTaxCalcInfo.getMiscellaneousTaxes())
                if (NON_VACANT_TAX_DEMAND_CODES.contains(miscTax.getTaxName()))
                    genTax = genTax.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_UNAUTHORIZED_PENALTY)
                    unAuthPenalty = unAuthPenalty.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_EDUCATIONAL_TAX)
                    eduTax = eduTax.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_VACANT_TAX)
                    vacLandTax = vacLandTax.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_LIBRARY_CESS)
                    libCess = libCess.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_SEWERAGE_TAX)
                    sewrageTax = sewrageTax.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_PRIMARY_SERVICE_CHARGES)
                    serviceCharges = serviceCharges.add(miscTax.getTotalCalculatedTax());

        BigDecimal totalTax = genTax.setScale(0, BigDecimal.ROUND_HALF_UP)
                .add(unAuthPenalty.setScale(0, BigDecimal.ROUND_HALF_UP))
                .add(eduTax.setScale(0, BigDecimal.ROUND_HALF_UP)).add(vacLandTax.setScale(0, BigDecimal.ROUND_HALF_UP))
                .add(libCess.setScale(0, BigDecimal.ROUND_HALF_UP)).add(sewrageTax.setScale(0, BigDecimal.ROUND_HALF_UP))
                .add(serviceCharges.setScale(0, BigDecimal.ROUND_HALF_UP));

        setTaxDetailsToMap(propTypeMstr, taxDetails, annualValue, totalTax, genTax, libCess, eduTax, unAuthPenalty,
                vacLandTax, sewrageTax, serviceCharges);
        return taxDetails;
    }

    public void setTaxDetailsToMap(final PropertyTypeMaster propTypeMstr, final Map<String, String> taxDetails,
            final BigDecimal annualValue, final BigDecimal totalPropertyTax, BigDecimal genTax, BigDecimal libCess,
            BigDecimal eduTax, BigDecimal unAuthPenalty, BigDecimal vacLandTax, BigDecimal sewrageTax,
            BigDecimal serviceCharges) {
        taxDetails.put("Annual Rental Value", propertyTaxCommonUtils.formatAmount(annualValue));
        if (OWNERSHIP_TYPE_VAC_LAND.equalsIgnoreCase(propTypeMstr.getCode()))
            taxDetails.put("Vacant Land Tax", propertyTaxCommonUtils.formatAmount(vacLandTax));
        else {
            taxDetails.put("Property Tax", propertyTaxCommonUtils.formatAmount(genTax));
            taxDetails.put("Education Tax", propertyTaxCommonUtils.formatAmount(eduTax));
        }
        taxDetails.put("Library Cess", propertyTaxCommonUtils.formatAmount(libCess));
        final Boolean isCorporation = propertyTaxUtil.isCorporation();
        if (isCorporation)
            taxDetails.put("Sewrage Tax", propertyTaxCommonUtils.formatAmount(sewrageTax));
        if (isCorporation && propertyTaxUtil.isPrimaryServiceApplicable())
            taxDetails.put("Service Charges", propertyTaxCommonUtils.formatAmount(serviceCharges));
        if (!OWNERSHIP_TYPE_VAC_LAND.equalsIgnoreCase(propTypeMstr.getCode()))
            taxDetails.put("Unauthorized Penalty", propertyTaxCommonUtils.formatAmount(unAuthPenalty));

        taxDetails.put("Total Tax", propertyTaxCommonUtils.formatAmount(totalPropertyTax));
    }

}
