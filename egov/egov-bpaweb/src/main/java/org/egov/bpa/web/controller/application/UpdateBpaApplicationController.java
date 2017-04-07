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
package org.egov.bpa.web.controller.application;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.bpa.application.entity.BpaApplication;
import org.egov.bpa.application.entity.BpaAppointmentSchedule;
import org.egov.bpa.application.service.ApplicationBpaService;
import org.egov.bpa.application.service.BpaAppointmentScheduleService;
import org.egov.bpa.utils.BPASmsAndEmailService;
import org.egov.bpa.utils.BpaConstants;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class UpdateBpaApplicationController extends BpaGenericApplicationController {
    
    private static final String REDIRECT_APPLICATION_VIEW_APPOINTMENT = "redirect:/application/view-appointment/";

    private static final String RESCHEDULE_APPIONTMENT = "reschedule-appiontment";

    private static final String BPA_APPOINTMENT_SCHEDULE = "bpaAppointmentSchedule";

    private static final String VIEW_SCHEDULE_APPIONTMENT = "view-schedule-appiontment";

    private static final String SCHEDULE_APPIONTMENT_NEW = "schedule-appiontment-new";

    private static final String ADDITIONALRULE = "additionalRule";
    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ApplicationBpaService applicationBpaService;
    @Autowired
    private BpaAppointmentScheduleService bpaAppointmentScheduleService;
    @Autowired
    private BPASmsAndEmailService bpaSmsAndEmailService;

    @ModelAttribute
    public BpaApplication getBpaApplication(@PathVariable final String applicationNumber) {
        return applicationBpaService.findByApplicationNumber(applicationNumber);
    }

    @RequestMapping(value = "/update/{applicationNumber}", method = RequestMethod.GET)
    public String updateApplicationForm(final Model model, @PathVariable final String applicationNumber,
            final HttpServletRequest request) {
        final BpaApplication application = getBpaApplication(applicationNumber);
        if(application.getAppointmentSchedule().isEmpty()){
            model.addAttribute("mode", "newappointment");
        } else {
            model.addAttribute("mode", "postponeappointment");
        }
        if (application != null) {
            loadViewdata(model, application);
            if (application.getState() != null
                    && application.getState().getValue().equalsIgnoreCase(BpaConstants.BPA_STATUS_SUPERINDENT_APPROVED)) {
                return "documentscrutiny-form";
            }
        }
        return "bpaapplication-Form";
    }
    
    @RequestMapping(value = "/documentscrutiny/{applicationNumber}", method = RequestMethod.GET)
    public String documentScrutinyForm(final Model model, @PathVariable final String applicationNumber,
            final HttpServletRequest request) {
        final BpaApplication application = getBpaApplication(applicationNumber);
        if (application != null && application.getState() != null
                && application.getState().getValue().equalsIgnoreCase(BpaConstants.BPA_STATUS_SUPERINDENT_APPROVED)) {
            loadViewdata(model, application);
            model.addAttribute("loginUser", securityUtils.getCurrentUser());
        }
        // return to error page if status is not superindent approved.
        return "createdocumentscrutiny-form";
    }

    @RequestMapping(value = "/documentscrutiny/{applicationNumber}", method = RequestMethod.POST)
    public String documentScrutinyForm(@Valid @ModelAttribute("bpaApplication") BpaApplication bpaApplication,
            @PathVariable final String applicationNumber,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model, @RequestParam("files") final MultipartFile[] files) {
        if (resultBinder.hasErrors()) {
            loadViewdata(model, bpaApplication);
            return "createdocumentscrutiny-form";
        }

        bpaApplication = applicationBpaService.updateApplication(bpaApplication,
                Long.valueOf(request.getParameter("approvalPosition")));
        return "viewapplication-form";
    }

    private void loadViewdata(final Model model, final BpaApplication application) {
        model.addAttribute("stateType", application.getClass().getSimpleName());
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        model.addAttribute(ADDITIONALRULE, BpaConstants.CREATE_ADDITIONAL_RULE_CREATE);
        workflowContainer.setAdditionalRule(BpaConstants.CREATE_ADDITIONAL_RULE_CREATE);
        prepareWorkflow(model, application, workflowContainer);
        model.addAttribute("currentState", application.getCurrentState().getValue());
        model.addAttribute("bpaApplication", application);
    }

    @RequestMapping(value = "/update/{applicationNumber}", method = RequestMethod.POST)
    public String updateApplication(@Valid @ModelAttribute("bpaApplication") BpaApplication bpaApplication,
            @PathVariable final String applicationNumber,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model, @RequestParam("files") final MultipartFile[] files) {
       
        if (resultBinder.hasErrors()) {
            loadViewdata(model, bpaApplication);
            return "bpaapplication-Form";
        }
        bpaApplication = applicationBpaService.updateApplication(bpaApplication);
        return "viewapplication-form";
    }
    
    @RequestMapping(value="/scheduleappointment/{applicationNumber}",method=RequestMethod.GET)
    public String newScheduleAppointment(@PathVariable final String applicationNumber, final Model model) {
        model.addAttribute(BPA_APPOINTMENT_SCHEDULE, new BpaAppointmentSchedule());
        model.addAttribute("applicationNumber", applicationNumber);
        return SCHEDULE_APPIONTMENT_NEW;
    }
    
    @RequestMapping(value="/scheduleappointment/{applicationNumber}",method=RequestMethod.POST)
    public String createScheduleAppointment(@Valid @ModelAttribute final BpaAppointmentSchedule appointmentSchedule,@PathVariable final String applicationNumber, final Model model) {
        BpaApplication bpaApplication = applicationBpaService.findByApplicationNumber(applicationNumber);
        appointmentSchedule.setApplication(bpaApplication);
        BpaAppointmentSchedule schedule = bpaAppointmentScheduleService.save(appointmentSchedule);
        bpaSmsAndEmailService.sendSMSAndEmailForDocumentScrtiny(schedule, bpaApplication);
        return REDIRECT_APPLICATION_VIEW_APPOINTMENT + schedule.getId();
    }
    
    @RequestMapping(value="/postponeappointment/{applicationNumber}", method=RequestMethod.GET)
    public String editScheduleAppointment(@PathVariable final String applicationNumber, final Model model) {
        BpaApplication bpaApplication = applicationBpaService.findByApplicationNumber(applicationNumber);
        List<BpaAppointmentSchedule> appointmentScheduledList = bpaAppointmentScheduleService.findByApplication(bpaApplication);
        model.addAttribute(BPA_APPOINTMENT_SCHEDULE, new BpaAppointmentSchedule());
        model.addAttribute("applicationNumber", applicationNumber);
        model.addAttribute("appointmentScheduledList", appointmentScheduledList);
        model.addAttribute("mode", "postponeappointment");
        return RESCHEDULE_APPIONTMENT;
    }
    
    @RequestMapping(value="/postponeappointment/{applicationNumber}",method=RequestMethod.POST)
    public String updateScheduleAppointment(@Valid @ModelAttribute final BpaAppointmentSchedule appointmentSchedule, @PathVariable final String applicationNumber,
            @RequestParam Long bpaAppointmentScheduleId ,final Model model) {
        BpaApplication bpaApplication = applicationBpaService.findByApplicationNumber(applicationNumber);
        BpaAppointmentSchedule parent = bpaAppointmentScheduleService.findById(bpaAppointmentScheduleId);
        appointmentSchedule.setApplication(bpaApplication);
        appointmentSchedule.setPostponed(true);
        appointmentSchedule.setParent(parent);
        BpaAppointmentSchedule schedule = bpaAppointmentScheduleService.save(appointmentSchedule);
        bpaSmsAndEmailService.sendSMSAndEmailForDocumentScrtiny(schedule, bpaApplication);
        return REDIRECT_APPLICATION_VIEW_APPOINTMENT + schedule.getId();
    }
    
    @RequestMapping(value="/view-appointment/{applicationNumber}",method=RequestMethod.GET)
    public String viewScheduledAppointment(@PathVariable final String applicationNumber, final Model model) {
        List<BpaAppointmentSchedule> appointmentScheduledList = bpaAppointmentScheduleService.findByIdAsList(Long.valueOf(applicationNumber));
        model.addAttribute("appointmentScheduledList", appointmentScheduledList);
        return VIEW_SCHEDULE_APPIONTMENT;
    }
}
