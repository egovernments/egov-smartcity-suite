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

package org.egov.egf.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.egov.commons.service.AccountEntityService;
import org.egov.commons.service.AccountdetailtypeService;
import org.egov.egf.web.adaptor.AccountEntityJsonAdaptor;
import org.egov.masters.model.AccountEntity;
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

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/accountentity")
public class AccountEntityController {
	private final static String ACCOUNTENTITY_NEW = "accountentity-new";
	private final static String ACCOUNTENTITY_RESULT = "accountentity-result";
	private final static String ACCOUNTENTITY_EDIT = "accountentity-edit";
	private final static String ACCOUNTENTITY_VIEW = "accountentity-view";
	private final static String ACCOUNTENTITY_SEARCH = "accountentity-search";
	@Autowired
	private AccountEntityService accountEntityService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private AccountdetailtypeService accountdetailtypeService;

	private void prepareNewForm(Model model) {
		model.addAttribute("accountdetailtypes",accountdetailtypeService.findByFullQualifiedName("org.egov.masters.model.AccountEntity"));

	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(final Model model) {
		prepareNewForm(model);
		model.addAttribute("accountEntity", new AccountEntity());
		return ACCOUNTENTITY_NEW;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(
			@Valid @ModelAttribute final AccountEntity accountEntity,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return ACCOUNTENTITY_NEW;
		}
/*		if(accountEntity.getAccountdetailtype()!=null && accountEntity.getAccountdetailtype().getId()!=null)
		{
			accountEntity.setAccountdetailtype(accountdetailtypeService.findOne(accountEntity.getAccountdetailtype().getId()));
		}
*/		accountEntityService.create(accountEntity);
		redirectAttrs.addFlashAttribute("message", messageSource.getMessage(
				"msg.accountentity.success", null, null));
		return "redirect:/accountentity/result/" + accountEntity.getId();
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") final Integer id, Model model) {
		AccountEntity accountEntity = accountEntityService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("accountEntity", accountEntity);
		return ACCOUNTENTITY_EDIT;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(
			@Valid @ModelAttribute final AccountEntity accountEntity,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return ACCOUNTENTITY_EDIT;
		}
		
		accountEntityService.update(accountEntity);
		redirectAttrs.addFlashAttribute("message", messageSource.getMessage(
				"msg.accountentity.success", null, null));
		return "redirect:/accountentity/result/" + accountEntity.getId();
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") final Integer id, Model model) {
		AccountEntity accountEntity = accountEntityService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("accountEntity", accountEntity);
		return ACCOUNTENTITY_VIEW;
	}

	@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
	public String result(@PathVariable("id") final Integer id, Model model) {
		AccountEntity accountEntity = accountEntityService.findOne(id);
		model.addAttribute("accountEntity", accountEntity);
		return ACCOUNTENTITY_RESULT;
	}

	@RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
	public String search(@PathVariable("mode") final String mode, Model model) {
		AccountEntity accountEntity = new AccountEntity();
		prepareNewForm(model);
		model.addAttribute("accountEntity", accountEntity);
		return ACCOUNTENTITY_SEARCH;

	}

	@RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearch(
			@PathVariable("mode") final String mode, Model model,
			@ModelAttribute final AccountEntity accountEntity) {
		List<AccountEntity> searchResultList = accountEntityService.search(accountEntity);
		String result = new StringBuilder("{ \"data\":")
				.append(toSearchResultJson(searchResultList)).append("}")
				.toString();
		return result;
	}

	public Object toSearchResultJson(final Object object) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(AccountEntity.class,
				new AccountEntityJsonAdaptor()).create();
		final String json = gson.toJson(object);
		return json;
	}
}