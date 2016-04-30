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
package org.egov.bpa.web.actions.extd.buildingdetails;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.ApprdBuildingDetailsExtn;
import org.egov.bpa.models.extd.ApprdBuildingFloorDtlsExtn;
import org.egov.bpa.models.extd.AutoDcrExtn;
import org.egov.bpa.models.extd.AutoDcrFloorDtlsExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegnAutoDcrExtn;
import org.egov.bpa.services.extd.autoDcr.AutoDcrExtnService;
import org.egov.bpa.services.extd.common.ApprdBldgDetailExtnService;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.masters.BuildingUsageExtnService;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
@Namespace("/buildingdetails")
@SuppressWarnings("serial")
@ParentPackage("egov")
public class BuildingDetailsExtnAction extends BaseFormAction{

	private ApprdBuildingDetailsExtn apprdBldgDetails = new ApprdBuildingDetailsExtn();
	private List<ApprdBuildingFloorDtlsExtn> builflorlsList = new ArrayList<ApprdBuildingFloorDtlsExtn>(0);
	private Logger LOGGER = Logger.getLogger(getClass());
	private String mode;
	private Map<String,String> unitClsfnList;
	private BuildingUsageExtnService bldgUsageExtnService;
	private Long registrationId;
	private Map<Integer, String> floorNoMap=new HashMap<Integer, String>();
	private ApprdBldgDetailExtnService apprdBldgDetExtnService;
	private RegisterBpaExtnService registerBpaExtnService;
	private RegistrationExtn registration;
	private String serviceType;
	private AutoDcrExtnService autoDcrExtnService;



	public BuildingDetailsExtnAction() {

	}
	public Object getModel() {
		return apprdBldgDetails;
	}

	public Map<Integer,String> getFloorNoMap()
	{

		if(apprdBldgDetails.getFloorCount()==null)
			return  Collections.emptyMap();
		else
		{
			floorNoMap=BpaCommonExtnService.getFloorsMap(apprdBldgDetails.getFloorCount());
			return floorNoMap;
		}
	}

	@SkipValidation
	public void prepare() {
		super.prepare();
		apprdBldgDetails.setIsBasementUnit(Boolean.FALSE);
		setUnitClsfnList(BpaCommonExtnService.unitClassification());
		addDropdownData("bldgUsageMstrList", bldgUsageExtnService.getBldgUsageMstrList());
		setRegistration(registerBpaExtnService.getRegistrationById(registrationId));
		setServiceType(registration.getServiceType().getCode());
	}

	@SkipValidation
	@Action(value = "/buildingDetailsExtn-newForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String newForm() {

		if(serviceType.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
				|| serviceType.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
				|| serviceType.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))
			{
				populateBldgDtlsByAutoDcr();
				builflorlsList.clear();
				builflorlsList = new ArrayList<ApprdBuildingFloorDtlsExtn>(apprdBldgDetails.getApprdBuildingFlrDtlsSet());
				if(builflorlsList.isEmpty())
				builflorlsList.add(new ApprdBuildingFloorDtlsExtn());

			}
			else
			{
				builflorlsList.add(new ApprdBuildingFloorDtlsExtn());
			}
		setMode(NEW);

