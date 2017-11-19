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
package org.egov.works.web.actions.tender;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.tender.NegotiationNumberGenerator;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.models.tender.TenderEstimate;
import org.egov.works.models.tender.TenderHeader;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.TenderResponseActivity;
import org.egov.works.models.tender.TenderResponseContractors;
import org.egov.works.models.tender.TenderResponseQuotes;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.TenderResponseService;
import org.egov.works.services.WorksPackageService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.DateConversionUtil;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Result(name = TenderNegotiationAction.NEW, location = "tenderNegotiation-new.jsp")
public class TenderNegotiationAction extends SearchFormAction {

    private static final long serialVersionUID = 1705982410378688424L;
    private static final Logger logger = Logger.getLogger(TenderNegotiationAction.class);
    private TenderResponseService tenderResponseService;
    private PersistenceService<TenderHeader, Long> tenderHeaderService;
    private AbstractEstimateService abstractEstimateService;
    private NegotiationNumberGenerator negotiationNumberGenerator;
    private TenderResponse tenderResponse = new TenderResponse();
    private AbstractEstimate abstractEstimate;
    private WorksPackage worksPackage;
    private TenderHeader tenderHeader = new TenderHeader();
    private TenderEstimate tenderEstimate = new TenderEstimate();
    private WorkflowService<TenderResponse> workflowService;
    private WorksPackageService workspackageService;
    private String messageKey;
    private String designation;
    private Long estimateId;
    private Long worksPackageId;
    private String tenderType;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private UserService userService;
    private static final String PREPARED_BY_LIST = "preparedByList";
    private static final String DEPARTMENT_LIST = "departmentList";
    private List<TenderResponseActivity> actionTenderResponseActivities = new LinkedList<TenderResponseActivity>();
    private List<TenderResponseContractors> actionTenderResponseContractors = new LinkedList<TenderResponseContractors>();
    @Autowired
    private DepartmentService departmentService;
    private static final String OBJECT_TYPE = "TenderResponseContractors";
    private PersonalInformationService personalInformationService;
    private Long contractorId;

    private String negotiationNumber;
    private String negoNumber;
    private Date negotiationDate;

    private String wpYear;
    private Long deptId;
    private Integer departmentId;
    private String estimateNumber;
    private String projectCode;
    private String tenderSource;
    private String mode;
    private String status;
    private WorksService worksService;
    private String employeeName;
    private String designationNegotiation;
    private static final String SAVE_ACTION = "save";
    private static final String SOURCE_INBOX = "inbox";
    private static final String VALIDATION_PREPAREDBY = "tenderResponse.negotiationPreparedBy";
    private static final String VALIDATION_PREPAREDBY_ERROR = "tenderResponse.negotiationPreparedBy.null";
    private static final String ACTION_NAME = "actionName";
    private String sourcepage = "";
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private EmployeeServiceOld employeeServiceOld;

    private String nsActionName;
    private String option;
    private Integer negotiationStatusId;
    private Integer negotiationStatusApprovedById;
    private Long tenderRespId;
    private Date approvedDateHidden;
    public final static String APPROVED = "APPROVED";
    public final static String REJECTED = "REJECTED";
    public final static String NEWNs = "NEW";
    private String errorType = "";
    private WorkOrder workOrder = new WorkOrder();
    public final static String TENDERRESPONSE_CONTRACTOR = "tenderResponse.contractor";
    public final static String TENDERRESPONSE_CONTRACTOR_NULL = "tenderResponse.contractor.null";
    private Integer statusIdValue;
    private String currentApproverName;
    private String currentStatus;
    private Date currentApprovedDate;
    private String currentApprovedDateStr = "";
    private static final String SOURCE_SEARCH = "search";
    private final DecimalFormat formatter = new DecimalFormat("0.00");
    private String createdBySelection = "no";
    private String editableDate = "yes";
    private Double emdAmountDeposited;
    private Double securityDeposit;
    private Double labourWelfareFund;
    private Integer workOrderInchargeId;
    private String setStatus;
    private String wpNumber;
    private Date fromDate;
    private Date toDate;
    private String tenderCretedBy;
    private String tenderFileNumber;
    private Double sorPerDiff;
    private OfflineStatus setStatusObj = new OfflineStatus();
    private final static String TENDER_ACCEPTANCE_NOTE = "acceptanceNote";
    private PersistenceService<Contractor, Long> contractorService;
    private final static String CONTRACTOR_LIST = "contractorList";
    private List<String> tenderTypeList;
    private PersistenceService<OfflineStatus, Long> worksStatusService;
    private static final String STATUS_OBJECTID = "getStatusDateByObjectId_Type_Desc";
    private static final String SEARCH_NEGOTIATION_FOR_WO = "searchNegotiationForWO";
    private String cancellationReason;
    private String cancelRemarks;
    private String loggedInUserEmployeeCode;
    private EisUtilService eisService;
    private Map<String, String> tenderInvitationTypeMap = new LinkedHashMap<String, String>();

    public TenderResponse getTenderResponse() {
        return tenderResponse;
    }

    public Double getEmdAmountDeposited() {
        if (emdAmountDeposited == null)
            emdAmountDeposited = new Double("0.0");

        return emdAmountDeposited;
    }

    public void setEmdAmountDeposited(final Double emdAmountDeposited) {
        this.emdAmountDeposited = emdAmountDeposited;
    }

    public Double getSecurityDeposit() {
        if (securityDeposit == null)
            try {
                securityDeposit = new Double(worksService.getWorksConfigValue("SECURITY_DEPOSIT_MULTIPLIER"))
                        .doubleValue() / 100 * abstractEstimate.getWorkValueIncludingTaxes().getValue();
                securityDeposit = new Double(formatter.format(securityDeposit));
            } catch (final Exception e) {
                securityDeposit = 0.0d;
                logger.info("exception " + e);
            }
        return securityDeposit;
    }

