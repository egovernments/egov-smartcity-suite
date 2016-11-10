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

package org.egov.bpms.service.inbox;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.egov.bpms.entity.Group;
import org.egov.bpms.service.GroupService;
import org.egov.infra.web.support.ui.Inbox;
import org.egov.infra.workflow.service.WorkflowTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ActivitiInboxService {

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private GroupService groupService;
    @Autowired
    private WorkflowTypeService workflowTypeService;

    @SuppressWarnings("deprecation")
    public List<Inbox> search(String userName) {
        List<Inbox> items = new ArrayList<Inbox>();
        try {
            SimpleDateFormat DDMMYYY = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            Inbox item;
            List<Task> tasks = taskService.createTaskQuery().taskAssignee(userName).list();


            //tasks.addAll(taskService.createTaskQuery().taskCandidateUser(userName).list());//this will not work tried for a day
            //below logic should be removed when above line is fixed ie taskCandidateUser api
            List<Group> groups = groupService.findGroupsByUser(userName);
            List<String> candidateGroups = new ArrayList<String>();
            for (Group p : groups) {
                candidateGroups.add(p.getId().toString());
            }
            tasks.addAll(taskService.createTaskQuery().taskCandidateGroupIn(candidateGroups).list());
            //   tasks.addAll(taskService.createTaskQuery().taskOwner(userName).list());
            ProcessDefinition processDefinition = null;
            for (Task t : tasks) {

                item = new Inbox();
                item.setTaskId(t.getId());
                item.setSender(getSender(t));
                item.setDate(DDMMYYY.format(t.getCreateTime()));
                processDefinition = getProcessDefinition(t);
                item.setTask(getNatureOfWork(t, processDefinition));
                item.setStatus(t.getName());
                item.setDetails(getDescription(t));
                item.setId(getObjectId(t));
                items.add(item);
                item.setLink(getLink(t, item.getId(), processDefinition));
            }
            item = new Inbox();
            item.setSender("Sample Sender");
            item.setTask("Sample Work");
            item.setLink("Sample Link");
            item.setDetails("Sample details");
            items.add(item);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return items;

    }

    private String getNatureOfWork(Task t, ProcessDefinition processDefinition) {

        String displayName = workflowTypeService.findByBusinessKey(processDefinition.getKey()).getDisplayName();
        if (displayName != null)
            return displayName + " : " + t.getName();
        else
            return t.getName();

    }

    private String getObjectId(Task t) {
        String id = null;
        Map<String, Object> variables = runtimeService.getVariables(t.getProcessInstanceId());

        if (variables != null && variables.get("objectId") != null) {
            Object object = variables.get("objectId");
            try {
                id = object.toString();
            } catch (Exception e) {

                throw new RuntimeException("Unable to get the ObjectId");
            }


        }
        return id;


    }

    //this is not working as of now
    private String getDescription(Task t) {
        HistoricTaskInstance previousTask = getPreviousTask(t);
        List<Comment> taskComments = taskService.getTaskComments(previousTask.getId());
        List<Comment> taskComments1 = taskService.getTaskComments(t.getId());


        HistoryService historyService = processEngine.getHistoryService();

        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().taskId(t.getId()).list();
        for (Comment hvi : taskComments) {
            System.err.println(hvi.getFullMessage());

        }
        return "";
    }

    private String getLink(Task t, String objectId, ProcessDefinition processDefinition) {
        String link = "";
        if (objectId == null)
            return link;

        if (t.getCategory() != null)
            link = t.getCategory();
        else {
            link = processDefinition.getCategory();
        }
        if (link != null && !link.isEmpty()) {
            if (link.indexOf(":objectId") != -1)
                link = link.replace(":objectId", objectId.toString());

        }
        return link;

    }

    private ProcessDefinition getProcessDefinition(Task t) {
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionId(t.getProcessDefinitionId()).singleResult();
        return processDefinition;
    }


    private String getSender(Task t) {
        return getPreviousTask(t).getOwner();

    }

    private HistoricTaskInstance getPreviousTask(Task t) {
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(t.getProcessInstanceId()).orderByHistoricTaskInstanceEndTime().desc().list();
        if (list.size() >= 2) {
            return list.get(1);

        } else
            return list.get(0);
    }
}