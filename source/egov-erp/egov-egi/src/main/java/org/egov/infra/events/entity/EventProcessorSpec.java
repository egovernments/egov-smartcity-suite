package org.egov.infra.events.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractPersistable;

@Entity
@Table(name = "eg_event_processor_spec")
@NamedQuery(name = "event_specByModuleAndCode", query = "select EP from EventProcessorSpec EP where EP.module=:module and EP.eventCode=:eventCode")
public class EventProcessorSpec extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 5661966690272607421L;

    @Column(name = "module")
    private String module;

    @Column(name = "event_code")
    private String eventCode;

    @Column(name = "response_template")
    private String responseTemplate;

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

    public String getResponseTemplate() {
        return responseTemplate;
    }

    public void setResponseTemplate(final String responseTemplate) {
        this.responseTemplate = responseTemplate;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("EventProcessorSpec [id=").append(getId()).append(", module=").append(module)
        .append(", eventCode=").append(eventCode).append(", responseTemplate=").append(responseTemplate)
        .append("]");
        return builder.toString();
    }

}
