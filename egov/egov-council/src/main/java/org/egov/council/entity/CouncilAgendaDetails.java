package org.egov.council.entity;

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

import org.egov.search.domain.Searchable;

@Entity
@Table(name = "egcncl_agenda_details")
@Searchable
@SequenceGenerator(name = CouncilAgendaDetails.SEQ_AGENDADETAILS, sequenceName = CouncilAgendaDetails.SEQ_AGENDADETAILS)
public class CouncilAgendaDetails  {

    public static final String SEQ_AGENDADETAILS = "seq_egcncl_agenda_details";

    @Id
    @GeneratedValue(generator = SEQ_AGENDADETAILS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "agenda", nullable = false)
    private CouncilAgenda agenda;

    @Column(name = "itemnumber")
    private String itemNumber;

    @Column(name = "order")
    private Long order;

    @ManyToOne
    @JoinColumn(name = "preamble", nullable = false)
    private CouncilPreamble preamble;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CouncilAgenda getAgenda() {
        return agenda;
    }

    public void setAgenda(CouncilAgenda agenda) {
        this.agenda = agenda;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public CouncilPreamble getPreamble() {
        return preamble;
    }

    public void setPreamble(CouncilPreamble preamble) {
        this.preamble = preamble;
    }

}
