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
package org.egov.works.web.actions.workorder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.EmployeeView;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infra.persistence.utils.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.AssetsForEstimate;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.tender.EstimateLineItemsForWP;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.TenderResponseActivity;
import org.egov.works.models.tender.TenderResponseContractors;
import org.egov.works.models.workorder.AssetsForWorkOrder;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.TenderResponseService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.services.impl.WorkOrderServiceImpl;
import org.egov.works.utils.DateConversionUtil;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.jasperreports.engine.JRException;

@ParentPackage("egov")
@Results(value = {
        @Result(name = WorkOrderAction.PRINT, type = "stream", location = "WorkOrderPDF", params = {
                "inputName", "WorkOrderPDF", "contentType", "application/pdf", "contentDisposition", "no-cache" }),
        @Result(name = WorkOrderAction.WORKORDERNOTICEPDF, type = "stream", location = "WorkOrderPDF", params = {
                "inputName", "WorkOrderPDF", "contentType", "application/pdf", "contentDisposition",
                "no-cache;filename=WorkOrderNotice.pdf" }),
        @Result(name = WorkOrderAction.NEW, location = "workOrder-new.jsp") })
public class WorkOrderAction extends BaseFormAction {

    private static final long serialVersionUID = -8902400945730474523L;
    private static final String SAVE_ACTION = "save";
    public final static String APPROVED = "APPROVED";
    private final NumberFormat formatter = new DecimalFormat("#0.00");
    private WorkOrder workOrder = new WorkOrder();
    private WorkOrderService workOrderService;
    private WorksService worksService;
    private TenderResponseService tenderResponseService;
    private AbstractEstimateService abstractEstimateService;
    private PersistenceService<OfflineStatus, Long> worksStatusService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private EmployeeServiceOld employeeServiceOld;
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    private EisUtilService eisService;
    private Long tenderRespId;
    private TenderResponse tenderResponse;
    private Long deptId;
    private Integer empId;
    private String editableDate;
    private String createdBySelection;
    private String status;
    private Double activityAssignedAmt;
    private Date fromDate;
    private Date toDate;
    private Long assignedTo1;
    private Long assignedTo2;
    private String messageKey;
    private Long id;
    private String setStatus;
    private String mode;
    private static final String PREPARED_BY_LIST = "preparedByList";
    private static final String DEPARTMENT_LIST = "departmentList";
    private static final String ASSIGNED_TO_LIST = "assignedToList";
    private static final String ASSIGNED_USER_LIST1 = "assignedUserList1";
    private static final String ASSIGNED_USER_LIST2 = "assignedUserList2";
    private static final String OBJECT_TYPE = "TenderResponse";
    private static final String WO_OBJECT_TYPE = "WorkOrder";
    private static final String STATUS_OBJECTID = "getStatusDateByObjectId_Type_Desc";
    private static final String WORK_ORDER_CREATIONDATE = "STATUS_FOR_WORKORDER_CREATION";
    private static final String SITE_HAND_OVER = "Site handed over";
    private static final String WORK_COMMENCED = "Work commenced";
    private static final String WF_APPROVED = "APPROVED";
    private static final String SEARCH_WO = "searchWorkOrder";
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    private static final String SOURCE_INBOX = "inbox";

    private List<Designation> workOrderDesigList = new ArrayList<Designation>();
    private List<WorkOrder> workOrderList = null;
    // private List<String> workOrderActions;
    private Long workOrderId;
    private String sourcepage = "";
    private String percTenderType = "";
    private String tenderResponseType = null;

    private WorkflowService<WorkOrder> workOrderWorkflowService;

    private OfflineStatus setStatusObj;
    public static final String PRINT = "print";
    private InputStream workOrderPDF;
    private ReportService reportService;

    private String employeeName;
    private String designation;
    private String estimateNumber;
    private String wpNumber;
    private String tenderFileNumber;
    private PersonalInformationService personalInformationService;
    private Long tenderRespContrId;
    private TenderResponseContractors tenderResponseContractor;
    private PersistenceService<TenderResponseContractors, Long> tenderResponseContractorsService;
    private Double securityDepositConfValue;
    private Double labourWelfareFundConfValue;
    private List<WorkOrderActivity> actionWorkOrderActivities = new LinkedList<WorkOrderActivity>();
    private List<WorkOrderActivity> woActivities = new LinkedList<WorkOrderActivity>();
    private PersistenceService<Activity, Long> activityService;

    private Integer page = 1;
    private Integer pageSize = 30;
    private EgovPaginatedList pagedResults;
    private String cancellationReason;
    private String cancelRemarks;
    private String workOrderNo;
    private String woStatus;
    private static final Logger logger = Logger.getLogger(WorkOrderAction.class);
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private Date contractPeriodCutOffDate;
    private Integer defaultPreparedBy;
    private Long defaultDepartmentId;
    private final String loggedInUserEmployeeCode = null;
    private Long estimateId;
    private AbstractEstimate abstractEstimate = null;
    private Boolean isWorkCommenced;
    private Integer reportId = -1;
    protected static final String WORKORDERNOTICEPDF = "workOrderNotice";
    public static final String WORKFLOW_ENDS = "END";

    public WorkOrderAction() {
        addRelatedEntity("contractor", Contractor.class);
        addRelatedEntity("engineerIncharge", PersonalInformation.class);
        addRelatedEntity("engineerIncharge2", PersonalInformation.class);
        addRelatedEntity("workOrderPreparedBy", PersonalInformation.class);
    }

