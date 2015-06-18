/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pgr.web.controller.complaint.citizen;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.validation.ValidatorUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.web.controller.complaint.GenericComplaintController;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/complaint/citizen")
public class CitizenComplaintRegistrationController extends GenericComplaintController {

    @RequestMapping(value = "show-reg-form", method = GET)
    public String showComplaintRegistrationForm(@ModelAttribute final Complaint complaint) {
        return "complaint/citizen/registration-form";
    }

    @RequestMapping(value = "anonymous/show-reg-form", method = GET)
    public String showAnonymousComplaintRegistrationForm(@ModelAttribute final Complaint complaint) {
        return "complaint/citizen/anonymous-registration-form";
    }

    @RequestMapping(value = "register", method = POST)
    public String registerComplaint(@Valid @ModelAttribute final Complaint complaint, final BindingResult resultBinder,
            final RedirectAttributes redirectAttributes, @RequestParam("files") final MultipartFile[] files) {

        if (complaint.getComplaintType() != null && complaint.getComplaintType().isLocationRequired())
            if (complaint.getLocation() == null && (complaint.getLat() == 0 || complaint.getLng() == 0))
                resultBinder.rejectValue("location", "location.required");

        if (resultBinder.hasErrors())
            return "complaint/citizen/registration-form";

        complaint.setSupportDocs(addToFileStore(files));
        complaintService.createComplaint(complaint);
        redirectAttributes.addFlashAttribute("complaint", complaint);
        return "redirect:/reg-success";
    }

    @RequestMapping(value = "anonymous/register", method = POST)
    public String registerComplaintAnonymous(@Valid @ModelAttribute final Complaint complaint, final BindingResult resultBinder,
            final RedirectAttributes redirectAttributes, final HttpServletRequest request, @RequestParam("files") final MultipartFile[] files) {

        if (!ValidatorUtils.isCaptchaValid(request))
            resultBinder.reject("captcha.not.valid");
                
        if (StringUtils.isBlank(complaint.getComplainant().getEmail())
                && StringUtils.isBlank(complaint.getComplainant().getMobile()))
            resultBinder.rejectValue("complainant.email", "email.or.mobile.ismandatory");

        if (StringUtils.isBlank(complaint.getComplainant().getName()))
            resultBinder.rejectValue("complainant.name", "complainant.name.ismandatory");

        if (complaint.getComplaintType() != null && complaint.getComplaintType().isLocationRequired())
            if (complaint.getLocation() == null && (complaint.getLat() == 0 || complaint.getLng() == 0))
                resultBinder.rejectValue("location", "location.required");

        if (resultBinder.hasErrors())
            return "complaint/citizen/anonymous-registration-form";

        complaint.setSupportDocs(addToFileStore(files));
        complaintService.createComplaint(complaint);
        redirectAttributes.addFlashAttribute("complaint", complaint);
        return "redirect:/reg-success";
    }

}