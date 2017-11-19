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

package org.egov.pgr.web.controller.complaint.citizen;

import org.egov.infra.admin.master.entity.CrossHierarchy;
import org.egov.infra.security.utils.captcha.CaptchaUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.web.controller.complaint.GenericComplaintController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.egov.pgr.utils.constants.PGRConstants.DEFAULT_RECEIVING_MODE;
import static org.egov.pgr.utils.constants.PGRConstants.MODULE_NAME;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/complaint/citizen")
public class CitizenComplaintRegistrationController extends GenericComplaintController {

    private static final String CITIZEN_COMPLAINT_REGISTRATION_FORM = "complaint/citizen/registration-form";
    private static final String ANONYMOUS_COMPLAINT_REGISTRATION_FORM = "complaint/citizen/anonymous-registration-form";
    private static final String LOCATION = "location";

    @Autowired
    private CaptchaUtils captchaUtils;

    @RequestMapping(value = "show-reg-form", method = GET)
    public String showComplaintRegistrationForm(@ModelAttribute Complaint complaint) {
        setReceivingMode(complaint, DEFAULT_RECEIVING_MODE);
        return CITIZEN_COMPLAINT_REGISTRATION_FORM;
    }

    @RequestMapping(value = "anonymous/show-reg-form", method = GET)
    public String showAnonymousComplaintRegistrationForm(@ModelAttribute Complaint complaint,
                                                         @RequestParam(required = false) String source) {
        setReceivingMode(complaint, isBlank(source) ? DEFAULT_RECEIVING_MODE : source);
        return ANONYMOUS_COMPLAINT_REGISTRATION_FORM;
    }

    @RequestMapping(value = "register", method = POST)
    public String registerComplaint(@Valid @ModelAttribute Complaint complaint, BindingResult resultBinder,
                                    RedirectAttributes redirectAttributes, @RequestParam("files") MultipartFile[] files, Model model) {

        if (complaint.getCrossHierarchyId() != null) {
            CrossHierarchy crosshierarchy = crossHierarchyService.findById(complaint.getCrossHierarchyId());
            complaint.setLocation(crosshierarchy.getParent());
            complaint.setChildLocation(crosshierarchy.getChild());
        }
        if (complaint.getLocation() == null && (complaint.getLat() == 0 || complaint.getLng() == 0))
            resultBinder.rejectValue(LOCATION, "location.required");

        if (resultBinder.hasErrors()) {
            if (complaint.getCrossHierarchyId() != null)
                model.addAttribute("crossHierarchyLocation",
                        complaint.getChildLocation().getName() + " - " + complaint.getLocation().getName());
            return CITIZEN_COMPLAINT_REGISTRATION_FORM;
        }

        try {
            complaint.setSupportDocs(fileStoreUtils.addToFileStore(files, MODULE_NAME, true));
            complaintService.createComplaint(complaint);
        } catch (ValidationException e) {
            resultBinder.rejectValue(LOCATION, e.getMessage());
            return CITIZEN_COMPLAINT_REGISTRATION_FORM;
        }
        redirectAttributes.addFlashAttribute("complaint", complaint);
        return "redirect:/complaint/reg-success/" + complaint.getCrn();
    }

    @RequestMapping(value = "anonymous/register", method = POST)
    public String registerComplaintAnonymous(@Valid @ModelAttribute Complaint complaint, BindingResult resultBinder,
                                             RedirectAttributes redirectAttributes, HttpServletRequest request,
                                             @RequestParam("files") MultipartFile[] files, Model model) {

        if (!captchaUtils.captchaIsValid(request))
            resultBinder.reject("captcha.not.valid");

        if (isBlank(complaint.getComplainant().getEmail())
                && isBlank(complaint.getComplainant().getMobile()))
            resultBinder.rejectValue("complainant.email", "email.or.mobile.ismandatory");

        if (isBlank(complaint.getComplainant().getName()))
            resultBinder.rejectValue("complainant.name", "complainant.name.ismandatory");

        if (complaint.getCrossHierarchyId() != null) {
            CrossHierarchy crosshierarchy = crossHierarchyService.findById(complaint.getCrossHierarchyId());
            complaint.setLocation(crosshierarchy.getParent());
            complaint.setChildLocation(crosshierarchy.getChild());
        }

        if (complaint.getLocation() == null && (complaint.getLat() == 0 || complaint.getLng() == 0))
            resultBinder.rejectValue(LOCATION, "location.required");

        if (resultBinder.hasErrors()) {
            if (null != complaint.getCrossHierarchyId())
                model.addAttribute("crossHierarchyLocation",
                        complaint.getChildLocation().getName() + " - " + complaint.getLocation().getName());
            return ANONYMOUS_COMPLAINT_REGISTRATION_FORM;
        }

        try {
            complaint.setSupportDocs(fileStoreUtils.addToFileStore(files, MODULE_NAME, true));
            complaintService.createComplaint(complaint);
        } catch (ValidationException e) {
            resultBinder.rejectValue(LOCATION, e.getMessage());
            return ANONYMOUS_COMPLAINT_REGISTRATION_FORM;
        }
        redirectAttributes.addFlashAttribute("complaint", complaint);
        return "redirect:/complaint/reg-success/" + complaint.getCrn();

    }
}