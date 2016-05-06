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
package org.egov.asset.web.actions.assetmaster;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetActivities;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetType;
import org.egov.asset.model.ModeOfAcquisition;
import org.egov.asset.service.AppService;
import org.egov.asset.service.AssetActivitiesService;
import org.egov.asset.service.AssetCategoryService;
import org.egov.asset.service.AssetService;
import org.egov.asset.util.AssetConstants;
import org.egov.asset.util.AssetIdentifier;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static org.egov.asset.util.AssetConstants.CREATEASSET;
import static org.egov.asset.util.AssetConstants.MODIFYASSET;
import static org.egov.asset.util.AssetConstants.VIEWASSET;

@ParentPackage("egov")
@Results({ @Result(name = AssetAction.NEW, location = "asset-new.jsp"),
        @Result(name = AssetAction.SEARCH, location = "asset-search.jsp"),
        @Result(name = AssetAction.EDIT, location = "asset-edit.jsp"),
        @Result(name = AssetAction.SEARCH_PLUGIN, location = "asset-searchplugin.jsp"),
        @Result(name = AssetAction.CREATE_PLUGIN, location = "asset-create.jsp"),
        @Result(name = AssetAction.PLUGIN, location = "asset-plugin.jsp") })
public class AssetAction extends SearchFormAction {

    private static final long serialVersionUID = 4814346089005994541L;
    public static final String SEARCH = "search";
    public static final String SEARCH_PLUGIN = "searchplugin";
    public static final String CREATE_PLUGIN = "create";
    public static final String PLUGIN = "plugin";
    public static final String VIEW = "view";

    private static final Logger LOGGER = Logger.getLogger(AssetAction.class);
    private AssetService assetService;
    private AssetActivitiesService assetActivitiesService;
    private AppService appService;
    private Asset asset = new Asset();
    private List<Asset> assetList = null;
    private Long id;
    private static final String LOCATION_HIERARCHY_TYPE = "LOCATION";
    private static final String ADMIN_HIERARCHY_TYPE = "ADMINISTRATION";

    private static final String AREA_BOUNDARY_TYPE = "Area";
    private static final String LOACTION_BOUNDARY_TYPE = "Locality";
    private static final String WARD_BOUNDARY_TYPE = "Ward";
    private static final String Zone_BOUNDARY_TYPE = "Zone";
    private static final String Asset_SAVE_SUCCESS = "asset.save.success";
    private static final String WardList = "wardList";
    private static final String StatusList = "statusList";
    // UI fields
    private String userMode;
    private boolean fDisabled;
    private boolean sDisabled;
    private String dataDisplayStyle;
    private Integer rowId;

    // asset search page
    private Long parentId;
    private String assetType;
    private Long departmentId;
    private List<Integer> statusId;
    private List<String> assetStatus;
    // selectedstatusId
    private String code;
    private String description;
    private Long locationId;
    private Long zoneId;
    private Long areaId;
    private Long streetId;
    private Long street2Id;
    private Long wardId;

    private String selectType;
    private String isAutoGeneratedCode;

    private String category;
    private String searchBy;

    private String messageKey;
    private final List<Long> assetChildCategoryList = new LinkedList<Long>();
    Query query = null;
    private BigDecimal lengthValue;
    private BigDecimal widthValue;
    private BigDecimal areaValue;

    private Long parentCategoryId;
    private List<Long> subCategoryIds;
    private Boolean fromDiaryModule = FALSE;
    private String actionType;

    private AssetCategoryService assetCategoryService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private HierarchyTypeService hierarchyTypeService;

    @Autowired
    private CrossHierarchyService crossHeirarchyService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private BoundaryTypeService boundaryTypeService;

    /**
     * Default Constructor
     */
    public AssetAction() {
        addRelatedEntity("department", Department.class);
        addRelatedEntity("assetCategory", AssetCategory.class);
        addRelatedEntity("area", Boundary.class);
        addRelatedEntity("location", Boundary.class);
        addRelatedEntity("street", Boundary.class);
        addRelatedEntity("street2", Boundary.class);
        addRelatedEntity("ward", Boundary.class);
        addRelatedEntity("zone", Boundary.class);
        addRelatedEntity("status", EgwStatus.class);
    }

