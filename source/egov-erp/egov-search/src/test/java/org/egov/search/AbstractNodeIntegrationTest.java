package org.egov.search;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexAction;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.network.NetworkUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.Arrays;

public abstract class AbstractNodeIntegrationTest {
    private static final TimeValue TIMEOUT = TimeValue.timeValueSeconds(5L);
    private static final ESLogger ES_LOGGER = Loggers.getLogger(AbstractNodeIntegrationTest.class);
    protected static Node node;
    protected static int PORT = 9209;

    @BeforeClass
    public static void beforeAllTests() throws IOException {
        ImmutableSettings.Builder settingsBuilder = ImmutableSettings.settingsBuilder()
                .put("path.data", "target/es-data")
                .put("http.port", PORT)
                .put("cluster.name", "test-cluster-" + NetworkUtils.getLocalAddress());

        node = NodeBuilder.nodeBuilder().local(true).settings(settingsBuilder).node();
    }

    @AfterClass
    public static void afterAllTests() {
        String[] indices = node.client().admin().cluster().prepareState().execute().actionGet().getState().getMetaData().concreteAllIndices();
        DeleteIndexResponse deleteIndexResponse = node.client().admin().indices().prepareDelete(indices).execute().actionGet();
        ES_LOGGER.info("Delete indices [{}] acknowledged [{}]", Arrays.toString(indices), deleteIndexResponse.isAcknowledged());
        node.close();
    }

    protected CreateIndexResponse createIndexOnNode(String indexName) {
        if (indexExists(indexName)) {
            ES_LOGGER.warn("Index [" + indexName + "] already exists. Attempting to delete it.");
            DeleteIndexResponse builder = this.deleteIndexOnNode(indexName);
            ES_LOGGER.info("Delete index [{}] acknowledged [{}]", indexName, builder.isAcknowledged());
        }

        ImmutableSettings.Builder settingsBuilder = ImmutableSettings.settingsBuilder();
        Settings settings = settingsBuilder.put("index.mapper.dynamic", true)
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
                .build();
        CreateIndexResponse createIndexResponse = node.client().admin().indices()
                .create((new CreateIndexRequest(indexName)).settings(settings)).actionGet(TIMEOUT);
        waitForGreenClusterState(indexName);
        return createIndexResponse;
    }

    protected DeleteIndexResponse deleteIndexOnNode(String indexName) {
        if (!this.indexExists(indexName)) {
            ES_LOGGER.warn("Index [" + indexName + "] does not exist. Cannot delete.");
            return DeleteIndexAction.INSTANCE.newResponse();
        } else {
            return node.client().admin().indices().delete(new DeleteIndexRequest(indexName)).actionGet(TIMEOUT);
        }
    }

    protected boolean indexExists(String name) {
        return node.client().admin().indices().exists(new IndicesExistsRequest(name)).actionGet().isExists();
    }

    protected boolean typeExists(String indexName, String typeName) {
        return node.client().admin().indices().prepareTypesExists(new String[]{indexName})
                .setTypes(new String[]{typeName}).execute().actionGet().isExists();
    }

    protected void refreshIndices(String... indices) {
        node.client().admin().indices().prepareRefresh(indices).execute().actionGet();
    }

    private ClusterHealthResponse waitForGreenClusterState(String index) {
        ClusterAdminClient clusterAdminClient = node.client().admin().cluster();
        ClusterHealthRequest request = (new ClusterHealthRequestBuilder(clusterAdminClient))
                .setIndices(index).setWaitForGreenStatus().request();
        return clusterAdminClient.health(request).actionGet();
    }

    protected CreateIndexResponse createIndexOnNode(String indexName, Settings settings) {
        if (this.indexExists(indexName)) {
            ES_LOGGER.warn("Index [" + indexName + "] already exists. Attempting to delete it.");
            DeleteIndexResponse deleteIndexResponse = this.deleteIndexOnNode(indexName);
            ES_LOGGER.info("Delete index [{}] acknowledged [{}]", indexName, deleteIndexResponse.isAcknowledged());
        }

        CreateIndexResponse createIndexResponse = node.client().admin().indices()
                .create((new CreateIndexRequest(indexName)).settings(settings)).actionGet(TIMEOUT);
        this.waitForGreenClusterState(indexName);
        return createIndexResponse;
    }

    protected AdminClient adminClient() {
        return node.client().admin();
    }

    protected Client client() {
        return node.client();
    }
}