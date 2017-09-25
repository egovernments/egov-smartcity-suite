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
package org.egov.ptis.web.controller.reports;

import java.util.Date;
import java.util.List;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.ServiceTypeReportResponse;
import org.egov.ptis.domain.entity.property.WardWiseServiceTypeRequest;
import org.egov.ptis.domain.service.report.ServiceTypeReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/reports/wardWiseServiceTypeReport")
public class WardWiseServiceTypeReportController {
    @Autowired
    private ServiceTypeReportService reportService;

    @Autowired
    private BoundaryService boundaryService;

    @ModelAttribute
    public void getReportModel(final Model model) {
        model.addAttribute("wardWiseServiceType", new WardWiseServiceTypeRequest());
    }

    @ModelAttribute("propertyTypes")
    public List<String> propertyTypes() {
        return PropertyTaxConstants.PROPERTY_TYPE_SERVICE_CODES;
    }

    @ModelAttribute("wards")
    public List<Boundary> wardBoundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(PropertyTaxConstants.WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("serviceTypes")
    public List<String> serviceTypes() {
        return PropertyTaxConstants.SERVICE_TYPES;
    }

    @RequestMapping(value = "/countApplications", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchWardWiseService(final Model model) {
        model.addAttribute("currDate", new Date());
        return "wardwiseServiceReport-form";
    }

    @RequestMapping(value = "/countApplications", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceTypeReportResponse getWardWiseApplicationDetails(
            @RequestBody WardWiseServiceTypeRequest serviceRequest) {
        return reportService.getDetails(serviceRequest);
    }
}
