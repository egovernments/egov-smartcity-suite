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

package org.egov.bpms.scraps.service;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ValuedDataObject;
import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomizedWorkFlowServiceScrap {

	private static final String DESGQUERY = "getDesignationForListOfDesgNames";
	@Autowired
	@Qualifier("entityQueryService")
	private PersistenceService entityQueryService;

    @Autowired
    @Qualifier("workflowService")
	private WorkflowService<? extends StateAware> workflowService;
    
    @Autowired
	private RepositoryService repositoryService;
    
    @Autowired
	private FormService formService;

	public List<Designation> getNextDesignations(final String type, final String department, final BigDecimal businessRule, final String additionalRule, final String currentState, final String pendingAction, final Date date) {

		final WorkFlowMatrix wfMatrix = this.workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState, pendingAction, date);
		final List<String> designationNames = new ArrayList<String>();
		if (wfMatrix != null && wfMatrix.getNextDesignation() != null) {
			final List<String> tempDesignationName = Arrays.asList(wfMatrix.getNextDesignation().split(","));
			for (final String desgName : tempDesignationName) {
				if (desgName != null && !"".equals(desgName.trim())) {
					designationNames.add(desgName.toUpperCase());
				}
			}
		}
		List<Designation> designationList = Collections.EMPTY_LIST;
		if (!designationNames.isEmpty()) {
			designationList = entityQueryService.findAllByNamedQuery(DESGQUERY, designationNames);
		}
		return designationList;
	}
	
	public List<Designation> getNextDesignationsFromActiviti(final String type, final String department,
			final BigDecimal businessRule, final String additionalRule, final String currentState,
			final String pendingAction, final Date date) {

		final List<String> designationNames = new ArrayList<String>();
		final List<String> tempDesignationName = getNextDesignations(currentState, type);
		for (final String desgName : tempDesignationName) {
			if (desgName != null && !"".equals(desgName.trim())) {
				designationNames.add(desgName.toUpperCase());
			}
		}
		List<Designation> designationList = Collections.EMPTY_LIST;
		if (!designationNames.isEmpty()) {
			designationList = entityQueryService.findAllByNamedQuery(DESGQUERY, designationNames);
		}
		return designationList;
	}
	
	public List<String> getNextDesignations(final String processInstanceId, final String processDefinitionKey) {
    	if (processInstanceId == null) {
        	BpmnModel bpmnModel = repositoryService.getBpmnModel(repositoryService.createProcessDefinitionQuery()
    				.processDefinitionKey(processDefinitionKey).list().get(0).getId());
    		List<ValuedDataObject> dataObjects = bpmnModel.getProcesses().get(0).getDataObjects();
    		for (ValuedDataObject object : dataObjects) {
    			if ("designation".equalsIgnoreCase(object.getId()))
    				return Arrays.asList(object.getValue().toString().split(","));
    		}
        } else {
        	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    		TaskService taskService = processEngine.getTaskService();
    		Task newTask = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
    		TaskFormData taskFormData = formService.getTaskFormData(newTask.getId());
    		for(FormProperty property : taskFormData.getFormProperties()) {
    			if ("designation".equalsIgnoreCase(property.getId()))
    				return Arrays.asList(property.getName().toString().split(","));
    		}
        }
    	return Collections.EMPTY_LIST;
    }

	public List<String> getNextValidActions(final String type, final String departmentName, final BigDecimal businessRule, final String additionalRule, final String currentState, final String pendingAction) {

		final WorkFlowMatrix wfMatrix = this.workflowService.getWfMatrix(type, departmentName, businessRule, additionalRule, currentState, pendingAction);
		List<String> validActions = Collections.EMPTY_LIST;

		if (wfMatrix != null && wfMatrix.getValidActions() != null) {
			validActions = Arrays.asList(wfMatrix.getValidActions().split(","));
		}
		return validActions;
	}

	public List<String> getNextValidActions(final String type, final String departmentName, final BigDecimal businessRule, final String additionalRule, final String currentState, final String pendingAction, final Date date) {

		final WorkFlowMatrix wfMatrix = this.workflowService.getWfMatrix(type, departmentName, businessRule, additionalRule, currentState, pendingAction, date);
		List<String> validActions = Collections.EMPTY_LIST;

		if (wfMatrix != null && wfMatrix.getValidActions() != null) {
			validActions = Arrays.asList(wfMatrix.getValidActions().split(","));
		}
		return validActions;
	}

	public WorkFlowMatrix getWfMatrix(final String type, final String department, final BigDecimal businessRule, final String additionalRule, final String currentState, final String pendingAction, final Date date) {
		return this.workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState, pendingAction, date);
	}

	public WorkFlowMatrix getWfMatrix(final String type, final String department, final BigDecimal businessRule, final String additionalRule, final String currentState, final String pendingAction) {
		return this.workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState, pendingAction);
	}
}
