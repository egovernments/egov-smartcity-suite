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
package org.egov.bpa.web.actions.extd.search;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.BpaAddressExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.utils.ApplicationMode;
import org.egov.bpa.web.actions.extd.common.BpaExtnRuleBook;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Transactional(readOnly = true)
@ParentPackage("egov")
//@Namespace("/extd/search")
public class SearchExtnAction extends SearchFormAction{
	private static final Logger LOGGER = Logger.getLogger(SearchExtnAction.class);
	private RegistrationExtn registration=new RegistrationExtn();
	private String autoDCRNo;
	private String phoneNo;
	private Date applicationFromDate;
	private Date applicationToDate;
	private Date challanFromDate;
	private Date challanTodate;
	private String user;
	private String searchMode;	
	private BpaCommonExtnService bpaCommonExtnService;
	private String mode;
	private Integer rowId;
	
	public SearchExtnAction() {
		addRelatedEntity("adminboundaryid", Boundary.class);
		addRelatedEntity("locboundaryid", Boundary.class);
		addRelatedEntity("serviceType", ServiceTypeExtn.class);
	}

	public RegistrationExtn getModel() {
		return registration;
	}
		
	public void prepare()
	{
		 super.prepare();
		 addDropdownData("applicationModeList",Arrays.asList(ApplicationMode.values()));
		 addDropdownData("serviceTypeList", bpaCommonExtnService.getAllServiceTypeList());
		 addDropdownData("statusList", bpaCommonExtnService.getAllStatusForBPA());
		 
	}
	@Action(value = "/extd/search/searchExtn-searchForm", results = { @Result(name = NEW,location = "/WEB-INF/jsp/extd/search/searchExtn-new.jsp") })
	public String searchForm(){
		return NEW;
	}
	
	@ValidationErrorPage(NEW)
	@Action(value = "/searchExtn-searchResults", results = { @Result(name = NEW,type = "dispatcher") })
	@Transactional
	public String searchResults(){
		
			super.search();
			setActionsByRoles(searchResult.getList());
			setSearchMode("result");
			return NEW;
		
		
	}

	private void setActionsByRoles(List<RegistrationExtn>  registrationSearchList) {
	
		List<String> roleList = bpaCommonExtnService.getRoleNamesByPassingUserId((EgovThreadLocals.getUserId()));
		
		prepareActionList(registrationSearchList, roleList);
	}

