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
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.BUTTONREJECT;
import static org.egov.tl.utils.Constants.GENERATECERTIFICATE;
import static org.egov.tl.utils.Constants.WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING;
import static org.egov.tl.utils.Constants.WORKFLOW_STATE_REJECTED;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.persistence.utils.SequenceNumberGenerator;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseDemand;
import org.egov.tl.entity.LicenseDocument;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.LicenseStatus;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.entity.transfer.LicenseTransfer;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseChecklistHelper;
import org.hibernate.CacheMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mani
 */
@Transactional(readOnly = true)
public abstract class AbstractLicenseService<T extends License> {

    protected static final Logger LOGGER = Logger.getLogger(AbstractLicenseService.class);

    @Autowired
    @Qualifier("entityQueryService")
    protected PersistenceService persistenceService;

    @Autowired
    protected InstallmentHibDao installmentDao;

    @Autowired
    protected SequenceNumberGenerator sequenceNumberGenerator;

    @Autowired
    protected ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    protected FileStoreService fileStoreService;

    @Autowired
    protected FeeMatrixService feeMatrixService;

    @Autowired
    @Qualifier("licenseDocumentTypeService")
    protected PersistenceService<LicenseDocumentType, Long> licenseDocumentTypeService;

    @Autowired
    private TradeLicenseUpdateIndexService updateIndexService;

    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected DemandGenericHibDao demandGenericDao;

    @Autowired
    protected ValidityService validityService;

    protected SimpleWorkflowService<T> licenseWorkflowService;

    protected SimpleWorkflowService<LicenseTransfer> transferWorkflowService;

    protected PersistenceService<T, Long> licensePersitenceService;

    public AbstractLicenseService(final PersistenceService<T, Long> licensePersitenceService) {
        this.licensePersitenceService = licensePersitenceService;
    }

    protected abstract LicenseAppType getLicenseApplicationTypeForRenew();

    protected abstract License additionalOperations(T license, Set<EgDemandReasonMaster> egDemandReasonMasters,
            Installment installment);

    protected abstract LicenseAppType getLicenseApplicationType();

    protected abstract Module getModuleName();

    protected abstract NatureOfBusiness getNatureOfBusiness();

    protected abstract void sendEmailAndSMS(T license, String currentAction);

    public PersistenceService<T, Long> licensePersitenceService() {
        return licensePersitenceService;
    }

    public void setLicenseWorkflowService(final SimpleWorkflowService<T> licenseWorkflowService) {
        this.licenseWorkflowService = licenseWorkflowService;
    }

    public void setTransferWorkflowService(final SimpleWorkflowService<LicenseTransfer> transferWorkflowService) {
        this.transferWorkflowService = transferWorkflowService;
    }

    public T getLicenseById(final Long id) {
        return licensePersitenceService.findById(id, false);
    }

    @Transactional
    public void create(final T license, final WorkflowBean workflowBean) {
        license.setLicenseAppType((LicenseAppType) this.persistenceService.find("from  LicenseAppType where name='New' "));
        this.raiseNewDemand(license);
        license.getLicensee().setLicense(license);
        license.updateStatus((LicenseStatus) persistenceService.find("from org.egov.tl.entity.LicenseStatus where name=? ",
                Constants.LICENSE_STATUS_ACKNOWLEDGED));
        final EgwStatus statusChange = (EgwStatus) persistenceService.find(
                "from org.egov.commons.EgwStatus where moduletype=? and code=?", Constants.TRADELICENSEMODULE,
                Constants.APPLICATION_STATUS_CREATED_CODE);
        license.setEgwStatus(statusChange);
        license.setApplicationNumber(applicationNumberGenerator.generate());
        this.licensePersitenceService.applyAuditing(license);
        this.processAndStoreDocument(license.getDocuments());
        this.transitionWorkFlow(license, workflowBean);
        license.getState().setCreatedBy(license.getCreatedBy());
        license.getState().setCreatedDate(new Date());
        license.getState().setLastModifiedBy(license.getCreatedBy());
        license.getState().setLastModifiedDate(new Date());
        this.licensePersitenceService.persist(license);
        this.sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        this.updateIndexService.updateTradeLicenseIndexes(license);

    }

