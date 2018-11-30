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

package org.egov.infra.web.controller.admin.masters.boundarytype;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/boundarytype")
public class SearchBoundaryTypeController {

    @Autowired
    private HierarchyTypeService hierarchyTypeService;

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @ModelAttribute
    public BoundaryType boundaryTypeModel() {
        return new BoundaryType();
    }

    @GetMapping("by-hierarchy")
    @ResponseBody
    public String getBoundaryTypeByHierarchyType(@RequestParam Long hierarchyTypeId) {
        JsonArray boundaryTypes = new JsonArray();
        for (BoundaryType boundaryType : boundaryTypeService.getAllBoundarTypesByHierarchyTypeId(hierarchyTypeId)) {
            JsonObject boundaryTypeData = new JsonObject();
            boundaryTypeData.addProperty("name", boundaryType.getName());
            boundaryTypeData.addProperty("id", boundaryType.getId());
            boundaryTypes.add(boundaryTypeData);
        }

        return boundaryTypes.toString();
    }

    @GetMapping("has-child-boundarytype")
    @ResponseBody
    public boolean childBoundaryTypeIsPresent(@RequestParam Long parentId) {
        return boundaryTypeService.getBoundaryTypeByParent(parentId) != null;
    }

    @GetMapping("view")
    public String viewSearch(Model model) {
        model.addAttribute("hierarchyTypes", hierarchyTypeService.getAllHierarchyTypes());
        model.addAttribute("mode", "view");
        return "boundaryType-search";
    }

    @GetMapping("update")
    public String updateSearch(Model model) {
        model.addAttribute("hierarchyTypes", hierarchyTypeService.getAllHierarchyTypes());
        model.addAttribute("mode", "update");
        return "boundaryType-search";
    }

    @GetMapping("addchild")
    public String addChildSearch(Model model) {
        model.addAttribute("hierarchyTypes", hierarchyTypeService.getAllHierarchyTypes());
        model.addAttribute("mode", "addChild");
        return "boundaryType-search";
    }

    @PostMapping({"view", "update", "addchild"})
    public String search(@ModelAttribute BoundaryType boundaryType, BindingResult bindResult, HttpServletRequest request) {
        if (bindResult.hasErrors())
            return "boundaryType-form";

        String requestURI = request.getRequestURI();
        String redirectURI = "";
        String[] idSplit = boundaryType.getName().split(",");
        Long boundaryTypeId = Long.valueOf(idSplit[idSplit.length - 1]);

        if (requestURI.contains("view"))
            redirectURI = "redirect:/boundarytype/view/" + boundaryTypeId;
        else if (requestURI.contains("update"))
            redirectURI = "redirect:/boundarytype/update/" + boundaryTypeId;
        else if (requestURI.contains("addchild"))
            redirectURI = "redirect:/boundarytype/addchild/" + boundaryTypeId;

        return redirectURI;
    }
}