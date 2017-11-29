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
package org.egov.works.web.actions.contractoradvance;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.DrawingOfficer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.model.advance.EgAdvanceRequisitionDetails;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Result(name = ContractorAdvanceRequisitionAction.NEW, location = "contractorAdvanceRequisition-new.jsp")
public class ContractorAdvanceRequisitionAction extends BaseFormAction {

    private static final Logger LOGGER = Logger.getLogger(ContractorAdvanceRequisitionAction.class);
    private static final long serialVersionUID = 1L;
    private ContractorAdvanceRequisition contractorAdvanceRequisition = new ContractorAdvanceRequisition();
    private Long workOrderEstimateId;
    private Long id;
    private WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
    private BigDecimal advancePaid = BigDecimal.ZERO;
    private WorksService worksService;
    private ContractorAdvanceService contractorAdvanceService;
    private static final String ADVANCE_COA_LIST = "advanceAccountCodeList";
    private static final String ARF_TYPE = "Contractor";
    private Long advanceAccountCode;
    private static final String CANCEL_ACTION = "cancel";
    private static final Object REJECT_ACTION = "reject";
    private static final String SAVE_ACTION = "save";
    private static final String SOURCE_INBOX = "inbox";
    private static final String ACTION_NAME = "actionName";
    private Integer logedInUserDept = -1;
    private AbstractEstimateService abstractEstimateService;
    private WorkflowService<ContractorAdvanceRequisition> workflowService;
    @Autowired
    private UserService userService;
    private String sourcepage = "";
    private Integer workflowFunctionaryId;
    @Autowired
    private DepartmentService departmentService;
    private String messageKey;
    private String nextEmployeeName;
    private String nextDesignation;
    private String drawingOfficerName;

    public ContractorAdvanceRequisitionAction() {
        addRelatedEntity("drawingOfficer", DrawingOfficer.class);
    }

    @Override
    public StateAware getModel() {
        return contractorAdvanceRequisition;
    }

    @Override
    public void prepare() {

        if (workOrderEstimateId != null) {
            contractorAdvanceRequisition.setWorkOrderEstimate((WorkOrderEstimate) persistenceService.find(
                    "from WorkOrderEstimate where id = ?", workOrderEstimateId));

            if (id == null)
                // Approved ARF amount
                advancePaid = contractorAdvanceService.getAdvancePaidByWOEstimateId(workOrderEstimateId);
        }

        addDropdownData(ADVANCE_COA_LIST, contractorAdvanceService.getContractorAdvanceAccountcodes());

        if (id != null) {
            contractorAdvanceRequisition = contractorAdvanceService.getContractorAdvanceRequisitionById(id);
            for (final EgAdvanceRequisitionDetails advanceRequisitionDetails : contractorAdvanceRequisition
                    .getEgAdvanceReqDetailses())
                advanceAccountCode = advanceRequisitionDetails.getChartofaccounts().getId();
            workOrderEstimateId = contractorAdvanceRequisition.getWorkOrderEstimate().getId();
            if (StringUtils.isBlank(drawingOfficerName))
                drawingOfficerName = contractorAdvanceRequisition.getDrawingOfficer().getCode() + " - "
                        + contractorAdvanceRequisition.getDrawingOfficer().getName();
        }

        if (workOrderEstimateId != null && id != null)
            // Approved ARF amount for View mode
            advancePaid = contractorAdvanceService.getAdvancePaidByWOEstIdForView(workOrderEstimateId, id);
        if (advancePaid == null)
            advancePaid = new BigDecimal(0.00);
        super.prepare();

        // Load workflow related data here
        addDropdownData("executingDepartmentList", departmentService.getAllDepartments());
        final Assignment loggedInUserAssignment = abstractEstimateService.getLatestAssignmentForCurrentLoginUser();
        if (loggedInUserAssignment != null)
            contractorAdvanceRequisition.setWorkflowDepartmentId(loggedInUserAssignment.getDepartment().getId());
        workflowFunctionaryId = contractorAdvanceService.getFunctionaryForWorkflow(contractorAdvanceRequisition);
    }

    @SkipValidation
    @Action(value = "/contractoradvance/contractorAdvanceRequisition-newform")
    public String newform() {
        return NEW;
    }

    @SkipValidation
    public String edit() throws Exception {
        if (SOURCE_INBOX.equalsIgnoreCase(sourcepage)) {
            final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
            final boolean isValidUser = worksService.validateWorkflowForUser(contractorAdvanceRequisition, user);
            if (isValidUser)
                throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
        } else if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";
        return EDIT;
    }

