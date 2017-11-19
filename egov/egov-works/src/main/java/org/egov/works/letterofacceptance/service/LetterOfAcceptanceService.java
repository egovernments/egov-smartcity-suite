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
package org.egov.works.letterofacceptance.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.pims.commons.Designation;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.repository.ContractorBillRegisterRepository;
import org.egov.works.letterofacceptance.entity.SearchRequestContractor;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.egov.works.letterofacceptance.entity.WorkOrderHistory;
import org.egov.works.letterofacceptance.repository.LetterOfAcceptanceRepository;
import org.egov.works.letterofacceptance.repository.WorkOrderHistoryRepository;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.lineestimate.service.LineEstimateAppropriationService;
import org.egov.works.lineestimate.service.LineEstimateDetailService;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.service.MilestoneService;
import org.egov.works.models.masters.ContractorDetail;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class LetterOfAcceptanceService {

    @PersistenceContext
    private EntityManager entityManager;

    private final LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private LineEstimateDetailService lineEstimateDetailService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private LineEstimateAppropriationService lineEstimateAppropriationService;

    @Autowired
    private WorkOrderHistoryRepository workOrderHistoryRepository;

    @Autowired
    private ContractorBillRegisterRepository contractorBillRegisterRepository;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private MilestoneService milestoneService;

    @Autowired
    private BudgetControlTypeService budgetControlTypeService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public LetterOfAcceptanceService(final LetterOfAcceptanceRepository letterOfAcceptanceRepository) {
        this.letterOfAcceptanceRepository = letterOfAcceptanceRepository;
    }

    public WorkOrder getWorkOrderById(final Long id) {
        return letterOfAcceptanceRepository.findById(id);
    }

    public List<String> getApprovedWorkOrderByNumberToModifyLOA(final String name) {
        return letterOfAcceptanceRepository.findDistinctWorkorderNumberToModifyLOA("%" + name + "%", WorksConstants.APPROVED,
                ContractorBillRegister.BillStatus.CANCELLED.toString(),
                BillTypes.Final_Bill.toString());
    }

    @Transactional
    public WorkOrder create(final WorkOrder workOrder, final MultipartFile[] files) throws IOException {

        workOrder.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.WORKORDER,
                WorksConstants.APPROVED));

        if (StringUtils.isNotBlank(workOrder.getPercentageSign()) && workOrder.getPercentageSign().equals("-"))
            workOrder.setTenderFinalizedPercentage(workOrder.getTenderFinalizedPercentage() * -1);

        // createWorkOrderEstimate(workOrder);

        final WorkOrder savedworkOrder = letterOfAcceptanceRepository.save(workOrder);
        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, savedworkOrder,
                WorksConstants.WORKORDER);
        if (!documentDetails.isEmpty()) {
            savedworkOrder.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return savedworkOrder;
    }

    public WorkOrderEstimate createWorkOrderEstimate(WorkOrder workOrder) {
        final WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
        workOrderEstimate.setWorkOrder(workOrder);
        workOrderEstimate
                .setEstimate(estimateService.getAbstractEstimateByEstimateNumberAndStatus(workOrder.getEstimateNumber()));
        workOrderEstimate.setEstimateWOAmount(workOrder.getWorkOrderAmount());

        // TO-DO Remove this code after converting entity to JPA
        workOrderEstimate.setCreatedBy(securityUtils.getCurrentUser());
        workOrderEstimate.setModifiedBy(securityUtils.getCurrentUser());
        workOrderEstimate.setCreatedDate(new Date());
        workOrderEstimate.setModifiedDate(new Date());

        workOrder.addWorkOrderEstimate(workOrderEstimate);
        return workOrderEstimate;
    }

    public WorkOrder getWorkOrderByWorkOrderNumber(final String workOrderNumber) {
        return letterOfAcceptanceRepository.findByWorkOrderNumberAndEgwStatus_codeNotLike(workOrderNumber,
                WorksConstants.CANCELLED_STATUS);
    }

    public List<Long> getEngineerInchargeDesignationIds() {
        final List<Long> designationIds = new ArrayList<Long>();
        final List<String> designationNames = new ArrayList<String>();
        final List<AppConfigValues> configList = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_ENGINEERINCHARGE_DESIGNATION);
        for (final AppConfigValues value : configList) {
            designationNames.add(value.getValue().toUpperCase());
        }
        final List<Designation> designations = designationService.getDesignationsByNames(designationNames);
        for (final Designation designation : designations) {
            designationIds.add(designation.getId());
        }
        return designationIds;
    }

    public List<Assignment> getEngineerInchargeList(final Long departmentId, final List<Long> designationIds) {
        return assignmentService.findByDepartmentDesignationsAndGivenDate(
                departmentId, designationIds, new Date());
    }

    public WorkOrder getWorkOrderByEstimateNumber(final String estimateNumber) {
        return letterOfAcceptanceRepository.findByEstimateNumberAndEgwStatus_codeNotLike(estimateNumber,
                WorksConstants.CANCELLED_STATUS);
    }

    public WorkOrder getLetterOfAcceptanceDocumentAttachments(final WorkOrder workOrder) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(workOrder.getId(),
                WorksConstants.WORKORDER);
        workOrder.setDocumentDetails(documentDetailsList);
        return workOrder;
    }

    public WorkOrder getApprovedWorkOrder(final String workOrderNumber) {
        return letterOfAcceptanceRepository.findByWorkOrderNumberAndEgwStatus_codeEquals(workOrderNumber,
                WorksConstants.APPROVED);
    }

    public List<WorkOrder> searchLetterOfAcceptance(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        // TODO Need TO handle in single query
        final List<String> estimateNumbers = lineEstimateDetailsRepository
                .findEstimateNumbersForDepartment(searchRequestLetterOfAcceptance.getDepartmentName());
        if (estimateNumbers.isEmpty())
            estimateNumbers.add("");
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkOrder.class, "wo")
                .addOrder(Order.asc("workOrderDate"))
                .createAlias("wo.contractor", "woc")
                .createAlias("egwStatus", "status");
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                criteria.add(
                        Restrictions.eq("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                criteria.add(Restrictions.ge("workOrderDate", searchRequestLetterOfAcceptance.getFromDate()));
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                criteria.add(Restrictions.le("workOrderDate", searchRequestLetterOfAcceptance.getToDate()));
            if (searchRequestLetterOfAcceptance.getName() != null)
                criteria.add(Restrictions.eq("woc.name", searchRequestLetterOfAcceptance.getName()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getFileNumber() != null)
                criteria.add(
                        Restrictions.ilike("fileNumber", searchRequestLetterOfAcceptance.getFileNumber(), MatchMode.ANYWHERE));
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                criteria.add(Restrictions.eq("estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                criteria.add(Restrictions.in("estimateNumber", estimateNumbers));
            if (searchRequestLetterOfAcceptance.getEgwStatus() != null)
                criteria.add(Restrictions.eq("status.code", searchRequestLetterOfAcceptance.getEgwStatus()));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<WorkOrder> searchLetterOfAcceptanceForContractorBill(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        final List<String> estimateNumbers = lineEstimateService
                .getEstimateNumberForDepartment(searchRequestLetterOfAcceptance.getDepartmentName());
        if (estimateNumbers.isEmpty())
            estimateNumbers.add("");
        // TODO: replace fetching workorders by query with criteria alias
        final List<String> workOrderNumbers = letterOfAcceptanceRepository.getDistinctNonCancelledWorkOrderNumbersByBillType(
                ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString());
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkOrder.class, "wo")
                .createAlias("contractor", "woc")
                .createAlias("egwStatus", "status");

        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                criteria.add(
                        Restrictions.eq("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                criteria.add(Restrictions.ge("workOrderDate", searchRequestLetterOfAcceptance.getFromDate()));
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                criteria.add(Restrictions.le("workOrderDate", searchRequestLetterOfAcceptance.getToDate()));
            if (searchRequestLetterOfAcceptance.getName() != null)
                criteria.add(Restrictions.eq("woc.name", searchRequestLetterOfAcceptance.getName()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getFileNumber() != null)
                criteria.add(
                        Restrictions.ilike("fileNumber", searchRequestLetterOfAcceptance.getFileNumber(), MatchMode.ANYWHERE));
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                criteria.add(Restrictions.eq("estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                criteria.add(Restrictions.in("estimateNumber", estimateNumbers));
            if (workOrderNumbers != null && !workOrderNumbers.isEmpty())
                criteria.add(Restrictions.not(Restrictions.in("workOrderNumber", workOrderNumbers)));
        }
        criteria.add(Restrictions.eq("status.code", WorksConstants.APPROVED));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<String> getApprovedEstimateNumbersToModifyLOA(final String name) {
        return letterOfAcceptanceRepository.findDistinctEstimateNumberToModifyLOA("%" + name + "%", WorksConstants.APPROVED,
                ContractorBillRegister.BillStatus.CANCELLED.toString(),
                BillTypes.Final_Bill.toString());
    }

    public List<String> findDistinctContractorsInWorkOrderByCodeOrName(final String name) {
        final List<String> results = letterOfAcceptanceRepository
                .findDistinctContractorByContractor_codeAndNameContainingIgnoreCase("%" + name + "%");
        return results;
    }

    public List<String> findLoaEstimateNumbersForContractorBill(final String estimateNumber) {
        final List<WorkOrder> workorders = letterOfAcceptanceRepository
                .findByEstimateNumberAndEgwStatus_codeEquals(estimateNumber, WorksConstants.APPROVED);
        final List<String> results = new ArrayList<String>();
        for (final WorkOrder details : workorders)
            results.add(details.getEstimateNumber());
        return results;
    }

    public List<String> getApprovedWorkOrdersForCreateContractorBill(final String workOrderNumber) {
        final List<String> results = letterOfAcceptanceRepository.findWorkOrderNumberForContractorBill(
                "%" + workOrderNumber + "%", WorksConstants.APPROVED, ContractorBillRegister.BillStatus.CANCELLED.toString(),
                BillTypes.Final_Bill.toString());
        return results;

    }

    public List<String> getApprovedEstimateNumbersForCreateContractorBill(final String estimateNumber) {
        final List<String> results = letterOfAcceptanceRepository.findEstimateNumberForContractorBill("%" + estimateNumber + "%",
                WorksConstants.APPROVED, ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString());
        return results;
    }

    public List<String> getApprovedContractorsForCreateContractorBill(final String contractorname) {
        final List<String> results = letterOfAcceptanceRepository.findContractorForContractorBill("%" + contractorname + "%",
                WorksConstants.APPROVED, ContractorBillRegister.BillStatus.CANCELLED.toString(), BillTypes.Final_Bill.toString());
        return results;
    }

    public Boolean validateContractorBillInWorkflowForWorkorder(final Long workOrderId) {
        final List<String> results = letterOfAcceptanceRepository.getContractorBillInWorkflowForWorkorder(workOrderId,
                ContractorBillRegister.BillStatus.CANCELLED.toString(), ContractorBillRegister.BillStatus.APPROVED.toString());
        if (results.isEmpty())
            return true;
        else
            return false;
    }

    public List<ContractorDetail> searchContractorDetails(final SearchRequestContractor searchRequestContractor) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(ContractorDetail.class, "cd")
                .createAlias("contractor", "contractor").createAlias("cd.status", "status");
        if (searchRequestContractor != null) {
            if (searchRequestContractor.getDepartment() != null)
                criteria.add(Restrictions.eq("department.id", searchRequestContractor.getDepartment()));
            if (searchRequestContractor.getContractorClass() != null)
                criteria.add(Restrictions.ge("grade.id", searchRequestContractor.getContractorClass()));
            if (searchRequestContractor.getContractorCode() != null)
                criteria.add(
                        Restrictions.eq("contractor.code", searchRequestContractor.getContractorCode()).ignoreCase());
            if (searchRequestContractor.getNameOfAgency() != null)
                criteria.add(Restrictions.ilike("contractor.name", searchRequestContractor.getNameOfAgency(),
                        MatchMode.ANYWHERE));
        }
        criteria.add(Restrictions.eq("status.code", WorksConstants.ACTIVE).ignoreCase());
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<String> findLoaWorkOrderNumberForMilestone(final String workOrderNumber) {
        final List<WorkOrder> workorders = letterOfAcceptanceRepository
                .findByWorkOrderNumberContainingIgnoreCaseAndEgwStatus_codeEquals(workOrderNumber, WorksConstants.APPROVED);
        final List<String> results = new ArrayList<String>();
        for (final WorkOrder details : workorders)
            results.add(details.getWorkOrderNumber());
        return results;
    }

    public List<String> findWorkIdentificationNumbersToCreateMilestone(final String code) {
        final List<String> workIdNumbers = letterOfAcceptanceRepository
                .findWorkIdentificationNumberToCreateMilestone("%" + code + "%");
        return workIdNumbers;
    }

    public List<WorkOrder> getLoaForCreateMilestone(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        final StringBuilder queryStr = new StringBuilder(500);

        buildWhereClause(searchRequestLetterOfAcceptance, queryStr);
        final Query query = setParameterForMilestone(searchRequestLetterOfAcceptance, queryStr);
        final List<WorkOrder> workOrderList = query.getResultList();
        return workOrderList;
    }

    private void buildWhereClause(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final StringBuilder queryStr) {

        queryStr.append(
                "select distinct wo from WorkOrder wo where wo.egwStatus.moduletype = :moduleType and wo.egwStatus.code = :status and not exists (select ms.workOrderEstimate.workOrder.id from Milestone ms where ms.workOrderEstimate.workOrder.id = wo.id and upper(wo.egwStatus.code)  != upper(:workorderstatus) and upper(ms.status.code)  != upper(:milestonestatus))");
        queryStr.append(
                " and wo.estimateNumber in (select led.estimateNumber from LineEstimateDetails led where led.lineEstimate.executingDepartment.id = :departmentName)");

        if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getWorkIdentificationNumber()))
            queryStr.append(
                    " and wo.estimateNumber = (select led.estimateNumber from LineEstimateDetails led where led.projectCode = (select po.id from ProjectCode po where upper(po.code) = :workIdentificationNumber))");

        if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getEstimateNumber()))
            queryStr.append(" and upper(wo.estimateNumber) = :estimateNumber");

        if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getWorkOrderNumber()))
            queryStr.append(" and upper(wo.workOrderNumber) = :workOrderNumber");

        if (searchRequestLetterOfAcceptance.getTypeOfWork() != null)
            queryStr.append(
                    " and wo.estimateNumber in (select led.estimateNumber from LineEstimateDetails led where led.lineEstimate.typeOfWork.id = :typeOfWork)");

        if (searchRequestLetterOfAcceptance.getSubTypeOfWork() != null)
            queryStr.append(
                    " and wo.estimateNumber in (select led.estimateNumber from LineEstimateDetails led where led.lineEstimate.subTypeOfWork.id = :subTypeOfWork)");

        if (searchRequestLetterOfAcceptance.getAdminSanctionFromDate() != null)
            queryStr.append(
                    " and wo.estimateNumber in (select led.estimateNumber from LineEstimateDetails led where led.lineEstimate.adminSanctionDate >= :adminSanctionFromDate)");

        if (searchRequestLetterOfAcceptance.getAdminSanctionToDate() != null)
            queryStr.append(
                    " and wo.estimateNumber in (select led.estimateNumber from LineEstimateDetails led where led.lineEstimate.adminSanctionDate <= :adminSanctionFromDate)");

    }

    private Query setParameterForMilestone(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        qry.setParameter("status", WorksConstants.APPROVED);
        qry.setParameter("moduleType", WorksConstants.WORKORDER);
        qry.setParameter("workorderstatus", WorksConstants.CANCELLED_STATUS);
        qry.setParameter("milestonestatus", WorksConstants.CANCELLED_STATUS);
        if (searchRequestLetterOfAcceptance != null) {
            qry.setParameter("departmentName", searchRequestLetterOfAcceptance.getDepartmentName());
            if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getWorkIdentificationNumber()))
                qry.setParameter("workIdentificationNumber",
                        searchRequestLetterOfAcceptance.getWorkIdentificationNumber().toUpperCase());
            if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getEstimateNumber()))
                qry.setParameter("estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber().toUpperCase());
            if (StringUtils.isNotBlank(searchRequestLetterOfAcceptance.getWorkOrderNumber()))
                qry.setParameter("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber().toUpperCase());
            if (searchRequestLetterOfAcceptance.getTypeOfWork() != null)
                qry.setParameter("typeOfWork", searchRequestLetterOfAcceptance.getTypeOfWork());
            if (searchRequestLetterOfAcceptance.getSubTypeOfWork() != null)
                qry.setParameter("subTypeOfWork", searchRequestLetterOfAcceptance.getSubTypeOfWork());
            if (searchRequestLetterOfAcceptance.getAdminSanctionFromDate() != null)
                qry.setParameter("adminSanctionFromDate", searchRequestLetterOfAcceptance.getAdminSanctionFromDate());
            if (searchRequestLetterOfAcceptance.getAdminSanctionToDate() != null)
                qry.setParameter("adminSanctionToDate", searchRequestLetterOfAcceptance.getAdminSanctionToDate());

        }
        return qry;
    }

    public Double getGrossBillAmountOfBillsCreated(String workOrderNumber, String status, String billstatus) {
        return letterOfAcceptanceRepository.getGrossBillAmountOfBillsCreated(workOrderNumber, status, billstatus);
    }

    @Transactional
    public WorkOrder update(WorkOrder workOrder, LineEstimateDetails lineEstimateDetails, Double appropriationAmount,
            Double revisedWorkOrderAmount)
            throws ValidationException {
        WorkOrderHistory history = new WorkOrderHistory();
        history.setWorkOrder(workOrder);
        history.setWorkOrderAmount(workOrder.getWorkOrderAmount());
        history.setRevisedWorkOrderAmount(revisedWorkOrderAmount);

        workOrderHistoryRepository.save(history);

        workOrder.setWorkOrderAmount(revisedWorkOrderAmount);
        if (StringUtils.isNotBlank(workOrder.getPercentageSign()) && workOrder.getPercentageSign().equals("-"))
            workOrder.setTenderFinalizedPercentage(workOrder.getTenderFinalizedPercentage() * -1);
        if (workOrder.getPercentageSign().equals("+")) {
            if (appropriationAmount > 0 && !BudgetControlType.BudgetCheckOption.NONE.toString()
                    .equalsIgnoreCase(budgetControlTypeService.getConfigValue())) {
                final List<Long> budgetheadid = new ArrayList<Long>();
                budgetheadid.add(lineEstimateDetails.getLineEstimate().getBudgetHead().getId());
                final boolean flag = lineEstimateDetailService.checkConsumeEncumbranceBudget(lineEstimateDetails,
                        lineEstimateService.getCurrentFinancialYear(new Date())
                                .getId(),
                        appropriationAmount, budgetheadid);

                if (!flag)
                    throw new ValidationException("", "error.budgetappropriation.insufficient.amount");
            }
        } else if (workOrder.getPercentageSign().equals("-")) {
            if (appropriationAmount > 0 && !BudgetControlType.BudgetCheckOption.NONE.toString()
                    .equalsIgnoreCase(budgetControlTypeService.getConfigValue())) {
                String appropriationNumber = lineEstimateAppropriationService
                        .generateBudgetAppropriationNumber(lineEstimateDetails);
                try{
                lineEstimateService.releaseBudgetOnReject(lineEstimateDetails, appropriationAmount, appropriationNumber);
                }catch(final ValidationException v) {
                    throw new ValidationException(v.getErrors());
                }
            }
        }
        final WorkOrder savedworkOrder = letterOfAcceptanceRepository.save(workOrder);
        return savedworkOrder;
    }

    public List<WorkOrder> searchLetterOfAcceptanceToModify(
            final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        // TODO Need TO handle in single query
        final List<String> estimateNumbers = lineEstimateDetailsRepository
                .findEstimateNumbersForDepartment(searchRequestLetterOfAcceptance.getDepartmentName());
        final List<String> workOrderNumbers = letterOfAcceptanceRepository.findWorkOrderNumbersToModifyLoa(
                WorksConstants.APPROVED, ContractorBillRegister.BillStatus.CANCELLED.toString(),
                BillTypes.Final_Bill.toString());
        if (estimateNumbers.isEmpty())
            estimateNumbers.add("");
        if (workOrderNumbers.isEmpty())
            workOrderNumbers.add("");
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkOrder.class, "wo")
                .addOrder(Order.asc("workOrderDate"))
                .createAlias("wo.contractor", "woc")
                .createAlias("egwStatus", "status");
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                criteria.add(
                        Restrictions.eq("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getFromDate() != null)
                criteria.add(Restrictions.ge("workOrderDate", searchRequestLetterOfAcceptance.getFromDate()));
            if (searchRequestLetterOfAcceptance.getToDate() != null)
                criteria.add(Restrictions.le("workOrderDate", searchRequestLetterOfAcceptance.getToDate()));
            if (searchRequestLetterOfAcceptance.getName() != null)
                criteria.add(Restrictions.eq("woc.name", searchRequestLetterOfAcceptance.getName()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getFileNumber() != null)
                criteria.add(
                        Restrictions.ilike("fileNumber", searchRequestLetterOfAcceptance.getFileNumber(), MatchMode.ANYWHERE));
            if (searchRequestLetterOfAcceptance.getEstimateNumber() != null)
                criteria.add(Restrictions.eq("estimateNumber", searchRequestLetterOfAcceptance.getEstimateNumber()).ignoreCase());
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null)
                criteria.add(Restrictions.in("estimateNumber", estimateNumbers));

        }
        criteria.add(Restrictions.in("workOrderNumber", workOrderNumbers));
        criteria.add(Restrictions.eq("status.code", WorksConstants.APPROVED));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<WorkOrder> searchLOAsToCancel(final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(WorkOrder.class, "wo")
                .addOrder(Order.asc("workOrderDate"))
                .createAlias("wo.contractor", "woc")
                .createAlias("egwStatus", "status");
        if (searchRequestLetterOfAcceptance != null) {
            if (searchRequestLetterOfAcceptance.getWorkOrderNumber() != null)
                criteria.add(Restrictions.eq("workOrderNumber", searchRequestLetterOfAcceptance.getWorkOrderNumber())
                        .ignoreCase());
            if (searchRequestLetterOfAcceptance.getContractor() != null) {
                criteria.add(
                        Restrictions.or(Restrictions.eq("woc.name", searchRequestLetterOfAcceptance.getContractor()).ignoreCase(),
                                Restrictions.eq("woc.code", searchRequestLetterOfAcceptance.getContractor()).ignoreCase()));
            }
            if (searchRequestLetterOfAcceptance.getDepartmentName() != null) {
                final List<String> estimateNumbers = lineEstimateDetailsRepository
                        .findEstimateNumbersForDepartment(searchRequestLetterOfAcceptance.getDepartmentName());
                if (estimateNumbers.isEmpty())
                    estimateNumbers.add("");
                criteria.add(Restrictions.in("estimateNumber", estimateNumbers));
            }
            if (searchRequestLetterOfAcceptance.getWorkIdentificationNumber() != null) {
                final List<String> estimateNumbers = lineEstimateDetailsRepository
                        .findEstimateNumbersForWorkIdentificationNumber(searchRequestLetterOfAcceptance
                                .getWorkIdentificationNumber());
                if (estimateNumbers.isEmpty())
                    estimateNumbers.add("");
                criteria.add(Restrictions.in("estimateNumber", estimateNumbers));
            }
            if (searchRequestLetterOfAcceptance.getEgwStatus() != null)
                criteria.add(Restrictions.eq("status.code", searchRequestLetterOfAcceptance.getEgwStatus()));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public List<String> findWorkIdentificationNumbersToSearchLOAToCancel(final String code) {
        final List<String> workIdNumbers = letterOfAcceptanceRepository
                .findWorkIdentificationNumbersToSearchLOAToCancel("%" + code + "%",
                        WorksConstants.APPROVED.toString());
        return workIdNumbers;
    }

    public List<String> findContractorsToSearchLOAToCancel(final String code) {
        final List<String> contractors = letterOfAcceptanceRepository
                .findContractorsToSearchLOAToCancel("%" + code + "%",
                        WorksConstants.APPROVED.toString());
        return contractors;
    }

    public String checkIfBillsCreated(final Long id) {
        String billNumbers = "";
        final WorkOrder workOrder = letterOfAcceptanceRepository.findById(id);
        final List<ContractorBillRegister> bills = contractorBillRegisterRepository.findByWorkOrderAndBillstatusNot(workOrder,
                ContractorBillRegister.BillStatus.CANCELLED.toString());
        if (bills == null || bills.isEmpty())
            return "";
        else {
            for (ContractorBillRegister cbr : bills) {
                billNumbers += cbr.getBillnumber() + ", ";
            }
        }
        return billNumbers;
    }

    public boolean checkIfMileStonesCreated(final WorkOrder workOrder) {
        Boolean flag = false;
        for (WorkOrderEstimate woe : workOrder.getWorkOrderEstimates()) {
            List<Milestone> milestones = milestoneService.getMilestoneByWorkOrderEstimateId(woe.getId());
            for (Milestone ms : milestones) {
                if (!ms.getStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED)) {
                    flag = true;
                    break;
                }
                if (flag)
                    break;
            }
        }
        return flag;
    }

    @Transactional
    public WorkOrder cancel(final WorkOrder workOrder) {
        workOrder.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.WORKORDER,
                WorksConstants.CANCELLED_STATUS));
        workOrder.setStatus(WorksConstants.CANCELLED.toString());
        return letterOfAcceptanceRepository.save(workOrder);
    }

    public List<WorkOrder> findWorkOrderByEstimateNumberAndEgwStatus(final String estimateNumber) {
        return letterOfAcceptanceRepository.findByEstimateNumberAndEgwStatus_codeEquals(estimateNumber, WorksConstants.APPROVED);
    }

    public List<String> getEstimateNumbersToSearchLOAToCancel(final Long lineEstimateId) {
        final List<String> estimateNumbers = letterOfAcceptanceRepository
                .findEstimateNumbersToSearchLOAToCancel(lineEstimateId,
                        WorksConstants.APPROVED.toString());
        return estimateNumbers;
    }
    
    public List<String> getWorkOrderNumbersForViewEstimatePhotograph(final String workOrderNumber) {
        final List<String> workOrderNumbers = letterOfAcceptanceRepository
                .findworkOrderNumbersToViewEstimatePhotograph("%" + workOrderNumber + "%",
                        WorksConstants.APPROVED.toString());
        return workOrderNumbers;
    }

    public List<String> getContractorsNamesForViewEstimatePhotograph(final String contractorName) {
        final List<String> contractorNames = letterOfAcceptanceRepository
                .findContractorsToViewEstimatePhotograph("%" + contractorName + "%",
                        WorksConstants.APPROVED.toString());
        return contractorNames;
    }

}
