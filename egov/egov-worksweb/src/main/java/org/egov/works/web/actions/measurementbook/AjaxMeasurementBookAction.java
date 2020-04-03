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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.EmployeeView;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

public class AjaxMeasurementBookAction extends BaseFormAction {

    private static final long serialVersionUID = 3154955519825652739L;
    private static final Logger logger = Logger.getLogger(AjaxMeasurementBookAction.class);
    private static final String USERS_IN_DEPT = "usersInDept";
    private static final String DESIGN_FOR_EMP = "designForEmp";
    private static final String WORK_ORDER_DETAILS = "workOrderDetails";
    private static final String ACTIVITY_DETAILS = "activityDetails";
    private static final String WORKFLOW_USER_LIST = "workflowUsers";
    private static final String WORKFLOW_DESIG_LIST = "workflowDesignations";
    private static final String CHANGE_QUANTITY = "CHANGE_QUANTITY";

    private MeasurementBookService measurementBookService;
    @Autowired
    private AssignmentService assignmentService;
    private Assignment assignment;
    private Long empID;
    private Long executingDepartment;
    private List<EmployeeView> usersInExecutingDepartment;
    private WorkOrder workOrder;
    private String workOrderNumber;
    private Long woActivityId;
    private Long mbHeaderId;
    private WorkOrderActivity workOrderActivity;
    private Double prevCulmEntry;
    private Double totalEstQuantity;

    // -----------------------Manual Workflow ----------------------------
    private EisUtilService eisService;
    private Integer departmentId;
    private Integer designationId;
    private String scriptName;
    private String stateName;
    private Long modelId;
    private String modelType;
    private Integer wardId;
    private List<Designation> workflowKDesigList;
    private List<EmployeeView> workflowUsers;
    private PersonalInformationService personalInformationService;
    private WorksService worksService;
    private String activityRemarks;
    // -------------------------------------------------------------------

    private String query = "";
    private List<AbstractEstimate> estimateList = new LinkedList<>();
    private List<WorkOrder> workOrderList = new LinkedList<>();
    private List<MBHeader> mbHeaderList = new LinkedList<>();
    private Long estId;
    private Date latestMBDate;
    private Long woId;
    @Autowired
    private ScriptService scriptService;

    @PersistenceContext
    private EntityManager entityManager;

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

    public String usersInExecutingDepartment() {
        try {
            final HashMap<String, Object> criteriaParams = new HashMap<>();
            criteriaParams.put("departmentId", executingDepartment);
            if (executingDepartment == null || executingDepartment == -1)
                usersInExecutingDepartment = Collections.emptyList();
            else
                usersInExecutingDepartment = personalInformationService.getListOfEmployeeViewBasedOnCriteria(
                        criteriaParams, -1, -1);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("user.find.error", e);
        }
        logger.info(
                "Success ajax call to 'usersInExecutingDepartment' ----------------------------------------------------------");
        return USERS_IN_DEPT;
    }

    public String workOrderDetails() {
        try {
            final List<WorkOrder> results = entityManager
                    .createQuery("from WorkOrder where workOrderNumber = :workOrderNumber", WorkOrder.class)
                    .setParameter("workOrderNumber", workOrderNumber)
                    .getResultList();
            workOrder = results.isEmpty() ? null : results.get(0);
            if (workOrder != null) {
                final HashMap<String, Object> criteriaParams = new HashMap<>();
                criteriaParams.put("departmentId", executingDepartment);
                criteriaParams.put("isPrimary", "Y");
                if (executingDepartment == null || executingDepartment == -1)
                    usersInExecutingDepartment = Collections.emptyList();
                else
                    usersInExecutingDepartment = personalInformationService.getListOfEmployeeViewBasedOnCriteria(
                            criteriaParams, -1, -1);
            }
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("workorder.find.error", e);
        }
        logger.info("Success ajax call to 'workOrderDetails' ----------------------------------------------------------");
        return WORK_ORDER_DETAILS;
    }

