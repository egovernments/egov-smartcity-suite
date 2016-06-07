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
package org.egov.asset.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.displaytag.pagination.PaginatedList;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.ModeOfAcquisition;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonAssetsService {

    private static final Logger LOGGER = Logger.getLogger(CommonAssetsService.class);
    protected AssetCategoryService assetCategoryService;
    protected AssetService assetService;
    protected PersistenceService genericService;
    // Create Asset parameters
    public static final String ASSET_ID = "ASSET_ID"; // Long
    public static final String ASSET_CODE = "ASSET_CODE"; // String
    public static final String ASSET_NAME = "ASSET_NAME"; // String
    public static final String ASSET_CATEGORY_ID = "ASSET_CATEGORY_ID"; // Long
    public static final String ASSET_AREA_ID = "ASSET_AREA_ID"; // Integer
    public static final String ASSET_LOCATION_ID = "ASSET_LOCATION_ID"; // Integer
    public static final String ASSET_STREET_ID = "ASSET_STREET_ID"; // Integer
    public static final String ASSET_WARD_ID = "ASSET_WARD_ID"; // Integer
    public static final String ASSET_DETAILS = "ASSET_DETAILS"; // String
    public static final String ASSET_MODE_OF_ACQUISITION = "ASSET_MODE_OF_ACQUISITION"; // String
    public static final String ASSET_COMMISSIONING_DATE = "ASSET_COMMISSIONING_DATE"; // Date
    public static final String ASSET_STATUS_ID = "ASSET_STATUS_ID"; // Integer
    public static final String ASSET_GROSS_VALUE = "ASSET_GROSS_VALUE"; // BigDecimal
    public static final String ASSET_ACCUMULATIVE_DEPRECIATION = "ASSET_ACCUMULATIVE_DEPRECIATION"; // BigDecimal
    public static final String ASSET_WRITTEN_DOWN_VALUE = "ASSET_WRITTEN_DOWN_VALUE"; // BigDecimal
    public static final String ASSET_DESCRIPTION = "ASSET_DESCRIPTION"; // String
    public static final String ASSET_CATEGORY_CODE = "ASSET_CATEGORY_CODE"; // String
    public static final String ASSET_CATEGORY_NAME = "ASSET_CATEGORY_NAME"; // String
    // Search Asset parameters
    public static final String ASSET_TYPE = "ASSET_TYPE"; // Long
    public static final String ASSET_DEPARTMENT_ID = "ASSET_DEPARTMENT_ID"; // Integer
    public static final String ASSET_STATUS_ID_LIST = "ASSET_STATUS_ID_LIST"; // List<Integer>
    public static final String ASSET_TYPE_NAME = "ASSET_TYPE_NAME";
    public static final String ASSET_DEPARTMENT_CODE = "ASSET_DEPARTMENT_CODE";
    public static final String ASSET_WARD_NAME = "ASSET_WARD_NAME";
    public static final String ASSET_STATUS_LIST = "ASSET_STATUS_LIST";
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private BoundaryService boundaryService;

    /**
     * Find the <code>AssetCategory</code> by its ID. Return NULL if <code>AssetCategory</code> not found.
     *
     * @param categoryId
     * @return <code>AssetCategory</code>
     * @since v2.1
     */
    public AssetCategory getAssetCategoryById(final Long categoryId) {
        AssetCategory assetCategory = null;
        try {
            assetCategory = assetCategoryService.findById(categoryId, false);
        } catch (final HibernateException he) {
            LOGGER.error("Error while getting AssetCategoryById.");
            throw new ApplicationRuntimeException("Hibernate Exception : getting AssetCategoryById." + he.getMessage(), he);
        }

        return assetCategory;
    }

    /**
     * Get all the List of <code>AssetCategory</code>.
     *
     * @return List<AssetCategory> - List of <code>AssetCategory</code> (Ordered by <tt>name</tt>)
     * @since v2.1
     */
    public List<AssetCategory> getAllAssetCategory() {
        List<AssetCategory> assetCategoryList = null;
        try {
            assetCategoryList = assetCategoryService.findAll("name");
        } catch (final HibernateException he) {
            LOGGER.error("Error while getting AllAssetCategoryList.");
            throw new ApplicationRuntimeException("Hibernate Exception : getting AllAssetCategoryList." + he.getMessage(), he);
        }

        return assetCategoryList;
    }

    /**
     * Find the <code>Asset</code> by its ID. Returns NULL if <code>Asset</code> not found.
     *
     * @param assetId
     * @return <code>Asset</code>
     * @since v2.1
     */
    public Asset getAssetById(final Long assetId) {
        Asset asset = null;
        try {
            asset = assetService.findById(assetId, false);
        } catch (final HibernateException he) {
            LOGGER.error("Error while getting AssetById.");
            throw new ApplicationRuntimeException("Hibernate Exception : getting AssetById." + he.getMessage(), he);
        }

        return asset;
    }

    /**
     * Find the <code>Asset</code> by its Code. Returns NULL if <code>Asset</code> not found.
     *
     * @param code
     * @return <code>Asset</code>
     * @since v2.1
     */
    public Asset getAssetByCode(final String code) {
        Asset asset = null;
        try {
            asset = assetService.getAssetByCode(code);
        } catch (final HibernateException he) {
            LOGGER.error("Error while getting AssetByCode.");
            throw new ApplicationRuntimeException("Hibernate Exception : getting AssetByCode." + he.getMessage(), he);
        }

        return asset;
    }

    /**
     * Find the list of <code>Asset</code> by category ID.
     *
     * @param categoryId
     * @return <code>Asset</code>
     * @since v2.1
     */
    public List<Asset> getAssetsByCategoryId(final Long categoryId) {
        List<Asset> assetList = null;
        try {
            assetList = assetService.getAssetsByCategoryId(categoryId);
        } catch (final HibernateException he) {
            LOGGER.error("Error while getting AssetsByCategoryId.");
            throw new ApplicationRuntimeException("Hibernate Exception : getting AssetsByCategoryId." + he.getMessage(), he);
        }

        return assetList;
    }

    /**
     * Find the <code>AssetCategory</code> by its Code. Returns NULL if <code>AssetCategory</code> not found.
     *
     * @param code
     * @return <code>AssetCategory</code>
     * @since v2.1
     */
    public AssetCategory getAssetCategoryByCode(final String code) {
        AssetCategory assetCategory = null;
        try {
            assetCategory = assetCategoryService.getAssetCategoryByCode(code);
        } catch (final HibernateException he) {
            LOGGER.error("Error while getting AssetCategoryByCode.");
            throw new ApplicationRuntimeException("Hibernate Exception : getting AssetCategoryByCode." + he.getMessage(), he);
        }

        return assetCategory;
    }

    /**
     * Get all the List of <code>Asset</code>.
     *
     * @return List<Asset> - List of <code>Asset</code> (Ordered by <tt>code</tt>)
     * @since v2.1
     */
    public List<Asset> getAllAssets() {
        List<Asset> assetList = null;
        try {
            assetList = assetService.findAll("code");
        } catch (final HibernateException he) {
            LOGGER.error("Error while getting AllAssetList.");
            throw new ApplicationRuntimeException("Hibernate Exception : getting AllAssetList." + he.getMessage(), he);
        }

        return assetList;
    }

    /**
     * Get all status for asset
     *
     * @return - List of all status for asset
     */
    public List<EgwStatus> getAllAssetStatus() {
        return egwStatusHibernateDAO.getStatusByModule("ASSET");
    }

    /**
     * API to create or update an Asset. Expecting a map which will have :
     *
     * @param ASSET_ID (optional) - Long - This param must be pass in case of update only. If its not passed the API will create a
     * new asset.
     * @param ASSET_CODE (mandatory) - String
     * @param ASSET_NAME (mandatory) - String
     * @param ASSET_CATEGORY_ID (mandatory) - Long
     * @param ASSET_STATUS_ID (mandatory) - Integer
     * @param ASSET_AREA_ID (optional) - Integer
     * @param ASSET_LOCATION_ID (optional) - Integer
     * @param ASSET_STREET_ID (optional) - Integer
     * @param ASSET_WARD_ID (optional) - Integer
     * @param ASSET_DETAILS (optional) - String
     * @param ASSET_MODE_OF_ACQUISITION (optional) - String
     * @param ASSET_COMMISSIONING_DATE (optional) - Date
     * @param ASSET_GROSS_VALUE (optional) - BigDecimal
     * @param ASSET_ACCUMULATIVE_DEPRECIATION (optional) - BigDecimal
     * @param ASSET_WRITTEN_DOWN_VALUE (optional) - BigDecimal
     * @param ASSET_DESCRIPTION (optional) - String
     * @param parameters - Map holding asset values
     * @return <code>Asset</code> - if the asset has create or update successfully otherwise <code>Null</code>.
     * @exception ValidationException , {@link ClassCastException}
     */
    public Asset createOrUpdateAsset(final Map<String, Object> parameters) {

        Asset asset = new Asset();

        // **************************Update Logic***********************
        // Check for update or create asset
        if (parameters.get(ASSET_ID) != null)
            asset = assetService.findById((Long) parameters.get(ASSET_ID), false);
        // Check if passed invalid AssetId for update
        if (asset == null)
            throw new ValidationException(
                    Arrays.asList(new ValidationError("asset.id.doesNotExist", "asset.id.doesNotExist")));
        // **************************Update Logic End***********************

        try {
            // Code (String) - Mandatory Field
            if (parameters.get(ASSET_CODE) == null && asset.getCode() == null)
                throw new ValidationException(
                        Arrays.asList(new ValidationError("asset.code.mandatory", "asset.code.mandatory")));
            else if (parameters.get(ASSET_CODE) != null)
                asset.setCode((String) parameters.get(ASSET_CODE));

            // Name (String) - Mandatory Field
            if (parameters.get(ASSET_NAME) == null && asset.getName() == null)
                throw new ValidationException(
                        Arrays.asList(new ValidationError("asset.name.mandatory", "asset.name.mandatory")));
            else if (parameters.get(ASSET_NAME) != null)
                asset.setName((String) parameters.get(ASSET_NAME));

            // Category Id (Long) - Mandatory Field
            if (parameters.get(ASSET_CATEGORY_ID) == null && asset.getAssetCategory() == null)
                throw new ValidationException(
                        Arrays.asList(new ValidationError("asset.category.mandatory", "asset.category.mandatory")));
            else if (parameters.get(ASSET_CATEGORY_ID) != null) {
                final AssetCategory assetCategory = assetCategoryService
                        .findById((Long) parameters.get(ASSET_CATEGORY_ID), false);
                if (assetCategory == null)
                    throw new ValidationException(Arrays
                            .asList(new ValidationError("asset.category.doesNotExist", "asset.category.doesNotExist")));
                asset.setAssetCategory(assetCategory);
            }
            // Status Id (Integer) - Mandatory Field
            if (parameters.get(ASSET_STATUS_ID) == null && asset.getStatus() == null)
                throw new ValidationException(
                        Arrays.asList(new ValidationError("asset.status.mandatory", "asset.status.mandatory")));
            else if (parameters.get(ASSET_STATUS_ID) != null) {
                final EgwStatus status = (EgwStatus) egwStatusHibernateDAO
                        .findById((Integer) parameters.get(ASSET_STATUS_ID), false);
                if (status == null)
                    throw new ValidationException(Arrays
                            .asList(new ValidationError("asset.status.doesNotExist", "asset.status.doesNotExist")));
                asset.setStatus(status);

            }
            // Asset Description (String)
            if (parameters.get(ASSET_DESCRIPTION) != null)
                asset.setDescription((String) parameters.get(ASSET_DESCRIPTION));

            // Area Id (Integer) - Mandatory Field
            if (parameters.get(ASSET_AREA_ID) != null) {
                final Boundary area = boundaryService.getBoundaryById((Long) parameters.get(ASSET_AREA_ID));
                if (area == null)
                    throw new ValidationException(
                            Arrays.asList(new ValidationError("asset.area.doesNotExist", "asset.area.doesNotExist")));
                asset.setArea(area);
            }

            // Location Id (Integer) - Mandatory Field
            if (parameters.get(ASSET_LOCATION_ID) != null) {
                final Boundary location = boundaryService.getBoundaryById((Long) parameters.get(ASSET_LOCATION_ID));
                if (location == null)
                    throw new ValidationException(Arrays
                            .asList(new ValidationError("asset.location.doesNotExist", "asset.location.doesNotExist")));
                asset.setLocation(location);
            }

            // Street Id (Integer) - Mandatory Field
            if (parameters.get(ASSET_STREET_ID) != null) {
                final Boundary street = boundaryService.getBoundaryById((Long) parameters.get(ASSET_STREET_ID));
                if (street == null)
                    throw new ValidationException(Arrays
                            .asList(new ValidationError("asset.street.doesNotExist", "asset.street.doesNotExist")));
                asset.setStreet(street);
            }

            // Ward Id (Integer) - Mandatory Field
            if (parameters.get(ASSET_WARD_ID) != null) {
                final Boundary ward = boundaryService.getBoundaryById((Long) parameters.get(ASSET_WARD_ID));
                if (ward == null)
                    throw new ValidationException(
                            Arrays.asList(new ValidationError("asset.ward.doesNotExist", "asset.ward.doesNotExist")));
                asset.setWard(ward);
            }

            // Mode Of Acquisition (String)
            if (parameters.get(ASSET_MODE_OF_ACQUISITION) != null)
                asset.setModeOfAcquisition(
                        ModeOfAcquisition.valueOf((String) parameters.get(ASSET_MODE_OF_ACQUISITION)));

            asset = assetService.persist(asset);

        } catch (final HibernateException he) {
            LOGGER.error("Error while creating Asset through API.");
            throw new ApplicationRuntimeException("Hibernate Exception : in createAsset." + he.getMessage(), he);
        }

        return asset;
    }

    /**
     * Search Assets. If the map is empty it will fetch all the assets. Expecting a map which will have :
     *
     * @param ASSET_CODE (optional) - String
     * @param ASSET_TYPE_ID (optional) - Long
     * @param ASSET_CATEGORY_ID (optional) - Long
     * @param ASSET_DEPARTMENT_ID (optional) - Integer
     * @param ASSET_WARD_ID (optional) - Integer
     * @param ASSET_STATUS_ID_LIST (optional) - List of Integer
     * @param ASSET_DESCRIPTION (optional) - String
     * @param parameters - Map holding asset search criteria
     * @return List of assets for given search criteria
     * @exception ClassCastException
     */
    public List<Asset> searchAssets(final Map<String, Object> parameters) {
        final Map queryAndParam1 = getsearchAssetQuery(parameters);
        final String query = (String) queryAndParam1.get("query");
        final List<Object> params2 = (List<Object>) queryAndParam1.get("params");
        return assetService.findAllBy(query, params2.toArray());

    }

    /**
     * Search Assets page. If the map is empty it will fetch asset page with.out ant filter. Expecting a map which will have :
     *
     * @param ASSET_CODE (optional) - String
     * @param ASSET_TYPE_ID (optional) - Long
     * @param ASSET_CATEGORY_ID (optional) - Long
     * @param ASSET_DEPARTMENT_ID (optional) - Integer
     * @param ASSET_WARD_ID (optional) - Integer+assetId+
     * @param ASSET_STATUS_ID_LIST (optional) - List of Integer
     * @param ASSET_DESCRIPTION (optional) - String
     * @param parameters - Map holding asset search criteria
     * @return List of assets for given search criteria
     * @exception ClassCastException
     * @return PaginatedList.
     */
    public PaginatedList searchAssetPage(final Map<String, Object> parameters, final int pageNumber,
            final int pageSize) {
        final Map queryAndParams1 = getsearchAssetQuery(parameters);
        final String query = (String) queryAndParams1.get("query");
        final List<Object> params1 = (List<Object>) queryAndParams1.get("params");
        final Page page = assetService.findPageBy(query, pageNumber, pageSize, params1.toArray());
        final Long count = (Long) genericService.find("select count(*) " + query, params1.toArray());
        return new EgovPaginatedList(page, count.intValue());
    }

    /**
     * Search Assets page. If the map is empty it will fetch asset page without any filter. Expecting a map which will have :
     * * @param ASSET_NAME (optional) – String
     *
     * @param ASSET_CODE (optional) – it's free type search. Query is using ' like clause' to search.
     * @param ASSET_TYPE_NAME (optional) – String (There can be three values :Land,MovableAsset,ImmovableAsset)
     * @param ASSET_CATEGORY_CODE (optional) – String
     * @param ASSET_DEPARTMENT_CODE (optional) – String
     * @param ASSET_WARD_CODE (optional) – String
     * @param ASSET_STATUS_LIST (optional) – List of String
     * @param ASSET_DESCRIPTION (optional) – String
     * @param parameters – Map holding asset search criteria
     * @return List of assets for given search criteria
     * @return PaginatedList.
     */
    public PaginatedList findAssetPage(final Map<String, Object> parameters, final int pageNumber, final int pageSize) {
        final Map queryAndParam = findAssetQuery(parameters);
        final String query = (String) queryAndParam.get("query");
        final List<Object> params = (List<Object>) queryAndParam.get("params");
        final Page page = assetService.findPageBy(query, pageNumber, pageSize, params.toArray());
        final Long count = (Long) genericService.find("select count(*) " + query, params.toArray());
        return new EgovPaginatedList(page, count.intValue());
    }

    /**
     * Formulate the query based on map parameters
     *
     * @param parameters
     * @return search query
     */
    private Map findAssetQuery(final Map<String, Object> parameters) {
        final HashMap<String, Object> queryAndParams = new HashMap<String, Object>();
        final List<Object> paramsList = new ArrayList<Object>();
        final StringBuilder sql = new StringBuilder(500);
        sql.append("from Asset asset where asset.code is not null ");

        if (parameters == null) {
            queryAndParams.put("query", sql.toString());
            queryAndParams.put("params", paramsList);
            return queryAndParams;
        }
        int counter = 0;
        if (!StringUtils.isEmpty((String) parameters.get(ASSET_CODE))) {
            sql.append(" and UPPER(asset.code) like '%'||?||'%'");
            paramsList.add(((String) parameters.get(ASSET_CODE)).toUpperCase());
            counter++;
        }
        if (!StringUtils.isEmpty((String) parameters.get(ASSET_TYPE_NAME))) {
            sql.append(" and asset.assetCategory.assetType = ? ");
            paramsList.add(parameters.get(ASSET_TYPE_NAME));
            counter++;
        }
        if (!StringUtils.isEmpty((String) parameters.get(ASSET_CATEGORY_CODE))) {
            final AssetCategory assetCategory = assetCategoryService
                    .getAssetCategoryByCode(((String) parameters.get(ASSET_CATEGORY_CODE)).toUpperCase());
            if (assetCategory == null)
                throw new ValidationException("invalid.assetcategory.code", "Invalid Assetcategory Code");
            else {
                sql.append(" and asset.assetCategory.id = ?");
                paramsList.add(assetCategory.getId());
                counter++;
            }
        }
        if (!StringUtils.isEmpty((String) parameters.get(ASSET_DEPARTMENT_CODE))) {
            final Department dept = departmentService
                    .getDepartmentByCode(((String) parameters.get(ASSET_DEPARTMENT_CODE)).toUpperCase());
            if (dept == null)
                throw new ValidationException("invalid.department.code", "Invalid Department Code");
            else {
                sql.append(" and asset.department.id = ?");
                paramsList.add(dept.getId());
                counter++;
            }
        }
        if (!StringUtils.isEmpty((String) parameters.get(ASSET_WARD_NAME))) {
            // TODO - Fixme: Try to get this API from BoundaryService
            final Boundary boundary = (Boundary) genericService.find(
                    " from Boundary where upper(name)=? and boundaryType=(select id from BoundaryType where upper(name)=? and heirarchyType=(select id from HierarchyType where upper(name)=?))",
                    ((String) parameters.get(ASSET_WARD_NAME)).toUpperCase(), "WARD", "ADMINISTRATION");
            if (boundary == null)
                throw new ValidationException("invalid.ward.name", "Invalid Ward Name");
            else {
                sql.append(" and asset.ward.id = ?");
                paramsList.add(boundary.getId());
                counter++;
            }
        }
        if (parameters.get(ASSET_DESCRIPTION) != null
                && !((String) parameters.get(ASSET_DESCRIPTION)).trim().equalsIgnoreCase("")) {
            sql.append(" and UPPER(asset.description) like '%'||?||'%'");
            paramsList.add(((String) parameters.get(ASSET_DESCRIPTION)).toUpperCase());
            counter++;
        }
        if (parameters.get(ASSET_STATUS_LIST) != null) {
            sql.append(" and asset.status.id in (?").append(counter).append(")");
            final ArrayList<Integer> statuses = new ArrayList<Integer>();
            for (final String statusDesc : (String[]) parameters.get(ASSET_STATUS_LIST)) {
                final EgwStatus status = egwStatusHibernateDAO.getStatusByModuleAndCode("ASSET", statusDesc);
                if (status == null)
                    throw new ValidationException("invalid.status.description", "Invalid Status Description");
                statuses.add(status.getId());
            }
            paramsList.add(statuses);

        }
        queryAndParams.put("query", sql.toString());
        queryAndParams.put("params", paramsList);
        return queryAndParams;
    }

    /**
     * @param pageNumber
     * @param pageSize
     * @return PaginatedList
     */
    public PaginatedList getAssetCategoryPage(final int pageNumber, final int pageSize) {
        final Map queryAndParamsCat = getAssetCategorySearchQuery(null);
        final String query = (String) queryAndParamsCat.get("query");
        final List<Object> paramsCat = (List<Object>) queryAndParamsCat.get("params");
        final Page page = assetCategoryService.findPageBy(query, pageNumber, pageSize, paramsCat.toArray());
        final Long count = (Long) genericService.find("select count(*) " + query, paramsCat.toArray());
        return new EgovPaginatedList(page, count.intValue());

    }

    /**
     * Expecting a map which will have :
     *
     * @param ASSET_CATEGORY_CODE
     * @param ASSET_CATEGORY_NAME
     * @return List<AssetCategory>
     */
    public List<AssetCategory> searchAssetCategory(final Map<String, Object> parameters) {
        final Map queryAndParamsCat = getAssetCategorySearchQuery(parameters);
        final String query = (String) queryAndParamsCat.get("query");
        final List<Object> paramsCat2 = (List<Object>) queryAndParamsCat.get("params");
        return assetCategoryService.findAllBy(query, paramsCat2.toArray());
    }

    /**
     * Expecting a map which will have :
     *
     * @param ASSET_CATEGORY_CODE
     * @param ASSET_CATEGORY_NAME
     * @param parameters
     * @param pageNumber
     * @param pageSize
     * @return PaginatedList
     */
    public PaginatedList searchAssetCategoryPage(final Map<String, Object> parameters, final int pageNumber,
            final int pageSize) {
        final Map queryAndParamsCat = getAssetCategorySearchQuery(parameters);
        final String query = (String) queryAndParamsCat.get("query");
        final List<Object> paramsCat1 = (List<Object>) queryAndParamsCat.get("params");
        final Page page = assetCategoryService.findPageBy(query, pageNumber, pageSize, paramsCat1.toArray());
        final Long count = (Long) genericService.find("select count(*) " + query, paramsCat1.toArray());
        return new EgovPaginatedList(page, count.intValue());
    }

    /**
     * Formulate the query based on map parameters
     *
     * @param parameters
     * @return search query
     */
    private Map getsearchAssetQuery(final Map<String, Object> parameters) {
        final StringBuilder sql = new StringBuilder(260);
        final HashMap<String, Object> queryAndParams1 = new HashMap<String, Object>();
        final List<Object> paramsList1 = new ArrayList<Object>();

        sql.append("from Asset asset where asset.code is not null ");

        if (parameters == null) {
            queryAndParams1.put("query", sql.toString());
            queryAndParams1.put("params", paramsList1);
            return queryAndParams1;
        }
        int counter = 0;
        if (parameters.get(ASSET_CATEGORY_ID) != null) {
            sql.append(" and asset.assetCategory.id = ?");
            paramsList1.add(parameters.get(ASSET_CATEGORY_ID));
            counter++;
        }
        if (parameters.get(ASSET_TYPE) != null) {
            sql.append(" and asset.assetCategory.assetType = ?");
            paramsList1.add(parameters.get(ASSET_TYPE));
            counter++;
        }
        if (parameters.get(ASSET_DEPARTMENT_ID) != null) {
            sql.append(" and asset.assetCategory.department.id = ?");
            paramsList1.add(parameters.get(ASSET_DEPARTMENT_ID));
            counter++;
        }
        if (parameters.get(ASSET_CODE) != null && !((String) parameters.get(ASSET_CODE)).trim().equalsIgnoreCase("")) {
            sql.append(" and UPPER(asset.code) like '%'||?||'%'");
            paramsList1.add(((String) parameters.get(ASSET_CODE)).toUpperCase());
            counter++;
        }
        if (parameters.get(ASSET_DESCRIPTION) != null
                && !((String) parameters.get(ASSET_DESCRIPTION)).trim().equalsIgnoreCase("")) {
            sql.append(" and UPPER(asset.description) like '%'||?||'%'");
            paramsList1.add(((String) parameters.get(ASSET_DESCRIPTION)).toUpperCase());
            counter++;
        }
        if (parameters.get(ASSET_WARD_ID) != null) {
            sql.append(" and asset.ward.id = ?");
            paramsList1.add(parameters.get(ASSET_WARD_ID));
            counter++;
        }
        if (parameters.get(ASSET_STATUS_ID_LIST) != null) {
            final List<Integer> statusIds = (List<Integer>) parameters.get(ASSET_STATUS_ID_LIST);
            if (!statusIds.isEmpty()) {
                sql.append(" and asset.status.id in (?").append(counter).append(")");
                paramsList1.add(statusIds);
            }
        }

        queryAndParams1.put("query", sql.toString());
        queryAndParams1.put("params", paramsList1);
        return queryAndParams1;
    }

    /**
     * Formulate the query based on map parameters
     *
     * @param parameters
     * @return AssetCategorySerahcQuery
     */
    private Map getAssetCategorySearchQuery(final Map<String, Object> parameters) {
        final HashMap<String, Object> queryAndParamsCat = new HashMap<String, Object>();
        final List<Object> paramsListCat = new ArrayList<Object>();
        final StringBuffer sql = new StringBuffer(270);
        sql.append("from AssetCategory assetcategory where assetcategory.code is not null ");

        if (parameters == null) {
            queryAndParamsCat.put("query", sql.toString());
            queryAndParamsCat.put("params", paramsListCat);
            return queryAndParamsCat;
        }
        if (parameters.get(ASSET_CATEGORY_CODE) != null
                && !((String) parameters.get(ASSET_CATEGORY_CODE)).trim().equalsIgnoreCase("")) {
            sql.append(" and UPPER(assetcategory.code) like '%'||?||'%'");
            paramsListCat.add(((String) parameters.get(ASSET_CATEGORY_CODE)).toUpperCase());
        }
        if (parameters.get(ASSET_CATEGORY_NAME) != null
                && !((String) parameters.get(ASSET_CATEGORY_NAME)).trim().equalsIgnoreCase("")) {
            sql.append(" and UPPER(assetcategory.name) like '%'||?||'%'");
            paramsListCat.add(((String) parameters.get(ASSET_CATEGORY_NAME)).toUpperCase());
        }
        queryAndParamsCat.put("query", sql.toString());
        queryAndParamsCat.put("params", paramsListCat);
        return queryAndParamsCat;
    }

    @SuppressWarnings("unchecked")
    public String getAllChilds(final Long parentCatgId) {
        final StringBuffer assetCatIdStr = new StringBuffer(100);
        final List<Long> assetChildCategoryList = genericService.findAllByNamedQuery("ParentChildCategories",
                parentCatgId);
        for (int i = 0, len = assetChildCategoryList.size(); i < len; i++) {
            assetCatIdStr.append(assetChildCategoryList.get(i).toString());
            if (i < len - 1)
                assetCatIdStr.append(',');
        }
        return assetCatIdStr.toString();
    }

    /**
     * Get the list of <code>EgwStatus</code> related to ASSET module.
     *
     * @param statusDescList - List of status descriptions
     * @return
     */
    @SuppressWarnings("unchecked")
    // TODO - Fixme: Try to get this API from Status DAO
    public List<EgwStatus> getStatusListByDescs(final String[] statusDesc) {
        List<EgwStatus> lStatusList = null;
        final List<String> descriptions = Arrays.asList(statusDesc);
        final StringBuffer sql = new StringBuffer(100);
        sql.append("from EgwStatus st where st.moduletype=?  and UPPER(st.description) in (?1) order by description");
        final String query = sql.toString();
        final List<Object> paramList = new ArrayList<Object>();
        paramList.add("ASSET");
        paramList.add(new ArrayList(descriptions));
        lStatusList = genericService.findAllBy(query, paramList.toArray());
        return lStatusList;
    }

    // Setter for Services
    public void setAssetCategoryService(final AssetCategoryService assetCategoryService) {
        this.assetCategoryService = assetCategoryService;
    }

    public void setAssetService(final AssetService assetService) {
        this.assetService = assetService;
    }

    public void setGenericService(final PersistenceService genericService) {
        this.genericService = genericService;
    }

}
