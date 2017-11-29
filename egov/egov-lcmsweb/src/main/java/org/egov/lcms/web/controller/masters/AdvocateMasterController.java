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
package org.egov.lcms.web.controller.masters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.egov.commons.Bankbranch;
import org.egov.commons.service.BankBranchService;
import org.egov.lcms.masters.entity.AdvocateMaster;
import org.egov.lcms.masters.service.AdvocateMasterService;
import org.egov.lcms.transactions.service.LegalCaseSmsService;
import org.egov.lcms.web.adaptor.AdvocateMasterJsonAdaptor;
import org.egov.services.masters.BankService;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/advocatemaster")
public class AdvocateMasterController {
    private final static String ADVOCATEMASTER_NEW = "advocatemaster-new";
    private final static String ADVOCATEMASTER_RESULT = "advocatemaster-result";
    private final static String ADVOCATEMASTER_EDIT = "advocatemaster-edit";
    private final static String ADVOCATEMASTER_VIEW = "advocatemaster-view";
    private final static String ADVOCATEMASTER_SEARCH = "advocatemaster-search";
    @Autowired
    @Qualifier("advocateMasterService")
    private AdvocateMasterService advocateMasterService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    @Qualifier("bankService")
    private BankService bankService;
    @Autowired
    @Qualifier("bankBranchService")
    private BankBranchService bankBranchService;

    @Autowired
    private LegalCaseSmsService legalCaseSmsService;

    private void prepareNewForm(final Model model) {
        model.addAttribute("banks", bankService.findAll());
        model.addAttribute("bankbranchs", Collections.EMPTY_LIST);
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        model.addAttribute("advocateMaster", new AdvocateMaster());
        model.addAttribute("mode", "create");
        return ADVOCATEMASTER_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final AdvocateMaster advocateMaster, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return ADVOCATEMASTER_NEW;
        }
        if (advocateMaster.getBankName() != null && advocateMaster.getBankName().getId() != null)
            advocateMaster.setBankName(bankService.findById(advocateMaster.getBankName().getId(), false));
        else
            advocateMaster.setBankName(null);
        if (advocateMaster.getBankBranch() != null && advocateMaster.getBankBranch().getId() != null)
            advocateMaster.setBankBranch(bankBranchService.findById(advocateMaster.getBankBranch().getId(), false));
        else
            advocateMaster.setBankBranch(null);
        advocateMasterService.persist(advocateMaster);
        model.addAttribute("mode", "create");
        advocateMasterService.createAccountDetailKey(advocateMaster);
        advocateMasterService.createAdvocateUser(advocateMaster);
        legalCaseSmsService.sendSmsAndEmailForAdvocate(advocateMaster);
        return "redirect:/advocatemaster/result/" + advocateMaster.getId() + ",create";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") final Long id, final Model model) {
        final AdvocateMaster advocateMaster = advocateMasterService.findOne(id);
        model.addAttribute("mode", "edit");

        if (advocateMaster.getBankName() != null && advocateMaster.getBankBranch() != null) {
            final Bankbranch bankbranch = bankBranchService.findById(advocateMaster.getBankBranch().getId(), false);
            final List<Bankbranch> bankbranchList = new ArrayList<Bankbranch>();
            bankbranchList.add(bankbranch);
            advocateMaster.setBankBranch(bankbranch);
            model.addAttribute("bankbranchlist", bankbranchList);
        }
        prepareNewForm(model);
        model.addAttribute("advocateMaster", advocateMaster);
        return ADVOCATEMASTER_EDIT;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final AdvocateMaster advocateMaster, final BindingResult errors,
            final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return ADVOCATEMASTER_EDIT;
        }
        if (advocateMaster.getBankName() != null && advocateMaster.getBankName().getId() != null)
            advocateMaster.setBankName(bankService.findById(advocateMaster.getBankName().getId(), false));
        else
            advocateMaster.setBankName(null);
        if (advocateMaster.getBankBranch() != null && advocateMaster.getBankBranch().getId() != null)
            advocateMaster.setBankBranch(bankBranchService.findById(advocateMaster.getBankBranch().getId(), false));
        else
            advocateMaster.setBankBranch(null);

        advocateMasterService.persist(advocateMaster);
        model.addAttribute("mode", "edit");
        advocateMasterService.createAdvocateUser(advocateMaster);
        redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.advocateMaster.update", null, null));
        return "redirect:/advocatemaster/result/" + advocateMaster.getId() + ",edit";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") final Long id, final Model model) {
        final AdvocateMaster advocateMaster = advocateMasterService.findOne(id);
        prepareNewForm(model);
        model.addAttribute("advocateMaster", advocateMaster);
        return ADVOCATEMASTER_VIEW;
    }

    @RequestMapping(value = "/result/{id},{mode}", method = RequestMethod.GET)
    public String result(@PathVariable("id") final Long id, final Model model,@PathVariable("mode") final String mode) {
        final AdvocateMaster advocateMaster = advocateMasterService.findOne(id);
        model.addAttribute("mode", mode);
        model.addAttribute("advocateMaster", advocateMaster);
        return ADVOCATEMASTER_RESULT;
    }

    public @ModelAttribute("paymentModeList") List<String> modeOfPaymentList() {
        final List<String> paymentModeList = new ArrayList<String>();
        paymentModeList.add("Cash");
        paymentModeList.add("Cheque");
        paymentModeList.add("RTGS");
        return paymentModeList;
    }

    @RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
    public String search(@PathVariable("mode") final String mode, final Model model) {
        final AdvocateMaster advocateMaster = new AdvocateMaster();
        prepareNewForm(model);
        model.addAttribute("advocateMaster", advocateMaster);
        return ADVOCATEMASTER_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, final Model model,
            @ModelAttribute final AdvocateMaster advocateMaster) {
        final List<AdvocateMaster> searchResultList = advocateMasterService.search(advocateMaster);
        final String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}")
                .toString();
        return result;
    }

    public Object toSearchResultJson(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(AdvocateMaster.class, new AdvocateMasterJsonAdaptor())
                .create();
        final String json = gson.toJson(object);
        return json;
    }
}