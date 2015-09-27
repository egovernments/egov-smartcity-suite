/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.actions.tender;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.models.Money;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeServiceOld;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.tender.WorksPackageDetails;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksPackageService;
import org.egov.works.services.WorksService;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.jasperreports.engine.JRException;

@Results({ @Result(name = WorksPackageAction.PRINT, type = "stream", location = "WorkspackagePDF", params = {
        "inputName", "WorkspackagePDF", "contentType", "application/pdf", "contentDisposition", "no-cache" }),
        @Result(name = WorksPackageAction.NEW, location = "worksPackage-new.jsp") })
public class WorksPackageAction extends BaseFormAction {

    private static final long serialVersionUID = -6365331777546797839L;
    private String editableDate = "yes";
    private String createdBySelection = "no";
    private WorksService worksService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private EmployeeServiceOld employeeServiceOld;
    private DepartmentService departmentService;
    private WorksPackage worksPackage = new WorksPackage();
    private String designation;
    private Integer empId;
    private Long[] estId;
    private String sourcepage;
    private Money worktotalValue;
    @Autowired
    private UserService userService;
    private WorkflowService<WorksPackage> workflowService;
    private WorksPackageService workspackageService;
    private AbstractEstimateService abstractEstimateService;
    private List<AbstractEstimate> abstractEstimateList = new ArrayList<AbstractEstimate>();
    private Long id;
    private String actionName;
    private String messageKey;
    private String nextEmployeeName;
    private String nextDesignation;
    private String packageNumber;
    private static final String PREPARED_BY_LIST = "preparedByList";
    private static final String DEPARTMENT_LIST = "departmentList";
    private static final String SOURCE_INBOX = "inbox";
    private static final String SAVE_ACTION = "save";
    private EisUtilService eisService;
    private String loggedInUserEmployeeCode;

    /**
     * pdf variable declaration
     */

    public static final String PRINT = "print";
    private InputStream WorkspackagePDF;
    private ReportService reportService;
    private PersonalInformationService personalInformationService;

    public WorksPackageAction() {
        addRelatedEntity("department", Department.class);
    }

    @Override
    public void prepare() {
        final AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
        ajaxEstimateAction.setPersistenceService(getPersistenceService());
        ajaxEstimateAction.setAssignmentService(assignmentService);
        ajaxEstimateAction.setPersonalInformationService(personalInformationService);
        ajaxEstimateAction.setAbstractEstimateService(abstractEstimateService);
        ajaxEstimateAction.setEisService(eisService);
        super.prepare();
        if (id != null)
            worksPackage = workspackageService.findById(id, false);
        if (id == null && packageNumber != null && StringUtils.isNotBlank(packageNumber))
            worksPackage = workspackageService.find("from WorksPackage where wpNumber=? and egwStatus.code='APPROVED'",
                    packageNumber);
        setupDropdownDataExcluding("department");

        addDropdownData("executingDepartmentList", departmentService.getAllDepartments());
        final Assignment latestAssignment = abstractEstimateService.getLatestAssignmentForCurrentLoginUser();
        if (latestAssignment != null) {
            worksPackage.setWorkflowDepartmentId(abstractEstimateService.getLatestAssignmentForCurrentLoginUser()
                    .getDepartment().getId());
            if (worksPackage.getPreparedBy() == null)
                loggedInUserEmployeeCode = latestAssignment.getEmployee().getCode();
            else
                loggedInUserEmployeeCode = worksPackage.getPreparedBy().getEmployeeCode();
            if (worksPackage.getDepartment() == null) {
                worksPackage.setDepartment(latestAssignment.getDepartment());
                setDesignation(latestAssignment.getDesignation().getName());
            }
        }

        if (StringUtils.isNotBlank(getCreatedBy()) && "yes".equalsIgnoreCase(getCreatedBy())) {
            setCreatedBySelection(getCreatedBy());
            addDropdownData(DEPARTMENT_LIST, departmentService.getAllDepartments());
            populatePreparedByList(ajaxEstimateAction, worksPackage.getDepartment() != null);
        } else {
            if (id == null
                    || worksPackage.getEgwStatus() != null
                            && (worksPackage.getEgwStatus().getCode()
                                    .equals(WorksPackage.WorkPacakgeStatus.REJECTED.toString()) || worksPackage.getEgwStatus()
                                            .getCode().equals("NEW")))
                addDropdownData(DEPARTMENT_LIST, worksService.getAllDeptmentsForLoggedInUser());
            else
                addDropdownData(DEPARTMENT_LIST, departmentService.getAllDepartments());
            populatePreparedByList(ajaxEstimateAction, worksPackage.getDepartment() != null);
            empId = getEmployee().getId();
        }
        if (StringUtils.isNotBlank(getPastDate()))
            setEditableDate(getPastDate());

    }

