package org.egov.config;

import java.util.Properties;

public class ApplicationConfig {

    private Properties properties;

    public ApplicationConfig(Properties properties) {
        this.properties = properties;
    }

    public String[] searchHosts() {
        return properties.getProperty("search.hosts").split(",");
    }

    public int searchPort() {
        return Integer.parseInt(properties.getProperty("search.port"));
    }

    public String searchClusterName() {
        return properties.getProperty("search.clusterName");
    }

    public int searchShardsFor(String indexName) {
        return Integer.parseInt(properties.getProperty(String.format("search.%s.shards", indexName)));
    }

    public int searchReplicasFor(String indexName) {
        return Integer.parseInt(properties.getProperty(String.format("search.%s.replicas", indexName)));
    }
}
