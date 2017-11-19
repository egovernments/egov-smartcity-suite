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
package org.egov.ptis.client.util;

import static org.egov.billsaccounting.services.VoucherConstant.VOUCHERNUMBER;
import static org.egov.ptis.constants.PropertyTaxConstants.ARREARS_DEMAND;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_DEMAND;
import static org.egov.ptis.constants.PropertyTaxConstants.DEFAULT_FUNCTIONARY_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEFAULT_FUND_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEFAULT_FUND_SRC_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEPT_CODE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.FUNCTION_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODEMAP_FOR_ARREARTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODEMAP_FOR_CURRENTTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODEMAP_FOR_TAX_PAYABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.PTIS_EG_MODULES_ID;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author subhash Provides API to create Voucher in financials whenever there
 *         is change(increase/decrease) in demand in PTIS. Use this API to
 *         create Voucher in the case of demand change ( either increment or
 *         decrement )
 */

public class FinancialUtil {
    private static final Logger LOGGER = Logger.getLogger(FinancialUtil.class);

    private static final String VOUCHERNAME = "JVoucher";

    private static final String VOUCHERTYPE = "Journal Voucher";

    private static final String URL_FOR_DCB = "/ptis/view/viewDCBProperty!displayPropInfo.action?propertyId=";

    @Autowired
    private ModuleService moduleDao;
    @Autowired
    private InstallmentDao installmentDao;
    @Autowired
    private CreateVoucher createVoucher;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtil;