	public void prepareActionList(List<RegistrationExtn> registrationSearchList,
			List<String> roleList) {
		List<String> actionsList=BpaExtnRuleBook.getInstance().getSearchActionsByRoles(roleList);
		
		if(actionsList!=null && !actionsList.isEmpty()){
		
			for(RegistrationExtn registrationObj:registrationSearchList){
				if(actionsList.contains(BpaConstants.VIEWAPPLICATION))
					registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWAPPLICATION);
				
				if(null!=registrationObj.getEgwStatus()){
					
					if(registrationObj.getEgwStatus().getCode().equals(BpaConstants.LETTERTOPARTYSENT)){
						if(actionsList.contains(BpaConstants.LETTERTOPARTYREPLY))
							registrationObj.getRegisterBpaSearchActions().add(BpaConstants.LETTERTOPARTYREPLY);
					}
					if(registrationObj.getServiceType()!=null && registrationObj.getServiceType().getCode()!=null &&
							registrationObj.getServiceType().getCode().equals(BpaConstants.CMDACODE) && registrationObj.getEgwStatus().getCode().equals(BpaConstants.CMDALETTERTOPARTYSENT))
					{
						if(actionsList.contains(BpaConstants.CMDALETTERTOPARTYREPLY))
							registrationObj.getRegisterBpaSearchActions().add(BpaConstants.CMDALETTERTOPARTYREPLY);
					}
					BigDecimal finalFeeTobePaid= bpaCommonExtnService.isFeeCollectionPending(registrationObj);
					if(!finalFeeTobePaid.equals(BigDecimal.ZERO)){
						if(registrationObj!=null && registrationObj.getEgwStatus().getCode().equals(BpaConstants.CHALLANNOTICESENT) || 
								registrationObj.getEgwStatus().getCode().equals(BpaConstants.INSPECTEDBYLS) || 	
								registrationObj.getEgwStatus().getCode().equals("CitizenRegisteredApplication") ){
							BpaAddressExtn addressObjForProperty=null;
							for(BpaAddressExtn addressObj:registrationObj.getBpaAddressSet())
							{
								if(addressObj!=null && addressObj.getAddressTypeMaster()!=null && addressObj.getAddressTypeMaster().equals(BpaConstants.PROPERTY_ADDRESS))
								{
									 addressObjForProperty=addressObj;
								}
							}
							if(actionsList.contains(BpaConstants.COLLECTFEE) && (registrationObj.getRequest_number()!=null &&   
									null!=registrationObj.getServiceType() && 
									(registrationObj.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE) ||
									registrationObj.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) ||
									registrationObj.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))))
							{
									if(registrationObj.getAutoDcrSet()!=null && 
											!registrationObj.getAutoDcrSet().isEmpty()
											 && registrationObj.getDocumenthistorySet()!=null  
											 && !registrationObj.getDocumenthistorySet().isEmpty() 
											 && registrationObj.getInspectionSet()!=null 
											 && !registrationObj.getInspectionSet().isEmpty() &&
											 (addressObjForProperty!=null && addressObjForProperty.getPlotBlockNumber()!=null &&
													 addressObjForProperty.getPlotSurveyNumber()!=null && addressObjForProperty.getVillageName()!=null && 
													 addressObjForProperty.getPlotNumber()!=null  && 
													 addressObjForProperty.getPincode()!=null))
									{
										//property is mandatory only got servicetypes 03,06
										 if(!registrationObj.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE))
										 {
											 if(registrationObj.getServiceType().getIsPtisNumberRequired()!=null && 
													 registrationObj.getServiceType().getIsPtisNumberRequired().equals(Boolean.TRUE) && 
													 registrationObj.getPropertyid()!=null){
												 
											 registrationObj.getRegisterBpaSearchActions().add(BpaConstants.COLLECTFEE); 
											 }
										 }
										 else{
											 registrationObj.getRegisterBpaSearchActions().add(BpaConstants.COLLECTFEE); 
										 }
									}
									
							
							}
							if(actionsList.contains(BpaConstants.COLLECTFEE) && (null!=registrationObj.getServiceType() && 
									registrationObj.getServiceType().getCode().equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE) ||
									registrationObj.getServiceType().getCode().equals(BpaConstants.SUBDIVISIONOFLANDCODE) ||
									registrationObj.getServiceType().getCode().equals(BpaConstants.LAYOUTAPPPROVALCODE)||
									registrationObj.getServiceType().getCode().equals(BpaConstants.CMDACODE)||
									registrationObj.getServiceType().getCode().equals(BpaConstants.RECLASSIFICATIONCODE))){
								
								registrationObj.getRegisterBpaSearchActions().add(BpaConstants.COLLECTFEE);
							}
							
						
							/*if(actionsList.contains(BpaConstants.ADDREVISEDFEE))
								registrationObj.getRegisterBpaSearchActions().add(BpaConstants.ADDREVISEDFEE);*/
							
						
						}
					}
						if(null!=registrationObj.getEgwStatus()){
						
						if(registrationObj.getEgwStatus().getCode().equals(BpaConstants.ORDERPREPARED)){
							if(actionsList.contains(BpaConstants.ORDERISSUED))
								registrationObj.getRegisterBpaSearchActions().add(BpaConstants.ORDERISSUED);
						}
						}
					if(registrationObj.getEgDemand()!=null && (registrationObj.getEgwStatus().getCode().equals(BpaConstants.STATUSAPPROVED) || 
							registrationObj.getEgwStatus().getCode().equals(BpaConstants.CHALLANNOTICESENT) ||
								registrationObj.getEgwStatus().getCode().equals(BpaConstants.CHALLANAMOUNTCOLLECTED) ||
										registrationObj.getEgwStatus().getCode().equals(BpaConstants.APPLICANTSIGNUPDATED) || 
										registrationObj.getEgwStatus().getCode().equals(BpaConstants.ORDERISSUEDTOAPPLICANT) ||
										registrationObj.getEgwStatus().getCode().equals(BpaConstants.ORDERPREPARED))){
						
						if(actionsList.contains(BpaConstants.VIEWFEETOBEPAID))
							registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWFEETOBEPAID);
					}
					
					
					// Reclassification changes.
					if(null!=registrationObj.getServiceType() && registrationObj.getServiceType().getCode().equals(BpaConstants.RECLASSIFICATIONCODE)){

						if (registrationObj.getRegisterBpaSearchActions() != null) {

							if (registrationObj.getRegisterBpaSearchActions().contains(BpaConstants.VIEWFEETOBEPAID)) {
								registrationObj.getRegisterBpaSearchActions().remove(BpaConstants.VIEWFEETOBEPAID);
							}
						}
					}
			
					
					Boolean planFlag=bpaCommonExtnService.isPlanOrBuildingPermitAllowedForSearch(registrationObj,finalFeeTobePaid, BpaConstants.PLANPERMIT);
					if(planFlag.equals(Boolean.TRUE)){
						if(actionsList.contains(BpaConstants.VIEWPLANINGPERMITORDER))
							registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWPLANINGPERMITORDER);
					}
					Boolean BuildingFlag=bpaCommonExtnService.isPlanOrBuildingPermitAllowedForSearch(registrationObj,finalFeeTobePaid, BpaConstants.BUILDINGPERMIT);
					if(BuildingFlag.equals(Boolean.TRUE)){
						if(actionsList.contains(BpaConstants.VIEWBUILDINGPERMITORDER))
							registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWBUILDINGPERMITORDER);
					}
				
					if(registrationObj.getEgwStatus().getCode().equals(BpaConstants.REJECTIONAPPROVED) || 
							registrationObj.getEgwStatus().getCode().equals(BpaConstants.REJECTORDERPREPARED) ||
								registrationObj.getEgwStatus().getCode().equals(BpaConstants.REJECTORDERISSUED)){
						
						if(actionsList.contains(BpaConstants.VIEWREJECTIONORDER))
							registrationObj.getRegisterBpaSearchActions().add(BpaConstants.VIEWREJECTIONORDER);
					}
					
					if (actionsList
							.contains(BpaConstants.PRINTEXTERNALFEEDETAILS)) {
						if (registrationObj.getFeeDDSet() != null
								&& !registrationObj.getFeeDDSet().isEmpty()) {
							registrationObj.getRegisterBpaSearchActions().add(
									BpaConstants.PRINTEXTERNALFEEDETAILS);
						}
					}
				}	
			}
		}
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder dynQuery = new StringBuilder(800);
		dynQuery.append( " from RegistrationExtn reg where id is not null ") ;
		
		LOGGER.info(registration.getAdminboundaryid()+"----"+registration.getLocboundaryid());
		if(registration.getAdminboundaryid()!=null){
			
			if(registration.getLocboundaryid()!=null){
				LOGGER.info("registration.getLocboundaryid().getId() "+registration.getLocboundaryid().getId());
				if(registration.getLocboundaryid().getBoundaryType().getName().equals("Street")){
					LOGGER.info("street");		
					dynQuery.append(" and reg.locboundaryid.id = ?");
					paramList.add(registration.getLocboundaryid().getId());	
				}else if(registration.getLocboundaryid().getBoundaryType().getName().equals("Locality")){
					LOGGER.info("Locality");
					dynQuery.append("  and (reg.locboundaryid.id in (select id from Boundary where parent.id=?) OR reg.locboundaryid.id=?)");
					paramList.add(registration.getLocboundaryid().getId());
					paramList.add(registration.getLocboundaryid().getId());
				}else if(registration.getLocboundaryid().getBoundaryType().getName().equals("Area")){
					LOGGER.info("Area");
					dynQuery.append(" and (reg.locboundaryid.id in (select id from Boundary where parent.id=? OR parent.id in (select id from Boundary where parent.id=?) ) OR reg.locboundaryid.id=?) ");
					paramList.add(registration.getLocboundaryid().getId());	
					paramList.add(registration.getLocboundaryid().getId());	
					paramList.add(registration.getLocboundaryid().getId());	
				}
			}else{
				if(registration.getAdminboundaryid().getBoundaryType().getName().equals("Ward")){
					LOGGER.info("Ward");
					dynQuery.append(" and reg.adminboundaryid.id = ?   ");
					paramList.add(registration.getAdminboundaryid().getId());
				}else if(registration.getAdminboundaryid().getBoundaryType().getName().equals("Zone")){
					dynQuery.append("  and (reg.adminboundaryid.id in (select id from Boundary where parent.id=?) OR reg.adminboundaryid.id= ? ) ");
					paramList.add(registration.getAdminboundaryid().getId());
					paramList.add(registration.getAdminboundaryid().getId());
				}
				
			}
		}
		
		if(StringUtils.isNotBlank(registration.getPlanSubmissionNum()))
		{
			dynQuery.append(" and lower(reg.planSubmissionNum) like ?");
			paramList.add("%"+registration.getPlanSubmissionNum().toLowerCase()+"%");
		}
		if(StringUtils.isNotBlank(registration.getInitialPlanSubmissionNum()))
		{
			dynQuery.append(" and lower(reg.initialPlanSubmissionNum) like ?");
			paramList.add("%"+registration.getInitialPlanSubmissionNum().toLowerCase()+"%");
		}
		
		
		
		
		
		if(StringUtils.isNotBlank(registration.getBaNum()))
		{
			dynQuery.append(" and lower(reg.baNum) like ?");
			paramList.add("%"+registration.getBaNum().toLowerCase()+"%");
		}
		
		if(StringUtils.isNotBlank(registration.getAppMode()) && !"-1".equals(registration.getAppMode() ) )
		{
			dynQuery.append(" and lower(reg.appMode) like ?");
			paramList.add("%"+registration.getAppMode().toLowerCase()+"%");
		}
		if(registration.getServiceType()!=null && registration.getServiceType().getId()!=-1)
		{
			dynQuery.append(" and reg.serviceType.id=? ");
			paramList.add(registration.getServiceType().getId());
		}
		
		
		if(StringUtils.isNotBlank(getAutoDCRNo()))
		{
			dynQuery.append(" and reg.id in (select registration.id from RegnAutoDcr where autoDcrNum=? ) ");
			paramList.add(getAutoDCRNo().toString());
		}
		
		if(StringUtils.isNotBlank(registration.getPropertyid()))
		{
			dynQuery.append(" and reg.propertyid like ?");
			paramList.add("%"+registration.getPropertyid()+"%");
		}
		
		/*if(registration.getSurveyorName()!=null && StringUtils.isNotBlank(registration.getSurveyorName().getName()))
		{
			dynQuery.append(" and lower(reg.surveyorName.name) like ?");
			paramList.add("%"+registration.getSurveyorName().getName().toLowerCase()+"%");
		}*/
		
		if(StringUtils.isNotBlank(registration.getCmdaNum()))
		{
			dynQuery.append(" and lower(reg.cmdaNum) like ?");
			paramList.add("%"+registration.getCmdaNum().toLowerCase()+"%");
		}
		
		if(registration.getEgwStatus()!=null && registration.getEgwStatus().getId()!=-1)
		{
			dynQuery.append(" and reg.egwStatus.id=? ");
			paramList.add(registration.getEgwStatus().getId());
		}
		
		if(applicationFromDate!=null && applicationToDate!=null && !DateUtils.compareDates(getApplicationToDate(),getApplicationFromDate()))
			addFieldError("fromDate",getText("greaterthan.endDate.fromDate"));
		if(applicationToDate!=null && !DateUtils.compareDates(new Date(),getApplicationToDate()))
			addFieldError("toDate",getText("greaterthan.endDate.currentdate"));
					
		if(applicationFromDate!=null){
			dynQuery.append(" and trunc(reg.planSubmissionDate) >= '"+DateUtils.getFormattedDate(applicationFromDate,"dd-MMM-yyyy")+"' ");
		}
		if(applicationToDate!=null ){
			dynQuery.append(" and trunc(reg.planSubmissionDate) <= '"+DateUtils.getFormattedDate(applicationToDate,"dd-MMM-yyyy")+"' ");
		}
		
		if(registration.getOwner()!=null && StringUtils.isNotBlank(registration.getOwner().getName()))
		{
			dynQuery.append(" and lower(reg.owner.firstName) like ? ");
			paramList.add("%"+registration.getOwner().getName().toLowerCase()+"%");
		}
		
		/*if(registration.getOwner()!=null && StringUtils.isNotBlank(registration.getOwner().getFatherName()))
		{
			dynQuery.append(" and lower(reg.owner.fatherName) like ? ");
			paramList.add("%"+registration.getOwner().getFatherName().toLowerCase()+"%");
		}*/
		
		if(registration.getOwner()!=null && StringUtils.isNotBlank(registration.getOwner().getEmailId()))
		{
			dynQuery.append(" and lower(reg.owner.emailAddress) like ? "); 
			paramList.add("%"+registration.getOwner().getEmailId().toLowerCase()+"%");
		}
		
		if(StringUtils.isNotBlank(getPhoneNo())){
			dynQuery.append(" and (reg.owner.homePhone || reg.owner.officePhone || reg.owner.mobilePhone) like ? ");
			paramList.add(getPhoneNo());
		}
			
		setPageSize(10);
		String regSearchQuery=" select distinct reg  "+	dynQuery;
		String countQuery = " select count(distinct reg)  " + dynQuery;
		return new SearchQueryHQL(regSearchQuery, countQuery, paramList);
		
	}

	
	public  String getUsertName(Integer id)
    {
		String owner=bpaCommonExtnService.getUsertName(id);
		return owner;
    }
	
	
	public RegistrationExtn getRegistration() {
		return registration;
	}


	public void setRegistration(RegistrationExtn registration) {
		this.registration = registration;
	}

	public String getPhoneNo() {
		return phoneNo;
	}


	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}


	public Date getApplicationFromDate() {
		return applicationFromDate;
	}


	public void setApplicationFromDate(Date applicationFromDate) {
		this.applicationFromDate = applicationFromDate;
	}


	public Date getApplicationToDate() {
		return applicationToDate;
	}


	public void setApplicationToDate(Date applicationToDate) {
		this.applicationToDate = applicationToDate;
	}


	public Date getChallanFromDate() {
		return challanFromDate;
	}


	public void setChallanFromDate(Date challanFromDate) {
		this.challanFromDate = challanFromDate;
	}


	public Date getChallanTodate() {
		return challanTodate;
	}


	public void setChallanTodate(Date challanTodate) {
		this.challanTodate = challanTodate;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}

	
	public String getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

	

	public String getAutoDCRNo() {
		return autoDCRNo;
	}

	public void setAutoDCRNo(String autoDCRNo) {
		this.autoDCRNo = autoDCRNo;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonExtnService) {
		this.bpaCommonExtnService = bpaCommonExtnService;
	}
	
}
