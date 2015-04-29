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
