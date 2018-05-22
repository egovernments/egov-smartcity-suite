package org.egov.eventnotification.entity;

import org.apache.commons.lang3.StringUtils;

public enum DraftType {
    GENERAL("General"), BUSINESS("Business");

    private String name;

    private DraftType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return StringUtils.capitalize(name());
    }
}
