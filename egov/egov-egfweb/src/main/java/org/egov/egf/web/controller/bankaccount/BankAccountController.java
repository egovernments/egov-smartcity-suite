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

import javax.validation.Valid;

import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.commons.service.FundService;
import org.egov.commons.utils.BankAccountType;
import org.egov.egf.commons.bank.service.CreateBankService;
import org.egov.egf.commons.bankaccount.service.CreateBankAccountService;
import org.egov.egf.commons.bankbranch.service.CreateBankBranchService;
import org.egov.egf.utils.FinancialUtils;
import org.egov.egf.web.controller.bankaccount.adaptor.BankAccountJsonAdaptor;
import org.egov.model.masters.AccountCodePurpose;
import org.egov.services.voucher.GeneralLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping("/bankaccount")
public class BankAccountController {

    private static final String BANKACCOUNT = "bankaccount";

    @Autowired
    private CreateBankAccountService createBankAccountService;

    @Autowired
    private CreateBankBranchService createBankBranchService;

    @Autowired
    private FundService fundService;

    @Autowired
    @Qualifier(value = "chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    @Qualifier(value = "generalLedgerService")
    private GeneralLedgerService generalLedgerService;

    @Autowired
    private FinancialUtils financialUtils;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CreateBankService createBankService;
    
    private void setDropDownValues(final Model model) {
        model.addAttribute("banks", createBankService.getByIsActiveTrueOrderByName());
        model.addAttribute("bankbranches", createBankBranchService.getByIsActiveTrueOrderByBranchname());
        model.addAttribute("funds", fundService.getByIsActive(true));
        model.addAttribute("usagetypes", BankAccountType.values());
        model.addAttribute("accounttypes", chartOfAccountsService.getAccountTypes());
        model.addAttribute("autoglcode", createBankAccountService.autoBankAccountGLCodeEnabled());
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        setDropDownValues(model);
        model.addAttribute(BANKACCOUNT, new Bankaccount());
        return "bankaccount-new";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model) {
        final Bankaccount bankaccount = createBankAccountService.getById(id);
        setDropDownValues(model);
        model.addAttribute("autoglcode", true);
        model.addAttribute(BANKACCOUNT, bankaccount);
        return "bankaccount-update";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, final Model model) {
        final Bankaccount bankaccount = createBankAccountService.getById(id);
        model.addAttribute(BANKACCOUNT, bankaccount);
        return "bankaccount-view";
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final Bankaccount bankaccount = new Bankaccount();
        setDropDownValues(model);
        model.addAttribute(BANKACCOUNT, bankaccount);
        return "bankaccount-search";

    }

    @RequestMapping(value = "/success/{id}", method = RequestMethod.GET)
    public String success(@PathVariable("id") final Long id, final Model model) {
        final Bankaccount bankaccount = createBankAccountService.getById(id);
        model.addAttribute(BANKACCOUNT, bankaccount);
        return "bankaccount-success";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final Bankaccount bankaccount, final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs) {
        if (!createBankAccountService.autoBankAccountGLCodeEnabled())
            validateGlCode(bankaccount.getChartofaccounts().getGlcode(), errors);
        if (errors.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute(BANKACCOUNT, bankaccount);
            return "bankaccount-new";
        }
        createBankAccountService.create(bankaccount);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.bankaccount.success", null, null));
        return "redirect:/bankaccount/success/" + bankaccount.getId();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final Bankaccount bankaccount, final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("autoglcode", true);
            model.addAttribute(BANKACCOUNT, bankaccount);
            return "bankaccount-update";
        }
        createBankAccountService.update(bankaccount);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.bankaccount.success", null, null));
        return "redirect:/bankaccount/success/" + bankaccount.getId();
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

    private void validateGlCode(final String glCode, final BindingResult errors) {
        final CChartOfAccounts coa = chartOfAccountsService.getByGlCode(glCode);
        Bankaccount account;
        AccountCodePurpose purpose;
        if (coa == null)
            errors.reject("msg.glcode.does.not.exist", new String[] {}, null);
        if (glCode != null) {
            final List<CGeneralLedger> glList = generalLedgerService.getGeneralLedgerByGlCode(glCode);
            if (glList != null && !glList.isEmpty())
                errors.reject("msg.transaction.already.exist.for.given.glcode", new String[] {}, null);

        }
        if (coa != null) {
            account = createBankAccountService.getByGlcode(glCode);
            if (account != null)
                errors.reject("msg.glcode.already.mapped", new String[] { account.getAccountnumber() }, null);
        }
        if (coa != null && !coa.getIsActiveForPosting())
            errors.reject("msg.glcode.not.active.for.posting", new String[] {}, null);
        if (coa != null && coa.getChartOfAccountDetails() != null
                && !coa.getChartOfAccountDetails().isEmpty())
            errors.reject("msg.glcode.should.not.be.control.code", new String[] {}, null);
        if (coa != null && coa.getType() != null && coa.getType().compareTo('A') != 0)
            errors.reject("msg.glcode.should.be.of.type.assets", new String[] {}, null);
        if (coa != null && coa.getPurposeId() == null)
            errors.reject("msg.glcode.is.not.mapped.with.any.purpose", new String[] {}, null);
        if (coa != null && coa.getPurposeId() != null) {
            purpose = financialUtils.getAccountCodePurposeById(coa.getPurposeId());
            if (purpose != null && !purpose.getName().contains("Bank Account Codes"))
                errors.reject("msg.glcode.should.be.of.purpose.bank.account.codes", new String[] {}, null);
        }

    }

}