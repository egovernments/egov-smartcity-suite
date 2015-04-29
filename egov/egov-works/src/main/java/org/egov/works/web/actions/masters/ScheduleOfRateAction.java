package org.egov.works.web.actions.masters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.core.security.user.UserImpl;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.Period;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.masters.MarketRate;
import org.egov.works.models.masters.Rate;
import org.egov.works.models.masters.ScheduleCategory;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.utils.WorksConstants;

import com.opensymphony.xwork2.Action;

@Result(name = Action.SUCCESS, type = "ServletRedirectResult.class", location = "scheduleOfRate.action")
@ParentPackage("egov")
public class ScheduleOfRateAction extends SearchFormAction {
	private PersistenceService<ScheduleOfRate, Long> scheduleOfRateService;
	private ScheduleOfRate scheduleOfRate = new ScheduleOfRate();
	private List<ScheduleOfRate> scheduleOfRateList = null;
	private static final List<ScheduleCategory> scheduleCategoryList = null;
	private Long id;
	private String messageKey;
	private String mode;
	private String displData;

	@Required(message = "sor.category.not.null")
	private Long categry = -1l;
	private String code;
	private String description;
	
	/* on  9/10/09 */
	private Map<Long, String> deletFlagMap = new HashMap<Long, String>();	
	private Map<Long, String> deleteFlagMap2 = new HashMap<Long, String>();
	private String estimateDtFlag = "no";
	private Date estimateDate;
	public static final String flagValue="yes";
	
	private List<Rate> actionRates = new LinkedList<Rate>();
	private List<MarketRate> actionMarketRates=new LinkedList<MarketRate>();
	
	private List abstractEstimateList=null;
	private List woeList =null;
	private Date woDate;
	private String woDateFlag = "no";
	private List<Rate> editableRateList=new ArrayList<Rate>();
	
	public ScheduleOfRateAction() {
		addRelatedEntity("category", ScheduleCategory.class);
		addRelatedEntity("uom", EgUom.class);
		addRelatedEntity("createdBy", UserImpl.class);
	}

	public String execute() {
		return list();
	}

	public String newform() {
		return NEW;
	}

	/*
	 * sept2309 @return searchpage
	 */
	public String searchList() {
		setDisplData("no");
		scheduleOfRateList = scheduleOfRateService.findAllBy(" from ScheduleOfRate sor order by code asc");
		return "searchpage";
	}

	public String list() {
		scheduleOfRateList = scheduleOfRateService.findAllBy(" from ScheduleOfRate sor order by code asc");
		return INDEX;
	}
	
	public String edit() {		
		 scheduleOfRate = (ScheduleOfRate)scheduleOfRateService.findById(scheduleOfRate.getId(), false);
		 if(StringUtils.isNotBlank(mode) && mode.equalsIgnoreCase("view"))
			 return "edit";
		 getRateDetailsForSORId(false);	
		 getRateDetailsForSORIdForREValidation(false);
		 return "edit";
	}

	public String save() {
		scheduleOfRateService.update(scheduleOfRate);
		return SUCCESS;
	}
	
	public String create() {		
		populateRates();
	    populateMarketRates();	
	    getPersistedRateDetails(scheduleOfRate);
	    getRateDetailsForSORId(true); 
	    getRateDetailsForSORIdForREValidation(true);
	 	      
	    if(scheduleOfRate.getId()==null)
	    	scheduleOfRateService.persist(scheduleOfRate);
	    else{
	    	scheduleOfRateService.merge(scheduleOfRate);
	    }
	    scheduleOfRate=(ScheduleOfRate)scheduleOfRateService.findById(scheduleOfRate.getId(), false);
		scheduleOfRateList=new ArrayList<ScheduleOfRate>();
		scheduleOfRateList.add(scheduleOfRate);
		messageKey = "sor.save.success";
		addActionMessage(getText(messageKey, "The SOR was saved successfully"));
		//return list();
		return INDEX;
	}
	
	 protected void populateRates() {
		 for(Rate rate: actionRates) {
			 if (validRate(rate)) {				 
				 scheduleOfRate.addRate(rate);
			 }
		 }
	 }
	 
	 protected boolean validRate(Rate rate){		
		 if (rate != null){			 
			 return true;
		 }
		 return false;
	 }
	 
	 protected void populateMarketRates(){
		 for(MarketRate marketRate: actionMarketRates) {
			 if (validMarketRate(marketRate)){
				 scheduleOfRate.addMarketRate(marketRate);
			 }
		 }
	 }
	 
	 protected boolean validMarketRate(MarketRate marketRate){
		 if (marketRate != null){
	        return true;
		 }
		 return false;
	 }
	
