package org.egov.eventnotification.entity;

public enum EventStatus {
    ACTIVE("Active"), INACTIVE("Inactive");

    private String name;

    private EventStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
