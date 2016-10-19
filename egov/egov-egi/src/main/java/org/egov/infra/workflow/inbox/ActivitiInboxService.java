package org.egov.infra.workflow.inbox;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.egov.infra.web.support.ui.Inbox;
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
    private IdentityService identityService;


    private static final   SimpleDateFormat DDMMYYY = new SimpleDateFormat("dd/MM/yyyy hh:mm a");;

    public List<Inbox> search(String userName) {
        List<Inbox> items = new ArrayList<Inbox>();
        try {

            Inbox item;
            List<Task> tasks = taskService.createTaskQuery().taskAssignee(userName).list();
            tasks.addAll(taskService.createTaskQuery().taskCandidateUser(userName).list());
            //  tasks.addAll(taskService.createTaskQuery().taskCandidateOrAssigned(userName).list());
            //tasks.addAll(taskService.createTaskQuery().taskCandidateGroup("35").list());
         //   tasks.addAll(taskService.createTaskQuery().taskOwner(userName).list());
            for (Task t : tasks) {

                item = new Inbox();
                item.setTaskId(t.getId());
                item.setSender(getSender(t));
                item.setDate(DDMMYYY.format(t.getCreateTime()));
                item.setTask(t.getName());

                item.setDetails(getDescription(t));
                item.setId(getObjectId(t));
                items.add(item);
                item.setLink(getLink(t,item.getId()));
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

    private String getObjectId(Task t) {
        String id=null;
        Map<String, Object> variables = runtimeService.getVariables(t.getProcessInstanceId());

        if(variables!=null && variables.get("objectId")!=null)
        {
            Object object = variables.get("objectId");
            try{
                id= object.toString();
            }catch(Exception e)
            {

                throw new RuntimeException("Unable to get the ObjectId");
            }



        }
        return id;


    }

    private String getDescription(Task t) {
        HistoricTaskInstance previousTask = getPreviousTask(t);
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().taskId(t.getId()).list();
        for(HistoricVariableInstance hvi:list)
        {
          //  System.err.println(hvi.getVariableName().equals("desciption"));
            hvi.getValue();
        }
        return "";
    }

    private String getLink(Task t,String objectId) {
    	String link="";
    	if(objectId==null)
    		return link;
        
        if (t.getCategory() != null)
            link= t.getCategory();
        else {
            ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
                    .processDefinitionId(t.getProcessDefinitionId()).singleResult();
            link= processDefinition.getCategory();
        }
        if(link!=null && !link.isEmpty())
        {
            if(link.indexOf(":objectId")!=-1)
                link=link.replace(":objectId",objectId.toString());
           /* if(link.indexOf(":taskId")!=-1)
                link=link.replace(":taskId",t.getId());*/
        }
        return link;

    }


    private String getSender(Task t) {
        return getPreviousTask(t).getAssignee();

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