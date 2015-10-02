package org.egov.tl.web.actions.masters;

import java.util.HashMap;
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
import org.egov.tl.domain.entity.LicenseSubCategory;
import org.egov.tl.domain.service.masters.LicenseCategoryService;
import org.egov.tl.domain.service.masters.LicenseSubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({ @Result(name = LicenseSubCategoryAction.NEW, location = "licenseSubCategory-new.jsp"),
    @Result(name = LicenseSubCategoryAction.SEARCH, location = "licenseSubCategory-search.jsp"),
    @Result(name = LicenseSubCategoryAction.EDIT, location = "licenseSubCategory-edit.jsp") })
public class LicenseSubCategoryAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = 6242612156153747913L;
    private LicenseSubCategory subCategory = new LicenseSubCategory();
    private Long id;
    private Long categoryId;
    public static final String SEARCH = "search";
    public static final String VIEW = "view";
    private Map<Long, String> licenseCategoryMap;
    private Map<Long, String> licenseSubCategoryMap;

    @Autowired
    private LicenseSubCategoryService licenseSubCategoryService;
    @Autowired
    private LicenseCategoryService licenseCategoryService;

    private static final Logger LOGGER = Logger.getLogger(LicenseSubCategoryAction.class);
    
    // UI field
    private String userMode;

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return subCategory;
    }

    @Override
    public void prepare() {
        
        setLicenseCategoryMap(getFormattedCategoryMap(licenseCategoryService.findAllBy("from LicenseCategory order by id")));
        // In Modify and View Mode Load category dropdown.
        if (userMode != null && !userMode.isEmpty() && (userMode.equalsIgnoreCase(EDIT) || userMode.equalsIgnoreCase(VIEW)))
            setLicenseSubCategoryMap(getFormattedSubCategoryMap(licenseSubCategoryService.findAllBy("from org.egov.tl.domain.entity.LicenseSubCategory order by id")));
        if (getId() != null){
            subCategory = licenseSubCategoryService.find("from org.egov.tl.domain.entity.LicenseSubCategory where id=?", getId());
            setCategoryId(subCategory.getCategory().getId());
        }
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
     * @param licenseSubCategoryList
     * @return
     */
    public static Map<Long, String> getFormattedSubCategoryMap(final List<LicenseSubCategory> licenseSubCategoryList) {
        final Map<Long, String> subCategoryMap = new TreeMap<Long, String>();
        for (final LicenseSubCategory licenseSubCategory : licenseSubCategoryList)
            subCategoryMap.put(licenseSubCategory.getId(),
                    licenseSubCategory.getName().concat(" ~ ").concat(licenseSubCategory.getCode()));
        return subCategoryMap;
    }

    /**
     * This method is invoked to create a new form.
     *
     * @return a <code>String</code> representing the value 'NEW'
     */
    @Action(value = "/masters/licenseSubCategory-newform")
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
    @Action(value = "/masters/licenseSubCategory-edit")
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
    @Action(value = "/masters/licenseSubCategory-save")
    public String save() throws NumberFormatException, ApplicationException {
        try {
            if(categoryId!=null){
                subCategory.setCategory(licenseCategoryService.find("from LicenseCategory where id=?", categoryId));
            }
            subCategory = licenseSubCategoryService.persist(subCategory);
        } catch (final ValidationException valEx) {
            LOGGER.error("Exception found while persisting License category: " + valEx.getErrors());
            throw new ValidationException(valEx.getErrors());
        }
        addActionMessage("\'" + subCategory.getCode() + "\' " + getText("license.subcategory.save.success"));
        id = subCategory.getId();
        return NEW;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Map<Long, String> getLicenseCategoryMap() {
        return licenseCategoryMap;
    }

    public void setLicenseCategoryMap(Map<Long, String> licenseCategoryMap) {
        this.licenseCategoryMap = licenseCategoryMap;
    }

    public Map<Long, String> getLicenseSubCategoryMap() {
        return licenseSubCategoryMap;
    }

    public void setLicenseSubCategoryMap(Map<Long, String> licenseSubCategoryMap) {
        this.licenseSubCategoryMap = licenseSubCategoryMap;
    }

    public LicenseSubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(LicenseSubCategory subCategory) {
        this.subCategory = subCategory;
    }


}
