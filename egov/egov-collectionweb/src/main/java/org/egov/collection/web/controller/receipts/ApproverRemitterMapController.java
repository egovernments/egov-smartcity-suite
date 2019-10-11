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
package org.egov.collection.web.controller.receipts;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.egov.collection.entity.ApproverRemitterMapping;
import org.egov.collection.service.ApproverRemitterMapService;
import org.egov.collection.service.ApproverRemitterMapService.ApproverRemitterSpec;
import org.egov.collection.service.ApproverRemitterMapService.ApproverType;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.infra.admin.master.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/receipts/approverRemitterMapping")
public class ApproverRemitterMapController {

    private static final String VIEW_ERROR = "approverRemitterMap-error";
    private static final String VIEW_UPDATE = "approverRemitterMap-update";
    private static final String VIEW_INDEX = "approverRemitterMap-view";
    private static final String ATTR_MODE = "mode";
    private static final String ATTR_SUCCESS_MSG = "successMsg";
    private static final String ATTR_MAP_SPEC = "mapspec";
    private static final String ATTR_MAP_LIST = "maplist";
    private static final String ATTR_ACTIVE_APPROVER_LIST = "activeApproverList";
    private static final String ATTR_UNMAPPED_APPROVER_LIST = "unmappedApproverList";
    private static final String ATTR_REMITTER_LIST = "remitterList";

    // For using in messageSource.getMessage's mandatory arg
    private static final String[] EMPTY_ARGS = {};

    enum Mode {
        VIEW, MODIFY, CREATE;
    }

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ApproverRemitterMapService approverRemitterService;

    @Autowired
    private CollectionsUtil collectionsUtil;

    private void populateViewModel(final Model model, ApproverRemitterSpec approverRemitterSpec) {
        model.addAttribute(ATTR_MAP_SPEC, approverRemitterSpec);
        Map<ApproverType, Set<User>> approverMap = approverRemitterService.getCollectionAprovers();
        model.addAttribute(ATTR_ACTIVE_APPROVER_LIST, approverMap.get(ApproverType.ACTIVELY_MAPPED));
        model.addAttribute(ATTR_UNMAPPED_APPROVER_LIST, approverMap.get(ApproverType.UNMAPPED));
        model.addAttribute(ATTR_REMITTER_LIST, collectionsUtil.getConfiguredRemitters());
        model.addAttribute(ATTR_MAP_LIST, Collections.emptyList());
    }

    @GetMapping("/view")
    public String view(final Model model) {
        populateViewModel(model, new ApproverRemitterSpec());
        model.addAttribute(ATTR_MODE, Mode.VIEW);
        return VIEW_INDEX;
    }

    @PostMapping("/view")
    public String view(
            @ModelAttribute(ATTR_MAP_SPEC) ApproverRemitterSpec searchSpec,
            BindingResult bindingResult, Model model) {
        populateViewModel(model, searchSpec);
        model.addAttribute(ATTR_MODE, Mode.VIEW);
        if (!bindingResult.hasErrors())
            model.addAttribute(ATTR_MAP_LIST, approverRemitterService.searchMappingBySpec(searchSpec));
        return VIEW_INDEX;
    }

    @GetMapping("/modify")
    public String modify(Model model) {
        populateViewModel(model, new ApproverRemitterSpec());
        model.addAttribute(ATTR_MODE, Mode.MODIFY);
        return VIEW_INDEX;
    }

    @PostMapping("/modify")
    public String modify(
            @ModelAttribute(ATTR_MAP_SPEC) ApproverRemitterSpec searchSpec,
            BindingResult bindingResult, Model model) {
        this.view(searchSpec, bindingResult, model);
        model.addAttribute(ATTR_MODE, Mode.MODIFY);
        return VIEW_INDEX;
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("selectedId") Long id, Model model) {
        if (id != null || id > 0) {
            ApproverRemitterMapping mapping = approverRemitterService.findById(id);
            if (mapping == null)
                model.addAttribute("errors", messageSource.getMessage("mapping.404", EMPTY_ARGS, Locale.getDefault()));
            else
                populateViewModel(model, ApproverRemitterSpec.of(mapping));
            model.addAttribute(ATTR_MODE, Mode.MODIFY);
            return VIEW_UPDATE;
        }
        model.addAttribute(ATTR_MODE, Mode.MODIFY);
        model.addAttribute("errors", messageSource.getMessage("mapping.404", EMPTY_ARGS, Locale.getDefault()));
        populateViewModel(model, new ApproverRemitterSpec());
        return VIEW_INDEX;
    }

    @PostMapping("/edit")
    public String edit(
            @RequestParam("id") Long id,
            @Valid @ModelAttribute(ATTR_MAP_SPEC) ApproverRemitterSpec approverRemitterSpec,
            BindingResult bindingResult,
            Model model) {
        approverRemitterSpec.setId(id);
        populateViewModel(model, approverRemitterSpec);
        model.addAttribute(ATTR_MODE, Mode.MODIFY);
        if (!bindingResult.hasErrors() && approverRemitterService.validate(approverRemitterSpec, bindingResult)) {
            approverRemitterService.update(approverRemitterSpec);
            model.addAttribute(ATTR_SUCCESS_MSG,
                    messageSource.getMessage("msg.mapping.update.successful", EMPTY_ARGS, Locale.getDefault()));
        }
        return bindingResult.hasErrors() ? VIEW_UPDATE : VIEW_ERROR;
    }

    @GetMapping("/create")
    public String create(Model model) {
        populateViewModel(model, new ApproverRemitterSpec());
        model.addAttribute(ATTR_MODE, Mode.CREATE);
        return VIEW_UPDATE;
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute(ATTR_MAP_SPEC) ApproverRemitterSpec approverRemitterSpec,
            BindingResult bindingResult,
            Model model) {
        model.addAttribute(ATTR_MODE, Mode.CREATE);
        populateViewModel(model, approverRemitterSpec);
        if (!bindingResult.hasErrors() && approverRemitterService.validate(approverRemitterSpec, bindingResult)) {
            approverRemitterService.create(approverRemitterSpec);
            model.addAttribute(ATTR_SUCCESS_MSG,
                    messageSource.getMessage("msg.mapping.create.successful", EMPTY_ARGS, Locale.getDefault()));
        }
        return bindingResult.hasErrors() ? VIEW_UPDATE : VIEW_ERROR;
    }
}