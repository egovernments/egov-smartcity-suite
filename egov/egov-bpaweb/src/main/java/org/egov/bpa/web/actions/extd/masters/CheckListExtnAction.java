package org.egov.bpa.web.actions.extd.masters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.models.extd.masters.CheckListDetailsExtn;
import org.egov.bpa.models.extd.masters.ChecklistExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.masters.CheckListExtnSevice;
import org.egov.bpa.utils.CheckList;
import org.egov.infra.admin.master.entity.User;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

@SuppressWarnings("serial")
@ParentPackage("egov")
public class CheckListExtnAction extends BaseFormAction
{    
	public static final String LIST = "list";
	public static final String VIEW = "view";
	public static final String SAVE = "save";
	private ChecklistExtn checkList=new ChecklistExtn();
	private Long id;
	private String mode;
	private Long bpafeeId;

	 private String CheckId;
	 private Long ServiceId;
	// private String checkCode;
	
	//private Long checkListId;
	private CheckListExtnSevice checkListExtnService;

	private List<CheckListDetailsExtn> checkListDetailsList = new ArrayList<CheckListDetailsExtn>(0);
	
	
	public CheckListExtnAction()
	{    
		addRelatedEntity("modifiedBy", User.class);
		addRelatedEntity("createdBy",User.class);
		addRelatedEntity("serviceType",ServiceTypeExtn.class);
		//addRelatedEntity("checkList",	Checklist.class);
		
		
	}
	
	@SkipValidation
	public String newform()
	{    
		if(validateCheckListType()){
			addActionMessage("For Selected CheckList Type And Service Type Description is Available ...");
			return "list";
	
		}
		checkListDetailsList.add(new CheckListDetailsExtn());
		setMode(SAVE);
		return NEW;
	}
	

	@SkipValidation	
	public String search()
	{ 
		return LIST;
	}
	

	
	@ValidationErrorPage(LIST)
	@SkipValidation	
	public String view()
	{
		if(!validateCheckListType())
		{
			addActionMessage("For Selected CheckList Type And Service Type Discription is Not Available ....");
			return "list";
		}
		buildChecklistDetail();
		setMode(VIEW);
		return NEW;
	}

	@Override
	public ChecklistExtn getModel() {
		// TODO Auto-generated method stub
		return checkList;
	}
	
	    @ValidationErrorPage(LIST)	
		@SkipValidation	
		public String modify()
		{  
	    	
	    	if(!validateCheckListType())
			{
				addActionMessage("For Selected CheckList Type And Service Type Discription is Not Available ...");
				return "list";
			}
	    	buildChecklistDetail();
			setMode(EDIT);
			return NEW;
		}
	    
	 
		
	
	
	//@SkipValidation
	public Boolean validateCheckListType()
	{
		ChecklistExtn relation= null;
		boolean isCodeAlreadyExist=Boolean.FALSE;
		if(checkList.getId()==null){
		
		
		if(checkList.getChecklistType()!=null && checkList.getServiceType().getId()!=null || checkList.getChecklistType()=="-1" && checkList.getServiceType().getId()==-1 )
		{
			isCodeAlreadyExist=checkListExtnService.checkCode(checkList.getChecklistType(),checkList.getServiceType().getId());
          
		}
		return isCodeAlreadyExist;
		
		}
		else
		{
			relation=(ChecklistExtn)persistenceService.find("from org.egov.bpa.models.extd.masters.ChecklistExtn where id=? " ,id);
		if(!	relation.getChecklistType().equals(checkList.getChecklistType()) || ! relation.getServiceType().getId().equals(checkList.getServiceType().getId()))
		{
			return validateCheckListTypemodify();
		}
		}
		return isCodeAlreadyExist;
	}

	
	
	public boolean validateCheckListTypemodify()
	{

		boolean isCodeAlreadyExist=Boolean.FALSE;
		if(checkList.getChecklistType()!=null && checkList.getServiceType().getId()!=null || checkList.getChecklistType()=="-1" && checkList.getServiceType().getId()==-1 )
		{
			isCodeAlreadyExist=checkListExtnService.checkCode(checkList.getChecklistType(),checkList.getServiceType().getId());
       
		}if(isCodeAlreadyExist)
		{
			addFieldError("CODE",getMessage("bpachecklist.exists"));
		}
		return isCodeAlreadyExist;
	}
	
