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

package org.egov.wtms.web.controller.masters;

import org.egov.wtms.masters.entity.ApplicationProcessTime;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/masters")
public class ApplicationProcessTimeController {
    private final ConnectionCategoryService connectionCategoryService;

    private final ApplicationTypeService applicationTypeService;

    private final ApplicationProcessTimeService applicationProcessTimeService;

    @Autowired
    public ApplicationProcessTimeController(final ApplicationTypeService applicationTypeService,
            final ConnectionCategoryService connectionCategoryService,
            final ApplicationProcessTimeService applicationProcessTimeService) {
        this.connectionCategoryService = connectionCategoryService;
        this.applicationTypeService = applicationTypeService;
        this.applicationProcessTimeService = applicationProcessTimeService;
    }

    @RequestMapping(value = "/applicationProcessTime", method = RequestMethod.GET)
    public String viewForm(final Model model) {
        final ApplicationProcessTime applicationProcessTime = new ApplicationProcessTime();
        model.addAttribute("applicationProcessTime", applicationProcessTime);
        model.addAttribute("connectionCategories", connectionCategoryService.getAllActiveConnectionCategory());
        model.addAttribute("applicationTypes", applicationTypeService.findAll());
        model.addAttribute("reqAttr", "false");
        model.addAttribute("mode", "create");
        return "application-process-time-master";
    }

    @RequestMapping(value = "/applicationProcessTime", method = RequestMethod.POST)
    public String createApplicationProcessTimeMasterData(
            @Valid @ModelAttribute final ApplicationProcessTime applicationProcessTime, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("connectionCategories", connectionCategoryService.getAllActiveConnectionCategory());
            model.addAttribute("applicationTypes", applicationTypeService.findAll());
            return "application-process-time-master";
        } else

            applicationProcessTimeService.createApplicationProcessTime(applicationProcessTime);
        redirectAttrs.addFlashAttribute("applicationProcessTime", applicationProcessTime);
        model.addAttribute("message", "Application ProcessTime created successfully.");
        model.addAttribute("mode", "create");
        return "application-process-time-master-success";
    }

    @RequestMapping(value = "/applicationProcessTime/list", method = RequestMethod.GET)
    public String getApplicationProcessTimeList(final Model model) {
        final List<ApplicationProcessTime> applicationProcessTimeList = applicationProcessTimeService.findAll();
        model.addAttribute("applicationProcessTimeList", applicationProcessTimeList);
        return "application-process-master-list";
    }

    @RequestMapping(value = "/applicationProcessTime/edit", method = RequestMethod.GET)
    public String getApplicationProcessTimeMaster(final Model model) {
        model.addAttribute("mode", "edit");
        return getApplicationProcessTimeList(model);
    }

    @RequestMapping(value = "/applicationProcessTime/edit/{applicationProcessId}", method = RequestMethod.GET)
    public String getApplicationProcessTimeMasterDetails(final Model model,
            @PathVariable final String applicationProcessId) {
        final ApplicationProcessTime applicationProcessTime = applicationProcessTimeService
                .findOne(Long.parseLong(applicationProcessId));
        model.addAttribute("applicationProcessTime", applicationProcessTime);
        model.addAttribute("connectionCategories", connectionCategoryService.getAllActiveConnectionCategory());
        model.addAttribute("applicationTypes", applicationTypeService.findAll());
        model.addAttribute("reqAttr", "true");
        return "application-process-time-master";
    }

    @RequestMapping(value = "/applicationProcessTime/edit/{applicationProcessId}", method = RequestMethod.POST)
    public String editApplicationProcessTimeData(
            @Valid @ModelAttribute final ApplicationProcessTime applicationProcessTime, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model, @PathVariable final long applicationProcessId) {
        if (errors.hasErrors()) {
            model.addAttribute("connectionCategories", connectionCategoryService.getAllActiveConnectionCategory());
            model.addAttribute("applicationTypes", applicationTypeService.findAll());
            return "application-process-time-master";
        } else
            applicationProcessTimeService.updateApplicationProcessTime(applicationProcessTime);
        redirectAttrs.addFlashAttribute("applicationProcessTime", applicationProcessTime);
        model.addAttribute("message", "Application ProcessTime updated successfully.");
        return "application-process-time-master-success";
    }
}
