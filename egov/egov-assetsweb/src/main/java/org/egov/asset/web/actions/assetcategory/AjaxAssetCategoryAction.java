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
package org.egov.asset.web.actions.assetcategory;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.service.AssetCategoryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;

import java.util.List;

@ParentPackage("egov")
@Results({
        @Result(name = AjaxAssetCategoryAction.PARENT_CATEGORIES, location = "ajaxAssetCategory-parentcategories.jsp"),
        @Result(name = AjaxAssetCategoryAction.ASSET_CAT_DETAILS, location = "ajaxAssetCategory-assetcatdetails.jsp") })
public class AjaxAssetCategoryAction extends BaseFormAction {

    private static final long serialVersionUID = -8703869606104325609L;
    private static final Logger LOGGER = Logger.getLogger(AjaxAssetCategoryAction.class);
    public static final String PARENT_CATEGORIES = "parentcategories";
    public static final String ASSET_CAT_DETAILS = "assetcatdetails";
    private String assetType; // Set by Ajax call
    private Long parentCatId; // Set by Ajax call
    private AssetCategory parentCategory;
    private List<AssetCategory> assetCategoryList;
    private AssetCategoryService assetCategoryService;

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    @Action(value = "/assetcategory/ajaxAssetCategory-populateParentCategories")
    public String populateParentCategories() {
        try {
            if (assetType.equalsIgnoreCase("") || assetType.equalsIgnoreCase("-1"))
                assetCategoryList = assetCategoryService.findAll();
            else
                assetCategoryList = assetCategoryService.getAllAssetCategoryByAssetType(assetType);
        } catch (final Exception e) {
            LOGGER.error(
                    "Error while loading assetCategoryList in populateParentCategories() method " + e.getMessage());
            throw new ApplicationRuntimeException(e.getMessage());
        }
        return PARENT_CATEGORIES;
    }

    @Action(value = "/assetcategory/ajaxAssetCategory-populateParentDetails")
    public String populateParentDetails() {
        parentCategory = assetCategoryService.findById(parentCatId, false);
        return ASSET_CAT_DETAILS;
    }

    // Property accessors

    public void setAssetType(final String assetType) {
        this.assetType = assetType;
    }

    public void setParentCatId(final Long parentCatId) {
        this.parentCatId = parentCatId;
    }

    public List<AssetCategory> getAssetCategoryList() {
        return assetCategoryList;
    }

    public AssetCategory getParentCategory() {
        return parentCategory;
    }

    public void setAssetCategoryService(final AssetCategoryService assetCategoryService) {
        this.assetCategoryService = assetCategoryService;
    }

}
