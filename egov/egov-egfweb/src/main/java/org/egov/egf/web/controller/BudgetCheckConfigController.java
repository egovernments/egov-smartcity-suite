package org.egov.egf.web.controller;


import java.util.List;

import javax.validation.Valid;

import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.model.BudgetControlType.BudgetCheckOption;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.egf.model.ClosedPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller 
@RequestMapping("/budgetcheckconfig")
public class BudgetCheckConfigController {
	private final static String BUDGETCHECKCONFIG_NEW="budgetcheckconfig-new";
	private final static String BUDGETCHECKCONFIG_RESULT="budgetcheckconfig-result";
	
	@Autowired
	private  BudgetControlTypeService budgetCheckConfigService;
	@Autowired 
	private MessageSource messageSource;
	
	private BudgetControlType prepareNewForm(Model model) {
		List<BudgetControlType> configs = budgetCheckConfigService.findAll();
		if(configs.size()>0)
		{
			return configs.get(0);
		}else
		{
			return  new BudgetControlType();
		}
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(final Model model){
		//prepareNewForm(model);
		model.addAttribute("budgetControlType", prepareNewForm(model) );
		model.addAttribute("configoptions",BudgetCheckOption.values());
		return BUDGETCHECKCONFIG_NEW;
	}

	
	

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute final BudgetControlType budgetControlType,final BindingResult errors,final Model model,final RedirectAttributes redirectAttrs){
		if (errors.hasErrors()){
			prepareNewForm(model);
			return BUDGETCHECKCONFIG_NEW;
		}
		budgetCheckConfigService.update(budgetControlType);
		redirectAttrs.addFlashAttribute("message", messageSource.getMessage("msg.budgetcheckconfig.success",null,null));
		return "redirect:/budgetcheckconfig/result/"+budgetControlType.getId();
	}
	
	
	@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
	public String result(@PathVariable("id") final Long id, Model model) {
		BudgetControlType budgetControlType = budgetCheckConfigService.findOne(id);
		model.addAttribute("budgetControlType", budgetControlType);
		return BUDGETCHECKCONFIG_RESULT;
	}

}