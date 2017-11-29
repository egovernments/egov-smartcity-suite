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
package org.egov.portal.entity;

import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.workflow.entity.State;
import org.egov.portal.entity.enums.MessageType;
import org.egov.portal.entity.enums.Priority;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import java.util.Date;

import static org.egov.portal.entity.CitizenInbox.SEQ_CITIZENINBOX;

/**
 * CitizenInbox class
 *
 * @author rishi
 */
@Entity
@Table(name = "egp_citizeninbox")
@SequenceGenerator(name = SEQ_CITIZENINBOX, sequenceName = SEQ_CITIZENINBOX, allocationSize = 1)
public class CitizenInbox extends AbstractAuditable {

    public static final String SEQ_CITIZENINBOX = "seq_egp_citizeninbox";
    private static final long serialVersionUID = -2303996521024126504L;
    @Id
    @GeneratedValue(generator = SEQ_CITIZENINBOX, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODULE_ID")
    private Module module;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "MESSAGE_TYPE")
    private MessageType messageType;

    @Length(max = 50)
    @Column(name = "IDENTIFIER")
    private String identifier;

    @NotNull
    @Length(max = 500)
    @Column(name = "HEADER_MSG")
    private String headerMessage;

    @NotNull
    @Length(max = 2048)
    @Column(name = "DETAILED_MSG")
    private String detailedMessage;

    @Length(max = 256)
    @Column(name = "LINK")
    private String link;

    @Column(name = "READ")
    private boolean read;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "MSG_DATE")
    private Date messageDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATE_ID")
    private State state;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSIGNED_TO_USER")
    private User assignedToCitizen;

    @NotNull
    @Length(max = 20)
    @Column(name = "PRIORITY")
    private Priority priority;

    @Length(max = 100)
    @Column(name = "STATUS")
    private String status;

    public Module getModule() {
        return module;
    }

    public void setModule(final Module module) {
        this.module = module;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(final MessageType messageType) {
        this.messageType = messageType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public String getHeaderMessage() {
        return headerMessage;
    }

    public void setHeaderMessage(final String headerMessage) {
        this.headerMessage = headerMessage;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(final String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(final boolean read) {
        this.read = read;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(final Date messageDate) {
        this.messageDate = messageDate;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public User getAssignedToCitizen() {
        return assignedToCitizen;
    }

    public void setAssignedToCitizen(final User assignedToCitizen) {
        this.assignedToCitizen = assignedToCitizen;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }
}
