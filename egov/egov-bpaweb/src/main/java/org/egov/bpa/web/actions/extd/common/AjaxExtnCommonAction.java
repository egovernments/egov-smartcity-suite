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
package org.egov.bpa.web.actions.extd.common;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.FeeExtnService;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.lib.admbndry.BoundaryDAO;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static org.egov.bpa.constants.BpaConstants.STREET_BNDRY_TYPE;
import static org.egov.bpa.constants.BpaConstants.WARD_BNDRY_TYPE;
@Transactional(readOnly = true)
@Results( { @Result(name = AjaxExtnCommonAction.AJAX_RESULT, type = "stream", location = "returnStream", params = { "contentType","text/plain" }) })
@ParentPackage("egov")
public class AjaxExtnCommonAction extends BaseFormAction {

	private List<Boundary> wardList;
	private List<Boundary> streetList;
	private Integer zoneId;
	private Integer wardId; 
	private Long boundaryId;
	private String returnStream ="";
	private BoundaryDAO boundaryDAO;
	private BpaCommonExtnService bpaCommonExtnService; 
	private String arguments;
	private FeeExtnService feeExtnService; 
	private Long serviceTypeId;
	private Integer adminBoundaryId;
	private Long  surveyorId;
	private BigDecimal sitalAreaInSqmt;
	private Long registrationId;
	private RegisterBpaExtnService registerBpaExtnService;
	private String surveyorName;
	private String surveyorCode;
	private String surveyorMobNo; 
	//List<SurveyorDetail>surveyorDetail=new ArrayList<SurveyorDetail>();
	
	
	public BoundaryDAO getBoundaryDAO() {
		return boundaryDAO;
	}

	public void setBoundaryDAO(BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}

	public static final String AJAX_RESULT = "ajaxResult";
	

	public Long getBoundaryId() {
		return boundaryId;
	}

	public void setBoundaryId(Long boundaryId) {
		this.boundaryId = boundaryId;
	}

	public Integer getAdminBoundaryId() {
		return adminBoundaryId;
	}

	public void setAdminBoundaryId(Integer adminBoundaryId) {
		this.adminBoundaryId = adminBoundaryId;
	}

	private Logger LOGGER = Logger.getLogger(getClass());

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Action(value = "/ajaxExtnCommon-wardByZone", results = { @Result(name = "ward",type = "dispatcher") })
	public String wardByZone() {
		LOGGER.debug("Entered into wardByZone, zoneId: " + zoneId);
		wardList = new ArrayList<Boundary>();
		wardList = getPersistenceService()
		.findAllBy(
				"from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.id ",
				WARD_BNDRY_TYPE, getZoneId());
		LOGGER.debug("Exiting from wardByZone, No of wards in zone: " + zoneId + "are "
				+ ((wardList != null) ? wardList : ZERO));
		return "ward";
	}

	@Action(value = "/ajaxExtnCommon-streetByWard", results = { @Result(name = "street",type = "dispatcher") })
	public String streetByWard() {
		LOGGER.debug("Entered into streetByWard, wardId: " + wardId);
		streetList = new ArrayList<Boundary>();
		streetList = getPersistenceService()
		.findAllBy(
				"from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
				STREET_BNDRY_TYPE, getWardId());
		LOGGER.debug("Exiting from streetByWard, No of streets in ward: " + wardId + " are "
				+ ((streetList != null) ? streetList : ZERO));
		return "street";
	}
	


	public List<Boundary> getWardList() {
		return wardList;
	}

