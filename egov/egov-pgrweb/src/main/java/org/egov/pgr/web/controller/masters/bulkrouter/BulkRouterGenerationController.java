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
package org.egov.pgr.web.controller.masters.bulkrouter;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.ComplaintRouterAdaptor;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.service.ComplaintRouterService;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/bulkRouter")
public class BulkRouterGenerationController {

    private final ComplaintRouterService complaintRouterService;
    private final BoundaryTypeService boundaryTypeService;
    private final ComplaintTypeCategoryService complaintTypeCategoryService;

    @Autowired
    public BulkRouterGenerationController(final ComplaintRouterService complaintRouterService,
            final BoundaryTypeService boundaryTypeService, final ComplaintTypeCategoryService complaintTypeCategoryService) {
        this.complaintRouterService = complaintRouterService;
        this.boundaryTypeService = boundaryTypeService;
        this.complaintTypeCategoryService = complaintTypeCategoryService;
    }

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

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String newform() {
        return "bulkRouter-new";
    }

    @RequestMapping(value = "/search-result", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void search(final Model model, final HttpServletRequest request,
            @ModelAttribute final BulkRouterGenerator bulkRouterGenerator, final HttpServletResponse response)
            throws IOException {
        final List<ComplaintRouter> pageOfRouters = complaintRouterService
                .getRoutersByComplaintTypeBoundary(bulkRouterGenerator.getComplaintTypes(), bulkRouterGenerator.getBoundaries());
        final String complaintRouterJSONData = new StringBuilder("{ \"data\":").append(toJSON(pageOfRouters, ComplaintRouter.class, ComplaintRouterAdaptor.class)).append("}")
                .toString();
        IOUtils.write(complaintRouterJSONData, response.getWriter());
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute final BulkRouterGenerator bulkRouterGenerator, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("message", "router.unble.to.save");
            return "bulkRouter-new";
        } else {
            for (final ComplaintType complaintType : bulkRouterGenerator.getComplaintTypes())
                for (final Boundary boundary : bulkRouterGenerator.getBoundaries()) {
                    final ComplaintRouter router = new ComplaintRouter();
                    router.setComplaintType(complaintType);
                    router.setBoundary(boundary);
                    router.setPosition(bulkRouterGenerator.getPosition());
                    final ComplaintRouter existingRouter = complaintRouterService.getExistingRouter(router);
                    if (existingRouter != null) {
                        existingRouter.setPosition(bulkRouterGenerator.getPosition());
                        complaintRouterService.updateComplaintRouter(existingRouter);
                    } else
                        complaintRouterService.createComplaintRouter(router);
                }
            redirectAttrs.addFlashAttribute("message", "msg.bulkrouter.success");
            return "redirect:/bulkRouter/search";
        }

    }

}
