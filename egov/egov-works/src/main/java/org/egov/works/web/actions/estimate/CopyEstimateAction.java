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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.service.CommonsService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.estimate.AssetsForEstimate;
import org.egov.works.models.estimate.MultiYearEstimate;
import org.egov.works.models.estimate.NonSor;
import org.egov.works.models.estimate.OverheadValue;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
public class CopyEstimateAction extends BaseFormAction{

	private Long estimateId;
	private String copyCancelledEstNum;
	private AbstractEstimate copyEstimate = new AbstractEstimate(); 
	private AbstractEstimateService abstractEstimateService;
	@Autowired
        private EmployeeService employeeService;
	private Date financialYearStartDate;
	@Autowired
        private CommonsService commonsService;
	private String messageKey;
	private WorksService worksService; 
	private WorkflowService<AbstractEstimate> workflowService;
	
	public void prepare() {
		super.prepare();
	}
	
	public String copyEstimate() {
	    AbstractEstimate abstractEstimate= abstractEstimateService.findById(estimateId, false);
            PersonalInformation loggedInEmp = employeeService.getEmpForUserId(worksService.getCurrentLoggedInUserId());
            
            
            
            copyEstimate.setWard(abstractEstimate.getWard());
            copyEstimate.setName(abstractEstimate.getName());
            copyEstimate.setEstimateDate(abstractEstimate.getEstimateDate());
            copyEstimate.setDescription(abstractEstimate.getDescription());
            copyEstimate.setLocation(abstractEstimate.getLocation());
            copyEstimate.setType(abstractEstimate.getType());
            copyEstimate.setCategory(abstractEstimate.getCategory());
            copyEstimate.setParentCategory(abstractEstimate.getParentCategory());
            copyEstimate.setUserDepartment(abstractEstimate.getUserDepartment());
            copyEstimate.setExecutingDepartment(abstractEstimate.getExecutingDepartment());
            copyEstimate.setEstimatePreparedBy(loggedInEmp);
            copyEstimate.setFundSource(abstractEstimate.getFundSource());
            copyEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","NEW"));

            copyEstimate.setOverheadValues(cloneOverheadValue(abstractEstimate.getOverheadValues()));
            copyEstimate.setActivities(cloneActivity(abstractEstimate.getActivities()));
            copyEstimate.setAssetValues(closeAssetForEstimate(abstractEstimate.getAssetValues()));
            copyEstimate.setMultiYearEstimates(cloneMultiYearEstimate(abstractEstimate.getMultiYearEstimates()));
            
            copyEstimate.setWorkValue(abstractEstimate.getWorkValue());
            copyEstimate.setDocument(abstractEstimate.getDocument());
            copyEstimate.setDocumentNumber(abstractEstimate.getDocumentNumber());
            copyEstimate.setIsCopiedEst("Y");
            
            Position owner = loggedInEmp.getAssignment(getFinancialYearStartDate()).getPosition();
            
            //TODO - workflowService.start hase been removed. Need to find alternative. 
            //copyEstimate = (AbstractEstimate) workflowService.start(copyEstimate, owner, "Copy Estimate created");
            copyEstimate = abstractEstimateService.persist(copyEstimate);   
            
            //set estimate number
            if(abstractEstimate.getEstimateNumber().endsWith("/C") && copyCancelledEstNum.equals("yes")) {
                    String estNum = abstractEstimate.getEstimateNumber().substring(0, abstractEstimate.getEstimateNumber().length() - 2);
                    copyEstimate.setEstimateNumber(estNum);
            } else {
                    //EstimateNumberGenerator is invoked
                    abstractEstimateService.setEstimateNumber(copyEstimate);
            }
            
            messageKey = "The estimate was copied successfully from estimate "
                            +abstractEstimate.getEstimateNumber()+" to estimate "+copyEstimate.getEstimateNumber();
            return SUCCESS;       
	}
	
