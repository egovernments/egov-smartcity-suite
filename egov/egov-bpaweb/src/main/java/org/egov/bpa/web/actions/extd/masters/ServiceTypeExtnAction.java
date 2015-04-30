package org.egov.bpa.web.actions.extd.masters;



import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.masters.ServiceTypeExtnService;
import org.egov.infra.admin.master.entity.User;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
	

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
	public String newform(){
		 return NEW;
	}
			
	@ValidationErrorPage("search")	
	@SkipValidation	
	public String modify()
	{
		serviceTypeExtn=serviceTypeExtnService.getservicetypeById(getIdTemp());
		setMode(EDIT);
		return NEW;
	}

	@ValidationErrorPage("search")
	@SkipValidation	
	public String view()
	{
		serviceTypeExtn=serviceTypeExtnService.getservicetypeById(getIdTemp());
		setMode(VIEW);
		return NEW;
	}
	
	

	@ValidationErrorPage("search")
	@SkipValidation	
	public String searchList()
	{ 
		return LIST;
	}
	
	
	
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
	
	
	

