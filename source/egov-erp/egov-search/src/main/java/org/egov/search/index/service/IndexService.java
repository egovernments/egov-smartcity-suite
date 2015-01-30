package org.egov.search.index.service;

import org.egov.search.ResourceType;
import org.egov.search.index.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.TextMessage;

@Service
public class IndexService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination indexQueue;

    public void index(String index, ResourceType resourceType, Document document) {
        jmsTemplate.send(indexQueue, session -> {
            TextMessage textMessage = session.createTextMessage(document.getResource());
            textMessage.setStringProperty("index", index);
            textMessage.setStringProperty("type", resourceType.indexType());
            return textMessage;
        });
    }
}
