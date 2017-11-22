/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.council.entity;

import org.egov.commons.EgwStatus;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Unique(fields = "meetingNumber", enableDfltMsg = true)
@Table(name = "egcncl_meeting")
@SequenceGenerator(name = CouncilMeeting.SEQ_MEETING, sequenceName = CouncilMeeting.SEQ_MEETING, allocationSize = 1)
public class CouncilMeeting extends StateAware<Position> {

    public static final String SEQ_MEETING = "seq_egcncl_meeting";
    private static final long serialVersionUID = 5607959287745538396L;
    @Id
    @GeneratedValue(generator = SEQ_MEETING, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "committeeType")
    private CommitteeType committeeType;
    
    @ManyToOne
    @JoinColumn(name = "meetingType")
    private CouncilMeetingType meetingType;

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
    private List<MeetingMOM> meetingMOMs = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<MeetingAttendence> meetingAttendence = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<CouncilSmsDetails> smsDetails = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "filestore")
    private FileStoreMapper filestore;
    @Transient
    private List<MeetingAttendence> updateMeetingAttendance = new ArrayList<>();

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

    public FileStoreMapper getFilestore() {
        return filestore;
    }

    public void setFilestore(FileStoreMapper filestore) {
        this.filestore = filestore;
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

    public CouncilMeetingType getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(CouncilMeetingType meetingType) {
        this.meetingType = meetingType;
    }

}