    public void setSecurityDeposit(final Double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    protected PersonalInformation getEmployee() {
        if (tenderResponse.getNegotiationPreparedBy() == null)
            return employeeServiceOld.getEmpForUserId(worksService.getCurrentLoggedInUserId());
        else
            return tenderResponse.getNegotiationPreparedBy();
    }

    public Double getLabourWelfareFund() {
        if (labourWelfareFund == null)
            try {
                labourWelfareFund = new Double(worksService.getWorksConfigValue("LWF_MULTIPLIER")).doubleValue() / 100
                        * abstractEstimate.getWorkValueIncludingTaxes().getValue();
                labourWelfareFund = new Double(formatter.format(labourWelfareFund));
            } catch (final Exception e) {
                labourWelfareFund = 0.0d;
                logger.info("exception " + e);
            }
        return labourWelfareFund;
    }

    public void setLabourWelfareFund(final Double labourWelfareFund) {
        this.labourWelfareFund = labourWelfareFund;
    }

    public TenderNegotiationAction() {
        addRelatedEntity("contractor", Contractor.class);
        addRelatedEntity("negotiationPreparedBy", PersonalInformation.class);
    }

    @SkipValidation
    public String edit() {

        if (SOURCE_INBOX.equalsIgnoreCase(sourcepage)) {
            if (userService != null) {
                final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
                final boolean isValidUser = worksService.validateWorkflowForUser(tenderResponse, user);
                if (isValidUser)
                    throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
            }
        } else if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";

        if (SOURCE_INBOX.equals(getSourcepage())) {
            if (tenderResponse.getTenderEstimate().getAbstractEstimate() != null)
                abstractEstimate = abstractEstimateService.findById(tenderResponse.getTenderEstimate()
                        .getAbstractEstimate().getId(), false);
            if (tenderResponse.getTenderEstimate().getWorksPackage() != null)
                worksPackage = workspackageService.findById(tenderResponse.getTenderEstimate().getWorksPackage()
                        .getId(), false);
            tenderHeader = tenderHeaderService.findById(tenderResponse.getTenderEstimate().getTenderHeader().getId(),
                    false);
            tenderType = tenderResponse.getTenderEstimate().getTenderType();
        }
        if (getAssignment(tenderResponse.getNegotiationPreparedBy()) != null) {
            setDesignationNegotiation(getAssignment(tenderResponse.getNegotiationPreparedBy()).getDesignation()
                    .getName());
            setDeptId(getAssignment(tenderResponse.getNegotiationPreparedBy()).getDepartment().getId());
        }
        final List<Contractor> contractorList = new ArrayList<Contractor>();
        for (final TenderResponseContractors tenderResponseContractors : tenderResponse.getTenderResponseContractors())
            contractorList.add(tenderResponseContractors.getContractor());
        addDropdownData(CONTRACTOR_LIST, contractorList);
        return EDIT;
    }

    /*
     * (non-Javadoc)
     * @ return String
     */
    @ValidationErrorPage(value = "searchNegotiationPage")
    public String searchNegotiation() {
        return "searchNegotiationPage";
    }

    @Override
    public Object getModel() {
        return tenderResponse;
    }

    @Override
    public void prepare() {
        final AjaxTenderNegotiationAction ajaxTenderNegotiationAction = new AjaxTenderNegotiationAction();
        ajaxTenderNegotiationAction.setPersistenceService(getPersistenceService());
        ajaxTenderNegotiationAction.setAssignmentService(assignmentService);
        ajaxTenderNegotiationAction.setPersonalInformationService(personalInformationService);
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        ajaxEstimateAction.setAssignmentService(assignmentService);
        ajaxEstimateAction.setAbstractEstimateService(abstractEstimateService);
        ajaxEstimateAction.setEisService(eisService);
        tenderResponseService.setPersonalInformationService(personalInformationService);
        if (StringUtils.isNotBlank(getPastDate()))
            setEditableDate(getPastDate());

        if (negoNumber != null && StringUtils.isNotBlank(negoNumber)) {
            tenderResponse = tenderResponseService.find(
                    "from TenderResponse tr where tr.negotiationNumber =? and tr.egwStatus.code = 'APPROVED'",
                    negoNumber);
            id = tenderResponse.getId();
            tenderHeader = tenderResponse.getTenderEstimate().getTenderHeader();
            worksPackageId = tenderResponse.getTenderEstimate().getWorksPackage().getId();
        }
        if (estimateId != null)
            abstractEstimate = abstractEstimateService.findById(estimateId, false);
        if (worksPackageId != null) {
            worksPackage = workspackageService.findById(worksPackageId, false);
            final String status = worksService.getWorksConfigValue("WORKSPACKAGE_STATUS");
            String objType = getTenderSource();
            if (objType != null && objType.equals("package"))
                objType = "WorksPackage";
            final Long objId = getWorksPackageId();
            if (StringUtils.isNotBlank(status)) {
                setStatusObj = (OfflineStatus) getPersistenceService().findByNamedQuery(
                        "getStatusDateByObjectId_Type_Desc", objId, objType, status);
                if (setStatusObj != null)
                    tenderHeader.setTenderDate(setStatusObj.getStatusDate());
            }
        }

        if (id != null) {
            tenderResponse = tenderResponseService.findById(id, false);
            tenderHeader = tenderHeaderService.findById(tenderHeader.getId(), false);
            if (mode != null && (mode.equalsIgnoreCase("view") || mode.equalsIgnoreCase(SOURCE_SEARCH)))
                tenderType = tenderResponse.getTenderEstimate().getTenderType();

            if (tenderResponse.getTenderEstimate().getAbstractEstimate() != null)
                setTenderSource("estimate");
            if (tenderResponse.getTenderEstimate().getWorksPackage() != null)
                setTenderSource("package");
        }
        final Assignment latestAssignment = abstractEstimateService.getLatestAssignmentForCurrentLoginUser();
        if (latestAssignment != null) {
            tenderResponse.setWorkflowDepartmentId(abstractEstimateService.getLatestAssignmentForCurrentLoginUser()
                    .getDepartment().getId());
            if (tenderResponse.getNegotiationPreparedBy() == null) {
                setDesignationNegotiation(latestAssignment.getDesignation().getName());
                loggedInUserEmployeeCode = latestAssignment.getEmployee().getCode();
            } else
                loggedInUserEmployeeCode = tenderResponse.getNegotiationPreparedBy().getEmployeeCode();
        }
        if (StringUtils.isNotBlank(getCreatedBy()) && "yes".equalsIgnoreCase(getCreatedBy())) {
            setCreatedBySelection(getCreatedBy());
            addDropdownData(DEPARTMENT_LIST, departmentService.getAllDepartments());
            populatePreparedByList(ajaxEstimateAction, deptId != null);
        } else {
            if (id != null)
                worksPackage = tenderResponse.getTenderEstimate().getWorksPackage();
            if (worksPackage != null) {
                addDropdownData(DEPARTMENT_LIST, Arrays.asList(worksPackage.getDepartment()));
                populatePreparedByList(ajaxEstimateAction, worksPackage.getDepartment().getId() != null);
                tenderResponse.setNegotiationPreparedBy(getEmployee());
            }
        }
        final List departmentList = departmentService.getAllDepartments();
        if (sourcepage != null && sourcepage.equalsIgnoreCase(SEARCH_NEGOTIATION_FOR_WO))
            addDropdownData("searchDepartmentList", worksService.getAllDeptmentsForLoggedInUser());
        else
            addDropdownData("searchDepartmentList", departmentList);
        super.prepare();
        setupDropdownDataExcluding("contractor", "negotiationPreparedBy", "");
        addDropdownData("executingDepartmentList", departmentList);
        tenderTypeList = worksService.getTendertypeList();
        addDropdownData("tenderTypeList", tenderTypeList);

        if (StringUtils.isNotBlank(worksService.getWorksConfigValue("SOR_PERCENTAGE_DIFF")))
            sorPerDiff = Double.valueOf(worksService.getWorksConfigValue("SOR_PERCENTAGE_DIFF"));

        setTenderCretedBy(worksService.getWorksConfigValue("ESTIMATE_OR_WP_SEARCH_REQ"));
        final List<Contractor> contractorList = new ArrayList<Contractor>();
        for (final TenderResponseContractors tenderResponseContractors : tenderResponseService
                .getActionTenderResponseContractorsList(actionTenderResponseContractors))
            contractorList.add(tenderResponseContractors.getContractor());
        addDropdownData(CONTRACTOR_LIST, contractorList);
    }

    protected void populatePreparedByList(final AjaxEstimateAction ajaxEstimateAction, final boolean departID) {
        if (departID) {
            ajaxEstimateAction.setExecutingDepartment(worksPackage.getDepartment().getId());
            if (StringUtils.isNotBlank(loggedInUserEmployeeCode))
                ajaxEstimateAction.setEmployeeCode(loggedInUserEmployeeCode);
            ajaxEstimateAction.usersInExecutingDepartment();
            addDropdownData(PREPARED_BY_LIST, ajaxEstimateAction.getUsersInExecutingDepartment());
        } else
            addDropdownData(PREPARED_BY_LIST, Collections.EMPTY_LIST);
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    @SkipValidation
    @Action(value = "/tender/tenderNegotiation-newform")
    public String newform() {
        setDeptId(worksPackage.getDepartment().getId());
        return NEW;
    }

    protected Assignment getAssignment(final PersonalInformation pi) {
        if (tenderResponse.getNegotiationPreparedBy() == null)
            return employeeServiceOld.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
        else
            return employeeServiceOld.getAssignmentByEmpAndDate(new Date(), tenderResponse.getNegotiationPreparedBy()
                    .getIdPersonalInformation());
    }

    public String getCreatedBy() {
        return worksService.getWorksConfigValue("TENDER_NEGOTIATION_CREATED_BY_SELECTION");
    }

    public String getPastDate() {
        return worksService.getWorksConfigValue("TENDER_NEGOTIATION_DATE_SELECTION");
    }

    /**
     * Method to check for valid Tender date. Tender date should be greater than Admin approval date and less than current date
     *
     * @return boolean
     */

    private boolean validTenderDate() {

        if (abstractEstimate != null)
            return abstractEstimate != null
                    && tenderHeader != null
                    && tenderHeader.getTenderDate() != null
                    && abstractEstimate.getCurrentState().getCreatedDate() != null
                    && abstractEstimate.getCurrentState().getValue().equalsIgnoreCase(WorksConstants.END)
                    && !DateConversionUtil.isBeforeByDate(tenderHeader.getTenderDate(), abstractEstimate
                            .getCurrentState().getCreatedDate())
                    && !DateConversionUtil.isBeforeByDate(new Date(), tenderHeader.getTenderDate());
        if (worksPackage != null)
            return worksPackage != null
                    && tenderHeader != null
                    && tenderHeader.getTenderDate() != null
                    && worksPackage.getCurrentState().getCreatedDate() != null
                    && worksPackage.getEgwStatus().getCode()
                            .equalsIgnoreCase(WorksPackage.WorkPacakgeStatus.APPROVED.toString())
                    && !DateConversionUtil.isBeforeByDate(tenderHeader.getTenderDate(), worksPackage.getWpDate())
                    && !DateConversionUtil.isBeforeByDate(new Date(), tenderHeader.getTenderDate());
        return Boolean.TRUE;
    }

    public String save() {
        if (tenderResponse != null
                && (tenderResponse.getEgwStatus() == null
                        || NEW.equalsIgnoreCase(tenderResponse.getEgwStatus().getCode()) || REJECTED
                                .equalsIgnoreCase(tenderResponse.getEgwStatus().getCode())))
            tenderResponse.getTenderResponseContractors().clear();
        actionTenderResponseContractors = (List) tenderResponseService
                .getActionTenderResponseContractorsList(actionTenderResponseContractors);

        if (tenderResponse != null
                && !actionTenderResponseContractors.isEmpty()
                && actionTenderResponseContractors.get(0) != null
                && actionTenderResponseContractors.get(0).getContractor() != null
                && (actionTenderResponseContractors.get(0).getContractor().getId() == null
                        || actionTenderResponseContractors.get(0).getContractor().getId() == 0 || actionTenderResponseContractors
                                .get(0).getContractor().getId() == -1))
            throw new ValidationException(Arrays.asList(new ValidationError(TENDERRESPONSE_CONTRACTOR,
                    TENDERRESPONSE_CONTRACTOR_NULL)));
        else if (tenderResponse != null && !actionTenderResponseContractors.isEmpty()
                && actionTenderResponseContractors.get(0) != null
                && actionTenderResponseContractors.get(0).getContractor() == null)
            throw new ValidationException(Arrays.asList(new ValidationError(TENDERRESPONSE_CONTRACTOR,
                    TENDERRESPONSE_CONTRACTOR_NULL)));
        if (!validTenderDate()) {
            if (abstractEstimate != null)
                throw new ValidationException(Arrays.asList(new ValidationError("tenderHeader.tenderDate.invalid",
                        "tenderHeader.tenderDate.invalid")));

            if (worksPackage != null)
                throw new ValidationException(
                        Arrays.asList(new ValidationError("tenderHeader.tenderDate.workspackage.invalid",
                                "tenderHeader.tenderDate.workspackage.invalid")));
        }
        if (validTenderNo())
            throw new ValidationException(Arrays.asList(new ValidationError("tenderHeader.tenderNo.isunique",
                    "tenderHeader.tenderNo.isunique")));

        if (tenderResponse != null
                && tenderResponse.getNegotiationPreparedBy() != null
                && (tenderResponse.getNegotiationPreparedBy().getIdPersonalInformation() == null
                        || tenderResponse.getNegotiationPreparedBy().getIdPersonalInformation() == 0 || tenderResponse
                                .getNegotiationPreparedBy().getIdPersonalInformation() == -1)) {
            logger.info("2--- inside save and validation err");
            throw new ValidationException(Arrays.asList(new ValidationError(VALIDATION_PREPAREDBY,
                    VALIDATION_PREPAREDBY_ERROR)));
        } else if (tenderResponse != null && tenderResponse.getNegotiationPreparedBy() == null) {
            logger.info("3--- inside save and validation err");
            throw new ValidationException(Arrays.asList(new ValidationError(VALIDATION_PREPAREDBY,
                    VALIDATION_PREPAREDBY_ERROR)));
        }
        CFinancialYear financialYear = null;
        if (tenderResponse != null) {
            financialYear = abstractEstimateService.getCurrentFinancialYear(tenderResponse.getNegotiationDate());
            if (tenderResponse.getEgwStatus() == null
                    || REJECTED.equalsIgnoreCase(tenderResponse.getEgwStatus().getCode())
                    || NEW.equalsIgnoreCase(tenderResponse.getEgwStatus().getCode()))
                tenderResponse.getTenderResponseActivities().clear();
        }

        if (tenderResponse.getEgwStatus() == null || REJECTED.equalsIgnoreCase(tenderResponse.getEgwStatus().getCode())
                || NEW.equalsIgnoreCase(tenderResponse.getEgwStatus().getCode())) {
            populateTenderResponseContractors();
            populateTenderResponseActivities();
        }
        final List<Contractor> contractorList = new ArrayList<Contractor>();
        for (final TenderResponseContractors tenderResponseContractors : tenderResponse.getTenderResponseContractors())
            contractorList.add(tenderResponseContractors.getContractor());
        addDropdownData(CONTRACTOR_LIST, contractorList);

        saveTender();
        tenderResponse.setTenderEstimate(tenderHeader.getTenderEstimates().get(0));
        String actionName = "";
        if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null)
            actionName = parameters.get(ACTION_NAME)[0];
        try {
            setNegotiationNumber(tenderResponse, financialYear);
            tenderHeaderService.persist(tenderHeader);
            if (SAVE_ACTION.equals(parameters.get(ACTION_NAME)[0]) && tenderResponse.getEgwStatus() == null)
                tenderResponse.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("TenderResponse", "NEW"));
            tenderResponse = tenderResponseService.persist(tenderResponse);
        }

        catch (final ValidationException sequenceException) {
            final List<ValidationError> errorList = sequenceException.getErrors();
            for (final ValidationError error : errorList)
                if (error.getMessage().contains("DatabaseSequenceFirstTimeException")) {
                    prepare();
                    if (tenderResponse != null)
                        tenderResponse.getTenderResponseContractors().clear();
                    actionTenderResponseContractors = (List) tenderResponseService
                            .getActionTenderResponseContractorsList(actionTenderResponseContractors);
                    if (tenderResponse != null) {
                        tenderResponse.getTenderResponseActivities().clear();
                        financialYear = abstractEstimateService.getCurrentFinancialYear(tenderResponse
                                .getNegotiationDate());
                    }
                    populateTenderResponseContractors();
                    populateTenderResponseActivities();
                    throw new ValidationException(Arrays.asList(new ValidationError("error", error.getMessage())));
                }
        }

        // tenderResponse = tenderResponseService.persist(tenderResponse);

        // calling workflow api
        if (actionName != null)
            tenderResponse = workflowService.transition(actionName, tenderResponse,
                    tenderResponse.getWorkflowapproverComments());

        tenderResponse = tenderResponseService.persist(tenderResponse);

        if (tenderResponse.getEgwStatus() != null && APPROVED.equalsIgnoreCase(tenderResponse.getEgwStatus().getCode()))
            messageKey = "tenderResponse.approved";
        else
            messageKey = "tenderNegotiation.save.success";

        addActionMessage(getText(messageKey, messageKey));
        getDesignation(tenderResponse);
        if (SAVE_ACTION.equals(actionName))
            sourcepage = "inbox";

        return SAVE_ACTION.equals(actionName) ? EDIT : SUCCESS;
    }

