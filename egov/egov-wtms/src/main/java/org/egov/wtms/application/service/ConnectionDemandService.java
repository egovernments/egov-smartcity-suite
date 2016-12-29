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
package org.egov.wtms.application.service;

import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;

import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.DemandDetail;
import org.egov.wtms.application.entity.FieldInspectionDetails;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.entity.WaterDemandConnection;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.rest.WaterChargesDetails;
import org.egov.wtms.application.rest.WaterTaxDue;
import org.egov.wtms.application.service.collection.ConnectionBillService;
import org.egov.wtms.application.service.collection.WaterConnectionBillable;
import org.egov.wtms.autonumber.BillReferenceNumberGenerator;
import org.egov.wtms.autonumber.MeterDemandNoticeNumberGenerator;
import org.egov.wtms.masters.entity.DonationDetails;
import org.egov.wtms.masters.entity.DonationHeader;
import org.egov.wtms.masters.entity.WaterRatesDetails;
import org.egov.wtms.masters.entity.WaterRatesHeader;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.DonationDetailsService;
import org.egov.wtms.masters.service.DonationHeaderService;
import org.egov.wtms.masters.service.WaterRatesDetailsService;
import org.egov.wtms.masters.service.WaterRatesHeaderService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConnectionDemandService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private DonationDetailsService donationDetailsService;

    @Autowired
    private DonationHeaderService donationHeaderService;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private InstallmentHibDao installmentDao;

    @Autowired
    private DemandGenericDao demandGenericDao;

    @Autowired
    private WaterConnectionService waterConnectionService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private WaterDemandConnectionService waterDemandConnectionService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EgBillDao egBillDAO;

    @Autowired
    private ConnectionBillService connectionBillService;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @Autowired
    private WaterRatesDetailsService waterRatesDetailsService;

    @Autowired
    private WaterRatesHeaderService waterRatesHeaderService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public EgDemand createDemand(final WaterConnectionDetails waterConnectionDetails) {
        final Map<String, Object> feeDetails = new HashMap<String, Object>();
        DonationDetails donationDetails = null;
        final FieldInspectionDetails fieldInspectionDetails = waterConnectionDetails.getFieldInspectionDetails();
        EgDemand egDemand = null;
        if (null != fieldInspectionDetails) {
            feeDetails.put(WaterTaxConstants.WATERTAX_SECURITY_CHARGE, fieldInspectionDetails.getSecurityDeposit());
            feeDetails.put(WaterTaxConstants.WATERTAX_ROADCUTTING_CHARGE,
                    fieldInspectionDetails.getRoadCuttingCharges());
            feeDetails.put(WaterTaxConstants.WATERTAX_SUPERVISION_CHARGE,
                    fieldInspectionDetails.getSupervisionCharges());
        }
        
        // (!WaterTaxConstants.BPL_CATEGORY.equalsIgnoreCase(waterConnectionDetails.getCategory().getCode()))
        if (!WaterTaxConstants.CHANGEOFUSE.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            donationDetails = getDonationDetails(waterConnectionDetails);

        if (donationDetails != null) {
            feeDetails.put(WaterTaxConstants.WATERTAX_DONATION_CHARGE, donationDetails.getAmount());
            waterConnectionDetails.setDonationCharges(donationDetails.getAmount());
        }

        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                moduleService.getModuleByName(WaterTaxConstants.EGMODULE_NAME), new Date(), WaterTaxConstants.YEARLY);
        // Not updating demand amount collected for new connection as per the
        // discussion.
        // double totalFee = 0.0;
        if (installment != null) {
            final Set<EgDemandDetails> dmdDetailSet = new HashSet<EgDemandDetails>();
            for (final String demandReason : feeDetails.keySet())
                dmdDetailSet.add(createDemandDetails((Double) feeDetails.get(demandReason), demandReason, installment));
            // totalFee += (Double) feeDetails.get(demandReason);

            egDemand = new EgDemand();
            egDemand.setEgInstallmentMaster(installment);
            egDemand.getEgDemandDetails().addAll(dmdDetailSet);
            egDemand.setIsHistory("N");
            egDemand.setCreateDate(new Date());
            egDemand.setModifiedDate(new Date());
        } else
            throw new ValidationException("err.water.installment.not.found");
        return egDemand;
    }

    public DonationDetails getDonationDetails(final WaterConnectionDetails waterConnectionDetails) {
        DonationDetails donationDetails = null;
        final List<DonationHeader> donationHeaderTempList = donationHeaderService
                .findDonationDetailsByPropertyAndCategoryAndUsageandPipeSize(waterConnectionDetails.getPropertyType(),
                        waterConnectionDetails.getCategory(), waterConnectionDetails.getUsageType(),
                        waterConnectionDetails.getPipeSize().getSizeInInch(),
                        waterConnectionDetails.getPipeSize().getSizeInInch());
        for (final DonationHeader donationHeaderTemp : donationHeaderTempList) {
            donationDetails = donationDetailsService.findByDonationHeaderAndFromDateAndToDate(donationHeaderTemp,
                    new Date(), new Date());
            if (donationDetails != null)
                break;
        }
        return donationDetails;
    }

    private EgDemandDetails createDemandDetails(final Double amount, final String demandReason,
            final Installment installment) {
        final EgDemandReason demandReasonObj = getDemandReasonByCodeAndInstallment(demandReason, installment);
        final EgDemandDetails demandDetail = new EgDemandDetails();
        demandDetail.setAmount(BigDecimal.valueOf(amount));
        demandDetail.setAmtCollected(BigDecimal.ZERO);
        demandDetail.setAmtRebate(BigDecimal.ZERO);
        demandDetail.setEgDemandReason(demandReasonObj);
        demandDetail.setCreateDate(new Date());
        demandDetail.setModifiedDate(new Date());
        return demandDetail;
    }

    private EgDemandDetails createDemandDetailsrForDataEntry(final BigDecimal amount, final BigDecimal collectAmount,
            final String demandReason, final String installment, final DemandDetail demandTempObj,
            final WaterConnectionDetails waterConnectionDetails) {
        final Installment installObj = waterConnectionDetailsRepository
                .findInstallmentByDescription(WaterTaxConstants.PROPERTY_MODULE_NAME, installment);
        EgDemandDetails demandDetailBean = null;
        final EgDemandDetails demandDetailsObj = waterConnectionDetailsRepository
                .findEgDemandDetailById(demandTempObj.getId());
        final EgDemandReason demandReasonObj = getDemandReasonByCodeAndInstallment(demandReason, installObj);
        if (demandDetailsObj != null && demandTempObj.getId() != null) {
            demandDetailBean = demandDetailsObj;
            if (demandDetailsObj.getAmount().compareTo(amount) != 0)
                demandDetailBean.setAmount(amount);
            if (demandDetailsObj.getAmtCollected().compareTo(collectAmount) != 0)
                demandDetailBean.setAmtCollected(collectAmount);
            demandDetailBean.setEgDemandReason(demandReasonObj);
            demandDetailBean.setModifiedDate(new Date());
        } else {
            demandDetailBean = new EgDemandDetails();
            demandDetailBean.setAmount(amount);
            demandDetailBean.setAmtCollected(collectAmount);
            demandDetailBean.setAmtRebate(BigDecimal.ZERO);
            demandDetailBean.setEgDemandReason(demandReasonObj);
            demandDetailBean.setCreateDate(new Date());
            demandDetailBean.setModifiedDate(new Date());

        }
        return demandDetailBean;
    }

    public EgDemandReason getDemandReasonByCodeAndInstallment(final String demandReason,
            final Installment installment) {
        final Query demandQuery = getCurrentSession().getNamedQuery("DEMANDREASONBY_CODE_AND_INSTALLMENTID");
        demandQuery.setParameter(0, demandReason);
        demandQuery.setParameter(1, installment.getId());
        final EgDemandReason demandReasonObj = (EgDemandReason) demandQuery.uniqueResult();
        return demandReasonObj;
    }

    public HashMap<String, Double> getSplitFee(final WaterConnectionDetails waterConnectionDetails) {
        final EgDemand demand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        final HashMap<String, Double> splitAmount = new HashMap<>();
        if (demand != null && demand.getEgDemandDetails() != null && demand.getEgDemandDetails().size() > 0)
            for (final EgDemandDetails detail : demand.getEgDemandDetails())
                if (WaterTaxConstants.WATERTAX_FIELDINSPECTION_CHARGE
                        .equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WaterTaxConstants.WATERTAX_FIELDINSPECTION_CHARGE,
                            detail.getAmount().doubleValue());
                else if (WaterTaxConstants.WATERTAX_DONATION_CHARGE
                        .equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WaterTaxConstants.WATERTAX_DONATION_CHARGE, detail.getAmount().doubleValue());
                else if (WaterTaxConstants.WATERTAX_SECURITY_CHARGE
                        .equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WaterTaxConstants.WATERTAX_SECURITY_CHARGE, detail.getAmount().doubleValue());
                else if (WaterTaxConstants.WATERTAX_ROADCUTTING_CHARGE
                        .equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WaterTaxConstants.WATERTAX_ROADCUTTING_CHARGE, detail.getAmount().doubleValue());
                else if (WaterTaxConstants.WATERTAX_SUPERVISION_CHARGE
                        .equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WaterTaxConstants.WATERTAX_SUPERVISION_CHARGE, detail.getAmount().doubleValue());
        return splitAmount;
    }

    public WaterTaxDue getDueDetailsByConsumerCode(final String consumerCode) {
        final WaterTaxDue waterTaxDue = new WaterTaxDue();
        final List<String> consumerCodes = new ArrayList<>();
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumberOrConsumerCode(consumerCode);
        if (null != waterConnectionDetails) {
            getDueInfo(waterConnectionDetails);
            consumerCodes.add(waterConnectionDetails.getConnection().getConsumerCode());
            waterTaxDue.setConsumerCode(consumerCodes);
            waterTaxDue.setPropertyID(waterConnectionDetails.getConnection().getPropertyIdentifier());
            waterTaxDue.setConnectionCount(consumerCodes.size());
            waterTaxDue.setIsSuccess(true);
        } else {
            waterTaxDue.setIsSuccess(false);
            waterTaxDue.setConsumerCode(Collections.EMPTY_LIST);
            waterTaxDue.setConnectionCount(0);
            waterTaxDue.setErrorCode(WaterTaxConstants.CONSUMERCODE_NOT_EXIST_ERR_CODE);
            waterTaxDue.setErrorMessage(WaterTaxConstants.WTAXDETAILS_CONSUMER_CODE_NOT_EXIST_ERR_MSG_PREFIX
                    + consumerCode + WaterTaxConstants.WTAXDETAILS_NOT_EXIST_ERR_MSG_SUFFIX);
        }
        return waterTaxDue;
    }

    public WaterTaxDue getDueDetailsByPropertyId(final String propertyIdentifier) {
        BigDecimal arrDmd = new BigDecimal(0);
        BigDecimal arrColl = new BigDecimal(0);
        BigDecimal currDmd = new BigDecimal(0);
        BigDecimal currColl = new BigDecimal(0);
        BigDecimal totalDue = new BigDecimal(0);
        WaterTaxDue waterTaxDue = null;
        final List<WaterConnection> waterConnections = waterConnectionService
                .findByPropertyIdentifier(propertyIdentifier);
        if (waterConnections.isEmpty()) {
            waterTaxDue = new WaterTaxDue();
            waterTaxDue.setConsumerCode(Collections.EMPTY_LIST);
            waterTaxDue.setConnectionCount(0);
            waterTaxDue.setIsSuccess(false);
            waterTaxDue.setErrorCode(WaterTaxConstants.PROPERTYID_NOT_EXIST_ERR_CODE);
            waterTaxDue.setErrorMessage(WaterTaxConstants.WTAXDETAILS_PROPERTYID_NOT_EXIST_ERR_MSG_PREFIX
                    + propertyIdentifier + WaterTaxConstants.WTAXDETAILS_NOT_EXIST_ERR_MSG_SUFFIX);
        } else {
            waterTaxDue = new WaterTaxDue();
            final List<String> consumerCodes = new ArrayList<>();
            for (final WaterConnection connection : waterConnections)
                if (connection.getConsumerCode() != null) {
                    final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                            .findByConsumerCodeAndConnectionStatus(connection.getConsumerCode(),
                                    ConnectionStatus.ACTIVE);
                    if (waterConnectionDetails != null) {
                        waterTaxDue = getDueInfo(waterConnectionDetails);
                        waterTaxDue.setPropertyID(propertyIdentifier);
                        consumerCodes.add(connection.getConsumerCode());
                        arrDmd = arrDmd.add(waterTaxDue.getArrearDemand());
                        arrColl = arrColl.add(waterTaxDue.getArrearCollection());
                        currDmd = currDmd.add(waterTaxDue.getCurrentDemand());
                        currColl = currColl.add(waterTaxDue.getCurrentCollection());
                        totalDue = totalDue.add(waterTaxDue.getTotalTaxDue());
                    }
                }
            waterTaxDue.setArrearDemand(arrDmd);
            waterTaxDue.setArrearCollection(arrColl);
            waterTaxDue.setCurrentDemand(currDmd);
            waterTaxDue.setCurrentCollection(currColl);
            waterTaxDue.setTotalTaxDue(totalDue);
            waterTaxDue.setConsumerCode(consumerCodes);
            waterTaxDue.setConnectionCount(waterConnections.size());
            waterTaxDue.setIsSuccess(true);
        }
        return waterTaxDue;
    }

    public List<WaterChargesDetails> getWaterTaxDetailsByPropertyId(final String propertyIdentifier) {
        final List<WaterConnection> waterConnections = waterConnectionService
                .findByPropertyIdentifier(propertyIdentifier);
        final List<WaterChargesDetails> waterChargesDetailsList = new ArrayList<>();
        if (waterConnections.isEmpty())
            return waterChargesDetailsList;
        else {
            WaterChargesDetails waterChargesDetails = new WaterChargesDetails();
            for (final WaterConnection connection : waterConnections)
                if (connection.getConsumerCode() != null) {
                    WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                            .findByConsumerCodeAndConnectionStatus(connection.getConsumerCode(),
                                    ConnectionStatus.ACTIVE);
                    if (waterConnectionDetails != null)
                        waterChargesDetails = getWatertaxDetails(waterConnectionDetails, connection.getConsumerCode(),
                                propertyIdentifier);
                    else {
                        waterConnectionDetails = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(
                                connection.getConsumerCode(), ConnectionStatus.INACTIVE);
                        if (waterConnectionDetails != null)
                            waterChargesDetails = getWatertaxDetails(waterConnectionDetails,
                                    connection.getConsumerCode(), propertyIdentifier);
                    }
                    waterChargesDetailsList.add(waterChargesDetails);
                }
            return waterChargesDetailsList;
        }
    }

    public WaterChargesDetails getWatertaxDetails(final WaterConnectionDetails waterConnectionDetails,
            final String consumerCode, final String propertyIdentifier) {
        final WaterChargesDetails waterChargesDetails = new WaterChargesDetails();
        waterChargesDetails.setTotalTaxDue(getDueInfo(waterConnectionDetails).getTotalTaxDue());
        waterChargesDetails.setConnectionType(waterConnectionDetails.getConnectionType().toString());
        waterChargesDetails.setConsumerCode(consumerCode);
        waterChargesDetails.setPropertyID(propertyIdentifier);
        waterChargesDetails.setConnectionStatus(waterConnectionDetails.getConnectionStatus().toString());
        return waterChargesDetails;
    }

    private WaterTaxDue getDueInfo(final WaterConnectionDetails waterConnectionDetails) {
        final Map<String, BigDecimal> resultmap = getDemandCollMap(waterConnectionDetails);
        final WaterTaxDue waterTaxDue = new WaterTaxDue();
        if (null != resultmap && !resultmap.isEmpty()) {
            final BigDecimal currDmd = resultmap.get(WaterTaxConstants.CURR_DMD_STR);
            waterTaxDue.setCurrentDemand(currDmd);
            final BigDecimal arrDmd = resultmap.get(WaterTaxConstants.ARR_DMD_STR);
            waterTaxDue.setArrearDemand(arrDmd);
            final BigDecimal currCollection = resultmap.get(WaterTaxConstants.CURR_COLL_STR);
            waterTaxDue.setCurrentCollection(currCollection);
            final BigDecimal arrCollection = resultmap.get(WaterTaxConstants.ARR_COLL_STR);
            waterTaxDue.setArrearCollection(arrCollection);
            // Calculating tax dues
            final BigDecimal taxDue = currDmd.add(arrDmd).subtract(currCollection).subtract(arrCollection);
            waterTaxDue.setTotalTaxDue(taxDue);
        }
        return waterTaxDue;
    }

    public Map<String, BigDecimal> getDemandCollMap(final WaterConnectionDetails waterConnectionDetails) {
        final EgDemand currDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        Installment installment = null;
        List<Object> dmdCollList = new ArrayList<Object>(0);
        Installment currFirstHalf = null;
        Installment currSecondHalf = null;
        Integer instId = null;
        BigDecimal currDmd = BigDecimal.ZERO;
        BigDecimal arrDmd = BigDecimal.ZERO;
        BigDecimal currCollection = BigDecimal.ZERO;
        BigDecimal arrCollection = BigDecimal.ZERO;
        final Map<String, BigDecimal> retMap = new HashMap<String, BigDecimal>(0);
        if (currDemand != null)
            dmdCollList = getDmdCollAmtInstallmentWise(currDemand);
        currFirstHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                .get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
        currSecondHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date())
                .get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);
        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            instId = Integer.valueOf(listObj[1].toString());
            installment = installmentDao.findById(instId, false);
            if (currFirstHalf.equals(installment) || currSecondHalf.equals(installment)) {
                if (listObj[3] != null && new BigDecimal((Double) listObj[3]).compareTo(BigDecimal.ZERO) == 1)
                    currCollection = currCollection.add(new BigDecimal((Double) listObj[3]));
                currDmd = currDmd.add(new BigDecimal((Double) listObj[2]));
            } else if (listObj[2] != null) {
                arrDmd = arrDmd.add(new BigDecimal((Double) listObj[2]));
                if (new BigDecimal((Double) listObj[3]).compareTo(BigDecimal.ZERO) == 1)
                    arrCollection = arrCollection.add(new BigDecimal((Double) listObj[3]));
            }
        }
        retMap.put(WaterTaxConstants.CURR_DMD_STR, currDmd);
        retMap.put(WaterTaxConstants.ARR_DMD_STR, arrDmd);
        retMap.put(WaterTaxConstants.CURR_COLL_STR, currCollection);
        retMap.put(WaterTaxConstants.ARR_COLL_STR, arrCollection);
        return retMap;
    }

    public List<Object> getDmdCollAmtInstallmentWise(final EgDemand egDemand) {
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

    public List<Object> getDmdCollAmtInstallmentWiseUptoCurrentInstallmemt(final EgDemand egDemand,
            final WaterConnectionDetails waterConnectionDetails) {
        Installment currInstallment = null;
        if (waterConnectionDetails.getConnectionType().equals(ConnectionType.NON_METERED))
            currInstallment = getCurrentInstallment(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null,
                    new Date());
        else
            currInstallment = getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME, WaterTaxConstants.MONTHLY,
                    new Date());
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

    public List<Object> getDmdCollAmtInstallmentWiseUptoCurrentFinYear(final EgDemand egDemand,
            final WaterConnectionDetails waterConnectionDetails) {
        final CFinancialYear financialyear = financialYearDAO.getFinancialYearByDate(new Date());

        final StringBuffer strBuf = new StringBuffer(2000);
        strBuf.append(
                "select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, "
                        + "sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, "
                        + "eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id "
                        + "and dmdDet.id_demand =:dmdId and inst.start_date<=:currFinEndDate and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master "
                        + "group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
        final Query query = getCurrentSession().createSQLQuery(strBuf.toString())
                .setParameter("dmdId", egDemand.getId()).setParameter("currFinEndDate", financialyear.getEndingDate());
        return query.list();
    }

    public String generateBill(final String consumerCode, final String applicationTypeCode) {
        String collectXML = "";
        final SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        String currentInstallmentYear = null;
        final WaterConnectionBillable waterConnectionBillable = (WaterConnectionBillable) context
                .getBean("waterConnectionBillable");
        final WaterConnectionDetails waterConnectionDetails;
        final BillReferenceNumberGenerator billRefeNumber = beanResolver
                .getAutoNumberServiceFor(BillReferenceNumberGenerator.class);

        if (applicationTypeCode != null && (applicationTypeCode.equals(WaterTaxConstants.CHANGEOFUSE)
                || applicationTypeCode.equals(WaterTaxConstants.RECONNECTIONCONNECTION)))
            waterConnectionDetails = waterConnectionDetailsService
                    .findByApplicationNumberOrConsumerCodeAndStatus(consumerCode, ConnectionStatus.ACTIVE);
        else
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(consumerCode);
        if (ConnectionStatus.INPROGRESS.equals(waterConnectionDetails.getConnectionStatus()))
            currentInstallmentYear = formatYear
                    .format(getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME, WaterTaxConstants.YEARLY, new Date())
                            .getInstallmentYear());
        else if (ConnectionStatus.ACTIVE.equals(waterConnectionDetails.getConnectionStatus())
                && ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType()))
            currentInstallmentYear = formatYear
                    .format(getCurrentInstallment(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null, new Date())
                            .getInstallmentYear());
        else if (ConnectionStatus.ACTIVE.equals(waterConnectionDetails.getConnectionStatus())
                && ConnectionType.METERED.equals(waterConnectionDetails.getConnectionType()))
            currentInstallmentYear = formatYear.format(
                    getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME, WaterTaxConstants.MONTHLY, new Date())
                            .getInstallmentYear());
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        waterConnectionBillable.setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionBillable.setAssessmentDetails(assessmentDetails);
        waterConnectionBillable.setUserId(ApplicationThreadLocals.getUserId());

        waterConnectionBillable.setReferenceNumber(billRefeNumber.generateBillNumber(currentInstallmentYear));
        waterConnectionBillable.setBillType(getBillTypeByCode(WaterTaxConstants.BILLTYPE_AUTO));

        final String billXml = connectionBillService.getBillXML(waterConnectionBillable);
        try {
            collectXML = URLEncoder.encode(billXml, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new ApplicationRuntimeException(e.getMessage());
        }
        return collectXML;
    }

    public EgBillType getBillTypeByCode(final String typeCode) {
        final EgBillType billType = egBillDAO.getBillTypeByCode(typeCode);
        return billType;
    }

    public EgDemand getDemandByInstAndApplicationNumber(final Installment installment, final String consumerCode) {

        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsRepository
                .findByApplicationNumberAndInstallment(installment, consumerCode);

        return waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();

    }

    /**
     * @param waterConnectionDetails
     * @param billAmount
     * @param currentDate
     * @return Updates WaterConnectionDetails after Meter Entry Demand
     *         Calculettion and Update Previous Bill and Generates New Bill
     */
    @Transactional
    public WaterConnectionDetails updateDemandForMeteredConnection(final WaterConnectionDetails waterConnectionDetails,
            final BigDecimal billAmount, final Date currentDate) {
        final Installment installment = getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME,
                WaterTaxConstants.MONTHLY, currentDate);
        if (installment != null) {
            final EgDemand demandObj = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
            final Set<EgDemandDetails> dmdDetailSet = new HashSet<EgDemandDetails>();
            dmdDetailSet.add(createDemandDetails(Double.parseDouble(billAmount.toString()),
                    WaterTaxConstants.WATERTAXREASONCODE, installment));
            demandObj.setBaseDemand(demandObj.getBaseDemand().add(billAmount));
            demandObj.setEgInstallmentMaster(installment);
            demandObj.getEgDemandDetails().addAll(dmdDetailSet);
            demandObj.setModifiedDate(new Date());
            if (demandObj.getId() != null && waterDemandConnectionService
                    .findByWaterConnectionDetailsAndDemand(waterConnectionDetails, demandObj) == null) {
                final WaterDemandConnection waterdemandConnection = new WaterDemandConnection();
                waterdemandConnection.setDemand(demandObj);
                waterdemandConnection.setWaterConnectionDetails(waterConnectionDetails);
                waterConnectionDetails.addWaterDemandConnection(waterdemandConnection);
                waterDemandConnectionService.createWaterDemandConnection(waterdemandConnection);
            }
            final List<EgBill> billlist = demandGenericDao.getAllBillsForDemand(demandObj, "N", "N");
            if (!billlist.isEmpty()) {
                final EgBill billObj = billlist.get(0);
                billObj.setIs_History("Y");
                billObj.setModifiedDate(new Date());
                egBillDAO.create(billObj);
            }
            generateBillForMeterAndMonthly(waterConnectionDetails.getConnection().getConsumerCode());
        } else
            throw new ValidationException("err.water.meteredinstallment.not.found");
        return waterConnectionDetails;
    }

    /**
     * @param waterConnectionDetails
     * @param demandDeatilslist
     * @return creation or updating demand and demanddetails for data Entry
     *         Screen
     */
    @Transactional
    public WaterConnectionDetails updateDemandForNonMeteredConnectionDataEntry(
            final WaterConnectionDetails waterConnectionDetails, final String sourceChannel) {
        EgDemand demandObj = null;
        propertyTaxUtil.getInstallmentsForCurrYear(new Date()).get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);
        if (waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand() == null)
            demandObj = new EgDemand();
        else

            demandObj = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        final Set<EgDemandDetails> dmdDetailSet = new HashSet<EgDemandDetails>();
        for (final DemandDetail demanddetailBean : waterConnectionDetails.getDemandDetailBeanList())
            if (demanddetailBean.getActualAmount().compareTo(BigDecimal.ZERO) == 1
                    && demanddetailBean.getActualCollection().compareTo(BigDecimal.ZERO) >= 0
                    && demanddetailBean.getActualCollection().compareTo(demanddetailBean.getActualAmount()) < 1) {
                demandObj.setBaseDemand(getTotalAmountForBaseDemand(demanddetailBean, demandObj.getBaseDemand()));
                demandObj.setAmtCollected(
                        getTotalCollectedAmountForDemand(demanddetailBean, demandObj.getAmtCollected()));
                dmdDetailSet.add(createDemandDetailsrForDataEntry(demanddetailBean.getActualAmount(),
                        demanddetailBean.getActualCollection(), demanddetailBean.getReasonMaster(),
                        demanddetailBean.getInstallment(), demanddetailBean, waterConnectionDetails));
            }
        demandObj.getEgDemandDetails().clear();
        demandObj.getEgDemandDetails().addAll(dmdDetailSet);
        final int listlength = demandObj.getEgDemandDetails().size() - 1;
        final Installment installObj = waterConnectionDetailsRepository.findInstallmentByDescription(
                WaterTaxConstants.PROPERTY_MODULE_NAME,
                waterConnectionDetails.getDemandDetailBeanList().get(listlength).getInstallment());
        demandObj.setEgInstallmentMaster(installObj);
        demandObj.setModifiedDate(new Date());
        if (demandObj.getIsHistory() == null)
            demandObj.setIsHistory("N");
        if (demandObj.getCreateDate() == null)
            demandObj.setCreateDate(new Date());
        if (demandObj.getId() == null) {
            final WaterDemandConnection waterdemandConnection = new WaterDemandConnection();
            waterdemandConnection.setDemand(demandObj);
            waterdemandConnection.setWaterConnectionDetails(waterConnectionDetails);
            waterConnectionDetails.addWaterDemandConnection(waterdemandConnection);
            waterDemandConnectionService.createWaterDemandConnection(waterdemandConnection);
        }

        waterConnectionDetailsService.updateIndexes(waterConnectionDetails, sourceChannel);
        return waterConnectionDetails;
    }

    public BigDecimal getTotalAmountForBaseDemand(final DemandDetail demanddetailBean,
            final BigDecimal baseDemandAmount) {
        BigDecimal currentTotalAmount = BigDecimal.ZERO;
        final EgDemandDetails demandDetailsObj = waterConnectionDetailsRepository
                .findEgDemandDetailById(demanddetailBean.getId());
        if (demanddetailBean.getId() == null)
            currentTotalAmount = baseDemandAmount.add(demanddetailBean.getActualAmount());
        else if (demanddetailBean.getActualAmount().compareTo(demandDetailsObj.getAmount()) == -1) {
            final BigDecimal diffExtraless = demandDetailsObj.getAmount().subtract(demanddetailBean.getActualAmount());
            currentTotalAmount = baseDemandAmount.subtract(diffExtraless);
        } else if (demanddetailBean.getActualAmount().compareTo(demandDetailsObj.getAmount()) == 1) {
            final BigDecimal diffExtra = demanddetailBean.getActualAmount().subtract(demandDetailsObj.getAmount());
            currentTotalAmount = baseDemandAmount.add(diffExtra);
        } else if (demanddetailBean.getActualAmount().compareTo(demandDetailsObj.getAmount()) == 0)
            currentTotalAmount = baseDemandAmount;
        return currentTotalAmount;

    }

    public BigDecimal getTotalCollectedAmountForDemand(final DemandDetail demanddetailBean,
            final BigDecimal demandAmountCollected) {
        BigDecimal currentTotalAmount = BigDecimal.ZERO;
        final EgDemandDetails demandDetailsObj = waterConnectionDetailsRepository
                .findEgDemandDetailById(demanddetailBean.getId());
        if (demanddetailBean.getId() == null)
            currentTotalAmount = demandAmountCollected.add(demanddetailBean.getActualCollection());
        else if (demanddetailBean.getActualCollection().compareTo(demandDetailsObj.getAmtCollected()) == -1) {
            final BigDecimal diffExtraless = demandDetailsObj.getAmtCollected()
                    .subtract(demanddetailBean.getActualCollection());
            currentTotalAmount = demandAmountCollected.subtract(diffExtraless);
        } else if (demanddetailBean.getActualCollection().compareTo(demandDetailsObj.getAmtCollected()) == 1) {
            final BigDecimal diffExtra = demanddetailBean.getActualCollection()
                    .subtract(demandDetailsObj.getAmtCollected());
            currentTotalAmount = demandAmountCollected.add(diffExtra);
        } else if (demanddetailBean.getActualCollection().compareTo(demandDetailsObj.getAmtCollected()) == 0)
            currentTotalAmount = demandAmountCollected;
        return currentTotalAmount;

    }

    /**
     * @param consumerCode
     * @return Generates Eg_bill Entry and saved with Demand and As of now we
     *         are generating Bill and its in XML format because no Method to
     *         just to generate Bill and Save as of now in
     *         connectionBillService.
     */
    @Transactional
    public String generateBillForMeterAndMonthly(final String consumerCode) {

        final WaterConnectionBillable waterConnectionBillable = (WaterConnectionBillable) context
                .getBean("waterConnectionBillable");
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.ACTIVE);
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
        final MeterDemandNoticeNumberGenerator meterDemandNotice = beanResolver
                .getAutoNumberServiceFor(MeterDemandNoticeNumberGenerator.class);

        waterConnectionBillable.setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionBillable.setAssessmentDetails(assessmentDetails);
        waterConnectionBillable.setUserId(ApplicationThreadLocals.getUserId());
        waterConnectionBillable.setReferenceNumber(meterDemandNotice.generateMeterDemandNoticeNumber());
        waterConnectionBillable.setBillType(getBillTypeByCode(WaterTaxConstants.BILLTYPE_MANUAL));

        final String billObj = connectionBillService.getBillXML(waterConnectionBillable);

        return billObj;
    }

    public WaterConnectionDetails updateDemandForNonmeteredConnection(
            final WaterConnectionDetails waterConnectionDetails, Installment installment,
            final Boolean reconnInSameInstallment) throws ValidationException {
        Date InstallemntStartDate = null;
        if (installment == null) {
            installment = getCurrentInstallment(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null, new Date());
            InstallemntStartDate = new Date();
        } else if (reconnInSameInstallment)
            InstallemntStartDate = installment.getFromDate();
        else
            InstallemntStartDate = waterConnectionDetails.getReconnectionApprovalDate();
        double totalWaterRate = 0;
        final WaterRatesDetails waterRatesDetails = getWaterRatesDetailsForDemandUpdate(waterConnectionDetails);
        final int noofmonths = DateUtils.noOfMonths(InstallemntStartDate, installment.getToDate());
        if (null != waterRatesDetails) {
            if (noofmonths > 0)
                totalWaterRate = waterRatesDetails.getMonthlyRate() * (noofmonths + 1);
            else
                totalWaterRate = waterRatesDetails.getMonthlyRate();

            final EgDemand demand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
            final EgDemandDetails demandDetails = createDemandDetails(totalWaterRate,
                    WaterTaxConstants.WATERTAXREASONCODE, installment);
            demand.setBaseDemand(BigDecimal.valueOf(totalWaterRate));
            demand.setEgInstallmentMaster(installment);
            demand.getEgDemandDetails().add(demandDetails);
            demand.setModifiedDate(new Date());
            if (demand.getId() != null && waterDemandConnectionService
                    .findByWaterConnectionDetailsAndDemand(waterConnectionDetails, demand) == null) {
                final WaterDemandConnection waterdemandConnection = new WaterDemandConnection();
                waterdemandConnection.setDemand(demand);
                waterdemandConnection.setWaterConnectionDetails(waterConnectionDetails);
                waterConnectionDetails.addWaterDemandConnection(waterdemandConnection);
                waterDemandConnectionService.createWaterDemandConnection(waterdemandConnection);
            }

        } else
            throw new ValidationException("err.water.rate.not.found");
        return waterConnectionDetails;
    }

    public WaterRatesDetails getWaterRatesDetailsForDemandUpdate(final WaterConnectionDetails waterConnectionDetails) {
        final List<WaterRatesHeader> waterRatesHeaderList = waterRatesHeaderService
                .findByConnectionTypeAndUsageTypeAndWaterSourceAndPipeSize(waterConnectionDetails.getConnectionType(),
                        waterConnectionDetails.getUsageType(), waterConnectionDetails.getWaterSource(),
                        waterConnectionDetails.getPipeSize());
        WaterRatesDetails waterRatesDetails = null;
        if (!waterRatesHeaderList.isEmpty())
            for (final WaterRatesHeader waterRatesHeadertemp : waterRatesHeaderList) {
                waterRatesDetails = waterRatesDetailsService
                        .findByWaterRatesHeaderAndFromDateAndToDate(waterRatesHeadertemp, new Date(), new Date());
                if (waterRatesDetails != null)
                    break;
            }
        return waterRatesDetails;
    }

    public Map<String, BigDecimal> getDemandCollMapForPtisIntegration(
            final WaterConnectionDetails waterConnectionDetails, final String moduleName,
            final String installmentType) {
        final EgDemand currDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        Installment installment = null;
        List<Object> dmdCollList = new ArrayList<Object>(0);
        Installment currInst = null;
        Integer instId = null;
        BigDecimal curDue = BigDecimal.ZERO;
        BigDecimal arrDue = BigDecimal.ZERO;

        BigDecimal arrearInstallmentfrom = BigDecimal.ZERO;
        final Map<String, BigDecimal> retMap = new HashMap<String, BigDecimal>(0);
        if (currDemand != null)
            dmdCollList = getDmdCollAmtInstallmentWiseWithIsDmdTrue(currDemand);
        currInst = getCurrentInstallment(moduleName, null, new Date());
        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            instId = Integer.valueOf(listObj[2].toString());
            installment = installmentDao.findById(instId, false);
            if (currInst.equals(installment))
                curDue = new BigDecimal(listObj[6].toString());
            else {
                arrDue = new BigDecimal(listObj[6].toString());
                if (arrDue.signum() > 0)
                    if (BigDecimal.ZERO == arrearInstallmentfrom || null == arrearInstallmentfrom)
                        arrearInstallmentfrom = BigDecimal.valueOf(instId);

            }
        }
        retMap.put(WaterTaxConstants.ARR_DUE, arrDue);
        retMap.put(WaterTaxConstants.CURR_DUE, curDue);
        retMap.put(WaterTaxConstants.ARR_INSTALFROM_STR, arrearInstallmentfrom);
        return retMap;
    }

    public List<Object> getDmdCollAmtInstallmentWiseWithIsDmdTrue(final EgDemand egDemand) {
        final StringBuffer strBuf = new StringBuffer(2000);
        strBuf.append(
                "SELECT wcdid,dmdResId,installment,amount,amt_collected,amt_rebate,amount-amt_collected AS balance,"
                        + "instStartDate FROM (SELECT wcd.id AS wcdid,dmdRes.id AS dmdResId,dmdRes.id_installment AS installment,"
                        + "SUM(dmdDet.amount) AS amount,SUM(dmdDet.amt_collected) AS amt_collected,SUM(dmdDet.amt_rebate) AS amt_rebate,"
                        + "inst.start_date AS inststartdate FROM eg_demand_details dmdDet,eg_demand_reason dmdRes,eg_installment_master inst,"
                        + "eg_demand_reason_master dmdresmas,egwtr_connectiondetails wcd WHERE dmdDet.id_demand_reason=dmdRes.id "
                        + "AND dmdDet.id_demand =:dmdId AND dmdRes.id_installment = inst.id AND dmdresmas.id = dmdres.id_demand_reason_master "
                        + "AND dmdresmas.isdemand=TRUE AND wcd.demand = dmdDet.id_demand GROUP BY dmdRes.id, dmdRes.id_installment,"
                        + "inst.start_date,wcd.id ORDER BY inst.start_date) AS dcb");
        return getCurrentSession().createSQLQuery(strBuf.toString()).setLong("dmdId", egDemand.getId()).list();
    }

    public Installment getCurrentInstallment(final String moduleName, final String installmentType, final Date date) {
        if (null == installmentType)
            return installmentDao.getInsatllmentByModuleForGivenDate(moduleService.getModuleByName(moduleName),
                    new Date());
        else
            return installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                    moduleService.getModuleByName(moduleName), date, installmentType);
    }

    public Map<String, BigDecimal> getDemandCollMapForBill(final WaterConnectionDetails waterConnectionDetails,
            final String moduleName, final String installmentType) {
        final EgDemand currDemand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        List<Object> dmdCollList = new ArrayList<Object>(0);
        Integer instId = null;
        Double balance = null;
        Integer val = null;
        final Map<String, BigDecimal> retMap = new HashMap<String, BigDecimal>(0);
        if (currDemand != null)
            dmdCollList = getDmdCollAmtInstallmentWiseWithIsDmdTrue(currDemand);
        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            balance = (Double) listObj[6];
            if (BigDecimal.valueOf(balance).signum() > 0) {
                val = Integer.valueOf(listObj[0].toString());
                instId = Integer.valueOf(listObj[2].toString());
                retMap.put("wcdid", BigDecimal.valueOf(val));
                retMap.put("inst", BigDecimal.valueOf(instId));
            }
        }
        return retMap;
    }

    /**
     * @param waterConnectionDetails
     * @param givenDate
     *            It Checks the Meter Entry Exist For the Entred Date Month and
     *            Returns True if It Exists and checks with Demand Current
     *            Installment
     */
    public Boolean meterEntryAllReadyExistForCurrentMonth(final WaterConnectionDetails waterConnectionDetails,
            final Date givenDate) {
        Boolean currrentInstallMentExist = false;

        final Installment installment = getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME,
                WaterTaxConstants.MONTHLY, givenDate);
        if (waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand() != null
                && waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand() != null)
            if (installment != null
                    && installment.getInstallmentNumber().equals(waterTaxUtils.getCurrentDemand(waterConnectionDetails)
                            .getDemand().getEgInstallmentMaster().getInstallmentNumber()))
                currrentInstallMentExist = true;
        return currrentInstallMentExist;
    }

    public List<Object> getDmdCollAmtInstallmentWiseUptoPreviousFinYear(final EgDemand egDemand,
            final WaterConnectionDetails waterConnectionDetails) {
        final CFinancialYear financialyear = financialYearDAO.getFinancialYearByDate(new Date());

        final StringBuffer strBuf = new StringBuffer(2000);
        strBuf.append(
                "select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, "
                        + "sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, "
                        + "eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id "
                        + "and dmdDet.id_demand =:dmdId and inst.start_date<:currFinStartDate and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master "
                        + "group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
        final Query query = getCurrentSession().createSQLQuery(strBuf.toString())
                .setParameter("dmdId", egDemand.getId())
                .setParameter("currFinStartDate", financialyear.getStartingDate());
        return query.list();
    }

    public Map<String, Installment> getInstallmentsForPreviousYear(final Date currDate) {
        final Map<String, Installment> currYearInstMap = new HashMap<String, Installment>();
        final String query = "select installment from Installment installment,CFinancialYear finYear where installment.module.name = '"
                + PTMODULENAME
                + "'  and cast(installment.toDate as date) <= cast(finYear.startingDate as date) order by installment.id desc";
        final Query qry = getCurrentSession().createQuery(query);
        final List<Installment> installments = qry.list();
        currYearInstMap.put(WaterTaxConstants.PREVIOUS_SECOND_HALF, installments.get(0));
        return currYearInstMap;
    }

}
