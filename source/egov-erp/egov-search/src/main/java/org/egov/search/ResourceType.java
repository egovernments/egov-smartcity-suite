package org.egov.search;

public enum ResourceType {
    COMPLAINT_TYPE,;

    public String indexType() {
        return this.name().toLowerCase();
    }
}
