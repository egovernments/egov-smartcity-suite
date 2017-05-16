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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.EstimatePhotographSearchRequest;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.autonumber.EstimateNumberGenerator;
import org.egov.works.autonumber.LineEstimateNumberGenerator;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.LineEstimateSearchRequest;
import org.egov.works.lineestimate.entity.LineEstimatesForAbstractEstimate;
import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class LineEstimateService {

    private static final Logger LOG = LoggerFactory.getLogger(LineEstimateService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final LineEstimateRepository lineEstimateRepository;

    private final LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private WorksUtils worksUtils;

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
    private LineEstimateDetailService lineEstimateDetailService;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private BudgetControlTypeService budgetControlTypeService;

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private CityService cityService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @Autowired
    private EstimateAppropriationService estimateAppropriationService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public LineEstimateService(final LineEstimateRepository lineEstimateRepository,
            final LineEstimateDetailsRepository lineEstimateDetailsRepository) {
        this.lineEstimateRepository = lineEstimateRepository;
        this.lineEstimateDetailsRepository = lineEstimateDetailsRepository;
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
        final CFinancialYear financialYear = worksUtils.getFinancialYearByDate(lineEstimate.getLineEstimateDate());
        mergeLineEstimateDetails(lineEstimate);
        for (final LineEstimateDetails lineEstimateDetail : lineEstimate.getLineEstimateDetails()) {
            final EstimateNumberGenerator e = beanResolver.getAutoNumberServiceFor(EstimateNumberGenerator.class);
            final String estimateNumber = e.getNextNumber(lineEstimate, financialYear);
            lineEstimateDetail.setEstimateNumber(estimateNumber);
            lineEstimateDetail.setLineEstimate(lineEstimate);
        }
        if (lineEstimate.getLineEstimateNumber() == null || lineEstimate.getLineEstimateNumber().isEmpty()) {

            final LineEstimateNumberGenerator l = beanResolver
                    .getAutoNumberServiceFor(LineEstimateNumberGenerator.class);

            final String lineEstimateNumber = l.getNextNumber(lineEstimate);
            lineEstimate.setLineEstimateNumber(lineEstimateNumber);
        }

        final LineEstimate newLineEstimate = lineEstimateRepository.save(lineEstimate);

        createLineEstimateWorkflowTransition(newLineEstimate, approvalPosition, approvalComent, additionalRule,
                workFlowAction);

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
                .createAlias("lineEstimateDetails", "lineEstimateDetail").createAlias("status", "les");
        if (lineEstimateSearchRequest != null) {
            if (lineEstimateSearchRequest.getAdminSanctionNumber() != null)
                criteria.add(Restrictions.eq("adminSanctionNumber", lineEstimateSearchRequest.getAdminSanctionNumber())
                        .ignoreCase());
            if (lineEstimateSearchRequest.getBudgetHead() != null)
                criteria.add(Restrictions.eq("budgetHead.id", lineEstimateSearchRequest.getBudgetHead()));
            if (lineEstimateSearchRequest.getExecutingDepartment() != null)
                criteria.add(
                        Restrictions.eq("executingDepartment.id", lineEstimateSearchRequest.getExecutingDepartment()));
            if (lineEstimateSearchRequest.getFunction() != null)
                criteria.add(Restrictions.eq("function.id", lineEstimateSearchRequest.getFunction()));
            if (lineEstimateSearchRequest.getFund() != null)
                criteria.add(Restrictions.eq("fund.id", lineEstimateSearchRequest.getFund().intValue()));
            if (lineEstimateSearchRequest.getEstimateNumber() != null)
                criteria.add(Restrictions.eq("lineEstimateNumber", lineEstimateSearchRequest.getEstimateNumber())
                        .ignoreCase());
            if (lineEstimateSearchRequest.getAdminSanctionFromDate() != null)
                criteria.add(
                        Restrictions.ge("adminSanctionDate", lineEstimateSearchRequest.getAdminSanctionFromDate()));
            if (lineEstimateSearchRequest.getAdminSanctionToDate() != null)
                criteria.add(Restrictions.le("adminSanctionDate", lineEstimateSearchRequest.getAdminSanctionToDate()));
            if (lineEstimateSearchRequest.getLineEstimateStatus() != null)
                criteria.add(Restrictions.eq("les.code", lineEstimateSearchRequest.getLineEstimateStatus()));

            criteria.add(Restrictions.eq("spillOverFlag", lineEstimateSearchRequest.isSpillOverFlag()));

        }

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<LineEstimateDetails> getLineEstimatesForAbstractEstimate(
            final LineEstimatesForAbstractEstimate lineEstimatesForAbstractEstimate) {

        StringBuilder mainQuery = new StringBuilder();
        final StringBuilder filterConditions = new StringBuilder();
        List<LineEstimateDetails> lineEstimateDetailsList = new ArrayList<LineEstimateDetails>();
        Query query = null;

        if (lineEstimatesForAbstractEstimate.getAdminSanctionNumber() != null)
            filterConditions.append(" and lineEstimate.adminSanctionNumber =:adminSanctionNumber ");
        if (lineEstimatesForAbstractEstimate.getExecutingDepartment() != null)
            filterConditions.append(" and lineEstimate.executingDepartment.id =:executingDepartment ");
        if (lineEstimatesForAbstractEstimate.getEstimateNumber() != null)
            filterConditions.append(" and upper(estimateNumber) =:estimateNumber ");
        if (lineEstimatesForAbstractEstimate.getAdminSanctionFromDate() != null)
            filterConditions.append(" and lineEstimate.adminSanctionDate >=:adminSanctionFromDate ");
        if (lineEstimatesForAbstractEstimate.getAdminSanctionToDate() != null)
            filterConditions.append(" and lineEstimate.adminSanctionDate <=:adminSanctionToDate ");
        if (lineEstimatesForAbstractEstimate.getLineEstimateCreatedBy() != null)
            filterConditions.append(" and lineEstimate.createdBy.id =:createdBy ");
        if (lineEstimatesForAbstractEstimate.getWorkIdentificationNumber() != null)
            filterConditions.append(" and upper(projectCode.code) =:projectCode ");
        filterConditions.append(" and lineEstimate.abstractEstimateCreated =:spillOverFlag ");

        // Getting LineEstimateDetails where LineEstimate status is
        // ADMINISTRATIVE_SANCTIONED or TECHNICAL_SANCTIONED and
        // AbstractEstimate,WorkOrder is
        // not yet created .

        mainQuery.append("select led from LineEstimateDetails as led ");
        mainQuery.append(
                " where not exists (select distinct(wo.estimateNumber) from WorkOrder as wo where led.estimateNumber = wo.estimateNumber and upper(wo.egwStatus.code) !=:wostatus) ");
        mainQuery.append(
                " and (upper(led.lineEstimate.status.code) =:lestatus1 or upper(led.lineEstimate.status.code) =:lestatus2 )");
        mainQuery.append(
                " and  not exists (select distinct(ae.lineEstimateDetails.id) from AbstractEstimate as ae where ae.lineEstimateDetails.id = led.id and upper(ae.egwStatus.code) !=:aestatus) ");
        mainQuery.append(filterConditions.toString());

        query = entityManager.createQuery(mainQuery.toString());
        query.setParameter("lestatus1", LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString());
        query.setParameter("lestatus2", LineEstimateStatus.TECHNICAL_SANCTIONED.toString());
        query = setParameterToGetLineEstimatesForAbstractEstimate(lineEstimatesForAbstractEstimate, query);

        lineEstimateDetailsList = query.getResultList();

        mainQuery = new StringBuilder();

        // Getting LineEstimateDetails where LineEstimate status is
        // TECHNICAL_SANCTIONED , AbstractEstimate status other then
        // CANCELLED and WorkOrder is not yet created .

        mainQuery.append("select led from LineEstimateDetails as led ");
        mainQuery.append(
                " where not exists (select distinct(wo.estimateNumber) from WorkOrder as wo where led.estimateNumber = wo.estimateNumber and upper(wo.egwStatus.code) !=:wostatus) ");
        mainQuery.append(" and upper(led.lineEstimate.status.code) =:lestatus ");
        mainQuery.append(
                " and exists (select distinct(ae.lineEstimateDetails.id) from AbstractEstimate as ae where ae.state is null and not exists (select act.abstractEstimate from Activity as act where ae = act.abstractEstimate) and ae.lineEstimateDetails.id = led.id and upper(ae.egwStatus.code) !=:aestatus) ");
        mainQuery.append(filterConditions.toString());

        query = null;
        query = entityManager.createQuery(mainQuery.toString());
        query.setParameter("lestatus", LineEstimateStatus.TECHNICAL_SANCTIONED.toString());
        query = setParameterToGetLineEstimatesForAbstractEstimate(lineEstimatesForAbstractEstimate, query);

        lineEstimateDetailsList.addAll(query.getResultList());

        mainQuery = new StringBuilder();

        return lineEstimateDetailsList;
    }

    private Query setParameterToGetLineEstimatesForAbstractEstimate(
            final LineEstimatesForAbstractEstimate lineEstimatesForAbstractEstimate, final Query query) {
        query.setParameter("wostatus", WorksConstants.CANCELLED_STATUS);
        query.setParameter("aestatus", WorksConstants.CANCELLED_STATUS);
        query.setParameter("spillOverFlag", lineEstimatesForAbstractEstimate.isSpillOverFlag());
        if (lineEstimatesForAbstractEstimate.getAdminSanctionNumber() != null)
            query.setParameter("adminSanctionNumber", lineEstimatesForAbstractEstimate.getAdminSanctionNumber());
        if (lineEstimatesForAbstractEstimate.getExecutingDepartment() != null)
            query.setParameter("executingDepartment", lineEstimatesForAbstractEstimate.getExecutingDepartment());
        if (lineEstimatesForAbstractEstimate.getEstimateNumber() != null)
            query.setParameter("estimateNumber", lineEstimatesForAbstractEstimate.getEstimateNumber().toUpperCase());
        if (lineEstimatesForAbstractEstimate.getAdminSanctionFromDate() != null)
            query.setParameter("adminSanctionFromDate", lineEstimatesForAbstractEstimate.getAdminSanctionFromDate());
        if (lineEstimatesForAbstractEstimate.getAdminSanctionToDate() != null)
            query.setParameter("adminSanctionToDate", lineEstimatesForAbstractEstimate.getAdminSanctionToDate());
        if (lineEstimatesForAbstractEstimate.getLineEstimateCreatedBy() != null)
            query.setParameter("createdBy", lineEstimatesForAbstractEstimate.getLineEstimateCreatedBy());
        if (lineEstimatesForAbstractEstimate.getWorkIdentificationNumber() != null)
            query.setParameter("projectCode",
                    lineEstimatesForAbstractEstimate.getWorkIdentificationNumber().toUpperCase());
        return query;
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
        final List<String> lineEstimateNumbers = lineEstimateDetailsRepository.findEstimateNumbersForLoa(
                "%" + name.toUpperCase() + "%", LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString(),
                WorksConstants.CANCELLED_STATUS);

        return lineEstimateNumbers;
    }

    public List<String> findEstimateNumbersForAbstractEstimate(final String name) {
        List<String> lineEstimateNumbers = new ArrayList<String>();
        lineEstimateNumbers = lineEstimateDetailsRepository.findEstimateNumbersForLoa("%" + name.toUpperCase() + "%",
                LineEstimateStatus.TECHNICAL_SANCTIONED.toString(), WorksConstants.CANCELLED_STATUS);
        lineEstimateNumbers.addAll(
                lineEstimateDetailsRepository.findEstimateNumbersForAbstractEstimate("%" + name.toUpperCase() + "%",
                        LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString(), WorksConstants.CANCELLED_STATUS));

        return lineEstimateNumbers;
    }

    public List<String> findAdminSanctionNumbers(final String name) {
        final List<String> adminSanctionNumbers = lineEstimateRepository
                .findDistinctAdminSanctionNumberContainingIgnoreCase("%" + name + "%");
        return adminSanctionNumbers;
    }

    public List<String> findAdminSanctionNumbersForLoa(final String name) {
        final List<String> adminSanctionNumbers = lineEstimateDetailsRepository.findAdminSanctionNumbersForLoa(
                "%" + name.toUpperCase() + "%", AbstractEstimate.EstimateStatus.APPROVED.toString(),
                WorksConstants.CANCELLED_STATUS);

        return adminSanctionNumbers;
    }

    public List<String> findAdminSanctionNumbersForAbstractEstimate(final String name) {
        List<String> adminSanctionNumbers = new ArrayList<String>();
        adminSanctionNumbers = lineEstimateDetailsRepository.findAdminSanctionNumbersForAbstractEstimate(
                "%" + name.toUpperCase() + "%", LineEstimateStatus.TECHNICAL_SANCTIONED.toString(),
                WorksConstants.CANCELLED_STATUS);
        adminSanctionNumbers.addAll(lineEstimateDetailsRepository.findAdminSanctionNumbersForAbstractEstimate(
                "%" + name.toUpperCase() + "%", LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString(),
                WorksConstants.CANCELLED_STATUS));
        return adminSanctionNumbers;
    }

    public List<String> findWorkIdentificationNumbersToSearchEstimatesForLoa(final String name) {
        final List<String> workIdNumbers = lineEstimateDetailsRepository
                .findWorkIdentificationNumbersToSearchEstimatesForLoa("%" + name.toUpperCase() + "%",
                        WorksConstants.CANCELLED_STATUS,
                        AbstractEstimate.EstimateStatus.APPROVED.toString().toUpperCase());

        return workIdNumbers;
    }

    public List<String> findWorkIdentificationNumbersToSearchEstimates(final String name) {
        final List<String> workIdNumbers = lineEstimateDetailsRepository.findWorkIdentificationNumbersToSearchEstimates(
                "%" + name.toUpperCase() + "%", WorksConstants.CANCELLED_STATUS,
                LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString());

        return workIdNumbers;
    }

    public List<LineEstimatesForAbstractEstimate> searchLineEstimatesForAbstractEstimate(
            final LineEstimatesForAbstractEstimate lineEstimatesForAbstractEstimate) {
        final List<LineEstimateDetails> lineEstimateDetails = getLineEstimatesForAbstractEstimate(
                lineEstimatesForAbstractEstimate);
        final List<LineEstimatesForAbstractEstimate> lineEstimatesForAbstractEstimates = new ArrayList<LineEstimatesForAbstractEstimate>();
        for (final LineEstimateDetails led : lineEstimateDetails) {
            final LineEstimatesForAbstractEstimate result = new LineEstimatesForAbstractEstimate();
            result.setId(led.getId());
            result.setLeId(led.getLineEstimate().getId());
            result.setAdminSanctionNumber(led.getLineEstimate().getAdminSanctionNumber());
            result.setCreatedBy(led.getLineEstimate().getCreatedBy().getName());
            result.setEstimateAmount(led.getEstimateAmount());
            result.setEstimateNumber(led.getEstimateNumber());
            result.setNameOfWork(led.getNameOfWork());
            if (led.getLineEstimate().getAdminSanctionBy() != null)
                result.setAdminSanctionBy(led.getLineEstimate().getAdminSanctionBy());
            result.setActualEstimateAmount(led.getActualEstimateAmount());
            result.setWorkIdentificationNumber(led.getProjectCode().getCode());
            lineEstimatesForAbstractEstimates.add(result);
        }
        return lineEstimatesForAbstractEstimates;
    }

    public Long getApprovalPositionByMatrixDesignation(final LineEstimate lineEstimate, Long approvalPosition,
            final String additionalRule, final String mode, final String workFlowAction) {
        final WorkFlowMatrix wfmatrix = lineEstimateWorkflowService.getWfMatrix(lineEstimate.getStateType(), null,
                lineEstimate.getTotalEstimateAmount(),
                additionalRule, lineEstimate.getCurrentState().getValue(), null);
        if (lineEstimate.getStatus() != null && lineEstimate.getStatus().getCode() != null)
            if ((lineEstimate.getStatus().getCode().equals(LineEstimateStatus.CREATED.toString()) ||
                    lineEstimate.getStatus().getCode().equals(LineEstimateStatus.RESUBMITTED.toString()))
                    && lineEstimate.getState() != null)
                if (mode.equals("edit"))
                    approvalPosition = lineEstimate.getState().getOwnerPosition().getId();
                else
                    approvalPosition = worksUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                            lineEstimate.getState(), lineEstimate.getCreatedBy().getId());
        if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.CHECKED.toString()))
            approvalPosition = worksUtils.getApproverPosition(wfmatrix.getNextDesignation(), lineEstimate.getState(),
                    lineEstimate.getCreatedBy().getId());
        if (workFlowAction.equals(WorksConstants.CANCEL_ACTION)
                && wfmatrix.getNextState().equals(WorksConstants.WF_STATE_CREATED)
                || workFlowAction.equals(WorksConstants.APPROVE_ACTION))
            approvalPosition = null;

        return approvalPosition;
    }

    @Transactional
    public LineEstimate updateLineEstimateDetails(final LineEstimate lineEstimate, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction, final String mode,
            final ReportOutput reportOutput, final String removedLineEstimateDetailsIds, final MultipartFile[] files,
            final CFinancialYear financialYear) throws ValidationException, IOException {
        LineEstimate updatedLineEstimate = null;

        if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.REJECTED.toString())) {
            updatedLineEstimate = update(lineEstimate, removedLineEstimateDetailsIds, files, financialYear);
            try {
                if (workFlowAction.equals(WorksConstants.FORWARD_ACTION) || workFlowAction.equals(WorksConstants.CANCEL_ACTION))
                    lineEstimateStatusChange(updatedLineEstimate, workFlowAction, mode);
            } catch (final ValidationException e) {
                throw new ValidationException(e.getErrors());
            }
        } else
            try {
                if (workFlowAction.equals(WorksConstants.REJECT_ACTION))
                    resetAdminSanctionDetails(lineEstimate);
                else if (workFlowAction.equals(WorksConstants.APPROVE_ACTION)) {
                    if (!BudgetControlType.BudgetCheckOption.NONE.toString()
                            .equalsIgnoreCase(budgetControlTypeService.getConfigValue()))
                        doBudgetoryAppropriation(lineEstimate);
                    setAdminSanctionByAndDate(lineEstimate);
                    for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails())
                        lineEstimateDetailService.setProjectCode(led);
                }

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
                final boolean flag = estimateAppropriationService.checkConsumeEncumbranceBudgetForLineEstimate(led,
                        worksUtils.getFinancialYearByDate(new Date()).getId(), appropriationAmount.doubleValue(), budgetheadid);

                if (!flag)
                    throw new ValidationException("", "error.budgetappropriation.insufficient.amount");
            }
        }
    }

    public void lineEstimateStatusChange(final LineEstimate lineEstimate, final String workFlowAction,
            final String mode) throws ValidationException {
        WorkFlowMatrix wfmatrix = null;
        if (LineEstimateStatus.REJECTED.toString().equals(lineEstimate.getStatus().getCode())
                && WorksConstants.CANCEL_ACTION.equals(workFlowAction))
            lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                    LineEstimateStatus.CANCELLED.toString()));
        else if (WorksConstants.REJECT_ACTION.equals(workFlowAction))
            lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                    LineEstimateStatus.REJECTED.toString()));
        else if (null != lineEstimate.getState()) {
            wfmatrix = lineEstimateWorkflowService.getWfMatrix(lineEstimate.getStateType(), null,
                    lineEstimate.getTotalEstimateAmount(),
                    (String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY),
                    lineEstimate.getCurrentState().getValue(),
                    lineEstimate.getCurrentState().getNextAction());
            lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                    wfmatrix.getNextStatus().toUpperCase()));
        }
    }

    public List<User> getLineEstimateCreatedByUsers() {
        return lineEstimateRepository.getLineEstimateCreatedByUsers(LineEstimateStatus.TECHNICAL_SANCTIONED.toString());
    }

    public LineEstimateDetails findByEstimateNumber(final String estimateNumber) {
        return lineEstimateDetailsRepository.findByEstimateNumberAndLineEstimate_Status_CodeEquals(estimateNumber,
                LineEstimateStatus.TECHNICAL_SANCTIONED.toString());
    }

    public void createLineEstimateWorkflowTransition(final LineEstimate lineEstimate, final Long approvalPosition,
            final String approvalComent, final String additionalRule, final String workFlowAction) {
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
            
            lineEstimate.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition()).withNextAction("").withNatureOfTask(natureOfwork);

        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0))
                    && !WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction))
                pos = positionMasterService.getPositionById(approvalPosition);
            WorkFlowMatrix wfmatrix = null;
            if (null == lineEstimate.getState()) {
                wfmatrix = lineEstimateWorkflowService.getWfMatrix(lineEstimate.getStateType(), null,
                        lineEstimate.getTotalEstimateAmount(),
                        additionalRule, currState, null);
                lineEstimate.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            } else if (WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = WorksConstants.WF_STATE_CANCELLED;
                lineEstimate.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(pos).withNextAction("").withNatureOfTask(natureOfwork);
            } else {
                wfmatrix = lineEstimateWorkflowService.getWfMatrix(lineEstimate.getStateType(), null, lineEstimate.getTotalEstimateAmount(),
                        additionalRule, lineEstimate.getCurrentState().getValue(), lineEstimate.getCurrentState().getNextAction());
                lineEstimate.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
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

    public LineEstimate getLineEstimateByAdminSanctionNumber(final String adminSanctionNumber) {
        return lineEstimateRepository.findByAdminSanctionNumberIgnoreCaseAndStatus_codeNotLike(adminSanctionNumber,
                WorksConstants.CANCELLED_STATUS);
    }

    @Transactional
    public LineEstimate createSpillOver(final LineEstimate lineEstimate, final MultipartFile[] files)
            throws IOException {
        lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE,
                LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString()));
        lineEstimate.setSpillOverFlag(true);

        if (lineEstimate.getLineEstimateNumber() == null || lineEstimate.getLineEstimateNumber().isEmpty()) {

            final LineEstimateNumberGenerator l = beanResolver
                    .getAutoNumberServiceFor(LineEstimateNumberGenerator.class);
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

    public List<User> getLineEstimateCreatedByUsersForCancelLineEstimateByDepartment(final Long department) {
        return lineEstimateDetailsRepository.findCreatedByForCancelLineEstimateByDepartment(department,
                LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString(),
                LineEstimateStatus.TECHNICAL_SANCTIONED.toString(), WorksConstants.APPROVED);
    }

    public List<LineEstimate> searchLineEstimatesToCancel(final LineEstimateSearchRequest lineEstimateSearchRequest) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(LineEstimate.class)
                .createAlias("lineEstimateDetails", "led").createAlias("executingDepartment", "ed")
                .createAlias("led.projectCode", "pc").createAlias("status", "status");
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
        criteria.add(Restrictions.or(
                Restrictions.eq("status.code", LineEstimateStatus.TECHNICAL_SANCTIONED.toString()).ignoreCase(),
                Restrictions.eq("status.code", LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString()).ignoreCase()));

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public String checkIfLOAsCreated(final Long lineEstimateId) {
        final List<String> listString = letterOfAcceptanceService.getEstimateNumbersToCancelLineEstimate(lineEstimateId);
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
                try{
                    estimateAppropriationService.releaseBudgetOnReject(led, null, null);
                }catch(final ValidationException v) {
                    throw new ValidationException(v.getErrors());
                }

        for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails()) {
            final Long id = led.getId();
            final AbstractEstimate abstractEstimate = estimateService
                    .getAbstractEstimateByLineEstimateDetailsForCancelLineEstimate(id);

            if (abstractEstimate != null && abstractEstimate.getActivities().isEmpty()) {
                abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                        WorksConstants.ABSTRACTESTIMATE, AbstractEstimate.EstimateStatus.CANCELLED.toString()));
                abstractEstimate.getProjectCode().setActive(Boolean.FALSE);
            }
        }
        return lineEstimateRepository.save(lineEstimate);
    }

    public LineEstimate getLineEstimateByCouncilResolutionNumber(final String councilResolutionNumber) {
        return lineEstimateRepository.findByCouncilResolutionNumberIgnoreCaseAndStatus_codeNotLike(
                councilResolutionNumber, WorksConstants.CANCELLED_STATUS);
    }

    public LineEstimate getLineEstimateByContractCommitteeApprovalNumber(final String contractCommitteeApprovalNumber) {
        return lineEstimateRepository.findByContractCommitteeApprovalNumberIgnoreCaseAndStatus_codeNotLike(
                contractCommitteeApprovalNumber, WorksConstants.CANCELLED_STATUS);
    }

    public LineEstimate getLineEstimateByStandingCommitteeApprovalNumber(final String standingCommitteeApprovalNumber) {
        return lineEstimateRepository.findByStandingCommitteeApprovalNumberIgnoreCaseAndStatus_codeNotLike(
                standingCommitteeApprovalNumber, WorksConstants.CANCELLED_STATUS);
    }

    public LineEstimate getLineEstimateByGovernmentApprovalNumber(final String governmentApprovalNumber) {
        return lineEstimateRepository.findByGovernmentApprovalNumberIgnoreCaseAndStatus_codeNotLike(
                governmentApprovalNumber, WorksConstants.CANCELLED_STATUS);
    }

    public String checkAbstractEstimatesWithBOQForLineEstimate(final Long lineEstimateId) {
        final List<String> listString = estimateService.getAbstractEstimateNumbersToCancelLineEstimate(lineEstimateId);
        String estimateNumbers = "";
        for (final String estimateNumber : listString)
            estimateNumbers += estimateNumber + ", ";
        if (estimateNumbers.equals(""))
            return "";
        else
            return estimateNumbers;
    }

    public List<String> getEstimateNumbersForEstimatePhotograph(final String estimateNumber) {
        return lineEstimateDetailsRepository.findEstimateNumbersForEstimatePhotograph("%" + estimateNumber + "%",
                WorksConstants.CANCELLED_STATUS);
    }

    public List<String> getWinForEstimatePhotograph(final String workIdentificationNumber) {
        return lineEstimateDetailsRepository.findWorkIdentificationNumberForEstimatePhotograph(
                "%" + workIdentificationNumber + "%", WorksConstants.CANCELLED_STATUS);
    }

    public List<LineEstimateDetails> searchLineEstimatesForEstimatePhotograph(
            final EstimatePhotographSearchRequest estimatePhotographSearchRequest) {
        final StringBuilder queryStr = new StringBuilder(500);

        buildWhereClauseForEstimatePhotograph(estimatePhotographSearchRequest, queryStr);
        final Query query = setParameterForEstimatePhotograph(estimatePhotographSearchRequest, queryStr);
        final List<LineEstimateDetails> lineEstimateDetailsList = query.getResultList();
        return lineEstimateDetailsList;
    }

    private void buildWhereClauseForEstimatePhotograph(final EstimatePhotographSearchRequest estimatePhotographSearchRequest,
            final StringBuilder queryStr) {

        queryStr.append(
                "select distinct led from LineEstimateDetails as led where led.lineEstimate.status.code != :lineEstimateStatus ");

        // TODO : remove this comment when search result need to restrict after create contractor bill
        /*
         * queryStr.append(
         * "and not exists(select distinct(cbr.workOrderEstimate.estimate.lineEstimateDetails) from ContractorBillRegister as cbr where cbr.workOrderEstimate.estimate.lineEstimateDetails.id = led.id and upper(cbr.billstatus) != :billstatus and cbr.billtype = :billtype)"
         * );
         */

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
        // TODO : remove this comment when search result need to restrict after create contractor bill
        /*
         * qry.setParameter("billstatus", ContractorBillRegister.BillStatus.CANCELLED.toString()); qry.setParameter("billtype",
         * BillTypes.Final_Bill.toString());
         */

        if (estimatePhotographSearchRequest != null) {
            if (estimatePhotographSearchRequest.getExecutingDepartment() != null)
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
            if (estimatePhotographSearchRequest.getNatureOfWork() != null)
                qry.setParameter("natureOfWork", estimatePhotographSearchRequest.getNatureOfWork());

        }
        return qry;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getWorkFlowLevelFields(final LineEstimate lineEstimate) {
        final String cityGrade = (String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY);
        return (Map<String, Object>) scriptService.executeScript(WorksConstants.LINEESTIMATE_APPROVALRULES,
                ScriptService.createContext("estimateAmount", lineEstimate.getTotalEstimateAmount(),
                        "cityGrade", cityGrade));
    }

    public void validateAdminSanctionFields(final LineEstimate lineEstimate, final BindingResult errors) {
        if (StringUtils.isBlank(lineEstimate.getCouncilResolutionNumber()))
            errors.rejectValue("councilResolutionNumber", "error.councilResolutionNumber.required");
        else {
            final LineEstimate LEByCouncilResolutionNumber = getLineEstimateByCouncilResolutionNumber(
                    lineEstimate.getCouncilResolutionNumber());
            if (LEByCouncilResolutionNumber != null)
                errors.rejectValue("councilResolutionNumber", "error.councilresolutionnumber.unique");
            if (lineEstimate.getCouncilResolutionDate() == null)
                errors.rejectValue("councilResolutionDate", "error.councilResolutionDate.required");
        }
    }

    public void validateLineEstimateDetails(final LineEstimate lineEstimate, final BindingResult errors) {
        BigDecimal estimateAmount = BigDecimal.ZERO;
        for (final LineEstimateDetails led : lineEstimate.getTempLineEstimateDetails())
            estimateAmount = estimateAmount.add(led.getEstimateAmount());
        final List<AppConfigValues> nominationLimit = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_NOMINATION_AMOUNT);
        final AppConfigValues value = nominationLimit.get(0);
        final List<AppConfigValues> nominationName = estimateService.getNominationName();
        if (value.getValue() != null && !value.getValue().isEmpty()
                && lineEstimate.getModeOfAllotment()
                        .equalsIgnoreCase(!nominationName.isEmpty() ? nominationName.get(0).getValue() : "")
                && Double.parseDouble(estimateAmount.toString()) > Double.parseDouble(value.getValue()))
            errors.reject("error.lineestimate.modeofentrustment",
                    new String[] { !nominationName.isEmpty() ? nominationName.get(0).getValue() : "", estimateAmount.toString() },
                    "error.lineestimate.modeofentrustment");

    }

    public void loadModelValues(final LineEstimate lineEstimate, final Model model) {
        final List<AppConfigValues> nominationLimit = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_NOMINATION_AMOUNT);
        final AppConfigValues value = nominationLimit.get(0);
        if (!value.getValue().isEmpty())
            model.addAttribute("nominationLimit", value.getValue());
        final List<AppConfigValues> nominationName = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.NOMINATION_NAME);
        model.addAttribute("nominationName", !nominationName.isEmpty() ? nominationName.get(0).getValue() : "");
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

    public List getLineEstimateHiddenFields() {
        return Arrays.asList(worksApplicationProperties.lineEstimateHideFields());
    }

    public String getLineEstimateMultipleWorkDetailsAllowed() {
        return worksApplicationProperties.lineEstimateMultipleWorkDetailsAllowed();
    }
}