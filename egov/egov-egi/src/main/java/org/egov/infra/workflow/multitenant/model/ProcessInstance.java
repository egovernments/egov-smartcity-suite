package org.egov.infra.workflow.multitenant.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProcessInstance {
    
    private String id = null;

    private String type = null;

    private String description = null;

    private Date createdDate = null;

    private Date lastupdatedSince = null;

    private String status = null;
    
    private String action=null;
   
    private String businessKey = null;

    private String assignee = null;

    private String group = null;

    private WorkflowEntity entity = null;
    

    private Map<String,Attribute> attributes = new HashMap<String,Attribute>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastupdatedSince() {
        return lastupdatedSince;
    }

    public void setLastupdatedSince(Date lastupdatedSince) {
        this.lastupdatedSince = lastupdatedSince;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAsignee(String assignee) {
        this.assignee = assignee;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public WorkflowEntity getEntity() {
        return entity;
    }

    public void setEntity(WorkflowEntity entity) {
        this.entity = entity;
    }

    

   

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    public ProcessInstance id(String id) {
      this.id = id;
      return this;
    }


}