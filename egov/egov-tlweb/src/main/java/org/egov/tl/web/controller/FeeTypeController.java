package org.egov.tl.web.controller;

import javax.validation.Valid;

import org.egov.tl.domain.entity.FeeType;
import org.egov.tl.domain.service.FeeTypeService;
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