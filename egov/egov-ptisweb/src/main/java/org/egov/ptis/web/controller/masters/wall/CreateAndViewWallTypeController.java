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
package org.egov.ptis.web.controller.masters.wall;

import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.master.service.WallTypeService;
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
 * Controller for Wall Type Master
 */

@Controller
@RequestMapping(value = "/walltype")
public class CreateAndViewWallTypeController {

    private final WallTypeService wallTypeService;
    
    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    public CreateAndViewWallTypeController(final WallTypeService wallTypeService) {
        this.wallTypeService = wallTypeService;
    }

    @ModelAttribute
    public WallType wallTypeModel() {
        return new WallType();
    }

    @ModelAttribute(value = "wallTypes")
    public List<WallType> listWallTypes() {
        return wallTypeService.getAllWalls();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showWallTypes(final Model model) {
        String roleName = wallTypeService.getRolesForUserId(securityUtils.getCurrentUser().getId());
        model.addAttribute("roleName",roleName);
        return "wallType-main";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create() {
        return "wallType-form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute final WallType wallType, final BindingResult errors,
            final HttpServletRequest request, final RedirectAttributes redirectAttributes) {

        if (errors.hasErrors())
            return "wallType-main";
        wallTypeService.create(wallType);

        redirectAttributes.addFlashAttribute("message", "msg.walltype.create.success");
        return "redirect:/walltype/create";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable Long id, Model model) {
        WallType wallType = wallTypeService.getWallTypeById(id);

        model.addAttribute("wallType", wallType);
        return "wallType-view";
    }
}