	private List<OverheadValue> cloneOverheadValue(List<OverheadValue> overHeadValueList) {
		List<OverheadValue> newOverHeadList = new ArrayList<OverheadValue>();
		for(OverheadValue overhead : overHeadValueList) {
			OverheadValue newOverhead = new OverheadValue();
			
			newOverhead.setAmount(overhead.getAmount());
			newOverhead.setOverhead(overhead.getOverhead());
			newOverhead.setAbstractEstimate(copyEstimate);
			
			newOverHeadList.add(newOverhead);
		}
		return newOverHeadList;
	}
	
	private List<MultiYearEstimate> cloneMultiYearEstimate(List<MultiYearEstimate> multiYearEstList) {
		List<MultiYearEstimate> newMultiYearEstList = new ArrayList<MultiYearEstimate>();
		for(MultiYearEstimate multiYearEst : multiYearEstList) {
			MultiYearEstimate newMultiYearEst = new MultiYearEstimate();
			
			newMultiYearEst.setAbstractEstimate(copyEstimate);
			newMultiYearEst.setFinancialYear(multiYearEst.getFinancialYear());
			newMultiYearEst.setPercentage(multiYearEst.getPercentage());
			
			newMultiYearEstList.add(newMultiYearEst);
		}
		return newMultiYearEstList;
	}

	private List<Activity> cloneActivity(List<Activity> activityList) {
		List<Activity> newActivityList = new ArrayList<Activity>();
		for(Activity activity : activityList) {
			Activity newActivity = new Activity();
			
			newActivity.setUom(activity.getUom());
			newActivity.setSchedule(activity.getSchedule());
			newActivity.setServiceTaxPerc(activity.getServiceTaxPerc());
			newActivity.setQuantity(activity.getQuantity());
			newActivity.setRate(activity.getRate());
			newActivity.setAbstractEstimate(copyEstimate);
			if(activity.getNonSor()!=null)
			{
				NonSor nonSORObj = new NonSor();
				nonSORObj.setDescription(activity.getNonSor().getDescription());
				nonSORObj.setUom(activity.getNonSor().getUom());
				newActivity.setNonSor(nonSORObj);
			}
			else
				newActivity.setNonSor(null);
			newActivityList.add(newActivity);
		}
		return newActivityList;
	}
	
	private List<AssetsForEstimate> closeAssetForEstimate(List<AssetsForEstimate> assetEstList) {
		List<AssetsForEstimate> newAssetEstList = new ArrayList<AssetsForEstimate>();
		for(AssetsForEstimate asset : assetEstList) {
			AssetsForEstimate newAssetEst = new AssetsForEstimate();
			
			newAssetEst.setAbstractEstimate(copyEstimate);
			newAssetEst.setAsset(asset.getAsset());
			
			newAssetEstList.add(newAssetEst);
		}
		return newAssetEstList;
	}
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	

	public Date getFinancialYearStartDate() {
		financialYearStartDate = commonsService.getFinancialYearByFinYearRange(worksService.getWorksConfigValue("FINANCIAL_YEAR_RANGE")).getStartingDate();
		return financialYearStartDate;
	}

	public void setFinancialYearStartDate(Date financialYearStartDate) {
		this.financialYearStartDate = financialYearStartDate;
	}

	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}

	public void setAbstractEstimateService(AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getCopyCancelledEstNum() {
		return copyCancelledEstNum;
	}

	public void setCopyCancelledEstNum(String copyCancelledEstNum) {
		this.copyCancelledEstNum = copyCancelledEstNum;
	}

	public AbstractEstimate getCopyEstimate() {
		return copyEstimate;
	}

	public void setCopyEstimate(AbstractEstimate copyEstimate) {
		this.copyEstimate = copyEstimate;
	}

	public void setEstimateWorkflowService(WorkflowService<AbstractEstimate> workflow) {
		this.workflowService = workflow;
	}
}
