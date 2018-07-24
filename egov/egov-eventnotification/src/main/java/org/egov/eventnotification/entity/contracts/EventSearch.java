package org.egov.eventnotification.entity.contracts;

public class EventSearch {

    private String eventType;
    private String name;
    private String eventHost;
    private String eventDateType;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventHost() {
        return eventHost;
    }

    public void setEventHost(String eventHost) {
        this.eventHost = eventHost;
    }

    public String getEventDateType() {
        return eventDateType;
    }

    public void setEventDateType(String eventDateType) {
        this.eventDateType = eventDateType;
    }
}
