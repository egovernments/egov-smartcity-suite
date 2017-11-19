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
package org.egov.wtms.masters.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;

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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "egwtr_water_rates_header")
@SequenceGenerator(name = WaterRatesHeader.SEQ_WATERRATESHEADER, sequenceName = WaterRatesHeader.SEQ_WATERRATESHEADER, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
    @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class WaterRatesHeader extends AbstractAuditable {

    private static final long serialVersionUID = -2596548687171468023L;
    public static final String SEQ_WATERRATESHEADER = "SEQ_EGWTR_WATER_RATES_HEADER";

    @Id
    @GeneratedValue(generator = SEQ_WATERRATESHEADER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Audited
    private ConnectionType connectionType;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "usagetype", nullable = false)
    @Audited
    private UsageType usageType;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "watersource", nullable = false)
    @Audited
    private WaterSource waterSource;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "pipesize", nullable = false)
    @Audited
    private PipeSize pipeSize;

    @Audited
    private boolean active;

    @OneToMany(mappedBy = "waterRatesHeader", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @Audited
    private List<WaterRatesDetails> waterRatesDetails = new ArrayList<WaterRatesDetails>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public UsageType getUsageType() {
        return usageType;
    }

    public void setUsageType(final UsageType usageType) {
        this.usageType = usageType;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public List<WaterRatesDetails> getWaterRatesDetails() {
        return waterRatesDetails;
    }

    public void setWaterRatesDetails(final List<WaterRatesDetails> waterRatesDetails) {
        this.waterRatesDetails.clear();
        this.waterRatesDetails.addAll(waterRatesDetails);
    }

    public void addWaterRatesDetails(final WaterRatesDetails waterRatesDetail) {
        waterRatesDetails.add(waterRatesDetail);
    }

}