    public String activityDetails() {
        prevCulmEntry = null;
        activityRemarks = "";
        try {
            workOrderActivity = entityManager.find(WorkOrderActivity.class, woActivityId);
            prevCulmEntry = measurementBookService.prevCumulativeQuantityIncludingCQ(woActivityId, mbHeaderId,
                    workOrderActivity.getActivity().getId(), workOrderActivity.getWorkOrderEstimate().getWorkOrder());
            if (modelType != null && modelType.equalsIgnoreCase("MB"))
                totalEstQuantity = measurementBookService.totalEstimatedQuantity(woActivityId, null, workOrderActivity
                        .getActivity().getId(), workOrderActivity.getWorkOrderEstimate().getWorkOrder()); // This
            // considers
            // work
            // order
            // activities
            // where
            // the
            // associated
            // Est/REs
            // are
            // approved
            else
                totalEstQuantity = measurementBookService.totalEstimatedQuantityForRE(woActivityId, null,
                        workOrderActivity.getActivity().getId(), workOrderActivity.getWorkOrderEstimate()
                                .getWorkOrder()); // This considers work order
            // activities where the
            // associated Est/REs are not
            // cancelled
            final List<WorkOrderActivity> results = entityManager.createQuery(new StringBuffer("from WorkOrderActivity")
                    .append(" where activity.parent.id = :parentId and workOrderEstimate.estimate.egwStatus.code != 'CANCELLED'  ")
                    .toString(), WorkOrderActivity.class)
                    .setParameter("parentId", workOrderActivity.getActivity().getId())
                    .getResultList();
            final WorkOrderActivity revWorkOrderActivity = results.isEmpty() ? null : results.get(0);
            if (revWorkOrderActivity != null)
                activityRemarks = CHANGE_QUANTITY;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("activity.find.error", e);
        }

        return ACTIVITY_DETAILS;
    }

    public String getWorkFlowUsers() {
        if (designationId != -1) {
            final HashMap<String, Object> paramMap = new HashMap<>();
            if (departmentId != null && departmentId != -1)
                paramMap.put("departmentId", departmentId.toString());
            if (wardId != null && wardId != -1)
                paramMap.put("boundaryId", wardId.toString());

            paramMap.put("designationId", designationId.toString());
            final List<String> roleList = worksService.getWorksRoles();
            if (roleList != null)
                paramMap.put("roleList", roleList);
            workflowUsers = eisService.getEmployeeInfoList(paramMap);
        }
        return WORKFLOW_USER_LIST;
    }