    public String save() {
        String actionName = "";
        try {
            if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null)
                actionName = parameters.get(ACTION_NAME)[0];

            if (!(CANCEL_ACTION.equals(actionName) || REJECT_ACTION.equals(actionName))) {
                final BigDecimal totalEstimateValueIncludingREValue = contractorAdvanceService
                        .getTotalEstimateValueIncludingRE(contractorAdvanceRequisition.getWorkOrderEstimate()
                                .getEstimate());
                if (advancePaid.add(contractorAdvanceRequisition.getAdvanceRequisitionAmount())
                        .longValue() > totalEstimateValueIncludingREValue
                                .longValue())
                    throw new ValidationException(Arrays.asList(new ValidationError(
                            "advancerequisition.validate.advancepaid.estimatevalue",
                            "advancerequisition.validate.advancepaid.estimatevalue")));
            }

            contractorAdvanceRequisition.setArftype(ARF_TYPE);
            contractorAdvanceService.save(contractorAdvanceRequisition, actionName, advanceAccountCode);
        } catch (final ValidationException validationException) {
            final List<ValidationError> errorList = validationException.getErrors();
            for (final ValidationError error : errorList)
                if (error.getMessage().contains("DatabaseSequenceFirstTimeException")) {
                    prepare();
                    throw new ValidationException(Arrays.asList(new ValidationError("error", error.getMessage())));
                } else
                    throw new ValidationException(validationException.getErrors());
        }

        if (SAVE_ACTION.equals(actionName))
            messageKey = "advancerequisition.save.success";
        else
            messageKey = "advancerequisition." + actionName;
        addActionMessage(getText(messageKey, messageKey));

        getDesignation(contractorAdvanceRequisition);

