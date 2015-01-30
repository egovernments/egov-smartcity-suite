package org.egov.search.index.service;

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

    private ESIndexClient esIndexClient;

    @Autowired
    public IndexQueueListener(ESIndexClient esIndexClient) {
        this.esIndexClient = esIndexClient;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String indexName = message.getStringProperty("index");
            String indexType = message.getStringProperty("type");
            esIndexClient.index(((TextMessage) message).getText(), indexName, indexType);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
