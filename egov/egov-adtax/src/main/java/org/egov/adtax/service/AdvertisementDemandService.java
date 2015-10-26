/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.adtax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.entity.Hoarding;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.service.ModuleService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementDemandService {

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private DemandGenericDao demandGenericDao;

    
    @Autowired
    private ModuleService moduleService;

    @PersistenceContext
    private EntityManager entityManager;
    
    
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
/**
 * 
 * @param hoarding
 * @return
 */
    public EgDemand createDemand(final Hoarding hoarding) {

        EgDemand demand = null;
        final Set<EgDemandDetails> demandDetailSet = new HashSet<EgDemandDetails>();
        final Installment installment = getCurrentInstallment();
        BigDecimal totalDemandAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;
        if (hoarding != null && hoarding.getDemandId() == null) {
            if (hoarding.getTaxAmount() != null || hoarding.getPendingTax()!=null) {
                
                if( hoarding.getPendingTax()!=null)
                    taxAmount=taxAmount.add( hoarding.getPendingTax());
                if( hoarding.getTaxAmount()!=null)
                    taxAmount=taxAmount.add(hoarding.getTaxAmount());
                
                demandDetailSet.add(createDemandDetails(
                        (taxAmount),
                        getDemandReasonByCodeAndInstallment(AdvertisementTaxConstants.DEMANDREASON_ADVERTISEMENTTAX,
                                installment), BigDecimal.ZERO));
                totalDemandAmount=  totalDemandAmount.add((taxAmount));
            }
            if (hoarding.getEncroachmentFee() != null) {
                demandDetailSet.add(createDemandDetails(
                        (hoarding.getEncroachmentFee()),
                        getDemandReasonByCodeAndInstallment(AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE,
                                installment), BigDecimal.ZERO));
                totalDemandAmount= totalDemandAmount.add((hoarding.getEncroachmentFee()));
            }
            demand = createDemand(demandDetailSet, installment, totalDemandAmount);
        }

        return demand;
    }
/**
 * 
 * @param demandReason
 * @param installment
 * @return
 */
    public EgDemandReason getDemandReasonByCodeAndInstallment(final String demandReason, final Installment installment) {
        final Query demandQuery = getCurrentSession().getNamedQuery("DEMANDREASONBY_CODE_AND_INSTALLMENTID");
        demandQuery.setParameter(0, demandReason);
        demandQuery.setParameter(1, installment.getId());
        final EgDemandReason demandReasonObj = (EgDemandReason) demandQuery.uniqueResult();
        return demandReasonObj;
    }
/**
 * 
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
        egDemand.setBaseDemand(totalDemandAmount);
        egDemand.setModifiedDate(new Date());
        return egDemand;
    }
/**
 * 
 * @param hoarding
 * @param demand
 * @return
 */
    public EgDemand updateDemand(final Hoarding hoarding,EgDemand demand) {
        final Installment installment = getCurrentInstallment();
        BigDecimal totalDemandAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;

        // Boolean calculateTax=true;
        Boolean enchroachmentFeeAlreadyExistInDemand = false;

        //EgDemand demand = hoarding.getDemandId();
        if (demand == null) {
            demand = createDemand(hoarding);
        } else {
            EgDemandReason pendingTaxReason = getDemandReasonByCodeAndInstallment(
                    AdvertisementTaxConstants.DEMANDREASON_ADVERTISEMENTTAX, installment);
            EgDemandReason encroachmentFeeReason = getDemandReasonByCodeAndInstallment(
                    AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE, installment);

            if (hoarding.getTaxAmount() != null || hoarding.getPendingTax() != null) {

                if (hoarding.getPendingTax() != null)
                    taxAmount = taxAmount.add(hoarding.getPendingTax());
                if (hoarding.getTaxAmount() != null)
                    taxAmount = taxAmount.add(hoarding.getTaxAmount());

            }
            for (EgDemandDetails dmdDtl : demand.getEgDemandDetails()) {
                // Assumption: tax amount is mandatory.
                if (dmdDtl.getEgDemandReason().getId() == pendingTaxReason.getId()
                        && taxAmount.compareTo(BigDecimal.ZERO) > 0) {
                    // TODO: Also check whether fully collected ?
                    totalDemandAmount = totalDemandAmount.add(taxAmount.subtract(dmdDtl.getAmount()));
                    dmdDtl.setAmount(taxAmount);

                }
                // Encroachment fee may not mandatory. If already part of demand
                // then
                if (dmdDtl.getEgDemandReason().getId() == encroachmentFeeReason.getId()) {
                    enchroachmentFeeAlreadyExistInDemand = true;
                    if (hoarding.getEncroachmentFee() != null
                            && hoarding.getEncroachmentFee().compareTo(BigDecimal.ZERO) > 0) {
                        totalDemandAmount = totalDemandAmount.add(hoarding.getEncroachmentFee().subtract(
                                dmdDtl.getAmount()));
                        dmdDtl.setAmount(hoarding.getEncroachmentFee());
                        // update encroachment fee..
                    } else {
                        totalDemandAmount = totalDemandAmount.subtract(dmdDtl.getAmount());
                        demand.removeEgDemandDetails(dmdDtl);
                        // delete demand detail
                    }

                }
            }

            if (!enchroachmentFeeAlreadyExistInDemand && hoarding.getEncroachmentFee() != null
                    && hoarding.getEncroachmentFee().compareTo(BigDecimal.ZERO) > 0) {
                demand.addEgDemandDetails(createDemandDetails(
                        (hoarding.getEncroachmentFee()),
                        getDemandReasonByCodeAndInstallment(AdvertisementTaxConstants.DEMANDREASON_ENCROCHMENTFEE,
                                installment), BigDecimal.ZERO));
                totalDemandAmount = totalDemandAmount.add(hoarding.getEncroachmentFee());
            }
            demand.addBaseDemand(totalDemandAmount);

        }
        return demand;

    }
/**
 * 
 * @return
 */
    public Installment getCurrentInstallment() {
        return installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                moduleService.getModuleByName(AdvertisementTaxConstants.MODULE_NAME), new Date(),
                AdvertisementTaxConstants.YEARLY);

    }