    public String cancel() {
        actionTenderResponseContractors = (List) tenderResponseService
                .getActionTenderResponseContractorsList(actionTenderResponseContractors);
        if (tenderResponse != null
                && !actionTenderResponseContractors.isEmpty()
                && actionTenderResponseContractors.get(0) != null
                && actionTenderResponseContractors.get(0).getContractor() != null
                && (actionTenderResponseContractors.get(0).getContractor().getId() == null
                        || actionTenderResponseContractors.get(0).getContractor().getId() == 0 || actionTenderResponseContractors
                                .get(0).getContractor().getId() == -1))
            throw new ValidationException(Arrays.asList(new ValidationError(TENDERRESPONSE_CONTRACTOR,
                    TENDERRESPONSE_CONTRACTOR_NULL)));
        else if (tenderResponse != null && !actionTenderResponseContractors.isEmpty()
                && actionTenderResponseContractors.get(0) != null
                && actionTenderResponseContractors.get(0).getContractor() == null)
            throw new ValidationException(Arrays.asList(new ValidationError(TENDERRESPONSE_CONTRACTOR,
                    TENDERRESPONSE_CONTRACTOR_NULL)));
        if (tenderResponse != null
                && tenderResponse.getNegotiationPreparedBy() != null
                && (tenderResponse.getNegotiationPreparedBy().getIdPersonalInformation() == null
                        || tenderResponse.getNegotiationPreparedBy().getIdPersonalInformation() == 0 || tenderResponse
                                .getNegotiationPreparedBy().getIdPersonalInformation() == -1))
            throw new ValidationException(Arrays.asList(new ValidationError(VALIDATION_PREPAREDBY,
                    VALIDATION_PREPAREDBY_ERROR)));
        else if (tenderResponse != null && tenderResponse.getNegotiationPreparedBy() == null)
            throw new ValidationException(Arrays.asList(new ValidationError(VALIDATION_PREPAREDBY,
                    VALIDATION_PREPAREDBY_ERROR)));

        saveTender();
        tenderResponse.setTenderEstimate(tenderHeader.getTenderEstimates().get(0));

        if (tenderResponse.getId() != null) {
            workflowService.transition(TenderResponse.Actions.CANCEL.toString(), tenderResponse, "");
            tenderResponse = tenderResponseService.persist(tenderResponse);
        }
        messageKey = "tenderResponse.cancel";
        getDesignation(tenderResponse);
        return SUCCESS;
    }

