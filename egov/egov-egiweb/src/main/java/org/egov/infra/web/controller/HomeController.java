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

import com.google.gson.GsonBuilder;
import org.egov.infra.admin.common.entity.Favourites;
import org.egov.infra.admin.common.service.FavouritesService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.validation.ValidatorUtils;
import org.egov.infra.web.support.ui.menu.ApplicationMenuRenderingService;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.egov.infra.persistence.entity.enums.UserType.EMPLOYEE;
import static org.egov.infra.persistence.entity.enums.UserType.SYSTEM;
import static org.egov.infra.persistence.utils.PersistenceUtils.unproxy;
import static org.egov.infra.web.utils.WebUtils.setUserLocale;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    private static final String FEEDBACK_MSG_FORMAT = "%s\n\n%s\n%s";
    private static final String NON_EMPLOYE_PORTAL_HOME = "/portal/home";

    @Autowired
    private ApplicationMenuRenderingService applicationMenuRenderingService;

    @Autowired
    private FavouritesService favouritesService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CityService cityService;

    @Autowired
    private ValidatorUtils validatorUtils;

    @Value("${employee.portal.access.role}")
    private String portalAccessibleRole;

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.build.no}")
    private String appBuild;

    @Value("${app.core.build.no}")
    private String appCoreBuild;

    @Value("${issue.report.url}")
    private String issueReportingUrl;

    @Value("${dev.mode}")
    private boolean devMode;

    @ModelAttribute("user")
    public User user() {
        return unproxy(userService.getCurrentUser());
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id", "username", "mobileNumber", "type");
    }

    @GetMapping
    public ModelAndView showHome(HttpServletRequest request, HttpServletResponse response, ModelMap modelData) {
        User user = userService.getCurrentUser();
        setUserLocale(user, request, response);
        if (user.getType().equals(EMPLOYEE) || user.getType().equals(SYSTEM) || user.hasRole(portalAccessibleRole)) {
            String menuJson = new StringBuilder(100)
                    .append("[")
                    .append(new GsonBuilder().create().toJson(applicationMenuRenderingService.getApplicationMenuForUser(user)))
                    .append("]").toString();
            modelData.addAttribute("menu", menuJson);
            modelData.addAttribute("userName", defaultIfBlank(user.getName(), "Anonymous"));
            modelData.addAttribute("app_version", appVersion);
            modelData.addAttribute("app_buildno", appBuild);
            if (!devMode) {
                modelData.addAttribute("app_core_build_no", appCoreBuild);
                modelData.addAttribute("dflt_pwd_reset_req", checkDefaultPasswordResetRequired(user));
                int daysToExpirePwd = daysToExpirePassword(user);
                modelData.addAttribute("pwd_expire_in_days", daysToExpirePwd);
                modelData.addAttribute("warn_pwd_expire", daysToExpirePwd <= 5);
            }
            modelData.addAttribute("issue_report_url", issueReportingUrl);
            return new ModelAndView("home", modelData);
        } else {
            return new ModelAndView(new RedirectView(NON_EMPLOYE_PORTAL_HOME, false));
        }
    }

    @ResponseBody
    @PostMapping("favourite/add")
    public boolean addFavourite(@Valid @ModelAttribute Favourites favourites, BindingResult bindResult) {
        return !bindResult.hasErrors() && favouritesService.addToCurrentUserFavourite(favourites).getId() != null;
    }

    @ResponseBody
    @PostMapping("favourite/remove")
    public boolean removeFavourite(@RequestParam Integer actionId) {
        return favouritesService.removeFromCurrentUserFavourite(actionId);
    }

    @ResponseBody
    @PostMapping("password/update")
    public String changePassword(@RequestParam String currentPwd, @RequestParam String newPwd, @RequestParam String retypeNewPwd) {
        User user = userService.getCurrentUser();
        if (passwordEncoder.matches(currentPwd, user.getPassword())) {
            if (!validatorUtils.isValidPassword(newPwd))
                return "NEWPWD_INVALID";
            if (newPwd.equals(retypeNewPwd)) {
                userService.updateUserPassword(user, newPwd);
                return "SUCCESS";
            }
            return "NEWPWD_UNMATCH";
        }
        return "CURRPWD_UNMATCH";
    }

    @ResponseBody
    @PostMapping("feedback/sent")
    public boolean sendFeedback(@RequestParam String subject, @RequestParam String message) {
        cityService.sentFeedBackMail(cityService.getContactEmail(), subject,
                format(FEEDBACK_MSG_FORMAT, message, "Regards", user().getName()));
        return true;
    }

    @GetMapping("profile/edit")
    public String editProfile() {
        return "profile-edit";
    }

    @PostMapping("profile/edit")
    public String saveProfile(@Valid @ModelAttribute User user, BindingResult binder, HttpServletRequest request,
                              HttpServletResponse response, RedirectAttributes redirAttrib) {
        if (binder.hasErrors())
            return "profile-edit";
        userService.updateUser(user);
        setUserLocale(user, request, response);
        redirAttrib.addFlashAttribute("message", "msg.profile.update.success");
        return "redirect:/home/profile/edit";
    }

    private boolean checkDefaultPasswordResetRequired(User user) {
        return passwordEncoder.matches("12345678", user.getPassword()) || passwordEncoder.matches("demo", user.getPassword());
    }

    private int daysToExpirePassword(User user) {
        return Days.daysBetween(new LocalDate(), user.getPwdExpiryDate().toLocalDate()).getDays();
    }
}
