package org.egov.council.entity;

import org.egov.commons.EgwStatus;

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
import java.io.Serializable;

@Entity
@Table(name = "egcncl_meeting_mom")
@SequenceGenerator(name = MeetingMOM.SEQ_MEETINGMOM, sequenceName = MeetingMOM.SEQ_MEETINGMOM, allocationSize = 1)
public class MeetingMOM implements Serializable {

    public static final String SEQ_MEETINGMOM = "seq_egcncl_meetingMom";

    @Id
    @GeneratedValue(generator = SEQ_MEETINGMOM, strategy = GenerationType.SEQUENCE)
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meeting", nullable = false)
    private CouncilMeeting meeting;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "agenda")
    private CouncilAgenda agenda;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "preamble", nullable = false)
    private CouncilPreamble preamble;

    @Column(name = "resolutionDetail")
    private String resolutionDetail;

    @ManyToOne
    @JoinColumn(name = "resolutionStatus")
    private EgwStatus resolutionStatus;

    @Column(name = "itemnumber")
    private String itemNumber;
    @Column(name = "resolutionNumber")
    private String resolutionNumber;

    public CouncilMeeting getMeeting() {
        return meeting;
    }

    public void setMeeting(CouncilMeeting meeting) {
        this.meeting = meeting;
    }

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

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

}
