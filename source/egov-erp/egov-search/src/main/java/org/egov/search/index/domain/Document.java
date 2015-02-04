package org.egov.search.index.domain;

import org.egov.search.util.Serializer;

public class Document {
    private String correlationId;
    private String resource;

    Document() {
    }

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

    public static Document fromJson(String json) {
        return Serializer.fromJson(json, Document.class);
    }

    public String toJson() {
        return Serializer.toJson(this);
    }
}
