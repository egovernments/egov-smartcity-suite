package org.egov.works.web.actions.estimate;

import java.util.Collection;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.admbndry.Boundary;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.Action;

@Result(name=Action.SUCCESS, type="redirect", location = "wardSearch-searchResults")  

@ParentPackage("egov")  

public class WardSearchAction extends BaseFormAction {
	private PersistenceService<Boundary,Integer> boundaryService;  
	private static final String SEARCH_RESULTS = "searchResults";
	private String query;
	private String boundaryTypeName;
	
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
		return boundaryService.findAllBy("from BoundaryImpl where upper(boundaryType.name) in ('CITY','ZONE','WARD') and upper(boundaryType.heirarchyType.name)='ADMINISTRATION' and isHistory='N' and upper(name) like '%' || ? || '%'   order by name", query.toUpperCase());
	}

	public void setBoundaryTypeName(String boundaryTypeName) {
		this.boundaryTypeName = boundaryTypeName;
	}


}
