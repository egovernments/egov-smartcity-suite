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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name = "egcncl_meeting_attendence")
@SequenceGenerator(name = MeetingAttendence.SEQ_MEETINGATTENDENCE, sequenceName = MeetingAttendence.SEQ_MEETINGATTENDENCE, allocationSize = 1)
public class MeetingAttendence extends AbstractAuditable {

    private static final long serialVersionUID = 1739845457408050470L;

    public static final String SEQ_MEETINGATTENDENCE = "seq_egcncl_meetingAttendence";

    @Id
    @GeneratedValue(generator = SEQ_MEETINGATTENDENCE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meeting", nullable = false)
    private CouncilMeeting meeting;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "committeemember", nullable = false)
    private CouncilMember councilMember;

    @NotNull
    @Column(name = "attendedMeeting")
    private Boolean attendedMeeting;
    
    @Transient
    private Boolean checked;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CouncilMeeting getMeeting() {
        return meeting;
    }

    public void setMeeting(CouncilMeeting meeting) {
        this.meeting = meeting;
    }

    public CouncilMember getCouncilMember() {
        return councilMember;
    }

    public void setCouncilMember(CouncilMember councilMember) {
        this.councilMember = councilMember;
    }

    public Boolean getAttendedMeeting() {
        return attendedMeeting;
    }

    public void setAttendedMeeting(Boolean attendedMeeting) {
        this.attendedMeeting = attendedMeeting;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
    
}
