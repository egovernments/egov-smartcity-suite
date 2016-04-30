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