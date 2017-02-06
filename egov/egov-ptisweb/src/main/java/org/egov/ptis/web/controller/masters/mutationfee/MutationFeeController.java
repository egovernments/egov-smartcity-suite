/*
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~ accountability and the service delivery of the government  organizations.
  ~
  ~  Copyright (C) 2016  eGovernments Foundation
  ~
  ~  The updated version of eGov suite of products as by eGovernments Foundation
  ~  is available at http://www.egovernments.org
  ~
  ~  This program is free software: you can redistribute it and/or modify
  ~  it under the terms of the GNU General Public License as published by
  ~  the Free Software Foundation, either version 3 of the License, or
  ~  any later version.
  ~
  ~  This program is distributed in the hope that it will be useful,
  ~  but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~  GNU General Public License for more details.
  ~
  ~  You should have received a copy of the GNU General Public License
  ~  along with this program. If not, see http://www.gnu.org/licenses/ or
  ~  http://www.gnu.org/licenses/gpl.html .
  ~
  ~  In addition to the terms of the GPL license to be adhered to in using this
  ~  program, the following additional terms are to be complied with:
  ~
  ~      1) All versions of this program, verbatim or modified must carry this
  ~         Legal Notice.
  ~
  ~      2) Any misrepresentation of the origin of the material is prohibited. It
  ~         is required that all modified versions of this material be marked in
  ~         reasonable ways as different from the original version.
  ~
  ~      3) This license does not grant any rights to any user of the program
  ~         with regards to rights under trademark law for use of the trade names
  ~         or trademarks of eGovernments Foundation.
  ~
  ~  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.

*/
package org.egov.ptis.web.controller.masters.mutationfee;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.ptis.domain.model.MutationFeeDetails;
import org.egov.ptis.master.service.MutationFeeService;
import org.joda.time.DateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/mutationfee")
public class MutationFeeController {
    private static final String MUTATION_FORM = "add-mutation-fee-form";
    private static final String MSG = "message";
    private static final String ERROR = "error";
    private final MutationFeeService mutationFeeService;

    @Autowired
    public MutationFeeController(final MutationFeeService mutationFeeService) {
        this.mutationFeeService = mutationFeeService;
    }

    @ModelAttribute
    public MutationFeeDetails mutationFeeDetailsModel() {
        return new MutationFeeDetails();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@ModelAttribute final MutationFeeDetails mutationFeeDetails, final Model model) {
        return MUTATION_FORM;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute final MutationFeeDetails mutationFeeDetails,
            final BindingResult resultBinder, @RequestParam("fromDate") final Date fromDate,
            final RedirectAttributes redirectAttributes, final HttpServletRequest request, final Model model) {

        mutationFeeService.generateSlabName(mutationFeeDetails);
        if (mutationFeeService.findExistingSlabName(mutationFeeDetails.getSlabName())) {
            if (mutationFeeService.getToDateBySlabName(mutationFeeDetails.getSlabName())) {
                return validateDateForSlabName(mutationFeeDetails, resultBinder, fromDate, redirectAttributes);
            } else {
                resultBinder.reject("error.mutationfee.datevalidation.fail", "error.mutationfee.datevalidation.fail");
                return MUTATION_FORM;
            }
        } else {

            if (mutationFeeService.validateForHighLimitNull()) {
                resultBinder.reject("error.mutationfee.add.fail", "error.mutationfee.add.fail");
                return MUTATION_FORM;

            } else if (!mutationFeeService.validateForMaxHighLimit(mutationFeeDetails.getLowLimit())) {
                final BigDecimal high = mutationFeeService.getMaxHighLimit().add(BigDecimal.ONE);
                resultBinder.reject("error.mutationfee.fail", new String[] { high.toString() }, "error.mutationfee.fail");
                return MUTATION_FORM;
            } else {
                mutationFeeService.createMutationFee(mutationFeeDetails);
                redirectAttributes.addFlashAttribute(MSG, "msg.mutationfee.create.success");
                return "redirect:/mutationfee/create";
            }
        }
    }
    
    public String validateDateForSlabName(@ModelAttribute final MutationFeeDetails mutationFeeDetails,
            final BindingResult resultBinder, @RequestParam("fromDate") final Date fromDate,
            final RedirectAttributes redirectAttributes) {
        if (fromDate.compareTo(mutationFeeService.getLatestToDateForSlabName(mutationFeeDetails.getSlabName())) >= 0) {
            mutationFeeService.createMutationFee(mutationFeeDetails);
            redirectAttributes.addFlashAttribute(MSG, "msg.mutationfee.create.success");
            return "redirect:/mutationfee/create";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(mutationFeeService.getLatestToDateForSlabName(mutationFeeDetails.getSlabName()));
            cal.add(Calendar.DATE, 1);
            resultBinder.reject("error.mutationfee.fromdatevalidation.fail", new String[] { cal.getTime().toString() },
                    "error.mutationfee.fromdatevalidation.fail");
            return MUTATION_FORM;
        }
    }

    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    public String searchMutationFee(final Model model) {
        final List<MutationFeeDetails> slabNames = mutationFeeService.findSlabName();
        model.addAttribute("slabNames", slabNames);
        model.addAttribute("mutationFeeDetails", new MutationFeeDetails());
        return "search-mutation-fee-form";
    }

    @RequestMapping(value = "/modify/{slabName}", method = RequestMethod.GET)
    public String modifyMutationFee(@PathVariable final String slabName,
            @ModelAttribute final MutationFeeDetails mutationFeeDetails,
            final RedirectAttributes redirectAttributes, final Model model) {
        final List<MutationFeeDetails> mutationfee = mutationFeeService.findDuplicateSlabName(slabName);
        model.addAttribute("mutationfee", mutationfee);
        model.addAttribute("mutationFeeDetails", new MutationFeeDetails());
        return "modify-mutation-fee-form";
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.POST)
    public String modifyMutationFee(@PathVariable("id") final Long id, @RequestParam("toDate") final Date toDate,
            @ModelAttribute final MutationFeeDetails mutationFeeDetails, final RedirectAttributes redirectAttributes,
            final BindingResult resultBinder, final Model model) {
        final MutationFeeDetails mfd = mutationFeeService.getDetailsById(id);
        final Date date = new Date();
        if (DateTimeComparator.getDateOnlyInstance().compare(toDate, date) >= 0) {
            mfd.setToDate(toDate);
            mutationFeeService.createMutationFee(mfd);
            redirectAttributes.addFlashAttribute(MSG, "msg.mutationfee.update.success");
            return "redirect:/mutationfee/modify";
        } else {
            redirectAttributes.addFlashAttribute(ERROR, "error.mutationfee.update.fail");
            return "redirect:/mutationfee/modify";
        }
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String showMutationFee(@ModelAttribute final MutationFeeDetails mutationFee, final Model model) {
        final List<MutationFeeDetails> mutationfee = mutationFeeService.getAllMutationFee();
        model.addAttribute("mutationfee", mutationfee);
        return "view-mutation-fee-form";
    }
}
