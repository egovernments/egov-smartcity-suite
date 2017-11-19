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
package org.egov.utils;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.budget.BudgetDetail;
import org.egov.egf.model.BudgetAmountView;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BudgetDetailHelper {
    
    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;
    @Autowired    
    private FinancialYearDAO financialYearDAO;

    public void setBudgetDetailsDAO(final BudgetDetailsDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

    public Date getBudgetAsOnDate(final BudgetDetail detail) {
        if (detail.getBudget() != null && detail.getBudget().getAsOnDate() != null)
            return detail.getBudget().getAsOnDate();
        return new Date();
    }

    public void removeEmptyBudgetDetails(final List<BudgetDetail> budgetDetailList) {
        for (final Iterator<BudgetDetail> detail = budgetDetailList.iterator(); detail.hasNext();)
            if (detail.next() == null)
                detail.remove();
    }

    public String getActualsFor(final Map<String, Object> paramMap, final Date asOn) {
        paramMap.put(Constants.ASONDATE, asOn);
        try {
            return budgetDetailsDAO.getActualBudgetUtilized(paramMap).setScale(0, BigDecimal.ROUND_CEILING).toString();
        } catch (final ValidationException e) {
            return "0.0";
        } catch (final ArithmeticException e) {
            return "0.0";
        }
    }

    // gives actuals from bill and voucher
    public BigDecimal getTotalActualsFor(final Map<String, Object> paramMap, final Date asOn) {
        BigDecimal actuals = BigDecimal.ZERO;
        paramMap.put(Constants.ASONDATE, asOn);
        try {
            actuals = budgetDetailsDAO.getActualBudgetUtilized(paramMap);
            actuals = actuals == null ? BigDecimal.ZERO : actuals;
            actuals = actuals.add(budgetDetailsDAO.getBillAmountForBudgetCheck(paramMap));
        } catch (final ValidationException e) {
            return BigDecimal.ZERO;
        }
        return actuals;
    }

    public void populateData(final BudgetAmountView amountDisplay, final Map<String, Object> paramMap, final Date asOnDate,
            final boolean isRe) {
        final Date previousYear = subtractYear(asOnDate);
        final String actualsFor = getActualsFor(paramMap, previousYear);
        amountDisplay.setPreviousYearActuals(actualsFor == null ? BigDecimal.ZERO.setScale(0, BigDecimal.ROUND_CEILING)
                : new BigDecimal(actualsFor).setScale(0, BigDecimal.ROUND_CEILING));
        amountDisplay.setOldActuals(getActualsFor(paramMap, subtractYear(previousYear)));
        final String currentYearActuals = getActualsFor(paramMap, asOnDate);
        amountDisplay.setCurrentYearBeActuals(currentYearActuals == null ? BigDecimal.ZERO.setScale(2) : new BigDecimal(
                currentYearActuals).setScale(0, BigDecimal.ROUND_CEILING));
    }

    public String getPreviousActualData(final Map<String, Object> paramMap, final Date asOnDate) {
        return getActualsFor(paramMap, subtractYear(asOnDate));
    }

    public BigDecimal getTotalPreviousActualData(final Map<String, Object> paramMap, final Date asOnDate) {
        return getTotalActualsFor(paramMap, subtractYear(asOnDate));
    }

    protected Date subtractYear(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    public Long getFinancialYear() {
        return Long.valueOf(financialYearDAO.getCurrYearFiscalId());
    }

    public Map<String, Object> constructParamMap(final ValueStack valueStack, final BudgetDetail detail) {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        valueStack.setValue("budgetDetail", detail);
        paramMap.put(Constants.DEPTID,
                Constants.INT_ZERO.equals(valueStack.findValue("budgetDetail.executingDepartment.id")) ? null
                        : valueStack.findValue("budgetDetail.executingDepartment.id"));
        paramMap.put(
                Constants.FUNCTIONID,
                Constants.LONG_ZERO.equals(valueStack.findValue("budgetDetail.function.id")) ? null : valueStack
                        .findValue("budgetDetail.function.id"));
        paramMap.put(Constants.FUNCTIONARYID, Constants.INT_ZERO.equals(valueStack
                .findValue("budgetDetail.functionary.id")) ? null : valueStack.findValue("budgetDetail.functionary.id"));
        paramMap.put(
                Constants.SCHEMEID,
                Constants.INT_ZERO.equals(valueStack.findValue("budgetDetail.scheme.id")) ? null : valueStack
                        .findValue("budgetDetail.scheme.id"));
        paramMap.put(
                Constants.SUBSCHEMEID,
                Constants.INT_ZERO.equals(valueStack.findValue("budgetDetail.subScheme.id")) ? null : valueStack
                        .findValue("budgetDetail.subScheme.id"));
        paramMap.put(
                "budgetheadid",
                Constants.LONG_ZERO.equals(valueStack.findValue("budgetDetail.budgetGroup.id")) ? null : valueStack
                        .findValue("budgetDetail.budgetGroup.id"));
        paramMap.put("glcodeid",
                Constants.LONG_ZERO.equals(valueStack.findValue("budgetDetail.budgetGroup.minCode.id")) ? null
                        : valueStack.findValue("budgetDetail.budgetGroup.minCode.id"));
        paramMap.put(
                Constants.BOUNDARYID,
                Constants.INT_ZERO.equals(valueStack.findValue("budgetDetail.boundary.id")) ? null : valueStack
                        .findValue("budgetDetail.boundary.id"));
        paramMap.put(Constants.FUNDID, Constants.INT_ZERO.equals(valueStack.findValue("budgetDetail.fund.id")) ? null
                : valueStack.findValue("budgetDetail.fund.id"));
        return paramMap;
    }

    public BigDecimal getBillAmountForBudgetCheck(final Map<String, Object> constructParamMap) {
        return budgetDetailsDAO.getBillAmountForBudgetCheck(constructParamMap);
    }

    public String computePreviousYearRange(final String currentYearRange) {
        if (StringUtils.isNotBlank(currentYearRange)) {
            final String[] list = currentYearRange.split("-");
            return subtract(list[0]) + "-" + subtract(list[1]);
        }
        return "";
    }

    public String computeNextYearRange(final String currentYearRange) {
        if (StringUtils.isNotBlank(currentYearRange)) {
            final String[] list = currentYearRange.split("-");
            return add(list[0]) + "-" + add(list[1]);
        }
        return "";
    }

    protected String subtract(final String value) {
        final int val = Integer.parseInt(value) - 1;
        if (val < 10)
            return "0" + val;
        return String.valueOf(val);
    }

    protected String add(final String value) {
        final int val = Integer.parseInt(value) + 1;
        if (val < 10)
            return "0" + val;
        return String.valueOf(val);
    }

    public CFinancialYear getPreviousYearFor(final CFinancialYear financialYear) {
        try {
            return financialYearDAO.getFinancialYearByDate(subtractYear(financialYear.getStartingDate()));
        } catch (final ApplicationRuntimeException e) {
            return null;
        }
    }

    /**
     *
     * @param budgetName
     * @return rewrite this api when u load receipt budgets
     */

    public String accountTypeForFunctionDeptMap(final String budgetName) {
        String accountType;
        if (budgetName.toLowerCase().contains("cap"))
            accountType = "CAPITAL_EXPENDITURE";
        else
            accountType = "REVENUE_EXPENDITURE";
        return accountType;
    }

}
