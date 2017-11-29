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
package org.egov.works.web.controller.milestone;

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.service.MilestoneService;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.workorderestimate.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping(value = "/milestone")
public class CreateMilestoneController {

    @Autowired
    private EgwTypeOfWorkHibernateDAO egwTypeOfWorkHibernateDAO;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private MilestoneService milestoneService;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;
    
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;
    
    @Autowired
    private EstimateService estimateService;

    @RequestMapping(value = "/newform", method = RequestMethod.GET)
    public String showNewMilestoneForm(
            final Model model, final HttpServletRequest request) throws ApplicationException {
        final String estimateNumber = request.getParameter("estimateNumber");
        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderByEstimateNumber(estimateNumber);
        if(milestoneService.checkMilestoneCreated(workOrder.getId())){
            String message = messageSource.getMessage("error.milestonecreated.validate", new String[] { workOrder.getWorkOrderNumber() }, null);
            model.addAttribute("errorMessage", message);
            return "milestone-success";
        }
        
        final LineEstimateDetails lineEstimateDetails = lineEstimateService.findByEstimateNumber(estimateNumber);
        model.addAttribute("workOrder", workOrder);
        model.addAttribute("lineEstimateDetails", lineEstimateDetails);
        model.addAttribute("milestone", new Milestone());
        return "newMilestone-form";
    }

    @RequestMapping(value = "/milestone-save", method = RequestMethod.POST)
    public String create(@ModelAttribute("milestone") final Milestone milestone,
            final Model model, final BindingResult errors, final HttpServletRequest request, final BindingResult resultBinder)
                    throws ApplicationException, IOException {
        final Long workOrderId = Long.valueOf(request.getParameter("workOrderId"));
        final String estimateNumber = request.getParameter("estimateNumber");
        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderById(workOrderId);
        final LineEstimateDetails lineEstimateDetails = lineEstimateService.findByEstimateNumber(estimateNumber);
        final AbstractEstimate abstractEstimate = estimateService
                .getAbstractEstimateByEstimateNumberAndStatus(lineEstimateDetails.getEstimateNumber());
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService.getEstimateByWorkOrderAndEstimateAndStatus(
                workOrder.getId(), abstractEstimate.getId());
        milestone.setWorkOrderEstimate(workOrderEstimate);
        final Milestone newMilestone = milestoneService.create(milestone);
        model.addAttribute("milestone", newMilestone);
        model.addAttribute("message", messageSource.getMessage("msg.milestone.create.success",
                new String[] { estimateNumber },null));

        return "milestone-success";
    }
}
