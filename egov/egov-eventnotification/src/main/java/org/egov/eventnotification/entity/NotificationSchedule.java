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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egevntnotification_schedule")
@SequenceGenerator(name = NotificationSchedule.SEQ_EGEVENTNOTIFICATION_SCHEDULE, sequenceName = NotificationSchedule.SEQ_EGEVENTNOTIFICATION_SCHEDULE, allocationSize = 1)
public class NotificationSchedule extends AbstractAuditable {

    /**
     *
     */
    private static final long serialVersionUID = 3093610095876757549L;

    public static final String SEQ_EGEVENTNOTIFICATION_SCHEDULE = "seq_egevntnotification_schedule";

    @Id
    @GeneratedValue(generator = SEQ_EGEVENTNOTIFICATION_SCHEDULE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    private String templateName;

    @NotNull
    @Length(max = 200)
    @SafeHtml
    @Column(name = "notification_type")
    private String notificationType;

    @SafeHtml
    private String status;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
