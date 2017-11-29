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
package org.egov.ptis.web.controller.masters.floor;

import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.master.service.FloorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller for Floor Type Master
 */

@Controller
@RequestMapping(value = "/floortype")
public class CreateAndViewFloorTypeController {

    private final FloorTypeService floorTypeService;
    
    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    public CreateAndViewFloorTypeController(final FloorTypeService floorTypeService) {
        this.floorTypeService = floorTypeService;
    }

    @ModelAttribute
    public FloorType FloorTypeModel() {
        return new FloorType();
    }

    @ModelAttribute(value = "floortypes")
    public List<FloorType> listFloorTypes() {
        return floorTypeService.getAllFloors();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showFloorTypes(final Model model) {
        String roleName = floorTypeService.getRolesForUserId(securityUtils.getCurrentUser().getId());
        model.addAttribute("roleName",roleName);
        return "floorType-main";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(final Model model) {
        model.addAttribute("floortype", FloorTypeModel());
        return "floorType-form";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable Long id, Model model) {

        FloorType floorType = floorTypeService.getFloorTypeById(id);
        model.addAttribute("floorType", floorType);
        return "floorType-view";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute final FloorType floorType, final BindingResult errors,
            final RedirectAttributes redirectAttributes, final HttpServletRequest request, final Model model) {

        if (errors.hasErrors())
            return "floorType-main";
        floorTypeService.create(floorType);
        redirectAttributes.addFlashAttribute("message", "msg.floortype.create.success");
        return "redirect:/floortype/create";

    }

}
