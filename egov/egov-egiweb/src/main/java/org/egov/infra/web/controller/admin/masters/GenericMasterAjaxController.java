/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.infra.web.controller.admin.masters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.infra.utils.ApplicationConstant.ADMIN_HIERARCHY_TYPE;

@Controller
public class GenericMasterAjaxController {

    private static final String BLOCK = "Block";
    private static final String REVENUE_HIERARCHY_TYPE = "REVENUE";

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @GetMapping("/boundary/block/by-locality")
    @ResponseBody
    public String blockByLocality(@RequestParam Long locality) {
        BoundaryType blockType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName(BLOCK, REVENUE_HIERARCHY_TYPE);
        List<Boundary> blocks = crossHierarchyService.getParentBoundaryByChildBoundaryAndParentBoundaryType(locality, blockType.getId());
        List<Boundary> streets = boundaryService.getActiveChildBoundariesByBoundaryId(locality);
        List<JsonObject> wardJsonObjs = new ArrayList<>();
        List<Long> boundaries = new ArrayList<>();
        for (Boundary block : blocks) {
            Boundary ward = block.getParent();
            JsonObject jsonObject = new JsonObject();
            if (!boundaries.contains(ward.getId())) {
                jsonObject.addProperty("wardId", ward.getId());
                jsonObject.addProperty("wardName", ward.getName());
            }
            jsonObject.addProperty("blockId", block.getId());
            jsonObject.addProperty("blockName", block.getName());
            wardJsonObjs.add(jsonObject);
            boundaries.add(ward.getId());
        }
        List<JsonObject> streetJsonObjs = new ArrayList<>();
        for (Boundary street : streets) {
            JsonObject streetObj = new JsonObject();
            streetObj.addProperty("streetId", street.getId());
            streetObj.addProperty("streetName", street.getName());
            streetJsonObjs.add(streetObj);
        }
        Map<String, List<JsonObject>> map = new HashMap<>();
        map.put("boundaries", wardJsonObjs);
        map.put("streets", streetJsonObjs);
        JsonObject blockJSON = new JsonObject();
        blockJSON.add("results", new Gson().toJsonTree(map));
        return blockJSON.toString();
    }

    @GetMapping("/boundary/block/by-ward")
    @ResponseBody
    public String blockByWard(@RequestParam Long wardId) {
        List<Boundary> blocks = boundaryService.getActiveChildBoundariesByBoundaryId(wardId);
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (Boundary block : blocks) {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("blockId", block.getId());
            jsonObj.addProperty("blockName", block.getName());
            jsonObjects.add(jsonObj);
        }
        return jsonObjects.toString();
    }

    @GetMapping("/boundary/ward/by-locality")
    @ResponseBody
    public String wardsByLocality(@RequestParam Long locality) {
        BoundaryType wardBoundaryType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName("Ward", ADMIN_HIERARCHY_TYPE);
        List<Boundary> wards = crossHierarchyService.getParentBoundaryByChildBoundaryAndParentBoundaryType(locality, wardBoundaryType.getId());
        List<JsonObject> jsonObjects = new ArrayList<>();
        for (Boundary block : wards) {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("wardId", block.getId());
            jsonObj.addProperty("wardName", block.getName());
            jsonObjects.add(jsonObj);
        }
        return jsonObjects.toString();
    }
}
