/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.web.controller.admin.masters;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class GenericMasterAjaxController {

    private static final String BLOCK = "Block";
    private static final String REVENUE_HIERARCHY_TYPE = "REVENUE";
    private static final String DISPLAY_KEY = "Text";
    private static final String VALUE_KEY = "Value";

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @RequestMapping(value = "/boundarytype/ajax/boundarytypelist-for-hierarchy", method = RequestMethod.GET)
    @ResponseBody
    public void getBoundaryTypeByHierarchyType(@RequestParam Long hierarchyTypeId, HttpServletResponse response)
            throws IOException {
        final List<BoundaryType> boundaryTypes = boundaryTypeService.getAllBoundarTypesByHierarchyTypeId(hierarchyTypeId);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(buildJSONString(boundaryTypes), response.getWriter());
    }

    @RequestMapping(value = "/boundaries-by-boundaryType", method = RequestMethod.GET)
    @ResponseBody
    public void getBoundariesByBoundaryType(@RequestParam Long boundaryTypeId, HttpServletResponse response)
            throws IOException {
        BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeById(boundaryTypeId);
        final List<Boundary> boundaries = boundaryService.getAllBoundariesOrderByBoundaryNumAsc(boundaryType);
        final JSONArray jsonArray = new JSONArray();
        for (final Boundary boundary : boundaries) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(DISPLAY_KEY, boundary.getLocalName());
            jsonObject.put(VALUE_KEY, boundary.getId());
            jsonArray.add(jsonObject);
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(jsonArray.toString(), response.getWriter());
    }

    @RequestMapping(value = "/check-is-root", method = RequestMethod.GET)
    @ResponseBody
    public boolean isRootBoundary(@RequestParam Long boundaryTypeId, @RequestParam Long hierarchyTypeId) {
        final BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeByIdAndHierarchyType(boundaryTypeId,
                hierarchyTypeId);
        return boundaryType.getParent() != null && boundaryType.getParent().getId() == 0;
    }

    private String buildJSONString(List<BoundaryType> boundaryTypes) {
        final JSONArray jsonArray = new JSONArray();
        for (final BoundaryType boundaryType : boundaryTypes) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(DISPLAY_KEY, boundaryType.getName());
            jsonObject.put(VALUE_KEY, boundaryType.getId());
            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }

    /*
     * Used in ajax validation to check if child exists for a boundary type -
     * Add child screen
     */
    @RequestMapping(value = "/boundarytype/ajax/checkchild", method = RequestMethod.GET)
    @ResponseBody
    public boolean isChildBoundaryTypePresent(@RequestParam Long parentId) {
        final BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeByParent(parentId);
        return boundaryType != null;
    }

    @RequestMapping(value = "/userRole/ajax/rolelist-for-user", method = RequestMethod.GET)
    @ResponseBody
    public void getRolesByUserName(@RequestParam String username, HttpServletResponse response) throws IOException {
        if (username != null) {
            final Set<Role> roles = userService.getRolesByUsername(username);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            IOUtils.write(buildRoles(roles), response.getWriter());
        }
    }

    private String buildRoles(Set<Role> roles) {
        final JSONArray jsonArray = new JSONArray();
        for (final Role role : roles) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(VALUE_KEY, role.getId());
            jsonObject.put(DISPLAY_KEY, role.getName());
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    @RequestMapping(value = {"/userRole/ajax/userlist"}, method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void getAllActiveUserByNameLike(@RequestParam String userName, HttpServletResponse response) throws IOException {
        final List<User> userList = userService.findAllByMatchingUserNameForType(userName, UserType.EMPLOYEE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(buildUser(userList), response.getWriter());
    }

    private String buildUser(List<User> users) {
        final JSONArray jsonArray = new JSONArray();
        for (final User user : users) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(VALUE_KEY, user.getId());
            jsonObject.put(DISPLAY_KEY, user.getUsername());
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    @RequestMapping(value = {"/boundary/ajaxBoundary-blockByLocality", "/public/boundary/ajaxBoundary-blockByLocality"}, method = RequestMethod.GET)
    public void blockByLocality(@RequestParam Long locality, HttpServletResponse response) throws IOException {
        BoundaryType blockType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName(BLOCK, REVENUE_HIERARCHY_TYPE);
        final List<Boundary> blocks = crossHierarchyService.getParentBoundaryByChildBoundaryAndParentBoundaryType(locality, blockType.getId());
        List<Boundary> streets = boundaryService.getChildBoundariesByBoundaryId(locality);
        final List<JSONObject> wardJsonObjs = new ArrayList<>();
        final List<Long> boundaries = new ArrayList<>();
        for (final Boundary block : blocks) {
            final Boundary ward = block.getParent();
            final JSONObject jsonObject = new JSONObject();
            if (!boundaries.contains(ward.getId())) {
                jsonObject.put("wardId", ward.getId());
                jsonObject.put("wardName", ward.getName());
            }
            jsonObject.put("blockId", block.getId());
            jsonObject.put("blockName", block.getName());
            wardJsonObjs.add(jsonObject);
            boundaries.add(ward.getId());
        }
        final List<JSONObject> streetJsonObjs = new ArrayList<>();
        for (final Boundary street : streets) {
            final JSONObject streetObj = new JSONObject();
            streetObj.put("streetId", street.getId());
            streetObj.put("streetName", street.getName());
            streetJsonObjs.add(streetObj);
        }
        final Map<String, List<JSONObject>> map = new HashMap<>();
        map.put("boundaries", wardJsonObjs);
        map.put("streets", streetJsonObjs);
        final JSONObject bj = new JSONObject();
        bj.put("results", map);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(bj.toString(), response.getWriter());
    }

    @RequestMapping(value = {"/boundary/ajaxBoundary-blockByWard","/public/boundary/ajaxBoundary-blockByWard"}, method = RequestMethod.GET)
    public void blockByWard(@RequestParam Long wardId, HttpServletResponse response) throws IOException {
        List<Boundary> blocks = boundaryService.getActiveChildBoundariesByBoundaryId(wardId);
        final List<JSONObject> jsonObjects = new ArrayList<>();
        for (final Boundary block : blocks) {
            final JSONObject jsonObj = new JSONObject();
            jsonObj.put("blockId", block.getId());
            jsonObj.put("blockName", block.getName());
            jsonObjects.add(jsonObj);
        }
        IOUtils.write(jsonObjects.toString(), response.getWriter());
    }
}
