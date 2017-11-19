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

package org.egov.stms.transactions.service;

import static org.egov.stms.utils.constants.SewerageTaxConstants.FEES_ADVANCE_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULE_NAME;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.stms.entity.PaySewerageTaxDetails;
import org.egov.stms.entity.SewerageDemandGenerationLog;
import org.egov.stms.entity.SewerageTaxDetails;
import org.egov.stms.entity.SewerageTaxPaidDetails;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.masters.entity.enums.SewerageProcessStatus;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.transactions.entity.SewerageDemandConnection;
import org.egov.stms.transactions.entity.SewerageDemandDetail;
import org.egov.stms.transactions.repository.SewerageDemandGenerationLogRepository;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Transactional(readOnly = true)
public class SewerageDemandService {

    private static final Logger LOGGER = Logger.getLogger(SewerageDemandService.class);
    private static final String SUCCESSFUL = "Successful";
    private final List<EgDemandDetails> detailList = new ArrayList<>();
    @Autowired
    protected SewerageApplicationDetailsService sewerageApplicationDetailsService;
    @Autowired
    private InstallmentDao installmentDao;
    @Autowired
    private DemandGenericDao demandGenericDao;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private ModuleService moduleService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private SewerageDemandGenerationLogService stDemandGenerationLogService;
    @Autowired
    private SewerageDemandGenerationLogRepository demandGenerationLogRepository;
    @Autowired
    private SewerageTaxUtils sewerageTaxUtils; 
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    
    @SuppressWarnings("rawtypes")
    @Autowired
    private InstallmentHibDao installmentHibDao;


    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * @param demandReason
     * @param installment
     * @return
     */
    public EgDemandReason getDemandReasonByCodeAndInstallment(final String demandReason, final Integer installment) {
        final Query demandQuery = getCurrentSession().getNamedQuery("DEMANDREASONBY_CODE_AND_INSTALLMENTID");
        demandQuery.setParameter(0, demandReason);
        demandQuery.setParameter(1, installment);
        return (EgDemandReason) demandQuery.uniqueResult();
    }

    /**
     * @param demandDetailSet
     * @param installment
     * @param totalDemandAmount
     * @return
     */
    private EgDemand createDemand(final Set<EgDemandDetails> demandDetailSet, final Installment installment,
            final BigDecimal totalDemandAmount) {
        final EgDemand egDemand = new EgDemand();
        egDemand.setEgInstallmentMaster(installment);
        egDemand.getEgDemandDetails().addAll(demandDetailSet);
        egDemand.setIsHistory("N");
        egDemand.setCreateDate(new Date());
        egDemand.setBaseDemand(totalDemandAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
        egDemand.setModifiedDate(new Date());
        return egDemand;
    }

    /**
     * @return
     */
    public Installment getCurrentInstallment() {
        return installmentDao.getInsatllmentByModuleForGivenDate(
                moduleService.getModuleByName(MODULE_NAME), new Date());

    }

    public Installment getNextInstallment() {

        final Installment currentInstlalment = getCurrentInstallment();
        if (currentInstlalment != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentInstlalment.getToDate());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            return installmentDao.getInsatllmentByModuleForGivenDate(
                    moduleService.getModuleByName(MODULE_NAME), calendar.getTime());
        }
        return null;
    }

    public Installment getInstallmentByDescription(final String description) {
        return installmentDao.getInsatllmentByModuleAndDescription(
                moduleService.getModuleByName(MODULE_NAME), description);

    }

    public Installment getInsatllmentByModuleForGivenDate(final Date installmentDate) {
        return installmentDao.getInsatllmentByModuleForGivenDate(
                moduleService.getModuleByName(MODULE_NAME), installmentDate);

    }

    public List<Installment> getPreviousInstallment(final Date curentInstalmentEndate) {
        return installmentDao.fetchPreviousInstallmentsInDescendingOrderByModuleAndDate(
                moduleService.getModuleByName(MODULE_NAME), curentInstalmentEndate, 1);

    }

    /**
     * @param demand
     * @param dmdAmount
     * @param egDemandReason
     * @param amtCollected
     * @return
     */
    public EgDemandDetails createDemandDetails(final EgDemand demand, final BigDecimal dmdAmount,
            final EgDemandReason egDemandReason,
            final BigDecimal amtCollected) {
        final EgDemandDetails demandDetail = EgDemandDetails.fromReasonAndAmounts(dmdAmount.setScale(0, BigDecimal.ROUND_HALF_UP),
                egDemandReason,
                amtCollected);
        if (demandDetail != null) {
            demandDetail.setEgDemand(demand);
            return demandDetail;
        }

        return null;
    }

    public EgDemandDetails createDemandDetails(final BigDecimal dmdAmount, final EgDemandReason egDemandReason,
            final BigDecimal amtCollected) {
        return EgDemandDetails.fromReasonAndAmounts(dmdAmount.setScale(0, BigDecimal.ROUND_HALF_UP), egDemandReason,
                amtCollected);
    }

    /**
     * @param sewerageConnection
     * @return
     */
    public Boolean checkAnyTaxIsPendingToCollect(final EgDemand demand) {
        Boolean pendingTaxCollection = false;

        if (demand != null)
            for (final EgDemandDetails demandDtl : demand.getEgDemandDetails())
                if (demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0) {
                    pendingTaxCollection = true;
                    break;

                }

        return pendingTaxCollection;

    }

    public BigDecimal checkForPendingTaxAmountToCollect(final EgDemand demand) {
        BigDecimal pendingTaxCollection = BigDecimal.ZERO;

        if (demand != null)
            for (final EgDemandDetails demandDtl : demand.getEgDemandDetails())
                if (!demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode().equals(FEES_ADVANCE_CODE)
                        && demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0)
                    pendingTaxCollection = pendingTaxCollection.add(demandDtl.getAmount().subtract(demandDtl.getAmtCollected()));
        return pendingTaxCollection;

    }

