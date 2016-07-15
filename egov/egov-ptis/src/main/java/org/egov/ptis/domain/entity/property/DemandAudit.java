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
