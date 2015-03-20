package org.egov.infra.events.service;

import org.egov.infra.events.entity.EventProcessorSpec;
import org.egov.infra.events.repository.EventProcessorSpecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProcessorSpecService {

    @Autowired
    private EventProcessorSpecRepository eventProcessorSpecRepository;

    public EventProcessorSpec getEventProcessingSpecByModAndCode(final String module, final String eventCode) {
        return eventProcessorSpecRepository.findByModuleAndEventCode(module, eventCode);
    }
}
