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
package org.egov.bpms.scraps.controller.workflow;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ValuedDataObject;
import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.infra.workflow.service.WorkflowTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public abstract class GenericWorkFlowScrapController {

    @Autowired
    protected CustomizedWorkFlowService customizedWorkFlowService;

    @Autowired
    protected DepartmentService departmentService;
    
    @Autowired
	private RepositoryService repositoryService;
    
    @Autowired
	private ProcessEngine ProcessEngine;
    
    @Autowired
     private TaskService taskService;
    
    @Autowired
	private FormService formService;
    
    @Autowired
    protected WorkflowTypeService workflowTypeService;

    @ModelAttribute(value = "approvalDepartmentList")
    public List<Department> addAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @ModelAttribute("workflowcontainer")
    public WorkflowContainer getWorkflowContainer() {
        return new WorkflowContainer();
    }

    /**
     * @param prepareModel
     * @param model
     * @param container
     *            This method we are calling In GET Method..
     */
    protected void prepareWorkflow(final Model prepareModel, final StateAware model, final WorkflowContainer container) {
    	
    	WorkflowTypes workflowTypeByType = workflowTypeService.getWorkflowTypeByType(container.getWorkflowType());
    	if(workflowTypeByType!=null && workflowTypeByType.getBusinessKey()!=null)
    	{
    	prepareActivitiWorkflow(prepareModel,container.getProcessInstanceId(),workflowTypeByType.getBusinessKey());
    		
    	}else
    	{
        prepareModel.addAttribute("approverDepartmentList", addAllDepartments());
        prepareModel.addAttribute("validActionList", getValidActions(model, container));
        prepareModel.addAttribute("nextAction", getNextAction(model, container));
    	}
    } 
    
    protected void prepareActivitiWorkflow(final Model prepareModel, final String processInstanceId, final String processDefinitionKey) {
        prepareModel.addAttribute("approverDepartmentList", addAllDepartments());
        prepareModel.addAttribute("validActionList", getValidActivitiActions(processInstanceId, processDefinitionKey));
    }

    /**
     * @param model
     * @param container
     * @return NextAction From Matrix With Parameters
     *         Type,CurrentState,CreatedDate
     */
    public String getNextAction(final StateAware model, final WorkflowContainer container) {

        WorkFlowMatrix wfMatrix = null;
        if (null != model && null != model.getId())
            if (null != model.getCurrentState())
                wfMatrix = customizedWorkFlowService.getWfMatrix(model.getStateType(),
                        container.getWorkFlowDepartment(), container.getAmountRule(), container.getAdditionalRule(),
                        model.getCurrentState().getValue(), container.getPendingActions(), model.getCreatedDate());
            else
                wfMatrix = customizedWorkFlowService.getWfMatrix(model.getStateType(),
                        container.getWorkFlowDepartment(), container.getAmountRule(), container.getAdditionalRule(),
                        State.DEFAULT_STATE_VALUE_CREATED, container.getPendingActions(), model.getCreatedDate());
        return wfMatrix == null ? "" : wfMatrix.getNextAction();
    }

    /**
     * @param model
     * @param container
     * @return List of WorkFlow Buttons From Matrix By Passing parametres
     *         Type,CurrentState,CreatedDate
     */
    public List<String> getValidActions(final StateAware model, final WorkflowContainer container) {
        List<String> validActions = Collections.emptyList();
        if (null == model
                || null == model.getId() || (model.getCurrentState()==null) 
                || (model != null && model.getCurrentState() != null ? model.getCurrentState().getValue()
                        .equals("Closed")
                        || model.getCurrentState().getValue().equals("END") : false))
            validActions = Arrays.asList("Forward");
        else if (null != model.getCurrentState())
            validActions = customizedWorkFlowService.getNextValidActions(model.getStateType(), container
                    .getWorkFlowDepartment(), container.getAmountRule(), container.getAdditionalRule(), model
                    .getCurrentState().getValue(), container.getPendingActions(), model.getCreatedDate());
        else
            // FIXME This May not work
            validActions = customizedWorkFlowService.getNextValidActions(model.getStateType(),
                    container.getWorkFlowDepartment(), container.getAmountRule(), container.getAdditionalRule(),
                    State.DEFAULT_STATE_VALUE_CREATED, container.getPendingActions(), model.getCreatedDate());
        return validActions;
    }
    
    public List<String> getValidActivitiActions(final String processInstanceId, final String processDefinitionKey) {
    	if (processInstanceId == null) {
        	BpmnModel bpmnModel = repositoryService.getBpmnModel(repositoryService.createProcessDefinitionQuery()
    				.processDefinitionKey(processDefinitionKey).list().get(0).getId());
    		List<ValuedDataObject> dataObjects = bpmnModel.getProcesses().get(0).getDataObjects();
    		for (ValuedDataObject object : dataObjects) {
    			if ("validactions".equalsIgnoreCase(object.getId()))
    				return Arrays.asList(object.getValue().toString().split(","));
    		}
        } else {
        	
    		Task newTask = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
    		TaskFormData taskFormData = formService.getTaskFormData(newTask.getId());
    		for(FormProperty property : taskFormData.getFormProperties()) {
    			if ("validactions".equalsIgnoreCase(property.getId()))
    				return Arrays.asList(property.getName().toString().split(","));
    		}
        }
    	return Collections.EMPTY_LIST;
    }

}
