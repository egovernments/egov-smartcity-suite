package org.egov.tl.web.actions.masters;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.tl.domain.entity.LicenseCategory;
import org.egov.tl.domain.entity.LicenseSubCategory;
import org.egov.tl.domain.entity.UnitOfMeasurement;
import org.egov.tl.domain.service.masters.LicenseCategoryService;
import org.egov.tl.domain.service.masters.LicenseSubCategoryService;
import org.egov.tl.domain.service.masters.UnitOfMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@ParentPackage("egov")
@Results({ @Result(name = AjaxMasterAction.UNIQUECHECK, location = "ajaxMaster-uniqueCheck.jsp") })
public class AjaxMasterAction extends BaseFormAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -3409384411484334947L;
	private String name;
	private String errorMsg = "";
	private String code;
	public static final String UNIQUECHECK = "uniqueCheck";
	private static final String UOM_MASTER = "uomMaster";
	private static final String CATEGORY_MASTER = "categoryMaster";
	private static final String SUB_CATEGORY_MASTER = "subcategoryMaster";
	private static final String NAME = "name";
	private static final String CODE = "code";
	private Boolean isUnique;
	private String screenType;
	private String paramType;
	@Autowired
	@Qualifier("unitOfMeasurementService")
	private UnitOfMeasurementService unitOfMeasurementService;
	@Autowired
	@Qualifier("licenseCategoryService")
	private LicenseCategoryService licenseCategoryService;
	@Autowired
	@Qualifier("licenseSubCategoryService")
	private LicenseSubCategoryService licenseSubCategoryService;

	@Override
	public Object getModel()
	{
		return null;
	}

	/**
	 * @description : Checks whether name / code exists
	 * @return
	 */
	@Action(value = "/masters/ajaxMaster-validateActions")
	public String validateActions() {
		if (name != null && !name.isEmpty()) {
			// Invoked from UOM Master Screen - name unique check
			if (screenType != null && screenType.equalsIgnoreCase(UOM_MASTER)) {
				paramType = NAME;
				final UnitOfMeasurement unitOfMeasurement = unitOfMeasurementService.findUOMByName(name);
				if (unitOfMeasurement != null) {
					errorMsg = getText("uom.validate.duplicateName", new String[] { name });
					isUnique = Boolean.FALSE;
				} else
					isUnique = Boolean.TRUE;
			}  // Invoked from Category Master Screen - name unique check
			else if (screenType != null && screenType.equalsIgnoreCase(CATEGORY_MASTER)) {
				paramType = NAME;
				final LicenseCategory licenseCategory = licenseCategoryService.findCategoryByName(name);
				if (licenseCategory != null) {
					errorMsg = getText("lc.validate.duplicateName", new String[] { name });
					isUnique = Boolean.FALSE;
				} else
					isUnique = Boolean.TRUE;
			} // Invoked from Category Master Screen - name unique check
			else if (screenType != null && screenType.equalsIgnoreCase(SUB_CATEGORY_MASTER)) {
				paramType = NAME;
				final LicenseSubCategory licenseSubCategory = licenseSubCategoryService.findSubCategoryByName(name);
				if (licenseSubCategory != null) {
					errorMsg = getText("lsc.validate.duplicateName", new String[] { name });
					isUnique = Boolean.FALSE;
				} else
					isUnique = Boolean.TRUE;
			}


		} else if (code != null && !code.isEmpty()) {
			// Invoked from UOM Master Screen - code unique check
			if (screenType != null && screenType.equalsIgnoreCase(UOM_MASTER)) {
				paramType = CODE;
				final UnitOfMeasurement unitOfMeasurement = unitOfMeasurementService.findUOMByCode(code);
				if (unitOfMeasurement != null) {
					errorMsg = getText("uom.validate.duplicateCode", new String[] { code });
					isUnique = Boolean.FALSE;
				} else
					isUnique = Boolean.TRUE;
			} // Invoked from Category Master Screen - code unique check
			else if (screenType != null && screenType.equalsIgnoreCase(CATEGORY_MASTER)) {
				paramType = CODE;
				final LicenseCategory licenseCategory = licenseCategoryService.findCategoryByCode(code);
				if (licenseCategory != null) {
					errorMsg = getText("lc.validate.duplicateCode", new String[] { code });
					isUnique = Boolean.FALSE;
				} else
					isUnique = Boolean.TRUE;
			} // Invoked from Category Master Screen - code unique check
			else if (screenType != null && screenType.equalsIgnoreCase(SUB_CATEGORY_MASTER)) {
				paramType = CODE;
				final LicenseSubCategory licenseSubCategory = licenseSubCategoryService.findSubCategoryByCode(code);
				if (licenseSubCategory != null) {
					errorMsg = getText("lsc.validate.duplicateCode", new String[] { code });
					isUnique = Boolean.FALSE;
				} else
					isUnique = Boolean.TRUE;
			}
		} 
		return UNIQUECHECK;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(final String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getScreenType() {
		return screenType;
	}

	public void setScreenType(final String screenType) {
		this.screenType = screenType;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(final String paramType) {
		this.paramType = paramType;
	}

	public Boolean getIsUnique() {
		return isUnique;
	}

	public void setIsUnique(final Boolean isUnique) {
		this.isUnique = isUnique;
	}

}