package org.egov.works.web.actions.reports;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.estimate.AbstractEstimate;

public class AjaxRetentionMoneyRecoveryRegisterAction extends BaseFormAction {
	private static final Logger logger = Logger.getLogger(AjaxRetentionMoneyRecoveryRegisterAction.class);

	private static final String ESTIMATE_NUMBER_SEARCH_RESULTS = "estimateNoSearchResults";	
	private static final String PROJECT_CODE_SEARCH_RESULTS = "projectCodeSearchResults";
	private static final String CONTRACTOR_CODE_SEARCH_RESULTS = "contractorCodeSearchResults";
	
	private String query;
	private List<String> estimateNumberSearchList = new LinkedList<String>();
	private List<String> projectCodeList = new LinkedList<String>();
	private List<String> contractorCodeNameList = new LinkedList<String>();
	
	public String execute(){
		return SUCCESS;
	}

	public Object getModel() {
		return null;
	}
		
	/*
	 * Autocomplete for estimates where bills are created
	 */
	public String searchEstimateNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct(ae.estimateNumber) from AbstractEstimate ae where ae.parent is null and ae.projectCode.id in " +
					"(select bpd.accountDetailKeyId from EgBillPayeedetails bpd where bpd.accountDetailTypeId = (" +
					" select id from Accountdetailtype where name='PROJECTCODE') and bpd.egBilldetailsId.egBillregister.status.code=? " +
					" and expendituretype='Works' ) " +
					"and UPPER(ae.estimateNumber) like '%'||?||'%' and ae.egwStatus.code = ? )";			
			params.add(ContractorBillRegister.BillStatus.APPROVED.toString());
			params.add(query.toUpperCase());
			params.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
			
			estimateNumberSearchList = persistenceService.findAllBy(strquery,params.toArray());
		}
		return ESTIMATE_NUMBER_SEARCH_RESULTS;
	}
	
	/*
	 * Autocomplete of Project codes where bills are created
	 */
	public String searchProjectCode(){
		if(!StringUtils.isEmpty(query)) {
			String strquery="";
			ArrayList<Object> params=new ArrayList<Object>();
			strquery="select pc.code from ProjectCode pc where upper(pc.code) like '%'||?||'%'"+
					" and pc.id in (select bpd.accountDetailKeyId from EgBillPayeedetails bpd where bpd.accountDetailTypeId = (" +
					" select id from Accountdetailtype where name='PROJECTCODE') and bpd.egBilldetailsId.egBillregister.status.code=? " +
					" and expendituretype='Works')";
			params.add(query.toUpperCase());
			params.add(ContractorBillRegister.BillStatus.APPROVED.toString());
			projectCodeList = getPersistenceService().findAllBy(strquery,params.toArray());
		}	
		return PROJECT_CODE_SEARCH_RESULTS;
	}
	
	/*
	 * Autocomplete of Contractor Code/Names where bills are created
	 */
	public String searchContractors(){
		if(!StringUtils.isEmpty(query)) {
			String strquery="";
			ArrayList<Object> params=new ArrayList<Object>();
			strquery="select cont.code||'~'||cont.name from Contractor cont where upper(cont.code) like '%'||?||'%' or upper(cont.name) like '%'||?||'%'"+
					" and cont.id in (select bpd.accountDetailKeyId from EgBillPayeedetails bpd where bpd.accountDetailTypeId = (" +
					" select id from Accountdetailtype where name='contractor') and bpd.egBilldetailsId.egBillregister.status.code=? " +
					" and expendituretype='Works')";
			params.add(query.toUpperCase());
			params.add(query.toUpperCase());
			params.add(ContractorBillRegister.BillStatus.APPROVED.toString());
			contractorCodeNameList = getPersistenceService().findAllBy(strquery,params.toArray());
		}	
		return CONTRACTOR_CODE_SEARCH_RESULTS;
	}
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<String> getEstimateNumberSearchList() {
		return estimateNumberSearchList;
	}

	public List<String> getProjectCodeList() {
		return projectCodeList;
	}

	public List<String> getContractorCodeNameList() {
		return contractorCodeNameList;
	}
}