	public void setWardList(List<Boundary> wardList) {
		this.wardList = wardList;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public List<Boundary> getStreetList() {
		return streetList;
	}

	public void setStreetList(List<Boundary> streetList) {
		this.streetList = streetList;
	}
	@Action(value = "/ajaxExtnCommon-getCrossHierarchyBoundaries", results = { @Result(name = AJAX_RESULT,type = "dispatcher") })
	public String getCrossHierarchyBoundaries() {
		List<Boundary> bndryList = bpaCommonExtnService.populateArea(boundaryId);
		StringBuffer bndryString = new StringBuffer();
		for (Boundary bndry : bndryList) {
			if (bndryString.length() > 0) 
				bndryString.append('^');
			bndryString.append(bndry.getId()).append('+').append(bndry.getName());
		}
		returnStream = bndryString.toString();
		return AJAX_RESULT;
	}
	@Action(value = "/ajaxExtnCommon-getChildBoundaries", results = { @Result(name =AJAX_RESULT,type = "dispatcher") })
	public String getChildBoundaries() {
		List<Boundary> bndryList = new ArrayList<Boundary>();
		bndryList.addAll(boundaryDAO.getAllchildBoundaries(boundaryId));
		StringBuffer bndryString = new StringBuffer();
		for (Boundary bndry : bndryList) {
			if (bndryString.length() > 0) 
				bndryString.append('^');
			bndryString.append(bndry.getId()).append('+').append(bndry.getName());
		}
		returnStream = bndryString.toString();
		return AJAX_RESULT;
	}
	@Action(value = "/ajaxExtnCommon-newForm", results = { @Result(name = AJAX_RESULT,type = "dispatcher") })
	public String newForm(){
		returnStream ="agvasdg sdg ";
		return AJAX_RESULT;
	}
	public InputStream getReturnStream() {
		return  new ByteArrayInputStream(returnStream.getBytes());

	}
	public void setReturnStream(String xmlStream) {
		this.returnStream = xmlStream;
	}


	@Action(value = "/ajaxExtnCommon-ajaxLoadActionsForSearch", results = { @Result(name = AJAX_RESULT,type = "dispatcher") })
	public String ajaxLoadActionsForSearch(){

		RegistrationExtn registration=null;
		if(registrationId!=null)
			registration=registerBpaExtnService.findById(registrationId,false);
		
		String message=bpaCommonExtnService.getValidActionDropdowns(arguments,registration);
		returnStream = message;
		return AJAX_RESULT;
	}

	public String getArguments() {
		return arguments;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}
	@Action(value = "/ajaxExtnCommon-ajaxGetAdmissionFeeAmount", results = { @Result(name =AJAX_RESULT,type = "dispatcher") })
	public String ajaxGetAdmissionFeeAmount(){
		BigDecimal admissionfeeAmount = BigDecimal.ZERO;
	//	BpaFee fee=(BpaFee)persistenceService.find("from BpaFee where serviceType.id=? ", serviceTypeId);
		//if(fee!=null && fee.getFeeType()!=null){
			admissionfeeAmount=feeExtnService.getTotalFeeAmountByPassingServiceTypeandArea(serviceTypeId, sitalAreaInSqmt, BpaConstants.ADMISSIONFEE);
		//}
		returnStream = admissionfeeAmount.toString();
		return AJAX_RESULT;

	}
	@Action(value = "/ajaxExtnCommon-getSurveyObjectforZone", results = { @Result(name = AJAX_RESULT,type = "dispatcher") })
	public String getSurveyObjectforZone()
	{
		String surveyorCode="";
		String surveyorNameLocal="";
		String surveyorClass="";
	/*	//Surveyor surveyorObj=bpaCommonExtnService.getSurveyour(surveyorId);
		if(surveyorObj!=null){
		surveyorCode=surveyorObj.getCode();
		surveyorNameLocal=surveyorObj.getName();
		SurveyorDetail surveyorDetail=bpaCommonExtnService.getSurveyourDetail(null, surveyorId);
		if(surveyorDetail!=null){
		surveyorClass=surveyorDetail.getSurveyorClass();
		}
		else{
			surveyorClass="II";
		}
		}
		returnStream =surveyorCode.toString()+"-"+surveyorNameLocal.toString()+"-"+surveyorClass.toString();
		*/return AJAX_RESULT;
	}
	
	/**
	 * @return Surveyor Id-Code-Name-MobileNo
	 */
	@Action(value = "/ajaxExtnCommon-getSurveyDetailbyParam", results = { @Result(name = "surveyor",type = "dispatcher") })
	public String getSurveyDetailbyParam(){
	/*	if(surveyorCode!=null && surveyorCode!=""){
			surveyorDetail=bpaCommonExtnService.getSurveyourDetailbyCodeNameMobNo(null, null, surveyorCode);
		}
		if(surveyorName!=null && surveyorName!=""){
			surveyorDetail=bpaCommonExtnService.getSurveyourDetailbyCodeNameMobNo(surveyorName, null, null);
		}
		if(surveyorMobNo!=null && surveyorMobNo!=""){
			surveyorDetail=bpaCommonExtnService.getSurveyourDetailbyCodeNameMobNo(null, surveyorMobNo, null);
		}*/
		return "surveyor"; 
	}
	@Action(value = "/ajaxExtnCommon-ajaxGetMandatoryFieldsForServiceType", results = { @Result(name = AJAX_RESULT,type = "dispatcher") })
	public String ajaxGetMandatoryFieldsForServiceType(){
		Boolean isCMDA=Boolean.FALSE;
		Boolean isAutoDcrReq=Boolean.FALSE;
		Boolean isPropertyMandatory=Boolean.FALSE;
		Boolean isDocUplaodMendatory=Boolean.FALSE;
		ServiceTypeExtn serviceTypeObj = (ServiceTypeExtn) persistenceService.find("from ServiceTypeExtn where id=?",serviceTypeId);
		isCMDA=serviceTypeObj.getIsCmdaType();
		isAutoDcrReq=serviceTypeObj.getISAutoDcrNumberRequired();
		isPropertyMandatory=serviceTypeObj.getIsPtisNumberRequired();
		isDocUplaodMendatory=serviceTypeObj.getIsDocUploadForCitizen();
		returnStream =isCMDA.toString()+"-"+isAutoDcrReq.toString()+"-"+isPropertyMandatory.toString()+"-"+isDocUplaodMendatory.toString();
		return AJAX_RESULT; 
		
	}
	

	
	

	

	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public BigDecimal getSitalAreaInSqmt() {
		return sitalAreaInSqmt;
	}

	public void setSitalAreaInSqmt(BigDecimal sitalAreaInSqmt) {
		this.sitalAreaInSqmt = sitalAreaInSqmt;
	}

	public Long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonExtnService) {
		this.bpaCommonExtnService = bpaCommonExtnService;
	}

