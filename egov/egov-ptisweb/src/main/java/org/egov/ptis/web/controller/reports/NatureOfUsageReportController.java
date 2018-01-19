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

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.ptis.constants.PropertyTaxConstants.BLOCK;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.service.report.ReportService;
import org.egov.ptis.master.service.PropertyUsageService;
import org.egov.ptis.report.bean.NatureOfUsageResult;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author subhash
 *
 */
@Controller
@RequestMapping(value = "/reports")
public class NatureOfUsageReportController {

    private static final String NATURE_OF_USAGE_REPORT_FORM = "natureOfUsageReport-form";

    @PersistenceContext
    EntityManager entityManager;


    @Autowired
    private PropertyUsageService propertyUsageService;

    @Autowired
    private BoundaryService boundaryService;
    
    @Autowired
    private ReportService reportService;

    @ModelAttribute("natureOfUsages")
    public List<PropertyUsage> getNatureOfUsages() {
        return propertyUsageService.getAllActivePropertyUsages();
    }

    @ModelAttribute("wards")
    public List<Boundary> getWards() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("blocks")
    public List<Boundary> getBlocks() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BLOCK, PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @RequestMapping(value = "/natureOfUsageReport-form", method = RequestMethod.GET)
    public String searchForm(final Model model) {
        model.addAttribute("natureOfUsageResult", new NatureOfUsageResult());
        return NATURE_OF_USAGE_REPORT_FORM;
    }

    @RequestMapping(value = "/natureOfUsageReportList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void search(final HttpServletRequest request, final HttpServletResponse response, final Model model)
            throws IOException {
        final List<NatureOfUsageResult> natureOfUsageResultList = reportService.getNatureOfUsageReportList(request);
        final StringBuilder natureOfUsageSONData = new StringBuilder("{ \"data\":").append(toJSON(natureOfUsageResultList, NatureOfUsageResult.class, NatureOfUsageReportAdaptor.class))
                .append("}");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(natureOfUsageSONData.toString(), response.getWriter());
    }

    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }

}