/**
 * 
 * @param dmdAmount
 * @param egDemandReason
 * @param amtCollected
 * @return
 */
    public EgDemandDetails createDemandDetails(final BigDecimal dmdAmount, final EgDemandReason egDemandReason,
            final BigDecimal amtCollected) {
        return EgDemandDetails.fromReasonAndAmounts(dmdAmount, egDemandReason, amtCollected);
    }
/**
 * 
 * @param hoarding
 * @return
 */
    public Boolean checkAnyTaxIsPendingToCollect(final Hoarding hoarding) {
        Boolean pendingTaxCollection = false;

        if (hoarding != null && hoarding.getDemandId() != null)
            for (final EgDemandDetails demandDtl : hoarding.getDemandId().getEgDemandDetails())
                if (demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0) {
                    pendingTaxCollection = true;
                    break;

                }

        return pendingTaxCollection;

    }
    /**
     * @param demand
     * @param penaltyCalculationDate 
     * @return Penalty Amount and PendingAmount
     */
    public Map<String, BigDecimal> checkPedingAmountByDemand(final EgDemand demand, Date penaltyCalculationDate) {

        final Map<String, BigDecimal> demandFeeType = new LinkedHashMap<String, BigDecimal>();

        BigDecimal penaltyAmt = BigDecimal.ZERO;
        BigDecimal pendingAmount = BigDecimal.ZERO;
        if (demand != null) {
            for (final EgDemandDetails demandDtl : demand.getEgDemandDetails()) {
                // Mean if installment is different than current, then calculate
                // penalty
                if (demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0
                // && currentInstallment.getId() !=
                // demandDtl.getEgDemandReason().getEgInstallmentMaster().getId())
                ) {
                    final BigDecimal amount = demandDtl.getAmount().subtract(demandDtl.getAmtCollected());

                    pendingAmount = pendingAmount.add(amount);
                    // PENALTY is not the part of existing demand
                    penaltyAmt = calculatePenalty(penaltyAmt, demandDtl, amount, penaltyCalculationDate);

                }
            }
        }
        demandFeeType.put(AdvertisementTaxConstants.PENALTYAMOUNT, penaltyAmt);
        demandFeeType.put(AdvertisementTaxConstants.PENDINGDEMANDAMOUNT, pendingAmount);

        return demandFeeType;

    }

    private int noOfMonths(final Date startDate, final Date endDate) {
        DateTime sDate = new DateTime(startDate);
        DateTime eDate = new DateTime(endDate);
        final int yearDiff = eDate.getYear() - sDate.getYear();
        int noOfMonths = yearDiff * 12 + eDate.getMonthOfYear() - sDate.getMonthOfYear();
        return noOfMonths;

    }
    
    private BigDecimal calculatePenalty(BigDecimal penaltyAmt,final EgDemandDetails demandDtl, final BigDecimal amount, Date penaltyCalculationDate) {
        int noofmonths = 0;

        if (penaltyCalculationDate != null)
            noofmonths = (noOfMonths(penaltyCalculationDate, new Date())); 
        else
            noofmonths = (noOfMonths(demandDtl.getEgDemandReason().getEgInstallmentMaster().getFromDate(),
                    new Date()));
      
        if (noofmonths > 0) {
            penaltyAmt = penaltyAmt.add(amount.multiply(BigDecimal.valueOf(noofmonths))
                    .divide(BigDecimal.valueOf(100).setScale(0, BigDecimal.ROUND_HALF_UP))
                    .setScale(0, BigDecimal.ROUND_HALF_UP));
        }
        return penaltyAmt;
    }
    
    public BigDecimal calculatePenalty(final EgDemandDetails demandDtl, final BigDecimal amount, Date penaltyCalculationDate) {
        BigDecimal penaltyAmt=BigDecimal.ZERO;
        penaltyAmt= calculatePenalty(penaltyAmt,demandDtl,amount,penaltyCalculationDate);
        return penaltyAmt;
  }
    /**
     * 
     * @param demand
     * @return
     */
    public BigDecimal checkPenaltyAmountByDemand(final EgDemand demand,Date penaltyCalculationDate) {
        BigDecimal penaltyAmt = BigDecimal.ZERO;
       // final Installment currentInstallment = getCurrentInstallment();
        if (demand != null){
            for (final EgDemandDetails demandDtl : demand.getEgDemandDetails())
                // Mean if installment is different than current, then calculate
                // penalty
                if (demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0
                //        && currentInstallment.getId() != demandDtl.getEgDemandReason().getEgInstallmentMaster().getId())
                ){
                    final BigDecimal amount = demandDtl.getAmount().subtract(demandDtl.getAmtCollected());
                    penaltyAmt = calculatePenalty(penaltyAmt, demandDtl, amount,penaltyCalculationDate);
                }
        }
        return penaltyAmt;

    }
    /**
     * 
     * @param hoarding
     * @return
     */
    public boolean anyDemandPendingForCollection(final Hoarding hoarding) {
        return checkAnyTaxIsPendingToCollect(hoarding);
    }

    /*
     * Check any amount collected in the current financial year or not.
     */
    public boolean collectionDoneForThisYear(final Hoarding hoarding) {
        Boolean amountCollectedInCurrentYear = false;
        if (hoarding != null && hoarding.getDemandId() != null) {
            final Installment currentInstallment = getCurrentInstallment();

            if (currentInstallment != null) {
                for (final EgDemandDetails demandDtl : hoarding.getDemandId().getEgDemandDetails())
                {
                    if (demandDtl.getAmtCollected().compareTo(BigDecimal.ZERO) > 0
                            && currentInstallment.getId() == demandDtl.getEgDemandReason().getEgInstallmentMaster()
                                    .getId()) {
                        amountCollectedInCurrentYear = true;
                        break;
                    }
                }
            }
        }
        return amountCollectedInCurrentYear;
    }
    
    public List<EgDemandDetails> getDemandDetailByPassingDemandDemandReason(EgDemand demand , EgDemandReason demandReason) {

        return  demandGenericDao.getDemandDetailsForDemandAndReasons(
            demand, Arrays.asList(demandReason));
 
    }

    public List<BillReceipt> getBilReceiptsByDemand(EgDemand demand) {
        List<BillReceipt> billReceiptList = new ArrayList<BillReceipt>();
        billReceiptList = demandGenericDao.getBillReceipts(demand);
        return billReceiptList;
    }
  }
