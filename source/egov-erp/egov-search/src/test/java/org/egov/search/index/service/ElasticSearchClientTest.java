package org.egov.search.index.service;

import com.jayway.restassured.RestAssured;
import org.egov.config.ApplicationConfig;
import org.egov.search.AbstractNodeIntegrationTest;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.jayway.jsonassert.JsonAssert.with;
import static com.jayway.restassured.RestAssured.get;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ElasticSearchClientTest extends AbstractNodeIntegrationTest {

    private String indexName;
    private ElasticSearchClient indexClient;

    @Mock
    private ApplicationConfig applicationConfig;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        indexName = this.getClass().getSimpleName().toLowerCase();
        when(applicationConfig.searchShardsFor(indexName)).thenReturn(1);
        when(applicationConfig.searchReplicasFor(indexName)).thenReturn(0);
        indexClient = new ElasticSearchClient(client(), applicationConfig);

        RestAssured.baseURI = "http://localhost/";
        RestAssured.port = PORT;
    }

    @Test
    public void shouldIndexDocument() {
        Map<String, String> attrs = new HashMap<>();
        attrs.put("name", "Srivatsa Katta");
        attrs.put("role", "Application Developer");
        attrs.put("location", "Bengaluru");

        String type = "employee";
        boolean documentIndexed = indexClient.index("document_id", new JSONObject(attrs).toJSONString(), indexName, type);
        assertTrue(documentIndexed);

        refreshIndices(indexName);

        String searchResult = indexClient.search(indexName, type, "developer");

        with(searchResult).assertEquals("$.hits.total", 1);
        with(searchResult).assertEquals("$.hits.hits[0]._id", "document_id");
    }

    @Test
    public void shouldCreateMappingBasedOnDynamicTemplates() throws ExecutionException, InterruptedException {
        JSONObject searchable = new JSONObject();
        searchable.put("name", "Srivatsa Katta");
        searchable.put("role", "Application Developer");
        searchable.put("location", "Bengaluru");


        JSONObject clauses = new JSONObject();
        clauses.put("department", "PS");
        clauses.put("status", "Permanant");

        JSONObject resource = new JSONObject();
        resource.put("searchable", searchable);
        resource.put("clauses", clauses);

        boolean indexed = indexClient.index("123", resource.toJSONString(), indexName, "users");
        assertTrue(indexed);

        refreshIndices(indexName);

        String mapping = get(indexName + "/_mapping/users").asString();
        with(mapping).assertEquals("$..properties.clauses..department.index", Arrays.asList("not_analyzed"));
        with(mapping).assertEquals("$..properties.clauses..status.index", Arrays.asList("not_analyzed"));
        with(mapping).assertNotDefined("$..searchable..name.index");
        with(mapping).assertNotDefined("$..searchable..role.index");
    }
}