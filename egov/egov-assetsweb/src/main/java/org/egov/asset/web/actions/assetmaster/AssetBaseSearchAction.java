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

import org.apache.log4j.Logger;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetType;
import org.egov.asset.util.AssetCommonUtil;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;

public abstract class AssetBaseSearchAction extends SearchFormAction {

    private static final long serialVersionUID = 3403911393047059235L;
    protected Asset asset = new Asset();
    protected String assetType;
    protected Long zoneId;

    @Autowired
    private AssetCommonUtil assetCommonUtil;
    private static final Logger LOGGER = Logger.getLogger(AssetBaseSearchAction.class);

    public AssetBaseSearchAction() {
        addRelatedEntity("department", Department.class);
        addRelatedEntity("assetCategory", AssetCategory.class);
        addRelatedEntity("area", Boundary.class);
        addRelatedEntity("location", Boundary.class);
        addRelatedEntity("street", Boundary.class);
        addRelatedEntity("ward", Boundary.class);
        addRelatedEntity("zone", Boundary.class);
        addRelatedEntity("status", EgwStatus.class);
    }

    @Override
    public abstract SearchQuery prepareQuery(String sortField, String sortOrder);

    @Override
    public Object getModel() {

        return asset;
    }

    @Override
    public void prepare() {
        super.prepare();
        setupDropdownDataExcluding("area", "location", "street", "ward", "zone", "status");
        addDropdownData("assetTypeList", Arrays.asList(AssetType.values()));
        addDropdownData("areaList", Collections.EMPTY_LIST);
        addDropdownData("locationList", Collections.EMPTY_LIST);
        addDropdownData("wardList", Collections.EMPTY_LIST);
        addDropdownData("streetList", Collections.EMPTY_LIST);
        addDropdownData("zoneList", assetCommonUtil.getAllZoneOfHTypeAdmin());
    }

    /**
     * data loaded by Ajax need to be reloaded again in the screen(specially required when validaion fails)
     */
    protected void loadPreviousData() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Loading ajax data");
        setAssetType(assetType);
        if (null != zoneId && zoneId != -1)
            addDropdownData("wardList", assetCommonUtil.populateWard(zoneId));
        if (null != asset.getWard() && asset.getWard().getId() != -1) {
            addDropdownData("streetList", assetCommonUtil.populateStreets(asset.getWard().getId()));
            addDropdownData("areaList", assetCommonUtil.populateArea(asset.getWard().getId()));
        }
        if (null != asset.getArea() && asset.getArea().getId() != -1)
            addDropdownData("locationList", assetCommonUtil.populateLocations(asset.getArea().getId()));

    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(final Asset asset) {
        this.asset = asset;
    }

    public String getAssetType() {
        return assetType;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setAssetType(final String assetType) {
        this.assetType = assetType;
    }

    public void setZoneId(final Long zoneId) {
        this.zoneId = zoneId;
    }

}
