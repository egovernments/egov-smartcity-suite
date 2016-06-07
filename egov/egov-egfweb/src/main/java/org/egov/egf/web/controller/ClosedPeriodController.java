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

package org.egov.egf.web.controller;

import java.util.List;

import org.egov.commons.ClosedPeriod;
import org.egov.commons.service.CFinancialYearService;
import org.egov.egf.web.adaptor.ClosedPeriodJsonAdaptor;
import org.egov.services.closeperiod.ClosedPeriodService;
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

@Controller
@RequestMapping("/closedperiod")
public class ClosedPeriodController {
	private final static String CLOSEDPERIOD_RESULT = "closedperiod-result";
	private final static String CLOSEDPERIOD_EDIT = "closedperiod-edit";
	private final static String CLOSEDPERIOD_SEARCH = "closedperiod-search";
	@Autowired
	private ClosedPeriodService closedPeriodService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CFinancialYearService cFinancialYearService;

	private void prepareNewForm(Model model) {
		model.addAttribute("cFinancialYears", cFinancialYearService.findAll());
	}


	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") final Long id, Model model) {
		ClosedPeriod closedPeriod = closedPeriodService.findOne(id);

		prepareNewForm(model);
		model.addAttribute("closedPeriod", closedPeriod);
		return CLOSEDPERIOD_EDIT;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute final ClosedPeriod closedPeriod,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return CLOSEDPERIOD_EDIT;
		}
		String result = null;
		List<ClosedPeriod> closePer = closedPeriodService.findAll();
		Long cId = closedPeriod.getcFinancialYearId().getId();
		if (closePer.size() != 0) {
			for (ClosedPeriod cp : closePer) {
				if (cp.getcFinancialYearId().getId() == closedPeriod
						.getcFinancialYearId().getId()
						&& closedPeriod.getIsClosed() == false) {
					closedPeriodService.delete(cp);

					redirectAttrs
							.addFlashAttribute("message", messageSource
									.getMessage("msg.closedPeriod.success",
											null, null));
					return "redirect:/closedperiod/result/" + cId;
				}

			}
		} else if (closePer.size() == 0 && closedPeriod.getIsClosed() == true) {
			closedPeriodService.update(closedPeriod);
			redirectAttrs.addFlashAttribute("message", messageSource
					.getMessage("msg.closedPeriod.success", null, null));
			return "redirect:/closedperiod/result/" + closedPeriod.getId();
		}

		if (closedPeriod.getcFinancialYearId().getIsClosed() == false
				&& closedPeriod.getIsClosed() == false) {
			redirectAttrs.addFlashAttribute("message", messageSource
					.getMessage("msg.closedPeriod.success", null, null));
			return "redirect:/closedperiod/result/" + cId;

		}

		closedPeriodService.update(closedPeriod);
		redirectAttrs.addFlashAttribute("message", messageSource.getMessage(
				"msg.closedPeriod.success", null, null));
		return "redirect:/closedperiod/result/" + closedPeriod.getId();

	}


	@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
	public String result(@PathVariable("id") final Long id, Model model) {
		ClosedPeriod closedPeriod = closedPeriodService.findOne(id);
		model.addAttribute("closedPeriod", closedPeriod);
		return CLOSEDPERIOD_RESULT;
	}

	@RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
	public String search(@PathVariable("mode") final String mode, Model model) {
		ClosedPeriod closedPeriod = new ClosedPeriod();
		prepareNewForm(model);
		model.addAttribute("closedPeriod", closedPeriod);
		return CLOSEDPERIOD_SEARCH;

	}

	@RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearch(
			@PathVariable("mode") final String mode, Model model,
			@ModelAttribute final ClosedPeriod closedPeriod) {
		List<ClosedPeriod> searchResultList = closedPeriodService
				.search(closedPeriod);

		String result = new StringBuilder("{ \"data\":")
				.append(toSearchResultJson(searchResultList)).append("}")
				.toString();
		return result;
	}

	public Object toSearchResultJson(final Object object) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(ClosedPeriod.class,
				new ClosedPeriodJsonAdaptor()).create();
		final String json = gson.toJson(object);
		return json;
	}
}