    // start for customizing workflow message display
    private void getDesignation(final TenderResponse tenderResponse) {
        if (tenderResponse.getEgwStatus() != null && !"NEW".equalsIgnoreCase(tenderResponse.getEgwStatus().getCode())) {
            final String result = worksService.getEmpNameDesignation(tenderResponse.getState().getOwnerPosition(),
                    tenderResponse.getState().getCreatedDate());
            if (result != null && !"@".equalsIgnoreCase(result)) {
                final String empName = result.substring(0, result.lastIndexOf('@'));
                final String designation = result.substring(result.lastIndexOf('@') + 1, result.length());
                setEmployeeName(empName);
                setDesignation(designation);
            }
        }
    }

    // end

    private void saveTender() {
        populateTenderEstimate();

    }

    private void populateTenderEstimate() {
        if (id != null)
            tenderResponse.getTenderEstimate().getTenderHeader().getTenderEstimates().clear();
        if (abstractEstimate != null)
            tenderEstimate.setAbstractEstimate(abstractEstimate);
        if (worksPackage != null)
            tenderEstimate.setWorksPackage(worksPackage);
        if (tenderType != null && !tenderType.equals("-1"))
            tenderEstimate.setTenderType(tenderType);
        if (id == null)
            tenderEstimate.setTenderHeader(tenderHeader);
        tenderHeader.addTenderEstimate(tenderEstimate);
    }

    private void populateTenderResponseContractors() {
        for (final TenderResponseContractors tenderResponseContractors : actionTenderResponseContractors)
            if (tenderResponseContractors != null)
                tenderResponse.addTenderResponseContractors(tenderResponseContractors);
    }

    private void populateTenderResponseActivities() {
        if (abstractEstimate != null)
            addAllActivities(abstractEstimate.getActivities());
        if (worksPackage != null)
            addAllActivities(worksPackage.getAllActivities());
    }

    private void addAllActivities(final List<Activity> actList) {
        if (tenderTypeList != null && !tenderTypeList.isEmpty() && tenderType.equals(tenderTypeList.get(0)))
            for (final Activity activity : actList) {
                final TenderResponseActivity tenderResponseActivity = new TenderResponseActivity();
                final List<TenderResponseQuotes> tenderResponseQuotesList = new ArrayList<TenderResponseQuotes>();
                tenderResponseActivity.setActivity(activity);
                tenderResponseActivity.setNegotiatedQuantity(activity.getQuantity());
                if (WorksConstants.BILL.equalsIgnoreCase(worksService.getWorksConfigValue("REBATE_PREMIUM_LEVEL"))) {
                    for (final TenderResponseContractors tenderResponseContractors : actionTenderResponseContractors)
                        if (tenderResponseContractors != null) {
                            final TenderResponseQuotes tenderResponseQuotes = new TenderResponseQuotes();
                            if (activity.getNonSor() == null) {
                                tenderResponseQuotes.setQuotedRate(activity.getSORCurrentRate().getValue());
                                tenderResponseActivity.setNegotiatedRate(activity.getSORCurrentRate().getValue());
                            } else if (activity.getSchedule() == null) {
                                tenderResponseQuotes.setQuotedRate(activity.getRate());
                                tenderResponseActivity.setNegotiatedRate(activity.getRate());
                            }
                            tenderResponseQuotes.setContractor(tenderResponseContractors.getContractor());
                            tenderResponseQuotes.setQuotedQuantity(activity.getQuantity());
                            tenderResponseQuotes.setTenderResponseActivity(tenderResponseActivity);
                            tenderResponseQuotesList.add(tenderResponseQuotes);
                            tenderResponseActivity.setTenderResponseQuotes(tenderResponseQuotesList);
                        }
                } else
                    for (final TenderResponseContractors tenderResponseContractors : actionTenderResponseContractors)
                        if (tenderResponseContractors != null) {
                            final TenderResponseQuotes tenderResponseQuotes = new TenderResponseQuotes();

                            if (activity.getNonSor() == null) {
                                tenderResponseQuotes.setQuotedRate(activity.getSORCurrentRate().getValue()
                                        + activity.getSORCurrentRate().getValue() * tenderResponse.getPercQuotedRate()
                                                / 100);
                                tenderResponseActivity.setNegotiatedRate(activity.getSORCurrentRate().getValue()
                                        + activity.getSORCurrentRate().getValue()
                                                * tenderResponse.getPercNegotiatedAmountRate() / 100);
                            } else if (activity.getSchedule() == null) {
                                tenderResponseQuotes.setQuotedRate(activity.getRate()
                                        + activity.getRate() * tenderResponse.getPercQuotedRate() / 100);
                                tenderResponseActivity.setNegotiatedRate(activity.getRate()
                                        + activity.getRate() * tenderResponse.getPercNegotiatedAmountRate()
                                                / 100);
                            }
                            tenderResponseQuotes.setContractor(tenderResponseContractors.getContractor());
                            tenderResponseQuotes.setQuotedQuantity(activity.getQuantity());
                            tenderResponseQuotes.setTenderResponseActivity(tenderResponseActivity);
                            tenderResponseQuotesList.add(tenderResponseQuotes);
                            tenderResponseActivity.setTenderResponseQuotes(tenderResponseQuotesList);
                        }
                tenderResponseActivity.setTenderResponse(tenderResponse);
                tenderResponse.addTenderResponseActivity(tenderResponseActivity);
            }
        else
            for (final Activity activity : actList) {
                double negotiatedRate = 0.0;
                final Map<Long, Object> tenderResponseQuotesMap = new HashMap<Long, Object>();
                for (final TenderResponseActivity tenderResponseActivityTemp : actionTenderResponseActivities)
                    if (tenderResponseActivityTemp != null) {
                        final List<TenderResponseQuotes> tenderResponseQuotesList = new ArrayList<TenderResponseQuotes>();
                        for (final TenderResponseQuotes tenderResponseQuotesTemp : tenderResponseActivityTemp
                                .getTenderResponseQuotesList()) {
                            final TenderResponseQuotes tenderResponseQuotes = new TenderResponseQuotes();
                            tenderResponseQuotes.setQuotedRate(tenderResponseQuotesTemp.getQuotedRate());
                            final Contractor contractor = contractorService.findById(tenderResponseQuotesTemp
                                    .getContractor().getId(), false);
                            tenderResponseQuotes.setContractor(contractor);
                            tenderResponseQuotes.setQuotedQuantity(activity.getQuantity());
                            tenderResponseQuotesList.add(tenderResponseQuotes);
                        }

                        tenderResponseQuotesMap.put(tenderResponseActivityTemp.getActivity().getId(),
                                tenderResponseQuotesList);

                        if (StringUtils.isNotBlank(tenderResponseActivityTemp.getSchCode())
                                && activity.getSchedule() != null
                                && activity.getSchedule().getCode().equals(tenderResponseActivityTemp.getSchCode())) {
                            negotiatedRate = tenderResponseActivityTemp.getNegotiatedRate();
                            tenderResponseQuotesMap.put(activity.getId(), tenderResponseQuotesList);
                        } else if (tenderResponseActivityTemp.getActivity().getId().intValue() == activity.getId()
                                .intValue())
                            negotiatedRate = tenderResponseActivityTemp.getNegotiatedRate();
                    }
                final TenderResponseActivity tenderResponseActivity = new TenderResponseActivity();
                tenderResponseActivity.setActivity(activity);
                tenderResponseActivity.setNegotiatedQuantity(activity.getQuantity());
                tenderResponseActivity.setNegotiatedRate(negotiatedRate);
                tenderResponseActivity.setTenderResponse(tenderResponse);
                List<TenderResponseQuotes> tenderResponseQuotesList = new ArrayList<TenderResponseQuotes>();

                tenderResponseQuotesList = (List<TenderResponseQuotes>) tenderResponseQuotesMap.get(activity.getId());
                for (final TenderResponseQuotes tenderResponseQuotes : tenderResponseQuotesList) {
                    tenderResponseQuotes.setTenderResponseActivity(tenderResponseActivity);
                    tenderResponseActivity.addTenderResponseQuotes(tenderResponseQuotes);
                }
                tenderResponse.addTenderResponseActivity(tenderResponseActivity);
            }
    }

