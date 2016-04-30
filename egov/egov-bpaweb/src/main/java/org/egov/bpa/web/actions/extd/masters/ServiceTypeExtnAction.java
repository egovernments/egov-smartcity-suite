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
package org.egov.bpa.web.actions.extd.masters;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.masters.ServiceTypeExtnService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
	
@Transactional(readOnly = true)
@Namespace("/masters")
@ParentPackage("egov")
@SuppressWarnings("serial")
public class ServiceTypeExtnAction extends BaseFormAction
{
	private ServiceTypeExtn serviceTypeExtn=new ServiceTypeExtn();  
	
	private String mode;
	private String servicecode;
	private Long idTemp;
	private List<ServiceTypeExtn> servicetypeExtnList;
	private  ServiceTypeExtnService serviceTypeExtnService;
	public static final String LIST = "list";
	public static final String VIEW = "view";
	
	public ServiceTypeExtnAction(){
		addRelatedEntity("modifiedBy", User.class);
		addRelatedEntity("createdBy",User.class);
	}
	
	public ServiceTypeExtn getModel() {
		// TODO Auto-generated method stub
		return serviceTypeExtn;
	}
	
	public void prepare() {        
		super.prepare();
		addDropdownData("servicetypeList", persistenceService.findAllBy(" from ServiceTypeExtn order by code "));
	}
	
	@SkipValidation
	@Action(value = "/serviceTypeExtn-newform", results = { @Result(name = NEW,type = "dispatcher") })
	public String newform(){
		 return NEW;
	}
			
	@ValidationErrorPage("search")	
	@SkipValidation	
	@Action(value = "/serviceTypeExtn-modify", results = { @Result(name = NEW,type = "dispatcher") })
	public String modify()
	{
		serviceTypeExtn=serviceTypeExtnService.getservicetypeById(getIdTemp());
		setMode(EDIT);
		return NEW;
	}

	@ValidationErrorPage("search")
	@SkipValidation	
	@Action(value = "/serviceTypeExtn-view", results = { @Result(name = NEW,type = "dispatcher") })
	public String view()
	{
		serviceTypeExtn=serviceTypeExtnService.getservicetypeById(getIdTemp());
		setMode(VIEW);
		return NEW;
	}
	
	

	@ValidationErrorPage("search")
	@SkipValidation	
	@Action(value = "/serviceTypeExtn-searchList", results = { @Result(name = LIST,type = "dispatcher") })
	public String searchList()
	{ 
		return LIST;
	}
	
	@Transactional
	@Action(value = "/serviceTypeExtn-create", results = { @Result(name = NEW,type = "dispatcher") })
	public String create()
	{    
		serviceTypeExtn=serviceTypeExtnService.save(serviceTypeExtn);
		if(getMode().equals(EDIT))
			addActionMessage("Service Type  "   +serviceTypeExtn.getCode()+   "   Updated Successfully");
		else
			addActionMessage("Service Type  "   +serviceTypeExtn.getCode()+    "  Created Successfully");
		setMode(VIEW);
		return NEW;	

	}
	
	
	@SkipValidation
	public String codeUniqueCheck(){
		return "codeUniqueCheck" ;
	} 	
	public boolean getCodeUniqueCheck() throws Exception
	{
		return serviceTypeExtnService.checkCode(servicecode,idTemp);
	}
	
	
	private String getMessage(String key) {
		// TODO Auto-generated method stub
		return getText(key);
	}
	
	
	@ValidationErrorPage("searchList")
	public void validate()
	{
		if(serviceTypeExtn.getCode()==null || "".equals(serviceTypeExtn.getCode())){
			addFieldError("CODE", getMessage("serviceType.servicecode.required"));
		}
		else if(serviceTypeExtn.getCode()!=null && !"".equals(serviceTypeExtn.getCode())){
			boolean isCodeAlreadyExist=serviceTypeExtnService.checkCode(serviceTypeExtn.getCode(),serviceTypeExtn.getId());
			if(isCodeAlreadyExist)
				addFieldError("codeAlreadyExist", getMessage("service.code.exists"));
		 }
		if(serviceTypeExtn.getDescription()==null || "".equals(serviceTypeExtn.getDescription())){
			addFieldError("ServiceDescription", getMessage("serviceType.description.required"));
		}

		if(serviceTypeExtn.getServiceNumberPrefix()==null || "".equals(serviceTypeExtn.getServiceNumberPrefix())){
			addFieldError("ServiceNumberPrefix", getMessage("serviceType.ServiceNumberPrefix.required"));
		}

	}
	public String getServicecode() {
		return servicecode;
	}

	
	public void setServicecode(String servicecode) {
		this.servicecode = servicecode;
	}
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Long getIdTemp() {
		return idTemp;
	}

	public void setIdTemp(Long idTemp) {
		this.idTemp = idTemp;
	}

	public ServiceTypeExtn getServiceTypeExtn() {
		return serviceTypeExtn;
	}

	public void setServiceTypeExtn(ServiceTypeExtn serviceTypeExtn) {
		this.serviceTypeExtn = serviceTypeExtn;
	}

	public List<ServiceTypeExtn> getServicetypeExtnList() {
		return servicetypeExtnList;
	}

	public void setServicetypeExtnList(List<ServiceTypeExtn> servicetypeExtnList) {
		this.servicetypeExtnList = servicetypeExtnList;
	}

	public ServiceTypeExtnService getServiceTypeExtnService() {
		return serviceTypeExtnService;
	}

	public void setServiceTypeExtnService(
			ServiceTypeExtnService serviceTypeExtnService) {
		this.serviceTypeExtnService = serviceTypeExtnService;
	}

	

	
	
}
	
	
	

