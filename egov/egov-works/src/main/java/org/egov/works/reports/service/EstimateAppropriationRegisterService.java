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

package org.egov.works.reports.service;

import org.apache.log4j.Logger;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetUsage;
import org.egov.services.budget.BudgetGroupService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.egov.works.reports.entity.EstimateAppropriationRegisterSearchRequest;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class EstimateAppropriationRegisterService {
    
    private static final Logger logger = Logger.getLogger(EstimateAppropriationRegisterService.class);

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;
    
    @Autowired
    private BudgetGroupService budgetGroupService;
    
    @Autowired
    private LineEstimateDetailsRepository lineEstimateDetailsRepository;
    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
    
    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;
    
    @SuppressWarnings("unchecked")
    public Map<String, List> searchEstimateAppropriationRegister(
            EstimateAppropriationRegisterSearchRequest estimateAppropriationRegisterSearchRequest) {
        
        Map<String, Object> queryParamMap = new HashMap<String, Object>();
        BigDecimal totalGrant = BigDecimal.ZERO;
        BigDecimal totalGrantPerc = BigDecimal.ZERO;
        Map<String, List> approvedBudgetFolioDetailsMap = null;
            
        if (estimateAppropriationRegisterSearchRequest != null && estimateAppropriationRegisterSearchRequest.getFund() != null)
            queryParamMap.put("fundid", estimateAppropriationRegisterSearchRequest.getFund().intValue());

        if (estimateAppropriationRegisterSearchRequest != null && estimateAppropriationRegisterSearchRequest.getFunction() != null)
            queryParamMap.put("functionid", estimateAppropriationRegisterSearchRequest.getFunction());
        if (estimateAppropriationRegisterSearchRequest != null && estimateAppropriationRegisterSearchRequest.getBudgetHead() != null) {
            final List<BudgetGroup> budgetheadid = new ArrayList<BudgetGroup>();
            BudgetGroup budgetGroup = budgetGroupService.findById(estimateAppropriationRegisterSearchRequest.getBudgetHead(), true);
            budgetheadid.add(budgetGroup);
            queryParamMap.put("budgetheadid", budgetheadid);
        }

        if (estimateAppropriationRegisterSearchRequest != null
                && estimateAppropriationRegisterSearchRequest.getDepartment() != null)
            queryParamMap.put("deptid", estimateAppropriationRegisterSearchRequest.getDepartment());

        if (estimateAppropriationRegisterSearchRequest != null
                && estimateAppropriationRegisterSearchRequest.getFinancialYear() != null) {
            queryParamMap.put("financialyearid", estimateAppropriationRegisterSearchRequest.getFinancialYear());
            queryParamMap.put("fromDate", financialYearHibernateDAO.getFinancialYearById(estimateAppropriationRegisterSearchRequest.getFinancialYear()).getStartingDate());
            queryParamMap.put("toDate", new Date());
        }

        if (!queryParamMap.isEmpty()) {
            BigDecimal planningBudgetPerc = new BigDecimal(0);
            try {
                totalGrant = budgetDetailsDAO.getBudgetedAmtForYear(queryParamMap);
                queryParamMap.put("deptid", estimateAppropriationRegisterSearchRequest.getDepartment().intValue());
                planningBudgetPerc = getPlanningBudgetPercentage(queryParamMap);
                queryParamMap.put("deptid", estimateAppropriationRegisterSearchRequest.getDepartment());
            } catch (final ValidationException valEx) {
                logger.error(valEx);
            }
            if (planningBudgetPerc != null && !planningBudgetPerc.equals(0)) {
                totalGrantPerc = totalGrant.multiply(planningBudgetPerc.divide(new BigDecimal(100)));
                queryParamMap.put("totalGrantPerc", totalGrantPerc);
            }
            approvedBudgetFolioDetailsMap = getApprovedAppropriationDetailsForBugetHead(queryParamMap);
        }
        
        return approvedBudgetFolioDetailsMap;
    }
    
    private BigDecimal getPlanningBudgetPercentage(final Map<String, Object> queryParamMap) {
        return budgetDetailsDAO.getPlanningPercentForYear(queryParamMap);
    }
    
    public Map<String, List> getApprovedAppropriationDetailsForBugetHead(final Map<String, Object> queryParamMap) {
        final List<BudgetFolioDetail> approvedBudgetFolioResultList = new ArrayList<BudgetFolioDetail>();
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        if (queryParamMap.get("budgetheadid") != null) {
            final List<BudgetGroup> budgetheadid = (List) queryParamMap.get("budgetheadid");
            final BudgetGroup bg = budgetheadid.get(0);
            paramMap.put("budgetgroupId", bg.getId());
        }
        if (queryParamMap.get("deptid") != null)
            paramMap.put("ExecutionDepartmentId", queryParamMap.get("deptid"));
        if (queryParamMap.get("functionid") != null)
            paramMap.put("functionId", queryParamMap.get("functionid"));
        if (queryParamMap.get("fundid") != null)
            paramMap.put("fundId", queryParamMap.get("fundid"));
        if (queryParamMap.get("financialyearid") != null)
            paramMap.put("financialYearId", queryParamMap.get("financialyearid"));
        if (queryParamMap.get("fromDate") != null)
            paramMap.put("fromDate", queryParamMap.get("fromDate"));
        if (queryParamMap.get("toDate") != null)
            paramMap.put("toDate", queryParamMap.get("toDate"));
        final Integer moduleId = 11;
        paramMap.put("moduleId", moduleId);
        final List<BudgetUsage> budgetUsageList = budgetDetailsDAO.getListBudgetUsage(paramMap);
        if (budgetUsageList != null && !budgetUsageList.isEmpty())
            return addApprovedEstimateResultList(approvedBudgetFolioResultList, budgetUsageList, new BigDecimal(
                    queryParamMap.get("totalGrantPerc").toString()));
        return new HashMap<String, List>();
    }
    
    public Map<String, List> addApprovedEstimateResultList(final List<BudgetFolioDetail> budgetFolioResultList,
            final List<BudgetUsage> budgetUsageList, final BigDecimal totalGrantPerc) {
        int srlNo = 1;
        Double cumulativeTotal = 0.00D;
        BigDecimal balanceAvailable = BigDecimal.ZERO;
        final Map<String, List> budgetFolioMap = new HashMap<String, List>();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
        for (final BudgetUsage budgetUsage : budgetUsageList) {
            final BudgetFolioDetail budgetFolioDetail = new BudgetFolioDetail();
            budgetFolioDetail.setSrlNo(srlNo++);

            final List<LineEstimateDetails> leds = lineEstimateDetailsRepository.findByEstimateNumberContainingIgnoreCase(budgetUsage.getReferenceNumber());
            LineEstimateDetails led = leds.isEmpty() ? null : leds.get(0);
            if (led != null) {
                budgetFolioDetail.setEstimateNo(led.getEstimateNumber());
                budgetFolioDetail.setNameOfWork(led.getNameOfWork());
                budgetFolioDetail.setWorkValue(led.getEstimateAmount().doubleValue());
                budgetFolioDetail.setEstimateDate(sdf.format(led.getLineEstimate().getLineEstimateDate()));
                if(led.getProjectCode() != null)
                    budgetFolioDetail.setWorkIdentificationNumber(led.getProjectCode().getCode());

            }

            budgetFolioDetail.setBudgetApprNo(budgetUsage.getAppropriationnumber());
            budgetFolioDetail.setCumulativeTotal(cumulativeTotal);
            balanceAvailable = totalGrantPerc.subtract(new BigDecimal(cumulativeTotal));
            budgetFolioDetail.setBalanceAvailable(balanceAvailable);
            budgetFolioDetail.setAppDate(sdf.format(new Date(budgetUsage.getUpdatedTime().getTime())));
            budgetFolioDetail.setAppType(getApporpriationType(budgetUsage.getId()));
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
        calculatedValuesList.add(totalGrantPerc.subtract(new BigDecimal(cumulativeTotal)));
        budgetFolioMap.put("budgetFolioList", budgetFolioResultList);
        budgetFolioMap.put("calculatedValues", calculatedValuesList);
        return budgetFolioMap;
    }
    
    public String getApporpriationType(final long budgetUsageId) {
        String appType = "Regular";
        return appType;
    }
}
