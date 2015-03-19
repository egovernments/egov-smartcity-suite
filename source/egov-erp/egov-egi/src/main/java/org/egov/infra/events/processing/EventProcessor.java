package org.egov.infra.events.processing;

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
import org.egov.infra.events.entity.Event;
import org.egov.infra.events.entity.EventProcessorSpec;
import org.egov.infra.events.entity.EventResult;
import org.egov.infra.events.entity.schema.Response;
import org.egov.infra.events.service.EventProcessorSpecService;
import org.egov.infra.events.service.EventResultService;
import org.egov.infra.events.service.EventService;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class EventProcessor {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventService.class);
	private static String DOMAIN_NAME = "";
	private static String FACTORY_NAME = "";
	private static String JNDI_NAME = "";
	private static String RESULT = "";
	private @Autowired ApplicationContext applicationContext;
	private @Autowired EventProcessorSpecService eventProcessSpecService;
	private @Autowired ResponseHandler responseHandler;
	private @Autowired EventResultService eventResultService;

	/*
	 * 1. Take the responseTemplate of the event object and do the variable
	 * substitution using freemarker and the params object for the values. 2.
	 * See what is the type of the response and proceed accordingly. Create an
	 * appropriate response handler and call its respond() method. 3. Create and
	 * persist the EventResult
	 */
	public void process(final Event e) {
		EventProcessorSpec eventSpec = null;
		try {
			RESULT = "success";
			DOMAIN_NAME = e.getParams().get("DOMAIN_NAME");
			JNDI_NAME = e.getParams().get("JNDI_NAME");
			FACTORY_NAME = e.getParams().get("FACTORY_NAME");
			SetDomainJndiHibFactNames.setThreadLocals(DOMAIN_NAME, JNDI_NAME,
					FACTORY_NAME);
			eventSpec = this.eventProcessSpecService
					.getEventProcessingSpecByModAndCode(e.getModule(),
							e.getEventCode());
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
	private void responseCallback(final Event e, final Response response)
			throws EGOVRuntimeException, BeansException {
		/*
		 * CallBack : Executes the Additional Process Required by the calling
		 * application(Add to or Modify the event's key-value data)
		 */
		if (response.getCallback() != null
				&& response.getCallback().getBean() != null
				&& !response.getCallback().getBean().isEmpty()) {
			try {
				if (this.applicationContext != null) {
					final EventCallBack eventCallBack = (EventCallBack) this.applicationContext
							.getBean(response.getCallback().getBean());
					eventCallBack.execute(e.getParams());
				}

			} catch (final NoSuchBeanDefinitionException exp) {
				LOG.error("No bean named " + response.getCallback().getBean()
						+ " is available in class path context");
				throw new EGOVRuntimeException(
						"No bean named {} is available in class path context",
						exp);
			}
		}
	}

	private String substituteParams(final Event e) {
		final Configuration cfg = new Configuration();
		final StringWriter writer = new StringWriter();
		String processedTemplate = null;
		// StringTemplateLoader Freemarker used to read the ResponseTemplate of
		// Event object
		final StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("template", e.getResponseTemplate());
		cfg.setTemplateLoader(stringLoader);
		Template tpl;

		// Substitutes the ResponseTemplate variables with the values defined in
		// the map(params)
		try {
			tpl = cfg.getTemplate("template");
			tpl.process(e.getParams(), writer);
			processedTemplate = writer.toString();

		} catch (final IOException ex) {
			throw new EGOVRuntimeException(
					"Exception Occurred in EventProcessor while substituting Parameters"
							+ ex);
		} catch (final TemplateException ex) {
			throw new EGOVRuntimeException(
					"Exception Occurred in EventProcessor while processing Template"
							+ ex);
		}
		return processedTemplate;
	}

	// JAXB-unmarshal : To unmarshal XML into Java objects.
	private Response response(final String processedTemplate) {
		Response response = null;
		try {
			final JAXBContext jaxbContext = JAXBContext
					.newInstance("org.egov.infra.events.entity.schema");
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			final InputStream is = new ByteArrayInputStream(
					processedTemplate.getBytes("UTF-8"));
			response = (Response) unmarshaller.unmarshal(is);
		} catch (final UnsupportedEncodingException e) {
			throw new EGOVRuntimeException(
					"Exception Occurred in EventProcessor while unmarshalling Template"
							+ e);
		} catch (final JAXBException e) {
			throw new EGOVRuntimeException(
					"Exception Occurred in EventProcessor while unmarshalling Template"
							+ e);
		}
		return response;
	}

	/**
	 * @param e
	 * @param eventSpec
	 */
	private void persistEventResult(final Event e,
			final EventProcessorSpec eventSpec) {
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
}