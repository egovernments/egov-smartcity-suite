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
		
		if(designation.getName().length() == 0 || null == designation.getName())
		{
			addFieldError("designationName", "Please enter designation name");
		}

		if(designation.getDescription().length() == 0 || null == designation.getDescription())
		{
			addFieldError("designationDescription", "Please enter designation description");
		}
	}
	
	
	public void validateModifyDesignation()
	{
		if(designation.getId() == null  || designation.getId() == 0)
		{
			addFieldError("designationId", "Please select a designation");
		}
		
	}
	
	
	public void validateViewDesignation()
	{
		if(designation.getId() == null  || designation.getId() == 0)
		{
			addFieldError("designationId", "Please select a designation");
		}
		mode = "view";
		
	}
	
	@ValidationErrorPage(value=NEW)	
	public String saveDesignation()
	{
		this.persistenceService.getSession().save(designation);
		addActionMessage(getText("designation.save.message",new String[]{designation.getName()}));
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
		addActionMessage(getText("designation.saveModified.message",new String[]{designation.getName()}));
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
		query.setParameter("designationId",designation.getId());
		DesignationMaster designationToModify = (DesignationMaster)query.uniqueResult();
		designation.setName(designationToModify.getName());
		designation.setDescription(designationToModify.getDescription());
		//designation.setDesignationId(designationToModify.getId());
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
