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
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Immutable
@Table(name = "eg_wf_state_history")
@SequenceGenerator(name = StateHistory.SEQ_STATEHISTORY, sequenceName = StateHistory.SEQ_STATEHISTORY, allocationSize = 1)
public class StateHistory<T extends OwnerGroup> implements Serializable {
    static final String SEQ_STATEHISTORY = "SEQ_EG_WF_STATE_HISTORY";
    private static final long serialVersionUID = -2286621991905578107L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_STATEHISTORY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy")
    private User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lastModifiedBy")
    private User lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @ManyToOne(targetEntity = State.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "state_id")
    private State<T> state;

    @NotNull
    private String value;

    @ManyToOne(targetEntity = OwnerGroup.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_POS")
    private T ownerPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_USER")
    private User ownerUser;

    private String senderName;
    private String nextAction;
    private String comments;
    private String natureOfTask;
    private String extraInfo;
    private Date dateInfo;
    private Date extraDateInfo;

    @ManyToOne(targetEntity = OwnerGroup.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "INITIATOR_POS")
    private T initiatorPosition;

    StateHistory() {
    }

    public StateHistory(State<T> state) {
        this.state = state;
        createdBy = state.getCreatedBy();
        createdDate = state.getCreatedDate();
        lastModifiedBy = state.getLastModifiedBy();
        lastModifiedDate = state.getLastModifiedDate();
        value = state.getValue();
        ownerPosition = state.getOwnerPosition();
        ownerUser = state.getOwnerUser();
        senderName = state.getSenderName();
        nextAction = state.getNextAction();
        comments = state.getComments();
        extraInfo = state.getExtraInfo();
        dateInfo = state.getDateInfo();
        extraDateInfo = state.getExtraDateInfo();
        natureOfTask = state.getNatureOfTask();
        initiatorPosition = state.getInitiatorPosition();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public T getOwnerPosition() {
        return ownerPosition;
    }

    public void setOwnerPosition(T ownerPosition) {
        this.ownerPosition = ownerPosition;
    }

    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getNatureOfTask() {
        return natureOfTask;
    }

    public void setNatureOfTask(String natureOfTask) {
        this.natureOfTask = natureOfTask;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Date getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(Date dateInfo) {
        this.dateInfo = dateInfo;
    }

    public Date getExtraDateInfo() {
        return extraDateInfo;
    }

    public void setExtraDateInfo(Date extraDateInfo) {
        this.extraDateInfo = extraDateInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public T getInitiatorPosition() {
        return initiatorPosition;
    }

    public void setInitiatorPosition(T initiatorPosition) {
        this.initiatorPosition = initiatorPosition;
    }

}