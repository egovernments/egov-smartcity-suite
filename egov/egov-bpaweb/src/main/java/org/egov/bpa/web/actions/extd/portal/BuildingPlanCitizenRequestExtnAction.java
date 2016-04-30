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
package org.egov.bpa.web.actions.extd.portal;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.bpa.models.extd.AutoDcrDtlsExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegnAutoDcrDtlsExtn;
import org.egov.bpa.models.extd.RegnAutoDcrExtn;
import org.egov.bpa.services.extd.autoDcr.AutoDcrExtnService;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.bpa.utils.ServiceType;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("serial")
@ParentPackage("egov")
public class BuildingPlanCitizenRequestExtnAction extends BaseFormAction {
	private String serviceRegId;
	private String requestID;
	//private String NEW= "new";
	private String AUTODCR= "autodcr";
	//private PortalIntegrationService portalIntegrationService;
	private Logger LOGGER = Logger.getLogger(BuildingPlanCitizenRequestExtnAction.class);
	private RegisterBpaExtnService registerBpaExtnService;
	private AutoDcrExtnService autoDcrExtnService;
	private List<RegnAutoDcrDtlsExtn> regnAutoDcrList = new ArrayList<RegnAutoDcrDtlsExtn>();
	private List<AutoDcrDtlsExtn> autoDcrList = new ArrayList<AutoDcrDtlsExtn>();
	private String serviceType;

	@Action(value = "/buildingPlanCitizenRequestExtn-newAppInvacantPlot", results = { @Result(name = NEW,type = "dispatcher") })
	public String newAppInvacantPlot()
	{
		LOGGER.info(" serviceRegId" + getServiceRegId() + " Request Id " + getRequestID());
		setServiceType(getSeriveTypeIdByPassingCode(ServiceType.NEWBUILDINGONVACANTPLOTCODE.getCode()));
		validateForm(Boolean.FALSE);
			return NEW;
	}
	@Action(value = "/buildingPlanCitizenRequestExtn-demolition", results = { @Result(name = NEW,type = "dispatcher") })
	public String demolition()
	{ 
		LOGGER.info(" serviceRegId" + getServiceRegId() + " Request Id " + getRequestID());
		setServiceType(getSeriveTypeIdByPassingCode(ServiceType.APPLICATIONFORDEMOLITIONCODE.getCode()));
		validateForm(Boolean.FALSE);
		return NEW;
	}
	@Action(value = "/buildingPlanCitizenRequestExtn-demolitionAndReconstruction", results = { @Result(name = NEW,type = "dispatcher") })
	public String demolitionAndReconstruction()
	{	
		setServiceType(getSeriveTypeIdByPassingCode(ServiceType.DEMOLITIONRECONSTRUCTIONCODE.getCode()));
		validateForm(Boolean.FALSE);
		return NEW;
	}
	@Action(value = "/buildingPlanCitizenRequestExtn-subDivision", results = { @Result(name = NEW,type = "dispatcher") })
	public String subDivision()
	{
		setServiceType(getSeriveTypeIdByPassingCode(ServiceType.SUBDIVISIONOFLANDCODE.getCode()));
		validateForm(Boolean.FALSE);
		return NEW;
	}
	@Action(value = "/buildingPlanCitizenRequestExtn-layoutApproval", results = { @Result(name = NEW,type = "dispatcher") })
	public String layoutApproval()
	{
		setServiceType(getSeriveTypeIdByPassingCode(ServiceType.LAYOUTAPPPROVALCODE.getCode()));
		validateForm(Boolean.FALSE);
		return NEW;
	}
	
	@Action(value = "/buildingPlanCitizenRequestExtn-additionalConstruction", results = { @Result(name = NEW,type = "dispatcher") })
	public String additionalConstruction()
	{
		setServiceType(getSeriveTypeIdByPassingCode(ServiceType.ADDITIONALCONSTRUCTIONCODE.getCode()));
		validateForm(Boolean.FALSE);
		return NEW;
	}
	
	@Action(value = "/buildingPlanCitizenRequestExtn-cmdaType", results = { @Result(name = NEW,type = "dispatcher") })
	public String cmdaType()
	{
		setServiceType(getSeriveTypeIdByPassingCode(ServiceType.CMDACODE.getCode()));
		validateForm(Boolean.FALSE);
		return NEW;
	}
	
	@Action(value = "/buildingPlanCitizenRequestExtn-reclassification", results = { @Result(name = NEW,type = "dispatcher") })
	public String reclassification()
	{
		setServiceType(getSeriveTypeIdByPassingCode(ServiceType.RECLASSIFICATIONCODE.getCode()));
		validateForm(Boolean.FALSE);
		return NEW;
	}
	
