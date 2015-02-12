package org.egov.pgr.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "pgr_complaint")
@SuppressWarnings("unchecked")
public class Complaint extends AbstractAuditable<UserImpl, Long> {

    private static final long serialVersionUID = 4020616083055647372L;

    // any future Receiving mode addition should be added at the end of this
    // enum
    // since we are asking hibernate use its ordinal to be persisted
    public enum ReceivingMode {
        WebSite, SMS, Call, Email, Paper, Mobile;
    }

 
    @NotNull
    @NotEmpty
    @Column(name="crn",unique=true)
    private String CRN = "NONE";

    @ManyToOne
    @Valid
    @NotNull
    @JoinColumn(name="complaintType", nullable = false)
    private ComplaintType complaintType;

    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name="complainant", nullable = false)
    private Complainant complainant = new Complainant();    

    @ManyToOne(fetch=FetchType.LAZY)
    @Valid
    @JoinColumn(name="assignee")
    private Position assignee;

    @ManyToOne(optional = true)
    @Valid
    @JoinColumn(name="location", nullable = true)
    private BoundaryImpl location;

    @ManyToOne     @NotNull
    @JoinColumn(name="status")
    private ComplaintStatus status ;

  
    @Length(min = 10, max = 500)
    @SafeHtml
    private String details;


    @Length(max = 200)
    @SafeHtml
    private String landmarkDetails;

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private ReceivingMode receivingMode = ReceivingMode.WebSite;

    @ManyToOne
    @JoinColumn(name="receivingCenter", nullable = true)
    private ReceivingCenter receivingCenter;

  /*  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinTable(name="pgr_supportdocs",
            joinColumns = @JoinColumn(name = "filestoreid"),
            inverseJoinColumns = @JoinColumn(name = "complaintid")
    )
    private Set<FileStoreMapper> supportDocs = Collections.EMPTY_SET;
*/
    private double lng;

    private double lat;

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

	public void setStatus(ComplaintStatus status) {
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

    /*public Set<FileStoreMapper> getSupportDocs() {
        return supportDocs;
    }

    public void setSupportDocs(final Set<FileStoreMapper> supportDocs) {
        this.supportDocs = supportDocs;
    }
*/
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

}