	public Object getModel() {
		return scheduleOfRate;
	}

	public Collection<ScheduleOfRate> getScheduleOfRateList() {
		return scheduleOfRateList;
	}

	public void setScheduleOfRateService(
		PersistenceService<ScheduleOfRate, Long> service) {
		this.scheduleOfRateService = service;
	}

	public void prepare() {
		if (id != null) {
			scheduleOfRate = scheduleOfRateService.findById(id, false);
		}
		super.prepare();
		setupDropdownDataExcluding();
		List<ScheduleOfRate> categories = (List<ScheduleOfRate>) scheduleOfRateService.findAllBy("from ScheduleCategory sc");
		addDropdownData("categorylist", categories);
		addDropdownData("uomlist", scheduleOfRateService.findAllBy("from EgUom  order by upper(uom)"));
	}

	private void getPersistedRateDetails(ScheduleOfRate sor){
		List<Rate> rateList=null;		
		 rateList = scheduleOfRate.getRates();
		
		 for(Rate rt : rateList){
			 if(rt.getId()!=null){
				 editableRateList.add(rt);
			 }
		 }
	}	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ScheduleOfRate getScheduleOfRate() {
		return scheduleOfRate;
	}

	public void setScheduleOfRate(ScheduleOfRate scheduleOfRate) {
		this.scheduleOfRate = scheduleOfRate;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * prashanth
	 * 
	 * @return the displData
	 */
	public String getDisplData() {
		return displData;
	}

	/**
	 * @param displData
	 *            the displData to set
	 */
	public void setDisplData(String displData) {
		this.displData = displData;
	}

	public List<ScheduleCategory> getScheduleCategoryList() {
		// scheduleCategoryList= scheduleCategoryService.findAllBy(" from
		// ScheduleCategory sc order by code asc");
		// System.out.println("$$$$$$$$$$$inside action
		// getScheduleCategoryList");
		return scheduleCategoryList;
	}

	/**
	 * @return the categry
	 * @Validation
	 * @RequiredStringValidator(message="Please select a category")
	 */
	public Long getCategry() {
		return categry;
	}

	/**
	 * @param categry
	 *            the categry to set
	 */
	public void setCategry(Long categry) {
		this.categry = categry;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the sorCode to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public String searchSorDetails() {
		if(categry == -1){
			messageKey = "sor.category.not.null";
			addActionError(getText(messageKey, "Please Select Category"));			
			return "searchpage";
		}else {
			setPageSize(WorksConstants.PAGE_SIZE);
			search();
		} 
		if (searchResult.getFullListSize()==0) {
			setDisplData("noData");
		}else{
			setDisplData(flagValue);
		}
		
		return "searchpage";
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	

	/*
	 * getRateDetailsForSORId
	 */	
	 public void getRateDetailsForSORId(boolean validationMessageRequired){		 
		 if(scheduleOfRate.getId()!=null && validationMessageRequired){
			 if(!editableRateList.isEmpty()){
				 iterateRateList(editableRateList,validationMessageRequired);
	         } //end of rate not null  
		 }
	 }
	 
	 public void getRateDetailsForSORIdForREValidation(boolean validationMessageFlag){		 
		if(scheduleOfRate.getId()!=null && validationMessageFlag){
			if(!editableRateList.isEmpty()){
				iterateRateListForRE(editableRateList,validationMessageFlag);
		 	} //end of rate not null   
		 }
	 }
	 
	
	 public void iterateRateList(List<Rate> rateList,boolean validationMessageRequired){		
		 abstractEstimateList = getPersistenceService().findAllBy("select ae from AbstractEstimate ae, Activity act where act.abstractEstimate=ae and act.abstractEstimate.parent is null and act.abstractEstimate.egwStatus.code <> 'CANCELLED' and act.schedule.id = "+ scheduleOfRate.getId());
		  Rate rate = rateList.get(rateList.size()-1);
	 	     if(!abstractEstimateList.isEmpty()){   	
	 	    	 iterateAbstractList(abstractEstimateList,rate,validationMessageRequired);
	 	     }
	 }
	 
	 public void iterateRateListForRE(List<Rate> rateList,boolean validationMessageFlag){
		woeList = getPersistenceService().findAllBy("select distinct(woa.workOrderEstimate) from WorkOrderActivity woa where woa.workOrderEstimate.estimate.parent.id is not null and woa.workOrderEstimate.estimate.egwStatus.code<> 'CANCELLED' and exists (select sor.id from ScheduleOfRate sor where sor.id = woa.activity.schedule.id and sor.id = ? )", scheduleOfRate.getId());
		 Rate rate = rateList.get(rateList.size()-1);
	 	     if(!woeList.isEmpty()){   	
				iterateWOList(woeList,rate,validationMessageFlag);
			}
	 }
	 
	 public void iterateAbstractList(List abstractEstimateList,Rate rate,boolean validationMessageRequired){
		 AbstractEstimate abstractEstimate = null;
		 Map<Integer,String> trackFlagMap=new HashMap<Integer,String>();
		 for(int i = 0; i < abstractEstimateList.size(); i++){
               abstractEstimate = (AbstractEstimate)abstractEstimateList.get(i);
               if(abstractEstimate != null){	                	
                   estimateDate = abstractEstimate.getEstimateDate();
                   if(rate != null){	
                   	Period validity = rate.getValidity();
                   	Date startDate = validity.getStartDate();	                                
                   	Date endDate = null;
                   	if(validity.getEndDate()!=null){	                                
                   		endDate=validity.getEndDate();	                                	
                   	}                                
                   	boolean flag = false;
                   	if(startDate!=null && rate.getId()!=null){
                   		flag=checkGivenDateWithinRange(estimateDate, startDate, endDate);
                   	}
                   	if(flag){     	                    		
                   		deletFlagMap.put(rate.getId(), flagValue);
                   		setEstimateDtFlag(flagValue);
                   		trackFlagMap.put(0, flagValue);
                   		if(validationMessageRequired){
                   			throw new ValidationException(Arrays.asList(new ValidationError("sor.estimateDate.overlap.error",getText("sor.estimateDate.overlap.error"))));
                   		}
                   	}else {
                   		if(!trackFlagMap.isEmpty()){
                   			String value=(String)trackFlagMap.get(0);
                   			if(value!=null && !value.equalsIgnoreCase(flagValue)){     	                    				
                   				deletFlagMap.put(rate.getId(), "no");
                   			}
                   		}
                   	}
                   }
               }
      } //end of for abstractestimate
	 }
	 
	 
	 public void iterateWOList(List woeList,Rate rate,boolean validationMessageFlag){
		 WorkOrder revisionWO = null;
		 WorkOrder parentWO = null;
		 WorkOrderEstimate woe = null;
		 Map<Integer,String> trackFlagMap=new HashMap<Integer,String>();
		 for(int i = 0; i < woeList.size(); i++){
             woe = (WorkOrderEstimate) woeList.get(i);  
             revisionWO = woe.getWorkOrder();
	         parentWO = revisionWO.getParent();
             if(parentWO != null){	                	
            	   woDate = parentWO.getWorkOrderDate();
                   if(rate != null){	
                   	Period validity = rate.getValidity();
                   	Date startDate = validity.getStartDate();	                                
                   	Date endDate = null;
                   	if(validity.getEndDate()!=null){	                                
                   		endDate=validity.getEndDate();	                                	
                   	}                                
                   	boolean flag = false;
                   	if(startDate!=null && rate.getId()!=null){
                   		flag=checkGivenDateWithinRangeWO(woDate, startDate, endDate);
                   	}
                   	if(flag){     	                    		
                   		deleteFlagMap2.put(rate.getId(), flagValue);
                   		setWoDateFlag(flagValue);
                   		trackFlagMap.put(0, flagValue);
                   		//This flag is added to display validation message, only when called from create() method, not from edit() method.
                   		if(validationMessageFlag){
                   				validateWODate(flag,woeList);
                   			}
                   		}
                   	}else {
                   		if(!trackFlagMap.isEmpty()){
                   			String value=(String)trackFlagMap.get(0);
                   			if(value!=null && !value.equalsIgnoreCase(flagValue)){     	                    				
                   				deleteFlagMap2.put(rate.getId(), "no");
                   			}
                   		}
                   	}
               }
          } //end of for wo
	 }
	 
	
	 public void validateWODate(boolean flag,List woList)
	 {
		 WorkOrderEstimate woe1 = (WorkOrderEstimate) woList.get(0);
		 if(flag==true){
			 if(woList.size()==1){
				 throw new ValidationException(Arrays.asList(new ValidationError("sor.RE.woDate.overlap",getText("sor.RE.woDate.overlap",new String[]{woe1.getEstimate().getEstimateNumber()}))));
			 }
			 else{
				 throw new ValidationException(Arrays.asList(new ValidationError("sor.multipleRE.woDate.overlap",getText("sor.multipleRE.woDate.overlap"))));
			 }
		 }
			 
	 }
	 
	 private static boolean isWithinDateRangeOfEstimateOrWO(Date dateToSearch, Date startdate, Date enddate) {
		
			if(enddate==null){
				if((startdate.before(dateToSearch))){			
					return true;
				}
			}else{
				if((startdate.before(dateToSearch) && dateToSearch.after(enddate))){				
					return true;
				}
			}
			return false;
		}
	 
	/**
	 * @return the deletFlagMap
	 */
	public Map<Long, String> getDeletFlagMap() {
		return deletFlagMap;
	}

	/**
	 * @param deletFlagMap the deletFlagMap to set
	 */
	public void setDeletFlagMap(Map<Long, String> deletFlagMap) {
		this.deletFlagMap = deletFlagMap;
	}

	
	/**
	 * @return the estimateDtFlag
	 */
	public String getEstimateDtFlag() {
		return estimateDtFlag;
	}

	/**
	 * @param estimateDtFlag the estimateDtFlag to set
	 */
	public void setEstimateDtFlag(String estimateDtFlag) {
		this.estimateDtFlag = estimateDtFlag;
	}

	/**
	 * @return the estimateDate
	 */
	public Date getEstimateDate() {
		return estimateDate;
	}

	/**
	 * @param estimateDate the estimateDate to set
	 */
	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}
	
	public boolean checkGivenDateWithinRange(Date estimateDate, Date startDate,Date endDate) {
		if (estimateDate == null) {			
			return false;
		} else {
			return isWithinDateRangeOfEstimateOrWO(estimateDate,startDate, endDate);			
		}
	}

	public boolean checkGivenDateWithinRangeWO(Date woDate, Date startDate,Date endDate) {
		if (woDate == null) {			
			return false;
		} else {
			return isWithinDateRangeOfEstimateOrWO(woDate,startDate, endDate);			
		}
	}
	

	/**
	 * @return the actionRates
	 */
	public List<Rate> getActionRates() {
		return actionRates;
	}

	/**
	 * @param actionRates the actionRates to set
	 */
	public void setActionRates(List<Rate> actionRates) {
		this.actionRates = actionRates;
	}

	/**
	 * @return the actionMarketRates
	 */
	public List<MarketRate> getActionMarketRates() {
		return actionMarketRates;
	}

	/**
	 * @param actionMarketRates the actionMarketRates to set
	 */
	public void setActionMarketRates(List<MarketRate> actionMarketRates) {
		this.actionMarketRates = actionMarketRates;
	}

	/**
	 * @return the abstractEstimateList
	 */
	public List getAbstractEstimateList() {
		return abstractEstimateList;
	}

	/**
	 * @param abstractEstimateList the abstractEstimateList to set
	 */
	public void setAbstractEstimateList(List abstractEstimateList) {
		this.abstractEstimateList = abstractEstimateList;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder){
		StringBuffer scheduleOfRateSql = new StringBuffer(100);
		String scheduleOfRateStr = "";
		List<Object> paramList = new ArrayList<Object>();		
		scheduleOfRateSql.append(" from ScheduleOfRate sor where sor.category.id=?");
		paramList.add(getCategry()); 
		
		if (getCode() != null && !getCode().equals("")) {
			scheduleOfRateSql.append(" and UPPER(sor.code) like ?");
			paramList.add("%"+getCode().toUpperCase()+"%");
		}

		if (getDescription() != null && !getDescription().equals("")) {
			scheduleOfRateSql.append(" and UPPER(sor.description) like ?");
			paramList.add("%"+getDescription().toUpperCase()+"%");
		}		

		scheduleOfRateStr = scheduleOfRateSql.toString();
		String countQuery = "select count(*) " + scheduleOfRateStr;
		return new SearchQueryHQL(scheduleOfRateStr, countQuery, paramList);
		
	}
	
	public Map<Long, String> getDeleteFlagMap2() {
		return deleteFlagMap2;
	}

	public void setDeleteFlagMap2(Map<Long, String> deleteFlagMap2) {
		this.deleteFlagMap2 = deleteFlagMap2;
	}

	public Date getWoDate() {
		return woDate;
	}

	public void setWoDate(Date woDate) {
		this.woDate = woDate;
	}
	
	public String getWoDateFlag() {
		return woDateFlag;
	}

	public void setWoDateFlag(String woDateFlag) {
		this.woDateFlag = woDateFlag;
	}

	public List getWoeList() {
		return woeList;
	}

	public void setWoeList(List woeList) {
		this.woeList = woeList;
	}

	public List<Rate> getEditableRateList() {
		return editableRateList;
	}

	public void setEditableRateList(List<Rate> editableRateList) {
		this.editableRateList = editableRateList;
	}
}