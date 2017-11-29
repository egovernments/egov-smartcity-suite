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

import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.tender.WorksPackageDetails;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksPackageService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.DateConversionUtil;
import org.egov.works.utils.WorksConstants;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Results({ @Result(name = WorksPackageAction.PRINT, type = "stream", location = "WorkspackagePDF", params = {
        "inputName", "WorkspackagePDF", "contentType", "application/pdf", "contentDisposition",
        "no-cache;filename=WorksPackage.pdf" }),
        @Result(name = WorksPackageAction.NEW, location = "worksPackage-new.jsp"),
        @Result(name = WorksPackageAction.EDIT, location = "worksPackage-edit.jsp"),
        @Result(name = WorksPackageAction.SUCCESS, location = "worksPackage-success.jsp") })
public class WorksPackageAction extends GenericWorkFlowAction {

    private static final long serialVersionUID = -6365331777546797839L;
    private WorksService worksService;
    private WorksPackage worksPackage = new WorksPackage();
    private String designation;
    private Integer empId;
    private Long[] estId;
    private String sourcepage;
    private Money worktotalValue;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    private WorksPackageService workspackageService;
    private AbstractEstimateService abstractEstimateService;
    private List<AbstractEstimate> abstractEstimateList = new ArrayList<AbstractEstimate>();
    private Long id;
    private String messageKey;
    private String nextEmployeeName;
    private String nextDesignation;
    private String packageNumber;
    private static final String DEPARTMENT_LIST = "departmentList";
    public static final String WORKSPACKAGE = "WorksPackage";

    public static final String PRINT = "print";
    private InputStream WorkspackagePDF;
    private ReportService reportService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<AbstractEstimate> worksPackageWorkflowService;

    public WorksPackageAction() {
        addRelatedEntity("department", Department.class);
    }

    @Override
    public void prepare() {
        super.prepare();
        if (id != null) {
            worksPackage = workspackageService.findById(id, false);
            worksPackage = workspackageService.merge(worksPackage);
        }
        if (id == null && packageNumber != null && StringUtils.isNotBlank(packageNumber))
            worksPackage = workspackageService.find("from WorksPackage where wpNumber=? and egwStatus.code='APPROVED'",
                    packageNumber);
        setupDropdownDataExcluding("department");

        if (worksPackage.getId() == null || worksPackage.getId() != null
                && (worksPackage.getEgwStatus().getCode()
                        .equalsIgnoreCase(WorksPackage.WorkPacakgeStatus.REJECTED.toString()) || worksPackage
                                .getEgwStatus().getCode().equalsIgnoreCase("NEW")))
            addDropdownData(DEPARTMENT_LIST, worksService.getAllDeptmentsForLoggedInUser());
        else
            addDropdownData(DEPARTMENT_LIST, Arrays.asList(worksPackage.getDepartment()));

        final Assignment latestAssignment = abstractEstimateService.getLatestAssignmentForCurrentLoginUser();
        if (latestAssignment != null) {
            approverDepartment = abstractEstimateService.getLatestAssignmentForCurrentLoginUser()
                    .getDepartment().getId().toString();
            if (worksPackage.getDepartment() == null)
                worksPackage.setDepartment(latestAssignment.getDepartment());
        }
    }

    @Action(value = "/tender/worksPackage-newform")
    public String newform() {
        return NEW;
    }

    @Action(value = "/tender/worksPackage-edit")
    public String edit() {
        // TODO:Fixme - commented out for time being since the validation not working properly
        /*
         * if (SOURCE_INBOX.equalsIgnoreCase(sourcepage)) { final User user =
         * userService.getUserById(worksService.getCurrentLoggedInUserId()); final boolean isValidUser =
         * worksService.validateWorkflowForUser(worksPackage, user); if (isValidUser) throw new ApplicationRuntimeException(
         * "Error: Invalid Owner - No permission to view this page."); } else
         */if (StringUtils.isEmpty(sourcepage))
            sourcepage = "search";

        abstractEstimateList = workspackageService.getAbStractEstimateListByWorksPackage(worksPackage);
        setWorktotalValue(abstractEstimateService.getWorkValueIncludingTaxesForEstList(abstractEstimateList));
        return EDIT;
    }

