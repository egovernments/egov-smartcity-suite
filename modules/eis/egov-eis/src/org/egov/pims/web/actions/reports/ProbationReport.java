package org.egov.pims.web.actions.reports;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.displaytag.pagination.PaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.model.Probation;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;

public class ProbationReport extends SearchFormAction{


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String target = "search";
	private Date probationDate;
	private Integer departmentId;
	private Probation model = new Probation();
	private PaginatedList pagedResults;
	private int page;
	private int pageSize;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public PaginatedList getPagedResults() {
		return pagedResults;
	}

	public void setPagedResults(PaginatedList searchResult) {
		this.pagedResults = searchResult;
	}

	public String empsUnderProbSearch() throws Exception
	{
		target="search";
		return target;
	}

	public void prepare()
	{
		addDropdownData("departmentlist", EgovMasterDataCaching.getInstance().get("egi-department"));
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Date getProbationDate() {
		return probationDate;
	}

	public void setProbationDate(Date probationDate) {
		this.probationDate = probationDate;
	}

	@SkipValidation
	public String load() {
		return NEW;
	}

	@ValidationErrorPage(NEW)
	public String getEmpsUnderProbation() {
		super.search();
		setPagedResults(searchResult);
		return NEW;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {

		List<Object> params=new ArrayList<Object>();
		params.add(probationDate);
		params.add(probationDate);
		if(departmentId !=null && departmentId > 0)
		{
			params.add(departmentId);
		}
		String countQuery = getEmployessUnderProbationQueryStringCount(departmentId);
		SearchQuery searchquery= new SearchQuerySQL(getEmployessUnderProbationQueryString(departmentId),countQuery,
				params);
		return searchquery;
	}

	private String getEmployessUnderProbationQueryString(Integer departmentId)
	{
		String query="";
		if(departmentId > 0 && departmentId!=null)
		{
			query="(SELECT EMPINFO.CODE,EMPINFO.NAME, DEPT.DEPT_NAME, PROBATION.PROBATION_FROM_DATE," +
					"PROBATION.PROBATION_TO_DATE FROM EGEIS_PROBATION PROBATION,EG_EIS_EMPLOYEEINFO EMPINFO" +
					",EG_DEPARTMENT DEPT where PROBATION.ID = EMPINFO.ID AND EMPINFO.DEPT_ID=DEPT.ID_DEPT AND"+
					" EMPINFO.IS_PRIMARY='Y' AND PROBATION.PROBATION_FROM_DATE < ? " +
					"AND PROBATION.PROBATION_TO_DATE > ?  AND EMPINFO.DEPT_ID = ?)";
		}
		else
		{
			 query="(SELECT EMPINFO.CODE,EMPINFO.NAME, DEPT.DEPT_NAME, PROBATION.PROBATION_FROM_DATE," +
					"PROBATION.PROBATION_TO_DATE FROM EGEIS_PROBATION PROBATION,EG_EIS_EMPLOYEEINFO EMPINFO" +
					",EG_DEPARTMENT DEPT where PROBATION.ID = EMPINFO.ID AND EMPINFO.DEPT_ID=DEPT.ID_DEPT AND"+
					" EMPINFO.IS_PRIMARY='Y' AND PROBATION.PROBATION_FROM_DATE < ? AND PROBATION.PROBATION_TO_DATE > ?  )";
		}

		return query;
	}

	private String getEmployessUnderProbationQueryStringCount(Integer departmentId)
	{
		String countQuery = "SELECT COUNT(*) FROM  ";
		countQuery += getEmployessUnderProbationQueryString(departmentId);
		return countQuery;
	}
}
