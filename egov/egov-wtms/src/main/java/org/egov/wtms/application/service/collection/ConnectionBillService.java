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
package org.egov.wtms.application.service.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConnectionBillService extends BillServiceInterface {

    private static final String STRING_WCMS_FUCNTION_CODE = "5100";
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EgBillDao egBillDAO;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private DemandGenericDao demandGenericDAO;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public List<EgBillDetails> getBilldetails(final Billable billObj) {
        final List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>();
        final EgDemand demand = billObj.getCurrentDemand();
        final Date currentDate = new Date();
        final Map installmentWise = new HashMap<Installment, List<EgDemandDetails>>();
        final Set<Installment> sortedInstallmentSet = new TreeSet<Installment>();
        final DemandComparatorByOrderId demandComparatorByOrderId = new DemandComparatorByOrderId();
        final List<EgDemandDetails> orderedDetailsList = new ArrayList<EgDemandDetails>();
        final Installment currInstallment = connectionDemandService
                .getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME, WaterTaxConstants.YEARLY, new Date());
        final CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(new Date());
        new TreeMap<Date, String>();

        for (final EgDemandDetails demandDetail : demand.getEgDemandDetails()) {
            final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            if (installmentWise.get(installment) == null) {
                final List<EgDemandDetails> detailsList = new ArrayList<EgDemandDetails>();
                detailsList.add(demandDetail);
                installmentWise.put(demandDetail.getEgDemandReason().getEgInstallmentMaster(), detailsList);
                sortedInstallmentSet.add(installment);
            } else
                ((List<EgDemandDetails>) installmentWise.get(demandDetail.getEgDemandReason().getEgInstallmentMaster()))
                        .add(demandDetail);
        }
        for (final Installment i : sortedInstallmentSet) {
            final List<EgDemandDetails> installmentWiseDetails = (List<EgDemandDetails>) installmentWise.get(i);
            Collections.sort(installmentWiseDetails, demandComparatorByOrderId);
            orderedDetailsList.addAll(installmentWiseDetails);
        }

        int i = 1;
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .getWaterConnectionDetailsByDemand(demand);
        for (final EgDemandDetails demandDetail : orderedDetailsList) {

            final EgDemandReason reason = demandDetail.getEgDemandReason();
            final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();

            if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDebit().equalsIgnoreCase("N")
                    && demandDetail.getAmount().compareTo(demandDetail.getAmtCollected()) > 0) {
                final EgBillDetails billdetail = new EgBillDetails();
                if (demandDetail.getAmount() != null) {
                    billdetail.setDrAmount(BigDecimal.ZERO);
                    billdetail.setCrAmount(demandDetail.getAmount().subtract(demandDetail.getAmtCollected()));
                }

                LOGGER.info("demandDetail.getEgDemandReason()"
                        + demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster() + " glcodeerror"
                        + demandDetail.getEgDemandReason().getGlcodeId());
                billdetail.setGlcode(demandDetail.getEgDemandReason().getGlcodeId().getGlcode());
                billdetail.setEgDemandReason(demandDetail.getEgDemandReason());
                billdetail.setAdditionalFlag(Integer.valueOf(1));
                billdetail.setCreateDate(currentDate);
                billdetail.setModifiedDate(currentDate);
                billdetail.setOrderNo(i++);
                billdetail.setDescription(
                        reason.getEgDemandReasonMaster().getReasonMaster() + " - " + installment.getDescription()
                                + " # " + billObj.getCurrentDemand().getEgInstallmentMaster().getDescription());
                billdetail.setFunctionCode(STRING_WCMS_FUCNTION_CODE);
                if (waterConnectionDetails != null
                        && waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED))
                    if (billdetail.getDescription().contains(WaterTaxConstants.DEMANDRSN_REASON_ADVANCE))
                        billdetail.setPurpose(PURPOSE.ADVANCE_AMOUNT.toString());
                    else if (billdetail.getEgDemandReason().getEgInstallmentMaster().getToDate()
                            .compareTo(finYear.getStartingDate()) < 0)
                        billdetail.setPurpose(PURPOSE.ARREAR_AMOUNT.toString());
                    else if (billdetail.getEgDemandReason().getEgInstallmentMaster().getFromDate()
                            .compareTo(finYear.getStartingDate()) >= 0
                            && billdetail.getEgDemandReason().getEgInstallmentMaster().getFromDate()
                                    .compareTo(finYear.getEndingDate()) < 0)
                        billdetail.setPurpose(PURPOSE.CURRENT_AMOUNT.toString());
                    else
                        billdetail.setPurpose(PURPOSE.OTHERS.toString());
                if (waterConnectionDetails != null
                        && waterConnectionDetails.getConnectionType().equals(ConnectionType.METERED))
                    billdetail.setPurpose(PURPOSE.OTHERS.toString());
                if (currInstallment != null && installment.getFromDate().before(currInstallment.getToDate()))
                    billdetail.setAdditionalFlag(1);
                else
                    billdetail.setAdditionalFlag(0);
                billDetails.add(billdetail);
            }
        }

        if (waterConnectionDetails != null
                && waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED)) {
            final Map<String, Installment> currInstallments = new HashMap<String, Installment>();
            final Installment currFirstHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                    .get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
            final Installment currSecondHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                    .get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);
            currInstallments.put(WaterTaxConstants.CURRENTYEAR_FIRST_HALF, currFirstHalf);
            currInstallments.put(WaterTaxConstants.CURRENTYEAR_SECOND_HALF, currSecondHalf);
            final Date advanceStartDate = org.apache.commons.lang3.time.DateUtils
                    .addYears(currInstallments.get(WaterTaxConstants.CURRENTYEAR_FIRST_HALF).getFromDate(), 1);

            final List<Installment> advanceInstallments = getAdvanceInstallmentsList(advanceStartDate);
            BigDecimal currentInstDemand = BigDecimal.ZERO;
            for (final EgDemandDetails dmdDet : demand.getEgDemandDetails())
                if (dmdDet.getInstallmentStartDate()
                        .equals(currInstallments.get(WaterTaxConstants.CURRENTYEAR_SECOND_HALF).getFromDate()))
                    currentInstDemand = currentInstDemand.add(dmdDet.getAmount());
            if (ConnectionStatus.ACTIVE.equals(waterConnectionDetails.getConnectionStatus()))
                createAdvanceBillDetails(billDetails, currentInstDemand, demand, billObj, advanceInstallments,
                        currInstallments.get(WaterTaxConstants.CURRENTYEAR_SECOND_HALF));
        }
        return billDetails;
    }

    /**
     * Creates the advance bill details
     *
     * @param billDetails
     * @param orderMap
     * @param currentInstallmentDemand
     * @param demandDetail
     * @param reason
     * @param installment
     */
    private void createAdvanceBillDetails(final List<EgBillDetails> billDetails,
            final BigDecimal currentInstallmentDemand, final EgDemand demand, final Billable billable,
            final List<Installment> advanceInstallments, final Installment dmdDetInstallment) {

        /*
         * Advance will be created with current year second half installment.
         * While fetching advance collection, we will pass current year second
         * half installment
         */
        BigDecimal advanceCollection = demandGenericDAO.getBalanceByDmdMasterCodeInst(demand,
                WaterTaxConstants.DEMANDRSN_CODE_ADVANCE,
                moduleService.getModuleByName(WaterTaxConstants.EGMODULE_NAME), dmdDetInstallment);
        final CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(new Date());

        if (advanceCollection.compareTo(BigDecimal.ZERO) < 0)
            advanceCollection = advanceCollection.abs();
        BigDecimal partiallyCollectedAmount = BigDecimal.ZERO;
        if (currentInstallmentDemand.compareTo(BigDecimal.ZERO) > 0)
            partiallyCollectedAmount = advanceCollection.remainder(currentInstallmentDemand);
        if (currentInstallmentDemand.compareTo(BigDecimal.ZERO) > 0) {
            final Integer noOfAdvancesPaid = advanceCollection.subtract(partiallyCollectedAmount)
                    .divide(currentInstallmentDemand).intValue();

            LOGGER.debug("getBilldetails - advanceCollection = " + advanceCollection + ", noOfAdvancesPaid="
                    + noOfAdvancesPaid);

            // DateTime installmentDate = null;
            Installment installment = null;
            int j = billDetails.size() + 1;
            if (noOfAdvancesPaid < WaterTaxConstants.MAX_ADVANCES_ALLOWED)
                for (int i = noOfAdvancesPaid; i < advanceInstallments.size(); i++) {
                    installment = advanceInstallments.get(i);
                    // installmentDate = new
                    // DateTime(installment.getInstallmentYear().getTime());
                    final EgDemandReason reasonmaster = connectionDemandService
                            .getDemandReasonByCodeAndInstallment(WaterTaxConstants.DEMANDRSN_CODE_ADVANCE, installment);
                    if (reasonmaster != null) {
                        final EgBillDetails billdetail = new EgBillDetails();
                        billdetail.setDrAmount(BigDecimal.ZERO);
                        billdetail.setCrAmount(currentInstallmentDemand);

                        billdetail.setGlcode(WaterTaxConstants.GLCODE_FOR_ADVANCE);
                        billdetail.setEgDemandReason(reasonmaster);
                        billdetail.setCreateDate(new Date());
                        billdetail.setModifiedDate(new Date());
                        j = billDetails.size() + 1;
                        billdetail.setOrderNo(j);
                        billdetail.setDescription(reasonmaster.getEgDemandReasonMaster().getReasonMaster() + " - "
                                + installment.getDescription());
                        if (billdetail.getDescription().contains(WaterTaxConstants.DEMANDRSN_REASON_ADVANCE))
                            billdetail.setPurpose(PURPOSE.ADVANCE_AMOUNT.toString());
                        else if (billdetail.getEgDemandReason().getEgInstallmentMaster().getToDate()
                                .compareTo(finYear.getStartingDate()) < 0)
                            billdetail.setPurpose(PURPOSE.ARREAR_AMOUNT.toString());
                        else if (billdetail.getEgDemandReason().getEgInstallmentMaster().getFromDate()
                                .compareTo(finYear.getStartingDate()) >= 0
                                && billdetail.getEgDemandReason().getEgInstallmentMaster().getToDate()
                                        .compareTo(finYear.getEndingDate()) >= 0)
                            billdetail.setPurpose(PURPOSE.CURRENT_AMOUNT.toString());
                        else
                            billdetail.setPurpose(PURPOSE.OTHERS.toString());
                        billdetail.setFunctionCode(STRING_WCMS_FUCNTION_CODE);
                        billdetail.setAdditionalFlag(0);
                        billDetails.add(billdetail);
                    }
                }
        } else
            LOGGER.debug("getBillDetails - All advances are paid...");
    }

    @Override
    public void cancelBill() {

    }

    public EgBill updateBillWithLatest(final Long billId) {
        LOGGER.debug("updateBillWithLatest billId " + billId);
        final EgBill bill = egBillDAO.findById(billId, false);
        LOGGER.debug("updateBillWithLatest old bill " + bill);
        if (bill == null)
            throw new ApplicationRuntimeException("No bill found with bill reference no :" + billId);
        bill.getEgBillDetails().clear();
        final WaterConnectionBillable waterConnectionBillable = (WaterConnectionBillable) context
                .getBean("waterConnectionBillable");

        waterConnectionBillable
                .setWaterConnectionDetails(waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                        bill.getConsumerId().trim().toUpperCase(), ConnectionStatus.ACTIVE));
        final List<EgBillDetails> egBillDetails = getBilldetails(waterConnectionBillable);
        for (final EgBillDetails billDetail : egBillDetails) {
            bill.addEgBillDetails(billDetail);
            billDetail.setEgBill(bill);
        }
        LOGGER.debug("Bill update with bill details for water charges " + bill.getConsumerId() + " as billdetails "
                + egBillDetails);
        return bill;
    }

    /**
     * Fetches the list of installments for advance collections
     *
     * @param startDate
     * @return List of Installment
     */
    public List<Installment> getAdvanceInstallmentsList(final Date startDate) {
        List<Installment> advanceInstallments = new ArrayList<Installment>();
        final String query = "select inst from Installment inst where inst.module.name = '"
                + WaterTaxConstants.PROPERTY_MODULE_NAME
                + "' and inst.fromDate >= :startdate order by inst.fromDate asc ";
        advanceInstallments = getCurrentSession().createQuery(query).setParameter("startdate", startDate)
                .setMaxResults(WaterTaxConstants.MAX_ADVANCES_ALLOWED).list();
        return advanceInstallments;
    }

    public HashMap<String, Integer> generateOrderForDemandDetails(final Set<EgDemandDetails> demandDetails,
            final Billable billable, final List<Installment> advanceInstallments,
            final Map<String, Installment> currInstallments) {

        final Map<Date, String> instReasonMap = new TreeMap<Date, String>();
        final HashMap<String, Integer> orderMap = new HashMap<String, Integer>();
        Date key = null;
        DateTime dateTime = null;
        for (final Installment inst : advanceInstallments) {
            dateTime = new DateTime(inst.getInstallmentYear());

            key = getOrder(inst.getInstallmentYear(),
                    WaterTaxConstants.DEMAND_REASON_ORDER_MAP.get(WaterTaxConstants.DEMANDRSN_CODE_ADVANCE));

            instReasonMap.put(key, dateTime.getMonthOfYear() + "/" + dateTime.getYear() + "-"
                    + WaterTaxConstants.DEMANDRSN_CODE_ADVANCE);
        }
        int order = 1;
        final Map<String, Map<String, String>> installmentAndReason = new LinkedHashMap<String, Map<String, String>>();

        for (final Map.Entry<Date, String> entry : instReasonMap.entrySet()) {
            final String[] split = entry.getValue().split("-");
            if (installmentAndReason.get(split[0]) == null) {
                final Map<String, String> reason = new HashMap<String, String>();
                reason.put(split[1], entry.getValue());
                installmentAndReason.put(split[0], reason);
            } else
                installmentAndReason.get(split[0]).put(split[1], entry.getValue());
        }

        for (final String installmentYear : installmentAndReason.keySet())
            for (final String reasonCode : WaterTaxConstants.ORDERED_DEMAND_RSNS_LIST)
                if (installmentAndReason.get(installmentYear).get(reasonCode) != null)
                    orderMap.put(installmentAndReason.get(installmentYear).get(reasonCode), order++);

        return orderMap;
    }

    public Date getOrder(final Date date, final int reasonOrder) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, reasonOrder);
        return calendar.getTime();
    }
}