        if (SAVE_ACTION.equals(actionName))
            sourcepage = "inbox";
        return SAVE_ACTION.equals(actionName) ? EDIT : SUCCESS;
    }

    public String cancel() {
        final String actionName = parameters.get("actionName")[0];
        contractorAdvanceService.cancelContractorAdvanceRequisition(contractorAdvanceRequisition, actionName);
        messageKey = "advancerequisition.cancel";
        getDesignation(contractorAdvanceRequisition);
        return SUCCESS;
    }

    private void getDesignation(final ContractorAdvanceRequisition contractorAdvanceRequisition) {
        if (contractorAdvanceRequisition.getCurrentState() != null
                && !"NEW".equalsIgnoreCase(contractorAdvanceRequisition.getCurrentState().getValue())) {
            final String result = worksService.getEmpNameDesignation(contractorAdvanceRequisition.getState()
                    .getOwnerPosition(), contractorAdvanceRequisition.getState().getCreatedDate());
            if (result != null && !"@".equalsIgnoreCase(result)) {
                final String empName = result.substring(0, result.lastIndexOf('@'));
                final String designation = result.substring(result.lastIndexOf('@') + 1, result.length());
                setNextEmployeeName(empName);
                setNextDesignation(designation);
            }
        }
    }

    public List<WorkflowAction> getValidActions() {
        return workflowService.getValidActions(contractorAdvanceRequisition);
    }

    @Override
    public void validate() {
        String actionName = "";
        if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null)
            actionName = parameters.get(ACTION_NAME)[0];

        if (!(actionName.equalsIgnoreCase("reject") || actionName.equalsIgnoreCase("cancel"))) {
            if (contractorAdvanceRequisition.getAdvanceRequisitionDate() == null)
                addFieldError("advanceRequisitionDate", getText("advancerequisition.date.required"));
            if (!DateUtils.compareDates(contractorAdvanceRequisition.getAdvanceRequisitionDate(),
                    contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getApprovedDate()))
                addFieldError("advanceRequisitionDate",
                        getText("advancerequisition.date.lessthan.workorder.approveddate"));
            if (!DateUtils.compareDates(new Date(), contractorAdvanceRequisition.getAdvanceRequisitionDate()))
                addFieldError("advanceRequisitionDate",
                        getText("advancerequisition.validate.date.greaterthan.currentDate"));

            if (advanceAccountCode == null || advanceAccountCode == -1 || advanceAccountCode == 0)
                addFieldError("advanceAccountCode", getText("advancerequisition.advanceaccountcode.required"));
            if (contractorAdvanceRequisition.getDrawingOfficer() == null
                    || contractorAdvanceRequisition.getDrawingOfficer().getId() == null
                    || contractorAdvanceRequisition.getDrawingOfficer().getId() == -1
                    || contractorAdvanceRequisition.getDrawingOfficer().getId() == 0)
                addFieldError("drawingOfficer", getText("advancerequisition.drawingofficer.required"));

            if (!worksService.checkBigDecimalValue(contractorAdvanceRequisition.getAdvanceRequisitionAmount(),
                    BigDecimal.valueOf(0.00)))
                addFieldError("advanceRequisitionAmount", getText("advancerequisition.valid.amount.greaterthan.zero"));

            if (!sourcepage.equals("search")) {
                // Check for any bill is created for this estimate
                checkForBills();

                // Check is there already a ARF for this estimate is in workflow
                if (id == null)
                    checkARFInWorkflowForEstimate();
            }
        }
    }

    protected void checkForBills() {
        final Long billCount = (Long) persistenceService.find(
                "select count(*) from MBHeader mbh where mbh.workOrderEstimate.id = ? and mbh.egwStatus.code = ? "
                        + "and (mbh.egBillregister is not null and mbh.egBillregister.billstatus <> ?)",
                contractorAdvanceRequisition.getWorkOrderEstimate().getId(),
                MBHeader.MeasurementBookStatus.APPROVED.toString(),
                ContractorBillRegister.BillStatus.CANCELLED.toString());
        if (billCount != null && billCount > 0)
            addActionError(getText("advancerequisition.validate.billcreated.message"));
    }

    protected void checkARFInWorkflowForEstimate() {
        final ContractorAdvanceRequisition arf = (ContractorAdvanceRequisition) persistenceService
                .find(" from ContractorAdvanceRequisition arf where arf.workOrderEstimate.id = ? and arf.status.code not in(?, ?) ",
                        contractorAdvanceRequisition.getWorkOrderEstimate().getId(),
                        ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString(),
                        ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString());
        if (arf != null)
            addActionError(getText("advancerequisition.validate.arf.in.workflow.message",
                    new String[] { arf.getAdvanceRequisitionNumber() }));
    }

    public ContractorAdvanceRequisition getContractorAdvanceRequisition() {
        return contractorAdvanceRequisition;
    }

    public void setContractorAdvanceRequisition(final ContractorAdvanceRequisition contractorAdvanceRequisition) {
        this.contractorAdvanceRequisition = contractorAdvanceRequisition;
    }

    public Long getWorkOrderEstimateId() {
        return workOrderEstimateId;
    }

    public void setWorkOrderEstimateId(final Long workOrderEstimateId) {
        this.workOrderEstimateId = workOrderEstimateId;
    }

    public WorkOrderEstimate getWorkOrderEstimate() {
        return workOrderEstimate;
    }

    public void setWorkOrderEstimate(final WorkOrderEstimate workOrderEstimate) {
        this.workOrderEstimate = workOrderEstimate;
    }

    public BigDecimal getAdvancePaid() {
        return advancePaid;
    }

    public void setAdvancePaid(final BigDecimal advancePaid) {
        this.advancePaid = advancePaid;
    }

    public WorksService getWorksService() {
        return worksService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public Long getAdvanceAccountCode() {
        return advanceAccountCode;
    }

    public void setAdvanceAccountCode(final Long advanceAccountCode) {
        this.advanceAccountCode = advanceAccountCode;
    }

    public void setContractorAdvanceService(final ContractorAdvanceService contractorAdvanceService) {
        this.contractorAdvanceService = contractorAdvanceService;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getLogedInUserDept() {
        return logedInUserDept;
    }

    public void setLogedInUserDept(final Integer logedInUserDept) {
        this.logedInUserDept = logedInUserDept;
    }

    public AbstractEstimateService getAbstractEstimateService() {
        return abstractEstimateService;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public String getSourcepage() {
        return sourcepage;
    }

    public void setSourcepage(final String sourcepage) {
        this.sourcepage = sourcepage;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public Integer getWorkflowFunctionaryId() {
        return workflowFunctionaryId;
    }

    public void setWorkflowFunctionaryId(final Integer workflowFunctionaryId) {
        this.workflowFunctionaryId = workflowFunctionaryId;
    }

    public void setDepartmentService(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(final String messageKey) {
        this.messageKey = messageKey;
    }

    public String getNextEmployeeName() {
        return nextEmployeeName;
    }

    public void setNextEmployeeName(final String nextEmployeeName) {
        this.nextEmployeeName = nextEmployeeName;
    }

    public String getNextDesignation() {
        return nextDesignation;
    }

    public void setNextDesignation(final String nextDesignation) {
        this.nextDesignation = nextDesignation;
    }

    public void setContractorAdvanceWorkflowService(final WorkflowService<ContractorAdvanceRequisition> workflow) {
        workflowService = workflow;
    }

    public String getDrawingOfficerName() {
        return drawingOfficerName;
    }

    public void setDrawingOfficerName(final String drawingOfficerName) {
        this.drawingOfficerName = drawingOfficerName;
    }

}