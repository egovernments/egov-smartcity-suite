package org.egov.infra.workflow.multitenant.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.egov.pims.commons.Position;

public class Task {

    private String id = null;

    private String type = null;

    private String description = null;

    private Date createdDate = null;

    private Date lastupdatedSince = null;

    private String owner = null;
    
    private String assignee = null;

    private String module  = null;

    private String state = null;

    private String status = null;

    private String url = null;

    private String businessKey = null;
    
    private String action = null;
    
    private String sender;
    
  
    private WorkflowEntity entity;
    
    private String comments;
    
    private String extraInfo;
    
    private String details;
    
    private Position ownerPosition;
    
    private String natureOfTask;
    
    
    
    private Map<String,Attribute> attributes = new HashMap<String,Attribute>();

    public Position getOwnerPosition() {
        return ownerPosition;
    }

    public void setOwnerPosition(Position ownerPosition) {
        this.ownerPosition = ownerPosition;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public WorkflowEntity getEntity() {
        return entity;
    }

    public void setEntity(WorkflowEntity entity) {
        this.entity = entity;
    }

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

     
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businesskey) {
        this.businessKey = businesskey;
    }


    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public String getNatureOfTask() {
        return natureOfTask;
    }

    public void setNatureOfTask(String natureOfTask) {
        this.natureOfTask = natureOfTask;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }

   






}