    public Boolean checkAnyTaxIsPendingToCollectExcludingAdvance(final EgDemand demand) {
        Boolean taxPendingForCollection = false;

        if (demand != null)
            for (final EgDemandDetails demandDtl : demand.getEgDemandDetails())
                if (!demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equalsIgnoreCase(FEES_ADVANCE_CODE)
                        && demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0) {
                    taxPendingForCollection = true;
                    break;

                }

        return taxPendingForCollection;

    }

    /**
     * Check any tax pay pending for selected advertisement in selected installment
     *
     * @param advertisement
     * @param installment
     * @return
     */
    public Boolean checkAnyTaxPendingForSelectedFinancialYear(final SewerageDemandConnection sewerageDemandConnection,
            final Installment installment) {
        Boolean pendingTaxCollection = false;

        if (sewerageDemandConnection != null && sewerageDemandConnection.getDemand() != null)
            for (final EgDemandDetails demandDtl : sewerageDemandConnection.getDemand().getEgDemandDetails())
                if (demandDtl.getEgDemandReason().getEgInstallmentMaster().getId().equals(installment.getId())
                        && demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0) {
                    pendingTaxCollection = true;
                    break;

                }

        return pendingTaxCollection;

    }

    /**
     * @param sewerageConnection
     * @return
     */
    public boolean anyDemandPendingForCollection(final SewerageApplicationDetails sewerageApplicationDetails) {
        return checkAnyTaxIsPendingToCollect(sewerageApplicationDetails.getCurrentDemand());
    }

    public List<EgDemandDetails> getDemandDetailByPassingDemandDemandReason(final EgDemand demand,
            final EgDemandReason demandReason) {

        return demandGenericDao.getDemandDetailsForDemandAndReasons(demand, Arrays.asList(demandReason));

    }

    public List<BillReceipt> getBilReceiptsByDemand(final EgDemand demand) {
        List<BillReceipt> billReceiptList;
        billReceiptList = demandGenericDao.getBillReceipts(demand);
        return billReceiptList;
    }

    public EgDemand createDemandOnNewConnection(final List<SewerageConnectionFee> connectionFees,
            final SewerageApplicationDetails sewerageApplicationDetail) {
        EgDemand demand = null;
        final Set<EgDemandDetails> demandDetailSet = new HashSet<>();
        BigDecimal totalDemandAmount = BigDecimal.ZERO;
        if (sewerageApplicationDetail != null && sewerageApplicationDetail.getCurrentDemand() == null) {
            final Installment installment = getCurrentInstallment();

            for (final SewerageConnectionFee fees : connectionFees) {
                final EgDemandReason pendingTaxReason = getDemandReasonByCodeAndInstallment(fees.getFeesDetail()
                        .getCode(), installment.getId()); // TODO: CHECK CURRENT
                // INSTALLMENT
                // REQUIRED?
                if (pendingTaxReason != null) {
                    demandDetailSet.add(createDemandDetails(BigDecimal.valueOf(fees.getAmount()), pendingTaxReason,
                            BigDecimal.ZERO));
                    totalDemandAmount = totalDemandAmount.add(BigDecimal.valueOf(fees.getAmount()));
                } else
                    throw new ApplicationRuntimeException("SEWERAGE.001");
            }

            demand = createDemand(demandDetailSet, installment, totalDemandAmount);
        }

        return demand;

    }

    /**
     * @param sewerageDemandDetail
     * @param sewerageApplicationDetail
     * @return
     * @throws ApplicationRuntimeException
     */
    public EgDemand createDemandOnLegacyConnection(final List<SewerageDemandDetail> sewerageDemandDetail,
            final SewerageApplicationDetails sewerageApplicationDetail) {

        EgDemand demand = null;
        final Set<EgDemandDetails> demandDetailSet = new HashSet<>();
        BigDecimal totalDemandAmount = BigDecimal.ZERO;
        BigDecimal totalCollectedAmount = BigDecimal.ZERO;
        if (sewerageApplicationDetail != null && sewerageApplicationDetail.getCurrentDemand() == null) {
            final List<Installment> installmentList = sewerageTaxUtils
                    .getInstallmentsByModuledescendingorder(moduleService.getModuleByName(MODULE_NAME), new DateTime().getYear());
            for (final SewerageDemandDetail sdd : sewerageDemandDetail) {
                final EgDemandReason pendingTaxReason = getDemandReasonByCodeAndInstallment(sdd.getReasonMaster(),
                        sdd.getInstallmentId());
                if (pendingTaxReason != null) {
                    if (sdd.getActualAmount() == null)
                        sdd.setActualAmount(BigDecimal.ZERO);
                    if (sdd.getActualCollection() == null)
                        sdd.setActualCollection(BigDecimal.ZERO);
                    demandDetailSet.add(createDemandDetails(sdd.getActualAmount(), pendingTaxReason,
                            sdd.getActualCollection()));
                    totalDemandAmount = totalDemandAmount.add(sdd.getActualAmount());
                    totalCollectedAmount = totalCollectedAmount.add(sdd.getActualCollection());
                } else
                    throw new ApplicationRuntimeException("SEWERAGE.001");
            }

            demand = createDemand(demandDetailSet, installmentList.get(0), totalDemandAmount);
            demand.setAmtCollected(totalCollectedAmount);
        }
        return demand;
    }

