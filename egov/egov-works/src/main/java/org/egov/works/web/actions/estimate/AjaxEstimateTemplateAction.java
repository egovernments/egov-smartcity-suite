/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
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
