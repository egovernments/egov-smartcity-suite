/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.web.actions.masters;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.services.masters.SubSchemeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.exception.ConstraintViolationException;

@ParentPackage("egov")
public class SubSchemeAction extends BaseFormAction{
	private SubScheme subScheme = new SubScheme();
	private boolean isActive = false;
	private boolean clearValues = false;
	private int fundId;
	private int schemeId;
	private List<SubScheme> subSchemeList;
	private String showMode="view";
	private SubSchemeService subSchemeService;
	
	@Override
	public Object getModel() {
		return subScheme;
	}
	
	public SubSchemeAction() {
		addRelatedEntity("scheme", Scheme.class,"name");
		addRelatedEntity("department", Department.class,"deptName");
		addRelatedEntity("createdBy",User.class);
	}
	
	@Override
	public void prepare() {
		super.prepare();
		setupDropdownDataExcluding();
		dropdownData.put("schemeList", persistenceService.findAllBy("from Scheme where isActive=1 order by name"));
	}
	
	@Override
	public String execute() throws Exception {
		return NEW;
	}
	@ValidationErrorPage(value="new")
	public String save(){
		if(isActive)
			subScheme.setIsactive(true);
		else
			subScheme.setIsactive(false);

		/*if (!showMode.equals("") && showMode.equals("view")) {
			subScheme.setCreatedDate(new Date());
			subScheme.setCreatedBy(getLoggedInUser());
			subScheme.setLastmodifieddate(new Date());
		} else {
			subScheme.setLastModifiedBy(getLoggedInUser());
			subScheme.setLastmodifieddate(new Date());
		}*/// Need to fix phoenix migration
		validatemandatoryFields();
		try {
			subSchemeService.persist(subScheme);
			subSchemeService.getSession().flush();
		}catch (ValidationException e) {
			throw e;
		}catch (ConstraintViolationException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("duplicate.subscheme","duplicate.subscheme")));
		}catch (Exception e) {
			throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator","An error occured contact Administrator")));
		}
		clearValues = true;		
		if(showMode.equals("edit"))
			return "view";
		else
		{
			addActionMessage(getText("subscheme.saved.successfully"));
			return NEW;
		}
	}

	private void validatemandatoryFields() {
		if (subScheme.getScheme() == null || subScheme.getScheme().getId() == null
				|| subScheme.getScheme().getId() == -1)
			throw new ValidationException(Arrays.asList(new ValidationError("scheme.mandatory", "scheme.mandatory")));
		if (subScheme.getName() == null || "".equals(subScheme.getName()))
			throw new ValidationException(Arrays.asList(new ValidationError("subscheme.name.mandatory",
					"subscheme.name.mandatory")));
		if (subScheme.getCode() == null || "".equals(subScheme.getCode()))
			throw new ValidationException(Arrays.asList(new ValidationError("subscheme.code.mandatory",
					"subscheme.code.mandatory")));
		if (subScheme.getValidfrom() == null)
			throw new ValidationException(Arrays.asList(new ValidationError("subscheme.validfrom.mandatory",
					"subscheme.validfrom.mandatory")));
		if (subScheme.getValidto() == null)
			throw new ValidationException(Arrays.asList(new ValidationError("subscheme.validto.mandatory","subscheme.validto.mandatory")));
		if (subScheme.getValidfrom().compareTo(subScheme.getValidto()) > 0)
			throw new ValidationException(Arrays.asList(new ValidationError("subscheme.invalid.dates",
					"subscheme.invalid.dates")));
	}

	public void setSubScheme(SubScheme subScheme) {
		this.subScheme = subScheme;
	}
	public SubScheme getSubScheme() {
		return subScheme;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setClearValues(boolean clearValues) {
		this.clearValues = clearValues;
	}

	public boolean isClearValues() {
		return clearValues;
	}
@Action(value="/masters/subScheme-edit")
	public String edit()
	{
		showMode="edit";
		beforeSearch();
		return "search";
	}
@Action(value="/masters/subScheme-beforeSearch")
	public String beforeSearch() {

		dropdownData.put("fundList", persistenceService
				.findAllBy("from Fund where isActive=1 order by name"));
		dropdownData.put("schemeList", Collections.emptyList());
		dropdownData.put("subSchemeList", Collections.emptyList());
		fundId=0;
		return "search";
	}

	public String search() {
		StringBuffer query = new StringBuffer(500);
		StringBuffer params = new StringBuffer(100);
		query.append("From SubScheme s ");
		if (fundId != 0) {
			query.append("where s.scheme.fund.id= " + fundId);
			// params.append(""+fundId);

			if (schemeId != -1) {
				query.append("and  s.scheme.id= " + schemeId);
				// params.append(","+schemeId);

				if (subScheme.getId() != -1) {
					query.append("and s.id=" + subScheme.getId());
					// params.append(","+subSchemeId);
				}
			}
		}
	    loadDropDowns();
		subSchemeList = persistenceService.findAllBy(query.toString());
		return "search";
	}

@Action(value="/masters/subScheme-viewSubScheme")
	public String viewSubScheme() {
		subScheme = (SubScheme) persistenceService.find("from SubScheme where id=?", subScheme.getId());
		return "view";
	}

	private void loadDropDowns() {
		
		dropdownData.put("fundList", persistenceService
				.findAllBy("from Fund where isActive=1 order by name"));
		StringBuffer st = new StringBuffer();
		
		if (fundId != 0) {
			
			st.append("from Scheme where isactive=1 and fund.id=");
			st.append(fundId);
			dropdownData.put("schemeList", persistenceService.findAllBy(st
					.toString()));
			st.delete(0, st.length()-1);

		} else
			dropdownData.put("schemeList", Collections.emptyList());
		if (schemeId != -1) {
	
			dropdownData.put("subSchemeList", persistenceService.findAllBy("from SubScheme where isactive=1 and scheme.id=?",schemeId));

		} else
			dropdownData.put("subSchemeList", Collections.emptyList());
			
	} 

	private User getLoggedInUser() {
		return (User) persistenceService.getSession().load(User.class,EGOVThreadLocals.getUserId());
	}
	
	public void setFundId(int fundId) {
		this.fundId = fundId;
	}

	public int getFundId() {
		return fundId;
	}

	public void setSchemeId(int schemeId) {
		this.schemeId = schemeId;
	}

	public int getSchemeId() {
		return schemeId;
	}

	public void setSubSchemeList(List<SubScheme> subSchemeList) {
		this.subSchemeList = subSchemeList;
	}

	public List<SubScheme> getSubSchemeList() {
		return subSchemeList;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	public String getShowMode() {
		return showMode;
	}

	public SubSchemeService getSubSchemeService() {
		return subSchemeService;
	}

	public void setSubSchemeService(SubSchemeService subSchemeService) {
		this.subSchemeService = subSchemeService;
	}
	
}

