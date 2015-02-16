package org.egov.config.search;

public enum IndexType {
    COMPLAINT_TYPE, COMPLAINT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
