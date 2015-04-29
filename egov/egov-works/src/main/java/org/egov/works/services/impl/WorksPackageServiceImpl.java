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
package org.egov.works.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.tender.EstimateLineItemsForWP;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.tender.WorksPackageDetails;
import org.egov.works.models.tender.WorksPackageNumberGenerator;
import org.egov.works.services.WorksPackageService;
import org.egov.works.utils.WorksConstants;

public class WorksPackageServiceImpl extends BaseServiceImpl<WorksPackage,Long> implements WorksPackageService{
	private WorksPackageNumberGenerator workspackageGenerator;
	public WorksPackageServiceImpl(
			PersistenceService<WorksPackage, Long> persistenceService) {
		super(persistenceService);
	}
	
	public void setWorksPackageNumber(WorksPackage entity,CFinancialYear finYear) {
		if(entity.getWpNumber()==null) {
			entity.setWpNumber(workspackageGenerator.getWorksPackageNumber(entity,finYear));
		}
	}

	public List<AbstractEstimate> getAbStractEstimateListByWorksPackage(WorksPackage entity) {
		List<AbstractEstimate> abList = new ArrayList<AbstractEstimate>();
		if(entity!=null && !entity.getWorksPackageDetails().isEmpty())
		{
			for(WorksPackageDetails wpd:entity.getWorksPackageDetails())
					abList.add(wpd.getEstimate());
		}
		return abList;
	}

	public void setWorkspackageGenerator(
			WorksPackageNumberGenerator workspackageGenerator) {
		this.workspackageGenerator = workspackageGenerator;
	}
	
	public Collection<EstimateLineItemsForWP> getActivitiesForEstimate(WorksPackage wpObj)
	{
		Map<Long,EstimateLineItemsForWP> resultMap = new HashMap<Long, EstimateLineItemsForWP>();
		List<AbstractEstimate> abList = getAbStractEstimateListByWorksPackage(wpObj);
		for(Activity act:getAllActivities(abList))
		{
			EstimateLineItemsForWP estlineItem = new EstimateLineItemsForWP();
			if(act.getSchedule()!=null){
				if(resultMap.containsKey(act.getSchedule().getId())){
					EstimateLineItemsForWP preEstlineItem = resultMap.get(act.getSchedule().getId());
					preEstlineItem.setQuantity(act.getQuantity() + preEstlineItem.getQuantity());
						if(DateUtils.compareDates(act.getAbstractEstimate().getEstimateDate(),
				  			preEstlineItem.getEstimateDate())){
				  		preEstlineItem.setRate(act.getSORCurrentRate().getValue());
				  		preEstlineItem.setAmt(preEstlineItem.getQuantity()*act.getRate().getValue());
				  	}
				  	resultMap.put(act.getSchedule().getId(), preEstlineItem);
				}
				else{
					addEstLineItem(act, estlineItem);
					resultMap.put(act.getSchedule().getId(), estlineItem);
				}
			}
			if(act.getNonSor()!=null)
			{
				addEstLineItem(act, estlineItem);
				resultMap.put(act.getNonSor().getId(), estlineItem);
			}
		}
		return getEstLineItemsWithSrlNo(resultMap.values());
	}

	private void addEstLineItem(Activity act,EstimateLineItemsForWP estlineItem) {
		if(act.getSchedule()==null){
			estlineItem.setCode("");
			estlineItem.setDescription(act.getNonSor().getDescription());
			estlineItem.setRate(act.getRate().getValue());
		}
		else{
			estlineItem.setCode(act.getSchedule().getCode());
			estlineItem.setDescription(act.getSchedule().getDescription());
			estlineItem.setRate(act.getSORCurrentRate().getValue());
		}
		estlineItem.setAmt(act.getQuantity()*act.getRate().getValue());
		estlineItem.setEstimateDate(act.getAbstractEstimate().getEstimateDate());
		estlineItem.setQuantity(act.getQuantity());
		estlineItem.setUom(act.getUom().getUom());
	}
	
	
	private List<Activity> getAllActivities(List<AbstractEstimate> abList)
	{
		List<Activity> actList = new ArrayList<Activity>();
		for(AbstractEstimate ab:abList)
			actList.addAll(ab.getActivities());
		return actList;
	}
	
	public double getTotalAmount(Collection<EstimateLineItemsForWP> actList)
	{
		double totalAmt=0;
		for(EstimateLineItemsForWP act:actList)
		   totalAmt+=act.getAmt();
		return totalAmt;
	}
	
	private Collection<EstimateLineItemsForWP> getEstLineItemsWithSrlNo(Collection<EstimateLineItemsForWP> actList)
	{
		int i=1;
		Collection<EstimateLineItemsForWP> latestEstLineItemList = new ArrayList<EstimateLineItemsForWP>();
		for(EstimateLineItemsForWP act:actList){
		   act.setSrlNo(i);
		   latestEstLineItemList.add(act);
			i++;
		}
		return latestEstLineItemList;
	}
	
	public WorksPackage getWorksPackageForAbstractEstimate(AbstractEstimate estimate) {
		WorksPackage wp = (WorksPackage) persistenceService
				.find("select wpd.worksPackage from WorksPackageDetails wpd where wpd.estimate.estimateNumber = ? and wpd.worksPackage.egwStatus.code<>'CANCELLED'",
						estimate.getEstimateNumber());
		return wp;
	}
	
	public List<Object> getWorksPackageDetails(Long estimateId){
		List<Object> wpDetails = genericService.findAllBy("select wpd.worksPackage.id, wpd.worksPackage.wpNumber from WorksPackageDetails wpd "
					+ " where wpd.estimate.id= ? and  wpd.worksPackage.egwStatus.code not in (?,?) ",
						estimateId,WorksConstants.NEW,WorksConstants.CANCELLED_STATUS);
		return wpDetails;
	}
}