    @Action(value = "/tender/worksPackage-newform")
    public String newform() {
        /*
         * PersonalInformation pi = getEmployee(); Assignment assignment = getAssignment(pi); if(assignment!=null &&
         * "no".equalsIgnoreCase(getCreatedBy())){ setDesignation(assignment.getDesigId().getDesignationName());
         * worksPackage.setUserDepartment(assignment.getDeptId()); setEmpId(pi.getId()); }
         */

        return NEW;
    }

    public String edit() {
        if (SOURCE_INBOX.equalsIgnoreCase(sourcepage)) {
            final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
            final boolean isValidUser = worksService.validateWorkflowForUser(worksPackage, user);
            if (isValidUser)
                throw new ApplicationRuntimeException("Error: Invalid Owner - No permission to view this page.");
        } else if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";

        setDesignation(getAssignment(worksPackage.getPreparedBy()).getDesignation().getName());
        setEmpId(worksPackage.getPreparedBy().getIdPersonalInformation());
        abstractEstimateList = workspackageService.getAbStractEstimateListByWorksPackage(worksPackage);
        setWorktotalValue(abstractEstimateService.getWorkValueIncludingTaxesForEstList(abstractEstimateList));
        return EDIT;
    }

    public String save() {
        if (validTenderFileNo())
            throw new ValidationException(Arrays.asList(new ValidationError("wp.tenderfilenumber.isunique",
                    "wp.tenderfilenumber.isunique")));

        if (worksPackage.getId() != null
                && (worksPackage.getEgwStatus().getCode()
                        .equalsIgnoreCase(WorksPackage.WorkPacakgeStatus.REJECTED.toString()) || worksPackage
                                .getEgwStatus().getCode().equalsIgnoreCase("NEW")))
            worksPackage.getWorksPackageDetails().clear();
        if (worksPackage.getId() == null
                || worksPackage.getEgwStatus().getCode()
                        .equalsIgnoreCase(WorksPackage.WorkPacakgeStatus.REJECTED.toString())
                || worksPackage.getEgwStatus().getCode().equalsIgnoreCase("NEW"))
            populateEstimatesList(estId);
        if (worksPackage.getId() == null && worksPackage.getEgwStatus() == null) {
            validateFinancingSource(abstractEstimateList);
            validateEstimateForUniqueness();

        }
        worksPackage.setPreparedBy(employeeServiceOld.getEmloyeeById(empId));
        try {
            workspackageService.setWorksPackageNumber(worksPackage,
                    abstractEstimateService.getCurrentFinancialYear(worksPackage.getPackageDate()));
        } catch (final ValidationException sequenceException) {
            final List<ValidationError> errorList = sequenceException.getErrors();
            for (final ValidationError error : errorList)
                if (error.getMessage().contains("DatabaseSequenceFirstTimeException")) {
                    prepare();
                    throw new ValidationException(Arrays.asList(new ValidationError("error", error.getMessage())));
                }
        }
        worksPackage = workspackageService.persist(worksPackage);
        workflowService.transition(actionName, worksPackage, "");
        messageKey = "worksPackage." + actionName;
        addActionMessage(getText(messageKey, "The Works Package was saved successfully"));
        getDesignation(worksPackage);
        if (SAVE_ACTION.equals(actionName))
            sourcepage = "inbox";

        return SAVE_ACTION.equals(actionName) ? EDIT : SUCCESS;
    }

    private void validateFinancingSource(final List<AbstractEstimate> estimateList) {
        Integer fundSourceId1;
        fundSourceId1 = abstractEstimateList.get(0).getFundSource().getId();
        for (int i = 1; i < abstractEstimateList.size(); i++)
            if (fundSourceId1 != abstractEstimateList.get(i).getFundSource().getId())
                throw new ValidationException(Arrays.asList(new ValidationError(
                        "wp.estimate.different.fund.source.not.allowed",
                        "wp.estimate.different.fund.source.not.allowed")));

    }

