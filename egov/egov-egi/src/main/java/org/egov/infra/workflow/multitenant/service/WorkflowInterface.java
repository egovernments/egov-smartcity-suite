package org.egov.infra.workflow.multitenant.service;

import java.util.List;
import org.egov.infra.workflow.multitenant.model.ProcessInstance;
import org.egov.infra.workflow.multitenant.model.Task;
import org.egov.pims.commons.Designation;

public interface WorkflowInterface {

    ProcessInstance start(String jurisdiction, ProcessInstance processInstance);

    ProcessInstance getProcess(String jurisdiction, ProcessInstance processInstance);

    List<Task> getTasks(String jurisdiction, ProcessInstance processInstance);

    ProcessInstance update(String jurisdiction, ProcessInstance processInstance);

    Task update(String jurisdiction, Task task);

    List<Task> getHistoryDetail(String workflowId);

    List<Designation> getDesignations(Task t, String departmentCode);

    List<Object> getAssignee(String deptCode, String designationName);
}