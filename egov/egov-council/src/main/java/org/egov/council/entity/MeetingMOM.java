package org.egov.council.entity;

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

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.search.domain.Searchable;

@Entity
@Unique(id = "id", tableName = "egcncl_meeting_mom", fields = "", columnName = "", enableDfltMsg = true)
@Table(name = "egcncl_meeting_mom")
@Searchable
@SequenceGenerator(name = MeetingMOM.SEQ_MEETINGMOM, sequenceName = MeetingMOM.SEQ_MEETINGMOM, allocationSize = 1)
public class MeetingMOM {

    public static final String SEQ_MEETINGMOM = "seq_egcncl_meetingMom";

    @Id
    @GeneratedValue(generator = SEQ_MEETINGMOM, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "agenda")
    private Agenda agenda;

    @NotNull
    @Column(name = "preamble")
    private CouncilPreamble preamble;

    @NotNull
    @Column(name = "resolutionDetail")
    private String resolutionDetail;

    @ManyToOne
    @JoinColumn(name = "resolutionStatus")
    private EgwStatus resolutionStatus;
    

    @Column(name = "resolutionNumber")
    private String resolutionNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public CouncilPreamble getPreamble() {
        return preamble;
    }

    public void setPreamble(CouncilPreamble preamble) {
        this.preamble = preamble;
    }

    public String getResolutionDetail() {
        return resolutionDetail;
    }

    public void setResolutionDetail(String resolutionDetail) {
        this.resolutionDetail = resolutionDetail;
    }

    public EgwStatus getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(EgwStatus resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    public String getResolutionNumber() {
        return resolutionNumber;
    }

    public void setResolutionNumber(String resolutionNumber) {
        this.resolutionNumber = resolutionNumber;
    }

}
