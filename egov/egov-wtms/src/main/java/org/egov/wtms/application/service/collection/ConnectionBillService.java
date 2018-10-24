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
package org.egov.wtms.application.service.collection;

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
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.time.DateUtils.addYears;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_SANCTIONED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.BILLTYPE_MANUAL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.COLLECTION_STRING_SERVICE_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEMAND_ISHISTORY_N;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEMANDRSN_CODE_ADVANCE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEMANDRSN_REASON_ADVANCE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEMAND_REASON_ORDER_MAP;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ESTIMATIONCHARGES_SERVICE_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.GLCODE_FOR_ADVANCE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MAX_ADVANCES_ALLOWED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.METERED_CHARGES_REASON_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULETYPE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NON_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ORDERED_DEMAND_RSNS_LIST;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PROPERTY_MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAXREASONCODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_CHARGES_SERVICE_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_CONNECTION_CHARGE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.YEARLY;

@Service
@Transactional(readOnly = true)
public class ConnectionBillService extends BillServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionBillService.class);
    private static final String ACTIVE = "ACTIVE";
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
    public List<EgBillDetails> getBilldetails(Billable billObj) {
        List<EgBillDetails> billDetails = new ArrayList<>();
        EgDemand demand = billObj.getCurrentDemand();
        Date currentDate = new Date();
        Map<Installment, List<EgDemandDetails>> installmentWise = new HashMap<>();
        Set<Installment> sortedInstallmentSet = new TreeSet<>();
        DemandComparatorByOrderId demandComparatorByOrderId = new DemandComparatorByOrderId();
        List<EgDemandDetails> orderedDetailsList = new ArrayList<>();
        Installment currInstallment = connectionDemandService.getCurrentInstallment(MODULE_NAME, YEARLY, new Date());
        CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(new Date());

        getDemandDetailsInstallmentWise(billObj, demand, installmentWise, sortedInstallmentSet);
        for (Installment i : sortedInstallmentSet) {
            List<EgDemandDetails> installmentWiseDetails = installmentWise.get(i);
            Collections.sort(installmentWiseDetails, demandComparatorByOrderId);
            orderedDetailsList.addAll(installmentWiseDetails);
        }

        int i = 1;
        WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService.getWaterConnectionDetailsByDemand(demand);
        for (EgDemandDetails demandDetail : orderedDetailsList) {

            EgDemandReason reason = demandDetail.getEgDemandReason();
            Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();

            if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDebit().equalsIgnoreCase("N")
                    && (demandDetail.getAmount().subtract(demandDetail.getAmtRebate())).compareTo(demandDetail.getAmtCollected()) > 0) {
                EgBillDetails billdetail = new EgBillDetails();
                if (demandDetail.getAmount() != null) {
                    billdetail.setDrAmount(ZERO);
                    billdetail.setCrAmount(demandDetail.getAmount().subtract(demandDetail.getAmtCollected()));
                }
                if (LOG.isInfoEnabled())
                    LOG.info("demandDetail.getEgDemandReason() {}, glcodeerror {}",
                            demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(),
                            demandDetail.getEgDemandReason().getGlcodeId());
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
                if (waterConnectionDetails != null && waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED))
                    if (billdetail.getDescription().contains(DEMANDRSN_REASON_ADVANCE))
                        billdetail.setPurpose(PURPOSE.ADVANCE_AMOUNT.toString());
                    else if (billdetail.getEgDemandReason().getEgInstallmentMaster().getToDate().compareTo(finYear.getStartingDate()) < 0)
                        billdetail.setPurpose(PURPOSE.ARREAR_AMOUNT.toString());
                    else if (billdetail.getEgDemandReason().getEgInstallmentMaster().getFromDate().compareTo(finYear.getStartingDate()) >= 0
                            && billdetail.getEgDemandReason().getEgInstallmentMaster().getFromDate().compareTo(finYear.getEndingDate()) < 0)
                        billdetail.setPurpose(PURPOSE.CURRENT_AMOUNT.toString());
                    else
                        billdetail.setPurpose(PURPOSE.OTHERS.toString());
                if (waterConnectionDetails != null && waterConnectionDetails.getConnectionType().equals(ConnectionType.METERED))
                    billdetail.setPurpose(PURPOSE.OTHERS.toString());
                if (currInstallment != null && installment.getFromDate().before(currInstallment.getToDate()))
                    billdetail.setAdditionalFlag(1);
                else
                    billdetail.setAdditionalFlag(0);
                billDetails.add(billdetail);
            }
        }

        if (waterConnectionDetails != null && waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED)) {
            Map<String, Installment> currInstallments = new HashMap<>();
            Installment currFirstHalf = propertyTaxUtil
                    .getInstallmentsForCurrYear(new Date()).get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
            Installment currSecondHalf = propertyTaxUtil
                    .getInstallmentsForCurrYear(new Date()).get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);
            currInstallments.put(CURRENTYEAR_FIRST_HALF, currFirstHalf);
            currInstallments.put(CURRENTYEAR_SECOND_HALF, currSecondHalf);
            Date advanceStartDate = addYears(currInstallments.get(CURRENTYEAR_FIRST_HALF).getFromDate(), 1);

            List<Installment> advanceInstallments = getAdvanceInstallmentsList(advanceStartDate);
            BigDecimal currentInstDemand = ZERO;
            for (EgDemandDetails dmdDet : demand.getEgDemandDetails())
                if (dmdDet.getInstallmentStartDate().equals(currInstallments.get(CURRENTYEAR_SECOND_HALF).getFromDate()))
                    currentInstDemand = currentInstDemand.add(dmdDet.getAmount());
            if (ConnectionStatus.ACTIVE.equals(waterConnectionDetails.getConnectionStatus())
                    && WATERTAX_CHARGES_SERVICE_CODE.equals(billObj.getServiceCode()))
                createAdvanceBillDetails(billDetails, currentInstDemand, demand, billObj, advanceInstallments,
                        currInstallments.get(CURRENTYEAR_SECOND_HALF));
        }
        return billDetails;
    }

    private void getDemandDetailsInstallmentWise(Billable billObj, EgDemand demand,
                                                 Map<Installment, List<EgDemandDetails>> installmentWise,
                                                 Set<Installment> sortedInstallmentSet) {
        List<String> demandCodes = Arrays.asList(METERED_CHARGES_REASON_CODE, WATERTAXREASONCODE,
                DEMANDRSN_CODE_ADVANCE, WATERTAX_CONNECTION_CHARGE);
        for (EgDemandDetails demandDetail : demand.getEgDemandDetails()) {
            Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            boolean checkDemandReason = demandCodes.contains(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode());
            if (installmentWise.get(installment) == null
                    && ((ESTIMATIONCHARGES_SERVICE_CODE.equals(billObj.getServiceCode()) && !checkDemandReason)
                    || (WATERTAX_CHARGES_SERVICE_CODE.equals(billObj.getServiceCode()) && checkDemandReason))) {
                List<EgDemandDetails> detailsList = new ArrayList<>();
                detailsList.add(demandDetail);
                installmentWise.put(demandDetail.getEgDemandReason().getEgInstallmentMaster(), detailsList);
                sortedInstallmentSet.add(installment);
            } else {
                if ((ESTIMATIONCHARGES_SERVICE_CODE.equals(billObj.getServiceCode()) && !checkDemandReason)
                        || (WATERTAX_CHARGES_SERVICE_CODE.equals(billObj.getServiceCode()) && checkDemandReason)) {
                    installmentWise.get(demandDetail.getEgDemandReason().getEgInstallmentMaster()).add(demandDetail);
                }
            }
        }
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
    private void createAdvanceBillDetails(List<EgBillDetails> billDetails,
                                          BigDecimal currentInstallmentDemand, EgDemand demand, Billable billable,
                                          List<Installment> advanceInstallments, Installment dmdDetInstallment) {

        /*
         * Advance will be created with current year second half installment. While fetching advance collection, we will pass
         * current year second half installment
         */
        BigDecimal advanceCollection = demandGenericDAO.getBalanceByDmdMasterCodeInst(demand, DEMANDRSN_CODE_ADVANCE,
                moduleService.getModuleByName(MODULE_NAME), dmdDetInstallment);
        CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(new Date());

        if (advanceCollection.signum() < 0)
            advanceCollection = advanceCollection.abs();
        BigDecimal partiallyCollectedAmount = ZERO;
        if (currentInstallmentDemand.signum() > 0)
            partiallyCollectedAmount = advanceCollection.remainder(currentInstallmentDemand);
        if (currentInstallmentDemand.signum() > 0) {
            Integer noOfAdvancesPaid = advanceCollection.subtract(partiallyCollectedAmount).divide(currentInstallmentDemand).intValue();
            if (LOG.isDebugEnabled())
                LOG.debug("getBilldetails - advanceCollection = {}, noOfAdvancesPaid={}", advanceCollection, noOfAdvancesPaid);

            Installment installment ;
            int j;
            if (noOfAdvancesPaid < MAX_ADVANCES_ALLOWED)
                for (int i = noOfAdvancesPaid; i < advanceInstallments.size(); i++) {
                    installment = advanceInstallments.get(i);
                    EgDemandReason reasonmaster = connectionDemandService.getDemandReasonByCodeAndInstallment(
                            DEMANDRSN_CODE_ADVANCE, installment);
                    if (reasonmaster != null) {
                        EgBillDetails billdetail = new EgBillDetails();
                        billdetail.setDrAmount(ZERO);
                        billdetail.setCrAmount(currentInstallmentDemand);

                        billdetail.setGlcode(GLCODE_FOR_ADVANCE);
                        billdetail.setEgDemandReason(reasonmaster);
                        billdetail.setCreateDate(new Date());
                        billdetail.setModifiedDate(new Date());
                        j = billDetails.size() + 1;
                        billdetail.setOrderNo(j);
                        billdetail.setDescription(reasonmaster.getEgDemandReasonMaster().getReasonMaster() + " - "
                                + installment.getDescription());
                        if (billdetail.getDescription().contains(DEMANDRSN_REASON_ADVANCE))
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
        } else if (LOG.isDebugEnabled())
            LOG.debug("getBillDetails - All advances are paid...");
    }

    @Override
    public void cancelBill() {

    }

    public EgBill updateBillWithLatest(Long billId) {
        if (LOG.isDebugEnabled())
            LOG.debug("updateBillWithLatest billId  = {}", billId);
        EgBill bill = egBillDAO.findById(billId, false);
        if (LOG.isDebugEnabled())
            LOG.debug("updateBillWithLatest old bill = {}", bill);
        if (bill == null)
            throw new ApplicationRuntimeException("No bill found with bill reference no :" + billId);
        bill.getEgBillDetails().clear();
        WaterConnectionBillable waterConnectionBillable = (WaterConnectionBillable) context.getBean("waterConnectionBillable");

        waterConnectionBillable.setWaterConnectionDetails(
                waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                        bill.getConsumerId().trim().toUpperCase(), ConnectionStatus.ACTIVE));
        List<EgBillDetails> egBillDetails = getBilldetails(waterConnectionBillable);
        for (EgBillDetails billDetail : egBillDetails) {
            bill.addEgBillDetails(billDetail);
            billDetail.setEgBill(bill);
        }
        if (LOG.isDebugEnabled())
            LOG.debug("Bill update with bill details for water charges {} as billdetails {}", bill.getConsumerId(), egBillDetails);
        return bill;
    }

    /**
     * Fetches the list of installments for advance collections
     *
     * @param startDate
     * @return List of Installment
     */
    @SuppressWarnings("unchecked")
    public List<Installment> getAdvanceInstallmentsList(Date startDate) {
        List<Installment> advanceInstallments;
        String query = "select inst from Installment inst where inst.module.name = '" + PROPERTY_MODULE_NAME
                + "' and inst.fromDate >= :startdate order by inst.fromDate asc ";
        advanceInstallments = getCurrentSession().createQuery(query).setParameter("startdate", startDate)
                .setMaxResults(MAX_ADVANCES_ALLOWED).list();
        return advanceInstallments;
    }

    public Map<String, Integer> generateOrderForDemandDetails(Set<EgDemandDetails> demandDetails,
                                                              Billable billable, List<Installment> advanceInstallments,
                                                              Map<String, Installment> currInstallments) {

        Map<Date, String> instReasonMap = new TreeMap<>();
        HashMap<String, Integer> orderMap = new HashMap<>();
        Date key ;
        DateTime dateTime ;
        for (Installment inst : advanceInstallments) {
            dateTime = new DateTime(inst.getInstallmentYear());

            key = getOrder(inst.getInstallmentYear(), DEMAND_REASON_ORDER_MAP.get(DEMANDRSN_CODE_ADVANCE));

            instReasonMap.put(key, dateTime.getMonthOfYear() + "/" + dateTime.getYear() + "-" + DEMANDRSN_CODE_ADVANCE);
        }
        int order = 1;
        Map<String, Map<String, String>> installmentAndReason = new LinkedHashMap<>();

        for (Map.Entry<Date, String> entry : instReasonMap.entrySet()) {
            String[] split = entry.getValue().split("-");
            if (installmentAndReason.get(split[0]) == null) {
                Map<String, String> reason = new HashMap<>();
                reason.put(split[1], entry.getValue());
                installmentAndReason.put(split[0], reason);
            } else
                installmentAndReason.get(split[0]).put(split[1], entry.getValue());
        }

        for (String installmentYear : installmentAndReason.keySet())
            for (String reasonCode : ORDERED_DEMAND_RSNS_LIST)
                if (installmentAndReason.get(installmentYear).get(reasonCode) != null)
                    orderMap.put(installmentAndReason.get(installmentYear).get(reasonCode), order++);

        return orderMap;
    }

    public Date getOrder(Date date, int reasonOrder) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, reasonOrder);
        return calendar.getTime();
    }

    @SuppressWarnings("unchecked")
    public EgBill getBillByConsumerCode(String consumerCode) {
        EgBill egBill = null;
        StringBuilder queryString = new StringBuilder();

        queryString.append(
                " select distinct bill From EgBill bill,EgBillType billtype,WaterConnection conn,WaterConnectionDetails connDet,EgwStatus status,WaterDemandConnection conndem  , EgDemand demd ")
                .append("where billtype.id=bill.egBillType and billtype.code=:billType")
                .append(" and bill.consumerId = conn.consumerCode ")
                .append(" and conn.id=connDet.connection ")
                .append(" and connDet.id=conndem.waterConnectionDetails ")
                .append(" and demd.id=bill.egDemand ")
                .append(" and demd.id=conndem.demand ")
                .append(" and connDet.connectionType=:connectionType")
                .append(" and demd.isHistory =:isDemandHistory")
                .append(" and bill.is_Cancelled=:isCancelled")
                .append(" and bill.serviceCode=:serviceCode")
                .append(" and connDet.connectionStatus=:connectionStatus")
                .append(" and connDet.status=status.id ")
                .append(" and status.moduletype=:moduleType")
                .append(" and status.code=:statusCode")
                .append(" and conn.consumerCode =:consumerCode ")
                .append(" order by bill.id desc ");

        Query query = getCurrentSession().createQuery(queryString.toString())
                .setString("consumerCode", consumerCode)
                .setString("billType", BILLTYPE_MANUAL)
                .setString("connectionType", NON_METERED)
                .setString("isDemandHistory", DEMAND_ISHISTORY_N)
                .setString("isCancelled", DEMAND_ISHISTORY_N)
                .setString("serviceCode", COLLECTION_STRING_SERVICE_CODE)
                .setString("moduleType", MODULETYPE)
                .setString("connectionStatus", ACTIVE)
                .setString("statusCode", APPLICATION_STATUS_SANCTIONED);

        List<EgBill> egBilltemp = query.list();
        if (!egBilltemp.isEmpty())
            egBill = egBilltemp.get(0);
        if (LOG.isDebugEnabled())
            LOG.debug("query to get Bill for is {}. for consumer No {}. ", queryString, consumerCode);
        return egBill;
    }
}
