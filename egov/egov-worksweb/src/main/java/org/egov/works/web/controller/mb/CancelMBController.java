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
package org.egov.works.web.controller.mb;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.contractorbill.entity.ContractorBillRegister.BillStatus;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.entity.SearchRequestCancelMB;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/measurementbook")
public class CancelMBController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private MBHeaderService mbHeaderService;
    
    @Autowired
    private WorksUtils worksUtils;

    @RequestMapping(value = "/cancel/search", method = RequestMethod.GET)
    public String showSearchMBForm(
            @ModelAttribute final SearchRequestCancelMB searchRequestCancelMB,
            final Model model) throws ApplicationException {
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("searchRequestCancelMB", searchRequestCancelMB);
        model.addAttribute("defaultDepartmentId", worksUtils.getDefaultDepartmentId());
        return "searchmb-cancel";
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancelMB(final HttpServletRequest request,
            final Model model) throws ApplicationException {
        final Long mbId = Long.parseLong(request.getParameter("id"));
        final String cancellationReason = request.getParameter("cancellationReason");
        final String cancellationRemarks = request.getParameter("cancellationRemarks");
        MBHeader mbHeader = mbHeaderService.getMBHeaderById(mbId);
        String message = "";

        if (mbHeader.getEgBillregister() != null
                && !mbHeader.getEgBillregister().getStatus().getCode().equalsIgnoreCase(BillStatus.CANCELLED.toString())) {
            message = messageSource.getMessage("error.mb.bill.created",
                    new String[] { mbHeader.getEgBillregister().getBillnumber() },
                    null);
            model.addAttribute("errorMessage", message);
            return "mb-success";
        }

        final MBHeader latestMBHeader = mbHeaderService.getLatestMBHeader(mbHeader.getWorkOrderEstimate().getId());
        if (latestMBHeader != null && !mbHeader.getId().equals(latestMBHeader.getId())) {
            message = messageSource.getMessage("error.mb.not.latest",
                    new String[] { latestMBHeader.getMbRefNo(), latestMBHeader.getMbRefNo() }, null);
            model.addAttribute("errorMessage", message);
            return "mb-success";
        }

        message = messageSource.getMessage("msg.mbheader.cancelled", new String[] { mbHeader.getMbRefNo() }, null);
        mbHeader.setCancellationReason(cancellationReason);
        mbHeader.setCancellationRemarks(cancellationRemarks);
        mbHeader = mbHeaderService.cancel(mbHeader);
        model.addAttribute("mbHeader", mbHeader);
        model.addAttribute("message", message);
        return "mb-success";
    }

}
