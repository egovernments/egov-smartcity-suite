/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.lineestimate.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ValidationException;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.LineEstimateForLoaSearchRequest;
import org.egov.works.lineestimate.entity.LineEstimateForLoaSearchResult;
import org.egov.works.lineestimate.entity.LineEstimateSearchRequest;
import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.lineestimate.repository.LineEstimateRepository;
import org.egov.works.models.estimate.EstimateNumberGenerator;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.elasticsearch.common.joda.time.DateTime;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private LineEstimateNumberGenerator lineEstimateNumberGenerator;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private EstimateNumberGenerator estimateNumberGenerator;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    private SimpleWorkflowService<LineEstimate> lineEstimateWorkflowService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private UserService userService;

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
        final CFinancialYear cFinancialYear = financialYearDAO.getFinancialYearByDate(lineEstimate.getLineEstimateDate());
        for (final LineEstimateDetails lineEstimateDetail : lineEstimate.getLineEstimateDetails()) {
            final String estimateNumber = estimateNumberGenerator.generateEstimateNumber(lineEstimate, cFinancialYear);
            lineEstimateDetail.setEstimateNumber(estimateNumber);
            lineEstimateDetail.setLineEstimate(lineEstimate);
        }
        if (lineEstimate.getLineEstimateNumber() == null || lineEstimate.getLineEstimateNumber().isEmpty()) {
            final String lineEstimateNumber = lineEstimateNumberGenerator.generateLineEstimateNumber(lineEstimate,
                    cFinancialYear);
            lineEstimate.setLineEstimateNumber(lineEstimateNumber);
        }

        final LineEstimate newLineEstimate = lineEstimateRepository.save(lineEstimate);

        createLineEstimateWorkflowTransition(newLineEstimate,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, newLineEstimate,
                WorksConstants.MODULE_NAME_LINEESTIMATE);
        if (!documentDetails.isEmpty()) {
            newLineEstimate.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return newLineEstimate;
    }

    @Transactional
    public LineEstimate update(final LineEstimate lineEstimate, final String removedLineEstimateDetailsIds,
            final MultipartFile[] files) throws IOException {
        final CFinancialYear cFinancialYear = financialYearDAO.getFinancialYearByDate(lineEstimate.getLineEstimateDate());
        for (final LineEstimateDetails lineEstimateDetails : lineEstimate.getLineEstimateDetails())
            if (lineEstimateDetails.getLineEstimate() == null) {
                lineEstimateDetails.setLineEstimate(lineEstimate);
                lineEstimateDetails.setEstimateNumber(estimateNumberGenerator
                        .generateEstimateNumber(lineEstimate, cFinancialYear));
            }
        List<LineEstimateDetails> list = new ArrayList<LineEstimateDetails>(lineEstimate.getLineEstimateDetails());
        list = removeDeletedLineEstimateDetails(list, removedLineEstimateDetailsIds);
        for (final LineEstimateDetails details : list)
            details.setId(null);
        lineEstimate.getLineEstimateDetails().clear();
        // TODO: use save instead of saveAndFlush
        final LineEstimate persistedLineEstimate = lineEstimateRepository.saveAndFlush(lineEstimate);

        persistedLineEstimate.setLineEstimateDetails(list);
        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, persistedLineEstimate,
                WorksConstants.MODULE_NAME_LINEESTIMATE);
        if (!documentDetails.isEmpty()) {
            lineEstimate.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        // TODO: use save instead of saveAndFlush
        return lineEstimateRepository.saveAndFlush(persistedLineEstimate);
    }

    public LineEstimate getLineEstimateByLineEstimateNumber(final String lineEstimateNumber) {
        return lineEstimateRepository.findByLineEstimateNumber(lineEstimateNumber);
    }

    @Transactional
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
                }
                else
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
                criteria.add(Restrictions.eq("adminSanctionNumber", lineEstimateSearchRequest.getAdminSanctionNumber()));
            if (lineEstimateSearchRequest.getBudgetHead() != null)
                criteria.add(Restrictions.eq("budgetHead.id", lineEstimateSearchRequest.getBudgetHead()));
            if (lineEstimateSearchRequest.getExecutingDepartment() != null)
                criteria.add(Restrictions.eq("executingDepartment.id", lineEstimateSearchRequest.getExecutingDepartment()));
            if (lineEstimateSearchRequest.getFunction() != null)
                criteria.add(Restrictions.eq("function.id", lineEstimateSearchRequest.getFunction()));
            if (lineEstimateSearchRequest.getFund() != null)
                criteria.add(Restrictions.eq("fund.id", lineEstimateSearchRequest.getFund().intValue()));
            if (lineEstimateSearchRequest.getEstimateNumber() != null)
                criteria.add(Restrictions.eq("lineEstimateDetail.estimateNumber", lineEstimateSearchRequest.getEstimateNumber()));
            if (lineEstimateSearchRequest.getAdminSanctionFromDate() != null)
                criteria.add(Restrictions.ge("adminSanctionDate", lineEstimateSearchRequest.getAdminSanctionFromDate()));
            if (lineEstimateSearchRequest.getAdminSanctionToDate() != null)
                criteria.add(Restrictions.le("adminSanctionDate", lineEstimateSearchRequest.getAdminSanctionToDate()));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<LineEstimate> searchLineEstimatesForLoa(final LineEstimateForLoaSearchRequest lineEstimateForLoaSearchRequest) {
        
        String queryString = "select distinct(estimateNumber) from LineEstimateDetails where estimateNumber not in (select distinct(estimateNumber) from WorkOrder where upper(egwStatus.code) != :status)";
        TypedQuery<String> query = (TypedQuery<String>) entityManager.createQuery(queryString);
        query.setParameter("status", LineEstimateStatus.CANCELLED.toString());
        List<String> lineEstimateNumbers = query.getResultList();
        
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(LineEstimate.class)
                .createAlias("lineEstimateDetails", "lineEstimateDetail");
        if (lineEstimateForLoaSearchRequest != null) {
            if (lineEstimateForLoaSearchRequest.getAdminSanctionNumber() != null)
                criteria.add(Restrictions.ilike("adminSanctionNumber", lineEstimateForLoaSearchRequest.getAdminSanctionNumber()));
            if (lineEstimateForLoaSearchRequest.getExecutingDepartment() != null)
                criteria.add(Restrictions.eq("executingDepartment.id", lineEstimateForLoaSearchRequest.getExecutingDepartment()));
            if (lineEstimateForLoaSearchRequest.getEstimateNumber() != null)
                criteria.add(Restrictions.eq("lineEstimateDetail.estimateNumber",
                        lineEstimateForLoaSearchRequest.getEstimateNumber()));
            if (lineEstimateForLoaSearchRequest.getAdminSanctionFromDate() != null)
                criteria.add(Restrictions.ge("adminSanctionDate", lineEstimateForLoaSearchRequest.getAdminSanctionFromDate()));
            if (lineEstimateForLoaSearchRequest.getAdminSanctionToDate() != null)
                criteria.add(Restrictions.le("adminSanctionDate", lineEstimateForLoaSearchRequest.getAdminSanctionToDate()));
            if (lineEstimateForLoaSearchRequest.getLineEstimateCreatedBy() != null)
                criteria.add(Restrictions.le("createdBy.id", lineEstimateForLoaSearchRequest.getLineEstimateCreatedBy()));
            criteria.add(Restrictions.in("lineEstimateDetail.estimateNumber", lineEstimateNumbers));
//            criteria.add(Restrictions.eq("status.code", LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString()));
//            criteria.add(Restrictions.eq("status.code", LineEstimateStatus.TECHNICAL_SANCTIONED.toString()));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<String> findLineEstimateNumbers(final String name) {
        final List<LineEstimateDetails> lineEstimateDetails = lineEstimateDetailsRepository
                .findByEstimateNumberContainingIgnoreCase(name);
        final List<String> results = new ArrayList<String>();
        for (final LineEstimateDetails details : lineEstimateDetails)
            results.add(details.getEstimateNumber());
        return results;
    }
    
    public List<String> findLineEstimateNumbersForLoa(final String name) {
        String queryString = "select distinct(estimateNumber) from LineEstimateDetails where estimateNumber like = '%:estimateNumber' and estimateNumber not in (select distinct(estimateNumber) from WorkOrder where upper(egwStatus.code) != :status)";
        TypedQuery<String> query = (TypedQuery<String>) entityManager.createQuery(queryString);
        query.setParameter("estimateNumber", name);
        query.setParameter("status", LineEstimateStatus.CANCELLED.toString());
        List<String> lineEstimateNumbers = query.getResultList();
        
        return lineEstimateNumbers;
    }

    public List<String> findAdminSanctionNumbers(final String name) {
        final List<LineEstimate> lineEstimates = lineEstimateRepository.findByAdminSanctionNumberContainingIgnoreCase(name);
        final List<String> results = new ArrayList<String>();
        for (final LineEstimate estimate : lineEstimates)
            results.add(estimate.getAdminSanctionNumber());
        return results;
    }
    
    public List<String> findAdminSanctionNumbersForLoa(String name) {
        String queryString = "select distinct(adminSanctionNumber) from LineEstimate where adminSanctionNumber like = '%:adminSanctionNumber' and lineEstimateNumber not in (select distinct(estimateNumber) from WorkOrder where upper(egwStatus.code) != :status)";
        TypedQuery<String> query = (TypedQuery<String>) entityManager.createQuery(queryString);
        query.setParameter("adminSanctionNumber", name);
        query.setParameter("status", LineEstimateStatus.CANCELLED.toString());
        List<String> adminSanctionNumbers = query.getResultList();
        
        return adminSanctionNumbers;
    }

    public List<LineEstimateForLoaSearchResult> searchLineEstimatesForLOA(
            final LineEstimateForLoaSearchRequest lineEstimateForLoaSearchRequest) {
        final List<LineEstimate> lineEstimates = searchLineEstimatesForLoa(lineEstimateForLoaSearchRequest);
        final List<LineEstimateForLoaSearchResult> lineEstimateForLoaSearchResults = new ArrayList<LineEstimateForLoaSearchResult>();
        for (final LineEstimate le : lineEstimates)
            for (final LineEstimateDetails led : le.getLineEstimateDetails())
                if (lineEstimateForLoaSearchRequest.getEstimateNumber() != null) {
                    if (led.getEstimateNumber().equalsIgnoreCase(lineEstimateForLoaSearchRequest.getEstimateNumber())) {
                        final LineEstimateForLoaSearchResult result = new LineEstimateForLoaSearchResult();
                        result.setId(le.getId());
                        result.setAdminSanctionNumber(le.getAdminSanctionNumber());
                        result.setCreatedBy(le.getCreatedBy().getName());
                        result.setEstimateAmount(led.getEstimateAmount());
                        result.setEstimateNumber(led.getEstimateNumber());
                        result.setNameOfWork(led.getNameOfWork());
                        if (le.getAdminSanctionBy() != null)
                            result.setAdminSanctionBy(le.getAdminSanctionBy().getName());
                        result.setActualEstimateAmount(led.getActualEstimateAmount());
                        result.setCurrentOwner(worksUtils.getApproverName(le.getState().getOwnerPosition().getId()));
                        lineEstimateForLoaSearchResults.add(result);
                    }
                }
                else {
                    final LineEstimateForLoaSearchResult result = new LineEstimateForLoaSearchResult();
                    result.setId(le.getId());
                    result.setAdminSanctionNumber(le.getAdminSanctionNumber());
                    result.setCreatedBy(le.getCreatedBy().getName());
                    result.setEstimateAmount(led.getEstimateAmount());
                    result.setEstimateNumber(led.getEstimateNumber());
                    result.setNameOfWork(led.getNameOfWork());
                    result.setActualEstimateAmount(led.getActualEstimateAmount());
                    if (le.getState().getOwnerPosition() != null)
                        result.setCurrentOwner(worksUtils.getApproverName(le.getState().getOwnerPosition().getId()));
                    if (le.getAdminSanctionBy() != null)
                        result.setAdminSanctionBy(le.getAdminSanctionBy().getName());
                    lineEstimateForLoaSearchResults.add(result);
                }
        return lineEstimateForLoaSearchResults;
    }

    public List<Hashtable<String, Object>> getHistory(final LineEstimate lineEstimate) {
        User user = null;
        final List<Hashtable<String, Object>> historyTable = new ArrayList<Hashtable<String, Object>>();
        final State state = lineEstimate.getState();
        final Hashtable<String, Object> map = new Hashtable<String, Object>(0);
        if (null != state) {
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
            if (!lineEstimate.getStateHistory().isEmpty() && lineEstimate.getStateHistory() != null)
                Collections.reverse(lineEstimate.getStateHistory());
            for (final StateHistory stateHistory : lineEstimate.getStateHistory()) {
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
                            lineEstimate);
        if (lineEstimate.getStatus().getCode()
                .equals(LineEstimateStatus.CHECKED.toString()))
            approvalPosition = worksUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                    lineEstimate);
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
            final MultipartFile[] files) throws ValidationException, IOException {
        LineEstimate updatedLineEstimate = null;

        if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.REJECTED.toString())) {
            updatedLineEstimate = update(lineEstimate, removedLineEstimateDetailsIds, files);
            lineEstimateStatusChange(updatedLineEstimate, workFlowAction, mode);
        } else {
            lineEstimateStatusChange(lineEstimate, workFlowAction, mode);
            updatedLineEstimate = lineEstimateRepository.save(lineEstimate);
        }

        createLineEstimateWorkflowTransition(updatedLineEstimate,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        return updatedLineEstimate;
    }

    public void lineEstimateStatusChange(final LineEstimate lineEstimate, final String workFlowAction,
            final String mode) {
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
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(LineEstimate.class);
        criteria.add(Restrictions.isNotNull("technicalSanctionNumber"));
        final TypedQuery<User> query = (TypedQuery<User>) entityManager
                .createQuery("select distinct(createdBy) from LineEstimate where technicalSanctionNumber != '' ");
        return query.getResultList();
    }

    public List<Department> getUserDepartments(User currentUser) {
        List<Assignment> assignments = assignmentService.findByEmployeeAndGivenDate(currentUser.getId(), new Date());
        List<Department> uniqueDepartmentList= new ArrayList<Department>();
        Department prevDepartment = new Department();
        Iterator iterator = assignments.iterator(); 
        while(iterator.hasNext()) {
            Assignment assignment=(Assignment)iterator.next();
            if(!((assignment.getDepartment().getName().equals(prevDepartment.getName())))){
                uniqueDepartmentList.add(assignment.getDepartment()); 
            }
            prevDepartment=assignment.getDepartment();
        }
        return uniqueDepartmentList;
    }

    public LineEstimateDetails findByEstimateNumber(final String estimateNumber) {
        return lineEstimateDetailsRepository.findByEstimateNumber(estimateNumber);
    }

    public void createLineEstimateWorkflowTransition(final LineEstimate lineEstimate,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;
        final String currState = "";
        final String natureOfwork = WorksConstants.WORKFLOWTYPE_DISPLAYNAME;

        if (null != lineEstimate.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(lineEstimate.getCreatedBy().getId());
        if (WorksConstants.REJECT_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment))
                lineEstimate.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork);
            else {
                final String stateValue = WorksConstants.WF_STATE_REJECTED;
                lineEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                .withComments(approvalComent)
                .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                .withOwner(wfInitiator.getPosition())
                .withNextAction("")
                .withNatureOfTask(natureOfwork);
            }
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

    public boolean validateTechnicalSanctionDate(final Long id, final Date technicalSanctionDate) {
        final LineEstimate lineEstimate = lineEstimateRepository.findOne(id);
        if (lineEstimate.getAdminSanctionDate() != null && lineEstimate.getAdminSanctionDate().after(technicalSanctionDate))
            return false;
        return true;
    }

    public LineEstimate getLineEstimateByTechnicalSanctionNumber(final String technicalSanctionNumber) {
        return lineEstimateRepository.findByTechnicalSanctionNumberAndStatus_CodeNot(technicalSanctionNumber,
                LineEstimateStatus.CANCELLED.toString());
    }
}
