package org.egov.infra.workflow.multitenant.model;

public class WorkflowConstants {

    public static final String ACTION_CREATE = "create";
    public static final String ACTION_FORWARD = "forward";
    public static final String ACTION_REJECT = "reject";
    public static final String ACTION_APPROVE = "approve";
    public static final String ACTION_SAVE = "save";
    public static final String ACTION_CANCEL = "cancel";
    public static final String ACTION_CREATE_AND_APPROVE = "create and approve";

    public static final String STATUS_REJECTED = "Rejected";
    public static final String STATUS_NEW = "NEW";

    public static final String VAR_WORKFLOWACTION = "workflowAction";
    public static final String VAR_WORKFLOWCOMMENTS = "workflowComments";
    public static final String VAR_WORKFLOWDEPARTMENT = "workflowDepartment";
    public static final String VAR_CURRENTSTATE = "currentState";
    public static final String VAR_CURRENTDESIGNATIONID = "currentDesignationId";
    public static final String VAR_CURRENTDEPARTMENTID = "currentDepartmentId";
    public static final String VAR_CURRENTPOSITIONID = "currentPositionId";
    public static final String VAR_BUSINESSKEY = "businessKey";
    public static final String VAR_APPROVERDESIGNATIONNAME = "approverDesignationName";
    public static final String VAR_WORKFLOWID = "workflowId";
    public static final String VAR_ADDITIONALRULE = "additionalRule";
    public static final String VAR_AMOUNTRULE = "amountRule";
    public static final String VAR_PENDINGACTIONS = "pendingActions";
    public static final String VAR_APPROVERNAME = "approverName";
    public static final String VAR_APPROVERPOSITIONID = "approverPositionId";
    public static final String VAR_APPROVERDESIGNATIONID = "approverDesignationId";
    public static final String VAR_APPROVERDEPARTMENTID = "approverDepartmentId";
    public static final String VAR_APPROVERGROUPID = "approverGroupId";
    public static final String VAR_NEXTACTION = "nextAction";
    public static final String VAR_NEXTSTATE = "nextState";
    public static final String VAR_VALIDACTIONS = "validActions";
    public static final String VAR_DEPARTMENTLIST = "departmentList";
    public static final String VAR_designationList = "designationList";

    public static final String OBJ_WORKFLOWBEAN = "workflowBean";

}