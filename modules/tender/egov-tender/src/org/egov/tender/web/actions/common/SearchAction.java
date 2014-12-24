package org.egov.tender.web.actions.common;

import java.util.Date;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.tender.services.tendernotice.TenderNoticeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.utils.EgovPaginatedList;

/**
 * 
 * @author pritiranjan
 *
 */

@SuppressWarnings("serial")
public abstract class SearchAction extends BaseFormAction{
	
	protected Date 		fromDate;
	protected Date 		toDate;
	protected Integer 	departmentId;
	protected String 	fileType;
	protected Integer 	page=1;
	protected String 	mode;
	protected EgovPaginatedList paginatedList;
	protected TenderNoticeService tenderNoticeService;
	public static final String REPORT = "report";
	protected String searchMode;
	
	public void prepare()
	{
		super.prepare();
		addDropdownData("departmentIdList",persistenceService.findAllBy("from DepartmentImpl order by deptName"));
		addDropdownData("tenderFileTypeList",persistenceService.findAllBy("from TenderFileType where isActive=1 order by fileType "));
	}
	
	@SkipValidation
	public String searchForm()
	{
		return NEW;
	}
	@SkipValidation
	public String generateComparisionSheet()
	{
		searchMode=REPORT;
		return NEW;
	}
	
	public Object getModel()
	{
		return null;
	}
	
	public EgovPaginatedList getPaginatedList() {
		return paginatedList;
	}

	public String getMode() {
		return mode;
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

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public void setPage(Integer page) {
		this.page = page;
	}
	
	public String getMessage(String key)
	{
		return getText(key);
	}
	
	public void setTenderNoticeService(TenderNoticeService tenderNoticeService) {
		this.tenderNoticeService = tenderNoticeService;
	}

	public String getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}
	
	
}
