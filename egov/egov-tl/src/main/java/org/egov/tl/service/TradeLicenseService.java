/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.tl.service;

import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.*;
import org.egov.tl.entity.contracts.DemandNoticeForm;
import org.egov.tl.entity.contracts.OnlineSearchForm;
import org.egov.tl.entity.contracts.SearchForm;
import org.egov.tl.repository.LicenseDocumentTypeRepository;
import org.egov.tl.repository.LicenseRepository;
import org.egov.tl.repository.SearchTradeRepository;
import org.egov.tl.repository.specs.SearchTradeSpec;
import org.egov.tl.service.es.LicenseApplicationIndexService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseNumberUtils;
import org.egov.tl.utils.LicenseUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static org.apache.commons.lang.StringEscapeUtils.escapeXml;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.demand.utils.DemandConstants.DEMAND_REASON_CATEGORY_FEE;
import static org.egov.demand.utils.DemandConstants.DEMAND_REASON_CATEGORY_PENALTY;
import static org.egov.infra.config.core.ApplicationThreadLocals.getMunicipalityName;
import static org.egov.infra.reporting.engine.ReportFormat.PDF;
import static org.egov.infra.reporting.util.ReportUtil.CONTENT_TYPES;
import static org.egov.infra.utils.ApplicationConstant.N;
import static org.egov.infra.utils.ApplicationConstant.NA;
import static org.egov.infra.utils.DateUtils.currentDateToDefaultDateFormat;
import static org.egov.infra.utils.DateUtils.getDefaultFormattedDate;
import static org.egov.infra.utils.DateUtils.toYearFormat;
import static org.egov.infra.utils.FileUtils.addFilesToZip;
import static org.egov.infra.utils.FileUtils.byteArrayToFile;
import static org.egov.infra.utils.FileUtils.toByteArray;
import static org.egov.infra.utils.StringUtils.append;
import static org.egov.tl.utils.Constants.*;
import static org.hibernate.criterion.MatchMode.ANYWHERE;

@Service("tradeLicenseService")
@Transactional(readOnly = true)
public class TradeLicenseService {

    private static final String ARREAR = "arrear";
    private static final String CURRENT = "current";
    private static final String PENALTY = "penalty";
    private static final String ERROR_WF_INITIATOR_NOT_DEFINED = "error.wf.initiator.not.defined";
    private static final String ERROR_MSG_NO_ASSIGNMENT = "No officials assigned to process this application";

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected InstallmentHibDao installmentDao;

    @Autowired
    protected LicenseNumberUtils licenseNumberUtils;

    @Autowired
    protected LicenseDocumentTypeService licenseDocumentTypeService;

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

    @Autowired
    @Qualifier("tradeLicenseWorkflowService")
    protected SimpleWorkflowService<TradeLicense> licenseWorkflowService;

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
    protected EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    protected DesignationService designationService;

    @Autowired
    protected LicenseConfigurationService licenseConfigurationService;

    @Autowired
    protected TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;
    @Autowired
    protected LicenseUtils licenseUtils;
    @Autowired
    private PenaltyRatesService penaltyRatesService;

    @Autowired
    private SubCategoryDetailsService subCategoryDetailsService;

    @Autowired
    private FeeTypeService feeTypeService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private SearchTradeRepository searchTradeRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    private EisCommonService eisCommonService;

    public TradeLicense getLicenseById(Long id) {
        return this.licenseRepository.findOne(id);
    }

