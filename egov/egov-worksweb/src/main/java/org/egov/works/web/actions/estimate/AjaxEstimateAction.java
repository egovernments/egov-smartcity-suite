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
package org.egov.works.web.actions.estimate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.service.EisUtilService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.masters.Overhead;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.script.ScriptContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({
        @Result(name = AjaxEstimateAction.SUBCATEGORIES, location = "ajaxEstimate-subcategories.jsp"),
        @Result(name = AjaxEstimateAction.OVERHEADS, location = "ajaxEstimate-overheads.jsp"),
        @Result(name = AjaxEstimateAction.USERS_IN_DEPT, location = "ajaxEstimate-usersInDept.jsp")
})
public class AjaxEstimateAction extends BaseFormAction {

    private static final long serialVersionUID = 4566034960012106080L;

    private static final Logger logger = Logger.getLogger(AjaxEstimateAction.class);

    public static final String USERS_IN_DEPT = "usersInDept";
    private static final String DESIGN_FOR_EMP = "designForEmp";
    public static final String SUBCATEGORIES = "subcategories";
    public static final String OVERHEADS = "overheads";
    private static final String WORKFLOW_USER_LIST = "workflowUsers";
    private static final String WORKFLOW_DESIG_LIST = "workflowDesignations";
    private static final String CHANGE_DEPARTMENT = "changeDepartment";
    private static final String ESTIMATE_NUMBER_SEARCH_RESULTS = "estimateNoSearchResults";
    private static final String PROJECT_CODE_SEARCH_RESULTS = "projectCodeSearchResults";
    private static final String DRAFT_ESTIMATE_NUMBER_SEARCH_RESULTS = "draftEstimateNoSearchResults";
    private Long executingDepartment;
    private Long empID;
    @Autowired
    private AssignmentService assignmentService;
    private List usersInExecutingDepartment;
    private Assignment assignment;
    private List subCategories;
    private Long category;
    private boolean isSkipDeptChange;

    private Date estDate;
    private List<Overhead> overheads;
    private List<Overhead> validOverheads;
    private Overhead overhead;
    private String uomVal;
    private Double rate;
    private List workflowUsers;
    private Integer departmentId;
    private Integer designationId;
    private Integer wardId;
    private List workflowKDesigList;
    private String scriptName;
    private String stateName;
    private Long estimateId;
    private WorksService worksService;
    private EisUtilService eisService;
    private AbstractEstimateService abstractEstimateService;
    private Money worktotalValue;
    private String query;
    private String wpNumber = "";
    private boolean isVoucherExists = false;
    private EgovCommon egovCommon;
    private List<String> estimateNumberSearchList = new LinkedList<String>();
    private List<String> projectCodeList = new LinkedList<String>();
    private List<String> draftsEstimateNumberSearchList = new LinkedList<String>();
    private String estimateNum = "";
    private boolean isCancelEstCopyExists = false;
    private Integer approverDepartmentId;
    private List<Object> approverList;
    private String employeeCode;
    private Long estimateTemplateId;
    private String estimateIds;
    private String sorCodes = "";
    private Long woId;
    private String woNumber = "";
    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;
    private List<String> estimateNoList = new LinkedList<String>();
    private BigDecimal estimateAmount;
    @Autowired
    private ScriptService scriptService;