    /** reject */

    public String reject() {
        saveTender();
        tenderResponse.setTenderEstimate(tenderHeader.getTenderEstimates().get(0));
        // calling workflow api
        workflowService.transition(TenderResponse.Actions.REJECT.toString(), tenderResponse, "");
        tenderResponse = tenderResponseService.persist(tenderResponse);
        getDesignation(tenderResponse);
        messageKey = "tenderResponse.reject";
        return SUCCESS;
    }

    public void setNegotiationNumber(final TenderResponse entity, final CFinancialYear financialYear) {
        if (entity.getNegotiationNumber() == null || entity.getNegotiationNumber() != null
                && negotiationNumberChangeRequired(entity, financialYear)) {
            if (entity.getTenderEstimate().getAbstractEstimate() != null)
                entity.setNegotiationNumber(negotiationNumberGenerator.getTenderNegotiationNumber(entity
                        .getTenderEstimate().getAbstractEstimate(), financialYear));
            if (entity.getTenderEstimate().getWorksPackage() != null)
                entity.setNegotiationNumber(negotiationNumberGenerator.getTenderNegotiationNumber(entity
                        .getTenderEstimate().getWorksPackage(), financialYear));
        }
    }

    private boolean negotiationNumberChangeRequired(final TenderResponse entity, final CFinancialYear financialYear) {
        final String[] estNum = entity.getNegotiationNumber().split("/");
        if (entity.getTenderEstimate().getAbstractEstimate() != null
                && estNum[0]
                        .equals(entity.getTenderEstimate().getAbstractEstimate().getExecutingDepartment().getCode())
                && estNum[2].equals(financialYear.getFinYearRange()))
            return false;
        else if (entity.getTenderEstimate().getWorksPackage() != null
                && estNum[0].equals(entity.getTenderEstimate().getWorksPackage().getDepartment().getCode())
                && estNum[2].equals(financialYear.getFinYearRange()))
            return false;
        return true;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public void setTenderResponse(final TenderResponse tenderResponse) {
        this.tenderResponse = tenderResponse;
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

    public TenderHeader getTenderHeader() {
        return tenderHeader;
    }

    public void setTenderHeader(final TenderHeader tenderHeader) {
        this.tenderHeader = tenderHeader;
    }

    public String getTenderType() {
        return tenderType;
    }

    public void setTenderType(final String tenderType) {
        this.tenderType = tenderType;
    }

    public void setTenderHeaderService(final PersistenceService<TenderHeader, Long> tenderHeaderService) {
        this.tenderHeaderService = tenderHeaderService;
    }

    public void setTenderEstimate(final TenderEstimate tenderEstimate) {
        this.tenderEstimate = tenderEstimate;
    }

    public void setNegotiationNumberGenerator(final NegotiationNumberGenerator negotiationNumberGenerator) {
        this.negotiationNumberGenerator = negotiationNumberGenerator;
    }

    /**
     * @return the negotiationNumber
     */
    public String getNegotiationNumber() {
        return negotiationNumber;
    }

    /**
     * @param negotiationNumber the negotiationNumber to set
     */
    public void setNegotiationNumber(final String negotiationNumber) {
        this.negotiationNumber = negotiationNumber;
    }

    /**
     * @return the negotiationDate
     */
    public Date getNegotiationDate() {
        return negotiationDate;
    }

    /**
     * @param negotiationDate the negotiationDate to set
     */
    public void setNegotiationDate(final Date negotiationDate) {
        this.negotiationDate = negotiationDate;
    }

    /**
     * @return the departmentId
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @return the estimateNumber
     */
    public String getEstimateNumber() {
        return estimateNumber;
    }

    /**
     * @param estimateNumber the estimateNumber to set
     */
    public void setEstimateNumber(final String estimateNumber) {
        this.estimateNumber = estimateNumber;
    }

    /**
     * @return the projectCode
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * @param projectCode the projectCode to set
     */
    public void setProjectCode(final String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(final String mode) {
        this.mode = mode;
    }

    public List<WorkflowAction> getValidActions() {
        return workflowService.getValidActions(tenderResponse);
    }

    public void setTenderResponseWorkflowService(final WorkflowService<TenderResponse> workflow) {
        workflowService = workflow;
    }

    public List<EgwStatus> getEstimateStatuses() {
        final List<EgwStatus> tnStatusList = egwStatusHibernateDAO.getStatusByModule(TenderResponse.class.getSimpleName());
        tnStatusList.remove(egwStatusHibernateDAO.getStatusByModuleAndCode(TenderResponse.class.getSimpleName(), "NEW"));
        return tnStatusList;
    }

    public List<EgwStatus> getNegotiationStatusesForWO() {
        final List<EgwStatus> tnStatusList = new ArrayList<EgwStatus>();
        final String wpStatus = worksService.getWorksConfigValue("WP_STATUS_SEARCH");
        final String status = worksService.getWorksConfigValue("TenderResponse.setstatus");
        final String lastStatus = worksService.getWorksConfigValue("TenderResponse.laststatus");
        final List<String> statList = new ArrayList<String>();
        if (StringUtils.isNotBlank(wpStatus))
            statList.add(wpStatus);
        if (StringUtils.isNotBlank(status) && StringUtils.isNotBlank(lastStatus)) {
            final List<String> statusList = Arrays.asList(status.split(","));
            for (final String stat : statusList)
                if (stat.equals(lastStatus)) {
                    statList.add(stat);
                    break;
                } else
                    statList.add(stat);
        }
        if ("cancelTN".equals(sourcepage))
            tnStatusList.add(egwStatusHibernateDAO.getStatusByModuleAndCode(TenderResponse.class.getSimpleName(), "APPROVED"));
        tnStatusList.addAll(egwStatusHibernateDAO.getStatusListByModuleAndCodeList(TenderResponse.class.getSimpleName(),
                statList));

        return tnStatusList;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public void validate() {
        if (actionTenderResponseContractors != null) {
            final List<Contractor> contractorList = new ArrayList<Contractor>();
            for (final TenderResponseContractors tenderResponseContractors : tenderResponseService
                    .getActionTenderResponseContractorsList(actionTenderResponseContractors))
                contractorList.add(tenderResponseContractors.getContractor());
            addDropdownData(CONTRACTOR_LIST, contractorList);
        }

        if (tenderResponse.getId() == null && tenderResponse.getEgwStatus() == null
                && sourcepage.equalsIgnoreCase("createNegotiationForWP") && !sourcepage.equalsIgnoreCase(SOURCE_INBOX)
                && !sourcepage.equalsIgnoreCase("cancelTN") && !sourcepage.equalsIgnoreCase(SEARCH_NEGOTIATION_FOR_WO)
                && !sourcepage.equalsIgnoreCase("search")) {
            final TenderResponse tenderResp = (TenderResponse) persistenceService.findByNamedQuery(
                    "getTenderForWorksPackageNum", getWpNumber());
            if (tenderResp != null)
                addActionError(getText("tenderNegotiation.workspackage.uniquecheck.message", new String[] {
                        getWpNumber(), tenderResp.getNegotiationNumber() }));
        }

        if (tenderResponse != null && tenderType != null && !tenderTypeList.isEmpty()
                && tenderType.equals(tenderTypeList.get(0))) {
            if (tenderResponse.getPercQuotedRate() <= -100D)
                addActionError(getText("tenderResponse.percQuotedRate.valid"));
            if (tenderResponse.getPercNegotiatedAmountRate() <= -100D)
                addActionError(getText("tenderResponse.percNegotiatedRate.valid"));
        }
        final Collection<TenderResponseActivity> tenderResponseActivityList = tenderResponseService
                .getTenderResponseActivityList(actionTenderResponseActivities);
        if (tenderResponse != null && tenderType != null && !tenderTypeList.isEmpty()
                && tenderType.equals(tenderTypeList.get(1)) && !tenderResponseActivityList.isEmpty())
            for (final TenderResponseActivity tenderResponseActivity : tenderResponseActivityList) {
                for (final TenderResponseQuotes tenderResponseQuotes : tenderResponseActivity
                        .getTenderResponseQuotesList())
                    if (tenderResponseQuotes.getQuotedRate() == 0.0)
                        addActionError(getText("tenderResponseActivity.quotedRate.non.negative"));
                if (tenderResponseActivity.getNegotiatedRate() == 0.0)
                    addActionError(getText("tenderResponseActivity.negotiatedRate.non.negative"));
            }

    }

    /**
     * @return the worksService
     */
    public WorksService getWorksService() {
        return worksService;
    }

    /**
     * @param worksService the worksService to set
     */
    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(final String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the sourcepage
     */
    public String getSourcepage() {
        return sourcepage;
    }

    /**
     * @param sourcepage the sourcepage to set
     */
    public void setSourcepage(final String sourcepage) {
        this.sourcepage = sourcepage;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(final WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(final String errorType) {
        this.errorType = errorType;
    }

    public Date getApprovedDateHidden() {
        return approvedDateHidden;
    }

    public void setApprovedDateHidden(final Date approvedDateHidden) {
        this.approvedDateHidden = approvedDateHidden;
    }

    public String getNsActionName() {
        return nsActionName;
    }

    public void setNsActionName(final String nsActionName) {
        this.nsActionName = nsActionName;
    }

    public Long getTenderRespId() {
        return tenderRespId;
    }

    public void setTenderRespId(final Long tenderRespId) {
        this.tenderRespId = tenderRespId;
    }

    public Integer getNegotiationStatusId() {
        return negotiationStatusId;
    }

    public void setNegotiationStatusId(final Integer negotiationStatusId) {
        this.negotiationStatusId = negotiationStatusId;
    }

    public Integer getNegotiationStatusApprovedById() {
        return negotiationStatusApprovedById;
    }

    public void setNegotiationStatusApprovedById(final Integer negotiationStatusApprovedById) {
        this.negotiationStatusApprovedById = negotiationStatusApprovedById;
    }

    public String getOption() {
        return option;
    }

    public void setOption(final String option) {
        this.option = option;
    }

    public String getMessageKey() {
        return messageKey;
    }

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    // set status and workorder no generation
    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(final String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCurrentApproverName() {
        return currentApproverName;
    }

    public void setCurrentApproverName(final String currentApproverName) {
        this.currentApproverName = currentApproverName;
    }

    public String getCurrentApprovedDateStr() {
        if (currentApprovedDate != null)
            currentApprovedDateStr = new java.text.SimpleDateFormat("dd/MM/yyyy").format(currentApprovedDate);
        return currentApprovedDateStr;
    }

    public void setCurrentApprovedDate(final Date currentApprovedDate) {
        this.currentApprovedDate = currentApprovedDate;
    }

    public Integer getStatusIdValue() {
        return statusIdValue;
    }

    public void setStatusIdValue(final Integer statusIdValue) {
        this.statusIdValue = statusIdValue;
    }

    public void setTenderResponseService(final TenderResponseService tenderResponseService) {
        this.tenderResponseService = tenderResponseService;
    }

    public Integer getWorkOrderInchargeId() {
        return workOrderInchargeId;
    }

    public void setWorkOrderInchargeId(final Integer workOrderInchargeId) {
        this.workOrderInchargeId = workOrderInchargeId;
    }

    public Long getWorksPackageId() {
        return worksPackageId;
    }

    public void setWorksPackageId(final Long worksPackageId) {
        this.worksPackageId = worksPackageId;
    }

    public void setWorkspackageService(final WorksPackageService workspackageService) {
        this.workspackageService = workspackageService;
    }

    public WorksPackage getWorksPackage() {
        return worksPackage;
    }

    public void setWorksPackage(final WorksPackage worksPackage) {
        this.worksPackage = worksPackage;
    }

    public String getCreatedBySelection() {
        return createdBySelection;
    }

    public void setCreatedBySelection(final String createdBySelection) {
        this.createdBySelection = createdBySelection;
    }

    public String getEditableDate() {
        return editableDate;
    }

    public void setEditableDate(final String editableDate) {
        this.editableDate = editableDate;
    }

    public void setDepartmentService(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public String getWpYear() {
        if (worksPackage != null)
            return abstractEstimateService.getCurrentFinancialYear(worksPackage.getWpDate()).getFinYearRange();
        return wpYear;
    }

    public boolean validTenderNo() {
        boolean status = false;
        if (tenderHeader != null && tenderHeader.getTenderNo() != null) {
            final AjaxTenderNegotiationAction ajaxTenderNegotiationAction = new AjaxTenderNegotiationAction();
            ajaxTenderNegotiationAction.setPersistenceService(getPersistenceService());
            ajaxTenderNegotiationAction.setAssignmentService(assignmentService);
            ajaxTenderNegotiationAction.setTenderNo(tenderHeader.getTenderNo());
            ajaxTenderNegotiationAction.setId(id);
            if (ajaxTenderNegotiationAction.getTendernoCheck())
                status = true;
        }
        return status;
    }

    @SuppressWarnings("unchecked")
    protected void getPositionAndUser() {
        final List<TenderResponseContractors> tenderResponseContractorsList = new ArrayList<TenderResponseContractors>();
        final Map<String, Integer> exceptionaSorMap = worksService.getExceptionSOR();

        final Iterator i = searchResult.getList().iterator();
        while (i.hasNext()) {
            final TenderResponse tenderResponse = (TenderResponse) i.next();
            if (SEARCH_NEGOTIATION_FOR_WO.equals(sourcepage)) {
                double totalTenderNegQty = tenderResponse.getTotalNegotiatedQuantity().getValue();
                double totalWorkOrderQty = getTotalWorkOrderQuantity(tenderResponse.getNegotiationNumber());
                totalTenderNegQty = Math.round(totalTenderNegQty * 100) / 100.0;
                totalWorkOrderQty = Math.round(totalWorkOrderQty * 100) / 100.0;
                if (totalWorkOrderQty >= totalTenderNegQty)
                    continue;
            }
            if (!tenderResponse.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED)
                    && !tenderResponse.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)) {
                final PersonalInformation emp = employeeServiceOld.getEmployeeforPosition(tenderResponse.getCurrentState()
                        .getOwnerPosition());
                if (emp != null && StringUtils.isNotBlank(emp.getEmployeeName()))
                    if (tenderResponse.getTenderEstimate().getAbstractEstimate() == null)
                        tenderResponse.getTenderEstimate().getWorksPackage().setEmployeeName(emp.getEmployeeName());
                    else
                        tenderResponse.getTenderEstimate().getAbstractEstimate()
                                .setPositionAndUserName(emp.getEmployeeName());
            }
            double totalAmt = 0;
            for (final TenderResponseActivity act : tenderResponse.getTenderResponseActivities()) {
                double result = 1;
                if (act.getActivity().getSchedule() != null
                        && exceptionaSorMap.containsKey(act.getActivity().getUom().getUom())) {
                    result = exceptionaSorMap.get(act.getActivity().getUom().getUom());
                    totalAmt += act.getNegotiatedQuantity() * act.getNegotiatedRate() / result;
                } else
                    totalAmt += act.getNegotiatedQuantity() * act.getNegotiatedRate();
            }
            tenderResponse.setTotalAmount(totalAmt);

            final Iterator j = tenderResponse.getTenderResponseContractors().iterator();

            while (j.hasNext()) {
                final TenderResponseContractors tenderResponseCntractrs = (TenderResponseContractors) j.next();
                if (tenderResponseCntractrs.getTenderResponse().getEgwStatus() != null
                        && tenderResponseCntractrs.getTenderResponse().getEgwStatus().getCode()
                                .equals(TenderResponse.TenderResponseStatus.APPROVED.toString())) {
                    final OfflineStatus setStatus = (OfflineStatus) persistenceService.findByNamedQuery(
                            "getmaxStatusByObjectId_Type", tenderResponseCntractrs.getId(),
                            tenderResponseCntractrs.getId(), TenderResponseContractors.class.getSimpleName(),
                            TenderResponseContractors.class.getSimpleName());
                    if (setStatus == null) {
                        tenderResponseCntractrs.setStatus(tenderResponse.getEgwStatus().getCode());
                        tenderResponseCntractrs.setStatusCode(tenderResponse.getEgwStatus().getCode());
                    } else {
                        tenderResponseCntractrs.setStatus(setStatus.getEgwStatus().getDescription());
                        tenderResponseCntractrs.setStatusCode(setStatus.getEgwStatus().getCode());
                    }
                } else {
                    tenderResponseCntractrs.setStatus(tenderResponse.getEgwStatus().getCode());
                    tenderResponseCntractrs.setStatusCode(tenderResponse.getEgwStatus().getCode());
                }
            }

            final String approved = getApprovedValue();
            final OfflineStatus lastStatus = worksStatusService.findByNamedQuery(STATUS_OBJECTID, tenderResponse
                    .getTenderResponseContractors().get(0).getId(), OBJECT_TYPE, getLastStatus());
            final String actions = worksService.getWorksConfigValue("TENDERNEGOTIATION_SHOW_ACTIONS");
            if (StringUtils.isNotBlank(actions)) {
                String setStat = "";
                tenderResponse.getTenderNegotiationsActions().addAll(Arrays.asList(actions.split(",")));

                if (lastStatus != null || "view".equalsIgnoreCase(setStatus))
                    setStat = worksService.getWorksConfigValue("WORKS_VIEW_OFFLINE_STATUS_VALUE");
                else if (lastStatus == null && StringUtils.isNotBlank(approved)
                        && tenderResponse.getEgwStatus() != null
                        && approved.equals(tenderResponse.getEgwStatus().getCode()))
                    setStat = worksService.getWorksConfigValue("WORKS_SETSTATUS_VALUE");
                if (StringUtils.isNotBlank(setStat))
                    tenderResponse.getTenderNegotiationsActions().add(setStat);
            }

            tenderResponseContractorsList.addAll(tenderResponse.getTenderResponseContractors());
        }

        if (SEARCH_NEGOTIATION_FOR_WO.equals(sourcepage)) {
            final List<TenderResponseContractors> tenderResponseContractorsTempList = new ArrayList<TenderResponseContractors>();
            final Iterator k = tenderResponseContractorsList.listIterator();
            while (k.hasNext()) {
                final TenderResponseContractors tenderResponseCntractrs = (TenderResponseContractors) k.next();
                if (status != null && status != "" && contractorId != null && contractorId != -1) {
                    if (tenderResponseCntractrs.getStatusCode().equals(status)
                            && tenderResponseCntractrs.getContractor().getId().equals(contractorId))
                        tenderResponseContractorsTempList.add(tenderResponseCntractrs);
                } else if (status != null && status != "" && (contractorId == null || contractorId == -1)) {
                    if (tenderResponseCntractrs.getStatusCode().equals(status))
                        tenderResponseContractorsTempList.add(tenderResponseCntractrs);
                } else if ((status == null || status == "") && contractorId != null && contractorId != -1
                        && tenderResponseCntractrs.getContractor().getId().equals(contractorId))
                    tenderResponseContractorsTempList.add(tenderResponseCntractrs);
            }
            searchResult.getList().clear();
            searchResult.getList().addAll(tenderResponseContractorsTempList);
        } else {
            searchResult.getList().clear();
            searchResult.getList().addAll(tenderResponseContractorsList);
        }
    }

    private double getTotalWorkOrderQuantity(final String negotiationNumber) {
        Object[] params = new Object[] { negotiationNumber, WorksConstants.CANCELLED_STATUS };
        Double totalWorkOrderQty = (Double) getPersistenceService().findByNamedQuery("getTotalQuantityForWO", params);
        params = new Object[] { negotiationNumber, WorksConstants.NEW };
        final Double totalWorkOrderQtyForNew = (Double) getPersistenceService().findByNamedQuery(
                "getTotalQuantityForNewWO", params);

        if (totalWorkOrderQty != null && totalWorkOrderQtyForNew != null)
            totalWorkOrderQty = totalWorkOrderQty + totalWorkOrderQtyForNew;
        if (totalWorkOrderQty == null && totalWorkOrderQtyForNew != null)
            totalWorkOrderQty = totalWorkOrderQtyForNew;
        if (totalWorkOrderQty == null)
            return 0.0d;
        else
            return totalWorkOrderQty.doubleValue();
    }

    public String getApprovedValue() {
        return TenderResponse.TenderResponseStatus.APPROVED.toString();
    }

    public String getLastStatus() {
        return worksService.getWorksConfigValue(WorksConstants.TENDERRESPONSE_LASTSTATUS);
    }

    public String getDesignationNegotiation() {
        return designationNegotiation;
    }

    public void setDesignationNegotiation(final String designationNegotiation) {
        this.designationNegotiation = designationNegotiation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(final String designation) {
        this.designation = designation;
    }

    public String getTenderSource() {
        return tenderSource;
    }

    public void setTenderSource(final String tenderSource) {
        this.tenderSource = tenderSource;
    }

    public String getSetStatus() {
        return setStatus;
    }

    public void setSetStatus(final String setStatus) {
        this.setStatus = setStatus;
    }

    public Double getSorPerDiff() {
        return sorPerDiff;
    }

    public void setSorPerDiff(final Double sorPerDiff) {
        this.sorPerDiff = sorPerDiff;
    }

    public String getWpNumber() {
        return wpNumber;
    }

    public void setWpNumber(final String wpNumber) {
        this.wpNumber = wpNumber;
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

    public String getTenderCretedBy() {
        return tenderCretedBy;
    }

    public void setTenderCretedBy(final String tenderCretedBy) {
        this.tenderCretedBy = tenderCretedBy;
    }

    public String getTenderFileNumber() {
        return tenderFileNumber;
    }

    public void setTenderFileNumber(final String tenderFileNumber) {
        this.tenderFileNumber = tenderFileNumber;
    }

    public List<Contractor> getContractorForApprovedNegotiation() {
        return getPersistenceService().findAllByNamedQuery("getApprovedNegotiationContractors");
    }

    public void setDeptId(final Long deptId) {
        this.deptId = deptId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        final List<Object> paramList = new ArrayList<Object>();
        String negotiationStr = "from TenderResponse as tenderResponse "
                + " where tenderResponse.createdBy is not null ";

        if (getStatus().equals(TenderResponse.TenderResponseStatus.APPROVED.toString())) {
            negotiationStr += " and tenderResponse.egwStatus.code= ?  and "
                    + "  tenderResponse.id  in ( select trc.tenderResponse.id from TenderResponseContractors trc where trc.id not in (select objectId from OfflineStatus where objectType='"
                    + OBJECT_TYPE + "' ))";
            paramList.add(status);
        } else if (getStatus().equals(TenderResponse.TenderResponseStatus.CANCELLED.toString())) {
            negotiationStr += " and tenderResponse.egwStatus.code= ? ";
            paramList.add(status);
        } else if (StringUtils.isNotBlank(getStatus()) && !getStatus().equals("-1")) {
            negotiationStr += "and tenderResponse.egwStatus.code != 'NEW' and ((tenderResponse.egwStatus.code = ?) or "
                    + "(tenderResponse.egwStatus.code = 'APPROVED' and tenderResponse.id in (select trc.tenderResponse.id from TenderResponseContractors trc where trc.id in ("
                    + " select stat.objectId from "
                    + "OfflineStatus stat where stat.egwStatus.code= ? and stat.id = (select"
                    + " max(stat1.id) from OfflineStatus stat1 where trc.id=stat1.objectId and stat1.objectType='"
                    + OBJECT_TYPE + "') and stat.objectType='" + OBJECT_TYPE + "')))) ";
            paramList.add(getStatus());
            paramList.add(getStatus());
            if (sourcepage != null && "cancelTN".equals(sourcepage))
                negotiationStr += " and tenderResponse.egwStatus.code = 'APPROVED'";
        }

        if (StringUtils.isNotBlank(negotiationNumber)) {
            negotiationStr = negotiationStr + " and UPPER(tenderResponse.negotiationNumber) like ?";
            paramList.add("%" + negotiationNumber.trim().toUpperCase() + "%");
        }

        if (fromDate != null && toDate != null && getFieldErrors().isEmpty()) {
            negotiationStr = negotiationStr + " and tenderResponse.negotiationDate between ? and ? ";
            paramList.add(fromDate);
            paramList.add(toDate);
        }

        if (departmentId != null && departmentId != -1) {
            negotiationStr = negotiationStr
                    + " and (tenderResponse.tenderEstimate.abstractEstimate.id in "
                    + "(select tr1.tenderEstimate.abstractEstimate.id from TenderResponse tr1 "
                    + " where tr1.tenderEstimate.abstractEstimate.id!=null and  tr1.tenderEstimate.worksPackage.id=null and"
                    + " tr1.tenderEstimate.abstractEstimate.executingDepartment.id= ?) "
                    + " or tenderResponse.tenderEstimate.worksPackage.id in "
                    + "(select tr.tenderEstimate.worksPackage.id from TenderResponse tr "
                    + " where tr.tenderEstimate.abstractEstimate.id=null and  tr.tenderEstimate.worksPackage.id!=null and"
                    + " tr.tenderEstimate.worksPackage.department.id= ?)) ";
            paramList.add(departmentId);
            paramList.add(departmentId);
        }
        if (StringUtils.isNotBlank(estimateNumber)) {
            negotiationStr = negotiationStr
                    + " and tenderResponse.tenderEstimate.worksPackage.id in "
                    + "(select wpd.worksPackage.id from WorksPackageDetails wpd where UPPER(wpd.estimate.estimateNumber) like ? )";
            paramList.add("%" + estimateNumber.trim().toUpperCase() + "%");
        }
        if (StringUtils.isNotBlank(projectCode)) {
            negotiationStr = negotiationStr
                    + " and tenderResponse.tenderEstimate.worksPackage.id in "
                    + "(select wpd.worksPackage.id from WorksPackageDetails wpd where UPPER(wpd.estimate.projectCode.code) like ?)";
            paramList.add("%" + projectCode.trim().toUpperCase() + "%");
        }
        if (StringUtils.isNotBlank(wpNumber)) {
            negotiationStr = negotiationStr + " and UPPER(tenderResponse.tenderEstimate.worksPackage.wpNumber) like ?";
            paramList.add("%" + wpNumber.trim().toUpperCase() + "%");
        }
        if (StringUtils.isNotBlank(tenderFileNumber)) {
            negotiationStr = negotiationStr
                    + "  and  UPPER(tenderResponse.tenderEstimate.worksPackage.tenderFileNumber) like ? ";
            paramList.add("%" + tenderFileNumber.toUpperCase() + "%");
        }
        if (getContractorId() != null && getContractorId() != -1) {
            negotiationStr = negotiationStr
                    + "  and tenderResponse.id in ( select trc.tenderResponse.id from TenderResponseContractors trc where trc.contractor.id= ?)";
            paramList.add(getContractorId());
        }

        final String countQuery = "select count(*) " + negotiationStr;
        return new SearchQueryHQL(negotiationStr, countQuery, paramList);

    }

    @Override
    @ValidationErrorPage(value = "searchNegotiationPage")
    public String search() {
        setPageSize(WorksConstants.PAGE_SIZE);
        super.search();
        getPositionAndUser();
        return "searchNegotiationPage";
    }

    public String cancelApprovedTN() {
        final TenderResponse tenderResponse = tenderResponseService.findById(tenderRespId, false);
        tenderResponse.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode("TenderResponse",
                WorksConstants.CANCELLED_STATUS));

        final PersonalInformation prsnlInfo = employeeServiceOld.getEmpForUserId(worksService.getCurrentLoggedInUserId());
        String empName = "";
        if (prsnlInfo.getEmployeeFirstName() != null)
            empName = prsnlInfo.getEmployeeFirstName();
        if (prsnlInfo.getEmployeeLastName() != null)
            empName = empName.concat(" ").concat(prsnlInfo.getEmployeeLastName());
        if (cancelRemarks != null && StringUtils.isNotBlank(cancelRemarks))
            cancellationReason.concat(" : ").concat(cancelRemarks).concat(". ")
                    .concat(getText("tenderNegotiation.cancel.cancelledby")).concat(": ").concat(empName);
        else
            cancellationReason.concat(". ").concat(getText("tenderNegotiation.cancel.cancelledby")).concat(": ")
                    .concat(empName);

        // TODO - The setter methods of variables in State.java are protected.
        // Need to alternative way to solve this issue.
        // Set the status and workflow state to cancelled
        /*****
         * State oldEndState = tenderResponse.getCurrentState(); Position owner = prsnlInfo.getAssignment(new
         * Date()).getPosition(); oldEndState.setCreatedBy(prsnlInfo.getUserMaster());
         * oldEndState.setModifiedBy(prsnlInfo.getUserMaster()); oldEndState.setCreatedDate(new Date());
         * oldEndState.setModifiedDate(new Date()); oldEndState.setOwner(owner); oldEndState
         * .setValue(TenderResponse.TenderResponseStatus.CANCELLED.toString()); oldEndState.setText1(cancellationText);
         * tenderResponse.changeState("END", owner, null);
         *****/

        negotiationNumber = tenderResponse.getNegotiationNumber();
        messageKey = negotiationNumber + " : " + getText("tenderResponse.cancel");
        return SUCCESS;
    }

    public void setPersonalInformationService(final PersonalInformationService personalInformationService) {
        this.personalInformationService = personalInformationService;
    }

    public String acceptanceNote() {
        return TENDER_ACCEPTANCE_NOTE;
    }

    public List<TenderResponseContractors> getActionTenderResponseContractors() {
        return actionTenderResponseContractors;
    }

    public void setActionTenderResponseContractors(final List<TenderResponseContractors> actionTenderResponseContractors) {
        this.actionTenderResponseContractors = actionTenderResponseContractors;
    }

    public void setContractorService(final PersistenceService<Contractor, Long> service) {
        contractorService = service;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(final Long contractorId) {
        this.contractorId = contractorId;
    }

    public List<String> getTenderTypeList() {
        return tenderTypeList;
    }

    public void setTenderTypeList(final List<String> tenderTypeList) {
        this.tenderTypeList = tenderTypeList;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
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

    public PersistenceService<OfflineStatus, Long> getWorksStatusService() {
        return worksStatusService;
    }

    public void setWorksStatusService(final PersistenceService<OfflineStatus, Long> worksStatusService) {
        this.worksStatusService = worksStatusService;
    }

    public String getNegoNumber() {
        return negoNumber;
    }

    public void setNegoNumber(final String negoNumber) {
        this.negoNumber = negoNumber;
    }

    public String getLoggedInUserEmployeeCode() {
        return loggedInUserEmployeeCode;
    }

    public void setLoggedInUserEmployeeCode(final String loggedInUserEmployeeCode) {
        this.loggedInUserEmployeeCode = loggedInUserEmployeeCode;
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public Map<String, String> getTenderInvitationTypeMap() {
        return tenderInvitationTypeMap;
    }

    public void setTenderInvitationTypeMap(final Map<String, String> tenderInvitationTypeMap) {
        this.tenderInvitationTypeMap = tenderInvitationTypeMap;
    }

    public List<TenderResponseActivity> getActionTenderResponseActivities() {
        return actionTenderResponseActivities;
    }

    public void setActionTenderResponseActivities(final List<TenderResponseActivity> actionTenderResponseActivities) {
        this.actionTenderResponseActivities = actionTenderResponseActivities;
    }
}
