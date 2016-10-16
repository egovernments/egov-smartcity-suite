package org.egov.infra.workflow.service;

/**
 * Created by mani on 10/10/16.
 */

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class ActivitiWorkflowService {
    private static Logger Log = Logger.getLogger(WorkflowService.class);

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







    public boolean initiate(String fullClassname, Long id, String message, String type)

    {
        WorkflowTypes workflowType = null;
        if (type != null && !type.isEmpty())
            workflowType = workflowTypesService.findByClassNameAnType(fullClassname, type);
        else
            workflowType = workflowTypesService.findByClassName(fullClassname);
        if (null == workflowType || null == workflowType.getBusinessKey()) {
            System.out.println("BPMN key is empty cant start workflow.");
            return false;

        }
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("description", message);
        variables.put("objectId", id);
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(workflowType.getBusinessKey(), variables);
        processInstance.getProcessVariables();

        Log.info("Worflow Started .Instance id" + processInstance.getId() + ", variables:" + processInstance.getProcessVariables());

        return true;
    }

    @Transactional
    public boolean update(String taskId, StateAware  stateAware, String message, String sender) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //verify
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("wfObject", stateAware);
        taskService.addComment(task.getId(), task.getProcessInstanceId(), message);
        taskService.complete(task.getId(), variables);
        return true;
    }


}