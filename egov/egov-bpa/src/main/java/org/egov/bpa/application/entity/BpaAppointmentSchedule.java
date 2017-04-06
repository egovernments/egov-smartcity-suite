package org.egov.bpa.application.entity;

import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_APPOINTMENT_SCHEDULE")
@SequenceGenerator(name = BpaAppointmentSchedule.SEQ_EGBPA_APPOINTMENT_SCHEDULE, sequenceName = BpaAppointmentSchedule.SEQ_EGBPA_APPOINTMENT_SCHEDULE, allocationSize = 1)
public class BpaAppointmentSchedule extends AbstractAuditable {

    private static final long serialVersionUID = -8837161988347102633L;
    public static final String SEQ_EGBPA_APPOINTMENT_SCHEDULE = "SEQ_EGBPA_APPOINTMENT_SCHEDULE";
    @Id
    @GeneratedValue(generator = SEQ_EGBPA_APPOINTMENT_SCHEDULE, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "application", nullable = false)
    private BpaApplication application;
    @Length(min = 1, max = 32)
    private String purpose;
    @Temporal(value = TemporalType.DATE)
    private Date appointmentDate;

    @Length(min = 1, max = 50)
    private String appointmentTime;
    @Length(min = 1, max = 100)
    private String appointmentLocation;

    @Length(min = 1, max = 256)
    private String remarks;
    @Length(min = 1, max = 256)
    private String postponementReason;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent")
    private BpaAppointmentSchedule parent;
    private boolean isPostponed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BpaApplication getApplication() {
        return application;
    }

    public void setApplication(BpaApplication application) {
        this.application = application;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public String getPostponementReason() {
        return postponementReason;
    }

    public void setPostponementReason(String postponementReason) {
        this.postponementReason = postponementReason;
    }

    public BpaAppointmentSchedule getParent() {
        return parent;
    }

    public void setParent(BpaAppointmentSchedule parent) {
        this.parent = parent;
    }

    public boolean isPostponed() {
        return isPostponed;
    }

    public void setPostponed(boolean isPostponed) {
        this.isPostponed = isPostponed;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
