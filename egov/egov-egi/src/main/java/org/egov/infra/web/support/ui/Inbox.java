/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.web.support.ui;

import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.entity.WorkflowTypes;

import java.util.Date;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;
import static org.egov.infra.utils.DateUtils.toDefaultDateTimeFormat;
import static org.egov.infra.utils.StringUtils.escapeSpecialChars;

public class Inbox {
    private String id;
    private String sender;
    private String date;
    private String task;
    private String status;
    private String details;
    private String link;
    private String moduleName;
    private Date createdDate;
    private boolean draft;

    public Inbox() {
        //Default constructor for external inbox integration
    }

    private Inbox(StateAware stateAware, WorkflowTypes workflowTypes, String nextAction) {
        State state = stateAware.getCurrentState();
        setId(workflowTypes.isGrouped() ? EMPTY : state.getId() + "#" + workflowTypes.getId());
        setDate(toDefaultDateTimeFormat(state.getCreatedDate()));
        setSender(state.getSenderName());
        setTask(isBlank(state.getNatureOfTask()) ? workflowTypes.getDisplayName() : state.getNatureOfTask());
        setStatus(state.getValue() + (isBlank(nextAction) ? EMPTY : " - " + nextAction));
        setDetails(isBlank(stateAware.getStateDetails()) ? EMPTY : stateAware.getStateDetails());
        setLink(workflowTypes.getLink().replace(":ID", stateAware.myLinkId()));
        setModuleName(workflowTypes.getModule().getDisplayName());
        setCreatedDate(state.getCreatedDate());
        setDraft(state.isNew() && state.getCreatedBy().getId().equals(getUserId()));
    }

    private Inbox(StateHistory stateHistory, WorkflowTypes workflowTypes) {
        setId(stateHistory.getState().getId().toString());
        setDate(toDefaultDateTimeFormat(stateHistory.getLastModifiedDate()));
        setSender(stateHistory.getSenderName());
        setTask(isBlank(stateHistory.getNatureOfTask()) ? workflowTypes.getDisplayName()
                : stateHistory.getNatureOfTask());
        setStatus(stateHistory.getValue()
                + (isBlank(stateHistory.getNextAction()) ? EMPTY : " - " + stateHistory.getNextAction()));
        setDetails(
                isBlank(stateHistory.getComments()) ? EMPTY : escapeSpecialChars(stateHistory.getComments()));
        setLink(EMPTY);
    }

    public static Inbox build(StateAware stateAware, WorkflowTypes workflowType, String nextAction) {
        return new Inbox(stateAware, workflowType, nextAction);
    }

    public static Inbox buildHistory(StateHistory stateHistory, WorkflowTypes workflowType) {
        return new Inbox(stateHistory, workflowType);
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getTask() {
        return task;
    }

    public void setTask(final String task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(final boolean draft) {
        this.draft = draft;
    }
}
