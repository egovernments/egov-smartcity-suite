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
package org.egov.works.web.controller.lineestimate;

import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateSearchRequest;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/lineestimate")
public class CancelLineEstimateController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private WorksUtils worksUtils;

    @RequestMapping(value = "/cancel/search", method = RequestMethod.GET)
    public String showSearchLetterOfAcceptanceForm(
            @ModelAttribute final LineEstimateSearchRequest lineEstimateSearchRequest, final Model model)
            throws ApplicationException {
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("lineEstimateSearchRequest", lineEstimateSearchRequest);
        lineEstimateSearchRequest.setExecutingDepartment(worksUtils.getDefaultDepartmentId());

        return "searchlineestimate-cancel";
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancelLineEstimate(final HttpServletRequest request, final Model model) throws ApplicationException {
        final Long lineEstimateId = Long.parseLong(request.getParameter("id"));
        final String cancellationReason = request.getParameter("cancellationReason");
        final String cancellationRemarks = request.getParameter("cancellationRemarks");
        LineEstimate lineEstimate = lineEstimateService.getLineEstimateById(lineEstimateId);
        final String loaNumbers = lineEstimateService.checkIfLOAsCreated(lineEstimate.getId());
        if (!loaNumbers.equals("")) {
            final String message = messageSource.getMessage("error.lineestimate.loa.created",
                    new String[] { loaNumbers }, null);
            model.addAttribute("errorMessage", message);
            return "letterofacceptance-success";
        }

        lineEstimate.setCancellationReason(cancellationReason);
        lineEstimate.setCancellationRemarks(cancellationRemarks);
        try {
            lineEstimate = lineEstimateService.cancel(lineEstimate);
        } catch (final ValidationException v) {
            model.addAttribute("errorMessage", v.getErrors().get(0).getMessage());
        }
        model.addAttribute("lineEstimate", lineEstimate);
        model.addAttribute("mode", "cancel");
        return "lineestimate-success";
    }

}
