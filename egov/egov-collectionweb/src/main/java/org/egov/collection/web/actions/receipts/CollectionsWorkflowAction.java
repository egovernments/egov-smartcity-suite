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
package org.egov.collection.web.actions.receipts;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.codehaus.jackson.map.ObjectMapper;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ApproverRemitterMapping;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.service.ApproverRemitterMapService;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.service.spec.ReceiptApproverSpec;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Action class for "Approve Collections"
 */
@ParentPackage("egov")
@Results({
        @Result(name = CollectionsWorkflowAction.SUCCESS, location = "collectionsWorkflow-success.jsp"),
        @Result(name = CollectionsWorkflowAction.INDEX, location = "collectionsWorkflow-index.jsp"),
        @Result(name = CollectionsWorkflowAction.ERROR, location = "collectionsWorkflow-error.jsp"),
        @Result(name = CollectionsWorkflowAction.SUBMISSION_REPORT_CASH, type = "redirectAction", location = "cashCollectionReport-submissionReport.action", params = {
                "namespace", "/reports", "receiptDate", "${receiptDate}" }),
        @Result(name = CollectionsWorkflowAction.SUBMISSION_REPORT_CHEQUE, type = "redirectAction", location = "chequeCollectionReport-submissionReport.action", params = {
                "namespace", "/reports", "receiptDate", "${receiptDate}" }),
        @Result(name = "cancel", type = "redirectAction", location = "receipt", params = { "namespace", "/receipts",
                "method", "cancel" }) })
public class CollectionsWorkflowAction extends BaseFormAction {

    /**
     * Result for cash submission report (redirects to the cash collection report)
     */
    protected static final String SUBMISSION_REPORT_CASH = "submissionReportCash";
    /**
     * Result for cheque submission report (redirects to the cheque collection report)
     */
    protected static final String SUBMISSION_REPORT_CHEQUE = "submissionReportCheque";
    private static final long serialVersionUID = 1L;
    /**
     * Map of instrument type wise amounts for all receipts that are eligible for the workflow
     */
    private final Map<String, BigDecimal> instrumentWiseAmounts = new HashMap<>(4);
    /**
     * Updates the receipt's status by invoking the workflow action
     *
     * @param wfAction Workflow action e.g. submit_for_approval/approve/reject
     * @param remarks Approval/rejection remarks
     * @return SUCCESS/ERROR
     */
    @Autowired
    UserService userService;

    @Autowired
    AssignmentService assignmentService;

    /**
     * List of receipt headers to be submitted/approved
     */
    private List<ReceiptHeader> receiptHeaders;
    /**
     * Array of selected receipt ids that are to be submitted/approved
     */
    private Long[] receiptIds;
    /**
     * Total amount of all receipts eligible for the workflow action
     */
    private BigDecimal totalAmount;

    /**
     * The counter id for which the receipt list is to be submitted/approved.
     */
    private Long counterId = -1l;
    /**
     * The user name for which the receipt list is to be submitted/approved.
     */
    private String userName;
    /**
     * The service code for which the receipt list is to be submitted/approved.
     */
    private String serviceCode;
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
     * Workflow action (SUBMIT/APPROVE). Based on this, the JSP can decide to display/hide various buttons
     */
    private String wfAction;

    /**
     * For capturing dropdown form-fields
     */
    private Long approverDepartmentId;
    private Long approverDesignationId;
    private String approverIdPositionId;

    private List<Department> departmentList = Collections.emptyList();
    private List<Designation> designationList = Collections.emptyList();
    private List<Employee> employeeList = Collections.emptyList();
    private String receiptDate;
    private String currentUserDescription;
    private SortedMap<String, String> paymentModesMap = new TreeMap<>();
    private String paymentMode = CollectionConstants.ALL;
    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private ApproverRemitterMapService approverRemitterMapService;

    private String inboxItemDetails;

    /**
     * Constructor
     */
    public CollectionsWorkflowAction() {
        super();
    }

    public void setWfAction(final String wfAction) {
        this.wfAction = wfAction;
    }

    @SuppressWarnings("unused")
    public String getInboxItemDetails() {
        return inboxItemDetails;
    }

