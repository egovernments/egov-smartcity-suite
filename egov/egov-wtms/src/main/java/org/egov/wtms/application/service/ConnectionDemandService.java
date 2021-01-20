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
package org.egov.wtms.application.service;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.toYearFormat;
import static org.egov.infra.utils.StringUtils.encodeURL;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.ACTIVE;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.INPROGRESS;
import static org.egov.wtms.masters.entity.enums.ConnectionType.METERED;
import static org.egov.wtms.masters.entity.enums.ConnectionType.NON_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.BPL_CATEGORY_DONATION_AMOUNT;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CATEGORY_BPL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEMAND_ISHISTORY_N;
import static org.egov.wtms.utils.constants.WaterTaxConstants.METERED_CHARGES_REASON_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MONTHLY;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PENALTYCHARGES;
import static org.egov.wtms.utils.constants.WaterTaxConstants.PROPERTY_MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAXREASONCODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_CHARGES_SERVICE_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_DONATION_CHARGE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_FIELDINSPECTION_CHARGE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_ROADCUTTING_CHARGE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_SECURITY_CHARGE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAX_SUPERVISION_CHARGE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATER_MATERIAL_CHARGES_REASON_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.YEARLY;
import static org.egov.wtms.utils.constants.WaterTaxConstants.RECONNECTION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
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
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.service.AppConfigService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.ConnectionEstimationDetails;
import org.egov.wtms.application.entity.DemandDetail;
import org.egov.wtms.application.entity.EstimationNotice;
import org.egov.wtms.application.entity.FieldInspectionDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.entity.WaterDemandConnection;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.service.collection.ConnectionBillService;
import org.egov.wtms.application.service.collection.WaterConnectionBillable;
import org.egov.wtms.autonumber.BillReferenceNumberGenerator;
import org.egov.wtms.autonumber.MeterDemandNoticeNumberGenerator;
import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.entity.DonationDetails;
import org.egov.wtms.masters.entity.DonationHeader;
import org.egov.wtms.masters.entity.WaterRatesDetails;
import org.egov.wtms.masters.entity.WaterRatesHeader;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.DemandComparatorByInstallmentStartDate;
import org.egov.wtms.masters.service.DonationDetailsService;
import org.egov.wtms.masters.service.DonationHeaderService;
import org.egov.wtms.masters.service.WaterRatesDetailsService;
import org.egov.wtms.masters.service.WaterRatesHeaderService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional(readOnly = true)
public class ConnectionDemandService {

    private static final String FIELD_INSPECTION = "fieldInspection";
    private static final String FROM_INSTALLMENT = "fromInstallment";
    private static final String TO_INSTALLMENT = "toInstallment";
    private static final String WATER_CHARGES = "Water Charges";
    private static final String DEMAND_ISHISTORY_Y = "Y";
    private static final String UPDATE_MATERIAL_DETAILS = "msg.ulb.material.detail.update";

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
    private RegulariseDemandGenerationImpl regulariseDemandGenImpl;

    @Autowired
    private AppConfigService appConfigService;
    
