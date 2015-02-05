package org.egov.search.index.service;

import org.egov.search.index.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
@Transactional
public class IndexQueueListener implements MessageListener {

    private ElasticSearchClient esIndexClient;

    @Autowired
    public IndexQueueListener(ElasticSearchClient esIndexClient) {
        this.esIndexClient = esIndexClient;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String indexName = message.getStringProperty("index");
            String indexType = message.getStringProperty("type");
            String documentMessage = ((TextMessage) message).getText();
            Document doc = Document.fromJson(documentMessage);
            esIndexClient.index(doc.getCorrelationId(), documentMessage, indexName, indexType);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
