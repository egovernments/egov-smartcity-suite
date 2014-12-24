package org.egov.pims.web.actions.report;

import java.util.Date;

import org.apache.struts2.convention.annotation.ParentPackage
;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryCriteria;
import org.egov.pims.commons.service.PositionService;
import org.egov.web.actions.SearchFormAction;
import org.hibernate.Criteria;
@ParentPackage("egov")
public class SearchVacantPositionAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
private Date fromDate;
 private Date toDate;
 private Integer designationId;
 private PositionService positionService;
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		
		Criteria searchCriteria=positionService.getVacantPositionCriteria(
				getFromDate(), getToDate(), getDesignationId());
		Criteria countCriteria=positionService.getVacantPositionCriteria(
				getFromDate(), getToDate(), getDesignationId());
		countCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		SearchQuery searchquery= new SearchQueryCriteria(searchCriteria,countCriteria);
		return searchquery;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void prepare(){ 
		addDropdownData("designationList", getPersistenceService().findAllBy("from DesignationMaster order by designationName"));
		
	}
	@Override
	public String execute()   
	{
		return SUCCESS;
	}
	@Override
	public String search()   
	{		
		setPageSize(20);
		super.search();
		request.put("positionList", searchResult);
		return SUCCESS;
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

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	

	public void setPositionService(PositionService positionService) {
		this.positionService = positionService;
	}
	

}
