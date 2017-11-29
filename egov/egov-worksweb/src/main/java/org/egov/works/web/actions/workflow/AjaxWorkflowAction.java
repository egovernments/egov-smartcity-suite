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
package org.egov.works.web.actions.workflow;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.Functionary;
import org.egov.eis.entity.EmployeeView;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.pims.commons.Designation;
import org.egov.pims.service.EisUtilService;
import org.egov.works.models.workflow.WorkFlow;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.script.ScriptContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AjaxWorkflowAction extends BaseFormAction {

    private static final long serialVersionUID = 1212117794224094188L;
    private WorkFlow workflow = new WorkFlow();
    private static final String WORKFLOW_USER_LIST = "workflowUsers";
    private static final String WORKFLOW_DESIG_LIST = "workflowDesignations";
    private Long objectId;
    private String scriptName;
    private List<Designation> workflowDesigList = new ArrayList<Designation>();
    private EisUtilService eisService;
    private WorksService worksService;
    @Autowired
    private ScriptService scriptService;
    @Autowired
    private DepartmentService departmentService;

    @Override
    public Object getModel() {
        return workflow;
    }

    public void setModel(final WorkFlow workflow) {
        this.workflow = workflow;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    public String getWorkFlowUsers() {
        return WORKFLOW_USER_LIST;
    }

    public List<EmployeeView> getApproverUserList() {
        List roleList;
        Integer funcId;
        if (workflow.getWorkflowDepartmentId() != null && workflow.getWorkflowDepartmentId() != -1
                && workflow.getWorkflowDesignationId() != null && workflow.getWorkflowDesignationId() != -1) {
            final HashMap<String, Object> paramMap = new HashMap<String, Object>();
            if (workflow.getWorkflowWardId() != null && workflow.getWorkflowWardId() != -1)
                paramMap.put("boundaryId", workflow.getWorkflowWardId().toString());
            paramMap.put("designationId", workflow.getWorkflowDesignationId().toString());
            if (workflow.getWorkflowDepartmentId() != null && workflow.getWorkflowDepartmentId() != -1)
                paramMap.put("departmentId", workflow.getWorkflowDepartmentId().toString());
            if (workflow.getWorkflowFunctionaryId() != null
                    && StringUtils.isNotBlank(workflow.getWorkflowFunctionaryId().toString())
                    && workflow.getWorkflowFunctionaryId() != -1)
                paramMap.put("functionaryId", workflow.getWorkflowFunctionaryId().toString());
            if (paramMap.get("functionaryId") != null) {
                funcId = Integer.parseInt((String) paramMap.get("functionaryId"));
                final Functionary func = (Functionary) persistenceService.find(" from  Functionary where id = ?",
                        funcId);
                if (func != null && func.getName().equalsIgnoreCase("UAC")) {
                    roleList = worksService.getWorksRoles();
                    roleList.add("ContractorBill Approver");
                } else
                    roleList = worksService.getWorksRoles();
            } else
                roleList = worksService.getWorksRoles();
            if (roleList != null)
                paramMap.put("roleList", roleList);
            return eisService.getEmployeeInfoList(paramMap);
        }
        return Collections.emptyList();
    }

    public String getDesgByDeptAndType() {
        String departmentName = "";
        if (workflow.getWorkflowDepartmentId() != null && workflow.getWorkflowDepartmentId() != -1)
            departmentName = departmentService.getDepartmentById(workflow.getWorkflowDepartmentId()).getName();
        final ScriptContext scriptContext = ScriptService.createContext("department", departmentName, "objectId",
                objectId, "genericService", getPersistenceService());
        final List<String> desglist = (List<String>) scriptService.executeScript("works.estimatenumber.generator",
                scriptContext);

        /*
         * List<Script> scriptList = persistenceService.findAllByNamedQuery(Script .BY_NAME,scriptName+".nextDesignation");
         * if(!scriptList.isEmpty()){ List<String> desglist = (List<String>) scriptList.get(0).eval(Script.createContext
         * ("department",departmentName, "objectId",objectId,"genericService",getPersistenceService()));
         */
        final List<String> desgListUpper = new ArrayList<String>();
        for (final String desgNames : desglist)
            desgListUpper.add(desgNames.toUpperCase());
        workflowDesigList.addAll(getPersistenceService().findAllByNamedQuery("getDesignationForListOfDesgNames",
                desgListUpper));
        // }
        return WORKFLOW_DESIG_LIST;
    }

    public void setEisService(final EisUtilService eisService) {
        this.eisService = eisService;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(final Long objectId) {
        this.objectId = objectId;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(final String scriptName) {
        this.scriptName = scriptName;
    }

    public List<Designation> getWorkflowDesigList() {
        return workflowDesigList;
    }

    public void setWorkflowDesigList(final List<Designation> workflowDesigList) {
        this.workflowDesigList = workflowDesigList;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

}