    @Override
    public String execute() {
        return SUCCESS;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public String designationForUser() {
        try {
            assignment = assignmentService.getPrimaryAssignmentForEmployee(empID);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("user.find.error", e);
        }
        return DESIGN_FOR_EMP;
    }

    /**
     * This auto-complete method is used to fetch estimate numbers for which final bill is not created, and Year end appropriation
     * is not done in the current financial year and for which Financial sanction is not done in the current financial year .
     *
     * @return
     */
    public String searchEstimateNumberForYearendAppr() {
        CFinancialYear currentFinYear;
        currentFinYear = financialYearHibernateDAO.getFinancialYearByDate(new Date());
        String strquery = "";
        if (!StringUtils.isEmpty(query)) {
            strquery = "select ae.estimateNumber from AbstractEstimate ae where ae.projectCode.egwStatus.code!='CLOSED' and ae.depositCode is null and ae.egwStatus.code='ADMIN_SANCTIONED' and ae.estimateNumber like  '%'||?||'%'  "
                    + "and NOT EXISTS (select 'true' from  AbstractEstimateAppropriation aea where aea.abstractEstimate.id=ae.id and aea.budgetUsage.releasedAmount=0 and aea.budgetUsage.financialYearId=?) "
                    + "and NOT EXISTS (select 'true' from MBHeader as mbh left outer join mbh.egBillregister egbr where mbh.workOrderEstimate.estimate.id=ae.id"
                    + " and (egbr.billtype='Final Bill' and egbr.billstatus<>'CANCELLED'))"
                    + "  and NOT EXISTS (select 'true' from MultiyearEstimateApprDetail myea where myea.estimate.id=ae.id "
                    + "and myea.multiyearEstimateAppr.status.code<>'CANCELLED' and myea.multiyearEstimateAppr.financialYear.id=?)  ";

            estimateNoList = getPersistenceService().findAllBy(strquery, query.toUpperCase(),
                    Integer.valueOf(currentFinYear.getId().toString()), currentFinYear.getId());

        }
        return "estimateNumSearchResults";
    }

    @Action(value = "/estimate/ajaxEstimate-usersInExecutingDepartment")
    public String usersInExecutingDepartment() {
        try {
            final HashMap<String, Object> criteriaParams = new HashMap<String, Object>();
            if (executingDepartment != null && executingDepartment != -1)
                criteriaParams.put("departmentId", executingDepartment.toString());
            if (StringUtils.isNotBlank(employeeCode))
                criteriaParams.put("code", employeeCode);
            if (executingDepartment == null || executingDepartment == -1)
                usersInExecutingDepartment = Collections.EMPTY_LIST;
            // TODO:Fixeme - commented out for time being since there is no corresponding API with latest EmployeeView is not
            // provided from EIS
            /*
             * else usersInExecutingDepartment = eisService.getEmployeeInfoList(criteriaParams);
             */
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("user.find.error", e);
        }
        return USERS_IN_DEPT;
    }

    @Action(value = "/estimate/ajaxEstimate-subcategories")
    public String subcategories() {
        subCategories = getPersistenceService().findAllBy("from EgwTypeOfWork where parentid.id=?", category);
        return SUBCATEGORIES;
    }

    public String isSkipDepartmentChange() {
        Department department = null;
        if (departmentId != null && departmentId != -1)
            department = (Department) getPersistenceService().find("from Department where id=?", departmentId);
        final String departmentCodes = worksService.getWorksConfigValue("REAPPROPRIATION_DEPARTMENTS");
        isSkipDeptChange = true;
        if (department != null && departmentCodes != null)
            for (final String dept : departmentCodes.split(","))
                if (dept.equals(department.getCode())) {
                    isSkipDeptChange = false;
                    break;
                }
        return CHANGE_DEPARTMENT;
    }

    @Action(value = "/estimate/ajaxEstimate-overheads")
    public String overheads() {
        overheads = getPersistenceService().findAllByNamedQuery(Overhead.OVERHEADS_BY_DATE, estDate, estDate);
        return OVERHEADS;
    }

    @Action(value = "/estimate/ajaxEstimate-getUOMFactor")
    public String getUOMFactor() {
        Integer result = 1;

        final Map<String, Integer> exceptionaSorMap = worksService.getExceptionSOR();
        if (exceptionaSorMap.containsKey(uomVal))
            result = exceptionaSorMap.get(uomVal);

        final Double finalRate = rate / result;

        final String outStr = finalRate.toString();
        getServletResponse().setContentType("text/xml");
        getServletResponse().setHeader("Cache-Control", "no-cache");
        try {
            getServletResponse().getWriter().write(outStr);
        } catch (final IOException ioex) {
            logger.error("Error while writing to response --from getByResponseAware()");
        }
        return null;
    }

    public String getWorkFlowUsers() {
        if (designationId != -1) {
            final HashMap<String, Object> paramMap = new HashMap<String, Object>();
            if (departmentId != null && departmentId != -1)
                paramMap.put("departmentId", departmentId.toString());
            if (wardId != null && wardId != -1)
                paramMap.put("boundaryId", wardId.toString());

            paramMap.put("designationId", designationId.toString());
            final List roleList = worksService.getWorksRoles();
            if (roleList != null)
                paramMap.put("roleList", roleList);
            workflowUsers = eisService.getEmployeeInfoList(paramMap);
        }
        return WORKFLOW_USER_LIST;
    }

    public String getPositionByPassingDesigId() {
        if (designationId != null && designationId != -1) {

            final HashMap<String, Object> paramMap = new HashMap<String, Object>();
            if (approverDepartmentId != null && approverDepartmentId != -1)
                paramMap.put("departmentId", approverDepartmentId.toString());
            paramMap.put("designationId", designationId.toString());
            final List roleList = worksService.getWorksRoles();
            if (roleList != null)
                paramMap.put("roleList", roleList);
            approverList = new ArrayList<Object>();
            final List<? extends Object> empList = eisService.getEmployeeInfoList(paramMap);
            for (final Object emp : empList)
                approverList.add(emp);
        }
        return "workFlowApprovers";
    }

    public String getDesgByDeptAndType() {
        workflowKDesigList = new ArrayList<Designation>();
        String departmentName = "";
        Department department = null;
        if (departmentId != -1) {
            department = (Department) getPersistenceService().find("from Department where id=?", departmentId);
            departmentName = department.getName();
        }
        Designation designation = null;
        AbstractEstimate abstractEstimate = null;
        if (estimateId != null)
            abstractEstimate = abstractEstimateService.findById(estimateId, false);

        final ScriptContext scriptContext = ScriptService.createContext("state", stateName, "department",
                departmentName, "wfItem", abstractEstimate);
        final List<String> list = (List<String>) scriptService.executeScript(scriptName, scriptContext);

        for (final String desgName : list)
            if (desgName.trim().length() != 0)
                try {
                    designation = new DesignationMasterDAO().getDesignationByDesignationName(desgName);
                    workflowKDesigList.add(designation);
                } catch (final NoSuchObjectException e) {
                    logger.error(e);
                }
        return WORKFLOW_DESIG_LIST;
    }

    public String validateEstimateForCancel() {
        woNumber = "";
        if (woNumber.equals("")) {
            wpNumber = (String) getPersistenceService().find(
                    "select wpd.worksPackage.wpNumber from WorksPackageDetails wpd where wpd.estimate.id=? "
                            + "and wpd.estimate.egwStatus.code=? and wpd.worksPackage.egwStatus.code<>?",
                    estimateId,
                    AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString(),
                    WorksPackage.WorkPacakgeStatus.CANCELLED.toString());
            if (wpNumber == null)
                wpNumber = "";
            final Long projectCodeId = (Long) getPersistenceService().find(
                    "select ae.projectCode.id from AbstractEstimate ae where ae.id=?", estimateId);
            final List<Map<String, String>> expenditureDetails = egovCommon.getExpenditureDetailsforProject(
                    projectCodeId, new Date());
            if (expenditureDetails != null && !expenditureDetails.isEmpty())
                isVoucherExists = true;
        }

        return "cancelEstimate";
    }

    /*
     * Autocomplete of Admin sanctioned Estimate nos for Cancel Estimate screen
     */
    public String searchEstimateNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct(ae.estimateNumber) from AbstractEstimate ae where ae.parent is null and UPPER(ae.estimateNumber) like '%'||?||'%' "
                    + " and ae.egwStatus.code = ? )";
            params.add(query.toUpperCase());
            params.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
            estimateNumberSearchList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return ESTIMATE_NUMBER_SEARCH_RESULTS;
    }

