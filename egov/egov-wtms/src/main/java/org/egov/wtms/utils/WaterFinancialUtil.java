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
package org.egov.wtms.utils;

import static org.egov.wtms.utils.constants.WaterTaxConstants.FINANCIALYEAR_END_DATE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FINANCIALYEAR_START_DATE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaterFinancialUtil {

    private static final Logger LOGGER = Logger.getLogger(WaterFinancialUtil.class);
    private static final String DCB_URL = "/wtms/viewDcb/consumerCodeWis/%s";
    private static final String VOUCHERDESCRIPTION = "WT %s demand voucher for %s  ";
    private static final String WCMS_DEMAND_VOUCHER_HEADER_PARAMS = "WCMS_DEMAND_VOUCHER_HEADER_PARAMS";
    private static final String DEMANDVOUCHER_DEPARTMENTCODE = "DEPARTMENT";
    private static final String DEMANDVOUCHER_FUNCTIONCODE = "FUNCTION";
    private static final String DEMANDVOUCHER_FUNDCODE = "FUND";
    private static final String DEMANDVOUCHER_FUNDSOURCECODE = "FUNDSOURCE";
    private static final String DEMANDVOUCHER_VOUCHERNAME = "VOUCHERNAME";
    private static final String DEMANDVOUCHER_VOUCHERTYPE = "VOUCHERTYPE";
    private static final String DEMANDVOUCHER_VOUCHERSTATUS = "VOUCHERSTATUS";
    private static final String MODULESID_QUERY = "select id from eg_modules where code =:code";
    final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private static final String PIPE_SEPERATOR = "|";

    @Autowired
    private CreateVoucher createVoucher;
    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CFinancialYearService cFinancialYearService;

    /**
     * createVoucher create the water demand voucher
     * @param accountDetList Account details required to created voucher
     * @param headerdetails Voucher header details
     * @return demand voucher created in finance.
     */

    public CVoucherHeader createVoucher(List<HashMap<String, Object>> accountDetList, HashMap<String, Object> headerdetails) {
        validateAccountDetails(accountDetList);
        try {
            CVoucherHeader voucher = createVoucher.createVoucher(headerdetails, accountDetList,
                    new ArrayList<HashMap<String, Object>>());
            if (voucher == null) {
                LOGGER.error("Voucher Creation failed. CVoucherHeader is null.");
                throw new ApplicationRuntimeException("Voucher Creation failed.");
            }
            LOGGER.info(" WT create demand voucher is created. Voucher number : " + voucher.getVoucherNumber());
            return voucher;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("createVoucher: headerdetails==>" + headerdetails + " \n accountDetList ==>" + accountDetList);
            throw new ApplicationRuntimeException("FAILED TO CREATE WT DEMAND VOUCHER", e);
        }
    }

    /**
     * prepareHeaderDetails prepares voucher header date required to create demand voucher
     * @param consumerCode is is Water connection consumer code
     * @param applicationType Water connection application type.
     * @return Voucher header details map
     */
    public HashMap<String, Object> prepareHeaderDetails(String consumerCode, String applicationType) {
        HashMap<String, Object> headerdetails = new HashMap<>();
        try {
            headerdetails.put(VoucherConstant.VOUCHERDATE, format.parse(format.format(new Date())));
        } catch (final ParseException e) {
            LOGGER.error("Exception while parsing voucher date", e);
            throw new ApplicationRuntimeException(e.getMessage());
        }
        Map<String, String> voucherHeaderDataMap = getAppconfigValueMap(WCMS_DEMAND_VOUCHER_HEADER_PARAMS);
        headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeaderDataMap.get(DEMANDVOUCHER_VOUCHERNAME));
        headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeaderDataMap.get(DEMANDVOUCHER_VOUCHERTYPE));
        headerdetails.put(VoucherConstant.FUNDSOURCECODE, voucherHeaderDataMap.get(DEMANDVOUCHER_FUNDSOURCECODE));
        headerdetails.put(VoucherConstant.STATUS, voucherHeaderDataMap.get(DEMANDVOUCHER_VOUCHERSTATUS));
        headerdetails.put(VoucherConstant.DEPARTMENTCODE, voucherHeaderDataMap.get(DEMANDVOUCHER_DEPARTMENTCODE));
        headerdetails.put(VoucherConstant.FUNDCODE, voucherHeaderDataMap.get(DEMANDVOUCHER_FUNDCODE));
        headerdetails.put(VoucherConstant.FUNCTIONCODE, voucherHeaderDataMap.get(DEMANDVOUCHER_FUNCTIONCODE));
        headerdetails.put(VoucherConstant.MODULEID, getModulesId());
        headerdetails.put(VoucherConstant.SOURCEPATH, String.format(DCB_URL, consumerCode));
        headerdetails.put(VoucherConstant.DESCRIPTION, String.format(VOUCHERDESCRIPTION, applicationType, consumerCode));
        return headerdetails;
    }

    private BigInteger getModulesId() {
        final Query query = entityManager.unwrap(Session.class).createSQLQuery(MODULESID_QUERY);
        query.setParameter("code", WaterTaxConstants.WATERTAX_CHARGES_SERVICE_CODE);
        List<BigInteger> result = query.list();
        return result.isEmpty() ? BigInteger.ZERO : result.get(0);
    }

    public HashMap<String, Date> getCurrentFinancialYearStartEndDate() {
        HashMap<String, Date> dateMap = new HashMap<>();
        final CFinancialYear financialYear = cFinancialYearService.getCurrentFinancialYear();
        dateMap.put(FINANCIALYEAR_START_DATE, financialYear.getStartingDate());
        dateMap.put(FINANCIALYEAR_END_DATE, financialYear.getEndingDate());
        return dateMap;
    }

    private void validateAccountDetails(List<HashMap<String, Object>> accountDetList) {
        BigDecimal totaldebitAmount = BigDecimal.valueOf(0);
        BigDecimal totalcreditAmount = BigDecimal.valueOf(0);
        for (final HashMap<String, Object> accDetailMap : accountDetList) {
            final BigDecimal debitAmount = new BigDecimal(accDetailMap.get(
                    VoucherConstant.DEBITAMOUNT).toString());
            final BigDecimal creditAmount = new BigDecimal(accDetailMap.get(
                    VoucherConstant.CREDITAMOUNT).toString());
            totaldebitAmount = totaldebitAmount.add(debitAmount);
            totalcreditAmount = totalcreditAmount.add(creditAmount);
            if (debitAmount.compareTo(BigDecimal.ZERO) != 0
                    && creditAmount.compareTo(BigDecimal.ZERO) != 0)
                throw new ApplicationRuntimeException(
                        "Both debit amount and credit amount cannot be greater than zero");
            if (debitAmount.compareTo(BigDecimal.ZERO) == 0
                    && creditAmount.compareTo(BigDecimal.ZERO) == 0)
                throw new ApplicationRuntimeException(
                        "debit and credit both amount is Zero");

        }
        if (totaldebitAmount.compareTo(totalcreditAmount) != 0)
            throw new ApplicationRuntimeException(
                    "total debit and total credit amount is not matching");
    }

    public Map<String, String> getAppconfigValueMap(String appconfigKeyName) {
        final List<AppConfigValues> appConfigValuesList = waterTaxUtils
                .getAppConfigValueByModuleNameAndKeyName(
                        WaterTaxConstants.MODULE_NAME, appconfigKeyName);
        Map<String, String> voucherHeaderDataMap = new HashMap<>();
        for (final AppConfigValues appConfigVal : appConfigValuesList) {
            final String value = appConfigVal.getValue();
            voucherHeaderDataMap.put(appConfigVal.getValue().substring(0, value.indexOf(PIPE_SEPERATOR)),
                    appConfigVal.getValue().substring(value.indexOf(PIPE_SEPERATOR) + 1));
        }
        return voucherHeaderDataMap;
    }

    public HashMap<String, Object> createAccDetailmap(String glcode, BigDecimal debitAmount, BigDecimal creditAmount,
            String functionCode) {
        HashMap<String, Object> accountdetailmap = new HashMap<>();
        accountdetailmap.put(VoucherConstant.GLCODE, glcode);
        accountdetailmap.put(VoucherConstant.DEBITAMOUNT, debitAmount);
        accountdetailmap.put(VoucherConstant.CREDITAMOUNT, creditAmount);
        accountdetailmap.put(VoucherConstant.FUNCTIONCODE, functionCode);
        return accountdetailmap;
    }

}