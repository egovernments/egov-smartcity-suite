/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.web.actions.extd.search;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.AutoDcrExtn;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@ParentPackage("egov")
public class SearchAutoDcrExtnAction extends SearchFormAction
{

	private String initialSearch="NO";
	private String searchMode;	
	private AutoDcrExtn searchAutoDcr=new AutoDcrExtn();
	private Long id;
	private String autonum;
	
	public SearchAutoDcrExtnAction() {
		
	}
	
	public void prepare() {
		super.prepare();
		// addDropdownData("autoDcrList",persistenceService.findAllBy("from SearchAutoDcr order by autoDcrNum"));
			
	}
	@Action(value = "/searchAutoDcrExtn-searchAuto", results = { @Result(name = NEW,type = "dispatcher") })
	public String searchAuto() {
	
		return NEW;
	}
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return searchAutoDcr;
	}
	@ValidationErrorPage(NEW)
	@Transactional
	@Action(value = "/searchAutoDcrExtn-searchResults", results = { @Result(name = NEW,type = "dispatcher") })
	public String searchResults(){
		
		super.search();
		searchResult.getList();
		setSearchMode("result");
		return NEW;
	}

	@Override
	public void validate(){
		
	}

	protected String getMessage(String key)
	{
		return getText(key);
	}

@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder dynQuery = new StringBuilder(800);
		dynQuery.append( " from AutoDcrExtn auto where auto.id is not null and not exists (select 1 from RegnAutoDcrExtn regnautodcr, RegistrationExtn reg" +
				" where auto.autoDcrNum=regnautodcr.autoDcrNum and reg.id=regnautodcr.registration.id and reg.egwStatus.moduletype='NEWBPAREGISTRATION' and " +
				" reg.egwStatus.code not in('"+BpaConstants.NEWBPACANCELLEDSTATUS+"','"+BpaConstants.REJECTIONAPPROVED+"')) ") ;
      
		if(StringUtils.isNotBlank(searchAutoDcr.getAutoDcrNum()))
		{
			dynQuery.append(" and lower(auto.autoDcrNum) like ?  ");
			paramList.add("%"+searchAutoDcr.getAutoDcrNum().toLowerCase()+"%");  
		}
		dynQuery.append(" order by id");
		setPageSize(15);
		String regSearchQuery=" select distinct auto  "+	dynQuery;
		String countQuery = " select count(distinct auto)  " + dynQuery;
		return new SearchQueryHQL(regSearchQuery, countQuery, paramList);
		
	}
  
   
	public String getInitialSearch() {
		return initialSearch;
	}

	public void setInitialSearch(String initialSearch) {
		this.initialSearch = initialSearch;
	}

	

	public String getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

	
	public AutoDcrExtn getSearchAutoDcr() {
		return searchAutoDcr;
	}

	public void setSearchAutoDcr(AutoDcrExtn searchAutoDcr) {
		this.searchAutoDcr = searchAutoDcr;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAutonum() {
		return autonum;
	}

	public void setAutonum(String autonum) {
		this.autonum = autonum;
	}

	
	
}
