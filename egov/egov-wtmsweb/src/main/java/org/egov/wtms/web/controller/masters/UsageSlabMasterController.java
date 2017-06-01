/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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

package org.egov.wtms.web.controller.masters;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.egov.infra.web.support.ui.DataTable;
import org.egov.infstr.services.Page;
import org.egov.wtms.application.entity.UsageSlabSearchRequest;
import org.egov.wtms.masters.entity.UsageSlab;
import org.egov.wtms.masters.entity.UsageSlabJsonAdapter;
import org.egov.wtms.masters.service.MeteredRatesService;
import org.egov.wtms.masters.service.UsageSlabService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/masters")
public class UsageSlabMasterController {

    private static final String MODE_VALUE = "mode";
    private static final String USAGESLAB_SEARCH_REQUEST = "usageSlabSearchRequest";

    @Autowired
    private UsageSlabService usageSlabService;

    @Autowired
    private UsageTypeService usageTypeService;

    @Autowired
    private MeteredRatesService meteredRatesService;

    @RequestMapping(value = "/usageslab-create", method = GET)
    public String createUsageSlab(@ModelAttribute final UsageSlab usageSlab, final Model model) {
        model.addAttribute("usageType", usageTypeService.getActiveUsageTypes());
        return "usageslab-create";
    }

    @RequestMapping(value = "/usageslab-create", method = POST)
    public String saveUsageSlab(@ModelAttribute final UsageSlab usageSlab, final BindingResult bindingResult,
            final RedirectAttributes redirectAttrs, final Model model) {
        usageSlabService.save(usageSlab);
        model.addAttribute(MODE_VALUE, "create");
        model.addAttribute("usageSlab", usageSlab);
        model.addAttribute("message", "Usage Slab Created Successfully");
        return "usageslab-success";
    }

    @RequestMapping(value = "usageslab-view", method = GET)
    public String viewUsageSlab(@ModelAttribute(USAGESLAB_SEARCH_REQUEST) final UsageSlabSearchRequest usageSlabSearchRequest,
            final Model model) {
        model.addAttribute(MODE_VALUE, "view");
        model.addAttribute("usageType", usageTypeService.getActiveUsageTypes());
        model.addAttribute("slabNameList", usageSlabService.getActiveUsageSlabs());
        return "usageslab-search";
    }

    @RequestMapping(value = "/usageslab-view/{mode}", method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxSearchResult(final UsageSlabSearchRequest usageSlabSearchRequest,
            @PathVariable final String mode, final Model model) {
        final Page<UsageSlab> page = usageSlabService.search(usageSlabSearchRequest);
        final long draw = Long.valueOf(usageSlabSearchRequest.draw());
        return new DataTable<>(page, draw).toJson(UsageSlabJsonAdapter.class);
    }

    @RequestMapping(value = "/usageslab-edit", method = GET)
    public String searchForModify(@ModelAttribute final UsageSlabSearchRequest usageSlabSearchRequest, final Model model) {
        model.addAttribute(MODE_VALUE, "edit");
        model.addAttribute("usageType", usageTypeService.getActiveUsageTypes());
        model.addAttribute("slabNameList", usageSlabService.getActiveUsageSlabs());
        return "usageslab-search";
    }

    @RequestMapping(value = "/usageslab-edit/{id}", method = GET)
    public String modifyUsageSlabRecord(
            @ModelAttribute(USAGESLAB_SEARCH_REQUEST) final UsageSlabSearchRequest usageSlabSearchRequest,
            @PathVariable("id") final String id,
            final Model model) {
        UsageSlab existingUsageSlab = null;
        if (id != null)
            existingUsageSlab = usageSlabService.findById(Long.valueOf(id));
        model.addAttribute(MODE_VALUE, "edit");
        model.addAttribute(USAGESLAB_SEARCH_REQUEST, existingUsageSlab);
        return "usageslab-edit";
    }

    @RequestMapping(value = "/usageslab-edit/{id}", method = POST)
    public String updteUsageSlab(@ModelAttribute(USAGESLAB_SEARCH_REQUEST) final UsageSlabSearchRequest usageSlabSearchRequest,
            @PathVariable("id") final String id,
            final RedirectAttributes redirectAttrs, final Model model) {
        UsageSlab existingObject = null;
        if (id != null)
            existingObject = usageSlabService.findById(Long.valueOf(id));

        if (existingObject != null) {
            if (usageSlabSearchRequest.getUsage() != null)
                existingObject.setUsage(usageSlabSearchRequest.getUsage());
            if (usageSlabSearchRequest.getSlabName() != null)
                existingObject.setSlabName(usageSlabSearchRequest.getSlabName());
            if (usageSlabSearchRequest.getFromVolume() != null)
                existingObject.setFromVolume(usageSlabSearchRequest.getFromVolume());
            if (usageSlabSearchRequest.getToVolume() != null)
                existingObject.setToVolume(usageSlabSearchRequest.getToVolume());
            existingObject.setActive(usageSlabSearchRequest.isActive());
            usageSlabService.save(existingObject);
        }
        model.addAttribute("usageSlab", existingObject);
        model.addAttribute("message", "Usage Slab Updated Successfully");
        return "usageslab-success";
    }

    @RequestMapping(value = "/usageslab-overlap-ajax", method = GET)
    @ResponseBody
    public UsageSlab usageSlabOverlapCheck(@RequestParam final String usage, @RequestParam final String slabName,
            @RequestParam final Long fromVolume, @RequestParam final Long toVolume) {
        return usageSlabService.checkSlabOverlap(usage, fromVolume, toVolume);
    }

    @RequestMapping(value = "/usageslab-gap-ajax", method = GET)
    @ResponseBody
    public UsageSlab usageSlabGapCheck(@RequestParam final String usage, @RequestParam final String slabName,
            @RequestParam final Long fromVolume, @RequestParam final Long toVolume) {
        return usageSlabService.checkSlabGap(usage, fromVolume, toVolume);
    }

    @RequestMapping(value = "/usageslab-rate-exists-ajax", method = GET)
    @ResponseBody
    public Boolean usageSlabRateExistsCheck(@RequestParam final String slabName) {
        final UsageSlab slabNameVal = usageSlabService.findBySlabName(slabName);
        if (slabNameVal != null && slabNameVal.isActive())
            return meteredRatesService.isRateExists(slabName);
        else
            return false;
    }

}
