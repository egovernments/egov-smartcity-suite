/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.application.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.demand.model.EgDemand;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.wtms.masters.entity.ApplicationType;
import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.entity.PipeSize;
import org.egov.wtms.masters.entity.PropertyType;
import org.egov.wtms.masters.entity.UsageType;
import org.egov.wtms.masters.entity.WaterSource;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;

@Entity
@Table(name = "egwtr_connectiondetails")
@SequenceGenerator(name = WaterConnectionDetails.SEQ_CONNECTIONDETAILS, sequenceName = WaterConnectionDetails.SEQ_CONNECTIONDETAILS, allocationSize = 1)
public class WaterConnectionDetails extends StateAware {

    private static final long serialVersionUID = -4667948558401042849L;
    public static final String SEQ_CONNECTIONDETAILS = "SEQ_EGWTR_CONNECTIONDETAILS";

    @Id
    @GeneratedValue(generator = SEQ_CONNECTIONDETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "applicationtype", nullable = false)
    private ApplicationType applicationType;

    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "connection", nullable = false)
    private WaterConnection connection;

    // @Column(name = "applicationNumber", unique = true)
    private String applicationNumber;

    @Temporal(value = TemporalType.DATE)
    private Date applicationDate;

    @Temporal(value = TemporalType.DATE)
    private Date disposalDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ConnectionType connectionType;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "category", nullable = false)
    private ConnectionCategory category;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "usageType", nullable = false)
    private UsageType usageType;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "propertyType", nullable = false)
    private PropertyType propertyType;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "waterSource", nullable = false)
    private WaterSource waterSource;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "pipeSize", nullable = false)
    private PipeSize pipeSize;

    @NotNull
    private Long sumpCapacity;

    private Long plotSize;

    private Long plinthArea;

    private Integer numberOfPerson;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ConnectionStatus connectionStatus;

    private String approvalNumber;

    @Temporal(value = TemporalType.DATE)
    private Date approvalDate;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "demand")
    private EgDemand demand;

    @OneToMany(mappedBy = "waterConnectionDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApplicationDocuments> applicationDocs = new ArrayList<ApplicationDocuments>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public WaterConnection getConnection() {
        return connection;
    }

    public void setConnection(final WaterConnection connection) {
        this.connection = connection;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(final Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(final Date disposalDate) {
        this.disposalDate = disposalDate;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public ConnectionCategory getCategory() {
        return category;
    }

    public void setCategory(final ConnectionCategory category) {
        this.category = category;
    }

    public UsageType getUsageType() {
        return usageType;
    }

    public void setUsageType(final UsageType usageType) {
        this.usageType = usageType;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public WaterSource getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(final WaterSource waterSource) {
        this.waterSource = waterSource;
    }

    public PipeSize getPipeSize() {
        return pipeSize;
    }

    public void setPipeSize(final PipeSize pipeSize) {
        this.pipeSize = pipeSize;
    }

    public Long getSumpCapacity() {
        return sumpCapacity;
    }

    public void setSumpCapacity(final Long sumpCapacity) {
        this.sumpCapacity = sumpCapacity;
    }

    public Long getPlotSize() {
        return plotSize;
    }

    public void setPlotSize(final Long plotSize) {
        this.plotSize = plotSize;
    }

    public Long getPlinthArea() {
        return plinthArea;
    }

    public void setPlinthArea(final Long plinthArea) {
        this.plinthArea = plinthArea;
    }

    public Integer getNumberOfPerson() {
        return numberOfPerson;
    }

    public void setNumberOfPerson(final Integer numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(final ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getApprovalNumber() {
        return approvalNumber;
    }

    public void setApprovalNumber(final String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(final Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public EgDemand getDemand() {
        return demand;
    }

    public void setDemand(final EgDemand demand) {
        this.demand = demand;
    }

    public List<ApplicationDocuments> getApplicationDocs() {
        return applicationDocs;
    }

    public void setApplicationDocs(final List<ApplicationDocuments> applicationDocs) {
        this.applicationDocs = applicationDocs;
    }

    @Override
    public String getStateDetails() {
        return String.format("Application Number %s for %s with application date %s.", applicationNumber,
                applicationType.getName(), applicationDate);
    }
}