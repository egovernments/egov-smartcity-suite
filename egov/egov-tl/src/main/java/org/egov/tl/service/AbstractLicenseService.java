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

import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
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
import org.egov.tl.entity.LicenseStatus;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseNumberUtils;
import org.hibernate.CacheMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.math.BigDecimal.ZERO;
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.BUTTONREJECT;
import static org.egov.tl.utils.Constants.GENERATECERTIFICATE;
import static org.egov.tl.utils.Constants.WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING;
import static org.egov.tl.utils.Constants.WORKFLOW_STATE_REJECTED;

@Transactional(readOnly = true)
public abstract class AbstractLicenseService<T extends License> {

    @Autowired
    @Qualifier("entityQueryService")
    protected PersistenceService persistenceService;

    @Autowired
    protected InstallmentHibDao installmentDao;

    @Autowired
    protected LicenseNumberUtils licenseNumberUtils;

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
    protected TradeLicenseUpdateIndexService updateIndexService;

    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected DemandGenericHibDao demandGenericDao;

    @Autowired
    protected ValidityService validityService;

    protected SimpleWorkflowService<T> licenseWorkflowService;

    protected PersistenceService<T, Long> licensePersitenceService;

    public AbstractLicenseService(PersistenceService<T, Long> licensePersitenceService) {
        this.licensePersitenceService = licensePersitenceService;
    }

    protected abstract LicenseAppType getLicenseApplicationTypeForRenew();

    protected abstract LicenseAppType getLicenseApplicationType();

    protected abstract Module getModuleName();

    protected abstract NatureOfBusiness getNatureOfBusiness();

    protected abstract void sendEmailAndSMS(T license, String currentAction);

    public PersistenceService<T, Long> licensePersitenceService() {
        return this.licensePersitenceService;
    }

    public void setLicenseWorkflowService(SimpleWorkflowService<T> licenseWorkflowService) {
        this.licenseWorkflowService = licenseWorkflowService;
    }

    public T getLicenseById(Long id) {
        return this.licensePersitenceService.findById(id, false);
    }

