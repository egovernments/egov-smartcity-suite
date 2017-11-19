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

package org.egov.ptis.domain.entity.property;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;

@SuppressWarnings("serial")
@Entity
@Table(name = "EGPT_DEMANDAUDIT")
@Unique(id = "id", tableName = "EGPT_DEMANDAUDIT")

@SequenceGenerator(name = DemandAudit.SEQ_DEMANDAUDIT, sequenceName = DemandAudit.SEQ_DEMANDAUDIT, allocationSize = 1)
public class DemandAudit extends AbstractAuditable {

    private static final long serialVersionUID = 1L;
    public static final String SEQ_DEMANDAUDIT = "SEQ_EGPT_DEMANDAUDIT";

    @Id
    @GeneratedValue(generator = SEQ_DEMANDAUDIT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "assessmentno")
    private String basicproperty;

    @Column(name = "transaction")
    private String transaction;

    @OneToMany(mappedBy = "demandAudit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DemandAuditDetails> demandAuditDetails = new ArrayList<DemandAuditDetails>();

    @Column(name = "remarks")
    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBasicproperty() {
        return basicproperty;
    }

    public void setBasicproperty(String basicproperty) {
        this.basicproperty = basicproperty;
    }

    public void setAuditDemandDetails(List<DemandAuditDetails> demandAuditDetails) {
        this.demandAuditDetails = demandAuditDetails;
    }

    public List<DemandAuditDetails> getDemandAuditDetails() {
        return demandAuditDetails;
    }

}
