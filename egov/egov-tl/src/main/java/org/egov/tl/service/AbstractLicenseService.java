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

package org.egov.tl.service;

import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseDemand;
import org.egov.tl.entity.LicenseDocument;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.LicenseSubCategoryDetails;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.entity.enums.RateTypeEnum;
import org.egov.tl.repository.LicenseDocumentTypeRepository;
import org.egov.tl.repository.LicenseRepository;
import org.egov.tl.service.es.LicenseApplicationIndexService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseNumberUtils;
import org.egov.tl.utils.LicenseUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.math.BigDecimal.ZERO;
import static org.egov.tl.utils.Constants.*;

@Transactional(readOnly = true)
public abstract class AbstractLicenseService<T extends License> {

    public static final String ARREAR = "arrear";
    public static final String LICENSE_WF_INITIATOR_NOT_DEFINED = "license.wf.initiator.not.defined";
    private static final String CURRENT = "current";
    private static final String PENALTY = "penalty";
    @Autowired
    @Qualifier("entityQueryService")
    protected PersistenceService entityQueryService;

    @Autowired
    protected InstallmentHibDao installmentDao;

    @Autowired
    protected LicenseNumberUtils licenseNumberUtils;

    @Autowired
    protected DocumentTypeService documentTypeService;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    protected FileStoreService fileStoreService;

    @Autowired
    protected FeeMatrixService<License> feeMatrixService;

    @Autowired
    protected LicenseDocumentTypeRepository licenseDocumentTypeRepository;

    @Autowired
    protected LicenseApplicationIndexService licenseApplicationIndexService;

    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected DemandGenericHibDao demandGenericDao;

    @Autowired
    protected ValidityService validityService;

    protected SimpleWorkflowService<T> licenseWorkflowService;

    @Autowired
    protected LicenseRepository licenseRepository;

    @Autowired
    protected LicenseStatusService licenseStatusService;

    @Autowired
    protected LicenseAppTypeService licenseAppTypeService;

    @Autowired
    protected PositionMasterService positionMasterService;

    @Autowired
    protected NatureOfBusinessService natureOfBusinessService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private PenaltyRatesService penaltyRatesService;

    @Autowired
    private LicenseUtils licenseUtils;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DesignationService designationService;

    @Autowired
    private TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;

    @Autowired
    private SubCategoryDetailsService subCategoryDetailsService;
    @Autowired
    private FeeTypeService feeTypeService;

    protected abstract LicenseAppType getLicenseApplicationTypeForRenew();

    protected abstract LicenseAppType getLicenseApplicationType();

    protected abstract Module getModuleName();

    protected abstract NatureOfBusiness getNatureOfBusiness();

    protected abstract void sendEmailAndSMS(T license, String currentAction);

    protected abstract LicenseAppType getClosureLicenseApplicationType();

    public void setLicenseWorkflowService(final SimpleWorkflowService<T> licenseWorkflowService) {
        this.licenseWorkflowService = licenseWorkflowService;
    }

    public T getLicenseById(final Long id) {
        return (T) this.licenseRepository.findOne(id);
    }