	public ChecklistExtn buildChecklistDetail()
	{
		
		checkListDetailsList.clear();
		
		checkList=updatecheck(checkList.getChecklistType(),checkList.getServiceType().getId());
	
		checkListDetailsList = new ArrayList<CheckListDetailsExtn>(checkList.getCheckListDetailsSet());
		
		return checkList;
	}
     //get values from search jsp to new jsp
	public ChecklistExtn updatecheck(String CheckId,Long ServiceId){
		ChecklistExtn relation = null;
		
		if(CheckId!=null && !"".equals(CheckId) && ServiceId!=null && !"".equals(ServiceId))
			relation =(ChecklistExtn)persistenceService.find("from org.egov.bpa.models.extd.masters.ChecklistExtn where checklistType=? and serviceType.id=? " ,CheckId,ServiceId);

		return relation;

			
		
	}

	@ValidationErrorPage(LIST)
	public void validate()
	{
		List<String> temp=new ArrayList<String>();
		temp.add(null);
		checkListDetailsList.removeAll(temp);
		if(validateCheckListType() && checkList.getId()==null)
		{
			addFieldError("CODE", getMessage("bpachecklist.exists"));
		}
	 if(checkList.getChecklistType()=="-1")
	 {
		 addFieldError("CODE", getMessage("checkList.ChecklistType.required"));
	 }
	
	if(checkList.getServiceType().getId()==null || checkList.getServiceType().getId()==-1 ){
		 addFieldError("CODE", getMessage("checkList.serviceType.required"));
		}
    
	
	int i=1;
		for(CheckListDetailsExtn unitDetail: checkListDetailsList) {
			if(unitDetail!=null){
				if(unitDetail.getCode()==null ||"".equals(unitDetail.getCode().trim())|| unitDetail.getDescription()==null || "".equals(unitDetail.getDescription().trim()))
					
						addFieldError("checklist.detail",getMessage("checklist.details.required")+" "+i);
				i++;
			}
		}
	
		
		
	}

	
	 private String getMessage(String key) {
	     // TODO Auto-generated method stub
	      return getText(key);
     }
	
	 
	public String create()
	{
		for(CheckListDetailsExtn unitDetail: checkListDetailsList) {
			if(unitDetail!=null){
				if(unitDetail.getIsActive()==null)
					unitDetail.setIsActive(false);
				if(unitDetail.getIsMandatory()==null)
					unitDetail.setIsMandatory(false);
				
			}
		}
		
		
		checkList=checkListExtnService.save(checkList,checkListDetailsList);
			
	if(getMode().equals(EDIT))
			addActionMessage("Check List Type Updated Successfully");
		else
			addActionMessage("Check List Type Created Successfully");
		setMode(VIEW);
		return NEW;
	}
	
	public void prepare()
	{
		super.prepare();
		 addDropdownData("servicetypeList", persistenceService.findAllBy("from ServiceTypeExtn order by code"));
		 //addDropdownData("checkIdList", persistenceService.findAllBy("from Checklist order by id"));
		 addDropdownData("checkIdList",Arrays.asList(CheckList.values()));
		
    }
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Long getBpafeeId() {
		return bpafeeId;
	}

	public void setBpafeeId(Long bpafeeId) {
		this.bpafeeId = bpafeeId;
	}

	public ChecklistExtn getCheckList() {
		return checkList;
	}

	public void setCheckList(ChecklistExtn checkList) {
		this.checkList = checkList;
	}

	public CheckListExtnSevice getCheckListExtnService() {
		return checkListExtnService;
	}

	public void setCheckListExtnService(CheckListExtnSevice checkListExtnService) {
		this.checkListExtnService = checkListExtnService;
	}

	public List<CheckListDetailsExtn> getCheckListDetailsList() {
		return checkListDetailsList;
	}

	public void setCheckListDetailsList(
			List<CheckListDetailsExtn> checkListDetailsList) {
		this.checkListDetailsList = checkListDetailsList;
	}

	


/*
	public Long getCheckId() {
		return checkId;
	}

	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
*/

	
}
