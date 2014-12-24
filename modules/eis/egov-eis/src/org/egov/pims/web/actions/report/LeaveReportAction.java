package org.egov.pims.web.actions.report;

import java.util.Date;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.web.actions.BaseFormAction;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.reports.services.EisReportService;

public class LeaveReportAction extends BaseFormAction {
	
	private List<Object[]> results;
	private PersistenceService persistenceService;
	private EisReportService eisReportService;
	private Integer departmentId;
	private Date fromDate;
	private Date toDate;
	private EISServeable eisService;
	
	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
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

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public EisReportService getEisReportService() {
		return eisReportService;
	}

	public void setEisReportService(EisReportService eisReportService) {
		this.eisReportService = eisReportService;
	}

	public EISServeable getEisService() {
		return eisService;
	}

	public void setEisService(EISServeable eisService) {
		this.eisService = eisService;
	}

	public LeaveReportAction() {
		super();
	}
	
	@Override
	public Object getModel() {
		return null;
	}

	@Override
	public void prepare() {
		super.prepare();
		addDropdownData("departmentList", getEisService().getDeptsForUser());
	}

	@Override
	@SkipValidation
	public String execute() throws Exception {
		return SUCCESS;
	}

	
	public String list() throws Exception {
		results = eisReportService.getEmployeeLeaveDatesForDept(departmentId, fromDate, toDate);
		return "list";		
	}

	public List<Object[]> getResults() {
		return results;
	}
	
	public String getFormattedDate(Date dt){
		return DateUtils.getFormattedDate(dt, "dd/MM/yyyy");
	}

	@Override
	public void validate() {
		if (departmentId == null || departmentId == -1)
			addFieldError("departmentId", getText("department.required"));
		if (fromDate == null )
			addFieldError("fromDate", getText("fromDate.required"));
		if (toDate == null )
			addFieldError("toDate", getText("toDate.required"));
		if (fromDate!=null && toDate!=null && (fromDate.compareTo(toDate) > 0))
			addFieldError("fromDate", getText("fromDate.lesserThan.toDate"));
	}
	
	
}
