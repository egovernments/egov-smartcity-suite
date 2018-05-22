package org.egov.eventnotification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egevntnotification_schedule")
@SequenceGenerator(name = NotificationSchedule.SEQ_EGEVENTNOTIFICATION_SCHEDULE, sequenceName = NotificationSchedule.SEQ_EGEVENTNOTIFICATION_SCHEDULE, allocationSize = 1)
public class NotificationSchedule extends AbstractAuditable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String SEQ_EGEVENTNOTIFICATION_SCHEDULE = "seq_egevntnotification_schedule";

    @Id
    @GeneratedValue(generator = SEQ_EGEVENTNOTIFICATION_SCHEDULE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    private String templatename;

    @NotNull
    @Length(max = 200)
    @SafeHtml
    @Column(name = "notification_type")
    private String notificationType;

    @SafeHtml
    private String status;

    @Column(name = "start_date")
    private Long startDate;

    @NotNull
    @Length(max = 20)
    @Column(name = "start_time")
    @SafeHtml
    private String startTime;

    @SafeHtml
    private String repeat;

    @NotNull
    @Length(max = 500)
    @SafeHtml
    @Column(name = "message_template")
    private String messageTemplate;

    @Transient
    private EventDetails eventDetails;

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    public EventDetails getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(EventDetails eventDetails) {
        this.eventDetails = eventDetails;
    }

}