    @Autowired
    private EstimationNoticeService estimationNoticeService; 

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public EgDemand createDemand(WaterConnectionDetails waterConnectionDetails) {
        Map<String, Object> feeDetails = new HashMap<>();
        DonationDetails donationDetails = null;
        FieldInspectionDetails fieldInspectionDetails = waterConnectionDetails.getFieldInspectionDetails();
        EgDemand egDemand;
        if (fieldInspectionDetails != null) {
            feeDetails.put(WATERTAX_SECURITY_CHARGE, fieldInspectionDetails.getSecurityDeposit());
            feeDetails.put(WATERTAX_ROADCUTTING_CHARGE, fieldInspectionDetails.getRoadCuttingCharges());
            feeDetails.put(WATERTAX_SUPERVISION_CHARGE, fieldInspectionDetails.getSupervisionCharges());
            waterConnectionDetails.getFieldInspectionDetails().setEstimationCharges(fieldInspectionDetails.getSecurityDeposit()
                    + fieldInspectionDetails.getRoadCuttingCharges() + fieldInspectionDetails.getSupervisionCharges());
        }
        if (NON_METERED.equals(waterConnectionDetails.getConnectionType()) &&
                !WaterTaxConstants.CHANGEOFUSE.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            donationDetails = getDonationDetails(waterConnectionDetails);
        if (METERED.equals(waterConnectionDetails.getConnectionType())
                && BigDecimal.valueOf(waterConnectionDetails.getDonationCharges()).compareTo(ZERO) > 0)
            feeDetails.put(WATERTAX_DONATION_CHARGE, waterConnectionDetails.getDonationCharges());

        if (!REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()) &&
                CATEGORY_BPL.equalsIgnoreCase(waterConnectionDetails.getCategory().getName())) {
            AppConfig appConfig = null;
            appConfig = appConfigService.getAppConfigByModuleNameAndKeyName(MODULE_NAME, BPL_CATEGORY_DONATION_AMOUNT);
            if (appConfig != null && !appConfig.getConfValues().isEmpty())
                waterConnectionDetails.setDonationCharges(Long.valueOf(appConfig.getConfValues().get(0).getValue()));
            feeDetails.put(WATERTAX_DONATION_CHARGE, waterConnectionDetails.getDonationCharges());
        } else if (donationDetails != null) {
            feeDetails.put(WATERTAX_DONATION_CHARGE, donationDetails.getAmount());
            waterConnectionDetails.setDonationCharges(donationDetails.getAmount());
        }

        Installment installment = installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                moduleService.getModuleByName(MODULE_NAME), new Date(), YEARLY);
        // Not updating demand amount collected for new connection as per the
        // discussion.
        if (installment != null) {
            Set<EgDemandDetails> dmdDetailSet = new HashSet<>();
            for (Map.Entry<String, Object> feeDetail : feeDetails.entrySet())
                dmdDetailSet
                        .add(createDemandDetails((Double) feeDetails.get(feeDetail.getKey()), feeDetail.getKey(), installment));
            egDemand = createDemandObject(waterConnectionDetails, installment, dmdDetailSet);
        } else
            throw new ValidationException("err.water.installment.not.found");
        return egDemand;
    }

    public EgDemand createDemandObject(WaterConnectionDetails waterConnectionDetails, Installment installment,
            Set<EgDemandDetails> dmdDetailSet) {
        EgDemand egDemand = new EgDemand();
        BigDecimal rebateAmount = ZERO;
        BigDecimal baseDemand = ZERO;
        Set<EgDemandDetails> newDemandDetailSet;
        if (!REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()) &&
                CATEGORY_BPL.equalsIgnoreCase(waterConnectionDetails.getCategory().getName()))
            newDemandDetailSet = demandChangesForBplCategory(dmdDetailSet);
        else
            newDemandDetailSet = dmdDetailSet;
        for (EgDemandDetails demandDetails : newDemandDetailSet) {
            baseDemand = baseDemand.add(demandDetails.getAmount());
            if (!REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()) &&
                    CATEGORY_BPL.equalsIgnoreCase(waterConnectionDetails.getCategory().getName())
                    && !WATERTAX_DONATION_CHARGE
                            .equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                rebateAmount = rebateAmount.add(demandDetails.getAmtRebate());
        }

        egDemand.setBaseDemand(baseDemand);
        if (!REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            egDemand.addRebateAmt(rebateAmount);
        egDemand.setEgInstallmentMaster(installment);
        egDemand.getEgDemandDetails().addAll(newDemandDetailSet);
        egDemand.setIsHistory(DEMAND_ISHISTORY_N);
        egDemand.setCreateDate(new Date());
        egDemand.setModifiedDate(new Date());
        return egDemand;
    }

    public Set<EgDemandDetails> demandChangesForBplCategory(Set<EgDemandDetails> demandDetailsSet) {
        for (EgDemandDetails demandDetail : demandDetailsSet)
            if (WATERTAX_DONATION_CHARGE.equalsIgnoreCase(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode())) {
                AppConfig appConfig = null;
                appConfig = appConfigService.getAppConfigByModuleNameAndKeyName(MODULE_NAME, BPL_CATEGORY_DONATION_AMOUNT);
                if (appConfig != null)
                    demandDetail.setAmount(BigDecimal.valueOf(Long.valueOf(appConfig.getConfValues().get(0).getValue())));
            } else
                demandDetail.setAmtRebate(demandDetail.getAmount());
        return demandDetailsSet;
    }

    public DonationDetails getDonationDetails(WaterConnectionDetails waterConnectionDetails) {
        DonationDetails donationDetails = null;
        List<DonationHeader> donationHeaderTempList = donationHeaderService
                .findDonationDetailsByPropertyAndCategoryAndUsageandPipeSize(waterConnectionDetails.getPropertyType(),
                        waterConnectionDetails.getCategory(), waterConnectionDetails.getUsageType(),
                        waterConnectionDetails.getPipeSize().getSizeInInch(),
                        waterConnectionDetails.getPipeSize().getSizeInInch());
        for (DonationHeader donationHeaderTemp : donationHeaderTempList) {
            donationDetails = donationDetailsService.findByDonationHeaderAndFromDateAndToDate(donationHeaderTemp, new Date(),
                    new Date());
            if (donationDetails != null)
                break;
        }
        return donationDetails;
    }

    public EgDemandDetails createDemandDetails(Double amount, String demandReason, Installment installment) {
        EgDemandReason demandReasonObj = getDemandReasonByCodeAndInstallment(demandReason, installment);
        EgDemandDetails demandDetail = new EgDemandDetails();
        demandDetail.setAmount(BigDecimal.valueOf(amount));
        demandDetail.setAmtCollected(ZERO);
        demandDetail.setAmtRebate(ZERO);
        demandDetail.setEgDemandReason(demandReasonObj);
        demandDetail.setCreateDate(new Date());
        demandDetail.setModifiedDate(new Date());
        return demandDetail;
    }

    @Transactional
    public EgDemandDetails createDemandDetailsrForDataEntry(BigDecimal amount, BigDecimal collectAmount,
            String demandReason, String installment, DemandDetail demandTempObj,
            WaterConnectionDetails waterConnectionDetails, String mode) {
        Installment installObj;
        if (waterConnectionDetails.getConnectionType().equals(NON_METERED) && !FIELD_INSPECTION.equals(mode))
            installObj = installmentDao.getInsatllmentByModuleAndDescription(
                    moduleService.getModuleByName(PROPERTY_MODULE_NAME), installment);
        else
            installObj = installmentDao.getInsatllmentByModuleAndDescription(
                    moduleService.getModuleByName(MODULE_NAME), installment);

        EgDemandDetails demandDetailBean;
        EgDemandDetails demandDetailsObj = waterConnectionDetailsRepository.findEgDemandDetailById(demandTempObj.getId());
        EgDemandReason demandReasonObj = getDemandReasonByCodeAndInstallment(demandReason, installObj);

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
            demandDetailBean.setAmtRebate(ZERO);
            demandDetailBean.setEgDemandReason(demandReasonObj);
            demandDetailBean.setCreateDate(new Date());
            demandDetailBean.setModifiedDate(new Date());
        }
        return demandDetailBean;
    }

    
	public EgDemandReason getDemandReasonByCodeAndInstallment(String demandReason, Installment installment) {
		return (EgDemandReason) getCurrentSession()
				.createQuery(
						"select dr from org.egov.demand.model.EgDemandReason dr where dr.egDemandReasonMaster.code = :demandReason and dr.egInstallmentMaster.id = :installment")
				.setParameter("demandReason", demandReason).setParameter("installment", installment.getId()).getSingleResult();
		  
	}
    
    

    public Map<String, Double> getSplitFee(WaterConnectionDetails waterConnectionDetails) {
        EgDemand demand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        Map<String, Double> splitAmount = new HashMap<>();
        if (demand != null && demand.getEgDemandDetails() != null && !demand.getEgDemandDetails().isEmpty())
            for (EgDemandDetails detail : demand.getEgDemandDetails())
                if (WATERTAX_FIELDINSPECTION_CHARGE.equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WATERTAX_FIELDINSPECTION_CHARGE, detail.getAmount().doubleValue());
                else if (WATERTAX_DONATION_CHARGE.equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WATERTAX_DONATION_CHARGE, detail.getAmount().doubleValue());
                else if (WATERTAX_SECURITY_CHARGE.equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WATERTAX_SECURITY_CHARGE, detail.getAmount().doubleValue());
                else if (WATERTAX_ROADCUTTING_CHARGE.equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WATERTAX_ROADCUTTING_CHARGE, detail.getAmount().doubleValue());
                else if (WATERTAX_SUPERVISION_CHARGE.equals(detail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    splitAmount.put(WATERTAX_SUPERVISION_CHARGE, detail.getAmount().doubleValue());
        return splitAmount;
    }

    @SuppressWarnings("unchecked")
    public List<Object> getDmdCollAmtInstallmentWise(EgDemand egDemand, String effectiveDate) {
		StringBuilder queryBuilder = new StringBuilder(600);
		queryBuilder.append(
				"select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, ")
				.append("sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, ")
				.append("eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id ")
				.append("and dmdDet.id_demand =:dmdId and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master ");
		if (StringUtils.isNotBlank(effectiveDate)) 
			queryBuilder.append("and cast(inst.start_date as date)<=cast(:effectiveDate as date) ");
		
		queryBuilder.append("group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
		Query query = getCurrentSession().createSQLQuery(queryBuilder.toString()).setLong("dmdId", egDemand.getId());
		if (StringUtils.isNotBlank(effectiveDate))
			query = query.setString("effectiveDate", effectiveDate);
		return query.list();
        
    }

    @SuppressWarnings("unchecked")
    public List<Object> getDmdCollAmtInstallmentWiseUptoCurrentInstallmemt(EgDemand egDemand,
            WaterConnectionDetails waterConnectionDetails) {
        Installment currInstallment;
        if (NON_METERED.equals(waterConnectionDetails.getConnectionType()))
            currInstallment = getCurrentInstallment(PROPERTY_MODULE_NAME, null, new Date());
        else
            currInstallment = getCurrentInstallment(MODULE_NAME, MONTHLY, new Date());
        StringBuilder queryBuilder = new StringBuilder(500);
        queryBuilder
                .append("select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, ")
                .append("sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, ")
                .append("eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id ")
                .append("and dmdDet.id_demand =:dmdId and inst.start_date<=:currInstallmentDate and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master ")
                .append("group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
        Query query = getCurrentSession().createNativeQuery(queryBuilder.toString())
                .setParameter("dmdId", egDemand.getId())
                .setParameter("currInstallmentDate", currInstallment.getToDate());
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Object> getDmdCollAmtInstallmentWiseUptoCurrentFinYear(EgDemand egDemand) {
        CFinancialYear financialyear = financialYearDAO.getFinancialYearByDate(new Date());

        StringBuilder queryBuilder = new StringBuilder(500);
        queryBuilder
                .append("select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, ")
                .append("sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, ")
                .append("eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id ")
                .append("and dmdDet.id_demand =:dmdId and inst.start_date<=:currFinEndDate and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master ")
                .append("group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
        Query query = getCurrentSession().createNativeQuery(queryBuilder.toString())
                .setParameter("dmdId", egDemand.getId()).setParameter("currFinEndDate", financialyear.getEndingDate());
        return query.list();
    }

    @Transactional
    public String generateBill(String consumerCode, String applicationTypeCode) {
        String currentInstallmentYear = EMPTY;
        WaterConnectionBillable waterConnectionBillable = (WaterConnectionBillable) context.getBean("waterConnectionBillable");
        BillReferenceNumberGenerator billRefeNumber = beanResolver.getAutoNumberServiceFor(BillReferenceNumberGenerator.class);
        WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
        		consumerCode, INPROGRESS);
        if (waterConnectionDetails == null)
        	waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
        			consumerCode, ACTIVE);

        if (INPROGRESS.equals(waterConnectionDetails.getConnectionStatus()))
            currentInstallmentYear = toYearFormat(getCurrentInstallment(MODULE_NAME, YEARLY, new Date()).getInstallmentYear());
        else if (ACTIVE.equals(waterConnectionDetails.getConnectionStatus())
                && NON_METERED.equals(waterConnectionDetails.getConnectionType()))
            currentInstallmentYear = toYearFormat(getCurrentInstallment(
                    PROPERTY_MODULE_NAME, null, new Date()).getInstallmentYear());
        else if (ACTIVE.equals(waterConnectionDetails.getConnectionStatus())
                && METERED.equals(waterConnectionDetails.getConnectionType()))
            currentInstallmentYear = toYearFormat(getCurrentInstallment(
                    MODULE_NAME, MONTHLY, new Date()).getInstallmentYear());

        AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);

        waterConnectionBillable.setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionBillable.setAssessmentDetails(assessmentDetails);
        waterConnectionBillable.setUserId(ApplicationThreadLocals.getUserId());
        waterConnectionBillable.setReferenceNumber(billRefeNumber.generateBillNumber(currentInstallmentYear));
        waterConnectionBillable.setBillType(getBillTypeByCode(WaterTaxConstants.BILLTYPE_AUTO));
        waterConnectionBillable.setServiceCode(WATERTAX_CHARGES_SERVICE_CODE);
        waterConnectionBillable.getCurrentDemand().setMinAmtPayable(ZERO);

        return encodeURL(connectionBillService.getBillXML(waterConnectionBillable));
    }

    public EgBillType getBillTypeByCode(String typeCode) {
        return egBillDAO.getBillTypeByCode(typeCode);
    }

    public EgDemand getDemandByInstAndApplicationNumber(Installment installment, String consumerCode) {
        WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsRepository
                .findByApplicationNumberAndInstallment(installment, consumerCode);
        return waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
    }

    /**
     * @param waterConnectionDetails
     * @param billAmount
     * @param currentDate
     * @return Updates WaterConnectionDetails after Meter Entry Demand Calculettion and Update Previous Bill and Generates New
     * Bill
     */
    @Transactional
    public WaterConnectionDetails updateDemandForMeteredConnection(WaterConnectionDetails waterConnectionDetails,
            BigDecimal billAmount, Date currentDate, Date previousDate,
            int noofmonths, Boolean currentMonthIncluded) {
        Installment installment;
        List<Installment> installmentList = new ArrayList<>();
        if (!waterConnectionDetails.getMeterConnection().get(0).isMeterDamaged())
            installmentList = installmentDao.getInstallmentsByModuleAndFromDateAndInstallmentType(
                    moduleService.getModuleByName(WaterTaxConstants.MODULE_NAME), previousDate, currentDate, MONTHLY);

        installment = getCurrentInstallment(MODULE_NAME, MONTHLY, currentDate);

        if (!currentMonthIncluded && installmentList.size() > 1 && installmentList.contains(installment))
            installmentList.remove(installment);

        EgDemand demandObj = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        Date date = new Date();
        if (demandObj == null && waterConnectionDetails.getLegacy()) {
            EgDemand demand = new EgDemand();
            demand.setEgInstallmentMaster(installment);
            demand.setIsHistory(DEMAND_ISHISTORY_N);
            demand.setCreateDate(date);
            demand.setModifiedDate(date);
            demandObj = demand;
            WaterDemandConnection waterDemandConnection = new WaterDemandConnection();
            waterDemandConnection.setDemand(demandObj);
            waterDemandConnection.setWaterConnectionDetails(waterConnectionDetails);
            waterConnectionDetails.addWaterDemandConnection(waterDemandConnection);
            waterDemandConnectionService.createWaterDemandConnection(waterDemandConnection);
        }
        if (!installmentList.isEmpty()) {
            for (Installment installmentVal : installmentList)
                createMeteredDemandDetails(demandObj, waterConnectionDetails, billAmount, installmentVal);

            List<EgBill> billlist = demandGenericDao.getAllBillsForDemand(demandObj, "N", "N");
            if (!billlist.isEmpty()) {
                EgBill billObj = billlist.get(0);
                billObj.setIs_History(DEMAND_ISHISTORY_Y);
                billObj.setModifiedDate(date);
                egBillDAO.create(billObj);
            }
            generateBillForMeterAndMonthly(waterConnectionDetails.getConnection().getConsumerCode());
        } else
            throw new ValidationException("err.water.meteredinstallment.not.found");
        waterConnectionDetails.getWaterDemandConnection().get(0).setDemand(demandObj);
        return waterConnectionDetails;
    }

    @Transactional
    public void createMeteredDemandDetails(EgDemand demandObj, WaterConnectionDetails waterConnectionDetails,
            BigDecimal billAmount, Installment installment) {
        Boolean demandDetailExists = false;
        for (EgDemandDetails demandDetails : demandObj.getEgDemandDetails())
            if (demandDetails.getEgDemandReason().getEgInstallmentMaster().equals(installment)) {
                demandDetailExists = true;
                break;
            }
        if (!demandDetailExists) {
            Set<EgDemandDetails> dmdDetailSet = new HashSet<>();
            dmdDetailSet.add(
                    createDemandDetails(Double.parseDouble(billAmount.toString()), METERED_CHARGES_REASON_CODE, installment));
            demandObj.setBaseDemand(demandObj.getBaseDemand().add(billAmount));
            demandObj.setEgInstallmentMaster(installment);
            demandObj.getEgDemandDetails().addAll(dmdDetailSet);
            demandObj.setModifiedDate(new Date());
            final WaterDemandConnection waterDemandConnection = waterDemandConnectionService
                    .findByWaterConnectionDetailsAndDemand(waterConnectionDetails, demandObj);
            if (demandObj.getId() != null && waterDemandConnection == null) {
                final WaterDemandConnection waterdemandConnection = new WaterDemandConnection();
                waterdemandConnection.setDemand(demandObj);
                waterdemandConnection.setWaterConnectionDetails(waterConnectionDetails);
                waterConnectionDetails.addWaterDemandConnection(waterdemandConnection);
                waterDemandConnectionService.createWaterDemandConnection(waterdemandConnection);
            }
        }
    }

    /**
     * @param waterConnectionDetails
     * @param demandDeatilslist
     * @return creation or updating demand and demanddetails for data Entry Screen
     */
    @Transactional
    public WaterConnectionDetails updateDemandForNonMeteredConnectionDataEntry(WaterConnectionDetails waterConnectionDetails,
            String sourceChannel) {
        EgDemand demandObj = new EgDemand();
        Installment installObj;
        List<String> installmentList = new ArrayList<>();
        EgDemand demand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        List<WaterDemandConnection> demandList = waterConnectionDetails.getWaterDemandConnection();
        if (!demandList.isEmpty() && waterConnectionDetails.getLegacy() && waterConnectionDetails.getState() == null
                && demand != null)
            demandObj = demand;
        Set<EgDemandDetails> dmdDetailSet = new HashSet<>();
        for (DemandDetail demanddetailBean : waterConnectionDetails.getDemandDetailBeanList())
            if (demanddetailBean.getActualAmount().signum() > 0
                    && demanddetailBean.getActualCollection().signum() >= 0
                    && demanddetailBean.getActualCollection().compareTo(demanddetailBean.getActualAmount()) < 1) {
                demandObj.setBaseDemand(getTotalAmountForBaseDemand(demanddetailBean, demandObj.getBaseDemand()));
                demandObj.setAmtCollected(getTotalCollectedAmountForDemand(demanddetailBean, demandObj.getAmtCollected()));
                dmdDetailSet.add(createDemandDetailsrForDataEntry(demanddetailBean.getActualAmount(),
                        demanddetailBean.getActualCollection(), demanddetailBean.getReasonMaster(),
                        demanddetailBean.getInstallment(), demanddetailBean, waterConnectionDetails, null));

                installmentList.add(demanddetailBean.getInstallment());
            }
        demandObj.getEgDemandDetails().clear();
        demandObj.getEgDemandDetails().addAll(dmdDetailSet);
        int listlength = demandObj.getEgDemandDetails().size() - 1;
        if (waterConnectionDetails.getConnectionType().equals(NON_METERED))
            installObj = installmentDao.getInsatllmentByModuleAndDescription(moduleService.getModuleByName(PROPERTY_MODULE_NAME),
                    waterConnectionDetails.getDemandDetailBeanList().get(listlength).getInstallment());
        else {
            listlength = installmentList.size() - 1;
            installObj = installmentDao.getInsatllmentByModuleAndDescription(moduleService.getModuleByName(MODULE_NAME),
                    waterConnectionDetails.getDemandDetailBeanList().get(listlength).getInstallment());
        }
        demandObj.setEgInstallmentMaster(installObj);
        demandObj.setModifiedDate(new Date());
        if (StringUtils.isBlank(demandObj.getIsHistory()))
            demandObj.setIsHistory(DEMAND_ISHISTORY_N);
        if (demandObj.getCreateDate() == null)
            demandObj.setCreateDate(new Date());
        if (demandObj.getId() == null) {
            WaterDemandConnection waterdemandConnection = new WaterDemandConnection();
            if (waterConnectionDetails.getWaterDemandConnection().isEmpty()
                    || DEMAND_ISHISTORY_Y.equalsIgnoreCase(
                            waterConnectionDetails.getWaterDemandConnection().get(0).getDemand().getIsHistory())) {
                waterdemandConnection.setDemand(demandObj);
                waterdemandConnection.setWaterConnectionDetails(waterConnectionDetails);
                waterConnectionDetails.addWaterDemandConnection(waterdemandConnection);
                waterDemandConnectionService.createWaterDemandConnection(waterdemandConnection);
            } else {
                Iterator<EgDemandDetails> iterator = dmdDetailSet.iterator();
                while (iterator.hasNext())
                    waterConnectionDetails.getWaterDemandConnection().get(0).getDemand().addEgDemandDetails(iterator.next());
            }
        }
        waterConnectionDetailsService.updateIndexes(waterConnectionDetails);
        waterConnectionDetailsService.save(waterConnectionDetails);
        return waterConnectionDetails;
    }

    public BigDecimal getTotalAmountForBaseDemand(DemandDetail demanddetailBean, BigDecimal baseDemandAmount) {
        BigDecimal currentTotalAmount = ZERO;
        EgDemandDetails demandDetailsObj = waterConnectionDetailsRepository.findEgDemandDetailById(demanddetailBean.getId());
        if (demanddetailBean.getId() == null)
            currentTotalAmount = baseDemandAmount.add(demanddetailBean.getActualAmount());
        else if (demanddetailBean.getActualAmount().compareTo(demandDetailsObj.getAmount()) < 0) {
            BigDecimal diffExtraless = demandDetailsObj.getAmount().subtract(demanddetailBean.getActualAmount());
            currentTotalAmount = baseDemandAmount.subtract(diffExtraless);
        } else if (demanddetailBean.getActualAmount().compareTo(demandDetailsObj.getAmount()) > 0) {
            BigDecimal diffExtra = demanddetailBean.getActualAmount().subtract(demandDetailsObj.getAmount());
            currentTotalAmount = baseDemandAmount.add(diffExtra);
        } else if (demanddetailBean.getActualAmount().compareTo(demandDetailsObj.getAmount()) == 0)
            currentTotalAmount = baseDemandAmount;
        return currentTotalAmount;

    }

    public BigDecimal getTotalCollectedAmountForDemand(final DemandDetail demanddetailBean, BigDecimal demandAmountCollected) {
        BigDecimal currentTotalAmount = ZERO;
        EgDemandDetails demandDetailsObj = waterConnectionDetailsRepository.findEgDemandDetailById(demanddetailBean.getId());
        if (demanddetailBean.getId() == null)
            currentTotalAmount = demandAmountCollected.add(demanddetailBean.getActualCollection());
        else if (demanddetailBean.getActualCollection().compareTo(demandDetailsObj.getAmtCollected()) < 0) {
            BigDecimal diffExtraless = demandDetailsObj.getAmtCollected().subtract(demanddetailBean.getActualCollection());
            currentTotalAmount = demandAmountCollected.subtract(diffExtraless);
        } else if (demanddetailBean.getActualCollection().compareTo(demandDetailsObj.getAmtCollected()) > 0) {
            BigDecimal diffExtra = demanddetailBean.getActualCollection().subtract(demandDetailsObj.getAmtCollected());
            currentTotalAmount = demandAmountCollected.add(diffExtra);
        } else if (demanddetailBean.getActualCollection().compareTo(demandDetailsObj.getAmtCollected()) == 0)
            currentTotalAmount = demandAmountCollected;
        return currentTotalAmount;

    }

    /**
     * @param consumerCode
     * @return Generates Eg_bill Entry and saved with Demand and As of now we are generating Bill and its in XML format because no
     * Method to just to generate Bill and Save as of now in connectionBillService.
     */
    @Transactional
    public String generateBillForMeterAndMonthly(String consumerCode) {

        WaterConnectionBillable waterConnectionBillable = (WaterConnectionBillable) context.getBean("waterConnectionBillable");
        WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.ACTIVE);
        AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
        MeterDemandNoticeNumberGenerator meterDemandNotice = beanResolver
                .getAutoNumberServiceFor(MeterDemandNoticeNumberGenerator.class);

        waterConnectionBillable.setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionBillable.setAssessmentDetails(assessmentDetails);
        waterConnectionBillable.setUserId(ApplicationThreadLocals.getUserId());
        waterConnectionBillable.setReferenceNumber(meterDemandNotice.generateMeterDemandNoticeNumber());
        waterConnectionBillable.setBillType(getBillTypeByCode(WaterTaxConstants.BILLTYPE_MANUAL));

        return connectionBillService.getBillXML(waterConnectionBillable);
    }

    public WaterConnectionDetails updateDemandForNonmeteredConnection(WaterConnectionDetails waterConnectionDetails,
            Installment installment, Boolean reconnInSameInstallment,
            String workFlowAction) {
        Date installemntStartDate;
        EgDemandDetails existingDemandDtlObject = null;
        int numberOfMonths;
        double totalWaterRate;
        List<Installment> installmentList = new ArrayList<>();

        if (installment == null)
            installment = getCurrentInstallment(PROPERTY_MODULE_NAME, null, new Date());
        if (workFlowAction != null && workFlowAction.equals(WaterTaxConstants.WF_STATE_TAP_EXECUTION_DATE_BUTTON))
            installemntStartDate = waterConnectionDetails.getExecutionDate();

        else if (reconnInSameInstallment != null) {
            if (reconnInSameInstallment)
                installemntStartDate = installment.getFromDate();
            else
                installemntStartDate = waterConnectionDetails.getReconnectionApprovalDate();
        } else
            installemntStartDate = new Date();

        CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(new Date());
        numberOfMonths = DateUtils.noOfMonthsBetween(installemntStartDate, finYear.getEndingDate());
        if (numberOfMonths >= 6)
            installmentList = waterTaxUtils.getInstallmentsbetweenFromAndTodate(installemntStartDate, finYear.getEndingDate());
        else
            installmentList.add(installment);

        Collections.sort(installmentList);
        EgDemandDetails demandDetails;
        EgDemand currentDemand;
        WaterRatesDetails waterRatesDetails = getWaterRatesDetailsForDemandUpdate(waterConnectionDetails);
        if (RECONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
            currentDemand = waterDemandConnectionService.getLatestHistoryDemand(waterConnectionDetails).getDemand();
        else
            currentDemand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        WaterDemandConnection demandConnection = waterDemandConnectionService.findByWaterConnectionDetailsAndDemand(
                waterConnectionDetails, currentDemand);
        for (Installment instlment : installmentList)
            if (instlment.getToDate().compareTo(finYear.getEndingDate()) <= 1) {
                for (EgDemandDetails demandDtls : currentDemand.getEgDemandDetails())
                    if (WATERTAXREASONCODE.equalsIgnoreCase(demandDtls.getEgDemandReason().getEgDemandReasonMaster().getCode())
                            && instlment.getDescription()
                                    .equalsIgnoreCase(demandDtls.getEgDemandReason().getEgInstallmentMaster().getDescription()))
                        existingDemandDtlObject = demandDtls;

                Integer noofmonths = DateUtils.noOfMonthsBetween(installemntStartDate, instlment.getToDate());
                if (existingDemandDtlObject == null) {
                    if (waterRatesDetails == null)
                        throw new ValidationException("err.water.rate.not.found");
                    else {
                        if (noofmonths > 0)
                            totalWaterRate = waterRatesDetails.getMonthlyRate() * (noofmonths + 1);
                        else
                            totalWaterRate = waterRatesDetails.getMonthlyRate();

                        demandDetails = createDemandDetails(totalWaterRate,
                                WATERTAXREASONCODE, instlment);

                        currentDemand.setBaseDemand(currentDemand.getBaseDemand().add(BigDecimal.valueOf(totalWaterRate)));
                        currentDemand.setEgInstallmentMaster(instlment);
                        currentDemand.getEgDemandDetails().add(demandDetails);
                        currentDemand.setModifiedDate(new Date());
                        if (currentDemand.getId() != null && demandConnection == null) {
                            WaterDemandConnection waterdemandConnection = new WaterDemandConnection();
                            waterdemandConnection.setDemand(currentDemand);
                            waterdemandConnection.setWaterConnectionDetails(waterConnectionDetails);
                            waterConnectionDetails.addWaterDemandConnection(waterdemandConnection);
                            waterDemandConnectionService.createWaterDemandConnection(waterdemandConnection);
                        }
                    }
                    installemntStartDate = new DateTime(instlment.getToDate()).plusDays(1).toDate();
                }
                if (RECONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())){
                	currentDemand.setIsHistory(DEMAND_ISHISTORY_N);
                	currentDemand.setModifiedDate(new Date());
                }
            }
        return waterConnectionDetails;
    }

    public WaterRatesDetails getWaterRatesDetailsForDemandUpdate(WaterConnectionDetails waterConnectionDetails) {
        List<WaterRatesHeader> waterRatesHeaderList = waterRatesHeaderService
                .findByConnectionTypeAndUsageTypeAndWaterSourceAndPipeSize(waterConnectionDetails.getConnectionType(),
                        waterConnectionDetails.getUsageType(), waterConnectionDetails.getWaterSource(),
                        waterConnectionDetails.getPipeSize());
        WaterRatesDetails waterRatesDetails = null;
        if (!waterRatesHeaderList.isEmpty())
            for (WaterRatesHeader waterRatesHeadertemp : waterRatesHeaderList) {
                waterRatesDetails = waterRatesDetailsService.findByWaterRatesHeaderAndFromDateAndToDate(
                        waterRatesHeadertemp, new Date(), new Date());
                if (waterRatesDetails != null)
                    break;
            }
        return waterRatesDetails;
    }

    public Installment getCurrentInstallment(String moduleName, String installmentType, Date date) {
        if (installmentType == null)
            return installmentDao.getInsatllmentByModuleForGivenDate(moduleService.getModuleByName(moduleName), date);
        else
            return installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                    moduleService.getModuleByName(moduleName), date, installmentType);
    }

    @SuppressWarnings("unchecked")
    public List<Object> getDmdCollAmtInstallmentWiseWithIsDmdTrue(EgDemand egDemand) {
        StringBuilder stringBuilder = new StringBuilder(2000);
        stringBuilder.append(
                "SELECT wcdid,dmdResId,installment,amount,amt_collected,amt_rebate,amount-amt_collected AS balance,")
                .append(" instStartDate FROM (SELECT wcd.id AS wcdid,dmdRes.id AS dmdResId,dmdRes.id_installment AS installment, ")
                .append(" SUM(dmdDet.amount) AS amount,SUM(dmdDet.amt_collected) AS amt_collected,SUM(dmdDet.amt_rebate) AS amt_rebate, ")
                .append(" inst.start_date AS inststartdate FROM eg_demand_details dmdDet,eg_demand_reason dmdRes,eg_installment_master inst, ")
                .append(" eg_demand_reason_master dmdresmas,egwtr_connectiondetails wcd WHERE dmdDet.id_demand_reason=dmdRes.id ")
                .append(" AND dmdDet.id_demand =:dmdId AND dmdRes.id_installment = inst.id AND dmdresmas.id = dmdres.id_demand_reason_master ")
                .append(" AND dmdresmas.isdemand=TRUE AND wcd.demand = dmdDet.id_demand GROUP BY dmdRes.id, dmdRes.id_installment, ")
                .append(" inst.start_date,wcd.id ORDER BY inst.start_date) AS dcb");
        return getCurrentSession().createNativeQuery(stringBuilder.toString()).setLong("dmdId", egDemand.getId()).list();
    }

    /**
     * @param waterConnectionDetails
     * @param givenDate It Checks the Meter Entry Exist For the Entred Date Month and Returns True if It Exists and checks with
     * Demand Current Installment
     */
    public Boolean meterEntryAllReadyExistForCurrentMonth(WaterConnectionDetails waterConnectionDetails, Date givenDate) {
        Boolean currrentInstallMentExist = false;

        Installment installment = getCurrentInstallment(MODULE_NAME, MONTHLY, givenDate);
        WaterDemandConnection waterDemandConnection = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails);
        if (waterDemandConnection != null && waterDemandConnection.getDemand() != null && installment != null
                && installment.getInstallmentNumber().equals(waterDemandConnection.getDemand().getEgInstallmentMaster().getInstallmentNumber()))
            currrentInstallMentExist = true;
        return currrentInstallMentExist;
    }

    @SuppressWarnings("unchecked")
    public List<Object> getDmdCollAmtInstallmentWiseUptoPreviousFinYear(EgDemand egDemand) {
        CFinancialYear financialyear = financialYearDAO.getFinancialYearByDate(new Date());

        StringBuilder stringBuilder = new StringBuilder(600);
        stringBuilder.append(
                "select dmdRes.id,dmdRes.id_installment, sum(dmdDet.amount) as amount, sum(dmdDet.amt_collected) as amt_collected, ")
                .append(" sum(dmdDet.amt_rebate) as amt_rebate, inst.start_date from eg_demand_details dmdDet,eg_demand_reason dmdRes, ")
                .append(" eg_installment_master inst,eg_demand_reason_master dmdresmas where dmdDet.id_demand_reason=dmdRes.id ")
                .append(" and dmdDet.id_demand =:dmdId and inst.start_date<:currFinStartDate and dmdRes.id_installment = inst.id and dmdresmas.id = dmdres.id_demand_reason_master ")
                .append(" group by dmdRes.id,dmdRes.id_installment, inst.start_date order by inst.start_date ");
        Query query = getCurrentSession().createNativeQuery(stringBuilder.toString())
                .setParameter("dmdId", egDemand.getId())
                .setParameter("currFinStartDate", financialyear.getStartingDate());
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Installment> getInstallmentsForPreviousYear(Date currDate) {
        Map<String, Installment> currYearInstMap = new HashMap<>();
        StringBuilder queryString = new StringBuilder(500);
        queryString.append(
                "select installment from Installment installment,CFinancialYear finYear where installment.module.name =:moduleName")
                .append("  and cast(installment.toDate as date) <= cast(finYear.startingDate as date) order by installment.id desc ");
        Query qry = getCurrentSession().createQuery(queryString.toString()).setString("moduleName", PTMODULENAME);
        List<Installment> installments = qry.list();
        currYearInstMap.put(WaterTaxConstants.PREVIOUS_SECOND_HALF, installments.get(0));
        return currYearInstMap;
    }

    public BindingResult getWaterTaxDue(WaterConnectionDetails waterConnectionDetails, BindingResult resultBinder) {
    	EstimationNotice estimationNotice = estimationNoticeService.getNonHistoryEstimationNoticeForConnection(waterConnectionDetails);
        if (estimationNotice == null)
            resultBinder.reject("err.demandnote.not.present");
        else {
            BigDecimal waterChargesDue = waterConnectionDetailsService.getTotalAmount(waterConnectionDetails);
            if (waterChargesDue.signum() > 0)
                resultBinder.reject("err.water.charges.due", new Double[] { waterChargesDue.doubleValue() }, null);
        }
        return resultBinder;
    }

    public boolean checkWaterChargesCurrentDemand(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        if (currentDemand != null && !currentDemand.getEgDemandDetails().isEmpty())
            for (EgDemandDetails demandDetails : currentDemand.getEgDemandDetails())
                if (WATERTAXREASONCODE.equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode())
                        || METERED_CHARGES_REASON_CODE
                                .equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    return true;
        return false;
    }

    public void createDemandDetailForPenaltyAndServiceCharges(WaterConnectionDetails waterConnectionDetails) {

        Installment installment = installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                moduleService.getModuleByName(MODULE_NAME), new Date(), YEARLY);

        waterConnectionDetails.getWaterDemandConnection().get(0).getDemand().addEgDemandDetails(createDemandDetailsrForDataEntry(
                BigDecimal.valueOf(waterConnectionDetails.getDonationCharges()).divide(new BigDecimal(2)).setScale(0, ROUND_HALF_UP),
                ZERO, PENALTYCHARGES, installment == null ? null : installment.getDescription(), new DemandDetail(),
                waterConnectionDetails, FIELD_INSPECTION));
    }

    public Map<String, String> getMonthlyWaterChargesDue(WaterConnectionDetails waterConnectionDetails) {
        Map<String, String> resultMap = new HashMap<>();
        BigDecimal amount = ZERO;
        EgDemand currentDemand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        for (EgDemandDetails demandDetails : currentDemand.getEgDemandDetails())
            if (WATERTAXREASONCODE.equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()) ||
                    METERED_CHARGES_REASON_CODE
                            .equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                amount = amount.add(demandDetails.getAmount());
        List<EgDemandDetails> demandDetailList = new ArrayList<>(currentDemand.getEgDemandDetails());
        DemandComparatorByInstallmentStartDate demandComparatorByInstallmentStartDate = new DemandComparatorByInstallmentStartDate();
        Collections.sort(demandDetailList, demandComparatorByInstallmentStartDate);
        resultMap.put(WATER_CHARGES, amount.toString());
        Boolean isInstallmentSet = false;
        for (EgDemandDetails demandDetails : demandDetailList)
            if (!isInstallmentSet && WATERTAXREASONCODE
                    .equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode())) {
                resultMap.put(FROM_INSTALLMENT, demandDetails.getEgDemandReason().getEgInstallmentMaster().getDescription());
                resultMap.put(TO_INSTALLMENT, demandDetailList.get(demandDetailList.size() - 1).getEgDemandReason()
                        .getEgInstallmentMaster().getDescription());
                isInstallmentSet = true;
            } else if (!isInstallmentSet && METERED_CHARGES_REASON_CODE
                    .equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode())) {
                resultMap.put(FROM_INSTALLMENT,
                        demandDetails.getEgDemandReason().getEgInstallmentMaster().getDescription().replace("WT_MC-", ""));
                resultMap.put(TO_INSTALLMENT, demandDetailList.get(demandDetailList.size() - 1).getEgDemandReason()
                        .getEgInstallmentMaster().getDescription().replace("WT_MC-", ""));
                isInstallmentSet = true;
            }
        return resultMap;
    }

    @Transactional
    public void generateDemandForApplication(WaterConnectionDetails waterConnectionDetails,
            ConnectionCategory connectionCategory, Double donationCharges) {
        populateEstimationDetails(waterConnectionDetails);
        WaterDemandConnection waterDemandConnection = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails);
        if (METERED.equals(waterConnectionDetails.getConnectionType())
                && waterConnectionDetails.getStatus().getCode().equalsIgnoreCase(APPLICATION_STATUS_CREATED))
            waterConnectionDetails.setDonationCharges(donationCharges);
        if (!waterConnectionDetails.getWaterDemandConnection().isEmpty())
            waterConnectionDetails.getWaterDemandConnection().get(0).getDemand().setIsHistory(DEMAND_ISHISTORY_Y);
        waterDemandConnection.setDemand(createDemand(waterConnectionDetails));
        waterDemandConnection.setWaterConnectionDetails(waterConnectionDetails);
        waterConnectionDetails.addWaterDemandConnection(waterDemandConnection);
        waterDemandConnectionService.createWaterDemandConnection(waterDemandConnection);
        waterConnectionDetailsService.save(waterConnectionDetails);
        if (REGULARIZE_CONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())
                && APPLICATION_STATUS_CREATED.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode())
                && NON_METERED.equals(waterConnectionDetails.getConnectionType()))
            regulariseDemandGenImpl.generateDemandForRegulariseConnection(waterConnectionDetails);
    }

    private void populateEstimationDetails(WaterConnectionDetails waterConnectionDetails) {
        List<ConnectionEstimationDetails> estimationDetails = new ArrayList<>();
        if (!waterConnectionDetails.getEstimationDetails().isEmpty())
            for (ConnectionEstimationDetails estimationDetail : waterConnectionDetails.getEstimationDetails())
                if (validEstimationDetail(estimationDetail)) {
                    estimationDetail.setWaterConnectionDetails(waterConnectionDetails);
                    estimationDetails.add(estimationDetail);
                }

        waterConnectionDetails.getEstimationDetails().clear();
        waterConnectionDetails.setEstimationDetails(estimationDetails);
    }

    private boolean validEstimationDetail(ConnectionEstimationDetails connectionEstimationDetails) {
        return connectionEstimationDetails != null && isNotBlank(connectionEstimationDetails.getItemDescription());
    }

    public BigDecimal getTotalDemandAmountDue(EgDemand demand) {
        if (demand == null)
            return ZERO;
        else {
            BigDecimal amountDue = ZERO;
            for (EgDemandDetails details : demand.getEgDemandDetails())
                amountDue = amountDue.add(details.getAmount().subtract(details.getAmtCollected().add(details.getAmtRebate())));
            return amountDue;
        }

    }

    @Transactional
    public String updateUlbMaterial(String applicationNumber, WaterConnectionDetails waterConnectionDetails) {
        WaterConnectionDetails connectionDetails = null;
        if (applicationNumber != null)
            connectionDetails = waterConnectionDetailsService.findByApplicationNumber(applicationNumber);
        if (waterConnectionDetails.getUlbMaterial() && !containsMaterialDemand(connectionDetails))
            generateMaterialDemand(connectionDetails);

        if (!waterConnectionDetails.getUlbMaterial() && containsMaterialDemand(connectionDetails))
            deleteMaterialDemand(connectionDetails);

        if (connectionDetails != null)
            connectionDetails.setUlbMaterial(waterConnectionDetails.getUlbMaterial());
        waterConnectionDetailsService.save(connectionDetails);
        return UPDATE_MATERIAL_DETAILS;
    }

    @Transactional
    public void generateMaterialDemand(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        EgDemandDetails demandDetails;
        if (currentDemand != null) {
            BigDecimal amount = ZERO;
            Boolean createMaterialDemand = true;
            for (ConnectionEstimationDetails details : waterConnectionDetails.getEstimationDetails())
                amount = amount.add(BigDecimal.valueOf(details.getUnitRate() * details.getQuantity()));
            Installment installment = null;
            for (EgDemandDetails demandDetail : currentDemand.getEgDemandDetails())
                if (WATER_MATERIAL_CHARGES_REASON_CODE
                        .equalsIgnoreCase(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    createMaterialDemand = false;
                else if (WATERTAX_SUPERVISION_CHARGE.equals(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
                else if (WATERTAXREASONCODE.equals(demandDetail.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    installment = installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                            moduleService.getModuleByName(MODULE_NAME),
                            demandDetail.getInstallmentStartDate(), YEARLY);
            if (createMaterialDemand) {
                demandDetails = createDemandDetails(amount.doubleValue(), WATER_MATERIAL_CHARGES_REASON_CODE, installment);
                currentDemand.setBaseDemand(currentDemand.getBaseDemand().add(demandDetails.getAmount()));
                currentDemand.getEgDemandDetails().add(demandDetails);
            }
        }

    }

    public boolean containsMaterialDemand(WaterConnectionDetails waterConnectionDetails) {
        boolean isMaterialDemandPresent = false;
        EgDemand currentDemand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        if (currentDemand != null)
            for (EgDemandDetails demandDetails : currentDemand.getEgDemandDetails())
                if (WATER_MATERIAL_CHARGES_REASON_CODE
                        .equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    isMaterialDemandPresent = true;
        return isMaterialDemandPresent;
    }

    @Transactional
    public void deleteMaterialDemand(WaterConnectionDetails waterConnectionDetails) {
        EgDemand currentDemand = waterDemandConnectionService.getCurrentDemand(waterConnectionDetails).getDemand();
        List<EgDemandDetails> detailsList = new ArrayList<>();
        if (currentDemand != null) {
            for (EgDemandDetails demandDetails : currentDemand.getEgDemandDetails())
                if (WATER_MATERIAL_CHARGES_REASON_CODE
                        .equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()) &&
                        demandDetails.getAmtCollected() == ZERO)
                    detailsList.add(demandDetails);
            if (!detailsList.isEmpty()) {
                currentDemand.getEgDemandDetails().removeAll(detailsList);
                currentDemand.setBaseDemand(currentDemand.getBaseDemand().subtract(detailsList.get(0).getAmount()));
            }
        }
    }
}
