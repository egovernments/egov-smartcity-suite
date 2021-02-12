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

package org.egov.infra.web.controller.admin.masters.boundary;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.contracts.BoundarySearchRequest;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.egov.infra.web.support.json.adapter.BoundaryAdapter;
import org.egov.infra.web.support.json.adapter.BoundaryDatatableAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.egov.infra.utils.ApplicationConstant.ADMIN_HIERARCHY_TYPE;
import static org.egov.infra.utils.ApplicationConstant.BLOCK_BOUNDARY_TYPE;
import static org.egov.infra.utils.ApplicationConstant.REVENUE_HIERARCHY_TYPE;
import static org.egov.infra.utils.ApplicationConstant.WARD_BOUNDARY_TYPE;
import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping("/boundary")
public class SearchBoundaryController {
	
    @Autowired
    private HierarchyTypeService hierarchyTypeService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private CrossHierarchyService crossHierarchyService;


    @ModelAttribute("hierarchyTypes")
    public List<HierarchyType> hierarchyTypes() {
        return hierarchyTypeService.getAllHierarchyTypes();
    }

    @GetMapping("search")
    public String showBoundarySearchForm() {
        return "boundary-search";
    }

    @PostMapping(value = "search", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchBoundary(@Valid BoundarySearchRequest searchRequest, BindingResult bindResult) {
        return new DataTable<>(bindResult.hasErrors() ? new PageImpl<>(emptyList())
                : boundaryService.getPageOfBoundaries(searchRequest), searchRequest.draw())
                .toJson(BoundaryDatatableAdapter.class);

    }

    @GetMapping(value = "wards-by-zone", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Boundary> getWardByZone(@RequestParam Long zoneId) {
        return boundaryService.getActiveChildBoundariesByBoundaryId(zoneId);
    }

    @GetMapping(value = "by-boundarytype", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String boundaryByBoundaryType(@RequestParam Long boundaryTypeId) {
        return toJSON(boundaryService
                .getActiveBoundariesByBoundaryTypeId(boundaryTypeId), Boundary.class, BoundaryAdapter.class);
    }

    @GetMapping("block/by-locality")
    @ResponseBody
    public String blockByLocality(@RequestParam Long locality) {
        BoundaryType blockType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName(BLOCK_BOUNDARY_TYPE, REVENUE_HIERARCHY_TYPE);
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

    @GetMapping("block/by-ward")
    @ResponseBody
    public String blockByWard(@RequestParam Long wardId) {
        return boundaryJsonData(boundaryService.getActiveChildBoundariesByBoundaryId(wardId));
    }

    @GetMapping("ward/by-locality")
    @ResponseBody
    public String wardsByLocality(@RequestParam Long locality) {
        BoundaryType wardBoundaryType = boundaryTypeService
                .getBoundaryTypeByNameAndHierarchyTypeName(WARD_BOUNDARY_TYPE, ADMIN_HIERARCHY_TYPE);
        return boundaryJsonData(crossHierarchyService
                .getParentBoundaryByChildBoundaryAndParentBoundaryType(locality, wardBoundaryType.getId()));
    }

    @GetMapping("by-name-and-type")
    @ResponseBody
    public String getBoundariesByType(@RequestParam String boundaryName, @RequestParam Long boundaryTypeId) {
        return boundaryJsonData(boundaryService
                .getBondariesByNameAndTypeOrderByBoundaryNumAsc("%" + boundaryName + "%", boundaryTypeId));
    }

    @GetMapping(value = "child/by-parent")
    @ResponseBody
    public String getChildBoundariesById(@RequestParam Long parentId) {
        return boundaryJsonData(crossHierarchyService.getActiveChildBoundariesByParentId(parentId));
    }

    private String boundaryJsonData(List<Boundary> boundaries) {
        List<JsonObject> boundaryJson = new ArrayList<>();
        for (Boundary boundary : boundaries) {
            JsonObject boundaryData = new JsonObject();
            boundaryData.addProperty("id", boundary.getId());
            boundaryData.addProperty("name", boundary.getName());
            boundaryJson.add(boundaryData);
        }
        return boundaryJson.toString();
    }

	@GetMapping({"ajaxboundary-activeblockbylocality"})
    public void activeBlockByLocality(@RequestParam Long locality, HttpServletResponse response) throws IOException {
        BoundaryType blockType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName(BLOCK_BOUNDARY_TYPE, REVENUE_HIERARCHY_TYPE);
        List<Boundary> blocks = crossHierarchyService.getActiveParentBoundaryByChildBoundaryAndParentBoundaryType(locality, blockType.getId());
        List<Boundary> streets = boundaryService.getActiveChildBoundariesByBoundaryId(locality);
        final List<JsonObject> wardJsonObjs = new ArrayList<>();
        final List<Long> boundaries = new ArrayList<>();
        for (final Boundary block : blocks) {
            final Boundary ward = block.getParent();
            final JsonObject jsonObject = new JsonObject();
            if (!boundaries.contains(ward.getId()) && ward.isActive()) {
                jsonObject.addProperty("wardId", ward.getId());
                jsonObject.addProperty("wardName", ward.getName());
            }
            jsonObject.addProperty("blockId", block.getId());
            jsonObject.addProperty("blockName", block.getName());
            wardJsonObjs.add(jsonObject);
            boundaries.add(ward.getId());
        }
        final List<JsonObject> streetJsonObjs = new ArrayList<>();
        for (final Boundary street : streets) {
            final JsonObject streetObj = new JsonObject();
            streetObj.addProperty("streetId", street.getId());
            streetObj.addProperty("streetName", street.getName());
            streetJsonObjs.add(streetObj);
        }
        final Map<String, List<JsonObject>> map = new HashMap<>();
        map.put("boundaries", wardJsonObjs);
        map.put("streets", streetJsonObjs);
        final JsonObject bj = new JsonObject();
        bj.add("results", new Gson().toJsonTree(map));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(bj.toString(), response.getWriter());
    }
}