    private void validateEstimateForUniqueness() {
        WorksPackage wp = null;
        final Map<String, List<AbstractEstimate>> wpMap = new HashMap<String, List<AbstractEstimate>>();
        List<AbstractEstimate> estimateList = null;
        for (final AbstractEstimate estimate : abstractEstimateList) {
            wp = workspackageService.getWorksPackageForAbstractEstimate(estimate);
            if (wp != null) {
                final String wpString = wp.getWpNumber() + "~!"
                        + (wp.getEgwStatus().getDescription() != null ? wp.getEgwStatus().getDescription() : " ");
                if (wpMap.get(wpString) == null) {
                    estimateList = new ArrayList<AbstractEstimate>();
                    estimateList.add(estimate);
                    wpMap.put(wpString, estimateList);
                } else
                    wpMap.get(wpString).add(estimate);
            }
        }
        if (!wpMap.isEmpty()) {
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            for (final String wpnumber : wpMap.keySet()) {
                final List<AbstractEstimate> estList = wpMap.get(wpnumber);
                StringBuffer estimatesSting = null;
                for (final AbstractEstimate absEstimate : estList)
                    if (estimatesSting == null) {
                        estimatesSting = new StringBuffer();
                        estimatesSting.append(absEstimate.getEstimateNumber());
                    } else
                        estimatesSting.append(", ").append(absEstimate.getEstimateNumber());
                final String[] str = StringUtils.split(wpnumber, "~!");
                errors.add(new ValidationError("worksPackage.uniqueCheck.message", "worksPackage.uniqueCheck.message",
                        new String[] { str[0], estimatesSting.toString(), str[1] }));
            }
            throw new ValidationException(errors);
        }
    }

    public String cancelWorkflow() {
        if (worksPackage.getId() != null) {
            workflowService.transition(WorksPackage.Actions.CANCEL.toString(), worksPackage,
                    worksPackage.getWorkflowapproverComments());
            worksPackage = workspackageService.persist(worksPackage);
        }
        messageKey = "worksPackage.cancel";
        getDesignation(worksPackage);
        return SUCCESS;
    }

    @Override
    public Object getModel() {
        return worksPackage;
    }

    public void setModel(final WorksPackage worksPackage) {
        this.worksPackage = worksPackage;
    }

    protected void populatePreparedByList(final AjaxEstimateAction ajaxEstimateAction,
            final boolean executingDeptPopulated) {
        if (executingDeptPopulated) {
            ajaxEstimateAction.setExecutingDepartment(worksPackage.getDepartment().getId());

            if (StringUtils.isNotBlank(loggedInUserEmployeeCode))
                ajaxEstimateAction.setEmployeeCode(loggedInUserEmployeeCode);
            ajaxEstimateAction.usersInExecutingDepartment();
            addDropdownData(PREPARED_BY_LIST, ajaxEstimateAction.getUsersInExecutingDepartment());
        } else
            addDropdownData(PREPARED_BY_LIST, Collections.EMPTY_LIST);
    }

    protected void populateEstimatesList(final Long[] estimateID) {
        if (estimateID != null && estimateID.length > 0) {
            abstractEstimateList = abstractEstimateService.getAbEstimateListById(StringUtils.join(estId, "`~`"));
            setWorktotalValue(abstractEstimateService.getWorkValueIncludingTaxesForEstList(abstractEstimateList));
        }
        setWPDetails();
    }

    public void setWPDetails() {
        if (!abstractEstimateList.isEmpty())
            for (final AbstractEstimate ab : abstractEstimateList) {
                final WorksPackageDetails wpDetails = new WorksPackageDetails();
                wpDetails.setEstimate(ab);
                wpDetails.setWorksPackage(worksPackage);
                worksPackage.addEstimates(wpDetails);
            }
    }

    public boolean validTenderFileNo() {
        boolean status = false;
        if (worksPackage != null && worksPackage.getTenderFileNumber() != null) {
            final AjaxWorksPackageAction ajaxWorksPackageAction = new AjaxWorksPackageAction();
            ajaxWorksPackageAction.setPersistenceService(getPersistenceService());
            ajaxWorksPackageAction.setWorkspackageService(workspackageService);
            ajaxWorksPackageAction.setTenderFileNumber(worksPackage.getTenderFileNumber());
            ajaxWorksPackageAction.setId(id);
            if (ajaxWorksPackageAction.getTenderFileNumberCheck())
                status = true;
        }
        return status;
    }

    /**
     * print pdf
     *
     * @throws JRException ,Exception
     */
    @SkipValidation
    public String viewWorksPackagePdf() throws JRException, Exception {
        final ReportRequest reportRequest = new ReportRequest("Workspackage", worksPackage.getActivitiesForEstimate(),
                createHeaderParams());
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            WorkspackagePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return PRINT;
    }

    public Map createHeaderParams() {
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        final List<WorksPackageDetails> worksPackageDetails = worksPackage.getWorksPackageDetails();
        final AbstractEstimate estimate = worksPackageDetails.get(0).getEstimate();
        final Boundary b = getTopLevelBoundary(estimate.getWard());
        reportParams
                .put("financialYear", abstractEstimateService.getCurrentFinancialYear(new Date()).getFinYearRange());
        reportParams.put("total", worksPackage.getTotalAmount());
        reportParams.put("cityName", b == null ? "" : b.getName());
        reportParams.put("workPackageName", worksPackage.getName());
        reportParams.put("worksPackageNumber", worksPackage.getWpNumber());
        reportParams.put("departmentName", worksPackage.getDepartment().getName());
        reportParams.put("tenderFileNumber", worksPackage.getTenderFileNumber());
        reportParams.put("estimateNumbers", getEstimateNumbers(worksPackage));
        return reportParams;
    }

