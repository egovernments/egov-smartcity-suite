package org.egov.pgr.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.egov.infra.filestore.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "pgr_complaint", uniqueConstraints = @UniqueConstraint(columnNames = { "crn" }))
public class Complaint extends AbstractAuditable<UserImpl, Long> {

    private static final long serialVersionUID = 4020616083055647372L;

    // any future Receiving mode addition should be added at the end of this
    // enum
    // since we are asking hibernate use its ordinal to be persisted
    public enum ReceivingMode {
	WebSite, SMS, Call, Email, Paper, Mobile;
    }

    public enum Status {
	REGISTERED, FORWARDED, PROCESSING, COMPLETED, REJECTED, WITHDRAWN, REOPENED, CLOSED

    }

    private String CRN;

    @ManyToOne
    @Valid
    @NotNull
    private ComplaintType complaintType;

    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    private Complainant complainant;

    @ManyToOne
    @Valid
    private Position assignee;

    @ManyToOne(optional = true)
    @Valid
    @JoinColumn(nullable = true)
    private BoundaryImpl location;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    @Length(min = 10, max = 500)
    @SafeHtml
    private String details;

    @Max(200)
    @SafeHtml
    private String landmarkDetails;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private ReceivingMode receivingMode;

    @ManyToOne
    private ReceivingCenter receivingCenter;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "ref_id", referencedColumnName = "id")
    private Set<FileStoreMapper> supportDocs;

    private long lon;

    private long lat;
    
    public String getCRN() {
        return CRN;
    }

    public void setCRN(String cRN) {
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

    public Status getStatus() {
	return status;
    }

    public void setStatus(final Status status) {
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

    public void setSupportDocs(Set<FileStoreMapper> supportDocs) {
        this.supportDocs = supportDocs;
    }

    public BoundaryImpl getLocation() {
        return location;
    }

    public void setLocation(BoundaryImpl location) {
        this.location = location;
    }

    public String getLandmarkDetails() {
        return landmarkDetails;
    }

    public void setLandmarkDetails(String landmarkDetails) {
        this.landmarkDetails = landmarkDetails;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

}
