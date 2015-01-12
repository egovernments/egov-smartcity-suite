/*
 * @(#)EventProcessor.java 3.0, 17 Jun, 2013 12:00:34 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.events.processing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.infstr.events.domain.entity.Event;
import org.egov.infstr.events.domain.entity.EventProcessorSpec;
import org.egov.infstr.events.domain.entity.EventResult;
import org.egov.infstr.events.domain.entity.schema.Response;
import org.egov.infstr.events.domain.service.EventProcessorSpecService;
import org.egov.infstr.events.domain.service.EventResultService;
import org.egov.infstr.events.domain.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class EventProcessor {
	private static String DOMAIN_NAME = "";
	private static String FACTORY_NAME = "";
	private static String JNDI_NAME = "";
	private static String RESULT = "";
	private ClassPathXmlApplicationContext globalAppContext = null;
	private static final Logger LOG = LoggerFactory.getLogger(EventService.class);
	private EventProcessorSpecService eventProcessSpecService;
	private ResponseHandler responseHandler;
	private EventResultService eventResultService;

	/*
	 * 1. Take the responseTemplate of the event object and do the variable substitution using freemarker and the params object for the values. 
	 * 2. See what is the type of the response and proceed accordingly. Create an appropriate response handler and call its respond() method. 
	 * 3. Create and persist
	 * the EventResult
	 */
	public void process(final Event e) {
		EventProcessorSpec eventSpec = null;
		try {
			RESULT = "success";
			DOMAIN_NAME = e.getParams().get("DOMAIN_NAME");
			JNDI_NAME = e.getParams().get("JNDI_NAME");
			FACTORY_NAME = e.getParams().get("FACTORY_NAME");
			SetDomainJndiHibFactNames.setThreadLocals(DOMAIN_NAME, JNDI_NAME, FACTORY_NAME);
			eventSpec = this.eventProcessSpecService.getEventProcessingSpecByModAndCode(e.getModule(), e.getEventCode());
			if (eventSpec != null) {
				e.setResponseTemplate(eventSpec.getResponseTemplate());
			}
			String processedTemplate = null;
			processedTemplate = substituteParams(e);
			e.setResponseTemplate(processedTemplate);
			final Response response = response(processedTemplate);
			responseCallback(e, response);
			this.responseHandler.respond(response);
		} catch (final Exception ex) {
			RESULT = "failure:: " + ex.getMessage();
		}
		persistEventResult(e, eventSpec);
	}

	/**
	 * @param e
	 * @param response
	 * @throws EGOVRuntimeException
	 * @throws BeansException
	 */
	private void responseCallback(final Event e, final Response response) throws EGOVRuntimeException, BeansException {
		/*
		 * CallBack : Executes the Additional Process Required by the calling application(Add to or Modify the event's key-value data)
		 */
		if (response.getCallback() != null && response.getCallback().getBean() != null && !response.getCallback().getBean().isEmpty()) {
			try {
				this.globalAppContext = new ClassPathXmlApplicationContext("org/egov/infstr/beanfactory/globalApplicationContext.xml");
			} catch (final Exception exp) {
				LOG.error("Bean cannot be retrieved, cause : {}" + exp.getMessage());
				throw new EGOVRuntimeException("Bean cannot be retrieved, cause : {}", exp);
			}
			try {
				if (this.globalAppContext != null) {
					final EventCallBack eventCallBack = (EventCallBack) this.globalAppContext.getBean(response.getCallback().getBean());
					eventCallBack.execute(e.getParams());
				}
			} catch (final NoSuchBeanDefinitionException exp) {
				LOG.error("No bean named {} is available in class path context" + response.getCallback().getBean());
				throw new EGOVRuntimeException("No bean named {} is available in class path context", exp);
			}
		}
	}

	private String substituteParams(final Event e) {
		final Configuration cfg = new Configuration();
		final StringWriter writer = new StringWriter();
		String processedTemplate = null;
		// StringTemplateLoader Freemarker used to read the ResponseTemplate of Event object
		final StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("template", e.getResponseTemplate());
		cfg.setTemplateLoader(stringLoader);
		Template tpl;

		// Substitutes the ResponseTemplate variables with the values defined in the map(params)
		try {
			tpl = cfg.getTemplate("template");
			tpl.process(e.getParams(), writer);
			processedTemplate = writer.toString();

		} catch (final IOException ex) {
			throw new EGOVRuntimeException("Exception Occurred in EventProcessor while substituting Parameters" + ex);
		} catch (final TemplateException ex) {
			throw new EGOVRuntimeException("Exception Occurred in EventProcessor while processing Template" + ex);
		}
		return processedTemplate;
	}

	// JAXB-unmarshal : To unmarshal XML into Java objects.
	private Response response(final String processedTemplate) {
		Response response = null;
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance("org.egov.infstr.events.domain.entity.schema");
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			final InputStream is = new ByteArrayInputStream(processedTemplate.getBytes("UTF-8"));
			response = (Response) unmarshaller.unmarshal(is);
		} catch (final UnsupportedEncodingException e) {
			throw new EGOVRuntimeException("Exception Occurred in EventProcessor while unmarshalling Template" + e);
		} catch (final JAXBException e) {
			throw new EGOVRuntimeException("Exception Occurred in EventProcessor while unmarshalling Template" + e);
		}
		return response;
	}

	/**
	 * @param e
	 * @param eventSpec
	 */
	private void persistEventResult(final Event e, final EventProcessorSpec eventSpec) {
		final EventResult eventResult = new EventResult();
		eventResult.setModule(eventSpec.getModule());
		eventResult.setEventCode(eventSpec.getEventCode());
		eventResult.setDateRaised(e.getDateRaised());
		eventResult.setResult(RESULT);
		eventResult.setTimeOfProcessing(new Date());
		eventResult.setDetails(e.toString());
		this.eventResultService.persistEventResult(eventResult);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Event Result Processed::::" + eventResult);
		}
	}

	public EventProcessorSpecService getEventProcessSpecService() {
		return this.eventProcessSpecService;
	}

	public void setEventProcessSpecService(final EventProcessorSpecService eventProcessSpecService) {
		this.eventProcessSpecService = eventProcessSpecService;
	}

	public ResponseHandler getResponseHandler() {
		return this.responseHandler;
	}

	public void setResponseHandler(final ResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}

	public EventResultService getEventResultService() {
		return this.eventResultService;
	}

	public void setEventResultService(final EventResultService eventResultService) {
		this.eventResultService = eventResultService;
	}
}
