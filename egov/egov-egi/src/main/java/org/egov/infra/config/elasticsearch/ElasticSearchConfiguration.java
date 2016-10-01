package org.egov.infra.config.elasticsearch;

import java.net.InetSocketAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.egov.**.repository.elasticsearch")
public class ElasticSearchConfiguration {
	
	@Bean
	public Client transportClient() {
		Settings settings = Settings.settingsBuilder()
				.put("cluster.name", "elasticsearch_ap_prod").build();
		Client client = TransportClient.builder().settings(settings).build();
		addTransportClient(client, "52.74.114.29", 9300);
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