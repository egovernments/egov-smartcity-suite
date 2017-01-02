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

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.ptis.actions.reports.DefaultersReportHelperAdaptor;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.DefaultersInfo;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.service.report.ReportService;
import org.hibernate.Query;
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
@RequestMapping(value = "/report")
public class DefaultersReportController {

    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    public PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private ReportService reportService;

    @ModelAttribute("wards")
    public List<Boundary> wardBoundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(PropertyTaxConstants.WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("limits")
    public List<Integer> limitList() {
        List<Integer> limitList = new ArrayList<>();
        limitList.add(10);
        limitList.add(50);
        limitList.add(100);
        limitList.add(500);
        limitList.add(1000);
        return limitList;
    }

    @ModelAttribute("categories")
    public Map<String, String> ownershipCategories() {
        return PropertyTaxConstants.OWNERSHIP_OF_PROPERTY_FOR_DEFAULTERS_REPORT;
    }

    @RequestMapping(value = "/defaultersReportVLT", method = RequestMethod.GET)
    public String searchVLTDefaultersForm(final Model model) {
        model.addAttribute("currDate", new Date());
        model.addAttribute("mode", PropertyTaxConstants.CATEGORY_TYPE_VACANTLAND_TAX);
        model.addAttribute("DefaultersReport", new DefaultersInfo());
        return "defaultersVLT-form";
    }

    @RequestMapping(value = "/defaultersReportPT", method = RequestMethod.GET)
    public String searchPTDefaultersForm(final Model model) {
        model.addAttribute("currDate", new Date());
        model.addAttribute("mode", PropertyTaxConstants.CATEGORY_TYPE_PROPERTY_TAX);
        model.addAttribute("DefaultersReport", new DefaultersInfo());
        return "defaultersPT-form";
    }

    @RequestMapping(value = "/defaultersReport/result", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String defaultersReportSearchResult(@RequestParam final String wardId,
            @RequestParam final String fromAmount,
            @RequestParam final String toAmount, @RequestParam final String limit, @RequestParam final String category,
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        Query query = propertyTaxUtil.prepareQueryforDefaultersReport(Long.valueOf(wardId), fromAmount, toAmount,
                StringUtils.isBlank(limit) ? null : Integer.valueOf(limit), category);
        List<PropertyMaterlizeView> properties = query.list();
        List<DefaultersInfo> defaultersList = reportService.getDefaultersInformation(properties);
        return new StringBuilder("{ \"data\":").append(toJSON(defaultersList, DefaultersInfo.class,
                DefaultersReportHelperAdaptor.class)).append("}").toString();
    }
}
