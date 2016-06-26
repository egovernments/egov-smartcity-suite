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

package org.egov.infra.web.controller.admin.masters.crossHierarchy;

import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.CrossHierarchy;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/crossHierarchy/update/{nameArray}")
public class ModifyCrossHierarchyController {

    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @ModelAttribute
    public CrossHierarchyGenerator crossHierarchyGenerator() {
        return new CrossHierarchyGenerator();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String crossHierarchyFormForUpdate(@ModelAttribute final CrossHierarchyGenerator crossHierarchyGenerator,
            @PathVariable final String[] nameArray, final Model model) {
        Boundary boundary = null;
        BoundaryType boundaryType = null;
        if (nameArray.length > 1) {
            boundary = boundaryService.getBoundaryById(Long.parseLong(nameArray[0]));
            boundaryType = boundaryTypeService.getBoundaryTypeById(Long.parseLong(nameArray[1]));
        }

        final List<Boundary> boundaryList = getBoundaryByBoundaryType();
        final List<Boundary> mappedBoundary = getMappedLocationForBoundary(Long.parseLong(nameArray[0]));
        boundaryList.remove(mappedBoundary);
        model.addAttribute("boundary", boundary);
        model.addAttribute("boundaryType", boundaryType);
        model.addAttribute("boundaryList", boundaryList);
        model.addAttribute("mappedBoundary", mappedBoundary);

        return "crossHierarchy-editform";

    }

    @RequestMapping(method = RequestMethod.POST)
    public String crossHierarchyFormForUpdate(@ModelAttribute final CrossHierarchyGenerator crossHierarchyGenerator,
            final BindingResult errors, final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors())
            return "crossHierarchy-editform";

        final Boundary boundary = crossHierarchyGenerator.getBoundary();
        final BoundaryType boundaryType = boundaryTypeService
                .getBoundaryTypeById(crossHierarchyGenerator.getBoundaryType().getId());
        final List<Boundary> mappedBoundaries = getMappedLocationForBoundary(boundary.getId());
        final List<Boundary> selectedBoundaries = crossHierarchyGenerator.getBoundaries();
        if(!mappedBoundaries.isEmpty()){
        mappedBoundaries.remove(selectedBoundaries);
        
        for (final Boundary mappedBoundary : mappedBoundaries) {
            final CrossHierarchy existingCrossHierarchy = crossHierarchyService
                    .findAllByParentAndChildBoundary(boundary.getId(), mappedBoundary.getId());
            if (existingCrossHierarchy != null)
                crossHierarchyService.delete(existingCrossHierarchy);
        }
        }
        if(!selectedBoundaries.isEmpty()){
        for (final Boundary mappedBoundary : crossHierarchyGenerator.getBoundaries()) {
            final CrossHierarchy crossHierarchy = new CrossHierarchy();
            final CrossHierarchy existingCrossHierarchy = crossHierarchyService
                    .findAllByParentAndChildBoundary(boundary.getId(), mappedBoundary.getId());
            if (existingCrossHierarchy == null) {
                crossHierarchy.setChild(mappedBoundary);
                crossHierarchy.setParent(boundary);
                crossHierarchy.setParentType(boundaryType);
                crossHierarchy.setChildType(mappedBoundary.getBoundaryType());
                crossHierarchyService.create(crossHierarchy);
            }
        }

        }

        model.addAttribute("message", "msg.crossHierarchy.update.success");

        return "crossHierarchy-success";
    }

    public List<Boundary> getBoundaryByBoundaryType() {
        final BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeByName("Locality");
        final List<Boundary> boundaryList = boundaryService.getAllBoundariesByBoundaryTypeId(boundaryType.getId());
        return boundaryList;
    }

    public List<Boundary> getMappedLocationForBoundary(final Long boundaryId) {

        final List<Boundary> boundaryList = crossHierarchyService.getActiveChildBoundariesByBoundaryId(boundaryId);
        return boundaryList;
    }
}
