/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.stms.transactions.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CVoucherHeader;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageDemandVoucher;
import org.egov.stms.transactions.repository.SewerageDemandVoucherRepository;
import org.egov.stms.utils.SewerageFinancialUtil;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SewerageDemandVoucherService {

    private static final Logger LOGGER = Logger.getLogger(SewerageDemandVoucherService.class);
    private static final String APPCONFIG_STMS_DEMAND_VOUCHER_GLCODES = "STMS_DEMAND_VOUCHER_GLCODES";
    private static final String APPCONFIG_STMS_DEMAND_VOUCHER_REQUIRED = "STMS_DEMAND_VOUCHER_GENERATION_REQUIRED";
    private static final String APPCONFIG_VALUE_VOUCHER_ARREARRECEIVABLE_GLCODE = "ARREARRECEIVABLE";
    private static final String APPCONFIG_VALUE_VOUCHER_PRIORINCOMEGLCODE = "PRIORPERIODINCOME";
    private static final String APPCONFIG_VALUE_VOUCHER_CURRENTRECEIVABLE_GLCODE = "CURRENTRECEIVABLE";
    private static final String APPCONFIG_VALUE_VOUCHER_CURRENTINCOME_GLCODE = "CURRENTINCOME";

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    private SewerageFinancialUtil sewerageFinancialUtil;

    @Autowired
    SewerageDemandVoucherRepository sewerageDemandVoucherRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create Demand Voucher for the sewerage application details when demand is generated for an application
     * @param sewerageApplicationDetails
     */
    @Transactional
    public void createDemandVoucher(SewerageApplicationDetails sewerageApplicationDetails) {
        if (getDemandVoucherEnable()) {
            LOGGER.info("Sewerage demand voucher posting started");
            String uniqueIdentifier = StringUtils.isNotBlank(sewerageApplicationDetails.getConnection().getShscNumber())
					? sewerageApplicationDetails.getConnection().getShscNumber()
					: sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier();
			HashMap<String, Object> headerdetails = sewerageFinancialUtil.prepareHeaderDetails(
					sewerageApplicationDetails.getApplicationNumber(), uniqueIdentifier,
					sewerageApplicationDetails.getApplicationType().getName(), StringUtils.EMPTY);
            List<HashMap<String, Object>> accountDetails = prepareDemandVoucherAccountDetails(sewerageApplicationDetails,
                    headerdetails.get(VoucherConstant.FUNCTIONCODE).toString());
            if (!accountDetails.isEmpty() && !headerdetails.isEmpty()) {
                LOGGER.info("Sewerage Account Details :" + accountDetails);
                LOGGER.info("Sewerage Header Details :" + headerdetails);
                CVoucherHeader voucherHeader = sewerageFinancialUtil.createVoucher(accountDetails,
                        headerdetails);
                persistSewerageDemandVoucher(sewerageApplicationDetails, voucherHeader);
            }
        }
    }

    public Boolean getDemandVoucherEnable() {
        AppConfigValues appConfigValue = sewerageTaxUtils.getAppConfigValues(APPCONFIG_STMS_DEMAND_VOUCHER_REQUIRED);
        return appConfigValue != null
                ? "YES".equalsIgnoreCase(appConfigValue.getValue()) : Boolean.FALSE;
    }

    /**
     * prepareDemandVoucherAccountDetails creates demand voucher account details list
     * @param sewerageApplicationDetails Sewerage application details to create account details
     * @param functionCode function code to create account details
     * @return Voucher account details list to create demand voucher
     */
    public List<HashMap<String, Object>> prepareDemandVoucherAccountDetails(SewerageApplicationDetails sewerageApplicationDetails,
            final String functionCode) {
        EgDemand demand = sewerageApplicationDetails.getCurrentDemand();
        List<HashMap<String, Object>> accountDetList = new ArrayList<>();
        if (demand != null) {
            final List<EgDemandDetails> demandDetailsList = new ArrayList<>();
            demandDetailsList.addAll(demand.getEgDemandDetails());
            HashMap<String, Date> dateMap = sewerageFinancialUtil.getCurrentFinancialYearStartEndDate();

            List<EgDemandDetails> arrearDemanddDetails = demandDetailsList.stream()
                    .filter(dmdDetail -> SewerageTaxConstants.FEES_SEWERAGETAX_CODE
                            .equals(dmdDetail.getEgDemandReason().getEgDemandReasonMaster().getCode())
                            && dmdDetail.getInstallmentEndDate()
                                    .compareTo(dateMap.get(SewerageTaxConstants.FINANCIALYEAR_START_DATE)) < 0)
                    .collect(Collectors.toList());

            List<EgDemandDetails> currentDemandDetails = demandDetailsList.stream()
                    .filter(dmdDetail -> SewerageTaxConstants.FEES_SEWERAGETAX_CODE
                            .equals(dmdDetail.getEgDemandReason().getEgDemandReasonMaster().getCode())
                            && dmdDetail.getInstallmentEndDate()
                                    .compareTo(dateMap.get(SewerageTaxConstants.FINANCIALYEAR_START_DATE)) > 0)
                    .collect(Collectors.toList());
            BigDecimal arrearDemandAmount = arrearDemanddDetails.stream().map(EgDemandDetails::getAmount).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
            BigDecimal currentDemandAmount = currentDemandDetails.stream().map(EgDemandDetails::getAmount).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
            Map<String, String> glCodeMap = sewerageFinancialUtil.getAppconfigValueMap(APPCONFIG_STMS_DEMAND_VOUCHER_GLCODES);
            if (arrearDemandAmount.compareTo(BigDecimal.ZERO) > 0) {

                accountDetList.add(sewerageFinancialUtil.createAccDetailmap(glCodeMap.get(APPCONFIG_VALUE_VOUCHER_ARREARRECEIVABLE_GLCODE),
                        arrearDemandAmount,
                        BigDecimal.ZERO, functionCode));
                accountDetList
                        .add(sewerageFinancialUtil.createAccDetailmap(glCodeMap.get(APPCONFIG_VALUE_VOUCHER_PRIORINCOMEGLCODE),
                                BigDecimal.ZERO,
                                arrearDemandAmount, functionCode));
            }
            if (currentDemandAmount.compareTo(BigDecimal.ZERO) > 0) {
                accountDetList.add(sewerageFinancialUtil.createAccDetailmap(glCodeMap.get(APPCONFIG_VALUE_VOUCHER_CURRENTRECEIVABLE_GLCODE),
                        currentDemandAmount,
                        BigDecimal.ZERO, functionCode));
                accountDetList.add(sewerageFinancialUtil.createAccDetailmap(
                        currentDemandDetails.get(0).getEgDemandReason().getGlcodeId().getGlcode(),
                        BigDecimal.ZERO, currentDemandAmount, functionCode));
            }
        }
        return accountDetList;
    }

    @Transactional
    public void persistSewerageDemandVoucher(SewerageApplicationDetails sewerageApplicationDetails,
            CVoucherHeader voucherHeader) {

        SewerageDemandVoucher sewerageDemandVoucher = new SewerageDemandVoucher();
        sewerageDemandVoucher.setApplicationDetails(sewerageApplicationDetails);
        sewerageDemandVoucher.setVoucherHeader(voucherHeader);
        sewerageDemandVoucherRepository.save(sewerageDemandVoucher);
    }
    
    /**
     * Prepares account details for consolidated demand amount generated during rollover
     * @param totalDemand
     * @param functionCode
     * @return Voucher account details list to create demand voucher
     */
    public List<HashMap<String, Object>> prepareDemandVoucherAccountDetailsForRollover(BigDecimal totalDemand, String functionCode) {
        List<HashMap<String, Object>> accountDetList = new ArrayList<>();
        Map<String, String> glCodeMap = sewerageFinancialUtil.getAppconfigValueMap(APPCONFIG_STMS_DEMAND_VOUCHER_GLCODES);
		if (totalDemand.compareTo(BigDecimal.ZERO) > 0) {
			accountDetList.add(sewerageFinancialUtil.createAccDetailmap(
					glCodeMap.get(APPCONFIG_VALUE_VOUCHER_CURRENTRECEIVABLE_GLCODE), 
					totalDemand, BigDecimal.ZERO, functionCode));
			accountDetList.add(sewerageFinancialUtil.createAccDetailmap(
					glCodeMap.get(APPCONFIG_VALUE_VOUCHER_CURRENTINCOME_GLCODE),
					BigDecimal.ZERO, totalDemand, functionCode));
		}
        return accountDetList;
    }
    
    /**
     * Creates Demand Voucher for consolidated demand amount during rollover
     * @param installment
     * @param totalDemand
     */
    @Transactional
    public void createDemandVoucherAfterRollover(String installment, BigDecimal totalDemand) {
        if (getDemandVoucherEnable()) {
            LOGGER.info("Sewerage demand voucher posting for rollover started ");
			HashMap<String, Object> headerdetails = sewerageFinancialUtil.prepareHeaderDetails(
					StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, installment);
            List<HashMap<String, Object>> accountDetails = prepareDemandVoucherAccountDetailsForRollover(totalDemand,
                    headerdetails.get(VoucherConstant.FUNCTIONCODE).toString());
            if (!accountDetails.isEmpty() && !headerdetails.isEmpty()) {
                LOGGER.info("Sewerage Account Details :" + accountDetails);
                LOGGER.info("Sewerage Header Details :" + headerdetails);
                sewerageFinancialUtil.createVoucher(accountDetails,
                        headerdetails);
            }
        }
    }
}