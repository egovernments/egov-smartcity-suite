package org.egov.works.models.estimate;

import java.util.Date;
import java.util.List;

import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.SequenceGenerator;

public class AdminSanctionNumberGenerator {
	public static final String SEQUENCE_TYPE = "ADMINSANCTION_NUMBER";
	private SequenceGenerator sequenceGenerator;
	private PersistenceService<Script, Long> scriptService;
	private DateUtils date;
	private transient ScriptService scriptExecutionService;

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public String getAdminNumber(AbstractEstimate estimate) {
		String date = DateUtils.getFormattedDate(new Date(), "dd-MM-yyyy");
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.adminnumber.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), 
				ScriptService.createContext("estimate", estimate, "date", date, "sequenceGenerator", sequenceGenerator));
	}

	public void setScriptService(
			PersistenceService<Script, Long> persistenceService) {
		this.scriptService = persistenceService;
	}

	public void discardNumber() {
		sequenceGenerator.discardNumber(SEQUENCE_TYPE);
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

}