    @Override
    public void prepare() {
        isAutoGeneratedCode = appService.getUniqueAppConfigValue("IS_ASSET_CODE_AUTOGENERATED");
        if (id != null && id != -1) {
            asset = assetService.findById(id, false);
            final List<AssetActivities> activities = assetActivitiesService.getAssetActivitiesByAssetId(asset.getId());
            if (activities != null && !activities.isEmpty())
                asset.setGrossValue(activities.get(0).getAdditionAmount());
        }
        super.prepare();
        setupDropdownDataExcluding("area", "location", "street", "street2", "ward", "zone", "status");
        addDropdownData("assetTypeList", Arrays.asList(AssetType.values()));

        /**
         * Fetch Acquisition Mode Dropdown List
         */
        addDropdownData("acquisitionModeList", Arrays.asList(ModeOfAcquisition.values()));

        // Fetch HeirarchyType
        final HierarchyType hType = hierarchyTypeService.getHierarchyTypeByName(LOCATION_HIERARCHY_TYPE);

        /**
         * Fetch Area Dropdown List
         */
        List<Boundary> areaList = new ArrayList<Boundary>();
        if (asset.getArea() != null || areaId != null) {
            final BoundaryType bType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType(AREA_BOUNDARY_TYPE,
                    hType);
            if (bType != null)
                areaList = boundaryService.getAllBoundariesByBoundaryTypeId(bType.getId());
        }
        addDropdownData("areaList", areaList);
        addDropdownData("parentCatList", assetCategoryService.getAllParentAssetCategory());
        final AjaxAssetAction ajaxAssetAction = new AjaxAssetAction();
        ajaxAssetAction.setPersistenceService(getPersistenceService());
        populateSubCategories(ajaxAssetAction);

        /**
         * Fetch Location Dropdown List
         */
        List<Boundary> locationList = new ArrayList<Boundary>();
        try {
            if (asset.getArea() != null)
                locationList = boundaryService.getChildBoundariesByBoundaryId(asset.getArea().getId());
            if (areaId != null)
                locationList = boundaryService.getChildBoundariesByBoundaryId(areaId);
        } catch (final Exception e) {
            LOGGER.error("Error while loading location - location." + e.getMessage());
        }
        addDropdownData("locationList", locationList);

        /**
         * Fetch Ward Dropdown List
         */
        if (zoneId == null) {
            final List<Boundary> wardList = new ArrayList<Boundary>();
            addDropdownData(WardList, wardList);
        } else {
            List<Boundary> wardList = null;
            try {
                wardList = boundaryService.getChildBoundariesByBoundaryId(zoneId);
            } catch (final Exception e) {
                LOGGER.error("Error while loading wards - wards." + e.getMessage());
            }
            addDropdownData(WardList, wardList);
        }

        /**
         * Fetch Ward Dropdown List
         */
        addDropdownData("wardsList", getAllWard());

        /**
         * Fetch Street Dropdown List
         */
        List<Boundary> streetList = new ArrayList<Boundary>();
        if (wardId != null) {
            final BoundaryType childBoundaryType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType("Street",
                    hType);
            final Boundary parentBoundary = boundaryService.getBoundaryById(wardId);
            streetList = crossHeirarchyService.getCrossHierarchyChildrens(parentBoundary, childBoundaryType);
        }
        if (asset.getWard() != null) {
            final BoundaryType childBoundaryType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType("Street",
                    hType);
            final Boundary parentBoundary = boundaryService.getBoundaryById(asset.getWard().getId());
            streetList = crossHeirarchyService.getCrossHierarchyChildrens(parentBoundary, childBoundaryType);
        }
        addDropdownData("streetList", streetList);

        /**
         * Fetch Street Dropdown List
         */
        if (locationId == null) {
            final List<Boundary> street2List = new ArrayList<Boundary>();
            addDropdownData("street2List", street2List);
        } else {
            List<Boundary> street2List = null;
            try {
                street2List = boundaryService.getChildBoundariesByBoundaryId(locationId);
            } catch (final Exception e) {
                LOGGER.error("Error while loading wards - wards." + e.getMessage());
            }
            addDropdownData("street2List", street2List);
        }

        /**
         * Fetch Status Dropdown List
         */
        addDropdownData(StatusList, egwStatusHibernateDAO.getStatusByModule("ASSET"));

        /**
         * Fetch Zone Dropdown List
         */
        addDropdownData("zoneList", getAllZone());
        if (searchBy == null)
            searchBy = "1";
    }