    @SuppressWarnings("unchecked")
    public String getDesgByDeptAndType() {
        workflowKDesigList = new ArrayList<>();
        String departmentName = "";
        Department department = null;
        if (departmentId != -1) {
            department = entityManager.find(Department.class, departmentId);
            departmentName = department.getName();
        }
        Designation designation = null;
        MBHeader mbHeader = null;
        if (modelId != null)
            mbHeader = entityManager.find(MBHeader.class, modelId);
        final ScriptContext scriptContext = ScriptService.createContext("state", stateName, "department",
                departmentName, "wfItem", mbHeader);
        final List<String> list = (List<String>) scriptService.executeScript(scriptName, scriptContext);
        // Script validScript = (Script)
        // persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName).get(0);
        // List<String> list = (List<String>)
        // validScript.eval(Script.createContext("state",stateName,"department",departmentName,"wfItem",mbHeader));

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

    public String searchEstimateNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select woe.estimate")
                    .append(" from WorkOrderEstimate woe")
                    .append(" where woe.workOrder.parent is null and woe.estimate.estimateNumber like '%'||:estimateNumber||'%' ")
                    .append(" and woe.id in (select distinct mbh.workOrderEstimate.id from MBHeader mbh where mbh.egwStatus.code <> :statusCode )");
            estimateList = entityManager.createQuery(strquery.toString(), AbstractEstimate.class)
                    .setParameter("estimateNumber", query.toUpperCase())
                    .setParameter("statusCode", "NEW")
                    .getResultList();
        }
        return "estimateNoSearchResults";
    }

    public String searchWorkOrderNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct mbh.workOrder")
                    .append(" from MBHeader mbh")
                    .append(" where mbh.workOrder.parent is null and mbh.workOrder.workOrderNumber like '%'||:workOrderNumber||'%' ")
                    .append(" and mbh.egwStatus.code <> :statusCode ");

            workOrderList = entityManager.createQuery(strquery.toString(), WorkOrder.class)
                    .setParameter("workOrderNumber", query.toUpperCase())
                    .setParameter("statusCode", "NEW")
                    .getResultList();
        }
        return "workOrderNoSearchResults";
    }

    public String searchMbRefNo() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append(
                    " from MBHeader mbh where mbh.mbRefNo like '%'||:mbRefNo||'%' and mbh.egwStatus.code <> :statusCode ");
            mbHeaderList = entityManager.createQuery(strquery.toString(), MBHeader.class)
                    .setParameter("mbRefNo", query.toUpperCase())
                    .setParameter("statusCode", "NEW")
                    .getResultList();
        }
        return "mbRefNoSearchResults";
    }

    public String getLatestMBDateforSelectedEstimate() {
        latestMBDate = measurementBookService.getLastMBCreatedDate(woId, estId);
        return "mblatestDateResult";
    }

    public void setAssignmentService(final AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public void setEmpID(final Long empID) {
        this.empID = empID;
    }

    public List<EmployeeView> getUsersInExecutingDepartment() {
        return usersInExecutingDepartment;
    }

    public void setExecutingDepartment(final Long executingDepartment) {
        this.executingDepartment = executingDepartment;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public void setWoActivityId(final Long woActivityId) {
        this.woActivityId = woActivityId;
    }

    public WorkOrderActivity getWorkOrderActivity() {
        return workOrderActivity;
    }

    public Double getPrevCulmEntry() {
        return prevCulmEntry;
    }

    public void setMbHeaderId(final Long mbHeaderId) {
        this.mbHeaderId = mbHeaderId;
    }

    public void setMeasurementBookService(final MeasurementBookService measurementBookService) {
        this.measurementBookService = measurementBookService;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDesignationId() {
        return designationId;
    }

    public void setDesignationId(final Integer designationId) {
        this.designationId = designationId;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(final String scriptName) {
        this.scriptName = scriptName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(final String stateName) {
        this.stateName = stateName;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(final Long modelId) {
        this.modelId = modelId;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(final String modelType) {
        this.modelType = modelType;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(final Integer wardId) {
        this.wardId = wardId;
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public List<Designation> getWorkflowKDesigList() {
        return workflowKDesigList;
    }

    public List<EmployeeView> getWorkflowUsers() {
        return workflowUsers;
    }

    public void setPersonalInformationService(final PersonalInformationService personalInformationService) {
        this.personalInformationService = personalInformationService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<AbstractEstimate> getEstimateList() {
        return estimateList;
    }

    public void setEstimateList(final List<AbstractEstimate> estimateList) {
        this.estimateList = estimateList;
    }

    public List<WorkOrder> getWorkOrderList() {
        return workOrderList;
    }

    public void setWorkOrderList(final List<WorkOrder> workOrderList) {
        this.workOrderList = workOrderList;
    }

    public List<MBHeader> getMbHeaderList() {
        return mbHeaderList;
    }

    public void setMbHeaderList(final List<MBHeader> mbHeaderList) {
        this.mbHeaderList = mbHeaderList;
    }

    public Double getTotalEstQuantity() {
        return totalEstQuantity;
    }

    public String getActivityRemarks() {
        return activityRemarks;
    }

    public Long getEstId() {
        return estId;
    }

    public void setEstId(final Long estId) {
        this.estId = estId;
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