    /**
     * This method creates a Voucher
     * 
     * @param indexNum
     *            Property id for which the voucher is creating.
     * @param amounts
     *            Map of Installment and reason wise demand map demand map --
     *            (key-demand reason, val-respective demand)
     * @param transaction
     *            Reason for voucher creation ( Property creation or
     *            modification etc )
     * @throws IOException
     */
    public void createVoucher(String indexNum, Map<Installment, Map<String, BigDecimal>> amounts, String transaction) {

        LOGGER.info("createVoucher: IndexNumber==>" + indexNum + " amounts==>" + amounts + "actionName==>"
                + transaction);

        Map<String, Map<String, BigDecimal>> resultMap = prepareDemandForGlcode(amounts);
        Map<String, BigDecimal> arrearsDemandMap = resultMap.get(ARREARS_DEMAND);
        Map<String, BigDecimal> currentDemandMap = resultMap.get(CURRENT_DEMAND);

        HashMap<String, Object> headerdetails = createHeaderDetails(indexNum, transaction);
        List<HashMap<String, Object>> accountDetList = new ArrayList<HashMap<String, Object>>();
        try {

            for (Map.Entry<String, BigDecimal> arrearsDemand : arrearsDemandMap.entrySet()) {
                if (arrearsDemand.getValue().compareTo(BigDecimal.ZERO) == 1) {
                    accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_ARREARTAX.get(arrearsDemand.getKey()),
                            arrearsDemand.getValue().abs(), BigDecimal.ZERO));
                    accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_TAX_PAYABLE.get(arrearsDemand.getKey()),
                            BigDecimal.ZERO, arrearsDemand.getValue().abs()));

                } else if (arrearsDemand.getValue().compareTo(BigDecimal.ZERO) == -1) {
                    accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_ARREARTAX.get(arrearsDemand.getKey()),
                            BigDecimal.ZERO, arrearsDemand.getValue().abs()));
                    accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_TAX_PAYABLE.get(arrearsDemand.getKey()),
                            arrearsDemand.getValue().abs(), BigDecimal.ZERO));
                }
            }
            for (Map.Entry<String, BigDecimal> currentDemand : currentDemandMap.entrySet()) {
                if (currentDemand.getValue().compareTo(BigDecimal.ZERO) == 1) {
                    accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_CURRENTTAX.get(currentDemand.getKey()),
                            currentDemand.getValue().abs(), BigDecimal.ZERO));
                    accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_TAX_PAYABLE.get(currentDemand.getKey()),
                            BigDecimal.ZERO, currentDemand.getValue().abs()));

                } else if (currentDemand.getValue().compareTo(BigDecimal.ZERO) == -1) {

                    if (currentDemand.getKey().equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE)) {
                        accountDetList.add(createAccDetailmap(PropertyTaxConstants.GLCODE_FOR_ADVANCE, BigDecimal.ZERO,
                                currentDemand.getValue().abs()));
                        accountDetList.add(createAccDetailmap(PropertyTaxConstants.GLCODE_FOR_ADVANCE, currentDemand
                                .getValue().abs(), BigDecimal.ZERO));
                    } else {
                        accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_CURRENTTAX.get(currentDemand.getKey()),
                                BigDecimal.ZERO, currentDemand.getValue().abs()));
                        accountDetList.add(createAccDetailmap(GLCODEMAP_FOR_TAX_PAYABLE.get(currentDemand.getKey()),
                                currentDemand.getValue().abs(), BigDecimal.ZERO));
                    }

                }

            }

            CVoucherHeader cvh = createVoucher.createVoucher(headerdetails, accountDetList,
                    new ArrayList<HashMap<String, Object>>());
            if (cvh == null) {
                LOGGER.error("Voucher Creation failed. CVoucherHeader is null.");
                throw new ApplicationRuntimeException("Voucher Creation failed.");
            }
            LOGGER.info("createVoucherForPTIS(): Voucher is created for PTIS with the voucher number : "
                    + cvh.getVoucherNumber());
        } catch (Throwable t) {
            LOGGER.error(t.getMessage(), t);
            throw new ApplicationRuntimeException("Unable to create a voucher.", t);
        }
    }

    /**
     * Creates Account Details map
     * 
     * @param glcode
     *            GLCode for the account head.
     * @param debitAmount
     *            Debit amount for the account head
     * @param creditAmount
     *            Credit amount for the account head
     * @return Map Map contains account details.
     */
    private HashMap<String, Object> createAccDetailmap(String glcode, BigDecimal debitAmount, BigDecimal creditAmount) {
        HashMap<String, Object> accountdetailmap = new HashMap<String, Object>();
        accountdetailmap.put(VoucherConstant.GLCODE, glcode);
        accountdetailmap.put(VoucherConstant.DEBITAMOUNT, debitAmount);
        accountdetailmap.put(VoucherConstant.CREDITAMOUNT, creditAmount);
        accountdetailmap.put(VoucherConstant.FUNCTIONCODE, getFunctionaryCode());
        return accountdetailmap;
    }

    /**
     * Creates Voucher Header details
     * 
     * @param indexNumber
     *            Property id
     * @param transaction
     *            Voucher creation reason ( Property creation or modification
     *            etc)
     * @return Map Contains voucher header details
     */
    private HashMap<String, Object> createHeaderDetails(String indexNumber, String transaction) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
        String description = "PTIS / " + indexNumber + " / " + transaction + " / " + sdf.format(new Date());
        String sourceURL = URL_FOR_DCB + indexNumber;

        HashMap<String, Object> headerdetails = new HashMap<String, Object>();
        headerdetails.put(VoucherConstant.VOUCHERNAME, VOUCHERNAME);
        headerdetails.put(VoucherConstant.VOUCHERTYPE, VOUCHERTYPE);
        headerdetails.put(VoucherConstant.DESCRIPTION, description);
        headerdetails.put(VoucherConstant.VOUCHERNUMBER, VOUCHERNUMBER);
        headerdetails.put(VoucherConstant.VOUCHERDATE, new Date());
        headerdetails.put(VoucherConstant.STATUS, 0);
        headerdetails.put(VoucherConstant.MODULEID, PTIS_EG_MODULES_ID);
        headerdetails.put(VoucherConstant.DEPARTMENTCODE, DEPT_CODE_TAX);
        headerdetails.put(VoucherConstant.FUNDCODE, getFundCode());
        headerdetails.put(VoucherConstant.SOURCEPATH, sourceURL);
        return headerdetails;
    }

    /**
     * Creates a map of map contains GLCODE and the aggregate amount for GLCODE
     * 
     * @param amounts
     *            Map of Installment and reason wise demand map demand map --
     *            (key-demand reason, val-respective demand)
     * @return Map Map of map contains demand reason and the aggregate amount
     *         for demand reason
     * @throws IOException
     */
    private Map<String, Map<String, BigDecimal>> prepareDemandForGlcode(
            Map<Installment, Map<String, BigDecimal>> amounts) {

        Module module = moduleDao.getModuleByName(PTMODULENAME);
        Installment currentInstall = installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());

        Map<String, Map<String, BigDecimal>> demandForGlcode = new HashMap<String, Map<String, BigDecimal>>();
        Map<String, BigDecimal> arrearsDemand = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> currentDemand = new HashMap<String, BigDecimal>();
        BigDecimal taxAmount = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.ZERO;
        String demandReason = "";
        for (Entry<Installment, Map<String, BigDecimal>> amountsRecord : amounts.entrySet()) {

            String instDesc = amountsRecord.getKey().getDescription();
            Map<String, BigDecimal> demandReasonMap = amountsRecord.getValue();

            if (!instDesc.equalsIgnoreCase(currentInstall.toString())) {
                for (Map.Entry<String, BigDecimal> demandReasonRecord : demandReasonMap.entrySet()) {

                    demandReason = demandReasonRecord.getKey();
                    amount = demandReasonRecord.getValue();
                    taxAmount = BigDecimal.ZERO;

                    if (arrearsDemand.get(demandReason) == null) {
                        arrearsDemand.put(demandReason, amount);
                    } else {
                        taxAmount = arrearsDemand.get(demandReason);
                        arrearsDemand.put(demandReason, taxAmount.add(amount));
                    }
                }
            } else {
                for (Map.Entry<String, BigDecimal> demandReasonRecord : demandReasonMap.entrySet()) {
                    demandReason = demandReasonRecord.getKey();
                    amount = demandReasonRecord.getValue();
                    taxAmount = BigDecimal.ZERO;

                    if (currentDemand.get(demandReason) == null) {
                        currentDemand.put(demandReason, amount);
                    } else {
                        taxAmount = currentDemand.get(demandReason);
                        currentDemand.put(demandReason, taxAmount.add(amount));
                    }
                }
            }
        }

        demandForGlcode.put(ARREARS_DEMAND, arrearsDemand);
        demandForGlcode.put(CURRENT_DEMAND, currentDemand);
        return demandForGlcode;
    }

    /**
     * Gets the Functionary Code for Collections from AppConfigValues
     */
    public BigDecimal getFunctionaryCode() {

        return new BigDecimal(propertyTaxCommonUtil.getAppConfigValue(DEFAULT_FUNCTIONARY_CODE,
                PropertyTaxConstants.PTMODULENAME));
    }

    /**
     * Gets the Fund Code for Collections from AppConfigValues
     */
    public String getFundCode() {

        return new String(propertyTaxCommonUtil.getAppConfigValue(DEFAULT_FUND_CODE, PropertyTaxConstants.PTMODULENAME));
    }

    /**
     * Gets the Fund Source Code for Collections from AppConfigValues
     */
    public String getFundSourceCode() {

        return new String(propertyTaxCommonUtil.getAppConfigValue(DEFAULT_FUND_SRC_CODE,
                PropertyTaxConstants.PTMODULENAME));
    }

    /**
     * Gets the Department Code for Collections from AppConfigValues
     */
    public String getDepartmentCode() {

        return new String(propertyTaxCommonUtil.getAppConfigValue(DEPT_CODE_TAX, PropertyTaxConstants.PTMODULENAME));
    }

    /**
     * Gets the Function Code for Collections from AppConfigValues
     */
    public String getFunctionCode() {

        return new String(propertyTaxCommonUtil.getAppConfigValue(FUNCTION_CODE, PropertyTaxConstants.PTMODULENAME));
    }

}
