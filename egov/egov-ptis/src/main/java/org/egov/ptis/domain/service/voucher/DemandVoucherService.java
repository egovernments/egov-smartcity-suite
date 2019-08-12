/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2019  eGovernments Foundation
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

package org.egov.ptis.domain.service.voucher;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_PT_DEMAND_VOUCHER_GLCODES;
import static org.egov.ptis.constants.PropertyTaxConstants.ARREAR_DEMANDRSN_GLCODE;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_DEMANDRSN_GLCODE;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_DRAINAGE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIGHT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_SCAVENGE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_WATER_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class DemandVoucherService {

    private static final String AMOUNT_TYPE = "amountType";
    private static final String AMOUNT = "amount";
    private static final String ARREAR_TAX = "ARREAR_TAX";
    private static final String CURR_TAX = "CURR_TAX";

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private PtDemandDao ptDemandDao;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private ModuleService moduleService;

    public Map<String, Map<String, Object>> prepareDemandVoucherData(Property currProperty, Property existingProperty,
            boolean forCreate) {
        BigDecimal existingPropTax = ZERO;
        Map<String, BigDecimal> currPropTaxDetails = propertyService.getDCBDetailsForProperty(currProperty);
        BigDecimal currentPropTax = currPropTaxDetails.get(CURR_FIRSTHALF_DMD_STR)
                .add(currPropTaxDetails.get(CURR_SECONDHALF_DMD_STR))
                .add(currPropTaxDetails.get(ARR_DMD_STR));

        if (!forCreate) {
            Map<String, BigDecimal> existingPropTaxDetails = propertyService.getDCBDetailsForProperty(existingProperty);
            existingPropTax = existingPropTaxDetails.get(CURR_FIRSTHALF_DMD_STR)
                    .add(existingPropTaxDetails.get(CURR_SECONDHALF_DMD_STR))
                    .add(existingPropTaxDetails.get(ARR_DMD_STR));
        }

        boolean demandIncreased = currentPropTax.compareTo(existingPropTax) > 0 ? true : false;

        return prepareDataForDemandVoucher(currProperty, existingProperty, demandIncreased, forCreate);
    }

    private Map<String, Map<String, Object>> prepareDataForDemandVoucher(Property currProperty, Property oldProperty,
            boolean demandIncreased, boolean forCreate) {
        Map<String, Map<String, Object>> voucherDetails = new HashMap<>();
        Map<String, String> glCodeMap = getGlCodesForTaxes();
        Module module = moduleService.getModuleByName(PTMODULENAME);
        Date effectiveDate;
        if (forCreate) {
            if (!currProperty.getPropertyDetail().getPropertyTypeMaster().getCode()
                    .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                effectiveDate = propertyService.getLowestDtOfCompFloorWise(currProperty.getPropertyDetail().getFloorDetails());
            else
                effectiveDate = currProperty.getPropertyDetail().getDateOfCompletion();
        } else
            effectiveDate = currProperty.getEffectiveDate();
        Installment effectiveInstall = installmentDao.getInsatllmentByModuleForGivenDate(module, effectiveDate);
        Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        Installment currFirstHalf = currYearInstMap.get(CURRENTYEAR_FIRST_HALF);
        Installment currSecondHalf = currYearInstMap.get(CURRENTYEAR_SECOND_HALF);
        /*
         * if demandIncreased, yearwise current and arrear tax details go as debit and headwise taxes go as credit, else yearwise
         * current and arrear tax details go as credit and headwise taxes go as debit
         */
        Ptdemand ptDemand = currProperty.getPtDemandSet().iterator().next();
        Ptdemand oldPtDemand;
        Map<String, BigDecimal> oldPropertyTaxMap = new LinkedHashMap<>();
        Map<String, BigDecimal> currPropertyTaxMap = fetchHeadwiseDetailsForDemandVoucher(effectiveInstall,
                currFirstHalf, currSecondHalf, ptDemand);
        if (oldProperty != null) {
            oldPtDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(oldProperty);
            oldPropertyTaxMap = fetchHeadwiseDetailsForDemandVoucher(effectiveInstall, currFirstHalf, currSecondHalf,
                    oldPtDemand);
        }
        if (!currPropertyTaxMap.isEmpty())
            prepareVoucherDetailsMap(voucherDetails, glCodeMap, oldPropertyTaxMap, currPropertyTaxMap, demandIncreased);

        return voucherDetails;
    }

    private void prepareVoucherDetailsMap(Map<String, Map<String, Object>> voucherDetails,
            Map<String, String> glCodeMap, Map<String, BigDecimal> oldPropertyTaxMap,
            Map<String, BigDecimal> currPropertyTaxMap, boolean demandIncreased) {
        Map<String, Object> values;
        BigDecimal advance = ZERO;
        if (currPropertyTaxMap.get(DEMANDRSN_CODE_ADVANCE) != null)
            advance = currPropertyTaxMap.get(DEMANDRSN_CODE_ADVANCE)
                    .subtract(oldPropertyTaxMap.isEmpty()
                            ? ZERO
                            : oldPropertyTaxMap.get(DEMANDRSN_CODE_ADVANCE));
        BigDecimal generaltax = currPropertyTaxMap.get(DEMANDRSN_CODE_GENERAL_TAX)
                .subtract(oldPropertyTaxMap.isEmpty()
                        ? ZERO
                        : oldPropertyTaxMap.get(DEMANDRSN_CODE_GENERAL_TAX))
                .abs();
        BigDecimal vacantTax = currPropertyTaxMap.get(DEMANDRSN_CODE_VACANT_TAX)
                .subtract(oldPropertyTaxMap.isEmpty()
                        ? ZERO
                        : oldPropertyTaxMap.get(DEMANDRSN_CODE_VACANT_TAX))
                .abs();
        BigDecimal libCess = currPropertyTaxMap.get(DEMANDRSN_CODE_LIBRARY_CESS)
                .subtract(oldPropertyTaxMap.isEmpty()
                        ? ZERO
                        : oldPropertyTaxMap.get(DEMANDRSN_CODE_LIBRARY_CESS))
                .abs();
        BigDecimal currTax = currPropertyTaxMap.get(CURR_TAX)
                .subtract(oldPropertyTaxMap.isEmpty()
                        ? ZERO
                        : oldPropertyTaxMap.get(CURR_TAX))
                .abs();
        BigDecimal arrearTax = currPropertyTaxMap.get(ARREAR_TAX)
                .subtract(oldPropertyTaxMap.isEmpty()
                        ? ZERO
                        : oldPropertyTaxMap.get(ARREAR_TAX))
                .abs();

        if (advance.compareTo(ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, advance);
            values.put(AMOUNT_TYPE, demandIncreased ? VoucherConstant.CREDITAMOUNT : VoucherConstant.DEBITAMOUNT);
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_ADVANCE), values);
        }
        if (generaltax.compareTo(ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, generaltax);
            values.put(AMOUNT_TYPE, demandIncreased ? VoucherConstant.CREDITAMOUNT : VoucherConstant.DEBITAMOUNT);
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_GENERAL_TAX), values);
        }
        if (vacantTax.compareTo(ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, vacantTax);
            values.put(AMOUNT_TYPE, demandIncreased ? VoucherConstant.CREDITAMOUNT : VoucherConstant.DEBITAMOUNT);
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_VACANT_TAX), values);
        }
        if (libCess.compareTo(ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, libCess);
            values.put(AMOUNT_TYPE, demandIncreased ? VoucherConstant.CREDITAMOUNT : VoucherConstant.DEBITAMOUNT);
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_LIBRARY_CESS), values);
        }
        if (currTax.compareTo(ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, currTax);
            values.put(AMOUNT_TYPE, demandIncreased ? VoucherConstant.DEBITAMOUNT : VoucherConstant.CREDITAMOUNT);
            voucherDetails.put(CURRENT_DEMANDRSN_GLCODE, values);
        }
        if (arrearTax.compareTo(ZERO) != 0) {
            values = new HashMap<>();
            values.put(AMOUNT, arrearTax);
            values.put(AMOUNT_TYPE, demandIncreased ? VoucherConstant.DEBITAMOUNT : VoucherConstant.CREDITAMOUNT);
            voucherDetails.put(ARREAR_DEMANDRSN_GLCODE, values);
        }
    }

    public Map<String, BigDecimal> fetchHeadwiseDetailsForDemandVoucher(Installment effectiveInstall,
            Installment currFirstHalf, Installment currSecondHalf, Ptdemand ptDemand) {
        String taxHead;
        BigDecimal advance = ZERO;
        BigDecimal generalTax = ZERO;
        BigDecimal vacantLandTax = ZERO;
        BigDecimal libCess = ZERO;
        BigDecimal currTax = ZERO;
        BigDecimal arrearTax = ZERO;
        Map<String, BigDecimal> currPropertyTaxMap = new LinkedHashMap<>();
        for (EgDemandDetails demandDetails : ptDemand.getEgDemandDetails()) {
            if (!demandDetails.getInstallmentStartDate().before(effectiveInstall.getFromDate())) {
                taxHead = demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode();
                if (DEMANDRSN_CODE_ADVANCE.equalsIgnoreCase(taxHead)) {
                    advance = advance.add(demandDetails.getAmtCollected());
                }
                if (DEMANDRSN_CODE_GENERAL_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_EDUCATIONAL_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_WATER_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_DRAINAGE_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_SCAVENGE_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_LIGHT_TAX.equalsIgnoreCase(taxHead)
                        || DEMANDRSN_CODE_UNAUTHORIZED_PENALTY.equalsIgnoreCase(taxHead))
                    generalTax = generalTax.add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()));

                if (DEMANDRSN_CODE_VACANT_TAX.equalsIgnoreCase(taxHead))
                    vacantLandTax = vacantLandTax.add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()));
                if (DEMANDRSN_CODE_LIBRARY_CESS.equalsIgnoreCase(taxHead))
                    libCess = libCess.add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()));

                if (!DEMANDRSN_CODE_PENALTY_FINES.equalsIgnoreCase(taxHead)
                        && !DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY.equalsIgnoreCase(taxHead)) {
                    if (demandDetails.getInstallmentStartDate().equals(currFirstHalf.getFromDate())
                            || demandDetails.getInstallmentStartDate().equals(currSecondHalf.getFromDate())) {
                        currTax = currTax.add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()));
                    } else {
                        arrearTax = arrearTax.add(demandDetails.getAmount().subtract(demandDetails.getAmtCollected()));
                    }
                }
            }
        }
        if (advance.compareTo(ZERO) >= 0)
            currPropertyTaxMap.put(DEMANDRSN_CODE_ADVANCE, advance);
        if (generalTax.compareTo(ZERO) >= 0)
            currPropertyTaxMap.put(DEMANDRSN_CODE_GENERAL_TAX, generalTax);
        if (vacantLandTax.compareTo(ZERO) >= 0)
            currPropertyTaxMap.put(DEMANDRSN_CODE_VACANT_TAX, vacantLandTax);
        if (libCess.compareTo(ZERO) >= 0)
            currPropertyTaxMap.put(DEMANDRSN_CODE_LIBRARY_CESS, libCess);
        if (currTax.compareTo(ZERO) >= 0)
            currPropertyTaxMap.put(CURR_TAX, currTax);
        if (arrearTax.compareTo(ZERO) >= 0)
            currPropertyTaxMap.put(ARREAR_TAX, arrearTax);

        return currPropertyTaxMap;
    }

    public Map<String, String> getGlCodesForTaxes() {
        Map<String, String> glCodeMap = new HashMap<>();
        List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                APPCONFIG_PT_DEMAND_VOUCHER_GLCODES);
        String[] taxHeads;
        String[] value;
        for (AppConfigValues appConfig : appConfigValues) {
            taxHeads = appConfig.getValue().split("~");
            for (String taxHead : taxHeads) {
                value = taxHead.split("=");
                glCodeMap.put(value[0], value[1]);
            }
        }
        return glCodeMap;
    }

}