    /*
     * Autocomplete for estimates in Drafts - Planend Estimate Report
     */
    public String searchEstimateNumberForDraftEstimates() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct(ae.estimateNumber) from AbstractEstimate ae where ae.parent is null and UPPER(ae.estimateNumber) like '%'||?||'%' "
                    + " and ae.egwStatus.code = 'NEW' )";
            params.add(query.toUpperCase());

            draftsEstimateNumberSearchList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return DRAFT_ESTIMATE_NUMBER_SEARCH_RESULTS;
    }

    /*
     * Autocomplete of Admin sanctioned Project codes for Cancel Estimate screen
     */
    public String searchProjectCodes() {
        if (!StringUtils.isEmpty(query)) {
            String strquery = "";
            final ArrayList<Object> params = new ArrayList<Object>();
            strquery = "select distinct(ae.projectCode.code) from AbstractEstimate ae where ae.parent is null and upper(ae.projectCode.code) like '%'||?||'%'"
                    + " and ae.egwStatus.code=? and ae.projectCode.isActive=1";
            params.add(query.toUpperCase());
            params.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
            projectCodeList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return PROJECT_CODE_SEARCH_RESULTS;
    }

    /**
     * This method is to validate if the estimate number of the cancelled estimate has been already copied or not
     *
     * @return
     */
    public String validateCancelledEstForCopy() {
        final String estNo = estimateNum.substring(0, estimateNum.length() - 2);
        final String cancelledEst = (String) getPersistenceService().find(
                "select est.estimateNumber from AbstractEstimate est where est.estimateNumber= ?", estNo);
        if (cancelledEst != null)
            isCancelEstCopyExists = true;
        return "copyCancelledEst";
    }

    /**
     * Convenience method to get the response
     *
     * @return current response
     */

    public HttpServletResponse getServletResponse() {
        return ServletActionContext.getResponse();
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public List getUsersInExecutingDepartment() {
        return usersInExecutingDepartment;
    }

    public void setAssignmentService(final AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setExecutingDepartment(final Long executingDepartment) {
        this.executingDepartment = executingDepartment;
    }

    public void setCategory(final Long category) {
        this.category = category;
    }

    public List getSubCategories() {
        return subCategories;
    }

    public List<Overhead> getOverheads() {
        return overheads;
    }

    public void setOverheads(final List<Overhead> overheads) {
        this.overheads = overheads;
    }

    public Date getEstDate() {
        return estDate;
    }

    public void setEstDate(final Date estDate) {
        this.estDate = estDate;
    }

    public Overhead getOverhead() {
        return overhead;
    }

    public void setOverhead(final Overhead overhead) {
        this.overhead = overhead;
    }

    public List<Overhead> getValidOverheads() {
        return validOverheads;
    }

    public void setValidOverheads(final List<Overhead> validOverheads) {
        this.validOverheads = validOverheads;
    }

    public Long getEmpID() {
        return empID;
    }

    public void setEmpID(final Long empID) {
        this.empID = empID;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(final Assignment assignment) {
        this.assignment = assignment;
    }

    public String getUomVal() {
        return uomVal;
    }

    public void setUomVal(final String uomVal) {
        this.uomVal = uomVal;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(final Double rate) {
        this.rate = rate;
    }

    public List getWorkflowUsers() {
        return workflowUsers;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public EisUtilService getEisService() {
        return eisService;
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public Integer getDesignationId() {
        return designationId;
    }

    public List getWorkflowKDesigList() {
        return workflowKDesigList;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(final String scriptName) {
        this.scriptName = scriptName;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public void setDesignationId(final Integer designationId) {
        this.designationId = designationId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(final String stateName) {
        this.stateName = stateName;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public void setEstimateId(final Long estimateId) {
        this.estimateId = estimateId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(final Integer wardId) {
        this.wardId = wardId;
    }

    public Money getWorktotalValue() {
        return worktotalValue;
    }

    public void setWorktotalValue(final Money worktotalValue) {
        this.worktotalValue = worktotalValue;
    }

    public boolean getIsSkipDeptChange() {
        return isSkipDeptChange;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public String getWpNumber() {
        return wpNumber;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public boolean getIsVoucherExists() {
        return isVoucherExists;
    }

    public List<String> getEstimateNumberSearchList() {
        return estimateNumberSearchList;
    }

    public List<String> getProjectCodeList() {
        return projectCodeList;
    }

    public List<String> getDraftsEstimateNumberSearchList() {
        return draftsEstimateNumberSearchList;
    }

    public void setDraftsEstimateNumberSearchList(final List<String> draftsEstimateNumberSearchList) {
        this.draftsEstimateNumberSearchList = draftsEstimateNumberSearchList;
    }

    public String getEstimateNum() {
        return estimateNum;
    }

    public void setEstimateNum(final String estimateNum) {
        this.estimateNum = estimateNum;
    }

    public boolean getIsCancelEstCopyExists() {
        return isCancelEstCopyExists;
    }

    public Integer getApproverDepartmentId() {
        return approverDepartmentId;
    }

    public void setApproverDepartmentId(final Integer approverDepartmentId) {
        this.approverDepartmentId = approverDepartmentId;
    }

    public List<? extends Object> getApproverList() {
        return approverList;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(final String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getSorCodes() {
        return sorCodes;
    }

    public void setSorCodes(final String sorCodes) {
        this.sorCodes = sorCodes;
    }

    public Long getEstimateTemplateId() {
        return estimateTemplateId;
    }

    public void setEstimateTemplateId(final Long estimateTemplateId) {
        this.estimateTemplateId = estimateTemplateId;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(final Long woId) {
        this.woId = woId;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public String getEstimateIds() {
        return estimateIds;
    }

    public void setEstimateIds(final String estimateIds) {
        this.estimateIds = estimateIds;
    }

    public String getWoNumber() {
        return woNumber;
    }

    public void setWoNumber(final String woNumber) {
        this.woNumber = woNumber;
    }

    public List<String> getEstimateNoList() {
        return estimateNoList;
    }

    public BigDecimal getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(final BigDecimal estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

}
