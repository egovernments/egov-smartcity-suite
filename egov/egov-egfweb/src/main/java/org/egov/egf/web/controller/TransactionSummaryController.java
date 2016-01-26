package org.egov.egf.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.model.contra.TransactionSummary;
import org.egov.model.contra.TransactionSummaryDto;
import org.egov.model.contra.TransactionSummarySearchDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transactionsummary")
public class TransactionSummaryController {
	private final static String TRANSACTIONSUMMARY_NEW = "transactionsummary-new";
	private final static String TRANSACTIONSUMMARY_RESULT = "transactionsummary-result";
	private final static String TRANSACTIONSUMMARY_EDIT = "transactionsummary-edit";
	private final static String TRANSACTIONSUMMARY_VIEW = "transactionsummary-view";
	@Autowired
	private TransactionSummaryService transactionSummaryService;
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
	@Autowired
	private EmployeeService employeeService;

	private void prepareNewForm(Model model) {
		model.addAttribute("accountdetailtypes",
				accountdetailtypeService.findAll());
		model.addAttribute("cFinancialYears", cFinancialYearService.findAll());
		model.addAttribute("fundsources", fundsourceService.findAll());
		model.addAttribute("funds", fundService.findAll());
		model.addAttribute("cChartOfAccountss",
				cChartOfAccountsService.findAll());
		model.addAttribute("departments", departmentService.getAllDepartments());
		model.addAttribute("functionarys", functionaryService.findAll());
		model.addAttribute("cFunctions", cFunctionService.findAll());
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(final Model model) {
		prepareNewForm(model);
		model.addAttribute("transactionSummaryDto", new TransactionSummaryDto());
		return TRANSACTIONSUMMARY_NEW;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody List<TransactionSummary> create(
			@Valid @ModelAttribute final TransactionSummaryDto transactionSummaryDto,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		List<TransactionSummary> transactionSummaries = new ArrayList<TransactionSummary>();
		for(TransactionSummary ts : transactionSummaryDto.getTransactionSummaryList()) {
			TransactionSummary transactionSummary = null;
			if(ts.getId() != null) {
				transactionSummary = transactionSummaryService.findOne(ts.getId());
			} else {
				transactionSummary = new TransactionSummary();
			}
			if(ts.getId() == null && ts.getGlcodeid() == null) {
				//Ignore ts and move to next
			}
			else if(ts.getId() != null && ts.getGlcodeid() == null) {
				//delete this transaction
				transactionSummaryService.delete(transactionSummary);
			}
			else {
				transactionSummary.setDepartmentid(departmentService.getDepartmentById(transactionSummaryDto.getDepartmentid().getId()));
				transactionSummary.setDivisionid(transactionSummaryDto.getDivisionid());
				transactionSummary.setFinancialyear(cFinancialYearService.findOne(transactionSummaryDto.getFinancialyear().getId()));
				if(transactionSummaryDto.getFunctionaryid().getId() != null)
					transactionSummary.setFunctionaryid(functionaryService.findOne(transactionSummaryDto.getFunctionaryid().getId()));
				transactionSummary.setFunctionid(cFunctionService.findOne(transactionSummaryDto.getFunctionid().getId()));
				transactionSummary.setFund(fundService.findOne(transactionSummaryDto.getFund().getId()));
				if(transactionSummaryDto.getFundsource().getId() != null)
					transactionSummary.setFundsource(fundsourceService.findOne(transactionSummaryDto.getFundsource().getId()));
				transactionSummary.setAccountdetailkey(ts.getAccountdetailkey());
				transactionSummary.setAccountdetailtype(accountdetailtypeService.findOne(ts.getAccountdetailtype().getId()));
				transactionSummary.setGlcodeid(cChartOfAccountsService.findOne(ts.getGlcodeid().getId()));
				transactionSummary.setNarration(ts.getNarration());
				transactionSummary.setOpeningcreditbalance(ts.getOpeningcreditbalance() == null ? BigDecimal.ZERO : ts.getOpeningcreditbalance());
				transactionSummary.setOpeningdebitbalance(ts.getOpeningdebitbalance() == null ? BigDecimal.ZERO : ts.getOpeningdebitbalance());
				transactionSummary = transactionSummaryService.create(transactionSummary);
				transactionSummaries.add(transactionSummary);
			}
		}
		redirectAttrs.addFlashAttribute("message",
				"msg.transactionSummary.success");
		return transactionSummaries;
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") final Long id, Model model) {
		TransactionSummary transactionSummary = transactionSummaryService
				.findOne(id);
		prepareNewForm(model);
		model.addAttribute("transactionSummary", transactionSummary);
		return TRANSACTIONSUMMARY_EDIT;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(
			@Valid @ModelAttribute final TransactionSummary transactionSummary,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return TRANSACTIONSUMMARY_EDIT;
		}
		transactionSummaryService.update(transactionSummary);
		redirectAttrs.addFlashAttribute("message",
				"msg.transactionSummary.success");
		return "redirect:/transactionsummary/result/"
				+ transactionSummary.getId();
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") final Long id, Model model) {
		TransactionSummary transactionSummary = transactionSummaryService
				.findOne(id);
		prepareNewForm(model);
		model.addAttribute("transactionSummary", transactionSummary);
		return TRANSACTIONSUMMARY_VIEW;
	}

	@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
	public String result(@PathVariable("id") final Long id, Model model) {
		TransactionSummary transactionSummary = transactionSummaryService
				.findOne(id);
		model.addAttribute("transactionSummary", transactionSummary);
		return TRANSACTIONSUMMARY_RESULT;
	}

	@RequestMapping(value = "/ajax/getMajorHeads", method = RequestMethod.GET)
	public @ResponseBody List<CChartOfAccounts> getMajorHeads(
			@RequestParam("type") Character type) {
		List<CChartOfAccounts> accounts = cChartOfAccountsService
				.findByType(type);
		return accounts;
	}

	@RequestMapping(value = "/ajax/getMinorHeads", method = RequestMethod.GET)
	public @ResponseBody List<CChartOfAccounts> getMinorHeads(
			@RequestParam("majorCode") String majorCode,
			@RequestParam("classification") Long classification) {
		List<CChartOfAccounts> accounts = cChartOfAccountsService
				.findByMajorCodeAndClassification(majorCode, classification);
		return accounts;
	}
	
	@RequestMapping(value = "/ajax/getAccounts", method = RequestMethod.GET)
	public @ResponseBody List<CChartOfAccounts> getAccounts(
			@RequestParam("term") String glcode, @RequestParam("majorCode") String majorCode,
			@RequestParam("classification") Long classification) {
		List<CChartOfAccounts> accounts = null;
		if(majorCode != null) {
			accounts = cChartOfAccountsService
					.findByGlcodeLikeIgnoreCaseAndClassificationAndMajorCode(glcode + "%", classification, majorCode);
		}
		else {
			accounts = cChartOfAccountsService
					.findByGlcodeLikeIgnoreCaseAndClassification(glcode + "%", classification);
		}
		/*for(CChartOfAccounts account : accounts) {
			for(CChartOfAccountDetail detail : account.getChartOfAccountDetails()){
				System.out.println(account.getGlcode() + " : " + detail.getDetailTypeId().getName());
			}
		}*/
		return accounts;
	}
	
	@RequestMapping(value = "/ajax/getAccountDetailTypes", method = RequestMethod.GET)
	public @ResponseBody List<Accountdetailtype> getAccountDetailTypes(@RequestParam("id") Long id) {
		CChartOfAccounts account = cChartOfAccountsService.findOne(id);
		List<Accountdetailtype> detailTypes = new ArrayList<Accountdetailtype>();
		for(CChartOfAccountDetail detail : account.getChartOfAccountDetails()) {
			detailTypes.add(detail.getDetailTypeId());
		}
		return detailTypes;
	}
	
	@RequestMapping(value = "/ajax/searchTransactionSummaries", method = RequestMethod.GET)
	public @ResponseBody List<TransactionSummarySearchDto> searchTransactionSummaries(
			@RequestParam("finYear") Long finYear, @RequestParam("fund") Long fund,
			@RequestParam("functn") Long functn, @RequestParam("department") Long department) {
		
		List<TransactionSummarySearchDto> searchDtos = new ArrayList<TransactionSummarySearchDto>();
		
		List<TransactionSummary> transactionSummaries = transactionSummaryService
				.searchTransactions(finYear, fund, functn, department);
		
		for(TransactionSummary ts : transactionSummaries) {
			TransactionSummarySearchDto dto = new TransactionSummarySearchDto();
			CChartOfAccounts account = cChartOfAccountsService.findOne(ts.getGlcodeid().getId());
			Employee employee = employeeService.getEmployeeById(ts.getAccountdetailkey().longValue());
			List<Accountdetailtype> detailTypes = new ArrayList<Accountdetailtype>();
			for(CChartOfAccountDetail detail : account.getChartOfAccountDetails()) {
				detailTypes.add(detail.getDetailTypeId());
			}
			dto.setTransactionSummary(ts);
			dto.setAccountdetailtypes(detailTypes);
			dto.setEntityCode(employee.getCode());
			searchDtos.add(dto);
		}
		
		return searchDtos;
	}
	
	@RequestMapping(value = "/ajax/deleteTransaction", method = RequestMethod.GET)
	public @ResponseBody String deleteTransaction(@RequestParam("id") Long id) {
		
		if(id != null){
			TransactionSummary ts = transactionSummaryService.findOne(id);
			transactionSummaryService.delete(ts);
		}
		
		return "success";
	}
	
	@RequestMapping(value = "/ajax/getTransactionSummary", method = RequestMethod.GET)
	public @ResponseBody TransactionSummary getTransactionSummary(@RequestParam("glcodeid") Long glcodeId,
			@RequestParam("accountdetailtypeid") Long accountDetailTypeId, @RequestParam("accountdetailkey") Integer accountDetailKey) {
		TransactionSummary ts = null;
		if(glcodeId != null && accountDetailTypeId != null && accountDetailKey != null){
			ts = transactionSummaryService.getTransactionSummary(glcodeId, accountDetailTypeId, accountDetailKey);
		}
		return ts;
	}
}
