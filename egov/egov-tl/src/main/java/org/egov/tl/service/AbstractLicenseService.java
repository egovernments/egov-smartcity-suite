/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.LicenseSubCategoryDetails;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.entity.enums.RateTypeEnum;
import org.egov.tl.repository.LicenseDocumentTypeRepository;
import org.egov.tl.repository.LicenseRepository;
import org.egov.tl.service.es.LicenseApplicationIndexService;
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
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.egov.tl.utils.Constants.*;

@Transactional(readOnly = true)
public abstract class AbstractLicenseService<T extends License> {

    private static final String ARREAR = "arrear";
    private static final String CURRENT = "current";
    private static final String PENALTY = "penalty";
    private static final String ERROR_KEY_WF_INITIATOR_NOT_DEFINED = "error.wf.initiator.not.defined";
    private static final String ERROR_KEY_WF_NEXT_OWNER_NOT_FOUND = "error.wf.next.owner.not.found";

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
    public License create(final T license, final WorkflowBean workflowBean) {
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
        if (isBlank(license.getApplicationNumber()))
            license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        processAndStoreDocument(license);
        if (securityUtils.currentUserIsEmployee())
            transitionWorkFlow(license, workflowBean);
        else
            wfWithCscOperator(license, workflowBean);
        licenseRepository.save(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        return license;
    }

    private void wfWithCscOperator(final T license, final WorkflowBean workflowBean) {
        List<Assignment> assignmentList = getAssignments();
        if (!assignmentList.isEmpty()) {
            final Assignment wfAssignment = assignmentList.get(0);
            final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), PUBLIC_HEALTH_DEPT,
                    null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
            if (!license.hasState())
                license.transition().start();
            else
                license.transition().startNext();
            license.transition().withSenderName(
                    wfAssignment.getEmployee().getUsername() + DELIMITER_COLON + wfAssignment.getEmployee().getName())
                    .withComments(workflowBean.getApproverComments())
                    .withNatureOfTask(license.isReNewApplication() ? RENEWAL_NATUREOFWORK : NEW_NATUREOFWORK)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(wfAssignment.getPosition())
                    .withNextAction(wfmatrix.getNextAction()).withInitiator(wfAssignment.getPosition());
            license.setEgwStatus(
                    egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
        } else
            throw new ValidationException(ERROR_KEY_WF_INITIATOR_NOT_DEFINED, ERROR_KEY_WF_INITIATOR_NOT_DEFINED);
    }

    private List<Assignment> getAssignments() {
        Department nextAssigneeDept = departmentService.getDepartmentByCode(PUBLIC_HEALTH_DEPT_CODE);
        Designation nextAssigneeDesig = designationService.getDesignationByName(JA_DESIGNATION);
        List<Assignment> assignmentList = getAssignmentsForDeptAndDesignation(nextAssigneeDept, nextAssigneeDesig);
        if (assignmentList.isEmpty()) {
            nextAssigneeDesig = Optional.ofNullable(designationService.getDesignationByName(SA_DESIGNATION)).
                    orElseThrow(() -> new ValidationException(ERROR_KEY_WF_INITIATOR_NOT_DEFINED, ERROR_KEY_WF_INITIATOR_NOT_DEFINED));
            assignmentList = getAssignmentsForDeptAndDesignation(nextAssigneeDept, nextAssigneeDesig);
        }
        if (assignmentList.isEmpty()) {
            nextAssigneeDesig = Optional.ofNullable(designationService.getDesignationByName(RC_DESIGNATION)).
                    orElseThrow(() -> new ValidationException(ERROR_KEY_WF_INITIATOR_NOT_DEFINED, ERROR_KEY_WF_INITIATOR_NOT_DEFINED));
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
        BigDecimal amt = ZERO;
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
        Date date = new Date();
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
                    dmd.setModifiedDate(date);
                }
        licenseDemand.recalculateBaseDemand();
        return license;

    }


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
    public License renew(final T license, final WorkflowBean workflowBean) {
        final List<Assignment> assignments = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(this.securityUtils.getCurrentUser().getId());
        if (!currentUserIsMeeseva())
            license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        recalculateDemand(this.feeMatrixService.getLicenseFeeDetails(license,
                license.getLicenseDemand().getEgInstallmentMaster().getFromDate()), license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        license.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
        license.setLicenseAppType(this.getLicenseApplicationTypeForRenew());
        processAndStoreDocument(license);
        final User currentUser = this.securityUtils.getCurrentUser();
        if (securityUtils.currentUserIsEmployee()) {
            Position wfInitiator = null;
            if (license.getState() == null || license.transitionCompleted()) {
                if (!assignments.isEmpty())
                    wfInitiator = assignments.get(0).getPosition();
                else
                    throw new ValidationException(ERROR_KEY_WF_NEXT_OWNER_NOT_FOUND, "No employee assigned to process Renewal application", "Renewal");
            }
            final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
            if (!license.hasState())
                license.transition().start();
            else if (license.transitionCompleted())
                license.transition().startNext();
            else
                throw new ValidationException("error.appl.under.workflow", "Cannot initiate Renewal process, application under processing");
            license.transition().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments()).withNatureOfTask(RENEWAL_NATUREOFWORK)
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate())
                    .withOwner(wfInitiator)
                    .withNextAction(wfmatrix.getNextAction()).withInitiator(wfInitiator);
        } else
            wfWithCscOperator(license, workflowBean);
        this.licenseRepository.save(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        return license;
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


    public void transitionWorkFlow(final T license, final WorkflowBean workflowBean) {
        DateTime currentDate = new DateTime();
        User user = this.securityUtils.getCurrentUser();
        if (BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            Position initiatorPosition = license.getCurrentState().getInitiatorPosition();
            List<Position> userPositions = positionMasterService.getPositionsForEmployee(securityUtils.getCurrentUser().getId());
            if (userPositions.contains(initiatorPosition) && ("Rejected".equals(license.getState().getValue())
                    || "License Created".equals(license.getState().getValue())))
                license.transition().end().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                final String stateValue = WORKFLOW_STATE_REJECTED;
                license.transition().progressWithStateCopy().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(initiatorPosition)
                        .withNextAction(WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING);
            }

        } else if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
            license.transition().end().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                    .withOwner(license.getCurrentState().getInitiatorPosition())
                    .withNextAction(wfmatrix.getNextAction());
        } else {
            if (!license.hasState()) {
                Position wfInitiator;
                List<Assignment> assignments = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(user.getId());
                if (!assignments.isEmpty())
                    wfInitiator = assignments.get(0).getPosition();
                else
                    throw new ValidationException(ERROR_KEY_WF_INITIATOR_NOT_DEFINED, "No officials assigned to process this application");
                final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
                license.transition().start().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withNatureOfTask(license.isReNewApplication() ? RENEWAL_NATUREOFWORK : NEW_NATUREOFWORK)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(wfInitiator)
                        .withNextAction(wfmatrix.getNextAction()).withInitiator(wfInitiator);
                license.setEgwStatus(
                        egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
            } else if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                Position commissioner = getCommissionerPosition();
                if (APPLICATION_STATUS_APPROVED_CODE.equals(license.getEgwStatus().getCode())) {
                    if (licenseUtils.isDigitalSignEnabled())
                        license.transition().progressWithStateCopy()
                                .withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                                .withComments(workflowBean.getApproverComments())
                                .withStateValue(WF_ACTION_DIGI_SIGN_COMMISSION_NO_COLLECTION)
                                .withDateInfo(currentDate.toDate())
                                .withOwner(commissioner)
                                .withNextAction(WF_ACTION_DIGI_PENDING);
                    else
                        license.transition().progressWithStateCopy()
                                .withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                                .withComments(workflowBean.getApproverComments())
                                .withStateValue(WF_COMMISSIONER_APPRVD_WITHOUT_COLLECTION)
                                .withDateInfo(currentDate.toDate())
                                .withOwner(license.getCurrentState().getInitiatorPosition())
                                .withNextAction(WF_CERTIFICATE_GEN_PENDING);
                } else if (APPLICATION_STATUS_SECONDCOLLECTION_CODE.equals(license.getEgwStatus().getCode())) {
                    final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                            null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
                    license.transition().progressWithStateCopy()
                            .withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                            .withComments(workflowBean.getApproverComments())
                            .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                            .withOwner(commissioner)
                            .withNextAction(wfmatrix.getNextAction());
                }

            } else {
                Position pos = null;
                if (workflowBean.getApproverPositionId() != null && workflowBean.getApproverPositionId() > 0)
                    pos = positionMasterService.getPositionById(workflowBean.getApproverPositionId());
                final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
                license.transition().progressWithStateCopy()
                        .withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                        .withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }

        }
    }

