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

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.ComplaintRouterAdaptor;
import org.egov.pgr.service.ComplaintRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/router")
public class SearchRoutingController {
    public static final String CONTENTTYPE_JSON = "application/json";

    protected BoundaryTypeService boundaryTypeService;

    protected ComplaintRouterService complaintRouterService;

    @Autowired
    public SearchRoutingController(final BoundaryTypeService boundaryTypeService,
            final ComplaintRouterService complaintRouterService) {
        this.boundaryTypeService = boundaryTypeService;
        this.complaintRouterService = complaintRouterService;
    }

    @ModelAttribute
    public ComplaintRouter complaintRouter() {
        return new ComplaintRouter();
    }

    @ModelAttribute("boundaryTypes")
    public List<BoundaryType> boundaryTypes() {
        return boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION");
    }

    @RequestMapping(value = "/search-update", method = GET)
    public String searchRouterUpdateForm(final Model model) {
        model.addAttribute("boundaryTypes", boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION"));
        return "router-searchUpdate";
    }

    @RequestMapping(value = "/search-view", method = GET)
    public String searchRouterViewForm(final Model model) {
        model.addAttribute("boundaryTypes", boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION"));
        return "router-searchView";
    }

    @RequestMapping(value = "/resultList-update", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTablesUpdate(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {

        final Long boundaryTypeId = Long.valueOf(request.getParameter("boundaryTypeId"));
        final Long complaintTypeId = Long.valueOf(request.getParameter("complaintTypeId"));
        final Long boundaryId = Long.valueOf(request.getParameter("boundaryId"));

        final String complaintRouterJSONData = commonSearchResult(boundaryTypeId, complaintTypeId, boundaryId);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(complaintRouterJSONData, response.getWriter());
    }

    @RequestMapping(value = "/resultList-view", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTablesView(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {

        final Long boundaryTypeId = Long.valueOf(request.getParameter("boundaryTypeId"));
        final Long complaintTypeId = Long.valueOf(request.getParameter("complaintTypeId"));
        final Long boundaryId = Long.valueOf(request.getParameter("boundaryId"));

        final String complaintRouterJSONData = commonSearchResult(boundaryTypeId, complaintTypeId, boundaryId);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(complaintRouterJSONData, response.getWriter());
    }

    public String commonSearchResult(final Long boundaryTypeId, final Long complaintTypeId, final Long boundaryId) {
        final List<ComplaintRouter> pageOfRouters = complaintRouterService.getPageOfRouters(boundaryTypeId,
                complaintTypeId, boundaryId);
        return new StringBuilder("{ \"data\":").append(toJSON(pageOfRouters, ComplaintRouter.class, ComplaintRouterAdaptor.class)).append("}").toString();
    }
}
