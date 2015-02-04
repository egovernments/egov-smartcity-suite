package org.egov.search.index.service;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
class ESIndexClient {

    private Client client;

    @Autowired
    public ESIndexClient(@Qualifier("transportClient") Client client) {
        this.client = client;
    }


    public boolean index(String documentId, String document, String indexName, String type) {
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
}
