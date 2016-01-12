package org.egov.egf.web.controller;


import javax.validation.Valid;

import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.model.contra.TransactionSummary;
import org.egov.model.service.TransactionSummaryService;
import org.egov.service.AccountdetailtypeService;
import org.egov.service.CChartOfAccountsService;
import org.egov.service.CFinancialYearService;
import org.egov.service.CFunctionService;
import org.egov.service.FunctionaryService;
import org.egov.service.FundService;
import org.egov.service.FundsourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller 
@RequestMapping("/transactionsummary")
 public class TransactionSummaryController {
private final static String TRANSACTIONSUMMARY_NEW="transactionsummary-new";
private final static String TRANSACTIONSUMMARY_RESULT="transactionsummary-result";
private final static String TRANSACTIONSUMMARY_EDIT="transactionsummary-edit";
private final static String TRANSACTIONSUMMARY_VIEW="transactionsummary-view";
@Autowired
	private  TransactionSummaryService transactionSummaryService;
	@Autowired
private AccountdetailtypeService accountdetailtypeService;
	@Autowired
private CFinancialYearService cFinancialYearService;
	@Autowired
private FundsourceService fundsourceService;
	@Autowired
private FundService fundService;
	@Autowired
private CChartOfAccountsService cChartOfAccountsService;
	@Autowired
private DepartmentService departmentService;
	@Autowired
private FunctionaryService functionaryService;
	@Autowired
private CFunctionService cFunctionService;
private void prepareNewForm(Model model) {
	model.addAttribute("accountdetailtypes",accountdetailtypeService.findAll());
	model.addAttribute("cFinancialYears",cFinancialYearService.findAll());
	model.addAttribute("fundsources",fundsourceService.findAll());
	model.addAttribute("funds",fundService.findAll());
	model.addAttribute("cChartOfAccountss",cChartOfAccountsService.findAll());
	model.addAttribute("departments",departmentService.getAllDepartments());
	model.addAttribute("functionarys",functionaryService.findAll());
	model.addAttribute("cFunctions",cFunctionService.findAll());
}

@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(final Model model){
prepareNewForm(model);
model.addAttribute("transactionSummary", new TransactionSummary() );
	return TRANSACTIONSUMMARY_NEW;
}

@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute final TransactionSummary transactionSummary,final BindingResult errors,final Model model,final RedirectAttributes redirectAttrs){
	if (errors.hasErrors()) {
prepareNewForm(model);
	return TRANSACTIONSUMMARY_NEW; }
transactionSummaryService.create(transactionSummary);
redirectAttrs.addFlashAttribute("message", "msg.transactionSummary.success");
return "redirect:/transactionsummary/result/"+transactionSummary.getId();
}

@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") final Long id,Model model){
TransactionSummary transactionSummary  = transactionSummaryService.findOne(id);prepareNewForm(model);
model.addAttribute("transactionSummary", transactionSummary);	return TRANSACTIONSUMMARY_EDIT;
}

@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute final TransactionSummary transactionSummary,final BindingResult errors,final Model model,final RedirectAttributes redirectAttrs){
	if (errors.hasErrors()){
prepareNewForm(model);return TRANSACTIONSUMMARY_EDIT;
}transactionSummaryService.update(transactionSummary);
redirectAttrs.addFlashAttribute("message", "msg.transactionSummary.success");
return "redirect:/transactionsummary/result/"+transactionSummary.getId();
}

@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") final Long id,Model model){
TransactionSummary transactionSummary  = transactionSummaryService.findOne(id);
prepareNewForm(model);
model.addAttribute("transactionSummary", transactionSummary);	return TRANSACTIONSUMMARY_VIEW;
}

@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
public String result(@PathVariable("id") final Long id,Model model){
TransactionSummary transactionSummary  = transactionSummaryService.findOne(id);model.addAttribute("transactionSummary", transactionSummary);
return TRANSACTIONSUMMARY_RESULT;}
}