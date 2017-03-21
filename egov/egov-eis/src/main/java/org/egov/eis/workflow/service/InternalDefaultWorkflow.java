package org.egov.eis.workflow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.State.StateStatus;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.infra.workflow.multitenant.model.Attribute;
import org.egov.infra.workflow.multitenant.model.ProcessInstance;
import org.egov.infra.workflow.multitenant.model.Task;
import org.egov.infra.workflow.multitenant.model.WorkflowBean;
import org.egov.infra.workflow.multitenant.model.WorkflowConstants;
import org.egov.infra.workflow.multitenant.model.WorkflowEntity;
import org.egov.infra.workflow.multitenant.service.WorkflowInterface;
import org.egov.infra.workflow.service.StateService;
import org.egov.infra.workflow.service.WorkflowTypeService;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InternalDefaultWorkflow implements WorkflowInterface {

    private static Logger LOG = LoggerFactory.getLogger(InternalDefaultWorkflow.class);
    private List<Object> approverList;
    @Autowired
    @Qualifier("assignmentService")
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private CustomizedWorkFlowService customizedWorkFlowService;

    @Autowired
    private DesignationService designationService;
    @Autowired
    private StateService stateService;

    @Autowired
    @Qualifier("eisService")
    private EISServeable eisService;

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private WorkflowTypeService workflowTypeService;

    @Autowired
    protected DepartmentService departmentService;

    @Autowired
    private CustomizedWorkFlowService workflowService;
    @Autowired
    private SecurityUtils securityUtils;

    @Transactional
    @Override
    public ProcessInstance start(final String jurisdiction, final ProcessInstance processInstance) {
        final WorkFlowMatrix wfMatrix = workflowService.getWfMatrix(processInstance.getBusinessKey(), null, null, null, null,
                null);
        Position owner = null;
        if (processInstance.getAssignee() != null)
            owner = positionMasterService.getPositionById(Long.valueOf(processInstance.getAssignee()));
        final WorkflowEntity entity = processInstance.getEntity();
        final State state = new State();
        state.setType(processInstance.getType());
        state.setSenderName(securityUtils.getCurrentUser().getName());
        state.setStatus(StateStatus.INPROGRESS);
        state.setValue(wfMatrix.getNextState());
        state.setComments(processInstance.getDescription());
        state.setOwnerPosition(owner);
        state.setNextAction(wfMatrix.getNextAction());
        state.setType(processInstance.getBusinessKey());
        state.setInitiatorPosition(getInitiator());
        final WorkflowTypes type = workflowTypeService.getWorkflowTypeByType(state.getType());
        state.setMyLinkId(type.getLink().replace(":ID", entity.myLinkId()));
        state.setNatureOfTask(type.getDisplayName());
        state.setExtraInfo(entity.getStateDetails());
        stateService.create(state);
        if (state.getId() != null)
            processInstance.setId(state.getId().toString());
        return processInstance;
    }

    private Position getInitiator() {
        Position position = null;
        try {
            position = assignmentService.getPrimaryAssignmentForUser(securityUtils.getCurrentUser().getId())
                    .getPosition();
        } catch (final Exception e) {
            LOG.error("Error while setting initiator position");
        }
        return position;
    }

    @Transactional
    @Override
    public ProcessInstance update(final String jurisdiction, final ProcessInstance pi) {
        return pi;

    }

    @Transactional
    @Override
    public Task update(final String jurisdiction, final Task task) {
        Position owner = null;
        if (task.getAssignee() != null)
            owner = positionMasterService.getPositionById(Long.valueOf(task.getAssignee()));
        final WorkflowEntity entity = task.getEntity();
        String dept = null;
        if (task.getAttributes().get("department") != null)
            dept = task.getAttributes().get("department").getCode();
        final WorkFlowMatrix wfMatrix = workflowService.getWfMatrix(task.getBusinessKey(), dept, null, null, task.getStatus(),
                null);

        String nextState = wfMatrix.getNextState();
        final State state = stateService.getStateById(Long.valueOf(task.getId()));
        if ("END".equalsIgnoreCase(wfMatrix.getNextAction()))
            state.setStatus(State.StateStatus.ENDED);
        else
            state.setStatus(State.StateStatus.INPROGRESS);

        if (task.getAction().equalsIgnoreCase(WorkflowConstants.ACTION_REJECT)) {
            owner = state.getInitiatorPosition();
            if (owner != null)
                task.setAssignee(owner.getId().toString());
            else
                owner = assignmentService.getPrimaryAssignmentForUser(entity.getCreatedBy().getId()).getPosition();

            final Attribute approverDesignationName = new Attribute();
            approverDesignationName.setCode(owner.getDeptDesig().getDesignation().getName());
            task.getAttributes().put("approverDesignationName", approverDesignationName);

            final Attribute approverName = new Attribute();
            approverName.setCode(getApproverName(owner));
            task.getAttributes().put("approverName", approverName);
            nextState = "Rejected";
        }
        if (task.getAction().equalsIgnoreCase(WorkflowConstants.ACTION_CANCEL)) {
            state.setStatus(State.StateStatus.ENDED);
            nextState = State.DEFAULT_STATE_VALUE_CLOSED;
        }

        state.addStateHistory(new StateHistory(state));

        state.setValue(nextState);
        state.setComments(task.getDescription());
        state.setSenderName(securityUtils.getCurrentUser().getName());
        if (owner != null)
            state.setOwnerPosition(owner);
        state.setNextAction(wfMatrix.getNextAction());
        state.setType(task.getBusinessKey());
        state.setExtraInfo(entity.getStateDetails());
        stateService.create(state);
        if (state.getId() != null)
            task.setId(state.getId().toString());
        stateService.update(state);

        return task;
    }

    private String getApproverName(final Position owner) {
        String approverName = null;
        try {
            approverName = assignmentService.getPrimaryAssignmentForPositionAndDate(owner.getId(), new Date())
                    .getEmployee().getName();
        } catch (final Exception e) {
            LOG.error("error while fetching users name");
        }
        return approverName;
    }

    private String getNextAction(final WorkflowEntity model, final WorkflowBean container) {

        WorkFlowMatrix wfMatrix = null;
        if (null != model && null != model.getId())
            if (null != model.getProcessInstance())
                wfMatrix = workflowService.getWfMatrix(model.getProcessInstance().getBusinessKey(),
                        container.getWorkflowDepartment(), container.getAmountRule(), container.getAdditionalRule(),
                        container.getCurrentState(), container.getPendingActions(), model.getCreatedDate());
            else
                wfMatrix = workflowService.getWfMatrix(model.getProcessInstance().getBusinessKey(),
                        container.getWorkflowDepartment(), container.getAmountRule(), container.getAdditionalRule(),
                        State.DEFAULT_STATE_VALUE_CREATED, container.getPendingActions(), model.getCreatedDate());
        return wfMatrix == null ? "" : wfMatrix.getNextAction();
    }

    /**
     * @param model
     * @param container
     * @return List of WorkFlow Buttons From Matrix By Passing parametres Type,CurrentState,CreatedDate
     */
    private List<String> getValidActions(final WorkflowEntity model, final WorkflowBean workflowBean) {
        List<String> validActions = Collections.emptyList();
        if (null == model || model.getWorkflowId() == null)
            validActions = workflowService.getNextValidActions(workflowBean.getBusinessKey(),
                    workflowBean.getWorkflowDepartment(), workflowBean.getAmountRule(),
                    workflowBean.getAdditionalRule(), "NEW", workflowBean.getPendingActions(), model.getCreatedDate());
        else if (null != model.getProcessInstance())
            validActions = workflowService.getNextValidActions(workflowBean.getBusinessKey(),
                    workflowBean.getWorkflowDepartment(), workflowBean.getAmountRule(),
                    workflowBean.getAdditionalRule(), model.getProcessInstance().getStatus(),
                    workflowBean.getPendingActions(), model.getCreatedDate());
        return validActions;
    }

    @Override
    public ProcessInstance getProcess(final String jurisdiction, final ProcessInstance processInstance) {
        final WorkflowBean wfbean = new WorkflowBean();

        State state = null;
        if (processInstance.getId() != null && !processInstance.getId().isEmpty())
            state = stateService.getStateById(Long.valueOf(processInstance.getId()));
        if (state != null) {
            processInstance.setBusinessKey(state.getType());
            if (state.getOwnerPosition() != null)
                processInstance.setAsignee(state.getOwnerPosition().getId().toString());
            else if (state.getOwnerUser() != null)
                processInstance.setAsignee(state.getOwnerUser().getId().toString());
            processInstance.setStatus(state.getValue());
        }
        processInstance.getEntity().setProcessInstance(processInstance);
        wfbean.map(processInstance);
        final Attribute validActions = new Attribute();
        validActions.setValues(getValidActions(processInstance.getEntity(), wfbean));
        processInstance.getAttributes().put("validActions", validActions);
        final Attribute nextAction = new Attribute();
        nextAction.setCode(getNextAction(processInstance.getEntity(), wfbean));
        processInstance.getAttributes().put("nextAction", nextAction);
        return processInstance;
    }

    @Override
    public List<Task> getTasks(final String jurisdiction, final ProcessInstance processInstance) {
        final List<Task> tasks = new ArrayList<Task>();
        final Long userId = securityUtils.getCurrentUser().getId();
        final List<String> types = workflowTypeService.getEnabledWorkflowType(false);
        final List<Long> ownerIds = eisService.getPositionsForUser(userId, new Date()).parallelStream()
                .map(position -> position.getId()).collect(Collectors.toList());
        List<State> states = new ArrayList<State>();
        if (!types.isEmpty())
            states = stateService.getStates(ownerIds, types, userId);
        for (final State s : states)
            tasks.add(s.map());
        return tasks;
    }

    @Override
    public List<Task> getHistoryDetail(final String workflowId) {
        final List<Task> tasks = new ArrayList<Task>();
        Task t;
        final State state = stateService.getStateById(Long.valueOf(workflowId));
        final Set<StateHistory> history = state.getHistory();
        for (final StateHistory stateHistory : history) {
            t = stateHistory.map();
            tasks.add(t);
        }
        t = state.map();
        tasks.add(t);
        return tasks;
    }

    @Override
    public List<Designation> getDesignations(final Task task, final String departmentCode) {

        String currentState = task.getStatus();
        final Department deptCode = departmentService.getDepartmentByCode(departmentCode);
        final String departmentRule = deptCode.getName();
        final String additionalRule = task.getAttributes().get("additionalRule").getCode();
        final String amountRule = task.getAttributes().get("amountRule").getCode();
        BigDecimal amtRule = null;
        if (amountRule != null)
            amtRule = new BigDecimal(amountRule);
        final String type = task.getBusinessKey();
        final String pendingAction = task.getAction();
        List<Designation> designationList;
        if ("END".equals(currentState))
            currentState = "";
        designationList = customizedWorkFlowService.getNextDesignations(type, departmentRule, amtRule, additionalRule,
                currentState, pendingAction, new Date());
        if (designationList.isEmpty())
            designationList = persistenceService.findAllBy("from Designation");
        return designationList;
    }

    @Override
    public List<Object> getAssignee(final String deptCode, final String designationName) {
        final Department dept = departmentService.getDepartmentByCode(deptCode);
        final Long ApproverDepartmentId = dept.getId();
        final Designation desig = designationService.getDesignationByName(designationName);
        final Long DesignationId = desig.getId();
        if (DesignationId != null && DesignationId != -1) {
            final HashMap<String, String> paramMap = new HashMap<String, String>();
            if (ApproverDepartmentId != null && ApproverDepartmentId != -1)
                paramMap.put("departmentId", ApproverDepartmentId.toString());
            paramMap.put("DesignationId", DesignationId.toString());
            approverList = new ArrayList<Object>();
            final List<Assignment> assignmentList = assignmentService
                    .findAllAssignmentsByDeptDesigAndDates(ApproverDepartmentId,
                            DesignationId, new Date());
            for (final Assignment assignment : assignmentList)
                approverList.add(assignment);
        }
        return approverList;
    }
}