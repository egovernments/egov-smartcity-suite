/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tl.web.actions.masters;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.service.masters.LicenseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@ParentPackage("egov")
@Results({ @Result(name = LicenseCategoryAction.NEW, location = "licenseCategory-new.jsp"),
	@Result(name = LicenseCategoryAction.SEARCH, location = "licenseCategory-search.jsp"),
	@Result(name = LicenseCategoryAction.EDIT, location = "licenseCategory-edit.jsp") })
public class LicenseCategoryAction extends BaseFormAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 6242612156153747913L;
	private LicenseCategory licenseCategory = new LicenseCategory();
	private Long id;
	public static final String SEARCH = "search";
	public static final String VIEW = "view";
	private Map<Long, String> licenseCategoryMap;

	@Autowired
	@Qualifier("licenseCategoryService")
	private LicenseCategoryService licenseCategoryService;

	private static final Logger LOGGER = Logger.getLogger(LicenseCategoryAction.class); 

	// UI field
	private String userMode;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return licenseCategory;
	}

	@Override
	public void prepare() {
		// IN Modify and View Mode Load category dropdown.
		if (userMode != null && !userMode.isEmpty() && (userMode.equalsIgnoreCase(EDIT) || userMode.equalsIgnoreCase(VIEW)))
			setLicenseCategoryMap(getFormattedCategoryMap(licenseCategoryService.findAll()));
		if (getId() != null)
			licenseCategory = licenseCategoryService.findById(getId());
	}

	/**
	 * @param licenseCategoryList
	 * @return
	 */
	public static Map<Long, String> getFormattedCategoryMap(final List<LicenseCategory> licenseCategoryList) {
		final Map<Long, String> categoryMap = new TreeMap<Long, String>();
		for (final LicenseCategory licenseCategory : licenseCategoryList)
			categoryMap.put(licenseCategory.getId(),
					licenseCategory.getName().concat(" ~ ").concat(licenseCategory.getCode()));
		return categoryMap;
	}

	/**
	 * This method is invoked to create a new form.
	 *
	 * @return a <code>String</code> representing the value 'NEW'
	 */
	@Action(value = "/masters/licenseCategory-newform")
	public String newform() {
		if (userMode != null && !userMode.isEmpty()) {
			if (userMode.equalsIgnoreCase(VIEW))
				userMode = VIEW;
			else if (userMode.equalsIgnoreCase(EDIT))
				userMode = EDIT;
		}
		else
			userMode = NEW;
		return NEW;
	}

	/**
	 * This method is invoked to Edit a form.
	 *
	 * @return a <code>String</code> representing the value 'SEARCH'
	 */
	@Action(value = "/masters/licenseCategory-edit")
	public String edit() {
		if (userMode.equalsIgnoreCase(EDIT))
			userMode = EDIT;
		else if (userMode.equalsIgnoreCase(VIEW))
			userMode = VIEW;
		return SEARCH;
	}

	/**
	 * @return
	 * @throws NumberFormatException
	 * @throws ApplicationException
	 */
	@ValidationErrorPage(value = EDIT)
	@Action(value = "/masters/licenseCategory-save")
	public String save() throws NumberFormatException, ApplicationException {
		try {
			licenseCategory = licenseCategoryService.create(licenseCategory);
			//licenseCategoryService.findById(licenseCategory.getId(), false);
		} catch (final ValidationException valEx) {
			LOGGER.error("Exception found while persisting License category: " + valEx.getErrors());
			throw new ValidationException(valEx.getErrors());
		}
		if (userMode.equalsIgnoreCase(NEW))
			addActionMessage("\'" + licenseCategory.getCode() + "\' " + getText("license.category.save.success"));
		else if (userMode.equalsIgnoreCase(EDIT))
			addActionMessage("\'" + licenseCategory.getCode() + "\' " + getText("license.category.edit.success"));
		userMode = SUCCESS;
		return NEW;
	}

	public LicenseCategory getLicenseCategory() { 
		return licenseCategory;
	}

	public void setLicenseCategory(final LicenseCategory licenseCategory) {
		this.licenseCategory = licenseCategory;
	}

	public String getUserMode() {
		return userMode;
	}

	public void setUserMode(final String userMode) {
		this.userMode = userMode;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Map<Long, String> getLicenseCategoryMap() {
		return licenseCategoryMap;
	}

	public void setLicenseCategoryMap(final Map<Long, String> licenseCategoryMap) {
		this.licenseCategoryMap = licenseCategoryMap;
	}

}