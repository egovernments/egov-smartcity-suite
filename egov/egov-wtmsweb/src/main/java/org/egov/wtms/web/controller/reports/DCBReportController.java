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

package org.egov.wtms.web.controller.reports;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.wtms.application.service.CurrentDcbService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.reports.entity.DCBReportHelperAdaptor;
import org.egov.wtms.reports.entity.DCBReportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.ptis.constants.PropertyTaxConstants.BLOCK;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Controller
@RequestMapping(value = "/reports")
public class DCBReportController {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private CurrentDcbService currentDcbService;

    @Autowired(required = true)
    protected WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private BoundaryService boundaryService;

    @ModelAttribute("dCBReportResult")
    public DCBReportResult dCBReportResultModel() {
        return new DCBReportResult();
    }

    @ModelAttribute("connectionTypes")
    public Map<String, String> connectionTypes() {
        return waterConnectionDetailsService.getConnectionTypesMap();
    }

    @ModelAttribute("zones")
    public List<Boundary> zones() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("wards")
    public List<Boundary> wards() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("blocks")
    public List<Boundary> blocks() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BLOCK,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("localitys")
    public List<Boundary> localitys() {
        return boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE);
    }

    @RequestMapping(value = "/dCBReport/zoneWise", method = RequestMethod.GET)
    public String zoneWisesearch(final Model model) {
        model.addAttribute("mode", "zone");
        model.addAttribute("reportType", "zoneWise");
        return "dCBReport-search";
    }

    @RequestMapping(value = "/dCBReport/wardWise", method = RequestMethod.GET)
    public String wardWisesearch(final Model model) {
        model.addAttribute("mode", "ward");
        model.addAttribute("reportType", "wardWise");
        return "dCBReport-search";
    }

    @RequestMapping(value = "/dCBReport/blockWise", method = RequestMethod.GET)
    public String blockWisesearch(final Model model) {
        model.addAttribute("mode", "block");
        model.addAttribute("reportType", "blockWise");
        return "dCBReport-search";
    }

    @RequestMapping(value = "/dCBReport/localityWise", method = RequestMethod.GET)
    public String localityWisesearch(final Model model) {
        model.addAttribute("mode", "locality");
        model.addAttribute("reportType", "localityWise");
        return "dCBReport-search";
    }

    @RequestMapping(value = "/dCBReportList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void search(final HttpServletRequest request, final HttpServletResponse response,
            final Model model) throws IOException {
        String connectionType = "";
        String mode = "";
        String reportType = "";
        String[] boundaryId = null;
        final StringBuilder zones = new StringBuilder();
        if (isNotBlank(request.getParameter("boundaryId[]")))
            boundaryId = request.getParameterValues("boundaryId[]");
        else if (isNotBlank(request.getParameter("boundaryId")))
            boundaryId = request.getParameterValues("boundaryId");
        if (boundaryId != null)
            for (final String n : boundaryId) {
                if (zones.length() > 0)
                    zones.append(',');
                zones.append(n);
            }
        if (isNotBlank(request.getParameter("connectionType")))
            connectionType = request.getParameter("connectionType");
        if (isNotBlank(request.getParameter("mode")))
            mode = request.getParameter("mode");
        if (isNotBlank(request.getParameter("reportType")))
            reportType = request.getParameter("reportType");
        String result;
        result = new StringBuilder("{ \"data\":").append(toJSON(currentDcbService.getReportResult(zones.toString(), connectionType, mode, reportType), DCBReportResult.class, DCBReportHelperAdaptor.class))
                .append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
    }

}
