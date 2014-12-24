package org.egov.works.web.actions.milestone;

import java.util.ArrayList;
import java.util.Collection;

import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.masters.MilestoneTemplate;

public class AjaxMilestoneAction extends BaseFormAction {
	
    private MilestoneTemplate milestoneTemplate=new MilestoneTemplate(); 
    private static final String MILESTONECHECK = "milestoneCheck";
    private static final String SEARCH_RESULTS = "searchResults";
    private static final String ACTIVITIES="activities";
    private int status;
    private String code;
    private long workTypeId;
    private long subTypeId;
    private String query;
    private Long workOrderEstimateId;
	
    public Object getModel() {
        // TODO Auto-generated method stub
        return milestoneTemplate;
    }
    
	public String searchAjax(){
		return SEARCH_RESULTS;
	}
	
	public Collection<MilestoneTemplate> getMilestoneTemplateList(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(workTypeId>0){
			strquery="from MilestoneTemplate mt where upper(mt.code) like '%'||?||'%'"+" and mt.workType.id=?";
			params.add(query.toUpperCase());
			params.add(workTypeId);
		}
		if(subTypeId>0){
			strquery+=" and mt.subType.id=?";
			params.add(subTypeId);
		}
		return getPersistenceService().findAllBy(strquery,params.toArray());
	}
	public String findByCode(){
		milestoneTemplate=(MilestoneTemplate) getPersistenceService().find("from MilestoneTemplate where upper(code)=?", code.toUpperCase());
		
		return ACTIVITIES;
	}
	public String checkMilestone(){
        return MILESTONECHECK;
    }

    public boolean getMilestoneCheck() {
    	boolean milestoneexistsOrNot = false;
    	Long milestoneId=null;
    	if(workOrderEstimateId!=null){
    		if(getPersistenceService().find("from Milestone where workOrderEstimate.id=? and egwStatus.code<>?", workOrderEstimateId,"CANCELLED")!=null){
    			milestoneId=(Long) getPersistenceService().find("select m.id from Milestone m where m.workOrderEstimate.id=? and egwStatus.code<>?", workOrderEstimateId,"CANCELLED");
    		}
    	}
    	if(milestoneId!=null){
    		milestoneexistsOrNot=true;
    	}
    	else{
    		milestoneexistsOrNot=false;
    	}
    		
    	return milestoneexistsOrNot;
    }
    
	public MilestoneTemplate getMilestoneTemplate() {
		return milestoneTemplate;
	}

	public void setMilestoneTemplate(MilestoneTemplate milestoneTemplate) {
		this.milestoneTemplate = milestoneTemplate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getWorkTypeId() {
		return workTypeId;
	}

	public void setWorkTypeId(long workTypeId) {
		this.workTypeId = workTypeId;
	}

	public long getSubTypeId() {
		return subTypeId;
	}

	public void setSubTypeId(long subTypeId) {
		this.subTypeId = subTypeId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Long getWorkOrderEstimateId() {
		return workOrderEstimateId;
	}

	public void setWorkOrderEstimateId(Long workOrderEstimateId) {
		this.workOrderEstimateId = workOrderEstimateId;
	}

}
