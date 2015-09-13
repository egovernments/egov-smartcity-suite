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
package org.egov.asset.web.actions.assetmaster;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.service.AssetCategoryService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({ @Result(name = AjaxAssetAction.LOCATIONS, location = "ajaxAsset-locations.jsp"),
        @Result(name = AjaxAssetAction.WARDS, location = "ajaxAsset-wards.jsp"),
        @Result(name = AjaxAssetAction.AREAS, location = "ajaxAsset-areas.jsp"),
        @Result(name = AjaxAssetAction.STREETS, location = "ajaxAsset-streets.jsp"),
        @Result(name = AjaxAssetAction.STREETS2, location = "ajaxAsset-streets2.jsp"),
        @Result(name = AjaxAssetAction.ASSET_CAT_DETAILS, location = "ajaxAsset-assetcatdetails.jsp"),
        @Result(name = AjaxAssetAction.PARENT_CATEGORIES, location = "ajaxAsset-parentcategories.jsp"),
        @Result(name = AjaxAssetAction.AREA_BOUNDARY_TYPE, location = "ajaxAsset-Area.jsp"),
        @Result(name = AjaxAssetAction.SUB_CATEGORIES, location = "ajaxAsset-subcategories.jsp") })
public class AjaxAssetAction extends BaseFormAction {

    private static final long serialVersionUID = 2501333661006168742L;
    private static final Logger LOGGER = Logger.getLogger(AjaxAssetAction.class);
    private String assetType; // Set by Ajax call
    private Long areaId; // Set by Ajax call
    private Long locationId; // Set by Ajax call
    private Long categoryId; // Set by Ajax call
    private Long zoneId; // Set by Ajax call
    private Long wardId; // Set by Ajax call
    private List<Boundary> locationList = new LinkedList<Boundary>();
    private List<Boundary> zoneList = new LinkedList<Boundary>();
    private List<Boundary> streetList = new LinkedList<Boundary>();
    private List<Boundary> street2List = new LinkedList<Boundary>();
    private List<Boundary> areaList = new LinkedList<Boundary>();
    private AssetCategory assetCategory;
    private List<AssetCategory> assetCategoryList;
    public static final String LOCATIONS = "locations";
    public static final String WARDS = "wards";
    public static final String AREAS = "areas";
    public static final String STREETS = "streets";
    public static final String STREETS2 = "streets2";
    public static final String ASSET_CAT_DETAILS = "assetcatdetails";
    public static final String PARENT_CATEGORIES = "parentcategories";
    private String hierarchyTypeName = "LOCATION";
    public static final String AREA_BOUNDARY_TYPE = "Area";
    private String value;
    private Integer departmentId;
    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MMM-yyyy");
    private Long parentCatId; // Set by Ajax call
    private List<AssetCategory> assetSubCategoryList;
    public static final String SUB_CATEGORIES = "subcategories";
    private AssetCategoryService assetCategoryService;

    @Autowired
    private HierarchyTypeService heirarchyTypeService;
    
    @Autowired
    private CrossHierarchyService crossHeirarchyService;
    
    @Autowired
    private BoundaryService boundaryService;
    
    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    /**
     * Populate the Area list by ward
     */
    @Action(value = "/assetmaster/ajaxAsset-populateArea")
    public String populateArea() {
        final Boundary boundary = boundaryService.getBoundaryById(wardId);

        if (wardId != -1)
            areaList.add(boundaryService.getBoundaryById(boundary.getParent().getId()));
        else
            areaList = Collections.EMPTY_LIST;

        LOGGER.info("***********Ajax AreaList: " + areaList.toString());
        return AREAS;
    }

    /**
     * Populate the Area list by Location
     */
    @Action(value = "/assetmaster/ajaxAsset-populateAreaByLocation")
    public String populateAreaByLocation() {
        HierarchyType hType = null;
        try {
            hType = heirarchyTypeService.getHierarchyTypeByName(hierarchyTypeName);
        } catch (final Exception e) {
            LOGGER.error("Error while loading areas - areas." + e.getMessage());
            addFieldError("areas", "Unable to load areas information");
            throw new ApplicationRuntimeException("Unable to load areas information", e);
        }
        final BoundaryType bType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType(AREA_BOUNDARY_TYPE, hType);
        if (bType != null)
            areaList = boundaryService.getAllBoundariesByBoundaryTypeId(bType.getId());
        return AREAS;
    }

    /**
     * Populate the location list by area
     */
    @Action(value = "/assetmaster/ajaxAsset-populateLocations")
    public String populateLocations() {
        try {
            if (areaId != -1) {
                final Boundary boundary = boundaryService.getBoundaryById(areaId);
                locationList.add(boundaryService.getBoundaryById(boundary.getParent().getId()));
            } else
                locationList = Collections.EMPTY_LIST;
        } catch (final Exception e) {
            LOGGER.error("Error while loading locations - locations." + e.getMessage());
            addFieldError("location", "Unable to load location information");
            throw new ApplicationRuntimeException("Unable to load location information", e);
        }
        LOGGER.info("***********Ajax locationList: " + locationList.toString());
        return LOCATIONS;
    }

    /**
     * Populate the ward list by zone
     */
    @Action(value = "/assetmaster/ajaxAsset-populateWard")
    public String populateWard() {
        try {
            zoneList = boundaryService.getChildBoundariesByBoundaryId(zoneId);
        } catch (final Exception e) {
            LOGGER.error("Error while loading warda - wards." + e.getMessage());
            addFieldError("location", "Unable to load ward information");
            throw new ApplicationRuntimeException("Unable to load ward information", e);
        }
        LOGGER.info("***********Ajax ward: " + locationList.toString());
        return WARDS;
    }

