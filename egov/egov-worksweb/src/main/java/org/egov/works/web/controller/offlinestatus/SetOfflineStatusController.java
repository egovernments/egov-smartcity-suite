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
package org.egov.works.web.controller.offlinestatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.offlinestatus.service.OfflineStatusService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrder.OfflineStatuses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/offlinestatus")
public class SetOfflineStatusController {

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private OfflineStatusService offlineStatusService;
    
    @Autowired
    private ResourceBundleMessageSource messageSource;

    @RequestMapping(value = "/setstatus-loa/{workOrderId}", method = RequestMethod.GET)
    public String setOffLineStatus(final Model model, @PathVariable final Long workOrderId,
            final HttpServletRequest request)
            throws ApplicationException {
        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderById(workOrderId);
        final List<OfflineStatus> offlineStatuses = offlineStatusService.getOfflineStatusByObjectIdAndType(workOrder.getId(),WorksConstants.WORKORDER);
        workOrder.setOfflineStatuses(offlineStatuses);
        setDropDownValues(model);
        model.addAttribute("workOrder", workOrder);
        model.addAttribute("mode", "newWorkOrder");
        return "setstatus-form";
    }

    private void setDropDownValues(final Model model) {

        final List<String> offlineStatuses = new ArrayList<String>();

        for (final WorkOrder.OfflineStatuses status : WorkOrder.OfflineStatuses.values())
            offlineStatuses.add(status.toString().toUpperCase());

        final List<EgwStatus> egwStatuses = egwStatusHibernateDAO.getStatusListByModuleAndCodeList(WorksConstants.WORKORDER,
                offlineStatuses);
        model.addAttribute("egwStatus", egwStatuses);
    }

    @RequestMapping(value = "/offlinestatus-save", method = RequestMethod.POST)
    public String saveOffLineStatus(@ModelAttribute final WorkOrder workOrder, final Model model,
            final HttpServletRequest request, final BindingResult resultBinder)
            throws ApplicationException {

        int i = 0;
        // List of all status
        final OfflineStatuses[] statList = OfflineStatuses.values();
        final String[] statusName = new String[statList.length];
        for (int j = 0; j < statList.length; j++)
            statusName[j] = statList[j].name();
        final List<String> OffStatuses = Arrays.asList(statusName);

        // Get Selected status and add to string array
        final List<OfflineStatus> selectedStatus = workOrder.getOfflineStatuses();
        final String[] selectedStatusArr = new String[selectedStatus.size()];
        for (int j = 0; j < selectedStatus.size(); j++)
            if (selectedStatus.get(j).getEgwStatus() != null) {
                final EgwStatus egwStatus = egwStatusHibernateDAO.findById(selectedStatus.get(j).getEgwStatus().getId(), false);
                selectedStatusArr[j] = egwStatus.getCode();
            }

        // pass selected status to statusnamedetail if not matches return error
        for (final String statName : offlineStatusService.getStatusNameDetails(selectedStatusArr)) {
            if (!OffStatuses.isEmpty() && !statName.equals(OffStatuses.get(i))) {
                resultBinder.reject("errors.status.order.incorrect",
                        new String[] { statName.replaceAll("_", " "),
                                OffStatuses.get(OffStatuses.indexOf(statName) - 1).replaceAll("_", " ") },
                        "errors.status.order.incorrect");
                break;
            }
            i++;
        }
        final List<OfflineStatus> newOfflinestatus = workOrder.getOfflineStatuses();
        for (int k = 0; k < newOfflinestatus.size() - 1; k++)
            if (newOfflinestatus.get(k).getStatusDate().after(newOfflinestatus.get(k + 1).getStatusDate())) {
                final EgwStatus egwStatus = egwStatusHibernateDAO.findById(selectedStatus.get(k + 1).getEgwStatus().getId(), false);
                final EgwStatus newEgwStatus = egwStatusHibernateDAO.findById(selectedStatus.get(k).getEgwStatus().getId(), false);
                resultBinder.reject("errors.status.date.incorrect",
                        new String[] { egwStatus.getDescription(), newEgwStatus.getDescription() },
                        "errors.status.date.incorrect");
                break;
            }

        if (resultBinder.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("workOrder", workOrder);
            return "setstatus-form";
        }
        List<OfflineStatus> offlineStatuses = workOrder.getOfflineStatuses();
        offlineStatusService.create(offlineStatuses, workOrder.getId(), WorksConstants.WORKORDER);
        model.addAttribute("success", messageSource.getMessage("msg.offlinestatus.success",
                new String[] { "" }, null));
        return "setstatus-success";
    }

}
