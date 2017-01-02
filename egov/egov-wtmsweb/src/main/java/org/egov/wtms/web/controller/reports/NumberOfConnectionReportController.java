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
package org.egov.wtms.web.controller.reports;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.wtms.application.entity.WaterChargeMaterlizeView;
import org.egov.wtms.application.service.ArrearRegisterReportService;
import org.egov.wtms.masters.service.BoundaryWiseReportService;
import org.egov.wtms.reports.entity.WaterConnectionHelperAdaptor;
import org.egov.wtms.reports.entity.WaterConnectionReportResult;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;

@Controller
@RequestMapping(value = "/reports")
public class NumberOfConnectionReportController {

    @Autowired
    private final BoundaryWiseReportService boundaryWiseReportService;
    
    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ArrearRegisterReportService arrearRegisterReportService;

    @Autowired
    public NumberOfConnectionReportController(final BoundaryWiseReportService drillDownReportService) {
        boundaryWiseReportService = drillDownReportService;
    }

    @ModelAttribute("wards")
    public List<Boundary> wards() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute
    public void getReportHelper(final Model model) {
        final WaterConnectionReportResult reportHealperObj = new WaterConnectionReportResult();
        model.addAttribute("reportHelper", reportHealperObj);

    }

    @ModelAttribute("localitys")
    public List<Boundary> localitys() {
        return boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/coonectionReport/wardWise")
    public String searchNoOfConnectionByBoundaryForm(final Model model) {
        model.addAttribute("currDate", new Date());
        return "connectionReport-search";
    }

    @ExceptionHandler(Exception.class)
    @RequestMapping(value = "/connectionReportList", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTablesUpdate(@RequestParam final String ward,
            @RequestParam final String block, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        final List<WaterChargeMaterlizeView> propertyViewList = arrearRegisterReportService.prepareQueryforArrearRegisterReport(0l,
               0l);
        SQLQuery   drillDownreportQuery = boundaryWiseReportService.getDrillDownReportQuery(ward, block);
        drillDownreportQuery.setResultTransformer(Transformers.aliasToBean(WaterConnectionReportResult.class));
        final List<WaterConnectionReportResult> drillDownresult = drillDownreportQuery.list();
        String  result = new StringBuilder("{ \"data\":").append(toJSON(drillDownresult, WaterConnectionReportResult.class,
                WaterConnectionHelperAdaptor.class)).append("}").toString();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());

    }
}
