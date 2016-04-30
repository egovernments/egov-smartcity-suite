package org.egov.egf.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.egov.commons.dao.FunctionDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.egf.web.adaptor.BudgetUploadReportJsonAdaptor;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetUploadReport;
import org.egov.model.service.BudgetUploadReportService;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/budgetuploadreport")
public class BudgetUploadReportController {
    private final static String BUDGETUPLOADREPORT_SEARCH = "budgetuploadreport-search";
    @Autowired
    private BudgetUploadReportService budgetUploadReportService;
    @Autowired
    @Qualifier("budgetService")
    private BudgetService budgetService;
    @Autowired
    private FundHibernateDAO fundDAO;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private FunctionDAO functionDAO;
    @Autowired
    @Qualifier("budgetDetailService")
    private BudgetDetailService budgetDetailService;

    private void prepareNewForm(Model model) {
        model.addAttribute("budgets", budgetService.getBudgetsForUploadReport());
        model.addAttribute("funds", fundDAO.findAllActiveIsLeafFunds());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("functions", functionDAO.getAllActiveFunctions());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model)
    {
        BudgetUploadReport budgetUploadReport = new BudgetUploadReport();
        prepareNewForm(model);
        model.addAttribute("budgetUploadReport", budgetUploadReport);
        return BUDGETUPLOADREPORT_SEARCH;

    }

    @RequestMapping(value = "/ajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String ajaxsearch(Model model, @ModelAttribute final BudgetUploadReport budgetUploadReport)
    {
        List<BudgetUploadReport> searchResultList = budgetUploadReportService.search(budgetUploadReport);
        String result = new StringBuilder("{ \"data\":").append(toSearchResultJson(searchResultList)).append("}").toString();
        return result;
    }

    @RequestMapping(value = "/ajax/getReferenceBudget", method = RequestMethod.GET)
    public @ResponseBody Budget getReferenceBudget(
            @RequestParam("budgetId") Long budgetId) {
        if (budgetId != null)
            return budgetService.getReferenceBudgetFor(budgetService.findById(budgetId, false));
        else
            return null;
    }

    public Object toSearchResultJson(final Object object)
    {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(BudgetUploadReport.class, new BudgetUploadReportJsonAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}