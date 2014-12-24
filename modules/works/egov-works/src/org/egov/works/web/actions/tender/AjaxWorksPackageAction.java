package org.egov.works.web.actions.tender;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.models.Money;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksPackageService;

public class AjaxWorksPackageAction extends BaseFormAction {
	private static final String ESTIMATE_LIST = "estList";
	private List<AbstractEstimate> abstractEstimateList=new ArrayList<AbstractEstimate>();
	private AbstractEstimateService abstractEstimateService;
	private Money worktotalValue;
	private String estId;
	
	private WorksPackage worksPackage=new WorksPackage(); 
	
	
	private static final String TENDERFILENUMBERUNIQUECHECK = "tenderFileNumberUniqueCheck";
	private Long id;
	private String tenderFileNumber;
	private WorksPackageService workspackageService;
	
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
}
