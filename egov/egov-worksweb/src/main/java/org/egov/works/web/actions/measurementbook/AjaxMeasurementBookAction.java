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
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.commons.exception.NoSuchObjectException;
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

import javax.script.ScriptContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
    private List usersInExecutingDepartment;
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
    private List workflowKDesigList;
    private List workflowUsers;
    private PersonalInformationService personalInformationService;
    private WorksService worksService;
    private String activityRemarks;
    // -------------------------------------------------------------------

    private String query = "";
    private List<AbstractEstimate> estimateList = new LinkedList<AbstractEstimate>();
    private List<WorkOrder> workOrderList = new LinkedList<WorkOrder>();
    private List<MBHeader> mbHeaderList = new LinkedList<MBHeader>();
    private Long estId;
    private Date latestMBDate;
    private Long woId;
    @Autowired
    private ScriptService scriptService;

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
            final HashMap<String, Object> criteriaParams = new HashMap<String, Object>();
            criteriaParams.put("departmentId", executingDepartment);
            if (executingDepartment == null || executingDepartment == -1)
                usersInExecutingDepartment = Collections.EMPTY_LIST;
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
            workOrder = (WorkOrder) persistenceService.find("from WorkOrder where workOrderNumber=?", workOrderNumber);
            if (workOrder != null) {
                final HashMap<String, Object> criteriaParams = new HashMap<String, Object>();
                criteriaParams.put("departmentId", executingDepartment);
                criteriaParams.put("isPrimary", "Y");
                if (executingDepartment == null || executingDepartment == -1)
                    usersInExecutingDepartment = Collections.EMPTY_LIST;
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
            workOrderActivity = (WorkOrderActivity) persistenceService.find("from WorkOrderActivity where id=?",
                    woActivityId);
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
            final WorkOrderActivity revWorkOrderActivity = (WorkOrderActivity) persistenceService
                    .find("from WorkOrderActivity where activity.parent.id=? and workOrderEstimate.estimate.egwStatus.code !='CANCELLED'  ",
                            workOrderActivity.getActivity().getId());
            if (revWorkOrderActivity != null)
                activityRemarks = CHANGE_QUANTITY;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("activity.find.error", e);
        }

        return ACTIVITY_DETAILS;
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

    public String getDesgByDeptAndType() {
        workflowKDesigList = new ArrayList<Designation>();
        String departmentName = "";
        Department department = null;
        if (departmentId != -1) {
            department = (Department) getPersistenceService().find("from Department where id=?", departmentId);
            departmentName = department.getName();
        }
        Designation designation = null;
        MBHeader mbHeader = null;
        if (modelId != null)
            mbHeader = (MBHeader) getPersistenceService().find("from MBHeader where id=?", modelId);
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
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select woe.estimate from WorkOrderEstimate woe where woe.workOrder.parent is null and woe.estimate.estimateNumber like '%'||?||'%' "
                    + " and woe.id in (select distinct mbh.workOrderEstimate.id from MBHeader mbh where mbh.egwStatus.code <> ? )";
            params.add(query.toUpperCase());
            params.add("NEW");
            estimateList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "estimateNoSearchResults";
    }

    public String searchWorkOrderNumber() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = "select distinct mbh.workOrder from MBHeader mbh where mbh.workOrder.parent is null and mbh.workOrder.workOrderNumber like '%'||?||'%' "
                    + "and mbh.egwStatus.code <> ? ";
            params.add(query.toUpperCase());
            params.add("NEW");
            workOrderList = getPersistenceService().findAllBy(strquery, params.toArray());
        }
        return "workOrderNoSearchResults";
    }

    public String searchMbRefNo() {
        String strquery = "";
        final ArrayList<Object> params = new ArrayList<Object>();
        if (!StringUtils.isEmpty(query)) {
            strquery = " from MBHeader mbh where mbh.mbRefNo like '%'||?||'%' and mbh.egwStatus.code <> ? ";
            params.add(query.toUpperCase());
            params.add("NEW");
            mbHeaderList = getPersistenceService().findAllBy(strquery, params.toArray());
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

    public List getUsersInExecutingDepartment() {
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

    public List getWorkflowKDesigList() {
        return workflowKDesigList;
    }

    public List getWorkflowUsers() {
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
