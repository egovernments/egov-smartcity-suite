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
package org.egov.assets.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class AssetCommonUtil {

    private static final Logger LOGGER = Logger.getLogger(AssetCommonUtil.class);
    private static final String ADMIN_HIERARCHY_TYPE = "ADMINISTRATION";
    private static final String Zone_BOUNDARY_TYPE = "Zone";
    private static String hierarchyTypeName = "LOCATION";
    @Autowired
    private HierarchyTypeService heirarchyTypeService;
    
    @Autowired
    private CrossHierarchyService crossHeirarchyService;
    
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @SuppressWarnings("unchecked")
    public List<Boundary> getAllZoneOfHTypeAdmin() {
        HierarchyType hType = heirarchyTypeService.getHierarchyTypeByName(ADMIN_HIERARCHY_TYPE);
       List<Boundary> zoneList = null;
        final BoundaryType bType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType(Zone_BOUNDARY_TYPE, hType);
        zoneList = boundaryService.getChildBoundariesByBoundaryId(bType.getId());
        return zoneList;
    }

    /**
     * Populate the ward list by zone
     */
    public List<Boundary> populateWard(final Long zoneId) {
        List<Boundary> wardList = new LinkedList<Boundary>();
        try {
            wardList = boundaryService.getChildBoundariesByBoundaryId(zoneId);
        } catch (final Exception e) {
            LOGGER.error("Error while loading warda - wards." + e.getMessage());
            throw new ApplicationRuntimeException("Unable to load ward information", e);
        }
        return wardList;
    }

    /**
     * Populate the Area list by ward
     */
    @SuppressWarnings("unchecked")
    public List<Boundary> populateArea(final Long wardId) {
        HierarchyType hType = heirarchyTypeService.getHierarchyTypeByName(hierarchyTypeName);;
        List<Boundary> areaList = new LinkedList<Boundary>();
        final BoundaryType childBoundaryType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType("Area", hType);
        final Boundary parentBoundary = boundaryService.getBoundaryById(wardId);
        areaList = new LinkedList(crossHeirarchyService.getCrossHierarchyChildrens(parentBoundary, childBoundaryType));

        LOGGER.info("***********Ajax AreaList: " + areaList.toString());
        return areaList;
    }

    /**
     * Populate the street list by Ward
     *
     * @throws Exception
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Boundary> populateStreets(final Long wardId) {
        HierarchyType hType = heirarchyTypeService.getHierarchyTypeByName(hierarchyTypeName);
        List<Boundary> streetList = new LinkedList<Boundary>();
        final BoundaryType childBoundaryType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyType("Street",
                hType);
        final Boundary parentBoundary = boundaryService.getBoundaryById(wardId);
        streetList = new LinkedList(crossHeirarchyService.getCrossHierarchyChildrens(parentBoundary, childBoundaryType));
        return streetList;
    }

    /**
     * Populate the location list by area
     */
    public List<Boundary> populateLocations(final Long areaId) {
        List<Boundary> locationList = new LinkedList<Boundary>();
        try {
            locationList = boundaryService.getChildBoundariesByBoundaryId(areaId);
        } catch (final Exception e) {
            LOGGER.error("Error while loading locations - locations." + e.getMessage());
            throw new ApplicationRuntimeException("Unable to load location information", e);
        }
        LOGGER.info("***********Ajax locationList: " + locationList.toString());
        return locationList;
    }

    public static Date loadCurrentDate() {
        final Date currDate = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(sdf.format(currDate));
        } catch (final ParseException e) {
            throw new ValidationException(Arrays
                    .asList(new ValidationError("Exception while formatting voucher date", "Transaction failed")));
        }
    }

     public void setBoundaryService(final BoundaryService boundaryService) {
        this.boundaryService = boundaryService;
    }

    public void setBoundaryTypeService(final BoundaryTypeService boundaryTypeService) {
        this.boundaryTypeService = boundaryTypeService;
    }
    
	public String serialize(Object obj)
	{
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        String jsonResponse="";
		try {
			jsonResponse = objectMapper.writeValueAsString(obj);
		} catch (IOException e) {

			throw new ApplicationRuntimeException("Exception while converting to json"+obj.getClass().getCanonicalName());
		}
        return jsonResponse;
	}
}
