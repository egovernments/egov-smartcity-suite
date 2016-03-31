package org.egov.egf.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.commons.dao.ChartOfAccountsDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FunctionDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.service.AccountdetailtypeService;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.contra.TransactionSummary;
import org.egov.model.contra.TransactionSummaryDto;
import org.egov.model.service.TransactionSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private FundHibernateDAO fundHibernateDAO;
    @Autowired
    private ChartOfAccountsDAO chartOfAccountsDAO;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private FunctionDAO functionDAO;
    @Autowired
    private EmployeeService employeeService;

    private void prepareNewForm(Model model) {
        model.addAttribute("accountdetailtypes", accountdetailtypeService.findAll());
        model.addAttribute("cFinancialYears", financialYearDAO.getAllActivePostingFinancialYear());
        model.addAttribute("funds", fundHibernateDAO.findAllActiveFunds());
        model.addAttribute("cChartOfAccountss",
                chartOfAccountsDAO.findAll());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("cFunctions", functionDAO.getAllActiveFunctions());
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newForm(final Model model) {
        prepareNewForm(model);
        model.addAttribute("transactionSummaryDto", new TransactionSummaryDto());
        return TRANSACTIONSUMMARY_NEW;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> create(
            @ModelAttribute final TransactionSummaryDto transactionSummaryDto,
            final BindingResult errors, final Model model,
            final RedirectAttributes redirectAttrs, HttpServletResponse response) {
        List<TransactionSummary> transactionSummaries = new ArrayList<TransactionSummary>();
        transactionSummaries = removeEmptyRows(transactionSummaryDto.getTransactionSummaryList());
        try {
            for (TransactionSummary ts : transactionSummaries) {
                TransactionSummary transactionSummary = null;
                if (ts.getId() != null) {
                    transactionSummary = transactionSummaryService.findOne(ts.getId());
                } else {
                    transactionSummary = new TransactionSummary();
                }
                if (ts.getId() == null && ts.getGlcodeid() == null) {
                    // Ignore ts and move to next
                }
                else if (ts.getId() != null && ts.getGlcodeid() == null) {
                    // delete this transaction
                    transactionSummaryService.delete(transactionSummary);
                }
                else {
                    transactionSummary.setDepartmentid(departmentService.getDepartmentById(transactionSummaryDto
                            .getDepartmentid()
                            .getId()));
                    transactionSummary.setDivisionid(transactionSummaryDto.getDivisionid());
                    transactionSummary.setFinancialyear(financialYearDAO.getFinancialYearById(transactionSummaryDto
                            .getFinancialyear().getId()));

                    transactionSummary.setFunctionid((CFunction) persistenceService.find("from CFunction where id=?",
                            transactionSummaryDto.getFunctionid().getId()));
                    transactionSummary.setFund((Fund) fundHibernateDAO.fundById(transactionSummaryDto.getFund().getId(), false));

                    transactionSummary.setAccountdetailkey(ts.getAccountdetailkey());
                    if (ts.getAccountdetailtype() != null && ts.getAccountdetailtype().getId() != null)
                        transactionSummary.setAccountdetailtype(accountdetailtypeService.findOne(ts.getAccountdetailtype()
                                .getId()));
                    else
                        transactionSummary.setAccountdetailtype(null);

                    transactionSummary.setGlcodeid((CChartOfAccounts) chartOfAccountsDAO.getCChartOfAccountsByGlCode(ts
                            .getGlcodeDetail()));
                    transactionSummary.setNarration(ts.getNarration());
                    transactionSummary.setOpeningcreditbalance(ts.getOpeningcreditbalance() == null ? BigDecimal.ZERO : ts
                            .getOpeningcreditbalance());
                    transactionSummary.setOpeningdebitbalance(ts.getOpeningdebitbalance() == null ? BigDecimal.ZERO : ts
                            .getOpeningdebitbalance());
                    transactionSummary = transactionSummaryService.create(transactionSummary);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    private List<TransactionSummary> removeEmptyRows(List<TransactionSummary> transactionSummaries) {

        List<TransactionSummary> tempTransactionSummaries = new ArrayList<TransactionSummary>();
        for (TransactionSummary transactionSummary : transactionSummaries)
            if (transactionSummaries.size() != (tempTransactionSummaries.size() + 1))
                tempTransactionSummaries.add(transactionSummary);

        /**
         * Checking last row : if glcode is not there then delete row . else keep the row
         **/
        if (transactionSummaries.get(transactionSummaries.size() - 1).getGlcodeDetail() != null
                && transactionSummaries.get(transactionSummaries.size() - 1).getGlcodeDetail() != "")
            tempTransactionSummaries.add(transactionSummaries.get(transactionSummaries.size() - 1));
        return tempTransactionSummaries;
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
            @ModelAttribute final TransactionSummary transactionSummary,
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
        List<CChartOfAccounts> accounts = chartOfAccountsDAO.findByType(type);
        return accounts;
    }

    @RequestMapping(value = "/ajax/getMinorHeads", method = RequestMethod.GET)
    public @ResponseBody List<CChartOfAccounts> getMinorHeads(@RequestParam("majorCode") String majorCode,
            @RequestParam("classification") Long classification) {
        List<CChartOfAccounts> accounts = chartOfAccountsDAO
                .findByMajorCodeAndClassification(majorCode, classification);
        return accounts;
    }

    @RequestMapping(value = "/ajax/getAccounts", method = RequestMethod.GET)
    public @ResponseBody List<CChartOfAccounts> getAccounts(
            @RequestParam("term") String glcode, @RequestParam("majorCode") String majorCode,
            @RequestParam("classification") Long classification) {
        List<CChartOfAccounts> accounts = null;
        if (majorCode != null) {
            accounts = chartOfAccountsDAO
                    .findByGlcodeLikeIgnoreCaseAndClassificationAndMajorCode(glcode + "%", classification, majorCode);
        }
        else {
            accounts = chartOfAccountsDAO
                    .findByGlcodeLikeIgnoreCaseAndClassification(glcode + "%", classification);
        }
        /*
         * for(CChartOfAccounts account : accounts) { for(CChartOfAccountDetail detail : account.getChartOfAccountDetails()){
         * System.out.println(account.getGlcode() + " : " + detail.getDetailTypeId().getName()); } }
         */
        return accounts;
    }

    @RequestMapping(value = "/ajax/getAccountDetailTypes", method = RequestMethod.GET)
    public @ResponseBody List<Accountdetailtype> getAccountDetailTypes(@RequestParam("id") Long id) {
        CChartOfAccounts account = (CChartOfAccounts) chartOfAccountsDAO.findById(id.intValue(), false);
        List<Accountdetailtype> detailTypes = new ArrayList<Accountdetailtype>();
        for (CChartOfAccountDetail detail : account.getChartOfAccountDetails()) {
            detailTypes.add(detail.getDetailTypeId());
        }
        return detailTypes;
    }

    @RequestMapping(value = "/ajax/searchTransactionSummariesForNonSubledger", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, String>> searchTransactionSummariesForNonSubledger(
            @RequestParam("finYear") Long finYear, @RequestParam("fund") Long fund,
            @RequestParam("functn") Long functn, @RequestParam("department") Long department,
            @RequestParam("glcodeId") Long glcodeId) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<String, String> amountsMap = new HashMap<String, String>();

        List<TransactionSummary> transactionSummaries = transactionSummaryService
                .searchTransactionsForNonSubledger(finYear, fund, functn, department, glcodeId);
        for (TransactionSummary ts : transactionSummaries) {
            amountsMap.put("tsid", ts.getId().toString());
            amountsMap.put("openingdebitbalance", ts.getOpeningdebitbalance().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            amountsMap.put("openingcreditbalance", ts.getOpeningcreditbalance().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            amountsMap.put("narration", ts.getNarration());
            result.add(amountsMap);

        }

        return result;
    }

    @RequestMapping(value = "/ajax/searchTransactionSummariesForSubledger", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, String>> searchTransactionSummariesForSubledger(
            @RequestParam("finYear") Long finYear, @RequestParam("fund") Long fund,
            @RequestParam("functn") Long functn, @RequestParam("department") Long department,
            @RequestParam("glcodeId") Long glcodeId, @RequestParam("accountDetailTypeId") Integer accountDetailTypeId,
            @RequestParam("accountDetailKeyId") Integer accountDetailKeyId) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<String, String> amountsMap = new HashMap<String, String>();

        List<TransactionSummary> transactionSummaries = transactionSummaryService
                .searchTransactionsForSubledger(finYear, fund, functn, department, glcodeId, accountDetailTypeId,
                        accountDetailKeyId);
        for (TransactionSummary ts : transactionSummaries) {
            amountsMap.put("tsid", ts.getId().toString());
            amountsMap.put("openingdebitbalance", ts.getOpeningdebitbalance().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            amountsMap.put("openingcreditbalance", ts.getOpeningcreditbalance().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            amountsMap.put("narration", ts.getNarration());
            result.add(amountsMap);

        }

        return result;
    }

    @RequestMapping(value = "/ajax/deleteTransaction", method = RequestMethod.GET)
    public @ResponseBody String deleteTransaction(@RequestParam("id") Long id) {

        if (id != null) {
            TransactionSummary ts = transactionSummaryService.findOne(id);
            transactionSummaryService.delete(ts);
        }

        return "success";
    }

    @RequestMapping(value = "/ajax/getTransactionSummary", method = RequestMethod.GET)
    public @ResponseBody TransactionSummary getTransactionSummary(@RequestParam("glcodeid") Long glcodeId,
            @RequestParam("accountdetailtypeid") Long accountDetailTypeId,
            @RequestParam("accountdetailkey") Integer accountDetailKey) {
        TransactionSummary ts = null;
        if (glcodeId != null && accountDetailTypeId != null && accountDetailKey != null) {
            ts = transactionSummaryService.getTransactionSummary(glcodeId, accountDetailTypeId, accountDetailKey);
        }
        return ts;
    }
}
