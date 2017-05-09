/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.portal.entity;

import static org.egov.portal.entity.PortalInbox.SEQ_PORTALINBOX;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.workflow.entity.State;
import org.egov.portal.entity.enums.Priority;
import org.hibernate.validator.constraints.Length;

/**
 * PortalInbox class
 *
 * @author Pradeep
 */
@Entity
@Table(name = "egp_inbox")
@SequenceGenerator(name = SEQ_PORTALINBOX, sequenceName = SEQ_PORTALINBOX, allocationSize = 1)
public class PortalInbox extends AbstractAuditable {

    public static final String SEQ_PORTALINBOX = "seq_egp_inbox";
    private static final long serialVersionUID = -2303996521024126504L;
    @Id
    @GeneratedValue(generator = SEQ_PORTALINBOX, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODULEID")
    private Module module;

    @NotNull
    @Length(max = 128)
    private String serviceType;

    @Length(max = 50)
    private String applicationNumber;

    @NotNull
    @Length(max = 50)
    private String entityRefNumber;

    @NotNull
    private Long entityRefId;

    @Length(max = 256)
    private String headermessage;

    @NotNull
    @Length(max = 2048)
    private String detailedMessage;

    @Length(max = 256)
    private String link;

    private boolean read;

    private boolean resolved;

    private Date slaEndDate;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date applicationdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATE_ID")
    private State state;

    @OrderBy("id")
    @OneToMany(mappedBy = "portalInbox", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<PortalInboxUser> portalInboxUsers = new ArrayList<>(0);

    @Length(max = 20)
    private Priority priority;

    @Length(max = 100)
    private String status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(final Module module) {
        this.module = module;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(final String serviceType) {
        this.serviceType = serviceType;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getEntityRefNumber() {
        return entityRefNumber;
    }

    public void setEntityRefNumber(final String entityRefNumber) {
        this.entityRefNumber = entityRefNumber;
    }

    public Long getEntityRefId() {
        return entityRefId;
    }

    public void setEntityRefId(final Long entityRefId) {
        this.entityRefId = entityRefId;
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

    public Date getSlaEndDate() {
        return slaEndDate;
    }

    public void setSlaEndDate(final Date slaEndDate) {
        this.slaEndDate = slaEndDate;
    }

    public Date getApplicationdate() {
        return applicationdate;
    }

    public void setApplicationdate(final Date applicationdate) {
        this.applicationdate = applicationdate;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
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

    public List<PortalInboxUser> getPortalInboxUsers() {
        return portalInboxUsers;
    }

    public String getHeadermessage() {
        return headermessage;
    }

    public void setHeadermessage(final String headermessage) {
        this.headermessage = headermessage;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(final boolean resolved) {
        this.resolved = resolved;
    }

}
