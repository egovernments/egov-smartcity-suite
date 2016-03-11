package org.egov.egf.web.controller;

import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetUploadReport;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/approvebudget")
public class ApproveBudgetController {
    private final static String APPROVEBUDGET_SEARCH = "approvebudget-search";
    @Autowired
    @Qualifier("budgetService")
    private BudgetService budgetService;
    @Autowired
    @Qualifier("budgetDetailService")
    private BudgetDetailService budgetDetailService;

    private void prepareNewForm(Model model) {
        model.addAttribute("budgets", budgetService.getBudgetsForUploadReport());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model)
    {
        BudgetUploadReport budgetUploadReport = new BudgetUploadReport();
        prepareNewForm(model);
        model.addAttribute("budgetUploadReport", budgetUploadReport);
        return APPROVEBUDGET_SEARCH;

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute final BudgetUploadReport budgetUploadReport, final BindingResult errors,
            final RedirectAttributes redirectAttrs) {
        Budget reBudget = budgetService.findById(budgetUploadReport.getReBudget().getId(), false);
        Budget beBudget = budgetService.getReferenceBudgetFor(reBudget);
        budgetService.updateByMaterializedPath(reBudget.getMaterializedPath());
        budgetDetailService.updateByMaterializedPath(reBudget.getMaterializedPath());
        budgetService.updateByMaterializedPath(beBudget.getMaterializedPath());
        budgetDetailService.updateByMaterializedPath(beBudget.getMaterializedPath());
        redirectAttrs.addFlashAttribute("message", "msg.uploaded.budget.success");

        return "redirect:/approvebudget/search";
    }

}