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
import org.egov.council.entity.enums.PreambleType;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "egcncl_preamble")
@SequenceGenerator(name = CouncilPreamble.SEQ_COUNCILPREAMBLE, sequenceName = CouncilPreamble.SEQ_COUNCILPREAMBLE, allocationSize = 1)
public class CouncilPreamble extends StateAware<Position> {

    public static final String SEQ_COUNCILPREAMBLE = "seq_egcncl_preamble";
    private static final long serialVersionUID = -2561739732877438517L;
    @Id
    @GeneratedValue(generator = SEQ_COUNCILPREAMBLE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "department", nullable = false)
    private Department department;

    @Column(name = "preambleNumber", unique = true)
    @Length(max = 25)
    private String preambleNumber;

    private BigDecimal sanctionAmount;

    @NotNull
    @Length(max = 10000)
    @JoinColumn(name = "gistOfPreamble")
    private String gistOfPreamble;

    @Transient
    private transient MultipartFile attachments;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "filestoreid")
    private FileStoreMapper filestoreid;

    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    private EgwStatus status;

    @ManyToOne
    @JoinColumn(name = "ImplStatus")
    private EgwStatus implementationStatus;

    @OneToMany(mappedBy = "preamble", cascade = CascadeType.ALL)
    private List<MeetingMOM> meetingMOMs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "egcncl_preamble_wards", joinColumns = @JoinColumn(name = "preamble"), inverseJoinColumns = @JoinColumn(name = "ward"))
    private List<Boundary> wards = new ArrayList<>();

    @Enumerated(EnumType.ORDINAL)
    private PreambleType type;

    @Transient
    private Date fromDate;

    @Transient
    private Date toDate;

    @Transient
    private Long approvalDepartment;

    @Transient
    private String approvalComent;

    @Transient
    private Long approvalPosition;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getPreambleNumber() {
        return preambleNumber;
    }

    public void setPreambleNumber(String preambleNumber) {
        this.preambleNumber = preambleNumber;
    }

    public BigDecimal getSanctionAmount() {
        return sanctionAmount;
    }

    public void setSanctionAmount(BigDecimal sanctionAmount) {
        this.sanctionAmount = sanctionAmount;
    }

    public String getGistOfPreamble() {
        return gistOfPreamble;
    }

    public void setGistOfPreamble(String gistOfPreamble) {
        this.gistOfPreamble = gistOfPreamble;
    }

    public MultipartFile getAttachments() {
        return attachments;
    }

    public void setAttachments(MultipartFile attachments) {
        this.attachments = attachments;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

    public PreambleType getType() {
        return type;
    }

    public void setType(PreambleType type) {
        this.type = type;
    }

    public FileStoreMapper getFilestoreid() {
        return filestoreid;
    }

    public void setFilestoreid(FileStoreMapper filestoreid) {
        this.filestoreid = filestoreid;
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

    @Override
    public String getStateDetails() {
        return String.format("Preamble Number %s ", preambleNumber);
    }

    public Long getApprovalDepartment() {
        return approvalDepartment;
    }

    public void setApprovalDepartment(Long approvalDepartment) {
        this.approvalDepartment = approvalDepartment;
    }

    public String getApprovalComent() {
        return approvalComent;
    }

    public void setApprovalComent(String approvalComent) {
        this.approvalComent = approvalComent;
    }

    public Long getApprovalPosition() {
        return approvalPosition;
    }

    public void setApprovalPosition(Long approvalPosition) {
        this.approvalPosition = approvalPosition;
    }

    public List<MeetingMOM> getMeetingMOMs() {
        return meetingMOMs;
    }

    public void setMeetingMOMs(List<MeetingMOM> meetingMOMs) {
        this.meetingMOMs = meetingMOMs;
    }

    public List<Boundary> getWards() {
        return wards;
    }

    public void setWards(List<Boundary> wards) {
        this.wards = wards;
    }

    public EgwStatus getImplementationStatus() {
        return implementationStatus;
    }

    public void setImplementationStatus(EgwStatus implementationStatus) {
        this.implementationStatus = implementationStatus;
    }
}
