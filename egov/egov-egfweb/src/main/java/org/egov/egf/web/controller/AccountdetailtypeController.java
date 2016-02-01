package org.egov.egf.web.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.service.AccountdetailtypeService;
import org.egov.egf.web.adaptor.AccountdetailtypeJsonAdaptor;
import org.egov.infra.security.utils.SecurityUtils;
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
@RequestMapping("/accountdetailtype")
public class AccountdetailtypeController {
	private final static String ACCOUNTDETAILTYPE_NEW = "accountdetailtype-new";
	private final static String ACCOUNTDETAILTYPE_RESULT = "accountdetailtype-result";
	private final static String ACCOUNTDETAILTYPE_EDIT = "accountdetailtype-edit";
	private final static String ACCOUNTDETAILTYPE_VIEW = "accountdetailtype-view";
	private final static String ACCOUNTDETAILTYPE_SEARCH = "accountdetailtype-search";
	@Autowired
	private AccountdetailtypeService accountdetailtypeService;
	@Autowired
	private SecurityUtils securityUtils;
	@Autowired  
    private MessageSource messageSource;

	
	private void prepareNewForm(Model model) {

	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(final Model model) {
		prepareNewForm(model);
		model.addAttribute("accountdetailtype", new Accountdetailtype());
		return ACCOUNTDETAILTYPE_NEW;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@ModelAttribute final Accountdetailtype accountdetailtype,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return ACCOUNTDETAILTYPE_NEW;
		}
		accountdetailtype.setTablename("accountEntityMaster");
		accountdetailtype.setAttributename(accountdetailtype.getName()+"_id");
		accountdetailtype.setNbroflevels(BigDecimal.ONE);
		accountdetailtype.setColumnname("id");
		accountdetailtype.setFullQualifiedName("org.egov.masters.model.AccountEntity");
		accountdetailtype.setCreated(new Date());
		accountdetailtype.setLastmodified(new Date());
		accountdetailtype.setModifiedby(securityUtils.getCurrentUser().getId());
		accountdetailtypeService.create(accountdetailtype);
		redirectAttrs.addFlashAttribute("message",messageSource.getMessage("msg.accountdetailtype.success", null, null));
		return "redirect:/accountdetailtype/result/"+ accountdetailtype.getId();
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") final Integer id, Model model) {
		Accountdetailtype accountdetailtype = accountdetailtypeService
				.findOne(id);
		prepareNewForm(model);
		model.addAttribute("accountdetailtype", accountdetailtype);
		return ACCOUNTDETAILTYPE_EDIT;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update( @ModelAttribute final Accountdetailtype accountdetailtype,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return ACCOUNTDETAILTYPE_EDIT;
		}
		accountdetailtype.setTablename("accountEntityMaster");
		accountdetailtype.setAttributename(accountdetailtype.getName()+"_id");
		accountdetailtype.setNbroflevels(BigDecimal.ONE);
		accountdetailtype.setColumnname("id");
		accountdetailtype.setFullQualifiedName("org.egov.masters.model.AccountEntity");
		//accountdetailtype.setCreated(new Date());
		accountdetailtype.setLastmodified(new Date());
		accountdetailtype.setModifiedby(securityUtils.getCurrentUser().getId());
		accountdetailtypeService.update(accountdetailtype);
		redirectAttrs.addFlashAttribute("message",messageSource.getMessage("msg.accountdetailtype.success", null, Locale.ENGLISH));
		return "redirect:/accountdetailtype/result/"
		+ accountdetailtype.getId();
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") final Integer id, Model model) {
		Accountdetailtype accountdetailtype = accountdetailtypeService
				.findOne(id);
		prepareNewForm(model);
		model.addAttribute("accountdetailtype", accountdetailtype);
		return ACCOUNTDETAILTYPE_VIEW;
	}

	@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
	public String result(@PathVariable("id") final Integer id, Model model) {
		Accountdetailtype accountdetailtype = accountdetailtypeService
				.findOne(id);
		model.addAttribute("accountdetailtype", accountdetailtype);
		return ACCOUNTDETAILTYPE_RESULT;
	}

	@RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
	public String search(@PathVariable("mode") final String mode, Model model) {
		Accountdetailtype accountdetailtype = new Accountdetailtype();
		prepareNewForm(model);
		model.addAttribute("accountdetailtype", accountdetailtype);
		return ACCOUNTDETAILTYPE_SEARCH;

	}

	@RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearch(
			@PathVariable("mode") final String mode, Model model,
			@ModelAttribute final Accountdetailtype accountdetailtype) {
		List<Accountdetailtype> searchResultList = accountdetailtypeService
				.search(accountdetailtype);
		String result = new StringBuilder("{ \"data\":")
		.append(toSearchResultJson(searchResultList)).append("}")
		.toString();
		return result;
	}

	public Object toSearchResultJson(final Object object) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(
				Accountdetailtype.class, new AccountdetailtypeJsonAdaptor())
				.create();
		final String json = gson.toJson(object);
		return json;
	}
}