package org.egov.wtms.web.contract;

import java.math.BigDecimal;

public class WorkflowContainer {
    protected String workFlowAction;
    protected String approverComments;
    protected String currentState;
    protected String currentDesignation;
    public String additionalRule;
    public BigDecimal amountRule;
    public String workFlowDepartment;
    protected String pendingActions;
    protected String approverName;
    protected String approverDepartment;
    protected String approverDesignation;
    protected Long approverPositionId;
    
    public BigDecimal getAmountRule() {
        return null;
    }
    public String getAdditionalRule() {
        return null;
    }
    public String getWorkFlowDepartment() {
        return null;
    }

    
    public String getWorkFlowAction() {
        return workFlowAction;
    }
    public void setWorkFlowAction(String workFlowAction) {
        this.workFlowAction = workFlowAction;
    }
    public String getApproverComments() {
        return approverComments;
    }
    public void setApproverComments(String approverComments) {
        this.approverComments = approverComments;
    }
    public String getCurrentState() {
        return currentState;
    }
    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
    public String getCurrentDesignation() {
        return currentDesignation;
    }
    public void setCurrentDesignation(String currentDesignation) {
        this.currentDesignation = currentDesignation;
    }
   
    public void setAdditionalRule(String additionalRule) {
        this.additionalRule = additionalRule;
    }
   
    public void setAmountRule(BigDecimal amountRule) {
        this.amountRule = amountRule;
    }
   
    public void setWorkFlowDepartment(String workFlowDepartment) {
        this.workFlowDepartment = workFlowDepartment;
    }
    public String getPendingActions() {
        return pendingActions;
    }
    public void setPendingActions(String pendingActions) {
        this.pendingActions = pendingActions;
    }
    public String getApproverName() {
        return approverName;
    }
    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }
    public String getApproverDepartment() {
        return approverDepartment;
    }
    public void setApproverDepartment(String approverDepartment) {
        this.approverDepartment = approverDepartment;
    }
    public String getApproverDesignation() {
        return approverDesignation;
    }
    public void setApproverDesignation(String approverDesignation) {
        this.approverDesignation = approverDesignation;
    }
    public Long getApproverPositionId() {
        return approverPositionId;
    }
    public void setApproverPositionId(Long approverPositionId) {
        this.approverPositionId = approverPositionId;
    }
    
    

}