    private BigDecimal raiseNewDemand(final T license) {
        final LicenseDemand ld = new LicenseDemand();
        final Module moduleName = getModuleName();
        BigDecimal totalAmount = ZERO;
        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(moduleName,
                license.getApplicationDate());
        ld.setIsHistory("N");
        ld.setEgInstallmentMaster(installment);
        ld.setLicense(license);
        ld.setIsLateRenewal('0');
        ld.setCreateDate(new Date());
        List<FeeMatrixDetail> feeMatrixDetails = feeMatrixService.findFeeList(license);
        for (final FeeMatrixDetail fm : feeMatrixDetails) {
            final EgDemandReasonMaster reasonMaster = demandGenericDao
                    .getDemandReasonMasterByCode(fm.getFeeMatrix().getFeeType().getName(), moduleName);
            final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, installment,
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
        final Module moduleName = getModuleName();
        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(moduleName,
                license.getApplicationDate());
        final Set<EgDemandDetails> demandDetails = licenseDemand.getEgDemandDetails();
        final List<FeeMatrixDetail> feeList = feeMatrixService.findFeeList(license);
        for (final EgDemandDetails dmd : demandDetails) {
            for (final FeeMatrixDetail fm : feeList) {
                if (installment.getId().equals(dmd.getEgDemandReason().getEgInstallmentMaster().getId())) {
                    if (dmd.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(fm.getFeeMatrix().getFeeType().getName())) {
                        dmd.setAmount(fm.getAmount());
                        dmd.setModifiedDate(new Date());
                    }
                }
            }
        }
        recalculateBaseDemand(licenseDemand);
        return license;

    }

    @Transactional
    public BigDecimal recalculateDemand(final List<FeeMatrixDetail> feeList, final T license) {
        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), new Date());
        BigDecimal totalAmount = ZERO;
        final LicenseDemand licenseDemand = license.getCurrentDemand();
        //Recalculating current demand detail according to fee matrix
        for (final EgDemandDetails dmd : licenseDemand.getEgDemandDetails()) {
            for (final FeeMatrixDetail fm : feeList) {
                if (installment.getId().equals(dmd.getEgDemandReason().getEgInstallmentMaster().getId())) {
                    if (dmd.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(fm.getFeeMatrix().getFeeType().getName())) {
                        dmd.setAmount(fm.getAmount());
                        dmd.setAmtCollected(ZERO);
                        totalAmount = totalAmount.add(fm.getAmount());
                    }
                }
            }
        }
        recalculateBaseDemand(licenseDemand);
        return totalAmount;
    }

    @Transactional
    public void createLegacyLicense(final T license, final Map<Integer, Integer> legacyInstallmentwiseFees,
            final Map<Integer, Boolean> legacyFeePayStatus) {
        if (!this.licensePersitenceService.findAllBy("from License where oldLicenseNumber = ?", license.getOldLicenseNumber())
                .isEmpty())
            throw new ValidationException("TL-001", "TL-001", license.getOldLicenseNumber());
        addLegacyDemand(legacyInstallmentwiseFees, legacyFeePayStatus, license);
        this.processAndStoreDocument(license.getDocuments());
        license.setLicenseAppType((LicenseAppType) this.persistenceService.find("from  LicenseAppType where name='New' "));
        license.getLicensee().setLicense(license);
        license.updateStatus((LicenseStatus) persistenceService.find("from org.egov.tl.entity.LicenseStatus where name=? ",
                Constants.LICENSE_STATUS_ACTIVE));
        license.setApplicationNumber(applicationNumberGenerator.generate());
        license.setLegacy(true);
        license.setActive(true);
        this.licensePersitenceService.applyAuditing(license);
        license.generateLicenseNumber(getNextRunningLicenseNumber("egtl_license_number"));
        validityService.applyLicenseValidity(license);
        this.licensePersitenceService.persist(license);
    }

    private void addLegacyDemand(final Map<Integer, Integer> legacyInstallmentwiseFees,
            final Map<Integer, Boolean> legacyFeePayStatus,
            final T license) {
        final LicenseDemand licenseDemand = new LicenseDemand();
        licenseDemand.setIsHistory("N");
        licenseDemand.setCreateDate(new Date());
        licenseDemand.setLicense(license);
        licenseDemand.setIsLateRenewal('0');
        final Module module = getModuleName();
        for (final Map.Entry<Integer, Integer> legacyInstallmentwiseFee : legacyInstallmentwiseFees.entrySet()) {
            if (legacyInstallmentwiseFee.getValue() != null && legacyInstallmentwiseFee.getValue() > 0) {
                final Installment installment = installmentDao.fetchInstallmentByModuleAndInstallmentNumber(module,
                        legacyInstallmentwiseFee.getKey());

                licenseDemand.setEgInstallmentMaster(installment);
                final BigDecimal demandAmount = BigDecimal.valueOf(legacyInstallmentwiseFee.getValue());
                final BigDecimal amtCollected = legacyFeePayStatus.get(legacyInstallmentwiseFee.getKey()) == null
                        || !legacyFeePayStatus.get(legacyInstallmentwiseFee.getKey()) ? ZERO : demandAmount;
                licenseDemand.getEgDemandDetails().add(
                        EgDemandDetails.fromReasonAndAmounts(demandAmount,
                                demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                                        demandGenericDao.getDemandReasonMasterByCode("License Fee", module),
                                        installment, module),
                                amtCollected));
                licenseDemand.setBaseDemand(demandAmount.add(licenseDemand.getBaseDemand()));
                licenseDemand.setAmtCollected(amtCollected.add(licenseDemand.getAmtCollected()));
            }
        }
        license.setLicenseDemand(licenseDemand);

    }

    @Transactional
    public void updateLegacyLicense(final T license, final Map<Integer, Integer> updatedInstallmentFees,
            final Map<Integer, Boolean> legacyFeePayStatus) {
        updateLegacyDemand(license, updatedInstallmentFees, legacyFeePayStatus);
        this.licensePersitenceService.applyAuditing(license);
        this.processAndStoreDocument(license.getDocuments());
        this.licensePersitenceService.persist(license);
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
                final BigDecimal updatedDemandAmt = BigDecimal.valueOf(updatedFee);
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
        final Module module = getModuleName();
        for (final Map.Entry<Integer, Integer> updatedInstallmentFee : updatedInstallmentFees.entrySet()) {
            if (updatedInstallmentFee.getValue() != null && updatedInstallmentFee.getValue() > 0) {
                final Installment installment = installmentDao.fetchInstallmentByModuleAndInstallmentNumber(module,
                        updatedInstallmentFee.getKey());
                final BigDecimal demandAmount = BigDecimal.valueOf(updatedInstallmentFee.getValue());
                final BigDecimal amtCollected = legacyFeePayStatus.get(updatedInstallmentFee.getKey()) == null
                        || !legacyFeePayStatus.get(updatedInstallmentFee.getKey()) ? ZERO : demandAmount;
                licenseDemand.getEgDemandDetails().add(
                        EgDemandDetails.fromReasonAndAmounts(demandAmount,
                                demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                                        demandGenericDao.getDemandReasonMasterByCode("License Fee", module),
                                        installment, module),
                                amtCollected));
            }
        }
        // Recalculating BasedDemand
        recalculateBaseDemand(licenseDemand);

    }

    public void recalculateBaseDemand(final LicenseDemand licenseDemand) {
        licenseDemand.setAmtCollected(ZERO);
        licenseDemand.setBaseDemand(ZERO);
        for (final EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails()) {
            licenseDemand.setAmtCollected(licenseDemand.getAmtCollected().add(demandDetail.getAmtCollected()));
            licenseDemand.setBaseDemand(licenseDemand.getBaseDemand().add(demandDetail.getAmount()));
        }
    }

    public List<Installment> getCurrAndPreviousInstallment() {
        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), new Date());
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(installment.getInstallmentYear());
        calendar.add(Calendar.YEAR, -1);
        final Date previousInstallmentDate = calendar.getTime();
        final Installment previousInstallment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(),
                previousInstallmentDate);
        final List<Installment> installmentList = new ArrayList<Installment>();
        installmentList.add(installment);
        installmentList.add(previousInstallment);
        return installmentList;
    }

    public Serializable getNextRunningLicenseNumber(final String sequenceName) {
        return sequenceNumberGenerator.getNextSequence(sequenceName);
    }

    /**
     * method to get checklist details
     *
     * @param license
     * @return checkList
     */
    public List<LicenseChecklistHelper> getLicenseChecklist(final T license) {
        final List<LicenseChecklistHelper> checkList = new ArrayList<LicenseChecklistHelper>();
        if (license.getLicenseCheckList() != null) {
            final String[] str = license.getLicenseCheckList().split("\\^");
            for (final Object obj : str)
                checkList.add(new LicenseChecklistHelper(obj.toString(), obj.toString(), "checked"));
        }
        return checkList;
    }

    @Transactional
    public void renew(final T license, final WorkflowBean workflowBean) {
        license.setApplicationNumber(applicationNumberGenerator.generate());
        this.recalculateDemand(feeMatrixService.findFeeList(license), license);
        final LicenseStatus status = (LicenseStatus) persistenceService.find(
                "from org.egov.tl.entity.LicenseStatus where name=? ", Constants.LICENSE_STATUS_ACKNOWLEDGED);
        license.updateStatus(status);
        final EgwStatus statusChange = (EgwStatus) persistenceService.find(
                "from org.egov.commons.EgwStatus where moduletype=? and code=?", Constants.TRADELICENSEMODULE,
                Constants.APPLICATION_STATUS_CREATED_CODE);
        license.setEgwStatus(statusChange);
        Position pos = null;
        license.setLicenseAppType(getLicenseApplicationTypeForRenew());
        final User currentUser = securityUtils.getCurrentUser();
        if (null != workflowBean.getApproverPositionId() && workflowBean.getApproverPositionId() != -1)
            pos = (Position) persistenceService.find("from Position where id=?", workflowBean.getApproverPositionId());
        final WorkFlowMatrix wfmatrix = licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
        license.reinitiateTransition().start().withSenderName(currentUser.getUsername() + "::" + currentUser.getName())
                .withComments(workflowBean.getApproverComments())
                .withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(pos)
                .withNextAction(wfmatrix.getNextAction());
        this.licensePersitenceService.applyAuditing(license);
        licensePersitenceService.persist(license);
        this.sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        this.updateIndexService.updateTradeLicenseIndexes(license);
    }

    @Transactional
    public void transitionWorkFlow(final T license, final WorkflowBean workflowBean) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;

        if (null != license.getId())
            wfInitiator = getWorkflowInitiator(license);

        if (BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            if (wfInitiator.equals(userAssignment)) {
                license.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
                if (license.getLicenseAppType() != null
                        && license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE))
                    license.setLicenseAppType(getLicenseApplicationType());

            } else {
                final String stateValue = license.getCurrentState().getValue().split(":")[0] + ":" + WORKFLOW_STATE_REJECTED;
                license.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING);
            }

        } else if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            license.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate());
        else {
            if (null != workflowBean.getApproverPositionId() && workflowBean.getApproverPositionId() != -1)
                pos = (Position) persistenceService.find("from Position where id=?", workflowBean.getApproverPositionId());
            if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                final Assignment commissionerUsr = assignmentService.getPrimaryAssignmentForUser(user.getId());
                pos = (Position) persistenceService.find("from Position where id=?", commissionerUsr.getPosition().getId());
            }
            if (null == license.getState()) {
                final WorkFlowMatrix wfmatrix = licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, null, workflowBean.getCurrentState(), null);
                license.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if (license.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                license.transition(true).end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                final WorkFlowMatrix wfmatrix = licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
                license.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }

        }
    }

    protected Assignment getWorkflowInitiator(final T license) {
        final Assignment wfInitiator = assignmentService.getPrimaryAssignmentForUser(license.getCreatedBy().getId());
        return wfInitiator;
    }

    @Transactional
    public void processAndStoreDocument(final List<LicenseDocument> documents) {
        documents.forEach(document -> {
            if (!(document.getUploads().isEmpty() || document.getUploadsContentType().isEmpty())) {
                int fileCount = 0;
                for (final File file : document.getUploads()) {
                    final FileStoreMapper fileStore = fileStoreService.store(file,
                            document.getUploadsFileName().get(fileCount),
                            document.getUploadsContentType().get(fileCount++), "EGTL");
                    document.getFiles().add(fileStore);
                }
            }
            document.setType(licenseDocumentTypeService.load(document.getType().getId(), LicenseDocumentType.class));
            persistenceService.applyAuditing(document);
        });
    }

    public List<LicenseDocumentType> getDocumentTypesByTransaction(final String transaction) {
        return persistenceService.findAllBy("from LicenseDocumentType where applicationType = ?",
                transaction);
    }

    public List<NatureOfBusiness> getAllNatureOfBusinesses() {
        return persistenceService.findAllBy("from NatureOfBusiness order by name");
    }

    public T getLicenseByLicenseNumber(final String licenseNumber) {
        return licensePersitenceService.find("from License where licenseNumber=?", licenseNumber);
    }

    public T getLicenseByApplicationNumber(final String applicationNumber) {
        return licensePersitenceService.find("from License where applicationNumber=?", applicationNumber);
    }

    public Boundary blockByLocality(final Long localityId) {
        final Boundary blockBoundary = (Boundary) persistenceService.find(
                "select CH.parent from CrossHierarchy CH where CH.child.id = ? ", localityId);
        return blockBoundary;

    }

    public List<Installment> getLastFiveYearInstallmentsForLicense() {
        return installmentDao.fetchInstallments(getModuleName(), new Date(), 6);
    }

    public Map<String, Map<String, BigDecimal>> getOutstandingFee(final T license) {
        final Map<String, Map<String, BigDecimal>> outstandingFee = new HashMap<>();
        final Installment currentInstallmentYear = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), new Date());
        final LicenseDemand licenseDemand = license.getCurrentDemand();
        for (final EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails()) {
            final String demandReason = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
            final Installment installmentYear = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            Map<String, BigDecimal> feeByTypes = null;
            if (outstandingFee.containsKey(demandReason))
                feeByTypes = outstandingFee.get(demandReason);
            else {
                feeByTypes = new HashMap<>();
                feeByTypes.put("arrear", ZERO);
                feeByTypes.put("current", ZERO);
            }
            final BigDecimal demandAmount = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
            if (installmentYear.equals(currentInstallmentYear))
                feeByTypes.put("current", demandAmount);
            else
                feeByTypes.put("arrear", feeByTypes.get("arrear").add(demandAmount));
            outstandingFee.put(demandReason, feeByTypes);
        }

        return outstandingFee;

    }

    public TradeLicenseUpdateIndexService getUpdateIndexService() {
        return updateIndexService;
    }

    public void setUpdateIndexService(final TradeLicenseUpdateIndexService updateIndexService) {
        this.updateIndexService = updateIndexService;
    }
    
    public List<T> getAllLicensesByNatureOfBusiness(final String natureOfBusiness) {
        return licensePersitenceService.getSession().createCriteria(License.class).
                createAlias("natureOfBusiness", "nb", JoinType.LEFT_OUTER_JOIN).add(Restrictions.eq("nb.name", natureOfBusiness))
                .setCacheMode(CacheMode.IGNORE).list();
    }
    
}