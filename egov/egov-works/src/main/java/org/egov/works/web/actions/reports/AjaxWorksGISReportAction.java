package org.egov.works.web.actions.reports;
/**
 * 
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.web.actions.BaseFormAction;
import org.springframework.beans.factory.annotation.Autowired;

public class AjaxWorksGISReportAction extends BaseFormAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Boundary> wardList;
	private static final String WARDS = "wards";
	private Long zoneId;
	private String query;
	private List<String> estimateNumberSearchList = new LinkedList<String>();
	private static final String ESTIMATE_NUMBER_SEARCH_RESULTS = "estimateNoSearchResults";
	@Autowired
	private BoundaryService boundaryService;

	public String ajaxLoadWardsByZone()
	{
		wardList = boundaryService.getAllBoundariesByBoundaryTypeId(zoneId);
		return WARDS;
	}
	
	/*
	 * Autocomplete of Admin sanctioned Estimate nos for Cancel Estimate screen 
	 */
	public String searchEstimateNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct(ae.estimateNumber) from AbstractEstimate ae where ae.parent is null and UPPER(ae.estimateNumber) like '%'||?||'%' " +
			" and ae.egwStatus.code != 'NEW' )";
			params.add(query.toUpperCase());
			
			estimateNumberSearchList = getPersistenceService().findAllBy(strquery,params.toArray());
		}
		return ESTIMATE_NUMBER_SEARCH_RESULTS;
	}
	
	public List<Boundary> getWardList() {
		return wardList;
	}

	public void setWardList(List<Boundary> wardList) {
		this.wardList = wardList;
	}

	@Override
	public Object getModel() {
		return null;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<String> getEstimateNumberSearchList() {
		return estimateNumberSearchList;
	}
}