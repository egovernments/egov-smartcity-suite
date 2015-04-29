package org.egov.works.web.actions.reports;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.SubScheme;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.masters.Contractor;

public class AjaxWorkProgressAction  extends BaseFormAction {

	private Integer schemeId;
	private List<SubScheme> subSchemes;
	private static final String SUBSCHEMES = "subschemes";
	private String query;
	private List<Contractor> contractorList = new LinkedList<Contractor>();
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public AjaxWorkProgressAction()
	{
		
	}
	

	public String loadSubSchemes() {
		subSchemes = getPersistenceService().findAllBy("from org.egov.commons.SubScheme where scheme.id=?", schemeId);
		return SUBSCHEMES;
	}
//TODO: check only for approved work orders
	public String searchAllContractorsForWorkOrder(){
		if(!StringUtils.isEmpty(query))
		{
			StringBuilder strquery=new StringBuilder(300);
			ArrayList<Object> params=new ArrayList<Object>();
			strquery.append("select distinct(woe.workOrder.contractor) from WorkOrderEstimate woe where upper(woe.workOrder.contractor.name) like '%'||?||'%'" );
			strquery.append(" or upper(woe.workOrder.contractor.code) like '%'||?||'%'");
			strquery.append(" and woe.workOrder.egwStatus.code='APPROVED'");
			params.add(query.toUpperCase());
			params.add(query.toUpperCase());
			contractorList = getPersistenceService().findAllBy(strquery.toString(), params.toArray());
		}	
		return "contractorNameSearchResults";
	}
	
	public Integer getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}

	public List<SubScheme> getSubSchemes() {
		return subSchemes;
	}

	public void setSubSchemes(List<SubScheme> subSchemes) {
		this.subSchemes = subSchemes;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<Contractor> getContractorList() {
		return contractorList;
	}

	public void setContractorList(List<Contractor> contractorList) {
		this.contractorList = contractorList;
	}
	
}
