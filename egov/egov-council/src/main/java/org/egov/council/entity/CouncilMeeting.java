package org.egov.council.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.egov.commons.EgwStatus;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Unique(id = "id", tableName = "egcncl_meeting", fields = "meetingNumber", columnName = "meetingNumber", enableDfltMsg = true)
@Table(name = "egcncl_meeting")
@SequenceGenerator(name = CouncilMeeting.SEQ_MEETING, sequenceName = CouncilMeeting.SEQ_MEETING, allocationSize = 1)
public class CouncilMeeting extends StateAware {

    private static final long serialVersionUID = 5607959287745538396L;

    public static final String SEQ_MEETING = "seq_egcncl_meeting";

    @Id
    @GeneratedValue(generator = SEQ_MEETING, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "committeeType")
    private CommitteeType committeeType;

    @Column(name = "meetingNumber")
    private String meetingNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "meetingDate")
    private Date meetingDate;

    @Column(name = "meetingTime")
    private String meetingTime;

    @Column(name = "meetingLocation")
    private String meetingLocation;

    @ManyToOne
    @JoinColumn(name = "status")
    private EgwStatus status;
    @Transient
    private Date fromDate;
    @Transient
    private Date toDate;

    @OrderBy("id")
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<MeetingMOM> meetingMOMs = new ArrayList<>(0);

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<MeetingAttendence> meetingAttendence = new ArrayList<>(0);

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<CouncilSmsDetails> smsDetails = new ArrayList<>(0);

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "filestore")
    private FileStoreMapper filestore;
    @Transient
    private List<MeetingAttendence> updateMeetingAttendance = new ArrayList<>(0);

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "egcncl_meeting_document", joinColumns = @JoinColumn(name = "meetingid"), inverseJoinColumns = @JoinColumn(name = "filestoreid"))
    private Set<FileStoreMapper> supportDocs = Collections.emptySet();

    private transient MultipartFile[] files;

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

    public String getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    @Override
    public String getStateDetails() {
        return String.format("Meeting Number %s ", meetingNumber);
    }

    public List<MeetingMOM> getMeetingMOMs() {
        return meetingMOMs;
    }

    public void setMeetingMOMs(List<MeetingMOM> meetingMOMs) {
        this.meetingMOMs = meetingMOMs;
    }

    public void addMeetingMoms(MeetingMOM meetingMom) {
        this.meetingMOMs.add(meetingMom);
    }

    public List<MeetingAttendence> getMeetingAttendence() {
        return meetingAttendence;
    }

    public void setMeetingAttendence(List<MeetingAttendence> meetingAttendence) {
        this.meetingAttendence = meetingAttendence;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public void setFilestore(FileStoreMapper filestore) {
        this.filestore = filestore;
    }

    public FileStoreMapper getFilestore() {
        return filestore;
    }

    public List<CouncilSmsDetails> getSmsDetails() {
        return smsDetails;
    }

    public void setSmsDetails(List<CouncilSmsDetails> smsDetails) {
        this.smsDetails = smsDetails;
    }

    public void addSmsDetails(CouncilSmsDetails councilSmsDetails) {
        this.smsDetails.add(councilSmsDetails);
    }

    public List<MeetingAttendence> getUpdateMeetingAttendance() {
        return updateMeetingAttendance;
    }

    public void setUpdateMeetingAttendance(List<MeetingAttendence> updateMeetingAttendance) {
        this.updateMeetingAttendance = updateMeetingAttendance;
    }

    public Set<FileStoreMapper> getSupportDocs() {
        return supportDocs;
    }

    public void setSupportDocs(Set<FileStoreMapper> supportDocs) {
        this.supportDocs = supportDocs;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

}
