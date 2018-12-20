/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.council.web.controller;

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.council.entity.CouncilRouter;
import org.egov.council.enums.PreambleTypeEnum;
import org.egov.council.service.CouncilRouterService;
import org.egov.council.web.adaptor.CouncilRouterJsonAdaptor;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/councilrouter")
public class CouncilRouterController {
    private static final String POSITION = "position";
    private static final String COUNCILROUTER_NEW = "councilrouter-new";
    private static final String COUNCILROUTER_RESULT = "councilrouter-result";
    private static final String COUNCILROUTER_SEARCH = "councilrouter-search";
    private static final String COUNCILROUTER_VIEW = "councilrouter-view";
    private static final String COUNCILROUTER_EDIT = "councilrouter-edit";
    private static final String COUNCILROUTER = "councilRouter";

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private CouncilRouterService councilRouterService;

    private void prepareNewForm(final Model model) {
        model.addAttribute("department", departmentService.getAllDepartments());
        model.addAttribute("preambleType", PreambleTypeEnum.values());
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        CouncilRouter councilRouter = new CouncilRouter();
        model.addAttribute(POSITION, new ArrayList<>());
        model.addAttribute(COUNCILROUTER, councilRouter);
        return COUNCILROUTER_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final CouncilRouter councilRouter, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {

        councilRouterService.validateCouncilRouter(councilRouter, errors);
        if (errors.hasErrors()) {
            prepareNewForm(model);
            model.addAttribute(POSITION, positionMasterService.getPositionsByDepartment(councilRouter.getDepartment().getId()));
            return COUNCILROUTER_NEW;
        }
        councilRouterService.create(councilRouter);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilRouter.success", null, null));
        return "redirect:/councilrouter/result/".concat(councilRouter.getId().toString());
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, final Model model) {
        final CouncilRouter councilRouter = councilRouterService.findById(id);
        model.addAttribute(COUNCILROUTER, councilRouter);
        return COUNCILROUTER_RESULT;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final CouncilRouter councilRouter, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        councilRouterService.validateCouncilRouter(councilRouter, errors);
        if (errors.hasErrors()) {
            prepareNewForm(model);
            model.addAttribute(POSITION, positionMasterService.getPositionsByDepartment(councilRouter.getDepartment().getId()));
            return COUNCILROUTER_EDIT;
        }

        councilRouterService.update(councilRouter);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.councilRouter.update.success", null, null));
        return "redirect:/councilrouter/result/".concat(councilRouter.getId().toString());
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, Model model) {
        CouncilRouter councilRouter = councilRouterService.findById(id);
        prepareNewForm(model);
        model.addAttribute(COUNCILROUTER, councilRouter);

        return COUNCILROUTER_VIEW;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, Model model) {
        CouncilRouter councilRouter = new CouncilRouter();
        prepareNewForm(model);
        model.addAttribute(COUNCILROUTER, councilRouter);
        return COUNCILROUTER_SEARCH;

    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model) {
        final CouncilRouter councilRouter = councilRouterService.findById(id);
        prepareNewForm(model);
        model.addAttribute(POSITION, positionMasterService.getPositionsByDepartment(councilRouter.getDepartment().getId()));
        model.addAttribute(COUNCILROUTER, councilRouter);
        return COUNCILROUTER_EDIT;
    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") final String mode, Model model, 
            @ModelAttribute final CouncilRouter councilRouter) {
        List<CouncilRouter> searchResultList = councilRouterService.search(councilRouter);
        return new StringBuilder("{ \"data\":")
                .append(toJSON(searchResultList, CouncilRouter.class, CouncilRouterJsonAdaptor.class)).append("}")
                .toString();
    }

}