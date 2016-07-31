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

package org.egov.infra.web.controller;

import org.egov.infra.admin.common.service.IdentityRecoveryService;
import org.egov.infra.admin.master.entity.Location;
import org.egov.infra.admin.master.service.LocationService;
import org.egov.infra.config.properties.ApplicationProperties;
import org.egov.infra.validation.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private IdentityRecoveryService identityRecoveryService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private ApplicationProperties applicationProperties;

    @RequestMapping(value = "/password/recover", method = RequestMethod.POST)
    public String sendPasswordRecoveryURL(@RequestParam String identity,
                                          @RequestParam String originURL,
                                          @RequestParam boolean byOTP,
                                          final RedirectAttributes redirectAttrib) {
        redirectAttrib.addAttribute("recovered", identityRecoveryService.generateAndSendUserPasswordRecovery(identity,
                originURL + "/egi/login/password/reset?token=", byOTP));
        redirectAttrib.addAttribute("byOTP", byOTP);
        return "redirect:/login/secure";
    }

    @RequestMapping(value = "/password/reset", params = "token", method = RequestMethod.GET)
    public String viewPasswordReset(@RequestParam final String token, Model model) {
        model.addAttribute("valid", identityRecoveryService.tokenValid(token));
        return "password/reset";
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    public String validateAndSendNewPassword(@RequestParam final String token, @RequestParam final String newPassword,
                                             @RequestParam final String confirmPwd, final RedirectAttributes redirectAttrib) {
        if (!newPassword.equals(confirmPwd)) {
            redirectAttrib.addAttribute("error", "err.login.pwd.mismatch");
            return "redirect:/login/password/reset?token=" + token;
        }

        if (!validatorUtils.isValidPassword(newPassword)) {
            redirectAttrib.addAttribute("error", "usr.pwd.strength.msg."+applicationProperties.passwordStrength());
            return "redirect:/login/password/reset?token=" + token;
        }

        return "redirect:/login/secure?reset=" + identityRecoveryService.validateAndResetPassword(token, newPassword);
    }

    @RequestMapping(value = "/requiredlocations", method = RequestMethod.GET)
    @ResponseBody
    public List<Location> requiredLocations(@RequestParam final String username) {
        return locationService.getLocationRequiredByUserName(username);
    }
}
