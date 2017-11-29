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

package org.egov.ptis.web.controller.reports;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.transactions.AssessmentTransactions;
import org.egov.ptis.domain.service.report.DemandRegisterService;
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

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/report")
public class DemandRegisterController {

    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    public PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private DemandRegisterService transactionsService;

    @ModelAttribute("wards")
    public List<Boundary> wardBoundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(PropertyTaxConstants.WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("financialYears")
    public List<CFinancialYear> getFinancialYears() {

        return financialYearDAO.getAllPriorFinancialYears(new Date());
    }

    @RequestMapping(value = "/arrdmdrgstr-vlt/form", method = RequestMethod.GET)
    public String searchVLTArrearDemandRegisterForm(final Model model) {
        model.addAttribute("currDate", new Date());
        model.addAttribute("mode", PropertyTaxConstants.CATEGORY_TYPE_VACANTLAND_TAX);
        model.addAttribute("ADRReport", new AssessmentTransactions());
        return "arrdmdrgstr-vlt-form";
    }

    @RequestMapping(value = "/arrdmdrgstr-pt/form", method = RequestMethod.GET)
    public String searchPTArrearDemandRegisterForm(final Model model) {
        model.addAttribute("currDate", new Date());
        model.addAttribute("mode", PropertyTaxConstants.CATEGORY_TYPE_PROPERTY_TAX);
        model.addAttribute("ADRReport", new AssessmentTransactions());
        return "arrdmdrgstr-pt-form";
    }

    @ResponseBody
    @RequestMapping(value = "/arrdmdrgstr/result", method = RequestMethod.GET)
    public ResponseEntity<byte[]> generateNotice(final Model model, @RequestParam final Long wardId,
            @RequestParam final Long financialYearId, @RequestParam final String mode) {
        final CFinancialYear financialYear = financialYearDAO.getFinancialYearById(financialYearId);
        ReportOutput reportOutput = transactionsService.generateDemandRegisterReport(PropertyTaxConstants.ADR_REPORT,
                wardId, financialYear, mode);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition",
                "inline;filename=ArrearDemandRegister_" + financialYear.getFinYearRange() + ".pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

}
