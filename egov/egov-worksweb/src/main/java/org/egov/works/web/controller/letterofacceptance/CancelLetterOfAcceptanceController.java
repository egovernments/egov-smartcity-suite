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
package org.egov.works.web.controller.letterofacceptance;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.revisionestimate.service.RevisionWorkOrderService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/letterofacceptance")
public class CancelLetterOfAcceptanceController {

    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String LETTEROFACCEPTANCE_SUCCESS = "letterofacceptance-success";

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private RevisionWorkOrderService revisionWorkOrderService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WorksUtils worksUtils;

    @RequestMapping(value = "/cancel/search", method = RequestMethod.GET)
    public String showSearchLetterOfAcceptanceForm(
            @ModelAttribute final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final Model model)
            throws ApplicationException {
        model.addAttribute("departments", departmentService.getAllDepartments());
        final List<Department> departments = worksUtils.getUserDepartments(securityUtils.getCurrentUser());
        if (departments != null && !departments.isEmpty())
            searchRequestLetterOfAcceptance.setDepartmentName(departments.get(0).getId());
        final List<EgwStatus> egwStatuses = egwStatusHibernateDAO.getStatusByModule(WorksConstants.WORKORDER);
        final List<EgwStatus> newEgwStatuses = new ArrayList<EgwStatus>();
        for (final EgwStatus egwStatus : egwStatuses)
            if (!egwStatus.getCode().equalsIgnoreCase(WorksConstants.CREATED_STATUS)
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.REJECTED)
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.CANCELLED)
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.RESUBMITTED_STATUS))
                newEgwStatuses.add(egwStatus);
        model.addAttribute("egwStatus", newEgwStatuses);
        model.addAttribute("searchRequestContractorBill", searchRequestLetterOfAcceptance);
        searchRequestLetterOfAcceptance.setDepartmentName(worksUtils.getDefaultDepartmentId());

        return "searchloa-cancel";
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancelLetterOfAcceptance(final HttpServletRequest request, final Model model)
            throws ApplicationException {
        final Long letterOfAcceptanceId = Long.parseLong(request.getParameter("id"));
        final String cancellationReason = request.getParameter("cancellationReason");
        final String cancellationRemarks = request.getParameter("cancellationRemarks");
        WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderById(letterOfAcceptanceId);
        final WorkOrderEstimate workOrderEstimate = workOrder.getWorkOrderEstimates().get(0);
        if (workOrderEstimate.getWorkOrderActivities().isEmpty()) {
            final String billNumbers = letterOfAcceptanceService.checkIfBillsCreated(workOrder.getId());
            if (!billNumbers.equals(StringUtils.EMPTY)) {
                model.addAttribute(ERROR_MESSAGE,
                        messageSource.getMessage("error.loa.bills.created", new String[] { billNumbers },
                                null));
                return LETTEROFACCEPTANCE_SUCCESS;
            }
        } else {
            final String mbRefNumbers = letterOfAcceptanceService.checkIfMBCreatedForLOA(workOrderEstimate);
            if (!mbRefNumbers.equals(StringUtils.EMPTY)) {
                model.addAttribute(ERROR_MESSAGE, messageSource.getMessage("error.loa.mb.created", new String[] { mbRefNumbers },
                        null));
                return LETTEROFACCEPTANCE_SUCCESS;
            } else if (!workOrderEstimate.getWorkOrderActivities().isEmpty()) {
                final String revisionEstimates = revisionWorkOrderService.getRevisionEstimatesForWorkOrder(workOrder.getId());
                if (!revisionEstimates.equals(StringUtils.EMPTY)) {
                    model.addAttribute(ERROR_MESSAGE, messageSource.getMessage("error.revisionestimates.created",
                            new String[] { revisionEstimates }, null));
                    return LETTEROFACCEPTANCE_SUCCESS;
                }
            }

        }

        if (letterOfAcceptanceService.checkIfMileStonesCreated(workOrder)) {
            model.addAttribute(ERROR_MESSAGE,
                    messageSource.getMessage("error.loa.milestone.created", new String[] {}, null));
            return LETTEROFACCEPTANCE_SUCCESS;
        }

        final String arfNumbers = letterOfAcceptanceService.checkIfARFCreatedForLOA(workOrderEstimate);
        if (!arfNumbers.equals(StringUtils.EMPTY)) {
            model.addAttribute(ERROR_MESSAGE,
                    messageSource.getMessage("error.loa.arf.created", new String[] { arfNumbers }, null));
            return LETTEROFACCEPTANCE_SUCCESS;
        }

        workOrder.setCancellationReason(cancellationReason);
        workOrder.setCancellationRemarks(cancellationRemarks);
        workOrder = letterOfAcceptanceService.cancel(workOrder);
        model.addAttribute("workOrder", workOrder);
        model.addAttribute("mode", "cancel");
        return LETTEROFACCEPTANCE_SUCCESS;
    }
}
