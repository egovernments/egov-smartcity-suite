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

package org.egov.egf.web.controller.bankbranch;

import java.util.List;

import javax.validation.Valid;

import org.egov.commons.Bankbranch;
import org.egov.egf.commons.bank.service.CreateBankService;
import org.egov.egf.commons.bankbranch.service.CreateBankBranchService;
import org.egov.egf.web.controller.bankbranch.adaptor.BankBranchJsonAdaptor;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author venki
 *
 */

@Controller
@RequestMapping("/bankbranch")
public class BankBranchController {

    private static final String BANKBRANCH = "bankbranch";

    @Autowired
    private CreateBankBranchService createBankBranchService;

    @Autowired
    private CreateBankService createBankService;

    @Autowired
    private MessageSource messageSource;

    private void setDropDownValues(final Model model) {
        model.addAttribute("banks", createBankService.getByIsActiveTrueOrderByName());
        model.addAttribute("bankbranches", createBankBranchService.getByIsActiveTrueOrderByBranchname());
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        setDropDownValues(model);
        model.addAttribute(BANKBRANCH, new Bankbranch());
        return "bankbranch-new";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Integer id, final Model model) {
        final Bankbranch bankbranch = createBankBranchService.getById(id);
        setDropDownValues(model);
        model.addAttribute(BANKBRANCH, bankbranch);
        return "bankbranch-update";
    }

    @RequestMapping(value = "/success/{id}", method = RequestMethod.GET)
    public String success(@PathVariable("id") final Integer id, final Model model) {
        final Bankbranch bankbranch = createBankBranchService.getById(id);
        model.addAttribute(BANKBRANCH, bankbranch);
        return "bankbranch-success";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Integer id, final Model model) {
        final Bankbranch bankbranch = createBankBranchService.getById(id);
        model.addAttribute(BANKBRANCH, bankbranch);
        return "bankbranch-view";
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final Bankbranch bankbranch = new Bankbranch();
        setDropDownValues(model);
        model.addAttribute(BANKBRANCH, bankbranch);
        return "bankbranch-search";

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final Bankbranch bankbranch, final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            final Model model2 = model;
            setDropDownValues(model2);
            model.addAttribute(BANKBRANCH, bankbranch);
            return "bankbranch-new";
        }
        createBankBranchService.create(bankbranch);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.bankbranch.success", null, null));
        return "redirect:/bankbranch/success/" + bankbranch.getId();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final Bankbranch bankbranch, final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute(BANKBRANCH, bankbranch);
            return "bankbranch-update";
        }
        createBankBranchService.update(bankbranch);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.bankbranch.success", null, null));
        return "redirect:/bankbranch/success/" + bankbranch.getId();
    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") final String mode, final Model model,
            @ModelAttribute final Bankbranch bankbranch) {
        final List<Bankbranch> searchResultList = createBankBranchService.search(bankbranch);
        return new StringBuilder("{ \"data\":")
                .append(toSearchResultJson(searchResultList)).append("}")
                .toString();
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Bankbranch.class, new BankBranchJsonAdaptor()).create();
        return gson.toJson(object);
    }

}