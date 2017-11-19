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
package org.egov.commons.entity;

import org.egov.commons.Bankaccount;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.infstr.models.ECSType;
import org.egov.infstr.models.ServiceDetails;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "EGCL_BANKACCOUNTSERVICEMAPPING")
@SequenceGenerator(name = BankAccountServiceMap.SEQ_EGCL_BANKACCOUNTSERVICEMAPPING, sequenceName = BankAccountServiceMap.SEQ_EGCL_BANKACCOUNTSERVICEMAPPING, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "createdDate") })
public class BankAccountServiceMap extends AbstractAuditable implements Auditable {

    private static final long serialVersionUID = -5728387790753010430L;

    public static final String SEQ_EGCL_BANKACCOUNTSERVICEMAPPING = "SEQ_EGCL_BANKACCOUNTSERVICEMAPPING";

    @Id
    @GeneratedValue(generator = SEQ_EGCL_BANKACCOUNTSERVICEMAPPING, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bankaccount", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Bankaccount bankAccountId;

    @ManyToOne
    @JoinColumn(name = "department")
    private Department deptId;

    @ManyToOne
    @JoinColumn(name = "servicedetails", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ServiceDetails serviceDetails;

    private Date fromDate;

    private Date toDate;

    @ManyToOne
    @JoinColumn(name = "ecsType")
    private ECSType ecsType;

    public Bankaccount getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(final Bankaccount bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public Department getDeptId() {
        return deptId;
    }

    public void setDeptId(final Department deptId) {
        this.deptId = deptId;
    }

    public ServiceDetails getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(final ServiceDetails serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public ECSType getEcsType() {
        return ecsType;
    }

    public void setEcsType(final ECSType ecsType) {
        this.ecsType = ecsType;
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
