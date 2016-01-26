package org.egov.tl.web.controller;


import java.util.List;

import javax.validation.Valid;

import org.egov.tl.entity.Validity;
import org.egov.tl.service.NatureOfBusinessService;
import org.egov.tl.service.ValidityService;
import org.egov.tl.service.masters.LicenseCategoryService;
import org.egov.tl.web.adaptor.ValidityJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/validity")
public class ValidityController {
	private final static String VALIDITY_NEW="validity-new";
	private final static String VALIDITY_RESULT="validity-result";
	private final static String VALIDITY_EDIT="validity-edit";
	private final static String VALIDITY_VIEW="validity-view";
	private final static String VALIDITY_SEARCH="validity-search";
	@Autowired
	private  ValidityService validityService;
	@Autowired
	private NatureOfBusinessService natureOfBusinessService;
	@Autowired
	private LicenseCategoryService licenseCategoryService;
	private void prepareNewForm(Model model) {
		model.addAttribute("natureOfBusinesss",natureOfBusinessService.findAll());
		model.addAttribute("licenseCategorys",licenseCategoryService.findAll());
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(final Model model){
		prepareNewForm(model);
		model.addAttribute("validity", new Validity() );
		return VALIDITY_NEW;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute final Validity validity,final BindingResult errors,final Model model,final RedirectAttributes redirectAttrs){
		if (errors.hasErrors())
		{
			prepareNewForm(model);
			return VALIDITY_NEW;
			}
		validityService.create(validity);
		redirectAttrs.addFlashAttribute("message", "msg.validity.success");
		return "redirect:/validity/result/"+validity.getId();
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") final Long id,Model model){
		Validity validity  = validityService.findOne(id);prepareNewForm(model);
		model.addAttribute("validity", validity);	return VALIDITY_EDIT;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute final Validity validity,final BindingResult errors,final Model model,final RedirectAttributes redirectAttrs){
		if (errors.hasErrors()){
			prepareNewForm(model);return VALIDITY_EDIT;
		}validityService.update(validity);
		redirectAttrs.addFlashAttribute("message", "msg.validity.success");
		return "redirect:/validity/result/"+validity.getId();
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") final Long id,Model model){
		Validity validity  = validityService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("validity", validity);	return VALIDITY_VIEW;
	}

	@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
	public String result(@PathVariable("id") final Long id,Model model){
		Validity validity  = validityService.findOne(id);model.addAttribute("validity", validity);
		return VALIDITY_RESULT;}

	@RequestMapping(value =  "/search/{mode}", method = RequestMethod.GET)
	public String search(@PathVariable("mode") final String  mode,Model model)
	{
		Validity validity  = new Validity();
		prepareNewForm(model);
		model.addAttribute("validity",validity);
		return VALIDITY_SEARCH;

	}

	@RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST,produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearch(@PathVariable("mode") final String mode, Model model,@ModelAttribute final Validity validity ) 
	{
		List<Validity> searchResultList = validityService.search(validity);
		String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}").toString();
		return result;
	}
	public Object toSearchResultJson(final Object object)
	{
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(Validity.class,new ValidityJsonAdaptor()).create();
		final String json = gson.toJson(object);
		return json;
	}
}