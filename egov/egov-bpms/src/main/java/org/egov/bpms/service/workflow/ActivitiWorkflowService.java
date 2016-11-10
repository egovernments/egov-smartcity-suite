/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *
 *       Copyright (C) 2016  eGovernments Foundation
 *
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.bpms.service.workflow;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.egov.infra.workflow.entity.WorkflowAware;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.service.WorkflowTypeService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class ActivitiWorkflowService {
    private static Logger Log = Logger.getLogger(ActivitiWorkflowService.class);

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private WorkflowTypeService workflowTypesService;


    public boolean initiate(Map<String, Object> toBeSavedVariables, Map<String, String> workflowVariables)

    {
        String type = (String) workflowVariables.get("type");
        String fullyQualifiedName = (String) workflowVariables.get("fullyQualifiedName");
        String assignee = (String) workflowVariables.get("assignee");
        WorkflowAware workflowObject = (WorkflowAware) toBeSavedVariables.get("workflowObject");

        WorkflowTypes workflowType = null;
        if (type != null && !type.isEmpty())
            workflowType = workflowTypesService.findByClassNameAnType(fullyQualifiedName, type);
        else
            workflowType = workflowTypesService.findByClassName(fullyQualifiedName);
        if (null == workflowType || null == workflowType.getBusinessKey()) {
            Log.error("BPMN key is empty cant start workflow.");
            return false;

        }


        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(workflowType.getBusinessKey(), toBeSavedVariables);

        if (assignee != null) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            taskService.addCandidateGroup(task.getId(), assignee);
            workflowObject.setTaskId(task.getId());
            workflowObject.setProcessInstanceId(task.getProcessInstanceId());
        }

        Log.info("Worflow Started .Instance id" + processInstance.getId() + ", variables:" + processInstance.getProcessVariables());

        return true;
    }

    @Transactional
    public boolean update(Map<String, Object> toBeSavedVariables, Map<String, String> workflowVariables) {

        String type = (String) workflowVariables.get("type");
        String fullyQualifiedName = (String) workflowVariables.get("fullyQualifiedName");
        String assignee = (String) workflowVariables.get("assignee");
        WorkflowAware workflowObject = (WorkflowAware) toBeSavedVariables.get("workflowObject");
        String taskId = (String) workflowVariables.get("taskId");
        String workflowComent = (String) workflowVariables.get("workflowComent");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //verify
        String processInstanceId = task.getProcessInstanceId();
        taskService.addComment(task.getId(), task.getProcessInstanceId(), workflowComent);
        taskService.complete(task.getId(), toBeSavedVariables);

        task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if (task != null && assignee != null) {
            taskService.addCandidateGroup(task.getId(), assignee);
            workflowObject.setTaskId(task.getId());
        }
        return true;
    }


}