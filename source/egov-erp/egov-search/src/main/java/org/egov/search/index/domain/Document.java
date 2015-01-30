package org.egov.search.index.domain;

public class Document {
    private String correlationId;
    private String resource;


    public Document(String correlationId, String jsonResource) {
        this.correlationId = correlationId;
        this.resource = jsonResource;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getResource() {
        return resource;
    }
}
