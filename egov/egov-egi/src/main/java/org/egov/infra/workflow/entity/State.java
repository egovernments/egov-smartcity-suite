/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
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
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
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
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.egov.infra.workflow.entity.State.SEQ_STATE;

@Entity
@Table(name = "EG_WF_STATES")
@SequenceGenerator(name = SEQ_STATE, sequenceName = SEQ_STATE, allocationSize = 1)
public class State extends AbstractAuditable {

    public static final String DEFAULT_STATE_VALUE_CREATED = "Created";
    public static final String DEFAULT_STATE_VALUE_CLOSED = "Closed";
    public static final String STATE_REOPENED = "Reopened";
    protected static final String SEQ_STATE = "SEQ_EG_WF_STATES";
    private static final long serialVersionUID = -9159043292636575746L;

    @Id
    @GeneratedValue(generator = SEQ_STATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String type;

    @NotNull
    @Length(min = 1)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_POS")
    private Position ownerPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_USER")
    private User ownerUser;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "state")
    @OrderBy("id")
    private Set<StateHistory> history = new HashSet<>();

    private String senderName;

    private String nextAction;

    private String comments;

    private String natureOfTask;

    private String extraInfo;

    private Date dateInfo;

    private Date extraDateInfo;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private StateStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INITIATOR_POS")
    private Position initiatorPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previousOwner")
    private Position previousOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previousStateRef")
    private State previousStateRef;

    protected State() {
        //Explicit state initialization not allowed
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    protected void setType(final String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    protected void setValue(final String value) {
        this.value = value;
    }

    public Position getOwnerPosition() {
        return ownerPosition;
    }

    protected void setOwnerPosition(final Position ownerPosition) {
        this.ownerPosition = ownerPosition;
    }

    public User getOwnerUser() {
        return ownerUser;
    }

    protected void setOwnerUser(final User ownerUser) {
        this.ownerUser = ownerUser;
    }

    public Set<StateHistory> getHistory() {
        return history;
    }

    protected void setHistory(final Set<StateHistory> history) {
        this.history = history;
    }

    protected void addStateHistory(final StateHistory history) {
        getHistory().add(history);
    }

    public String getSenderName() {
        return senderName;
    }

    protected void setSenderName(final String senderName) {
        this.senderName = senderName;
    }

    public String getNextAction() {
        return nextAction;
    }

    protected void setNextAction(final String nextAction) {
        this.nextAction = nextAction;
    }

    public String getComments() {
        return comments;
    }

    protected void setComments(final String comments) {
        this.comments = comments;
    }

    public String getNatureOfTask() {
        return natureOfTask;
    }

    protected void setNatureOfTask(final String natureOfTask) {
        this.natureOfTask = natureOfTask;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    protected void setExtraInfo(final String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Date getDateInfo() {
        return dateInfo;
    }

    protected void setDateInfo(final Date dateInfo) {
        this.dateInfo = dateInfo;
    }

    public Date getExtraDateInfo() {
        return extraDateInfo;
    }

    protected void setExtraDateInfo(final Date extraDateInfo) {
        this.extraDateInfo = extraDateInfo;
    }

    protected StateStatus getStatus() {
        return status;
    }

    protected void setStatus(final StateStatus status) {
        this.status = status;
    }

    @Override
    public boolean isNew() {
        return status.equals(StateStatus.STARTED);
    }

    public boolean isEnded() {
        return status.equals(StateStatus.ENDED);
    }

    public boolean isInprogress() {
        return status.equals(StateStatus.INPROGRESS);
    }

    public Position getInitiatorPosition() {
        return initiatorPosition;
    }

    protected void setInitiatorPosition(Position initiatorPosition) {
        this.initiatorPosition = initiatorPosition;
    }

    public Position getPreviousOwner() {
        return previousOwner;
    }

    protected void setPreviousOwner(final Position previousOwner) {
        this.previousOwner = previousOwner;
    }

    public State getPreviousStateRef() {
        return previousStateRef;
    }

    protected void setPreviousStateRef(State previousStateRef) {
        this.previousStateRef = previousStateRef;
    }

    public enum StateStatus {
        STARTED, INPROGRESS, ENDED
    }

}