    @Transactional
    public void create(final T license, final WorkflowBean workflowBean) {
        final Date fromRange = installmentDao.getInsatllmentByModuleForGivenDate(this.getModuleName(), new DateTime().toDate())
                .getFromDate();
        final Date toRange = installmentDao
                .getInsatllmentByModuleForGivenDate(this.getModuleName(), new DateTime().plusYears(1).toDate()).getToDate();
        if (license.getCommencementDate().before(fromRange) || license.getCommencementDate().after(toRange))
            throw new ValidationException("TL-009", "TL-009");
        license.setLicenseAppType(getLicenseApplicationType());
        raiseNewDemand(license);
        license.getLicensee().setLicense(license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        processAndStoreDocument(license.getDocuments(), license);
        final String currentUserRoles = securityUtils.getCurrentUser().getRoles().toString();
        if (!currentUserRoles.contains(CSCOPERATOR))
            transitionWorkFlow(license, workflowBean);
        else
            wfWithCscOperator(license, workflowBean);
        licenseRepository.save(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
    }

    private void wfWithCscOperator(final T license, final WorkflowBean workflowBean) {
        List<Assignment> assignmentList = getAssignments();
        if (!assignmentList.isEmpty()) {
            final Assignment wfAssignment = assignmentList.get(0);
            final String natureOfWork = license.isReNewApplication() ? RENEWAL_NATUREOFWORK : NEW_NATUREOFWORK;
            final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), PUBLIC_HEALTH_DEPT,
                    null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
            if (!license.hasState())
                license.transition().start();
            else
                license.transition().startNext();
            license.transition().withSenderName(
                    wfAssignment.getEmployee().getUsername() + DELIMITER_COLON + wfAssignment.getEmployee().getName())
                    .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(wfAssignment.getPosition())
                    .withNextAction(wfmatrix.getNextAction()).withInitiator(wfAssignment.getPosition());
            license.setEgwStatus(
                    egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
        } else
            throw new ValidationException(LICENSE_WF_INITIATOR_NOT_DEFINED, LICENSE_WF_INITIATOR_NOT_DEFINED);
    }

    private List<Assignment> getAssignments() {
        Department nextAssigneeDept = departmentService.getDepartmentByName(PUBLIC_HEALTH_DEPT);
        Designation nextAssigneeDesig = designationService.getDesignationByName(JA_DESIGNATION);
        List<Assignment> assignmentList = getAssignmentsForDeptAndDesignation(nextAssigneeDept, nextAssigneeDesig);
        if (assignmentList.isEmpty()) {
            nextAssigneeDesig = Optional.ofNullable(designationService.getDesignationByName(SA_DESIGNATION)).
                    orElseThrow(() -> new ValidationException(LICENSE_WF_INITIATOR_NOT_DEFINED, LICENSE_WF_INITIATOR_NOT_DEFINED));
            assignmentList = getAssignmentsForDeptAndDesignation(nextAssigneeDept, nextAssigneeDesig);
        }
        if (assignmentList.isEmpty()) {
            nextAssigneeDesig = Optional.ofNullable(designationService.getDesignationByName(RC_DESIGNATION)).
                    orElseThrow(() -> new ValidationException(LICENSE_WF_INITIATOR_NOT_DEFINED, LICENSE_WF_INITIATOR_NOT_DEFINED));
            assignmentList = getAssignmentsForDeptAndDesignation(nextAssigneeDept, nextAssigneeDesig);
        }
        return assignmentList;
    }

    private List<Assignment> getAssignmentsForDeptAndDesignation(Department nextAssigneeDept, Designation nextAssigneeDesig) {
        return assignmentService.
                findAllAssignmentsByDeptDesigAndDates(nextAssigneeDept.getId(), nextAssigneeDesig.getId(), new Date());
    }

    private BigDecimal raiseNewDemand(final T license) {
        final LicenseDemand ld = new LicenseDemand();
        final Module moduleName = this.getModuleName();
        BigDecimal totalAmount = ZERO;
        final Installment installment = this.installmentDao.getInsatllmentByModuleForGivenDate(moduleName,
                license.getCommencementDate());
        ld.setIsHistory("N");
        ld.setEgInstallmentMaster(installment);
        ld.setLicense(license);
        ld.setIsLateRenewal('0');
        ld.setCreateDate(new Date());
        ld.setModifiedDate(new Date());
        final List<FeeMatrixDetail> feeMatrixDetails = this.feeMatrixService.getLicenseFeeDetails(license,
                license.getCommencementDate());
        for (final FeeMatrixDetail fm : feeMatrixDetails) {
            final EgDemandReasonMaster reasonMaster = this.demandGenericDao
                    .getDemandReasonMasterByCode(fm.getFeeMatrix().getFeeType().getName(), moduleName);
            final EgDemandReason reason = this.demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, installment, moduleName);
            if (fm.getFeeMatrix().getFeeType().getName().contains("Late"))
                continue;

            if (reason != null) {
                BigDecimal tradeAmt = calculateAmountByRateType(license, fm);
                ld.getEgDemandDetails().add(EgDemandDetails.fromReasonAndAmounts(tradeAmt, reason, ZERO));
                totalAmount = totalAmount.add(tradeAmt);
            }
        }

        ld.setBaseDemand(totalAmount);
        license.setLicenseDemand(ld);
        return totalAmount;
    }

    private BigDecimal calculateAmountByRateType(License license, FeeMatrixDetail feeMatrixDetail) {
        Long feeTypeId = feeTypeService.findByName(LICENSE_FEE_TYPE).getId();
        LicenseSubCategoryDetails licenseSubCategoryDetails = subCategoryDetailsService.getSubcategoryDetailBySubcategoryAndFeeType(license.getTradeName().getId(), feeTypeId);
        BigDecimal amt = BigDecimal.ZERO;
        if (licenseSubCategoryDetails != null) {
            if (RateTypeEnum.Flat_by_Range.equals(licenseSubCategoryDetails.getRateType()))
                amt = feeMatrixDetail.getAmount();
            else if (RateTypeEnum.Percentage.equals(licenseSubCategoryDetails.getRateType()))
                amt = license.getTradeArea_weight().multiply(feeMatrixDetail.getAmount()).divide(new BigDecimal(100));
            else if (RateTypeEnum.Unit_by_Range.equals(licenseSubCategoryDetails.getRateType()))
                amt = license.getTradeArea_weight().multiply(feeMatrixDetail.getAmount());
        }
        return amt;
    }

    public License updateDemandForChangeTradeArea(final T license) {
        final LicenseDemand licenseDemand = license.getLicenseDemand();
        final Set<EgDemandDetails> demandDetails = licenseDemand.getEgDemandDetails();
        final Date licenseDate = license.isNewApplication() ? license.getCommencementDate()
                : license.getLicenseDemand().getEgInstallmentMaster().getFromDate();
        final List<FeeMatrixDetail> feeList = this.feeMatrixService.getLicenseFeeDetails(license, licenseDate);
        for (final EgDemandDetails dmd : demandDetails)
            for (final FeeMatrixDetail fm : feeList)
                if (licenseDemand.getEgInstallmentMaster().equals(dmd.getEgDemandReason().getEgInstallmentMaster()) &&
                        dmd.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(fm.getFeeMatrix().getFeeType().getName())) {
                    BigDecimal tradeAmt = calculateAmountByRateType(license, fm);
                    dmd.setAmount(tradeAmt);
                    dmd.setModifiedDate(new Date());
                }
        licenseDemand.recalculateBaseDemand();
        return license;

    }

    @Transactional
    public void recalculateDemand(final List<FeeMatrixDetail> feeList, final T license) {
        final LicenseDemand licenseDemand = license.getCurrentDemand();
        // Recalculating current demand detail according to fee matrix
        for (final EgDemandDetails dmd : licenseDemand.getEgDemandDetails())
            for (final FeeMatrixDetail fm : feeList)
                if (licenseDemand.getEgInstallmentMaster().equals(dmd.getEgDemandReason().getEgInstallmentMaster()) &&
                        dmd.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(fm.getFeeMatrix().getFeeType().getName())) {
                    BigDecimal tradeAmt = calculateAmountByRateType(license, fm);
                    dmd.setAmount(tradeAmt.setScale(0, RoundingMode.HALF_UP));
                    dmd.setAmtCollected(ZERO);
                }
        licenseDemand.recalculateBaseDemand();
    }

    @Transactional
    public void renew(final T license, final WorkflowBean workflowBean) {
        license.setLicenseAppType(getLicenseApplicationTypeForRenew());
        final String natureOfWork = license.isReNewApplication()
                ? RENEWAL_NATUREOFWORK : NEW_NATUREOFWORK;
        final Assignment wfInitiator = this.assignmentService
                .getPrimaryAssignmentForUser(this.securityUtils.getCurrentUser().getId());
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        recalculateDemand(this.feeMatrixService.getLicenseFeeDetails(license,
                license.getLicenseDemand().getEgInstallmentMaster().getFromDate()), license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        license.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
        license.setLicenseAppType(this.getLicenseApplicationTypeForRenew());
        final User currentUser = this.securityUtils.getCurrentUser();
        final String currentUserRoles = securityUtils.getCurrentUser().getRoles().toString();
        if (!currentUserRoles.contains(CSCOPERATOR)) {
            final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
            if (!license.hasState())
                license.transition().start();
            else
                license.transition().startNext();

            license.transition().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate())
                    .withOwner(wfInitiator.getPosition())
                    .withNextAction(wfmatrix.getNextAction()).withInitiator(wfInitiator.getPosition());
        } else
            wfWithCscOperator(license, workflowBean);
        this.licenseRepository.save(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void raiseDemand(final T licenze, final Module module, final Installment installment) {
        // Refetching license in this txn to avoid lazy initialization issue
        final License license = licenseRepository.findOne(licenze.getId());
        final Map<EgDemandReason, EgDemandDetails> reasonWiseDemandDetails = getReasonWiseDemandDetails(
                license.getLicenseDemand());
        for (final FeeMatrixDetail feeMatrixDetail : feeMatrixService.getLicenseFeeDetails(license, installment.getFromDate())) {
            final String feeType = feeMatrixDetail.getFeeMatrix().getFeeType().getName();
            if (feeType.contains("Late"))
                continue;
            final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                    demandGenericDao.getDemandReasonMasterByCode(feeType, module), installment, module);
            if (reason == null)
                throw new ValidationException("TL-007", "Demand reason missing for " + feeType);
            final EgDemandDetails licenseDemandDetail = reasonWiseDemandDetails.get(reason);
            BigDecimal tradeAmt = calculateAmountByRateType(license, feeMatrixDetail);
            if (licenseDemandDetail == null)
                license.getLicenseDemand().getEgDemandDetails()
                        .add(EgDemandDetails.fromReasonAndAmounts(tradeAmt, reason, ZERO));
            else if (licenseDemandDetail.getBalance().compareTo(ZERO) != 0)
                licenseDemandDetail.setAmount(tradeAmt);
            if (license.getCurrentDemand().getEgInstallmentMaster().getInstallmentYear().before(installment.getInstallmentYear()))
                license.getLicenseDemand().setEgInstallmentMaster(installment);
        }
        license.getLicenseDemand().recalculateBaseDemand();
        licenseRepository.save(license);
    }

    public Map<EgDemandReason, EgDemandDetails> getReasonWiseDemandDetails(final EgDemand currentDemand) {
        final Map<EgDemandReason, EgDemandDetails> reasonWiseDemandDetails = new HashMap<>();
        if (currentDemand != null)
            for (final EgDemandDetails demandDetail : currentDemand.getEgDemandDetails())
                if (LICENSE_FEE_TYPE.equals(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    reasonWiseDemandDetails.put(demandDetail.getEgDemandReason(), demandDetail);
        return reasonWiseDemandDetails;
    }

    @Transactional
    public void transitionWorkFlow(final T license, final WorkflowBean workflowBean) {
        final DateTime currentDate = new DateTime();
        final User user = this.securityUtils.getCurrentUser();
        final List<Assignment> assignments = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(user.getId());

        final Assignment userAssignment = this.assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Position wfInitiator = null;
        final String natureOfWork = license.isReNewApplication()
                ? RENEWAL_NATUREOFWORK : NEW_NATUREOFWORK;
        if (null != license.getId())
            wfInitiator = this.getWorkflowInitiator(license);

        if (wfInitiator != null && BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            if (wfInitiator.equals(userAssignment.getPosition()) && ("Rejected".equals(license.getState().getValue())
                    || "License Created".equals(license.getState().getValue())))
                license.transition().end().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                final String stateValue = WORKFLOW_STATE_REJECTED;
                license.transition().progressWithStateCopy().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator).withNextAction(WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING);
            }

        } else if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
            license.transition().end().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                    .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator)
                    .withNextAction(wfmatrix.getNextAction());
        } else {
            if (null != workflowBean.getApproverPositionId() && workflowBean.getApproverPositionId() != -1)
                pos = positionMasterService.getPositionById(workflowBean.getApproverPositionId());
            if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                final Assignment commissionerUsr = this.assignmentService.getPrimaryAssignmentForUser(user.getId());
                pos = commissionerUsr.getPosition();
            }
            if (null == license.getState()) {
                if (!assignments.isEmpty())
                    wfInitiator = assignments.get(0).getPosition();
                else
                    throw new ValidationException("wf.initiator.not.found", "No employee exist for creator's position");
                final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
                license.transition().start().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(wfInitiator)
                        .withNextAction(wfmatrix.getNextAction()).withInitiator(wfInitiator);
                license.setEgwStatus(
                        egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
            } else if ("END".equalsIgnoreCase(license.getCurrentState().getNextAction()))
                license.transition().end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withNatureOfTask(natureOfWork)
                        .withDateInfo(currentDate.toDate());
            else if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                    && license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_SECONDCOLLECTION_CODE)) {
                final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
                license.transition().progressWithStateCopy().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                    && license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_APPROVED_CODE)
                    && !licenseUtils.isDigitalSignEnabled())
                license.transition().progressWithStateCopy().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(Constants.WF_COMMISSIONER_APPRVD_WITHOUT_COLLECTION).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator)
                        .withNextAction(Constants.WF_CERTIFICATE_GEN_PENDING);
            else if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                    && license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_APPROVED_CODE)
                    && licenseUtils.isDigitalSignEnabled())
                license.transition().progressWithStateCopy().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(Constants.WF_ACTION_DIGI_SIGN_COMMISSION_NO_COLLECTION).withDateInfo(currentDate.toDate())
                        .withOwner(pos)
                        .withNextAction(Constants.WF_ACTION_DIGI_PENDING);
            else {
                final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
                license.transition().progressWithStateCopy().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }

        }
    }

    protected Position getWorkflowInitiator(final T license) {
        List<Assignment> assignments = Collections.emptyList();
        if (license.getState().getInitiatorPosition() != null)
            assignments = assignmentService.getAssignmentsForPosition(license.getState().getInitiatorPosition().getId());
        if (assignments.isEmpty())
            throw new ValidationException("wf.initiator.not.found", "No employee exist for creator's position");
        else
            return license.getState().getInitiatorPosition();
    }

    @Transactional
    public void processAndStoreDocument(final List<LicenseDocument> documents, final License license) {
        documents.forEach(document -> {
            document.setType(licenseDocumentTypeRepository.findOne(document.getType().getId()));
            if (!(document.getUploads().isEmpty() || document.getUploadsContentType().isEmpty())) {
                int fileCount = 0;
                for (final File file : document.getUploads()) {
                    final FileStoreMapper fileStore = this.fileStoreService.store(file,
                            document.getUploadsFileName().get(fileCount),
                            document.getUploadsContentType().get(fileCount++), "EGTL");
                    document.getFiles().add(fileStore);
                }
                document.setEnclosed(true);
            } else if (document.getType().isMandatory() && document.getFiles().isEmpty()) {
                document.getFiles().clear();
                throw new ValidationException("TL-004", "TL-004", document.getType().getName());
            }
            document.setDocDate(new Date());
            document.setLicense(license);
        });
    }

    public List<LicenseDocumentType> getDocumentTypesByApplicationType(final ApplicationType applicationType) {
        return this.documentTypeService.getDocumentTypesByApplicationType(applicationType);
    }

    public List<NatureOfBusiness> getAllNatureOfBusinesses() {
        return natureOfBusinessService.getNatureOfBusinesses();
    }

    public T getLicenseByLicenseNumber(final String licenseNumber) {
        return (T) this.licenseRepository.findByLicenseNumber(licenseNumber);
    }

    public T getLicenseByApplicationNumber(final String applicationNumber) {
        return (T) this.licenseRepository.findByApplicationNumber(applicationNumber);
    }

    public List<Installment> getLastFiveYearInstallmentsForLicense() {
        final List<Installment> installmentList = this.installmentDao.fetchInstallments(this.getModuleName(), new Date(), 6);
        Collections.reverse(installmentList);
        return installmentList;
    }

    public Map<String, Map<String, BigDecimal>> getOutstandingFee(final T license) {
        final Map<String, Map<String, BigDecimal>> outstandingFee = new HashMap<>();
        final LicenseDemand licenseDemand = license.getCurrentDemand();
        for (final EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails()) {
            final String demandReason = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
            final Installment installmentYear = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            Map<String, BigDecimal> feeByTypes;
            if (outstandingFee.containsKey(demandReason))
                feeByTypes = outstandingFee.get(demandReason);
            else {
                feeByTypes = new HashMap<>();
                feeByTypes.put(ARREAR, ZERO);
                feeByTypes.put(CURRENT, ZERO);
            }
            final BigDecimal demandAmount = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
            if (installmentYear.equals(licenseDemand.getEgInstallmentMaster()))
                feeByTypes.put(CURRENT, demandAmount);
            else
                feeByTypes.put(ARREAR, feeByTypes.get(ARREAR).add(demandAmount));
            outstandingFee.put(demandReason, feeByTypes);
        }

        return outstandingFee;

    }

    /**
     * This method will return arrears, current tax and penalty on arrears tax.
     *
     * @param license
     * @param currentInstallment
     * @param previousInstallment
     * @return
     */
    public Map<String, Map<String, BigDecimal>> getOutstandingFeeForDemandNotice(final TradeLicense license,
                                                                                 final Installment currentInstallment, final Installment previousInstallment) {
        final Map<String, Map<String, BigDecimal>> outstandingFee = new HashMap<>();

        final LicenseDemand licenseDemand = license.getCurrentDemand();
        // 31st december will be considered as cutoff date for penalty calculation.
        final Date endDateOfPreviousFinancialYear = new DateTime(previousInstallment.getFromDate()).withMonthOfYear(12)
                .withDayOfMonth(31).toDate();

        for (final EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails()) {
            final String demandReason = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
            final Installment installmentYear = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            Map<String, BigDecimal> feeByTypes;
            if (!demandReason.equalsIgnoreCase(Constants.PENALTY_DMD_REASON_CODE)) {
                if (outstandingFee.containsKey(demandReason))
                    feeByTypes = outstandingFee.get(demandReason);
                else {
                    feeByTypes = new HashMap<>();
                    feeByTypes.put(ARREAR, ZERO);
                    feeByTypes.put(CURRENT, ZERO);
                    feeByTypes.put(PENALTY, ZERO);
                }
                final BigDecimal demandAmount = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());

                if (demandAmount.compareTo(BigDecimal.valueOf(0)) > 0)
                    if (installmentYear.equals(currentInstallment))
                        feeByTypes.put(CURRENT, feeByTypes.get(CURRENT).add(demandAmount));
                    else {
                        feeByTypes.put(ARREAR, feeByTypes.get(ARREAR).add(demandAmount));
                        // Calculate penalty by passing installment startdate and end of dec 31st date of previous installment
                        // dates using penalty master.
                        final BigDecimal penaltyAmt = penaltyRatesService.calculatePenalty(license, installmentYear.getFromDate(),
                                endDateOfPreviousFinancialYear, demandAmount);
                        feeByTypes.put(PENALTY, feeByTypes.get(PENALTY).add(penaltyAmt));
                    }
                outstandingFee.put(demandReason, feeByTypes);
            }
        }

        return outstandingFee;

    }

    public List<License> getAllLicensesByNatureOfBusiness(final String natureOfBusiness) {
        return licenseRepository.findByNatureOfBusinessName(natureOfBusiness);
    }

    @Transactional
    public void save(final License license) {
        licenseRepository.save(license);
    }

    public BigDecimal calculateFeeAmount(final License license) {
        final Date licenseDate = license.isNewApplication() ? license.getCommencementDate()
                : license.getLicenseDemand().getEgInstallmentMaster().getFromDate();
        final List<FeeMatrixDetail> feeList = this.feeMatrixService.getLicenseFeeDetails(license, licenseDate);
        BigDecimal totalAmount = ZERO;
        for (final FeeMatrixDetail fm : feeList) {
            BigDecimal tradeAmt = calculateAmountByRateType(license, fm);
            totalAmount = totalAmount.add(tradeAmt);
        }
        return totalAmount;
    }

    public BigDecimal recalculateLicenseFee(final LicenseDemand licenseDemand) {
        BigDecimal licenseFee = ZERO;
        for (final EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails())
            if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().equals(LICENSE_FEE_TYPE)
                    && licenseDemand.getEgInstallmentMaster().equals(demandDetail.getEgDemandReason().getEgInstallmentMaster()))
                licenseFee = licenseFee.add(demandDetail.getAmtCollected());
        return licenseFee;
    }

    @Transactional
    public void cancelLicenseWorkflow(final T license, final WorkflowBean workflowBean) {

        final User currentUser = this.securityUtils.getCurrentUser();
        final String currentUserRoles = securityUtils.getCurrentUser().getRoles().toString();
        final String natureOfWork = CLOSURE_NATUREOFTASK;
        Position owner = null;
        if (workflowBean.getApproverPositionId() != null)
            owner = positionMasterService.getPositionById(workflowBean.getApproverPositionId());
        final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
        if (workflowBean.getWorkFlowAction() != null && workflowBean.getWorkFlowAction().contains(BUTTONREJECT))
            if (WORKFLOW_STATE_REJECTED.equals(license.getState().getValue())) {
                licenseUtils.applicationStatusChange(license, Constants.APPLICATION_STATUS_GENECERT_CODE);
                license.setStatus(licenseStatusService.getLicenseStatusByName(Constants.LICENSE_STATUS_ACTIVE));
                license.setActive(true);
                if (license.getState().getExtraInfo() != null)
                    license.setLicenseAppType(licenseAppTypeService.getLicenseAppTypeByName(license.getState().getExtraInfo()));
                license.transition().end().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(new DateTime().toDate());
            } else {
                licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CREATED_CODE);
                license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
                final String stateValue = WORKFLOW_STATE_REJECTED;
                license.transition().progressWithStateCopy()
                        .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(stateValue).withDateInfo(new DateTime().toDate())
                        .withOwner(license.getState().getInitiatorPosition()).withNextAction("SI/SS Approval Pending");

            }
        else if (license.getState() == null || "END".equals(license.getState().getValue())
                || "Closed".equals(license.getState().getValue())) {
            final WorkFlowMatrix newwfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), "NEW", null);
            final Assignment wfInitiator = this.assignmentService
                    .getPrimaryAssignmentForUser(this.securityUtils.getCurrentUser().getId());
            if (!currentUserRoles.contains(CSCOPERATOR)) {
                if (!license.hasState())
                    license.transition().start();
                else
                    license.transition().startNext();
                license.transition()
                        .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(newwfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(owner)
                        .withNextAction(newwfmatrix.getNextAction()).withInitiator(wfInitiator.getPosition()).withExtraInfo(license.getLicenseAppType().getName());
            } else
                closureWfWithOperator(license);
            licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CREATED_CODE);
            license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
            license.setLicenseAppType(getClosureLicenseApplicationType());
            tradeLicenseSmsAndEmailService.sendSMsAndEmailOnClosure(license, workflowBean.getWorkFlowAction());

        } else if ("NEW".equals(license.getState().getValue())) {
            final WorkFlowMatrix newwfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), "NEW", null);
            license.transition().progressWithStateCopy()
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                    .withStateValue(newwfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(owner)
                    .withNextAction(newwfmatrix.getNextAction());
            licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CREATED_CODE);
            license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        } else if ("Revenue Clerk/JA Approved".equals(license.getState().getValue())
                || WORKFLOW_STATE_REJECTED.equals(license.getState().getValue())) {

            licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CREATED_CODE);
            license.setStatus(licenseStatusService.getLicenseStatusByName(Constants.LICENSE_STATUS_UNDERWORKFLOW));
            license.transition().progressWithStateCopy()
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(owner)
                    .withNextAction(wfmatrix.getNextAction());
        } else if ("SI/SS Approved".equals(license.getState().getValue())) {
            licenseUtils.applicationStatusChange(license, Constants.APPLICATION_STATUS_CANCELLED);
            license.setStatus(licenseStatusService.getLicenseStatusByName(Constants.LICENSE_STATUS_CANCELLED));
            license.setActive(false);
            final Assignment commissionerUsr = this.assignmentService.getPrimaryAssignmentForUser(currentUser.getId());
            owner = commissionerUsr.getPosition();
            final WorkFlowMatrix commWfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
            license.transition().end()
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                    .withStateValue(commWfmatrix.getNextState()).withDateInfo(new DateTime().toDate())
                    .withOwner(owner)
                    .withNextAction(commWfmatrix.getNextAction());
            tradeLicenseSmsAndEmailService.sendSMsAndEmailOnClosure(license, workflowBean.getWorkFlowAction());
        }

        this.licenseRepository.save(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
    }

    private void closureWfWithOperator(final T license) {
        final String natureOfWork = CLOSURE_NATUREOFTASK;
        List<Assignment> assignmentList = getAssignments();
        if (!assignmentList.isEmpty()) {
            final Assignment wfAssignment = assignmentList.get(0);
            if (!license.hasState())
                license.transition().start();
            else
                license.transition().startNext();
            license.transition().withSenderName(
                    wfAssignment.getEmployee().getUsername() + DELIMITER_COLON + wfAssignment.getEmployee().getName())
                    .withComments("CSC Operator Initiated").withNatureOfTask(natureOfWork)
                    .withStateValue("NEW").withDateInfo(new Date()).withOwner(wfAssignment.getPosition())
                    .withNextAction("SI/SS Approval Pending").withInitiator(wfAssignment.getPosition());
            license.setEgwStatus(
                    egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
        } else
            throw new ValidationException(LICENSE_WF_INITIATOR_NOT_DEFINED, LICENSE_WF_INITIATOR_NOT_DEFINED);
    }

    public boolean checkOldLicenseNumberIsDuplicated(final T t) {
        return licenseRepository.findByOldLicenseNumberAndIdIsNot(t.getOldLicenseNumber(), t.getId()) != null;
    }

    public List<License> getLicensesForDemandGeneration(final String natureOfBusiness, final CFinancialYear installmentYear) {
        Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), installmentYear.getStartingDate());
        return licenseRepository.findByNatureOfBusinessNameAndStatusName(natureOfBusiness, LICENSE_STATUS_ACTIVE, installment.getFromDate());
    }
}