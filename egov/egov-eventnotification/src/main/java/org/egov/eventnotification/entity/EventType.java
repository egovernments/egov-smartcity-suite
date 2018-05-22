package org.egov.eventnotification.entity;

public enum EventType {
    BUSINESS("Business"), EXHIBITION("Exhibition"), CULTURAL("Cultural"), DRAMA("Drama");

    private String name;

    private EventType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
