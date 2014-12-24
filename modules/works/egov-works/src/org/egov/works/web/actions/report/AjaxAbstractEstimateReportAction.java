package org.egov.works.web.actions.report;

import java.util.Collection;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.admbndry.Boundary;
import org.egov.model.budget.BudgetGroup;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
@SuppressWarnings("serial")
public class AjaxAbstractEstimateReportAction extends BaseFormAction {
	private Long zoneId;	    // Set by Ajax call
	private PersistenceService<Boundary,Integer> boundaryService;
	private static final String SEARCH_WARDS = "searchWards";
	private static final String SEARCH_BUDGETS = "searchBudgets";
	private String query;
	
	
	@Override
	public Object getModel() {
		return null;
	}
	
	public String searchWardAjax(){
		return SEARCH_WARDS;
	}
	
	public Collection<Boundary> getBoundaryList() {
		if(zoneId!=null && zoneId!=-1)
			return boundaryService.findAllBy("from BoundaryImpl where upper(boundaryType.name) in ('WARD') and upper(boundaryType.heirarchyType.name)='ADMINISTRATION' and parent.id=?  and isHistory='N'  and  upper(name) like '%' || ? || '%'   order by name",zoneId.intValue(), query.toUpperCase());
		else
			return boundaryService.findAllBy("from BoundaryImpl where upper(boundaryType.name) in ('WARD') and upper(boundaryType.heirarchyType.name)='ADMINISTRATION'  and isHistory='N' and  upper(name) like '%' || ? || '%'   order by name", query.toUpperCase());
	}
	
	public Collection<BudgetGroup> getBudgetHeadList() {
			return  getPersistenceService().findAllBy("from BudgetGroup where upper(name) like ? ","%"+query.toUpperCase()+"%");
	}
	
	public  String searchBudgetHeadAjax()
	{
		return SEARCH_BUDGETS;
	}
	
	public void setBoundaryService(
			PersistenceService<Boundary, Integer> boundaryService) {
		this.boundaryService = boundaryService;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

}
