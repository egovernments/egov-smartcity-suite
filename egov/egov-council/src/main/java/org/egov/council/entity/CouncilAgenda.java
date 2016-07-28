package org.egov.council.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.search.domain.Searchable;

@Entity
@Unique(id = "id", tableName = "egcncl_agenda", fields = "agendaNumber", columnName = "agendaNumber", enableDfltMsg = true)
@Table(name = "egcncl_agenda")
@Searchable
@SequenceGenerator(name = CouncilAgenda.SEQ_AGENDA, sequenceName = CouncilAgenda.SEQ_AGENDA, allocationSize = 1)
public class CouncilAgenda extends StateAware {

    private static final long serialVersionUID = 6941145759682765506L;

    public static final String SEQ_AGENDA = "seq_egcncl_agenda";

    @Id
    @GeneratedValue(generator = SEQ_AGENDA, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "committeeType", nullable = false)
    private CommitteeType committeeType;

    @NotNull
    @Column(name = "agendaNumber")
    private String agendaNumber;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus status;

    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CouncilAgendaDetails> agendaDetails = new ArrayList<CouncilAgendaDetails>(0);

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommitteeType getCommitteeType() {
        return committeeType;
    }

    public void setCommitteeType(CommitteeType committeeType) {
        this.committeeType = committeeType;
    }

    public String getAgendaNumber() {
        return agendaNumber;
    }

    public void setAgendaNumber(String agendaNumber) {
        this.agendaNumber = agendaNumber;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

    public List<CouncilAgendaDetails> getAgendaDetails() {
        return agendaDetails;
    }

    public void setAgendaDetails(List<CouncilAgendaDetails> agendaDetails) {
        this.agendaDetails = agendaDetails;
    }

    @Override
    public String getStateDetails() {
        return String.format("Agenda Number %s ", agendaNumber);

    }

}
