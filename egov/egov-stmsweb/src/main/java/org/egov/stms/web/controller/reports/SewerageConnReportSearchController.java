/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *
 *       Copyright (C) 2016  eGovernments Foundation
 *
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.stms.web.controller.reports;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.stms.utils.constants.SewerageTaxConstants.BOUNDARYTYPE_LOCALITY;
import static org.egov.stms.utils.constants.SewerageTaxConstants.BOUNDARYTYPE_WARD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.HIERARCHYTYPE_LOCATION;
import static org.egov.stms.utils.constants.SewerageTaxConstants.HIERARCHYTYPE_REVENUE;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.stms.reports.entity.SewerageNoOfConnReportResult;
import org.egov.stms.service.es.SewerageIndexService;
import org.egov.stms.utils.SewerageConnectionHelperAdopter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/reports")
public class SewerageConnReportSearchController {

    private static final Logger LOG = Logger.getLogger(SewerageConnReportSearchController.class);
    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private SewerageIndexService sewerageIndexService;

    @ModelAttribute("ward")
    public List<Boundary> getBoundary() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BOUNDARYTYPE_WARD, HIERARCHYTYPE_REVENUE);
    }

    @ModelAttribute("localities")
    public List<Boundary> getLocalities() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BOUNDARYTYPE_LOCALITY,
                HIERARCHYTYPE_LOCATION);
    }

    @ModelAttribute()
    public void getReportHelper(final Model model) {
        final SewerageNoOfConnReportResult sewerageReportResult = new SewerageNoOfConnReportResult();
        model.addAttribute("sewerageReportResult", sewerageReportResult);
    }

    @RequestMapping(value = "/search-no-of-application", method = RequestMethod.GET)
    public String getNoOfConnections(final Model model) {
        model.addAttribute("currDate", new Date());
        return "sewerage-connection-report-search";
    }

    @RequestMapping(value = "/view-no-of-application", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public void viewSewerageConnections(@RequestParam("ward") final String ward, @RequestParam("block") final String block,
            @RequestParam("locality") final String locality, final HttpServletRequest request,
            final HttpServletResponse response) {
        String wardName = null;
        String result;
        if (ward != null) {
            final Boundary boundary = boundaryService.getBoundaryById(Long.valueOf(ward));
            wardName = boundary.getName();
        }

        final List<SewerageNoOfConnReportResult> reportList = sewerageIndexService.searchNoOfApplnQuery(wardName, block,
                locality);

        result = new StringBuilder("{ \"data\":").append(toJSON(reportList, SewerageNoOfConnReportResult.class,
                SewerageConnectionHelperAdopter.class)).append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            IOUtils.write(result, response.getWriter());
        } catch (final IOException e) {
            if (LOG.isDebugEnabled())
                LOG.error("IO Exception " + e);
        }
    }
}