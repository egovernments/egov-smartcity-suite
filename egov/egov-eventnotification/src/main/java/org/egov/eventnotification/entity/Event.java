package org.egov.eventnotification.entity;

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

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "eg_event")
@SequenceGenerator(name = Event.SEQ_EG_EVENT, sequenceName = Event.SEQ_EG_EVENT, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class Event extends AbstractAuditable {

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

    @Column(name = "start_date")
    private Long startDate;

    @NotNull
    @Length(max = 20)
    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_date")
    private Long endDate;

    @NotNull
    @Length(max = 20)
    @Column(name = "end_time")
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

    @NotNull
    private Boolean ispaid;

    private Double cost;

    @NotNull
    @Length(max = 50)
    @Column(name = "event_type")
    private String eventType;

    private Long version;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "filestore")
    private FileStoreMapper filestore;

    @NotNull
    @Length(max = 200)
    private String message;

    @NotNull
    @Length(max = 200)
    private String url;

    @NotNull
    @Length(max = 20)
    private String status;

    @Transient
    private EventDetails eventDetails;

    public EventDetails getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(EventDetails eventDetails) {
        this.eventDetails = eventDetails;
    }

    public FileStoreMapper getFilestore() {
        return filestore;
    }

    public void setFilestore(FileStoreMapper filestore) {
        this.filestore = filestore;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
