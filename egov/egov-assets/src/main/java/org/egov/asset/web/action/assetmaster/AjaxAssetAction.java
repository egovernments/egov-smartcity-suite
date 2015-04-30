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
package org.egov.asset.web.action.assetmaster;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.asset.model.AssetCategory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.web.actions.BaseFormAction;
import org.springframework.beans.factory.annotation.Autowired;

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
    private static final String AREA_BOUNDARY_TYPE = "Area";
    private String value;
    private Integer departmentId;
    private static SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MMM-yyyy");
    private Long parentCatId; // Set by Ajax call
    private List<AssetCategory> assetSubCategoryList;
    public static final String SUB_CATEGORIES = "subcategories";

    @Autowired
    private HeirarchyTypeDAO heirarchyTypeDAO;
    @Autowired
    private BoundaryDAO boundaryDAO;
    @Autowired
    private BoundaryTypeDAO boundaryTypeDAO;

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
    public String populateArea() {
        final Boundary boundary = boundaryDAO.getBoundaryInclgHxById(wardId);

        if (wardId != -1)
            areaList.add(boundaryDAO.getBoundaryInclgHxById(boundary.getParent().getId()));
        else
            areaList = Collections.EMPTY_LIST;

        LOGGER.info("***********Ajax AreaList: " + areaList.toString());
        return AREAS;
    }

    /**
     * Populate the Area list by Location
     */
    public String populateAreaByLocation() {
        HierarchyType hType = null;
        try {
            hType = heirarchyTypeDAO.getHierarchyTypeByName(hierarchyTypeName);
        } catch (final Exception e) {
            LOGGER.error("Error while loading areas - areas." + e.getMessage());
            addFieldError("areas", "Unable to load areas information");
            throw new EGOVRuntimeException("Unable to load areas information", e);
        }
        final BoundaryType bType = boundaryTypeDAO.getBoundaryType(AREA_BOUNDARY_TYPE, hType);
        areaList = boundaryDAO.getAllBoundariesInclgHxByBndryTypeId(bType.getId());
        LOGGER.info("***********Ajax AreaList: " + areaList.toString());
        return AREAS;
    }

    /**
     * Populate the location list by area
     */
    public String populateLocations() {
        try {
            if (areaId != -1) {
                final Boundary boundary = boundaryDAO.getBoundaryInclgHxById(areaId);
                locationList.add(boundaryDAO.getBoundaryInclgHxById(boundary.getParent().getId()));
            } else
                locationList = Collections.EMPTY_LIST;
        } catch (final Exception e) {
            LOGGER.error("Error while loading locations - locations." + e.getMessage());
            addFieldError("location", "Unable to load location information");
            throw new EGOVRuntimeException("Unable to load location information", e);
        }
        LOGGER.info("***********Ajax locationList: " + locationList.toString());
        return LOCATIONS;
    }

    /**
     * Populate the ward list by zone
     */
    public String populateWard() {
        try {
            zoneList = boundaryDAO.getChildBoundariesInclgHx(zoneId);
        } catch (final Exception e) {
            LOGGER.error("Error while loading warda - wards." + e.getMessage());
            addFieldError("location", "Unable to load ward information");
            throw new EGOVRuntimeException("Unable to load ward information", e);
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
    public String populateStreets() throws Exception {
        HierarchyType hType = null;
        try {
            hType = heirarchyTypeDAO.getHierarchyTypeByName(hierarchyTypeName);
        } catch (final Exception e) {
            LOGGER.error("Error while loading Streets." + e.getMessage());
            addFieldError("streets", "Unable to load Streets Information");
            throw new EGOVRuntimeException("Unable to load Streets information", e);
        }
        final BoundaryType childBoundaryType = boundaryTypeDAO.getBoundaryType("Street", hType);
        if (wardId != -1) {
            final Boundary parentBoundary = boundaryDAO.getBoundaryInclgHxById(wardId);
            final Set<Boundary> boundarySet = boundaryDAO.getCrossHeirarchyChildren(parentBoundary, childBoundaryType);
            streetList = new LinkedList<Boundary>(boundarySet);
        } else
            streetList = Collections.EMPTY_LIST;
        // streetList = new
        // LinkedList(boundaryDAO.getCrossHeirarchyChildren(parentBoundary,
        // childBoundaryType));
        return STREETS;
    }

    /**
     * Populate the street list by Ward
     *
     * @throws Exception
     * @throws Exception
     */
    public String populateStreetsByLocation() throws Exception {
        if (locationId != -1)
            street2List = boundaryDAO.getChildBoundariesInclgHx(locationId);
        return STREETS2;
    }

    public String populateCategoryDetails() {
        assetCategory = (AssetCategory) getPersistenceService().find("from AssetCategory where id=?", categoryId);
        return ASSET_CAT_DETAILS;
    }

    public String populateParentCategories() {
        if (assetType == null || assetType.equalsIgnoreCase("-1"))
            assetCategoryList = getPersistenceService().findAllBy("from AssetCategory");
        else
            assetCategoryList = getPersistenceService().findAllBy("from AssetCategory where assetType=?",
                    assetType);
        return PARENT_CATEGORIES;
    }

    public String populateParentAssetCategoryList() {
        if (assetType == null || assetType.equalsIgnoreCase("-1"))
            assetCategoryList = getPersistenceService().findAllBy("from AssetCategory where parent is null");
        else
            assetCategoryList = getPersistenceService().findAllBy(
                    "from AssetCategory where parent is null and assetType=?", assetType);
        return PARENT_CATEGORIES;
    }

    public String populateSubCategories() {
        if (parentCatId != -1 && parentCatId != null)
            assetSubCategoryList = getPersistenceService().findAllBy("from AssetCategory where parent.id=?",
                    parentCatId);
        else
            assetSubCategoryList = Collections.emptyList();
        return SUB_CATEGORIES;
    }

    /**
     * Populate the location list by area - View Asset Screen
     */
    public String populateLocationsByArea() {
        try {
            if (areaId != -1)
                locationList = boundaryDAO.getChildBoundariesInclgHx(areaId);
            else
                locationList = Collections.EMPTY_LIST;
        } catch (final Exception e) {
            LOGGER.error("Error while loading locations - locations." + e.getMessage());
            addFieldError("location", "Unable to load location information");
            throw new EGOVRuntimeException("Unable to load location information", e);
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

}