    private String getEstimateNumbers(final WorksPackage wp) {
        String estimateNumbers = "";
        for (final WorksPackageDetails wpDetail : wp.getWorksPackageDetails())
            estimateNumbers = estimateNumbers.concat(wpDetail.getEstimate().getEstimateNumber()).concat(",");
        if (estimateNumbers.length() > 1)
            estimateNumbers = estimateNumbers.substring(0, estimateNumbers.length() - 1);
        return estimateNumbers;
    }

    protected Boundary getTopLevelBoundary(final Boundary boundary) {
        Boundary b = boundary;
        while (b != null && b.getParent() != null)
            b = b.getParent();
        return b;
    }

    public void getDesignation(final WorksPackage wp) {
        if (wp.getEgwStatus() != null && !"NEW".equalsIgnoreCase(wp.getEgwStatus().getCode())) {
            final String result = worksService.getEmpNameDesignation(wp.getState().getOwnerPosition(), wp.getState()
                    .getCreatedDate());
            if (result != null && !"@".equalsIgnoreCase(result)) {
                final String empName = result.substring(0, result.lastIndexOf('@'));
                final String designation = result.substring(result.lastIndexOf('@') + 1, result.length());
                setNextEmployeeName(empName);
                setNextDesignation(designation);
            }
        }
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public String getEditableDate() {
        return editableDate;
    }

    public void setEditableDate(final String editableDate) {
        this.editableDate = editableDate;
    }

    public String getCreatedBySelection() {
        return createdBySelection;
    }

    public void setCreatedBySelection(final String createdBySelection) {
        this.createdBySelection = createdBySelection;
    }

    public void setDepartmentService(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(final Integer empId) {
        this.empId = empId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(final String designation) {
        this.designation = designation;
    }

    public void setWorkspackageService(final WorksPackageService workspackageService) {
        this.workspackageService = workspackageService;
    }

    private PersonalInformation getEmployee() {
        if (worksPackage.getPreparedBy() == null)
            return employeeServiceOld.getEmpForUserId(worksService.getCurrentLoggedInUserId());
        else
            return worksPackage.getPreparedBy();
    }

    private Assignment getAssignment(final PersonalInformation pi) {
        if (worksPackage.getPreparedBy() == null)
            return employeeServiceOld.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
        else
            return employeeServiceOld.getAssignmentByEmpAndDate(new Date(), worksPackage.getPreparedBy()
                    .getIdPersonalInformation());
    }

    public String getPastDate() {
        return worksService.getWorksConfigValue("WORKS_PACKAGE_PASTDATE");
    }

    public String getCreatedBy() {
        return worksService.getWorksConfigValue("WORKS_PACKAGE_CREATEDBY");
    }

    public Long[] getEstId() {
        return estId;
    }

    public void setEstId(final Long[] estId) {
        this.estId = estId;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public List<AbstractEstimate> getAbstractEstimateList() {
        return abstractEstimateList;
    }

    public void setAbstractEstimateList(final List<AbstractEstimate> abstractEstimateList) {
        this.abstractEstimateList = abstractEstimateList;
    }

    public Money getWorktotalValue() {
        return worktotalValue;
    }

    public void setWorktotalValue(final Money worktotalValue) {
        this.worktotalValue = worktotalValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getSourcepage() {
        return sourcepage;
    }

    public void setSourcepage(final String sourcepage) {
        this.sourcepage = sourcepage;
    }

    public static String getPRINT() {
        return PRINT;
    }

    public InputStream getWorkspackagePDF() {
        return WorkspackagePDF;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public void setWorkspackagePDF(final InputStream workspackagePDF) {
        WorkspackagePDF = workspackagePDF;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public void setPackageWorkflowService(final WorkflowService<WorksPackage> workflow) {
        workflowService = workflow;
    }

    public List<org.egov.infstr.workflow.Action> getValidActions() {
        return workflowService.getValidActions(worksPackage);
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(final String actionName) {
        this.actionName = actionName;
    }

    public String getMessageKey() {
        return messageKey;
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

    public void setPersonalInformationService(final PersonalInformationService personalInformationService) {
        this.personalInformationService = personalInformationService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(final String packageNumber) {
        this.packageNumber = packageNumber;
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public String getLoggedInUserEmployeeCode() {
        return loggedInUserEmployeeCode;
    }

    public void setLoggedInUserEmployeeCode(final String loggedInUserEmployeeCode) {
        this.loggedInUserEmployeeCode = loggedInUserEmployeeCode;
    }

}