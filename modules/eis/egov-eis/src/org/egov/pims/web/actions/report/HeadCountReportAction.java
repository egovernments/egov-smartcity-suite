package org.egov.pims.web.actions.report;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.displaytag.pagination.PaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.pims.reports.services.EisReportService;
import org.egov.web.actions.SearchFormAction;
import com.opensymphony.xwork2.validator.annotations.Validation;

/**
 * @author Poornima
 * Created on 06/01/2011
 */


@ParentPackage("egov")
@Validation()
public class HeadCountReportAction extends SearchFormAction
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger=Logger.getLogger(EisReportService.class);
	private transient Date givenDate;
	private transient Integer departmentId;
	private PaginatedList deptDesigList; 
	private EisReportService eisReportService;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void prepare() {
		super.prepare();
		this.addDropdownData("departmentList", this.getPersistenceService().findAllBy("from DepartmentImpl order by deptName"));
	}
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) 
	{			
		Date userGivenDate=givenDate==null?new Date():givenDate;	    
		List<Object> params=new ArrayList<Object>();		
		params.add(userGivenDate);
		params.add(userGivenDate);
		
		String countQuery=eisReportService.getCountEmpCountAndDesigByDept(departmentId);					
		SearchQuery searchquery= new SearchQuerySQL(eisReportService.getEmpCountAndDesigByDept(departmentId), countQuery, params);
		return searchquery;		
	}
	
	@Override
	public String execute() {
		return SUCCESS;
	}
	
	/*
	 * This method counts the employees under each designation for a given department as on given date.
	 */
	@Override
	public String search()
	{
		
		if(givenDate == null)
		{
			addActionError("Please Select Date");
		}
		else
		{
		
			setPageSize(20);
			super.search();				
			setDeptDesigList(searchResult);			
		}
		return SUCCESS;
	}

	
	/**
	 * @return the givenDate
	 */
	public Date getGivenDate() {
		return givenDate;
	}

	/**
	 * @param givenDate the givenDate to set
	 */
	public void setGivenDate(Date givenDate) {
		this.givenDate = givenDate;
	}

	/**
	 * @return the departmentId
	 */
	public Integer getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	
	/**
	 * @return the deptDesigList
	 */
	public PaginatedList getDeptDesigList() {
		return deptDesigList;
	}

	/**
	 * @param deptDesigList the deptDesigList to set
	 */
	public void setDeptDesigList(PaginatedList deptDesigList) {
		this.deptDesigList = deptDesigList;
	}

	/**
	 * @return the eisReportService
	 */
	public EisReportService getEisReportService() {
		return eisReportService;
	}

	/**
	 * @param eisReportService the eisReportService to set
	 */
	public void setEisReportService(EisReportService eisReportService) {
		this.eisReportService = eisReportService;
	}
	

}
