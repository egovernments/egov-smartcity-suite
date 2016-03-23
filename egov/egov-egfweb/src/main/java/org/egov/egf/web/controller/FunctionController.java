package org.egov.egf.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.commons.CFunction;
import org.egov.commons.service.FunctionService;
import org.egov.egf.web.adaptor.FunctionJsonAdaptor;
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
@RequestMapping("/function")
public class FunctionController {
	private final static String FUNCTION_NEW = "function-new";
	private final static String FUNCTION_RESULT = "function-result";
	private final static String FUNCTION_EDIT = "function-edit";
	private final static String FUNCTION_VIEW = "function-view";
	private final static String FUNCTION_SEARCH = "function-search";
	@Autowired
	private FunctionService functionService;
	@Autowired
	private MessageSource messageSource;
	

	private void prepareNewForm(Model model) {
		model.addAttribute("functions", functionService.findAllIsNotLeafTrue());
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(final Model model) {
		prepareNewForm(model);
		model.addAttribute("CFunction", new CFunction());
		return FUNCTION_NEW;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute final CFunction function,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return FUNCTION_NEW;
		}
		if(function.getParentId()!=null && function.getParentId().getId()!=null )
			function.setParentId(functionService.findOne(function.getParentId().getId()));
		else
			function.setParentId(null);

		functionService.create(function);
		redirectAttrs.addFlashAttribute("message",
				messageSource.getMessage("msg.function.success", null, null));
		return "redirect:/function/result/" + function.getId();
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") final Long id, Model model) {
		CFunction function = functionService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("function", function);
		return FUNCTION_EDIT;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute final CFunction function,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return FUNCTION_EDIT;
		}
		functionService.update(function);
		redirectAttrs.addFlashAttribute("message",
				messageSource.getMessage("msg.function.success", null, null));
		return "redirect:/function/result/" + function.getId();
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") final Long id, Model model) {
		CFunction function = functionService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("function", function);
		return FUNCTION_VIEW;
	}

	@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
	public String result(@PathVariable("id") final Long id, Model model) {
		CFunction function = functionService.findOne(id);
		model.addAttribute("function", function);
		return FUNCTION_RESULT;
	}

	@RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
	public String search(@PathVariable("mode") final String mode, Model model) {
		CFunction function = new CFunction();
		prepareNewForm(model);
		model.addAttribute("function", function);
		return FUNCTION_SEARCH;

	}

	@RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearch(
			@PathVariable("mode") final String mode, Model model,
			@ModelAttribute final CFunction function) {
		List<CFunction> searchResultList = functionService.search(function);
		String result = new StringBuilder("{ \"data\":")
				.append(toSearchResultJson(searchResultList)).append("}")
				.toString();
		return result;
	}

	public Object toSearchResultJson(final Object object) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(CFunction.class,
				new FunctionJsonAdaptor()).create();
		final String json = gson.toJson(object);
		return json;
	}
}