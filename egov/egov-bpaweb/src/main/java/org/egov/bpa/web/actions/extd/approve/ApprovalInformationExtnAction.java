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
package org.egov.bpa.web.actions.extd.approve;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegnApprovalInformationExtn;
import org.egov.bpa.models.extd.masters.ChangeOfUsageExtn;
import org.egov.bpa.services.extd.approve.ApprovalInformationExtnService;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Transactional(readOnly = true)
@Namespace("/approve")
@SuppressWarnings("serial")
@ParentPackage("egov")
public class ApprovalInformationExtnAction extends BaseFormAction{

	private RegnApprovalInformationExtn approveInfo=new RegnApprovalInformationExtn();
	private Long registrationId;
	private  RegisterBpaExtnService registerBpaExtnService;
	private  BpaCommonExtnService BpaCommonExtnService;
	private RegistrationExtn registrationObj;
	private ApprovalInformationExtnService approvaInfoExtnService;
	private Map<Integer, String> approvalTypeMap=new HashMap<Integer, String>();
	private String mode;
	
	private String fromreg;
	private Date applicationdate;
	private Long regnApprovalInfoId;

	public Object getModel() {
		return approveInfo;
	}

	public ApprovalInformationExtnAction() {
		addRelatedEntity("usageFrom", ChangeOfUsageExtn.class);
		addRelatedEntity("usageTo", ChangeOfUsageExtn.class);
		addRelatedEntity("registration", RegistrationExtn.class);
		addRelatedEntity("createdBy", User.class);

	}
	
	public Map<Integer, String> getApprovalTypeMap() {

		approvalTypeMap = approvaInfoExtnService.getApprovalTypeMap();
		return approvalTypeMap;

	}
	
	@SkipValidation
	public void prepare()
	{
		super.prepare();
		
		if(getRegistrationId()!=null)
		{
			registrationObj=registerBpaExtnService.getRegistrationById(registrationId);
			if(registrationObj!=null && getRegistrationObj().getApprovalInfoSet()!=null){
			for(RegnApprovalInformationExtn regnApprovalInfo: getRegistrationObj().getApprovalInfoSet())
			{
				approveInfo=regnApprovalInfo;
			}
			}
			//approveInfo=approvaInfoService.getRegnApprovalInfobyRegistrationId(registrationId);
			
			setApplicationdate(getRegistrationObj().getPlanSubmissionDate());
			if(approveInfo!=null ){
				approveInfo.setRegistration(registrationObj);
			}
		}
		if(approveInfo!=null ){
			setRegnApprovalInfoId(approveInfo.getId());
		}
		addDropdownData("usageList", BpaCommonExtnService.getChangeOfUse());
		
		
	}
	@SkipValidation
	@Action(value = "/approvalInformationExtn-newForm", results = { @Result(name = NEW ,type = "dispatcher") })
	public String newForm(){
		if(getRegnApprovalInfoId()!=null)
		{
			approveInfo=approvaInfoExtnService.getRegnApprovalInfobyId(getRegnApprovalInfoId());
			setMode("modify");
			return NEW;
		}
		return NEW;
	}
	@ValidationErrorPage(NEW)
	@Override
	public void validate()
	{
		if(null==approveInfo.getCommApprovalDate() ||"".equals(approveInfo.getCommApprovalDate())){
			addFieldError("approvalinfo.approvalDate", getMessage("approval.approvalDate.required"));
		}
		if(null==approveInfo.getUsageFrom().getId() ){
			addFieldError("approvalinfo.usageFrom", getMessage("approval.usageFrom.required"));
		}
		if(null==approveInfo.getUsageTo().getId() ){
			addFieldError("approvalinfo.usageTo", getMessage("approval.usageTo.required"));
		}
		if(null==approveInfo.getApprovalType() || approveInfo.getApprovalType() == -1){
			addFieldError("approvalinfo.noteApprovalType", getMessage("approval.noteApprovalType.required"));
		}
		if(approveInfo.getIsForwardToCmda()==null)
		{
			addFieldError("approvalinfo.isforwordedtocmda", getMessage("approval.isforwordedtocmda.required"));
		}
		if(approveInfo.getIsForwardToCmda()==Boolean.TRUE){
		if(null==approveInfo.getDateOfForward() || "".equals(approveInfo.getDateOfForward())){
			addFieldError("approvalinfo.DateOfForWard", getMessage("approval.DateOfForWard.required"));
		}
		}
		if(approveInfo.getUsageTo().equals(approveInfo.getUsageFrom())){
			addFieldError("approvalinfo.usagefrom", getMessage("approval.usagefromandusageToarenotsame"));
		}
	}
	
	private String getMessage(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	@ValidationErrorPage(NEW)
	@Transactional
	@Action(value = "/approvalInformationExtn-create", results = { @Result(name = NEW,type = "dispatcher") })
	public String create()
	{
		approvaInfoExtnService.save(approveInfo);
		setMode(BpaConstants.MODEVIEW);
		return NEW;
		
	}

	public Long getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}
	
	
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getFromreg() {
		return fromreg;
	}
	public void setFromreg(String fromreg) {
		this.fromreg = fromreg;
	}
	public Date getApplicationdate() {
		return applicationdate;
	}
	public void setApplicationdate(Date applicationdate) {
		this.applicationdate = applicationdate;
	}
	public Long getRegnApprovalInfoId() {
		return regnApprovalInfoId;
	}
	public void setRegnApprovalInfoId(Long regnApprovalInfoId) {
		this.regnApprovalInfoId = regnApprovalInfoId;
	}

	public RegnApprovalInformationExtn getApproveInfo() {
		return approveInfo;
	}

	public void setApproveInfo(RegnApprovalInformationExtn approveInfo) {
		this.approveInfo = approveInfo;
	}

	public RegisterBpaExtnService getRegisterBpaExtnService() {
		return registerBpaExtnService;
	}

	public void setRegisterBpaExtnService(
			RegisterBpaExtnService registerBpaExtnService) {
		this.registerBpaExtnService = registerBpaExtnService;
	}


	public BpaCommonExtnService getBpaCommonExtnService() {
		return BpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonExtnService) {
		BpaCommonExtnService = bpaCommonExtnService;
	}

	public RegistrationExtn getRegistrationObj() {
		return registrationObj;
	}

	public void setRegistrationObj(RegistrationExtn registrationObj) {
		this.registrationObj = registrationObj;
	}

	public ApprovalInformationExtnService getApprovaInfoExtnService() {
		return approvaInfoExtnService;
	}

	public void setApprovaInfoExtnService(
			ApprovalInformationExtnService approvaInfoExtnService) {
		this.approvaInfoExtnService = approvaInfoExtnService;
	}

	public void setApprovalTypeMap(Map<Integer, String> approvalTypeMap) {
		this.approvalTypeMap = approvalTypeMap;
	}

	
	
}
