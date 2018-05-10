package org.egov.eventnotification.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.User;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "egevntnotification_schedule")
@SequenceGenerator(name = NotificationSchedule.SEQ_EGEVENTNOTIFICATION_SCHEDULE, sequenceName = NotificationSchedule.SEQ_EGEVENTNOTIFICATION_SCHEDULE, allocationSize = 1)
public class NotificationSchedule {

    public static final String SEQ_EGEVENTNOTIFICATION_SCHEDULE = "seq_egevntnotification_schedule";
    
    @Id
    @GeneratedValue(generator = SEQ_EGEVENTNOTIFICATION_SCHEDULE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String templatename;

    @NotNull
    @Length(max = 200)
    @Column(name = "notification_type")
    private String notificationType;

    private String status;

    @Column(name = "start_date")
    private Long startDate;
    
    @NotNull
    @Length(max = 20)
    @Column(name = "start_time")
    private String startTime;

    private String repeat;

    @NotNull
    @Length(max = 500)
    @Column(name = "message_template")
    private String messageTemplate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy")
    @CreatedBy
    private User createdBy;

    @Column(name = "createddate")
    private Long createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedby")
    @LastModifiedBy
    private User updatedby;

    @Column(name = "updateddate")
    private Long updatedDate;
    
    @Transient
    private EventDetails eventDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplatename() {
        return templatename;
    }

    public void setTemplatename(String templatename) {
        this.templatename = templatename;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public User getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(User updatedby) {
        this.updatedby = updatedby;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public EventDetails getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(EventDetails eventDetails) {
        this.eventDetails = eventDetails;
    }
    
    
}
