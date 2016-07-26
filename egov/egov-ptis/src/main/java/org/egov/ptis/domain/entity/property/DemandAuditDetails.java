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
