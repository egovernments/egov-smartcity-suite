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

package org.egov.wtms.application.service;

import static org.egov.wtms.utils.constants.WaterTaxConstants.FINANCIALYEAR_START_DATE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CVoucherHeader;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.entity.WaterDemandVoucher;
import org.egov.wtms.application.repository.WaterDemandVoucherRepository;
import org.egov.wtms.utils.WaterFinancialUtil;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class WaterDemandVoucherService {

    private static final Logger LOGGER = Logger.getLogger(WaterDemandVoucherService.class);
    private static final String APPCONFIG_WCMS_DEMAND_VOUCHER_GLCODES = "WCMS_DEMAND_VOUCHER_GLCODES";
    private static final String APPCONFIG_WC_DEMAND_VOUCHER_REQUIRED = "WT_DEMAND_VOUCHER_GENERATION_REQUIRED";
    private static final String APPCONFIG_VALUE_VOUCHER_ARREARGLCODE = "ARREARRECEIVABLE";
    private static final String APPCONFIG_VALUE_VOUCHER_PRIORINCOMEGLCODE = "PRIORPERIODINCOME";
    private static final String APPCONFIG_VALUE_VOUCHER_CURRENTGLCODE = "CURRENTRECEIVABLE";
    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private WaterFinancialUtil waterFinancialUtil;

    @Autowired
    WaterDemandVoucherRepository waterDemandVoucherRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private WaterDemandConnectionService waterDemandConnectionService;

    @Transactional
    public void createDemandVoucher(WaterConnectionDetails waterConnectionDetails) {
        if (getDemandVoucherEnable()) {
            LOGGER.info("Water demand voucher posting start");
            HashMap<String, Object> headerdetails = waterFinancialUtil.prepareHeaderDetails(
                    waterConnectionDetails.getConnection().getConsumerCode(),
                    waterConnectionDetails.getApplicationType().getName());
            List<HashMap<String, Object>> accountDetails = prepareDemandVoucherAccountDetails(waterConnectionDetails,
                    headerdetails.get(VoucherConstant.FUNCTIONCODE).toString());
            if (!accountDetails.isEmpty() && !headerdetails.isEmpty()) {
                LOGGER.info("Water Account Details :" + accountDetails);
                LOGGER.info("Water Header Details :" + headerdetails);
                CVoucherHeader voucherHeader = waterFinancialUtil.createVoucher(accountDetails,
                        headerdetails);
                persistWaterDemandVoucher(waterConnectionDetails, voucherHeader);
            }
        }
    }

    public Boolean getDemandVoucherEnable() {
        List<AppConfigValues> appConfigValues = waterTaxUtils
                .getAppConfigValueByModuleNameAndKeyName(WaterTaxConstants.MODULE_NAME, APPCONFIG_WC_DEMAND_VOUCHER_REQUIRED);
        return appConfigValues != null && !appConfigValues.isEmpty()
                ? WaterTaxConstants.APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfigValues.get(0).getValue()) : Boolean.FALSE;
    }

    /**
     * prepareDemandVoucherAccountDetails created demand voucher account details list
     * @param waterConnectionDetails water connection details to create account details
     * @param functionCode function code to create account details
     * @return Voucher account details list to create demand voucher
     */

    public List<HashMap<String, Object>> prepareDemandVoucherAccountDetails(WaterConnectionDetails waterConnectionDetails,
            final String functionCode) {
        EgDemand demand = waterDemandConnectionService
                .getCurrentDemand(waterConnectionDetails).getDemand();
        List<HashMap<String, Object>> accountDetList = new ArrayList<>();
        if (demand != null) {
            final List<EgDemandDetails> demandDetailsList = new ArrayList<>();
            demandDetailsList.addAll(demand.getEgDemandDetails());
            HashMap<String, Date> dateMap = waterFinancialUtil.getCurrentFinancialYearStartEndDate();

            List<EgDemandDetails> arrearDemanddDetails = demandDetailsList.stream()
                    .filter(dmdDetail -> WaterTaxConstants.WATERTAXREASONCODE
                            .equals(dmdDetail.getEgDemandReason().getEgDemandReasonMaster().getCode())
                            && dmdDetail.getInstallmentEndDate().compareTo(dateMap.get(FINANCIALYEAR_START_DATE)) < 0)
                    .collect(Collectors.toList());

            List<EgDemandDetails> currentDemandDetails = demandDetailsList.stream()
                    .filter(dmdDetail -> WaterTaxConstants.WATERTAXREASONCODE
                            .equals(dmdDetail.getEgDemandReason().getEgDemandReasonMaster().getCode())
                            && dmdDetail.getInstallmentEndDate().compareTo(dateMap.get(FINANCIALYEAR_START_DATE)) > 0)
                    .collect(Collectors.toList());
            BigDecimal arrearDemandAmount = arrearDemanddDetails.stream().map(EgDemandDetails::getAmount).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
            BigDecimal currentDemandAmount = currentDemandDetails.stream().map(EgDemandDetails::getAmount).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
            Map<String, String> glCodeMap = waterFinancialUtil.getAppconfigValueMap(APPCONFIG_WCMS_DEMAND_VOUCHER_GLCODES);
            if (arrearDemandAmount.compareTo(BigDecimal.ZERO) > 0) {

                accountDetList.add(waterFinancialUtil.createAccDetailmap(glCodeMap.get(APPCONFIG_VALUE_VOUCHER_ARREARGLCODE),
                        arrearDemandAmount,
                        BigDecimal.ZERO, functionCode));
                accountDetList.add(waterFinancialUtil.createAccDetailmap(glCodeMap.get(APPCONFIG_VALUE_VOUCHER_PRIORINCOMEGLCODE),
                        BigDecimal.ZERO,
                        arrearDemandAmount, functionCode));
            }
            if (currentDemandAmount.compareTo(BigDecimal.ZERO) > 0) {
                accountDetList.add(waterFinancialUtil.createAccDetailmap(glCodeMap.get(APPCONFIG_VALUE_VOUCHER_CURRENTGLCODE),
                        currentDemandAmount,
                        BigDecimal.ZERO, functionCode));
                accountDetList.add(waterFinancialUtil.createAccDetailmap(
                        currentDemandDetails.get(0).getEgDemandReason().getGlcodeId().getGlcode(),
                        BigDecimal.ZERO, currentDemandAmount, functionCode));
            }
        }
        return accountDetList;
    }

    @Transactional
    public void persistWaterDemandVoucher(WaterConnectionDetails waterConnectionDetails, CVoucherHeader voucherHeader) {

        WaterDemandVoucher waterDemandVoucher = new WaterDemandVoucher();
        waterDemandVoucher.setWaterConnectionDetails(waterConnectionDetails);
        waterDemandVoucher.setVoucherHeader(voucherHeader);
        waterDemandVoucherRepository.save(waterDemandVoucher);
    }

}
