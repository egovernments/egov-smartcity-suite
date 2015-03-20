package org.egov.infra.events.processing;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.events.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Message driven pojo that receives an Event object from the queue Delegates
 * processing to EventProcessor.
 *
 * @author sunil
 */

@Component
public class EventListener implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(EventListener.class);

    @Autowired
    protected EventProcessor eventProcessor;

    @Override
    public void onMessage(final Message message) {
        final ObjectMessage obj = (ObjectMessage) message;
        Event event = null;
        try {
            event = (Event) obj.getObject();
            eventProcessor.process(event);
            if (LOG.isDebugEnabled())
                LOG.debug("Event Received" + event);
        } catch (final JMSException e) {
            LOG.error("Exception in EventListener ===> " + e.getMessage());
            throw new EGOVRuntimeException("Exception in EventListener message ::::", e);
        } catch (final Exception e) {
            LOG.error("Exception Occurred in EventListener ===> " + e.getMessage());
            throw new EGOVRuntimeException("Exception Occurred in EventListener" + e);
        }
    }
}