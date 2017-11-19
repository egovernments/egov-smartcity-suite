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
import org.egov.infra.reporting.engine.ReportDisposition;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.contract.ComplaintRouterResponseAdaptor;
import org.egov.pgr.entity.contract.ComplaintRouterSearchRequest;
import org.egov.pgr.service.ComplaintRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.egov.infra.utils.StringUtils.appendTimestamp;
import static org.egov.infra.web.utils.WebUtils.reportAsResponseEntity;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping("/complaint/router/search")
public class SearchRoutingController {

    private static final String ADMINISTRATION = "ADMINISTRATION";
    private static final String ROUTER_SEARCH = "router-search";

    @Autowired
    private BoundaryTypeService boundaryTypeService;

    @Autowired
    private ComplaintRouterService complaintRouterService;

    @ModelAttribute
    public ComplaintRouter complaintRouter() {
        return new ComplaintRouter();
    }

    @ModelAttribute("boundaryTypes")
    public List<BoundaryType> boundaryTypes() {
        return boundaryTypeService.getBoundaryTypeByHierarchyTypeName(ADMINISTRATION);
    }

    @GetMapping("update")
    public String searchRouterUpdateForm() {
        return ROUTER_SEARCH;
    }

    @GetMapping("view")
    public String searchRouterViewForm(Model model) {
        model.addAttribute("mode", "view");
        return ROUTER_SEARCH;
    }

    @GetMapping(produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String search(ComplaintRouterSearchRequest routerSearchRequest) {
        return new DataTable<>(complaintRouterService.getComplaintRouter(routerSearchRequest),
                routerSearchRequest.draw()).toJson(ComplaintRouterResponseAdaptor.class);
    }

    @GetMapping("download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadRouterView(ComplaintRouterSearchRequest reportCriteria) {
        ReportOutput reportOutput = complaintRouterService.generateRouterReport(reportCriteria);
        reportOutput.setReportName(appendTimestamp("pgr_routerView"));
        reportOutput.setReportDisposition(ReportDisposition.ATTACHMENT);
        return reportAsResponseEntity(reportOutput);
    }
}
