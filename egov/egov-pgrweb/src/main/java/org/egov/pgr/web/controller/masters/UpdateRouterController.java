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

package org.egov.pgr.web.controller.masters;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.service.ComplaintRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/router")
class UpdateRouterController {

    private final BoundaryTypeService boundaryTypeService;
    private final ComplaintRouterService complaintRouterService;

    @Autowired
    public UpdateRouterController(final BoundaryTypeService boundaryTypeService,
                                  final ComplaintRouterService complaintRouterService) {
        this.boundaryTypeService = boundaryTypeService;
        this.complaintRouterService = complaintRouterService;
    }

    @ModelAttribute("boundaryTypes")
    public List<BoundaryType> boundaryTypes() {
        return boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION");
    }

    @ModelAttribute
    public ComplaintRouter complaintRouter(@PathVariable final Long id) {
        return complaintRouterService.getRouterById(id);
    }

    @RequestMapping(value = "/update/{id}", method = GET)
    public String updateRouterForm() {
        return "router-update";
    }

    @RequestMapping(value = "/search/update/{id}", method = GET)
    public String updatefromSearchRouterForm() {
        return "router-updateSearch";
    }

    @RequestMapping(value = "/view/{id}", method = GET)
    public String viewRouterForm() {
        return "router-view";
    }

    @RequestMapping(value = "/update/{id}", method = POST)
    public String update(@Valid @ModelAttribute final ComplaintRouter complaintRouter, final BindingResult errors,
                         final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors())
            return "router-update";

        complaintRouterService.updateComplaintRouter(complaintRouter);
        redirectAttrs.addFlashAttribute("complaintRouter", complaintRouter);
        model.addAttribute("routerHeading", "msg.router.update.heading");
        model.addAttribute("message", "msg.router.update.success");
        return "router-success";
    }

    @RequestMapping(value = "/search/update/{id}", method = POST)
    public String searchupdate(@Valid @ModelAttribute final ComplaintRouter complaintRouter,
                               final BindingResult errors, final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors())
            return "router-updateSearch";

        complaintRouterService.updateComplaintRouter(complaintRouter);
        redirectAttrs.addFlashAttribute("complaintRouter", complaintRouter);
        model.addAttribute("routerHeading", "msg.router.update.heading");
        model.addAttribute("message", "msg.router.update.success");
        return "router-updateSuccess";
    }

    @RequestMapping(value = "/delete/{id}", method = POST)
    public String delete(@Valid @ModelAttribute final ComplaintRouter complaintRouter, final BindingResult errors,
                         final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors())
            return "router-update";
        if (!validateIfCityLevelRouter(complaintRouter)) {
            complaintRouterService.deleteComplaintRouter(complaintRouter);
            redirectAttrs.addFlashAttribute("complaintRouter", complaintRouter);
            model.addAttribute("routerHeading", "msg.router.del.heading");
            model.addAttribute("message", "msg.router.del.success");
            return "router-deleteMsg";
        } else {
            model.addAttribute("message", "msg.router.cannot.delete");
            return "router-update";
        }
    }

    private boolean validateIfCityLevelRouter(final ComplaintRouter complaintRouter) {
        return complaintRouter.getBoundary() != null && "City".equalsIgnoreCase(complaintRouter.getBoundary().getBoundaryType().getName())
                && complaintRouter.getComplaintType() == null;
    }

}