    @Transactional
    public void create(T license, WorkflowBean workflowBean) {
        license.setLicenseAppType((LicenseAppType) persistenceService.find("from  LicenseAppType where name='New' "));
        raiseNewDemand(license);
        license.getLicensee().setLicense(license);
        license.setStatus((LicenseStatus) this.persistenceService.find("from org.egov.tl.entity.LicenseStatus where name=? ",
                Constants.LICENSE_STATUS_ACKNOWLEDGED));
        EgwStatus statusChange = (EgwStatus) this.persistenceService.find(
                "from org.egov.commons.EgwStatus where moduletype=? and code=?", Constants.TRADELICENSEMODULE,
                Constants.APPLICATION_STATUS_CREATED_CODE);
        license.setEgwStatus(statusChange);
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        licensePersitenceService.applyAuditing(license);
        processAndStoreDocument(license.getDocuments());
        transitionWorkFlow(license, workflowBean);
        license.getState().setCreatedBy(license.getCreatedBy());
        license.getState().setCreatedDate(new Date());
        license.getState().setLastModifiedBy(license.getCreatedBy());
        license.getState().setLastModifiedDate(new Date());
        licensePersitenceService.persist(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        updateIndexService.updateTradeLicenseIndexes(license);

    }

    private BigDecimal raiseNewDemand(T license) {
        LicenseDemand ld = new LicenseDemand();
        Module moduleName = this.getModuleName();
        BigDecimal totalAmount = ZERO;
        Installment installment = this.installmentDao.getInsatllmentByModuleForGivenDate(moduleName,
                license.getApplicationDate());
        ld.setIsHistory("N");
        ld.setEgInstallmentMaster(installment);
        ld.setLicense(license);
        ld.setIsLateRenewal('0');
        ld.setCreateDate(new Date());
        List<FeeMatrixDetail> feeMatrixDetails = this.feeMatrixService.findFeeList(license);
        for (FeeMatrixDetail fm : feeMatrixDetails) {
            EgDemandReasonMaster reasonMaster = this.demandGenericDao
                    .getDemandReasonMasterByCode(fm.getFeeMatrix().getFeeType().getName(), moduleName);
            EgDemandReason reason = this.demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, installment,
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

    public License updateDemandForChangeTradeArea(T license) {
        LicenseDemand licenseDemand = license.getLicenseDemand();
        Module moduleName = this.getModuleName();
        Installment installment = this.installmentDao.getInsatllmentByModuleForGivenDate(moduleName,
                license.getApplicationDate());
        Set<EgDemandDetails> demandDetails = licenseDemand.getEgDemandDetails();
        List<FeeMatrixDetail> feeList = this.feeMatrixService.findFeeList(license);
        for (EgDemandDetails dmd : demandDetails)
            for (FeeMatrixDetail fm : feeList)
                if (installment.getId().equals(dmd.getEgDemandReason().getEgInstallmentMaster().getId()))
                    if (dmd.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(fm.getFeeMatrix().getFeeType().getName())) {
                        dmd.setAmount(fm.getAmount());
                        dmd.setModifiedDate(new Date());
                    }
        this.recalculateBaseDemand(licenseDemand);
        return license;

    }

    @Transactional
    public BigDecimal recalculateDemand(List<FeeMatrixDetail> feeList, T license) {
        Installment installment = this.installmentDao.getInsatllmentByModuleForGivenDate(this.getModuleName(), new Date());
        BigDecimal totalAmount = ZERO;
        LicenseDemand licenseDemand = license.getCurrentDemand();
        // Recalculating current demand detail according to fee matrix
        for (EgDemandDetails dmd : licenseDemand.getEgDemandDetails())
            for (FeeMatrixDetail fm : feeList)
                if (installment.getId().equals(dmd.getEgDemandReason().getEgInstallmentMaster().getId()))
                    if (dmd.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(fm.getFeeMatrix().getFeeType().getName())) {
                        dmd.setAmount(fm.getAmount());
                        dmd.setAmtCollected(ZERO);
                        totalAmount = totalAmount.add(fm.getAmount());
                    }
        this.recalculateBaseDemand(licenseDemand);
        return totalAmount;
    }

    @Transactional
    public void createLegacyLicense(T license, Map<Integer, Integer> legacyInstallmentwiseFees,
                                    Map<Integer, Boolean> legacyFeePayStatus) {
        if (!licensePersitenceService.findAllBy("from License where oldLicenseNumber = ?", license.getOldLicenseNumber())
                .isEmpty())
            throw new ValidationException("TL-001", "TL-001", license.getOldLicenseNumber());
        this.addLegacyDemand(legacyInstallmentwiseFees, legacyFeePayStatus, license);
        processAndStoreDocument(license.getDocuments());
        license.setLicenseAppType((LicenseAppType) persistenceService.find("from  LicenseAppType where name='New' "));
        license.getLicensee().setLicense(license);
        license.setStatus((LicenseStatus) this.persistenceService.find("from org.egov.tl.entity.LicenseStatus where name=? ",
                Constants.LICENSE_STATUS_ACTIVE));
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        license.setLegacy(true);
        license.setActive(true);
        licensePersitenceService.applyAuditing(license);
        license.setLicenseNumber(licenseNumberUtils.generateLicenseNumber());
        this.validityService.applyLicenseValidity(license);
        licensePersitenceService.persist(license);
    }

    private void addLegacyDemand(Map<Integer, Integer> legacyInstallmentwiseFees,
                                 Map<Integer, Boolean> legacyFeePayStatus,
                                 T license) {
        LicenseDemand licenseDemand = new LicenseDemand();
        licenseDemand.setIsHistory("N");
        licenseDemand.setCreateDate(new Date());
        licenseDemand.setLicense(license);
        licenseDemand.setIsLateRenewal('0');
        Module module = this.getModuleName();
        for (Entry<Integer, Integer> legacyInstallmentwiseFee : legacyInstallmentwiseFees.entrySet())
            if (legacyInstallmentwiseFee.getValue() != null && legacyInstallmentwiseFee.getValue() > 0) {
                Installment installment = this.installmentDao.fetchInstallmentByModuleAndInstallmentNumber(module,
                        legacyInstallmentwiseFee.getKey());

                licenseDemand.setEgInstallmentMaster(installment);
                BigDecimal demandAmount = BigDecimal.valueOf(legacyInstallmentwiseFee.getValue());
                BigDecimal amtCollected = legacyFeePayStatus.get(legacyInstallmentwiseFee.getKey()) == null
                        || !legacyFeePayStatus.get(legacyInstallmentwiseFee.getKey()) ? ZERO : demandAmount;
                licenseDemand.getEgDemandDetails().add(
                        EgDemandDetails.fromReasonAndAmounts(demandAmount,
                                this.demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                                        this.demandGenericDao.getDemandReasonMasterByCode("License Fee", module),
                                        installment, module),
                                amtCollected));
                licenseDemand.setBaseDemand(demandAmount.add(licenseDemand.getBaseDemand()));
                licenseDemand.setAmtCollected(amtCollected.add(licenseDemand.getAmtCollected()));
            }
        license.setLicenseDemand(licenseDemand);

    }

    @Transactional
    public void updateLegacyLicense(T license, Map<Integer, Integer> updatedInstallmentFees,
                                    Map<Integer, Boolean> legacyFeePayStatus) {
        this.updateLegacyDemand(license, updatedInstallmentFees, legacyFeePayStatus);
        licensePersitenceService.applyAuditing(license);
        processAndStoreDocument(license.getDocuments());
        licensePersitenceService.persist(license);
    }

    private void updateLegacyDemand(T license, Map<Integer, Integer> updatedInstallmentFees,
                                    Map<Integer, Boolean> legacyFeePayStatus) {
        LicenseDemand licenseDemand = license.getCurrentDemand();

        // Update existing demand details
        Iterator<EgDemandDetails> demandDetails = licenseDemand.getEgDemandDetails().iterator();
        while (demandDetails.hasNext()) {
            EgDemandDetails demandDetail = demandDetails.next();
            Integer installmentNumber = demandDetail.getEgDemandReason().getEgInstallmentMaster()
                    .getInstallmentNumber();
            Integer updatedFee = updatedInstallmentFees.get(installmentNumber);
            Boolean feePaymentStatus = legacyFeePayStatus.get(installmentNumber);
            if (updatedFee != null) {
                BigDecimal updatedDemandAmt = BigDecimal.valueOf(updatedFee);
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
        Module module = this.getModuleName();
        for (Entry<Integer, Integer> updatedInstallmentFee : updatedInstallmentFees.entrySet())
            if (updatedInstallmentFee.getValue() != null && updatedInstallmentFee.getValue() > 0) {
                Installment installment = this.installmentDao.fetchInstallmentByModuleAndInstallmentNumber(module,
                        updatedInstallmentFee.getKey());
                BigDecimal demandAmount = BigDecimal.valueOf(updatedInstallmentFee.getValue());
                BigDecimal amtCollected = legacyFeePayStatus.get(updatedInstallmentFee.getKey()) == null
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

    public void recalculateBaseDemand(LicenseDemand licenseDemand) {
        licenseDemand.setAmtCollected(ZERO);
        licenseDemand.setBaseDemand(ZERO);
        for (EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails()) {
            licenseDemand.setAmtCollected(licenseDemand.getAmtCollected().add(demandDetail.getAmtCollected()));
            licenseDemand.setBaseDemand(licenseDemand.getBaseDemand().add(demandDetail.getAmount()));
        }
    }

    @Transactional
    public void renew(T license, WorkflowBean workflowBean) {
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        recalculateDemand(this.feeMatrixService.findFeeList(license), license);
        LicenseStatus status = (LicenseStatus) this.persistenceService.find(
                "from org.egov.tl.entity.LicenseStatus where name=? ", Constants.LICENSE_STATUS_ACKNOWLEDGED);
        license.setStatus(status);
        EgwStatus statusChange = (EgwStatus) this.persistenceService.find(
                "from org.egov.commons.EgwStatus where moduletype=? and code=?", Constants.TRADELICENSEMODULE,
                Constants.APPLICATION_STATUS_CREATED_CODE);
        license.setEgwStatus(statusChange);
        Position pos = null;
        license.setLicenseAppType(this.getLicenseApplicationTypeForRenew());
        User currentUser = this.securityUtils.getCurrentUser();
        if (null != workflowBean.getApproverPositionId() && workflowBean.getApproverPositionId() != -1)
            pos = (Position) this.persistenceService.find("from Position where id=?", workflowBean.getApproverPositionId());
        WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
        license.reinitiateTransition().start().withSenderName(currentUser.getUsername() + "::" + currentUser.getName())
                .withComments(workflowBean.getApproverComments())
                .withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(pos)
                .withNextAction(wfmatrix.getNextAction());
        licensePersitenceService.applyAuditing(license);
        this.licensePersitenceService.persist(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        updateIndexService.updateTradeLicenseIndexes(license);
    }

    @Transactional
    public void transitionWorkFlow(T license, WorkflowBean workflowBean) {
        DateTime currentDate = new DateTime();
        User user = this.securityUtils.getCurrentUser();
        Assignment userAssignment = this.assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;

        if (null != license.getId())
            wfInitiator = this.getWorkflowInitiator(license);

        if (BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            if (wfInitiator.equals(userAssignment)) {
                license.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
                if (license.getLicenseAppType() != null
                        && license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE))
                    license.setLicenseAppType(this.getLicenseApplicationType());

            } else {
                String stateValue = license.getCurrentState().getValue().split(":")[0] + ":" + WORKFLOW_STATE_REJECTED;
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
                pos = (Position) this.persistenceService.find("from Position where id=?", workflowBean.getApproverPositionId());
            if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                Assignment commissionerUsr = this.assignmentService.getPrimaryAssignmentForUser(user.getId());
                pos = (Position) this.persistenceService.find("from Position where id=?", commissionerUsr.getPosition().getId());
            }
            if (null == license.getState()) {
                WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
                license.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if (license.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                license.transition(true).end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
                license.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }

        }
    }

    protected Assignment getWorkflowInitiator(T license) {
        Assignment wfInitiator = this.assignmentService.getPrimaryAssignmentForUser(license.getCreatedBy().getId());
        return wfInitiator;
    }

    @Transactional
    public void processAndStoreDocument(List<LicenseDocument> documents) {
        documents.forEach(document -> {
            if (!(document.getUploads().isEmpty() || document.getUploadsContentType().isEmpty())) {
                int fileCount = 0;
                for (File file : document.getUploads()) {
                    FileStoreMapper fileStore = this.fileStoreService.store(file,
                            document.getUploadsFileName().get(fileCount),
                            document.getUploadsContentType().get(fileCount++), "EGTL");
                    document.getFiles().add(fileStore);
                }
            }
            document.setType(this.licenseDocumentTypeService.load(document.getType().getId(), LicenseDocumentType.class));
            this.persistenceService.applyAuditing(document);
        });
    }

    public List<LicenseDocumentType> getDocumentTypesByTransaction(String transaction) {
        return this.persistenceService.findAllBy("from LicenseDocumentType where applicationType = ?",
                transaction);
    }

    public List<NatureOfBusiness> getAllNatureOfBusinesses() {
        return this.persistenceService.findAllBy("from NatureOfBusiness order by name");
    }

    public T getLicenseByLicenseNumber(String licenseNumber) {
        return this.licensePersitenceService.find("from License where licenseNumber=?", licenseNumber);
    }

    public T getLicenseByApplicationNumber(String applicationNumber) {
        return this.licensePersitenceService.find("from License where applicationNumber=?", applicationNumber);
    }

    public List<Installment> getLastFiveYearInstallmentsForLicense() {
        List<Installment> installmentList = this.installmentDao.fetchInstallments(this.getModuleName(), new Date(), 6);
        Collections.reverse(installmentList);
        return installmentList;
    }

    public Map<String, Map<String, BigDecimal>> getOutstandingFee(T license) {
        Map<String, Map<String, BigDecimal>> outstandingFee = new HashMap<>();
        Installment currentInstallmentYear = this.installmentDao.getInsatllmentByModuleForGivenDate(this.getModuleName(), new Date());
        LicenseDemand licenseDemand = license.getCurrentDemand();
        for (EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails()) {
            String demandReason = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
            Installment installmentYear = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            Map<String, BigDecimal> feeByTypes = null;
            if (outstandingFee.containsKey(demandReason))
                feeByTypes = outstandingFee.get(demandReason);
            else {
                feeByTypes = new HashMap<>();
                feeByTypes.put("arrear", ZERO);
                feeByTypes.put("current", ZERO);
            }
            BigDecimal demandAmount = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
            if (installmentYear.equals(currentInstallmentYear))
                feeByTypes.put("current", demandAmount);
            else
                feeByTypes.put("arrear", feeByTypes.get("arrear").add(demandAmount));
            outstandingFee.put(demandReason, feeByTypes);
        }

        return outstandingFee;

    }

    public List<T> getAllLicensesByNatureOfBusiness(String natureOfBusiness) {
        return this.licensePersitenceService.getSession().createCriteria(License.class)
                .createAlias("natureOfBusiness", "nb", JoinType.LEFT_OUTER_JOIN).add(Restrictions.eq("nb.name", natureOfBusiness))
                .setCacheMode(CacheMode.IGNORE).list();
    }

}