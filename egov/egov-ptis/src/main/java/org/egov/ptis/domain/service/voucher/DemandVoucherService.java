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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

@Transactional(readOnly = true)
@Service
public class DemandVoucherService {

    private static final String AMOUNT_TYPE = "amountType";
    private static final String AMOUNT = "amount";
    private static final String ARREAR_TAX = "ARREAR_TAX";
    private static final String CURR_TAX = "CURR_TAX";
    protected static final Set<String> DEMAND_REASONS = new LinkedHashSet<>(Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
            DEMANDRSN_CODE_GENERAL_TAX, DEMANDRSN_CODE_DRAINAGE_TAX, DEMANDRSN_CODE_SCAVENGE_TAX,
            DEMANDRSN_CODE_WATER_TAX, DEMANDRSN_CODE_LIGHT_TAX, DEMANDRSN_CODE_VACANT_TAX, DEMANDRSN_CODE_EDUCATIONAL_TAX,
            DEMANDRSN_CODE_LIBRARY_CESS, DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, DEMANDRSN_CODE_ADVANCE));
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
        if ("Y".equalsIgnoreCase(appConfigValue)) {
            Map<String, Map<String, Object>> voucherData = prepareDemandVoucherData(newProperty, oldProperty, applicationDetails);
            if (!voucherData.isEmpty()) {
                CVoucherHeader cvh = financialUtil.createVoucher(newProperty.getBasicProperty().getUpicNo(), voucherData,
                        applicationDetails.get(PropertyTaxConstants.APPLICATION_TYPE));
                persistPropertyDemandVoucher(newProperty, cvh);
            }
        }
    }

    public Map<String, Map<String, Object>> prepareDemandVoucherData(Property newProperty, Property oldProperty,
            Map<String, String> applicationDetails) {
        BigDecimal existingPropTax = ZERO;
        BigDecimal currentPropTax = getTotalPropertyTax(newProperty);
        if (oldProperty != null)
            existingPropTax = getTotalPropertyTax(oldProperty);
        boolean demandIncreased = isDemandIncreased(existingPropTax, currentPropTax, applicationDetails);
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
        Ptdemand oldPtDemand;
        List<DemandVoucherDetails> demandVoucherDetailList = new ArrayList<>();
        if (oldProperty != null) {
            oldPtDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(oldProperty);
            demandVoucherDetailList = prepareDemandVoucherDetails(currFirstHalf, currSecondHalf,
                    oldPtDemand, ptDemand, applicationDetails);
        } else
            demandVoucherDetailList = prepareDemandVoucherDetails(currFirstHalf, currSecondHalf,
                    null, ptDemand, applicationDetails);

        if (!demandVoucherDetailList.isEmpty())
            prepareVoucherDetailsMap(voucherDetails, glCodeMap, demandIncreased,
                    demandVoucherDetailList);

        return voucherDetails;
    }

    private void prepareVoucherDetailsMap(Map<String, Map<String, Object>> voucherDetails,
            Map<String, String> glCodeMap, boolean demandIncreased,
            List<DemandVoucherDetails> demandVoucherDetailList) {
        BigDecimal generalTax = BigDecimal.ZERO;
        BigDecimal vacantLandtax = BigDecimal.ZERO;
        BigDecimal libraryCess = BigDecimal.ZERO;
        BigDecimal priorIncome = BigDecimal.ZERO;
        BigDecimal arrearsTax = BigDecimal.ZERO;
        BigDecimal currentTax = BigDecimal.ZERO;
        BigDecimal penalty = BigDecimal.ZERO;
        BigDecimal advance = BigDecimal.ZERO;
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

        if (advance.compareTo(BigDecimal.ZERO) > 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_ADVANCE),
                    putAmountAndType(advance.setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
        if (generalTax.compareTo(BigDecimal.ZERO) > 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_GENERAL_TAX),
                    putAmountAndType(generalTax.setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? false : true));
        if (vacantLandtax.compareTo(BigDecimal.ZERO) > 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_VACANT_TAX),
                    putAmountAndType(vacantLandtax.setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? false : true));
        if (libraryCess.compareTo(BigDecimal.ZERO) > 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_LIBRARY_CESS),
                    putAmountAndType(libraryCess.setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? false : true));
        if (penalty.compareTo(BigDecimal.ZERO) > 0)
            voucherDetails.put(glCodeMap.get(DEMANDRSN_CODE_PENALTY_FINES),
                    putAmountAndType(penalty.setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
        if (priorIncome.compareTo(BigDecimal.ZERO) > 0)
            voucherDetails.put(glCodeMap.get(PRIOR_INCOME),
                    putAmountAndType(priorIncome.setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? false : true));
        if (arrearsTax.compareTo(BigDecimal.ZERO) > 0)
            voucherDetails.put(glCodeMap.get(ARREAR_TAX),
                    putAmountAndType(arrearsTax.setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
        if (currentTax.compareTo(BigDecimal.ZERO) > 0)
            voucherDetails.put(glCodeMap.get(CURR_TAX),
                    putAmountAndType(currentTax.setScale(2, BigDecimal.ROUND_HALF_UP), demandIncreased ? true : false));
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
            Map<String, String> applicationDetails) {

        List<DemandVoucherDetails> demandVoucherDetailList = new ArrayList<>();
        List<NormalizeDemandDetails> normalizedDemandDetailListOld = new ArrayList<>();
        BigDecimal oldBalance = BigDecimal.ZERO;
        BigDecimal newBalance = BigDecimal.ZERO;

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
            final DemandVoucherDetails demandVoucherDetails = new DemandVoucherDetails();
            NormalizeDemandDetails normalizeDemandDetailsOld = oldIterator.next();
            NormalizeDemandDetails normalizeDemandDetailsNew = newIterator.next();
            demandVoucherDetails.setInstallment(normalizeDemandDetailsOld.getInstallment());
            setVariationAmount(demandVoucherDetails, normalizeDemandDetailsOld, normalizeDemandDetailsNew);
            demandVoucherDetails.setLibraryCessVariation(
                    normalizeDemandDetailsNew.getLibraryCess().subtract(normalizeDemandDetailsOld.getLibraryCess()).abs());
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
            demandVoucherDetails.setNetBalance(newBalance.subtract(oldBalance).abs());
            demandVoucherDetails.setPenalty(normalizeDemandDetailsNew.getPenalty()
                    .subtract(normalizeDemandDetailsNew.getPenaltyCollection())
                    .subtract(normalizeDemandDetailsOld.getPenalty().subtract(normalizeDemandDetailsOld.getPenaltyCollection()))
                    .abs());
            demandVoucherDetails.setPurpose(normalizeDemandDetailsOld.getPurpose());
            demandVoucherDetailList.add(demandVoucherDetails);
        }

        return demandVoucherDetailList;
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
                        .equals(DEMANDRSN_CODE_PENALTY_FINES)) {
                    normalizedDemandDetail.setPenalty(demandDetail.getAmount());
                    normalizedDemandDetail.setPenaltyCollection(demandDetail.getAmtCollected());
                }
                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equals(DEMANDRSN_CODE_LIBRARY_CESS)) {
                    normalizedDemandDetail.setLibraryCess(demandDetail.getAmount());
                    normalizedDemandDetail.setLibraryCessCollection(demandDetail.getAmtCollected());
                }
                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equals(DEMANDRSN_CODE_ADVANCE)) {
                    normalizedDemandDetail.setAdvance(demandDetail.getAmtCollected());
                }
                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equals(DEMANDRSN_CODE_GENERAL_TAX)) {
                    normalizedDemandDetail.setGeneralTax(demandDetail.getAmount());
                    normalizedDemandDetail.setGeneralTaxCollection(demandDetail.getAmtCollected());
                }
                if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equals(DEMANDRSN_CODE_VACANT_TAX)) {
                    normalizedDemandDetail.setVacantLandTax(demandDetail.getAmount());
                    normalizedDemandDetail.setVacantLandTaxCollection(demandDetail.getAmtCollected());
                    isVacant = Boolean.TRUE;
                }
                if (DEMAND_REASONS_COMMON.contains(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode())) {
                    normalizedDemandDetail.setCommonTax(normalizedDemandDetail.getCommonTax().add(demandDetail.getAmount()));
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
        if (oldDetails.getGeneralTax().compareTo(BigDecimal.ZERO) > 0
                && newDetails.getVacantLandTax().compareTo(BigDecimal.ZERO) > 0)
            demandVoucherDetails.setVacantTaxVariation(
                    newDetails.getVacantLandTax().subtract(oldDetails.getGeneralTax()).abs());
        else if (newDetails.getGeneralTax().compareTo(BigDecimal.ZERO) > 0
                && oldDetails.getVacantLandTax().compareTo(BigDecimal.ZERO) > 0)
            demandVoucherDetails.setGeneralTaxVariation(
                    newDetails.getGeneralTax().subtract(oldDetails.getVacantLandTax()).abs());
        else {
            demandVoucherDetails.setGeneralTaxVariation(
                    newDetails.getGeneralTax().subtract(oldDetails.getGeneralTax()).abs());
            demandVoucherDetails.setVacantTaxVariation(
                    newDetails.getVacantLandTax().subtract(oldDetails.getVacantLandTax()).abs());
        }

    }

    @Transactional
    public void persistPropertyDemandVoucher(PropertyImpl property, CVoucherHeader voucherHeader) {
        PropertyDemandVoucher demandVoucher = new PropertyDemandVoucher();
        demandVoucher.setProperty(property);
        demandVoucher.setVoucherHeader(voucherHeader);
        demandVoucherRepository.save(demandVoucher);
    }

    private boolean isDemandIncreased(BigDecimal existingPropTax, BigDecimal currentPropTax, Map<String, String> applicationDetails) {
        boolean demandIncreased = true;
        demandIncreased = currentPropTax.compareTo(existingPropTax) > 0 ? true : false;
        if (applicationDetails.get(PropertyTaxConstants.ACTION).equals(PropertyTaxConstants.ZERO_DEMAND)
                || applicationDetails.get(PropertyTaxConstants.APPLICATION_TYPE).equals(PropertyTaxConstants.APPLICATION_TYPE_VACANCY_REMISSION_APPROVAL))
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
        BigDecimal totalCollection = BigDecimal.ZERO;
        totalCollection = totalCollection
                .add(nmd.getGeneralTaxCollection().add(nmd.getVacantLandTaxCollection().add(nmd.getLibraryCessCollection())))
                .add(nmd.getPenaltyCollection());
        details.setInstallment(nmd.getInstallment());
        details.setPurpose(nmd.getPurpose());
        if (nmd.getPenalty().compareTo(BigDecimal.ZERO) > 0) {
            details.setPenalty(nmd.getPenalty().divide(new BigDecimal("2")).setScale(0,
                    BigDecimal.ROUND_HALF_UP));
            if (totalCollection.compareTo(details.getPenalty()) > 0) {
                details.setPenaltyCollection(details.getPenalty());
                totalCollection = totalCollection.subtract(details.getPenalty());
            } else
                details.setPenaltyCollection(totalCollection);
        }

        if (nmd.getGeneralTax().compareTo(BigDecimal.ZERO) > 0) {
            details.setGeneralTax(nmd.getGeneralTax().divide(new BigDecimal("2")).setScale(0,
                    BigDecimal.ROUND_HALF_UP));
            if (totalCollection.compareTo(details.getGeneralTax()) > 0) {
                details.setGeneralTaxCollection(details.getGeneralTax());
                totalCollection = totalCollection.subtract(details.getGeneralTax());
            } else
                details.setGeneralTaxCollection(totalCollection);
        }

        if (nmd.getVacantLandTax().compareTo(BigDecimal.ZERO) > 0) {
            details.setVacantLandTax(nmd.getVacantLandTax().divide(new BigDecimal("2")).setScale(0,
                    BigDecimal.ROUND_HALF_UP));
            if (totalCollection.compareTo(details.getVacantLandTax()) > 0) {
                details.setVacantLandTaxCollection(details.getVacantLandTax());
                totalCollection = totalCollection.subtract(details.getVacantLandTax());
            } else
                details.setVacantLandTaxCollection(totalCollection);
        }

        if (nmd.getLibraryCess().compareTo(BigDecimal.ZERO) > 0) {
            details.setLibraryCess(nmd.getLibraryCess().divide(new BigDecimal("2")).setScale(0,
                    BigDecimal.ROUND_HALF_UP));
            if (totalCollection.compareTo(details.getLibraryCess()) > 0) {
                details.setLibraryCessCollection(details.getLibraryCess());
                totalCollection = totalCollection.subtract(details.getLibraryCess());
            } else
                details.setLibraryCessCollection(totalCollection);
        }

        if (totalCollection.compareTo(BigDecimal.ZERO) > 0)
            details.setAdvance(totalCollection);

        return details;
    }
}
