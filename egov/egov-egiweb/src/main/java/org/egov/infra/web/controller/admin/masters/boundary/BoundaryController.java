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

package org.egov.infra.web.controller.admin.masters.boundary;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class BoundaryController {

    private static final String REDIRECT_URL_VIEW = "redirect:/view-boundary/";

    private final BoundaryService boundaryService;
    private final BoundaryTypeService boundaryTypeService;

    @Autowired
    public BoundaryController(final BoundaryService boundaryService, final BoundaryTypeService boundaryTypeService) {
        this.boundaryService = boundaryService;
        this.boundaryTypeService = boundaryTypeService;
    }

    @ModelAttribute
    public void getBoundary(@PathVariable final Long[] ids, final Model model) {
        final int paramLength = ids.length;
        Long boundaryId = null;

        if (paramLength > 2 && ids[2] != null) {
            boundaryId = ids[2];
            model.addAttribute("boundary", boundaryService.getBoundaryById(boundaryId));
        } else if (model.asMap().get("boundary") == null)
            model.addAttribute("boundary", new Boundary());

        final Long boundaryTypeId = ids[1];
        final BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeById(boundaryTypeId);
        model.addAttribute("boundaryType", boundaryType);
        model.addAttribute("hierarchyType", boundaryType.getHierarchyType());
    }

    @RequestMapping(value = "/view-boundary/{ids}", method = RequestMethod.GET)
    public String viewBoundaryDetails() {
        return "boundary-view";
    }

    @RequestMapping(value = "/create-boundary/{ids}", method = RequestMethod.POST)
    public String showCreateBoundaryForm(final Model model, final RedirectAttributes redirectAttributes) {
        model.addAttribute("isUpdate", false);
        final BoundaryType boundaryType = (BoundaryType) model.asMap().get("boundaryType");
        if (boundaryService.validateBoundary(boundaryType)) {
            redirectAttributes.addFlashAttribute("warning", "err.root.bndry.exists");
            return "redirect:/search-boundary";
        } else {
            if (boundaryType != null && boundaryType.getParent() != null)
                model.addAttribute("parentBoundary", boundaryService.getActiveBoundariesByBoundaryTypeId(boundaryType.getParent().getId()));
            return "boundary-create";
        }

    }

    @RequestMapping(value = "/update-boundary/{ids}", method = RequestMethod.GET)
    public String showUpdateBoundaryForm(final Model model) {
        model.addAttribute("isUpdate", true);
        final BoundaryType boundaryType = (BoundaryType) model.asMap().get("boundaryType");
        if (boundaryType != null && boundaryType.getParent() != null)
            model.addAttribute("parentBoundary", boundaryService.getActiveBoundariesByBoundaryTypeId(boundaryType.getParent().getId()));

        return "boundary-create";
    }

    @RequestMapping(value = "/list-boundaries/{ids}", method = RequestMethod.POST)
    public String showPaginationForm() {
        return "view-boundaries";
    }

    @RequestMapping(value = "/update-boundary/{ids}", method = RequestMethod.POST)
    public String UpdateBoundary(@Valid @ModelAttribute final Boundary boundary, final BindingResult errors,
                                 final RedirectAttributes redirectAttributes, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("isUpdate", true);
            model.addAttribute("boundaryType", boundary.getBoundaryType());
            model.addAttribute("parentBoundary", boundaryService.getActiveBoundariesByBoundaryTypeId(boundary.getBoundaryType().getParent().getId()));
            return "boundary-create";
        }
        boundaryService.updateBoundary(boundary);
        redirectAttributes.addFlashAttribute("boundary", boundary);
        redirectAttributes.addFlashAttribute("message", "msg.bndry.update.success");
        return REDIRECT_URL_VIEW + boundary.getBoundaryType().getHierarchyType().getId() + "," + boundary.getBoundaryType().getId();
    }
}
