package org.egov.infra.config.elasticsearch;

import org.egov.infra.config.properties.ApplicationProperties;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetSocketAddress;

@Configuration
@EnableElasticsearchRepositories(basePackages = {"org.egov.**.repository.es", "org.egov.**.repository.elasticsearch"})
public class ElasticSearchConfiguration {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public Client transportClient() {
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", applicationProperties.searchClusterName()).build();
        Client client = TransportClient.builder().settings(settings).build();
        applicationProperties.searchHosts().stream().forEach(host -> addTransportClient(client, host, applicationProperties.searchPort()));
        return client;
    }

    private TransportClient addTransportClient(Client client, String host, int port) {
        return ((TransportClient) client).addTransportAddress(
                new InetSocketTransportAddress(new InetSocketAddress(host, port)));
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(transportClient());
    }
}