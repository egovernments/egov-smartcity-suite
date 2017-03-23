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

package org.egov.infra.workflow.entity.contract;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.State.StateStatus;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.multitenant.model.Task;
import org.egov.pims.commons.Position;

public class StateModel {

	private Long id;

	private String type;

	private String value;

	private Position ownerPosition;

	private User ownerUser;

	private Set<StateHistory> history = new HashSet<>();

	private String senderName;

	private String nextAction;

	private String comments;

	private String natureOfTask;

	private String extraInfo;

	private Date dateInfo;

	private Date extraDateInfo;

	private StateStatus status;

	private Position initiatorPosition;

	private State previousStateRef;

	private String myLinkId;

	private String tenantId;

	private User createdBy;

	private Date createdDate;

	private User lastModifiedBy;

	private Date lastModifiedDate;

	public Task map() {
		Task t = new Task();
		t.setBusinessKey(this.type);
		t.setComments(this.comments);
		t.setCreatedDate(this.createdDate);
		t.setId(this.id.toString());
		t.setStatus(this.value);
		t.setNatureOfTask(this.natureOfTask);
		t.setDetails(this.extraInfo == null ? "" : this.extraInfo);
		t.setSender(this.senderName);
		t.setUrl(this.myLinkId);
		return t;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
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

	public Set<StateHistory> getHistory() {
		return history;
	}

	public void setHistory(Set<StateHistory> history) {
		this.history = history;
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

	public StateStatus getStatus() {
		return status;
	}

	public void setStatus(StateStatus status) {
		this.status = status;
	}

	public Position getInitiatorPosition() {
		return initiatorPosition;
	}

	public void setInitiatorPosition(Position initiatorPosition) {
		this.initiatorPosition = initiatorPosition;
	}

	public State getPreviousStateRef() {
		return previousStateRef;
	}

	public void setPreviousStateRef(State previousStateRef) {
		this.previousStateRef = previousStateRef;
	}

	public String getMyLinkId() {
		return myLinkId;
	}

	public void setMyLinkId(String myLinkId) {
		this.myLinkId = myLinkId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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

}