    @Override
    public void prepare() {
        if (estimateId != null)
            abstractEstimate = abstractEstimateService.findById(estimateId, false);

        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        final AjaxWorkOrderAction ajaxWorkOrderAction = new AjaxWorkOrderAction();
        ajaxWorkOrderAction.setPersistenceService(getPersistenceService());
        ajaxWorkOrderAction.setPersonalInformationService(personalInformationService);
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        ajaxEstimateAction.setAssignmentService(assignmentService);
        ajaxEstimateAction.setAbstractEstimateService(abstractEstimateService);
        ajaxEstimateAction.setEisService(eisService);
        addDropdownData("executingDepartmentList",
                getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)"));
        if (worksService != null) {
            final List<String> tenderTypeList = worksService.getTendertypeList();
            if (tenderTypeList != null && !tenderTypeList.isEmpty())
                percTenderType = tenderTypeList.get(0);
        }
        if (tenderRespId != null) {
            tenderResponse = tenderResponseService.findById(tenderRespId, false);
            if (tenderRespContrId != null)
                tenderResponseContractor = tenderResponseContractorsService.findById(tenderRespContrId, false);
            if (tenderResponse.getTenderResponseContractors().size() > 1)
                workOrder.setWorkOrderAmount(0.0D);
            else
                workOrder.setWorkOrderAmount(getEstimateAmountAfterNego());
            tenderResponse.setActivitiesForWorkorder(workOrderService.getActivitiesForWorkorder(tenderResponse));
        }
        if (id != null) {
            workOrder = workOrderService.findById(id, false);
            final Object woNoticeFlag = workOrderService.getWorkCommencedDateByWOId(id);
            isWorkCommenced = woNoticeFlag == null ? false : true;

            if (workOrder.getEgwStatus().getCode().equalsIgnoreCase("cancelled"))
                tenderResponse = tenderResponseService.findByNamedQuery("getTenderFortenderIdCanceledWO", workOrder
                        .getNegotiationNumber(), workOrder.getId(), workOrder.getTenderNumber(), workOrder
                                .getContractor().getId(),
                        workOrder.getPackageNumber());
            else
                tenderResponse = tenderResponseService.findByNamedQuery("getTenderFortenderId",
                        workOrder.getTenderNumber());
            tenderRespId = tenderResponse.getId();
            tenderResponse.setWorkOrderAmount(getWorkOrderAmount());
            tenderResponseContractor = tenderResponseContractorsService.find(
                    "from TenderResponseContractors where contractor.id=? and tenderResponse.id=?", workOrder
                            .getContractor().getId(),
                    tenderResponse.getId());
            tenderRespContrId = tenderResponseContractor.getId();
            setWorkOrderActivities(workOrder);
        }
        super.prepare();
        if (tenderResponse != null)
            if (tenderResponse.getTenderEstimate().getAbstractEstimate() == null)
                deptId = tenderResponse.getTenderEstimate().getWorksPackage().getDepartment().getId();
            else
                deptId = tenderResponse.getTenderEstimate().getAbstractEstimate().getExecutingDepartment().getId();
        setupDropdownDataExcluding("contractor", "engineerIncharge", "engineerIncharge2", "workOrderPreparedBy");
        if (StringUtils.isNotBlank(getCreatedBy()) && "yes".equalsIgnoreCase(getCreatedBy())) {
            setCreatedBySelection(getCreatedBy());
            addDropdownData(DEPARTMENT_LIST, departmentService.getAllDepartments());
            populatePreparedByList(ajaxEstimateAction, deptId != null);
        } else {
            final List<EmployeeView> empViewList = getUsersInDepartment();
            if (empViewList != null && empViewList.size() == 1)
                defaultPreparedBy = empViewList.get(0).getId().intValue();
            addDropdownData(PREPARED_BY_LIST, empViewList);
            defaultDepartmentId = deptId;
            if (tenderResponse != null && tenderResponse.getTenderEstimate() != null
                    && tenderResponse.getTenderEstimate().getWorksPackage() != null
                    && tenderResponse.getTenderEstimate().getWorksPackage().getDepartment() != null)
                addDropdownData(DEPARTMENT_LIST,
                        Arrays.asList(tenderResponse.getTenderEstimate().getWorksPackage().getDepartment()));

            setCreatedBySelection(getCreatedBy());
        }

        if (StringUtils.isNotBlank(getPastDate()))
            setEditableDate(getPastDate());
        populateWorkOrderAssignedToList(ajaxWorkOrderAction, deptId != null);
        populateWorkOrderUsersList1(ajaxWorkOrderAction, assignedTo1 != null, deptId != null);
        populateWorkOrderUsersList2(ajaxWorkOrderAction, assignedTo2 != null, deptId != null);

        addDropdownData("deptListForSearch", departmentService.getAllDepartments());
        getDeptList();

        final String cutOffDate = worksService.getWorksConfigValue("CONTRACT_PERIOD_CUT_OFF_DATE");
        if (StringUtils.isNotBlank(cutOffDate))
            try {
                contractPeriodCutOffDate = dateFormatter.parse(cutOffDate);
            } catch (final ParseException pe) {
                logger.error("Unable to parse contract period cut off date");
            }
        if (tenderResponse != null && tenderResponse.getTenderEstimate() != null
                && tenderResponse.getTenderEstimate().getTenderType() != null
                && tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(percTenderType))
            tenderResponseType = tenderResponse.getTenderEstimate().getTenderType();

        if ("cancelWO".equals(sourcepage))
            setWoStatus(WorksConstants.APPROVED);

    }

    private List getUsersInDepartment() {
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        ajaxEstimateAction.setAssignmentService(assignmentService);
        ajaxEstimateAction.setEisService(eisService);

        if (deptId != null)
            ajaxEstimateAction.setExecutingDepartment(deptId);
        else if (tenderResponse != null)
            if (tenderResponse.getTenderEstimate().getAbstractEstimate() == null)
                ajaxEstimateAction.setExecutingDepartment(tenderResponse.getTenderEstimate().getWorksPackage()
                        .getDepartment().getId());
            else
                ajaxEstimateAction.setExecutingDepartment(tenderResponse.getTenderEstimate().getAbstractEstimate()
                        .getExecutingDepartment().getId());
        ajaxEstimateAction.setEmployeeCode(loggedInUserEmployeeCode);
        ajaxEstimateAction.usersInExecutingDepartment();
        return ajaxEstimateAction.getUsersInExecutingDepartment();
    }

    @Action(value = "/workorder/worksOrder-newform")
    public String newform() {
        workOrder.setSecurityDeposit(getSecurityDepositConfValue() / 100 * workOrder.getWorkOrderAmount());
        workOrder.setLabourWelfareFund(getLabourWelfareFundConfValue() / 100 * workOrder.getWorkOrderAmount());
        return NEW;
    }

    public String save() {
        final String actionName = parameters.get("actionName")[0];
        if (workOrder.getEgwStatus() == null
                || WorksConstants.REJECTED.equalsIgnoreCase(workOrder.getEgwStatus().getCode())
                || NEW.equalsIgnoreCase(workOrder.getEgwStatus().getCode())) {
            workOrder.getWorkOrderEstimates().clear();
            // populateAssets();
            populateWorkOrderActivities();
        }
        try {

            workOrderService.setWorkOrderNumber(tenderResponse.getTenderEstimate().getAbstractEstimate(), workOrder,
                    tenderResponse.getTenderEstimate().getWorksPackage());
        } catch (final ValidationException sequenceException) {
            setSourcepage("inbox");
            final List<ValidationError> errorList = sequenceException.getErrors();
            for (final ValidationError error : errorList)
                if (error.getMessage().contains("DatabaseSequenceFirstTimeException")) {
                    prepare();
                    throw new ValidationException(Arrays.asList(new ValidationError("error", error.getMessage())));
                }
        }
        if (workOrder.getEgwStatus() == null
                || WorksConstants.REJECTED.equalsIgnoreCase(workOrder.getEgwStatus().getCode())
                || NEW.equalsIgnoreCase(workOrder.getEgwStatus().getCode()))
            validateWorkOrderDate();

        if (SAVE_ACTION.equals(actionName) && workOrder.getEgwStatus() == null)
            workOrder.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("WorkOrder", "NEW"));

        workOrder = workOrderService.persist(workOrder);
        workOrder = workOrderWorkflowService.transition(actionName, workOrder, "");

        if (workOrder.getEgwStatus() != null && APPROVED.equalsIgnoreCase(workOrder.getEgwStatus().getCode())) {
            messageKey = "workOrder.approved";
            workOrder.setApprovedDate(new Date());

        } else
            messageKey = "workorder.save.success";
        addActionMessage(getText(messageKey, messageKey));
        getDesignation(workOrder);
        if (workOrder.getEgwStatus() == null
                || WorksConstants.REJECTED.equalsIgnoreCase(workOrder.getEgwStatus().getCode())
                || NEW.equalsIgnoreCase(workOrder.getEgwStatus().getCode()))
            setWorkOrderActivities(workOrder);

        if (SAVE_ACTION.equals(actionName))
            sourcepage = "inbox";