    public void raiseNewDemand(TradeLicense license) {
        Module moduleName = licenseUtils.getModule();
        Date currentDate = new Date();
        Date commencementDate = license.getCommencementDate();
        Installment installment = this.installmentDao.getInsatllmentByModuleForGivenDate(moduleName, commencementDate);
        EgDemand demand = new EgDemand();
        demand.setIsHistory(N.toString());
        demand.setEgInstallmentMaster(installment);
        demand.setCreateDate(currentDate);
        demand.setModifiedDate(currentDate);
        List<FeeMatrixDetail> feeMatrixDetails = feeMatrixService.getLicenseFeeDetails(license, commencementDate);
        for (FeeMatrixDetail feeMatrixDetail : feeMatrixDetails) {
            EgDemandReasonMaster reasonMaster = demandGenericDao
                    .getDemandReasonMasterByCode(feeMatrixDetail.getFeeMatrix().getFeeType().getName(), moduleName);
            EgDemandReason demandReason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, installment,
                    moduleName);
            if (demandReason != null) {
                BigDecimal tradeAmt = calculateFeeByRateType(license, feeMatrixDetail);
                demand.getEgDemandDetails().add(EgDemandDetails.fromReasonAndAmounts(tradeAmt, demandReason, ZERO));
            }
        }
        applyPenalty(license, demand);
        license.setDemand(demand);
        license.recalculateBaseDemand();
    }

    private BigDecimal calculateFeeByRateType(TradeLicense license, FeeMatrixDetail feeMatrixDetail) {
        List<LicenseSubCategoryDetails> licenseSubCategoryDetails = subCategoryDetailsService
                .getSubcategoryDetailBySubcategoryAndFeeType(license.getTradeName(), feeTypeService.findAll());
        BigDecimal licenseFee = ZERO;
        for (LicenseSubCategoryDetails subCategoryDetail : licenseSubCategoryDetails) {
            switch (subCategoryDetail.getRateType()) {
            case FLAT_BY_RANGE:
                licenseFee = licenseFee.add(feeMatrixDetail.getAmount());
                break;
            case PERCENTAGE:
                licenseFee = licenseFee.add(license.getTradeArea_weight()
                        .multiply(feeMatrixDetail.getAmount())
                        .divide(BigDecimal.valueOf(100)));
                break;
            case UNIT_BY_RANGE:
                licenseFee = licenseFee.add(license.getTradeArea_weight()
                        .multiply(feeMatrixDetail.getAmount()));
                break;
            }
        }
        return licenseFee;
    }

    public TradeLicense updateDemandForTradeAreaChange(TradeLicense license) {
        EgDemand licenseDemand = license.getCurrentDemand();
        List<EgDemandDetails> demandDetails = licenseDemand.getEgDemandDetails().stream()
                .filter(egDemandDetails -> licenseDemand.getEgInstallmentMaster()
                        .equals(egDemandDetails.getEgDemandReason().getEgInstallmentMaster()))
                .collect(Collectors.toList());
        BigDecimal latestFeePaid = demandDetails.stream()
                .filter(egDemandDetails -> DEMAND_REASON_CATEGORY_FEE.equals(egDemandDetails.getReasonCategory()))
                .map(EgDemandDetails::getAmtCollected)
                .reduce(ZERO, BigDecimal::add);

        Date licenseDate = license.isNewApplication() ? license.getCommencementDate()
                : license.getDemand().getEgInstallmentMaster().getFromDate();
        List<FeeMatrixDetail> feeMatrixDetails = this.feeMatrixService.getLicenseFeeDetails(license, licenseDate);
        BigDecimal totalAmount = ZERO;
        for (FeeMatrixDetail feeMatrixDetail : feeMatrixDetails) {
            BigDecimal tradeAmt = calculateFeeByRateType(license, feeMatrixDetail);
            totalAmount = totalAmount.add(tradeAmt);
        }

        if (totalAmount.compareTo(latestFeePaid) > 0) {
            recalculateDemand(feeMatrixDetails, license);
        } else {
            Date currentDate = new Date();
            for (EgDemandDetails demandDetail : demandDetails) {
                demandDetail.setAmount(demandDetail.getAmtCollected());
                demandDetail.setModifiedDate(currentDate);
            }
            license.recalculateBaseDemand();
        }
        return license;
    }

    public void applyPenalty(TradeLicense license, EgDemand demand) {
        Map<Installment, EgDemandDetails> penaltyDemandDetails = getInstallmentWiseDemandDetails(demand,
                DEMAND_REASON_CATEGORY_PENALTY);
        Map<Installment, EgDemandDetails> licenseDemandDetails = getInstallmentWiseDemandDetails(demand,
                DEMAND_REASON_CATEGORY_FEE);
        for (Map.Entry<Installment, BigDecimal> penalty : calculatePenalty(license, demand).entrySet()) {
            EgDemandDetails penaltyDemandDetail = penaltyDemandDetails.get(penalty.getKey());
            EgDemandDetails licenseDemandDetail = licenseDemandDetails.get(penalty.getKey());
            if (penaltyDemandDetail != null && penalty.getValue().compareTo(penaltyDemandDetail.getAmtCollected()) >= 0) {
                penaltyDemandDetail.setAmount(penalty.getValue().setScale(0, HALF_UP));
            } else if (penaltyDemandDetail == null && penalty.getValue().signum() > 0
                    && licenseDemandDetail.getBalance().signum() > 0) {
                penaltyDemandDetail = getPenaltyDemandDetail(penalty.getKey(), penalty.getValue().setScale(0, HALF_UP));
                if (penaltyDemandDetail != null)
                    demand.getEgDemandDetails().add(penaltyDemandDetail);
            }
        }
    }

    private Map<Installment, EgDemandDetails> getInstallmentWiseDemandDetails(EgDemand currentDemand, String reasonCategory) {
        Map<Installment, EgDemandDetails> installmentwiseDemandDetails = new TreeMap<>();
        for (EgDemandDetails demandDetails : currentDemand.getEgDemandDetails())
            if (reasonCategory.equals(demandDetails.getReasonCategory()))
                installmentwiseDemandDetails.put(demandDetails.getEgDemandReason().getEgInstallmentMaster(), demandDetails);

        return installmentwiseDemandDetails;
    }

    private Map<Installment, BigDecimal> calculatePenalty(TradeLicense license, EgDemand demand) {
        boolean isNewApplication = license.isNewApplication();
        Date currentDate = new Date();
        Map<Installment, BigDecimal> installmentPenalty = new HashMap<>();
        for (EgDemandDetails demandDetails : demand.getEgDemandDetails()) {
            if (!DEMAND_REASON_CATEGORY_PENALTY.equals(demandDetails.getReasonCategory())
                    && demandDetails.getBalance().signum() == 1) {
                Date licenseDate = isNewApplication ? license.getCommencementDate()
                        : demandDetails.getEgDemandReason().getEgInstallmentMaster().getFromDate();
                installmentPenalty.put(demandDetails.getEgDemandReason().getEgInstallmentMaster(),
                        penaltyRatesService.calculatePenalty(license, licenseDate, currentDate, demandDetails.getAmount()));
            }
        }
        return installmentPenalty;
    }

    private EgDemandDetails getPenaltyDemandDetail(Installment inst, BigDecimal penaltyAmount) {
        EgDemandDetails demandDetail = null;
        if (penaltyAmount != null && penaltyAmount.compareTo(ZERO) > 0) {
            Module module = licenseUtils.getModule();
            EgDemandReasonMaster demandReasonMaster = demandGenericDao.getDemandReasonMasterByCode(PENALTY_DMD_REASON_CODE,
                    module);
            if (demandReasonMaster == null)
                throw new ApplicationRuntimeException("Penalty demand reason master is null in method insertPenalty");

            EgDemandReason demandReason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                    demandReasonMaster, inst, module);
            if (demandReason == null)
                throw new ApplicationRuntimeException("Penalty demand reason is null in method  insertPenalty");

            demandDetail = EgDemandDetails.fromReasonAndAmounts(penaltyAmount, demandReason, ZERO);
        }
        return demandDetail;
    }

    public void recalculateDemand(List<FeeMatrixDetail> feeMatrixDetails, TradeLicense license) {
        EgDemand demand = license.getCurrentDemand();
        Date currentDate = new Date();
        // Recalculating current demand detail according to fee matrix
        for (EgDemandDetails demandDetail : demand.getEgDemandDetails())
            for (FeeMatrixDetail feeMatrixDetail : feeMatrixDetails)
                if (demand.getEgInstallmentMaster().equals(demandDetail.getEgDemandReason().getEgInstallmentMaster()) &&
                        demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(feeMatrixDetail.getFeeMatrix().getFeeType().getName())) {
                    BigDecimal tradeAmount = calculateFeeByRateType(license, feeMatrixDetail);
                    demandDetail.setAmount(tradeAmount.setScale(0, HALF_UP));
                    demandDetail.setModifiedDate(currentDate);
                }
        applyPenalty(license, demand);
        license.recalculateBaseDemand();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void raiseDemand(Long licenseId, Module module, Installment installment) {
        // Refetching license in this txn to avoid lazy initialization issue
        TradeLicense license = licenseRepository.findOne(licenseId);
        Map<EgDemandReason, EgDemandDetails> reasonWiseDemandDetails = new HashMap<>();
        for (EgDemandDetails demandDetail : license.getDemand().getEgDemandDetails())
            if (DEMAND_REASON_CATEGORY_FEE.equals(demandDetail.getReasonCategory()))
                reasonWiseDemandDetails.put(demandDetail.getEgDemandReason(), demandDetail);
        LicenseAppType originalAppType = license.getLicenseAppType();
        LicenseAppType renewAppType = licenseAppTypeService.getLicenseAppTypeByCode(RENEW_APPTYPE_CODE);
        license.setLicenseAppType(renewAppType);
        for (FeeMatrixDetail feeMatrixDetail : feeMatrixService.getLicenseFeeDetails(license, installment.getFromDate())) {
            String feeType = feeMatrixDetail.getFeeMatrix().getFeeType().getName();
            EgDemandReason demandReason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                    demandGenericDao.getDemandReasonMasterByCode(feeType, module), installment, module);
            if (demandReason == null)
                throw new ValidationException("TL-007", "Demand Reason missing for " + feeType);
            EgDemandDetails licenseDemandDetail = reasonWiseDemandDetails.get(demandReason);
            BigDecimal tradeAmt = calculateFeeByRateType(license, feeMatrixDetail);
            if (licenseDemandDetail == null)
                license.getDemand().getEgDemandDetails().add(EgDemandDetails.fromReasonAndAmounts(tradeAmt, demandReason, ZERO));
            else if (licenseDemandDetail.getBalance().compareTo(ZERO) != 0)
                licenseDemandDetail.setAmount(tradeAmt);
            if (license.getCurrentDemand().getEgInstallmentMaster().getInstallmentYear().before(installment.getInstallmentYear()))
                license.getDemand().setEgInstallmentMaster(installment);

        }
        license.recalculateBaseDemand();
        if (originalAppType == null || originalAppType.getCode().equals(NEW_APPTYPE_CODE))
            license.setLicenseAppType(renewAppType);
        else
            license.setLicenseAppType(originalAppType);
        licenseRepository.save(license);
    }

    public void transitionWorkFlow(TradeLicense license, WorkflowBean workflowBean) {
        DateTime currentDate = new DateTime();
        User user = this.securityUtils.getCurrentUser();
        String senderName = user.getUsername() + DELIMITER_COLON + user.getName();
        if (BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            Position initiatorPosition = license.getCurrentState().getInitiatorPosition();
            List<Position> userPositions = positionMasterService.getPositionsForEmployee(securityUtils.getCurrentUser().getId());
            if (userPositions.contains(initiatorPosition) && ("Rejected".equals(license.getState().getValue())
                    || "License Created".equals(license.getState().getValue())))
                license.transition().end()
                        .withSenderName(senderName)
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                license.transition().progressWithStateCopy()
                        .withSenderName(senderName)
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(WORKFLOW_STATE_REJECTED)
                        .withDateInfo(currentDate.toDate())
                        .withOwner(initiatorPosition)
                        .withNextAction(WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING);
            }

        } else if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                    null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
            license.transition().end()
                    .withSenderName(senderName)
                    .withComments(workflowBean.getApproverComments())
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                    .withOwner(license.getCurrentState().getInitiatorPosition())
                    .withNextAction(wfmatrix.getNextAction());
        } else {
            if (!license.hasState()) {
                Position wfInitiator = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(user.getId())
                        .stream().findFirst()
                        .orElseThrow(() -> new ValidationException(ERROR_WF_INITIATOR_NOT_DEFINED, ERROR_MSG_NO_ASSIGNMENT))
                        .getPosition();
                WorkFlowMatrix wfmatrix = getWorkFlowMatrixApi(license, workflowBean);
                license.transition().start()
                        .withSLA(licenseUtils.getSlaForAppType(license.getLicenseAppType()))
                        .withSenderName(senderName)
                        .withComments(workflowBean.getApproverComments())
                        .withNatureOfTask(license.getLicenseAppType().getName())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(wfInitiator)
                        .withNextAction(wfmatrix.getNextAction()).withInitiator(wfInitiator);
                license.setEgwStatus(
                        egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
            } else if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                Position commissioner = getCommissionerPosition();
                if (APPLICATION_STATUS_APPROVED_CODE.equals(license.getEgwStatus().getCode())) {
                    if (licenseConfigurationService.digitalSignEnabled())
                        license.transition().progressWithStateCopy()
                                .withSenderName(senderName)
                                .withComments(workflowBean.getApproverComments())
                                .withStateValue(WF_ACTION_DIGI_SIGN_COMMISSION_NO_COLLECTION)
                                .withDateInfo(currentDate.toDate())
                                .withOwner(commissioner)
                                .withNextAction(WF_ACTION_DIGI_PENDING);
                    else
                        license.transition().progressWithStateCopy()
                                .withSenderName(senderName)
                                .withComments(workflowBean.getApproverComments())
                                .withStateValue(WF_COMMISSIONER_APPRVD_WITHOUT_COLLECTION)
                                .withDateInfo(currentDate.toDate())
                                .withOwner(license.getCurrentState().getInitiatorPosition())
                                .withNextAction(WF_CERTIFICATE_GEN_PENDING);
                } else if (APPLICATION_STATUS_SECONDCOLLECTION_CODE.equals(license.getEgwStatus().getCode())) {
                    WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                            null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
                    license.transition().progressWithStateCopy()
                            .withSenderName(senderName)
                            .withComments(workflowBean.getApproverComments())
                            .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                            .withOwner(commissioner)
                            .withNextAction(wfmatrix.getNextAction());
                }

            } else {
                Position pos = null;
                if (workflowBean.getApproverPositionId() != null && workflowBean.getApproverPositionId() > 0)
                    pos = positionMasterService.getPositionById(workflowBean.getApproverPositionId());
                WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
                license.transition().progressWithStateCopy()
                        .withSenderName(senderName)
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate())
                        .withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }

        }
    }

    public Position getCommissionerPosition() {
        return positionMasterService.getPositionsForEmployee(securityUtils.getCurrentUser().getId())
                .stream()
                .filter(position -> position.getDeptDesig().getDesignation().getName().equals(COMMISSIONER_DESGN))
                .findFirst()
                .orElseThrow(
                        () -> new ValidationException("error.wf.comm.pos.not.found", "You are not authorized approve this application"));
    }

    public WorkFlowMatrix getWorkFlowMatrixApi(TradeLicense license, WorkflowBean workflowBean) {
        return this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
    }

    public void processAndStoreDocument(TradeLicense license) {
        Date currentDate = new Date();
        license.getDocuments().forEach(document -> {
            document.setType(licenseDocumentTypeRepository.findOne(document.getType().getId()));
            if (!(document.getUploads().isEmpty() || document.getUploadsFileName().isEmpty())) {
                int fileCount = 0;
                for (File file : document.getUploads()) {
                    FileStoreMapper fileStore = this.fileStoreService.store(file,
                            document.getUploadsFileName().get(fileCount),
                            document.getUploadsContentType().get(fileCount++), TL_FILE_STORE_DIR);
                    document.getFiles().add(fileStore);
                }
                document.setEnclosed(true);
                document.setDocDate(currentDate);
            } else if (document.getType().isMandatory() && document.getFiles().isEmpty() && document.getId() == null) {
                document.getFiles().clear();
                throw new ValidationException("TL-004", "TL-004", document.getType().getName());
            }
            document.setLicense(license);
        });
    }

    public List<NatureOfBusiness> getAllNatureOfBusinesses() {
        return natureOfBusinessService.getNatureOfBusinesses();
    }

    @SuppressWarnings("unused")
    public TradeLicense getLicenseByLicenseNumber(String licenseNumber) {
        return this.licenseRepository.findByLicenseNumber(licenseNumber);
    }

    public TradeLicense getLicenseByApplicationNumber(String applicationNumber) {
        return this.licenseRepository.findByApplicationNumber(applicationNumber);
    }

    public Map<String, Map<String, BigDecimal>> getOutstandingFee(TradeLicense license) {
        Map<String, Map<String, BigDecimal>> outstandingFee = new HashMap<>();
        EgDemand egDemand = license.getCurrentDemand();
        for (EgDemandDetails demandDetail : egDemand.getEgDemandDetails()) {
            String demandReason = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
            Installment installmentYear = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            Map<String, BigDecimal> feeByTypes;
            if (outstandingFee.containsKey(demandReason))
                feeByTypes = outstandingFee.get(demandReason);
            else {
                feeByTypes = new HashMap<>();
                feeByTypes.put(ARREAR, ZERO);
                feeByTypes.put(CURRENT, ZERO);
            }
            BigDecimal demandAmount = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
            if (installmentYear.equals(egDemand.getEgInstallmentMaster()))
                feeByTypes.put(CURRENT, demandAmount);
            else
                feeByTypes.put(ARREAR, feeByTypes.get(ARREAR).add(demandAmount));
            outstandingFee.put(demandReason, feeByTypes);
        }
        return outstandingFee;

    }

    public Map<String, BigDecimal> getOutstandingFeeForDemandNotice(TradeLicense license,
            Installment currentInstallment, Installment previousInstallment) {

        EgDemand egDemand = license.getCurrentDemand();
        // 31st december will be considered as cutoff date for penalty calculation.
        Date endDateOfPreviousFinancialYear = new DateTime(previousInstallment.getFromDate()).withMonthOfYear(12)
                .withDayOfMonth(31).toDate();
        Map<String, BigDecimal> feeByTypes = new HashMap<>();
        feeByTypes.put(ARREAR, ZERO);
        feeByTypes.put(CURRENT, ZERO);
        feeByTypes.put(PENALTY, ZERO);
        for (EgDemandDetails demandDetail : egDemand.getEgDemandDetails()) {
            Installment installmentYear = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            if (!DEMAND_REASON_CATEGORY_PENALTY.equalsIgnoreCase(demandDetail.getReasonCategory())) {
                BigDecimal demandAmount = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
                if (demandAmount.compareTo(BigDecimal.valueOf(0)) > 0)
                    if (installmentYear.equals(currentInstallment))
                        feeByTypes.put(CURRENT, feeByTypes.get(CURRENT).add(demandAmount));
                    else {
                        feeByTypes.put(ARREAR, feeByTypes.get(ARREAR).add(demandAmount));
                        // Calculate penalty by passing installment startdate and end of dec 31st date of previous installment
                        // dates using penalty master.
                        BigDecimal penaltyAmt = penaltyRatesService.calculatePenalty(license, installmentYear.getFromDate(),
                                endDateOfPreviousFinancialYear, demandAmount);
                        feeByTypes.put(PENALTY, feeByTypes.get(PENALTY).add(penaltyAmt));
                    }
            }
        }
        return feeByTypes;
    }

    public List<Long> getLicenseIdsForDemandGeneration(CFinancialYear financialYear) {
        Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(licenseUtils.getModule(),
                financialYear.getStartingDate());
        return licenseRepository.findLicenseIdsForDemandGeneration(installment.getFromDate());
    }

    public Boolean currentUserIsMeeseva() {
        return securityUtils.getCurrentUser().hasRole(MEESEVAOPERATOR);
    }

    @Transactional
    public void digitalSignTransition(String applicationNumber) {
        User user = securityUtils.getCurrentUser();
        if (isNotBlank(applicationNumber)) {
            TradeLicense license = licenseRepository.findByApplicationNumber(applicationNumber);
            DateTime currentDate = new DateTime();
            license.setEgwStatus(egwStatusHibernateDAO
                    .getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_APPROVED_CODE));
            license.transition().progressWithStateCopy()
                    .withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(WF_DIGI_SIGNED)
                    .withStateValue(WF_DIGI_SIGNED)
                    .withDateInfo(currentDate.toDate())
                    .withOwner(license.getCurrentState().getInitiatorPosition())
                    .withNextAction(EMPTY);
            license.setCertificateFileId(license.getDigiSignedCertFileStoreId());
            licenseRepository.save(license);
            tradeLicenseSmsAndEmailService.sendSMsAndEmailOnDigitalSign(license);
            licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
        }

    }

    @Transactional
    public void save(TradeLicense license) {
        updateDemandForTradeAreaChange(license);
        processAndStoreDocument(license);
        licenseRepository.save(license);
    }

    @Transactional
    public void updateTradeLicense(TradeLicense license, WorkflowBean workflowBean) {
        processAndStoreDocument(license);
        licenseRepository.save(license);
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(license, workflowBean.getWorkFlowAction());
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
    }

    public void updateStatusInWorkFlowProgress(TradeLicense license, String workFlowAction) {

        List<Position> userPositions = positionMasterService.getPositionsForEmployee(securityUtils.getCurrentUser().getId());
        if (BUTTONAPPROVE.equals(workFlowAction)) {
            if (isEmpty(license.getLicenseNumber()) && license.isNewApplication())
                license.setLicenseNumber(licenseNumberUtils.generateLicenseNumber());

            if (license.getCurrentDemand().getBaseDemand().compareTo(license.getCurrentDemand().getAmtCollected()) <= 0)
                license.setEgwStatus(
                        egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_APPROVED_CODE));
            else
                license.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE,
                        APPLICATION_STATUS_SECONDCOLLECTION_CODE));
            generateAndStoreCertificate(license);

        }
        if (BUTTONAPPROVE.equals(workFlowAction) || BUTTONFORWARD.equals(workFlowAction)) {
            license.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_UNDERWORKFLOW));
            if (license.getState().getValue().equals(WF_REVENUECLERK_APPROVED))
                license.setEgwStatus(
                        egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_INSPE_CODE));
            else if (license.getState().getValue().equals(WORKFLOW_STATE_REJECTED))
                license.setEgwStatus(
                        egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
        }

        if (GENERATECERTIFICATE.equals(workFlowAction)) {
            license.setActive(true);
            license.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_ACTIVE));
            // setting license to non-legacy, old license number will be the only tracking
            // to check a license created as legacy or new hereafter.
            license.setLegacy(false);
            validityService.applyLicenseValidity(license);
            license.setEgwStatus(
                    egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_GENECERT_CODE));
        }
        if (BUTTONREJECT.equals(workFlowAction))
            if (license.getLicenseAppType() != null && userPositions.contains(license.getCurrentState().getInitiatorPosition())
                    && ("Rejected".equals(license.getState().getValue()))
                    || "License Created".equals(license.getState().getValue())) {
                license.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_CANCELLED));
                license.setEgwStatus(
                        egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CANCELLED));
                if (license.isNewApplication())
                    license.setActive(false);
            } else {
                license.setStatus(licenseStatusService.getLicenseStatusByCode(STATUS_REJECTED));
                license.setEgwStatus(
                        egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_REJECTED));
            }
        if (license.hasState() && license.getState().getValue().contains(WF_REVENUECLERK_APPROVED))
            updateDemandForTradeAreaChange(license);

    }

    public ReportOutput generateLicenseCertificate(TradeLicense license, boolean isProvisional) {
        String reportTemplate;
        if (CITY_GRADE_CORPORATION.equals(cityService.getCityGrade()))
            reportTemplate = "tl_licenseCertificateForCorp";
        else
            reportTemplate = "tl_licenseCertificate";
        ReportOutput reportOutput = reportService.createReport(new ReportRequest(reportTemplate, license,
                getReportParamsForCertificate(license, isProvisional)));
        reportOutput.setReportName(license.generateCertificateFileName());
        return reportOutput;
    }

    private Map<String, Object> getReportParamsForCertificate(TradeLicense license, boolean isProvisional) {

        Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("applicationnumber", license.getApplicationNumber());
        reportParams.put("applicantName", license.getLicensee().getApplicantName());
        reportParams.put("licencenumber", license.getLicenseNumber());
        reportParams.put("wardName", license.getBoundary().getName());
        reportParams.put("cscNumber", "");
        reportParams.put("nameOfEstablishment", escapeXml(license.getNameOfEstablishment()));
        reportParams.put("licenceAddress", escapeXml(license.getAddress()));
        reportParams.put("municipality", cityService.getMunicipalityName());
        reportParams.put("district", cityService.getDistrictName());
        reportParams.put("category", escapeXml(license.getCategory().getName()));
        reportParams.put("subCategory", escapeXml(license.getTradeName().getName()));
        reportParams.put("appType", license.isNewApplication() ? "New Trade" : "Renewal");
        reportParams.put("currentDate", currentDateToDefaultDateFormat());
        reportParams.put("carporationulbType", getMunicipalityName().contains("Corporation"));
        EgDemandDetails demandDetails = license.getCurrentDemand().getEgDemandDetails().stream()
                .sorted(Comparator.comparing(EgDemandDetails::getInstallmentEndDate).reversed())
                .filter(demandDetail -> demandDetail.getReasonCategory().equals(DEMAND_REASON_CATEGORY_FEE)
                        && demandDetail.getAmtCollected().doubleValue() > 0)
                .findFirst()
                .orElseThrow(() -> new ValidationException("License Fee is not paid", "License Fee is not paid"));
        String installmentYear = toYearFormat(demandDetails.getInstallmentStartDate()) + "-" +
                toYearFormat(demandDetails.getInstallmentEndDate());
        reportParams.put("installMentYear", installmentYear);
        reportParams.put("applicationdate", getDefaultFormattedDate(license.getApplicationDate()));
        Date receiptDate = licenseRepository.getReceiptDateByLicenseNumber(license.getLicenseNumber());
        reportParams.put("demandUpdateDate", getDefaultFormattedDate(receiptDate));
        reportParams.put("demandTotalamt", demandDetails.getAmtCollected());
        User approver = isProvisional || license.getApprovedBy() == null
                ? licenseUtils.getCommissionerAssignment().getEmployee() : license.getApprovedBy();
        ByteArrayInputStream commissionerSign = new ByteArrayInputStream(
                approver == null || approver.getSignature() == null ? new byte[0] : approver.getSignature());
        reportParams.put("commissionerSign", commissionerSign);

        if (isProvisional)
            reportParams.put("certificateType", "provisional");
        else
            reportParams.put("qrCode", license.qrCode(installmentYear, demandDetails.getAmtCollected()));

        return reportParams;
    }

    @ReadOnly
    public List<String> getTradeLicenseForGivenParam(String paramValue, String paramType) {
        List<String> licenseList = new ArrayList<>();
        if (isNotBlank(paramValue) && isNotBlank(paramType)) {
            if (SEARCH_BY_APPNO.equals(paramType))
                licenseList = licenseRepository.findAllApplicationNumberLike(paramValue);

            else if (SEARCH_BY_LICENSENO.equals(paramType))
                licenseList = licenseRepository.findAllLicenseNumberLike(paramValue);

            else if (SEARCH_BY_OLDLICENSENO.equals(paramType))
                licenseList = licenseRepository.findAllOldLicenseNumberLike(paramValue);

            else if (SEARCH_BY_TRADETITLE.equals(paramType))
                licenseList = licenseRepository.findAllNameOfEstablishmentLike(paramValue);

            else if (SEARCH_BY_TRADEOWNERNAME.equals(paramType))
                licenseList = licenseRepository.findAllApplicantNameLike(paramValue);

            else if (SEARCH_BY_PROPERTYASSESSMENTNO.equals(paramType))
                licenseList = licenseRepository.findAllAssessmentNoLike(paramValue);

            else if (SEARCH_BY_MOBILENO.equals(paramType))
                licenseList = licenseRepository.findAllMobilePhoneNumberLike(paramValue);
        }

        return licenseList;
    }

    @ReadOnly
    public Page<SearchForm> searchTradeLicense(SearchForm searchForm) {
        Pageable pageable = new PageRequest(searchForm.pageNumber(),
                searchForm.pageSize(), searchForm.orderDir(), searchForm.orderBy());
        User currentUser = securityUtils.getCurrentUser();
        Page<TradeLicense> licenses = searchTradeRepository.findAll(SearchTradeSpec.searchTrade(searchForm), pageable);
        List<SearchForm> searchResults = new ArrayList<>();
        String[] feeCollectorRoles = licenseConfigurationService.getFeeCollectorRoles();
        licenses.forEach(license -> searchResults
                .add(new SearchForm(license, currentUser, getProcessOwnerName(license), feeCollectorRoles)));
        return new PageImpl<>(searchResults, pageable, licenses.getTotalElements());
    }

    @ReadOnly
    public List<OnlineSearchForm> onlineSearchTradeLicense(OnlineSearchForm searchForm) {
        Criteria searchCriteria = entityManager.unwrap(Session.class).createCriteria(TradeLicense.class);
        searchCriteria.createAlias("licensee", "licc").createAlias("category", "cat")
                .createAlias("tradeName", "subcat").createAlias("status", "licstatus");
        if (isNotBlank(searchForm.getApplicationNumber()))
            searchCriteria.add(Restrictions.eq("applicationNumber", searchForm.getApplicationNumber()).ignoreCase());
        if (isNotBlank(searchForm.getLicenseNumber()))
            searchCriteria.add(Restrictions.eq(LICENSE_NUMBER, searchForm.getLicenseNumber()).ignoreCase());
        if (isNotBlank(searchForm.getMobileNo()))
            searchCriteria.add(Restrictions.eq("licc.mobilePhoneNumber", searchForm.getMobileNo()));
        if (isNotBlank(searchForm.getTradeOwnerName()))
            searchCriteria.add(Restrictions.like("licc.applicantName", searchForm.getTradeOwnerName(), ANYWHERE));

        searchCriteria.add(Restrictions.isNotNull("applicationNumber"));
        searchCriteria.addOrder(Order.asc("id"));
        List<OnlineSearchForm> searchResult = new ArrayList<>();
        for (TradeLicense license : (List<TradeLicense>) searchCriteria.list()) {
            if (license != null)
                searchResult.add(new OnlineSearchForm(license, getDemandColl(license)));
        }
        return searchResult;
    }

    public BigDecimal[] getDemandColl(TradeLicense license) {
        BigDecimal[] dmdColl = new BigDecimal[3];
        Arrays.fill(dmdColl, ZERO);
        Installment latestInstallment = this.installmentDao.getInsatllmentByModuleForGivenDate(licenseUtils.getModule(),
                new DateTime().withMonthOfYear(4).withDayOfMonth(1).toDate());
        license.getCurrentDemand().getEgDemandDetails().stream().forEach(egDemandDetails -> {
            if (latestInstallment.equals(egDemandDetails.getEgDemandReason().getEgInstallmentMaster())) {
                dmdColl[1] = dmdColl[1].add(egDemandDetails.getAmount());
                dmdColl[2] = dmdColl[2].add(egDemandDetails.getAmtCollected());
            } else {
                dmdColl[0] = dmdColl[0].add(egDemandDetails.getAmount());
                dmdColl[2] = dmdColl[2].add(egDemandDetails.getAmtCollected());
            }
        });
        return dmdColl;
    }

    public List<DemandNoticeForm> getLicenseDemandNotices(DemandNoticeForm demandNoticeForm) {
        Criteria searchCriteria = entityManager.unwrap(Session.class).createCriteria(TradeLicense.class);
        searchCriteria.createAlias("licensee", "licc").createAlias("category", "cat")
                .createAlias("tradeName", "subcat").createAlias("natureOfBusiness", "nob")
                .createAlias("demand", "licDemand").createAlias("licenseAppType", "appType")
                .add(Restrictions.ne("appType.code", CLOSURE_APPTYPE_CODE));
        if (isNotBlank(demandNoticeForm.getLicenseNumber()))
            searchCriteria.add(Restrictions.eq(LICENSE_NUMBER, demandNoticeForm.getLicenseNumber()).ignoreCase());
        if (isNotBlank(demandNoticeForm.getOldLicenseNumber()))
            searchCriteria
                    .add(Restrictions.eq("oldLicenseNumber", demandNoticeForm.getOldLicenseNumber()).ignoreCase());
        if (demandNoticeForm.getCategoryId() != null)
            searchCriteria.add(Restrictions.eq("cat.id", demandNoticeForm.getCategoryId()));
        if (demandNoticeForm.getSubCategoryId() != null)
            searchCriteria.add(Restrictions.eq("subcat.id", demandNoticeForm.getSubCategoryId()));
        if (demandNoticeForm.getWardId() != null)
            searchCriteria.createAlias("parentBoundary", "wards")
                    .add(Restrictions.eq("wards.id", demandNoticeForm.getWardId()));
        if (demandNoticeForm.getElectionWard() != null)
            searchCriteria.createAlias("adminWard", "electionWard")
                    .add(Restrictions.eq("electionWard.id", demandNoticeForm.getElectionWard()));
        if (demandNoticeForm.getLocalityId() != null)
            searchCriteria.createAlias("boundary", "locality")
                    .add(Restrictions.eq("locality.id", demandNoticeForm.getLocalityId()));
        searchCriteria
                .add(Restrictions.eq("isActive", true))
                .add(Restrictions.eq("nob.name", PERMANENT_NATUREOFBUSINESS))
                .add(Restrictions.gtProperty("licDemand.baseDemand", "licDemand.amtCollected"))
                .addOrder(Order.asc("id"));
        List<DemandNoticeForm> demandNotices = new LinkedList<>();
        Module tradeLicenseModule = licenseUtils.getModule();
        for (TradeLicense license : (List<TradeLicense>) searchCriteria.list()) {
            EgDemand egDemand = license.getCurrentDemand();
            if (egDemand != null) {
                Installment currentInstallment = egDemand.getEgInstallmentMaster();
                List<Installment> previousInstallment = installmentDao
                        .fetchPreviousInstallmentsInDescendingOrderByModuleAndDate(tradeLicenseModule,
                                currentInstallment.getToDate(), 1);
                Map<String, BigDecimal> licenseFees = getOutstandingFeeForDemandNotice(license,
                        currentInstallment, previousInstallment.get(0));
                demandNotices.add(new DemandNoticeForm(license, licenseFees, getProcessOwnerName(license)));
            }
        }
        return demandNotices;
    }

    public String getProcessOwnerName(TradeLicense license) {
        String ownerName = NA;
        if (license.getState() != null && license.currentAssignee() != null) {
            List<Assignment> assignmentList = assignmentService
                    .getAssignmentsForPosition(license.currentAssignee().getId(), new Date());
            if (!assignmentList.isEmpty())
                ownerName = assignmentList.get(0).getEmployee().getName();
            ownerName = format("%s [%s]", ownerName, license.currentAssignee().getName());
        }
        return ownerName;

    }

    public List<HashMap<String, Object>> populateHistory(TradeLicense tradeLicense) {
        List<HashMap<String, Object>> processHistoryDetails = new ArrayList<>();
        if (tradeLicense.hasState()) {
            State<Position> currentState = tradeLicense.getCurrentState();
            processHistoryDetails.add(constructHistory(currentState));
            currentState.getHistory().stream()
                    .sorted(Comparator.comparing(StateHistory<Position>::getLastModifiedDate).reversed())
                    .forEach(historyState -> processHistoryDetails.add(constructHistory(historyState.asState())));
        }
        return processHistoryDetails;
    }

    private HashMap<String, Object> constructHistory(State<Position> state) {
        HashMap<String, Object> processHistory = new HashMap<>();
        processHistory.put("date", state.getLastModifiedDate());
        processHistory.put("updatedBy", state.getSenderName().contains(DELIMITER_COLON)
                ? state.getSenderName().split(DELIMITER_COLON)[1] : state.getSenderName());
        processHistory.put("status", state.isEnded() ? "Completed" : state.getValue());
        processHistory.put("comments", defaultString(state.getComments()));
        Position ownerPosition = state.getOwnerPosition();
        User ownerUser = state.getOwnerUser();
        if (ownerPosition == null) {
            processHistory.put("user", ownerUser == null ? NA : ownerUser.getName());
        } else {
            User userPos = eisCommonService.getUserForPosition(ownerPosition.getId(), state.getLastModifiedDate());
            processHistory.put("user", userPos == null ? NA : userPos.getName());
        }
        return processHistory;
    }

    @ReadOnly
    public List<TradeLicense> getLicenses(Example license) {
        return licenseRepository.findAll(license);
    }

    public List<BillReceipt> getReceipts(TradeLicense license) {
        return demandGenericDao.getBillReceipts(license.getCurrentDemand());
    }

    public LicenseDocumentType getLicenseDocumentType(Long id) {
        return licenseDocumentTypeRepository.findOne(id);
    }

    public Map<String, Map<String, List<LicenseDocument>>> getAttachedDocument(Long licenseId) {

        List<LicenseDocument> licenseDocuments = getLicenseById(licenseId).getDocuments();
        Map<String, Map<String, List<LicenseDocument>>> licenseDocumentDetails = new HashMap<>();
        licenseDocumentDetails.put(NEW_APPTYPE_CODE, new HashMap<>());
        licenseDocumentDetails.put(RENEW_APPTYPE_CODE, new HashMap<>());
        licenseDocumentDetails.put(CLOSURE_APPTYPE_CODE, new HashMap<>());

        for (LicenseDocument document : licenseDocuments) {
            String docType = document.getType().getName();
            String appType = document.getType().getApplicationType().getCode();

            if (licenseDocumentDetails.get(appType).containsKey(docType)) {
                licenseDocumentDetails.get(appType).get(docType).add(document);
            } else {
                List<LicenseDocument> documents = new ArrayList<>();
                documents.add(document);
                licenseDocumentDetails.get(appType).put(docType, documents);
            }
        }
        return licenseDocumentDetails;
    }

    public ReportOutput generateAcknowledgment(String uid) {
        TradeLicense license = getLicenseByUID(uid);
        Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("amount", license.getTotalBalance());
        ReportRequest reportRequest = new ReportRequest("tl_license_acknowledgment", license, reportParams);
        reportRequest.setReportFormat(ReportFormat.PDF);
        ReportOutput reportOutput = reportService.createReport(reportRequest);
        reportOutput.setReportName(append("license_ack_", license.getApplicationNumber()));
        return reportOutput;
    }

    @ReadOnly
    public ReportOutput generateClosureNotice(String reportFormat) {
        ReportOutput reportOutput = new ReportOutput();
        Map<String, Object> reportParams = new HashMap<>();
        List<TradeLicense> licenses = searchTradeRepository.findLicenseClosureByCurrentInstallmentYear(new Date());
        if (licenses.isEmpty()) {
            reportOutput.setReportName("tl_closure_notice");
            reportOutput.setReportFormat(ReportFormat.PDF);
            reportOutput.setReportOutputData("No Data".getBytes());
        } else {
            reportParams.put("License", licenses);
            reportParams.put("corp", cityService.getCityGrade());
            reportParams.put("currentDate", currentDateToDefaultDateFormat());
            reportParams.put("municipality", cityService.getMunicipalityName());
            reportOutput = reportService.createReport(
                    new ReportRequest("tl_closure_notice", licenses, reportParams));
        }
        if ("zip".equalsIgnoreCase(reportFormat))
            reportOutput.setReportOutputData(toByteArray(addFilesToZip(byteArrayToFile(reportOutput.getReportOutputData(),
                    "tl_closure_notice_", ".pdf").toFile())));
        return reportOutput;
    }

    public void generateAndStoreCertificate(TradeLicense license) {
        FileStoreMapper fileStore = fileStoreService.store(generateLicenseCertificate(license, false).getReportOutputData(),
                license.generateCertificateFileName() + ".pdf", CONTENT_TYPES.get(PDF), TL_FILE_STORE_DIR);
        license.setCertificateFileId(fileStore.getFileStoreId());
    }

    public TradeLicense getLicenseByUID(String uid) {
        return licenseRepository.findByUid(uid);
    }

    public TradeLicense getLicenseByDemand(EgDemand demand) {
        return licenseRepository.findByDemand(demand);
    }

    public Map<String, String> getLicenseDetailsByLicenseNumberAndStatus(String licenseNumber, String status) {
        TradeLicense tradeLicense = this.licenseRepository.findByLicenseNumberAndStatusName(licenseNumber, status);
        HashMap<String, String> licenseDetails = new HashMap<>();
        licenseDetails.put(LICENSE_NUMBER, tradeLicense.getLicenseNumber());
        licenseDetails.put("tradeTitle", tradeLicense.getNameOfEstablishment());
        licenseDetails.put("tradeAddress", tradeLicense.getAddress());
        licenseDetails.put("applicantName", tradeLicense.getLicensee().getApplicantName());
        licenseDetails.put("applicantAddress", tradeLicense.getLicensee().getAddress());
        return licenseDetails;
    }
    
}