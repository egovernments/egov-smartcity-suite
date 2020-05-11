/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.egf.commons;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.service.FunctionService;
import org.egov.commons.service.FundService;
import org.egov.services.budget.BudgetDetailService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FinancialMasterService {

    private static final int BUDGET_MIN_RESULT_SIZE = 1;
    private static final String BUDGET_TYPE_BE = "BE";

    @Autowired
    private FundService fundService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private FinancialYearHibernateDAO financialYearDAO;

    @Autowired
    @Qualifier("budgetDetailService")
    protected BudgetDetailService budgetDetailService;

    @Autowired
    private EntityManager entityManager;

    public List<CFinancialYear> getAllActiveFinYears() {
        return financialYearDAO.getAllActiveFinancialYearList();
    }

    public List<Fund> getAllActiveFunds() {
        return fundService.getByIsActive(true);
    }

    public List<CFunction> getFunctions() {
        return functionService.findAllActive();
    }

    @SuppressWarnings("unchecked")
    public List<CChartOfAccounts> findAccountCodesByGlcodeNameLike(String searchString) {
        final javax.persistence.Query qry = entityManager.createQuery(
                "from CChartOfAccounts where classification='4' and isActiveForPosting=true and (glcode like :glCode or upper(name) like :name) order by glcode");
        qry.setParameter("glCode", searchString + "%");
        qry.setParameter("name", "%" + searchString.toUpperCase() + "%");
        return (List<CChartOfAccounts>) qry.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<BudgetDetails> getBudgetDetails(BudgetSearchRequest budgetSearchRequest) {
        List<BudgetDetails> budgetDetails;
        String queryStr = "select bdt.id,bd.financialyearid,bdt.originalamount,bdt.approvedamount,bdt.budgetavailable,bd.isbere "
                + "from egf_budget bd,egf_budgetdetail bdt,egf_budgetgroup bg where bdt.budget=bd.id and bdt.budgetgroup= bg.id "
                + "and bd.financialyearid=:finYearId and  bdt.fund=:fundId and bdt.function=:functionId "
                + "and bdt.executing_department=:deptId and bg.maxcode=:glCodeId and bg.mincode=:glCodeId";
        javax.persistence.Query searchQry = entityManager.createNativeQuery(queryStr);
        searchQry.setParameter("finYearId", budgetSearchRequest.getFinYearId());
        searchQry.setParameter("deptId", budgetSearchRequest.getDepartmentId());
        searchQry.setParameter("fundId", budgetSearchRequest.getFundId());
        searchQry.setParameter("functionId", budgetSearchRequest.getFunctionId());
        searchQry.setParameter("glCodeId", budgetSearchRequest.getGlCodeId());

        List<Object[]> results = searchQry.getResultList();
        if (results.isEmpty()) {
            return Collections.emptyList();
        } else {
            budgetDetails = results.stream()
                    .map(result -> new BudgetDetails(Long.valueOf(result[0].toString()),
                            Long.valueOf(result[1].toString()), (BigDecimal) result[2], (BigDecimal) result[3],
                            (BigDecimal) result[4], result[5].toString()))
                    .collect(Collectors.toList());

            if (budgetDetails.size() > BUDGET_MIN_RESULT_SIZE) {
                budgetDetails.removeIf(budget -> BUDGET_TYPE_BE.equals(budget.getIsBere()));
            }
            setBillAmountDeatils(budgetDetails, budgetSearchRequest.getFunctionId(), budgetSearchRequest.getGlCodeId());
            return budgetDetails;
        }
    }

    private void setBillAmountDeatils(List<BudgetDetails> budgetDetails, final Long functionId, final Long glCodeId) {

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        for (BudgetDetails detail : budgetDetails) {
            CFinancialYear financialYear = entityManager
                    .createQuery("from CFinancialYear where id=:finYearId", CFinancialYear.class)
                    .setParameter("finYearId", detail.getFinYearId()).getSingleResult();
            DateTime fromDate = new DateTime(financialYear.getStartingDate());
            DateTime toDate = new DateTime(financialYear.getEndingDate());
            String voucherFromDate = fromDate.toString(dtf);
            String voucherToDate = toDate.toString(dtf);
            final BigDecimal billsAmount = fetchTotalBillsCreatedAmount(functionId, glCodeId, voucherFromDate,
                    voucherToDate);
            detail.setBillsCreatedAmount(billsAmount);
            detail.setBudgetBalance(detail.getApprovedAmount().subtract(billsAmount));
        }

    }

    @SuppressWarnings("unchecked")
    private BigDecimal fetchTotalBillsCreatedAmount(Long functionId, Long glCodeId, String fromDate, String toDate) {
        final StringBuilder billAmountQuery = new StringBuilder();
        billAmountQuery.append(
                "select bd.glcodeid,coalesce(sum(bd.debitamount),0) from eg_billregister br,eg_billdetails bd where bd.billid=br.id and br.billstatus = 'APPROVED' and bd.glcodeid=:glCodeId and bd.functionid=:functionId and br.createddate>=to_timestamp(:fromDate, 'YYYY-MM-dd') and br.createddate <=to_timestamp(:toDate, 'YYYY-MM-dd') group by bd.glcodeid");
        javax.persistence.Query searchQry = entityManager.createNativeQuery(billAmountQuery.toString());

        searchQry.setParameter("fromDate", fromDate);
        searchQry.setParameter("toDate", toDate);
        searchQry.setParameter("glCodeId", glCodeId);
        searchQry.setParameter("functionId", functionId);

        final List<Object[]> result = searchQry.getResultList();
        if (result.isEmpty() || result.get(0) == null) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(result.get(0)[1].toString());
        }
    }
}
