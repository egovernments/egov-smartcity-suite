/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.web.controller.reports;

import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.pgr.entity.dto.RouterEscalationForm;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.egov.pgr.service.reports.RouterEscalationService;
import org.egov.pgr.web.controller.response.adaptor.RouterEscalationAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Collections;

import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping("routerescalation")
public class RouterEscalationReportController {

    private ComplaintTypeCategoryService complaintTypeCategoryService;
    private BoundaryService boundaryService;
    private RouterEscalationService routerEscalationService;

    @Autowired
    public RouterEscalationReportController(ComplaintTypeCategoryService complaintTypeCategoryService, BoundaryService boundaryService, RouterEscalationService routerEscalationService) {
        this.complaintTypeCategoryService = complaintTypeCategoryService;
        this.boundaryService = boundaryService;
        this.routerEscalationService = routerEscalationService;
    }

    @ModelAttribute
    public RouterEscalationForm routerEscalationForm() {
        return new RouterEscalationForm();
    }

    @RequestMapping(value = "/search-form", method = RequestMethod.GET)
    public String searchBaseRegister(Model model) {
        model.addAttribute("categories", complaintTypeCategoryService.findAll());
        model.addAttribute("complaintTypes", Collections.emptyList());
        model.addAttribute("wardList", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName( "Ward", "ADMINISTRATION"));
        return "routerescalation-report";
    }

    @RequestMapping(value = "/search-resultList", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String search(@ModelAttribute RouterEscalationForm routerEscalationForm) throws IOException {
        return new StringBuilder().append("{ \"data\":").append(toJSON(
                routerEscalationService.search(routerEscalationForm),
                RouterEscalationForm.class, RouterEscalationAdaptor.class)).append("}").toString();
    }
}
