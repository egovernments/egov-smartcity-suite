package org.egov.council.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.validator.annotation.Unique;

@Entity
@Table(name = "egcncl_councilsequence")
@Unique(id = "id", tableName = "egcncl_councilsequence", columnName = { "preambleSeqNumber" ,"agendaSeqNumber","resolutionSeqNumber"}, fields = {
"preambleSeqNumber" ,"agendaSeqNumber","resolutionSeqNumber"},enableDfltMsg = true)
@SequenceGenerator(name = CouncilSequenceNumber.SEQ_COUNCILSEQUENCENAME, sequenceName = CouncilSequenceNumber.SEQ_COUNCILSEQUENCENAME, allocationSize = 1)
public class CouncilSequenceNumber {

    public static final String SEQ_COUNCILSEQUENCENAME = "seq_egcncl_councilsequence";

    @Id
    @GeneratedValue(generator = SEQ_COUNCILSEQUENCENAME, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "preambleSeqNumber", unique = true)
    private String preambleSeqNumber;

    @Column(name = "agendaSeqNumber", unique = true)
    private String agendaSeqNumber;

    @Column(name = "resolutionSeqNumber", unique = true)
    private String resolutionSeqNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreambleSeqNumber() {
        return preambleSeqNumber;
    }

    public void setPreambleSeqNumber(String preambleSeqNumber) {
        this.preambleSeqNumber = preambleSeqNumber;
    }

    public String getAgendaSeqNumber() {
        return agendaSeqNumber;
    }

    public void setAgendaSeqNumber(String agendaSeqNumber) {
        this.agendaSeqNumber = agendaSeqNumber;
    }

    public String getResolutionSeqNumber() {
        return resolutionSeqNumber;
    }

    public void setResolutionSeqNumber(String resolutionSeqNumber) {
        this.resolutionSeqNumber = resolutionSeqNumber;
    }

}
