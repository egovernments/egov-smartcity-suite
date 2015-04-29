package org.egov.works.web.actions.masters;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.masters.MarketRate;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.utils.WorksConstants;

public class AjaxScheduleOfRateAction extends BaseFormAction implements ServletResponseAware{

	private static final Logger logger = Logger.getLogger(AjaxScheduleOfRateAction.class);
	private static final String codeNumberUniqueCheck = "codeNumberUniqueCheck";
	private PersistenceService<ScheduleOfRate,Long> scheduleOfRateService;  
	private HttpServletResponse response;
	private static final String MARKETRATEASONDATE = "marketRateAsOnDate";
	private Date asOnDate;
	private Long scheduleId;
	private String scheduleIds;
	private double marketRateValue=0;
	private double[] marketRateValues;
	private String estimatesExists;
	//TODO - Move this to WorksConstants
	private static final String YES = "yes";
	private static final String NO = "no";
	
	public double[] getMarketRateValues() {
		return marketRateValues;
	}

	public String getScheduleIds() {
		return scheduleIds;
	}

	public void setScheduleIds(String scheduleIds) {
		this.scheduleIds = scheduleIds;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	

	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}

	public double getMarketRateValue() {
		return marketRateValue;
	}

	private String codeNo;
	private Long categoryTypeId;

	public String codeNumberUniqueCheck(){
		return codeNumberUniqueCheck;
	}
	
	public boolean getCodenoCheck(){
		ScheduleOfRate scheduleOfRate = null;
		boolean codeNoexistsOrNot = false;
		scheduleOfRate =scheduleOfRateService.find("from ScheduleOfRate sor where sor.code=? and sor.category.id=?",codeNo,categoryTypeId);
		if(scheduleOfRate!=null) {
			codeNoexistsOrNot=true;
		}
		return codeNoexistsOrNot;
	}
	
	public String getByResponseAware() {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        try {
        	response.getWriter().write("From Action "+Calendar.getInstance().getTime());
        }catch (IOException ioex){
        	logger.info("Error while writing to response --from getByResponseAware()");
        }
		
		return null;
	}
	
	public String getByDirectResponse() {
        getResponse().setContentType("text/xml");
        getResponse().setHeader("Cache-Control", "no-cache");

        try {
        	getResponse().getWriter().write("From Action "+Calendar.getInstance().getTime());
        }catch (IOException ioex){
        	logger.info("Error while writing to response --from getByResponseAware()");
        }
		return null;
	}
		
	public String getMarketValueAsOnDate(){
		String[] schId=scheduleIds.split("~");
		int count=0;
		List<Long> params=new LinkedList<Long>();
		for(int i=0; i<schId.length;i++){			
			params.add(Long.valueOf(schId[i]));			
		}
		marketRateValues=new double[schId.length];
		List<ScheduleOfRate> scheduleOfRatesList =scheduleOfRateService.findAllByNamedQuery("SCHEDULEOFRATES_BY_IDS",params);
		
		for(int j=0;j<schId.length;j++) { 
			for(ScheduleOfRate scheduleOfRate:scheduleOfRatesList) {
				if(schId[j].equals(scheduleOfRate.getId().toString())) {
					marketRateValue=0.0;
					if(scheduleOfRate.hasValidMarketRateFor(asOnDate)){		
						MarketRate marketRate= scheduleOfRate.getMarketRateOn(asOnDate);
						marketRateValue=marketRate.getMarketRate().getValue();			
					}			
					marketRateValues[count]=marketRateValue;
					count++;
				} 
			}
		}
		return MARKETRATEASONDATE;
	}
	
	public String checkIfEstimateExists(){
		List<Long> ids = persistenceService.findAllBy("select distinct act.abstractEstimate.id from Activity act where act.schedule.id = ? and act.abstractEstimate.rateContract.id is not null and act.abstractEstimate.egwStatus.code != 'CANCELLED'", scheduleId);
		if(ids!=null && !ids.isEmpty())
			estimatesExists = WorksConstants.YES;
		else
			estimatesExists = WorksConstants.NO;

		return "checkEstimatesForSOR";
	}
	
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setCodeNo(String codeNo) {
		this.codeNo = codeNo;
	}
	
	public void setCategoryTypeId(Long categoryTypeId) {
		this.categoryTypeId = categoryTypeId;
	}

	public void setScheduleOfRateService(
			PersistenceService<ScheduleOfRate, Long> scheduleOfRateService) {
		this.scheduleOfRateService = scheduleOfRateService;
	}
	
	/**
     * Convenience method to get the request
     *
     * @return current request
     */
    protected HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    /**
     * Convenience method to get the response
     *
     * @return current response
     */
    protected HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public String getEstimatesExists() {
		return estimatesExists;
	}

	public void setEstimatesExists(String estimatesExists) {
		this.estimatesExists = estimatesExists;
	}
}
