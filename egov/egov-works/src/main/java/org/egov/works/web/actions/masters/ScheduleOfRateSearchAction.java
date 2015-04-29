package org.egov.works.web.actions.masters;

import java.util.Collection;
import java.util.Date;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.masters.Rate;
import org.egov.works.models.masters.ScheduleOfRate;

import com.opensymphony.xwork2.Action;

@Result(name=Action.SUCCESS, type="ServletRedirectResult.class", location = "scheduleOfRateSearch-searchResults")  

@ParentPackage("egov")  
public class ScheduleOfRateSearchAction extends BaseFormAction {
	private PersistenceService<ScheduleOfRate,Long> scheduleOfRateService;  
	//private static final String PUNCTUATIONS_AND_SPECIALCHARS = "[^\\w\\d\\.]";
	private static final String SEARCH_RESULTS = "searchResults";
	private ScheduleOfRate sor = new ScheduleOfRate();
	private Rate currentRate;
	private String sorID;
	private Date estimateDate;
	private String query;	
	private String scheduleCategoryId;
	private String estimateId;

	public void setScheduleCategoryId(String scheduleCategoryId) {
		this.scheduleCategoryId = scheduleCategoryId;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String searchAjax(){
		return SEARCH_RESULTS;
	}
	public String findSORAjax(){
		sor = scheduleOfRateService.findById(Long.parseLong(sorID), false);
		if(estimateDate!=null) 
		currentRate = sor.getRateOn(estimateDate);
		return "SOR";
	}

	public Object getModel() {
		return sor;
	}

	public void setScheduleOfRateService(
			PersistenceService<ScheduleOfRate, Long> scheduleOfRateService) {
		this.scheduleOfRateService = scheduleOfRateService;
	}

	public Collection<ScheduleOfRate> getScheduleOfRateList() {
	    if(estimateId!=null && estimateDate!=null)
                return scheduleOfRateService.findAllByNamedQuery("SCHEDULEOFRATES_SEARCH_REVISIONESTIMATE", query, query, Long.valueOf(scheduleCategoryId), estimateDate, estimateDate,Long.valueOf(estimateId),Long.valueOf(estimateId));
            else if(estimateDate!=null)
                return scheduleOfRateService.findAllByNamedQuery("SCHEDULEOFRATES_SEARCH", query, query, Long.valueOf(scheduleCategoryId), estimateDate, estimateDate);                 
            else
                return scheduleOfRateService.findAllByNamedQuery("SCHEDULEOFRATES_SEARCH_ESTIMATETEMPLATE", query, query, Long.valueOf(scheduleCategoryId));
	
	}


	public void setSorID(String sorID) {
		this.sorID = sorID;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	public ScheduleOfRate getSor() {
		return sor;
	}

	public void setSor(ScheduleOfRate scheduleOfRateInstance) {
		this.sor = scheduleOfRateInstance;
	}

	public Rate getCurrentRate() {
		return currentRate;
	}

	public String getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(String estimateId) {
		this.estimateId = estimateId;
	}

}
