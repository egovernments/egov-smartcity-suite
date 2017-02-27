/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.works.reports.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetUsage;
import org.egov.services.budget.BudgetGroupService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.BudgetFolioDetail;
import org.egov.works.lineestimate.entity.EstimateAppropriation;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.EstimateAppropriationService;
import org.egov.works.reports.entity.EstimateAppropriationRegisterSearchRequest;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstimateAppropriationRegisterService {

    private static final Logger logger = Logger.getLogger(EstimateAppropriationRegisterService.class);

    private static final String DEPTID = "deptid";
    private static final String BUDGETHEADID = "budgetheadid";
    private static final String FUNDID = "fundid";
    private static final String FUNCTIONID = "functionid";
    private static final String FINANCIALYEARID = "financialyearid";
    private static final String FROMDATE = "fromDate";
    private static final String TODATE = "toDate";
    private static final String REGULAR = "Regular";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @Autowired
    private BudgetGroupService budgetGroupService;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    private EstimateAppropriationService estimateAppropriationService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @SuppressWarnings({ "rawtypes" })
    public Map<String, List> searchEstimateAppropriationRegister(
            final EstimateAppropriationRegisterSearchRequest estimateAppropriationRegisterSearchRequest) {

        final Map<String, Object> queryParamMap = new HashMap<String, Object>();
        BigDecimal totalGrant = BigDecimal.ZERO;
        BigDecimal totalGrantPerc;
        Map<String, List> approvedBudgetFolioDetailsMap = null;

        setQueryParamMapValues(estimateAppropriationRegisterSearchRequest, queryParamMap);

        if (estimateAppropriationRegisterSearchRequest != null
                && estimateAppropriationRegisterSearchRequest.getDepartment() != null)
            queryParamMap.put(DEPTID, estimateAppropriationRegisterSearchRequest.getDepartment());

        if (estimateAppropriationRegisterSearchRequest != null
                && estimateAppropriationRegisterSearchRequest.getFinancialYear() != null) {
            queryParamMap.put(FINANCIALYEARID, estimateAppropriationRegisterSearchRequest.getFinancialYear());
            queryParamMap.put(FROMDATE,
                    financialYearHibernateDAO
                            .getFinancialYearById(estimateAppropriationRegisterSearchRequest.getFinancialYear())
                            .getStartingDate());
            queryParamMap.put(TODATE, new Date());
        }

        if (!queryParamMap.isEmpty()) {
            BigDecimal planningBudgetPerc = BigDecimal.ZERO;
            try {
                totalGrant = budgetDetailsDAO.getBudgetedAmtForYear(queryParamMap);
                if (estimateAppropriationRegisterSearchRequest != null)
                    queryParamMap.put(DEPTID, estimateAppropriationRegisterSearchRequest.getDepartment().intValue());
                planningBudgetPerc = getPlanningBudgetPercentage(queryParamMap);
            } catch (final ValidationException valEx) {
                logger.error(valEx);
            }
            if (planningBudgetPerc != null && planningBudgetPerc.compareTo(BigDecimal.ZERO) != 0) {
                totalGrantPerc = totalGrant.multiply(planningBudgetPerc.divide(new BigDecimal(100)));
                queryParamMap.put("totalGrantPerc", totalGrantPerc);
            }
            approvedBudgetFolioDetailsMap = getApprovedAppropriationDetailsForBugetHead(queryParamMap);
        }

        return approvedBudgetFolioDetailsMap;
    }

    private void setQueryParamMapValues(
            final EstimateAppropriationRegisterSearchRequest estimateAppropriationRegisterSearchRequest,
            final Map<String, Object> queryParamMap) {
        if (estimateAppropriationRegisterSearchRequest != null
                && estimateAppropriationRegisterSearchRequest.getFund() != null)
            queryParamMap.put(FUNDID, estimateAppropriationRegisterSearchRequest.getFund().intValue());

        if (estimateAppropriationRegisterSearchRequest != null
                && estimateAppropriationRegisterSearchRequest.getFunction() != null)
            queryParamMap.put(FUNCTIONID, estimateAppropriationRegisterSearchRequest.getFunction());
        if (estimateAppropriationRegisterSearchRequest != null
                && estimateAppropriationRegisterSearchRequest.getBudgetHead() != null) {
            final List<BudgetGroup> budgetheadid = new ArrayList<>();
            final BudgetGroup budgetGroup = budgetGroupService
                    .findById(estimateAppropriationRegisterSearchRequest.getBudgetHead(), true);
            budgetheadid.add(budgetGroup);
            queryParamMap.put(BUDGETHEADID, budgetheadid);
        }
    }

    private BigDecimal getPlanningBudgetPercentage(final Map<String, Object> queryParamMap) {
        return budgetDetailsDAO.getPlanningPercentForYear(queryParamMap);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, List> getApprovedAppropriationDetailsForBugetHead(final Map<String, Object> queryParamMap) {
        final List<BudgetFolioDetail> approvedBudgetFolioResultList = new ArrayList<BudgetFolioDetail>();
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        if (queryParamMap.get(BUDGETHEADID) != null) {
            final List<BudgetGroup> budgetheadid = (List) queryParamMap.get(BUDGETHEADID);
            final BudgetGroup bg = budgetheadid.get(0);
            paramMap.put("budgetgroupId", bg.getId());
        }
        if (queryParamMap.get(DEPTID) != null)
            paramMap.put("ExecutionDepartmentId", queryParamMap.get(DEPTID));
        if (queryParamMap.get(FUNCTIONID) != null)
            paramMap.put("functionId", queryParamMap.get(FUNCTIONID));
        if (queryParamMap.get(FUNDID) != null)
            paramMap.put("fundId", queryParamMap.get(FUNDID));
        if (queryParamMap.get(FINANCIALYEARID) != null)
            paramMap.put("financialYearId", queryParamMap.get(FINANCIALYEARID));
        if (queryParamMap.get(FROMDATE) != null)
            paramMap.put(FROMDATE, queryParamMap.get(FROMDATE));
        if (queryParamMap.get(TODATE) != null)
            paramMap.put(TODATE, queryParamMap.get(TODATE));
        final Integer moduleId = 11;
        paramMap.put("moduleId", moduleId);
        final List<BudgetUsage> budgetUsageList = budgetDetailsDAO.getListBudgetUsage(paramMap);
        if (budgetUsageList != null && !budgetUsageList.isEmpty())
            return addApprovedEstimateResultList(approvedBudgetFolioResultList, budgetUsageList,
                    new BigDecimal(queryParamMap.get("totalGrantPerc").toString()));
        return new HashMap<String, List>();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map<String, List> addApprovedEstimateResultList(final List<BudgetFolioDetail> budgetFolioResultList,
            final List<BudgetUsage> budgetUsageList, final BigDecimal totalGrantPerc) {
        int srlNo = 1;
        Double cumulativeTotal = 0.00D;
        BigDecimal balanceAvailable;
        final Map<String, List> budgetFolioMap = new HashMap<>();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
        for (final BudgetUsage budgetUsage : budgetUsageList) {
            final BudgetFolioDetail budgetFolioDetail = getBudgetFolioDetail();
            budgetFolioDetail.setSrlNo(srlNo++);

            final EstimateAppropriation estimateAppropriation = estimateAppropriationService
                    .findLatestByBudgetUsage(budgetUsage.getId());
            if (estimateAppropriation != null && estimateAppropriation.getLineEstimateDetails() != null) {
                final LineEstimateDetails led = estimateAppropriation.getLineEstimateDetails();
                if (led != null)
                    setBudgetFolioDetailsForLE(sdf, budgetFolioDetail, led);
            } else if (estimateAppropriation != null && estimateAppropriation.getAbstractEstimate() != null) {
                final AbstractEstimate ae = estimateAppropriation.getAbstractEstimate();
                setBudgetFolioDetailsForAE(sdf, budgetFolioDetail, ae);
            }

            budgetFolioDetail.setBudgetApprNo(budgetUsage.getAppropriationnumber());
            budgetFolioDetail.setCumulativeTotal(cumulativeTotal);
            balanceAvailable = totalGrantPerc.subtract(getCumulativeTotal(cumulativeTotal));
            budgetFolioDetail.setBalanceAvailable(balanceAvailable);
            budgetFolioDetail.setAppDate(sdf.format(getBudgetUsageUpdatedTime(budgetUsage)));
            budgetFolioDetail.setAppType(getApporpriationType());
            budgetFolioResultList.add(budgetFolioDetail);

            if (budgetUsage.getReleasedAmount() > 0) {
                cumulativeTotal = cumulativeTotal - budgetUsage.getReleasedAmount();
                budgetFolioDetail.setAppropriatedValue(0.0 - budgetUsage.getReleasedAmount());
            } else {
                cumulativeTotal = cumulativeTotal + budgetUsage.getConsumedAmount();
                budgetFolioDetail.setAppropriatedValue(budgetUsage.getConsumedAmount());
            }
        }
        final List calculatedValuesList = new ArrayList();
        calculatedValuesList.add(cumulativeTotal);
        calculatedValuesList.add(totalGrantPerc.subtract(getCumulativeTotal(cumulativeTotal)));
        budgetFolioMap.put("budgetFolioList", budgetFolioResultList);
        budgetFolioMap.put("calculatedValues", calculatedValuesList);
        return budgetFolioMap;
    }

    private void setBudgetFolioDetailsForAE(final SimpleDateFormat sdf, final BudgetFolioDetail budgetFolioDetail,
            final AbstractEstimate ae) {
        if (StringUtils.isNotBlank(ae.getEstimateNumber()))
            budgetFolioDetail.setEstimateNo(ae.getEstimateNumber());
        else
            budgetFolioDetail.setEstimateNo(StringUtils.EMPTY);
        if (ae.getParent() == null)
            budgetFolioDetail.setNameOfWork(ae.getName());
        else if (ae.getParent() != null)
            budgetFolioDetail.setNameOfWork(ae.getParent().getName());
        else
            budgetFolioDetail.setNameOfWork(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(ae.getEstimateValue().toString()))
            budgetFolioDetail.setWorkValue(ae.getEstimateValue().doubleValue());
        else
            budgetFolioDetail.setWorkValue(Double.valueOf(StringUtils.EMPTY));
        if (StringUtils.isNotBlank(ae.getEstimateDate().toString()))
            budgetFolioDetail.setEstimateDate(sdf.format(ae.getEstimateDate()));
        else
            budgetFolioDetail.setEstimateDate(StringUtils.EMPTY);
        if (ae.getProjectCode() != null)
            budgetFolioDetail.setWorkIdentificationNumber(ae.getProjectCode().getCode());
        else if (ae.getParent() != null)
            budgetFolioDetail.setWorkIdentificationNumber(ae.getParent().getProjectCode().getCode());
        else
            budgetFolioDetail.setWorkIdentificationNumber(StringUtils.EMPTY);
    }

    private void setBudgetFolioDetailsForLE(final SimpleDateFormat sdf, final BudgetFolioDetail budgetFolioDetail,
            final LineEstimateDetails led) {
        if (StringUtils.isNotBlank(led.getEstimateNumber()))
            budgetFolioDetail.setEstimateNo(led.getEstimateNumber());
        else
            budgetFolioDetail.setEstimateNo(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(led.getNameOfWork()))
            budgetFolioDetail.setNameOfWork(led.getNameOfWork());
        else
            budgetFolioDetail.setNameOfWork(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(led.getEstimateAmount().toString()))
            budgetFolioDetail.setWorkValue(led.getEstimateAmount().doubleValue());
        else
            budgetFolioDetail.setWorkValue(Double.valueOf(StringUtils.EMPTY));
        if (StringUtils.isNotBlank(led.getLineEstimate().getLineEstimateDate().toString()))
            budgetFolioDetail.setEstimateDate(sdf.format(led.getLineEstimate().getLineEstimateDate()));
        else
            budgetFolioDetail.setEstimateDate(StringUtils.EMPTY);
        if (led.getProjectCode() != null)
            budgetFolioDetail.setWorkIdentificationNumber(led.getProjectCode().getCode());
        else
            budgetFolioDetail.setWorkIdentificationNumber(StringUtils.EMPTY);
    }

    private BudgetFolioDetail getBudgetFolioDetail() {
        return new BudgetFolioDetail();
    }

    public String getApporpriationType() {
        return REGULAR;
    }

    private Date getBudgetUsageUpdatedTime(final BudgetUsage budgetUsage) {
        return new Date(budgetUsage.getUpdatedTime().getTime());
    }

    private BigDecimal getCumulativeTotal(final Double cumulativeTotal) {
        return new BigDecimal(cumulativeTotal.toString());
    }
}