    @Action(value = "/tender/worksPackage-save")
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
        if (worksPackage.getId() == null && worksPackage.getEgwStatus() == null && abstractEstimateList.size() > 0) {
            validateFinancingSource(abstractEstimateList);
            validateEstimateForUniqueness();
        }
        validateWorksPackageDate();
        transitionWorkFlow(worksPackage);
        abstractEstimateService.applyAuditing(worksPackage.getState());
        workspackageService.setWorksPackageNumber(worksPackage,
                abstractEstimateService.getCurrentFinancialYear(worksPackage.getWpDate()));
        if (worksPackage.getEgwStatus() != null
                && worksPackage.getEgwStatus().getCode()
                        .equals(WorksPackage.WorkPacakgeStatus.APPROVED.toString()))
            worksPackage.setApprovedDate(new Date());
        worksPackage = workspackageService.persist(worksPackage);
        messageKey = "worksPackage." + workFlowAction;
        addActionMessage(getText(messageKey, "The Works Package was saved successfully"));
        if (WorksConstants.SAVE_ACTION.equals(workFlowAction))
            sourcepage = "inbox";

        return WorksConstants.SAVE_ACTION.equals(workFlowAction) ? EDIT : SUCCESS;
    }

    private void validateWorksPackageDate() {
        if (worksPackage.getWpDate() != null && DateConversionUtil.isBeforeByDate(new Date(), worksPackage.getWpDate()))
            throw new ValidationException(Arrays.asList(new ValidationError("invalid.wpDate", "invalid.wpDate")));
    }

    private void validateFinancingSource(final List<AbstractEstimate> estimateList) {
        Integer fundSourceId1;
        fundSourceId1 = abstractEstimateList.get(0).getFundSource().getId().intValue();
        for (int i = 1; i < abstractEstimateList.size(); i++)
            if (fundSourceId1 != abstractEstimateList.get(i).getFundSource().getId().intValue())
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

    private void transitionWorkFlow(final WorksPackage worksPackage) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position position = null;
        Assignment wfInitiator = null;

        wfInitiator = getWorkflowInitiator(worksPackage);

        if (WorksConstants.CANCEL_ACTION.equals(workFlowAction)) {
            if (wfInitiator.equals(userAssignment)) {
                worksPackage.transition().end().withSenderName(user.getName()).withComments(approverComments)
                        .withStateValue(WorksPackage.WorkPacakgeStatus.CANCELLED.toString()).withDateInfo(currentDate.toDate())
                        .withNextAction("END");
                worksPackage.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WORKSPACKAGE,
                        WorksPackage.WorkPacakgeStatus.CANCELLED.toString()));
            }
        } else if (WorksConstants.REJECT_ACTION.equals(workFlowAction)) {
            worksPackage.transition().progressWithStateCopy().withSenderName(user.getName()).withComments(approverComments)
                    .withStateValue(WorksPackage.WorkPacakgeStatus.REJECTED.toString()).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition()).withNextAction("");
            worksPackage.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WORKSPACKAGE,
                    WorksPackage.WorkPacakgeStatus.REJECTED.toString()));

        } else if (WorksConstants.SAVE_ACTION.equals(workFlowAction)) {
            if (worksPackage.getState() == null) {
                final WorkFlowMatrix wfmatrix = worksPackageWorkflowService.getWfMatrix(worksPackage.getStateType(), null,
                        null, getAdditionalRule(), currentState, null);
                worksPackage.transition().start().withSenderName(user.getName()).withComments(approverComments)
                        .withStateValue(wfmatrix.getCurrentState()).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition());
                worksPackage
                        .setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WORKSPACKAGE, "NEW"));
            }
        } else {
            if (null != approverPositionId && approverPositionId != -1)
                position = (Position) persistenceService.find("from Position where id=?", approverPositionId);
            if (worksPackage.getState() == null) {
                final WorkFlowMatrix wfmatrix = worksPackageWorkflowService.getWfMatrix(worksPackage.getStateType(), null,
                        null, getAdditionalRule(), currentState, null);
                worksPackage.transition().start().withSenderName(user.getName()).withComments(approverComments)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(position)
                        .withNextAction(wfmatrix.getNextAction());
                worksPackage
                        .setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WORKSPACKAGE, wfmatrix.getNextStatus()));
            } else {
                final WorkFlowMatrix wfmatrix = worksPackageWorkflowService.getWfMatrix(worksPackage.getStateType(), null,
                        null, getAdditionalRule(), worksPackage.getCurrentState().getValue(), null);
                if (wfmatrix.getNextAction() != null && wfmatrix.getNextAction().equalsIgnoreCase("END"))
                    worksPackage.transition().end().withSenderName(user.getName()).withComments(approverComments)
                            .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                            .withNextAction(wfmatrix.getNextAction());
                else
                    worksPackage.transition().progressWithStateCopy().withSenderName(user.getName()).withComments(approverComments)
                            .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(position)
                            .withNextAction(wfmatrix.getNextAction());
                worksPackage
                        .setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WORKSPACKAGE, wfmatrix.getNextStatus()));
            }

        }
        if (!(WorksConstants.CANCEL_ACTION.equals(workFlowAction) || WorksConstants.SAVE_ACTION.equals(workFlowAction)))
            setApproverAndDesignation(worksPackage);
    }

    private Assignment getWorkflowInitiator(final WorksPackage worksPackage) {
        Assignment wfInitiator;
        if (worksPackage.getCreatedBy() == null)
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(worksService.getCurrentLoggedInUserId());
        else
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(worksPackage.getCreatedBy().getId());
        return wfInitiator;
    }

    @Override
    public StateAware getModel() {
        return worksPackage;
    }

    public void setModel(final WorksPackage worksPackage) {
        this.worksPackage = worksPackage;
    }

    protected void populateEstimatesList(final Long[] estimateID) {
        if (estimateID != null && estimateID.length > 0) {
            abstractEstimateList = abstractEstimateService.getAbEstimateListById(StringUtils.join(estId, "`~`"));
            setWorktotalValue(abstractEstimateService.getWorkValueIncludingTaxesForEstList(abstractEstimateList));
        } else
            throw new ValidationException(Arrays.asList(new ValidationError("estimates.null",
                    "estimates.null")));
        setWPDetails();
    }

    private void setWPDetails() {
        if (!abstractEstimateList.isEmpty())
            for (final AbstractEstimate ab : abstractEstimateList) {
                final WorksPackageDetails wpDetails = new WorksPackageDetails();
                wpDetails.setEstimate(ab);
                wpDetails.setWorksPackage(worksPackage);
                // TODO:Fixme - Manually setting auditable properties by time being since HibernateEventListener is not getting
                // triggered on update of workspackage for child objects
                final User user = worksService.getCurrentLoggedInUser();
                wpDetails.setCreatedBy(user);
                wpDetails.setCreatedDate(new Date());
                wpDetails.setModifiedBy(user);
                wpDetails.setModifiedDate(new Date());
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
     * print pdf *
     * @throws JRException ,Exception
     */
    @SkipValidation
    @Action(value = "/tender/worksPackage-viewWorksPackagePdf")
    public String viewWorksPackagePdf() throws JRException, Exception {
        final ReportRequest reportRequest = new ReportRequest("Workspackage", worksPackage.getActivitiesForEstimate(),
                createHeaderParams());
        final ReportOutput reportOutput = reportService.createReport(reportRequest);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            WorkspackagePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        return PRINT;
    }

    private Map createHeaderParams() {
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

    private void setApproverAndDesignation(final WorksPackage worksPackage) {
        /* start for customizing workflow message display */
        if (worksPackage.getEgwStatus() != null
                && !"NEW".equalsIgnoreCase(worksPackage.getEgwStatus().getCode())) {
            Date date = new Date();
            if (worksPackage.getState().getCreatedDate() != null)
                date = worksPackage.getState().getCreatedDate();
            final String result = worksService.getEmpNameDesignation(worksPackage.getState().getOwnerPosition(),
                    date);
            if (result != null && !"@".equalsIgnoreCase(result)) {
                final String empName = result.substring(0, result.lastIndexOf('@'));
                final String designation = result.substring(result.lastIndexOf('@') + 1, result.length());
                setNextEmployeeName(empName);
                setNextDesignation(designation);
            }
        }
        /* end */
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
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

    public void setUserService(final UserService userService) {
    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(final String packageNumber) {
        this.packageNumber = packageNumber;
    }

}