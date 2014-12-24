package org.egov.works.web.actions.estimate;

import java.util.Collection;
import java.util.Date;

import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.EstimateTemplate;
import org.egov.works.models.masters.Rate;

public class AjaxEstimateTemplateAction extends BaseFormAction {
	
	private EstimateTemplate estimateTemplate=new EstimateTemplate();
    private static final String CODEUNIQUECHECK = "codeUniqueCheck";
    private static final String SEARCH_RESULTS = "searchResults";
    private static final String ACTIVITIES="activities";
    private int status;
    private String code;
    private long workTypeId;
    private long subTypeId;
    private Rate currentRate;
    private Date estimateDate;
    private String query;
	
    public Object getModel() {
        // TODO Auto-generated method stub
        return estimateTemplate;
    }
    
	public String searchAjax(){
		return SEARCH_RESULTS;
	}
	
	public Collection<EstimateTemplate> getEstimateTemplateList(){
		String strquery="";
		if(workTypeId>0){
			strquery="from EstimateTemplate et where upper(et.code) like '"+query.toUpperCase()+"%' and et.workType.id="+ workTypeId;
		}
		if(subTypeId>0){
			strquery+=" and et.subType.id="+subTypeId;
		}
		if(status==1)
		{
			strquery+=" and et.status="+status;
		}
		return getPersistenceService().findAllBy(strquery);
	}
	public String findCodeAjax(){
		estimateTemplate=(EstimateTemplate) getPersistenceService().find("from EstimateTemplate where upper(code)=?", code.toUpperCase());
		
		return ACTIVITIES;
	}
    public String codeUniqueCheck(){
        return CODEUNIQUECHECK;
    }
    public boolean getCodeCheck() {
    	boolean codeexistsOrNot = false;
    	Long estimateTemplateId=null;
    	if(code!=null){
    		if(getPersistenceService().findByNamedQuery("EstimateTemplateCodeUniqueCheck",code.toUpperCase())!=null){
    			estimateTemplateId=(Long) getPersistenceService().findByNamedQuery("EstimateTemplateCodeUniqueCheck",code.toUpperCase());
    		}
    	}
    	if(estimateTemplateId!=null){
    		codeexistsOrNot=true;
    	}
    	else{
    		codeexistsOrNot=false;
    	}
    		
    	return codeexistsOrNot;
    }
    
	public EstimateTemplate getEstimateTemplate() {
		return estimateTemplate;
	}

	public void setEstimateTemplate(EstimateTemplate estimateTemplate) {
		this.estimateTemplate = estimateTemplate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setStatus(String status) {
		if(status!=null && !status.equalsIgnoreCase(""))
			this.status = Integer.parseInt(status);
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

	public Rate getCurrentRate() {
		return currentRate;
	}

	public void setCurrentRate(Rate currentRate) {
		this.currentRate = currentRate;
	}

	public Date getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
