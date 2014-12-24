package org.egov.works.web.actions.workorder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.tender.SetStatus;
import org.egov.works.services.WorksService;

public class SetStatusAction extends BaseFormAction{

	private SetStatus worksStatus = new SetStatus();
	private PersistenceService<SetStatus, Long> worksStatusService;
	private WorksService worksService;
	private String[] statusName;
	private Date[] statusDate;
	private Long objId;
	private String objectType;
	private CommonsService commonsService;
	private List<SetStatus>  setStatusList;
	private static final String STATUS_OBJECTID = "getStatusByObjectId";
	private static final String STATUS_VALUES = ".setstatus";
	private static final String LAST_STATUS = ".laststatus";
	private Date appDate;
	private String ObjNo;
	
//	private static final String TENDERRESPONSE_CONTRACTORS="TenderResponseContractors";
//	private static final String TENDERRESPONSE="TenderResponse"; 
	
	public Object getModel() {
		return worksStatus;
	}
	
	public void prepare(){
		if (objId != null) {
				setStatusList = worksStatusService.findAllByNamedQuery(STATUS_OBJECTID,objId,objectType);
		}
		addDropdownData("statusList",getAllStatus());
	}

	@SkipValidation
	public String edit()
	{
		if(setStatusList!=null && !setStatusList.isEmpty()){
		     populateStatusNameAndDateDetails(setStatusList);	
		}
		return EDIT;
	}
	
	public String save() 
	{
		int i=0;
		for(String statName:getStatusNameDetails()){
			if(i>(getSetStatusList().size()-1)){
				SetStatus stat = new SetStatus();
				stat.setObjectId(objId);
				stat.setObjectType(objectType);
				stat.setEgwStatus(getDescriptionByCode(statName));
				stat.setStatusDate(getDateList().get(i));
				worksStatusService.persist(stat);
			}
			i++;
		}
		return SUCCESS; 
	}

	private List<EgwStatus> getAllStatus()
	{
		String status;
		String lastStatus;
	/*	if(objectType!=null && objectType.equals(TENDERRESPONSE_CONTRACTORS)){
			 status = worksService.getWorksConfigValue(TENDERRESPONSE+STATUS_VALUES);
			 lastStatus = worksService.getWorksConfigValue(TENDERRESPONSE+LAST_STATUS); 
		}
		else{*/
			 status = worksService.getWorksConfigValue(objectType+STATUS_VALUES);
			 lastStatus = worksService.getWorksConfigValue(objectType+LAST_STATUS); 
		//}
		
		List<String> statList = new ArrayList<String>();
		if(StringUtils.isNotBlank(status) && StringUtils.isNotBlank(lastStatus)){
			List<String> statusList = Arrays.asList(status.split(","));
			for(String stat: statusList){
				if(stat.equals(lastStatus)){
					statList.add(stat);
					break;
				}
				else{
					statList.add(stat);
				}
			}
		}
/*		if(objectType!=null && objectType.equals(TENDERRESPONSE_CONTRACTORS))
			return commonsService.getStatusListByModuleAndCodeList(TENDERRESPONSE, statList);
		else
		
*/		
		
		return commonsService.getStatusListByModuleAndCodeList(objectType, statList);
	}
	
	
	@SuppressWarnings("unchecked")
	public Collection<String> getStatusNameDetails() {
		return CollectionUtils.select(Arrays.asList(statusName), new Predicate(){
			public boolean evaluate(Object statusName) {
				return ((String)statusName)!=null;
			}});
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Date> getStatusDateDetails() {
		return CollectionUtils.select(Arrays.asList(statusDate), new Predicate(){
			public boolean evaluate(Object statusDate) {
				return ((Date)statusDate)!=null;
			}});
	}
	
	private EgwStatus getDescriptionByCode(String statName) {
		
		/*if(objectType!=null && objectType.equals(TENDERRESPONSE_CONTRACTORS))
			return commonsService.getStatusByModuleAndCode(TENDERRESPONSE, statName); 
		else*/
			return commonsService.getStatusByModuleAndCode(objectType, statName);
	}

	private void populateStatusNameAndDateDetails(List<SetStatus> setStatusList)
	{
		int i=0;
		statusName = new String[setStatusList.size()];
		statusDate = new Date[setStatusList.size()];
		for(SetStatus stat: setStatusList){
			getStatusName()[i]=stat.getEgwStatus().getCode();
			getStatusDate()[i]=stat.getStatusDate();
			i++;
		}
	}
	
	private void validateStatusName()
	{
		int i=0;
		List<EgwStatus> statList = getAllStatus();
		for(String statName:getStatusNameDetails()){
			if(!statList.isEmpty() && !statName.equals(statList.get(i).getCode())){
				addFieldError("status.order.incorrect",getText("status.order.incorrect",new String[]{getDescriptionByCode(statName).getDescription(),
						statList.get(i).getDescription()}));
				break;
			}
			i++;
		}
	}

	private void validateStatusDate()
	{
		if(appDate!=null && !getDateList().isEmpty() && !getStatusCodeList().isEmpty()
				&& !DateUtils.compareDates(getDateList().get(0),appDate)){
			addFieldError("status.date.incorrect",getText("status.date.greaterThan.appDate",
					new String[]{getDescriptionByCode(getStatusCodeList().get(0)).getDescription(),objectType}));
		}
		int j=1;
		for(Date dateObj:getDateList()){
			if(getDateList().size()>j && !DateUtils.compareDates(getDateList().get(j),dateObj)){
				addFieldError("status.date.incorrect",getText("status.date.incorrect",
						new String[]{getDescriptionByCode(getStatusCodeList().get(j)).getDescription(),
						getDescriptionByCode(getStatusCodeList().get(j-1)).getDescription()}));
			}
			j++;
		}
	}

	public void validate()
	{
		validateStatusName();
		validateStatusDate();
	}

	private List<Date> getDateList() {
		return (List<Date>) getStatusDateDetails();
	}
	
	private List<String> getStatusCodeList() {
		return (List<String>) getStatusNameDetails();
	}
	
	public void setWorksStatusService(
			PersistenceService<SetStatus, Long> worksStatusService) {
		this.worksStatusService = worksStatusService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String[] getStatusName() {
		return statusName==null?new String[0]:statusName;
	}

	public void setStatusName(String[] statusName) {
		this.statusName = statusName;
	}

	public Date[] getStatusDate() {
		return statusDate==null?new Date[0]:statusDate;
	}

	public void setStatusDate(Date[] statusDate) {
		this.statusDate = statusDate;
	}

	public Long getObjId() {
		return objId;
	}

	public void setObjId(Long objId) {
		this.objId = objId;
	}

	public List<SetStatus> getSetStatusList() {
		return setStatusList;
	}

	public void setSetStatusList(List<SetStatus> setStatusList) {
		this.setStatusList = setStatusList;
	}

	public void setWorksStatus(SetStatus worksStatus) {
		this.worksStatus = worksStatus;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public Date getAppDate() {
		return appDate;
	}

	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}

	public String getObjNo() {
		return ObjNo;
	}

	public void setObjNo(String objNo) {
		ObjNo = objNo;
	}
}
