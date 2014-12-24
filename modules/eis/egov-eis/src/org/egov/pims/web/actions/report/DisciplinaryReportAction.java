package org.egov.pims.web.actions.report;

import java.util.Date;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.model.DisciplinaryPunishment;
import org.egov.pims.reports.services.EisReportService;
import org.egov.web.actions.BaseFormAction;

public class DisciplinaryReportAction extends BaseFormAction {
	private List<DisciplinaryPunishment> results;
	private PersistenceService persistenceService;
	private EisReportService eisReportService;
	private Date fromDate;
	private Date toDate = new Date();
	
	public List<DisciplinaryPunishment> getResults() {
		return results;
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	public EisReportService getEisReportService() {
		return eisReportService;
	}
	public void setEisReportService(EisReportService eisReportService) {
		this.eisReportService = eisReportService;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	@Override
	public Object getModel() {
		return null;
	}
	
	@Override
	@SkipValidation
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	public String report() throws Exception {
		results = eisReportService.getDisciplinaryActionsPendingForPeriod( fromDate, toDate);
		return "list";		
	}
	
	@Override
	public void validate() {
		if (fromDate == null )
			addFieldError("fromDate", getText("fromDate.required"));
		if (toDate == null )
			addFieldError("toDate", getText("toDate.required"));
		if (fromDate!=null && toDate!=null && (fromDate.compareTo(toDate) > 0))
			addFieldError("fromDate", getText("fromDate.lesserThan.toDate"));
	}
	
}
