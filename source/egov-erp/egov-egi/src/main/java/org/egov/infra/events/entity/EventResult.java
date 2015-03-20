package org.egov.infra.events.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractPersistable;

@Entity
@Table(name = "eg_event_result")
public class EventResult extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 5662966690272607421L;

    @Column(name = "module")
    private String module;

    @Column(name = "event_code")
    private String eventCode;

    @Column(name = "date_raised")
    private Date dateRaised;

    @Column(name = "result")
    private String result;

    @Column(name = "timeofprocessing")
    private Date timeOfProcessing;

    @Column(name = "details")
    private String details;

    public String getModule() {
        return module;
    }

    public void setModule(final String module) {
        this.module = module;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(final String eventCode) {
        this.eventCode = eventCode;
    }

    public Date getDateRaised() {
        return dateRaised;
    }

    public void setDateRaised(final Date dateRaised) {
        this.dateRaised = dateRaised;
    }

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public Date getTimeOfProcessing() {
        return timeOfProcessing;
    }

    public void setTimeOfProcessing(final Date timeOfProcessing) {
        this.timeOfProcessing = timeOfProcessing;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("EventResult [id=").append(getId()).append(", module=").append(module).append(", eventCode=")
        .append(eventCode).append(", dateRaised=").append(dateRaised).append(", result=").append(result)
        .append(", timeOfProcessing=").append(timeOfProcessing).append(", details=").append(details)
        .append("]");
        return builder.toString();
    }

}