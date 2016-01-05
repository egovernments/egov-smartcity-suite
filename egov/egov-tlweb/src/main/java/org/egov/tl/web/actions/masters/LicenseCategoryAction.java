package org.egov.tl.web.actions.masters;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.domain.entity.LicenseCategory;
import org.egov.tl.domain.service.masters.LicenseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
			setLicenseCategoryMap(getFormattedCategoryMap(licenseCategoryService.findAllBy("from LicenseCategory order by id")));
		if (getId() != null)
			licenseCategory = licenseCategoryService.find("from LicenseCategory where id=?", getId());
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
			licenseCategory = licenseCategoryService.persist(licenseCategory);
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