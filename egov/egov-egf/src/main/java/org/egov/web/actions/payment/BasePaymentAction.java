/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/**
 * Action class to route to appropriate URL for drilldown from INBOX
 */
package org.egov.web.actions.payment;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.payment.Paymentheader;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Position;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.voucher.BaseVoucherAction;
import org.elasticsearch.common.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;

/**
 * @author mani
 */

@Results({
        @Result(name = "billpayment", type = "redirectAction", location = "payment-view", params = { "namespace", "/payment"
        }),
        @Result(name = "advancepayment", type = "redirectAction", location = "payment-advanceView", params = { "namespace",
                "/payment"
        }),
        @Result(name = "directbankpayment", type = "redirectAction", location = "directBankPayment-viewInboxItem", params = {
                "namespace", "/payment"
        }),
        @Result(name = "remitRecovery", type = "redirectAction", location = "remitRecovery-viewInboxItem", params = {
                "namespace", "/deduction"
        }),
        @Result(name = "contractoradvancepayment", type = "redirectAction", location = "advancePayment-viewInboxItem", params = {
                "namespace", "/payment"
        })
})
public class BasePaymentAction extends BaseVoucherAction {
    /**
     *
     */
    private static final long serialVersionUID = 8589393885303282831L;
    EisCommonService eisCommonService;
    private static Logger LOGGER = Logger.getLogger(BasePaymentAction.class);
    @Autowired
    private SimpleWorkflowService<Paymentheader> paymentHeaderWorkflowService;
    @Autowired
    private VoucherTypeForULB voucherTypeForULB;

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public BasePaymentAction()
    {
        super();
    }

    protected String action = "";
    protected String paymentid = "";
    private final String BILLPAYMENT = "billpayment";
    private final String DIRECTBANKPAYMENT = "directbankpayment";
    private final String REMITTANCEPAYMENT = "remitRecovery";
    public static final String ARF_TYPE = "Contractor";

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
        if (paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_ADVANCE)) {
            final EgAdvanceRequisition arf = (EgAdvanceRequisition) persistenceService.find(
                    "from EgAdvanceRequisition where arftype = ? and egAdvanceReqMises.voucherheader = ?", ARF_TYPE,
                    paymentheader.getVoucherheader());
            if (arf != null)
                result = "contractoradvancepayment";
            else
                result = "advancepayment";
        }
        else if (paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_BILL) ||
                FinancialConstants.PAYMENTVOUCHER_NAME_SALARY.equalsIgnoreCase(paymentheader.getVoucherheader().getName()) ||
                FinancialConstants.PAYMENTVOUCHER_NAME_PENSION.equalsIgnoreCase(paymentheader.getVoucherheader().getName()))
            result = BILLPAYMENT;
        else if (paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK))
            result = DIRECTBANKPAYMENT;
        else if (paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE))
            result = REMITTANCEPAYMENT;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Completed viewInboxItems..... ");
        return result;
    }

    // used only in create
    public boolean shouldshowVoucherNumber()
    {
        String vNumGenMode = "Manual";
        vNumGenMode = voucherTypeForULB.readVoucherTypes(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        if (!"Auto".equalsIgnoreCase(vNumGenMode)) {
            mandatoryFields.add("vouchernumber");
            return true;
        } else
            return false;
    }

    public void transitionWorkFlow(final Paymentheader paymentheader, WorkflowBean workflowBean) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;

        if (null != paymentheader.getId())
            wfInitiator = getWorkflowInitiator(paymentheader);

        if (FinancialConstants.BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            if (wfInitiator.equals(userAssignment)) {
                paymentheader.transition(true).end().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            } else {
                final String stateValue = FinancialConstants.WORKFLOW_STATE_REJECTED;
                paymentheader.transition(true).withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(FinancialConstants.WF_STATE_EOA_Approval_Pending);
            }

        } else if (FinancialConstants.BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            paymentheader.getVoucherheader().setStatus(FinancialConstants.CREATEDVOUCHERSTATUS);
            paymentheader.transition(true).end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate());
        } else if (FinancialConstants.BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            paymentheader.getVoucherheader().setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
            paymentheader.transition(true).end().withStateValue(FinancialConstants.WORKFLOW_STATE_CANCELLED)
                    .withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate());
        } else {
            if (null != workflowBean.getApproverPositionId() && workflowBean.getApproverPositionId() != -1)
                pos = (Position) persistenceService.find("from Position where id=?", workflowBean.getApproverPositionId());
            if (null == paymentheader.getState()) {
                final WorkFlowMatrix wfmatrix = paymentHeaderWorkflowService.getWfMatrix(paymentheader.getStateType(), null,
                        null, null, workflowBean.getCurrentState(), null);
                paymentheader.transition().start().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if (paymentheader.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                paymentheader.transition(true).end().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                final WorkFlowMatrix wfmatrix = paymentHeaderWorkflowService.getWfMatrix(paymentheader.getStateType(), null,
                        null, null, paymentheader.getCurrentState().getValue(), null);
                paymentheader.transition(true).withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }
        }
    }

    protected Assignment getWorkflowInitiator(final Paymentheader paymentheader) {
        Assignment wfInitiator = assignmentService.getPrimaryAssignmentForUser(paymentheader.getCreatedBy().getId());
        return wfInitiator;
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

    public void setPaymentHeaderWorkflowService(SimpleWorkflowService<Paymentheader> paymentHeaderWorkflowService) {
        this.paymentHeaderWorkflowService = paymentHeaderWorkflowService;
    }
}
