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
/**
 * Action class to route to appropriate URL for drilldown from INBOX
 */
package org.egov.egf.web.actions.payment;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.egf.web.actions.voucher.BaseVoucherAction;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.service.AppConfigService;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.payment.Paymentheader;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * @author mani
 */
@ParentPackage("egov")
@Validations
@Results({
        @Result(name = "billpayment", type = "redirectAction", location = "payment-view", params = { "namespace",
                "/payment", "paymentid", "${paymentid}" }),
        @Result(name = "advancepayment", type = "redirectAction", location = "payment-advanceView", params = {
                "namespace", "/payment", "paymentid", "${paymentid}" }),
        @Result(name = "directbankpayment", type = "redirectAction", location = "directBankPayment-viewInboxItem", params = {
                "namespace", "/payment", "paymentid", "${paymentid}" }),
        @Result(name = "remitRecovery", type = "redirectAction", location = "remitRecovery-viewInboxItem", params = {
                "namespace", "/deduction", "paymentid", "${paymentid}" }),
        @Result(name = "contractoradvancepayment", type = "redirectAction", location = "advancePayment-viewInboxItem", params = {
                "namespace", "/payment", "paymentid", "${paymentid}" }) })
public class BasePaymentAction extends BaseVoucherAction {
    private static final long serialVersionUID = 8589393885303282831L;
    EisCommonService eisCommonService;
    private static Logger LOGGER = Logger.getLogger(BasePaymentAction.class);
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<Paymentheader> paymentHeaderWorkflowService;
    @Autowired
    private VoucherTypeForULB voucherTypeForULB;
    @Autowired
    AppConfigService appConfigService;

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public BasePaymentAction() {
        super();
    }

    @Override
    public void prepare() {

        super.prepare();
        bankBalanceValidation();
    }

    protected String action = "";
    protected String paymentid = "";
    private final String BILLPAYMENT = "billpayment";
    private final String DIRECTBANKPAYMENT = "directbankpayment";
    private final String REMITTANCEPAYMENT = "remitRecovery";
    public static final String ARF_TYPE = "Contractor";
    private String bankBalanceCheck = "";

    protected static final String ACTIONNAME = "actionname";
    protected boolean canCheckBalance = false;

    public boolean isCanCheckBalance() {
        return canCheckBalance;
    }

    public void setCanCheckBalance(final boolean canCheckBalance) {
        this.canCheckBalance = canCheckBalance;
    }

    @Autowired
    protected AppConfigValueService appConfigValuesService;

    protected String showMode;

    @SkipValidation
    @Action(value = "/payment/basePayment-viewInboxItems")
    public String viewInboxItems() {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting viewInboxItems..... ");
        String result = null;
        final Paymentheader paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where id=?",
                Long.valueOf(paymentid));
        if (!validateOwner(paymentheader.getState()))
            return INVALIDPAGE;
        getSession().put("paymentid", paymentid);
        if (paymentheader.getVoucherheader().getName()
                .equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_ADVANCE)) {
            final EgAdvanceRequisition arf = (EgAdvanceRequisition) persistenceService.find(
                    "from EgAdvanceRequisition where arftype = ? and egAdvanceReqMises.voucherheader = ?", ARF_TYPE,
                    paymentheader.getVoucherheader());
            if (arf != null)
                result = "contractoradvancepayment";
            else
                result = "advancepayment";
        } else if (paymentheader.getVoucherheader().getName()
                .equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_BILL)
                || FinancialConstants.PAYMENTVOUCHER_NAME_SALARY
                        .equalsIgnoreCase(paymentheader.getVoucherheader().getName())
                || FinancialConstants.PAYMENTVOUCHER_NAME_PENSION
                        .equalsIgnoreCase(paymentheader.getVoucherheader().getName()))
            result = BILLPAYMENT;
        else if (paymentheader.getVoucherheader().getName()
                .equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK))
            result = DIRECTBANKPAYMENT;
        else if (paymentheader.getVoucherheader().getName()
                .equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE))
            result = REMITTANCEPAYMENT;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed viewInboxItems..... ");
        return result;
    }

    // used only in create
    public boolean shouldshowVoucherNumber() {
        String vNumGenMode = "Manual";
        vNumGenMode = voucherTypeForULB.readVoucherTypes(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        if (!"Auto".equalsIgnoreCase(vNumGenMode)) {
            mandatoryFields.add("vouchernumber");
            return true;
        } else
            return false;
    }
    /*
     * This api is to check bank balance and allow to create bill based on appconfig values like mandatory, warning, none.
     */

    public void bankBalanceValidation() {
        final AppConfig appConfig = appConfigService.getAppConfigByModuleNameAndKeyName(
                FinancialConstants.MODULE_NAME_APPCONFIG, FinancialConstants.BALANCE_CHECK_CONTROL_TYPE);
        if (appConfig != null && !appConfig.getConfValues().isEmpty()) {
            final String appValue = appConfig.getConfValues().get(0).getValue();
            if (FinancialConstants.MANDATORY.equalsIgnoreCase(appValue))
                bankBalanceCheck = appValue.toLowerCase();
            else if (FinancialConstants.WARNING.equalsIgnoreCase(appValue))
                bankBalanceCheck = appValue.toLowerCase();
            else if (NONE.equalsIgnoreCase(appValue))
                bankBalanceCheck = appValue.toLowerCase();
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(final String paymentid) {
        this.paymentid = paymentid;
    }

    public String getShowMode() {
        return showMode;
    }

    public void setShowMode(final String showMode) {
        this.showMode = showMode;
    }

    public String getFinConstExpendTypeContingency() {
        return FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT;
    }

    public String getFinConstExpendTypePension() {
        return FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION;
    }

    public SimpleWorkflowService<Paymentheader> getPaymentHeaderWorkflowService() {
        return paymentHeaderWorkflowService;
    }

    public void setPaymentHeaderWorkflowService(final SimpleWorkflowService<Paymentheader> paymentHeaderWorkflowService) {
        this.paymentHeaderWorkflowService = paymentHeaderWorkflowService;
    }

    public String getBankBalanceCheck() {
        return bankBalanceCheck;
    }

    public void setBankBalanceCheck(final String bankBalanceCheck) {
        this.bankBalanceCheck = bankBalanceCheck;
    }
}
