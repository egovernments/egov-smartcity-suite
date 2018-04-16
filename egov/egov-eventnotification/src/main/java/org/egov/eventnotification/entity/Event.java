package org.egov.eventnotification.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "eg_event")
@SequenceGenerator(name = Event.SEQ_EG_EVENT, sequenceName = Event.SEQ_EG_EVENT, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
    @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class Event extends AbstractAuditable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SEQ_EG_EVENT = "SEQ_EG_EVENT";
	
	@Id
    @GeneratedValue(generator = SEQ_EG_EVENT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Length(max = 100)
    private String name;
    
    @NotNull
    @Length(max = 200)
    private String description;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="start_date")
    private Date startDate;
    
    @NotNull
    @Length(max = 20)
    @Column(name="start_time")
    private String startTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="end_date")
    private Date endDate;
    
    @NotNull
    @Length(max = 20)
    @Column(name="end_time")
    private String endTime;
    
    @NotNull
    @Length(max = 100)
    private String eventhost;
    
    @NotNull
    @Length(max = 100)
    private String eventlocation;
    
    @NotNull
    @Length(max = 200)
    private String address;
    
    @Length(max = 50)
    private String wallpaper;
    
    @NotNull
    private Boolean ispaid;
    
    private Double cost;
    
    @Transient
    private String startHH;
    
    @Transient
    private String startMM;
    
    @Transient
    private String endHH;
    
    @Transient
    private String endMM;
    
    @NotNull
    @Length(max = 50)
    @Column(name="event_type")
    private String eventType;
    
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEventhost() {
		return eventhost;
	}

	public void setEventhost(String eventhost) {
		this.eventhost = eventhost;
	}

	public String getEventlocation() {
		return eventlocation;
	}

	public void setEventlocation(String eventlocation) {
		this.eventlocation = eventlocation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWallpaper() {
		return wallpaper;
	}

	public void setWallpaper(String wallpaper) {
		this.wallpaper = wallpaper;
	}

	public Boolean getIspaid() {
		return ispaid;
	}

	public void setIspaid(Boolean ispaid) {
		this.ispaid = ispaid;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getStartHH() {
		return startHH;
	}

	public void setStartHH(String startHH) {
		this.startHH = startHH;
	}

	public String getStartMM() {
		return startMM;
	}

	public void setStartMM(String startMM) {
		this.startMM = startMM;
	}

	public String getEndHH() {
		return endHH;
	}

	public void setEndHH(String endHH) {
		this.endHH = endHH;
	}

	public String getEndMM() {
		return endMM;
	}

	public void setEndMM(String endMM) {
		this.endMM = endMM;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	
}
