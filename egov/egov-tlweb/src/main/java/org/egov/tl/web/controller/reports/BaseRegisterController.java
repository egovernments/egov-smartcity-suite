/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tl.web.controller.reports;

import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportDisposition;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.tl.entity.dto.BaseRegisterRequest;
import org.egov.tl.entity.view.BaseRegister;
import org.egov.tl.service.BaseRegisterService;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.LicenseStatusService;
import org.egov.tl.web.response.adaptor.BaseRegisterResponseAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Collections;

import static org.egov.infra.utils.StringUtils.appendTimestamp;
import static org.egov.infra.web.utils.WebUtils.reportToResponseEntity;
import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.REVENUE_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.REVENUE_WARD;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping("/report/base-register")
public class BaseRegisterController {

    @Autowired
    private LicenseStatusService licenseStatusService;

    @Autowired
    private BaseRegisterService baseRegisterService;

    @Autowired
    private LicenseCategoryService licenseCategoryService;

    @Autowired
    private BoundaryService boundaryService;

    @GetMapping("search")
    public String baseRegisterSearchForm(Model model) {
        model.addAttribute("baseRegister", new BaseRegister());
        model.addAttribute("categories", licenseCategoryService.getCategoriesOrderByName());
        model.addAttribute("subcategories", Collections.emptyList());
        model.addAttribute("statusList", licenseStatusService.findAll());
        model.addAttribute("filters", Arrays.asList("All", "Defaulters"));
        boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE);
        model.addAttribute("wardList", boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(REVENUE_WARD,
                REVENUE_HIERARCHY_TYPE));
        return "baseregister-report";
    }

    @PostMapping(value = "search", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchBaseRegister(BaseRegisterRequest baseRegisterRequest) {
        return new DataTable<>(baseRegisterService.pagedBaseRegisterRecords(baseRegisterRequest),
                baseRegisterRequest.draw())
                .toJson(BaseRegisterResponseAdaptor.class);
    }

    @GetMapping("grand-total")
    @ResponseBody
    public Object[] baseRegisterGrandTotal(BaseRegisterRequest baseRegisterRequest) {
        return baseRegisterService.baseRegisterGrandTotal(baseRegisterRequest);
    }

    @GetMapping("download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadReport(BaseRegisterRequest baseRegisterRequest) {
        ReportOutput reportOutput = baseRegisterService.generateBaseRegisterReport(baseRegisterRequest);
        reportOutput.setReportName(appendTimestamp("base_register_report"));
        reportOutput.setReportDisposition(ReportDisposition.ATTACHMENT);
        return reportToResponseEntity(reportOutput);
    }
}