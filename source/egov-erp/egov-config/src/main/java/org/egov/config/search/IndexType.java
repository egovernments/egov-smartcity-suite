package org.egov.config.search;

public enum IndexType {
    COMPLAINT_TYPE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
