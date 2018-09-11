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
package org.egov.collection.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.commons.CChartOfAccounts;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.infstr.models.ServiceDetails;
import org.egov.model.instrument.InstrumentType;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "EGCL_SERVICE_INSTRUMENTACCOUNTS")
@SequenceGenerator(name = ServiceInstrumentAccounts.SEQ_EGCL_SERVICE_INSTRUMENTACCOUNTS, sequenceName = ServiceInstrumentAccounts.SEQ_EGCL_SERVICE_INSTRUMENTACCOUNTS, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "createdDate") })
public class ServiceInstrumentAccounts extends AbstractAuditable implements Auditable {

    private static final long serialVersionUID = -3603055750726983915L;

    public static final String SEQ_EGCL_SERVICE_INSTRUMENTACCOUNTS = "SEQ_EGCL_SERVICE_INSTRUMENTACCOUNTS";

    @Id
    @GeneratedValue(generator = SEQ_EGCL_SERVICE_INSTRUMENTACCOUNTS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chartofaccounts", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private CChartOfAccounts chartOfAccounts;

    @ManyToOne
    @JoinColumn(name = "servicedetails", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ServiceDetails serviceDetails;

    @ManyToOne
    @JoinColumn(name = "instrumenttype", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private InstrumentType instrumentType;

    @ManyToOne
    @JoinColumn(name = "billservicedetails")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ServiceDetails billServiceDetails;

    public CChartOfAccounts getChartOfAccounts() {
        return chartOfAccounts;
    }

    public void setChartOfAccounts(final CChartOfAccounts chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

    public ServiceDetails getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(final ServiceDetails serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(final InstrumentType instrumentType) {
        this.instrumentType = instrumentType;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public ServiceDetails getBillServiceDetails() {
        return billServiceDetails;
    }

    public void setBillServiceDetails(ServiceDetails billServiceDetails) {
        this.billServiceDetails = billServiceDetails;
    }

}
