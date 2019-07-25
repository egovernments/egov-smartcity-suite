package org.egov.demand.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "eg_demand_detail_variation")
@SequenceGenerator(name = EgDemandDetailVariation.SEQ_DEMANDDETAIL_VARIATION, sequenceName = EgDemandDetailVariation.SEQ_DEMANDDETAIL_VARIATION, allocationSize = 1)
public class EgDemandDetailVariation {

    public static final String SEQ_DEMANDDETAIL_VARIATION = "SEQ_EG_DEMAND_DETAIL_VARIATION";
    
    @Id
    @GeneratedValue(generator = SEQ_DEMANDDETAIL_VARIATION, strategy = GenerationType.SEQUENCE)
    public Long id;

    @ManyToOne(targetEntity = EgDemandDetails.class, cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "demand_detail")
    private EgDemandDetails demandDetail;

    @ManyToOne(targetEntity = EgDemandReasonMaster.class,  cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "demand_reason_master")
    private EgDemandReasonMaster demandreasonMaster;

    @Column(name = "dramount")
    private BigDecimal dramount;

    @Column(name = "cramount")
    private BigDecimal cramount;

    @Column(name = "remarks")
    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EgDemandDetails getDemandDetail() {
        return demandDetail;
    }

    public void setDemandDetail(EgDemandDetails demandDetail) {
        this.demandDetail = demandDetail;
    }

    public EgDemandReasonMaster getDemandreasonMaster() {
        return demandreasonMaster;
    }

    public void setDemandreasonMaster(EgDemandReasonMaster demandreasonMaster) {
        this.demandreasonMaster = demandreasonMaster;
    }

    public BigDecimal getDramount() {
        return dramount;
    }

    public void setDramount(BigDecimal revisedAmount) {
        this.dramount = revisedAmount;
    }

    public BigDecimal getCramount() {
        return cramount;
    }

    public void setCramount(BigDecimal cramount) {
        this.cramount = cramount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
