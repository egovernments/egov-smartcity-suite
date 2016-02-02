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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.persistence.utils.SequenceNumberGenerator;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.FeeMatrix;
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
import org.elasticsearch.common.joda.time.DateTime;
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
    @Qualifier("feeService")
    protected FeeService feeService;

    @Autowired
    @Qualifier("persistenceService")
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
        this.raiseNewDemand(feeMatrixService.findFeeList(license), license);
        license.getLicensee().setLicense(license);
        license.updateStatus((LicenseStatus) persistenceService.find("from org.egov.tl.entity.LicenseStatus where name=? ",
                Constants.LICENSE_STATUS_ACKNOWLEDGED));
        final EgwStatus statusChange = (EgwStatus) persistenceService.find(
                "from org.egov.commons.EgwStatus where moduletype=? and code=?", Constants.TRADELICENSEMODULE,
                Constants.APPLICATION_STATUS_CREATED_CODE);
        license.setEgwStatus(statusChange);
        license.setApplicationNumber(applicationNumberGenerator.generate());
        setAuditEntries(license);
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

    private BigDecimal raiseNewDemand(final List<FeeMatrixDetail> feeList, final T license) {
        final LicenseDemand ld = new LicenseDemand();
        final Module moduleName = getModuleName();
        BigDecimal totalAmount = BigDecimal.ZERO;
        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(moduleName,
                license.getApplicationDate());
        ld.setIsHistory("N");
        ld.setEgInstallmentMaster(installment);
        ld.setCreateDate(new Date());
        ld.setLicense(license);
        ld.setIsLateRenewal('0');
        LOGGER.debug("calculating FEE          ...............................................");
        Set<EgDemandDetails> demandDetails = null;
        if (ld.getEgDemandDetails().isEmpty() || ld.getEgDemandDetails() == null)
            demandDetails = new LinkedHashSet<EgDemandDetails>();
        else
            demandDetails = ld.getEgDemandDetails();
        for (final FeeMatrixDetail fm : feeList) {
            final EgDemandReasonMaster reasonMaster = demandGenericDao
                    .getDemandReasonMasterByCode(fm.getFeeMatrix().getFeeType().getName(), moduleName);
            final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, installment,
                    moduleName);
            LOGGER.info("Reson for Reason Master" + ":master:" + reasonMaster.getReasonMaster() + "Reason:" + reason);
            if (fm.getFeeMatrix().getFeeType().getName().contains("Late"))
                continue;

            if (LOGGER.isDebugEnabled())
                LOGGER.debug(reason + "::" + fm.getAmount());
            if (reason != null) {
                demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
                totalAmount = totalAmount.add(fm.getAmount());
            }

        }

        ld.setEgDemandDetails(demandDetails);
        ld.setBaseDemand(totalAmount);
        final HashSet<LicenseDemand> demandSet = new HashSet<LicenseDemand>();
        demandSet.add(ld);
        license.setDemandSet(demandSet);
        return totalAmount;
    }

    @Transactional
    public BigDecimal recalculateDemand(final List<FeeMatrixDetail> feeList, final T license) {

        LOGGER.debug("Re calculating FEE          ...............................................");

        final EgDemand licenseDemand = license.getCurrentDemand();
        BigDecimal totalAmount = BigDecimal.ZERO;
        final Set<EgDemandDetails> egDemandDetails = licenseDemand.getEgDemandDetails();
        for (final EgDemandDetails dmd : egDemandDetails)
            for (final FeeMatrixDetail fm : feeList)
                if (dmd.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equalsIgnoreCase(fm.getFeeMatrix().getFeeType().getName())) {
                    dmd.setAmount(fm.getAmount());
                    totalAmount = totalAmount.add(fm.getAmount());
                }
        return totalAmount;
    }

    private void setAuditEntries(final T license) {
        if (license.getId() == null) {
            license.setCreatedBy(securityUtils.getCurrentUser());
            license.setCreatedDate(new Date());
        }
        license.setLastModifiedBy(securityUtils.getCurrentUser());
        license.setLastModifiedDate(new Date());

    }

    public String getFeeTypeForElectricalLicense(final T license) {
        final String feeType = null;
        // commented as licenseSubType is removed from License
        /*
         * if (license != null && license.getLicenseSubType() != null) if
         * (license.getLicenseSubType().getCode().equals(Constants.MAINTENANCE_CONTRACTORS)) feeType =
         * license.getLicenseSubType().getCode(); else if
         * (license.getLicenseSubType().getCode().equals(Constants.LIFT_CONTRACTORS)) feeType =
         * license.getLicenseSubType().getCode(); else if (license.getLicenseSubType().getCode().equals(Constants.BRAND_OWNER))
         * feeType = license.getLicenseSubType().getCode(); else if
         * (license.getLicenseSubType().getCode().equals(Constants.CONSULTANTS)) feeType = license.getLicenseSubType().getCode();
         * else if (license.getLicenseSubType().getCode().equals(Constants.CONTRACTORS)) feeType =
         * license.getLicenseSubType().getCode(); else if (license.getLicenseSubType().getCode().equals(Constants.DISTRIBUTOR))
         * feeType = license.getLicenseSubType().getCode(); else if
         * (license.getLicenseSubType().getCode().equals(Constants.POLE_SUPPLIER_CONTRACTORS)) feeType =
         * license.getLicenseSubType().getCode(); else if
         * (license.getLicenseSubType().getCode().equals(Constants.PUMP_MAINTENANCE)) feeType =
         * license.getLicenseSubType().getCode(); else if
         * (license.getLicenseSubType().getCode().equals(Constants.BOT_CONTRACTORS)) feeType =
         * license.getLicenseSubType().getCode(); else if
         * (license.getLicenseSubType().getCode().equals(Constants.FIRE_CONTRACTORS)) feeType =
         * license.getLicenseSubType().getCode();
         */
        return feeType;
    }

    @Transactional
    public void enterExistingLicense(final T license, final Map<Integer, BigDecimal> legacyInstallmentwiseFees) {
        if (!this.licensePersitenceService.findAllBy("from License where oldLicenseNumber = ?", license.getOldLicenseNumber())
                .isEmpty())
            throw new ApplicationRuntimeException("license.number.exist");
        addLegacyDemand(legacyInstallmentwiseFees, license);
        this.processAndStoreDocument(license.getDocuments());
        license.setLicenseAppType((LicenseAppType) this.persistenceService.find("from  LicenseAppType where name='New' "));
        license.getLicensee().setLicense(license);
        license.updateStatus((LicenseStatus) persistenceService.find("from org.egov.tl.entity.LicenseStatus where name=? ",
                Constants.LICENSE_STATUS_ACTIVE));
        license.setApplicationNumber(applicationNumberGenerator.generate());
        setAuditEntries(license);
        license.setLegacy(true);
        license.setActive(true);
        license.generateLicenseNumber(getNextRunningLicenseNumber("egtl_license_number"));
        this.licensePersitenceService.persist(license);
    }

    @Transactional
    public void updateLegacyLicense(final T license, final Map<Integer, BigDecimal> updatedInstallmentFees) {
        for (final LicenseDemand demand : license.getDemandSet()) {
            final BigDecimal updatedFee = updatedInstallmentFees.get(demand.getEgInstallmentMaster().getInstallmentNumber());
            demand.setBaseDemand(updatedFee);
            demand.getEgDemandDetails().iterator().next().setAmount(updatedFee);
        }
        this.processAndStoreDocument(license.getDocuments());
        setAuditEntries(license);
        this.licensePersitenceService.persist(license);
    }

    private void addLegacyDemand(final Map<Integer, BigDecimal> legacyInstallmentwiseFees, final T license) {
        final Module module = getModuleName();
        license.setDemandSet(new HashSet<>());
        for (final Map.Entry<Integer, BigDecimal> legacyInstallmentwiseFee : legacyInstallmentwiseFees.entrySet())
            if (legacyInstallmentwiseFee.getValue().doubleValue() > 0) {
                final Installment installment = installmentDao.fetchInstallmentByModuleAndInstallmentNumber(module,
                        legacyInstallmentwiseFee.getKey());
                final EgDemandReasonMaster reasonMaster = demandGenericDao.getDemandReasonMasterByCode("License Fee", module);
                final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster,
                        installment,
                        module);
                if (reason != null) {
                    final LicenseDemand licenseDemand = new LicenseDemand();
                    licenseDemand.setIsHistory("N");
                    licenseDemand.setEgInstallmentMaster(installment);
                    licenseDemand.setCreateDate(new Date());
                    licenseDemand.setLicense(license);
                    licenseDemand.setIsLateRenewal('0');
                    licenseDemand.getEgDemandDetails()
                            .add(EgDemandDetails.fromReasonAndAmounts(legacyInstallmentwiseFee.getValue(), reason,
                                    BigDecimal.ZERO));
                    licenseDemand.setBaseDemand(legacyInstallmentwiseFee.getValue());
                    license.getDemandSet().add(licenseDemand);
                }

            }
    }

    public LicenseDemand getCurrentYearDemand(final T license) {
        final Date currDate = new Date();
        final LicenseDemand currLicenseDemand = (LicenseDemand) persistenceService
                .find("from LicenseDemand ld where ld.license.id=? and (ld.egInstallmentMaster.fromDate <= ? and ld.egInstallmentMaster.toDate >=?)",
                        license.getId(), currDate, currDate);
        return currLicenseDemand;
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

    /**
     * TODO -- Fix me
     */
    public T processWorkflowForLicense(final T license, final WorkflowBean workflowBean) {
        /*
         * for (final State state : license.getState().getHistory()) if (state != null && state.getCreatedBy() != null) if
         * (state.getValue().equals(Constants.WORKFLOW_STATE_NEW)) { userID = state.getCreatedBy().getId(); break; } if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) { if (!license.isPaid()) throw new
         * ValidationException("applicationNumber", "license.fee.notcollected", license.getApplicationNumber()); Position position
         * = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
         * license.setCreationAndExpiryDate(); license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE +
         * Constants.WORKFLOW_STATE_APPROVED, position, workflowBean.getComments()); if (license.getIsUpgrade() == null &&
         * license.getTempLicenseNumber() == null) { String feeType = ""; if
         * (getName().getName().equals(Constants.ELECTRICALLICENSE_MODULENAME)) feeType = getFeeTypeForElectricalLicense(license);
         * else feeType = license.getClass().getSimpleName().toUpperCase(); final String nextRunningLicenseNumber =
         * getNextRunningLicenseNumber(feeType + "_" + license.getFeeTypeStr() + "_LICENSE_NUMBER");
         * license.generateLicenseNumber(nextRunningLicenseNumber); } final Module module =
         * license.getTradeName().getLicenseType().getModule(); if (module.getName().equals(Constants.PWDLICENSE_MODULENAME)) if
         * (org.apache.commons.lang.StringUtils.isBlank(license.getContractorCode()) &&
         * org.apache.commons.lang.StringUtils.isEmpty(license.getContractorCode()))
         * license.setContractorCode(license.getLicenseNumber()); position = eisCommonsManager.getPositionByUserId(userID); if
         * (license.getTradeName().isNocApplicable() != null && license.getTradeName().isNocApplicable())
         * license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_STATE_GENERATENOC, position,
         * workflowBean.getComments()); else license.changeState( Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE +
         * Constants.WORKFLOW_STATE_GENERATECERTIFICATE, position, workflowBean.getComments()); } else if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) { final Position nextPosition =
         * eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
         * license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_STATE_FORWARDED, nextPosition,
         * workflowBean.getComments()); } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) { if
         * (license.getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) { final Position position =
         * eisCommonsManager.getPositionByUserId(userID); license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE +
         * Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE, position, workflowBean.getComments()); } else { final Position
         * position = eisCommonsManager.getPositionByUserId(userID);
         * license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_STATE_REJECTED, position,
         * workflowBean.getComments()); } } else if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONPRINTCOMPLETED)) { if
         * (license.getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) { final Position position =
         * eisCommonsManager.getPositionByUserId(userID); workflowService().end(license, position); final LicenseStatus
         * activeStatus = (LicenseStatus) persistenceService .find("from org.egov.tl.entity.LicenseStatus where code='REJ'");
         * license.setStatus(activeStatus); } } else if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE)) { final Position position =
         * eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId())); workflowService().end(license,
         * position); final LicenseStatus activeStatus = (LicenseStatus) persistenceService .find(
         * "from org.egov.tl.entity.LicenseStatus where code='ACT'"); license.setStatus(activeStatus); }
         */
        return license;
    }

    /**
     * TODO -- Fix me
     */
    public T processWorkflowForRenewLicense(final T license, final WorkflowBean workflowBean) {
        /*
         * for (final State state : license.getState().getHistory()) if (state != null && state.getCreatedBy() != null) if
         * (state.getValue().equals(Constants.WORKFLOW_STATE_NEW)) { userID = new
         * UserManagerBean().getUserByID(state.getCreatedBy().getId()).getId(); break; } if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) { if (!license.isPaid()) throw new
         * ValidationException("licenseNumber", "renew.fee.notcollected", license.getLicenseNumber()); Position position =
         * eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId())); license.updateExpiryDate(new
         * Date()); license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_STATE_APPROVED, position,
         * workflowBean.getComments()); position = eisCommonsManager.getPositionByUserId(userID);
         * license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_STATE_GENERATECERTIFICATE,
         * position, workflowBean.getComments()); } else if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) { final Position nextPosition =
         * eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
         * license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_STATE_FORWARDED, nextPosition,
         * workflowBean.getComments()); } else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) { if
         * (license.getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) { final Position position =
         * eisCommonsManager.getPositionByUserId(userID); license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE +
         * Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE, position, workflowBean.getComments()); } else { final Position
         * position = eisCommonsManager.getPositionByUserId(userID);
         * license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_STATE_REJECTED, position,
         * workflowBean.getComments()); } } else if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONPRINTCOMPLETED)) { if
         * (license.getState().getValue().contains(Constants.WORKFLOW_STATE_GENERATEREJCERTIFICATE)) { final Position position =
         * eisCommonsManager.getPositionByUserId(userID); workflowService().end(license, position); final LicenseStatus
         * activeStatus = (LicenseStatus) persistenceService .find("from org.egov.tl.entity.LicenseStatus where code='REJ'");
         * license.setStatus(activeStatus); } } else if
         * (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE)) { final Position position =
         * eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId())); workflowService().end(license,
         * position); final LicenseStatus activeStatus = (LicenseStatus) persistenceService .find(
         * "from org.egov.tl.entity.LicenseStatus where code='ACT'"); license.setStatus(activeStatus); }
         */
        return license;
    }

    /**
     * TODO -- Fix me
     */
    /*
     * public void endWorkFlowForLicense(final T license) { final Position position =
     * eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId())); workflowService().end(license,
     * position); } public void initiateWorkFlowForLicense(final T license, final WorkflowBean workflowBean) { final Position
     * position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId())); if (position != null) {
     * workflowService().start(license, position, workflowBean.getComments());
     * license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + "NEW", position, workflowBean.getComments()); final
     * LicenseStatus underWorkflowStatus = (LicenseStatus) persistenceService .find(
     * "from org.egov.tl.entity.LicenseStatus where code='UWF'"); license.setStatus(underWorkflowStatus); final Module module =
     * license.getTradeName().getLicenseType().getModule(); if ((module.getName().equals(Constants.ELECTRICALLICENSE_MODULENAME)
     * || module.getName().equals( Constants.PWDLICENSE_MODULENAME)) && license.getFeeExemption() != null &&
     * license.getFeeExemption().equals("YES")) license.changeState(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE +
     * Constants.WORKFLOW_REG_FEE_STATE_COLLECTED, position, workflowBean.getComments()); } } public void
     * initiateWorkFlowForLicenseDraft(final T license, final WorkflowBean workflowBean) { final Position position =
     * eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId())); workflowService().start(license,
     * position, workflowBean.getComments()); final LicenseStatus underWorkflowStatus = (LicenseStatus) persistenceService .find(
     * "from org.egov.tl.entity.LicenseStatus where code='UWF'"); license.setStatus(underWorkflowStatus); } public void
     * initiateWorkFlowForRenewLicense(final T license, final WorkflowBean workflowBean) { final Position position =
     * eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId())); try {
     * workflowService().start(license, position, workflowBean.getComments()); } catch (final ApplicationRuntimeException e) { if
     * (license.getState().getValue().equalsIgnoreCase("END")) { license.setState(null); persistenceService.persist(license);
     * workflowService().start(license, position, workflowBean.getComments()); } else throw e; }
     * license.changeState(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + "NEW", position, workflowBean.getComments()); final
     * LicenseStatus underWorkflowStatus = (LicenseStatus) persistenceService .find(
     * "from org.egov.tl.entity.LicenseStatus where code='UWF'"); license.setStatus(underWorkflowStatus); }
     */
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
    public void renew(T license) {
        final LicenseAppType appType = getLicenseApplicationTypeForRenew();
        final NatureOfBusiness nature = getNatureOfBusiness();
        // commented need to be completed after fee matrix
        final List<FeeMatrix> feeList = new ArrayList<FeeMatrix>();// feeService.getFeeList(license.getTradeName(), appType,
                                                                   // nature);
        final BigDecimal totalAmount = BigDecimal.ZERO;
        // feeService.calculateFee(license, license.getTradeName(), getLicenseApplicationTypeForRenew(),
        // getNatureOfBusiness(), BigDecimal.ZERO, BigDecimal.ZERO);
        Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), new Date());
        final Date renewalDate = new Date();
        final String dateDiffToExpiryDate = license.getDateDiffToExpiryDate(renewalDate);
        if (dateDiffToExpiryDate != null) {
            boolean isExpired;
            final String[] split = dateDiffToExpiryDate.split("/");
            isExpired = split[0].equalsIgnoreCase("false") ? false : true;
            final int noOfMonths = Integer.parseInt(split[1]);

            if (isExpired == false && noOfMonths <= 1) {
                final Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.MONTH, 1);
                installment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), cal.getTime());
            } else if (isExpired == true && noOfMonths <= 6)
                installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), renewalDate);
            else
                throw new ApplicationRuntimeException("License already Expired Cant renew");

        }

        final EgReasonCategory reasonCategory = (EgReasonCategory) persistenceService
                .find("from org.egov.demand.model.EgReasonCategory where name='Fee'");
        final Set<EgDemandReasonMaster> egDemandReasonMasters = reasonCategory.getEgDemandReasonMasters();
        if (getModuleName().getName().equals(Constants.ELECTRICALLICENSE_MODULENAME))
            getFeeTypeForElectricalLicense(license);
        else
            license.getClass().getSimpleName().toUpperCase();
        license = (T) license.renew(feeList, appType, nature, installment, egDemandReasonMasters, totalAmount,
                applicationNumberGenerator.generate(), license.getFeeTypeStr(), getModuleName(), renewalDate);
        /*
         * HibernateUtil.getCurrentSession().flush(); HibernateUtil.getCurrentSession().refresh(license);
         */
        license = (T) additionalOperations(license, egDemandReasonMasters, installment);
        persistenceService.update(license);

        final LicenseStatus status = (LicenseStatus) persistenceService.find(
                "from org.egov.tl.entity.LicenseStatus where name=? ", Constants.LICENSE_STATUS_ACKNOWLEDGED);
        license.updateStatus(status);
    }

    @Transactional
    public T updateLicenseForFinalApproval(final T license) {
        final LicenseStatus status = (LicenseStatus) persistenceService
                .find("from org.egov.tl.entity.LicenseStatus where code='ACT'");
        license.setStatus(status);
        license.setCreationAndExpiryDateForEnterLicense();
        license.generateLicenseNumber(getNextRunningLicenseNumber("egtl_license_number"));
        return license;
    }

    @Transactional
    public T createDemandForViolationFee(T license) {
        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), new Date());
        license = (T) license.raiseDemandForViolationFee(installment, license);
        persistenceService.update(license);
        return license;
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
            if (wfInitiator.equals(userAssignment))
                license.transition(true).end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                final String stateValue = license.getCurrentState().getValue().split(":")[0] + ":" + WORKFLOW_STATE_REJECTED;
                license.transition(true).withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING);
            }

        } else if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            license.transition(true).end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
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
                license.transition().start().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if (license.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                license.transition(true).end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                final WorkFlowMatrix wfmatrix = licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, null, license.getCurrentState().getValue(), null);
                license.transition(true).withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
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

    /*
     * public TradeLicenseSmsAndEmailService getTradeLicenseSmsAndEmailService() { return tradeLicenseSmsAndEmailService; } public
     * void setTradeLicenseSmsAndEmailService(TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService) {
     * this.tradeLicenseSmsAndEmailService = tradeLicenseSmsAndEmailService; }
     */

    public List<Installment> getLastFiveYearInstallmentsForLicense() {
        return installmentDao.fetchInstallments(getModuleName(), new Date(), 6);
    }

    public Map<String, Map<String, BigDecimal>> getOutstandingFee(final T license) {
        final Map<String, Map<String, BigDecimal>> outstandingFee = new HashMap<>();
        final Installment currentInstallmentYear = installmentDao.getInsatllmentByModuleForGivenDate(getModuleName(), new Date());
        for (final LicenseDemand licenseDemand : license.getDemandSet()) {
            final Installment installmentYear = licenseDemand.getEgInstallmentMaster();
            for (final EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails()) {
                final String demandReason = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
                Map<String, BigDecimal> feeByTypes = null;
                if (outstandingFee.containsKey(demandReason))
                    feeByTypes = outstandingFee.get(demandReason);
                else {
                    feeByTypes = new HashMap<>();
                    feeByTypes.put("arrear", BigDecimal.ZERO);
                    feeByTypes.put("current", BigDecimal.ZERO);
                }
                if (installmentYear.equals(currentInstallmentYear))
                    feeByTypes.put("current", demandDetail.getAmount());
                else
                    feeByTypes.put("arrear", feeByTypes.get("arrear").add(demandDetail.getAmount()));
                outstandingFee.put(demandReason, feeByTypes);
            }

        }
        return outstandingFee;

    }
}