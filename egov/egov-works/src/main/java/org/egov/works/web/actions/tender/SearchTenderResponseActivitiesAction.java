package org.egov.works.web.actions.tender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.tender.TenderEstimate;
import org.egov.works.models.tender.TenderResponseActivity;
import org.egov.works.utils.WorksConstants;

public class SearchTenderResponseActivitiesAction extends SearchFormAction { 
	private Long tenderRespContrId;
	private Long tenderRespId;
	private String activityType;
	private String sorCode; 
	private String activityDesc;
	private Long estimateId;
	private String estimateName;
	private Long activityId;
	private String negotiationNumber;
	private double assignedQty;
	private String recordId;
	private String selectedactivities;
	
	
	public Object getModel() {
		return null;
	}
	
	public String execute(){
		return INDEX;
	}

	public SearchTenderResponseActivitiesAction(){	
	}
	
	public void prepare(){
		super.prepare();
		List<AbstractEstimate> estimateList=new ArrayList<AbstractEstimate>();
		TenderEstimate tenderEstimate=null;
		if(tenderRespId!=null){
			tenderEstimate=(TenderEstimate)getPersistenceService().find("select tr.tenderEstimate from TenderResponse tr where tr.id=?",tenderRespId);
		}
		if(tenderEstimate!=null && tenderEstimate.getAbstractEstimate()==null)
			estimateList.addAll(tenderEstimate.getWorksPackage().getAllEstimates());
		else if(tenderEstimate!=null && tenderEstimate.getWorksPackage()==null)
			estimateList.add(tenderEstimate.getAbstractEstimate());
		//List<TenderResponseActivity> tenderResponseList=getPersistenceService().findAllBy("select tra.activity from TenderResponseActivity tra where tra.tenderResponse.id=?",tenderRespId);
		addDropdownData("estimateList", estimateList);		
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		StringBuilder sb = new StringBuilder(300);
		List<Object> paramList = new ArrayList<Object>();
		int counter = 0;
		sb.append("from TenderResponseActivity as tra left join tra.activity.schedule schedule"  
							+ " left join tra.activity.nonSor nonSor where tra.tenderResponse.id= ?");
		paramList.add(tenderRespId);
		counter++;
		if(StringUtils.isNotBlank(activityType) && activityType.equalsIgnoreCase("SOR")){
			sb.append(" and schedule is not null");
		}
		if(StringUtils.isNotBlank(activityType) && activityType.equalsIgnoreCase("Non SOR")){
			sb.append(" and nonSor is not null");
		}
		if(StringUtils.isNotBlank(sorCode)){
			sb.append(" and UPPER(schedule.code) like ?");
			paramList.add("%" +sorCode.toUpperCase()+"%");
			counter++;
		}
		if(StringUtils.isNotBlank(activityDesc)){
			sb.append(" and ((UPPER(schedule.description) like ?) or " + 
					"(UPPER(nonSor.description) like ? ))"); 
			paramList.add("%" +activityDesc.toUpperCase()+"%");
			counter++;
			paramList.add("%" +activityDesc.toUpperCase()+"%");
			counter++;
		}
		if(StringUtils.isNotBlank(estimateName)){
			sb.append(" and UPPER(tra.activity.abstractEstimate.name) like ?");
			paramList.add("%" +estimateName.toUpperCase()+"%");
			counter++;
		}
		
		if(estimateId!=null && estimateId!=-1){
			sb.append(" and tra.activity.abstractEstimate.id= ?");
			paramList.add(estimateId);
			counter++;
		}

		if(StringUtils.isNotBlank(selectedactivities)){
			sb.append(" and tra.activity.id not in(?").append(counter).append(")");
			String[] activitiesId=selectedactivities.split(",");
			List<Long> activitiesIdList=new ArrayList<Long>();
			for (int i = 0; i < activitiesId.length; i++) {
				activitiesIdList.add(Long.valueOf(activitiesId[i]));
			}
			paramList.add(activitiesIdList);
		}
		
		sb.append(" order by tra.activity.abstractEstimate.id");
	
		String query = sb.toString();
		String countQuery = "select count(*) " + query;
		return new SearchQueryHQL(query, countQuery, paramList);
	}
	
	public String search() {
		setPageSize(100);
		String retVal=super.search();
		populateAssignedQunatity();
		if(searchResult.getFullListSize()==0)
			addFieldError("result not found", "No results found for search parameters");
		return retVal;
	}
	
	private void populateAssignedQunatity() {
		List<TenderResponseActivity> tenderResponseActivityList = new LinkedList<TenderResponseActivity>();
		
		Iterator iter = searchResult.getList().iterator();
		while(iter.hasNext()) {
			Object[] row = (Object[])iter.next();
			TenderResponseActivity tenderResponseActivity = (TenderResponseActivity) row[0];
			double assignedQty=getAssignedQuantity(tenderResponseActivity.getActivity().getId(), tenderResponseActivity.getTenderResponse().getNegotiationNumber());
			if(assignedQty<tenderResponseActivity.getNegotiatedQuantity()) {
				tenderResponseActivity.setAssignedQty(assignedQty);
				tenderResponseActivityList.add(tenderResponseActivity);
			}
		}
		searchResult.getList().clear();
		searchResult.getList().addAll(tenderResponseActivityList);
	}
	
	private double getAssignedQuantity(Long activityId, String negotiationNumber) {
		Object[] params = new Object[]{negotiationNumber,WorksConstants.CANCELLED_STATUS,activityId};
		Double assignedQty = (Double) getPersistenceService().findByNamedQuery("getAssignedQuantityForActivity",params);	
		params = new Object[]{negotiationNumber,WorksConstants.NEW,activityId};
		Double assignedQtyForNew = (Double) getPersistenceService().findByNamedQuery("getAssignedQuantityForActivityForNewWO",params);
		
		if(assignedQty!=null && assignedQtyForNew!=null)
			assignedQty=assignedQty+assignedQtyForNew;
		if(assignedQty==null && assignedQtyForNew!=null)
			assignedQty=assignedQtyForNew;
		if(assignedQty==null)
			return 0.0d;
		else
			return assignedQty.doubleValue();
	}
	
	public String getAssignedQuantity(){
		assignedQty=getAssignedQuantity(activityId,negotiationNumber);
		return "assignedQty";
	}
	
	public Long getTenderRespContrId() {
		return tenderRespContrId;
	}

	public void setTenderRespContrId(Long tenderRespContrId) {
		this.tenderRespContrId = tenderRespContrId;
	}

	public Long getTenderRespId() {
		return tenderRespId;
	}

	public void setTenderRespId(Long tenderRespId) {
		this.tenderRespId = tenderRespId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getSorCode() {
		return sorCode;
	}

	public void setSorCode(String sorCode) {
		this.sorCode = sorCode;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public String getEstimateName() {
		return estimateName;
	}

	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getNegotiationNumber() {
		return negotiationNumber;
	}

	public void setNegotiationNumber(String negotiationNumber) {
		this.negotiationNumber = negotiationNumber;
	}

	public double getAssignedQty() {
		return assignedQty;
	}

	public void setAssignedQty(double assignedQty) {
		this.assignedQty = assignedQty;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getSelectedactivities() {
		return selectedactivities;
	}

	public void setSelectedactivities(String selectedactivities) {
		this.selectedactivities = selectedactivities;
	}
}
