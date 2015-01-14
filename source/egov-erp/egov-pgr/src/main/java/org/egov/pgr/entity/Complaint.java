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
import javax.validation.constraints.NotNull;

import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.user.User;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "pgr_complaint",uniqueConstraints=@UniqueConstraint(columnNames = { "complaintID" }))
public class Complaint extends AbstractAuditable<User, Long> {
	private static final long serialVersionUID = 1L;
	
	//any future Receiving mode addition should be added at the end of this enum 
	//since we are asking hibernate use its ordinal to be persisted
	public enum ReceivingMode {
		Online, SMS, Call, Email, Paper;		
	}
	
	public enum Status {
		REGISTERED,FORWARDED,PROCESSING,COMPLETED,REJECTED,WITHDRAWN,REOPENED,CLOSE
		
	}
	
	private String complaintID;
	
	@ManyToOne
	@Valid
	@NotNull
	private ComplaintType complaintType;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@Valid
	@NotNull
	private Complainant complainant;
	
	@ManyToOne
	@Valid
	private User assignee;
	
	@ManyToOne(optional=true)
	@Valid
	@JoinColumn(nullable=true)
	private Boundary boundary;
	
	@NotNull(message="{error-not-empty}")
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@NotNull(message="{error-not-empty}")
	@Length(min=10,max=500,message="{error-min-max-length-exceed}")
	@SafeHtml(message = "{error-input-unsafe}")
	private String details;
	
	@Length(max=200,message="{error-max-length-exceed}")
	@SafeHtml(message = "{error-input-unsafe}")
	private String locationDetails;
	
	@Enumerated(EnumType.ORDINAL)
	@NotNull(message="{error-not-empty}")
	private ReceivingMode receivingMode;
	
	@ManyToOne
	private ReceivingCenter receivingCenter;
	
	@OneToMany(fetch=FetchType.LAZY,orphanRemoval=true,cascade=CascadeType.ALL)
	@JoinColumn(name="ref_id", referencedColumnName="complaintID")
	private Set<FileAttachment> supportFiles;  
	
	private Float lon;
	
	private Float lat;
	
	
	public ComplaintType getComplaintType() {
		return this.complaintType;
	}

	public void setComplaintType(final ComplaintType complaintType) {
		this.complaintType = complaintType;
	}

	public Complainant getComplainant() {
		return this.complainant;
	}

	public void setComplainant(final Complainant complainant) {
		this.complainant = complainant;
	}

	public User getAssignee() {
		return this.assignee;
	}

	public void setAssignee(final User assignee) {
		this.assignee = assignee;
	}

	public Boundary getBoundary() {
		return boundary;
	}

	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(final String details) {
		this.details = details;
	}

	public String getLocationDetails() {
		return this.locationDetails;
	}

	public void setLocationDetails(final String locationDetails) {
		this.locationDetails = locationDetails;
	}

	public Set<FileAttachment> getSupportFiles() {
		return supportFiles;
	}

	public void setSupportFiles(Set<FileAttachment> supportFiles) {
		this.supportFiles = supportFiles;
	}

	public String getComplaintID() {
		return complaintID;
	}

	public void setComplaintID(String complaintID) {
		this.complaintID = complaintID;
	}

	public ReceivingMode getReceivingMode() {
		return receivingMode;
	}

	public void setReceivingMode(ReceivingMode receivingMode) {
		this.receivingMode = receivingMode;
	}

	public ReceivingCenter getReceivingCenter() {
		return receivingCenter;
	}

	public void setReceivingCenter(ReceivingCenter receivingCenter) {
		this.receivingCenter = receivingCenter;
	}

	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

}
