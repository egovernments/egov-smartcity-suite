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

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.HierarchyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("boundary/update")
public class UpdateBoundaryController {

    private static final String BOUNDARY_UPDATE_VIEW = "boundary-update";
    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private HierarchyTypeService hierarchyTypeService;

    @ModelAttribute
    public Boundary boundary(@PathVariable Optional<Long> id) {
        return id.isPresent() ? boundaryService.getBoundaryById(id.get()) : new Boundary();
    }

    @ModelAttribute("hierarchyTypes")
    public List<HierarchyType> hierarchyTypes() {
        return hierarchyTypeService.getAllHierarchyTypes();
    }

    @GetMapping("/")
    public String showUpdateBoundarySearchForm(Model model) {
        model.addAttribute("search", true);
        return BOUNDARY_UPDATE_VIEW;
    }

    @GetMapping("{id}")
    public String showUpdateBoundaryForm(Model model) {
        model.addAttribute("search", false);
        return BOUNDARY_UPDATE_VIEW;
    }

    @PostMapping("{id}")
    public String updateBoundary(@Valid @ModelAttribute Boundary boundary, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boundaryType", boundary.getBoundaryType());
            model.addAttribute("parentBoundary", boundaryService
                    .getActiveBoundariesByBoundaryTypeId(boundary.getBoundaryType().getParent().getId()));
            return BOUNDARY_UPDATE_VIEW;
        }
        boundaryService.updateBoundary(boundary);
        redirectAttributes.addFlashAttribute("message", "msg.bndry.update.success");
        redirectAttributes.addFlashAttribute("edit", true);
        return "redirect:/boundary/view/" + boundary.getId();
    }

}
