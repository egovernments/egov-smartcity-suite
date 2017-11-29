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
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.workflow.entity.State;
import org.egov.portal.entity.enums.Priority;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.egov.portal.entity.PortalInbox.SEQ_PORTALINBOX;

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

    @Length(max = 50)
    private String entityRefNumber;

    private Long entityRefId;

    @Length(max = 256)
    private String headerMessage;

    @NotNull
    @Length(max = 2048)
    private String detailedMessage;

    @Length(max = 256)
    private String link;

    private boolean read;

    private boolean resolved;

    private Date resolvedDate;

    private Date slaEndDate;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date applicationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATE_ID")
    private State state;

    @OrderBy("id")
    @OneToMany(mappedBy = "portalInbox", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<PortalInboxUser> portalInboxUsers = new ArrayList<>(0);

    @Transient
    private List<PortalInboxUser> tempPortalInboxUser = new ArrayList<>(0);

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

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(final boolean resolved) {
        this.resolved = resolved;
    }

    public String getHeaderMessage() {
        return headerMessage;
    }

    public void setHeaderMessage(final String headerMessage) {
        this.headerMessage = headerMessage;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public List<PortalInboxUser> getTempPortalInboxUser() {
        return tempPortalInboxUser;
    }

    public void setTempPortalInboxUser(final List<PortalInboxUser> tempPortalInboxUser) {
        this.tempPortalInboxUser = tempPortalInboxUser;
    }

    public Date getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(final Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

}