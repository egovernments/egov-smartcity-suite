package org.egov.erpcollection.web.actions.receipts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.services.ReceiptHeaderService;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.Action;

@Result(name=Action.SUCCESS, type="redirect", location = "receiptNumberSearch-searchResults")  

@ParentPackage("egov")  

public class ReceiptNumberSearchAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private ReceiptHeaderService receiptHeaderService;   
	private static final String SEARCH_RESULTS = "searchResults";
	private List<ReceiptHeader> receiptNumberList = new ArrayList<ReceiptHeader>();
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
		return null;
	}

	
	public Collection<ReceiptHeader> getReceiptNumberList() {
		if(StringUtils.isNotBlank(query))
			receiptNumberList = receiptHeaderService.findAllBy("from org.egov.erpcollection.models.ReceiptHeader where upper(receiptnumber) like '%' || ? || '%'",query.toUpperCase());
		return receiptNumberList;
	}


	/**
	 * @param receiptHeaderService the receiptHeaderService to set
	 */
	public void setReceiptHeaderService(ReceiptHeaderService receiptHeaderService) {
		this.receiptHeaderService = receiptHeaderService;
	}
}

