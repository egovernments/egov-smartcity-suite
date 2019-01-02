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

package org.egov.infra.web.controller;

import org.egov.infra.admin.common.service.IdentityRecoveryService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.LocationService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.security.auth.PreAuthService;
import org.egov.infra.validation.ValidatorUtils;
import org.egov.infra.web.contract.response.PreAuthCheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    private static final String RESET_PASS_URL_PATH = "password/reset";
    private static final String TOKEN = "token";
    private static final String VALID = "valid";

    @Autowired
    private IdentityRecoveryService identityRecoveryService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private PreAuthService preAuthService;

    @Value("${user.pwd.strength}")
    private String passwordStrength;

    @PostMapping("password/recover")
    public String sendPasswordRecoveryURL(@RequestParam String identity, @RequestParam String originURL,
                                          @RequestParam boolean byOTP, RedirectAttributes redirectAttrib) {
        redirectAttrib.addAttribute("recovered", identityRecoveryService
                .generateAndSendUserPasswordRecovery(identity, originURL + "/egi/login/password/reset?token=", byOTP));
        redirectAttrib.addAttribute("byOTP", byOTP);
        return "redirect:/login/secure";
    }

    @RequestMapping(value = RESET_PASS_URL_PATH, params = TOKEN, method = {GET, POST})
    public String viewPasswordReset(@RequestParam String token, Model model) {
        model.addAttribute(VALID, identityRecoveryService.tokenValid(token));
        model.addAttribute(TOKEN, token);
        return RESET_PASS_URL_PATH;
    }

    @PostMapping(value = RESET_PASS_URL_PATH, params = {"validToken", "newPassword", "confirmPwd"})
    public String validateAndSendNewPassword(@RequestParam String validToken, @RequestParam String newPassword,
                                             @RequestParam String confirmPwd, Model model) {
        if (!newPassword.equals(confirmPwd)) {
            model.addAttribute("error", "err.login.pwd.mismatch");
            model.addAttribute(TOKEN, validToken);
            model.addAttribute(VALID, identityRecoveryService.tokenValid(validToken));
            return RESET_PASS_URL_PATH;
        }

        if (!validatorUtils.isValidPassword(newPassword)) {
            model.addAttribute("error", "usr.pwd.strength.msg." + passwordStrength);
            model.addAttribute(TOKEN, validToken);
            model.addAttribute(VALID, identityRecoveryService.tokenValid(validToken));
            return RESET_PASS_URL_PATH;
        }

        return "redirect:/login/secure?reset=" + identityRecoveryService.validateAndResetPassword(validToken, newPassword);
    }

    @PostMapping("preauth-check")
    @ResponseBody
    public PreAuthCheckResponse preAuthCheck(@RequestParam String username) {
        User user = this.userService.getUserByUsername(username);
        return new PreAuthCheckResponse(locationService.getUserLocations(user), preAuthService.sendOtpIfRequired(user));
    }
}
