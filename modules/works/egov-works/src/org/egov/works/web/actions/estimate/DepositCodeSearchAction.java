package org.egov.works.web.actions.estimate;

import java.util.Collection;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.masters.DepositCode;

import com.opensymphony.xwork2.Action;

@Result(name=Action.SUCCESS, type="redirect", location = "depositCodeSearch-searchResults")  

@ParentPackage("egov")  

public class DepositCodeSearchAction extends BaseFormAction {
	private PersistenceService<DepositCode,Long> depositCodeService;  
	private static final String SEARCH_RESULTS = "searchResults";
	private String query;
	private Integer fundId;
	
	public void setQuery(String query) {
		this.query = query;
	}

	public String searchAjax(){
		return SEARCH_RESULTS;
	}

	public Object getModel() {
		return null;
	}


	public Collection<DepositCode> getDepositCodeList() {
		return depositCodeService.findAllBy("from DepositCode where isActive=1 and fund.id=? and upper(code) like ? || '%'", fundId, query.toUpperCase());
	}


	public void setDepositCodeService(
			PersistenceService<DepositCode, Long> depositCodeService) {
		this.depositCodeService = depositCodeService;
	}

	public Integer getFundId() {
		return fundId;
	}

	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}


}
