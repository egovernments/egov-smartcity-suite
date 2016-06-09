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
package org.egov.works.abstractestimate.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.asset.service.AssetService;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.AssetsForEstimate;
import org.egov.works.abstractestimate.entity.EstimateTechnicalSanction;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.abstractestimate.entity.MultiYearEstimate;
import org.egov.works.abstractestimate.entity.OverheadValue;
import org.egov.works.abstractestimate.entity.SearchAbstractEstimate;
import org.egov.works.abstractestimate.repository.AbstractEstimateRepository;
import org.egov.works.autonumber.TechnicalSanctionNumberGenerator;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.master.service.OverheadService;
import org.egov.works.reports.entity.WorkProgressRegister;
import org.egov.works.reports.service.WorkProgressRegisterService;
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
public class EstimateService {

    private static final Logger LOG = LoggerFactory.getLogger(EstimateService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final AbstractEstimateRepository abstractEstimateRepository;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    private EstimateTechnicalSanctionService estimateTechnicalSanctionService;

    @Autowired
    private OverheadService overheadService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<AbstractEstimate> abstractEstimateWorkflowService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private TechnicalSanctionNumberGenerator technicalSanctionNumberGenerator;

    @Autowired
    private AssetService assetService;

    @Autowired
    private WorkProgressRegisterService workProgressRegisterService;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public EstimateService(final AbstractEstimateRepository abstractEstimateRepository) {
        this.abstractEstimateRepository = abstractEstimateRepository;
    }

    public AbstractEstimate getAbstractEstimateById(final Long id) {
        return abstractEstimateRepository.findOne(id);
    }

    @Transactional
    public AbstractEstimate createAbstractEstimate(final AbstractEstimate abstractEstimate, final MultipartFile[] files,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) throws IOException {
        AbstractEstimate newAbstractEstimate = null;
        final AbstractEstimate abstractEstimateFromDB = getAbstractEstimateByEstimateNumber(
                abstractEstimate.getEstimateNumber());
        if (abstractEstimateFromDB == null) {
            for (final MultiYearEstimate multiYearEstimate : abstractEstimate.getMultiYearEstimates())
                multiYearEstimate.setAbstractEstimate(abstractEstimate);
            for (final FinancialDetail financialDetail : abstractEstimate.getFinancialDetails())
                financialDetail.setAbstractEstimate(abstractEstimate);
            for (final OverheadValue obj : abstractEstimate.getOverheadValues()) {
                obj.setAbstractEstimate(abstractEstimate);
                obj.setOverhead(overheadService.getOverheadById(obj.getOverhead().getId()));
            }
            for (final AssetsForEstimate assetsForEstimate : abstractEstimate.getAssetValues()) {
                assetsForEstimate.setAbstractEstimate(abstractEstimate);
                assetsForEstimate.setAsset(assetService.getAssetByCode(assetsForEstimate.getAsset().getCode()));
            }
            for (final Activity act : abstractEstimate.getActivities())
                act.setAbstractEstimate(abstractEstimate);
            abstractEstimate.setProjectCode(abstractEstimate.getLineEstimateDetails().getProjectCode());
            newAbstractEstimate = abstractEstimateRepository.save(abstractEstimate);
        } else
            newAbstractEstimate = updateAbstractEstimate(abstractEstimateFromDB, abstractEstimate);

        createAbstractEstimateWorkflowTransition(newAbstractEstimate, approvalPosition, approvalComent, additionalRule,
                workFlowAction);
        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, newAbstractEstimate,
                WorksConstants.ABSTRACTESTIMATE);
        if (!documentDetails.isEmpty()) {
            newAbstractEstimate.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return newAbstractEstimate;

    }

    @Transactional
    public AbstractEstimate updateAbstractEstimate(final AbstractEstimate abstractEstimateFromDB,
            final AbstractEstimate newAbstractEstimate) {
        abstractEstimateFromDB.setEstimateDate(newAbstractEstimate.getEstimateDate());
        abstractEstimateFromDB.setEstimateNumber(newAbstractEstimate.getEstimateNumber());
        abstractEstimateFromDB.setName(newAbstractEstimate.getName());
        abstractEstimateFromDB.setDescription(newAbstractEstimate.getDescription());
        abstractEstimateFromDB.setWard(newAbstractEstimate.getWard());
        abstractEstimateFromDB.setNatureOfWork(newAbstractEstimate.getNatureOfWork());
        abstractEstimateFromDB.setLocation(newAbstractEstimate.getLocation());
        abstractEstimateFromDB.setParentCategory(newAbstractEstimate.getParentCategory());
        abstractEstimateFromDB.setCategory(newAbstractEstimate.getCategory());
        abstractEstimateFromDB.setExecutingDepartment(newAbstractEstimate.getExecutingDepartment());
        abstractEstimateFromDB.setProjectCode(newAbstractEstimate.getLineEstimateDetails().getProjectCode());
        abstractEstimateFromDB.setLineEstimateDetails(newAbstractEstimate.getLineEstimateDetails());

        for (final MultiYearEstimate multiYearEstimate : abstractEstimateFromDB.getMultiYearEstimates()) {
            multiYearEstimate.setCreatedDate(new Date());
            multiYearEstimate.setLastModifiedDate(new Date());
            multiYearEstimate.setCreatedBy(securityUtils.getCurrentUser());
            multiYearEstimate.setLastModifiedBy(securityUtils.getCurrentUser());
        }

        abstractEstimateFromDB.getActivities().clear();
        for (final Activity act : newAbstractEstimate.getActivities()) {
            act.setAbstractEstimate(abstractEstimateFromDB);
            abstractEstimateFromDB.addActivity(act);
        }

        for (final FinancialDetail finacilaDetail : abstractEstimateFromDB.getFinancialDetails()) {
            finacilaDetail.setCreatedDate(new Date());
            finacilaDetail.setLastModifiedDate(new Date());
            finacilaDetail.setCreatedBy(securityUtils.getCurrentUser());
            finacilaDetail.setLastModifiedBy(securityUtils.getCurrentUser());
        }
        for (final AssetsForEstimate assetsForEstimate : newAbstractEstimate.getAssetValues()) {
            assetsForEstimate.setAbstractEstimate(abstractEstimateFromDB);
            assetsForEstimate.setAsset(assetService.getAssetByCode(assetsForEstimate.getAsset().getCode()));
            abstractEstimateFromDB.addAssetValue(assetsForEstimate);
        }
        abstractEstimateFromDB.setEstimateValue(newAbstractEstimate.getEstimateValue());
        abstractEstimateFromDB.setWorkValue(newAbstractEstimate.getWorkValue());
        abstractEstimateFromDB.setCreatedDate(new Date());
        abstractEstimateFromDB.setLastModifiedDate(new Date());
        abstractEstimateFromDB.setCreatedBy(securityUtils.getCurrentUser());
        abstractEstimateFromDB.setLastModifiedBy(securityUtils.getCurrentUser());

        abstractEstimateFromDB.getOverheadValues().clear();
        for (final OverheadValue value : newAbstractEstimate.getOverheadValues()) {
            value.setAbstractEstimate(abstractEstimateFromDB);
            value.setOverhead(overheadService.getOverheadById(value.getOverhead().getId()));
            abstractEstimateFromDB.addOverheadValue(value);
        }

        abstractEstimateRepository.save(abstractEstimateFromDB);
        return abstractEstimateFromDB;
    }

    @Transactional
    public AbstractEstimate createAbstractEstimateOnLineEstimateTechSanction(
            final LineEstimateDetails lineEstimateDetails, final int i) {
        final AbstractEstimate savedAbstractEstimate = abstractEstimateRepository
                .save(populateAbstractEstimate(lineEstimateDetails));
        saveTechnicalSanction(savedAbstractEstimate, i);
        return savedAbstractEstimate;
    }

    private AbstractEstimate populateAbstractEstimate(final LineEstimateDetails lineEstimateDetails) {
        final AbstractEstimate abstractEstimate = new AbstractEstimate();
        abstractEstimate.setEstimateDate(lineEstimateDetails.getLineEstimate().getLineEstimateDate());
        abstractEstimate.setEstimateNumber(lineEstimateDetails.getEstimateNumber());
        abstractEstimate.setName(lineEstimateDetails.getNameOfWork());
        abstractEstimate.setDescription(lineEstimateDetails.getNameOfWork());
        abstractEstimate.setWard(lineEstimateDetails.getLineEstimate().getWard());
        abstractEstimate.setNatureOfWork(lineEstimateDetails.getLineEstimate().getNatureOfWork());
        if (lineEstimateDetails.getLineEstimate().getLocation() != null)
            abstractEstimate.setLocation(lineEstimateDetails.getLineEstimate().getLocation().getName());
        abstractEstimate.setParentCategory(lineEstimateDetails.getLineEstimate().getTypeOfWork());
        abstractEstimate.setCategory(lineEstimateDetails.getLineEstimate().getSubTypeOfWork());
        abstractEstimate.setExecutingDepartment(lineEstimateDetails.getLineEstimate().getExecutingDepartment());
        abstractEstimate.setWorkValue(lineEstimateDetails.getActualEstimateAmount().doubleValue());
        abstractEstimate.setEstimateValue(lineEstimateDetails.getActualEstimateAmount());
        abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE,
                AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString()));
        abstractEstimate.setProjectCode(lineEstimateDetails.getProjectCode());
        abstractEstimate.setApprovedDate(lineEstimateDetails.getLineEstimate().getTechnicalSanctionDate());
        abstractEstimate.setLineEstimateDetails(lineEstimateDetails);
        abstractEstimate.addFinancialDetails(populateEstimateFinancialDetails(abstractEstimate));
        abstractEstimate.addMultiYearEstimate(populateMultiYearEstimate(abstractEstimate));
        return abstractEstimate;
    }

    private FinancialDetail populateEstimateFinancialDetails(final AbstractEstimate abstractEstimate) {
        final FinancialDetail financialDetail = new FinancialDetail();
        financialDetail.setAbstractEstimate(abstractEstimate);
        financialDetail.setFund(abstractEstimate.getLineEstimateDetails().getLineEstimate().getFund());
        financialDetail.setFunction(abstractEstimate.getLineEstimateDetails().getLineEstimate().getFunction());
        financialDetail.setBudgetGroup(abstractEstimate.getLineEstimateDetails().getLineEstimate().getBudgetHead());
        financialDetail.setScheme(abstractEstimate.getLineEstimateDetails().getLineEstimate().getScheme());
        financialDetail.setSubScheme(abstractEstimate.getLineEstimateDetails().getLineEstimate().getSubScheme());
        return financialDetail;
    }

    private MultiYearEstimate populateMultiYearEstimate(final AbstractEstimate abstractEstimate) {
        final MultiYearEstimate multiYearEstimate = new MultiYearEstimate();
        multiYearEstimate.setAbstractEstimate(abstractEstimate);
        multiYearEstimate
                .setFinancialYear(financialYearHibernateDAO.getFinYearByDate(abstractEstimate.getEstimateDate()));
        multiYearEstimate.setPercentage(100);
        return multiYearEstimate;
    }

    private EstimateTechnicalSanction saveTechnicalSanction(final AbstractEstimate abstractEstimate, final int i) {
        final EstimateTechnicalSanction estimateTechnicalSanction = new EstimateTechnicalSanction();
        estimateTechnicalSanction.setAbstractEstimate(abstractEstimate);
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(abstractEstimate.getLineEstimateDetails().getLineEstimate().getTechnicalSanctionNumber());
        if (i > 0) {
            stringBuilder.append("/");
            stringBuilder.append(i);
        }
        estimateTechnicalSanction.setTechnicalSanctionNumber(stringBuilder.toString());
        estimateTechnicalSanction.setTechnicalSanctionDate(
                abstractEstimate.getLineEstimateDetails().getLineEstimate().getTechnicalSanctionDate());
        estimateTechnicalSanction.setTechnicalSanctionBy(
                abstractEstimate.getLineEstimateDetails().getLineEstimate().getTechnicalSanctionBy());

        // TODO: move to cascade save with AbstractEstimate object once
        // AbstractEstimate entity converted to JPA
        return estimateTechnicalSanctionService.save(estimateTechnicalSanction);
    }

    public AbstractEstimate getAbstractEstimateByEstimateNumber(final String estimateNumber) {
        return abstractEstimateRepository.findByEstimateNumberAndEgwStatus_codeNotLike(estimateNumber,
                AbstractEstimate.EstimateStatus.CANCELLED.toString());
    }

    public AbstractEstimate getAbstractEstimateByEstimateNumberAndStatus(final String estimateNumber) {
        return abstractEstimateRepository.findByLineEstimateDetails_EstimateNumberAndEgwStatus_codeEquals(
                estimateNumber, AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
    }

    public AbstractEstimate getAbstractEstimateByLineEstimateDetailsForCancelLineEstimate(final Long id) {
        return abstractEstimateRepository.findByLineEstimateDetails_IdAndEgwStatus_codeEquals(id,
                AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
    }

    public BigDecimal getEstimateValueForLineEstimate(final LineEstimateDetails lineEstimateDetails) {
        WorkProgressRegister workProgressRegister = workProgressRegisterService
                .getWorkProgressRegisterByLineEstimateDetailsId(lineEstimateDetails);
        return workProgressRegister != null ? workProgressRegister.getTotalBillPaidSoFar() : BigDecimal.ZERO;
    }

    public void populateDataForAbstractEstimate(final LineEstimateDetails lineEstimateDetails, final Model model,
            final AbstractEstimate abstractEstimate) {
        final LineEstimate lineEstimate = lineEstimateDetails.getLineEstimate();
        abstractEstimate.setLineEstimateDetails(lineEstimateDetails);
        abstractEstimate.setExecutingDepartment(lineEstimateDetails.getLineEstimate().getExecutingDepartment());
        abstractEstimate.setWard(lineEstimateDetails.getLineEstimate().getWard());
        if (lineEstimate.getLocation() != null)
            abstractEstimate.setLocation(lineEstimate.getLocation().getName());
        abstractEstimate.setNatureOfWork(lineEstimate.getNatureOfWork());
        abstractEstimate.setParentCategory(lineEstimate.getTypeOfWork());
        abstractEstimate.setCategory(lineEstimate.getSubTypeOfWork());
        abstractEstimate.setProjectCode(lineEstimateDetails.getProjectCode());
        final List<FinancialDetail> financialDetailList = new ArrayList<FinancialDetail>();
        final FinancialDetail financialDetails = new FinancialDetail();
        financialDetails.setFund(lineEstimate.getFund());
        financialDetails.setFunction(lineEstimate.getFunction());
        financialDetails.setScheme(lineEstimate.getScheme());
        financialDetails.setSubScheme(lineEstimate.getSubScheme());
        financialDetails.setBudgetGroup(lineEstimate.getBudgetHead());
        financialDetailList.add(financialDetails);
        abstractEstimate.setFinancialDetails(financialDetailList);
        model.addAttribute("estimateValue", getEstimateValueForLineEstimate(lineEstimateDetails));
        model.addAttribute("lineEstimateDetails", lineEstimateDetails);
        model.addAttribute("abstractEstimate", abstractEstimate);
        model.addAttribute("lineEstimate", lineEstimate);
        model.addAttribute("workOrder",
                letterOfAcceptanceService.getWorkOrderByEstimateNumber(lineEstimateDetails.getEstimateNumber()));
        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_SHOW_SERVICE_FIELDS);
        final AppConfigValues value = values.get(0);
        if (value.getValue().equalsIgnoreCase("Yes"))
            model.addAttribute("isServiceVATRequired", true);
        else
            model.addAttribute("isServiceVATRequired", false);

    }

    public void validateAssetDetails(final AbstractEstimate abstractEstimate, final BindingResult bindErrors) {
        if (worksApplicationProperties.assetRequired().toString().equalsIgnoreCase("Yes")) {
            final List<AppConfigValues> appConfigvalues = appConfigValuesService.getConfigValuesByModuleAndKey(
                    WorksConstants.WORKS_MODULE_NAME, WorksConstants.ASSETDETAILS_REQUIRED_FOR_ESTIMATE);
            final AppConfigValues value = appConfigvalues.get(0);
            if (value.getValue().equalsIgnoreCase("Yes") && abstractEstimate.getAssetValues() != null
                    && abstractEstimate.getAssetValues().isEmpty()) {
                bindErrors.reject("error.assetdetails.required", "error.assetdetails.required");
            }
        }
    }

    public Long getApprovalPositionByMatrixDesignation(final AbstractEstimate abstractEstimate, Long approvalPosition,
            final String additionalRule, final String mode, final String workFlowAction) {
        final WorkFlowMatrix wfmatrix = abstractEstimateWorkflowService.getWfMatrix(abstractEstimate.getStateType(),
                null, null, additionalRule, abstractEstimate.getCurrentState().getValue(), null);
        if (abstractEstimate.getEgwStatus() != null && abstractEstimate.getEgwStatus().getCode() != null)
            if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.CREATED.toString())
                    && abstractEstimate.getState() != null)
                if (mode.equals("edit"))
                    approvalPosition = abstractEstimate.getState().getOwnerPosition().getId();
                else
                    approvalPosition = worksUtils.getApproverPosition(wfmatrix.getNextDesignation(),
                            abstractEstimate.getState(), abstractEstimate.getCreatedBy().getId());
        if (workFlowAction.equals(WorksConstants.CANCEL_ACTION)
                && wfmatrix.getNextState().equals(WorksConstants.WF_STATE_CREATED))
            approvalPosition = null;

        return approvalPosition;
    }

    @Transactional
    public AbstractEstimate updateAbstractEstimateDetails(final AbstractEstimate abstractEstimate,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction, final String mode, final ReportOutput reportOutput,
            final MultipartFile[] files) throws ValidationException, IOException {
        AbstractEstimate updatedAbstractEstimate = null;

        if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.REJECTED.toString())) {
            updatedAbstractEstimate = abstractEstimateRepository.save(abstractEstimate);
            final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, updatedAbstractEstimate,
                    WorksConstants.ABSTRACTESTIMATE);
            if (!documentDetails.isEmpty()) {
                updatedAbstractEstimate.setDocumentDetails(documentDetails);
                worksUtils.persistDocuments(documentDetails);
            }
        }
        if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.CREATED.toString())
                && workFlowAction.equals(WorksConstants.SUBMIT_ACTION))
            saveTechnicalSanctionDetails(abstractEstimate);

        if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.TECH_SANCTIONED.toString())
                && workFlowAction.equalsIgnoreCase(WorksConstants.ACTION_APPROVE))
            saveAdminSanctionDetails(abstractEstimate);

        abstractEstimateStatusChange(abstractEstimate, workFlowAction, mode);
        updatedAbstractEstimate = abstractEstimateRepository.save(abstractEstimate);

        if (!workFlowAction.equals(WorksConstants.SAVE_ACTION))
            createAbstractEstimateWorkflowTransition(updatedAbstractEstimate, approvalPosition, approvalComent,
                    additionalRule, workFlowAction);

        return updatedAbstractEstimate;
    }

    private void saveAdminSanctionDetails(AbstractEstimate abstractEstimate) {
        abstractEstimate.setApprovedBy(securityUtils.getCurrentUser());
        abstractEstimate.setApprovedDate(new Date());
    }

    private void saveTechnicalSanctionDetails(final AbstractEstimate abstractEstimate) {
        final EstimateTechnicalSanction estimateTechnicalSanction = new EstimateTechnicalSanction();
        estimateTechnicalSanction.setAbstractEstimate(abstractEstimate);
        estimateTechnicalSanction.setTechnicalSanctionBy(securityUtils.getCurrentUser());
        estimateTechnicalSanction.setTechnicalSanctionDate(new Date());
        estimateTechnicalSanction
                .setTechnicalSanctionNumber(technicalSanctionNumberGenerator.getNextNumber(abstractEstimate));

        abstractEstimate.getEstimateTechnicalSanctions().add(estimateTechnicalSanction);
    }

    public void abstractEstimateStatusChange(final AbstractEstimate abstractEstimate, final String workFlowAction,
            final String mode) throws ValidationException {
        if (null != abstractEstimate && null != abstractEstimate.getEgwStatus()
                && null != abstractEstimate.getEgwStatus().getCode())
            if (workFlowAction.equals(WorksConstants.SAVE_ACTION))
                abstractEstimate.setEgwStatus(egwStatusHibernateDAO
                        .getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE, EstimateStatus.NEW.toString()));
            else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.NEW.toString()))
                abstractEstimate.setEgwStatus(egwStatusHibernateDAO
                        .getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE, EstimateStatus.CREATED.toString()));
            else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.CREATED.toString())
                    && abstractEstimate.getState() != null && workFlowAction.equals(WorksConstants.SUBMIT_ACTION))
                abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                        WorksConstants.ABSTRACTESTIMATE, EstimateStatus.TECH_SANCTIONED.toString()));
            else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.TECH_SANCTIONED.toString())
                    && !workFlowAction.equals(WorksConstants.REJECT_ACTION))
                abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                        WorksConstants.ABSTRACTESTIMATE, EstimateStatus.ADMIN_SANCTIONED.toString()));
            else if (workFlowAction.equals(WorksConstants.REJECT_ACTION))
                abstractEstimate.setEgwStatus(egwStatusHibernateDAO
                        .getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE, EstimateStatus.REJECTED.toString()));
            else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.CANCEL_ACTION))
                abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                        WorksConstants.ABSTRACTESTIMATE, EstimateStatus.CANCELLED.toString()));
            else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.FORWARD_ACTION))
                abstractEstimate.setEgwStatus(egwStatusHibernateDAO
                        .getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE, EstimateStatus.CREATED.toString()));
    }

    public void createAbstractEstimateWorkflowTransition(final AbstractEstimate abstractEstimate,
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
        final String natureOfwork = WorksConstants.WORKFLOWTYPE_DISPLAYNAME_ESTIMATE;
        WorkFlowMatrix wfmatrix = null;

        if (null != abstractEstimate.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(abstractEstimate.getCreatedBy().getId());
        if (WorksConstants.REJECT_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment))
                abstractEstimate.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate()).withNatureOfTask(natureOfwork);
            else
                abstractEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.WF_STATE_REJECTED)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition()).withNextAction("")
                        .withNatureOfTask(natureOfwork);
        } else if (WorksConstants.SAVE_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            wfmatrix = abstractEstimateWorkflowService.getWfMatrix(abstractEstimate.getStateType(), null, null,
                    additionalRule, WorksConstants.NEW, null);
            if (abstractEstimate.getState() == null)
                abstractEstimate.transition(true).start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.NEW)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition())
                        .withNextAction(WorksConstants.ESTIMATE_ONSAVE_NEXTACTION_VALUE).withNatureOfTask(natureOfwork);
            else
                abstractEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.NEW)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition())
                        .withNextAction(WorksConstants.ESTIMATE_ONSAVE_NEXTACTION_VALUE).withNatureOfTask(natureOfwork);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            if (null == abstractEstimate.getState()) {
                wfmatrix = abstractEstimateWorkflowService.getWfMatrix(abstractEstimate.getStateType(), null, null,
                        additionalRule, currState, null);
                abstractEstimate.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction()).withNatureOfTask(natureOfwork);
            } else if (WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = WorksConstants.WF_STATE_CANCELLED;
                wfmatrix = abstractEstimateWorkflowService.getWfMatrix(abstractEstimate.getStateType(), null, null,
                        additionalRule, abstractEstimate.getCurrentState().getValue(), null);
                abstractEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(pos).withNextAction("").withNatureOfTask(natureOfwork);
            } else {
                wfmatrix = abstractEstimateWorkflowService.getWfMatrix(abstractEstimate.getStateType(), null, null,
                        additionalRule, abstractEstimate.getCurrentState().getValue(), null);
                abstractEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(natureOfwork);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    public List<AbstractEstimate> getAbstractEstimateByEstimateNumberLike(final String estimateNumber) {
        return abstractEstimateRepository.findByEstimateNumberContainingIgnoreCase("%" + estimateNumber + "%");
    }

    public List<User> getCreatedByForViewAbstractEstimates() {
        return abstractEstimateRepository.findCreatedByForViewAbstractEstimates();
    }

    public List<AbstractEstimate> searchAbstractEstimates(final SearchAbstractEstimate searchAbstractEstimate) {
        if (searchAbstractEstimate != null) {
            final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(AbstractEstimate.class)
                    .createAlias("lineEstimateDetails", "led").createAlias("egwStatus", "status")
                    .createAlias("led.projectCode", "pc");
            if (searchAbstractEstimate.getAbstractEstimateNumber() != null)
                criteria.add(Restrictions.eq("estimateNumber", searchAbstractEstimate.getAbstractEstimateNumber()));
            if (searchAbstractEstimate.getDepartment() != null)
                criteria.add(Restrictions.eq("executingDepartment.id", searchAbstractEstimate.getDepartment()));
            if (searchAbstractEstimate.getWorkIdentificationNumber() != null)
                criteria.add(Restrictions.ilike("pc.code", searchAbstractEstimate.getWorkIdentificationNumber(),
                        MatchMode.ANYWHERE));
            if (searchAbstractEstimate.getStatus() != null) {
                criteria.add(Restrictions.eq("status.code", searchAbstractEstimate.getStatus()).ignoreCase());
            }
            if (searchAbstractEstimate.getCreatedBy() != null) {
                criteria.add(Restrictions.eq("createdBy.id", searchAbstractEstimate.getCreatedBy()));
            }
            if (searchAbstractEstimate.getFromDate() != null)
                criteria.add(Restrictions.ge("estimateDate", searchAbstractEstimate.getFromDate()));
            if (searchAbstractEstimate.getToDate() != null)
                criteria.add(Restrictions.le("estimateDate", searchAbstractEstimate.getToDate()));

            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            return criteria.list();
        } else
            return new ArrayList<AbstractEstimate>();
    }

}
