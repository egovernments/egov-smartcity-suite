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
package org.egov.works.lineestimate.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.budget.BudgetUsage;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.autonumber.BudgetAppropriationNumberGenerator;
import org.egov.works.lineestimate.entity.EstimateAppropriation;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.repository.EstimateAppropriationRepository;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EstimateAppropriationService {

    private final LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Autowired
    private EstimateAppropriationRepository estimateAppropriationRepository;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @Autowired
    public EstimateAppropriationService(final LineEstimateDetailsRepository lineEstimateDetailsRepository) {
        this.lineEstimateDetailsRepository = lineEstimateDetailsRepository;
    }

    @Transactional
    public void save(final LineEstimateDetails lineEstimateDetails) {
        lineEstimateDetailsRepository.save(lineEstimateDetails);
    }

    @Transactional
    public void update(final LineEstimateDetails lineEstimateDetails) {
        lineEstimateDetailsRepository.saveAndFlush(lineEstimateDetails);
    }

    @Transactional
    public void delete(final LineEstimateDetails lineEstimateDetails) {
        lineEstimateDetailsRepository.delete(lineEstimateDetails);
    }

    @Transactional
    public void delete(final List<LineEstimateDetails> lineEstimateDetailsList) {
        lineEstimateDetailsRepository.delete(lineEstimateDetailsList);
    }

    public LineEstimateDetails getById(final Long id) {
        return lineEstimateDetailsRepository.findOne(id);
    }

    public EstimateAppropriation findLatestByLineEstimateDetails(final LineEstimateDetails lineEstimateDetails) {
        return estimateAppropriationRepository
                .findLatestByLineEstimateDetails(lineEstimateDetails);
    }

    public EstimateAppropriation findLatestByAbstractEstimate(final AbstractEstimate abstractEstimate) {
        return estimateAppropriationRepository
                .findLatestByAbstractEstimate(abstractEstimate);
    }

    public EstimateAppropriation findLatestByAbstractEstimate(final Long abstractEstimateId) {
        return estimateAppropriationRepository
                .findLatestByAbstractEstimate_Id(abstractEstimateId);
    }

    public EstimateAppropriation findLatestByBudgetUsage(final Long budgetUsageId) {
        return estimateAppropriationRepository
                .findByBudgetUsage_Id(budgetUsageId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public String generateBudgetAppropriationNumber(final Date estimateDate) {
        final BudgetAppropriationNumberGenerator e = beanResolver
                .getAutoNumberServiceFor(BudgetAppropriationNumberGenerator.class);
        String budgetAppropriationNumber;
        budgetAppropriationNumber = e.getNextNumber(estimateDate);
        return budgetAppropriationNumber;
    }

    @Transactional
    public boolean checkConsumeEncumbranceBudgetForLineEstimate(final LineEstimateDetails lineEstimateDetails, final Long finyrId,
            final double budgApprAmnt, final List<Long> budgetheadid) {
        final boolean flag = true;
        BudgetUsage budgetUsage = null;
        final LineEstimate lineEstimate = lineEstimateDetails.getLineEstimate();
        budgetUsage = budgetDetailsDAO.consumeEncumbranceBudget(
                generateBudgetAppropriationNumber(lineEstimate.getLineEstimateDate()),
                finyrId,
                Integer.valueOf(11),
                lineEstimateDetails.getEstimateNumber(),
                Integer.parseInt(lineEstimate.getExecutingDepartment().getId().toString()),
                lineEstimate.getFunction() == null ? null : lineEstimate
                        .getFunction().getId(),
                null,
                lineEstimate.getScheme() == null ? null : lineEstimate
                        .getScheme().getId(),
                lineEstimate.getSubScheme() == null ? null : lineEstimate
                        .getSubScheme().getId(),
                lineEstimate.getWard() == null ? null : Integer.parseInt(lineEstimate.getWard().getId().toString()),
                budgetheadid,
                lineEstimate.getFund() == null ? null
                        : lineEstimate.getFund()
                                .getId(),
                budgApprAmnt);
        if (budgetUsage != null)
            persistBudgetAppropriationDetails(lineEstimateDetails, null, budgetUsage);
        else
            return false;

        return flag;
    }

    @Transactional
    public void persistBudgetAppropriationDetails(final LineEstimateDetails lineEstimateDetails,
            final AbstractEstimate abstractEstimate, final BudgetUsage budgetUsage) {
        EstimateAppropriation lineEstimateAppropriation = null;
        if (lineEstimateDetails != null)
            lineEstimateAppropriation = findLatestByLineEstimateDetails(lineEstimateDetails);
        else
            lineEstimateAppropriation = findLatestByAbstractEstimate(abstractEstimate);

        if (lineEstimateAppropriation != null)
            lineEstimateAppropriation.setBudgetUsage(budgetUsage);
        else {
            lineEstimateAppropriation = new EstimateAppropriation();
            if (lineEstimateDetails != null)
                lineEstimateAppropriation.setLineEstimateDetails(lineEstimateDetails);
            else
                lineEstimateAppropriation.setAbstractEstimate(abstractEstimate);
            lineEstimateAppropriation.setBudgetUsage(budgetUsage);
        }
        estimateAppropriationRepository.save(lineEstimateAppropriation);
    }

    @Transactional
    public boolean checkConsumeEncumbranceBudgetForAbstractEstimate(final AbstractEstimate abstractEstimate, final Long finyrId,
            final double budgApprAmnt, final List<Long> budgetheadid) {
        final boolean flag = true;
        BudgetUsage budgetUsage = null;
        final FinancialDetail financialDetail = abstractEstimate.getFinancialDetails().get(0);
        budgetUsage = budgetDetailsDAO.consumeEncumbranceBudget(
                generateBudgetAppropriationNumber(abstractEstimate.getEstimateDate()),
                finyrId,
                Integer.valueOf(11),
                abstractEstimate.getEstimateNumber(),
                Integer.parseInt(abstractEstimate.getExecutingDepartment().getId().toString()),
                financialDetail.getFunction() == null ? null
                        : financialDetail
                                .getFunction().getId(),
                null,
                financialDetail.getScheme() == null ? null
                        : financialDetail
                                .getScheme().getId(),
                financialDetail.getSubScheme() == null ? null
                        : financialDetail
                                .getSubScheme().getId(),
                abstractEstimate.getWard() == null ? null : Integer.parseInt(abstractEstimate
                        .getWard().getId().toString()),
                budgetheadid,
                financialDetail.getFund() == null ? null
                        : financialDetail.getFund()
                                .getId(),
                budgApprAmnt);

        if (budgetUsage != null)
            persistBudgetAppropriationDetails(null, abstractEstimate, budgetUsage);
        else
            return false;

        return flag;
    }

    @Transactional
    public boolean checkConsumeEncumbranceBudgetForRevisionEstimate(final RevisionAbstractEstimate revisionAbstractEstimate,
            final Long finyrId,
            final double budgApprAmnt, final List<Long> budgetheadid) {
        final boolean flag = true;
        BudgetUsage budgetUsage = null;
        final FinancialDetail financialDetail = revisionAbstractEstimate.getParent().getFinancialDetails().get(0);
        budgetUsage = budgetDetailsDAO.consumeEncumbranceBudget(
                generateBudgetAppropriationNumber(revisionAbstractEstimate.getEstimateDate()),
                finyrId,
                Integer.valueOf(11),
                revisionAbstractEstimate.getEstimateNumber(),
                Integer.parseInt(revisionAbstractEstimate.getParent().getExecutingDepartment().getId().toString()),
                financialDetail.getFunction() == null ? null
                        : financialDetail
                                .getFunction().getId(),
                null,
                financialDetail.getScheme() == null ? null
                        : financialDetail
                                .getScheme().getId(),
                financialDetail.getSubScheme() == null ? null
                        : financialDetail
                                .getSubScheme().getId(),
                revisionAbstractEstimate.getParent().getWard() == null ? null
                        : Integer.parseInt(revisionAbstractEstimate.getParent()
                                .getWard().getId().toString()),
                budgetheadid,
                financialDetail.getFund() == null ? null
                        : financialDetail.getFund()
                                .getId(),
                budgApprAmnt);

        if (budgetUsage != null)
            persistBudgetAppropriationDetails(null, revisionAbstractEstimate, budgetUsage);
        else
            return false;

        return flag;
    }

    @Transactional
    public boolean releaseBudgetOnReject(final LineEstimateDetails lineEstimateDetails, Double budgApprAmnt,
            String appropriationnumber) throws ValidationException {

        final EstimateAppropriation lineEstimateAppropriation = estimateAppropriationRepository
                .findLatestByLineEstimateDetails(lineEstimateDetails);
        final List<Long> budgetheadid = new ArrayList<Long>();
        budgetheadid.add(lineEstimateDetails.getLineEstimate().getBudgetHead().getId());
        BudgetUsage budgetUsage = null;
        final boolean flag = true;

        if (lineEstimateAppropriation != null) {
            if (budgApprAmnt == null)
                budgApprAmnt = lineEstimateAppropriation.getBudgetUsage().getConsumedAmount();

            if (appropriationnumber == null)
                appropriationnumber = lineEstimateAppropriation.getBudgetUsage().getAppropriationnumber();
            final BudgetAppropriationNumberGenerator b = beanResolver
                    .getAutoNumberServiceFor(BudgetAppropriationNumberGenerator.class);
            budgetUsage = budgetDetailsDAO.releaseEncumbranceBudget(
                    lineEstimateAppropriation.getBudgetUsage() == null ? null
                            : b.generateCancelledBudgetAppropriationNumber(appropriationnumber),
                    lineEstimateAppropriation.getBudgetUsage().getFinancialYearId().longValue(), Integer.valueOf(11),
                    lineEstimateAppropriation.getLineEstimateDetails().getEstimateNumber(),
                    Integer.parseInt(lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate()
                            .getExecutingDepartment().getId().toString()),
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getFunction() == null ? null
                            : lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getFunction()
                                    .getId(),
                    null,
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getScheme() == null ? null
                            : lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getScheme().getId(),
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getSubScheme() == null ? null
                            : lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getSubScheme()
                                    .getId(),
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getWard() == null ? null
                            : Integer.parseInt(lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate()
                                    .getWard().getId().toString()),
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getBudgetHead() == null ? null
                            : budgetheadid,
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getFund() == null ? null
                            : lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getFund().getId(),
                    budgApprAmnt);

            if (budgetUsage != null)
                persistBudgetReleaseDetails(lineEstimateDetails, null, budgetUsage);
            else
                return false;
        }
        return flag;
    }

    @Transactional
    public boolean releaseBudgetOnRejectForEstimate(final AbstractEstimate abstractEstimate, Double budgApprAmnt,
            String appropriationnumber) throws ValidationException {

        final EstimateAppropriation estimateAppropriation = estimateAppropriationRepository
                .findLatestByAbstractEstimate(abstractEstimate);
        final List<Long> budgetheadid = new ArrayList<Long>();
        if(abstractEstimate.getParent() != null)
            budgetheadid.add(abstractEstimate.getParent().getFinancialDetails().get(0).getBudgetGroup().getId());
        else
            budgetheadid.add(abstractEstimate.getFinancialDetails().get(0).getBudgetGroup().getId());
        BudgetUsage budgetUsage = null;
        final boolean flag = true;

        if (estimateAppropriation != null) {
            if (budgApprAmnt == null)
                budgApprAmnt = estimateAppropriation.getBudgetUsage().getConsumedAmount();

            if (appropriationnumber == null)
                appropriationnumber = estimateAppropriation.getBudgetUsage().getAppropriationnumber();
            final BudgetAppropriationNumberGenerator b = beanResolver
                    .getAutoNumberServiceFor(BudgetAppropriationNumberGenerator.class);
            if (abstractEstimate.getParent() == null)
                budgetUsage = budgetDetailsDAO.releaseEncumbranceBudget(
                        estimateAppropriation.getBudgetUsage() == null ? null
                                : b.generateCancelledBudgetAppropriationNumber(appropriationnumber),
                        estimateAppropriation.getBudgetUsage().getFinancialYearId().longValue(), Integer.valueOf(11),
                        estimateAppropriation.getAbstractEstimate().getEstimateNumber(),
                        Integer.parseInt(estimateAppropriation.getAbstractEstimate()
                                .getExecutingDepartment().getId().toString()),
                        estimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0).getFunction() == null ? null
                                : estimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0).getFunction()
                                        .getId(),
                        null,
                        estimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0).getScheme() == null ? null
                                : estimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0).getScheme().getId(),
                        estimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0).getSubScheme() == null ? null
                                : estimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0).getSubScheme()
                                        .getId(),
                        estimateAppropriation.getAbstractEstimate().getWard() == null ? null
                                : Integer.parseInt(estimateAppropriation.getAbstractEstimate()
                                        .getWard().getId().toString()),
                        estimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0).getBudgetGroup() == null ? null
                                : budgetheadid,
                        estimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0).getFund() == null ? null
                                : estimateAppropriation.getAbstractEstimate().getFinancialDetails().get(0).getFund().getId(),
                        budgApprAmnt);
            else
                budgetUsage = budgetDetailsDAO.releaseEncumbranceBudget(
                        estimateAppropriation.getBudgetUsage() == null ? null
                                : b.generateCancelledBudgetAppropriationNumber(appropriationnumber),
                        estimateAppropriation.getBudgetUsage().getFinancialYearId().longValue(), Integer.valueOf(11),
                        estimateAppropriation.getAbstractEstimate().getEstimateNumber(),
                        Integer.parseInt(estimateAppropriation.getAbstractEstimate().getParent()
                                .getExecutingDepartment().getId().toString()),
                        estimateAppropriation.getAbstractEstimate().getParent().getFinancialDetails().get(0).getFunction() == null
                                ? null
                                : estimateAppropriation.getAbstractEstimate().getParent().getFinancialDetails().get(0)
                                        .getFunction()
                                        .getId(),
                        null,
                        estimateAppropriation.getAbstractEstimate().getParent().getFinancialDetails().get(0).getScheme() == null
                                ? null
                                : estimateAppropriation.getAbstractEstimate().getParent().getFinancialDetails().get(0).getScheme()
                                        .getId(),
                        estimateAppropriation.getAbstractEstimate().getParent().getFinancialDetails().get(0)
                                .getSubScheme() == null ? null
                                        : estimateAppropriation.getAbstractEstimate().getParent().getFinancialDetails().get(0)
                                                .getSubScheme()
                                                .getId(),
                        estimateAppropriation.getAbstractEstimate().getParent().getWard() == null ? null
                                : Integer.parseInt(estimateAppropriation.getAbstractEstimate().getParent()
                                        .getWard().getId().toString()),
                        estimateAppropriation.getAbstractEstimate().getParent().getFinancialDetails().get(0)
                                .getBudgetGroup() == null ? null
                                        : budgetheadid,
                        estimateAppropriation.getAbstractEstimate().getParent().getFinancialDetails().get(0).getFund() == null
                                ? null
                                : estimateAppropriation.getAbstractEstimate().getParent().getFinancialDetails().get(0).getFund()
                                        .getId(),
                        budgApprAmnt);

            if (budgetUsage != null)
                persistBudgetReleaseDetails(null, abstractEstimate, budgetUsage);
            else
                return false;
        }
        return flag;
    }

    @Transactional
    private void persistBudgetReleaseDetails(final LineEstimateDetails lineEstimateDetails,
            final AbstractEstimate abstractEstimate, final BudgetUsage budgetUsage) {
        EstimateAppropriation lineEstimateAppropriation = null;
        if (lineEstimateDetails != null)
            lineEstimateAppropriation = estimateAppropriationRepository
                    .findLatestByLineEstimateDetails(lineEstimateDetails);
        else
            lineEstimateAppropriation = estimateAppropriationRepository
                    .findLatestByAbstractEstimate(abstractEstimate);
        lineEstimateAppropriation.setBudgetUsage(budgetUsage);
        estimateAppropriationRepository.save(lineEstimateAppropriation);
    }
}