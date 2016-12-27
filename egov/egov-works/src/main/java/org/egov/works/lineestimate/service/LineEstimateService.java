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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.model.budget.BudgetUsage;
import org.egov.pims.commons.Position;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.EstimatePhotographSearchRequest;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.autonumber.BudgetAppropriationNumberGenerator;
import org.egov.works.autonumber.EstimateNumberGenerator;
import org.egov.works.autonumber.LineEstimateNumberGenerator;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateAppropriation;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.LineEstimateForLoaSearchRequest;
import org.egov.works.lineestimate.entity.LineEstimateForLoaSearchResult;
import org.egov.works.lineestimate.entity.LineEstimateSearchRequest;
import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
import org.egov.works.lineestimate.repository.LineEstimateAppropriationRepository;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.lineestimate.repository.LineEstimateRepository;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class LineEstimateService {

    private static final Logger LOG = LoggerFactory.getLogger(LineEstimateService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final LineEstimateRepository lineEstimateRepository;

    private final LineEstimateDetailsRepository lineEstimateDetailsRepository;

    private final LineEstimateAppropriationRepository lineEstimateAppropriationRepository;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<LineEstimate> lineEstimateWorkflowService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @Autowired
    private LineEstimateDetailService lineEstimateDetailService;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private BudgetControlTypeService budgetControlTypeService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public LineEstimateService(final LineEstimateRepository lineEstimateRepository,
            final LineEstimateDetailsRepository lineEstimateDetailsRepository,
            final LineEstimateAppropriationRepository lineEstimateAppropriationRepository) {
        this.lineEstimateRepository = lineEstimateRepository;
        this.lineEstimateDetailsRepository = lineEstimateDetailsRepository;
        this.lineEstimateAppropriationRepository = lineEstimateAppropriationRepository;
    }

    public LineEstimate getLineEstimateById(final Long id) {
        return lineEstimateRepository.findById(id);
    }

    @Transactional
    public LineEstimate create(final LineEstimate lineEstimate, final MultipartFile[] files,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) throws IOException {
        lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                LineEstimateStatus.CREATED.toString()));
        final CFinancialYear financialYear = getCurrentFinancialYear(lineEstimate.getLineEstimateDate());
        mergeLineEstimateDetails(lineEstimate);
        for (final LineEstimateDetails lineEstimateDetail : lineEstimate.getLineEstimateDetails()) {
            final EstimateNumberGenerator e = beanResolver.getAutoNumberServiceFor(EstimateNumberGenerator.class);
            final String estimateNumber = e.getNextNumber(lineEstimate, financialYear);
            lineEstimateDetail.setEstimateNumber(estimateNumber);
            lineEstimateDetail.setLineEstimate(lineEstimate);
        }
        if (lineEstimate.getLineEstimateNumber() == null || lineEstimate.getLineEstimateNumber().isEmpty()) {

            final LineEstimateNumberGenerator l = beanResolver.getAutoNumberServiceFor(LineEstimateNumberGenerator.class);

            final String lineEstimateNumber = l.getNextNumber(lineEstimate);
            lineEstimate.setLineEstimateNumber(lineEstimateNumber);
        }

        final LineEstimate newLineEstimate = lineEstimateRepository.save(lineEstimate);

        createLineEstimateWorkflowTransition(newLineEstimate,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        lineEstimateRepository.save(newLineEstimate);

        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, newLineEstimate,
                WorksConstants.MODULE_NAME_LINEESTIMATE);
        if (!documentDetails.isEmpty()) {
            newLineEstimate.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return newLineEstimate;
    }

    private LineEstimate update(final LineEstimate lineEstimate, final String removedLineEstimateDetailsIds,
            final MultipartFile[] files, final CFinancialYear financialYear) throws IOException {
        mergeLineEstimateDetails(lineEstimate);
        for (final LineEstimateDetails lineEstimateDetails : lineEstimate.getLineEstimateDetails())
            if (lineEstimateDetails != null && lineEstimateDetails.getId() == null) {
                final EstimateNumberGenerator e = beanResolver.getAutoNumberServiceFor(EstimateNumberGenerator.class);
                final String estimateNumber = e.getNextNumber(lineEstimate, financialYear);
                lineEstimateDetails.setEstimateNumber(estimateNumber);
                lineEstimateDetails.setLineEstimate(lineEstimate);
            }
        List<LineEstimateDetails> list = new ArrayList<LineEstimateDetails>(lineEstimate.getLineEstimateDetails());
        list = removeDeletedLineEstimateDetails(list, removedLineEstimateDetailsIds);

        lineEstimate.setLineEstimateDetails(list);
        final LineEstimate persistedLineEstimate = lineEstimateRepository.save(lineEstimate);

        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, persistedLineEstimate,
                WorksConstants.MODULE_NAME_LINEESTIMATE);
        if (!documentDetails.isEmpty()) {
            persistedLineEstimate.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return lineEstimateRepository.save(persistedLineEstimate);
    }

    public LineEstimate getLineEstimateByLineEstimateNumber(final String lineEstimateNumber) {
        return lineEstimateRepository.findByLineEstimateNumber(lineEstimateNumber);
    }

    public List<LineEstimateDetails> removeDeletedLineEstimateDetails(final List<LineEstimateDetails> list,
            final String removedLineEstimateDetailsIds) {
        final List<LineEstimateDetails> details = new ArrayList<LineEstimateDetails>();
        if (null != removedLineEstimateDetailsIds) {
            final String[] ids = removedLineEstimateDetailsIds.split(",");
            final List<String> strList = new ArrayList<String>();
            for (final String str : ids)
                strList.add(str);
            for (final LineEstimateDetails line : list)
                if (line.getId() != null) {
                    if (!strList.contains(line.getId().toString()))
                        details.add(line);
                } else
                    details.add(line);
        } else
            return list;
        return details;
    }

    public List<LineEstimate> searchLineEstimates(final LineEstimateSearchRequest lineEstimateSearchRequest) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(LineEstimate.class)
                .createAlias("lineEstimateDetails", "lineEstimateDetail");
        if (lineEstimateSearchRequest != null) {
            if (lineEstimateSearchRequest.getAdminSanctionNumber() != null)
                criteria.add(
                        Restrictions.eq("adminSanctionNumber", lineEstimateSearchRequest.getAdminSanctionNumber()).ignoreCase());
            if (lineEstimateSearchRequest.getBudgetHead() != null)
                criteria.add(Restrictions.eq("budgetHead.id", lineEstimateSearchRequest.getBudgetHead()));
            if (lineEstimateSearchRequest.getExecutingDepartment() != null)
                criteria.add(Restrictions.eq("executingDepartment.id", lineEstimateSearchRequest.getExecutingDepartment()));
            if (lineEstimateSearchRequest.getFunction() != null)
                criteria.add(Restrictions.eq("function.id", lineEstimateSearchRequest.getFunction()));
            if (lineEstimateSearchRequest.getFund() != null)
                criteria.add(Restrictions.eq("fund.id", lineEstimateSearchRequest.getFund().intValue()));
            if (lineEstimateSearchRequest.getEstimateNumber() != null)
                criteria.add(Restrictions.eq("lineEstimateNumber", lineEstimateSearchRequest.getEstimateNumber()).ignoreCase());
            if (lineEstimateSearchRequest.getAdminSanctionFromDate() != null)
                criteria.add(Restrictions.ge("adminSanctionDate", lineEstimateSearchRequest.getAdminSanctionFromDate()));
            if (lineEstimateSearchRequest.getAdminSanctionToDate() != null)
                criteria.add(Restrictions.le("adminSanctionDate", lineEstimateSearchRequest.getAdminSanctionToDate()));

            criteria.add(Restrictions.eq("spillOverFlag", lineEstimateSearchRequest.isSpillOverFlag()));

        }

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<LineEstimateDetails> searchLineEstimatesForLoa(
            final LineEstimateForLoaSearchRequest lineEstimateForLoaSearchRequest) {

        final List<String> lineEstimateNumbers = lineEstimateDetailsRepository
                .findEstimateNumbersToSearchLineEstimatesForLoa(WorksConstants.CANCELLED_STATUS);

        if (!lineEstimateNumbers.isEmpty()) {
            final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(LineEstimateDetails.class)
                    .createAlias("lineEstimate", "lineEstimate")
                    .createAlias("lineEstimate.status", "status")
                    .createAlias("projectCode", "projectCode");
            if (lineEstimateForLoaSearchRequest != null) {
                if (lineEstimateForLoaSearchRequest.getAdminSanctionNumber() != null)
                    criteria.add(Restrictions.ilike("lineEstimate.adminSanctionNumber",
                            lineEstimateForLoaSearchRequest.getAdminSanctionNumber()));
                if (lineEstimateForLoaSearchRequest.getExecutingDepartment() != null)
                    criteria.add(Restrictions.eq("lineEstimate.executingDepartment.id",
                            lineEstimateForLoaSearchRequest.getExecutingDepartment()));
                if (lineEstimateForLoaSearchRequest.getEstimateNumber() != null)
                    criteria.add(Restrictions.eq("estimateNumber",
                            lineEstimateForLoaSearchRequest.getEstimateNumber()).ignoreCase());
                if (lineEstimateForLoaSearchRequest.getAdminSanctionFromDate() != null)
                    criteria.add(Restrictions.ge("lineEstimate.adminSanctionDate",
                            lineEstimateForLoaSearchRequest.getAdminSanctionFromDate()));
                if (lineEstimateForLoaSearchRequest.getAdminSanctionToDate() != null)
                    criteria.add(Restrictions.le("lineEstimate.adminSanctionDate",
                            lineEstimateForLoaSearchRequest.getAdminSanctionToDate()));
                if (lineEstimateForLoaSearchRequest.getLineEstimateCreatedBy() != null)
                    criteria.add(Restrictions.eq("lineEstimate.createdBy.id",
                            lineEstimateForLoaSearchRequest.getLineEstimateCreatedBy()));
                if (lineEstimateForLoaSearchRequest.getWorkIdentificationNumber() != null)
                    criteria.add(Restrictions.eq("projectCode.code",
                            lineEstimateForLoaSearchRequest.getWorkIdentificationNumber()).ignoreCase());
                criteria.add(Restrictions.in("estimateNumber", lineEstimateNumbers));
                criteria.add(Restrictions.eq("status.code", LineEstimateStatus.TECHNICAL_SANCTIONED.toString()));

                criteria.add(Restrictions.eq("lineEstimate.spillOverFlag", lineEstimateForLoaSearchRequest.isSpillOverFlag()));
            }

            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            return criteria.list();
        } else
            return new ArrayList<LineEstimateDetails>();
    }

    public List<String> findLineEstimateNumbers(final String name) {
        final List<LineEstimate> lineEstimates = lineEstimateRepository
                .findByLineEstimateNumberContainingIgnoreCase(name);
        final List<String> results = new ArrayList<String>();
        for (final LineEstimate details : lineEstimates)
            results.add(details.getLineEstimateNumber());
        return results;
    }

    public List<String> findEstimateNumbersForLoa(final String name) {
        final List<String> lineEstimateNumbers = lineEstimateDetailsRepository.findEstimateNumbersForLoa("%" + name + "%",
                LineEstimateStatus.TECHNICAL_SANCTIONED.toString(), WorksConstants.CANCELLED_STATUS);

        return lineEstimateNumbers;
    }

    public List<String> findAdminSanctionNumbers(final String name) {
        final List<LineEstimate> lineEstimates = lineEstimateRepository.findByAdminSanctionNumberContainingIgnoreCase(name);
        final List<String> results = new ArrayList<String>();
        for (final LineEstimate estimate : lineEstimates)
            results.add(estimate.getAdminSanctionNumber());
        return results;
    }

    public List<String> findAdminSanctionNumbersForLoa(final String name) {
        final List<String> adminSanctionNumbers = lineEstimateDetailsRepository.findAdminSanctionNumbersForLoa("%" + name + "%",
                LineEstimateStatus.TECHNICAL_SANCTIONED.toString(), WorksConstants.CANCELLED_STATUS);

        return adminSanctionNumbers;
    }

    public List<String> findWorkIdentificationNumbersToSearchLineEstimatesForLoa(final String name) {
        final List<String> workIdNumbers = lineEstimateDetailsRepository
                .findWorkIdentificationNumbersToSearchLineEstimatesForLoa("%" + name + "%",
                        WorksConstants.CANCELLED_STATUS);

        return workIdNumbers;
    }

    public List<LineEstimateForLoaSearchResult> searchLineEstimatesForLOA(
            final LineEstimateForLoaSearchRequest lineEstimateForLoaSearchRequest) {
        final List<LineEstimateDetails> lineEstimateDetails = searchLineEstimatesForLoa(lineEstimateForLoaSearchRequest);
        final List<LineEstimateForLoaSearchResult> lineEstimateForLoaSearchResults = new ArrayList<LineEstimateForLoaSearchResult>();
        for (final LineEstimateDetails led : lineEstimateDetails) {
            final LineEstimateForLoaSearchResult result = new LineEstimateForLoaSearchResult();
            result.setId(led.getLineEstimate().getId());
            result.setAdminSanctionNumber(led.getLineEstimate().getAdminSanctionNumber());
            result.setCreatedBy(led.getLineEstimate().getCreatedBy().getName());
            result.setEstimateAmount(led.getEstimateAmount());
            result.setEstimateNumber(led.getEstimateNumber());
            result.setNameOfWork(led.getNameOfWork());
            if (led.getLineEstimate().getAdminSanctionBy() != null)
                result.setAdminSanctionBy(led.getLineEstimate().getAdminSanctionBy());
            result.setActualEstimateAmount(led.getActualEstimateAmount());
            result.setWorkIdentificationNumber(led.getProjectCode().getCode());
            lineEstimateForLoaSearchResults.add(result);
        }
        return lineEstimateForLoaSearchResults;
    }

    public List<Hashtable<String, Object>> getHistory(final State state, final List<StateHistory> history) {
        User user = null;
        final List<Hashtable<String, Object>> historyTable = new ArrayList<Hashtable<String, Object>>();
        final Hashtable<String, Object> map = new Hashtable<String, Object>(0);
        if (null != state) {
            if (!history.isEmpty() && history != null)
                Collections.reverse(history);
            for (final StateHistory stateHistory : history) {
                final Hashtable<String, Object> HistoryMap = new Hashtable<String, Object>(0);
                HistoryMap.put("date", stateHistory.getDateInfo());
                HistoryMap.put("comments", stateHistory.getComments());
                HistoryMap.put("updatedBy", stateHistory.getLastModifiedBy().getUsername() + "::"
                        + stateHistory.getLastModifiedBy().getName());
                HistoryMap.put("status", stateHistory.getValue());
                final Position owner = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (null != user) {
                    HistoryMap.put("user", user.getUsername() + "::" + user.getName());
                    HistoryMap.put("department",
                            null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                                    .getDepartmentForUser(user.getId()).getName() : "");
                } else if (null != owner && null != owner.getDeptDesig()) {
                    user = eisCommonService.getUserForPosition(owner.getId(), new Date());
                    HistoryMap
                            .put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                    HistoryMap.put("department", null != owner.getDeptDesig().getDepartment() ? owner.getDeptDesig()
                            .getDepartment().getName() : "");
                }
                historyTable.add(HistoryMap);
            }
            map.put("date", state.getDateInfo());
            map.put("comments", state.getComments() != null ? state.getComments() : "");
            map.put("updatedBy", state.getLastModifiedBy().getUsername() + "::" + state.getLastModifiedBy().getName());
            map.put("status", state.getValue());
            final Position ownerPosition = state.getOwnerPosition();
            user = state.getOwnerUser();
            if (null != user) {
                map.put("user", user.getUsername() + "::" + user.getName());
                map.put("department", null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                        .getDepartmentForUser(user.getId()).getName() : "");
            } else if (null != ownerPosition && null != ownerPosition.getDeptDesig()) {
                user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put("user", null != user.getUsername() ? user.getUsername() + "::" + user.getName() : "");
                map.put("department", null != ownerPosition.getDeptDesig().getDepartment() ? ownerPosition
                        .getDeptDesig().getDepartment().getName() : "");
            }
            historyTable.add(map);
        }
        return historyTable;
    }

    public Long getApprovalPositionByMatrixDesignation(final LineEstimate lineEstimate,
            Long approvalPosition, final String additionalRule, final String mode, final String workFlowAction) {
        final WorkFlowMatrix wfmatrix = lineEstimateWorkflowService.getWfMatrix(lineEstimate
                .getStateType(), null, null, additionalRule, lineEstimate.getCurrentState().getValue(), null);
        if (lineEstimate.getStatus() != null && lineEstimate.getStatus().getCode() != null)
            if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.CREATED.toString())
                    && lineEstimate.getState() != null)
                if (mode.equals("edit"))
                    approvalPosition = lineEstimate.getState().getOwnerPosition().getId();
                else
                    approvalPosition = worksUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                            lineEstimate.getState(), lineEstimate.getCreatedBy().getId());
        if (lineEstimate.getStatus().getCode()
                .equals(LineEstimateStatus.CHECKED.toString()))
            approvalPosition = worksUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                    lineEstimate.getState(), lineEstimate.getCreatedBy().getId());
        if (workFlowAction.equals(WorksConstants.CANCEL_ACTION)
                && wfmatrix.getNextState().equals(WorksConstants.WF_STATE_CREATED))
            approvalPosition = null;

        return approvalPosition;
    }

    @Transactional
    public LineEstimate updateLineEstimateDetails(
            final LineEstimate lineEstimate,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction, final String mode, final ReportOutput reportOutput,
            final String removedLineEstimateDetailsIds,
            final MultipartFile[] files, final CFinancialYear financialYear) throws ValidationException, IOException {
        LineEstimate updatedLineEstimate = null;

        if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.REJECTED.toString())) {
            updatedLineEstimate = update(lineEstimate, removedLineEstimateDetailsIds, files, financialYear);
            try {
                if (workFlowAction.equals(WorksConstants.FORWARD_ACTION))
                    lineEstimateStatusChange(updatedLineEstimate, workFlowAction, mode);
            } catch (final ValidationException e) {
                throw new ValidationException(e.getErrors());
            }
        } else
            try {
                if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.BUDGET_SANCTIONED.toString())
                        && workFlowAction.equals(WorksConstants.REJECT_ACTION))
                    resetAdminSanctionDetails(lineEstimate);
                else if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.BUDGET_SANCTIONED.toString()) &&
                        !workFlowAction.equals(WorksConstants.REJECT_ACTION)) {
                    setAdminSanctionByAndDate(lineEstimate);
                    for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails())
                        lineEstimateDetailService.setProjectCode(led);
                } else if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString()) &&
                        !workFlowAction.equals(WorksConstants.REJECT_ACTION)) {
                    setTechnicalSanctionBy(lineEstimate);
                    int i = 0;
                    for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails()) {
                        // TODO:create abstract estimate on technical sanction. Need to remove once Abstract Estimate is in place.
                        if (lineEstimate.getLineEstimateDetails().size() > 1)
                            i++;
                        estimateService.createAbstractEstimateOnLineEstimateTechSanction(led, i);
                    }
                }
                if (lineEstimate.getStatus().getCode()
                        .equals(LineEstimateStatus.CHECKED.toString()) && !workFlowAction.equals(WorksConstants.REJECT_ACTION)) {

                    if (!BudgetControlType.BudgetCheckOption.NONE.toString()
                            .equalsIgnoreCase(budgetControlTypeService.getConfigValue()))
                        doBudgetoryAppropriation(lineEstimate);
                } else if (workFlowAction.equals(WorksConstants.REJECT_ACTION) && lineEstimate.getStatus().getCode()
                        .equals(LineEstimateStatus.BUDGET_SANCTIONED.toString()))
                    if (!BudgetControlType.BudgetCheckOption.NONE.toString()
                            .equalsIgnoreCase(budgetControlTypeService.getConfigValue()))
                        for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails())
                            releaseBudgetOnReject(led, null, null);

                lineEstimateStatusChange(lineEstimate, workFlowAction, mode);
            } catch (final ValidationException e) {
                throw new ValidationException(e.getErrors());
            }
        updatedLineEstimate = lineEstimateRepository.save(lineEstimate);

        createLineEstimateWorkflowTransition(updatedLineEstimate,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        updatedLineEstimate = lineEstimateRepository.save(updatedLineEstimate);

        return updatedLineEstimate;
    }

    private void resetAdminSanctionDetails(final LineEstimate lineEstimate) {
        lineEstimate.setAdminSanctionNumber(null);
        lineEstimate.setCouncilResolutionNumber(null);
        lineEstimate.setCouncilResolutionDate(null);
    }

    private void setAdminSanctionByAndDate(final LineEstimate lineEstimate) {
        lineEstimate.setAdminSanctionDate(new Date());
        lineEstimate.setAdminSanctionBy(securityUtils.getCurrentUser().getName());
    }

    private void setTechnicalSanctionBy(final LineEstimate lineEstimate) {
        lineEstimate.setTechnicalSanctionBy(securityUtils.getCurrentUser());
    }

    private void doBudgetoryAppropriation(final LineEstimate lineEstimate) {
        final List<Long> budgetheadid = new ArrayList<Long>();
        budgetheadid.add(lineEstimate.getBudgetHead().getId());

        for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails()) {
            final BigDecimal appropriationAmount;
            if (lineEstimate.isBillsCreated() && led.getGrossAmountBilled() != null)
                appropriationAmount = led.getEstimateAmount().subtract(led.getGrossAmountBilled());
            else
                appropriationAmount = led.getEstimateAmount();

            if (appropriationAmount.compareTo(BigDecimal.ZERO) == 1) {
                final boolean flag = lineEstimateDetailService.checkConsumeEncumbranceBudget(led,
                        getCurrentFinancialYear(new Date())
                                .getId(),
                        appropriationAmount.doubleValue(), budgetheadid);

                if (!flag)
                    throw new ValidationException("", "error.budgetappropriation.insufficient.amount");
            }
        }
    }

    public void lineEstimateStatusChange(final LineEstimate lineEstimate, final String workFlowAction,
            final String mode) throws ValidationException {
        if (null != lineEstimate && null != lineEstimate.getStatus()
                && null != lineEstimate.getStatus().getCode())
            if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.CREATED.toString())
                    && lineEstimate.getState() != null && workFlowAction.equals(WorksConstants.SUBMIT_ACTION))
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                        LineEstimateStatus.CHECKED.toString()));
            else if (lineEstimate.getStatus().getCode()
                    .equals(LineEstimateStatus.CHECKED.toString()) && !workFlowAction.equals(WorksConstants.REJECT_ACTION))
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                        LineEstimateStatus.BUDGET_SANCTIONED.toString()));
            else if (lineEstimate.getStatus().getCode()
                    .equals(LineEstimateStatus.BUDGET_SANCTIONED.toString())
                    && !workFlowAction.equals(WorksConstants.REJECT_ACTION))
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                        LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString()));
            else if (lineEstimate.getStatus().getCode()
                    .equals(LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString())
                    && !workFlowAction.equals(WorksConstants.REJECT_ACTION))
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                        LineEstimateStatus.TECHNICAL_SANCTIONED.toString()));
            else if (workFlowAction.equals(WorksConstants.REJECT_ACTION))
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                        LineEstimateStatus.REJECTED.toString()));
            else if (lineEstimate.getStatus().getCode()
                    .equals(LineEstimateStatus.REJECTED.toString()) && workFlowAction.equals(WorksConstants.CANCEL_ACTION))
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                        LineEstimateStatus.CANCELLED.toString()));
            else if (lineEstimate.getStatus().getCode()
                    .equals(LineEstimateStatus.REJECTED.toString()) && workFlowAction.equals(WorksConstants.FORWARD_ACTION))
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                        LineEstimateStatus.CREATED.toString()));

    }

    public List<User> getLineEstimateCreatedByUsers() {
        return lineEstimateRepository.getLineEstimateCreatedByUsers(LineEstimateStatus.TECHNICAL_SANCTIONED.toString());
    }

    public List<Department> getUserDepartments(final User currentUser) {
        final List<Assignment> assignments = assignmentService.findByEmployeeAndGivenDate(currentUser.getId(), new Date());
        final List<Department> uniqueDepartmentList = new ArrayList<Department>();
        Department prevDepartment = new Department();
        final Iterator iterator = assignments.iterator();
        while (iterator.hasNext()) {
            final Assignment assignment = (Assignment) iterator.next();
            if (!assignment.getDepartment().getName().equals(prevDepartment.getName()))
                uniqueDepartmentList.add(assignment.getDepartment());
            prevDepartment = assignment.getDepartment();
        }
        return uniqueDepartmentList;
    }

    public LineEstimateDetails findByEstimateNumber(final String estimateNumber) {
        return lineEstimateDetailsRepository.findByEstimateNumberAndLineEstimate_Status_CodeEquals(estimateNumber,
                LineEstimateStatus.TECHNICAL_SANCTIONED.toString());
    }

    public void createLineEstimateWorkflowTransition(final LineEstimate lineEstimate,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null;
        final String currState = "";
        final String natureOfwork = WorksConstants.WORKFLOWTYPE_DISPLAYNAME;

        if (null != lineEstimate.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(lineEstimate.getCreatedBy().getId());
        if (WorksConstants.REJECT_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            final String stateValue = WorksConstants.WF_STATE_REJECTED;
            lineEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition())
                    .withNextAction("")
                    .withNatureOfTask(natureOfwork);

        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            WorkFlowMatrix wfmatrix = null;
            if (null == lineEstimate.getState()) {
                wfmatrix = lineEstimateWorkflowService.getWfMatrix(lineEstimate.getStateType(), null,
                        null, additionalRule, currState, null);
                lineEstimate.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
            } else if (WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = WorksConstants.WF_STATE_CANCELLED;
                wfmatrix = lineEstimateWorkflowService.getWfMatrix(lineEstimate.getStateType(), null,
                        null, additionalRule, lineEstimate.getCurrentState().getValue(), null);
                lineEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction("")
                        .withNatureOfTask(natureOfwork);
            } else {
                wfmatrix = lineEstimateWorkflowService.getWfMatrix(lineEstimate.getStateType(), null,
                        null, additionalRule, lineEstimate.getCurrentState().getValue(), null);
                lineEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    public LineEstimate getLineEstimateByTechnicalSanctionNumber(final String technicalSanctionNumber) {
        return lineEstimateRepository.findByTechnicalSanctionNumberIgnoreCaseAndStatus_CodeNot(technicalSanctionNumber,
                LineEstimateStatus.CANCELLED.toString());
    }

    public CFinancialYear getCurrentFinancialYear(final Date estimateDate) {
        return financialYearHibernateDAO.getFinYearByDate(estimateDate);
    }

    public LineEstimate getLineEstimateByAdminSanctionNumber(final String adminSanctionNumber) {
        return lineEstimateRepository.findByAdminSanctionNumberIgnoreCaseAndStatus_codeNotLike(adminSanctionNumber,
                WorksConstants.CANCELLED_STATUS);
    }

    public boolean releaseBudgetOnReject(final LineEstimateDetails lineEstimateDetails, Double budgApprAmnt,
            String appropriationnumber) throws ValidationException {

        final LineEstimateAppropriation lineEstimateAppropriation = lineEstimateAppropriationRepository
                .findLatestByLineEstimateDetails_EstimateNumber(lineEstimateDetails.getEstimateNumber());
        final List<Long> budgetheadid = new ArrayList<Long>();
        budgetheadid.add(lineEstimateDetails.getLineEstimate().getBudgetHead().getId());
        BudgetUsage budgetUsage = null;
        final boolean flag = false;

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
                    lineEstimateAppropriation.getBudgetUsage().getFinancialYearId().longValue(),
                    Integer.valueOf(11),
                    lineEstimateAppropriation.getLineEstimateDetails().getEstimateNumber(),
                    Integer.parseInt(lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate()
                            .getExecutingDepartment()
                            .getId().toString()),
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getFunction() == null ? null
                            : lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getFunction().getId(),
                    null,
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getScheme() == null ? null
                            : lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getScheme().getId(),
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getSubScheme() == null ? null
                            : lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getSubScheme().getId(),
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getWard() == null ? null : Integer
                            .parseInt(lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getWard().getId()
                                    .toString()),
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getBudgetHead() == null ? null
                            : budgetheadid,
                    lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getFund() == null ? null
                            : lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getFund().getId(),
                    budgApprAmnt);

            if (lineEstimateAppropriation.getLineEstimateDetails() != null)
                persistBudgetReleaseDetails(lineEstimateDetails, budgetUsage);
        }
        return flag;
    }

    private void persistBudgetReleaseDetails(final LineEstimateDetails lineEstimateDetails, final BudgetUsage budgetUsage) {
        LineEstimateAppropriation lineEstimateAppropriation = null;
        lineEstimateAppropriation = lineEstimateAppropriationRepository
                .findLatestByLineEstimateDetails_EstimateNumber(lineEstimateDetails.getEstimateNumber());
        lineEstimateAppropriation.setBudgetUsage(budgetUsage);
        lineEstimateAppropriationRepository.save(lineEstimateAppropriation);
    }

    @Transactional
    public LineEstimate createSpillOver(final LineEstimate lineEstimate, final MultipartFile[] files) throws IOException {
        lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                LineEstimateStatus.TECHNICAL_SANCTIONED.toString()));
        lineEstimate.setSpillOverFlag(true);

        if (lineEstimate.getLineEstimateNumber() == null || lineEstimate.getLineEstimateNumber().isEmpty()) {

            final LineEstimateNumberGenerator l = beanResolver.getAutoNumberServiceFor(LineEstimateNumberGenerator.class);
            final String lineEstimateNumber = l.getNextNumber(lineEstimate);
            lineEstimate.setLineEstimateNumber(lineEstimateNumber);
        }

        for (final LineEstimateDetails lineEstimateDetails : lineEstimate.getLineEstimateDetails())
            lineEstimateDetails.setLineEstimate(lineEstimate);

        for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails())
            lineEstimateDetailService.setProjectCode(led);

        final LineEstimate newLineEstimate = lineEstimateRepository.save(lineEstimate);
        if (!BudgetControlType.BudgetCheckOption.NONE.toString()
                .equalsIgnoreCase(budgetControlTypeService.getConfigValue()))
            doBudgetoryAppropriation(lineEstimate);

        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, newLineEstimate,
                WorksConstants.MODULE_NAME_LINEESTIMATE);
        if (!documentDetails.isEmpty()) {
            newLineEstimate.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        int i = 0;
        for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails()) {
            if (lineEstimate.getLineEstimateDetails().size() > 1)
                i++;
            // TODO:create abstract estimate on technical sanction. Need to remove once Abstract Estimate is in place.
            estimateService.createAbstractEstimateOnLineEstimateTechSanction(led, i);
        }
        return newLineEstimate;
    }

    public List<String> getEstimateNumberForDepartment(final Long departmentId) {
        return lineEstimateDetailsRepository.findEstimateNumbersForDepartment(departmentId);
    }

    public List<String> getEstimateNumbersForWorkIdentificationNumber(final String workIdentificationNumber) {
        return lineEstimateDetailsRepository.findEstimateNumbersForWorkIdentificationNumber(workIdentificationNumber);
    }

    public List<String> getEstimateNumbersForSpillOverFlag(final boolean spillOverFlag) {
        return lineEstimateDetailsRepository.findEstimateNumbersForSpillOverFlag(spillOverFlag);
    }

    public List<User> getCreatedByUsersForCancelLineEstimateByDepartment(final Long department) {
        return lineEstimateDetailsRepository.findCreatedByForCancelLineEstimateByDepartment(department,
                LineEstimateStatus.TECHNICAL_SANCTIONED.toString(), WorksConstants.APPROVED);
    }

    public List<LineEstimate> searchLineEstimatesToCancel(final LineEstimateSearchRequest lineEstimateSearchRequest) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(LineEstimate.class)
                .createAlias("lineEstimateDetails", "led")
                .createAlias("executingDepartment", "ed")
                .createAlias("led.projectCode", "pc")
                .createAlias("status", "status");
        if (lineEstimateSearchRequest != null) {
            if (lineEstimateSearchRequest.getExecutingDepartment() != null)
                criteria.add(Restrictions.eq("ed.id", lineEstimateSearchRequest.getExecutingDepartment()));
            if (lineEstimateSearchRequest.getLineEstimateNumber() != null)
                criteria.add(Restrictions.eq("lineEstimateNumber", lineEstimateSearchRequest.getLineEstimateNumber())
                        .ignoreCase());
            if (lineEstimateSearchRequest.getWorkIdentificationNumber() != null)
                criteria.add(
                        Restrictions.ilike("pc.code", lineEstimateSearchRequest.getWorkIdentificationNumber(),
                                MatchMode.ANYWHERE));
        }
        if (lineEstimateSearchRequest.getCreatedBy() != null)
            criteria.add(Restrictions.eq("createdBy.id", lineEstimateSearchRequest.getCreatedBy()));
        if (lineEstimateSearchRequest.isSpillOverFlag())
            criteria.add(Restrictions.eq("spillOverFlag", lineEstimateSearchRequest.isSpillOverFlag()));
        criteria.add(Restrictions.eq("status.code", LineEstimateStatus.TECHNICAL_SANCTIONED.toString()).ignoreCase());
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public String checkIfLOAsCreated(final Long lineEstimateId) {
        final List<String> listString = letterOfAcceptanceService.getEstimateNumbersToSearchLOAToCancel(lineEstimateId);
        String estimateNumbers = "";
        for (final String estimateNumber : listString)
            estimateNumbers += estimateNumber + ", ";
        if (estimateNumbers.equals(""))
            return "";
        else
            return estimateNumbers;
    }

    @Transactional
    public LineEstimate cancel(final LineEstimate lineEstimate) {
        lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                LineEstimateStatus.CANCELLED.toString()));
        if (!BudgetControlType.BudgetCheckOption.NONE.toString()
                .equalsIgnoreCase(budgetControlTypeService.getConfigValue()))
            for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails())
                releaseBudgetOnReject(led, null, null);

        for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails()) {
            final Long id = led.getId();
            final AbstractEstimate abstractEstimate = estimateService
                    .getAbstractEstimateByLineEstimateDetailsForCancelLineEstimate(id);
            if (abstractEstimate != null)
                abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE,
                        AbstractEstimate.EstimateStatus.CANCELLED.toString()));
        }
        return lineEstimateRepository.save(lineEstimate);
    }

    public LineEstimate getLineEstimateByCouncilResolutionNumber(final String councilResolutionNumber) {
        return lineEstimateRepository.findByCouncilResolutionNumberIgnoreCaseAndStatus_codeNotLike(councilResolutionNumber,
                WorksConstants.CANCELLED_STATUS);
    }
    
    public List<String> getEstimateNumbersForEstimatePhotograph(final String estimateNumber) {
        return lineEstimateDetailsRepository.findEstimateNumbersForEstimatePhotograph("%" + estimateNumber + "%",WorksConstants.CANCELLED_STATUS);
    }
    
    public List<String> getWinForEstimatePhotograph(final String workIdentificationNumber) {
        return lineEstimateDetailsRepository.findWorkIdentificationNumberForEstimatePhotograph("%" + workIdentificationNumber + "%",WorksConstants.CANCELLED_STATUS);
    }
    
    public List<LineEstimateDetails> searchLineEstimatesForEstimatePhotograph(final EstimatePhotographSearchRequest estimatePhotographSearchRequest) {
        final StringBuilder queryStr = new StringBuilder(500);

        buildWhereClauseForEstimatePhotograph(estimatePhotographSearchRequest, queryStr);
        final Query query = setParameterForEstimatePhotograph(estimatePhotographSearchRequest, queryStr);
        final List<LineEstimateDetails> lineEstimateDetailsList = query.getResultList();
        return lineEstimateDetailsList;
    }

    private void buildWhereClauseForEstimatePhotograph(final EstimatePhotographSearchRequest estimatePhotographSearchRequest, final StringBuilder queryStr) {

        queryStr.append(
                "select distinct led from LineEstimateDetails as led where led.lineEstimate.status.code != :lineEstimateStatus ");
        
        //TODO : remove this comment when search result need to restrict after create contractor bill
        /*queryStr.append(
                "and not exists(select distinct(cbr.workOrderEstimate.estimate.lineEstimateDetails) from ContractorBillRegister as cbr where cbr.workOrderEstimate.estimate.lineEstimateDetails.id = led.id and upper(cbr.billstatus) != :billstatus and cbr.billtype = :billtype)");*/
        
        if (estimatePhotographSearchRequest.getExecutingDepartment() != null)
            queryStr.append(
                    " and led.lineEstimate.executingDepartment.id = :executingDepartment");
        
        if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getWorkIdentificationNumber()))
            queryStr.append(
                    " and upper(led.projectCode.code) = :workIdentificationNumber");

        if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getEstimateNumber()))
            queryStr.append(" and upper(led.estimateNumber) = :estimateNumber");

        if (estimatePhotographSearchRequest.getFromDate() != null)
            queryStr.append(
                    " and led.lineEstimate.createdDate >= :createdDate");

        if (estimatePhotographSearchRequest.getToDate() != null)
            queryStr.append(
                    " and led.lineEstimate.createdDate >= :createdDate");
        
        if (estimatePhotographSearchRequest.getNatureOfWork() != null)
            queryStr.append(
                    " and led.lineEstimate.natureOfWork.id = :natureOfWork");

    }

    private Query setParameterForEstimatePhotograph(final EstimatePhotographSearchRequest estimatePhotographSearchRequest,
            final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        qry.setParameter("lineEstimateStatus", WorksConstants.CANCELLED_STATUS);
        //TODO : remove this comment when search result need to restrict after create contractor bill
        /*qry.setParameter("billstatus", ContractorBillRegister.BillStatus.CANCELLED.toString());
        qry.setParameter("billtype", BillTypes.Final_Bill.toString());*/
        
        if (estimatePhotographSearchRequest != null) {
            if(estimatePhotographSearchRequest.getExecutingDepartment() != null)
                qry.setParameter("executingDepartment", estimatePhotographSearchRequest.getExecutingDepartment());
            if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getWorkIdentificationNumber()))
                qry.setParameter("workIdentificationNumber",
                        estimatePhotographSearchRequest.getWorkIdentificationNumber().toUpperCase());
            if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getEstimateNumber()))
                qry.setParameter("estimateNumber", estimatePhotographSearchRequest.getEstimateNumber().toUpperCase());
            if (estimatePhotographSearchRequest.getFromDate() != null)
                qry.setParameter("createdDate", estimatePhotographSearchRequest.getFromDate());
            if (estimatePhotographSearchRequest.getToDate() != null)
                qry.setParameter("createdDate", estimatePhotographSearchRequest.getToDate());
            if(estimatePhotographSearchRequest.getNatureOfWork() != null)
                qry.setParameter("natureOfWork", estimatePhotographSearchRequest.getNatureOfWork());

        }
        return qry;
    }

    private void mergeLineEstimateDetails(final LineEstimate lineEstimate) {
        for (final LineEstimateDetails details : lineEstimate.getTempLineEstimateDetails())
            if (details.getId() == null) {
                details.setLineEstimate(lineEstimate);
                lineEstimate.getLineEstimateDetails().add(details);
            } else
                for (final LineEstimateDetails oldDetails : lineEstimate.getLineEstimateDetails())
                    if (oldDetails.getId().equals(details.getId()))
                        updateLineEstimateDetailsValues(oldDetails, details);
    }

    public void updateLineEstimateDetailsValues(final LineEstimateDetails oldDetails, final LineEstimateDetails details) {
        oldDetails.setNameOfWork(details.getNameOfWork());
        oldDetails.setUom(details.getUom());
        oldDetails.setEstimateAmount(details.getEstimateAmount());
        oldDetails.setBeneficiary(details.getBeneficiary());
        oldDetails.setQuantity(details.getQuantity());
    }

}