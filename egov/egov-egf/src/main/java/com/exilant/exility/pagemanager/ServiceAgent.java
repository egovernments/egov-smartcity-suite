package com.exilant.exility.pagemanager;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilServiceInterface;
import com.exilant.exility.common.XMLGenerator;
import com.exilant.exility.service.DataService;
import com.exilant.exility.service.DescriptionService;
import com.exilant.exility.service.ListService;
import com.exilant.exility.service.TreeService;
import com.exilant.exility.service.UpdateService;

/**
 * @author raghu.bhandi, Exilant Consulting
 * 
 *         Common class to locate and execute a service. We can configure this
 *         to either look-up for EJB, or use local class Idea is that the JSP's
 *         need not assume remote. They will work either in a full J2EE
 *         architecture, or just with Tomcat JVM.
 * 
 *         This is the only class that needs to be configured to adapt Exility
 *         between remote/local systems
 * 
 */
public class ServiceAgent {
	private static final Logger LOGGER = Logger.getLogger(ServiceAgent.class);
	private static ServiceAgent agent;
	private HashMap services;

	/**
	 * 
	 */
	public static ServiceAgent getAgent() {
		if (agent == null) {
			agent = new ServiceAgent();
			agent.getInitialHashMap();
		}
		return agent;
	}

	private ServiceAgent() {
	}

	public void deliverService(String serviceName, DataCollection dc) {
		Object service = services.get(serviceName);
		if (service == null) { // try to locate that class
			dc.addMessage("exilNoServiceName", serviceName);
		} else {
			try {
				((ExilServiceInterface) service).doService(dc);

			} catch (Exception e) {
				LOGGER.error("exilServerError" + e.getMessage());
				dc.addMessage("exilServerError", e.getMessage());
			}
		}
	}

	private void getInitialHashMap() {
		HashMap classes = new HashMap();
		classes.put("DataService", DataService.getService());
		classes.put("UpdateService", UpdateService.getService());
		classes.put("DescriptionService", DescriptionService.getService());
		classes.put("ListService", ListService.getService());
		classes.put("TreeService", TreeService.getService());
		this.services = classes;
	}

	public static void main(String[] args) {
		DataCollection dc = new DataCollection();
		dc.addValue("serviceID", "getCustomerData");
		dc.addValue("nameStartingWith", "startOfName");
		dc.addValue("codeFrom", "codeStart");
		dc.addValue("codeTo", "codeEnd");
		ServiceAgent anAgent = ServiceAgent.getAgent();
		anAgent.deliverService("ListService", dc);
		XMLGenerator generator = XMLGenerator.getInstance();
		if(LOGGER.isDebugEnabled())     LOGGER.debug(generator.toXML(dc, "", ""));

	}
}
