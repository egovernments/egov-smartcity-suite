package org.egov.infstr.models;

import java.io.Serializable;
import java.util.Date;

import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.Position;

public class StateHistory implements Serializable {
    private static final long serialVersionUID = -2286621991905578107L;
    private Long id;
    private User createdBy;
    private Date createdDate;
    private User modifiedBy;
    private Date modifiedDate;
    private State state;
    private String value;
    private Position ownerPosition;
    private User ownerUser;
    private String senderName;
    private String nextAction;
    private String comments;
    private String extraInfo;
    private Date dateInfo;
    private Date extraDateInfo;
    
    StateHistory() {
    }
       
    public StateHistory(State state) {
        this.state = state;
        this.createdBy = state.getCreatedBy();
        this.createdDate = state.getCreatedDate();
        this.modifiedBy = state.getModifiedBy();
        this.modifiedDate = state.getModifiedDate();
        this.value = state.getValue();
        this.ownerPosition = state.getOwnerPosition();
        this.ownerUser = state.getOwnerUser();
        this.senderName = state.getSenderName();
        this.nextAction = state.getNextAction();
        this.comments = state.getComments();
        this.extraInfo = state.getExtraInfo();
        this.dateInfo = state.getDateInfo();
        this.extraDateInfo = state.getExtraDateInfo();
    }
    
    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public Position getOwnerPosition() {
        return ownerPosition;
    }

    public void setOwnerPosition(Position ownerPosition) {
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

    public void setNextAction(final String nextAction) {
        this.nextAction = nextAction;
    }


    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}