    private void populateSubCategories(final AjaxAssetAction ajaxAssetAction) {
        if (parentCategoryId != null && parentCategoryId != -1) {
            ajaxAssetAction.setParentCatId(parentCategoryId);
            ajaxAssetAction.setAssetCategoryService(assetCategoryService);
            ajaxAssetAction.populateSubCategories();
            addDropdownData("subCategoriesList", ajaxAssetAction.getAssetSubCategoryList());
        } else
            addDropdownData("subCategoriesList", Collections.emptyList());
    }

    /**
     * This method is invoked to create a new form.
     *
     * @return a <code>String</code> representing the value 'NEW'
     */
    @Action(value = "/assetmaster/asset-newform")
    public String newform() {
        userMode = NEW;
        return showform();
    }

    @Action(value = "/assetmaster/asset-showform")
    public String showform() {
        LOGGER.info("****User Mode: " + userMode);
        String result = null;

        if (userMode == null)
            userMode = NEW;
        if (NEW.equals(userMode)) {
            fDisabled = false;
            sDisabled = false;
            result = NEW;
        } else if (VIEW.equals(userMode)) {
            if (id == null) {
                addActionError(getText("asset.category.id.null"));
                result = SEARCH;
            } else {
                fDisabled = true;
                sDisabled = true;
                setLocationDetails(asset);
                result = NEW;
            }
        } else if (EDIT.equals(userMode))
            if (id == null) {
                addActionError(getText("asset.id.null"));
                result = SEARCH;
            } else {
                fDisabled = false;
                sDisabled = false;
                setCategory(category);
                setLocationDetails(asset);
                result = NEW;
            }

        return result;
    }

    private void setLocationDetails(final Asset asset) {

        if (asset.getArea() != null) {
            List<Boundary> locationList = new LinkedList<Boundary>();
            try {
                locationList = boundaryService.getActiveChildBoundariesByBoundaryId(asset.getArea().getId());
            } catch (final Exception e) {
                LOGGER.error("Error while loading locations - locations." + e.getMessage());
                addFieldError("location", "Unable to load location information");
                throw new ApplicationRuntimeException("Unable to load location information", e);
            }
            addDropdownData("locationList", locationList);
        }
        if (asset.getWard() != null) {
            final AjaxAssetAction ajaxAssetAction = new AjaxAssetAction();
            ajaxAssetAction.setHeirarchyTypeService(hierarchyTypeService);
            ajaxAssetAction.setCrossHeirarchyService(crossHeirarchyService);
            ajaxAssetAction.setBoundaryService(boundaryService);
            ajaxAssetAction.setBoundaryTypeService(boundaryTypeService);
            ajaxAssetAction.setWardId(asset.getWard().getId());
            try {
                ajaxAssetAction.populateStreets();
                addDropdownData("streetList", ajaxAssetAction.getStreetList());
            } catch (final Exception e) {
                LOGGER.error("Error while loading Streets." + e.getMessage());
                addFieldError("streets", "Unable to load Streets Information");
                throw new ApplicationRuntimeException("Unable to load Streets information", e);
            }
        }
        if (asset.getWard() != null) {
            setZoneId(asset.getWard().getParent().getId());
            List<Boundary> wardList = null;
            try {
                wardList = boundaryService.getActiveChildBoundariesByBoundaryId(Long.valueOf(String.valueOf(zoneId)));
            } catch (final Exception e) {
                LOGGER.error("Error while loading wards - wards." + e.getMessage());
            }
            addDropdownData(WardList, wardList);
        }
    }

