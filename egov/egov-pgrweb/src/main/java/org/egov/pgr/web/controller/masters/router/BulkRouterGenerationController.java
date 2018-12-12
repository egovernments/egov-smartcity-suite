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
import org.egov.infra.web.support.ui.DataTable;
import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.entity.contract.BulkRouterRequest;
import org.egov.pgr.service.ComplaintRouterService;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.egov.pgr.web.contracts.response.RouterResponseAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import static org.egov.infra.utils.ApplicationConstant.ADMIN_HIERARCHY_TYPE;
import static org.egov.infra.web.utils.WebUtils.bindErrorToMap;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

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
        return boundaryTypeService.getBoundaryTypeByHierarchyTypeName(ADMIN_HIERARCHY_TYPE);
    }

    @ModelAttribute("categories")
    public List<ComplaintTypeCategory> categories() {
        return complaintTypeCategoryService.findAll();
    }

    @ModelAttribute
    public BulkRouterRequest bulkRouterRequest() {
        return new BulkRouterRequest();
    }

    @GetMapping
    public String complaintBulkRouterForm() {
        return "bulkrouter";
    }

    @PostMapping(value = "/", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity search(@Valid @ModelAttribute BulkRouterRequest bulkRouterRequest, BindingResult binder) {
        return binder.hasErrors() ?
                ResponseEntity.badRequest().body(bindErrorToMap(binder)) :
                ResponseEntity.ok(new DataTable(complaintRouterService.getRoutersByComplaintTypeBoundary(bulkRouterRequest),
                        bulkRouterRequest.draw()).toJson(RouterResponseAdaptor.class));
    }

    @PostMapping("create")
    public String createComplaintBulkRouter(@Valid BulkRouterRequest bulkRouterRequest, RedirectAttributes redirectAttrs,
                                            BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("message", "router.unable.to.save");
            return "bulkrouter";
        }
        complaintRouterService.createBulkRouter(bulkRouterRequest);
        redirectAttrs.addFlashAttribute("message", "msg.bulkrouter.success");
        return "redirect:/complaint/bulkrouter";
    }
}
