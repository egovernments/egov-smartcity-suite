package org.egov.works.web.actions.tender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.tender.model.TenderResponseLine;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.tender.GenericTenderResponseActivity;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorkOrderService;

public class SearchTenderResponseActivitiesAction extends SearchFormAction { 

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
	private WorkOrderService workOrderService;
	private AbstractEstimate estimate;
	private AbstractEstimateService abstractEstimateService;
	
	public Object getModel() {
		return null;
	}
	
	public String execute(){
		prepare();
		return INDEX;
	}

	public SearchTenderResponseActivitiesAction(){	
	}
	
	public void prepare(){
		super.prepare();
		if(estimateId!=null){
			estimate=abstractEstimateService.findById(estimateId, false);
		}
		List<AbstractEstimate> estimateList=new ArrayList<AbstractEstimate>();
		estimateList.add(estimate);
		addDropdownData("estimateList", estimateList);		
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		StringBuilder sb = new StringBuilder(300);
		
		sb.append("from TenderResponseLine trl where trl.tenderResponse.id="+tenderRespId);
		
		if(StringUtils.isNotBlank(activityType) && activityType.equalsIgnoreCase("SOR")){
			sb.append(" and trl.tenderableEntity.tenderableType='WORKS_ACTIVITY_SOR' ");
		}
		if(StringUtils.isNotBlank(activityType) && activityType.equalsIgnoreCase("Non SOR")){
			sb.append(" and trl.tenderableEntity.tenderableType='WORKS_ACTIVITY_NONSOR' ");
		}
		if(StringUtils.isNotBlank(sorCode)){
			sb.append(" and UPPER(trl.tenderableEntity.number) like '%^" +sorCode.toUpperCase()+"%'");
		}
		if(StringUtils.isNotBlank(activityDesc)){
			sb.append(" and ((UPPER(trl.tenderableEntity.description) like '%" +activityDesc.toUpperCase()+"%'))"); 
		}
		//if(StringUtils.isNotBlank(estimateName)){
		//	sb.append(" and UPPER(tra.activity.abstractEstimate.name) like '%" +estimateName.toUpperCase()+"%'");
		//	}
		
		if(estimate!=null){
			sb.append(" and trl.tenderableEntity.tenderableEntityGroup.number= '"+estimate.getEstimateNumber()+"'");
		}
		if(StringUtils.isNotBlank(selectedactivities)){
			sb.append(" and trl.id not in("+selectedactivities+")");
		}
				
	
		String query = sb.toString();
		String countQuery = "select count(*) " + query;
		return new SearchQueryHQL(query, countQuery, null);
	}
	
	public String search() {
		prepare();
		setPageSize(100);
		String retVal=super.search();
		populateAssignedQunatity();
		if(searchResult.getFullListSize()==0)
			addFieldError("result not found", "No results found for search parameters");
		return retVal;
	}
	
	private void populateAssignedQunatity() {
		List<GenericTenderResponseActivity> tenderResponseActivityList = new LinkedList<GenericTenderResponseActivity>();
		
		Iterator iter = searchResult.getList().iterator();
		while(iter.hasNext()) {
			TenderResponseLine tenderResponseLine = (TenderResponseLine) iter.next();
			GenericTenderResponseActivity genericTRA=new GenericTenderResponseActivity();
			Activity activity=workOrderService.getActivityFromTenderResponseLineAndEstimate(tenderResponseLine, estimate);
			genericTRA.setActivity(activity);
			genericTRA.setGenericTenderResponse(tenderResponseLine.getTenderResponse());
			genericTRA.setTenderResponseLine(tenderResponseLine);
			genericTRA.setEstimatedQty(activity.getQuantity());
			
			genericTRA.setNegotiatedQuantity(tenderResponseLine.getQuantityByUom().doubleValue());
			genericTRA.setNegotiatedRate(tenderResponseLine.getBidRateByUom().doubleValue());
			double assignedQty=getAssignedQuantity(activity.getId(), tenderResponseLine.getTenderResponse().getTenderUnit().getTenderNotice().getNumber());
			//if(assignedQty<tenderResponseLine.getQuantityByUom().doubleValue()) {
				genericTRA.setAssignedQty(assignedQty);
			//}
			tenderResponseActivityList.add(genericTRA);
		}
		searchResult.getList().clear();
		searchResult.getList().addAll(tenderResponseActivityList);
	}
	
	private double getAssignedQuantity(Long activityId, String tenderNumber) {
		Object[] params = new Object[]{tenderNumber,activityId};
		Double assignedQty = (Double) getPersistenceService().findByNamedQuery("getAssignedQuantityForActivity",params);	
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
	
	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
}
	
	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}
}
