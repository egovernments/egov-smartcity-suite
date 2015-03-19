package org.egov.infra.workflow.entity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.UserImpl;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.workflow.entity.State.StateStatus;
import org.egov.pims.commons.Position;

@MappedSuperclass
public abstract class StateAware extends AbstractAuditable<UserImpl, Long> {
    private static final long serialVersionUID = 5776408218810221246L;
    
    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name="STATE_ID")
    private State state;
    
    /**
     * Need to overridden by the implementing class to give details about the
     * State <I>Used by Inbox to fetch the State Detail at runtime</I>
     *
     * @return String Detail
     */
    public abstract String getStateDetails();

    /**
     * To set the Group Link, Any State Aware Object which needs Grouping should
     * override this method
     **/
    public String myLinkId() {
        return getId().toString();
    }

    public State getState() {
        return state;
    }

    protected void setState(final State state) {
        this.state = state;
    }

    public final State getCurrentState() {
        return state;
    }

    public final List<StateHistory> getStateHistory() {
        return state == null ? Collections.emptyList() : state.getHistory();
    }

    public final String getStateType() {
        return this.getClass().getSimpleName();
    }

    public final boolean stateIsNew() {
        return hasState() && getCurrentState().isNew();
    }

    public final boolean stateIsEnded() {
        return hasState() && getCurrentState().isEnded();
    }

    public final boolean hasState() {
        return getCurrentState() != null;
    }

    public final StateAware transition() {
        if (hasState()) {
            state.addStateHistory(new StateHistory(state));
            state.setStatus(StateStatus.INPROGRESS);
            resetState();
        }
        return this;
    }

    public final StateAware transition(final boolean clone) {
        if (hasState() && clone) {
            state.addStateHistory(new StateHistory(state));
            state.setStatus(StateStatus.INPROGRESS);
        } else
            transition();
        return this;
    }

    public final StateAware start() {
        if (hasState())
            throw new EGOVRuntimeException("Workflow already started state.");
        else {
            state = new State();
            state.setType(getStateType());
            state.setStatus(StateStatus.STARTED);
            state.setValue(State.DEFAULT_STATE_VALUE_CREATED);
            state.setComments(State.DEFAULT_STATE_VALUE_CREATED);
        }

        return this;
    }

    public final StateAware end() {
        if (stateIsEnded())
            throw new EGOVRuntimeException("Workflow already ended state.");
        else {
            state.setValue(State.DEFAULT_STATE_VALUE_CLOSED);
            state.setStatus(StateStatus.ENDED);
            state.setComments(State.DEFAULT_STATE_VALUE_CLOSED);
        }
        return this;
    }

    public final StateAware reopen(final boolean clone) {
        if (stateIsEnded()) {
            final StateHistory stateHistory = new StateHistory(state);
            stateHistory.setValue(State.STATE_REOPENED);
            state.setStatus(StateStatus.INPROGRESS);
            state.addStateHistory(stateHistory);
            if (!clone)
                resetState();
        } else
            throw new EGOVRuntimeException("Workflow not ended.");
        return this;
    }

    public final StateAware withOwner(final UserImpl owner) {
        state.setOwnerUser(owner);
        return this;
    }

    public final StateAware withOwner(final Position owner) {
        state.setOwnerPosition(owner);
        return this;
    }

    public final StateAware withStateValue(final String currentStateValue) {
        state.setValue(currentStateValue);
        return this;
    }

    public final StateAware withNextAction(final String nextAction) {
        state.setNextAction(nextAction);
        return this;
    }

    public final StateAware withComments(final String comments) {
        state.setComments(comments);
        return this;
    }

    public final StateAware withExtraInfo(final String extraInfo) {
        state.setExtraInfo(extraInfo);
        return this;
    }

    public final StateAware withDateInfo(final Date dateInfo) {
        state.setDateInfo(dateInfo);
        return this;
    }

    public final StateAware withExtraDateInfo(final Date extraDateInfo) {
        state.setExtraDateInfo(extraDateInfo);
        return this;
    }

    public final StateAware withSenderName(final String senderName) {
        state.setSenderName(senderName);
        return this;
    }
    
    private void resetState() {
        state.setComments("");
        state.setDateInfo(null);
        state.setExtraDateInfo(null);
        state.setExtraInfo("");
        state.setNextAction("");
        state.setValue("");
        state.setSenderName("");
        state.setOwnerUser(null);
        state.setOwnerPosition(null);
    }
}
