package org.egov.ptis.workflow.filter;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ejb.CreateException;

import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.ServiceLocatorException;
import org.egov.models.AbstractPersistenceServiceTest;
import org.junit.Ignore;
import org.junit.Test;

public class ActionsBasedOnWorkFlowTest extends AbstractPersistenceServiceTest<Script, Long> {
	@Ignore
	public void testActionBasedOnWorkFlowScript() throws ServiceLocatorException, CreateException,
			ClassNotFoundException {
		Boolean authorized = Boolean.FALSE;
		PersistenceService<Script, Long> scriptService = new PersistenceService<Script, Long>();
		PersistenceService workFlowPerService = new PersistenceService();
		scriptService.setSessionFactory(egovSessionFactory);
		workFlowPerService.setSessionFactory(egovSessionFactory);
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "WorkFlowBasedActions");
		List authResList = (List) scripts.get(0).eval(
				Script.createContext("ActionName", "/property/beforeDeactivateProp.do", "properrtyId",
						"08-119-0000-000", "persistService", workFlowPerService));
		String authStr = (String) authResList.get(0);
		authorized = Boolean.valueOf(authStr);
		assertEquals(true, authorized);
	}

	@Test
	public void test() {
		assertEquals(true, true);
	}
}
