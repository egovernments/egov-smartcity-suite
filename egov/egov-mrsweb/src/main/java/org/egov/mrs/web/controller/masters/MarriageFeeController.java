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

package org.egov.mrs.web.controller.masters;

import static org.egov.infra.utils.JsonUtils.toJSON;

import java.util.List;

import javax.validation.Valid;

import org.egov.mrs.domain.enums.MarriageFeeCriteriaType;
import org.egov.mrs.masters.entity.MarriageFee;
import org.egov.mrs.masters.service.MarriageFeeService;
import org.egov.mrs.web.adaptor.FeeJsonAdaptor;
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
@RequestMapping(value = "/masters")
public class MarriageFeeController {
    private static final String MARRIAGE_FEE = "marriageFee";
    private static final String MRG_FEE_CREATE = "fee-create";
    private static final String MRG_FEE_SUCCESS = "fee-success";
    private static final String MRG_FEE_SEARCH = "fee-search";
    private static final String MRG_FEE_UPDATE = "fee-update";
    private static final String MRG_FEE_VIEW = "fee-view";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MarriageFeeService marriageFeeService;

    @RequestMapping(value = "/fee/create", method = RequestMethod.GET)
    public String loadCreateForm(final Model model) {
        model.addAttribute(MARRIAGE_FEE, new MarriageFee());
        return MRG_FEE_CREATE;
    }

    @RequestMapping(value = "/fee/create", method = RequestMethod.POST)
    public String createFee(@Valid @ModelAttribute final MarriageFee marriageFee,
            final BindingResult errors,
            final RedirectAttributes redirectAttributes) {

        if (errors.hasErrors())
            return MRG_FEE_CREATE;
        marriageFee.setFeeType(MarriageFeeCriteriaType.GENERAL);
        marriageFeeService.create(marriageFee);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("msg.fee.create.success", null, null));
        return "redirect:/masters/fee/success/" + marriageFee.getId();

    }

    @RequestMapping(value = "/fee/success/{id}", method = RequestMethod.GET)
    public String viewFee(@PathVariable final Long id, final Model model) {
        model.addAttribute(MARRIAGE_FEE, marriageFeeService.getFee(id));
        return MRG_FEE_SUCCESS;
    }

    @RequestMapping(value = "/fee/view/{id}", method = RequestMethod.GET)
    public String viewRegistrationunit(@PathVariable("id") final Long id,
            final Model model) {
        final MarriageFee marriageFee = marriageFeeService.getFee(id);
        model.addAttribute(MARRIAGE_FEE, marriageFee);
        return MRG_FEE_VIEW;
    }

    @RequestMapping(value = "/fee/search/{mode}", method = RequestMethod.GET)
    public String getSearchPage(@PathVariable("mode") final String mode,
            final Model model) {
        model.addAttribute("fee", new MarriageFee());
        return MRG_FEE_SEARCH;
    }

    @RequestMapping(value = "/fee/searchResult/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchFeeResult(
            @PathVariable("mode") final String mode, final Model model,
            @ModelAttribute final MarriageFee fee) {

        List<MarriageFee> searchResultList;
        if ("edit".equalsIgnoreCase(mode))
            searchResultList = marriageFeeService
            .searchRegistrationFeesWithGeneralType(fee);
        else
            searchResultList = marriageFeeService.searchFee(fee);
        return new StringBuilder("{ \"data\":").append(toJSON(searchResultList, MarriageFee.class, FeeJsonAdaptor.class))
                .append("}").toString();
    }

    @RequestMapping(value = "/fee/edit/{id}", method = RequestMethod.GET)
    public String editFee(@PathVariable("id") final Long id, final Model model) {
        model.addAttribute(MARRIAGE_FEE, marriageFeeService.getFee(id));
        return MRG_FEE_UPDATE;
    }

    @RequestMapping(value = "/fee/update", method = RequestMethod.POST)
    public String updateFee(
            @Valid @ModelAttribute final MarriageFee marriageFee,
            final BindingResult errors,
            final RedirectAttributes redirectAttributes) {
        if (errors.hasErrors())
            return MRG_FEE_UPDATE;
        marriageFeeService.update(marriageFee);
        redirectAttributes.addFlashAttribute("message",
                messageSource.getMessage("msg.fee.update.success", null, null));
        return "redirect:/masters/fee/success/" + marriageFee.getId();
    }
}
