package org.egov.works.web.actions.estimate;

import java.util.Collection;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.Action;

@Result(name=Action.SUCCESS, type="ServletRedirectResult.class", location = "wardSearch-searchResults")  

@ParentPackage("egov")  

public class WardSearchAction extends BaseFormAction {
	private PersistenceService<Boundary,Integer> boundaryService;  
	private static final String SEARCH_RESULTS = "searchResults";
	private String query;
	private String boundaryTypeName; 
	private Boolean isBoundaryHistory; 
	
	public void setQuery(String query) {
		this.query = query;
	}

	public String searchAjax(){
		return SEARCH_RESULTS;
	}

	public Object getModel() {
		return null;
	}

	public void setBoundaryService(
			PersistenceService<Boundary, Integer> boundaryService) {
		this.boundaryService = boundaryService;
	}

	public Collection<Boundary> getBoundaryList() {
		StringBuilder boundaryList = new StringBuilder(1000);
		boundaryList.append(" from BoundaryImpl where upper(boundaryType.name) in ('CITY','REGION','ZONE','WARD') " +
				" and upper(boundaryType.heirarchyType.name)='ADMINISTRATION' and upper(name) <> 'HQ' " +
				" and upper(name) like '%' || ? || '%' ");
		if(!isBoundaryHistory){
			boundaryList.append(" and isHistory = 'N' ");
		}
		boundaryList.append(" order by name ");
		return boundaryService.findAllBy(boundaryList.toString(), query.toUpperCase());
	}

	public void setBoundaryTypeName(String boundaryTypeName) {
		this.boundaryTypeName = boundaryTypeName;
	}

	public Boolean getIsBoundaryHistory() {
		return isBoundaryHistory;
	}

	public void setIsBoundaryHistory(Boolean isBoundaryHistory) {
		this.isBoundaryHistory = isBoundaryHistory;
	}

}
