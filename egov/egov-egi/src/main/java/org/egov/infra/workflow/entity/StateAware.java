/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.workflow.entity;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.workflow.entity.State.StateStatus;
import org.egov.infra.workflow.entity.contract.StateInfoBuilder;
import org.egov.pims.commons.Position;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@MappedSuperclass
public abstract class StateAware extends AbstractAuditable {
    private static final long serialVersionUID = 5776408218810221246L;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "STATE_ID")
    private State state;

    public static Comparator<? super StateAware> byCreatedDate() {
        return (stateAware_1, stateAware_2) -> {
            int returnVal = 1;
            if (stateAware_1 == null)
                returnVal = stateAware_2 == null ? 0 : -1;
            else if (stateAware_2 == null)
                returnVal = 1;
            else {
                final Date first_date = stateAware_1.getState().getCreatedDate();
                final Date second_date = stateAware_2.getState().getCreatedDate();
                if (first_date.after(second_date))
                    returnVal = -1;
                else if (first_date.equals(second_date))
                    returnVal = 0;
            }
            return returnVal;
        };
    }

    /**
     * Need to overridden by the implementing class to give details about the State <I>Used by Inbox to fetch the State Detail at
     * runtime</I>
     *
     * @return String Detail
     */
    public abstract String getStateDetails();

    /**
     * To set the Group Link, Any State Aware Object which needs Grouping should override this method
     **/
    public String myLinkId() {
        return getId().toString();
    }

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
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
        return state == null ? Collections.emptyList() : new LinkedList<>(state.getHistory());
    }

    public final String getStateType() {
        return this.getClass().getSimpleName();
    }

    public final boolean transitionInitialized() {
        return hasState() && getCurrentState().isNew();
    }

    public final boolean transitionCompleted() {
        return hasState() && getCurrentState().isEnded();
    }

    public final boolean transitionInprogress() {
        return hasState() && getCurrentState().isInprogress();
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
            throw new ApplicationRuntimeException("Workflow already started state.");
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
        if (transitionCompleted())
            throw new ApplicationRuntimeException("Workflow already ended state.");
        else {
            state.setValue(State.DEFAULT_STATE_VALUE_CLOSED);
            state.setStatus(StateStatus.ENDED);
            state.setComments(State.DEFAULT_STATE_VALUE_CLOSED);
        }
        return this;
    }

    public final StateAware reopen(final boolean clone) {
        if (transitionCompleted()) {
            final StateHistory stateHistory = new StateHistory(state);
            stateHistory.setValue(State.STATE_REOPENED);
            state.setStatus(StateStatus.INPROGRESS);
            state.addStateHistory(stateHistory);
            if (!clone)
                resetState();
        } else
            throw new ApplicationRuntimeException("Workflow not ended.");
        return this;
    }

    public final StateAware reinitiateTransition() {
        if (state != null && !transitionCompleted())
            throw new ApplicationRuntimeException("Could not reinitiate Workflow, existing workflow not ended.");
        else
            state = null;
        return this;
    }

    public final StateAware withOwner(final User owner) {
        state.setOwnerUser(owner);
        return this;
    }

    public final StateAware withOwner(final Position owner) {
        state.setOwnerPosition(owner);
        return this;
    }

    public final StateAware withInitiator(final Position owner) {
        state.setInitiatorPosition(owner);
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

    public final StateAware withNatureOfTask(final String natureOfTask) {
        state.setNatureOfTask(natureOfTask);
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
        state.setComments(EMPTY);
        state.setDateInfo(null);
        state.setExtraDateInfo(null);
        state.setExtraInfo(EMPTY);
        state.setNextAction(EMPTY);
        state.setValue(EMPTY);
        state.setSenderName(EMPTY);
        state.setNatureOfTask(EMPTY);
        state.setOwnerUser(null);
        state.setOwnerPosition(null);
        state.setInitiatorPosition(null);
    }

    protected StateInfoBuilder buildStateInfo() {
        return new StateInfoBuilder().task(this.getState().getNatureOfTask()).
                itemDetails(this.getStateDetails()).status(getCurrentState().getStatus().name()).
                refDate(this.getCreatedDate()).sender(this.getState().getSenderName()).
                senderPhoneno(this.getState().getExtraInfo());
    }

    public String getStateInfoJson() {
        return this.buildStateInfo().toJson();
    }
}
