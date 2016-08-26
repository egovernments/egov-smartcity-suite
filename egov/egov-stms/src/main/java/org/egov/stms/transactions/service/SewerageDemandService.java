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

package org.egov.stms.transactions.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.EgDemandDao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageConnectionFee;
import org.egov.stms.transactions.entity.SewerageDemandConnection;
import org.egov.stms.transactions.entity.SewerageDemandDetail;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SewerageDemandService {
    private static final Logger LOGGER = Logger.getLogger(SewerageDemandService.class);
    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private DemandGenericDao demandGenericDao;

    @Autowired
    private EgDemandDao egDemandDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @PersistenceContext
    private EntityManager entityManager;

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
        final EgDemandReason demandReasonObj = (EgDemandReason) demandQuery.uniqueResult();
        return demandReasonObj;
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
                moduleService.getModuleByName(SewerageTaxConstants.MODULE_NAME), new Date());

    }

    public Installment getNextInstallment() {

        Installment currentInstlalment = getCurrentInstallment();
        if (currentInstlalment != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentInstlalment.getToDate());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            return installmentDao.getInsatllmentByModuleForGivenDate(
                    moduleService.getModuleByName(SewerageTaxConstants.MODULE_NAME), calendar.getTime());
        }
        return null;
    }

    public Installment getInstallmentByDescription(final String description) {
        return installmentDao.getInsatllmentByModuleAndDescription(
                moduleService.getModuleByName(SewerageTaxConstants.MODULE_NAME), description);

    }

    public Installment getInsatllmentByModuleForGivenDate(final Date installmentDate) {
        return installmentDao.getInsatllmentByModuleForGivenDate(
                moduleService.getModuleByName(SewerageTaxConstants.MODULE_NAME), installmentDate);

    }

    public List<Installment> getPreviousInstallment(final Date curentInstalmentEndate) {
        return installmentDao.fetchPreviousInstallmentsInDescendingOrderByModuleAndDate(
                moduleService.getModuleByName(SewerageTaxConstants.MODULE_NAME), curentInstalmentEndate, 1);

    }

    /**
     * @param dmdAmount
     * @param egDemandReason
     * @param amtCollected
     * @return
     */
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
        List<BillReceipt> billReceiptList = new ArrayList<BillReceipt>();
        billReceiptList = demandGenericDao.getBillReceipts(demand);
        return billReceiptList;
    }

    public EgDemand createDemandOnNewConnection(final List<SewerageConnectionFee> connectionFees,
            final SewerageApplicationDetails sewerageApplicationDetail) throws ApplicationRuntimeException {

        EgDemand demand = null;
        final Set<EgDemandDetails> demandDetailSet = new HashSet<EgDemandDetails>();
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
            final SewerageApplicationDetails sewerageApplicationDetail) throws ApplicationRuntimeException {

        EgDemand demand = null;
        final Set<EgDemandDetails> demandDetailSet = new HashSet<EgDemandDetails>();
        BigDecimal totalDemandAmount = BigDecimal.ZERO;
        BigDecimal totalCollectedAmount = BigDecimal.ZERO;
        if (sewerageApplicationDetail != null && sewerageApplicationDetail.getCurrentDemand() == null) {
            final Installment installment = getCurrentInstallment();
            for (final SewerageDemandDetail sdd : sewerageDemandDetail) {
                final EgDemandReason pendingTaxReason = getDemandReasonByCodeAndInstallment(sdd.getReasonMaster(), sdd.getInstallmentId()); 
                if (pendingTaxReason != null) {
                    demandDetailSet.add(createDemandDetails(sdd.getActualAmount(), pendingTaxReason,
                            sdd.getActualCollection()));
                    totalDemandAmount = totalDemandAmount.add(sdd.getActualAmount());
                    totalCollectedAmount = totalCollectedAmount.add(sdd.getActualCollection());
                } else
                    throw new ApplicationRuntimeException("SEWERAGE.001");
            }
            demand = createDemand(demandDetailSet, installment, totalDemandAmount);
            demand.setAmtCollected(totalCollectedAmount);
        }
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
            for (final EgDemandDetails dmdDtl : demand.getEgDemandDetails()) {
                if (scf.getFeesDetail().getCode()
                        .equalsIgnoreCase(dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode())) {
                    demandDtlPresent = true;

                    totalDemandAmount = totalDemandAmount.subtract(dmdDtl.getAmount());
                    dmdDtl.setAmount(BigDecimal.valueOf(scf.getAmount()));
                    totalDemandAmount = totalDemandAmount.add(BigDecimal.valueOf(scf.getAmount()));
                }
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

    public EgDemand updateDemandOnChangeInClosets(SewerageApplicationDetails oldSewerageApplicationDetails,
            final List<SewerageConnectionFee> connectionFees, final EgDemand demand) {
        final Installment installment = getCurrentInstallment();
        BigDecimal totalDemandAmount = BigDecimal.ZERO;
        BigDecimal oldDonationCharge = BigDecimal.ZERO;
        BigDecimal oldSewerageTax = BigDecimal.ZERO;

        BigDecimal currentDonationCharge = BigDecimal.ZERO;
        BigDecimal currentSewerageTax = BigDecimal.ZERO;

        boolean demandDtlPresent = false;

        if (oldSewerageApplicationDetails != null) {
            for (final SewerageConnectionFee oldSewerageConnectionFee : oldSewerageApplicationDetails.getConnectionFees()) {
                if (oldSewerageConnectionFee.getFeesDetail().getCode()
                        .equalsIgnoreCase(SewerageTaxConstants.FEES_SEWERAGETAX_CODE)) {
                    oldSewerageTax = oldSewerageTax.add(BigDecimal.valueOf(oldSewerageConnectionFee.getAmount()));
                }
                if (oldSewerageConnectionFee.getFeesDetail().getCode()
                        .equalsIgnoreCase(SewerageTaxConstants.FEES_DONATIONCHARGE_CODE)) {
                    oldDonationCharge = oldDonationCharge.add(BigDecimal.valueOf(oldSewerageConnectionFee.getAmount()));
                }
            }
        }

        for (final SewerageConnectionFee scf : connectionFees) {

            if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_SEWERAGETAX_CODE)) {
                currentSewerageTax = currentSewerageTax.add(BigDecimal.valueOf(scf.getAmount()));
            }
            if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_DONATIONCHARGE_CODE)) {
                currentDonationCharge = currentDonationCharge.add(BigDecimal.valueOf(scf.getAmount()));
            }
        }

        // Compare previous tax deposit amount and sewerage tax.
        // if donation amount is less then previous detail , then do not create donation amount, else difference amount will be
        // added.
        // get sewerage tax of current tax, if amount paid, check if amount paid more, then adjust as advance.
        // if not paid, correct current tax.
        // if sewerage tax more, then add difference amount to current installment.

        // Get deposit amount,establishment charge and sewerage tax as current year tax. Update collected amount.
        for (final SewerageConnectionFee scf : connectionFees) {

            for (final EgDemandDetails dmdDtl : demand.getEgDemandDetails()) {

                if (scf.getFeesDetail().getCode()
                        .equalsIgnoreCase(dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode())) {

                    demandDtlPresent = true;

                    if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_DONATIONCHARGE_CODE)) {
                        // If donation chanrge more than earlier installment, then collect amount in this installment
                        if (currentDonationCharge.compareTo(oldDonationCharge) > 0) {
                            dmdDtl.setAmount(currentDonationCharge.subtract(oldDonationCharge));
                            totalDemandAmount = totalDemandAmount.add(currentDonationCharge.subtract(oldDonationCharge));
                        }
                    } else if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_SEWERAGETAX_CODE)) {
                        // Mean tax increased and diffence amount will be collected in this installment.
                        if (currentSewerageTax.compareTo(oldSewerageTax) > 0) {
                            dmdDtl.setAmount(currentSewerageTax);
                            dmdDtl.setAmtCollected(oldSewerageTax);
                            totalDemandAmount = totalDemandAmount.add(currentSewerageTax.subtract(oldSewerageTax));

                        } else if (oldSewerageTax.compareTo(BigDecimal.ZERO) > 0)// Mean, paid more amount in old installment.
                                                                                 // Difference amount will be used as next
                                                                                 // installment tax.
                        {
                            dmdDtl.setAmount(oldSewerageTax);
                            dmdDtl.setAmtCollected(oldSewerageTax);

                            if (oldSewerageTax.compareTo(currentSewerageTax) > 0) {
                                // Add amount to advance of next installment if not present in this transaction.

                                createAdvanceDemandDetail(demand, oldSewerageTax.subtract(currentSewerageTax));

                            }
                        }

                    } else {
                        totalDemandAmount = totalDemandAmount.subtract(dmdDtl.getAmount());
                        dmdDtl.setAmount(BigDecimal.valueOf(scf.getAmount()));
                        totalDemandAmount = totalDemandAmount.add(BigDecimal.valueOf(scf.getAmount()));

                    }
                }
            }

            if (!demandDtlPresent && scf.getAmount() > 0) {

                if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_DONATIONCHARGE_CODE)) {
                    if (currentDonationCharge.compareTo(oldDonationCharge) > 0) {
                        demand.addEgDemandDetails(createDemandDetails(currentDonationCharge.subtract(oldDonationCharge),
                                getDemandReasonByCodeAndInstallment(scf.getFeesDetail().getCode(), installment.getId()),
                                BigDecimal.ZERO));
                        totalDemandAmount = totalDemandAmount.add(BigDecimal.valueOf(scf.getAmount()));
                    }
                } else if (scf.getFeesDetail().getCode().equalsIgnoreCase(SewerageTaxConstants.FEES_SEWERAGETAX_CODE)) {
                    // Mean tax increased and diffence amount will be collected in this installment.
                    if (currentSewerageTax.compareTo(oldSewerageTax) > 0) {
                        demand.addEgDemandDetails(createDemandDetails(currentSewerageTax,
                                getDemandReasonByCodeAndInstallment(scf.getFeesDetail().getCode(), installment.getId()),
                                oldSewerageTax));
                        totalDemandAmount = totalDemandAmount.add(currentSewerageTax.subtract(oldSewerageTax));

                    } else if (oldSewerageTax.compareTo(BigDecimal.ZERO) > 0 )// Mean, paid more amount in old installment.
                                                                             // Difference amount will be used as next installment tax.
                    {
                        if (oldSewerageTax.compareTo(currentSewerageTax) > 0) {
                            demand.addEgDemandDetails(createDemandDetails(oldSewerageTax,
                                getDemandReasonByCodeAndInstallment(scf.getFeesDetail().getCode(), installment.getId()), 
                                oldSewerageTax));
                            // Add amount to advance of next installment if not present in this transaction.
                            createAdvanceDemandDetail(demand, oldSewerageTax.subtract(currentSewerageTax));
                            // totalDemandAmount = totalDemandAmount.add(oldSewerageTax.subtract(currentSewerageTax));
                        }
                    }

                } else {
                    demand.addEgDemandDetails(createDemandDetails(BigDecimal.valueOf(scf.getAmount()),
                            getDemandReasonByCodeAndInstallment(scf.getFeesDetail().getCode(), installment.getId()),
                            BigDecimal.ZERO));
                    totalDemandAmount = totalDemandAmount.add(BigDecimal.valueOf(scf.getAmount()));
                }
            }
        }
        demand.addBaseDemand(totalDemandAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
        return demand;

    }

    private void createAdvanceDemandDetail(EgDemand demand, BigDecimal amount) {
        // TODO Auto-generated method stub

        // get next installment
        Installment nextInstallment = getNextInstallment();
        Boolean advancePresent = false;
        for (final EgDemandDetails dmdDtl : demand.getEgDemandDetails()) {
            if (dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                    .equalsIgnoreCase(SewerageTaxConstants.FEES_ADVANCE_CODE)
                    && nextInstallment != null && nextInstallment.getDescription()
                            .equalsIgnoreCase(dmdDtl.getEgDemandReason().getEgInstallmentMaster().getDescription())) {
                dmdDtl.getEgDemand().getBaseDemand().subtract(dmdDtl.getAmount());
                dmdDtl.setAmount(amount);
                dmdDtl.getEgDemand().getBaseDemand().add(amount);

            }
        }
        if (!advancePresent) {
            demand.addEgDemandDetails(createDemandDetails(amount,
                    getDemandReasonByCodeAndInstallment(SewerageTaxConstants.FEES_ADVANCE_CODE, nextInstallment.getId()),
                    BigDecimal.ZERO));
            demand.getBaseDemand().add(amount);
        }
    }

    
}