    public List<Boundary> getAllLocation() {
        HierarchyType hType = null;
        hType = hierarchyTypeService.getHierarchyTypeByName(LOCATION_HIERARCHY_TYPE);
        List<Boundary> locationList = null;
        final BoundaryType bType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType(LOACTION_BOUNDARY_TYPE,
                hType);
        locationList = boundaryService.getActiveBoundariesByBoundaryTypeId(bType.getId());
        return locationList;
    }

    public List<Boundary> getAllWard() {
        HierarchyType hType = null;
        hType = hierarchyTypeService.getHierarchyTypeByName(ADMIN_HIERARCHY_TYPE);
        List<Boundary> wardList = null;
        final BoundaryType bType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType(WARD_BOUNDARY_TYPE, hType);
        wardList = boundaryService.getAllBoundariesByBoundaryTypeId(bType.getId());
        return wardList;
    }

    public List<Boundary> getAllZone() {
        final HierarchyType hType = hierarchyTypeService.getHierarchyTypeByName(ADMIN_HIERARCHY_TYPE);
        List<Boundary> zoneList = new ArrayList<Boundary>();
        final BoundaryType bType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType(Zone_BOUNDARY_TYPE, hType);
        if (actionType == null)
            actionType = "";
        if (bType != null)
            if (actionType.equalsIgnoreCase(CREATEASSET))
                zoneList = boundaryService.getActiveBoundariesByBoundaryTypeId(bType.getId());
            else if (actionType == "" || actionType.equalsIgnoreCase(VIEWASSET) || actionType.equalsIgnoreCase(MODIFYASSET))
                if ("edit".equalsIgnoreCase(userMode))
                    zoneList = boundaryService.getActiveBoundariesByBoundaryTypeId(bType.getId());
                else
                    zoneList = boundaryService.getAllBoundariesByBoundaryTypeId(bType.getId());
        return zoneList;
    }

    @Action(value = "/assetmaster/asset-edit")
    public String edit() {
        userMode = EDIT;
        return SEARCH;
    }

    @Action(value = "/assetmaster/asset-view")
    public String view() {
        userMode = VIEW;
        return SEARCH;
    }

    /**
     * Search Page for Assets view and edit screen
     *
     * @throws Exception
     */
    @Action(value = "/assetmaster/asset-list")
    public String list() throws Exception {
        setAssetType(assetType);
        if (departmentId == null && locationId == null && assetType == null
                && (code == null || code != null && code.trim().equalsIgnoreCase(""))
                && (description == null || description != null && description.trim().equalsIgnoreCase(""))
                && (statusId == null || statusId != null && statusId.isEmpty()) && zoneId == -1) {
            messageKey = "message.mandatory";
            addActionError(getText(messageKey, "At least one selection is required"));
            return SEARCH;
        }
        setPageSize(AssetConstants.PAGE_SIZE);
        search();
        return SEARCH;
    }

    private List<Asset> searchAssets() throws Exception {
        final HashMap<String, Object> queryAndParam = getQueryAndParam();
        final List<Object> params = (List<Object>) queryAndParam.get("param");

        final Object parameterObj[] = new Object[params.size()];
        for (int element = 0; element < params.size(); element++)
            parameterObj[element] = params.get(element);
        return assetService.findAllBy(queryAndParam.get("query").toString(), parameterObj);
    }

    /**
     * Method to setup request parameter received from other modules
     */
    private void setupRequestData() {
        setStatusList(getStatusList(assetStatus));
    }