    /***
     * Assumption: There is no fee collected for the selected demand.
     * @param sewerageDemandDetail
     * @param demand
     * @return
     */
    public EgDemand updateLegacyDemand(final List<SewerageDemandDetail> sewerageDemandDetail, final EgDemand demand) {
        BigDecimal totalDemandAmount = BigDecimal.ZERO;
        BigDecimal totalCollectedAmount = BigDecimal.ZERO;
        final List<EgDemandDetails> removableDemandDetailList = new ArrayList<>();
        boolean demandDtlPresent;
        for (final SewerageDemandDetail sdd : sewerageDemandDetail) {
            demandDtlPresent = false;
            if (sdd.getActualAmount() == null)
                sdd.setActualAmount(BigDecimal.ZERO);
            if (sdd.getActualCollection() == null)
                sdd.setActualCollection(BigDecimal.ZERO);

            for (final EgDemandDetails dmdDtl : demand.getEgDemandDetails())
                if (sdd.getReasonMaster()
                        .equalsIgnoreCase(dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode())
                        && sdd.getInstallmentId().equals(dmdDtl.getEgDemandReason().getEgInstallmentMaster().getId())) {
                    demandDtlPresent = true;
                    dmdDtl.setAmount(sdd.getActualAmount());
                    dmdDtl.setAmtCollected(sdd.getActualCollection());
                    totalDemandAmount = totalDemandAmount.add(sdd.getActualAmount());
                    totalCollectedAmount = totalCollectedAmount.add(sdd.getActualCollection());

                }
            if (!demandDtlPresent) {
                final EgDemandReason pendingTaxReason = getDemandReasonByCodeAndInstallment(sdd.getReasonMaster(),
                        sdd.getInstallmentId());

                demand.addEgDemandDetails(createDemandDetails(sdd.getActualAmount(), pendingTaxReason,
                        sdd.getActualCollection()));
                totalDemandAmount = totalDemandAmount.add(sdd.getActualAmount());
                totalCollectedAmount = totalCollectedAmount.add(sdd.getActualCollection());
            }
        }
        for (final EgDemandDetails dmdDtls : demand.getEgDemandDetails()) {
            demandDtlPresent = false;
            for (final SewerageDemandDetail sewDmdDtl : sewerageDemandDetail)
                if (sewDmdDtl.getReasonMaster()
                        .equalsIgnoreCase(dmdDtls.getEgDemandReason().getEgDemandReasonMaster().getCode())
                        && sewDmdDtl.getInstallmentId().equals(dmdDtls.getEgDemandReason().getEgInstallmentMaster().getId()))
                    demandDtlPresent = true;

            if (!demandDtlPresent)
                removableDemandDetailList.add(dmdDtls);
        }
        for (final EgDemandDetails removableDmdDtl : removableDemandDetailList)
            demand.removeEgDemandDetails(removableDmdDtl);

        demand.setBaseDemand(totalDemandAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
        demand.setAmtCollected(totalCollectedAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
        return demand;

    }

    /**
     * @param connectionFees
     * @param demand
     * @return
     */
    public EgDemand updateDemand(final List<SewerageConnectionFee> connectionFees, final EgDemand demand) {
        final Installment installment = getCurrentInstallment();
        BigDecimal totalDemandAmount = BigDecimal.ZERO;
        boolean demandDtlPresent;
        for (final SewerageConnectionFee scf : connectionFees) {
            demandDtlPresent = false; // used to decide over adding demandDetail
            // required or not
            for (final EgDemandDetails dmdDtl : demand.getEgDemandDetails())
                if (scf.getFeesDetail().getCode()
                        .equalsIgnoreCase(dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode())) {
                    demandDtlPresent = true;

                    totalDemandAmount = totalDemandAmount.subtract(dmdDtl.getAmount());
                    dmdDtl.setAmount(BigDecimal.valueOf(scf.getAmount()));
                    totalDemandAmount = totalDemandAmount.add(BigDecimal.valueOf(scf.getAmount()));
                }
            if (!demandDtlPresent) {// TODO: IF AMOUNT EQUAL TO ZERO CHECK
                // REQUIRED ?
                demand.addEgDemandDetails(createDemandDetails(BigDecimal.valueOf(scf.getAmount()),
                        getDemandReasonByCodeAndInstallment(scf.getFeesDetail().getCode(), installment.getId()),
                        BigDecimal.ZERO));
                totalDemandAmount = totalDemandAmount.add(BigDecimal.valueOf(scf.getAmount()));
            }
        }
        demand.addBaseDemand(totalDemandAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
        return demand;

    }

    public EgDemand updateDemandOnChangeInClosets(final SewerageApplicationDetails oldSewerageApplicationDetails,
            final List<SewerageConnectionFee> connectionFees, final EgDemand demand,
            final Boolean updateOldSewerageApplicationAdvance) {
        final Installment installment = getCurrentInstallment();
        BigDecimal totalDemandAmount = BigDecimal.ZERO;// (demand.getBaseDemand()!=null?demand.getBaseDemand(): BigDecimal.ZERO);
        // //TODO : CHECK THIS VARIABLE.
        BigDecimal oldDonationCharge = BigDecimal.ZERO;
        BigDecimal oldSewerageTax = BigDecimal.ZERO;
        BigDecimal oldApplicationAdvanceAmount = BigDecimal.ZERO;
        Boolean oldAdvanceUsedInSewerageTaxOrAddedAsAdvance = false;
        BigDecimal currentDonationCharge = BigDecimal.ZERO;
        BigDecimal currentSewerageTax = BigDecimal.ZERO;

        boolean demandDtlPresent = false;
        if (oldSewerageApplicationDetails != null) {
            for (final SewerageConnectionFee oldSewerageConnectionFee : oldSewerageApplicationDetails.getConnectionFees()) {
                if (oldSewerageConnectionFee.getFeesDetail().getCode()
                        .equalsIgnoreCase(SewerageTaxConstants.FEES_SEWERAGETAX_CODE))
                    oldSewerageTax = oldSewerageTax.add(BigDecimal.valueOf(oldSewerageConnectionFee.getAmount()));
                if (oldSewerageConnectionFee.getFeesDetail().getCode()
                        .equalsIgnoreCase(SewerageTaxConstants.FEES_DONATIONCHARGE_CODE))
                    oldDonationCharge = oldDonationCharge.add(BigDecimal.valueOf(oldSewerageConnectionFee.getAmount()));
            }

            // Check any advance amount present in old sewerage application. If yes, use advance amount and reset advance amount
            // as zero. This amount will be either
            // carry forward in new application demand or will adjust with current application sewerage tax.
            // On record approval only, adjust advance amount from earlier sewerage record.
            if (oldSewerageApplicationDetails.getCurrentDemand() != null && updateOldSewerageApplicationAdvance)
                for (final EgDemandDetails dmdDtl : oldSewerageApplicationDetails.getCurrentDemand().getEgDemandDetails())
                    if (dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(FEES_ADVANCE_CODE)) {
                        oldApplicationAdvanceAmount = oldApplicationAdvanceAmount.add(dmdDtl.getAmtCollected());
                        dmdDtl.setAmount(BigDecimal.ZERO);
                        dmdDtl.setAmtCollected(BigDecimal.ZERO);
                    }
        }

        if (demand != null)
            for (final EgDemandDetails dmdDtl : demand.getEgDemandDetails())
                if (dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equalsIgnoreCase(FEES_ADVANCE_CODE))
                    oldApplicationAdvanceAmount = oldApplicationAdvanceAmount.add(dmdDtl.getAmtCollected());
        // If current demand updated, then use advance amount in sewerage tax payment.

        for (final SewerageConnectionFee scf : connectionFees) {

            if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_SEWERAGETAX_CODE))
                currentSewerageTax = currentSewerageTax.add(BigDecimal.valueOf(scf.getAmount()));
            if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_DONATIONCHARGE_CODE))
                currentDonationCharge = currentDonationCharge.add(BigDecimal.valueOf(scf.getAmount()));
        }

