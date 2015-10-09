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
package org.egov.infra.web.controller.admin.masters;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class GenericMasterAjaxController {

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/boundarytype/ajax/boundarytypelist-for-hierarchy", method = RequestMethod.GET)
    public @ResponseBody void getBoundaryTypeByHierarchyType(@RequestParam final Long hierarchyTypeId,
            final HttpServletResponse response) throws IOException {
        final List<BoundaryType> boundaryTypes = boundaryTypeService
                .getAllBoundarTypesByHierarchyTypeId(hierarchyTypeId);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(buildJSONString(boundaryTypes), response.getWriter());
    }

    @RequestMapping(value = "/boundaries-by-boundaryType", method = RequestMethod.GET)
    public @ResponseBody void getBoundariesByBoundaryType(@RequestParam final Long boundaryTypeId,
            final HttpServletResponse response) throws IOException {
        final List<Boundary> boundaries = boundaryService.getAllBoundariesByBoundaryTypeId(boundaryTypeId);
        final JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;

        for (final Boundary boundary : boundaries) {
            jsonObject = new JSONObject();
            jsonObject.put("Text", boundary.getLocalName());
            jsonObject.put("Value", boundary.getId());
            jsonArray.add(jsonObject);
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(jsonArray.toString(), response.getWriter());
    }

    @RequestMapping(value = "/check-is-root", method = RequestMethod.GET)
    public @ResponseBody boolean isRootBoundary(@RequestParam final Long boundaryTypeId,
            @RequestParam final Long hierarchyTypeId) {
        final BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeByIdAndHierarchyType(boundaryTypeId,
                hierarchyTypeId);
        return boundaryType.getParent() == null ? false : boundaryType.getParent().getId() == 0 ? true : false;
    }

    // FIXME Can be made generic by the help of annotation which takes fields as
    // inputs [Nayeem]
    private String buildJSONString(final List<BoundaryType> boundaryTypes) {
        final JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;

        for (final BoundaryType boundaryType : boundaryTypes) {
            jsonObject = new JSONObject();
            jsonObject.put("Text", boundaryType.getName());
            jsonObject.put("Value", boundaryType.getId());
            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }

    /*
     * Used in ajax validation to check if child exists for a boundary type -
     * Add child screen
     */
    @RequestMapping(value = "/boundarytype/ajax/checkchild", method = RequestMethod.GET)
    public @ResponseBody boolean isChildBoundaryTypePresent(@RequestParam final Long parentId) {
        final BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeByParent(parentId);
        return boundaryType != null ? Boolean.TRUE : Boolean.FALSE;
    }

    @RequestMapping(value = "/userRole/ajax/rolelist-for-user", method = RequestMethod.GET)
    public @ResponseBody void getRolesByUserName(@RequestParam final String username,
            final HttpServletResponse response) throws IOException {
        if (username != null) {
            final Set<Role> roles = userService.getRolesByUsername(username);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            IOUtils.write(buildRoles(roles), response.getWriter());
        }
    }

    private String buildRoles(final Set<Role> roles) {
        final JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        for (final Role role : roles) {
            jsonObject = new JSONObject();
            jsonObject.put("Value", role.getId());
            jsonObject.put("Text", role.getName());
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    @RequestMapping(value = { "/userRole/ajax/userlist" }, method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void getAllActiveUserByNameLike(@RequestParam final String userName,
            final HttpServletResponse response) throws IOException {
        final List<User> userList = userService.findAllByMatchingUserNameForType(userName, UserType.EMPLOYEE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(buildUser(userList), response.getWriter());
    }

    private String buildUser(final List<User> users) {
        final JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        for (final User user : users) {
            jsonObject = new JSONObject();
            jsonObject.put("Value", user.getId());
            jsonObject.put("Text", user.getUsername());
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }
}
