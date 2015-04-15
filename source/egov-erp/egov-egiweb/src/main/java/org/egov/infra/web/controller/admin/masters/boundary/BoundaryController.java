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
package org.egov.infra.web.controller.admin.masters.boundary;

import javax.validation.Valid;

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

@Controller
public class BoundaryController {

    private static final String REDIRECT_URL_VIEW = "redirect:/view-boundary/";
    
    private BoundaryService boundaryService;
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    public BoundaryController(BoundaryService boundaryService, BoundaryTypeService boundaryTypeService) {
        this.boundaryService = boundaryService;
        this.boundaryTypeService = boundaryTypeService;
    }

    @ModelAttribute
    public void getBoundary(@PathVariable Long[] ids, Model model) {
        int paramLength = ids.length;
        Long boundaryId = null;

        if (paramLength > 2 && ids[2] != null) {
            boundaryId = ids[2];
            model.addAttribute("boundary", boundaryService.getBoundaryById(boundaryId));
        } else {
            if (model.asMap().get("boundary") == null) {
                model.addAttribute("boundary", new Boundary());
            }
        }

        Long boundaryTypeId = ids[1];
        BoundaryType boundaryType = boundaryTypeService.getBoundaryTypeById(boundaryTypeId);
        model.addAttribute("boundaryType", boundaryType);
        model.addAttribute("hierarchyType", boundaryType.getHierarchyType());
    }

    @RequestMapping(value = "/view-boundary/{ids}", method = RequestMethod.GET)
    public String viewBoundaryDetails() {
        return "boundary-view";
    }

    @RequestMapping(value = "/create-boundary/{ids}", method = RequestMethod.GET)
    public String showCreateBoundaryForm(Model model) {
        model.addAttribute("isUpdate", false);
        return "boundary-create";
    }

    @RequestMapping(value = "/update-boundary/{ids}", method = RequestMethod.GET)
    public String showUpdateBoundaryForm(Model model) {
        model.addAttribute("isUpdate", true);
        return "boundary-create";
    }
    
    @RequestMapping(value = "/list-boundaries/{ids}", method = RequestMethod.GET)
    public String showPaginationForm(Model model) {
        return "view-boundaries";
    }
    
    @RequestMapping(value = "/update-boundary/{ids}", method = RequestMethod.POST)
    public String UpdateBoundary(@Valid @ModelAttribute Boundary boundary, Model model,
            BindingResult errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return "boundary-create";
        }

        BoundaryType boundaryTypeObj = boundaryTypeService.getBoundaryTypeById(boundary.getBoundaryTypeId());

        boundary.setBoundaryType(boundaryTypeObj);
        boundary.setHistory(false);
        
        boundaryService.updateBoundary(boundary);
        
        redirectAttributes.addFlashAttribute("boundary", boundary);
        redirectAttributes.addFlashAttribute("message", "Boundary successfully updated !");
        
        String pathVars = boundaryTypeObj.getHierarchyType().getId() + "," + boundaryTypeObj.getId();

        return REDIRECT_URL_VIEW + pathVars;
    }
}
