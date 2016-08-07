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
package org.egov.portal.web.controller.citizen;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.security.utils.RecaptchaUtils;
import org.egov.infra.validation.ValidatorUtils;
import org.egov.portal.entity.Citizen;
import org.egov.portal.service.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/citizen")
public class CitizenRegistrationController {

    private final CitizenService citizenService;

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private RecaptchaUtils recaptchaUtils;

    @Autowired
    public CitizenRegistrationController(final CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerCitizen(@ModelAttribute final Citizen citizen) {
        return "signup";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerCitizen(@Valid @ModelAttribute final Citizen citizen, final BindingResult errors,
                                  final HttpServletRequest request,
                                  final RedirectAttributes redirectAttrib) {
        if (!validatorUtils.isValidPassword(citizen.getPassword()))
            errors.rejectValue("password", "error.pwd.invalid");
        else if (!StringUtils.equals(citizen.getPassword(), request.getParameter("con-password")))
            errors.rejectValue("password", "error.pwd.mismatch");
        if (!recaptchaUtils.captchaIsValid(request))
            errors.rejectValue("active", "error.recaptcha.verification");
        if (errors.hasErrors())
            return "signup";
        citizenService.create(citizen);
        redirectAttrib.addAttribute("message", "msg.reg.success");
        return "redirect:register?activation=true";
    }

    @RequestMapping(value = "/activation", method = RequestMethod.POST)
    public String citizenOTPActivation(@RequestParam final String activationCode) {
        return "redirect:register?activation=true&activated=" + (citizenService.activateCitizen(activationCode) != null);
    }

    @RequestMapping(value = "/activation/resendotp", method = RequestMethod.POST)
    public String resendOTP(@RequestParam final String mobile, final RedirectAttributes redirectAttrib) {
        Citizen citizen = citizenService.getCitizenByUserName(mobile);
        if (citizen == null)
            return "redirect:../register?activation=true&otprss=false";
        citizenService.resendActivationCode(citizen);
        redirectAttrib.addAttribute("message", "msg.otpresend.success");
        return "redirect:../register?activation=true";
    }
}