        // Compare previous tax deposit amount and sewerage tax.
        // if donation amount is less then previous detail , then do not create donation amount, else difference amount will be
        // added.
        // get sewerage tax of current tax, if amount paid, check if amount paid more, then adjust as advance.
        // if not paid, correct current tax.
        // if sewerage tax more, then add difference amount to current installment.

        // Get deposit amount,establishment charge and sewerage tax as current year tax. Update collected amount.
        for (final SewerageConnectionFee scf : connectionFees) {

            for (final EgDemandDetails dmdDtl : demand.getEgDemandDetails())
                if (scf.getFeesDetail().getCode()
                        .equalsIgnoreCase(dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode())
                        && !scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEE_INSPECTIONCHARGE)) {

                    demandDtlPresent = true;

                    if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_DONATIONCHARGE_CODE)) {
                        // If donation chanrge more than earlier installment, then collect amount in this installment
                        if (currentDonationCharge.compareTo(oldDonationCharge) > 0) {

                            totalDemandAmount = totalDemandAmount.subtract(dmdDtl.getAmount().subtract(dmdDtl.getAmtCollected()));
                            dmdDtl.setAmount(currentDonationCharge);
                            dmdDtl.setAmtCollected(oldDonationCharge);
                            totalDemandAmount = totalDemandAmount.add(currentDonationCharge.subtract(oldDonationCharge));

                        }
                    } else if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_SEWERAGETAX_CODE)) {
                        // Mean tax increased and diffence amount will be collected in this installment.
                        if (currentSewerageTax.compareTo(oldSewerageTax) > 0) {
                            totalDemandAmount = totalDemandAmount.subtract(dmdDtl.getAmount().subtract(dmdDtl.getAmtCollected()));
                            dmdDtl.setAmount(currentSewerageTax);

                            final BigDecimal differenceAmount = currentSewerageTax.subtract(oldSewerageTax);
                            if (oldApplicationAdvanceAmount.compareTo(BigDecimal.ZERO) > 0) {
                                // Eg: 500 diff, 100 advance present. adjust 100
                                // as collected.
                                if (differenceAmount.compareTo(oldApplicationAdvanceAmount) > 0) {
                                    dmdDtl.setAmtCollected(oldSewerageTax.add(oldApplicationAdvanceAmount));
                                    oldApplicationAdvanceAmount = BigDecimal.ZERO;
                                    oldAdvanceUsedInSewerageTaxOrAddedAsAdvance = true;
                                    createAdvanceDemandDetail(demand, oldApplicationAdvanceAmount);// reset advance as zero.
                                } else { // Eg: 500 diff, 600 advance present.
                                    // adjust 500 as collected.
                                    dmdDtl.setAmtCollected(oldSewerageTax.add(differenceAmount));
                                    oldApplicationAdvanceAmount = oldApplicationAdvanceAmount
                                            .subtract(differenceAmount);
                                    // Add remaining amount as advance.
                                    createAdvanceDemandDetail(demand, oldApplicationAdvanceAmount);
                                    oldAdvanceUsedInSewerageTaxOrAddedAsAdvance = true;
                                }

                            } else {

                                dmdDtl.setAmtCollected(oldSewerageTax);
                                totalDemandAmount = totalDemandAmount.add(differenceAmount);

                            }
                            // check sewerage tax, we need to adjust ? save remaining amount as advance for current demand.
                            // if taxgetCurrentDemand is less and advance also present.. then add amount into current demand..
                        } else if (oldSewerageTax.compareTo(BigDecimal.ZERO) > 0)// Mean, paid more amount in old installment.
                        // Difference amount will be used as next
                        // installment tax.
                        {
                            totalDemandAmount = totalDemandAmount.subtract(dmdDtl.getAmount().subtract(dmdDtl.getAmtCollected()));
                            dmdDtl.setAmount(currentSewerageTax);
                            dmdDtl.setAmtCollected(currentSewerageTax);
                            totalDemandAmount = totalDemandAmount.add(currentSewerageTax);

                            // Add remaining advance as current demand advance.
                            if (oldSewerageTax.compareTo(currentSewerageTax) > 0
                                    || oldApplicationAdvanceAmount.compareTo(BigDecimal.ZERO) > 0) {
                                // Add amount to advance of next installment if not present in this transaction.

                                // TODO: CHECK WHETHER WE NEED TO UPDATE ADVANCE OR ADD AS FRESH ONCE?
                                createAdvanceDemandDetail(demand,
                                        oldSewerageTax.subtract(currentSewerageTax).add(oldApplicationAdvanceAmount));
                                oldAdvanceUsedInSewerageTaxOrAddedAsAdvance = true;

                            }
                        }

                    } else {
                        totalDemandAmount = totalDemandAmount.subtract(dmdDtl.getAmount());
                        dmdDtl.setAmount(BigDecimal.valueOf(scf.getAmount()));
                        totalDemandAmount = totalDemandAmount.add(BigDecimal.valueOf(scf.getAmount()));

                    }
                }

            if (!demandDtlPresent && scf.getAmount() > 0
                    && !scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEE_INSPECTIONCHARGE))
                if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_DONATIONCHARGE_CODE)) {
                    if (currentDonationCharge.compareTo(oldDonationCharge) > 0) {
                        demand.addEgDemandDetails(createDemandDetails(currentDonationCharge,
                                getDemandReasonByCodeAndInstallment(scf.getFeesDetail().getCode(), installment.getId()),
                                oldDonationCharge));
                        totalDemandAmount = totalDemandAmount.add(currentDonationCharge.subtract(oldDonationCharge));
                    }
                } else if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_SEWERAGETAX_CODE)) {
                    // Mean tax increased and diffence amount will be collected in this installment.
                    if (currentSewerageTax.compareTo(oldSewerageTax) > 0) {

                        final BigDecimal differenceAmount = currentSewerageTax.subtract(oldSewerageTax);
                        BigDecimal amoountCollected = oldSewerageTax;
                        if (oldApplicationAdvanceAmount.compareTo(BigDecimal.ZERO) > 0)
                            // Eg: 500 diff, 100 advance present. adjust 100
                            // as collected.
                            if (differenceAmount.compareTo(oldApplicationAdvanceAmount) > 0) {
                            amoountCollected = oldSewerageTax.add(oldApplicationAdvanceAmount);
                            oldApplicationAdvanceAmount = BigDecimal.ZERO;
                            oldAdvanceUsedInSewerageTaxOrAddedAsAdvance = true;
                            createAdvanceDemandDetail(demand, oldApplicationAdvanceAmount);// reset advance as zero.
                            } else {
                            // Eg: 500 diff, 600 advance present.
                            // adjust 500 as collected.
                            amoountCollected = oldSewerageTax.add(differenceAmount);
                            oldApplicationAdvanceAmount = oldApplicationAdvanceAmount
                                    .subtract(differenceAmount);
                            // Add remaining amount as advance.
                            createAdvanceDemandDetail(demand, oldApplicationAdvanceAmount);
                            oldAdvanceUsedInSewerageTaxOrAddedAsAdvance = true;
                            }

                        demand.addEgDemandDetails(createDemandDetails(currentSewerageTax,
                                getDemandReasonByCodeAndInstallment(scf.getFeesDetail().getCode(), installment.getId()),
                                amoountCollected));
                        totalDemandAmount = totalDemandAmount.add(currentSewerageTax.subtract(amoountCollected));

                    } else if (oldSewerageTax.compareTo(BigDecimal.ZERO) > 0 && oldSewerageTax.compareTo(currentSewerageTax) >= 0
                            || oldApplicationAdvanceAmount.compareTo(BigDecimal.ZERO) > 0) {
                        demand.addEgDemandDetails(createDemandDetails(currentSewerageTax,
                                getDemandReasonByCodeAndInstallment(scf.getFeesDetail().getCode(), installment.getId()),
                                currentSewerageTax));
                        // Add amount to advance of next installment if not present in this transaction.
                        createAdvanceDemandDetail(demand,
                                oldSewerageTax.subtract(currentSewerageTax).add(oldApplicationAdvanceAmount));
                        totalDemandAmount = totalDemandAmount.add(currentSewerageTax);
                        oldAdvanceUsedInSewerageTaxOrAddedAsAdvance = true;
                    }

                } else {
                    demand.addEgDemandDetails(createDemandDetails(BigDecimal.valueOf(scf.getAmount()),
                            getDemandReasonByCodeAndInstallment(scf.getFeesDetail().getCode(), installment.getId()),
                            BigDecimal.ZERO));
                    totalDemandAmount = totalDemandAmount.add(BigDecimal.valueOf(scf.getAmount()));
                }
        }
        if (updateOldSewerageApplicationAdvance && !detailList.isEmpty())
            demand.addEgDemandDetails(detailList.get(0));
        // Copy Advance from previous demand and add to current demand(iff there is no adjustment happened in sewerage tax
        if (!oldAdvanceUsedInSewerageTaxOrAddedAsAdvance && oldApplicationAdvanceAmount.compareTo(BigDecimal.ZERO) > 0)
            createAdvanceDemandDetail(demand, oldApplicationAdvanceAmount);
        demand.addBaseDemand(totalDemandAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
        return demand;

    }

    private void createAdvanceDemandDetail(final EgDemand demand, final BigDecimal amount) {

        // TODO : CHECK WHETHER APPLICATION IN SAME INSTALLMENT OR DIFFERENT ?

        // get next installment
        final Installment nextInstallment = getNextInstallment();
        Boolean advancePresent = false;
        for (final EgDemandDetails dmdDtl : demand.getEgDemandDetails())
            if (dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                    .equalsIgnoreCase(FEES_ADVANCE_CODE)
                    && nextInstallment != null && nextInstallment.getDescription()
                            .equalsIgnoreCase(dmdDtl.getEgDemandReason().getEgInstallmentMaster().getDescription())) {
                dmdDtl.getEgDemand().getBaseDemand().subtract(dmdDtl.getAmtCollected());
                dmdDtl.setAmtCollected(amount);
                dmdDtl.getEgDemand().getBaseDemand().add(amount);
                advancePresent = true;
            }
        if (!advancePresent) {
            detailList.clear();
            detailList.add(createDemandDetails(demand, BigDecimal.ZERO,
                    getDemandReasonByCodeAndInstallment(FEES_ADVANCE_CODE, nextInstallment.getId()),
                    amount));
        }
    }

    /**
     * @param applicationDetails
     * @param oldtaxReasonInstallment
     * @param newtaxReasonInstallment
     * @return
     */
    public EgDemand generateNextYearDemandForSewerage(final SewerageApplicationDetails applicationDetails,
            final EgDemandReason oldtaxReasonInstallment,
            final EgDemandReason newtaxReasonInstallment) {

        BigDecimal totalDemandAmount = BigDecimal.ZERO;
        final EgDemand demand = applicationDetails.getCurrentDemand();

        Boolean taxFeeAlreadyExistInDemand = false;
        EgDemandDetails oldTaxDemandDetail = null;

        for (final EgDemandDetails dmdDtl : demand.getEgDemandDetails()) {
            // Assumption: tax amount is mandatory.
            if (dmdDtl.getEgDemandReason().getId() == oldtaxReasonInstallment.getId())
                oldTaxDemandDetail = dmdDtl;

            if (dmdDtl.getEgDemandReason().getId() == newtaxReasonInstallment.getId())
                taxFeeAlreadyExistInDemand = true;
        }
        // Copy last financial year sewerage tax
        // if sewerage tax already present in new installment, then
        // we are not updating.

        if (!taxFeeAlreadyExistInDemand && oldTaxDemandDetail != null) {
            demand.addEgDemandDetails(createDemandDetails(oldTaxDemandDetail.getAmount(), newtaxReasonInstallment,
                    BigDecimal.ZERO));
            totalDemandAmount = totalDemandAmount.add(oldTaxDemandDetail.getAmount());
        }
        demand.setEgInstallmentMaster(newtaxReasonInstallment.getEgInstallmentMaster());
        demand.addBaseDemand(totalDemandAmount.setScale(0, BigDecimal.ROUND_HALF_UP));

        return demand;
    }

    public Integer[] generateDemandForNextInstallment(final List<SewerageApplicationDetails> sewerageApplicationDetails,
            final List<Installment> previousInstallment, final Installment sewerageDmdGenerationInstallment) {
        Integer[] res;
        int totalNoOfRecords = 0;
        int noOfSuccessRecords = 0;
        int noOfFailureRecords = 0;
        if (LOGGER.isInfoEnabled())
            LOGGER.info("*************************************** total records " + sewerageApplicationDetails.size());
        if (!sewerageApplicationDetails.isEmpty()) {
            final EgDemandReason taxReasonOldInstallment = getDemandReasonByCodeAndInstallment(
                    SewerageTaxConstants.FEES_SEWERAGETAX_CODE, previousInstallment.get(0).getId());

            final EgDemandReason taxReasonNewInstallment = getDemandReasonByCodeAndInstallment(
                    SewerageTaxConstants.FEES_SEWERAGETAX_CODE, sewerageDmdGenerationInstallment.getId());

            SewerageDemandGenerationLog demandGenerationLog;
            demandGenerationLog = transactionTemplate.execute(result -> {
                return stDemandGenerationLogService
                        .createDemandGenerationLog(sewerageDmdGenerationInstallment.getDescription());
            });

            for (final SewerageApplicationDetails applicationDetails : sewerageApplicationDetails) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(
                            "*************************************** demand id " + applicationDetails.getCurrentDemand().getId());
                Boolean status = false;

                // get last year demand and add as current year.

                try {
                    status = transactionTemplate.execute(result -> {
                        final SewerageDemandGenerationLog demandGenerationLogObj;
                        if (LOGGER.isInfoEnabled())
                            LOGGER.info("SHSC Number ---> " + applicationDetails.getConnection().getShscNumber());
                        generateNextYearDemandForSewerage(applicationDetails, taxReasonOldInstallment,
                                taxReasonNewInstallment);
                        sewerageApplicationDetailsService.updateSewerageApplicationDetails(applicationDetails);

                        if (demandGenerationLog != null) {
                            demandGenerationLogObj = demandGenerationLogRepository.findOne(demandGenerationLog.getId());
                            stDemandGenerationLogService.createOrGetDemandGenerationLogDetail(demandGenerationLogObj,
                                    applicationDetails, SewerageProcessStatus.COMPLETED, SUCCESSFUL);
                        }

                        return Boolean.TRUE;
                    });
                } catch (final Exception e) {
                    status = transactionTemplate.execute(result -> {
                        final SewerageDemandGenerationLog demandGenerationLogObj;
                        if (demandGenerationLog != null) {
                            demandGenerationLogObj = demandGenerationLogRepository.findOne(demandGenerationLog.getId());
                            demandGenerationLogObj.setDemandGenerationStatus(SewerageProcessStatus.INCOMPLETE);
                            stDemandGenerationLogService.createOrGetDemandGenerationLogDetail(demandGenerationLogObj,
                                    applicationDetails, SewerageProcessStatus.INCOMPLETE, getErrorMessage(e));
                        }
                        LOGGER.error("Error in generating demand bill for SHSC Number ---> "
                                + applicationDetails.getConnection().getShscNumber()
                                + " and executeJob" + e);
                        return Boolean.FALSE;
                    });
                }
                noOfSuccessRecords = status ? noOfSuccessRecords + 1 : noOfSuccessRecords;
                noOfFailureRecords = !status ? noOfFailureRecords + 1 : noOfFailureRecords;
                totalNoOfRecords = noOfSuccessRecords + noOfFailureRecords;
            }
        }
        res = new Integer[] { totalNoOfRecords, noOfSuccessRecords, noOfFailureRecords };
        return res;
    }

    private String getErrorMessage(final Exception exception) {
        String error;
        if (exception instanceof ValidationException)
            error = ((ValidationException) exception).getErrors().get(0).getMessage();
        else
            error = "Error : " + exception;
        return error;
    }

    public SewerageTaxDetails getSeweragTaxeDemandDetails(PaySewerageTaxDetails paySewerageTaxDetails) {
        SewerageTaxDetails sewerageTaxDetails = new SewerageTaxDetails();
        SewerageApplicationDetails sewerageApplicationDetails = null;
        ErrorDetails errorDetails = null;
        if (paySewerageTaxDetails.getApplicaionNumber() != null && !"".equals(paySewerageTaxDetails.getApplicaionNumber()))
            sewerageApplicationDetails = sewerageApplicationDetailsService
                    .findByApplicationNumber(paySewerageTaxDetails.getApplicaionNumber());
        else if (paySewerageTaxDetails.getConsumerNo() != null)
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByConnectionShscNumberAndConnectionStatus(
                    paySewerageTaxDetails.getConsumerNo(), SewerageConnectionStatus.ACTIVE);
        if (sewerageApplicationDetails == null) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(SewerageTaxConstants.PROPERTYID_NOT_EXIST_ERR_CODE);
            errorDetails.setErrorMessage(SewerageTaxConstants.STAXDETAILS_CONSUMER_CODE_NOT_EXIST_ERR_MSG_PREFIX
                    + (paySewerageTaxDetails.getConsumerNo() != null ? paySewerageTaxDetails.getConsumerNo()
                            : paySewerageTaxDetails.getApplicaionNumber())
                    + SewerageTaxConstants.STAXDETAILS_NOT_EXIST_ERR_MSG_SUFFIX);
            sewerageTaxDetails.setErrorDetails(errorDetails);
        } else {
            sewerageTaxDetails.setConsumerNo(sewerageApplicationDetails.getConnection().getShscNumber());
            sewerageTaxDetails = buildSewerageTaxDetailsUsingDemand(sewerageTaxDetails, sewerageApplicationDetails);
        }
        return sewerageTaxDetails;

    }

    private SewerageTaxDetails buildSewerageTaxDetailsUsingDemand(SewerageTaxDetails sewerageTaxDetails,
            SewerageApplicationDetails sewerageApplicationDetails) {

        ErrorDetails errorDetails;
        sewerageTaxDetails.setConsumerNo(sewerageApplicationDetails.getConnection().getShscNumber());

        final String propertyIdentifier = sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier();
        final BasicProperty basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(propertyIdentifier);
        sewerageTaxDetails.setPropertyAddress(basicProperty.getAddress().toString());
        sewerageTaxDetails.setLocalityName(basicProperty.getPropertyID().getLocality().getName());

        final List<PropertyOwnerInfo> propOwnerInfos = basicProperty.getPropertyOwnerInfo();
        if (!propOwnerInfos.isEmpty()) {
            sewerageTaxDetails.setOwnerName(propOwnerInfos.get(0).getOwner().getName());
            sewerageTaxDetails.setMobileNo(propOwnerInfos.get(0).getOwner().getMobileNumber());
        }

        BigDecimal amtCollected ;

        final HashMap<String, SewerageTaxPaidDetails> sewerageReportMap = new HashMap<>();
        SewerageTaxPaidDetails dcbResult;

        EgDemand sewerageDemand = sewerageApplicationDetails.getCurrentDemand();
        if (sewerageDemand != null)
            for (final EgDemandDetails demandDtl : sewerageDemand.getEgDemandDetails()) {

                if (demandDtl.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_SEWERAGETAX_CODE)
                        && demandDtl.getAmount()
                        .compareTo((demandDtl.getAmtCollected() == null) ? BigDecimal.ZERO : demandDtl.getAmtCollected()) > 0) {

                    final SewerageTaxPaidDetails rateResult = sewerageReportMap
                            .get(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());
                    if (rateResult == null) {

                        dcbResult = new SewerageTaxPaidDetails();
                        dcbResult.setInstallment(
                                demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());
                        amtCollected = (demandDtl.getAmtCollected() == null) ? BigDecimal.ZERO : demandDtl.getAmtCollected();
                        if (demandDtl.getAmount().compareTo(amtCollected) > 0) {

                            dcbResult.setTaxAmount(demandDtl.getAmount().subtract(amtCollected));
                            dcbResult
                                    .setTotalAmount(dcbResult.getTotalAmount().add(demandDtl.getAmount().subtract(amtCollected)));
                        }
                        sewerageReportMap.put(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription(), dcbResult);
                    } else {

                        dcbResult = sewerageReportMap
                                .get(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription());

                        amtCollected = (demandDtl.getAmtCollected() == null) ? BigDecimal.ZERO : demandDtl.getAmtCollected();

                        if (demandDtl.getAmount().compareTo(amtCollected) > 0) {

                            dcbResult.setTaxAmount(dcbResult.getTaxAmount().add(demandDtl.getAmount().subtract(amtCollected)));
                            dcbResult
                                    .setTotalAmount(dcbResult.getTotalAmount().add(demandDtl.getAmount().subtract(amtCollected)));
                        }
                        sewerageReportMap.put(demandDtl.getEgDemandReason().getEgInstallmentMaster().getDescription(), dcbResult);
                    }
                }
            }

        final List<SewerageTaxPaidDetails> rateResultList = new ArrayList<>();
        if (sewerageReportMap.size() > 0)
            sewerageReportMap.forEach((key, value) -> {
                rateResultList.add(value);
            });

        if (!rateResultList.isEmpty())
            Collections.sort(rateResultList, (c1, c2) -> c1.getInstallment()
                    .compareTo(c2.getInstallment()));

        
        sewerageTaxDetails.setTaxDetails(rateResultList);

        errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(SewerageTaxConstants.THIRD_PARTY_ERR_CODE_SUCCESS);
        errorDetails.setErrorMessage(SewerageTaxConstants.THIRD_PARTY_ERR_MSG_SUCCESS);

        sewerageTaxDetails.setErrorDetails(errorDetails);
        return sewerageTaxDetails;

    }

    public Map<String, BigDecimal> getDemandDetailsMap(final SewerageApplicationDetails sewerageApplicationDetails) {
        final EgDemand currDemand = sewerageApplicationDetails.getCurrentDemand();
        Installment installment;
        List<Object> dmdCollList = new ArrayList<>(0);
        Installment currFirstHalf;
        Installment currSecondHalf;
        Integer instId;
        BigDecimal currDmd = BigDecimal.ZERO;
        BigDecimal arrDmd = BigDecimal.ZERO;
        BigDecimal currCollection = BigDecimal.ZERO;
        BigDecimal arrCollection = BigDecimal.ZERO;
        final Map<String, BigDecimal> retMap = new HashMap<>(0);
        if (currDemand != null)
            dmdCollList = getDmdCollAmtInstallmentWise(currDemand);
        currFirstHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                .get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
        currSecondHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                .get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);
        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            instId = Integer.valueOf(listObj[1].toString());
            installment = installmentHibDao.findById(instId, false);
            if (currFirstHalf.getDescription().equals(installment.getDescription())
                    || currSecondHalf.getDescription().equals(installment.getDescription())) {
                if (listObj[3] != null && BigDecimal.valueOf((Double) listObj[3]).compareTo(BigDecimal.ZERO) > 0)
                    currCollection = currCollection.add(BigDecimal.valueOf((Double) listObj[3]));
                currDmd = currDmd.add(BigDecimal.valueOf((Double) listObj[2]));
            } else if (listObj[2] != null) {
                arrDmd = arrDmd.add(BigDecimal.valueOf((Double) listObj[2]));
                if (BigDecimal.valueOf((Double) listObj[3]).compareTo(BigDecimal.ZERO) > 0)
                    arrCollection = arrCollection.add(BigDecimal.valueOf((Double) listObj[3]));
            }
        }
        retMap.put(SewerageTaxConstants.CURR_DMD_STR, currDmd);
        retMap.put(SewerageTaxConstants.ARR_DMD_STR, arrDmd);
        retMap.put(SewerageTaxConstants.CURR_COLL_STR, currCollection);
        retMap.put(SewerageTaxConstants.ARR_COLL_STR, arrCollection);
        return retMap;
    }

    public BigDecimal getCurrentDue(final SewerageApplicationDetails sewerageApplicationDetails) {
        final EgDemand currentDemand = sewerageApplicationDetails.getCurrentDemand();
        BigDecimal balance = BigDecimal.ZERO;
        if (currentDemand != null) {
            final List<Object> instVsAmt = getDmdCollAmtInstallmentWiseUptoCurrentInstallmemt(currentDemand,
                    sewerageApplicationDetails);
            for (final Object object : instVsAmt) {
                final Object[] ddObject = (Object[]) object;
                final BigDecimal dmdAmt = new BigDecimal((Double) ddObject[2]);
                BigDecimal collAmt = BigDecimal.ZERO;
                if (ddObject[2] != null)
                    collAmt = new BigDecimal((Double) ddObject[3]);
                balance = balance.add(dmdAmt.subtract(collAmt));
            }
        }
        return balance;
    }

    @SuppressWarnings("unchecked")
    private List<Object> getDmdCollAmtInstallmentWise(final EgDemand egDemand) {
        final StringBuilder queryStringBuilder = new StringBuilder();
        queryStringBuilder
                .append("select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, "
                        + "sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, "
                        + "eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id "
                        + "and dmdDet.id_demand =:dmdId and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master "
                        + "group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
        return getCurrentSession().createSQLQuery(queryStringBuilder.toString()).setLong("dmdId", egDemand.getId())
                .list();
    }

    @SuppressWarnings("unchecked")
    private List<Object> getDmdCollAmtInstallmentWiseUptoCurrentInstallmemt(final EgDemand egDemand,
            final SewerageApplicationDetails sewerageApplicationDetails) {
        Installment currInstallment = installmentDao.getInsatllmentByModuleAndDescription(
                moduleService.getModuleByName(MODULE_NAME),
                sewerageApplicationDetails.getCurrentDemand().getEgInstallmentMaster().getDescription());
        final StringBuffer strBuf = new StringBuffer(2000);
        strBuf.append(
                "select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, "
                        + "sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, "
                        + "eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id "
                        + "and dmdDet.id_demand =:dmdId and inst.start_date<=:currInstallmentDate and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master "
                        + "group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
        final Query query = getCurrentSession().createSQLQuery(strBuf.toString())
                .setParameter("dmdId", egDemand.getId())
                .setParameter("currInstallmentDate", currInstallment.getToDate());
        return query.list();
    }
}