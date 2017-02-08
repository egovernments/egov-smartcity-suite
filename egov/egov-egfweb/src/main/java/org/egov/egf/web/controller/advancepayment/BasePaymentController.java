/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.egf.web.controller.advancepayment;

import java.util.LinkedHashMap;
import java.util.Map;

import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.service.BankBranchService;
import org.egov.egf.advancepayment.service.AdvancePaymentService;
import org.egov.egf.web.controller.voucher.BaseVoucherController;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.services.payment.MiscbilldetailService;
import org.egov.services.payment.PaymentService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

public class BasePaymentController extends BaseVoucherController {

    @Autowired
    private BankBranchService bankBranchService;
    @Autowired
    protected MiscbilldetailService miscbilldetailService;
    @Autowired
    protected PaymentService paymentService;

    @Autowired
    protected AdvancePaymentService advancePaymentService;

    public BasePaymentController(final AppConfigValueService appConfigValuesService) {
        super(appConfigValuesService);
    }

    public void prepare(final Model model) {
        super.setDropDownValues(model);
    }

    private String getTypeOfAccount() {
        return FinancialConstants.TYPEOFACCOUNT_PAYMENTS + ","
                + FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS;
    }

    protected void loadbankBranch(final Fund fund, final Model model) {
        final String typeOfAccount = getTypeOfAccount();
        final String[] strArray = typeOfAccount.split(",");
        model.addAttribute("bankbranchList", bankBranchService.getBankBranchByFund(fund, strArray));

    }

    public Map<String, String> getModeOfPayments() {
        final Map<String, String> modeOfPaymentMap = new LinkedHashMap<>();
        modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_CHEQUE, FinancialConstants.MODEOFPAYMENT_CHEQUE.toUpperCase());
        modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_CASH, FinancialConstants.MODEOFPAYMENT_CASH.toUpperCase());
        modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_RTGS, FinancialConstants.MODEOFPAYMENT_RTGS.toUpperCase());
        return modeOfPaymentMap;
    }

    public CVoucherHeader setVoucherStatus(final CVoucherHeader voucherHeader, final String workFlowAction) {
        if (FinancialConstants.BUTTONCANCEL.toString().equalsIgnoreCase(workFlowAction))
            voucherHeader.setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
        else if (FinancialConstants.BUTTONAPPROVE.toString().equalsIgnoreCase(workFlowAction))
            voucherHeader.setStatus(FinancialConstants.CREATEDVOUCHERSTATUS);
        else
            voucherHeader.setStatus(FinancialConstants.PREAPPROVEDVOUCHERSTATUS);
        return voucherHeader;
    }

}
