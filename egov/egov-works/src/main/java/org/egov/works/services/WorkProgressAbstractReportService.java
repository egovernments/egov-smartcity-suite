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
package org.egov.works.services;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.model.budget.BudgetGroup;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class WorkProgressAbstractReportService {
    private static final Logger logger = Logger.getLogger(WorkProgressAbstractReportService.class);

    private BudgetDetailsDAO budgetDetailsDAO;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    public void setBudgetHeadsFromString(final String budgetHeadsStr, final List<String> budgetHeads,
            final List<Long> budgetHeadIds) {
        final List<BudgetGroup> budgetHeadList = new ArrayList<BudgetGroup>();
        final List<CChartOfAccounts> coaList = new ArrayList<CChartOfAccounts>();
        if (StringUtils.isNotBlank(budgetHeadsStr)) {
            final String[] budgetHeadsFromString = budgetHeadsStr.split(",");
            for (final String element : budgetHeadsFromString)
                // Split and obtain only the glcode
                coaList.addAll(chartOfAccountsHibernateDAO.getListOfDetailCode(element.split("-")[0]));
            budgetHeadList.addAll(budgetDetailsDAO.getBudgetHeadForGlcodeList(coaList));
            final List<Long> budgetHeadIdsLong = new ArrayList<Long>();
            final List<String> budgetHeadIdStr = new ArrayList<String>();
            if (budgetHeadList != null && budgetHeadList.size() > 0)
                for (final BudgetGroup bdgtGrp : budgetHeadList) {
                    budgetHeadIdStr.add(bdgtGrp.getId().toString());
                    budgetHeadIdsLong.add(bdgtGrp.getId());
                }
            budgetHeads.addAll(budgetHeadIdStr);
            budgetHeadIds.addAll(budgetHeadIdsLong);
        }
    }

    public void setBudgetHeadsFromIdString(final String budgetHeadsStr, final List<String> budgetHeads,
            final List<Long> budgetHeadIds) {
        if (StringUtils.isNotBlank(budgetHeadsStr)) {
            final String[] budgetHeadIdsFromString = budgetHeadsStr.split(",");
            for (final String element : budgetHeadIdsFromString) {
                budgetHeads.add(element);
                budgetHeadIds.add(Long.parseLong(element));
            }
        }
    }

    public void setDepositCodesFromString(final String depositCodesStr, final List<Long> depositCodeIds) {
        final List<CChartOfAccounts> coaList = new ArrayList<CChartOfAccounts>();
        if (StringUtils.isNotBlank(depositCodesStr)) {
            final String[] depositCodesFromStr = depositCodesStr.split(",");
            for (final String element : depositCodesFromStr)
                coaList.addAll(chartOfAccountsHibernateDAO.getListOfDetailCode(element.split("-")[0]));
            final List<Long> depositCodeIdsLong = new ArrayList<Long>();
            if (coaList != null && coaList.size() > 0)
                for (final CChartOfAccounts coa : coaList)
                    depositCodeIdsLong.add(coa.getId());
            depositCodeIds.addAll(depositCodeIdsLong);
        }
    }

    public void setDepositCodesFromIdString(final String depositCodesStr, final List<Long> depositCodeIds) {
        new ArrayList<CChartOfAccounts>();
        if (StringUtils.isNotBlank(depositCodesStr)) {
            final String[] depositCodesFromStr = depositCodesStr.split(",");
            for (final String element : depositCodesFromStr)
                depositCodeIds.add(Long.parseLong(element));
        }
    }

    /**
     * Converting given amount to show in Crores with no of decimal points to be rounded off
     *
     * @param amount
     * @param decimalPoints
     * @return
     */
    public BigDecimal getRoundedOfAmount(final Object amount, final int decimalPoints) {
        final int dividingFactor = 10000000; // 1 Crore
        if (amount != null) {
            final BigDecimal divisor = new BigDecimal(dividingFactor);
            final BigDecimal amountBD = (BigDecimal) amount;
            final BigDecimal result = amountBD.divide(divisor);
            return result.setScale(decimalPoints, RoundingMode.HALF_UP);
        } else
            return null;
    }

    public void setBudgetDetailsDAO(final BudgetDetailsDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

}
