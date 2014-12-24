package org.egov.pims.web.actions.designationMaster;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.pims.commons.DesignationMaster;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;

@ParentPackage("egov") 
public class DesignationCreateModifyAction extends BaseFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mode = "save";
	DesignationMaster designation = new DesignationMaster();
	List<DesignationMaster> designations;
	private String desName="";
	
	public String getDesName() {
		return desName;
	}

	public void setDesName(String desName) {
		this.desName = desName;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<DesignationMaster> getDesignations() {
		return designations;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return designation;
	}

	public void prepare()
	{
		designations = persistenceService.getSession().createQuery("from DesignationMaster").list();
	}

	@SkipValidation
	public String createDesignation()
	{		
		return "new";
	}	

	public void validateSaveDesignation()
	{
		
		if(designation.getDesignationName().length() == 0 || null == designation.getDesignationName())
		{
			addFieldError("designationName", "Please enter designation name");
		}

		if(designation.getDesignationDescription().length() == 0 || null == designation.getDesignationDescription())
		{
			addFieldError("designationDescription", "Please enter designation description");
		}
	}
	
	
	public void validateModifyDesignation()
	{
		if(designation.getDesignationId() == null  || designation.getDesignationId() == 0)
		{
			addFieldError("designationId", "Please select a designation");
		}
		
	}
	
	
	public void validateViewDesignation()
	{
		if(designation.getDesignationId() == null  || designation.getDesignationId() == 0)
		{
			addFieldError("designationId", "Please select a designation");
		}
		mode = "view";
		
	}
	
	@ValidationErrorPage(value=NEW)	
	public String saveDesignation()
	{
		this.persistenceService.getSession().save(designation);
		addActionMessage(getText("designation.save.message",new String[]{designation.getDesignationName()}));
		this.mode = "view";
		return "new";
	}

	public String designationNameUniqueness()
	{
		return "checkDesignationNameUniqueness";
	}

	public boolean getCheckDesignationNameUniqueness()
	{
		return checkDesignation(desName);
	}

	
	public String listDesignations()
	{
		return "search";
	}

	public String listDesignationsToView()
	{
		mode = "view";
		return "search";
	}
	
	@ValidationErrorPage("NEW")	
	public String saveModifiedDesignation()
	{
		this.persistenceService.getSession().merge(designation);
		addActionMessage(getText("designation.saveModified.message",new String[]{designation.getDesignationName()}));
		mode = "view";
		return "new";
	}

	@ValidationErrorPage("search")	
	public String modifyDesignation()
	{
		getdesignationById();
		mode = "edit";
		return "new";
	}

	@ValidationErrorPage("search")
	public String viewDesignation()
	{
		getdesignationById();
		mode="view";
		return "new";
	}

	private void getdesignationById()
	{
		Query query = persistenceService.getSession().createQuery("from DesignationMaster where designationId=:designationId");
		query.setParameter("designationId",designation.getDesignationId());
		DesignationMaster designationToModify = (DesignationMaster)query.uniqueResult();
		designation.setDesignationName(designationToModify.getDesignationName());
		designation.setDesignationDescription(designationToModify.getDesignationDescription());
		designation.setDesignationId(designationToModify.getDesignationId());
	}
	
	
	private boolean checkDesignation(String designationName)
	{
		boolean found = true;
		Query query = persistenceService.getSession().createQuery("from DesignationMaster where designationName=:designationName");
		query.setParameter("designationName",designationName);

		if (null == (DesignationMaster)query.uniqueResult())
		{
			found = false; 
		}
		return found;
	}

}
