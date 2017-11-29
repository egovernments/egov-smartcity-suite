/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.egf.web.actions.report;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.egf.model.BudgetReportView;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.egov.utils.BudgetDetailHelper;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Results(value = {
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=BudgetReport.pdf" }),
                @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                        "application/xls", "contentDisposition", "no-cache;filename=BudgetReport.xls" })
})
@ParentPackage("egov")
public class BudgetReportAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = -4750341307051187L;
    ReportHelper reportHelper;
    private BudgetDetail budgetDetail = new BudgetDetail();
    private List<BudgetReportView> budgetDetailsList = new ArrayList<BudgetReportView>();
    private BudgetDetailService budgetDetailService;
    private List<Budget> budgetList = new ArrayList<Budget>();
    FinancialYearDAO financialYearDAO;
    private boolean canViewREApprovedAmount = false;
    private boolean canViewBEApprovedAmount = false;
    private BudgetService budgetService;
    BudgetDetailHelper budgetDetailHelper;
    private InputStream inputStream;
    private boolean showResults = false;
    private String currentYearRange = "";
    private String nextYearRange = "";
    private String lastYearRange = "";
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public void setBudgetDetailHelper(final BudgetDetailHelper budgetHelper) {
        budgetDetailHelper = budgetHelper;
    }

    public BudgetReportAction() {
        addRelatedEntity(Constants.BUDGET_GROUP, BudgetGroup.class);
        addRelatedEntity(Constants.EXECUTING_DEPARTMENT, Department.class);
        addRelatedEntity(Constants.BUDGET, Budget.class);
    }

    @Override
    public void prepare() {
        persistenceService.getSession().setDefaultReadOnly(true);
        persistenceService.getSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();
        setupDropdownsInHeader();
    }

    @Override
    public String execute() throws Exception {
        return "form";
    }

    @Action(value = "/report/budgetReport-ajaxLoadBudgets")
    public String ajaxLoadBudgets() {
        final String isbere = budgetDetail.getBudget().getIsbere();
        if (budgetDetail.getBudget() != null && budgetDetail.getBudget().getFinancialYear() != null && isbere != null) {
            final Long finYearId = budgetDetail.getBudget().getFinancialYear().getId();
            setBudgetList(getPersistenceService()
                    .findAllBy(
                            "from Budget where isbere=? and financialYear.id=? and isPrimaryBudget=1 "
                                    +
                                    "and isActiveBudget=1 and id not in (select parent from Budget where parent is not null and isbere=? and "
                                    +
                                    "financialYear.id=? and isPrimaryBudget=1) order by name", isbere, finYearId, isbere,
                                    finYearId));
        }
        return "budgets";
    }

    private void setupDropdownsInHeader() {
        setupDropdownDataExcluding(Constants.SUB_SCHEME);
        dropdownData.put("budgetGroupList", masterDataCache.get("egf-budgetGroup"));
        dropdownData.put("executingDepartmentList", masterDataCache.get("egi-department"));
        addDropdownData("financialYearList", budgetService.getFYForNonApprovedBudgets());
        final List<String> isbereList = new ArrayList<String>();
        isbereList.add("BE");
        isbereList.add("RE");
        dropdownData.put("isbereList", isbereList);
    }

    public boolean canViewApprovedAmount(final Budget budget) {
        return budgetDetailService.canViewApprovedAmount(persistenceService, budget);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    private Date getNextYearFor(final Date date) {
        final GregorianCalendar previousYearToDate = new GregorianCalendar();
        previousYearToDate.setTime(date);
        final int prevYear = previousYearToDate.get(Calendar.YEAR) + 1;
        previousYearToDate.set(Calendar.YEAR, prevYear);
        return previousYearToDate.getTime();
    }

    public String exportXls() throws Exception {
        generateReport();
        final JasperPrint jasper = reportHelper.generateBudgetReportJasperPrint(budgetDetailsList, getParamMap().get("heading")
                .toString(),
                canViewBEApprovedAmount, canViewREApprovedAmount, lastYearRange, currentYearRange, nextYearRange);
        inputStream = reportHelper.exportXls(inputStream, jasper);
        return "XLS";
    }

    Map<String, Object> getParamMap() {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        String budgetName = "";
        if (budgetDetail.getBudget() != null)
            budgetName = budgetDetail.getBudget().getName();
        paramMap.put("heading", "Budget Report For " + budgetName);
        paramMap.put("enableReApproved", Boolean.valueOf(canViewREApprovedAmount));
        paramMap.put("enableBeApproved", Boolean.valueOf(canViewBEApprovedAmount));
        return paramMap;
    }

    public String exportPdf() throws Exception {
        generateReport();
        final JasperPrint jasper = reportHelper.generateBudgetReportJasperPrint(budgetDetailsList, getParamMap().get("heading")
                .toString(),
                canViewBEApprovedAmount, canViewREApprovedAmount, lastYearRange, currentYearRange, nextYearRange);
        inputStream = reportHelper.exportPdf(inputStream, jasper);
        return "PDF";
    }

    @ValidationErrorPage(value = "form")
    public String generateReport() {
        showResults = true;
        final CFinancialYear finYear = budgetService.find("from Budget where id=?", budgetDetail.getBudget().getId())
                .getFinancialYear();
        List<BudgetDetail> currentYearBeList = new ArrayList<BudgetDetail>();
        List<BudgetDetail> nextYearBeList = new ArrayList<BudgetDetail>();
        List<BudgetDetail> lastYearBe = new ArrayList<BudgetDetail>();
        List<BudgetDetail> lastYearRe = new ArrayList<BudgetDetail>();
        final Budget b = budgetService.findById(budgetDetail.getBudget().getId(), false);
        if ("BE".equalsIgnoreCase(b.getIsbere())) {
            final CFinancialYear previousYear = budgetDetailHelper.getPreviousYearFor(finYear);
            if (previousYear != null) {
                lastYearBe = budgetDetailService.findAllBy(
                        "from BudgetDetail where budget.financialYear.id=? and budget.isPrimaryBudget=1 and " +
                                "budget.isActiveBudget=1 and budget.isbere='BE'", previousYear.getId());
                lastYearRe = budgetDetailService.findAllBy(
                        "from BudgetDetail where budget.financialYear.id=? and budget.isPrimaryBudget=1 and " +
                                "budget.isActiveBudget=1 and budget.isbere='RE'", previousYear.getId());
            }
        } else
            nextYearBeList = populateNextYearBe(finYear);
        final List<BudgetDetail> results = budgetDetailService.findAllBudgetDetailsForParent(budgetDetail.getBudget(),
                budgetDetail,
                persistenceService);
        for (final BudgetDetail detail : results) {
            final BudgetReportView view = new BudgetReportView();
            view.setId(detail.getId());
            view.setDepartmentCode(detail.getExecutingDepartment().getCode());
            view.setFunctionCode(detail.getFunction().getCode());
            view.setBudgetGroupName(detail.getBudgetGroup().getName());
            if ("BE".equalsIgnoreCase(detail.getBudget().getIsbere())) {
                view.setBeNextYearApproved(detail.getApprovedAmount());
                view.setBeNextYearOriginal(detail.getOriginalAmount());
                for (final BudgetDetail budgetDetail : lastYearBe)
                    if (compareDetails(budgetDetail, detail))
                        view.setBeCurrentYearApproved(budgetDetail.getApprovedAmount());
                for (final BudgetDetail budgetDetail : lastYearRe)
                    if (compareDetails(budgetDetail, detail)) {
                        view.setReCurrentYearApproved(budgetDetail.getApprovedAmount());
                        view.setReCurrentYearOriginal(budgetDetail.getOriginalAmount());
                    }
            } else {
                view.setReCurrentYearApproved(detail.getApprovedAmount());
                view.setReCurrentYearOriginal(detail.getOriginalAmount());
                currentYearBeList = populateCurrentYearBe();
                for (final BudgetDetail budgetDetail : currentYearBeList)
                    if (compareDetails(budgetDetail, detail))
                        view.setBeCurrentYearApproved(budgetDetail.getApprovedAmount());
            }
            for (final BudgetDetail nextYear : nextYearBeList)
                if (compareDetails(nextYear, detail)) {
                    view.setBeNextYearApproved(nextYear.getApprovedAmount());
                    view.setBeNextYearOriginal(nextYear.getOriginalAmount());
                }
            budgetDetailsList.add(view);
        }
        populatePreviousYearActuals(results, budgetDetail.getBudget().getFinancialYear());
        ajaxLoadBudgets();
        populateYearRange();
        canViewREApprovedAmount = canViewApprovedAmount(budgetDetail.getBudget());
        canViewBEApprovedAmount = canViewApprovedAmount(budgetService.getReferenceBudgetFor(budgetDetail.getBudget()));
        return "form";
    }

    private void populateYearRange() {
        final CFinancialYear financialYear = budgetDetail.getBudget().getFinancialYear();
        if (financialYear != null)
            if ("BE".equalsIgnoreCase(budgetDetail.getBudget().getIsbere())) {
                currentYearRange = budgetDetailHelper.computePreviousYearRange(financialYear.getFinYearRange());
                lastYearRange = currentYearRange;
                nextYearRange = budgetDetailHelper.computeNextYearRange(currentYearRange);
            } else {
                currentYearRange = financialYear.getFinYearRange();
                lastYearRange = budgetDetailHelper.computePreviousYearRange(currentYearRange);
                nextYearRange = budgetDetailHelper.computeNextYearRange(currentYearRange);
            }
    }

    private boolean compareDetails(final BudgetDetail nextYear, final BudgetDetail current) {
        if (nextYear.getExecutingDepartment() != null && current.getExecutingDepartment() != null
                && current.getExecutingDepartment().getId() != nextYear.getExecutingDepartment().getId())
            return false;
        if (nextYear.getFunction() != null && current.getFunction() != null
                && current.getFunction().getId() != nextYear.getFunction().getId())
            return false;
        if (nextYear.getFund() != null && current.getFund() != null && current.getFund().getId() != nextYear.getFund().getId())
            return false;
        if (nextYear.getFunctionary() != null && current.getFunctionary() != null
                && current.getFunctionary().getId() != nextYear.getFunctionary().getId())
            return false;
        if (nextYear.getScheme() != null && current.getScheme() != null
                && current.getScheme().getId() != nextYear.getScheme().getId())
            return false;
        if (nextYear.getSubScheme() != null && current.getSubScheme() != null
                && current.getSubScheme().getId() != nextYear.getSubScheme().getId())
            return false;
        if (nextYear.getBoundary() != null && current.getBoundary() != null
                && current.getBoundary().getId() != nextYear.getBoundary().getId())
            return false;
        if (nextYear.getBudgetGroup() != null && current.getBudgetGroup() != null
                && current.getBudgetGroup().getId() != nextYear.getBudgetGroup().getId())
            return false;
        if (nextYear.getBudget() != null && current.getBudget() != null
                && current.getBudget().getId() == nextYear.getBudget().getId())
            return false;
        return true;
    }

    private List<BudgetDetail> populateNextYearBe(final CFinancialYear finYear) {
        final BudgetDetail detail = new BudgetDetail();
        detail.copyFrom(budgetDetail);
        detail.setBudget(null);
        final Date nextYear = getNextYearFor(finYear.getStartingDate());
        return budgetDetailService.searchByCriteriaWithTypeAndFY(financialYearDAO.getFinYearByDate(nextYear).getId(), "BE",
                detail);
    }

    private List<BudgetDetail> populateCurrentYearBe() {
        final BudgetDetail detail = new BudgetDetail();
        detail.copyFrom(budgetDetail);
        detail.setBudget(null);
        return budgetDetailService.searchByCriteriaWithTypeAndFY(budgetDetail.getBudget().getFinancialYear().getId(), "BE",
                detail);
    }

    protected ValueStack getValueStack() {
        return ActionContext.getContext().getValueStack();
    }

    private void populatePreviousYearActuals(final List<BudgetDetail> budgetDetails, CFinancialYear financialYear) {
        if (financialYear != null && financialYear.getId() != null)
            financialYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id=?", financialYear.getId());
        Map<String, Object> paramMap;
        for (final BudgetDetail detail : budgetDetails) {
            paramMap = budgetDetailHelper.constructParamMap(getValueStack(), detail);
            final BigDecimal amount = budgetDetailHelper.getTotalPreviousActualData(paramMap, financialYear.getEndingDate());
            for (final BudgetReportView row : budgetDetailsList)
                if (row.getId().equals(detail.getId()))
                    row.setActualsLastYear(amount == null ? BigDecimal.ZERO : amount);
        }
    }

    public Date subtractYear(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    @Override
    public Object getModel() {
        return budgetDetail;
    }

    public void setBudgetDetail(final BudgetDetail budgetDetail) {
        this.budgetDetail = budgetDetail;
    }

    public BudgetDetail getBudgetDetail() {
        return budgetDetail;
    }

    public void setBudgetDetailsList(final List<BudgetReportView> budgetDetailsList) {
        this.budgetDetailsList = budgetDetailsList;
    }

    public List<BudgetReportView> getBudgetDetailsList() {
        return budgetDetailsList;
    }

    public void setBudgetDetailService(final BudgetDetailService budgetDetailService) {
        this.budgetDetailService = budgetDetailService;
    }

    public void setBudgetList(final List<Budget> budgetList) {
        this.budgetList = budgetList;
    }

    public List<Budget> getBudgetList() {
        return budgetList;
    }

    public boolean getCanViewREApprovedAmount() {
        return canViewREApprovedAmount;
    }

    public boolean getCanViewBEApprovedAmount() {
        return canViewBEApprovedAmount;
    }

    public void setBudgetService(final BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public void setShowResults(final boolean showResults) {
        this.showResults = showResults;
    }

    public boolean getShowResults() {
        return showResults;
    }

    public void setCurrentYearRange(final String currentYearRange) {
        this.currentYearRange = currentYearRange;
    }

    public String getCurrentYearRange() {
        return currentYearRange;
    }

    public void setNextYearRange(final String nextYearRange) {
        this.nextYearRange = nextYearRange;
    }

    public String getNextYearRange() {
        return nextYearRange;
    }

    public void setLastYearRange(final String lastYearRange) {
        this.lastYearRange = lastYearRange;
    }

    public String getLastYearRange() {
        return lastYearRange;
    }
}