		return NEW;

	}


	@SkipValidation
	private void populateBldgDtlsByAutoDcr() {
		RegnAutoDcrExtn regnAutoDcr = autoDcrExtnService.getLatestRegnAutoDcrForRegn(registration);
		AutoDcrExtn autoDcr=null;
		if(regnAutoDcr!=null) {
			autoDcr = autoDcrExtnService.getAutoDcrByAutoDcrNum(regnAutoDcr.getAutoDcrNum());
		}
		if(autoDcr!=null) {

			//converting SqFt. value of plot area to SqMt.
			if(autoDcr.getPlotarea() != null) {
				BigDecimal floorAreaSqMt = (new BigDecimal(autoDcr.getPlotarea())).multiply(new BigDecimal(0.092903)).setScale(6, RoundingMode.HALF_UP);
				//System.out.println(floorAreaSqMt);
				apprdBldgDetails.setTotalFloorArea(floorAreaSqMt);
			}
			if(autoDcr.getFloorCount()!=null){
				apprdBldgDetails.setFloorCount(autoDcr.getFloorCount());

			if(autoDcr.getAutoDcrFlrDtlsSet().size() !=0) {
				for(AutoDcrFloorDtlsExtn autoDcrFloor : autoDcr.getAutoDcrFlrDtlsSet()) {
					ApprdBuildingFloorDtlsExtn apprdBldgFlr = new ApprdBuildingFloorDtlsExtn();
					apprdBldgFlr.setFloorNum(autoDcrFloor.getFloorNum());
					if(autoDcrFloor.getFloorNum()!=null && (autoDcr.getFloorCount() < autoDcrFloor.getFloorNum()))
					{
						addActionMessage("Floor Details Entered in auto dcr are incorrect");
					}
					apprdBldgFlr.setExistingBldgArea(autoDcrFloor.getExistingBldgArea());
					apprdBldgFlr.setExistingBldgUsage(autoDcrFloor.getExistingBldgUsage());
					apprdBldgFlr.setProposedBldgArea(autoDcrFloor.getProposedBldgArea());
					apprdBldgFlr.setProposedBldgUsage(autoDcrFloor.getProposedBldgUsage());
					apprdBldgDetails.getApprdBuildingFlrDtlsSet().add(apprdBldgFlr);
				}
					}

			}
		}
	}
	@ValidationErrorPage(NEW)
	@Override
	public void validate() {
		List<String> temp=new ArrayList<String>();
		temp.add(null);
		builflorlsList.removeAll(temp);
		if(apprdBldgDetails.getUnitClassification()==null || apprdBldgDetails.getUnitClassification().equals("-1")) {
			addFieldError("apprdBldgDetails.UnitClassification", getText("apprdBldgDetails.UnitClassification.required"));
		} else {
			if(apprdBldgDetails.getUnitClassification().equals("Multiple")
					&& apprdBldgDetails.getUnitCount()==null) {
				addFieldError("apprdBldgDetails.UnitCount", getText("apprdBldgDetails.UnitCount.required"));
			}
		}
		if(apprdBldgDetails.getFloorCount()==null || apprdBldgDetails.getFloorCount()==0 ) {
			addFieldError("apprdBldgDetails.FloorCount", getText("apprdBldgDetails.FloorCount.required"));
		}

		if(apprdBldgDetails.getBuildingHeight()==null) {
			addFieldError("apprdBldgDetails.BuildingHeight", getText("apprdBldgDetails.BuildingHeight.required"));
		}
		if(apprdBldgDetails.getTotalFloorArea()==null) {
			addFieldError("apprdBldgDetails.TotalFloorArea", getText("apprdBldgDetails.TotalFloorArea.required"));
		}

		/*if(getRegistration().getAutoDcrSet().size()==0){
			if(serviceType.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE) || serviceType.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE))
			{
				if(builflorlsList.size() != apprdBldgDetails.getFloorCount()  ) {
					addFieldError("apprdBldgDetails.BuidlingFlrDetCount", getText("apprdBldgDetails.BuidlingFlrDetCount.equalToFlrCount"));
				}
			}
		}*/
		for(ApprdBuildingFloorDtlsExtn floorDet: builflorlsList) {
			if(floorDet!=null){
				if ( getRegistration().getAutoDcrSet().size()!=0){
					if( floorDet.getFloorNum() == -10) {
						addFieldError("floore", getText("autodcr.details"));
					}
				}
				else if(floorDet.getFloorNum() == -10)
				{
					addFieldError("apprdBldgDetails.floorNum", getText("apprdBldgDetails.floorNum.required"));
				}

				if(serviceType.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| serviceType.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
						|| serviceType.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)
						|| serviceType.equals(BpaConstants.CMDACODE)) {

					if( floorDet.getProposedBldgArea()==null) {
						addFieldError("apprdBldgDetails.PropArea", getText("apprdBldgDetails.PropArea.required"));
					}
					if (floorDet.getProposedBldgUsage()==null || floorDet.getProposedBldgUsage().getId()== -1){
						addFieldError("apprdBldgDetails.PropUsage", getText("apprdBldgDetails.PropUsage.required"));
					}
				}

				if(serviceType.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
						|| serviceType.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)) {

					if( floorDet.getExistingBldgArea()==null) {
						addFieldError("apprdBldgDetails.ExistArea", getText("apprdBldgDetails.ExistArea.required"));
					}
					if (floorDet.getExistingBldgUsage()==null || floorDet.getExistingBldgUsage().getId()== -1){
						addFieldError("apprdBldgDetails.ExistUsage", getText("apprdBldgDetails.ExistUsage.required"));
					}
				}
			}
		}

	}

	@ValidationErrorPage(NEW)
	@Transactional
	@Action(value = "/buildingDetailsExtn-save", results = { @Result(name = NEW,type = "dispatcher") })
	public String save()
	  {
		apprdBldgDetExtnService.createApprdBldgDetail(apprdBldgDetails,builflorlsList, registration);
		addActionMessage("Building Measurement Details Saved Successfully ");
		setMode(BpaConstants.MODEVIEW);
		return NEW;
	 }

	public ApprdBuildingDetailsExtn getApprdBldgDetails() {
		return apprdBldgDetails;
	}
	public void setApprdBldgDetails(ApprdBuildingDetailsExtn apprdBldgDetails) {
		this.apprdBldgDetails = apprdBldgDetails;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Map<String, String> getUnitClsfnList() {
		return unitClsfnList;
	}
	public void setUnitClsfnList(Map<String, String> unitClsfnList) {
		this.unitClsfnList = unitClsfnList;
	}
	public BuildingUsageExtnService getBldgUsageExtnService() {
		return bldgUsageExtnService;
	}
	public void setBldgUsageExtnService(BuildingUsageExtnService bldgUsageService) {
		this.bldgUsageExtnService = bldgUsageService;
	}
	public Long getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}
	public ApprdBldgDetailExtnService getApprdBldgDetExtnService() {
		return apprdBldgDetExtnService;
	}
	public void setApprdBldgDetExtnService(ApprdBldgDetailExtnService apprdBldgDetService) {
		this.apprdBldgDetExtnService = apprdBldgDetService;
	}
	public RegisterBpaExtnService getRegisterBpaExtnService() {
		return registerBpaExtnService;
	}
	public void setRegisterBpaExtnService(RegisterBpaExtnService registerBpaService) {
		this.registerBpaExtnService = registerBpaService;
	}
	public RegistrationExtn getRegistration() {
		return registration;
	}
	public void setRegistration(RegistrationExtn registration) {
		this.registration = registration;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public AutoDcrExtnService getAutoDcrExtnService() {
		return autoDcrExtnService;
	}

	public void setAutoDcrExtnService(AutoDcrExtnService autoDcrService) {
		this.autoDcrExtnService = autoDcrService;
	}
	public List<ApprdBuildingFloorDtlsExtn> getBuilflorlsList() {
		return builflorlsList;
	}

	public void setBuilflorlsList(List<ApprdBuildingFloorDtlsExtn> builflorlsList) {
		this.builflorlsList = builflorlsList;
	}


}
