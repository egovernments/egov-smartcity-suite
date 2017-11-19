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

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.BudgetUsage;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimateAppropriation;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.models.estimate.DepositWorksUsage;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.DepositWorksUsageService;
import org.egov.works.services.RevisionEstimateService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This class will expose all Revision Estimate related operations.
 */
public class RevisionEstimateServiceImpl extends BaseServiceImpl<RevisionAbstractEstimate, Long> implements
        RevisionEstimateService {

    private AbstractEstimateService abstractEstimateService;
    private WorksService worksService;
    private EgovCommon egovCommon;
    private DepositWorksUsageService depositWorksUsageService;
    private PersistenceService<AbstractEstimateAppropriation, Long> estimateAppropriationService;
    private BudgetDetailsDAO budgetDetailsDAO;
    @Autowired
    private FinancialYearHibernateDAO finHibernateDao;
    private static final String MODULE_NAME = "Works";
    private static final String KEY_NAME = "SKIP_BUDGET_CHECK";

    public RevisionEstimateServiceImpl(final PersistenceService<RevisionAbstractEstimate, Long> persistenceService) {
        super(persistenceService);
    }

    @Override
    public void consumeBudget(final RevisionAbstractEstimate revisionEstimate) {
        final AbstractEstimate parentEstimate = revisionEstimate.getParent();
        final String appropriationNumber = abstractEstimateService.getBudgetAppropriationNumber(parentEstimate);
        if (isDepositWorksType(parentEstimate))
            checkForBudgetaryAppropriationForDepositWorks(revisionEstimate, appropriationNumber);
        else
            consumeBudgetForNormalWorks(revisionEstimate, appropriationNumber);
    }

    @Override
    public void releaseBudget(final RevisionAbstractEstimate revisionEstimate) {
        final AbstractEstimate parentEstimate = revisionEstimate.getParent();
        final String appropriationNumber = abstractEstimateService.getLatestEstimateAppropriationNumber(revisionEstimate);
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BC/");
        stringBuilder.append(appropriationNumber);
        final String budgetRejectionNumber = stringBuilder.toString();

        // Financial details of the parent
        final FinancialDetail financialDetail = parentEstimate.getFinancialDetails().get(0);
        if (isDepositWorksType(parentEstimate))
            releaseDepositWorksAmountOnReject(revisionEstimate, financialDetail, budgetRejectionNumber);
        else
            releaseBudgetOnReject(revisionEstimate, financialDetail, budgetRejectionNumber);
    }

    private boolean releaseDepositWorksAmountOnReject(final RevisionAbstractEstimate revisionEstimate,
            final FinancialDetail financialDetail, final String budgetRejectionNumber) throws ValidationException {
        boolean flag = false;
        final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("DEPOSITCODE");
        final AbstractEstimateAppropriation estimateAppropriation = estimateAppropriationService.findByNamedQuery(
                "getLatestDepositWorksUsageForEstimate", revisionEstimate.getId());
        // No budget appropriation was done in the first place
        if (estimateAppropriation == null)
            return flag;
        final BigDecimal creditBalance = egovCommon.getDepositAmountForDepositCode(new Date(), financialDetail.getCoa()
                .getGlcode(), financialDetail.getFund().getCode(), accountdetailtype.getId(), financialDetail
                        .getAbstractEstimate().getDepositCode().getId().intValue());
        final double releaseAmount = estimateAppropriation.getDepositWorksUsage().getConsumedAmount().doubleValue();
        DepositWorksUsage depositWorksUsage = new DepositWorksUsage();
        depositWorksUsage.setTotalDepositAmount(creditBalance);
        depositWorksUsage.setConsumedAmount(BigDecimal.ZERO);
        depositWorksUsage.setReleasedAmount(new BigDecimal(releaseAmount));
        depositWorksUsage.setAppropriationNumber(budgetRejectionNumber);
        depositWorksUsage.setAbstractEstimate(revisionEstimate);
        depositWorksUsage.setAppropriationDate(new Date());
        depositWorksUsage.setFinancialYear(estimateAppropriation.getDepositWorksUsage().getFinancialYear());
        depositWorksUsage.setCoa(financialDetail.getCoa());
        depositWorksUsage.setDepositCode(financialDetail.getAbstractEstimate().getDepositCode());
        depositWorksUsage = depositWorksUsageService.persist(depositWorksUsage);
        persistReleaseDepositWorksAmountDetails(revisionEstimate, financialDetail, depositWorksUsage);
        flag = true;
        return flag;
    }

    private void persistReleaseDepositWorksAmountDetails(final RevisionAbstractEstimate revisionEstimate,
            final FinancialDetail financialDetail, final DepositWorksUsage depositWorksUsage) {
        AbstractEstimateAppropriation estimateAppropriation = null;
        final BigDecimal creditBalance = depositWorksUsage.getTotalDepositAmount();
        final BigDecimal utilizedAmt = depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(financialDetail,
                new Date());
        final BigDecimal balance = creditBalance.subtract(utilizedAmt);
        estimateAppropriation = estimateAppropriationService.findByNamedQuery("getLatestDepositWorksUsageForEstimate",
                revisionEstimate.getId());
        estimateAppropriation.setBalanceAvailable(balance);
        estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
        estimateAppropriationService.persist(estimateAppropriation);
    }

    private boolean consumeBudgetForNormalWorks(final RevisionAbstractEstimate revisionEstimate,
            final String appropriationNumber) {
        boolean flag = false;
        final Long finYearId = finHibernateDao.getFinYearByDate(new Date()).getId();
        final List<Long> budgetHeadId = new ArrayList<Long>();
        final FinancialDetail financialDetail = revisionEstimate.getParent().getFinancialDetails().get(0);
        budgetHeadId.add(financialDetail.getBudgetGroup().getId());
        final BudgetUsage budgetUsage = budgetDetailsDAO.consumeEncumbranceBudget(
                appropriationNumber == null ? null : appropriationNumber,
                finYearId,
                Integer.valueOf(11),
                revisionEstimate.getEstimateNumber(),
                Integer.parseInt(financialDetail.getAbstractEstimate().getUserDepartment().getId().toString()),
                financialDetail.getFunction() == null ? null : financialDetail.getFunction().getId(),
                financialDetail.getFunctionary() == null ? null : financialDetail.getFunctionary().getId(),
                financialDetail.getScheme() == null ? null : financialDetail.getScheme().getId(),
                financialDetail.getSubScheme() == null ? null : financialDetail.getSubScheme().getId(),
                financialDetail.getAbstractEstimate().getWard() == null ? null : Integer.parseInt(financialDetail
                        .getAbstractEstimate().getWard().getId().toString()),
                financialDetail.getBudgetGroup() == null ? null : budgetHeadId,
                financialDetail.getFund() == null ? null : financialDetail.getFund().getId(), revisionEstimate
                        .getTotalAmount().getValue());
        if (budgetUsage == null)
            throw new ValidationException(Arrays.asList(new ValidationError("changeFDHeader.budget.consumption.failed",
                    "Insufficient funds available")));
        persistBudgetAppropriationDetails(revisionEstimate, revisionEstimate.getParent(), budgetUsage);
        flag = true;
        return flag;
    }

    private void persistBudgetAppropriationDetails(final RevisionAbstractEstimate revisionEstimate,
            final AbstractEstimate parentEstimate, final BudgetUsage budgetUsage) {
        AbstractEstimateAppropriation estimateAppropriation = null;
        final Integer finYearId = budgetUsage.getFinancialYearId();
        final Date endingDate = finHibernateDao.getFinancialYearById(finYearId.longValue()).getEndingDate();
        estimateAppropriation = estimateAppropriationService.findByNamedQuery("getBudgetUsageForEstimateByFinYear",
                revisionEstimate.getId(), finYearId.intValue());

        if (estimateAppropriation != null) {
            estimateAppropriation.setBalanceAvailable(abstractEstimateService.getBudgetAvailable(parentEstimate,
                    endingDate));
            estimateAppropriation.setBudgetUsage(budgetUsage);
        } else {
            estimateAppropriation = new AbstractEstimateAppropriation();
            estimateAppropriation.setAbstractEstimate(revisionEstimate);
            estimateAppropriation.setBalanceAvailable(abstractEstimateService.getBudgetAvailable(parentEstimate,
                    endingDate));
            estimateAppropriation.setBudgetUsage(budgetUsage);
        }
        estimateAppropriationService.persist(estimateAppropriation);
    }

    private boolean checkForBudgetaryAppropriationForDepositWorks(final RevisionAbstractEstimate revisionEstimate,
            final String appropriationNumber)
                    throws ValidationException {
        boolean flag = false;
        final Date appDate = new Date();
        double depApprAmnt = 0.0;
        final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("DEPOSITCODE");
        depApprAmnt = revisionEstimate.getTotalAmount().getValue();

        final FinancialDetail financialDetail = revisionEstimate.getParent().getFinancialDetails().get(0);
        final BigDecimal creditBalance = egovCommon.getDepositAmountForDepositCode(new Date(), financialDetail.getCoa()
                .getGlcode(), financialDetail.getFund().getCode(), accountdetailtype.getId(), financialDetail
                        .getAbstractEstimate().getDepositCode().getId().intValue());

        BigDecimal utilizedAmt = depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(financialDetail,
                appDate);
        BigDecimal balance = BigDecimal.ZERO;
        if (utilizedAmt == null) {
            balance = creditBalance;
            utilizedAmt = BigDecimal.ZERO;
        } else
            balance = creditBalance.subtract(utilizedAmt);

        if (balance.doubleValue() >= depApprAmnt) {
            DepositWorksUsage depositWorksUsage = new DepositWorksUsage();
            final CFinancialYear budgetApprDate_finYear = finHibernateDao.getFinYearByDate(appDate);
            depositWorksUsage.setTotalDepositAmount(creditBalance);
            depositWorksUsage.setConsumedAmount(new BigDecimal(depApprAmnt));
            depositWorksUsage.setReleasedAmount(BigDecimal.ZERO);
            depositWorksUsage.setAppropriationNumber(appropriationNumber);
            depositWorksUsage.setAbstractEstimate(revisionEstimate);
            depositWorksUsage.setAppropriationDate(appDate);
            depositWorksUsage.setFinancialYear(budgetApprDate_finYear);
            depositWorksUsage.setDepositCode(financialDetail.getAbstractEstimate().getDepositCode());
            depositWorksUsage.setCoa(financialDetail.getCoa());
            depositWorksUsage = depositWorksUsageService.persist(depositWorksUsage);
            persistDepositCodeAppDetails(depositWorksUsage, financialDetail);
            flag = true;
        }
        if (!flag)
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "reEstimate.estimate.validate.budget.amount", "Insufficient funds available")));
        return flag;
    }

    private void persistDepositCodeAppDetails(final DepositWorksUsage depositWorksUsage,
            final FinancialDetail financialDetail) {
        AbstractEstimateAppropriation estimateAppropriation = null;
        final int finYearId = finHibernateDao.getFinYearByDate(new Date()).getId().intValue();
        final BigDecimal creditBalance = depositWorksUsage.getTotalDepositAmount();
        final AbstractEstimate abstractEstimate = depositWorksUsage.getAbstractEstimate();
        BigDecimal utilizedAmt = depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(financialDetail,
                depositWorksUsage.getCreatedDate());
        BigDecimal balance = BigDecimal.ZERO;
        if (utilizedAmt == null) {
            balance = creditBalance;
            utilizedAmt = BigDecimal.ZERO;
        } else
            balance = creditBalance.subtract(utilizedAmt);

        estimateAppropriation = estimateAppropriationService.findByNamedQuery(
                "getDepositWorksUsageForEstimateByFinYear", abstractEstimate.getId(), finYearId);
        if (estimateAppropriation != null) {
            estimateAppropriation.setBalanceAvailable(balance);
            estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
        } else {
            estimateAppropriation = new AbstractEstimateAppropriation();
            estimateAppropriation.setAbstractEstimate(abstractEstimate);
            estimateAppropriation.setBalanceAvailable(balance);
            estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
        }
        estimateAppropriationService.persist(estimateAppropriation);
    }

    private boolean releaseBudgetOnReject(final RevisionAbstractEstimate revisionEstimate,
            final FinancialDetail financialDetail, final String budgetRejectionNumber) throws ValidationException {
        boolean flag = false;
        final AbstractEstimateAppropriation estimateAppropriation = estimateAppropriationService.findByNamedQuery(
                "getLatestBudgetUsageForEstimate", revisionEstimate.getId());
        // No budget appropriation was done for this revision estimate
        if (estimateAppropriation == null)
            return flag;
        final List<Long> budgetheadid = new ArrayList<Long>();
        budgetheadid.add(financialDetail.getBudgetGroup().getId());
        BudgetUsage budgetUsage = null;

        budgetUsage = budgetDetailsDAO.releaseEncumbranceBudget(
                budgetRejectionNumber == null ? null : budgetRejectionNumber,
                estimateAppropriation.getBudgetUsage().getFinancialYearId().longValue(),
                Integer.valueOf(11),
                revisionEstimate.getEstimateNumber(),
                Integer.parseInt(financialDetail.getAbstractEstimate().getUserDepartment().getId().toString()),
                financialDetail.getFunction() == null ? null : financialDetail.getFunction().getId(),
                financialDetail.getFunctionary() == null ? null : financialDetail.getFunctionary().getId(),
                financialDetail.getScheme() == null ? null : financialDetail.getScheme().getId(),
                financialDetail.getSubScheme() == null ? null : financialDetail.getSubScheme().getId(),
                financialDetail.getAbstractEstimate().getWard() == null ? null : Integer.parseInt(financialDetail
                        .getAbstractEstimate().getWard().getId().toString()),
                financialDetail.getBudgetGroup() == null ? null : budgetheadid,
                financialDetail.getFund() == null ? null : financialDetail.getFund().getId(), estimateAppropriation
                        .getBudgetUsage().getConsumedAmount());

        if (financialDetail.getAbstractEstimate() != null)
            persistBudgetReleaseDetails(revisionEstimate, financialDetail.getAbstractEstimate(), budgetUsage);
        flag = true;
        return flag;
    }

    private void persistBudgetReleaseDetails(final RevisionAbstractEstimate revisionEstimate,
            final AbstractEstimate parentEstimate, final BudgetUsage budgetUsage) {
        AbstractEstimateAppropriation estimateAppropriation = null;
        estimateAppropriation = estimateAppropriationService.findByNamedQuery("getLatestBudgetUsageForEstimate",
                revisionEstimate.getId());
        final Integer finYearId = estimateAppropriation.getBudgetUsage().getFinancialYearId();
        final Date endingDate = finHibernateDao.getFinancialYearById(finYearId.longValue()).getEndingDate();
        estimateAppropriation.setBalanceAvailable(abstractEstimateService
                .getBudgetAvailable(parentEstimate, endingDate));
        estimateAppropriation.setBudgetUsage(budgetUsage);
        estimateAppropriationService.persist(estimateAppropriation);
    }

    @Override
    public boolean isDepositWorksType(final AbstractEstimate estimate) {
        boolean isDepositWorks = false;
        final List<String> depositTypeList = getAppConfigValuesToSkipBudget();
        for (final String type : depositTypeList)
            if (type.equals(estimate.getNatureOfWork().getName()))
                isDepositWorks = true;
        return isDepositWorks;
    }

    @Override
    public boolean getShowBudgetFolio(final AbstractEstimate revisionEstimate) {
        if (revisionEstimate != null && revisionEstimate.getEgwStatus() != null && revisionEstimate.getParent() != null
                && !isDepositWorksType(revisionEstimate.getParent()))
            return estimateAppropriationService.findByNamedQuery("getLatestBudgetUsageForEstimate",
                    revisionEstimate.getId()) != null ? true : false;
        else
            return false;
    }

    @Override
    public boolean getShowDepositFolio(final AbstractEstimate revisionEstimate) {
        if (revisionEstimate != null && revisionEstimate.getEgwStatus() != null && revisionEstimate.getParent() != null
                && isDepositWorksType(revisionEstimate.getParent()))
            return estimateAppropriationService.findByNamedQuery("getLatestDepositWorksUsageForEstimate",
                    revisionEstimate.getId()) != null ? true : false;
        else
            return false;
    }

    private List<String> getAppConfigValuesToSkipBudget() {
        return worksService.getNatureOfWorkAppConfigValues(MODULE_NAME, KEY_NAME);
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public void setDepositWorksUsageService(final DepositWorksUsageService depositWorksUsageService) {
        this.depositWorksUsageService = depositWorksUsageService;
    }

    public void setEstimateAppropriationService(
            final PersistenceService<AbstractEstimateAppropriation, Long> estimateAppropriationService) {
        this.estimateAppropriationService = estimateAppropriationService;
    }

    public void setBudgetDetailsDAO(final BudgetDetailsDAO budgetDetailsDAO) {
        this.budgetDetailsDAO = budgetDetailsDAO;
    }

}
