package org.egov.search;

import org.egov.config.ApplicationConfig;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;

@Configuration
public class SearchBeans {

    @Bean
    @Autowired
    public Client transportClient(ApplicationConfig applicationConfig) {
        ImmutableSettings.Builder settingsBuilder = ImmutableSettings.builder();
        settingsBuilder.put("cluster.name", applicationConfig.searchClusterName());

        TransportClient client = new TransportClient(settingsBuilder);
        asList(applicationConfig.searchHosts()).stream().forEach(host -> addTransportClient(client, host, applicationConfig.searchPort()));

        return client;
    }

    private TransportClient addTransportClient(Client client, String host, int port) {
        return ((TransportClient) client).addTransportAddress(
                new InetSocketTransportAddress(host, port));
    }

}
