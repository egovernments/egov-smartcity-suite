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

package org.egov.egf.web.controller.bankaccount;

import java.util.List;

import org.egov.commons.Bankaccount;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.commons.service.FundService;
import org.egov.commons.utils.BankAccountType;
import org.egov.egf.commons.bankaccount.service.CreateBankAccountService;
import org.egov.egf.commons.bankbranch.service.CreateBankBranchService;
import org.egov.egf.web.controller.bankaccount.adaptor.BankAccountJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author venki
 *
 */

@Controller
@RequestMapping("/bankaccount")
public class ViewBankAccountController {

    @Autowired
    private CreateBankBranchService createBankBranchService;

    @Autowired
    private CreateBankAccountService createBankAccountService;

    @Autowired
    private FundService fundService;

    @Autowired
    @Qualifier(value = "chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, final Model model) {
        final Bankaccount bankaccount = createBankAccountService.getById(id);
        model.addAttribute("bankaccount", bankaccount);
        return "bankaccount-view";
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final Bankaccount bankaccount = new Bankaccount();
        model.addAttribute("bankbranches", createBankBranchService.getByIsActive(true));
        model.addAttribute("funds", fundService.getByIsActive(true));
        model.addAttribute("usagetypes", BankAccountType.values());
        model.addAttribute("accounttypes", chartOfAccountsService.getAccountTypes());
        model.addAttribute("autoglcode", createBankAccountService.autoBankAccountGLCodeEnabled());
        model.addAttribute("bankaccount", bankaccount);
        return "bankaccount-search";

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ajaxsearch(@PathVariable("mode") final String mode, final Model model,
            @ModelAttribute final Bankaccount bankaccount) {
        final List<Bankaccount> searchResultList = createBankAccountService.search(bankaccount);
        return new StringBuilder("{ \"data\":")
                .append(toSearchResultJson(searchResultList)).append("}")
                .toString();
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(Bankaccount.class, new BankAccountJsonAdaptor()).create();
        return gson.toJson(object);
    }
}