    private Position getCommissionerPosition() {
        return positionMasterService.getPositionsForEmployee(securityUtils.getCurrentUser().getId())
                .stream()
                .filter(position -> position.getDeptDesig().getDesignation().getName().equals(COMMISSIONER_DESGN))
                .findFirst()
                .orElseThrow(
                        () -> new ValidationException("error.wf.comm.pos.not.found", "You are not authorized approve this application"));
    }

    public WorkFlowMatrix getWorkFlowMatrixApi(License license, WorkflowBean workflowBean) {
        return this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
    }

    public void processAndStoreDocument(License license) {
        license.getDocuments().forEach(document -> {
            document.setType(licenseDocumentTypeRepository.findOne(document.getType().getId()));
            if (!(document.getUploads().isEmpty() || document.getUploadsFileName().isEmpty())) {
                int fileCount = 0;
                for (final File file : document.getUploads()) {
                    final FileStoreMapper fileStore = this.fileStoreService.store(file,
                            document.getUploadsFileName().get(fileCount),
                            document.getUploadsContentType().get(fileCount++), "EGTL");
                    document.getFiles().add(fileStore);
                }
                document.setEnclosed(true);
                document.setDocDate(new Date());
            } else if (document.getType().isMandatory() && document.getFiles().isEmpty()) {
                document.getFiles().clear();
                throw new ValidationException("TL-004", "TL-004", document.getType().getName());
            }
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
            if (!demandReason.equalsIgnoreCase(PENALTY_DMD_REASON_CODE)) {
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
    public License saveClosure(final T license, final WorkflowBean workflowBean) {
        final User currentUser = this.securityUtils.getCurrentUser();
        if (license.hasState() && !license.getState().isEnded())
            throw new ValidationException("lic.appl.wf.validation", "Cannot initiate Closure process, application under processing");
        Position position = null;
        if (workflowBean.getApproverPositionId() != null) {
            position = positionMasterService.getPositionById(workflowBean.getApproverPositionId());
        }
        if (license.getState() == null || "END".equals(license.getState().getValue())
                || "Closed".equals(license.getState().getValue())) {
            final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), "NEW", null);
            final List<Assignment> assignments = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(this.securityUtils.getCurrentUser().getId());
            if (securityUtils.currentUserIsEmployee()) {
                Position wfInitiator = null;
                if (license.getState() == null || license.transitionCompleted()) {
                    if (!assignments.isEmpty())
                        wfInitiator = assignments.get(0).getPosition();
                    else
                        throw new ValidationException(ERROR_KEY_WF_NEXT_OWNER_NOT_FOUND, "No employee assigned to process Closure application", "Closure");
                }
                if (!license.hasState())
                    license.transition().start();
                else
                    license.transition().startNext();
                license.transition()
                        .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(CLOSURE_NATUREOFTASK)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(position)
                        .withNextAction(wfmatrix.getNextAction()).withInitiator(wfInitiator).withExtraInfo(license.getLicenseAppType().getName());
            } else
                closureWfWithOperator(license);
            if (!currentUserIsMeeseva())
                license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
            licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CREATED_CODE);
            license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
            license.setLicenseAppType(getClosureLicenseApplicationType());
            tradeLicenseSmsAndEmailService.sendSMsAndEmailOnClosure(license, workflowBean.getWorkFlowAction());

        }
        this.licenseRepository.save(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        return license;
    }

    @Transactional
    public void cancelLicenseWorkflow(final T license, final WorkflowBean workflowBean) {
        final User currentUser = this.securityUtils.getCurrentUser();
        Position owner = null;
        if (workflowBean.getApproverPositionId() != null)
            owner = positionMasterService.getPositionById(workflowBean.getApproverPositionId());
        final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
        if (workflowBean.getWorkFlowAction() != null && workflowBean.getWorkFlowAction().contains(BUTTONREJECT))
            if (WORKFLOW_STATE_REJECTED.equals(license.getState().getValue())) {
                licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_GENECERT_CODE);
                license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACTIVE));
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
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(stateValue).withDateInfo(new DateTime().toDate())
                        .withOwner(license.getState().getInitiatorPosition()).withNextAction("SI/SS Approval Pending");

            }
        else if ("NEW".equals(license.getState().getValue())) {
            final WorkFlowMatrix newwfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), "NEW", null);
            license.transition().progressWithStateCopy()
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withStateValue(newwfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(owner)
                    .withNextAction(newwfmatrix.getNextAction());
            licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CREATED_CODE);
            license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        } else if ("Revenue Clerk/JA Approved".equals(license.getState().getValue())
                || WORKFLOW_STATE_REJECTED.equals(license.getState().getValue())) {

            licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CREATED_CODE);
            license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_UNDERWORKFLOW));
            license.transition().progressWithStateCopy()
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(owner)
                    .withNextAction(wfmatrix.getNextAction());
        } else if ("SI/SS Approved".equals(license.getState().getValue())) {
            licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CANCELLED);
            license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_CANCELLED));
            license.setActive(false);
            owner = getCommissionerPosition();
            final WorkFlowMatrix commWfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
            license.transition().end()
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withStateValue(commWfmatrix.getNextState()).withDateInfo(new DateTime().toDate())
                    .withOwner(owner)
                    .withNextAction(commWfmatrix.getNextAction());
            tradeLicenseSmsAndEmailService.sendSMsAndEmailOnClosure(license, workflowBean.getWorkFlowAction());
        }

        this.licenseRepository.save(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
    }

    private void closureWfWithOperator(final T license) {
        final String currentUserRoles = securityUtils.getCurrentUser().getRoles().toString();
        String comment = "";
        if (currentUserRoles.contains(CSCOPERATOR))
            comment = "CSC Operator Initiated";
        else if (currentUserRoles.contains("PUBLIC"))
            comment = "Citizen applied for closure";
        else if (currentUserRoles.contains(MEESEVAOPERATOR))
            comment = "Meeseva Operator Initiated";
        List<Assignment> assignmentList = getAssignments();
        if (!assignmentList.isEmpty()) {
            final Assignment wfAssignment = assignmentList.get(0);
            if (!license.hasState())
                license.transition().start();
            else
                license.transition().startNext();
            license.transition().withSenderName(
                    wfAssignment.getEmployee().getUsername() + DELIMITER_COLON + wfAssignment.getEmployee().getName())
                    .withComments(comment).withNatureOfTask(CLOSURE_NATUREOFTASK)
                    .withStateValue("NEW").withDateInfo(new Date()).withOwner(wfAssignment.getPosition())
                    .withNextAction("SI/SS Approval Pending").withInitiator(wfAssignment.getPosition());
            license.setEgwStatus(
                    egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
        } else
            throw new ValidationException(ERROR_KEY_WF_INITIATOR_NOT_DEFINED, ERROR_KEY_WF_INITIATOR_NOT_DEFINED);
    }

    public List<License> getLicensesForDemandGeneration(final String natureOfBusiness, final CFinancialYear installmentYear) {
        Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), installmentYear.getStartingDate());
        return licenseRepository.findByNatureOfBusinessNameAndStatusName(natureOfBusiness, LICENSE_STATUS_ACTIVE, installment.getFromDate());
    }

    @Transactional
    public License createWithMeseva(T license, WorkflowBean wfBean) {
        return create(license, wfBean);
    }

    @Transactional
    public License renewWithMeeseva(T license, WorkflowBean wfBean) {
        return renew(license, wfBean);
    }

    @Transactional
    public License closureWithMeeseva(T license, WorkflowBean wfBean) {
        return saveClosure(license, wfBean);
    }

    public Boolean currentUserIsMeeseva() {
        return securityUtils.getCurrentUser().hasRole(MEESEVAOPERATOR);
    }

    @Transactional
    public void digitalSignTransition(String fileStoreId, Map<String, String> appNoFileStoreIdsMap) {
        final User user = securityUtils.getCurrentUser();
        final String applicationNumber = appNoFileStoreIdsMap.get(fileStoreId);
        if (null != applicationNumber && !applicationNumber.isEmpty()) {
            License license = licenseRepository.findByApplicationNumber(applicationNumber);
            final DateTime currentDate = new DateTime();
            license = licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_APPROVED_CODE);
            WorkFlowMatrix wfmatrix;
            if (license.isReNewApplication())
                wfmatrix = this.licenseWorkflowService.getWfMatrix(TRADELICENSE, null, null,
                        RENEW_ADDITIONAL_RULE,
                        WF_DIGI_SIGNED, null);
            else
                wfmatrix = this.licenseWorkflowService.getWfMatrix(TRADELICENSE, null, null,
                        NEW_ADDITIONAL_RULE,
                        WF_DIGI_SIGNED, null);

            license.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(WF_DIGI_SIGNED).withStateValue(wfmatrix.getCurrentState())
                    .withDateInfo(currentDate.toDate()).withOwner(license.getCurrentState().getInitiatorPosition())
                    .withNextAction(wfmatrix.getCurrentStatus());
            licenseRepository.save(license);
            tradeLicenseSmsAndEmailService.sendSMsAndEmailOnDigitalSign(license);
        }

    }
}