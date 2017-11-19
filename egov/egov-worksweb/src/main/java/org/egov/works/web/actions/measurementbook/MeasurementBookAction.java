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
package org.egov.works.web.actions.measurementbook;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.EmployeeView;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.utils.Page;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.tender.TenderEstimate;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.MeasurementBookWFService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.services.impl.MeasurementBookServiceImpl;
import org.egov.works.services.impl.WorkOrderServiceImpl;
import org.egov.works.utils.DateConversionUtil;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Result(name = MeasurementBookAction.NEW, location = "measurementBook-new.jsp")
public class MeasurementBookAction extends BaseFormAction {

    private static final long serialVersionUID = 1536135285051426488L;
    private static final Logger logger = Logger.getLogger(MeasurementBookAction.class);
    private static final String VERIFY = "verify";
    private static final String SUBMITTED = "submitted";
    private static final String ACTIVITY_SEARCH = "activitySearch";
    private static final String MB_SEARCH = "mbSearch";
    private static final String DATEFORMAT = "dd-MMM-yyyy";

    private MBHeader mbHeader = new MBHeader();
    private List<MBDetails> mbDetails = new LinkedList<MBDetails>();
    private String messageKey;
    private Long id;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private EmployeeServiceOld employeeServiceOld;
    @Autowired
    private UserService userService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    private EmployeeView mbPreparedByView;
    private MeasurementBookService measurementBookService;
    private WorksService worksService;
    private WorkOrderService workOrderService;
    private List<MBDetails> actionMbDetailValues = new LinkedList<MBDetails>();
    private double quantityFactor;
    private List<WorkOrderActivity> activityList; // for search page
    private List<MBHeader> mbList; // for search page
    private List<WorkOrderEstimate> workOrderEstimateList = new ArrayList<WorkOrderEstimate>();
    private String workorderNo;
    private Long workOrderId;
    private String mode;
    private String mborderNumberRequired;
    private String workName;
    private String projectCode;
    // -----------------------Activity Search ----------------------------------
    private String activityCode;
    private String activityDesc;
    // -------------------------------------------------------------------

    // -----------------------MB Search ----------------------------------
    private Long contractorId;
    private Date fromDate;
    private Date toDate;
    private String mbRefNo;
    private String mbPages;
    private String mbStatus;
    // -------------------------------------------------------------------

    // -----------------------Manual Workflow ----------------------------
    private Long departmentId;
    private Integer designationId;
    private String approverComments;
    private Integer approverUserId;
    // -------------------------------------------------------------------

    // -------------- on for workflow service
    private MeasurementBookWFService measurementBookWFService;
    private WorkflowService<MBHeader> workflowService;
    private static final String SAVE_ACTION = "save";
    private static final String SOURCE_INBOX = "inbox";

    private String sourcepage = "";
    private String dispEmployeeName;
    private String dispDesignation;
    private Long estimateId;
    private PersonalInformationService personalInformationService;
    private static final String ACTION_NAME = "actionName";
    private String activitySearchMode;
    private boolean isLegacyMB;
    private static final String NON_TENDERED = "nonTendered";
    private static final String LUMP_SUM = "lumpSum";
    private String cancellationReason;
    private String cancelRemarks;
    private Long mbId;
    private String estimateNo;
    private Integer execDeptid;
    private Integer page = 1;
    private Integer pageSize = 30;
    private EgovPaginatedList pagedResults;
    private Integer defaultPreparedById;
    private String defaultDesgination;
    private String isRCEstimate;
    private static final String YES = "yes";
    private Date workCommencedDate;
    private Date latestMBDate;
    private Long woId;

    public MeasurementBookAction() {
        addRelatedEntity("workOrder", WorkOrder.class);
    }

    @Override
    public void prepare() {
        final AjaxMeasurementBookAction ajaxMBAction = new AjaxMeasurementBookAction();
        ajaxMBAction.setPersistenceService(getPersistenceService());
        ajaxMBAction.setAssignmentService(assignmentService);
        ajaxMBAction.setPersonalInformationService(personalInformationService);
        if (id != null) {
            mbHeader = measurementBookService.findById(id, false);
            if (mbHeader != null)
                workOrderEstimateList.add(mbHeader.getWorkOrderEstimate());
        } else if (workOrderId != null) {
            workOrderEstimateList.addAll(getPersistenceService().findAllByNamedQuery(
                    "getWorkOrderEstimateByWorkOrderId", workOrderId));
            mbHeader.setWorkOrder(workOrderService.findById(workOrderId, false));
        }
        if (workOrderEstimateList.isEmpty())
            addDropdownData("workOrderEstimateList", Collections.EMPTY_LIST);
        else
            addDropdownData("workOrderEstimateList",
                    measurementBookService.getWorkOrderEstimatesForMB(workOrderEstimateList));

        super.prepare();
        setupDropdownDataExcluding("workOrder");
        addDropdownData("executingDepartmentList", getPersistenceService().findAllBy("from DepartmentImpl"));
        if (getLatestAssignmentForCurrentLoginUser() != null)
            departmentId = getLatestAssignmentForCurrentLoginUser().getDepartment().getId();

        populateQuantityFactor();
        if ("cancelMB".equals(sourcepage))
            setMbStatus(MBHeader.MeasurementBookStatus.APPROVED.toString());
        getWrkCommndAndLatestMBDates();
    }

