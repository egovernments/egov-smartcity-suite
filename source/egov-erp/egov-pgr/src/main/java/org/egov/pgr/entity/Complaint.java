package org.egov.pgr.entity;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pims.commons.Position;
import org.egov.search.domain.Searchable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.joda.time.DateTime;

@Entity
@Table(name = "pgr_complaint")
public class Complaint extends StateAware {

    private static final long serialVersionUID = 4020616083055647372L;

    @NotNull
    @Column(name="crn",unique=true)
    @Searchable(name = "crn")
    private String CRN = "";

    @ManyToOne
    @Valid
    @NotNull
    @JoinColumn(name="complaintType", nullable = false)
    @Searchable
    private ComplaintType complaintType;

    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name="complainant", nullable = false)
    @Searchable(name="citizen", group = Searchable.Group.COMMON)
    private Complainant complainant = new Complainant();    

    @ManyToOne(fetch = FetchType.LAZY)
    @Valid
    @JoinColumn(name = "assignee")
    private Position assignee;

    @ManyToOne(optional = true)
    @Valid
    @JoinColumn(name="location", nullable = true)
    @Searchable(name = "boundary", group = Searchable.Group.COMMON)
    private BoundaryImpl location;

    @ManyToOne
    @NotNull
    @JoinColumn(name="status")
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
    @JoinTable(name = "pgr_supportdocs", joinColumns = @JoinColumn(name = "filestoreid"), inverseJoinColumns = @JoinColumn(name = "complaintid"))
    private Set<FileStoreMapper> supportDocs = Collections.emptySet();

    private double lng;

    private double lat;
    
    @Column(name="escalation_date",nullable = false)
    private Date escalationDate;
    
    public DateTime getEscalationDate() {
        return  null == escalationDate ? null : new DateTime(escalationDate);
    }

    public void setEscalationDate(final DateTime escalationDate) {
        this.escalationDate = null == escalationDate ? null : escalationDate.toDate();
    }

    public String getCRN() {
        return CRN;
    }

    public void setCRN(final String cRN) {
        CRN = cRN;
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

    public BoundaryImpl getLocation() {
        return location;
    }

    public void setLocation(final BoundaryImpl location) {
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

    @Override
    public String getStateDetails() {
        // TODO Implement something
        return String.format("CRN : %s", this.getCRN());
    }

}
