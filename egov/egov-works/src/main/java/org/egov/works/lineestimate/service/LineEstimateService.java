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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import org.hibernate.criterion.Projections;
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
        for (final LineEstimateDetails lineEstimateDetails : lineEstimate.getLineEstimateDetails()) {
            if (lineEstimateDetails.getLineEstimate() == null) {
                lineEstimateDetails.setLineEstimate(lineEstimate);
                lineEstimateDetails.setEstimateNumber(estimateNumberGenerator
                        .generateEstimateNumber(lineEstimate, cFinancialYear));
            }
        }
        List<LineEstimateDetails> list = new ArrayList<LineEstimateDetails>(lineEstimate.getLineEstimateDetails());
        list = removeDeletedLineEstimateDetails(list, removedLineEstimateDetailsIds);
        for (LineEstimateDetails details : list) {
            details.setId(null);
        }
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
        List<LineEstimateDetails> details = new ArrayList<LineEstimateDetails>();
        if (null != removedLineEstimateDetailsIds) {
            String[] ids = removedLineEstimateDetailsIds.split(",");
            List<String> strList = new ArrayList<String>();
            for (String str : ids) {
                strList.add(str);
            }
            for (LineEstimateDetails line : list) {
                if (line.getId() != null) {
                    if (!strList.contains(line.getId().toString()))
                        details.add(line);
                }
                else
                    details.add(line);
            }
        }
        else {
            return list;
        }
        return details;
    }

    public List<LineEstimate> searchLineEstimates(LineEstimateSearchRequest lineEstimateSearchRequest) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(LineEstimate.class)
                .createAlias("lineEstimateDetails", "lineEstimateDetail");
        if (lineEstimateSearchRequest != null) {
            if (lineEstimateSearchRequest.getAdminSanctionNumber() != null) {
                criteria.add(Restrictions.eq("adminSanctionNumber", lineEstimateSearchRequest.getAdminSanctionNumber()));
            }
            if (lineEstimateSearchRequest.getBudgetHead() != null) {
                criteria.add(Restrictions.eq("budgetHead.id", lineEstimateSearchRequest.getBudgetHead()));
            }
            if (lineEstimateSearchRequest.getExecutingDepartment() != null) {
                criteria.add(Restrictions.eq("executingDepartment.id", lineEstimateSearchRequest.getExecutingDepartment()));
            }
            if (lineEstimateSearchRequest.getFunction() != null) {
                criteria.add(Restrictions.eq("function.id", lineEstimateSearchRequest.getFunction()));
            }
            if (lineEstimateSearchRequest.getFund() != null) {
                criteria.add(Restrictions.eq("fund.id", lineEstimateSearchRequest.getFund().intValue()));
            }
            if (lineEstimateSearchRequest.getEstimateNumber() != null) {
                criteria.add(Restrictions.eq("lineEstimateDetail.estimateNumber", lineEstimateSearchRequest.getEstimateNumber()));
            }
            if (lineEstimateSearchRequest.getAdminSanctionFromDate() != null) {
                criteria.add(Restrictions.ge("adminSanctionDate", lineEstimateSearchRequest.getAdminSanctionFromDate()));
            }
            if (lineEstimateSearchRequest.getAdminSanctionToDate() != null) {
                criteria.add(Restrictions.le("adminSanctionDate", lineEstimateSearchRequest.getAdminSanctionToDate()));
            }
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }
    
    public List<LineEstimate> searchLineEstimatesForLoa(LineEstimateForLoaSearchRequest lineEstimateForLoaSearchRequest) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(LineEstimate.class)
                .createAlias("lineEstimateDetails", "lineEstimateDetail");
        if (lineEstimateForLoaSearchRequest != null) {
            if (lineEstimateForLoaSearchRequest.getAdminSanctionNumber() != null) {
                criteria.add(Restrictions.ilike("adminSanctionNumber", lineEstimateForLoaSearchRequest.getAdminSanctionNumber()));
            }
            if (lineEstimateForLoaSearchRequest.getExecutingDepartment() != null) {
                criteria.add(Restrictions.eq("executingDepartment.id", lineEstimateForLoaSearchRequest.getExecutingDepartment()));
            }
            if (lineEstimateForLoaSearchRequest.getEstimateNumber() != null) {
                criteria.add(Restrictions.eq("lineEstimateDetail.estimateNumber", lineEstimateForLoaSearchRequest.getEstimateNumber()));
            }
            if (lineEstimateForLoaSearchRequest.getAdminSanctionFromDate() != null) {
                criteria.add(Restrictions.ge("adminSanctionDate", lineEstimateForLoaSearchRequest.getAdminSanctionFromDate()));
            }
            if (lineEstimateForLoaSearchRequest.getAdminSanctionToDate() != null) {
                criteria.add(Restrictions.le("adminSanctionDate", lineEstimateForLoaSearchRequest.getAdminSanctionToDate()));
            }
            if(lineEstimateForLoaSearchRequest.getLineEstimateCreatedBy() != null) {
                criteria.add(Restrictions.le("createdBy.id", lineEstimateForLoaSearchRequest.getLineEstimateCreatedBy()));
            }
            //criteria.add(Restrictions.eq("status.code", LineEstimateStatus.TECHNICAL_SANCTIONED.toString()));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<String> findLineEstimateNumbers(String name) {
        List<LineEstimateDetails> lineEstimateDetails = lineEstimateDetailsRepository
                .findByEstimateNumberContainingIgnoreCase(name);
        List<String> results = new ArrayList<String>();
        for (LineEstimateDetails details : lineEstimateDetails) {
            results.add(details.getEstimateNumber());
        }
        return results;
    }

    public List<String> findAdminSanctionNumbers(String name) {
        List<LineEstimate> lineEstimates = lineEstimateRepository.findByAdminSanctionNumberContainingIgnoreCase(name);
        List<String> results = new ArrayList<String>();
        for (LineEstimate estimate : lineEstimates) {
            results.add(estimate.getAdminSanctionNumber());
        }
        return results;
    }

    public List<LineEstimateForLoaSearchResult> searchLineEstimatesForLOA(LineEstimateForLoaSearchRequest lineEstimateForLoaSearchRequest) {
        List<LineEstimate> lineEstimates = searchLineEstimatesForLoa(lineEstimateForLoaSearchRequest);
        List<LineEstimateForLoaSearchResult> lineEstimateForLoaSearchResults = new ArrayList<LineEstimateForLoaSearchResult>();
        for (LineEstimate le : lineEstimates) {
            for (LineEstimateDetails led : le.getLineEstimateDetails()) {
                if (lineEstimateForLoaSearchRequest.getEstimateNumber() != null) {
                    if (led.getEstimateNumber().equalsIgnoreCase(lineEstimateForLoaSearchRequest.getEstimateNumber())) {
                        LineEstimateForLoaSearchResult result = new LineEstimateForLoaSearchResult();
                        result.setId(le.getId());
                        result.setAdminSanctionNumber(le.getAdminSanctionNumber());
                        result.setCreatedBy(le.getCreatedBy().getName());
                        result.setEstimateAmount(led.getEstimateAmount());
                        result.setEstimateNumber(led.getEstimateNumber());
                        result.setNameOfWork(led.getNameOfWork());
                        if(le.getAdminSanctionBy() != null)
                            result.setAdminSanctionBy(le.getAdminSanctionBy().getName());
                        result.setActualEstimateAmount(led.getActualEstimateAmount());
                        result.setCurrentOwner(worksUtils.getApproverName(le.getState().getOwnerPosition().getId()));
                        lineEstimateForLoaSearchResults.add(result);
                    }
                }
                else {
                    LineEstimateForLoaSearchResult result = new LineEstimateForLoaSearchResult();
                    result.setId(le.getId());
                    result.setAdminSanctionNumber(le.getAdminSanctionNumber());
                    result.setCreatedBy(le.getCreatedBy().getName());
                    result.setEstimateAmount(led.getEstimateAmount());
                    result.setEstimateNumber(led.getEstimateNumber());
                    result.setNameOfWork(led.getNameOfWork());
                    result.setActualEstimateAmount(led.getActualEstimateAmount());
                    if(le.getState().getOwnerPosition() != null)
                        result.setCurrentOwner(worksUtils.getApproverName(le.getState().getOwnerPosition().getId()));
                    if(le.getAdminSanctionBy() != null)
                        result.setAdminSanctionBy(le.getAdminSanctionBy().getName());
                    lineEstimateForLoaSearchResults.add(result);
                }
            }
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
                        .equals(LineEstimateStatus.CHECKED.toString())) {
            approvalPosition = worksUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                    lineEstimate);
        }
        if (workFlowAction.equals(WorksConstants.CANCEL_ACTION)
                && wfmatrix.getNextState().equals(WorksConstants.WF_STATE_CREATED))
            approvalPosition = null;

        return approvalPosition;
    }
    
    @Transactional
    public LineEstimate updateLineEstimateDetails(
            final LineEstimate lineEstimate,
            final Long approvalPosition, final String approvalComent, String additionalRule,
            final String workFlowAction, final String mode, final ReportOutput reportOutput,
            final String removedLineEstimateDetailsIds,
            final MultipartFile[] files) throws ValidationException, IOException {
        LineEstimate updatedLineEstimate = null; 
        
        if(lineEstimate.getStatus().getCode().equals(LineEstimateStatus.REJECTED.toString())) {
            updatedLineEstimate = update(lineEstimate, removedLineEstimateDetailsIds, files);
            lineEstimateStatusChange(lineEstimate, workFlowAction, mode);
        } else {
            updatedLineEstimate = lineEstimateRepository.save(lineEstimate);
            lineEstimateStatusChange(lineEstimate, workFlowAction, mode);
        }

        createLineEstimateWorkflowTransition(updatedLineEstimate,
                approvalPosition, approvalComent, additionalRule, workFlowAction);

        return updatedLineEstimate;
    }
    
    public void lineEstimateStatusChange(final LineEstimate lineEstimate, final String workFlowAction,
            final String mode) {
        if (null != lineEstimate && null != lineEstimate.getStatus()
                && null != lineEstimate.getStatus().getCode()) {
            if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.CREATED.toString())
                    && lineEstimate.getState() != null && workFlowAction.equals(WorksConstants.SUBMIT_ACTION))
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE, 
                        LineEstimateStatus.CHECKED.toString()));
            else if (lineEstimate.getStatus().getCode()
                    .equals(LineEstimateStatus.CHECKED.toString()) && !workFlowAction.equals(WorksConstants.REJECT_ACTION))
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE, 
                        LineEstimateStatus.BUDGET_SANCTIONED.toString()));
            else if (lineEstimate.getStatus().getCode()
                    .equals(LineEstimateStatus.BUDGET_SANCTIONED.toString()) && !workFlowAction.equals(WorksConstants.REJECT_ACTION))
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE, 
                        LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString()));
            else if (lineEstimate.getStatus().getCode()
                    .equals(LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString()) && !workFlowAction.equals(WorksConstants.REJECT_ACTION)) {
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE, 
                        LineEstimateStatus.TECHNICAL_SANCTIONED.toString()));
            }
            else if (workFlowAction.equals(WorksConstants.REJECT_ACTION)) {
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE, 
                        LineEstimateStatus.REJECTED.toString()));
            }
            else if(lineEstimate.getStatus().getCode()
                    .equals(LineEstimateStatus.REJECTED.toString()) && workFlowAction.equals(WorksConstants.CANCEL_ACTION)) {
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE, 
                        LineEstimateStatus.CANCELLED.toString()));
            }
            else if(lineEstimate.getStatus().getCode()
                    .equals(LineEstimateStatus.REJECTED.toString()) && workFlowAction.equals(WorksConstants.FORWARD_ACTION)) {
                lineEstimate.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.MODULETYPE, 
                        LineEstimateStatus.CREATED.toString()));
            }
        }
    }

    public List<User> getLineEstimateCreatedByUsers() {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(LineEstimate.class);
        criteria.add(Restrictions.isNotNull("technicalSanctionNumber"));
        List<LineEstimate> lineEstimates = criteria.list();
        List<User> users = new ArrayList<User>();
        for(LineEstimate le : lineEstimates) {
            if(users.isEmpty())
                users.add(le.getCreatedBy());
            for(User user : users) {
                if(le.getCreatedBy().getId() != user.getId()) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    public List<Department> getUserDepartments(User currentUser) {
        List<Assignment> assignments = assignmentService.findByEmployeeAndGivenDate(currentUser.getId(), new Date());
        List<Department> departments = new ArrayList<Department>();
        for(Assignment assignment : assignments){
            if(departments.isEmpty())
                departments.add(assignment.getDepartment());
            for(Department d : departments) {
                if(!d.getName().equals(assignment.getDepartment().getName()))
                    departments.add(assignment.getDepartment());
            }
        }
        return departments;
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
        String currState = "";
        String natureOfwork = WorksConstants.WORKFLOWTYPE_DISPLAYNAME;

        if (null != lineEstimate.getId()) {
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(lineEstimate.getCreatedBy().getId());
        }
        if (WorksConstants.REJECT_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment)) {
                lineEstimate.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork);
            } else {
                final String stateValue = WorksConstants.WF_STATE_REJECTED;
                lineEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition())
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
                        .withNatureOfTask(natureOfwork);
            } else if(WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = WorksConstants.WF_STATE_CANCELLED;
                wfmatrix = lineEstimateWorkflowService.getWfMatrix(lineEstimate.getStateType(), null,
                        null, additionalRule, lineEstimate.getCurrentState().getValue(), null);
                lineEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNatureOfTask(natureOfwork);
            } else {
                wfmatrix = lineEstimateWorkflowService.getWfMatrix(lineEstimate.getStateType(), null,
                        null, additionalRule, lineEstimate.getCurrentState().getValue(), null);
                lineEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNatureOfTask(natureOfwork);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    public boolean validateTechnicalSanctionDate(Long id, Date technicalSanctionDate) {
        LineEstimate lineEstimate = lineEstimateRepository.findOne(id);
        if(lineEstimate.getAdminSanctionDate() != null && lineEstimate.getAdminSanctionDate().after(technicalSanctionDate))
            return false;
        return true;
    }

}
