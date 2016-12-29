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

import static java.math.BigDecimal.ZERO;
import static org.egov.tl.utils.Constants.APPLICATION_STATUS_CREATED_CODE;
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.BUTTONREJECT;
import static org.egov.tl.utils.Constants.DELIMITER_COLON;
import static org.egov.tl.utils.Constants.GENERATECERTIFICATE;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_ACKNOWLEDGED;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_ACTIVE;
import static org.egov.tl.utils.Constants.TRADELICENSEMODULE;
import static org.egov.tl.utils.Constants.WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING;
import static org.egov.tl.utils.Constants.WORKFLOW_STATE_REJECTED;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.egov.commons.Installment;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseDemand;
import org.egov.tl.entity.LicenseDocument;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.repository.LicenseDocumentTypeRepository;
import org.egov.tl.repository.LicenseRepository;
import org.egov.tl.service.es.LicenseApplicationIndexService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseNumberUtils;
import org.egov.tl.utils.LicenseUtils;
import org.hibernate.CacheMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class AbstractLicenseService<T extends License> {

    public static final String ARREAR = "arrear";
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
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
    protected FeeMatrixService feeMatrixService;

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
    private LicenseUtils licenseUtils;

    public static BigDecimal percentage(BigDecimal base, BigDecimal pct) {
        return base.multiply(pct).divide(ONE_HUNDRED);
    }

    protected abstract LicenseAppType getLicenseApplicationTypeForRenew();

    protected abstract LicenseAppType getLicenseApplicationType();

    protected abstract Module getModuleName();

    protected abstract NatureOfBusiness getNatureOfBusiness();

    protected abstract void sendEmailAndSMS(T license, String currentAction);

    public void setLicenseWorkflowService(final SimpleWorkflowService<T> licenseWorkflowService) {
        this.licenseWorkflowService = licenseWorkflowService;
    }

    public T getLicenseById(final Long id) {
        return (T) this.licenseRepository.findOne(id);
    }

    @Transactional
    public void create(final T license, final WorkflowBean workflowBean) {
        license.setLicenseAppType(getLicenseApplicationType());
        raiseNewDemand(license);
        license.getLicensee().setLicense(license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        processAndStoreDocument(license.getDocuments(), license);
        transitionWorkFlow(license, workflowBean);
        licenseRepository.save(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
    }

    private BigDecimal raiseNewDemand(final T license) {
        final LicenseDemand ld = new LicenseDemand();
        final Module moduleName = this.getModuleName();
        BigDecimal totalAmount = ZERO;
        final Installment installment = this.installmentDao.getInsatllmentByModuleForGivenDate(moduleName,
                license.getApplicationDate());
        ld.setIsHistory("N");
        ld.setEgInstallmentMaster(installment);
        ld.setLicense(license);
        ld.setIsLateRenewal('0');
        ld.setCreateDate(new Date());
        final List<FeeMatrixDetail> feeMatrixDetails = this.feeMatrixService.findFeeList(license);
        for (final FeeMatrixDetail fm : feeMatrixDetails) {
            final EgDemandReasonMaster reasonMaster = this.demandGenericDao
                    .getDemandReasonMasterByCode(fm.getFeeMatrix().getFeeType().getName(), moduleName);
            final EgDemandReason reason = this.demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster,
                    installment,
                    moduleName);
            if (fm.getFeeMatrix().getFeeType().getName().contains("Late"))
                continue;

            if (reason != null) {
                ld.getEgDemandDetails().add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, ZERO));
                totalAmount = totalAmount.add(fm.getAmount());
            }
        }

        ld.setBaseDemand(totalAmount);
        license.setLicenseDemand(ld);
        return totalAmount;
    }

    public License updateDemandForChangeTradeArea(final T license) {
        final LicenseDemand licenseDemand = license.getLicenseDemand();
        final Module moduleName = this.getModuleName();
        final Installment installment = this.installmentDao.getInsatllmentByModuleForGivenDate(moduleName,
                license.getApplicationDate());
        final Set<EgDemandDetails> demandDetails = licenseDemand.getEgDemandDetails();
        final List<FeeMatrixDetail> feeList = this.feeMatrixService.findFeeList(license);
        for (final EgDemandDetails dmd : demandDetails)
            for (final FeeMatrixDetail fm : feeList)
                if (installment.getId().equals(dmd.getEgDemandReason().getEgInstallmentMaster().getId()) &&
                        dmd.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(fm.getFeeMatrix().getFeeType().getName())) {
                    dmd.setAmount(fm.getAmount());
                    dmd.setModifiedDate(new Date());
                }
        this.recalculateBaseDemand(licenseDemand);
        return license;

    }

    @Transactional
    public BigDecimal recalculateDemand(final List<FeeMatrixDetail> feeList, final T license) {
        final Installment installment = this.installmentDao.getInsatllmentByModuleForGivenDate(this.getModuleName(), new Date());
        BigDecimal totalAmount = ZERO;
        final LicenseDemand licenseDemand = license.getCurrentDemand();
        // Recalculating current demand detail according to fee matrix
        for (final EgDemandDetails dmd : licenseDemand.getEgDemandDetails())
            for (final FeeMatrixDetail fm : feeList)
                if (installment.getId().equals(dmd.getEgDemandReason().getEgInstallmentMaster().getId()) &&
                        dmd.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(fm.getFeeMatrix().getFeeType().getName())) {
                    dmd.setAmount(fm.getAmount().setScale(0, RoundingMode.HALF_UP));
                    dmd.setAmtCollected(ZERO);
                    totalAmount = totalAmount.add(fm.getAmount().setScale(0, RoundingMode.HALF_UP));
                }
        this.recalculateBaseDemand(licenseDemand);
        return totalAmount;
    }

    @Transactional
    public void createLegacyLicense(final T license, final Map<Integer, Integer> legacyInstallmentwiseFees,
                                    final Map<Integer, Boolean> legacyFeePayStatus) {
        if (licenseRepository.findByOldLicenseNumber(license.getOldLicenseNumber()) != null)
            throw new ValidationException("TL-001", "TL-001", license.getOldLicenseNumber());
        this.addLegacyDemand(legacyInstallmentwiseFees, legacyFeePayStatus, license);
        processAndStoreDocument(license.getDocuments(), license);
        license.setLicenseAppType(getLicenseApplicationType());
        license.getLicensee().setLicense(license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACTIVE));
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        license.setLegacy(true);
        license.setActive(true);
        license.setLicenseNumber(licenseNumberUtils.generateLicenseNumber());
        this.validityService.applyLicenseValidity(license);
        licenseRepository.save(license);
    }

    private void addLegacyDemand(final Map<Integer, Integer> legacyInstallmentwiseFees,
                                 final Map<Integer, Boolean> legacyFeePayStatus,
                                 final T license) {
        final LicenseDemand licenseDemand = new LicenseDemand();
        licenseDemand.setIsHistory("N");
        licenseDemand.setCreateDate(new Date());
        licenseDemand.setLicense(license);
        licenseDemand.setIsLateRenewal('0');
        final Module module = this.getModuleName();
        for (final Entry<Integer, Integer> legacyInstallmentwiseFee : legacyInstallmentwiseFees.entrySet())
            if (legacyInstallmentwiseFee.getValue() != null && legacyInstallmentwiseFee.getValue() > 0) {
                final Installment installment = this.installmentDao.fetchInstallmentByModuleAndInstallmentNumber(module,
                        legacyInstallmentwiseFee.getKey());

                licenseDemand.setEgInstallmentMaster(installment);
                final BigDecimal demandAmount = BigDecimal.valueOf(legacyInstallmentwiseFee.getValue()).setScale(0,
                        RoundingMode.HALF_UP);
                final BigDecimal amtCollected = legacyFeePayStatus.get(legacyInstallmentwiseFee.getKey()) == null
                        || !legacyFeePayStatus.get(legacyInstallmentwiseFee.getKey()) ? ZERO : demandAmount;
                licenseDemand.getEgDemandDetails().add(
                        EgDemandDetails.fromReasonAndAmounts(demandAmount,
                                this.demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                                        this.demandGenericDao.getDemandReasonMasterByCode("License Fee", module),
                                        installment, module),
                                amtCollected));
                licenseDemand.setBaseDemand(demandAmount.add(licenseDemand.getBaseDemand()).setScale(0, RoundingMode.HALF_UP));
                licenseDemand
                        .setAmtCollected(amtCollected.add(licenseDemand.getAmtCollected()).setScale(0, RoundingMode.HALF_UP));
            }
        license.setLicenseDemand(licenseDemand);

    }

    @Transactional
    public void updateLegacyLicense(final T license, final Map<Integer, Integer> updatedInstallmentFees,
                                    final Map<Integer, Boolean> legacyFeePayStatus) {
        processAndStoreDocument(license.getDocuments(), license);
        this.updateLegacyDemand(license, updatedInstallmentFees, legacyFeePayStatus);
        licenseRepository.save(license);
    }

    private void updateLegacyDemand(final T license, final Map<Integer, Integer> updatedInstallmentFees,
                                    final Map<Integer, Boolean> legacyFeePayStatus) {
        final LicenseDemand licenseDemand = license.getCurrentDemand();

        // Update existing demand details
        final Iterator<EgDemandDetails> demandDetails = licenseDemand.getEgDemandDetails().iterator();
        while (demandDetails.hasNext()) {
            final EgDemandDetails demandDetail = demandDetails.next();
            final Integer installmentNumber = demandDetail.getEgDemandReason().getEgInstallmentMaster()
                    .getInstallmentNumber();
            final Integer updatedFee = updatedInstallmentFees.get(installmentNumber);
            final Boolean feePaymentStatus = legacyFeePayStatus.get(installmentNumber);
            if (updatedFee != null) {
                final BigDecimal updatedDemandAmt = BigDecimal.valueOf(updatedFee).setScale(0, RoundingMode.HALF_UP);
                demandDetail.setAmount(updatedDemandAmt);
                if (feePaymentStatus != null && feePaymentStatus)
                    demandDetail.setAmtCollected(updatedDemandAmt);
                else
                    demandDetail.setAmtCollected(ZERO);

            } else
                demandDetails.remove();
            updatedInstallmentFees.put(installmentNumber, 0);
        }

        // Create demand details which is newly entered
        final Module module = this.getModuleName();
        for (final Entry<Integer, Integer> updatedInstallmentFee : updatedInstallmentFees.entrySet())
            if (updatedInstallmentFee.getValue() != null && updatedInstallmentFee.getValue() > 0) {
                final Installment installment = this.installmentDao.fetchInstallmentByModuleAndInstallmentNumber(module,
                        updatedInstallmentFee.getKey());
                final BigDecimal demandAmount = BigDecimal.valueOf(updatedInstallmentFee.getValue()).setScale(0,
                        RoundingMode.HALF_UP);
                final BigDecimal amtCollected = legacyFeePayStatus.get(updatedInstallmentFee.getKey()) == null
                        || !legacyFeePayStatus.get(updatedInstallmentFee.getKey()) ? ZERO : demandAmount;
                licenseDemand.getEgDemandDetails().add(
                        EgDemandDetails.fromReasonAndAmounts(demandAmount,
                                this.demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                                        this.demandGenericDao.getDemandReasonMasterByCode("License Fee", module),
                                        installment, module),
                                amtCollected));
            }
        // Recalculating BasedDemand
        this.recalculateBaseDemand(licenseDemand);

    }

    public void recalculateBaseDemand(final LicenseDemand licenseDemand) {
        licenseDemand.setAmtCollected(ZERO);
        licenseDemand.setBaseDemand(ZERO);
        for (final EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails()) {
            licenseDemand.setAmtCollected(licenseDemand.getAmtCollected().add(demandDetail.getAmtCollected()));
            licenseDemand.setBaseDemand(licenseDemand.getBaseDemand().add(demandDetail.getAmount()));
        }
    }

    @Transactional
    public void renew(final T license, final WorkflowBean workflowBean) {
        license.setLicenseAppType(getLicenseApplicationTypeForRenew());
        final String natureOfWork = license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE)
                ? Constants.RENEWAL_NATUREOFWORK : Constants.NEW_NATUREOFWORK;
        final Assignment wfInitiator = this.assignmentService
                .getPrimaryAssignmentForUser(this.securityUtils.getCurrentUser().getId());
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        recalculateDemand(this.feeMatrixService.findFeeList(license), license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        license.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
        license.setLicenseAppType(this.getLicenseApplicationTypeForRenew());
        final User currentUser = this.securityUtils.getCurrentUser();
        final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
        license.reinitiateTransition().start()
                .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                .withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(wfInitiator.getPosition())
                .withNextAction(wfmatrix.getNextAction()).withInitiator(wfInitiator.getPosition());
        this.licenseRepository.save(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
    }

    @Transactional
    public void transitionWorkFlow(final T license, final WorkflowBean workflowBean) {
        final DateTime currentDate = new DateTime();
        final User user = this.securityUtils.getCurrentUser();
        List<Assignment> assignments = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(user.getId());

        final Assignment userAssignment = this.assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Position wfInitiator = null;
        final String natureOfWork = license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE)
                ? Constants.RENEWAL_NATUREOFWORK : Constants.NEW_NATUREOFWORK;
        if (null != license.getId())
            wfInitiator = this.getWorkflowInitiator(license);

        if (wfInitiator != null && BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            if (wfInitiator.equals(userAssignment.getPosition())) {
                license.transition(true).end().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
                if (license.getLicenseAppType() != null
                        && license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE))
                    license.setLicenseAppType(this.getLicenseApplicationType());

            } else {
                final String stateValue = WORKFLOW_STATE_REJECTED;
                license.transition(true).withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator).withNextAction(WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING);
            }

        } else if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
            license.transition(false).end().withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
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
                license.transition(true).end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withNatureOfTask(natureOfWork)
                        .withDateInfo(currentDate.toDate());
            else if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                    && license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_SECONDCOLLECTION_CODE)) {
                final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
                license.transition(true).withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                    && license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_APPROVED_CODE)
                    && !licenseUtils.isDigitalSignEnabled())
                license.transition(true).withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(Constants.WF_COMMISSIONER_APPRVD_WITHOUT_COLLECTION).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator)
                        .withNextAction(Constants.WF_CERTIFICATE_GEN_PENDING);
            else if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                    && license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_APPROVED_CODE)
                    && licenseUtils.isDigitalSignEnabled())
                license.transition(true).withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(Constants.WF_ACTION_DIGI_SIGN_COMMISSION_NO_COLLECTION).withDateInfo(currentDate.toDate())
                        .withOwner(pos)
                        .withNextAction(Constants.WF_ACTION_DIGI_PENDING);
            else {
                final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
                license.transition(true).withSenderName(user.getUsername() + DELIMITER_COLON + user.getName())
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
        else return license.getState().getInitiatorPosition();
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
        return natureOfBusinessService.findAll();
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
        final Installment currentInstallmentYear = this.installmentDao.getInsatllmentByModuleForGivenDate(this.getModuleName(),
                new Date());
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
                feeByTypes.put("current", ZERO);
            }
            final BigDecimal demandAmount = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
            if (installmentYear.equals(currentInstallmentYear))
                feeByTypes.put("current", demandAmount);
            else
                feeByTypes.put(ARREAR, feeByTypes.get(ARREAR).add(demandAmount));
            outstandingFee.put(demandReason, feeByTypes);
        }

        return outstandingFee;

    }

    public List<T> getAllLicensesByNatureOfBusiness(final String natureOfBusiness) {
        return this.entityQueryService.getSession().createCriteria(License.class)
                .createAlias("natureOfBusiness", "nb", JoinType.LEFT_OUTER_JOIN).add(Restrictions.eq("nb.name", natureOfBusiness))
                .setCacheMode(CacheMode.IGNORE).list();
    }

    @Transactional
    public void save(final License license) {
        licenseRepository.save(license);
    }

    public BigDecimal calculateFeeAmount(final License license) {
        final List<FeeMatrixDetail> feeList = this.feeMatrixService.findFeeList(license);
        BigDecimal totalAmount = ZERO;
        for (final FeeMatrixDetail fm : feeList)
            totalAmount = totalAmount.add(fm.getAmount());
        return totalAmount;
    }

    public BigDecimal recalculateLicenseFee(final LicenseDemand licenseDemand) {
        BigDecimal licenseFee = ZERO;
        for (final EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails())
            if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().equals(Constants.LICENSE_FEE_TYPE))
                licenseFee = licenseFee.add(demandDetail.getAmount());
        return licenseFee;
    }

    @Transactional
    public void cancelLicenseWorkflow(final T license, final WorkflowBean workflowBean) {


        final User currentUser = this.securityUtils.getCurrentUser();
        final String natureOfWork = "Closure License";
        Position owner = null;
        if (workflowBean.getApproverPositionId() != null)
            owner = positionMasterService.getPositionById(workflowBean.getApproverPositionId());
        final WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
        if (workflowBean.getWorkFlowAction().contains(BUTTONREJECT))
            if (WORKFLOW_STATE_REJECTED.equals(license.getState().getValue())) {
                licenseUtils.applicationStatusChange(license, Constants.APPLICATION_STATUS_GENECERT_CODE);
                license.setStatus(licenseStatusService.getLicenseStatusByName(Constants.LICENSE_STATUS_ACTIVE));
                license.setActive(true);
                license.transition(true).end().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(new DateTime().toDate());
            } else {
                licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CREATED_CODE);
                license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
                final String stateValue = WORKFLOW_STATE_REJECTED;
                license.transition(true).withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                        .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                        .withStateValue(stateValue).withDateInfo(new DateTime().toDate())
                        .withOwner(license.getState().getInitiatorPosition()).withNextAction("SI/SS Approval Pending");
            }
        else if (license.getState() == null || "END".equals(license.getState().getValue()) || "Closed".equals(license.getState().getValue())) {
            final WorkFlowMatrix newwfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), "NEW", null);
            licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CREATED_CODE);
            license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
            final Assignment wfInitiator = this.assignmentService
                    .getPrimaryAssignmentForUser(this.securityUtils.getCurrentUser().getId());
            license.reinitiateTransition().start()
                    .withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                    .withStateValue(newwfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(owner)
                    .withNextAction(newwfmatrix.getNextAction()).withInitiator(wfInitiator.getPosition());
        } else if ("Revenue Clerk/JA Approved".equals(license.getState().getValue()) || WORKFLOW_STATE_REJECTED.equals(license.getState().getValue())) {
            licenseUtils.applicationStatusChange(license, APPLICATION_STATUS_CREATED_CODE);
            license.setStatus(licenseStatusService.getLicenseStatusByName(Constants.LICENSE_STATUS_UNDERWORKFLOW));
            license.transition(true).withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
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
            license.transition(false).end().withSenderName(currentUser.getUsername() + DELIMITER_COLON + currentUser.getName())
                    .withComments(workflowBean.getApproverComments()).withNatureOfTask(natureOfWork)
                    .withStateValue(commWfmatrix.getNextState()).withDateInfo(new DateTime().toDate())
                    .withOwner(owner)
                    .withNextAction(commWfmatrix.getNextAction());
        }


        this.licenseRepository.save(license);
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
    }

    public boolean checkOldLicenseNumberIsDuplicated(T t) {
        return licenseRepository.findByOldLicenseNumberAndIdIsNot(t.getOldLicenseNumber(), t.getId()) != null;
    }
}