    /**
     * This method is called when user clicks on a collections work flow item in the inbox. The inbox item details contains the
     * next work flow action to be performed, service code, user id and counter id in the following form:
     * <next-workflow-action>-servicecode-username-counterid
     *
     * @param inboxItemDetails the id to set
     */
    public void setInboxItemDetails(final String inboxItemDetails) {
        if (StringUtils.isNotBlank(inboxItemDetails)) {
            final String params[] = inboxItemDetails.split(CollectionConstants.SEPARATOR_HYPHEN, -1);
            if (params.length <= 7) {
                setWfAction(params[0]);
                setServiceCode(params[1]);
                setUserName(params[2]);
                setCounterId(Long.valueOf(params[4]));
                setReceiptDate(params[3]);
            }
        }
        this.inboxItemDetails = inboxItemDetails;
    }

    /**
     * @param collectionsUtil the collectionsUtil to set
     */
    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    /**
     * @param receiptHeaderService Workflow the receipt workflow service
     */
    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    /**
     * @return true if the current action is submit_for_approval
     */
    public Boolean getIsSubmitAction() {
        return wfAction.equals(CollectionConstants.WF_ACTION_SUBMIT);
    }

    /**
     * @return true if partial selection is to be allowed for submission/approval, else false
     */
    @SuppressWarnings("unused")
    public Boolean getAllowPartialSelection() {
        // return wfAction.equals(CollectionConstants.WF_ACTION_SUBMIT);
        if (getIsSubmitAction())
            return false;
        else
            return true;
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

    @Override
    public Object getModel() {
        return null;
    }

    /**
     * @return the counter id
     */
    @SuppressWarnings("unused")
    public Long getCounterId() {
        return counterId;
    }

    /**
     * @param counterId the counter id to be set
     */
    public void setCounterId(final Long counterId) {
        this.counterId = counterId;
    }

    /**
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the user name to set
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * @return the service code
     */
    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * @param serviceCode the Service Code to set
     */
    public void setServiceCode(final String serviceCode) {
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
     * @param instrumentType The instrument type
     * @return amount for given instrument type
     */
    private BigDecimal getInstrumentTypeAmount(final String instrumentType) {
        final BigDecimal amount = instrumentWiseAmounts.get(instrumentType);

        return amount == null ? BigDecimal.valueOf(0) : amount;
    }

    /**
     * @return Total amount collected as cash amongst all receipts eligible for the workflow action
     */
    public BigDecimal getCashAmount() {
        return getInstrumentTypeAmount(CollectionConstants.INSTRUMENTTYPE_CASH);
    }

    /**
     * @return Total amount collected as cheque amongst all receipts eligible for the workflow action
     */
    public BigDecimal getChequeAmount() {
        return getInstrumentTypeAmount(CollectionConstants.INSTRUMENTTYPE_CHEQUE);
    }

    /**
     * @return Total amount collected as dd amongst all receipts eligible for the workflow action
     */
    public BigDecimal getDdAmount() {
        return getInstrumentTypeAmount(CollectionConstants.INSTRUMENTTYPE_DD);
    }

    /**
     * @return Total amount collected using card amongst all receipts eligible for the workflow action
     */
    public BigDecimal getCardAmount() {
        return getInstrumentTypeAmount(CollectionConstants.INSTRUMENTTYPE_CARD);
    }

    /**
     * @return Total amount collected using bank amongst all receipts eligible for the workflow action
     */
    public BigDecimal getBankAmount() {
        return getInstrumentTypeAmount(CollectionConstants.INSTRUMENTTYPE_BANK);
    }

    /**
     * @param receiptIds of receipt Ids
     */
    public void setReceiptIds(final Long[] receiptIds) {
        this.receiptIds = receiptIds;
    }

    /**
     * @param remarks /Approval/Rejection
     */
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public void setApproverDepartmentId(Long approverDepartmentId) {
        this.approverDepartmentId = approverDepartmentId;
    }

    @SuppressWarnings("unused")
    public void setApproverDesignationId(Long approverDesignationId) {
        this.approverDesignationId = approverDesignationId;
    }

    @SuppressWarnings("unused")
    public void setApproverIdPositionId(String approverIdPositionId) {
        this.approverIdPositionId = approverIdPositionId;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public List<Designation> getDesignationList() {
        return designationList;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    /**
     * Fetches all receipts for set user-counter combination and given status code. Also sets the work flow action code to given
     * value, and calculates the various amounts using the fetched receipts.
     *
     * @param workflowAction Work flow action code
     */
    private void fetchReceipts(final String workflowAction) {// Get
        // all
        // receipts
        // that
        // are created by
        // currently logged in user from
        // his/her current counter and are in SUBMITTED status
        final List<Position> positions = collectionsUtil.getPositionsForEmployee(securityUtils.getCurrentUser());
        List<Long> positionIds = new ArrayList<>();
        for (Position pos : positions)
            positionIds.add(pos.getId());
        receiptHeaders = receiptHeaderService.findAllByPositionAndInboxItemDetails(positionIds, inboxItemDetails);

        // Populate the selected receipt IDs with all receipt ids
        final int receiptCount = receiptHeaders.size();
        receiptIds = new Long[receiptCount];
        for (int i = 0; i < receiptCount; i++)
            receiptIds[i] = receiptHeaders.get(i).getId();

        wfAction = workflowAction;
        calculateAmounts();
    }

    /**
     * Action that will be called from the workflow inbox. The inbox also passes the id of the clicked item which is of the form:
     * <next-workflow-action>-servicecode-userid-counterid
     *
     * @return Next page to be displayed (index)
     */
    @Action(value = "/receipts/collectionsWorkflow-listWorkflow")
    public String listWorkflow() {
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CASH, CollectionConstants.INSTRUMENTTYPE_CASH);
        paymentModesMap.put(CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD, CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD);
        inboxItemDetails = inboxItemDetails.substring(0, inboxItemDetails.lastIndexOf(CollectionConstants.SEPARATOR_HYPHEN) + 1)
                + paymentMode;
        if (CollectionConstants.WF_ACTION_APPROVE.equals(wfAction)) {
            fetchReceipts(CollectionConstants.WF_ACTION_APPROVE);
        } else {
            fetchReceipts(CollectionConstants.WF_ACTION_SUBMIT);
            departmentList = getConfiguredDepartmentList();
            designationList = getConfiguredDesignationList();
        }
        return INDEX;
    }

    /**
     * Action for creating cash submission report
     *
     * @return submissionReport
     */
    @Action(value = "/receipts/collectionsWorkflow-submissionReportCash")
    public String submissionReportCash() {
        return SUBMISSION_REPORT_CASH;
    }

    /**
     * Action for creating cash submission report
     *
     * @return submissionReport
     */
    @Action(value = "/receipts/collectionsWorkflow-submissionReportCheque")
    public String submissionReportCheque() {
        return SUBMISSION_REPORT_CHEQUE;
    }

    /**
     * Action method to submit the selected receipt headers
     *
     * @return SUCCESS/ERROR
     */
    @Action(value = "/receipts/collectionsWorkflow-submitCollections")
    public String submitCollections() {
        List<ValidationError> validationErrorList = collectionsUtil.validateCollectionApprover(approverIdPositionId,
                approverDepartmentId, approverDesignationId);
        if (!validationErrorList.isEmpty())
            throw new ValidationException(validationErrorList);

        currentUserDescription = processReceipts(CollectionConstants.WF_ACTION_SUBMIT, remarks, receiptIds);
        // SUCCESS jsp shows msg based on wfAction
        wfAction = CollectionConstants.WF_ACTION_SUBMIT;
        return SUCCESS;
    }

    @Action(value = "/receipts/collectionsWorkflow-submitAllCollections")
    public String submitAllCollections() {
        List<ValidationError> validationErrorList = collectionsUtil.validateCollectionApprover(approverIdPositionId,
                approverDepartmentId, approverDesignationId);
        if (!validationErrorList.isEmpty())
            throw new ValidationException(validationErrorList);

        setInboxItemDetails(inboxItemDetails);
        currentUserDescription = processAllReceipts(CollectionConstants.WF_ACTION_SUBMIT, remarks, inboxItemDetails);
        // SUCCESS jsp shows msg based on wfAction
        // FIXME: use addActionMessage inplace of wfaction and if's
        wfAction = CollectionConstants.WF_ACTION_SUBMIT;
        return SUCCESS;
    }

    /**
     * Action method to approve the selected receipt headers
     *
     * @return SUCCESS/ERROR
     */
    @Action(value = "/receipts/collectionsWorkflow-approveCollections")
    public String approveCollections() {
        if (!validateApproverRemitterMap()) {
            // setting wfAction which is used by listWorkflow
            wfAction = CollectionConstants.WF_ACTION_APPROVE;
            return listWorkflow();
        }
        currentUserDescription = processReceipts(CollectionConstants.WF_ACTION_APPROVE, remarks, receiptIds);
        // SUCCESS jsp shows msg based on wfAction
        wfAction = CollectionConstants.WF_ACTION_APPROVE;
        return SUCCESS;
    }

    @Action(value = "/receipts/collectionsWorkflow-approveAllCollections")
    public String approveAllCollections() {
        // this set's wfAction which is used by listWorkflow
        setInboxItemDetails(inboxItemDetails);
        if (!validateApproverRemitterMap())
            return listWorkflow();
        currentUserDescription = processAllReceipts(CollectionConstants.WF_ACTION_APPROVE, remarks, inboxItemDetails);
        // SUCCESS jsp shows msg based on wfAction
        wfAction = CollectionConstants.WF_ACTION_APPROVE;
        return SUCCESS;
    }

    /**
     * Action method to reject the selected receipt headers
     *
     * @return SUCCESS/ERROR
     */
    @Action(value = "/receipts/collectionsWorkflow-rejectCollections")
    public String rejectCollections() {
        currentUserDescription = processReceipts(CollectionConstants.WF_ACTION_REJECT, remarks, receiptIds);
        // SUCCESS jsp shows msg based on wfAction
        wfAction = CollectionConstants.WF_ACTION_REJECT;
        return SUCCESS;
    }

    /**
     * Calculates instrument type wise amounts of all receipts eligible for the workflow
     */
    private void calculateAmounts() {
        totalAmount = BigDecimal.ZERO;
        for (final ReceiptHeader receiptHeader : receiptHeaders) {
            for (final InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument()) {
                final String instrumentType = instrumentHeader.getInstrumentType().getType();
                // Increment total amount
                totalAmount = totalAmount.add(instrumentHeader.getInstrumentAmount()).setScale(2,
                        BigDecimal.ROUND_HALF_UP);

                BigDecimal instrumentAmount = instrumentWiseAmounts.get(instrumentType);
                if (instrumentAmount == null)
                    instrumentAmount = instrumentHeader.getInstrumentAmount();
                else
                    instrumentAmount = instrumentAmount.add(instrumentHeader.getInstrumentAmount());
                instrumentWiseAmounts.put(instrumentType, instrumentAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            // Add to ReceiptHeader to populate in jsp
            receiptHeader.setInstrumentsAsString(receiptHeader.getInstrumentDetailAsString());
        }
    }

    @Action(value = "/receipts/workflowApproverAjax")
    public String workflowApproverAjax() throws IOException {

        String jsonString;
        ObjectMapper objectMapper = new ObjectMapper();

        List<Assignment> assignmentList = assignmentService.findAllAssignmentsByDeptDesigAndDates(approverDepartmentId,
                approverDesignationId, new Date());
        Map<String, String> approverIdPositionName = new HashMap<>();

        // FIXME: pass both position and employee
        for (Assignment assignment : assignmentList)
            if (assignment.getEmployee().isActive())
                approverIdPositionName.put(
                        String.format("%s~%s", assignment.getEmployee().getId(), assignment.getPosition().getId()),
                        assignment.getEmployee().getName().concat("/").concat(assignment.getPosition().getName()));
        jsonString = objectMapper.writeValueAsString(approverIdPositionName);

        ServletActionContext.getResponse().setContentType("application/json");
        ServletActionContext.getResponse().getWriter().write(jsonString);
        return null;
    }

    private boolean validateApproverRemitterMap() {
        User currentUser = collectionsUtil.getLoggedInUser();
        ApproverRemitterMapping remitterMapping = approverRemitterMapService.findByApproverIdAndIsActive(currentUser.getId(),
                true);
        if (remitterMapping == null) {
            addActionError(getText("approvecollections.validation.approverremitter.map", Arrays.asList(currentUser.getName())));
            return false;
        }
        return true;
    }

    /**
     * Returns configured department List for workflow approver
     */
    private List<Department> getConfiguredDepartmentList() {
        List<AppConfigValues> configuredDepartmentList = collectionsUtil.getAppConfigValues(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.COLLECTION_DEPARTMENTFORWORKFLOWAPPROVER);
        List<Department> departmentList = new ArrayList<>(configuredDepartmentList.size());
        for (AppConfigValues appConfigValues : configuredDepartmentList)
            departmentList.add(departmentService.getDepartmentByName(appConfigValues.getValue().toUpperCase()));

        return departmentList;
    }

    /**
     * Returns configured Designation List for workflow approver
     */
    private List<Designation> getConfiguredDesignationList() {
        List<AppConfigValues> configuredDesignationList = collectionsUtil.getAppConfigValues(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.COLLECTION_DESIGNATIONFORAPPROVER);
        List<String> designationNameList = new ArrayList<>(configuredDesignationList.size());
        for (AppConfigValues appConfigValues : configuredDesignationList)
            designationNameList.add(appConfigValues.getValue().toUpperCase());
        return designationService.getDesignationsByNames(designationNameList);
    }

    /**
     * Given array of receiptIds it <br>
     * fetches corresponding List of ReceiptHeader, <br>
     * calls performWorkflow, persists receiptIds into session and <br>
     * returns name~username~position of first receiptHeader's currentUser
     *
     * @param wfAction
     * @param remarks
     * @param receiptIds
     * @return name~username~position of first receiptHeader's currentUser
     */
    private String processReceipts(String wfAction, String remarks, Long[] receiptIds) {

        final List<ReceiptHeader> receiptHeaderList = receiptHeaderService.findReceiptListByIds(
                CollectionConstants.QUERY_RECEIPTS_BY_ID_LIST_AND_STATUSNOTCANCELLED, receiptIds);

        Long approverId = 0L;
        Long positionId = 0L;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(approverIdPositionId)) {
            String ids[] = approverIdPositionId.split("~");
            if (ids.length == 2) {
                approverId = Long.parseLong(ids[0]);
                positionId = Long.parseLong(ids[1]);
            }
        }

        ReceiptApproverSpec receiptApproverSpec = new ReceiptApproverSpec(
                positionId, approverId, approverDesignationId, approverDepartmentId, receiptHeaderList, remarks);
        getSession().put(CollectionConstants.SESSION_VAR_RECEIPT_IDS, receiptIds);

        return receiptHeaderService.performWorkflow(wfAction, receiptApproverSpec);
    }

    /**
     * For List of ReceiptHeader <br>
     * <b>given the inboxItemDetails it calls performWorkflow</b>, <br>
     * updates class property receiptIds, <br>
     * persists receiptIds into session and <br>
     * returns name~username~position of first receiptHeader's currentUser
     *
     * @param wfAction
     * @param remarks
     * @param inboxItemDetails
     * @return name~username~position of first receiptHeader's currentUser
     */
    private String processAllReceipts(String wfAction, String remarks, String inboxItemDetails) {

        List<Long> positionIds = new ArrayList<>();
        final List<Position> positions = collectionsUtil.getPositionsForEmployee(securityUtils.getCurrentUser());
        for (Position pos : positions)
            positionIds.add(pos.getId());
        final List<ReceiptHeader> receiptHeaderList = receiptHeaderService.findAllByPositionAndInboxItemDetails(positionIds,
                inboxItemDetails);
        receiptIds = new Long[receiptHeaderList.size()];
        int i = 0;
        for (final ReceiptHeader receiptHeader : receiptHeaderList) {
            if (receiptHeader != null)
                receiptIds[i] = receiptHeader.getId();
            i++;
        }

        Long approverId = 0L;
        Long positionId = 0L;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(approverIdPositionId)) {
            String ids[] = approverIdPositionId.split("~");
            if (ids.length == 2) {
                approverId = Long.parseLong(ids[0]);
                positionId = Long.parseLong(ids[1]);
            }
        }

        ReceiptApproverSpec receiptApproverSpec = new ReceiptApproverSpec(
                positionId, approverId, approverDesignationId, approverDepartmentId, receiptHeaderList, remarks);
        getSession().put(CollectionConstants.SESSION_VAR_RECEIPT_IDS, receiptIds);
        return receiptHeaderService.performWorkflow(wfAction, receiptApproverSpec);
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getCurrentUserDescription() {
        return currentUserDescription;
    }

    public void setCurrentUserDescription(final String currentUserDescription) {
        this.currentUserDescription = currentUserDescription;
    }

    public SortedMap<String, String> getPaymentModesMap() {
        return paymentModesMap;
    }

    public void setPaymentModesMap(final SortedMap<String, String> paymentModesMap) {
        this.paymentModesMap = paymentModesMap;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}