    private void getWrkCommndAndLatestMBDates() {
        if (id == null
                || mbHeader.getEgwStatus() != null
                        && (mbHeader.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.NEW) || mbHeader.getEgwStatus()
                                .getCode().equalsIgnoreCase(WorksConstants.REJECTED))) {
            if (id == null)
                woId = workOrderId;
            else
                woId = mbHeader.getWorkOrder().getId();
            if (woId != null)
                workCommencedDate = measurementBookService.getWorkCommencedDate(woId);
            if (workOrderEstimateList.size() == 1) {
                Long estId;
                if (id == null)
                    estId = workOrderEstimateList.get(0).getEstimate().getId();
                else
                    estId = mbHeader.getWorkOrderEstimate().getEstimate().getId();
                if (estId != null)
                    latestMBDate = measurementBookService.getLastMBCreatedDate(woId, estId);
            }
        }
    }

    public Assignment getLatestAssignmentForCurrentLoginUser() {
        PersonalInformation personalInformation = null;
        final Long loggedInUserId = worksService.getCurrentLoggedInUserId();
        if (loggedInUserId != null)
            personalInformation = employeeServiceOld.getEmpForUserId(loggedInUserId);
        Assignment assignment = null;
        if (personalInformation != null)
            assignment = employeeServiceOld.getLatestAssignmentForEmployee(personalInformation.getIdPersonalInformation());
        return assignment;
    }

    protected void populateQuantityFactor() {
        final String configVal = worksService.getWorksConfigValue("MAXEXTRALINEITEMPERCENTAGE");
        try {
            quantityFactor = Double.valueOf(configVal);
        } catch (final Exception e) {
            logger.error("Exception in populateQuantityFactor()>>>" + e.getMessage());
            quantityFactor = 0.0d;
        }
    }

    protected MBHeader calculateMBdetails(final MBHeader mbHeader, final boolean isPersistedObject) {
        return measurementBookService.calculateMBDetails(mbHeader, isPersistedObject);
    }

    public String loadSerachForActivity() {
        logger.info("Loading search page for Activity............");
        return ACTIVITY_SEARCH;
    }

    public String searchActivitiesForMB() {
        final Map<String, Object> criteriaMap = new HashMap<String, Object>();
        if (workorderNo != null && !"".equalsIgnoreCase(workorderNo))
            criteriaMap.put(WorkOrderServiceImpl.WORKORDER_NO, workorderNo);
        if (estimateId != null)
            criteriaMap.put(WorkOrderServiceImpl.WORKORDER_ESTIMATE_ID, estimateId);
        if (activityCode != null && !"".equalsIgnoreCase(activityCode))
            criteriaMap.put(WorkOrderServiceImpl.ACTIVITY_CODE, activityCode);
        if (activityDesc != null && !"".equalsIgnoreCase(activityDesc))
            criteriaMap.put(WorkOrderServiceImpl.ACTIVITY_DESC, activityDesc);
        if (workOrderId != null)
            criteriaMap.put(WorkOrderServiceImpl.WORKORDER_ID, workOrderId);
        if (StringUtils.isNotBlank(activitySearchMode) && activitySearchMode.equalsIgnoreCase(NON_TENDERED)) {
            criteriaMap.put(WorkOrderServiceImpl.REVISION_TYPE, RevisionType.NON_TENDERED_ITEM.toString());
            activityList = workOrderService.searchWOActivitiesFromRevEstimates(criteriaMap);
        } else if (StringUtils.isNotBlank(activitySearchMode) && activitySearchMode.equalsIgnoreCase(LUMP_SUM)) {
            criteriaMap.put(WorkOrderServiceImpl.REVISION_TYPE, RevisionType.LUMP_SUM_ITEM.toString());
            activityList = workOrderService.searchWOActivitiesFromRevEstimates(criteriaMap);
        } else
            activityList = workOrderService.searchWOActivities(criteriaMap);

        return ACTIVITY_SEARCH;
    }

    public String loadSerachForMB() {
        logger.debug("Loading search page for MB............");
        return MB_SEARCH;
    }

    public String loadSearchForMB() {
        logger.debug("Loading search page for MB............");
        return MB_SEARCH;
    }

    public String searchMB() {
        final Map<String, Object> criteriaMap = new HashMap<String, Object>();
        final List<Object> paramList = new ArrayList<Object>();
        List<String> qryObj = new ArrayList<String>();
        Object[] params;
        if (workorderNo != null && !"".equalsIgnoreCase(workorderNo))
            criteriaMap.put(MeasurementBookServiceImpl.WORKORDER_NO, workorderNo);

        if (contractorId != null && contractorId != -1)
            criteriaMap.put(MeasurementBookServiceImpl.CONTRACTOR_ID, contractorId);

        if (fromDate != null && toDate != null && !DateUtils.compareDates(getToDate(), getFromDate()))
            addFieldError("enddate", getText("greaterthan.endDate.fromDate"));

        if (toDate != null && !DateUtils.compareDates(new Date(), getToDate()))
            addFieldError("enddate", getText("greaterthan.endDate.currentdate"));

        if (fromDate != null && toDate == null)
            criteriaMap.put("FROM_DATE", new Date(DateUtils.getFormattedDate(getFromDate(), DATEFORMAT)));
        else if (toDate != null && fromDate == null)
            criteriaMap.put("TO_DATE", new Date(DateUtils.getFormattedDate(getToDate(), DATEFORMAT)));
        else if (fromDate != null && toDate != null && getFieldErrors().isEmpty()) {
            criteriaMap.put("FROM_DATE", new Date(DateUtils.getFormattedDate(getFromDate(), DATEFORMAT)));
            criteriaMap.put("TO_DATE", new Date(DateUtils.getFormattedDate(getToDate(), DATEFORMAT)));
        }

        if (mbRefNo != null && !"".equalsIgnoreCase(mbRefNo))
            criteriaMap.put(MeasurementBookServiceImpl.MB_REF_NO, mbRefNo);
        if (mbPages != null && !"".equalsIgnoreCase(mbPages))
            criteriaMap.put(MeasurementBookServiceImpl.MB_PAGE_NO, mbPages);
        if (mbStatus != null && !"".equalsIgnoreCase(mbStatus) && !"-1".equals(mbStatus))
            criteriaMap.put(MeasurementBookServiceImpl.STATUS, mbStatus);
        /*
         * if(mbStatus != null && "-1".equals(mbStatus)) criteriaMap.put(MeasurementBookServiceImpl.STATUS, mbStatus);
         */
        if (execDeptid != null && execDeptid != -1)
            criteriaMap.put(MeasurementBookServiceImpl.DEPT_ID, execDeptid);
        if (estimateNo != null && !"".equalsIgnoreCase(estimateNo))
            criteriaMap.put(MeasurementBookServiceImpl.EST_NO, estimateNo);

        qryObj = measurementBookService.searchMB(criteriaMap, paramList);
        // mbList = measurementBookService.searchMB(criteriaMap, paramList);

        Page resPage;
        Long count;

        final String qry = qryObj.get(0);
        if (paramList.isEmpty()) {
            params = null;
            final Query qry1 = persistenceService.getSession().createQuery(qry);
            count = (Long) persistenceService.find(qryObj.get(1));
            resPage = new Page(qry1, page, pageSize);
            // resPage= persistenceService.findPageBy(qry,page,pageSize,params);
        } else {
            params = new Object[paramList.size()];
            params = paramList.toArray(params);
            count = (Long) persistenceService.find(qryObj.get(1), params);
            resPage = persistenceService.findPageBy(qry, page, pageSize, params);
        }
        pagedResults = new EgovPaginatedList(resPage, count.intValue());
        mbList = pagedResults != null ? pagedResults.getList() : null;

        if (!mbList.isEmpty())
            mbList = getPositionAndUser(mbList);

        pagedResults.setList(mbList);
        return MB_SEARCH;
    }

    protected List<MBHeader> getPositionAndUser(final List<MBHeader> results) {
        final List<MBHeader> mbHeaderList = new ArrayList<MBHeader>();
        for (final MBHeader mbh : results) {
            if (!mbh.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED)
                    && !mbh.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)) {
                final PersonalInformation emp = employeeServiceOld.getEmployeeforPosition(mbh.getCurrentState()
                        .getOwnerPosition());
                if (emp != null && StringUtils.isNotBlank(emp.getEmployeeName()))
                    mbh.setOwner(emp.getEmployeeName());
            }
            mbHeaderList.add(mbh);
            final String actions = worksService.getWorksConfigValue("MB_SHOW_ACTIONS");
            if (StringUtils.isNotBlank(actions))
                mbh.getMbActions().addAll(Arrays.asList(actions.split(",")));
        }
        return mbHeaderList;
    }

    public Map<String, Object> getContractorForApprovedWorkOrder() {
        final Map<String, Object> contractorsWithWOList = new HashMap<String, Object>();
        if (workOrderService.getContractorsWithWO() != null)
            for (final Contractor contractor : workOrderService.getContractorsWithWO())
                contractorsWithWOList.put(contractor.getId() + "", contractor.getCode() + " - " + contractor.getName());
        return contractorsWithWOList;
    }

    public List<EgwStatus> getMbStatusList() {
        return persistenceService.findAllBy("from EgwStatus s where moduletype=? and code<>'NEW' order by orderId",
                MBHeader.class.getSimpleName());
    }

    public List<Contractor> getContractorList() {
        return workOrderService.getAllContractorForWorkOrder();
    }

    @Action(value = "/measurementbook/measurementBook-newform")
    public String newform() {
        return NEW;
    }

    public String edit() {
        if (SOURCE_INBOX.equalsIgnoreCase(sourcepage)) {
            final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
            final boolean isValidUser = worksService.validateWorkflowForUser(mbHeader, user);
            if (isValidUser)
                throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
        } else if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";

        mbHeader = calculateMBdetails(mbHeader, true);
        return NEW;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    private boolean canUserModify() {
        boolean result = false;
        String designWhoCanModify = null;
        designWhoCanModify = getMBWorkflowModifyDesignation();
        String currentDesgination = null;
        if (mbHeader.getCurrentState() != null
                && mbHeader.getCurrentState().getOwnerPosition() != null
                && mbHeader.getCurrentState().getOwnerPosition().getDeptDesig() != null
                && mbHeader.getCurrentState().getOwnerPosition().getDeptDesig().getDesignation() != null
                && mbHeader.getCurrentState().getOwnerPosition().getDeptDesig().getDesignation().getName() != null)
            currentDesgination = mbHeader.getCurrentState().getOwnerPosition().getDeptDesig().getDesignation()
                    .getName();
        if (designWhoCanModify != null && currentDesgination != null
                && designWhoCanModify.equalsIgnoreCase(currentDesgination))
            result = true;
        return result;
    }

    private void setApprovedQtyAndPrevCumlVal() {
        final AjaxMeasurementBookAction ajaxMBAction = new AjaxMeasurementBookAction();
        ajaxMBAction.setPersistenceService(getPersistenceService());
        ajaxMBAction.setAssignmentService(assignmentService);
        ajaxMBAction.setPersonalInformationService(personalInformationService);
        ajaxMBAction.setMeasurementBookService(measurementBookService);

        Long woActId = null;
        final Long mbHeaderId = mbHeader.getId() == null ? null : mbHeader.getId();
        ajaxMBAction.setMbHeaderId(mbHeaderId);
        for (final MBDetails mbDetails : mbHeader.getMbDetails())
            if (mbDetails != null) {
                woActId = mbDetails.getWorkOrderActivity().getId();
                ajaxMBAction.setWoActivityId(woActId);
                ajaxMBAction.activityDetails();
                mbDetails.setTotalEstQuantity(ajaxMBAction.getTotalEstQuantity());
                mbDetails.setPrevCumlvQuantity(ajaxMBAction.getPrevCulmEntry());
            }
    }

    public String save() {
        if (mbHeader.getEgwStatus() == null
                || WorksConstants.REJECTED.equalsIgnoreCase(mbHeader.getEgwStatus().getCode())
                || NEW.equalsIgnoreCase(mbHeader.getEgwStatus().getCode()) || canUserModify())
            mbHeader.getMbDetails().clear();
        mbHeader.setIsLegacyMB(isLegacyMB);
        String actionName = "";
        if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null)
            actionName = parameters.get(ACTION_NAME)[0];
        if (mbHeader.getEgwStatus() == null
                || WorksConstants.REJECTED.equalsIgnoreCase(mbHeader.getEgwStatus().getCode())
                || NEW.equalsIgnoreCase(mbHeader.getEgwStatus().getCode()) || canUserModify())
            populateActivities();
        final WorkOrderEstimate woe = (WorkOrderEstimate) persistenceService.findByNamedQuery(
                "getWorkOrderEstimateByEstAndWO", estimateId, workOrderId);
        mbHeader.setWorkOrderEstimate(woe);

        if (id == null
                || mbHeader.getEgwStatus() != null
                        && (mbHeader.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.NEW) || mbHeader.getEgwStatus()
                                .getCode().equalsIgnoreCase(WorksConstants.REJECTED))) {
            if (workCommencedDate != null && workCommencedDate.after(mbHeader.getMbDate()))
                throw new ValidationException(Arrays.asList(new ValidationError("mb.lessThan.wrk.cmmncd.date",
                        getText("mb.lessThan.wrk.cmmncd.date") + " "
                                + new SimpleDateFormat("dd/MM/yyyy").format(workCommencedDate) + ". "
                                + getText("pls.enter.valid.date"))));
            if (latestMBDate != null && latestMBDate.after(mbHeader.getMbDate()))
                throw new ValidationException(Arrays.asList(new ValidationError("mb.lessThan.latest.mbdate.date",
                        getText("mb.lessThan.latest.mbdate.date") + " "
                                + new SimpleDateFormat("dd/MM/yyyy").format(latestMBDate) + ". "
                                + getText("pls.enter.valid.date"))));
        }
        try {
            if (mbHeader.getEgwStatus() == null
                    || WorksConstants.REJECTED.equalsIgnoreCase(mbHeader.getEgwStatus().getCode())
                    || NEW.equalsIgnoreCase(mbHeader.getEgwStatus().getCode()) || canUserModify())
                validateMBAmount(woe);
        } catch (final ValidationException e) {
            throw e;
        } catch (final Exception e) {
            logger.debug("Error while validation of mb and wo amount" + e);
        }

        if (actionName.equalsIgnoreCase(MBHeader.Actions.SUBMIT_FOR_APPROVAL.toString())
                && mbHeader.getMbDetails().isEmpty()) {
            setApprovedQtyAndPrevCumlVal();
            throw new ValidationException(Arrays.asList(new ValidationError("measurementbook.item.mandatory",
                    "measurementbook.item.mandatory")));
        }

        /*
         * if(!actionName.equalsIgnoreCase(MBHeader.Actions.APPROVAL.toString()) &&
         * workOrderService.isMBInApprovalPendingForWO(workorderNo)) throw new ValidationException(Arrays.asList(new
         * ValidationError("measurementbook.approvalpending", "measurementbook.approvalpending")));
         */

        // if(measurementBookService.approvalLimitCrossed(mbHeader)){
        if (mbHeader.getEgwStatus() == null
                || WorksConstants.REJECTED.equalsIgnoreCase(mbHeader.getEgwStatus().getCode())
                || NEW.equalsIgnoreCase(mbHeader.getEgwStatus().getCode()) || canUserModify())
            for (final MBDetails details : mbHeader.getMbDetails()) {
                final Boolean limitStatus = measurementBookService.approvalLimitCrossed(details);
                if (limitStatus == null) {
                    final Double percentage = worksService.getConfigval() + 100;
                    setApprovedQtyAndPrevCumlVal();
                    if (isRCEstimate.equalsIgnoreCase(YES))
                        throw new ValidationException(Arrays.asList(new ValidationError(
                                "measurementbook.currMBEntry.quantityFactor.rcEstimate.complete.error", getText(
                                        "measurementbook.currMBEntry.quantityFactor.rcEstimate.complete.error",
                                        new String[] { percentage.toString() }))));
                    else
                        throw new ValidationException(Arrays.asList(new ValidationError(
                                "measurementbook.currMBEntry.quantityFactor.complete.error", getText(
                                        "measurementbook.currMBEntry.quantityFactor.complete.error",
                                        new String[] { percentage.toString() }))));
                } else if (limitStatus) {
                    if (!StringUtils.isNotBlank(details.getOrderNumber())) {
                        setApprovedQtyAndPrevCumlVal();
                        throw new ValidationException(Arrays.asList(new ValidationError(
                                "measurementbook.currMBEntry.enter.order.no",
                                getText("measurementbook.currMBEntry.enter.order.no"))));
                    }
                    if (details.getMbdetailsDate() == null) {
                        setApprovedQtyAndPrevCumlVal();
                        throw new ValidationException(Arrays.asList(new ValidationError(
                                "measurementbook.currMBEntry.enter.order.dt",
                                getText("measurementbook.currMBEntry.enter.order.dt"))));
                    } else if (details.getMbdetailsDate() != null
                            && DateConversionUtil.isBeforeByDate(new Date(), details.getMbdetailsDate())) {
                        setApprovedQtyAndPrevCumlVal();
                        throw new ValidationException(Arrays.asList(new ValidationError(
                                "measurementbook.currMBEntry.order.date.error",
                                getText("measurementbook.currMBEntry.order.date.error"))));
                    }
                }
            }

        // }

        if (SAVE_ACTION.equals(actionName) && mbHeader.getEgwStatus() == null)
            mbHeader.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("MBHeader", "NEW"));

        mbHeader = measurementBookService.persist(mbHeader);
        if (!actionName.isEmpty())
            mbHeader = workflowService.transition(actionName, mbHeader, approverComments);
        if (mbHeader.getEgwStatus() == null
                || WorksConstants.REJECTED.equalsIgnoreCase(mbHeader.getEgwStatus().getCode())
                || NEW.equalsIgnoreCase(mbHeader.getEgwStatus().getCode()) || canUserModify())
            mbHeader = calculateMBdetails(mbHeader, true);
        if (mbHeader.getEgwStatus() != null
                && mbHeader.getEgwStatus().getCode() != null
                && MBHeader.MeasurementBookStatus.APPROVED.toString().equalsIgnoreCase(
                        mbHeader.getEgwStatus().getCode()))
            messageKey = "measurementbook.approved";
        else
            messageKey = "measurementbook.save.success";
        addActionMessage(getText(messageKey, messageKey));
        getDesignation(mbHeader);

        if (SAVE_ACTION.equals(actionName))
            sourcepage = "inbox";

        return SAVE_ACTION.equals(actionName) ? EDIT : SUCCESS;

    }

    private void validateMBAmount(final WorkOrderEstimate workOrderEstimate) {
        double negoPerc = 0;
        String tenderType = "";
        BigDecimal totalMBAmount = BigDecimal.ZERO;
        BigDecimal currMBTotal = BigDecimal.ZERO;
        BigDecimal allMBsTotal = BigDecimal.ZERO;
        Double woEstimateAmount = 0D;
        Double totalWOAmount = 0D;
        TenderEstimate tenderEstimate;
        final Object[] obj = (Object[]) persistenceService
                .find("select tr.percNegotiatedAmountRate,tr.tenderEstimate,tr.tenderNegotiatedValue from TenderResponse tr where tr.egwStatus.code = 'APPROVED' and tr.negotiationNumber = ? ",
                        workOrderEstimate.getWorkOrder().getNegotiationNumber());

        tenderEstimate = (TenderEstimate) obj[1];
        tenderType = tenderEstimate.getTenderType();
        if (StringUtils.isNotBlank(tenderType) && tenderType.equalsIgnoreCase(WorksConstants.PERC_TENDER))
            negoPerc = (Double) obj[0];
        else {
            final double negotiationValue = (Double) obj[2];
            negoPerc = negotiationValue / tenderEstimate.getWorksPackage().getTotalAmount();
        }

        totalMBAmount = measurementBookService.getTotalMBAmountForPrevMBs(workOrderEstimate, negoPerc, tenderType);
        currMBTotal = getAmountsForCurrentMB(mbHeader.getMbDetails(), negoPerc, tenderType);
        allMBsTotal = totalMBAmount.add(currMBTotal);

        final Double approvedRevisionWOAmount = (Double) persistenceService
                .find(" select sum(woe.workOrder.workOrderAmount) from WorkOrderEstimate woe where woe.workOrder.parent is not null and woe.workOrder.egwStatus.code='APPROVED' and woe.estimate.parent.id=? ",
                        estimateId);
        if (StringUtils.isNotBlank(tenderType) && tenderType.equalsIgnoreCase(WorksConstants.PERC_TENDER)) {
            final Double estimateAmt = (Double) persistenceService
                    .find("select woe.estimate.workValue.value from WorkOrderEstimate woe where woe.workOrder.parent is null and woe.workOrder.egwStatus.code='APPROVED' and woe.estimate.id=? ",
                            estimateId);
            woEstimateAmount = estimateAmt + estimateAmt * negoPerc / 100;
        } else
            for (final WorkOrderActivity woa : workOrderEstimate.getWorkOrderActivities())
                woEstimateAmount = woEstimateAmount + woa.getApprovedAmount();
        totalWOAmount = approvedRevisionWOAmount == null ? woEstimateAmount : approvedRevisionWOAmount
                + woEstimateAmount;

        if (allMBsTotal.doubleValue() > totalWOAmount) {
            setApprovedQtyAndPrevCumlVal();
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "measurementbook.workOrder.amount.exceeded", "measurementbook.workOrder.amount.exceeded")));

        }
    }

    private BigDecimal getAmountsForCurrentMB(final List<MBDetails> mbDetailsList, final double negoPerc,
            final String tenderType) {

        BigDecimal currentMBTenderedAmt = BigDecimal.ZERO;
        BigDecimal currMBAmount = BigDecimal.ZERO;
        BigDecimal tenderedMBAmount = BigDecimal.ZERO;
        BigDecimal currMBTotal = BigDecimal.ZERO;

        if (tenderType.equalsIgnoreCase(WorksConstants.PERC_TENDER)) {
            for (final MBDetails mbd : mbDetailsList)
                if (mbd.getWorkOrderActivity().getActivity().getRevisionType() == null)
                    currentMBTenderedAmt = currentMBTenderedAmt.add(BigDecimal.valueOf(mbd.getAmount()));
            currMBAmount = mbHeader.getTotalMBAmount();

            // applying percentage on tendered items
            if (currentMBTenderedAmt != null)
                tenderedMBAmount = currentMBTenderedAmt.add(currentMBTenderedAmt.multiply(BigDecimal
                        .valueOf(negoPerc / 100)));
            // adding tendered amount with the non tendered items amount, to get
            // the total mb amount
            currMBTotal = tenderedMBAmount.add(currMBAmount.subtract(currentMBTenderedAmt));
        } else
            currMBTotal = mbHeader.getTotalMBAmount();

        return currMBTotal.setScale(2, RoundingMode.HALF_UP);
    }

    protected void populateActivities() {
        for (final MBDetails mbDetails : actionMbDetailValues)
            if (mbDetails != null) {
                mbDetails.setMbHeader(mbHeader);
                if (mbDetails.getWorkOrderActivity().getActivity().getId() == null)
                    mbDetails.setWorkOrderActivity((WorkOrderActivity) getPersistenceService().find(
                            "from WorkOrderActivity where id=?", mbDetails.getWorkOrderActivity().getId()));
                mbHeader.addMbDetails(mbDetails);
            }
    }

    // workflow for reject mb
    /** reject */

    public String reject() {
        String actionName = "";
        if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null)
            actionName = parameters.get(ACTION_NAME)[0];

        if (mbHeader != null && mbHeader.getId() != null && !actionName.isEmpty()) {
            // calling workflow api
            workflowService.transition(actionName, mbHeader, approverComments);
            mbHeader = measurementBookService.persist(mbHeader);
            getDesignation(mbHeader);
        }
        messageKey = "measurementbook.reject";
        return SUCCESS;
    }

    // workflow for cancel mb

    public String cancel() {
        if (mbHeader != null && mbHeader.getEgBillregister() != null
                && mbHeader.getEgBillregister().getStatus() != null
                && !mbHeader.getEgBillregister().getStatus().getCode().equalsIgnoreCase("CANCELLED")) {
            messageKey = "measurementbook.cancel.failure";
            addActionError(getText(messageKey));
            return EDIT;
        }

        String actionName = "";
        if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null)
            actionName = parameters.get(ACTION_NAME)[0];

        if (mbHeader != null && mbHeader.getId() != null && !actionName.isEmpty()) {
            workflowService.transition(actionName, mbHeader, approverComments);
            mbHeader = measurementBookService.persist(mbHeader);
            getDesignation(mbHeader);
        }
        messageKey = "measurementbook.cancel";
        return SUCCESS;
    }

    protected void getDesignation(final MBHeader mbHeader) {
        /* start for customizing workflow message display */
        if (mbHeader.getEgwStatus() != null && !WorksConstants.NEW.equalsIgnoreCase(mbHeader.getEgwStatus().getCode())) {
            final String result = worksService.getEmpNameDesignation(mbHeader.getState().getOwnerPosition(), mbHeader
                    .getState().getCreatedDate());
            if (result != null && !"@".equalsIgnoreCase(result)) {
                final String empName = result.substring(0, result.lastIndexOf('@'));
                final String designation = result.substring(result.lastIndexOf('@') + 1, result.length());
                setDispEmployeeName(empName);
                setDispDesignation(designation);
            }
        }
        /* end */
    }

    public String verify() {
        // TODO save and show details for submit
        return VERIFY;
    }

    public String submit() {
        // TODO make status approval pending for all MBS
        return SUBMITTED;
    }

    @Override
    public Object getModel() {
        return mbHeader;
    }

    public String cancelApprovedMB() {
        final MBHeader mbHeader = measurementBookService.findById(mbId, false);
        mbHeader.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("MBHeader",
                MBHeader.MeasurementBookStatus.CANCELLED.toString()));

        final PersonalInformation prsnlInfo = employeeServiceOld.getEmpForUserId(worksService.getCurrentLoggedInUserId());
        String empName = "";
        if (prsnlInfo.getEmployeeFirstName() != null)
            empName = prsnlInfo.getEmployeeFirstName();
        if (prsnlInfo.getEmployeeLastName() != null)
            empName = empName.concat(" ").concat(prsnlInfo.getEmployeeLastName());
        if (cancelRemarks != null && StringUtils.isNotBlank(cancelRemarks)) {
        } else {
        }

        // TODO - The setter methods of variables in State.java are protected.
        // Need to alternative way to solve this issue.
        // Set the status and workflow state to cancelled
        /*
         * State oldEndState = mbHeader.getCurrentState(); Position owner = prsnlInfo.getAssignment(new Date()).getPosition();
         * oldEndState.setCreatedBy(prsnlInfo.getUserMaster()); oldEndState.setModifiedBy(prsnlInfo.getUserMaster());
         * oldEndState.setCreatedDate(new Date()); oldEndState.setModifiedDate(new Date()); oldEndState.setOwner(owner);
         * oldEndState.setValue(WorksConstants.CANCELLED_STATUS); oldEndState.setText1(cancellationText);
         * mbHeader.changeState("END", owner, null);
         */

        mbRefNo = mbHeader.getMbRefNo();
        messageKey = mbRefNo + " : " + getText("mb.cancel.success.message");
        return SUCCESS;
    }

    public void setModel(final MBHeader mbHeader) {
        this.mbHeader = mbHeader;
    }

    public List<MBDetails> getMbDetails() {
        return mbDetails;
    }

    public void setMbDetails(final List<MBDetails> mbDetails) {
        this.mbDetails = mbDetails;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(final String messageKey) {
        this.messageKey = messageKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public EmployeeView getMbPreparedByView() {
        return mbPreparedByView;
    }

    public void setMbPreparedByView(final EmployeeView mbPreparedByView) {
        this.mbPreparedByView = mbPreparedByView;
    }

    public void setMeasurementBookService(final MeasurementBookService measurementBookService) {
        this.measurementBookService = measurementBookService;
    }

    public void setActionMbDetailValues(final List<MBDetails> actionMbDetailValues) {
        this.actionMbDetailValues = actionMbDetailValues;
    }

    public List<WorkOrderActivity> getActivityList() {
        return activityList;
    }

    public List<MBDetails> getActionMbDetailValues() {
        return actionMbDetailValues;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(final String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(final String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getWorkorderNo() {
        return workorderNo;
    }

    public void setWorkorderNo(final String workorderNo) {
        this.workorderNo = workorderNo;
    }

    public void setWorkOrderService(final WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    public double getQuantityFactor() {
        return quantityFactor;
    }

    public void setQuantityFactor(final double quantityFactor) {
        this.quantityFactor = quantityFactor;
    }

    public List<MBHeader> getMbList() {
        return mbList;
    }

    public void setActivityList(final List<WorkOrderActivity> activityList) {
        this.activityList = activityList;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(final Long contractorId) {
        this.contractorId = contractorId;
    }

    public String getMbRefNo() {
        return mbRefNo;
    }

    public void setMbRefNo(final String mbRefNo) {
        this.mbRefNo = mbRefNo;
    }

    public String getMbPages() {
        return mbPages;
    }

    public void setMbPages(final String mbPages) {
        this.mbPages = mbPages;
    }

    public String getMbStatus() {
        return mbStatus;
    }

    public void setMbStatus(final String mbStatus) {
        this.mbStatus = mbStatus;
    }

    // on jan 13 th workflow related
    public List<WorkflowAction> getValidActions() {
        return workflowService.getValidActions(mbHeader);
    }

    public void setMeasurementBookWorkflowService(final WorkflowService<MBHeader> workflow) {
        workflowService = workflow;
    }

    public MeasurementBookWFService getMeasurementBookWFService() {
        return measurementBookWFService;
    }

    public void setMeasurementBookWFService(final MeasurementBookWFService measurementBookWFService) {
        this.measurementBookWFService = measurementBookWFService;
    }

    public String getSourcepage() {
        return sourcepage;
    }

    public void setSourcepage(final String sourcepage) {
        this.sourcepage = sourcepage;
    }

    public String getDispEmployeeName() {
        return dispEmployeeName;
    }

    public void setDispEmployeeName(final String dispEmployeeName) {
        this.dispEmployeeName = dispEmployeeName;
    }

    public String getDispDesignation() {
        return dispDesignation;
    }

    public void setDispDesignation(final String dispDesignation) {
        this.dispDesignation = dispDesignation;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Long departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDesignationId() {
        return designationId;
    }

    public void setDesignationId(final Integer designationId) {
        this.designationId = designationId;
    }

    public String getApproverComments() {
        return approverComments;
    }

    public void setApproverComments(final String approverComments) {
        this.approverComments = approverComments;
    }

    public Integer getApproverUserId() {
        return approverUserId;
    }

    public void setApproverUserId(final Integer approverUserId) {
        this.approverUserId = approverUserId;
    }

    public String getMborderNumberRequired() {
        mborderNumberRequired = worksService.getWorksConfigValue("ORDER_NUMBER_REQUIRED");
        return mborderNumberRequired;
    }

    public String getWorkOrderEstimateRequired() {
        return worksService.getWorksConfigValue("WORKORDER_ESTIMATE_REQUIRED");
    }

    public String getMBWorkflowModifyDesignation() {
        return worksService.getWorksConfigValue("MB_WORKFLOW_MODIFY_DESIG");
    }

    public void setMborderNumberRequired(final String mborderNumberRequired) {
        this.mborderNumberRequired = mborderNumberRequired;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(final Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(final String workName) {
        this.workName = workName;
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

    public List<WorkOrderEstimate> getWorkOrderEstimateList() {
        return workOrderEstimateList;
    }

    public void setWorkOrderEstimateList(final List<WorkOrderEstimate> workOrderEstimateList) {
        this.workOrderEstimateList = workOrderEstimateList;
    }

    public void setPersonalInformationService(final PersonalInformationService personalInformationService) {
        this.personalInformationService = personalInformationService;
    }

    // end workflow related

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(final String projectCode) {
        this.projectCode = projectCode;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public String getActivitySearchMode() {
        return activitySearchMode;
    }

    public void setActivitySearchMode(final String activitySearchMode) {
        this.activitySearchMode = activitySearchMode;
    }

    public boolean getIsLegacyMB() {
        return isLegacyMB;
    }

    public void setIsLegacyMB(final boolean isLegacyMB) {
        this.isLegacyMB = isLegacyMB;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(final String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Long getMbId() {
        return mbId;
    }

    public void setMbId(final Long mbId) {
        this.mbId = mbId;
    }

    public String getEstimateNo() {
        return estimateNo;
    }

    public void setEstimateNo(final String estimateNo) {
        this.estimateNo = estimateNo;
    }

    public Integer getExecDeptid() {
        return execDeptid;
    }

    public void setExecDeptid(final Integer execDeptid) {
        this.execDeptid = execDeptid;
    }

    public String getCancelRemarks() {
        return cancelRemarks;
    }

    public void setCancelRemarks(final String cancelRemarks) {
        this.cancelRemarks = cancelRemarks;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public EgovPaginatedList getPagedResults() {
        return pagedResults;
    }

    public void setPage(final Integer page) {
        this.page = page;
    }

    public void setPageSize(final Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPagedResults(final EgovPaginatedList pagedResults) {
        this.pagedResults = pagedResults;
    }

    public void setEisService(final EisUtilService eisService) {
    }

    public Integer getDefaultPreparedById() {
        return defaultPreparedById;
    }

    public void setDefaultPreparedById(final Integer defaultPreparedById) {
        this.defaultPreparedById = defaultPreparedById;
    }

    public String getDefaultDesgination() {
        return defaultDesgination;
    }

    public String getIsRCEstimate() {
        return isRCEstimate;
    }

    public void setIsRCEstimate(final String isRCEstimate) {
        this.isRCEstimate = isRCEstimate;
    }

    public Date getWorkCommencedDate() {
        return workCommencedDate;
    }

    public void setWorkCommencedDate(final Date workCommencedDate) {
        this.workCommencedDate = workCommencedDate;
    }

    public Date getLatestMBDate() {
        return latestMBDate;
    }

    public void setLatestMBDate(final Date latestMBDate) {
        this.latestMBDate = latestMBDate;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(final Long woId) {
        this.woId = woId;
    }

}