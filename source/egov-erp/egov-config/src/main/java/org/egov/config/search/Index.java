package org.egov.config.search;

public enum Index {
    PGR;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
