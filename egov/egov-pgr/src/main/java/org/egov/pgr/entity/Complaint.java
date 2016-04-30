/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.pgr.entity;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.pgr.entity.enums.CitizenFeedback;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pims.commons.Position;
import org.egov.search.domain.Searchable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "egpgr_complaint")
@SequenceGenerator(name = Complaint.SEQ_COMPLAINT, sequenceName = Complaint.SEQ_COMPLAINT, allocationSize = 1)
@Unique(id = "id", tableName = "egpgr_complaint", columnName = "crn", fields = "crn", enableDfltMsg = true)
public class Complaint extends StateAware {

    private static final long serialVersionUID = 4020616083055647372L;
    public static final String SEQ_COMPLAINT = "SEQ_EGPGR_COMPLAINT";

    @Id
    @GeneratedValue(generator = SEQ_COMPLAINT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "crn", unique = true)
    @Searchable(name = "crn", group = Searchable.Group.CLAUSES)
    @Length(max = 32)
    @SafeHtml
    private String crn = "";

    @ManyToOne
    @NotNull
    @JoinColumn(name = "complaintType", nullable = false)
    @Searchable
    private ComplaintType complaintType;

    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "complainant", nullable = false)
    @Searchable(name = "citizen", group = Searchable.Group.COMMON)
    private Complainant complainant = new Complainant();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee")
    @Searchable(name = "owner")
    private Position assignee;

    @ManyToOne(optional = true)
    @JoinColumn(name = "location", nullable = true)
    @Searchable(name = "boundary", group = Searchable.Group.COMMON)
    private Boundary location;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "status")
    @Searchable(group = Searchable.Group.CLAUSES)
    private ComplaintStatus status = new ComplaintStatus();

    @Length(min = 10, max = 500)
    @SafeHtml
    @Searchable
    private String details;

    @Length(max = 200)
    @SafeHtml
    @Searchable
    private String landmarkDetails;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    @Searchable(group = Searchable.Group.CLAUSES)
    private ReceivingMode receivingMode = ReceivingMode.WEBSITE;

    @ManyToOne
    @JoinColumn(name = "receivingCenter", nullable = true)
    private ReceivingCenter receivingCenter;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name = "egpgr_supportdocs", joinColumns = @JoinColumn(name = "complaintid"), inverseJoinColumns = @JoinColumn(name = "filestoreid"))
    private Set<FileStoreMapper> supportDocs = Collections.emptySet();

    private double lng;

    private double lat;

    @Column(name = "escalation_date", nullable = false)
    private Date escalationDate;

    @ManyToOne
    @JoinColumn(name = "department", nullable = false)
    @Searchable(name = "department", group = Searchable.Group.CLAUSES)
    private Department department;

    @Enumerated(EnumType.ORDINAL)
    private CitizenFeedback citizenFeedback;

    @ManyToOne
    @JoinColumn(name = "childLocation", nullable = true)
    @Searchable(name = "locationBoundary", group = Searchable.Group.COMMON)
    private Boundary childLocation;

    @Transient
    private String latlngAddress;

    /*
     * For indexing the below fields are kept. These will not be added to the database. This will be available only in index.
     */
    @Transient
    private Long crossHierarchyId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(final String crn) {
        this.crn = crn;
    }

    @Override
    public String myLinkId() {
        return crn;

    }

    public ComplaintType getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(final ComplaintType complaintType) {
        this.complaintType = complaintType;
    }

    public Complainant getComplainant() {
        return complainant;
    }

    public void setComplainant(final Complainant complainant) {
        this.complainant = complainant;
    }

    public Position getAssignee() {
        return assignee;
    }

    public void setAssignee(final Position assignee) {
        this.assignee = assignee;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(final ComplaintStatus status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public ReceivingMode getReceivingMode() {
        return receivingMode;
    }

    public void setReceivingMode(final ReceivingMode receivingMode) {
        this.receivingMode = receivingMode;
    }

    public ReceivingCenter getReceivingCenter() {
        return receivingCenter;
    }

    public void setReceivingCenter(final ReceivingCenter receivingCenter) {
        this.receivingCenter = receivingCenter;
    }

    public Set<FileStoreMapper> getSupportDocs() {
        return supportDocs;
    }

    public void setSupportDocs(final Set<FileStoreMapper> supportDocs) {
        this.supportDocs = supportDocs;
    }

    public Boundary getLocation() {
        return location;
    }

    public void setLocation(final Boundary location) {
        this.location = location;
    }

    public String getLandmarkDetails() {
        return landmarkDetails;
    }

    public void setLandmarkDetails(final String landmarkDetails) {
        this.landmarkDetails = landmarkDetails;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(final double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(final double lng) {
        this.lng = lng;
    }

    public DateTime getEscalationDate() {
        return null == escalationDate ? null : new DateTime(escalationDate);
    }

    public void setEscalationDate(final DateTime escalationDate) {
        this.escalationDate = null == escalationDate ? null : escalationDate.toDate();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    @Override
    public String getStateDetails() {
        final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy hh:mm a");
        return String.format("Complaint Number %s for %s filed on %s. Date of resolution %s", getCrn(),
                getComplaintType().getName(), formatter.print(new DateTime(getCreatedDate())),
                formatter.print(getEscalationDate()));
    }

    public CitizenFeedback getCitizenFeedback() {
        return citizenFeedback;
    }

    public void setCitizenFeedback(final CitizenFeedback citizenFeedback) {
        this.citizenFeedback = citizenFeedback;
    }

    public Boundary getChildLocation() {
        return childLocation;
    }

    public void setChildLocation(final Boundary childLocation) {
        this.childLocation = childLocation;
    }

    public Long getCrossHierarchyId() {
        return crossHierarchyId;
    }

    public void setCrossHierarchyId(final Long crossHierarchyId) {
        this.crossHierarchyId = crossHierarchyId;
    }

    public String getLatlngAddress() {
        return latlngAddress;
    }

    public void setLatlngAddress(final String latlngAddress) {
        this.latlngAddress = latlngAddress;
    }

}