package org.egov.erpcollection.web.actions.receipts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.egov.commons.Bank;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.Action;

@Result(name=Action.SUCCESS, type="redirect", location = "bankSearch-searchResults")  

@ParentPackage("egov")  

public class BankSearchAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private PersistenceService<Bank,Integer> bankService;  
	private static final String SEARCH_RESULTS = "searchResults";
	private final Bank bank = new Bank();
	private List<Bank> bankList = new ArrayList<Bank>();
	private String query;
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String searchAjax(){
		return SEARCH_RESULTS;
	}

	public Object getModel() {
		return bank;
	}

	public void setBankService(
			PersistenceService<Bank, Integer> bankService) {
		this.bankService = bankService;
	}

	public Collection<Bank> getBankList() {
		if(StringUtils.isNotBlank(getQuery()))
		bankList = bankService.findAllBy("from Bank where upper(name) like ? || '%'",getQuery().toUpperCase());
		return bankList;
	}
	
}

