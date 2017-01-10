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
package org.egov.works.web.controller.masters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.infra.exception.ApplicationException;
import org.egov.works.masters.entity.ScheduleCategory;
import org.egov.works.masters.service.ScheduleCategoryService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/masters")
public class CreateScheduleCategoryController extends BaseScheduleCategoryController {

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private ScheduleCategoryService scheduleCategoryService;

    @RequestMapping(value = "/schedulecategory-newform", method = RequestMethod.GET)
    public String showScheduleCategoryForm(final Model model) {
        final ScheduleCategory scheduleCategory = new ScheduleCategory();
        model.addAttribute("scheduleCategory", scheduleCategory);
        return "scheduleCategory-form";
    }

    @RequestMapping(value = "/schedulecategory-save", method = RequestMethod.POST)
    public String createScheduleCategory(@Valid @ModelAttribute final ScheduleCategory scheduleCategory,
            final BindingResult resultBinder, final Model model) throws ApplicationException, IOException {
        validateScheduleCategory(scheduleCategory, resultBinder);
        if (resultBinder.hasErrors()) {
            model.addAttribute("scheduleCategory", scheduleCategory);
            return "scheduleCategory-form";
        }
        scheduleCategoryService.save(scheduleCategory);
        return "redirect:/masters/schedulecategory-success?scheduleCategoryId=" + scheduleCategory.getId();

    }

    @RequestMapping(value = "/schedulecategory-success", method = RequestMethod.GET)
    public String successView(final Model model, final HttpServletRequest request) {
        final Long scheduleCategoryId = Long.valueOf(request.getParameter("scheduleCategoryId"));
        final ScheduleCategory scheduleCategory = scheduleCategoryService.getScheduleCategoryById(scheduleCategoryId);
        final String mode = request.getParameter(WorksConstants.MODE);
        model.addAttribute("scheduleCategory", scheduleCategory);
        if (mode != null && mode.equalsIgnoreCase(WorksConstants.EDIT)) {
            model.addAttribute(WorksConstants.MODE, mode);
            model.addAttribute("modifySuccess", messageSource.getMessage("msg.schedulecategory.modify.success",
                    null, null));
        } else
            model.addAttribute("createSuccess", messageSource.getMessage("msg.schedulecategory.save.success",
                    new String[] { scheduleCategory.getCode() }, null));

        return "scheduleCategory-success";

    }

}
