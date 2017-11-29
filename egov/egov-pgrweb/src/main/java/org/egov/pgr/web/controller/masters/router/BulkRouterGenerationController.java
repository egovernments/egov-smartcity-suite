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
package org.egov.pgr.web.controller.masters.router;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.entity.contract.BulkRouterGenerator;
import org.egov.pgr.entity.contract.ComplaintRouterAdaptor;
import org.egov.pgr.service.ComplaintRouterService;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping("/complaint/bulkrouter")
public class BulkRouterGenerationController {

    @Autowired
    private ComplaintRouterService complaintRouterService;

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private ComplaintTypeCategoryService complaintTypeCategoryService;

    @ModelAttribute("boundaryTypes")
    public List<BoundaryType> boundaryTypes() {
        return boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION");
    }

    @ModelAttribute("categories")
    public List<ComplaintTypeCategory> categories() {
        return complaintTypeCategoryService.findAll();
    }

    @ModelAttribute
    public BulkRouterGenerator bulkRouterGenerator() {
        return new BulkRouterGenerator();
    }

    @GetMapping
    public String complaintBulkRouterForm() {
        return "bulkrouter";
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String search(BulkRouterGenerator bulkRouterGenerator) {
        return new StringBuilder("{ \"data\":").append(toJSON(complaintRouterService.getRoutersByComplaintTypeBoundary(
                bulkRouterGenerator.getComplaintTypes(),
                bulkRouterGenerator.getBoundaries()),
                ComplaintRouter.class, ComplaintRouterAdaptor.class)).append("}").toString();
    }

    @PostMapping("create")
    public String createComplaintBulkRouter(@Valid BulkRouterGenerator bulkRouterGenerator,
                                            RedirectAttributes redirectAttrs,
                                            BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("message", "router.unble.to.save");
            return "bulkrouter";
        } else {
            complaintRouterService.createBulkRouter(bulkRouterGenerator);
            redirectAttrs.addFlashAttribute("message", "msg.bulkrouter.success");
            return "redirect:/complaint/bulkrouter";
        }
    }
}
