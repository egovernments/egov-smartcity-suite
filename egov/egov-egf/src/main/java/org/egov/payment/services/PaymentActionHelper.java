package org.egov.payment.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFunction;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.payment.Paymentheader;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Position;
import org.egov.services.payment.MiscbilldetailService;
import org.egov.utils.FinancialConstants;
import org.elasticsearch.common.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
@Service
public class PaymentActionHelper {

	@Autowired
	@Qualifier("miscbilldetailService")
	private MiscbilldetailService miscbilldetailService;

	private static final Logger LOGGER = Logger.getLogger(PaymentActionHelper.class);
	
	@Autowired
	private SecurityUtils securityUtils;
	
	@Autowired
	@Qualifier("persistenceService")
	private PersistenceService persistenceService;
	
	@Autowired
    protected AssignmentService assignmentService;
	
	@Autowired
    private SimpleWorkflowService<Paymentheader> paymentHeaderWorkflowService;
	
	@Transactional
	public EgBillregister setbillRegisterFunction(EgBillregister bill,CFunction function)
	{
		bill.getEgBillregistermis().setFunction(function);
		return bill;
	}
	
	
	@Transactional
	public List<Miscbilldetail> getPaymentBills(Paymentheader paymentheader)
	{
		List<Miscbilldetail>    miscBillList=null;
		 if(LOGGER.isDebugEnabled()) LOGGER.debug("Inside getPaymentBills");
		try {
			  miscBillList = miscbilldetailService.findAllBy(
					" from Miscbilldetail where payVoucherHeader.id = ? order by paidto",
					paymentheader.getVoucherheader().getId());
			
			
		} catch (final Exception e) {
			throw new ValidationException("", "Total Paid Amount Exceeding Net Amount For This Bill");
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Retrived bill details fro the paymentheader");
		
		return miscBillList;
	}
	protected Assignment getWorkflowInitiator(final Paymentheader paymentheader) {
        Assignment wfInitiator = assignmentService.getPrimaryAssignmentForUser(paymentheader.getCreatedBy().getId());
        return wfInitiator;
    }
	
	@Transactional
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
	@Transactional  
	  public List<EgAdvanceRequisition> getAdvanceRequisitionDetails(Paymentheader paymentheader) {
	        if (LOGGER.isDebugEnabled())
	            LOGGER.debug("Inside getAdvanceRequisitionDetails");
	       return persistenceService.findAllBy("from EgAdvanceRequisition where egAdvanceReqMises.voucherheader.id=?",paymentheader.getVoucherheader().getId());
	    }
	
}
