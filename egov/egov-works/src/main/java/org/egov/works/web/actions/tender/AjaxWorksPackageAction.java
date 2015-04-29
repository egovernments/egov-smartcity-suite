package org.egov.works.web.actions.tender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.models.Money;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksPackageService;

public class AjaxWorksPackageAction extends BaseFormAction {
	private static final String ESTIMATE_LIST = "estList";
	private List<AbstractEstimate> abstractEstimateList=new ArrayList<AbstractEstimate>();
	private AbstractEstimateService abstractEstimateService;
	private Money worktotalValue;
	private String estId;
	private String wpId;
	
	private WorksPackage worksPackage=new WorksPackage(); 
	
	
	private static final String TENDERFILENUMBERUNIQUECHECK = "tenderFileNumberUniqueCheck";
	private Long id;
	private String tenderFileNumber;
	private WorksPackageService workspackageService;
	private static final String TENDER_RESPONSE_CHECK = "tenderResponseCheck";
	private boolean tenderResponseCheck;
	private String tenderNegotiationNo;
	private String query = "";
	private List<WorksPackage> wpList = new LinkedList<WorksPackage>();
	private static final String WP_NUMBER_SEARCH_RESULTS = "wpNoSearchResults";
	private static final String TENDER_FILE_NUMBER_SEARCH_RESULTS = "tenderFileNoSearchResults";
	private String mode ;
	private List<String> estimateNumberSearchList = new LinkedList<String>();
	private static final String ESTIMATE_NUMBER_SEARCH_RESULTS = "estimateNoSearchResults";
	
	
	
	
	public String estimateList()
	{
		if(StringUtils.isNotBlank(estId)){
			abstractEstimateList=abstractEstimateService.getAbEstimateListById(estId);
			setWorktotalValue(abstractEstimateService.getWorkValueIncludingTaxesForEstList(abstractEstimateList));
		}
		return ESTIMATE_LIST;
	}
	
	public String tenderFileNumberUniqueCheck(){
		return TENDERFILENUMBERUNIQUECHECK;
	}
	
	public boolean getTenderFileNumberCheck() {
		boolean tenderFileNoexistsOrNot = false;
		Long wpId=null;
		if(id==null || id==0) {
			if(getPersistenceService().findByNamedQuery("TenderFileNumberUniqueCheck",tenderFileNumber)!=null)
				wpId=(Long) getPersistenceService().findByNamedQuery("TenderFileNumberUniqueCheck",tenderFileNumber);
		}else{
			if(getPersistenceService().findByNamedQuery("TenderFileNumberUniqueCheckForEdit",tenderFileNumber,id)!=null)
				wpId=(Long) getPersistenceService().findByNamedQuery("TenderFileNumberUniqueCheckForEdit",tenderFileNumber,id);
		}
		
		if(wpId!=null)
			worksPackage= workspackageService.findById(wpId, false);
			
		if(worksPackage!=null && worksPackage.getId()!=null)
			tenderFileNoexistsOrNot=true;
		
		return tenderFileNoexistsOrNot;
	}
	
	public String isTRPresentForWPCheck()
	{
		tenderResponseCheck =false;
		tenderNegotiationNo="";
		if(!StringUtils.isEmpty(wpId))
		{
			String query = "from TenderResponse tr where tr.tenderEstimate.worksPackage.id=? and tr.egwStatus.code!='CANCELLED'";
			List<Object> paramList = new ArrayList<Object>();
			paramList.add(Long.valueOf(wpId));	
			List<TenderResponse> trList = getPersistenceService().findAllBy(query, paramList.toArray());
			if(trList!=null && trList.size()>0)
			{
				tenderResponseCheck=true;
				tenderNegotiationNo = trList.get(0).getNegotiationNumber();
			}
		}
		return TENDER_RESPONSE_CHECK;
	}
	
	public String searchWorksPackageNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			if(mode!=null && mode.equalsIgnoreCase("cancelWP"))
			{
				strquery="from WorksPackage as wp where wp.wpNumber like '%'||?||'%' and wp.egwStatus.code=? " ;
				params.add(query.toUpperCase());
				params.add("APPROVED");
			}
			else
			{
				strquery="from WorksPackage as wp where wp.wpNumber like '%'||?||'%' and wp.egwStatus.code<>? " ;
				params.add(query.toUpperCase());
				params.add("NEW");
			}
			
			wpList = getPersistenceService().findAllBy(strquery,params.toArray());
		}
		return WP_NUMBER_SEARCH_RESULTS;
	}
	
	public String searchTenderFileNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
				strquery="from WorksPackage as wp where wp.tenderFileNumber like '%'||?||'%' and wp.egwStatus.code<>?" ;
				params.add(query.toUpperCase());
				params.add("NEW");
				wpList = getPersistenceService().findAllBy(strquery,params.toArray());
		}
		return TENDER_FILE_NUMBER_SEARCH_RESULTS;
	}
	
	public String searchEstimateNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct(wpd.estimate.estimateNumber) from WorksPackageDetails wpd where wpd.worksPackage.egwStatus.code<>? and wpd.estimate.estimateNumber like '%'||?||'%' ";
			params.add("NEW");
			params.add(query.toUpperCase());
			estimateNumberSearchList = getPersistenceService().findAllBy(strquery,params.toArray());
		}
		return ESTIMATE_NUMBER_SEARCH_RESULTS;
	}

	public Money getWorktotalValue() {
		return worktotalValue;
	}

	public void setWorktotalValue(Money worktotalValue) {
		this.worktotalValue = worktotalValue;
	}

	public String getEstId() {
		return estId;
	}

	public void setEstId(String estId) {
		this.estId = estId;
	}

	public Object getModel() {
		return null;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public List<AbstractEstimate> getAbstractEstimateList() {
		return abstractEstimateList;
	}

	public void setAbstractEstimateList(List<AbstractEstimate> abstractEstimateList) {
		this.abstractEstimateList = abstractEstimateList;
	}

	public WorksPackage getWorksPackage() {
		return worksPackage;
	}

	public void setWorksPackage(WorksPackage worksPackage) {
		this.worksPackage = worksPackage;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTenderFileNumber() {
		return tenderFileNumber;
	}

	public void setTenderFileNumber(String tenderFileNumber) {
		this.tenderFileNumber = tenderFileNumber;
	}

	public void setWorkspackageService(WorksPackageService workspackageService) {
		this.workspackageService = workspackageService;
	}

	public void setWpId(String wpId) {
		this.wpId = wpId;
	}

	public boolean getTenderResponseCheck() {
		return tenderResponseCheck;
	}

	public String getTenderNegotiationNo() {
		return tenderNegotiationNo;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<WorksPackage> getWpList() {
		return wpList;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<String> getEstimateNumberSearchList() {
		return estimateNumberSearchList;
	}

}