    /**
     * Get the list of <code>EgwStatus</code> related to ASSET module.
     *
     * @param statusDescList - List of status descriptions
     * @return
     */
    private List<EgwStatus> getStatusList(final List<String> statusDescList) {
        List<EgwStatus> lStatusList = null;
        if (statusDescList != null && !statusDescList.isEmpty()) {
            final StringBuffer sql = new StringBuffer(100);
            sql.append("from EgwStatus st where st.moduletype='ASSET'  and UPPER(st.description) in (");
            for (int i = 0, len = statusDescList.size(); i < len; i++) {
                sql.append("'" + statusDescList.get(i).trim().toUpperCase() + "'");
                if (i < len - 1)
                    sql.append(',');
            }
            sql.append(") order by description");
            final String query = sql.toString();
            lStatusList = persistenceService.findAllBy(query);
        }
        return lStatusList;
    }

    private void setStatusList(List<EgwStatus> statusList) {
        if (statusList == null)
            statusList = new LinkedList<EgwStatus>();
        addDropdownData(StatusList, statusList);
    }

    /**
     * asset search plugin for other modules - works and stores
     */
    @Action(value = "/assetmaster/asset-showSearchPage")
    public String showSearchPage() {
        setupRequestData();
        return SEARCH_PLUGIN;
    }

    @Action(value = "/assetmaster/asset-showSearchResult")
    public String showSearchResult() throws Exception {
        if (!assetStatus.isEmpty() && assetStatus.get(0) != null) {
            final List<String> statusDescList = Arrays
                    .asList(assetStatus.get(0).substring(1, assetStatus.get(0).length() - 1).split(", "));
            setStatusList(getStatusList(statusDescList));
        }
        if (statusId != null && !statusId.isEmpty())
            assetList = searchAssets();
        else
            addFieldError("status", "Please select at least one status");

        return SEARCH_PLUGIN;
    }

    /**
     * test page for search plugin - not in use
     */
    @Action(value = "/assetmaster/asset-showPlugin")
    public String showPlugin() {
        return PLUGIN;
    }

    /**
     * asset create plugin for other modules - works and stores
     */
    @Action(value = "/assetmaster/asset-showCreatePage")
    public String showCreatePage() {
        setupRequestData();
        return CREATE_PLUGIN;
    }

    /**
     * create asset from other modules
     */
    @ValidationErrorPage(value = NEW)
    @Action(value = "/assetmaster/asset-create")
    public String create() {
        try {
            setDimensions();
            setStatusList(getStatusList(assetStatus));
            assetService.setAssetNumber(asset);
            assetService.persist(asset);
            addActionMessage("\'" + asset.getCode() + "\' " + getText(Asset_SAVE_SUCCESS));
            id = asset.getId();
            // make to view mode
            fDisabled = true;
            sDisabled = true;
            setLocationDetails(asset);
            return CREATE_PLUGIN;
        } catch (final ValidationException e) {
            clearMessages();
            prepare();
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        }

    }

    private void setDimensions() {
        if (lengthValue != null)
            asset.setLength(lengthValue);
        if (widthValue != null)
            asset.setWidth(widthValue);
        if (areaValue != null)
            asset.setTotalArea(areaValue);
    }

    @Action(value = "/assetmaster/asset-save")
    public String save() {
        try {
            setDimensions();
            if (asset.getDateOfCreation() != null)
                assetService.setAssetNumber(asset);
            assetService.persist(asset);

            if (NEW.equals(userMode) && asset.getStatus().getDescription().equalsIgnoreCase("Capitalized")) {
                final AssetActivities activities = new AssetActivities();
                activities.setAsset(asset);
                activities.setActivityDate(asset.getDateOfCreation());
                activities.setIdentifier(AssetIdentifier.C);
                activities.setAdditionAmount(asset.getGrossValue());
                assetActivitiesService.persist(activities);
            }
        } catch (final ValidationException e) {
            throw new ValidationException(e.getErrors());
        }
        addActionMessage("\'" + asset.getCode() + "\' " + getText(Asset_SAVE_SUCCESS));
        userMode = EDIT;
        id = asset.getId();
        setLocationDetails(asset);

        return showform();
    }

