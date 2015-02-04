package org.egov.search.index.service;

import com.jayway.jsonassert.JsonAssert;
import org.egov.config.ApplicationConfig;
import org.egov.search.AbstractNodeIntegrationTest;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class ESIndexClientTest extends AbstractNodeIntegrationTest {

    private String indexName;
    private ESIndexClient indexClient;

    @Mock
    private ApplicationConfig applicationConfig;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        indexClient = new ESIndexClient(client());

        indexName = this.getClass().getSimpleName().toLowerCase();
        createIndexOnNode(indexName);
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

        JsonAssert.with(searchResult).assertEquals("$.hits.total", 1);
        JsonAssert.with(searchResult).assertEquals("$.hits.hits[0]._id", "document_id");
    }
}