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
package org.egov.works.lineestimate.service;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.dao.AccountdetailtypeHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.model.budget.BudgetUsage;
import org.egov.works.lineestimate.entity.LineEstimateAppropriation;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.repository.LineEstimateAppropriationRepository;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.services.ProjectCodeService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineEstimateDetailService {

    private final LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Autowired
    private AccountdetailtypeHibernateDAO accountdetailtypeHibernateDAO;

    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;

    @Autowired
    private ProjectCodeService projectCodeService;

    @Autowired
    private LineEstimateAppropriationRepository lineEstimateAppropriationRepository;

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @Autowired
    private LineEstimateAppropriationService lineEstimateAppropriationService;

    @Autowired
    private WorkOrderIdentificationNumberGenerator workOrderIdentificationNumberGenerator;

    @Autowired
    public LineEstimateDetailService(final LineEstimateDetailsRepository lineEstimateDetailsRepository) {
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setProjectCode(final LineEstimateDetails lineEstimateDetails) {
        ProjectCode projectCode = null;
        if (lineEstimateDetails.getProjectCode() != null && lineEstimateDetails.getLineEstimate().isSpillOverFlag()) {
            projectCode = lineEstimateDetails.getProjectCode();
            projectCode.setCode(lineEstimateDetails.getProjectCode().getCode());
        } else {
            projectCode = new ProjectCode();
            projectCode
                    .setCode(workOrderIdentificationNumberGenerator.generateWorkOrderIdentificationNumber(lineEstimateDetails));
            lineEstimateDetails.setProjectCode(projectCode);
        }
        projectCode.setCodeName(lineEstimateDetails.getNameOfWork());
        projectCode.setDescription(lineEstimateDetails.getNameOfWork());
        projectCode.setActive(true);
        projectCode.setEgwStatus(lineEstimateAppropriationService.getStatusByModuleAndCode(
                ProjectCode.class.getSimpleName(), WorksConstants.DEFAULT_PROJECTCODE_STATUS));
        projectCodeService.persist(projectCode);
        createAccountDetailKey(projectCode);
    }

    protected void createAccountDetailKey(final ProjectCode proj) {
        final Accountdetailtype accountdetailtype = accountdetailtypeHibernateDAO.getAccountdetailtypeByName("PROJECTCODE");
        final Accountdetailkey adk = new Accountdetailkey();
        adk.setGroupid(1);
        adk.setDetailkey(proj.getId().intValue());
        adk.setDetailname(accountdetailtype.getAttributename());
        adk.setAccountdetailtype(accountdetailtype);
        accountdetailkeyHibernateDAO.create(adk);

    }

    @Transactional
    public void persistBudgetAppropriationDetails(final LineEstimateDetails lineEstimateDetails,
            final BudgetUsage budgetUsage) {
        LineEstimateAppropriation lineEstimateAppropriation = null;
        lineEstimateAppropriation = lineEstimateAppropriationService
                .findLatestByLineEstimateDetails_EstimateNumber(lineEstimateDetails.getEstimateNumber());

        if (lineEstimateAppropriation != null)
            lineEstimateAppropriation.setBudgetUsage(budgetUsage);
        else {
            lineEstimateAppropriation = new LineEstimateAppropriation();
            lineEstimateAppropriation.setLineEstimateDetails(lineEstimateDetails);
            lineEstimateAppropriation.setBudgetUsage(budgetUsage);
        }
        lineEstimateAppropriationRepository.save(lineEstimateAppropriation);
    }

    @Transactional
    public boolean checkConsumeEncumbranceBudget(final LineEstimateDetails lineEstimateDetails, final Long finyrId,
            final double budgApprAmnt, final List<Long> budgetheadid) {
        final boolean flag = true;
        final BudgetUsage budgetUsage = budgetDetailsDAO.consumeEncumbranceBudget(
                lineEstimateAppropriationService.generateBudgetAppropriationNumber(lineEstimateDetails),
                finyrId,
                Integer.valueOf(11),
                lineEstimateDetails.getEstimateNumber(),
                Integer.parseInt(lineEstimateDetails.getLineEstimate().getExecutingDepartment().getId().toString()),
                lineEstimateDetails.getLineEstimate().getFunction() == null ? null : lineEstimateDetails.getLineEstimate()
                        .getFunction().getId(),
                null,
                lineEstimateDetails.getLineEstimate().getScheme() == null ? null : lineEstimateDetails.getLineEstimate()
                        .getScheme().getId(),
                lineEstimateDetails.getLineEstimate().getSubScheme() == null ? null : lineEstimateDetails.getLineEstimate()
                        .getSubScheme().getId(),
                lineEstimateDetails.getLineEstimate().getWard() == null ? null : Integer.parseInt(lineEstimateDetails
                        .getLineEstimate().getWard().getId().toString()),
                budgetheadid,
                lineEstimateDetails.getLineEstimate().getFund() == null ? null : lineEstimateDetails.getLineEstimate().getFund()
                        .getId(),
                budgApprAmnt);

        if (budgetUsage != null)
            persistBudgetAppropriationDetails(lineEstimateDetails, budgetUsage);
        else
            return false;

        return flag;
    }
    
    public LineEstimateDetails getLineEstimateDetailsByProjectCode(final String workIdentificationNumber) {
        return lineEstimateDetailsRepository.findByProjectCode_codeAndLineEstimate_Status_CodeNotLike(workIdentificationNumber,
                WorksConstants.CANCELLED_STATUS);
    }

    public LineEstimateDetails getLineEstimateDetailsByEstimateNumber(final String estimateNumber) {
        return lineEstimateDetailsRepository.findByEstimateNumberAndLineEstimate_Status_CodeNot(estimateNumber,
                WorksConstants.CANCELLED_STATUS);
    }
    
}
