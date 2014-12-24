package org.egov.works.web.actions.masters;  
  
 
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.masters.ScheduleCategory;

import com.opensymphony.xwork2.Action;
  
@Result(name=Action.SUCCESS, type="redirect", location = "scheduleCategory.action")
@ParentPackage("egov")  
public class ScheduleCategoryAction extends BaseFormAction{  
	
	
	  
	private PersistenceService<ScheduleCategory,Long> scheduleCategoryService;  
	private ScheduleCategory scheduleCategoryInstance = new ScheduleCategory();  
	private List<ScheduleCategory> scheduleCategoryList=null;  
	
	public String execute() {  
		return list();  
	}  
	  
	public String newform(){  
		return NEW;  
	}  
	  

	public String list() {  
	scheduleCategoryList= scheduleCategoryService.findAllBy("from ScheduleCategory sc");  
		return NEW;  
	}  
  
	public String edit(){  
	scheduleCategoryInstance=scheduleCategoryService.findById(scheduleCategoryInstance.getId(), false);  
		return EDIT;  
	}  
	
	public void prepare(){
	scheduleCategoryList= scheduleCategoryService.findAllBy("from ScheduleCategory sc");  
		super.prepare();
	}
	  
	public String save(){  
	scheduleCategoryService.update(scheduleCategoryInstance);
		return SUCCESS;  
	}  
	  
	public String create(){  
	scheduleCategoryService.create(scheduleCategoryInstance); 
	addActionMessage("The Category Code for ScheduleCategory was saved successfully");

		return list();  
	}  
	  
	public Object getModel() {  
		return scheduleCategoryInstance;  
	}  
  
	public List<ScheduleCategory> getScheduleCategoryList() {  
		return scheduleCategoryList;  
	}  
	public void setScheduleCategoryList(List<ScheduleCategory> scheduleCategoryList) {  
		this.scheduleCategoryList=scheduleCategoryList;  
	}
  
	public void setScheduleCategoryService(PersistenceService<ScheduleCategory,Long> service) {  
		this.scheduleCategoryService= service;  
	}  
	
	public PersistenceService<ScheduleCategory,Long> getScheduleCategoryService() { 
		return scheduleCategoryService;  
	}

	public ScheduleCategory getScheduleCategoryInstance() {
		return scheduleCategoryInstance;
	}

	public void setScheduleCategoryInstance(
			ScheduleCategory scheduleCategoryInstance) {
		this.scheduleCategoryInstance = scheduleCategoryInstance;
	} 
}