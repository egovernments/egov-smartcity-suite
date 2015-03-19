package org.egov.infra.workflow.entity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.UserImpl;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="EG_WF_STATES")
@NamedQueries({
    @NamedQuery(name="WORKFLOWTYPES",query="select distinct s.type from State s where s.ownerPosition.id=?  and s.status is not 2"),
    @NamedQuery(name="WORKFLOWTYPES_BY_ID",query="select s from State s where s.id=?")
})
public class State extends AbstractAuditable<UserImpl, Long> {

    private static final long serialVersionUID = -9159043292636575746L;

    public static final String DEFAULT_STATE_VALUE_CREATED = "Created";
    public static final String DEFAULT_STATE_VALUE_CLOSED = "Closed";
    public static final String STATE_REOPENED = "Reopened";
    public static final String WORKFLOWTYPES_QRY = "WORKFLOWTYPES";
    public static final String WORKFLOWTYPES_BY_ID = "WORKFLOWTYPES_BY_ID";
    public static enum StateStatus {
        STARTED,INPROGRESS,ENDED
    }
    
    @NotNull
    private String type;
    
    @NotNull
    @Length(min=1)
    private String value;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OWNER_POS")
    private Position ownerPosition;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OWNER_USER")
    private UserImpl ownerUser;
    
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="state")
    @OrderBy("id")
    private List<StateHistory> history = Collections.emptyList();
    
    private String senderName;
    private String nextAction;
    private String comments;
    private String extraInfo;
    private Date dateInfo;
    private Date extraDateInfo;
    
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private StateStatus status;
    
    protected State() {
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

    public UserImpl getOwnerUser() {
        return ownerUser;
    }

    protected void setOwnerUser(final UserImpl ownerUser) {
        this.ownerUser = ownerUser;
    }

    public List<StateHistory> getHistory() {
        return history;
    }

    protected void setHistory(final List<StateHistory> history) {
        this.history = history;
    }
    
    protected void addStateHistory(StateHistory history) {
        this.getHistory().add(history);
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

    public String getExtraInfo() {
        return extraInfo;
    }

    protected void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Date getDateInfo() {
        return dateInfo;
    }

    protected void setDateInfo(Date dateInfo) {
        this.dateInfo = dateInfo;
    }

    public Date getExtraDateInfo() {
        return extraDateInfo;
    }

    protected void setExtraDateInfo(Date extraDateInfo) {
        this.extraDateInfo = extraDateInfo;
    }

    protected StateStatus getStatus() {
        return status;
    }

    protected void setStatus(StateStatus status) {
        this.status = status;
    }

    public boolean isNew() {
        return status.equals(StateStatus.STARTED);
    }
    
    public boolean isEnded() {
        return status.equals(StateStatus.ENDED);
    }
}
