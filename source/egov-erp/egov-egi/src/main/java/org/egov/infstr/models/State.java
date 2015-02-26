package org.egov.infstr.models;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.NotEmpty;

public class State extends BaseModel implements Cloneable {

    private static final long serialVersionUID = -9159043292636575746L;

    public static final String DEFAULT_STATE_VALUE_CREATED = "Created";
    public static final String DEFAULT_STATE_VALUE_CLOSED = "Closed";
    public static final String STATE_REOPENED = "Reopened";
    public static final String WORKFLOWTYPES_QRY = "WORKFLOWTYPES";
    public static final String WORKFLOWTYPES_BY_ID = "WORKFLOWTYPES_BY_ID";
    private @NotEmpty String type;
    private @NotEmpty String value;
    private Position ownerPosition;
    private User ownerUser;
    private List<StateHistory> history = Collections.emptyList();
    private String senderName;
    private String nextAction;
    private String comments;
    private String extraInfo;
    private Date dateInfo;
    private Date extraDateInfo;
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

    public User getOwnerUser() {
        return ownerUser;
    }

    protected void setOwnerUser(final User ownerUser) {
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