    /**
     * Populate the street list by Ward
     *
     * @throws Exception
     * @throws Exception
     */
    @Action(value = "/assetmaster/ajaxAsset-populateStreets")
    public String populateStreets() throws Exception {
        HierarchyType hType = null;
        try {
            hType = heirarchyTypeService.getHierarchyTypeByName(hierarchyTypeName);
        } catch (final Exception e) {
            LOGGER.error("Error while loading Streets." + e.getMessage());
            addFieldError("streets", "Unable to load Streets Information");
            throw new ApplicationRuntimeException("Unable to load Streets information", e);
        }
        final BoundaryType childBoundaryType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType("Street",
                hType);
        if (wardId != null && wardId != -1) {
            final Boundary parentBoundary = boundaryService.getBoundaryById(wardId);
            streetList = crossHeirarchyService.getCrossHierarchyChildrens(parentBoundary, childBoundaryType);
        } else
            streetList = Collections.EMPTY_LIST;
        return STREETS;
    }

    /**
     * Populate the street list by Ward
     *
     * @throws Exception
     * @throws Exception
     */
    @Action(value = "/assetmaster/ajaxAsset-populateStreetsByLocation")
    public String populateStreetsByLocation() throws Exception {
        if (locationId != -1)
            street2List = boundaryService.getChildBoundariesByBoundaryId(locationId);
        return STREETS2;
    }

    @Action(value = "/assetmaster/ajaxAsset-populateCategoryDetails")
    public String populateCategoryDetails() {
        assetCategory = assetCategoryService.findById(categoryId, false);
        return ASSET_CAT_DETAILS;
    }

    @Action(value = "/assetmaster/ajaxAsset-populateParentCategories")
    public String populateParentCategories() {
        if (assetType == null || assetType.equalsIgnoreCase("") || assetType.equalsIgnoreCase("-1"))
            assetCategoryList = assetCategoryService.findAll();
        else
            assetCategoryList = assetCategoryService.getAllAssetCategoryByAssetType(assetType);
        return PARENT_CATEGORIES;
    }

    @Action(value = "/assetmaster/ajaxAsset-populateParentAssetCategoryList")
    public String populateParentAssetCategoryList() {
        if (assetType == null || assetType.equalsIgnoreCase("-1"))
            assetCategoryList = assetCategoryService.getAllParentAssetCategory();
        else
            assetCategoryList = assetCategoryService.getAllParentAssetCategoryByAssetType(assetType);
        return PARENT_CATEGORIES;
    }

    @Action(value = "/assetmaster/ajaxAsset-populateSubCategories")
    public String populateSubCategories() {
        if (parentCatId != -1 && parentCatId != null)
            assetSubCategoryList = assetCategoryService.getAllAssetCategoryByParent(parentCatId);
        else
            assetSubCategoryList = Collections.emptyList();
        return SUB_CATEGORIES;
    }

    /**
     * Populate the location list by area - View Asset Screen
     */
    @Action(value = "/assetmaster/ajaxAsset-populateLocationsByArea")
    public String populateLocationsByArea() {
        try {
            if (areaId != -1)
                locationList = boundaryService.getChildBoundariesByBoundaryId(areaId);
            else
                locationList = Collections.EMPTY_LIST;
        } catch (final Exception e) {
            LOGGER.error("Error while loading locations - locations." + e.getMessage());
            addFieldError("location", "Unable to load location information");
            throw new ApplicationRuntimeException("Unable to load location information", e);
        }
        LOGGER.info("***********Ajax locationList: " + locationList.toString());
        return LOCATIONS;
    }

    // Property accessors

    public void setAreaId(final Long areaId) {
        this.areaId = areaId;
    }

    public void setLocationId(final Long locationId) {
        this.locationId = locationId;
    }

    public void setCategoryId(final Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setAssetType(final String assetType) {
        this.assetType = assetType;
    }

    public List<Boundary> getLocationList() {
        return locationList;
    }

    public List<Boundary> getStreetList() {
        return streetList;
    }

    public List<Boundary> getStreet2List() {
        return street2List;
    }

    public AssetCategory getAssetCategory() {
        return assetCategory;
    }

    public List<AssetCategory> getAssetCategoryList() {
        return assetCategoryList;
    }

    public List<Boundary> getZoneList() {
        return zoneList;
    }

    public void setZoneId(final Long zoneId) {
        this.zoneId = zoneId;
    }

    public void setWardId(final Long wardId) {
        this.wardId = wardId;
    }

    public void setHierarchyTypeName(final String hierarchyTypeName) {
        this.hierarchyTypeName = hierarchyTypeName;
    }

    public List<Boundary> getAreaList() {
        return areaList;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public List<AssetCategory> getAssetSubCategoryList() {
        return assetSubCategoryList;
    }

    public void setAssetSubCategoryList(final List<AssetCategory> assetSubCategoryList) {
        this.assetSubCategoryList = assetSubCategoryList;
    }

    public Long getParentCatId() {
        return parentCatId;
    }

    public void setParentCatId(final Long parentCatId) {
        this.parentCatId = parentCatId;
    }

    public void setBoundaryService(final BoundaryService boundaryService) {
        this.boundaryService = boundaryService;
    }

    public void setBoundaryTypeService(final BoundaryTypeService boundaryTypeService) {
        this.boundaryTypeService = boundaryTypeService;
    }

    public void setAssetCategoryService(final AssetCategoryService assetCategoryService) {
        this.assetCategoryService = assetCategoryService;
    }

    public void setHeirarchyTypeService(HierarchyTypeService heirarchyTypeService) {
        this.heirarchyTypeService = heirarchyTypeService;
    }

    public void setCrossHeirarchyService(CrossHierarchyService crossHeirarchyService) {
        this.crossHeirarchyService = crossHeirarchyService;
    }

}