	private String getSeriveTypeIdByPassingCode(String serviceCode) {
		org.egov.bpa.models.extd.masters.ServiceTypeExtn servTyp = registerBpaExtnService
				.getServiceTypeByCode(serviceCode);
		if (servTyp != null)
			return servTyp.getId().toString();
		else
			return null;
	}
	@Action(value = "/buildingPlanCitizenRequestExtn-validateForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String validateForm(Boolean autoDcrCheckRequired){
		
		LOGGER.info("   userid "+(EgovThreadLocals.getUserId()));
		// Get citizen details, by passing login user id.
	/*	EPortalUser citizen= null;
		if(portalIntegrationService!=null){
			portalIntegrationService.initServicePack();
			citizen=getPortalIntegrationService().getPortalUserByUserId(Integer.valueOf(EgovThreadLocals.getUserId()));
			}*/
		//TODO PHionix
		/*if(citizen!=null && citizen.getUserDetail()!=null && citizen.getUserDetail().getMobileNumber()!=null)
		LOGGER.info("   user mobile  " + citizen.getUserDetail().getMobileNumber());*/
		if(Boolean.TRUE){
		List<AutoDcrDtlsExtn> initialAutoDcrList= new ArrayList<AutoDcrDtlsExtn>();
		RegistrationExtn regn=registerBpaExtnService.getRegistrationByPassingServiceReqNumber(requestID);
	
		if(regn!=null)
		{
			// Mean old record. 
			// Get old record.. show next action in UI.
			
		}else
			
		{
			
			// Mean new record.
			//for new required.. check autodcr required ?
		  if(autoDcrCheckRequired){
				/*if(citizen!=null && citizen.getUserDetail()!=null && citizen.getUserDetail().getMobileNumber()!=null){
						initialAutoDcrList=autoDcrExtnService.getAutoDcrByMobileNumber(Long.valueOf(citizen.getUserDetail().getMobileNumber()));
				*/		
						
					if(initialAutoDcrList.size()==0)
					{
						addActionMessage(getMessage("user.autodcr.notexist"));	
					}
						//if(autoDcrList.size()>0)
					for(AutoDcrDtlsExtn autoDcr:initialAutoDcrList)	
						{
							List<RegnAutoDcrExtn> autoDcrObj = autoDcrExtnService
									.getRegnAutoDcrByPassingAutoDcrNumber(autoDcr.getAutoDcrNum());
							
							if (autoDcrObj.size()>0) {
								
								for(RegnAutoDcrExtn regnAutoDcr:autoDcrObj)
								{
									if(!autoDcrExtnService.checkAutodcrAlreadyExist(regnAutoDcr.getAutoDcrNum(), null)) //If already used but not in cancel
										autoDcrList.add(autoDcr);
								}
								
								//regnAutoDcrList.addAll( autoDcrObj);
							}else
								autoDcrList.add(autoDcr);
							// pass autodcr number and get registration details.
							// else build Details of autodcr.
						}
					}else
					{
						/*if(citizen!=null && citizen.getUserDetail()!=null && citizen.getUserDetail().getMobileNumber()==null){
							addFieldError("user.MobineNumberNotMapped", getMessage("user.MobineNumberNotMapped"));
							addActionMessage(getMessage("user.MobineNumberNotMapped"));
						}
						else{
							addFieldError("user.autodcr.notexist", getMessage("user.autodcr.notexist"));
							addActionMessage(getMessage("user.autodcr.notexist"));	
						}*///TODO PHionix
						//Show error message as no autodcr records found. 
					}
		    }
		}else
		    {
		    	// redirect to create screen.
		    }
						
		
		
	
		
		return "new";
	}

	public String getServiceRegId() {
		return serviceRegId;
	}

	public void setServiceRegId(String serviceRegId) {
		this.serviceRegId = serviceRegId;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	protected String getMessage(String key)
	{
		return getText(key);
	}
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*public PortalIntegrationService getPortalIntegrationService() {
		return portalIntegrationService;
	}
	public void setPortalIntegrationService(
			PortalIntegrationService portalIntegrationService) {//TODO PHionix
		this.portalIntegrationService = portalIntegrationService;
	}*/
	public RegisterBpaExtnService getRegisterBpaExtnService() {
		return registerBpaExtnService;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public void setRegisterBpaExtnService(RegisterBpaExtnService registerBpaService) {
		this.registerBpaExtnService = registerBpaService;
	}
	public AutoDcrExtnService getAutoDcrExtnService() {
		return autoDcrExtnService;
	}
	public void setAutoDcrExtnService(AutoDcrExtnService autoDcrService) {
		this.autoDcrExtnService = autoDcrService;
	}

	public List<RegnAutoDcrDtlsExtn> getRegnAutoDcrList() {
		return regnAutoDcrList;
	}

	public void setRegnAutoDcrList(List<RegnAutoDcrDtlsExtn> regnAutoDcrList) {
		this.regnAutoDcrList = regnAutoDcrList;
	}

	public List<AutoDcrDtlsExtn> getAutoDcrList() {
		return autoDcrList;
	}

	public void setAutoDcrList(List<AutoDcrDtlsExtn> autoDcrList) {
		this.autoDcrList = autoDcrList;
	}
	
	
}