	public RegisterBpaExtnService getRegisterBpaExtnService() {
		return registerBpaExtnService;
	}

	public void setRegisterBpaExtnService(
			RegisterBpaExtnService registerBpaExtnService) {
		this.registerBpaExtnService = registerBpaExtnService;
	}

	public FeeExtnService getFeeExtnService() {
		return feeExtnService;
	}

	public void setFeeExtnService(FeeExtnService feeExtnService) {
		this.feeExtnService = feeExtnService;
	}

	public Long getSurveyorId() {
		return surveyorId;
	}

	public void setSurveyorId(Long surveyorId) {
		this.surveyorId = surveyorId;
	}

	public String getSurveyorName() {
		return surveyorName;
	}

	public void setSurveyorName(String surveyorName) {
		this.surveyorName = surveyorName;
	}

	public String getSurveyorCode() {
		return surveyorCode;
	}

	public void setSurveyorCode(String surveyorCode) {
		this.surveyorCode = surveyorCode;
	}

	public String getSurveyorMobNo() {
		return surveyorMobNo;
	}

	public void setSurveyorMobNo(String surveyorMobNo) {
		this.surveyorMobNo = surveyorMobNo;
	}

/*	public List<SurveyorDetail> getSurveyorDetail() {
		return surveyorDetail;
	}

	public void setSurveyorDetail(List<SurveyorDetail> surveyorDetail) {
		this.surveyorDetail = surveyorDetail;
	}*/

	

}
