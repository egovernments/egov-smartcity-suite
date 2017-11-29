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
package org.egov.works.services.impl;

import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Fund;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.models.estimate.BudgetFolioDetail;
import org.egov.works.models.estimate.DepositWorksUsage;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.DepositWorksUsageService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DepositWorksUsageServiceImpl extends BaseServiceImpl<DepositWorksUsage, Long> implements
        DepositWorksUsageService {
    private static final Logger LOGGER = Logger.getLogger(DepositWorksUsageServiceImpl.class);
    private EgovCommon egovCommon;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    public static final String dateFormat = "dd-MMM-yyyy";
    private ContractorBillService contractorBillService;

    public DepositWorksUsageServiceImpl(final PersistenceService<DepositWorksUsage, Long> persistenceService) {
        super(persistenceService);

    }

    @Override
    public BigDecimal getTotalDepositWorksAmount(final Fund fund, final CChartOfAccounts coa,
            final Accountdetailtype accountdetailtype, final Long depositCode, final Date appropriationDate) {

        return egovCommon.getDepositAmountForDepositCode(appropriationDate, coa.getGlcode(), fund.getCode(),
                accountdetailtype.getId(), depositCode.intValue());
    }

    @Override
    public Map<String, List> getDepositFolioDetails(final AbstractEstimate abstractEstimate, final Fund fund,
            final CChartOfAccounts coa, final Accountdetailtype accountdetailtype, final Long depositCode,
            final Date appropriationDate) {
        final List<BudgetFolioDetail> approvedBudgetFolioResultList = new ArrayList<BudgetFolioDetail>();
        final List<Object> paramList = new ArrayList<Object>();
        Object[] params;
        paramList.add(appropriationDate);
        paramList.add(fund.getId());
        paramList.add(depositCode);
        paramList.add(coa.getId());
        params = new Object[paramList.size()];
        params = paramList.toArray(params);

        // Getting deposit works usage list across the financial year
        final List<DepositWorksUsage> depositWorksUsageList = persistenceService
                .findAllBy(
                        "from DepositWorksUsage dwu where trunc(dwu.appropriationDate)<=trunc(?) and  dwu.depositCode.fund.id=?  and  dwu.depositCode.id=?  and  dwu.coa.id=?   order by dwu.id asc",
                        params);

        if (depositWorksUsageList != null && !depositWorksUsageList.isEmpty())
            return addApprovedEstimateResultList(approvedBudgetFolioResultList, depositWorksUsageList,
                    appropriationDate);
        return new HashMap<String, List>();
    }

    public Map<String, List> addApprovedEstimateResultList(final List<BudgetFolioDetail> depositFolioResultList,
            final List<DepositWorksUsage> depositWorksUsageList, final Date appropriationDate) {
        int srlNo = 1;
        Double cumulativeTotal = 0.00D;
        BigDecimal totalDeposit = BigDecimal.ZERO;
        double cumulativeExpensesIncurred = 0.0;
        final Map<String, List> budgetFolioMap = new HashMap<String, List>();
        for (final DepositWorksUsage depositWorksUsage : depositWorksUsageList) {
            final BudgetFolioDetail budgetFolioDetail = new BudgetFolioDetail();
            budgetFolioDetail.setSrlNo(srlNo++);

            if (depositWorksUsage.getAbstractEstimate() != null) {
                budgetFolioDetail.setEstimateNo(depositWorksUsage.getAbstractEstimate().getEstimateNumber());
                budgetFolioDetail.setNameOfWork(depositWorksUsage.getAbstractEstimate().getName());
                budgetFolioDetail
                        .setEstimateDate(sdf.format(depositWorksUsage.getAbstractEstimate().getEstimateDate()));
                if (!isAppropriationRejected(depositWorksUsage.getAppropriationNumber())) {
                    budgetFolioDetail.setExpensesIncurred(contractorBillService.getTotalActualExpenseForProject(
                            depositWorksUsage.getAbstractEstimate(), appropriationDate));
                    budgetFolioDetail.setCumulativeExpensesIncurred(cumulativeExpensesIncurred);
                    budgetFolioDetail.setActualBalanceAvailable(depositWorksUsage.getTotalDepositAmount().doubleValue()
                            - cumulativeExpensesIncurred);
                    cumulativeExpensesIncurred = cumulativeExpensesIncurred
                            + budgetFolioDetail.getExpensesIncurred().doubleValue();
                } else {
                    budgetFolioDetail.setExpensesIncurred(0.0);
                    budgetFolioDetail.setCumulativeExpensesIncurred(cumulativeExpensesIncurred);
                    budgetFolioDetail.setActualBalanceAvailable(depositWorksUsage.getTotalDepositAmount().doubleValue()
                            - cumulativeExpensesIncurred);
                    cumulativeExpensesIncurred = cumulativeExpensesIncurred
                            + budgetFolioDetail.getExpensesIncurred().doubleValue();
                }
            }

            budgetFolioDetail.setBudgetApprNo(depositWorksUsage.getAppropriationNumber());
            budgetFolioDetail.setCumulativeTotal(cumulativeTotal);
            budgetFolioDetail.setAppDate(sdf.format(depositWorksUsage.getAppropriationDate()));
            if (depositWorksUsage.getReleasedAmount().compareTo(BigDecimal.ZERO) > 0) {
                cumulativeTotal = cumulativeTotal - depositWorksUsage.getReleasedAmount().doubleValue();
                budgetFolioDetail
                        .setWorkValue(depositWorksUsage.getAbstractEstimate().getTotalAmount().getValue() * -1);// to
                // display
                // released
                // amount
                // as
                // negative
                budgetFolioDetail.setAppropriatedValue(BigDecimal.ZERO.subtract(depositWorksUsage.getReleasedAmount())
                        .doubleValue());
            } else {
                cumulativeTotal = cumulativeTotal + depositWorksUsage.getConsumedAmount().doubleValue();
                budgetFolioDetail.setWorkValue(depositWorksUsage.getAbstractEstimate().getTotalAmount().getValue());
                budgetFolioDetail.setAppropriatedValue(depositWorksUsage.getConsumedAmount().doubleValue());
            }
            totalDeposit = depositWorksUsage.getTotalDepositAmount();

            budgetFolioDetail.setBalanceAvailable(totalDeposit);

            depositFolioResultList.add(budgetFolioDetail);
        }
        final List calculatedValuesList = new ArrayList();
        calculatedValuesList.add(cumulativeTotal);
        budgetFolioMap.put("depositFolioList", depositFolioResultList);
        budgetFolioMap.put("calculatedValues", calculatedValuesList);
        budgetFolioMap.put("totalCumulativeExpensesIncurred", Arrays.asList(cumulativeExpensesIncurred));
        return budgetFolioMap;
    }

    @Override
    public BigDecimal getTotalUtilizedAmountForDepositWorks(final FinancialDetail financialDetail) {
        return (BigDecimal) genericService.findByNamedQuery("getDepositWorksUsageAmount", financialDetail
                .getAbstractEstimate().getDepositCode().getId(), financialDetail.getFund().getId(), financialDetail
                        .getCoa().getId());
    }

    @Override
    public BigDecimal getTotalUtilizedAmountForDepositWorks(final FinancialDetail financialDetail, final Date appDate) {
        BigDecimal totalUtilizedAmount = BigDecimal.ZERO;
        // NOTE: utilizedAmountForRunningProject holds sum of all appropriation
        // amount for estimates which are not closed
        BigDecimal utilizedAmountForRunningProject = (BigDecimal) genericService
                .find("select sum(dwu.consumedAmount-dwu.releasedAmount) from DepositWorksUsage dwu where dwu.createdDate<=? and EXISTS (select 'true' from FinancialDetail fd where fd.abstractEstimate.id=dwu.abstractEstimate.id and fd.fund.id=? and fd.abstractEstimate.depositCode.id=? and fd.coa.id=?) "
                        + "and (dwu.abstractEstimate.projectCode.id is null or dwu.abstractEstimate.projectCode.id not in (select proj.id from ProjectCode proj where proj.egwStatus.code='CLOSED'))",
                        appDate, financialDetail.getFund().getId(), financialDetail.getAbstractEstimate()
                                .getDepositCode().getId(),
                        financialDetail.getCoa().getId());
        if (utilizedAmountForRunningProject == null)
            utilizedAmountForRunningProject = BigDecimal.ZERO;
        LOGGER.debug("Total Utilized amount for deposit works (Running projects) >>>>Depositcodeid="
                + financialDetail.getAbstractEstimate().getDepositCode().getId() + "|| till date=" + appDate
                + "||utilizedAmount=" + utilizedAmountForRunningProject);
        // NOTE: utilizedAmountForClosedProject holds sum of all appropriation
        // amount for estimates which are closed
        Double utilizedAmountForClosedProject = (Double) genericService
                .find("select sum(fd.abstractEstimate.projectCode.projectValue) from FinancialDetail fd where trunc(fd.abstractEstimate.projectCode.completionDate)<=trunc(?) and fd.fund.id=? and fd.abstractEstimate.depositCode.id=? and fd.coa.id=? and fd.abstractEstimate.projectCode.egwStatus.code='CLOSED'",
                        appDate, financialDetail.getFund().getId(), financialDetail.getAbstractEstimate()
                                .getDepositCode().getId(),
                        financialDetail.getCoa().getId());
        if (utilizedAmountForClosedProject == null)
            utilizedAmountForClosedProject = 0.0;
        totalUtilizedAmount = utilizedAmountForRunningProject.add(new BigDecimal(utilizedAmountForClosedProject
                .doubleValue()));
        LOGGER.debug("Total Utilized amount for deposit works (Closed projects) >>>>Depositcodeid="
                + financialDetail.getAbstractEstimate().getDepositCode().getId() + "|| till date=" + appDate
                + "||utilizedAmount=" + utilizedAmountForClosedProject);
        LOGGER.debug("Total Utilized amount for deposit works (including Closed and running projects) >>>>Depositcodeid="
                + financialDetail.getAbstractEstimate().getDepositCode().getId()
                + "|| till date="
                + appDate
                + "||totalutilizedAmount=" + totalUtilizedAmount);
        return totalUtilizedAmount;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    @Override
    public DepositWorksUsage getDepositWorksUsage(final AbstractEstimate estimate, final String appropriationNumber) {
        final DepositWorksUsage depositWorksUsage = persistenceService.find(
                "from DepositWorksUsage dwu where dwu.abstractEstimate=? and dwu.appropriationNumber=?", estimate,
                appropriationNumber);
        return depositWorksUsage;
    }

    public void setContractorBillService(final ContractorBillService contractorBillService) {
        this.contractorBillService = contractorBillService;
    }

    private boolean isAppropriationRejected(final String apprNumber) {

        if (apprNumber == null)
            throw new ApplicationRuntimeException("Invalid parameter passed to isAppropriationRejected() ||apprNumber="
                    + apprNumber);
        final String[] str = apprNumber.split("/");
        if (str.length > 0 && "BC".equalsIgnoreCase(str[0]))
            return true;

        final String rejectedApprNumber = "BC/" + apprNumber;

        final DepositWorksUsage depositWorksUsage = persistenceService.find(
                "from DepositWorksUsage dwu where dwu.appropriationNumber=?", rejectedApprNumber);
        if (depositWorksUsage != null)
            return true;

        return false;
    }
}
