package org.egov.erpcollection.web.actions.receipts;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.services.ReceiptHeaderService;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.security.terminal.model.Location;
import org.egov.web.actions.BaseFormAction;

/**
 * Action class for "Approve Collections"
 */
@ParentPackage("egov")
@Results( {
		@Result(name = "submissionReportCash", type = ServletActionRedirectResult.class, value = "cashCollectionReport", params = {
				"actionName", "cashCollectionReport.action",
				"method", "submissionReport.action",
				"namespace", "/reports" }),
		@Result(name = "submissionReportCheque", type = ServletActionRedirectResult.class, value = "chequeCollectionReport", params = {
				"actionName", "chequeCollectionReport.action",
				"method", "submissionReport.action",
				"namespace", "/reports" }) })
public class CollectionsWorkflowAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;

	/**
	 * List of receipt headers to be submitted/approved
	 */
	private List<ReceiptHeader> receiptHeaders;

	/**
	 * Array of selected receipt ids that are to be submitted/approved
	 */
	private Long[] receiptIds;

	/**
	 * Map of instrument type wise amounts for all receipts that are eligible
	 * for the workflow
	 */
	private final Map<String, BigDecimal> instrumentWiseAmounts = new HashMap<String, BigDecimal>(
			4);

	/**
	 * Total amount of all receipts eligible for the workflow action
	 */
	private BigDecimal totalAmount;

	/**
	 * The counter id for which the receipt list is to be submitted/approved.
	 */
	private Integer counterId = Integer.valueOf(-1);

	/**
	 * The user name for which the receipt list is to be submitted/approved.
	 */
	private String userName;

	/**
	 * The service code for which the receipt list is to be submitted/approved.
	 */
	private String serviceCode;

	/**
	 * Workflow service for changing the state of the receipt
	 */
	private WorkflowService<ReceiptHeader> workflowService;

	/**
	 * The collections utility object
	 */
	private CollectionsUtil collectionsUtil;

	/**
	 * Receipt header service
	 */
	private ReceiptHeaderService receiptHeaderService;

	/**
	 * Approval/Rejection remarks
	 */
	private String remarks;

	/**
	 * Workflow action (SUBMIT/APPROVE). Based on this, the JSP can decide to
	 * display/hide various buttons
	 */
	private String wfAction;

	public void setWfAction(String wfAction) {
		this.wfAction = wfAction;
	}

	/**
	 * Result for cash submission report (redirects to the cash collection
	 * report)
	 */
	private static final String SUBMISSION_REPORT_CASH = "submissionReportCash";

	/**
	 * Result for cheque submission report (redirects to the cheque collection
	 * report)
	 */
	private static final String SUBMISSION_REPORT_CHEQUE = "submissionReportCheque";

	/**
	 * This method is called when user clicks on a collections work flow item in
	 * the inbox. The inbox item details contains the next work flow action to
	 * be performed, service code, user id and counter id in the following form:
	 * <next-workflow-action>-servicecode-username-counterid
	 * 
	 * @param inboxItemDetails
	 *            the id to set
	 */
	public void setInboxItemDetails(String inboxItemDetails) {
		String params[] = inboxItemDetails.split(
				CollectionConstants.SEPARATOR_HYPHEN, -1);
		if (params.length == 4) {
			/*wfAction = params[0];
			serviceCode = params[1];
			userName = params[2];
			counterId = Integer.valueOf(params[3]);*/
			setWfAction(params[0]);
			setServiceCode( params[1]);
			setUserName(params[2]);
			setCounterId(Integer.valueOf(params[3]));
		}
	}

	/**
	 * @param collectionsUtil
	 *            the collectionsUtil to set
	 */
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}

	/**
	 * @param workflow
	 *            the receipt workflow service
	 */
	public void setReceiptWorkflowService(
			WorkflowService<ReceiptHeader> workflow) {
		this.workflowService = workflow;
	}

	/**
	 * @param workflow
	 *            the receipt workflow service
	 */
	public void setReceiptHeaderService(
			ReceiptHeaderService receiptHeaderService) {
		this.receiptHeaderService = receiptHeaderService;
	}

	/**
	 * @return true if the current action is submit_for_approval
	 */
	public Boolean getIsSubmitAction() {
		return wfAction.equals(CollectionConstants.WF_ACTION_SUBMIT);
	}

	/**
	 * @return true if partial selection is to be allowed for
	 *         submission/approval, else false
	 */
	public Boolean getAllowPartialSelection() {
		//return wfAction.equals(CollectionConstants.WF_ACTION_SUBMIT);
		if (getIsSubmitAction()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @return true if the current action is approve
	 */
	public Boolean getIsApproveAction() {
		return wfAction.equals(CollectionConstants.WF_ACTION_APPROVE);
	}

	/**
	 * @return true if the current action is approve
	 */
	public Boolean getIsRejectAction() {
		return wfAction.equals(CollectionConstants.WF_ACTION_REJECT);
	}

	/**
	 * Constructor
	 */
	public CollectionsWorkflowAction() {
		super();
	}

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @return the counter id
	 */
	public Integer getCounterId() {
		return counterId;
	}

	/**
	 * @param counterId
	 *            the counter id to be set
	 */
	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	/**
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the user name to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the service code
	 */
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param serviceCode
	 *            the Service Code to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 * @return List for receipt headers in status "To be submitted"
	 */
	public List<ReceiptHeader> getReceiptHeaders() {
		return receiptHeaders;
	}

	/**
	 * @return The total amount of all receipts eligible for the workflow action
	 */
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	/**
	 * Returns amount for given instrument type
	 * 
	 * @param instrumentType
	 *            The instrument type
	 * @return amount for given instrument type
	 */
	private BigDecimal getInstrumentTypeAmount(String instrumentType) {
		BigDecimal amount = instrumentWiseAmounts.get(instrumentType);

		return amount == null ? BigDecimal.valueOf(0) : amount;
	}

	/**
	 * @return Total amount collected as cash amongst all receipts eligible for
	 *         the workflow action
	 */
	public BigDecimal getCashAmount() {
		return getInstrumentTypeAmount(CollectionConstants.INSTRUMENTTYPE_CASH);
	}

	/**
	 * @return Total amount collected as cheque amongst all receipts eligible
	 *         for the workflow action
	 */
	public BigDecimal getChequeAmount() {
		return getInstrumentTypeAmount(CollectionConstants.INSTRUMENTTYPE_CHEQUE);
	}

	/**
	 * @return Total amount collected as dd amongst all receipts eligible for
	 *         the workflow action
	 */
	public BigDecimal getDdAmount() {
		return getInstrumentTypeAmount(CollectionConstants.INSTRUMENTTYPE_DD);
	}

	/**
	 * @return Total amount collected using card amongst all receipts eligible
	 *         for the workflow action
	 */
	public BigDecimal getCardAmount() {
		return getInstrumentTypeAmount(CollectionConstants.INSTRUMENTTYPE_CARD);
	}
	
	/**
	 * @return Total amount collected using bank amongst all receipts eligible
	 *         for the workflow action
	 */
	public BigDecimal getBankAmount() {
		return getInstrumentTypeAmount(CollectionConstants.INSTRUMENTTYPE_BANK);
	}

	/**
	 * @param Array
	 *            of receipt Ids
	 */
	public void setReceiptIds(Long[] receiptIds) {
		this.receiptIds = receiptIds;
	}

	/**
	 * @param Submission
	 *            /Approval/Rejection remarks
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * Updates the receipt's status by invoking the workflow action
	 * 
	 * @param wfAction
	 *            Workflow action e.g. submit_for_approval/approve/reject
	 * @param remarks
	 *            Approval/rejection remarks
	 * @return SUCCESS/ERROR
	 */
	private String updateReceiptWorkflowStatus(String wfAction, String remarks) {
		for (Long receiptId : receiptIds) {
			// Get the next receipt that is to be updated
			ReceiptHeader receiptHeader = receiptHeaderService.findById(receiptId, false);

			// Transition the workflow state with action "submit_for_approval"
			ReceiptHeader changedReceipt = workflowService.transition(wfAction,
					receiptHeader, remarks);
			if (changedReceipt.getStatus().getCode().equals(
					CollectionConstants.RECEIPT_STATUS_CODE_APPROVED)) {
				// Receipt approved. end workflow for this receipt.
				workflowService.end(changedReceipt, collectionsUtil
						.getPositionOfUser(receiptHeader.getCreatedBy()),
						"Receipt Approved - Workflow ends");
			}
			collectionsUtil.auditEventForReceiptEntity(changedReceipt, changedReceipt.getState().getValue());
		}

		// Add the selected receipt ids to session. This is used later by the
		// cash/cheque submission reports
		// Need to find a better mechanism to achieve this.
		getSession().put(CollectionConstants.SESSION_VAR_RECEIPT_IDS,
				receiptIds);
		return SUCCESS;
	}

	/**
	 * Fetches all receipts for set user-counter combination and given status
	 * code. Also sets the work flow action code to given value, and calculates
	 * the various amounts using the fetched receipts.
	 * 
	 * @param statusCode
	 *            Status code for which receipts are to be fetched
	 * @param workflowAction
	 *            Work flow action code
	 */
	private void fetchReceipts(String statusCode, String workflowAction) {
		// Get all receipts that are created by currently logged in user from
		// his/her current counter and are in TO_BE_SUBMITTED status
		receiptHeaders = receiptHeaderService
				.findAllByStatusUserCounterService(statusCode, userName,
						counterId, serviceCode);
		
		// Populate the selected receipt IDs with all receipt ids
		int receiptCount = receiptHeaders.size();
		receiptIds = new Long[receiptCount];
		for(int i = 0; i < receiptCount; i++) {
			receiptIds[i] = receiptHeaders.get(i).getId();
		}
		
		wfAction = workflowAction;
		calculateAmounts();
	}

	/**
	 * Retrieves all receipt headers created by currently logged in user from
	 * current counter and in status "To be submitted"
	 * 
	 * @return Next page to be displayed (index)
	 */
	public String listSubmit() {
		userName = collectionsUtil.getLoggedInUserName(getSession());
		Location counter = collectionsUtil.getLocationOfUser(getSession());
		if (counter != null) {
			counterId = counter.getId();
		}

		// In SUBMIT mode fetch receipts for ALL billing services
		serviceCode = CollectionConstants.ALL;

		// Get all receipt headers to be submitted
		fetchReceipts(CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED,
				CollectionConstants.WF_ACTION_SUBMIT);
		return INDEX;
	}

	/**
	 * Retrieves all receipt headers created by set user from set counter and in
	 * status "submitted". Also populates the dropdown data for counter list and
	 * user list.
	 * 
	 * @return Next page to be displayed (index)
	 */
	public String listApprove() {
		if (counterId == null) {
			// By default show receipts from all counters
			counterId = -1;
		}
		if (userName == null) {
			// By default show receipts created by all users
			userName = CollectionConstants.ALL;
		}
		if (serviceCode == null) {
			// By default show receipts for all billing services
			serviceCode = CollectionConstants.ALL;
		}

		// Get all receipt headers to be approved
		fetchReceipts(CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED,
				CollectionConstants.WF_ACTION_APPROVE);

		// Add counter list and user list to drop down data
		addDropdownData(CollectionConstants.DROPDOWN_DATA_SERVICE_LIST,
				collectionsUtil.getCollectionServiceList());
		addDropdownData(CollectionConstants.DROPDOWN_DATA_COUNTER_LIST,
				collectionsUtil.getActiveCounters());
		addDropdownData(CollectionConstants.DROPDOWN_DATA_RECEIPT_CREATOR_LIST,
				collectionsUtil.getReceiptCreators());

		return INDEX;
	}

	/**
	 * Action that will be called from the workflow inbox. The inbox also passes
	 * the id of the clicked item which is of the form:
	 * <next-workflow-action>-servicecode-userid-counterid
	 * 
	 * @return Next page to be displayed (index)
	 */
	public String listWorkflow() {
		if (wfAction.equals(CollectionConstants.WF_ACTION_APPROVE)) {
			return listApprove();
		} else {
			return listSubmit();
		}
	}

	/**
	 * Action for creating cash submission report
	 * 
	 * @return submissionReport
	 */
	public String submissionReportCash() {
		return SUBMISSION_REPORT_CASH;
	}

	/**
	 * Action for creating cash submission report
	 * 
	 * @return submissionReport
	 */
	public String submissionReportCheque() {
		return SUBMISSION_REPORT_CHEQUE;
	}

	/**
	 * Action method to submit the selected receipt headers
	 * 
	 * @return SUCCESS/ERROR
	 */
	public String submitCollections() {
		wfAction = CollectionConstants.WF_ACTION_SUBMIT;
		return updateReceiptWorkflowStatus(wfAction, remarks);
	}

	/**
	 * Action method to approve the selected receipt headers
	 * 
	 * @return SUCCESS/ERROR
	 */
	public String approveCollections() {
		wfAction = CollectionConstants.WF_ACTION_APPROVE;
		return updateReceiptWorkflowStatus(wfAction, remarks);
	}

	/**
	 * Action method to reject the selected receipt headers
	 * 
	 * @return SUCCESS/ERROR
	 */
	public String rejectCollections() {
		wfAction = CollectionConstants.WF_ACTION_REJECT;
		return updateReceiptWorkflowStatus(wfAction, remarks);
	}

	/**
	 * Calculates instrument type wise amounts of all receipts eligible for the
	 * workflow
	 */
	private void calculateAmounts() {
		totalAmount = BigDecimal.valueOf(0);
		for (ReceiptHeader receiptHeader : receiptHeaders) {
			BigDecimal receiptAmount = receiptHeader.getAmount();

			// Increment total amount
			totalAmount = totalAmount.add(receiptAmount);

			// Increment instrument type wise amount
			String instrumentType = receiptHeader.getInstrumentType();
			BigDecimal instrumentAmount = instrumentWiseAmounts
					.get(instrumentType);
			if (instrumentAmount == null) {
				instrumentAmount = receiptAmount;
			} else {
				instrumentAmount = instrumentAmount.add(receiptAmount);
			}
			instrumentWiseAmounts.put(instrumentType, instrumentAmount);
		}
	}
}
