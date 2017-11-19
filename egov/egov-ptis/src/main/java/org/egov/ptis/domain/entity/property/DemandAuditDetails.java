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

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.validator.annotation.Unique;

@SuppressWarnings("serial")
@Entity
@Table(name = "EGPT_DEMANDAUDIT_DETAILS")
@Unique(id = "id", tableName = "EGPT_DEMANDAUDIT_DETAILS")

@SequenceGenerator(name = DemandAuditDetails.SEQ_DEMANDAUDITDETAILS, sequenceName = DemandAuditDetails.SEQ_DEMANDAUDITDETAILS, allocationSize = 1)
public class DemandAuditDetails {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;
    public static final String SEQ_DEMANDAUDITDETAILS = "SEQ_EGPT_DEMANDAUDIT_DETAILS";

    @Version
    private Long version;

    @Id
    @GeneratedValue(generator = SEQ_DEMANDAUDITDETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "action")
    private String action;

    @Column(name = "year")
    private String year;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddemandaudit")
    private DemandAudit demandAudit;

    @Column(name = "taxtype")
    private String taxType;

    @Column(name = "actual_dmd")
    private BigDecimal actualDmd;

    @Column(name = "modified_dmd")
    private BigDecimal modifiedDmd;

    @Column(name = "actual_coll")
    private BigDecimal actualColl;

    @Column(name = "modified_coll")
    private BigDecimal modifiedColl;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public BigDecimal getActualDmd() {
        return actualDmd;
    }

    public void setActualDmd(BigDecimal actualDmd) {
        this.actualDmd = actualDmd;
    }

    public BigDecimal getModifiedDmd() {
        return modifiedDmd;
    }

    public void setModifiedDmd(BigDecimal modifiedDmd) {
        this.modifiedDmd = modifiedDmd;
    }

    public BigDecimal getActualColl() {
        return actualColl;
    }

    public void setActualColl(BigDecimal actualColl) {
        this.actualColl = actualColl;
    }

    public BigDecimal getModifiedColl() {
        return modifiedColl;
    }

    public void setModifiedColl(BigDecimal modifiedColl) {
        this.modifiedColl = modifiedColl;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public DemandAudit getDemandAudit() {
        return demandAudit;
    }

    public void setDemandAudit(DemandAudit demandAudit) {
        this.demandAudit = demandAudit;
    }
    /*
     * public Long getAuditDemandID() { return auditDemandID; } public void
     * setAuditDemandID(Long auditDemandID) { this.auditDemandID =
     * auditDemandID; }
     */

}
