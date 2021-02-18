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
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
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
import static org.egov.ptis.constants.PropertyTaxConstants.PRIOR_INCOME;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.ptis.client.util.FinancialUtil;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.model.demandvoucher.DemandVoucherDetails;
import org.egov.ptis.domain.model.demandvoucher.NormalizeDemandDetails;
import org.egov.ptis.domain.model.demandvoucher.PropertyDemandVoucher;
import org.egov.ptis.domain.repository.demandvoucher.PropertyDemandVoucherRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

@Transactional(readOnly = true)
@Service
public class DemandVoucherService {
    private static final Logger LOGGER = Logger.getLogger(DemandVoucherService.class);

    private static final String AMOUNT_TYPE = "amountType";
    private static final String AMOUNT = "amount";
    private static final String ARREAR_TAX = "ARREAR_TAX";
    private static final String CURR_TAX = "CURR_TAX";
    protected static final Set<String> DEMAND_REASONS = new LinkedHashSet<>(Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
            DEMANDRSN_CODE_GENERAL_TAX, DEMANDRSN_CODE_DRAINAGE_TAX, DEMANDRSN_CODE_SCAVENGE_TAX,
            DEMANDRSN_CODE_WATER_TAX, DEMANDRSN_CODE_LIGHT_TAX, DEMANDRSN_CODE_VACANT_TAX, DEMANDRSN_CODE_EDUCATIONAL_TAX,
            DEMANDRSN_CODE_LIBRARY_CESS, DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, DEMANDRSN_CODE_ADVANCE,
            DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY));
    protected static final Set<String> DEMAND_REASONS_COMMON = new LinkedHashSet<>(
            Arrays.asList(DEMANDRSN_CODE_DRAINAGE_TAX, DEMANDRSN_CODE_SCAVENGE_TAX,
                    DEMANDRSN_CODE_WATER_TAX, DEMANDRSN_CODE_LIGHT_TAX, DEMANDRSN_CODE_EDUCATIONAL_TAX,
                    DEMANDRSN_CODE_UNAUTHORIZED_PENALTY));

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private PtDemandDao ptDemandDao;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private PropertyDemandVoucherRepository demandVoucherRepository;

    @Autowired
    private FinancialUtil financialUtil;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    public void createDemandVoucher(PropertyImpl newProperty, PropertyImpl oldProperty, Map<String, String> applicationDetails) {
        String appConfigValue = propertyTaxCommonUtils.getDemandVoucherAppConfigValue();
        PropertyDemandVoucher demandVoucher = demandVoucherRepository.findByProperty(newProperty);
        if ("Y".equalsIgnoreCase(appConfigValue) && demandVoucher == null) {
            Map<String, Map<String, Object>> voucherData = prepareDemandVoucherData(newProperty, oldProperty, applicationDetails);
            if (!voucherData.isEmpty()) {
                LOGGER.info("Voucher Data-------------->" + voucherData);
                CVoucherHeader cvh = financialUtil.createVoucher(newProperty.getBasicProperty().getUpicNo(), voucherData,
                        applicationDetails.get(PropertyTaxConstants.APPLICATION_TYPE));
                persistPropertyDemandVoucher(newProperty, cvh);
            }
        }
    }

    public Map<String, Map<String, Object>> prepareDemandVoucherData(Property newProperty, Property oldProperty,
            Map<String, String> applicationDetails) {
        BigDecimal existingPropTax = ZERO;
        boolean demandIncreased;
        BigDecimal currentPropTax = getTotalPropertyTax(newProperty);
        if (oldProperty != null)
            existingPropTax = getTotalPropertyTax(oldProperty);
        demandIncreased = isDemandIncreased(existingPropTax, currentPropTax, applicationDetails);
        return prepareDataForDemandVoucher(newProperty, oldProperty, demandIncreased, applicationDetails);
    }

    private BigDecimal getTotalPropertyTax(Property property) {
        Map<String, BigDecimal> currPropTaxDetails = propertyService.getDCBDetailsForProperty(property);
        BigDecimal currentPropTax = currPropTaxDetails.get(CURR_FIRSTHALF_DMD_STR)
                .add(currPropTaxDetails.get(CURR_SECONDHALF_DMD_STR))
                .add(currPropTaxDetails.get(ARR_DMD_STR));
        return currentPropTax;
    }

    private Map<String, Map<String, Object>> prepareDataForDemandVoucher(Property newProperty, Property oldProperty,
            boolean demandIncreased, Map<String, String> applicationDetails) {
        Map<String, Map<String, Object>> voucherDetails = new HashMap<>();
        Map<String, String> glCodeMap = getGlCodesForTaxes();
        Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        Installment currFirstHalf = currYearInstMap.get(CURRENTYEAR_FIRST_HALF);
        Installment currSecondHalf = currYearInstMap.get(CURRENTYEAR_SECOND_HALF);
        /*
         * if demandIncreased, yearwise current and arrear tax details go as debit and headwise taxes go as credit, else yearwise
         * current and arrear tax details go as credit and headwise taxes go as debit
         */
        Ptdemand ptDemand;
        ptDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(newProperty);
        Ptdemand oldPtDemand = null;
        List<DemandVoucherDetails> demandVoucherDetailList = new ArrayList<>();
        /*
         * instChangeOpposite will be true when overall demand will increase/decrease and a particular installment(s) demand will
         * decrease/increase. areInstallmentsMismatch api will return true when old property has less installments as compared to
         * new property.
         */
        boolean instChangeOpposite = false;
        boolean areInstallmentsMismatch = false;
        if (oldProperty != null) {
            oldPtDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(oldProperty);
            areInstallmentsMismatch = areInstallmentsMismatch(oldPtDemand, ptDemand);
            boolean isRPNewPropertyCase = oldProperty.getPropertyModifyReason().equals(PropertyTaxConstants.PROP_CREATE_RSN)
                    && newProperty.getPropertyModifyReason()
                            .equals(PropertyTaxConstants.PROPERTY_MODIFY_REASON_REVISION_PETITION);
            if (!areInstallmentsMismatch)
                instChangeOpposite = ifInstallmentChangeIsOpposite(oldPtDemand, ptDemand, demandIncreased);
            if (areInstallmentsMismatch)
                demandVoucherDetailList = prepareDemandVoucherDetailsForMismatch(currFirstHalf, currSecondHalf,
                        oldPtDemand, ptDemand, applicationDetails, isRPNewPropertyCase);
            else
                demandVoucherDetailList = prepareDemandVoucherDetails(currFirstHalf, currSecondHalf,
                        oldPtDemand, ptDemand, applicationDetails, instChangeOpposite);
        } else
            demandVoucherDetailList = prepareDemandVoucherDetails(currFirstHalf, currSecondHalf,
                    null, ptDemand, applicationDetails, instChangeOpposite);

        if (!demandVoucherDetailList.isEmpty())
            if (oldProperty != null && (areInstallmentsMismatch
                    || instChangeOpposite))
                prepareVoucherDetailsMapForMismatch(voucherDetails, glCodeMap, demandIncreased,
                        demandVoucherDetailList, applicationDetails);
            else
                prepareVoucherDetailsMap(voucherDetails, glCodeMap, demandIncreased,
                        demandVoucherDetailList, applicationDetails);

        return voucherDetails;
    }

    private void prepareVoucherDetailsMap(Map<String, Map<String, Object>> voucherDetails,
            Map<String, String> glCodeMap, boolean demandIncreased,
            List<DemandVoucherDetails> demandVoucherDetailList, Map<String, String> applicationDetails) {
        BigDecimal generalTax = ZERO;
        BigDecimal vacantLandtax = ZERO;
        BigDecimal libraryCess = ZERO;
        BigDecimal priorIncome = ZERO;
        BigDecimal arrearsTax = ZERO;
        BigDecimal currentTax = ZERO;
        BigDecimal penalty = ZERO;
        BigDecimal advance = ZERO;
        for (DemandVoucherDetails demandVoucherDetail : demandVoucherDetailList) {
            if (demandVoucherDetail.getPurpose().equals(CURR_TAX)) {
                generalTax = generalTax.add(demandVoucherDetail.getGeneralTaxVariation());
                vacantLandtax = vacantLandtax.add(demandVoucherDetail.getVacantTaxVariation());
                currentTax = currentTax.add(demandVoucherDetail.getNetBalance());
            }
            if (demandVoucherDetail.getPurpose().equals(ARREAR_TAX)) {
                priorIncome = priorIncome.add(demandVoucherDetail.getGeneralTaxVariation())
                        .add(demandVoucherDetail.getVacantTaxVariation());
                arrearsTax = arrearsTax.add(demandVoucherDetail.getNetBalance());
            }
            libraryCess = libraryCess.add(demandVoucherDetail.getLibraryCessVariation());
            advance = advance.add(demandVoucherDetail.getAdvance());
            penalty = penalty.add(demandVoucherDetail.getPenalty());
        }

        if (advance.compareTo(ZERO) > 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_ADVANCE),
                    putAmountAndType(advance.setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
        assembleIncomeHeads(voucherDetails, glCodeMap, demandIncreased, generalTax, DEMANDRSN_CODE_GENERAL_TAX,
                applicationDetails);
        assembleIncomeHeads(voucherDetails, glCodeMap, demandIncreased, vacantLandtax, DEMANDRSN_CODE_VACANT_TAX,
                applicationDetails);
        assembleIncomeHeads(voucherDetails, glCodeMap, demandIncreased, libraryCess, DEMANDRSN_CODE_LIBRARY_CESS,
                applicationDetails);
        assembleIncomeHeads(voucherDetails, glCodeMap, demandIncreased, priorIncome, PRIOR_INCOME, applicationDetails);
        if (penalty.compareTo(ZERO) > 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_PENALTY_FINES),
                    putAmountAndType(penalty.setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
        if (penalty.compareTo(ZERO) < 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_PENALTY_FINES),
                    putAmountAndType(penalty.abs().setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? false : true));
        if (arrearsTax.abs().compareTo(ZERO) > 0)
            voucherDetails.put(glCodeMap.get(ARREAR_TAX),
                    putAmountAndType(arrearsTax.abs().setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
        if (currentTax.abs().compareTo(ZERO) > 0)
            voucherDetails.put(glCodeMap.get(CURR_TAX),
                    putAmountAndType(currentTax.abs().setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
    }

    private void prepareVoucherDetailsMapForMismatch(Map<String, Map<String, Object>> voucherDetails,
            Map<String, String> glCodeMap, boolean demandIncreased,
            List<DemandVoucherDetails> demandVoucherDetailList, Map<String, String> applicationDetails) {
        BigDecimal generalTax = ZERO;
        BigDecimal vacantLandtax = ZERO;
        BigDecimal libraryCess = ZERO;
        BigDecimal priorIncome = ZERO;
        BigDecimal arrearsTax = ZERO;
        BigDecimal currentTax = ZERO;
        BigDecimal penalty = ZERO;
        BigDecimal advance = ZERO;
        for (DemandVoucherDetails demandVoucherDetail : demandVoucherDetailList) {
            if (demandVoucherDetail.getPurpose().equals(CURR_TAX)) {
                generalTax = generalTax.add(demandVoucherDetail.getGeneralTaxVariation());
                vacantLandtax = vacantLandtax.add(demandVoucherDetail.getVacantTaxVariation());
                currentTax = currentTax.add(demandVoucherDetail.getNetBalance());
            }
            if (demandVoucherDetail.getPurpose().equals(ARREAR_TAX)) {
                priorIncome = priorIncome.add(demandVoucherDetail.getGeneralTaxVariation())
                        .add(demandVoucherDetail.getVacantTaxVariation());
                arrearsTax = arrearsTax.add(demandVoucherDetail.getNetBalance());
            }
            libraryCess = libraryCess.add(demandVoucherDetail.getLibraryCessVariation());
            advance = advance.add(demandVoucherDetail.getAdvance());
            penalty = penalty.add(demandVoucherDetail.getPenalty());
        }
        assembleIncomeHeads(voucherDetails, glCodeMap, demandIncreased, generalTax, DEMANDRSN_CODE_GENERAL_TAX,
                applicationDetails);
        assembleIncomeHeads(voucherDetails, glCodeMap, demandIncreased, vacantLandtax, DEMANDRSN_CODE_VACANT_TAX,
                applicationDetails);
        assembleIncomeHeads(voucherDetails, glCodeMap, demandIncreased, libraryCess, DEMANDRSN_CODE_LIBRARY_CESS,
                applicationDetails);
        assembleIncomeHeads(voucherDetails, glCodeMap, demandIncreased, priorIncome, PRIOR_INCOME, applicationDetails);
        if (penalty.compareTo(ZERO) > 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_PENALTY_FINES),
                    putAmountAndType(penalty.setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
        if (penalty.compareTo(ZERO) < 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_PENALTY_FINES),
                    putAmountAndType(penalty.abs().setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? false : true));
        if (arrearsTax.compareTo(ZERO) > 0)
            voucherDetails.put(glCodeMap.get(ARREAR_TAX),
                    putAmountAndType(arrearsTax.abs().setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
        if (arrearsTax.compareTo(ZERO) < 0)
            voucherDetails.put(glCodeMap.get(ARREAR_TAX),
                    putAmountAndType(arrearsTax.abs().setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? false : true));
        if (currentTax.compareTo(ZERO) > 0)
            voucherDetails.put(glCodeMap.get(CURR_TAX),
                    putAmountAndType(currentTax.abs().setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
        if (currentTax.compareTo(ZERO) < 0)
            voucherDetails.put(glCodeMap.get(CURR_TAX),
                    putAmountAndType(currentTax.abs().setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? false : true));
        if (advance.compareTo(ZERO) > 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_ADVANCE),
                    putAmountAndType(advance.abs().setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
        if (advance.compareTo(ZERO) < 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_ADVANCE),
                    putAmountAndType(advance.abs().setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? false : true));
    }

    private void assembleIncomeHeads(Map<String, Map<String, Object>> voucherDetails, Map<String, String> glCodeMap,
            boolean demandIncreased, BigDecimal incomeHeadAmount, String demandReasonCode,
            Map<String, String> applicationDetails) {
        if (incomeHeadAmount.compareTo(ZERO) != 0) {
            /*
             * if overall demand is decreased and income head is increased or vice-versa, then income head amount will go to
             * credit and debit account respectively and should not be a special case for e.g. Vacancy Remission, Write Off, Court
             * Verdict.
             */
            if ((!demandIncreased && incomeHeadAmount.compareTo(ZERO) < 0
                    || demandIncreased && incomeHeadAmount.compareTo(ZERO) > 0) && !isSpecialCase(applicationDetails))
                voucherDetails.put(glCodeMap.get(demandReasonCode),
                        putAmountAndType(incomeHeadAmount.abs().setScale(2, BigDecimal.ROUND_HALF_UP),
                                demandIncreased ? true : false));
            else
                voucherDetails.put(glCodeMap.get(demandReasonCode),
                        putAmountAndType(incomeHeadAmount.abs().setScale(2, BigDecimal.ROUND_HALF_UP),
                                demandIncreased ? false : true));
        }
    }

    private Map<String, Object> putAmountAndType(BigDecimal amount, boolean demandIncreased) {
        Map<String, Object> values = new HashMap<>();
        values.put(AMOUNT, amount);
        values.put(AMOUNT_TYPE, demandIncreased ? VoucherConstant.DEBITAMOUNT : VoucherConstant.CREDITAMOUNT);
        return values;
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

    public List<DemandVoucherDetails> prepareDemandVoucherDetails(
            Installment currFirstHalf, Installment currSecondHalf, Ptdemand oldPtDemand, Ptdemand newPtDemand,
            Map<String, String> applicationDetails, boolean instChangeOpposite) {

        List<DemandVoucherDetails> demandVoucherDetailList = new ArrayList<>();
        List<NormalizeDemandDetails> normalizedDemandDetailListOld = new ArrayList<>();
        BigDecimal oldBalance = ZERO;
        BigDecimal newBalance = ZERO;
        List<NormalizeDemandDetails> normalizedDemandDetailListNew = normalizeDemandDetails(currFirstHalf, currSecondHalf,
                newPtDemand);
        if (oldPtDemand != null)
            normalizedDemandDetailListOld = normalizeDemandDetails(currFirstHalf, currSecondHalf,
                    oldPtDemand);
        else
            normalizedDemandDetailListOld = constructNormalizeDemandDetailsByApplicationType(normalizedDemandDetailListNew,
                    applicationDetails.get(PropertyTaxConstants.APPLICATION_TYPE));
        if (applicationDetails.get(PropertyTaxConstants.ACTION).equals(PropertyTaxConstants.ZERO_DEMAND))
            normalizedDemandDetailListNew = constructNormalizeDemandDetailsForZeroDemand(normalizedDemandDetailListNew);

        Iterator<NormalizeDemandDetails> oldIterator = normalizedDemandDetailListOld.iterator();
        Iterator<NormalizeDemandDetails> newIterator = normalizedDemandDetailListNew.iterator();
        while (oldIterator.hasNext() && newIterator.hasNext()) {
            DemandVoucherDetails demandVoucherDetails = new DemandVoucherDetails();
            NormalizeDemandDetails normalizeDemandDetailsOld = oldIterator.next();
            NormalizeDemandDetails normalizeDemandDetailsNew = newIterator.next();
            demandVoucherDetails.setInstallment(normalizeDemandDetailsOld.getInstallment());
            setVariationAmount(demandVoucherDetails, normalizeDemandDetailsOld, normalizeDemandDetailsNew);
            demandVoucherDetails.setLibraryCessVariation(
                    normalizeDemandDetailsOld.getLibraryCess().subtract(normalizeDemandDetailsNew.getLibraryCess()));
            demandVoucherDetails
                    .setAdvance(normalizeDemandDetailsNew.getAdvance().subtract(normalizeDemandDetailsOld.getAdvance()).abs());
            oldBalance = normalizeDemandDetailsOld.getGeneralTax().add(normalizeDemandDetailsOld.getLibraryCess())
                    .add(normalizeDemandDetailsOld.getVacantLandTax()).subtract(normalizeDemandDetailsOld
                            .getGeneralTaxCollection().add(normalizeDemandDetailsOld.getVacantLandTaxCollection())
                            .add(normalizeDemandDetailsOld.getLibraryCessCollection()));
            newBalance = normalizeDemandDetailsNew.getGeneralTax().add(normalizeDemandDetailsNew.getLibraryCess())
                    .add(normalizeDemandDetailsNew.getVacantLandTax()).subtract(normalizeDemandDetailsNew
                            .getGeneralTaxCollection().add(normalizeDemandDetailsNew.getVacantLandTaxCollection())
                            .add(normalizeDemandDetailsNew.getLibraryCessCollection()));

            if (isPenaltyCollectionApportioned(applicationDetails, normalizeDemandDetailsNew, normalizeDemandDetailsOld))
                demandVoucherDetails.setPenalty(normalizeDemandDetailsNew.getPenalty()
                        .subtract(normalizeDemandDetailsOld.getPenaltyCollection()));
            else {
                if (instChangeOpposite) {
                    demandVoucherDetails.setPenalty(normalizeDemandDetailsNew.getPenaltyCollection()
                            .subtract(normalizeDemandDetailsOld.getPenaltyCollection()));
                    demandVoucherDetails.setNetBalance(oldBalance.subtract(newBalance));
                } else {
                    demandVoucherDetails.setPenalty(normalizeDemandDetailsNew.getPenaltyCollection()
                            .subtract(normalizeDemandDetailsOld.getPenaltyCollection()).abs());
                    demandVoucherDetails.setNetBalance(newBalance.subtract(oldBalance));
                }
            }
            demandVoucherDetails.setPurpose(normalizeDemandDetailsOld.getPurpose());
            demandVoucherDetailList.add(demandVoucherDetails);
        }

        return demandVoucherDetailList;
    }

    public List<DemandVoucherDetails> prepareDemandVoucherDetailsForMismatch(
            Installment currFirstHalf, Installment currSecondHalf, Ptdemand oldPtDemand, Ptdemand newPtDemand,
            Map<String, String> applicationDetails, boolean isRPNewPropertyCase) {

        List<DemandVoucherDetails> demandVoucherDetailList = new ArrayList<>();
        List<NormalizeDemandDetails> normalizedDemandDetailListOld = new ArrayList<>();

        List<NormalizeDemandDetails> normalizedDemandDetailListNew = normalizeDemandDetails(currFirstHalf, currSecondHalf,
                newPtDemand);
        if (oldPtDemand != null)
            normalizedDemandDetailListOld = normalizeDemandDetails(currFirstHalf, currSecondHalf,
                    oldPtDemand);
        else
            normalizedDemandDetailListOld = constructNormalizeDemandDetailsByApplicationType(normalizedDemandDetailListNew,
                    applicationDetails.get(PropertyTaxConstants.APPLICATION_TYPE));
        if (applicationDetails.get(PropertyTaxConstants.ACTION).equals(PropertyTaxConstants.ZERO_DEMAND))
            normalizedDemandDetailListNew = constructNormalizeDemandDetailsForZeroDemand(normalizedDemandDetailListNew);
        List<NormalizeDemandDetails> largeList;
        List<NormalizeDemandDetails> smallList;
        // isNewPropertyLarger depicts whether new property has more no of installments than the old property.
        boolean isNewPropertyLarger = normalizedDemandDetailListNew.size() > normalizedDemandDetailListOld.size();
        if (isNewPropertyLarger) {
            largeList = normalizedDemandDetailListNew;
            smallList = normalizedDemandDetailListOld;
        } else {
            largeList = normalizedDemandDetailListOld;
            smallList = normalizedDemandDetailListNew;
        }
        for (int s = 0, l = 0; l < largeList.size(); l++) {
            DemandVoucherDetails demandVoucherDetails = new DemandVoucherDetails();
            if (largeList.get(l).getInstallment().equals(smallList.get(s).getInstallment())) {
                if (isNewPropertyLarger)

                    prepareDataForMatchedInstallments(applicationDetails, demandVoucherDetails, smallList.get(s),
                            largeList.get(l), isRPNewPropertyCase);
                else
                    prepareDataForMatchedInstallments(applicationDetails, demandVoucherDetails, largeList.get(l),
                            smallList.get(s), isRPNewPropertyCase);
                demandVoucherDetailList.add(demandVoucherDetails);
                s++;
            } else {
                prepareDataForMissingInstallments(demandVoucherDetails, largeList.get(l), isNewPropertyLarger);
                demandVoucherDetailList.add(demandVoucherDetails);
            }
        }
        return demandVoucherDetailList;
    }

    private void prepareDataForMatchedInstallments(Map<String, String> applicationDetails,
            DemandVoucherDetails demandVoucherDetails, NormalizeDemandDetails normalizeDemandDetailsOld,
            NormalizeDemandDetails normalizeDemandDetailsNew, boolean isRPNewPropertyCase) {
        BigDecimal oldBalance = ZERO;
        BigDecimal newBalance = ZERO;
        demandVoucherDetails.setInstallment(normalizeDemandDetailsOld.getInstallment());
        setVariationAmount(demandVoucherDetails, normalizeDemandDetailsOld, normalizeDemandDetailsNew);
        demandVoucherDetails.setLibraryCessVariation(
                normalizeDemandDetailsOld.getLibraryCess().subtract(normalizeDemandDetailsNew.getLibraryCess()));
        demandVoucherDetails
                .setAdvance(normalizeDemandDetailsNew.getAdvance().subtract(normalizeDemandDetailsOld.getAdvance()).abs());
        oldBalance = normalizeDemandDetailsOld.getGeneralTax().add(normalizeDemandDetailsOld.getLibraryCess())
                .add(normalizeDemandDetailsOld.getVacantLandTax()).subtract(normalizeDemandDetailsOld
                        .getGeneralTaxCollection().add(normalizeDemandDetailsOld.getVacantLandTaxCollection())
                        .add(normalizeDemandDetailsOld.getLibraryCessCollection()));
        newBalance = normalizeDemandDetailsNew.getGeneralTax().add(normalizeDemandDetailsNew.getLibraryCess())
                .add(normalizeDemandDetailsNew.getVacantLandTax()).subtract(normalizeDemandDetailsNew
                        .getGeneralTaxCollection().add(normalizeDemandDetailsNew.getVacantLandTaxCollection())
                        .add(normalizeDemandDetailsNew.getLibraryCessCollection()));
        if (isRPNewPropertyCase)
            demandVoucherDetails.setNetBalance(oldBalance.subtract(newBalance));
        else
            demandVoucherDetails.setNetBalance(newBalance.subtract(oldBalance));
        if (isPenaltyCollectionApportioned(applicationDetails, normalizeDemandDetailsNew, normalizeDemandDetailsOld))
            demandVoucherDetails.setPenalty(normalizeDemandDetailsNew.getPenalty()
                    .subtract(normalizeDemandDetailsOld.getPenaltyCollection()));
        else
            demandVoucherDetails.setPenalty(normalizeDemandDetailsNew.getPenaltyCollection()
                    .subtract(normalizeDemandDetailsOld.getPenaltyCollection()).abs());
        demandVoucherDetails.setPurpose(normalizeDemandDetailsOld.getPurpose());
    }

    private List<NormalizeDemandDetails> normalizeDemandDetails(Installment currFirstHalf, Installment currSecondHalf,
            Ptdemand ptDemand) {
        List<NormalizeDemandDetails> normalizedDemandDetailList = new ArrayList<>();
        final Map<Installment, Set<EgDemandDetails>> installmentWiseDemandDetails = propertyService
                .getEgDemandDetailsSetByInstallment(
                        ptDemand.getEgDemandDetails());
        final List<Installment> Installments = new ArrayList<>(installmentWiseDemandDetails.keySet());
        Collections.sort(Installments);
        for (final Installment installment : Installments) {
            Boolean isVacant = Boolean.FALSE;
            final NormalizeDemandDetails normalizedDemandDetail = new NormalizeDemandDetails();
            normalizedDemandDetail.setInstallment(installment);
            for (final String demandReason : DEMAND_REASONS) {
                final EgDemandDetails demandDetail = propertyService.getEgDemandDetailsForReason(
                        installmentWiseDemandDetails.get(installment), demandReason);

                if (demandDetail == null) {
                    continue;
                }
                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equals(DEMANDRSN_CODE_PENALTY_FINES)
                        || demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equals(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
                    normalizedDemandDetail.setPenalty(normalizedDemandDetail.getPenalty()
                            .add(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail)));
                    normalizedDemandDetail.setPenaltyCollection(
                            normalizedDemandDetail.getPenaltyCollection().add(demandDetail.getAmtCollected()));
                }
                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equals(DEMANDRSN_CODE_LIBRARY_CESS)) {
                    normalizedDemandDetail.setLibraryCess(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail));
                    normalizedDemandDetail.setLibraryCessCollection(demandDetail.getAmtCollected());
                }
                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equals(DEMANDRSN_CODE_ADVANCE)) {
                    normalizedDemandDetail.setAdvance(demandDetail.getAmtCollected());
                }
                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equals(DEMANDRSN_CODE_GENERAL_TAX)) {
                    normalizedDemandDetail.setGeneralTax(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail));
                    normalizedDemandDetail.setGeneralTaxCollection(demandDetail.getAmtCollected());
                }
                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equals(DEMANDRSN_CODE_VACANT_TAX)) {
                    normalizedDemandDetail.setVacantLandTax(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail));
                    normalizedDemandDetail.setVacantLandTaxCollection(demandDetail.getAmtCollected());
                    isVacant = Boolean.TRUE;
                }
                if (DEMAND_REASONS_COMMON.contains(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode())) {
                    normalizedDemandDetail.setCommonTax(normalizedDemandDetail.getCommonTax()
                            .add(propertyTaxCommonUtils.getTotalDemandVariationAmount(demandDetail)));
                    normalizedDemandDetail.setCommonTaxCollection(
                            normalizedDemandDetail.getCommonTaxCollection().add(demandDetail.getAmtCollected()));
                }
                if (demandDetail.getInstallmentStartDate().equals(currFirstHalf.getFromDate()) ||
                        demandDetail.getInstallmentStartDate().equals(currSecondHalf.getFromDate()))
                    normalizedDemandDetail.setPurpose(CURR_TAX);
                else
                    normalizedDemandDetail.setPurpose(ARREAR_TAX);

            }
            if (isVacant) {
                normalizedDemandDetail.setVacantLandTax(
                        normalizedDemandDetail.getVacantLandTax().add(normalizedDemandDetail.getCommonTax()));
                normalizedDemandDetail.setVacantLandTaxCollection(normalizedDemandDetail.getVacantLandTaxCollection()
                        .add(normalizedDemandDetail.getCommonTaxCollection()));
            } else {
                normalizedDemandDetail
                        .setGeneralTax(normalizedDemandDetail.getGeneralTax().add(normalizedDemandDetail.getCommonTax()));
                normalizedDemandDetail.setGeneralTaxCollection(normalizedDemandDetail.getGeneralTaxCollection()
                        .add(normalizedDemandDetail.getCommonTaxCollection()));
            }

            normalizedDemandDetailList.add(normalizedDemandDetail);
        }
        return normalizedDemandDetailList;
    }

    private List<NormalizeDemandDetails> constructEmptyNormalizeDemandDetails(
            List<NormalizeDemandDetails> normalizedDemandDetailList) {
        List<NormalizeDemandDetails> normalizedDemandDetails = new ArrayList<>();
        for (NormalizeDemandDetails nmd : normalizedDemandDetailList) {
            NormalizeDemandDetails details = new NormalizeDemandDetails();
            details.setInstallment(nmd.getInstallment());
            details.setAdvance(ZERO);
            details.setGeneralTax(ZERO);
            details.setGeneralTaxCollection(ZERO);
            details.setVacantLandTax(ZERO);
            details.setVacantLandTaxCollection(ZERO);
            details.setLibraryCess(ZERO);
            details.setLibraryCessCollection(ZERO);
            details.setPenalty(ZERO);
            details.setPenaltyCollection(ZERO);
            details.setPurpose(nmd.getPurpose());
            normalizedDemandDetails.add(details);
        }
        return normalizedDemandDetails;
    }

    private void setVariationAmount(DemandVoucherDetails demandVoucherDetails, NormalizeDemandDetails oldDetails,
            NormalizeDemandDetails newDetails) {
        if (oldDetails.getGeneralTax().compareTo(ZERO) > 0
                && newDetails.getVacantLandTax().compareTo(ZERO) > 0)
            demandVoucherDetails.setVacantTaxVariation(
                    oldDetails.getGeneralTax().subtract(newDetails.getVacantLandTax()));
        else if (newDetails.getGeneralTax().compareTo(ZERO) > 0
                && oldDetails.getVacantLandTax().compareTo(ZERO) > 0)
            demandVoucherDetails.setGeneralTaxVariation(
                    oldDetails.getVacantLandTax().subtract(newDetails.getGeneralTax()));
        else {
            demandVoucherDetails.setGeneralTaxVariation(
                    oldDetails.getGeneralTax().subtract(newDetails.getGeneralTax()));
            demandVoucherDetails.setVacantTaxVariation(
                    oldDetails.getVacantLandTax().subtract(newDetails.getVacantLandTax()));
        }

    }

    @Transactional
    public void persistPropertyDemandVoucher(PropertyImpl property, CVoucherHeader voucherHeader) {
        PropertyDemandVoucher demandVoucher = new PropertyDemandVoucher();
        demandVoucher.setProperty(property);
        demandVoucher.setVoucherHeader(voucherHeader);
        demandVoucherRepository.save(demandVoucher);
    }

    private boolean isDemandIncreased(BigDecimal existingPropTax, BigDecimal currentPropTax,
            Map<String, String> applicationDetails) {
        boolean demandIncreased = true;
        demandIncreased = currentPropTax.compareTo(existingPropTax) > 0 ? true : false;
        if (isSpecialCase(applicationDetails))
            demandIncreased = false;
        return demandIncreased;
    }

    private List<NormalizeDemandDetails> constructNormalizeDemandDetailsByApplicationType(
            List<NormalizeDemandDetails> normalizedDemandDetailList, String applicationType) {
        List<NormalizeDemandDetails> normalizedDemandDetails = new ArrayList<>();
        if (applicationType.equals(PropertyTaxConstants.APPLICATION_TYPE_VACANCY_REMISSION_APPROVAL))
            normalizedDemandDetails = constructNormalizeDemandDetailsForVacancyRemission(normalizedDemandDetailList);
        else
            normalizedDemandDetails = constructEmptyNormalizeDemandDetails(normalizedDemandDetailList);
        return normalizedDemandDetails;
    }

    private List<NormalizeDemandDetails> constructNormalizeDemandDetailsForZeroDemand(
            List<NormalizeDemandDetails> normalizedDemandDetailList) {
        List<NormalizeDemandDetails> normalizedDemandDetails = new ArrayList<>();
        for (NormalizeDemandDetails nmd : normalizedDemandDetailList) {
            NormalizeDemandDetails details = new NormalizeDemandDetails();
            details.setInstallment(nmd.getInstallment());
            details.setAdvance(ZERO);
            details.setGeneralTax(nmd.getGeneralTax().subtract(nmd.getGeneralTaxCollection()));
            details.setGeneralTaxCollection(ZERO);
            details.setVacantLandTax(nmd.getVacantLandTax().subtract(nmd.getVacantLandTaxCollection()));
            details.setVacantLandTaxCollection(ZERO);
            details.setLibraryCess(nmd.getLibraryCess().subtract(nmd.getLibraryCessCollection()));
            details.setLibraryCessCollection(ZERO);
            details.setPenalty(ZERO);
            details.setPenaltyCollection(ZERO);
            details.setPurpose(nmd.getPurpose());
            normalizedDemandDetails.add(details);
        }
        return normalizedDemandDetails;
    }

    private List<NormalizeDemandDetails> constructNormalizeDemandDetailsForVacancyRemission(
            List<NormalizeDemandDetails> normalizedDemandDetailList) {
        List<NormalizeDemandDetails> normalizedDemandDetails = new ArrayList<>();
        Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        Installment currSecondHalf = currYearInstMap.get(CURRENTYEAR_SECOND_HALF);
        for (NormalizeDemandDetails nmd : normalizedDemandDetailList) {
            NormalizeDemandDetails details = new NormalizeDemandDetails();
            if (!nmd.getInstallment().equals(currSecondHalf)) {
                details.setInstallment(nmd.getInstallment());
                details.setAdvance(nmd.getAdvance());
                details.setGeneralTax(nmd.getGeneralTax());
                details.setGeneralTaxCollection(nmd.getGeneralTaxCollection());
                details.setVacantLandTax(nmd.getVacantLandTax());
                details.setVacantLandTaxCollection(nmd.getVacantLandTaxCollection());
                details.setLibraryCess(nmd.getLibraryCess());
                details.setLibraryCessCollection(nmd.getLibraryCessCollection());
                details.setPenalty(nmd.getPenalty());
                details.setPenaltyCollection(nmd.getPenaltyCollection());
                details.setPurpose(nmd.getPurpose());
            } else {
                details = reduceDemandForVacancyRemission(nmd);
            }
            normalizedDemandDetails.add(details);
        }
        return normalizedDemandDetails;
    }

    private NormalizeDemandDetails reduceDemandForVacancyRemission(NormalizeDemandDetails nmd) {
        NormalizeDemandDetails details = new NormalizeDemandDetails();
        BigDecimal totalCollection = ZERO;
        totalCollection = totalCollection
                .add(nmd.getGeneralTaxCollection().add(nmd.getVacantLandTaxCollection().add(nmd.getLibraryCessCollection())))
                .add(nmd.getPenaltyCollection());
        details.setInstallment(nmd.getInstallment());
        details.setPurpose(nmd.getPurpose());
        if (nmd.getPenalty().compareTo(ZERO) > 0) {
            details.setPenalty(nmd.getPenalty().divide(new BigDecimal("2")).setScale(0,
                    BigDecimal.ROUND_HALF_UP));
            if (totalCollection.compareTo(details.getPenalty()) > 0) {
                details.setPenaltyCollection(details.getPenalty());
                totalCollection = totalCollection.subtract(details.getPenalty());
            } else
                details.setPenaltyCollection(totalCollection);
        }

        if (nmd.getGeneralTax().compareTo(ZERO) > 0) {
            details.setGeneralTax(nmd.getGeneralTax().divide(new BigDecimal("2")).setScale(0,
                    BigDecimal.ROUND_HALF_UP));
            if (totalCollection.compareTo(details.getGeneralTax()) > 0) {
                details.setGeneralTaxCollection(details.getGeneralTax());
                totalCollection = totalCollection.subtract(details.getGeneralTax());
            } else
                details.setGeneralTaxCollection(totalCollection);
        }

        if (nmd.getVacantLandTax().compareTo(ZERO) > 0) {
            details.setVacantLandTax(nmd.getVacantLandTax().divide(new BigDecimal("2")).setScale(0,
                    BigDecimal.ROUND_HALF_UP));
            if (totalCollection.compareTo(details.getVacantLandTax()) > 0) {
                details.setVacantLandTaxCollection(details.getVacantLandTax());
                totalCollection = totalCollection.subtract(details.getVacantLandTax());
            } else
                details.setVacantLandTaxCollection(totalCollection);
        }

        if (nmd.getLibraryCess().compareTo(ZERO) > 0) {
            details.setLibraryCess(nmd.getLibraryCess().divide(new BigDecimal("2")).setScale(0,
                    BigDecimal.ROUND_HALF_UP));
            if (totalCollection.compareTo(details.getLibraryCess()) > 0) {
                details.setLibraryCessCollection(details.getLibraryCess());
                totalCollection = totalCollection.subtract(details.getLibraryCess());
            } else
                details.setLibraryCessCollection(totalCollection);
        }

        if (totalCollection.compareTo(ZERO) > 0)
            details.setAdvance(totalCollection);

        return details;
    }

    private boolean isPenaltyCollectionApportioned(Map<String, String> applicationDetails,
            NormalizeDemandDetails normalizedDemandDetaiNew,
            NormalizeDemandDetails normalizedDemandDetailOld) {
        return applicationDetails.get(PropertyTaxConstants.APPLICATION_TYPE)
                .equals(PropertyTaxConstants.APPLICATION_TYPE_COURT_VERDICT) &&
                normalizedDemandDetaiNew.getPenalty().subtract(normalizedDemandDetailOld.getPenaltyCollection())
                        .compareTo(ZERO) < 0;
    }

    public boolean isSpecialCase(Map<String, String> applicationDetails) {
        return applicationDetails.get(PropertyTaxConstants.ACTION).equals(PropertyTaxConstants.ZERO_DEMAND)
                || applicationDetails.get(PropertyTaxConstants.APPLICATION_TYPE)
                        .equals(PropertyTaxConstants.APPLICATION_TYPE_VACANCY_REMISSION_APPROVAL);
    }

    public void prepareDataForMissingInstallments(DemandVoucherDetails demandVoucherDetails,
            NormalizeDemandDetails normalizeDemandDetailsLarge, boolean isNewPropertyLarger) {
        BigDecimal balance = ZERO;
        demandVoucherDetails.setInstallment(normalizeDemandDetailsLarge.getInstallment());
        if (isNewPropertyLarger) {
            demandVoucherDetails.setGeneralTaxVariation(
                    ZERO.subtract(normalizeDemandDetailsLarge.getGeneralTax()));
            demandVoucherDetails.setVacantTaxVariation(
                    ZERO.subtract(normalizeDemandDetailsLarge.getVacantLandTax()));
            demandVoucherDetails.setLibraryCessVariation(
                    ZERO.subtract(normalizeDemandDetailsLarge.getLibraryCess()));
            demandVoucherDetails
                    .setAdvance(normalizeDemandDetailsLarge.getAdvance().subtract(ZERO).abs());
            balance = normalizeDemandDetailsLarge.getGeneralTax().add(normalizeDemandDetailsLarge.getLibraryCess())
                    .add(normalizeDemandDetailsLarge.getVacantLandTax()).subtract(normalizeDemandDetailsLarge
                            .getGeneralTaxCollection().add(normalizeDemandDetailsLarge.getVacantLandTaxCollection())
                            .add(normalizeDemandDetailsLarge.getLibraryCessCollection()));
            demandVoucherDetails.setNetBalance(ZERO.subtract(balance));
        } else {
            demandVoucherDetails.setGeneralTaxVariation(
                    normalizeDemandDetailsLarge.getGeneralTax().subtract(ZERO));
            demandVoucherDetails.setVacantTaxVariation(
                    normalizeDemandDetailsLarge.getVacantLandTax().subtract(ZERO));
            demandVoucherDetails.setLibraryCessVariation(
                    normalizeDemandDetailsLarge.getLibraryCess().subtract(ZERO));
            demandVoucherDetails
                    .setAdvance(normalizeDemandDetailsLarge.getAdvance().subtract(ZERO).abs());
            balance = normalizeDemandDetailsLarge.getGeneralTax().add(normalizeDemandDetailsLarge.getLibraryCess())
                    .add(normalizeDemandDetailsLarge.getVacantLandTax()).subtract(normalizeDemandDetailsLarge
                            .getGeneralTaxCollection().add(normalizeDemandDetailsLarge.getVacantLandTaxCollection())
                            .add(normalizeDemandDetailsLarge.getLibraryCessCollection()));
            demandVoucherDetails.setNetBalance(balance.subtract(ZERO));
        }
        demandVoucherDetails.setPenalty(normalizeDemandDetailsLarge.getPenaltyCollection()
                .subtract(ZERO)
                .abs());
        demandVoucherDetails.setPurpose(normalizeDemandDetailsLarge.getPurpose());
    }

    public boolean areInstallmentsMismatch(Ptdemand oldDemand, Ptdemand newDemand) {
        return getInstallmentSet(oldDemand).size() != getInstallmentSet(newDemand).size();
    }

    public boolean ifInstallmentChangeIsOpposite(Ptdemand oldDemand, Ptdemand newDemand, boolean demandIncreased) {
        final Map<Installment, BigDecimal> instWiseDemandOld = getInstallmentWiseDemand(oldDemand);
        final Map<Installment, BigDecimal> instWiseDemandNew = getInstallmentWiseDemand(newDemand);
        for (Installment key : Sets.union(instWiseDemandOld.keySet(), instWiseDemandNew.keySet())) {
            BigDecimal oldAmount = instWiseDemandOld.get(key);
            BigDecimal newAmount = instWiseDemandNew.get(key);
            if ((demandIncreased && oldAmount.subtract(newAmount).compareTo(ZERO) > 0)
                    || (!demandIncreased && oldAmount.subtract(newAmount).compareTo(ZERO) < 0)) {
                return true;
            }

        }
        return false;
    }

    public Map<Installment, BigDecimal> getInstallmentWiseDemand(Ptdemand demand) {
        Map<Installment, BigDecimal> instWiseDemand = new LinkedHashMap<>();
        BigDecimal totalamount = ZERO;
        for (Installment inst : getInstallmentSet(demand)) {
            for (EgDemandDetails demandDetails : demand.getEgDemandDetails())
                if (demandDetails.getEgDemandReason().getEgInstallmentMaster().equals(inst))
                    totalamount = totalamount.add(demandDetails.getAmount());
            instWiseDemand.put(inst, totalamount);
            totalamount = ZERO;
        }
        return instWiseDemand;
    }

    public Set<Installment> getInstallmentSet(Ptdemand demand) {
        Set<Installment> installments = new LinkedHashSet<>(demand.getEgDemandDetails().size());
        demand.getEgDemandDetails().stream()
                .filter(p -> installments.add(p.getEgDemandReason().getEgInstallmentMaster()))
                .collect(Collectors.toList());
        return installments;
    }

}
