package org.egov.payroll.web.actions.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.Action;

@Result(name=Action.SUCCESS, type="redirect", location = "glCodeSearch-searchResults")  
@ParentPackage("egov")  
public class GlCodeSearchAction extends BaseFormAction{
	
	private PersistenceService<CChartOfAccounts,Long> chartOfAccountPersistenceService;  
	private static final String SEARCH_RESULTS = "searchResults";
	private String query;
	private String accTypes;
	
	public void setAccTypes(String accTypes) {
		this.accTypes = accTypes;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String glCodeSearch(){
		return SEARCH_RESULTS;
	}

	public Object getModel() {
		return null;
	}

	public List<CChartOfAccounts> getGlCodeList() {
		String tmpQuery ="";
		
		if(StringUtils.isNotBlank(query)){
			tmpQuery ="from CChartOfAccounts coa where coa.isActiveForPosting=1 and coa.classification='4' and coa.glcode like ? || '%' ";
			
			if(StringUtils.isNotBlank(accTypes)){ 
				String[] accType= accTypes.split(",");
				tmpQuery = tmpQuery +" and ( ";
				for(int i=0;i<accType.length;i++){
					if(i==0){
						tmpQuery = tmpQuery +" coa.type = '"+accType[i]+"' ";
					}else
						tmpQuery = tmpQuery +" or coa.type = '"+accType[i]+"' ";
				}
				tmpQuery = tmpQuery +" ) ";
			}
			tmpQuery = tmpQuery +" order by coa.glcode ";
			return chartOfAccountPersistenceService.findAllBy(tmpQuery,query );
		}
		else	
			return new ArrayList<CChartOfAccounts>();
	}

	public void setChartOfAccountPersistenceService(
			PersistenceService<CChartOfAccounts, Long> chartOfAccountPersistenceService) {
		this.chartOfAccountPersistenceService = chartOfAccountPersistenceService;
	}

}
