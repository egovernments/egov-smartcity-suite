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

import org.egov.infra.admin.common.contracts.UserProfile;
import org.egov.infra.admin.common.entity.Favourites;
import org.egov.infra.admin.common.service.FavouritesService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.mapper.BeanMapperConfiguration;
import org.egov.infra.validation.ValidatorUtils;
import org.egov.infra.web.contract.request.FeedbackRequest;
import org.egov.infra.web.contract.request.PasswordChangeRequest;
import org.egov.infra.web.contract.response.HomePageResponse;
import org.egov.infra.web.support.ui.menu.ApplicationMenuRenderingService;
import org.egov.infra.web.utils.WebUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.egov.infra.persistence.entity.enums.UserType.EMPLOYEE;
import static org.egov.infra.persistence.entity.enums.UserType.SYSTEM;
import static org.egov.infra.persistence.utils.PersistenceUtils.unproxy;
import static org.egov.infra.utils.ApplicationConstant.ANONYMOUS;
import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    private static final String FEEDBACK_MSG_FORMAT = "%s\n\n%s\n%s";
    private static final String NON_EMPLOYE_PORTAL_HOME = "/portal/home";
    private static final String PROFILE_EDIT = "profile/edit";
    private static final String PROFILE_EDIT_VIEW = "profile-edit";
    private static final String DEFAULT_EMP_PWD = "12345678";
    private static final String DEFAULT_USER_PWD = "demo";
    private static final String RESP_SUCCESS = "SUCCESS";

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

    @Autowired
    private BeanMapperConfiguration beanMapperConfiguration;

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

    @GetMapping
    public ModelAndView showHome(HttpServletRequest request, HttpServletResponse response, ModelMap modelData) {
        User user = userService.getCurrentUser();
        setUserLocale(user, request, response);
        if (user.hasAnyType(EMPLOYEE, SYSTEM) || user.hasRole(portalAccessibleRole)) {
            HomePageResponse homePageResponse = new HomePageResponse();
            homePageResponse.setMenu(toJSON(applicationMenuRenderingService.getApplicationMenuForUser(user)));
            homePageResponse.setUserName(defaultIfBlank(user.getName(), ANONYMOUS));
            homePageResponse.setAppVersion(appVersion);
            homePageResponse.setAppBuildNo(appBuild);
            if (!devMode) {
                homePageResponse.setAppCoreBuildNo(appCoreBuild);
                homePageResponse.setRequiredPasswordReset(checkDefaultPasswordResetRequired(user));
                int daysToExpirePwd = daysToExpirePassword(user);
                homePageResponse.setDaysToPasswordExpiry(daysToExpirePwd);
                homePageResponse.setWarnPasswordExpiry(daysToExpirePwd <= 5);
            }
            homePageResponse.setIssueReportingURL(issueReportingUrl);
            modelData.addAttribute("homePageResponse", homePageResponse);
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
    public ResponseEntity changePassword(@Valid PasswordChangeRequest passwordChangeRequest, BindingResult bindResult) {
        if (bindResult.hasErrors()) {
            return ResponseEntity.badRequest().body(WebUtils.bindErrorToMap(bindResult));
        } else {
            User user = userService.getCurrentUser();
            if (passwordEncoder.matches(passwordChangeRequest.getCurrentPwd(), user.getPassword())) {
                if (passwordEncoder.matches(passwordChangeRequest.getNewPwd(), user.getPassword()))
                    return ResponseEntity.ok("NEW_AND_CURR_PWD_SAME");
                if (!validatorUtils.isValidPassword(passwordChangeRequest.getNewPwd()))
                    return ResponseEntity.ok("NEWPWD_INVALID");
                if (passwordChangeRequest.getNewPwd().equals(passwordChangeRequest.getRetypeNewPwd())) {
                    userService.updateUserPassword(user, passwordChangeRequest.getNewPwd());
                    return ResponseEntity.ok(RESP_SUCCESS);
                }
                return ResponseEntity.ok("NEWPWD_UNMATCH");
            }
            return ResponseEntity.ok("CURRPWD_UNMATCH");
        }
    }

    @ResponseBody
    @PostMapping("feedback/sent")
    public ResponseEntity sendFeedback(@Valid FeedbackRequest feedbackRequest, BindingResult bindResult) {

        if (bindResult.hasErrors()) {
            return ResponseEntity.badRequest().body(WebUtils.bindErrorToMap(bindResult));
        } else {
            cityService.sentFeedBackMail(cityService.getContactEmail(), feedbackRequest.getSubject(),
                    format(FEEDBACK_MSG_FORMAT, feedbackRequest.getMessage(), "Regards", userService.getCurrentUser().getName()));
            return ResponseEntity.ok(RESP_SUCCESS);
        }
    }

    @GetMapping(PROFILE_EDIT)
    public String editProfile(Model model) {
        model.addAttribute("userProfile", beanMapperConfiguration.map(userService.getCurrentUser(), UserProfile.class));
        return PROFILE_EDIT_VIEW;
    }

    @PostMapping(PROFILE_EDIT)
    public String saveProfile(@Valid @ModelAttribute UserProfile userProfile, BindingResult binder, HttpServletRequest request,
                              HttpServletResponse response, RedirectAttributes redirAttrib) {
        if (binder.hasErrors())
            return PROFILE_EDIT_VIEW;
        User user = unproxy(userService.getCurrentUser());
        userProfile.setMobileNumber(user.getMobileNumber());
        beanMapperConfiguration.map(userProfile, user);
        userService.updateUser(user);
        setUserLocale(user, request, response);
        redirAttrib.addFlashAttribute("message", "msg.profile.update.success");
        return "redirect:/home/profile/edit";
    }

    private boolean checkDefaultPasswordResetRequired(User user) {
        return passwordEncoder.matches(DEFAULT_EMP_PWD, user.getPassword())
                || passwordEncoder.matches(DEFAULT_USER_PWD, user.getPassword());
    }

    private int daysToExpirePassword(User user) {
        return Days.daysBetween(new LocalDate(), user.getPwdExpiryDate().toLocalDate()).getDays();
    }

    private void setUserLocale(User user, HttpServletRequest request, HttpServletResponse response) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        localeResolver.setLocale(request, response, user.locale());
    }
}