        return SAVE_ACTION.equals(actionName) ? EDIT : SUCCESS;
    }

    public String cancel() {
        final String actionName = parameters.get("actionName")[0];
        if (workOrder.getId() != null) {
            workOrderWorkflowService.transition(actionName, workOrder, "");
            workOrder = workOrderService.persist(workOrder);
        }
        messageKey = "workorder.cancel";
        return SUCCESS;
    }

    public void getDesignation(final WorkOrder workOrder) {
        /* start for customizing workflow message display */
        if (workOrder.getEgwStatus() != null && !"NEW".equalsIgnoreCase(workOrder.getEgwStatus().getCode())) {
            final String result = worksService.getEmpNameDesignation(workOrder.getState().getOwnerPosition(), workOrder
                    .getState().getCreatedDate());
            if (result != null && !"@".equalsIgnoreCase(result)) {
                final String empName = result.substring(0, result.lastIndexOf('@'));
                final String designation = result.substring(result.lastIndexOf('@') + 1, result.length());
                setEmployeeName(empName);
                setDesignation(designation);
            }
        }
        /* end */
    }

    private void validateWorkOrderDate() {

        // TODO - use Tender Response approved date instead of reading from
        // approved state createddate
        if (getWorkOrderCreationDate() == null
                && workOrder.getWorkOrderDate() != null
                && tenderResponse.getEgwStatus() != null
                && TenderResponse.TenderResponseStatus.APPROVED.toString().equals(
                        tenderResponse.getEgwStatus().getCode())
                && DateConversionUtil.isBeforeByDate(workOrder.getWorkOrderDate(), tenderResponse.getState()
                        .getCreatedDate()))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "workorder.workorderDate.lessthan.approvedDate", "workorder.workorderDate.lessthan.approvedDate")));
        else if (getWorkOrderCreationDate() != null && workOrder.getWorkOrderDate() != null
                && DateConversionUtil.isBeforeByDate(workOrder.getWorkOrderDate(), getWorkOrderCreationDate()))
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "workorder.workorderDate.lessthan.statusDate", getText(
                            "workorder.workorderDate.lessthan.statusDate", new String[] { setStatusObj.getEgwStatus()
                                    .getDescription() }))));
    }

    @SkipValidation
    public String viewWorkOrderPdf() throws JRException, Exception {
        ReportRequest reportRequest = null;
        Map<String, Object> reportParams = null;
        Double quotedPerc = null;
        Double negotiatedPerc = null;
        Double quotedAmount = null;
        Double negotiatedAmount = null;
        Double estimateAmt = null;
        TenderResponse tenderResponse;

        if (workOrder.getEgwStatus().getCode().equalsIgnoreCase("cancelled"))
            tenderResponse = (TenderResponse) persistenceService.findByNamedQuery("getTenderFortenderIdCanceledWO",
                    workOrder.getNegotiationNumber(), workOrder.getId(), workOrder.getTenderNumber(), workOrder
                            .getContractor().getId(),
                    workOrder.getPackageNumber());
        else
            tenderResponse = (TenderResponse) persistenceService.findByNamedQuery("getTenderFortenderId",
                    workOrder.getTenderNumber());

        final List<String> tenderTypeList = worksService.getTendertypeList();
        if (tenderTypeList != null && !tenderTypeList.isEmpty())
            percTenderType = tenderTypeList.get(0);
        if (tenderResponse != null && tenderResponse.getTenderEstimate() != null
                && tenderResponse.getTenderEstimate().getTenderType() != null
                && tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(percTenderType)) {
            quotedPerc = tenderResponse.getPercQuotedRate();
            negotiatedPerc = tenderResponse.getPercNegotiatedAmountRate();
            quotedAmount = null;
            negotiatedAmount = null;
            if (tenderResponse.getTenderResponseContractors().size() > 1) {
                if (negotiatedPerc >= 0)
                    estimateAmt = workOrder.getWorkOrderAmount() / (1 + Math.abs(negotiatedPerc) / 100);
                else
                    estimateAmt = workOrder.getWorkOrderAmount() / (1 - Math.abs(negotiatedPerc) / 100);
            } else
                estimateAmt = getWorkOrderAmount();
            if (quotedPerc >= 0)
                quotedAmount = estimateAmt + estimateAmt * (Math.abs(quotedPerc) / 100);
            else
                quotedAmount = estimateAmt - estimateAmt * (Math.abs(quotedPerc) / 100);
            if (negotiatedPerc >= 0)
                negotiatedAmount = estimateAmt + estimateAmt * (Math.abs(negotiatedPerc) / 100);
            else
                negotiatedAmount = estimateAmt - estimateAmt * (Math.abs(negotiatedPerc) / 100);
        }

        if (workOrder.getPackageNumber() == null) {
            reportParams = workOrderService.createHeaderParams(workOrder, "estimate");
            reportParams.put("WORKORDER_BILLOFQUANTITIES_LIST", workOrderService.getActivitiesForWorkorder(workOrder));
            reportParams.put("quotedAmount", quotedAmount);
            reportParams.put("quotedPerc", quotedPerc);
            reportParams.put("negotiatedAmount", negotiatedAmount);
            reportParams.put("negotiatedPerc", negotiatedPerc);
            reportParams.put("estimateAmt", estimateAmt);
            reportRequest = new ReportRequest("workorderForEstimate", workOrder, reportParams);
        } else {
            reportParams = workOrderService.createHeaderParams(workOrder, "wp");
            reportParams.put("WORKORDER_BILLOFQUANTITIES_LIST", workOrderService.getActivitiesForWorkorder(workOrder));
            reportParams.put("quotedAmount", quotedAmount);
            reportParams.put("quotedPerc", quotedPerc);
            reportParams.put("negotiatedAmount", negotiatedAmount);
            reportParams.put("negotiatedPerc", negotiatedPerc);
            reportRequest = new ReportRequest("workorderForWp", workOrderService.getAeForWp(workOrder), reportParams);
            reportParams.put("estimateAmt", estimateAmt);
        }
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            workOrderPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return PRINT;
    }

    private void populateWorkOrderActivities() {
        if (tenderResponse != null && tenderResponse.getTenderResponseContractors().size() == 1) {
            if (tenderResponse.getTenderEstimate().getWorksPackage() == null) {
                WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
                workOrderEstimate.setEstimate(tenderResponse.getTenderEstimate().getAbstractEstimate());
                workOrderEstimate.setWorkOrder(workOrder);
                for (final TenderResponseActivity tenderResponseActivity : tenderResponse.getTenderResponseActivities())
                    addTenderResponseActivities(workOrderEstimate, tenderResponseActivity);
                workOrderEstimate = populateAssets(workOrderEstimate, tenderResponse.getTenderEstimate()
                        .getAbstractEstimate());
                workOrder.addWorkOrderEstimate(workOrderEstimate);
            } else if (tenderResponse.getTenderEstimate().getAbstractEstimate() == null)
                for (final AbstractEstimate estimate : tenderResponse.getTenderEstimate().getWorksPackage()
                        .getAllEstimates()) {
                    WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
                    workOrderEstimate.setEstimate(estimate);
                    workOrderEstimate.setWorkOrder(workOrder);
                    for (final TenderResponseActivity tenderResponseActivity : tenderResponse
                            .getTenderResponseActivities())
                        for (final Activity act : estimate.getActivities())
                            if (act.getId().equals(tenderResponseActivity.getActivity().getId()))
                                addTenderResponseActivities(workOrderEstimate, tenderResponseActivity);
                    workOrderEstimate = populateAssets(workOrderEstimate, estimate);
                    workOrder.addWorkOrderEstimate(workOrderEstimate);
                }
        } else {
            final Map<Long, WorkOrderEstimate> workOrderEstimateMap = new HashMap<Long, WorkOrderEstimate>();
            for (final WorkOrderActivity woActivity : getActionWorkOrderActivityList()) {
                if (!workOrderEstimateMap.containsKey(woActivity.getActivity().getAbstractEstimate().getId())) {
                    WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
                    workOrderEstimate.setEstimate(woActivity.getActivity().getAbstractEstimate());
                    workOrderEstimate.setWorkOrder(workOrder);
                    workOrderEstimate = populateAssets(workOrderEstimate, woActivity.getActivity()
                            .getAbstractEstimate());
                    workOrderEstimateMap.put(woActivity.getActivity().getAbstractEstimate().getId(), workOrderEstimate);
                }
                final WorkOrderActivity workOrderActivity = new WorkOrderActivity();
                workOrderActivity.setActivity(woActivity.getActivity());
                workOrderActivity.setApprovedRate(woActivity.getApprovedRate());
                workOrderActivity.setApprovedQuantity(woActivity.getApprovedQuantity());
                workOrderActivity
                        .setApprovedAmount(new Money(woActivity.getApprovedRate() * woActivity.getApprovedQuantity()
                                * woActivity.getActivity().getConversionFactor()).getValue());
                final WorkOrderEstimate workOrderEstimate = workOrderEstimateMap.get(woActivity.getActivity()
                        .getAbstractEstimate().getId());
                workOrderActivity.setWorkOrderEstimate(workOrderEstimate);
                workOrderEstimate.addWorkOrderActivity(workOrderActivity);
            }
            for (final WorkOrderEstimate workOrderEstimate : workOrderEstimateMap.values())
                workOrder.addWorkOrderEstimate(workOrderEstimate);
        }
    }

    public String edit() {

        if (SOURCE_INBOX.equalsIgnoreCase(sourcepage)) {
            final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
            final boolean isValidUser = worksService.validateWorkflowForUser(workOrder, user);
            if (isValidUser)
                throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
        } else if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";

        return EDIT;
    }

    public Collection<EstimateLineItemsForWP> getActivitiesForWorkorder() {
        Collection<EstimateLineItemsForWP> li = null;
        if (id == null) {
            tenderResponse.setWorkOrderAmount(getWorkOrderAmount());
            li = workOrderService.getActivitiesForWorkorder(tenderResponse);
        } else
            li = workOrderService.getActivitiesForWorkorder(workOrder);
        return li;
    }

    @Override
    public Object getModel() {
        return workOrder;
    }

    @ValidationErrorPage(value = SEARCH_WO)
    public String searchWorkOrder() {
        return SEARCH_WO;
    }

    public List<EgwStatus> getWorkOrderStatuses() {
        final List<EgwStatus> woStatusList = egwStatusHibernateDAO.getStatusByModule(WorkOrder.class.getSimpleName());
        woStatusList.remove(egwStatusHibernateDAO.getStatusByModuleAndCode(WorkOrder.class.getSimpleName(), "NEW"));
        return woStatusList;
    }

    public List<EgwStatus> getWorkOrderStatusesForMBCreation() {
        return egwStatusHibernateDAO.getStatusListByModuleAndCodeList(WorkOrder.class.getSimpleName(),
                worksService.getNatureOfWorkAppConfigValues("Works", "WORKORDER_STATUS"));
    }

    public String getWOCreationForEstimateOrWP() {
        return worksService.getWorksConfigValue("ESTIMATE_OR_WP_FOR_WO");
    }

    private WorkOrderEstimate populateAssets(final WorkOrderEstimate workOrderEstimate, final AbstractEstimate estimate) {
        for (final AssetsForEstimate assetValue : estimate.getAssetValues()) {
            final AssetsForWorkOrder assetsForWorkOrder = new AssetsForWorkOrder();
            assetsForWorkOrder.setAsset(assetValue.getAsset());
            assetsForWorkOrder.setWorkOrderEstimate(workOrderEstimate);
            workOrderEstimate.addAssetValue(assetsForWorkOrder);
        }
        return workOrderEstimate;
    }

    private void addTenderResponseActivities(final WorkOrderEstimate workOrderEstimate,
            final TenderResponseActivity tenderResponseActivity) {
        final WorkOrderActivity workOrderActivity = new WorkOrderActivity();
        workOrderActivity.setActivity(tenderResponseActivity.getActivity());
        workOrderActivity.setApprovedRate(tenderResponseActivity.getNegotiatedRate());
        workOrderActivity.setApprovedQuantity(tenderResponseActivity.getNegotiatedQuantity());
        workOrderActivity.setApprovedAmount(new Money(workOrderActivity.getApprovedRate()
                * workOrderActivity.getApprovedQuantity() * tenderResponseActivity.getActivity().getConversionFactor())
                        .getValue());
        workOrderActivity.setWorkOrderEstimate(workOrderEstimate);
        workOrderEstimate.addWorkOrderActivity(workOrderActivity);
    }

    private Date getWorkOrderCreationDate() {
        final String statusForCreation = getWorkOrderCreationConfValue();
        if ("0".equals(statusForCreation))
            setStatusObj = (OfflineStatus) getPersistenceService().findByNamedQuery("getmaxStatusByObjectId", tenderRespId,
                    tenderRespId, OBJECT_TYPE);
        else
            setStatusObj = (OfflineStatus) getPersistenceService().findByNamedQuery("getStatusDateByObjectId_Type_Desc",
                    tenderRespId, OBJECT_TYPE, statusForCreation);
        if (setStatusObj != null)
            return setStatusObj.getStatusDate();
        return null;
    }

    private String getWorkOrderCreationConfValue() {
        return worksService.getWorksConfigValue(WORK_ORDER_CREATIONDATE);
    }

    public double getWorkOrderAmount() {
        double totalAmt = 0;
        for (final EstimateLineItemsForWP act : workOrderService.getActivitiesForWorkorder(tenderResponse))
            totalAmt += act.getAmt();
        return totalAmt;
    }

    public String getEstimateAmount() {
        double totalAmt = 0;
        for (final EstimateLineItemsForWP act : workOrderService.getActivitiesForWorksPackage(tenderResponse))
            totalAmt += act.getAmt();

        return formatter.format(totalAmt);
    }

    public double getEstimateAmountAfterNego() {
        double totalAmt = 0;
        for (final EstimateLineItemsForWP act : workOrderService.getActivitiesForWorkorder(tenderResponse))
            totalAmt += act.getAmt();
        if (tenderResponse != null && tenderResponse.getTenderEstimate() != null
                && tenderResponse.getTenderEstimate().getTenderType() != null
                && tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(percTenderType))
            if (tenderResponse.getPercNegotiatedAmountRate() >= 0)
                totalAmt = totalAmt + totalAmt * Math.abs(tenderResponse.getPercNegotiatedAmountRate()) / 100;
            else
                totalAmt = totalAmt - totalAmt * Math.abs(tenderResponse.getPercNegotiatedAmountRate()) / 100;

        return totalAmt;
    }

    @ValidationErrorPage(value = SEARCH_WO)
    public String searchWorkOrderDetails() {
        final Map<String, Object> criteriaMap = new HashMap<String, Object>();
        final List<Object> paramList = new ArrayList<Object>();

        if (StringUtils.isNotBlank(status) && !getStatus().equals("-1"))
            criteriaMap.put("STATUS", status);
        if (StringUtils.isNotBlank(workOrder.getWorkOrderNumber()))
            criteriaMap.put("WORKORDER_NO", workOrder.getWorkOrderNumber());
        if (StringUtils.isNotBlank(getEstimateNumber()))
            criteriaMap.put("ESTIMATE_NO", getEstimateNumber());
        if (getDeptId() != null && getDeptId() > 0)
            criteriaMap.put("DEPT_ID", getDeptId());
        if (StringUtils.isNotBlank(getWpNumber()))
            criteriaMap.put("WP_NO", getWpNumber());
        if (StringUtils.isNotBlank(getTenderFileNumber()))
            criteriaMap.put("TENDER_FILE_NO", getTenderFileNumber());
        if (workOrder.getContractor() != null && workOrder.getContractor().getId() != -1)
            criteriaMap.put("CONTRACTOR_ID", workOrder.getContractor().getId());
        if (fromDate != null && toDate != null && !DateUtils.compareDates(getToDate(), getFromDate()))
            addFieldError("enddate", getText("greaterthan.endDate.fromDate"));
        if (toDate != null && !DateUtils.compareDates(new Date(), getToDate()))
            addFieldError("enddate", getText("greaterthan.endDate.currentdate"));
        if (!getFieldErrors().isEmpty())
            return SEARCH_WO;
        if (fromDate != null && toDate == null)
            criteriaMap.put("FROM_DATE", new Date(DateUtils.getFormattedDate(getFromDate(), DATE_FORMAT)));
        else if (toDate != null && fromDate == null)
            criteriaMap.put("TO_DATE", new Date(DateUtils.getFormattedDate(getToDate(), DATE_FORMAT)));
        else if (fromDate != null && toDate != null && getFieldErrors().isEmpty()) {
            criteriaMap.put("FROM_DATE", new Date(DateUtils.getFormattedDate(getFromDate(), DATE_FORMAT)));
            criteriaMap.put("TO_DATE", new Date(DateUtils.getFormattedDate(getToDate(), DATE_FORMAT)));
        }

        criteriaMap.put(WorkOrderServiceImpl.SOURCEPAGE, sourcepage);

        if ("searchWOForMBCreation".equals(sourcepage))
            workOrderList = workOrderService.searchWOForMB(criteriaMap);
        else if ("searchWOForBillCreation".equals(sourcepage))
            workOrderList = workOrderService.searchWOForBilling(criteriaMap);
        else {
            List<String> qryObj;
            Object[] params;
            Long count;
            Page resPage;
            qryObj = workOrderService.searchWOToPaginatedView(criteriaMap, paramList);
            if (paramList.isEmpty()) {
                params = null;
                count = (Long) persistenceService.find(qryObj.get(0));
                final Query qryWithNoParam = persistenceService.getSession().createQuery(qryObj.get(1));
                resPage = new Page(qryWithNoParam, page, pageSize);
            } else {
                params = new Object[paramList.size()];
                params = paramList.toArray(params);
                count = (Long) persistenceService.find(qryObj.get(0), params);
                resPage = persistenceService.findPageBy(qryObj.get(1), page, pageSize, params);
            }
            pagedResults = new EgovPaginatedList(resPage, count.intValue());
            workOrderList = pagedResults != null ? pagedResults.getList() : null;
        }

        if (!workOrderList.isEmpty())
            workOrderList = getPositionAndUser(workOrderList);

        if (!("searchWOForBillCreation".equals(sourcepage) || "searchWOForMBCreation".equals(sourcepage)))
            pagedResults.setList(workOrderList);
        return SEARCH_WO;
    }

    protected List<WorkOrder> getPositionAndUser(final List<WorkOrder> results) {
        final List<WorkOrder> workOrderList = new ArrayList<WorkOrder>();
        for (final WorkOrder workOrder : results) {
            if (workOrder.getCurrentState() != null)
                if (!workOrder.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED)
                        && !workOrder.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)) {
                    final PersonalInformation emp = employeeServiceOld.getEmployeeforPosition(workOrder.getCurrentState()
                            .getOwnerPosition());
                    if (emp != null && StringUtils.isNotBlank(emp.getEmployeeName()))
                        workOrder.setOwner(emp.getEmployeeName());
                }
            // workOrder.setWorkOrderAmount(getWorkOrderActvitiesAmount(workOrder));
            workOrderList.add(workOrder);

            if (workOrder.getEgwStatus() != null && workOrder.getEgwStatus().getCode().equals(WF_APPROVED)) {
                final OfflineStatus set_status = (OfflineStatus) persistenceService.findByNamedQuery(
                        "getmaxStatusByObjectId_Type", workOrder.getId(), workOrder.getId(),
                        WorkOrder.class.getSimpleName(), WorkOrder.class.getSimpleName());
                if (set_status == null)
                    workOrder.setStatus(workOrder.getEgwStatus().getCode());
                else
                    workOrder.setStatus(set_status.getEgwStatus().getCode());
            } else if (workOrder.getEgwStatus() != null)
                workOrder.setStatus(workOrder.getEgwStatus().getCode());

            final String approved = getApprovedValue();
            final String actions = worksService.getWorksConfigValue("WORKORDER_SHOW_ACTIONS");
            if (StringUtils.isNotBlank(actions)) {
                String setStat = "";
                String workCommencedStatus = "";
                OfflineStatus lastStatus = null;
                workOrder.getWorkOrderActions().addAll(Arrays.asList(actions.split(",")));
                if (workOrder.getId() != null && getLastStatus() != null)
                    lastStatus = worksStatusService.findByNamedQuery(STATUS_OBJECTID, workOrder.getId(),
                            WO_OBJECT_TYPE, getLastStatus());
                if (lastStatus != null || "view".equalsIgnoreCase(setStatus))
                    setStat = worksService.getWorksConfigValue("WORKS_VIEW_OFFLINE_STATUS_VALUE");
                else if (lastStatus == null && StringUtils.isNotBlank(approved) && workOrder.getEgwStatus() != null
                        && approved.equals(workOrder.getEgwStatus().getCode()))
                    setStat = worksService.getWorksConfigValue("WORKS_SETSTATUS_VALUE");
                if (StringUtils.isNotBlank(setStat))
                    workOrder.getWorkOrderActions().add(setStat);
                if (lastStatus != null
                        && lastStatus.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.WO_STATUS_WOCOMMENCED)) {
                    workCommencedStatus = worksService.getWorksConfigValue("WORK_ORDER_NOTICE");
                    if (StringUtils.isNotBlank(workCommencedStatus))
                        workOrder.getWorkOrderActions().add(workCommencedStatus);
                }
            }
        }

        return workOrderList;
    }

    public String getApprovedValue() {
        return WorksConstants.APPROVED;
    }

    public String getLastStatus() {
        return worksService.getWorksConfigValue(WorksConstants.WORKORDER_LASTSTATUS);
    }

    public double getWorkOrderActvitiesAmount(final WorkOrder workOrder) {
        double totalAmt = 0;
        for (final WorkOrderEstimate workOrderEstimate : workOrder.getWorkOrderEstimates())
            for (final WorkOrderActivity woAct : workOrderEstimate.getWorkOrderActivities())
                totalAmt += woAct.getApprovedAmount();
        return totalAmt;
    }

    public List<WorkflowAction> getValidActions() {
        return workOrderWorkflowService.getValidActions(workOrder);
    }

    public String getPastDate() {
        return worksService.getWorksConfigValue("WORK_ORDER_PASTDATE");
    }

    private String getMBCreationBySelection() {
        return worksService.getWorksConfigValue("MB_CREATED_BY_SELECTION");
    }

    public void getDeptList() {
        if (StringUtils.isNotBlank(getMBCreationBySelection())) {
            if ("no".equals(getMBCreationBySelection())) {
                final List<Department> deptList = worksService.getAllDeptmentsForLoggedInUser();
                addDropdownData("deptListForMB", deptList);
            } else
                addDropdownData("deptListForMB", departmentService.getAllDepartments());
        } else
            addDropdownData("deptListForMB", Collections.EMPTY_LIST);
    }

    public String getCreatedBy() {
        return worksService.getWorksConfigValue("WORK_ORDER_CREATEDBY");
    }

    private void populatePreparedByList(final AjaxEstimateAction ajaxEstimateAction,
            final boolean executingDeptPopulated) {
        if (executingDeptPopulated) {
            ajaxEstimateAction.setExecutingDepartment(deptId);
            ajaxEstimateAction.usersInExecutingDepartment();
            addDropdownData(PREPARED_BY_LIST, ajaxEstimateAction.getUsersInExecutingDepartment());
        } else
            addDropdownData(PREPARED_BY_LIST, Collections.EMPTY_LIST);
    }

    private void populateWorkOrderAssignedToList(final AjaxWorkOrderAction ajaxWorkOrderAction,
            final boolean executingDeptPopulated) {
        if (executingDeptPopulated && deptId > 0) {
            ajaxWorkOrderAction.setDepartmentName(departmentService.getDepartmentById(Long.valueOf(deptId)).getName());
            ajaxWorkOrderAction.getDesignationByDeptId();
            addDropdownData(ASSIGNED_TO_LIST, ajaxWorkOrderAction.getWorkOrderDesigList());
        } else
            addDropdownData(ASSIGNED_TO_LIST, Collections.EMPTY_LIST);
    }

    private void populateWorkOrderUsersList1(final AjaxWorkOrderAction ajaxWorkOrderAction, final boolean desgId,
            final boolean executingDeptPopulated) {
        if (desgId && executingDeptPopulated && deptId > 0) {
            ajaxWorkOrderAction.setDesgId(getAssignedTo1());
            ajaxWorkOrderAction.setExecutingDepartment(deptId);
            ajaxWorkOrderAction.getUsersForDesg();
            addDropdownData(ASSIGNED_USER_LIST1, ajaxWorkOrderAction.getUserList());
        } else
            addDropdownData(ASSIGNED_USER_LIST1, Collections.EMPTY_LIST);
    }

    private void populateWorkOrderUsersList2(final AjaxWorkOrderAction ajaxWorkOrderAction, final boolean desgId,
            final boolean executingDeptPopulated) {
        if (desgId && executingDeptPopulated && deptId > 0) {
            ajaxWorkOrderAction.setDesgId(getAssignedTo2());
            ajaxWorkOrderAction.setExecutingDepartment(deptId);
            ajaxWorkOrderAction.getUsersForDesg();
            addDropdownData(ASSIGNED_USER_LIST2, ajaxWorkOrderAction.getUserList());
        } else
            addDropdownData(ASSIGNED_USER_LIST2, Collections.EMPTY_LIST);
    }

    public String viewWorkOrderNotice() {
        String nameOfWO = "";
        final Locale LOCALE = new Locale("en", "IN");
        final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", LOCALE);
        final Date wOCommencedDate = workOrderService.getWorkCommencedDateByWOId(id);
        final String workCommencedDate = DDMMYYYYFORMATS.format(wOCommencedDate);
        final String approverName = getApproverName(workOrder);
        final List<WorkOrderNoticeEsimateInfo> estimates = getEstimatesForWO(workOrder);
        // Setting work order name based on size of estimates for a work order
        // if size is 1 then assigning the name to name of abstract estimate
        // if size is greater than 1 then assigning the name for work order form
        // worksPackage
        if (estimates.size() == 1)
            nameOfWO = workOrder.getWorkOrderEstimates().get(0).getEstimate().getName();
        else
            nameOfWO = workOrderService.getWorksPackageName(workOrder.getPackageNumber());
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        reportParams.put("executingDept", workOrder.getWorkOrderEstimates().get(0).getEstimate()
                .getExecutingDepartment().getName());
        reportParams.put("workOrderNo", workOrder.getWorkOrderNumber());
        reportParams.put("contractorNC", workOrder.getContractor().getName() + " / "
                + workOrder.getContractor().getCode());
        reportParams.put("contractPeriod", workOrder.getContractPeriod());
        reportParams.put("defectLiability", workOrder.getDefectLiabilityPeriod());
        reportParams.put("allottedTo", workOrder.getEngineerIncharge().getName());
        reportParams.put("nameOfWO", nameOfWO);
        reportParams.put("valueOfWO", NumberUtil.formatNumber(BigDecimal.valueOf(workOrder.getWorkOrderAmount())));
        reportParams.put("workCommencedDate", workCommencedDate);
        reportParams.put("approverName", approverName);
        final ReportRequest reportRequest = new ReportRequest("WorkOrderNotice", estimates, reportParams);
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            workOrderPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return WORKORDERNOTICEPDF;
    }

    protected String getApproverName(final WorkOrder wo) {
        String approver = "";
        final List<StateHistory> history = wo.getStateHistory();
        for (final StateHistory st : history)
            if (st.getValue().equalsIgnoreCase(WORKFLOW_ENDS)) {
                final PersonalInformation pInfo = employeeServiceOld.getEmployeeforPosition(st.getOwnerPosition());
                if (pInfo != null && StringUtils.isNotBlank(pInfo.getName()))
                    approver = approver + pInfo.getName();
            }
        return approver;
    }

    protected List<WorkOrderNoticeEsimateInfo> getEstimatesForWO(final WorkOrder wo) {
        final List<WorkOrderNoticeEsimateInfo> woEstimateList = new ArrayList<WorkOrderNoticeEsimateInfo>();
        Double woEstimateAmount = 0D;
        final Object[] obj = (Object[]) workOrderService.getTenderNegotiationInfo(workOrder.getNegotiationNumber());
        final String tenderType = (String) obj[1];
        for (final WorkOrderEstimate woe : wo.getWorkOrderEstimates()) {
            final WorkOrderNoticeEsimateInfo estimateInfo = new WorkOrderNoticeEsimateInfo();
            estimateInfo.setEstimateNumber(woe.getEstimate().getEstimateNumber());
            estimateInfo.setWorkName(woe.getEstimate().getName());
            estimateInfo.setWorkDescription(woe.getEstimate().getDescription());
            if (StringUtils.isNotBlank(tenderType) && tenderType.equalsIgnoreCase(WorksConstants.PERC_TENDER)) {
                final Double estimateAmt = woe.getEstimate().getWorkValue();
                woEstimateAmount = estimateAmt + estimateAmt * (Double) obj[0] / 100;
            } else
                for (final WorkOrderActivity woa : woe.getWorkOrderActivities())
                    woEstimateAmount = woEstimateAmount + woa.getApprovedAmount();
            estimateInfo.setEstimateValue(NumberUtil.formatNumber(BigDecimal.valueOf(woEstimateAmount)));
            woEstimateList.add(estimateInfo);
        }
        return woEstimateList;
    }

    public Double getSecurityDepositConfValue() {
        securityDepositConfValue = workOrderService.getSecurityDepositConfValue();
        return securityDepositConfValue;
    }

    public void setSecurityDepositConfValue(final Double securityDepositConfValue) {
        this.securityDepositConfValue = securityDepositConfValue;
    }

    public Double getLabourWelfareFundConfValue() {
        labourWelfareFundConfValue = workOrderService.getLabourWelfareFundConfValue();
        return labourWelfareFundConfValue;
    }

    public void setLabourWelfareFundConfValue(final Double labourWelfareFundConfValue) {
        this.labourWelfareFundConfValue = labourWelfareFundConfValue;
    }

    public String getAssignedToRequiredOrNot() {
        return worksService.getWorksConfigValue("WORKORDER_ASSIGNEDTO_REQUIRED");
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(final WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public String getCreatedBySelection() {
        return createdBySelection;
    }

    public void setCreatedBySelection(final String createdBySelection) {
        this.createdBySelection = createdBySelection;
    }

    public void setWorkOrderService(final WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public void setTenderResponseService(final TenderResponseService tenderResponseService) {
        this.tenderResponseService = tenderResponseService;
    }

    public TenderResponse getTenderResponse() {
        return tenderResponse;
    }

    public void setTenderResponse(final TenderResponse tenderResponse) {
        this.tenderResponse = tenderResponse;
    }

    public void setDepartmentService(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public String getEditableDate() {
        return editableDate;
    }

    public void setEditableDate(final String editableDate) {
        this.editableDate = editableDate;
    }

    public Integer getEmpId() {
        return empId;
    }

    public Collection<EstimateLineItemsForWP> getActivitiesForWorkorderList() {
        return tenderResponse.getActivitiesForWorkorder();
    }

    public void setEmpId(final Integer empId) {
        this.empId = empId;
    }

    public Long getTenderRespId() {
        return tenderRespId;
    }

    public void setTenderRespId(final Long tenderRespId) {
        this.tenderRespId = tenderRespId;
    }

    public List<Designation> getWorkOrderDesigList() {
        return workOrderDesigList;
    }

    public void setWorkOrderDesigList(final List<Designation> workOrderDesigList) {
        this.workOrderDesigList = workOrderDesigList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public Long getAssignedTo1() {
        return assignedTo1;
    }

    public void setAssignedTo1(final Long assignedTo1) {
        this.assignedTo1 = assignedTo1;
    }

    public Long getAssignedTo2() {
        return assignedTo2;
    }

    public void setAssignedTo2(final Long assignedTo2) {
        this.assignedTo2 = assignedTo2;
    }

    public List<WorkOrder> getWorkOrderList() {
        return workOrderList;
    }

    public void setWorkOrderList(final List<WorkOrder> workOrderList) {
        this.workOrderList = workOrderList;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    /*
     * public List<String> getWorkOrderActions() { return workOrderActions ; }
     */

    public Map<String, Object> getContractorForApprovedWorkOrder() {
        final Map<String, Object> contractorsWithWOList = new HashMap<String, Object>();
        if (workOrderService.getContractorsWithWO() != null)
            for (final Contractor contractor : workOrderService.getContractorsWithWO())
                contractorsWithWOList.put(contractor.getId() + "", contractor.getCode() + " - " + contractor.getName());
        return contractorsWithWOList;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(final Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setWorkOrderWorkflowService(final WorkflowService<WorkOrder> workOrderWorkflowService) {
        this.workOrderWorkflowService = workOrderWorkflowService;
    }

    public String getSetStatus() {
        return setStatus;
    }

    public void setSetStatus(final String setStatus) {
        this.setStatus = setStatus;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public InputStream getWorkOrderPDF() {
        return workOrderPDF;
    }

    public void setWorkOrderPDF(final InputStream workOrderPDF) {
        this.workOrderPDF = workOrderPDF;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(final String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(final String designation) {
        this.designation = designation;
    }

    public String getSourcepage() {
        return sourcepage;
    }

    public void setSourcepage(final String sourcepage) {
        this.sourcepage = sourcepage;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(final Long deptId) {
        this.deptId = deptId;
    }

    public void setWorksStatusService(final PersistenceService<OfflineStatus, Long> worksStatusService) {
        this.worksStatusService = worksStatusService;
    }

    public Date getSiteHandOverDate() {
        if (id != null) {
            final OfflineStatus objStatusForSite = worksStatusService.findByNamedQuery(STATUS_OBJECTID, id, WO_OBJECT_TYPE,
                    SITE_HAND_OVER);
            if (objStatusForSite != null)
                return objStatusForSite.getStatusDate();
        }
        return null;
    }

    public Date getWorkCommencedDate() {
        if (id != null) {
            final OfflineStatus objStatusForSite = worksStatusService.findByNamedQuery(STATUS_OBJECTID, id, WO_OBJECT_TYPE,
                    WORK_COMMENCED);
            if (objStatusForSite != null)
                return objStatusForSite.getStatusDate();
        }
        return null;
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    public String getWpNumber() {
        return wpNumber;
    }

    public void setWpNumber(final String wpNumber) {
        this.wpNumber = wpNumber;
    }

    public String getTenderFileNumber() {
        return tenderFileNumber;
    }

    public void setTenderFileNumber(final String tenderFileNumber) {
        this.tenderFileNumber = tenderFileNumber;
    }

    public void setPersonalInformationService(final PersonalInformationService personalInformationService) {
        this.personalInformationService = personalInformationService;
    }

    public Long getTenderRespContrId() {
        return tenderRespContrId;
    }

    public void setTenderRespContrId(final Long tenderRespContrId) {
        this.tenderRespContrId = tenderRespContrId;
    }

    public TenderResponseContractors getTenderResponseContractor() {
        return tenderResponseContractor;
    }

    public void setTenderResponseContractor(final TenderResponseContractors tenderResponseContractor) {
        this.tenderResponseContractor = tenderResponseContractor;
    }

    public void setTenderResponseContractorsService(
            final PersistenceService<TenderResponseContractors, Long> tenderResponseContractorsService) {
        this.tenderResponseContractorsService = tenderResponseContractorsService;
    }

    public List<WorkOrderActivity> getActionWorkOrderActivities() {
        return actionWorkOrderActivities;
    }

    public void setActionWorkOrderActivities(final List<WorkOrderActivity> actionWorkOrderActivities) {
        this.actionWorkOrderActivities = actionWorkOrderActivities;
    }

    public void setActivityService(final PersistenceService<Activity, Long> activityService) {
        this.activityService = activityService;
    }

    public Collection<WorkOrderActivity> getActionWorkOrderActivityList() {
        final Collection<WorkOrderActivity> woActivityList = workOrderService
                .getActionWorkOrderActivitiesList(actionWorkOrderActivities);
        for (final WorkOrderActivity workOrderActivity : woActivityList) {
            workOrderActivity.setActivity(activityService.findById(workOrderActivity.getActivity().getId(), false));
            workOrderActivity.setUnAssignedQuantity(workOrderActivity.getActivity().getQuantity()
                    - getAssignedQuantity(workOrderActivity.getActivity().getId(), workOrder.getNegotiationNumber()));
        }
        return woActivityList;
    }

    public List<WorkOrderActivity> getWoActivities() {
        return woActivities;
    }

    public void setWoActivities(final List<WorkOrderActivity> woActivities) {
        this.woActivities = woActivities;
    }

    public void setWorkOrderActivities(final WorkOrder workorder) {
        woActivities.clear();
        for (final WorkOrderEstimate workOrderEstimate : workorder.getWorkOrderEstimates())
            if (workOrderEstimate != null)
                for (final WorkOrderActivity workOrderActivity : workOrderEstimate.getWorkOrderActivities()) {
                    workOrderActivity.setUnAssignedQuantity(workOrderActivity.getActivity().getQuantity()
                            - getAssignedQuantity(workOrderActivity.getActivity().getId(), workOrderActivity
                                    .getWorkOrderEstimate().getWorkOrder().getNegotiationNumber()));
                    woActivities.add(workOrderActivity);
                }
    }

    @Override
    public void validate() {
        final Collection<WorkOrderActivity> woActivityList = workOrderService
                .getActionWorkOrderActivitiesList(actionWorkOrderActivities);

        final String contractPrd = workOrder.getContractPeriod().toString();

        if (parameters.get("actionName") != null) {
            final String actionName = parameters.get("actionName")[0];
            if (!actionName.equalsIgnoreCase("reject")) {
                if (workOrder.getEgwStatus() == null && id == null && !"searchWOForMBCreation".equals(sourcepage)
                        && !"searchWOForBillCreation".equals(sourcepage) && !"cancelWO".equals(sourcepage)
                        && (estimateId != null || tenderRespId != null && tenderRespContrId != null)
                        && (SAVE_ACTION.equals(actionName) || "submit_for_approval".equals(actionName))) {
                    validateMandatoryFields();
                    validateContractPeriod(contractPrd);
                }
                if (workOrder.getEgwStatus() != null
                        && (workOrder.getEgwStatus().getCode().equalsIgnoreCase("NEW") || workOrder.getEgwStatus()
                                .getCode().equalsIgnoreCase("REJECTED"))
                        && id != null) {
                    validateMandatoryFields();
                    if (contractPeriodCutOffDate != null) {
                        final Date createdDate = workOrder.getCreatedDate() == null ? new Date() : workOrder
                                .getCreatedDate();
                        if (createdDate.after(contractPeriodCutOffDate))
                            validateContractPeriod(contractPrd);
                    }
                }
                if (!actionName.equalsIgnoreCase("cancel"))
                    for (final WorkOrderActivity workOrderActivity : woActivityList) {
                        if (workOrderActivity.getApprovedRate() == 0.0)
                            addActionError(getText("WorkOrderActivity.approvedRate.non.negative"));
                        if (workOrderActivity.getApprovedQuantity() == 0.0)
                            addActionError(getText("WorkOrderActivity.approvedQuantity.non.negative"));
                    }
                validateDLP();
            }
        }
        if (workOrder.getEgwStatus() == null && id == null && !"searchWOForMBCreation".equals(sourcepage)
                && !"searchWOForBillCreation".equals(sourcepage) && !"cancelWO".equals(sourcepage))
            if (tenderRespId != null && tenderRespContrId != null)
                if (tenderResponse.getTenderResponseContractors().size() > 1)
                    multipleContractorsValidation();
                else
                    singleContractorValidation();
    }

    private void validateContractPeriod(final String contractPrd) {
        if (StringUtils.isBlank(contractPrd))
            addActionError(getText("contractPeriod.null"));
        if (StringUtils.isNotBlank(contractPrd) && Integer.parseInt(contractPrd) <= 0)
            addActionError(getText("contractPeriod.greater.than.zero"));
    }

    private void validateMandatoryFields() {
        if (workOrder.getWorkOrderDate() == null)
            addActionError(getText("workorder.date.null"));
        if (workOrder.getEmdAmountDeposited() == 0.00)
            addActionError(getText("workOrder.emdAmount.invalid"));
    }

    private void singleContractorValidation() {
        if (id == null) {
            final WorkOrder woObj = (WorkOrder) persistenceService.find(
                    "from WorkOrder wo where wo.negotiationNumber = ? and wo.egwStatus.code!='CANCELLED' ",
                    workOrder.getNegotiationNumber());
            if (woObj != null)
                addActionError(getText("workOrder.tenderNegotiation.uniqueCheck.message"));
        }
    }

    private void multipleContractorsValidation() {
        if (id == null) {
            final String woaQuery = "select sum(woa.approvedQuantity) from WorkOrderActivity woa where woa.workOrderEstimate.workOrder.negotiationNumber=? and woa.workOrderEstimate.workOrder.egwStatus.code !='CANCELLED' ";
            final Double countWoaIds = (Double) persistenceService.find(woaQuery, workOrder.getNegotiationNumber());
            final String traQuery = "select sum(tra.negotiatedQuantity) from TenderResponseActivity tra where tra.tenderResponse.negotiationNumber = ? and tra.tenderResponse.egwStatus.code != 'CANCELLED'";
            final Double countTraIds = (Double) persistenceService.find(traQuery, workOrder.getNegotiationNumber());

            if (countWoaIds != null && countTraIds != null && countTraIds != 0 && countWoaIds != 0
                    && countWoaIds >= countTraIds)
                addActionError(getText("workOrder.tenderNegotiation.uniqueCheck.multipleContractor.message"));
        }
    }

    private void validateDLP() {
        if (workOrder.getDefectLiabilityPeriod() <= 0.0)
            addActionError(getText("defectLiabilityPeriod.validate"));
    }

    private double getAssignedQuantity(final Long activityId, final String negotiationNumber) {
        final Object[] params = new Object[] { negotiationNumber, WorksConstants.CANCELLED_STATUS, activityId };
        final Double assignedQty = (Double) getPersistenceService().findByNamedQuery("getAssignedQuantityForActivity",
                params);

        if (assignedQty == null)
            return 0.0d;
        else
            return assignedQty.doubleValue();
    }

    @ValidationErrorPage(value = SEARCH_WO)
    public String cancelApprovedWO() {
        final WorkOrder workOrder = workOrderService.findById(workOrderId, false);
        validateARFForWO(workOrder);
        workOrder
                .setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WO_OBJECT_TYPE, WorksConstants.CANCELLED_STATUS));

        if (workOrder.getCurrentState() != null) {
            final PersonalInformation prsnlInfo = employeeServiceOld.getEmpForUserId(worksService
                    .getCurrentLoggedInUserId());
            String empName = "";
            if (prsnlInfo.getEmployeeFirstName() != null)
                empName = prsnlInfo.getEmployeeFirstName();
            if (prsnlInfo.getEmployeeLastName() != null)
                empName = empName.concat(" ").concat(prsnlInfo.getEmployeeLastName());
            if (cancelRemarks != null && StringUtils.isNotBlank(cancelRemarks))
                cancellationReason.concat(" : ").concat(cancelRemarks).concat(". ")
                        .concat(getText("workOrder.cancel.cancelledby")).concat(": ").concat(empName);
            else
                cancellationReason.concat(". ").concat(getText("workOrder.cancel.cancelledby")).concat(": ")
                        .concat(empName);

            // TODO - The setter methods of variables in State.java are
            // protected. Need to alternative way to solve this issue.
            // Set the status and workflow state to cancelled
            /****
             * State oldEndState = workOrder.getCurrentState(); Position owner = prsnlInfo.getAssignment(new
             * Date()).getPosition(); oldEndState.setCreatedBy(prsnlInfo.getUserMaster());
             * oldEndState.setModifiedBy(prsnlInfo.getUserMaster()); oldEndState.setCreatedDate(new Date());
             * oldEndState.setModifiedDate(new Date()); oldEndState.setOwner(owner);
             * oldEndState.setValue(WorksConstants.CANCELLED_STATUS.toString()); oldEndState.setText1(cancellationText);
             * workOrder.changeState("END", owner, null);
             ****/
        }

        workOrderNo = workOrder.getWorkOrderNumber();
        messageKey = workOrderNo + " : " + getText("workorder.cancel");
        return SUCCESS;
    }

    /*
     * Validate is there any Advance Requisition forms created for Estimates in this work order. If yes then throw proper
     * validation message.
     */
    private void validateARFForWO(final WorkOrder workOrder) {
        String arfNo = "";
        String estimateNo = "";
        for (final WorkOrderEstimate woe : workOrder.getWorkOrderEstimates())
            for (final ContractorAdvanceRequisition arf : woe.getContractorAdvanceRequisitions()) {
                if (!arf.getStatus()
                        .getCode()
                        .equalsIgnoreCase(
                                ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString()))
                    if (!arfNo.equals(""))
                        arfNo = arfNo.concat(", ARF#:").concat(arf.getAdvanceRequisitionNumber());
                    else
                        arfNo = arfNo.concat(arf.getAdvanceRequisitionNumber());
                if (!arf.getWorkOrderEstimate().getEstimate().getEgwStatus().getCode()
                        .equalsIgnoreCase(AbstractEstimate.EstimateStatus.CANCELLED.toString()))
                    if (!estimateNo.equals(""))
                        estimateNo = estimateNo.concat(", ").concat(
                                arf.getWorkOrderEstimate().getEstimate().getEstimateNumber());
                    else
                        estimateNo = estimateNo.concat(arf.getWorkOrderEstimate().getEstimate().getEstimateNumber());
            }
        if (!arfNo.equals(""))
            throw new ValidationException(Arrays.asList(new ValidationError("cancelWO.arf.created.message", getText(
                    "cancelWO.arf.created.message", new String[] { arfNo, estimateNo }))));
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public String getPercTenderType() {
        return percTenderType;
    }

    public String getTenderResponseType() {
        return tenderResponseType;
    }

    public Double getActivityAssignedAmt() {
        return activityAssignedAmt;
    }

    public void setActivityAssignedAmt(final Double activityAssignedAmt) {
        this.activityAssignedAmt = activityAssignedAmt;
    }

    public EgovPaginatedList getPagedResults() {
        return pagedResults;
    }

    public void setPagedResults(final EgovPaginatedList pagedResults) {
        this.pagedResults = pagedResults;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(final String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancelRemarks() {
        return cancelRemarks;
    }

    public void setCancelRemarks(final String cancelRemarks) {
        this.cancelRemarks = cancelRemarks;
    }

    public String getWorkOrderNo() {
        return workOrderNo;
    }

    public void setWorkOrderNo(final String workOrderNo) {
        this.workOrderNo = workOrderNo;
    }

    public String getWoStatus() {
        return woStatus;
    }

    public void setWoStatus(final String woStatus) {
        this.woStatus = woStatus;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPage(final Integer page) {
        this.page = page;
    }

    public void setPageSize(final Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Date getContractPeriodCutOffDate() {
        return contractPeriodCutOffDate;
    }

    public void setContractPeriodCutOffDate(final Date contractPeriodCutOffDate) {
        this.contractPeriodCutOffDate = contractPeriodCutOffDate;
    }

    public Integer getDefaultPreparedBy() {
        return defaultPreparedBy;
    }

    public Long getDefaultDepartmentId() {
        return defaultDepartmentId;
    }

    public String getLoggedInUserEmployeeCode() {
        return loggedInUserEmployeeCode;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public AbstractEstimate getAbstractEstimate() {
        return abstractEstimate;
    }

    public void setAbstractEstimate(final AbstractEstimate abstractEstimate) {
        this.abstractEstimate = abstractEstimate;
    }

    public Boolean getIsWorkCommenced() {
        return isWorkCommenced;
    }

    public void setIsWorkCommenced(final Boolean isWorkCommenced) {
        this.isWorkCommenced = isWorkCommenced;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(final Integer reportId) {
        this.reportId = reportId;
    }

}