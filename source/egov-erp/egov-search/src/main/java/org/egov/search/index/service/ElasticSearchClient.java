package org.egov.search.index.service;

import org.egov.config.ApplicationConfig;
import org.egov.search.util.Classpath;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
class ElasticSearchClient {

    private Client client;
    private ApplicationConfig applicationConfig;

    @Autowired
    public ElasticSearchClient(@Qualifier("transportClient") Client client, ApplicationConfig applicationConfig) {
        this.client = client;
        this.applicationConfig = applicationConfig;
    }

    public boolean index(String documentId, String document, String indexName, String type) {

        if (!indexExists(indexName)) {
            createIndex(indexName);
        }

        IndexRequestBuilder indexRequestBuilder = new IndexRequestBuilder(client)
                .setIndex(indexName)
                .setType(type)
                .setSource(document)
                .setId(documentId);

        IndexResponse indexResponse = client.index(indexRequestBuilder.request()).actionGet();
        return indexResponse.isCreated();
    }

    public String search(String indexName, String type, String queryString) {
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryString(queryString);
        SearchRequest searchRequest = new SearchRequestBuilder(client).setIndices(indexName).setTypes(type).setQuery(queryStringQueryBuilder).request();
        SearchResponse searchResponse = client.search(searchRequest).actionGet();
        return searchResponse.toString();
    }

    private boolean indexExists(String name) {
        return client.admin().indices().exists(new IndicesExistsRequest(name)).actionGet().isExists();
    }

    private CreateIndexResponse createIndex(String indexName) {
        ImmutableSettings.Builder settingsBuilder = ImmutableSettings.settingsBuilder();
        Settings settings = settingsBuilder
                .put("index.mapper.dynamic", true)
                .put("index.number_of_shards", applicationConfig.searchShardsFor(indexName))
                .put("index.number_of_replicas", applicationConfig.searchReplicasFor(indexName))
                .build();

        String dynamicTemplates = Classpath.readAsString("config/search/dynamic-templates.json");
        CreateIndexRequest createIndexRequest = (new CreateIndexRequest(indexName)).settings(settings).mapping("_default_", dynamicTemplates);
        return client.admin().indices().create(createIndexRequest).actionGet();
    }
}
