package org.egov.egf.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.commons.Fund; 
import org.egov.commons.service.FundService;
import org.egov.egf.web.adaptor.FundJsonAdaptor;
import org.egov.infra.admin.master.service.UserService;
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
@RequestMapping("/fund")
public class FundController {
	private final static String FUND_NEW = "fund-new";
	private final static String FUND_RESULT = "fund-result";
	private final static String FUND_EDIT = "fund-edit";
	private final static String FUND_VIEW = "fund-view";
	private final static String FUND_SEARCH = "fund-search";
	@Autowired
	private FundService fundService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserService userService;

	private void prepareNewForm(Model model) {
		model.addAttribute("funds", fundService.findByIsnotleaf());
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(final Model model) {
		prepareNewForm(model);
		model.addAttribute("fund", new Fund());
		return FUND_NEW;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute final Fund fund,final BindingResult errors, final Model model,final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return FUND_NEW; 
		}
		fundService.create(fund);
		redirectAttrs.addFlashAttribute("message",
				messageSource.getMessage("msg.fund.success", null, null));
		return "redirect:/fund/result/" + fund.getId();
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") final Integer id, Model model) {
		Fund fund = fundService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("fund", fund);
		return FUND_EDIT;  
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute final Fund fund,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return FUND_EDIT;
		}
		fundService.update(fund);
		redirectAttrs.addFlashAttribute("message",
				messageSource.getMessage("msg.fund.success", null, null));
		return "redirect:/fund/result/" + fund.getId();
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") final Integer id, Model model) {
		Fund fund = fundService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("fund", fund);
		return FUND_VIEW;
	}

	@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
	public String result(@PathVariable("id") final Integer id, Model model) {
		Fund fund = fundService.findOne(id);
		model.addAttribute("fund", fund);
		return FUND_RESULT;
	}

	@RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
	public String search(@PathVariable("mode") final String mode, Model model) {
		Fund fund = new Fund();
		prepareNewForm(model);
		model.addAttribute("fund", fund);
		return FUND_SEARCH;  

	}

	@RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearch(
			@PathVariable("mode") final String mode, Model model,
			@ModelAttribute final Fund fund) {
		List<Fund> searchResultList = fundService.search(fund);
		String result = new StringBuilder("{ \"data\":")
				.append(toSearchResultJson(searchResultList)).append("}")
				.toString();
		return result;
	}

	public Object toSearchResultJson(final Object object) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(Fund.class,
				new FundJsonAdaptor()).create();
		final String json = gson.toJson(object);
		return json;
	}
}