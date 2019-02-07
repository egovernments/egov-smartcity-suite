/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egwtr_connection_address")
@SequenceGenerator(name = ConnectionAddress.SEQ_EGWTR_CONNECTION_ADDRESS, sequenceName = ConnectionAddress.SEQ_EGWTR_CONNECTION_ADDRESS, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class ConnectionAddress extends AbstractAuditable {

    private static final long serialVersionUID = 1L;
    public static final String SEQ_EGWTR_CONNECTION_ADDRESS = "SEQ_EGWTR_CONNECTION_ADDRESS";

    @Id
    @GeneratedValue(generator = SEQ_EGWTR_CONNECTION_ADDRESS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "connectiondetailsid", nullable = false)
    private WaterConnectionDetails waterConnectionDetails;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Length(min = 1, max = 50)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "zone")
    private Boundary zone;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Length(min = 1, max = 50)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "adminWard")
    private Boundary adminWard;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Length(min = 1, max = 50)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "revenueWard")
    private Boundary revenueWard;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Length(min = 1, max = 50)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "locality")
    private Boundary locality;

    @Length(min = 1, max = 150)
    @Audited
    @SafeHtml
    private String ownerName;

    @Length(min = 1, max = 250)
    @Audited
    @SafeHtml
    private String address;

    @Length(min = 1, max = 50)
    @Audited
    @SafeHtml
    private String doorNumber;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Boundary getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(Boundary revenueWard) {
        this.revenueWard = revenueWard;
    }

    public WaterConnectionDetails getWaterConnectionDetails() {
        return waterConnectionDetails;
    }

    public void setWaterConnectionDetails(WaterConnectionDetails waterConnectionDetails) {
        this.waterConnectionDetails = waterConnectionDetails;
    }

    public Boundary getZone() {
        return zone;
    }

    public void setZone(Boundary zone) {
        this.zone = zone;
    }

    public Boundary getAdminWard() {
        return adminWard;
    }

    public void setAdminWard(Boundary adminWard) {
        this.adminWard = adminWard;
    }

    public Boundary getLocality() {
        return locality;
    }

    public void setLocality(Boundary locality) {
        this.locality = locality;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

}
