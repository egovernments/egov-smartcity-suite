/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2020  eGovernments Foundation
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

package org.egov.ptis.web.controller.reports;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.model.reportregister.PropertyTaxRegisterBean;
import org.egov.ptis.domain.service.report.PropertyTaxRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/report")
public class PropertyTaxRegisterController {

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private PropertyTaxRegisterService propertyTaxRegisterService;

    @ModelAttribute("wards")
    public List<Boundary> wardBoundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(PropertyTaxConstants.WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("yearMonth")
    public List<String> getMonthAndYear() {
        List<String> monthAndYears = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH);
        String currentYearMonth = DateUtils.currentDateToGivenFormat("MMM-yyyy");
        YearMonth startDate = YearMonth.parse("Apr-2016", formatter);
        YearMonth endDate = YearMonth.parse(currentYearMonth, formatter);
        while (startDate.isBefore(endDate)) {
            monthAndYears.add(startDate.format(formatter).toString());
            startDate = startDate.plusMonths(1);
        }
        return monthAndYears;
    }

    @RequestMapping(value = "/taxregister-pt/form", method = RequestMethod.GET)
    public String buildingForm(final Model model) {
        model.addAttribute("currDate", new Date());
        model.addAttribute("mode", PropertyTaxConstants.CATEGORY_TYPE_PROPERTY_TAX);
        model.addAttribute("ptTaxRegister", new PropertyTaxRegisterBean());
        return "taxregister-pt-form";
    }

    @RequestMapping(value = "/taxregister-vlt/form", method = RequestMethod.GET)
    public String vacantLandForm(final Model model) {
        model.addAttribute("currDate", new Date());
        model.addAttribute("mode", PropertyTaxConstants.CATEGORY_TYPE_VACANTLAND_TAX);
        model.addAttribute("ptTaxRegister", new PropertyTaxRegisterBean());
        return "taxregister-pt-form";
    }

    @ResponseBody
    @RequestMapping(value = "/taxregister/result", method = RequestMethod.GET)
    public ResponseEntity<byte[]> generateNotice(final Model model, @RequestParam final Long wardId,
            @RequestParam final String yearMonth, @RequestParam final String mode) {
        ReportOutput reportOutput = propertyTaxRegisterService.generatePropertyTaxRegister(yearMonth, wardId, mode);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition",
                "inline;filename=PropertyTaxRegister_" + yearMonth.replace("-", "_") + ".pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }
}
