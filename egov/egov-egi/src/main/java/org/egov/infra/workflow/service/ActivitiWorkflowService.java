package org.egov.infra.workflow.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mani on 10/10/16.
 */

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.egov.infra.workflow.entity.WorkflowAware;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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







    public boolean initiate(Map<String, Object> toBeSavedVariables,Map<String, String> workflowVariables)

    {
    	String type=(String)workflowVariables.get("type");
        String	fullyQualifiedName=(String)workflowVariables.get("fullyQualifiedName");
        String assignee =(String)workflowVariables.get("assignee");
        WorkflowAware workflowObject=(WorkflowAware)  toBeSavedVariables.get("workflowObject");
        
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
        
        if(assignee!=null)
        {
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        task.setAssignee(assignee);
        task.setOwner(assignee);
        taskService.saveTask(task);
        workflowObject.setTaskId(task.getId());
        workflowObject.setProcessInstanceId(task.getProcessInstanceId()); 
        }

        Log.info("Worflow Started .Instance id" + processInstance.getId() + ", variables:" + processInstance.getProcessVariables());

        return true;
    }

    @Transactional
    public boolean update(Map<String, Object> toBeSavedVariables,Map<String, String> workflowVariables) {

    	String type=(String)workflowVariables.get("type");
        String	fullyQualifiedName=(String)workflowVariables.get("fullyQualifiedName");
        String assignee =(String)workflowVariables.get("assignee");
        WorkflowAware workflowObject=(WorkflowAware)  toBeSavedVariables.get("workflowObject");
        String taskId=(String)workflowVariables.get("taskId");
        String workflowComent=(String)workflowVariables.get("workflowComent");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //verify
        String processInstanceId = task.getProcessInstanceId();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("wfObject", workflowObject);
        taskService.addComment(task.getId(), task.getProcessInstanceId(), workflowComent);
        taskService.complete(task.getId(), variables);
       
        task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if(task!=null)
        {
        task.setAssignee(assignee);
        task.setOwner(assignee);
        taskService.saveTask(task);
        workflowObject.setTaskId(task.getId());
        }
        return true;
    }


}