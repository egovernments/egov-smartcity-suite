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

package org.egov.tl.web.controller;

import javax.validation.Valid;

import org.egov.tl.entity.FeeType;
import org.egov.tl.service.FeeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/feeType")
public class FeeTypeController {
	private final static String FEETYPE_NEW = "feetype-new";
	private final static String FEETYPE_RESULT = "feetype-result";
	private final static String FEETYPE_EDIT = "feetype-edit";
	private final static String FEETYPE_VIEW = "feetype-view";
	@Autowired
	private FeeTypeService feeTypeService;

	private void prepareForNewForm(Model model) {
		model.addAttribute("feeProcessTypes", FeeType.FeeProcessType.values());
	}

	@RequestMapping(value = "new", method = RequestMethod.GET)
	public String newForm(final Model model) {
		return FEETYPE_NEW;
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute final FeeType feeType,
			final BindingResult errors, final Model model) {
		if (errors.hasErrors())
			return FEETYPE_RESULT;
		feeTypeService.create(feeType);
		return FEETYPE_RESULT;
	}

	@RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
	public String edit(@PathVariable("id") final String id) {
		return FEETYPE_EDIT;
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute final FeeType feeType,
			final BindingResult errors, final Model model) {
		if (errors.hasErrors())
			return FEETYPE_RESULT;
		feeTypeService.update(feeType);
		return FEETYPE_RESULT;
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.POST)
	public String view(@PathVariable("id") final String id) {
		return FEETYPE_VIEW;
	}

}