    /**
     * The default action method
     */
    @Override
    public String execute() {
        return view();
    }

    // Property accessors

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Object getModel() {
        return asset;
    }

    public void setModel(final Asset asset) {
        this.asset = asset;
    }

    // Spring Injection
    public void setAssetService(final AssetService assetService) {
        this.assetService = assetService;
    }

    // Spring Injection
    public void setAppService(final AppService appService) {
        this.appService = appService;
    }

    public String getUserMode() {
        return userMode;
    }

    public void setUserMode(final String userMode) {
        this.userMode = userMode;
    }

    public boolean isFDisabled() {
        return fDisabled;
    }

    public boolean isSDisabled() {
        return sDisabled;
    }

    public List<Asset> getAssetList() {
        return assetList;
    }

    public void setAssetList(final List<Asset> assetList) {
        this.assetList = assetList;
    }

    public String getDataDisplayStyle() {
        return dataDisplayStyle;
    }

    /**
     * @return the parentId
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(final Long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the assetType
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * @param assetType the assetType to set
     */
    public void setAssetType(final String assetType) {
        this.assetType = assetType;
    }

    /**
     * @return the departmentId
     */
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(final Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return the locationId
     */
    public Long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the wardId to set for search
     */
    public void setLocationId(final Long wardId) {
        locationId = wardId;
    }

    /**
     * @return the messageKey
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * @param messageKey the messageKey to set
     */
    public void setMessageKey(final String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * @return the statusId
     */
    public List<Integer> getStatusId() {
        return statusId;
    }

    /**
     * @param statusId the statusId to set
     */
    public void setStatusId(final List<Integer> statusId) {
        this.statusId = statusId;
    }

    public void setAssetStatus(final List<String> assetStatus) {
        this.assetStatus = assetStatus;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(final Integer rowId) {
        this.rowId = rowId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(final Long zoneId) {
        this.zoneId = zoneId;
    }

    public List<Long> getAssetChildCategoryList() {
        return assetChildCategoryList;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(final String selectType) {
        this.selectType = selectType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getIsAutoGeneratedCode() {
        return isAutoGeneratedCode;
    }

    public void setIsAutoGeneratedCode(final String isAutoGeneratedCode) {
        this.isAutoGeneratedCode = isAutoGeneratedCode;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        final Map<String, Object> queryAndParam = getQueryAndParam();
        // System.out.println("getQueryAndParam()"+getQueryAndParam());
        return new SearchQueryHQL(queryAndParam.get("query").toString(),
                "select count(*) " + queryAndParam.get("query"), (List<Object>) queryAndParam.get("param"));
    }

    /**
     * @return query in string format
     */
    private HashMap<String, Object> getQueryAndParam() {
        final StringBuilder sql = new StringBuilder(265);
        final List<Object> parameters = new ArrayList<Object>();
        if (userMode != null && VIEW.equalsIgnoreCase(userMode))
            sql.append("from Asset asset where asset.code is not null ");
        else
            sql.append("from Asset asset where asset.code is not null and asset.status.code<>'CANCELLED' ");
        if (assetType != null && !assetType.equalsIgnoreCase("-1")) {
            sql.append(" and asset.assetCategory.assetType = ?");
            parameters.add(AssetType.valueOf(assetType));
        }
        if (departmentId != null && departmentId != -1) {
            sql.append(" and asset.department.id = ?");
            parameters.add(departmentId);
        }
        if (zoneId != null && zoneId != -1) {
            sql.append(" and asset.ward.parent.id = ?");
            parameters.add(zoneId);
        }
        if (wardId != null && wardId != -1) {
            sql.append(" and asset.ward.id = ?");
            parameters.add(wardId);
        }
        if (streetId != null && streetId != -1) {
            sql.append(" and asset.street.id = ?");
            parameters.add(streetId);
        }

        if (areaId != null && areaId != -1) {
            sql.append(" and asset.area.id = ?");
            parameters.add(areaId);
        }
        if (locationId != null && locationId != -1) {
            sql.append(" and asset.location.id = ?");
            parameters.add(locationId);
        }
        if (street2Id != null && street2Id != -1) {
            sql.append(" and asset.street.id = ?");
            parameters.add(street2Id);
        }
        if (code != null && !code.trim().equalsIgnoreCase("")) {
            sql.append(" and UPPER(asset.code) like ?");
            parameters.add("%" + code.toUpperCase() + "%");
        }
        if (description != null && !description.trim().equalsIgnoreCase("")) {
            sql.append(" and UPPER(asset.description) like ?");
            parameters.add("%" + description.toUpperCase() + "%");
        }

        if (statusId != null && !statusId.isEmpty()) {
            String statusList = "";
            for (final Integer status : statusId)
                statusList = statusList + status + ",";
            sql.append(" and asset.status.id in (" + statusList.substring(0, statusList.length() - 1) + ")");
        }

        if (parentId != null && parentId != -1 && !parentId.equals("")) {
            sql.append(" and asset.assetCategory.id = ? ");
            parameters.add(parentId);
        }

        if (parentCategoryId != null && parentCategoryId != -1
                && (subCategoryIds == null || subCategoryIds.size() == 1 && subCategoryIds.get(0) == -1)) {
            sql.append(" and (asset.assetCategory.id = ? or asset.assetCategory.parent.id = ?) ");
            parameters.add(parentCategoryId);
            parameters.add(parentCategoryId);
        } else if (parentCategoryId != null && parentCategoryId != -1 && subCategoryIds != null
                && !subCategoryIds.isEmpty()) {
            String subCatList = "";
            for (final Long subCat : subCategoryIds)
                subCatList = subCatList + subCat + ",";
            sql.append(" and asset.assetCategory.id in (" + subCatList.substring(0, subCatList.length() - 1) + ")");
        }

        final HashMap<String, Object> queryAndParam = new HashMap<String, Object>();
        queryAndParam.put("query", sql);
        queryAndParam.put("param", parameters);
        return queryAndParam;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(final Long areaId) {
        this.areaId = areaId;
    }

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(final Long streetId) {
        this.streetId = streetId;
    }

    public Long getStreet2Id() {
        return street2Id;
    }

    public void setStreet2Id(final Long street2Id) {
        this.street2Id = street2Id;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(final Long wardId) {
        this.wardId = wardId;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(final String searchBy) {
        this.searchBy = searchBy;
    }

    public void setAssetActivitiesService(final AssetActivitiesService assetActivitiesService) {
        this.assetActivitiesService = assetActivitiesService;
    }

    public BigDecimal getLengthValue() {
        return lengthValue;
    }

    public BigDecimal getWidthValue() {
        return widthValue;
    }

    public BigDecimal getAreaValue() {
        return areaValue;
    }

    public void setLengthValue(final BigDecimal lengthValue) {
        this.lengthValue = lengthValue;
    }

    public void setWidthValue(final BigDecimal widthValue) {
        this.widthValue = widthValue;
    }

    public void setAreaValue(final BigDecimal areaValue) {
        this.areaValue = areaValue;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(final Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public List<Long> getSubCategoryIds() {
        return subCategoryIds;
    }

    public void setSubCategoryIds(final List<Long> subCategoryIds) {
        this.subCategoryIds = subCategoryIds;
    }

    public Boolean getFromDiaryModule() {
        return fromDiaryModule;
    }

    public void setFromDiaryModule(final Boolean fromDiaryModule) {
        this.fromDiaryModule = fromDiaryModule;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(final String actionType) {
        this.actionType = actionType;
    }

    public void setAssetCategoryService(final AssetCategoryService assetCategoryService) {
        this.assetCategoryService = assetCategoryService;
    }

    public List<String> getAssetStatus() {
        return assetStatus;
    }

}