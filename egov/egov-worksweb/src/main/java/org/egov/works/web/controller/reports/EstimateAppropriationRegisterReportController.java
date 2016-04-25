package org.egov.works.web.controller.reports;

import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.works.reports.entity.EstimateAppropriationRegisterSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/reports/estimateappropriationregister")
public class EstimateAppropriationRegisterReportController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private FundHibernateDAO fundHibernateDAO;

    @Autowired
    private FunctionHibernateDAO functionHibernateDAO;

    @Autowired
    private BudgetGroupDAO budgetGroupDAO;

    @Autowired
    private FinancialYearDAO financialYearDAO;  
    
    @RequestMapping(value = "/searchform", method = RequestMethod.GET)
    public String showEstimateAppropriationRegister(
            @ModelAttribute final EstimateAppropriationRegisterSearchRequest estimateAppropriationRegisterSearchRequest,
            final Model model) throws ApplicationException {
        setDropDownValues(model);
        model.addAttribute("estimateAppropriationRegisterSearchRequest", estimateAppropriationRegisterSearchRequest);
        return "estimateAppropriationRegister-search";
    }

    private void setDropDownValues(final Model model) {
         model.addAttribute("funds", fundHibernateDAO.findAllActiveFunds());
         model.addAttribute("functions", functionHibernateDAO.getAllActiveFunctions());
         model.addAttribute("budgetHeads", budgetGroupDAO.getBudgetGroupList());
         model.addAttribute("financialYear", financialYearDAO.getAllActiveFinancialYearList());
         model.addAttribute("departments", departmentService.getAllDepartments());
         
    }

}
