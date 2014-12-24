package org.egov.works.web.actions.masters;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.Period;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.masters.MarketRate;
import org.egov.works.models.masters.Rate;
import org.egov.works.models.masters.ScheduleCategory;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.utils.DateConversionUtil;
import org.egov.works.utils.WorksConstants;

import com.opensymphony.xwork2.Action;

@Result(name = Action.SUCCESS, type = "redirect", location = "scheduleOfRate.action")
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
	private String estimateDtFlag = "no";
	private Date estimateDate;
	public static final String flagValue="yes";
	
	private List<Rate> actionRates = new LinkedList<Rate>();
	private List<MarketRate> actionMarketRates=new LinkedList<MarketRate>();
	
	private Long maxRateId;
	private Date maxRateStartDate;
	private Date maxRateEndDate;
	DecimalFormat decimalFormat = new DecimalFormat("###.##");
	
	private List abstractEstimateList=null;
	private AuditEventService auditEventService;
	public ScheduleOfRateAction() {
		addRelatedEntity("category", ScheduleCategory.class);
		addRelatedEntity("uom", EgUom.class);
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
		 getRateDetailsForSORId();
	     return "edit";
	}

	public String save() {
		scheduleOfRateService.update(scheduleOfRate);
		return SUCCESS;
	}
	
	public String create() {
		populateRates();
	    populateMarketRates();	    
	    getRateDetailsForSORId();
	    scheduleOfRateService.persist(scheduleOfRate);
		if (mode != null && mode.equals("edit")) {
			this.editSORAuditTrail(scheduleOfRate);
		} else {
			this.createSORAuditTrail(scheduleOfRate);
		}
		estimateSorRateQuery();
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
		if(id!=null){
			List<Rate> rateObj=scheduleOfRate.getRates();
			Date maxDate=rateObj.get(0).getValidity().getStartDate();
			Rate maxRate=rateObj.get(0);
			for(Rate rate: rateObj){
				HibernateUtil.getCurrentSession().evict(rate);
				if(maxDate.before(rate.getValidity().getStartDate())) {
					maxDate=rate.getValidity().getStartDate();
					maxRate=rate;
				}
			}
			maxRateId = maxRate.getId();
			maxRateStartDate = maxRate.getValidity().getStartDate();
			maxRateEndDate = maxRate.getValidity().getEndDate();
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
	 public void getRateDetailsForSORId(){		 
		 List<Rate> rateList=null;		
		 rateList = scheduleOfRate.getRates();
		 if(!rateList.isEmpty()){
			 iterateRateList(rateList);
         } //end of rate not null   
	 }
	 public void iterateRateList(List<Rate> rateList){		
		  abstractEstimateList = getPersistenceService().findAllBy("select ae from AbstractEstimate ae, Activity act where act.abstractEstimate=ae and ae.egwStatus.code<>'CANCELLED' and act.schedule.id = "+ scheduleOfRate.getId());
		  for(int k=0,len = rateList.size(); k < len; k++){
              Rate rate = (Rate)rateList.get(k);              
  	        if(abstractEstimateList.isEmpty()){	     
  	        	deletFlagMap.put(rate.getId(), "no");
  	        }//end of  abstractestimate
  	        else{
  	        	iterateAbstractList(abstractEstimateList,rate,k);
             	
         	}  
        }//end of rate for loop
	 }
	
	 public void iterateAbstractList(List abstractEstimateList,Rate rate,int k){
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
                   		trackFlagMap.put(Integer.valueOf(k), flagValue);
                   	}else {
                   		if(!trackFlagMap.isEmpty()){
                   			String value=(String)trackFlagMap.get(k);
                   			if(value!=null && !value.equalsIgnoreCase(flagValue)){     	                    				
                   				deletFlagMap.put(rate.getId(), "no");
                   			}
                   		}
                   	}
                   }
               }
      } //end of for abstractestimate
	 }
	 
	 private void createSORAuditTrail(ScheduleOfRate sor){
		 
		 String createSORAuditDetails1 = "";
		 StringBuffer createSORAuditDetails2 = new StringBuffer();
		 List<Rate> rateList=null;
		 rateList = sor.getRates();
		 DecimalFormat df = new DecimalFormat("###.##");
		 createSORAuditDetails1 = getText("audit.trail.SOR.category")+ (sor.getCategory()!=null?sor.getCategory().getCode():"") + 
				 ", "+getText("audit.trail.SOR.uom")+ (sor.getUom()!=null?sor.getUom().getUom():"");
		 
		 for(int k=0,len = rateList.size(); k < len; k++){
		      Rate rate = (Rate)rateList.get(k);
		      if (k == 0) {
		    	  prepareDetails2(createSORAuditDetails2, rate);
		      } else {
		    	  createSORAuditDetails2.append(" || ");
		    	  prepareDetails2(createSORAuditDetails2, rate);
		      }
		}
		/* createSORAuditDetails2 = getText("audit.trail.SOR.period")+DateUtils.getFormattedDate(sor.getRates().get(0).getValidity().getStartDate(),"dd/MM/yyyy") + 
				 					" - "+(rateList.get(0).getValidity().getEndDate()!=null?DateUtils.getFormattedDate(rateList.get(0).getValidity().getEndDate(),"dd/MM/yyyy"):"")+
			 						getText("audit.trail.SOR.rate")+(new BigDecimal(df.format(rateList.get(0).getRate().getValue())));*/
		
		 generateAuditEventForCreate("Create SOR",  createSORAuditDetails1, createSORAuditDetails2.toString());
	 }
	
	 public void generateAuditEventForCreate(String action, String createSORAuditDetails1, String createSORAuditDetails2) {
			final AuditEvent auditEvent = new AuditEvent(AuditModule.WORKS, AuditEntity.WORKS_SOR, 
					action, scheduleOfRate.getCode(), createSORAuditDetails1);
			auditEvent.setPkId(scheduleOfRate.getId());
			auditEvent.setDetails2(createSORAuditDetails2);
			this.auditEventService.createAuditEvent(auditEvent, ScheduleOfRate.class);
		}
	 
	 private void editSORAuditTrail(ScheduleOfRate sor){
		
		 String editSORAuditDetails1 = "";
		 StringBuffer editSORAuditDetails2 = new StringBuffer();
		 List<Rate> rateList=null;
		 rateList = sor.getRates();

		 editSORAuditDetails1 = getText("audit.trail.SOR.category") + (sor.getCategory()!=null?sor.getCategory().getDescription():"") +
				", "+getText("audit.trail.SOR.uom")+ (sor.getUom()!=null?sor.getUom().getUom():"");
			 
		 for(int k=0,len = rateList.size(); k < len; k++){
		      Rate rate = (Rate)rateList.get(k);
		      if (k == 0) {
		    	  prepareDetails2(editSORAuditDetails2, rate);
		      } else {
		    	  editSORAuditDetails2.append(" || ");
		    	  prepareDetails2(editSORAuditDetails2, rate);
		      }
			 }
		 generateAuditEventForEdit("Edit SOR",  editSORAuditDetails1, editSORAuditDetails2.toString());
	 }

	private void prepareDetails2(StringBuffer editSORAuditDetails2, Rate rate) {
		editSORAuditDetails2.append(getText("audit.trail.SOR.period"))
				.append(DateUtils.getFormattedDate(rate.getValidity().getStartDate(), "dd/MM/yyyy"))
				.append(" - ")
				.append(rate.getValidity().getEndDate() != null ? DateUtils.getFormattedDate(rate.getValidity().getEndDate(), "dd/MM/yyyy") : "")
				.append(",").append(getText("audit.trail.SOR.rate")).append(new BigDecimal(decimalFormat.format(rate.getRate().getValue())));
	}
	 
	 public void generateAuditEventForEdit(String action, String editSORAuditDetails1, String editSORAuditDetails2) {
			final AuditEvent auditEvent = new AuditEvent(AuditModule.WORKS, AuditEntity.WORKS_SOR, 
					action, scheduleOfRate.getCode(), editSORAuditDetails1);
			auditEvent.setPkId(scheduleOfRate.getId());
			auditEvent.setDetails2(editSORAuditDetails2);
			this.auditEventService.createAuditEvent(auditEvent, ScheduleOfRate.class);
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
			return DateConversionUtil.isWithinDateRange(estimateDate,startDate, endDate);			
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
				
		scheduleOfRateSql.append(" from ScheduleOfRate sor where sor.category.id like '%").append(getCategry()).append("%'");
		
		if (getCode() != null && !getCode().equals("")) {
			scheduleOfRateSql.append(" and UPPER(sor.code) like '%").append(getCode().toUpperCase()).append("%'");
		}

		if (getDescription() != null && !getDescription().equals("")) {
			scheduleOfRateSql.append(" and UPPER(sor.description) like '%").append(getDescription().toUpperCase()).append("%'");
		}		

		scheduleOfRateStr = scheduleOfRateSql.toString();
		String countQuery = "select count(*) " + scheduleOfRateStr;
		return new SearchQueryHQL(scheduleOfRateStr, countQuery, null);
		
	}
	
	@ValidationErrorPage(value="INDEX")
	public void estimateSorRateQuery(){
		String queryString="";
		Object resultSet1=null;
		//following query will return list of Object[]
		queryString="select max(estimateDate) from AbstractEstimate est where est.id in (select acti.abstractEstimate from Activity acti where acti.schedule.code =?) and est.egwStatus.code<>'CANCELLED'";
		resultSet1=getPersistenceService().find(queryString, scheduleOfRate.getCode());
		Date estimateDate=(Date)resultSet1;
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		for(Rate rte: actionRates){
			if(maxRateId!=null && (rte.getId() != null && rte.getId().longValue()==maxRateId) && (rte.getValidity().getEndDate()!=maxRateEndDate)){
				if(estimateDate!=null && estimateDate.compareTo(rte.getValidity().getEndDate())>0){
					rte.getValidity().setEndDate(maxRateEndDate);
					estimateDtFlag="yes";
					deletFlagMap.put(rte.getId(), "yes");
					throw new ValidationException(Arrays.asList(new ValidationError(null,"This SOR rate is already used in Estimate, please try to update the end date, greater than the latest Estimate date for selected SOR : "+formatter.format(estimateDate))));
				}
			}
		}
		
	} 
	public Date getToday(){
		return new Date();
	}

	public Long getMaxRateId() {
		return maxRateId;
	}

	public void setMaxRateId(Long maxRateId) {
		this.maxRateId = maxRateId;
	}

	public Date getMaxRateStartDate() {
		return maxRateStartDate;
	}

	public void setMaxRateStartDate(Date maxRateStartDate) {
		this.maxRateStartDate = maxRateStartDate;
	}

	public Date getMaxRateEndDate() {
		return maxRateEndDate;
	}

	public void setMaxRateEndDate(Date maxRateEndDate) {
		this.maxRateEndDate = maxRateEndDate;
	}
	
	public void setAuditEventService(AuditEventService auditEventService) {
		  		this.auditEventService = auditEventService;
	}
	
}