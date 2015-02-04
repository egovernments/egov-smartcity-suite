package org.egov.search;

public enum Index {
    PGR;

    public String indexName() {
        return this.name().toLowerCase();
    }
}
