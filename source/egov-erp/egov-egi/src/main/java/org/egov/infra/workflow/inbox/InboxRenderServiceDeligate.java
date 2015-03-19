package org.egov.infra.workflow.inbox;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.infra.workflow.inbox.InboxRenderService.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.Action;
import org.egov.pims.commons.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Service
@Lazy
public class InboxRenderServiceDeligate<T extends StateAware> {
    private static final Logger LOG = LoggerFactory.getLogger(InboxRenderServiceDeligate.class);
    
    public static final String UNKNOWN = "Unknown";
    public static final String SLASH_DELIMIT = " / ";
    private static final Map<String, WorkflowTypes> WF_TYPES = new TreeMap<String, WorkflowTypes>();

    private @Autowired ApplicationContext applicationContext;
    private @Autowired PersistenceService<State, Long> statePersistenceService;
    private @Autowired PersistenceService<WorkflowTypes, Long> workflowTypePersistenceService;
    @Qualifier("entityQueryService")
    private @Autowired PersistenceService entityQueryService;
    @Qualifier("eisService")
    private @Autowired EISServeable eisService;
    
    @PostConstruct
    public void initWorkflowTypes() {
        this.workflowTypePersistenceService.findAll().
        parallelStream().
        filter(wftype -> RENDER_Y.equals(wftype.getRenderYN())).forEach(wftype -> WF_TYPES.put(wftype.getType(),wftype));
    }

    public WorkflowTypes getWorkflowType(final String wfType) {
        return WF_TYPES.get(wfType);
    }

    public List<String> getAssignedWorkflowTypes(final Integer posId) {
        return this.entityQueryService.findAllByNamedQuery(State.WORKFLOWTYPES_QRY, posId);
    }

    public Optional<InboxRenderService<T>> getWorkflowTypeService(final String wfType) {
        InboxRenderService<T> workflowTypeService = null;
        try {
            workflowTypeService = (InboxRenderService<T>)applicationContext.getBean(wfType.concat(INBOX_RENDER_SERVICE_SUFFIX));
        } catch (BeansException e) {
            LOG.warn("InboxRenderService bean for {} not found, have you defined {}InboxRenderService bean ?",wfType,wfType);
        }
        return Optional.ofNullable(workflowTypeService);
    }

    public List<T> getDraftItems(final Integer owner, final Integer userId, final String order) {
        final List<T> draftWfItems = new ArrayList<T>();
        final List<String> wfTypes = this.getAssignedWorkflowTypes(owner);
        for (final String wfType : wfTypes) {
            final Optional<InboxRenderService<T>> wfTypeService = getWorkflowTypeService(wfType);
            if (wfTypeService.isPresent())
                draftWfItems.addAll(wfTypeService.get().getDraftWorkflowItems(owner, userId, order));
        }
        return draftWfItems;
    }

    public List<T> getFilteredInboxItems(final Integer owner, final Integer userId, final Integer sender,
            final String taskName, final Date fromDate, final Date toDate) {
        final List<T> filteredWFItems = new ArrayList<T>();
        List<String> wfTypes = null;

        if (taskName == null || EMPTY.equals(taskName.trim()))
            wfTypes = this.getAssignedWorkflowTypes(owner);
        else {
            wfTypes = new ArrayList<String>();
            final WorkflowTypes wfType = this.workflowTypePersistenceService.find(
                    "from org.egov.infstr.models.WorkflowTypes  where displayName=?", taskName);
            wfTypes.add(wfType.getType());
        }
        for (final String wfType : wfTypes) {
            final Optional<InboxRenderService<T>> wfTypeService = this.getWorkflowTypeService(wfType);
            if (wfTypeService.isPresent())
                filteredWFItems.addAll(wfTypeService.get().getFilteredWorkflowItems(owner, userId, sender, fromDate, toDate));
        }
        return filteredWFItems;
    }

    /**
     * Returns a list of workflow items that for the given criteria
     * 
     * @param criteria
     *            the search criteria #mandatory key in criteria 1] wfType
     *            StateAware Type name #other non mandatory keys in criteria 1]
     *            sender Position list 2] user Position list 3] fromDate 4]
     *            toDate 5] wfState
     * @return the List of workflow items
     */
    public List<T> getWorkflowItems(final Map<String, Object> criteria) {
        return this.getWorkflowTypeService(criteria.get("wfType").toString()).get().getWorkflowItems(criteria);
    }

    public List<T> getWorkflowItems(final String wfType, final String myLinkId) {
        final Optional<InboxRenderService<T>> wfTypeService = this.getWorkflowTypeService(wfType);
        List<T> stateAwares = new ArrayList<T>();
        if (wfTypeService.isPresent())
            stateAwares = wfTypeService.get().getWorkflowItems(myLinkId);
        return stateAwares;
    }

    public List<T> getWorkflowItems(final Integer owner, final Integer userId, final String order) {
        final List<T> assignedWFItems = new ArrayList<T>();
        final List<String> wfTypes = this.getAssignedWorkflowTypes(owner);
        for (final String wfType : wfTypes) {
            final Optional<InboxRenderService<T>> wfTypeService = this.getWorkflowTypeService(wfType);
            if (wfTypeService.isPresent())
                assignedWFItems.addAll(wfTypeService.get().getAssignedWorkflowItems(owner, userId, order));
        }
        return assignedWFItems;
    }

   public String getNextAction(final State state) {
        String nextAction = EMPTY;
        if (state.getNextAction() != null) {
            final Action action = (Action) this.entityQueryService.findByNamedQuery(Action.BY_NAME_AND_TYPE,
                    state.getNextAction(), state.getType());
            if (action == null)
                nextAction = state.getNextAction();
            else
                nextAction = action.getDescription() == null ? state.getNextAction() : action.getDescription();
        }
        return nextAction;
    }

    public Position getStateUserPosition(final State state) {
        return state.getHistory().isEmpty() ? this.getPrimaryPositionForUser(state.getCreatedBy().getId(),
                state.getCreatedDate().toDate()) : state.getHistory().get(state.getHistory().size() - 1).getOwnerPosition();

    }

    public User getStateUser(final State state, final Position position) {
        return this.getUserForPosition(position.getId(), state.getCreatedDate().toDate());
    }

    public String prettyPrintSenderName(final Position position, final User user) {
        final StringBuilder senderName = new StringBuilder();
        senderName
                .append(position == null ? UNKNOWN : position.getName())
                .append(SLASH_DELIMIT)
                .append(user == null ? UNKNOWN : user.getFirstName() + " "
                        + (user.getLastName() == null ? EMPTY : user.getLastName()));
        return senderName.toString();
    }

    public List<Position> getPositionForUser(final Integer userId, final Date forDate) {
        return this.eisService.getPositionsForUser(userId, forDate);
    }

    public Position getPrimaryPositionForUser(final Integer userId, final Date forDate) {
        return this.eisService.getPrimaryPositionForUser(userId, forDate);
    }

    public User getUserForPosition(final Integer posId, final Date forDate) {
        return this.eisService.getUserForPosition(posId, forDate);
    }
    
    public State getStateById(final Long stateId) {
        return this.statePersistenceService.findById(stateId, false);
    }
}
