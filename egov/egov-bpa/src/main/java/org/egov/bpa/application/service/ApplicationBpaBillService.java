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
package org.egov.bpa.application.service;

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
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.bpa.application.autonumber.ApplicationNumberGenerator;
import org.egov.bpa.application.autonumber.BpaBillReferenceNumberGenerator;
import org.egov.bpa.application.entity.BpaApplication;
import org.egov.bpa.application.entity.BpaFeeDetail;
import org.egov.bpa.application.repository.ApplicationBpaRepository;
import org.egov.bpa.application.service.collection.BpaApplicationBillable;
import org.egov.bpa.application.service.collection.BpaDemandComparatorByOrderId;
import org.egov.bpa.utils.BpaConstants;
import org.egov.collection.integration.models.BillAccountDetails.PURPOSE;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ApplicationBpaBillService extends BillServiceInterface {

    private static final String ISDEBIT = "N";

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private ApplicationBpaRepository applicationBpaRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private ModuleService moduleService;

    @Autowired
    private EgBillDao egBillDAO;
    @Autowired
    private ApplicationContext context;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
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

    public Criteria createCriteriaforFeeAmount(final Long serviceTypeId, final String feeType) {

        final Criteria feeCrit = getCurrentSession().createCriteria(BpaFeeDetail.class, "bpafeeDtl")
                .createAlias("bpafeeDtl.bpafee", "bpaFeeObj")
                .createAlias("bpaFeeObj.serviceType", "servicetypeObj");
        feeCrit.add(Restrictions.eq("servicetypeObj.id", serviceTypeId));
        feeCrit.add(Restrictions.eq("bpaFeeObj.isActive", Boolean.TRUE));
        if (feeType != null)
            feeCrit.add(Restrictions.ilike("bpaFeeObj.feeType", feeType));

        feeCrit.add(Restrictions.le("startDate", new Date()))
                .add(Restrictions.or(Restrictions.isNull("endDate"), Restrictions.ge("endDate", new Date())));
        return feeCrit;
    }

    @Transactional
    public String generateBill(final BpaApplication application) {
        String collectXML;
        String currentInstallmentYear;
        final BpaApplicationBillable bpaApplicationBillable = (BpaApplicationBillable) context
                .getBean("bpaApplicationBillable");
        final BpaBillReferenceNumberGenerator billRefeNumber = beanResolver
                .getAutoNumberServiceFor(BpaBillReferenceNumberGenerator.class);

        currentInstallmentYear = getCurrentInstallmentYear();

        bpaApplicationBillable.setApplication(application);
        bpaApplicationBillable.getApplication();
        bpaApplicationBillable.setUserId(ApplicationThreadLocals.getUserId());

        bpaApplicationBillable.setReferenceNumber(billRefeNumber.generateBillNumber(currentInstallmentYear));
        bpaApplicationBillable.setBillType(getBillTypeByCode("AUTO"));

        final String billXml = getBillXML(bpaApplicationBillable);
        try {
            collectXML = URLEncoder.encode(billXml, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new ApplicationRuntimeException(e.getMessage());
        }
        return collectXML;
    }

    public EgBillType getBillTypeByCode(final String typeCode) {
        return egBillDAO.getBillTypeByCode(typeCode);
    }

    public Installment getCurrentInstallment(final String moduleName, final String installmentType, final Date date) {
        if (null == installmentType)
            return installmentDao.getInsatllmentByModuleForGivenDate(moduleService.getModuleByName(moduleName),
                    date);
        else
            return installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                    moduleService.getModuleByName(moduleName), date, installmentType);
    }

    private EgDemandDetails createDemandDetails(final BigDecimal amount, final String demandReason,
            final Installment installment) {
        final EgDemandReason demandReasonObj = getDemandReasonByCodeAndInstallment(demandReason, installment);
        final EgDemandDetails demandDetail = new EgDemandDetails();
        demandDetail.setAmount(amount);
        demandDetail.setAmtCollected(BigDecimal.ZERO);
        demandDetail.setAmtRebate(BigDecimal.ZERO);
        demandDetail.setEgDemandReason(demandReasonObj);
        demandDetail.setCreateDate(new Date());
        demandDetail.setModifiedDate(new Date());
        return demandDetail;
    }

    public EgDemandReason getDemandReasonByCodeAndInstallment(final String demandReason,
            final Installment installment) {
        final Query demandQuery = getCurrentSession().getNamedQuery("DEMANDREASONBY_CODE_AND_INSTALLMENTID");
        demandQuery.setParameter(0, demandReason);
        demandQuery.setParameter(1, installment.getId());
        return (EgDemandReason) demandQuery.uniqueResult();
    }

    public EgDemand createDemand(final BpaApplication application) {

        final Map<String, BigDecimal> feeDetails = new HashMap<>();
        EgDemand egDemand = null;
        final Installment installment = installmentDao.getInsatllmentByModuleForGivenDateAndInstallmentType(
                moduleService.getModuleByName(BpaConstants.EGMODULE_NAME), new Date(), BpaConstants.YEARLY);
        // Not updating demand amount collected for new connection as per the
        // discussion.
        feeDetails.put(BpaConstants.ADMISSIONFEEREASON, application.getAdmissionfeeAmount());
        if (installment != null) {
            final Set<EgDemandDetails> dmdDetailSet = new HashSet<>();
            for (final String demandReason : feeDetails.keySet())
                dmdDetailSet.add(createDemandDetails(feeDetails.get(demandReason), demandReason, installment));
            egDemand = new EgDemand();
            egDemand.setEgInstallmentMaster(installment);
            egDemand.getEgDemandDetails().addAll(dmdDetailSet);
            egDemand.setIsHistory("N");
            egDemand.setBaseDemand(application.getAdmissionfeeAmount());
            egDemand.setCreateDate(new Date());
            egDemand.setModifiedDate(new Date());
        }

        return egDemand;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<EgBillDetails> getBilldetails(final Billable billObj) {
        final List<EgBillDetails> billDetails = new ArrayList<>();
        final EgDemand demand = billObj.getCurrentDemand();
        final Date currentDate = new Date();
        final Map installmentWise = new HashMap<Installment, List<EgDemandDetails>>();
        final Set<Installment> sortedInstallmentSet = new TreeSet<>();
        final BpaDemandComparatorByOrderId demandComparatorByOrderId = new BpaDemandComparatorByOrderId();
        final List<EgDemandDetails> orderedDetailsList = new ArrayList<>();
        final Installment currInstallment = getCurrentInstallment(BpaConstants.EGMODULE_NAME, BpaConstants.YEARLY, new Date());
        final CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(new Date());
        new TreeMap<Date, String>();

        for (final EgDemandDetails demandDetail : demand.getEgDemandDetails()) {
            final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            if (installmentWise.get(installment) == null) {
                final List<EgDemandDetails> detailsList = new ArrayList<>();
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
        for (final EgDemandDetails demandDetail : orderedDetailsList) {

            final EgDemandReason reason = demandDetail.getEgDemandReason();
            final Installment installment = demandDetail.getEgDemandReason().getEgInstallmentMaster();

            if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getIsDebit().equalsIgnoreCase(ISDEBIT)
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
                billdetail.setFunctionCode("01");
                if (billdetail.getEgDemandReason().getEgInstallmentMaster().getFromDate()
                        .compareTo(finYear.getStartingDate()) >= 0
                        && billdetail.getEgDemandReason().getEgInstallmentMaster().getFromDate()
                                .compareTo(finYear.getEndingDate()) < 0)
                    billdetail.setPurpose(PURPOSE.CURRENT_AMOUNT.toString());
                else
                    billdetail.setPurpose(PURPOSE.OTHERS.toString());
                billdetail.setPurpose(PURPOSE.OTHERS.toString());
                if (currInstallment != null && installment.getFromDate().before(currInstallment.getToDate()))
                    billdetail.setAdditionalFlag(1);
                else
                    billdetail.setAdditionalFlag(0);
                billDetails.add(billdetail);
            }
        }

        return billDetails;
    }

 

    public EgBill updateBillWithLatest(final Long billId) {
        LOGGER.debug("updateBillWithLatest billId " + billId);
        final EgBill bill = egBillDAO.findById(billId, false);
        LOGGER.debug("updateBillWithLatest old bill " + bill);
        if (bill == null)
            throw new ApplicationRuntimeException("No bill found with bill reference no :" + billId);
        bill.getEgBillDetails().clear();
        final BpaApplicationBillable bpaApplicationBillable = (BpaApplicationBillable) context
                .getBean("bpaApplicationBillable");

        bpaApplicationBillable
                .setApplication(applicationBpaRepository.findByApplicationNumber(
                        bill.getConsumerId().trim().toUpperCase()));
        final List<EgBillDetails> egBillDetails = getBilldetails(bpaApplicationBillable);
        for (final EgBillDetails billDetail : egBillDetails) {
            bill.addEgBillDetails(billDetail);
            billDetail.setEgBill(bill);
        }
        LOGGER.debug("Bill update with bill details for water charges " + bill.getConsumerId() + " as billdetails "
                + egBillDetails);
        return bill;
    }

    @Override
    public void cancelBill() {
        /*
         * ncell Bill still not developed Ca
         */
    }

    public String generateApplicationnumber(final BpaApplication application) {
        final String currentInstallmentYear = getCurrentInstallmentYear();
        final ApplicationNumberGenerator applicationnumber = beanResolver
                .getAutoNumberServiceFor(ApplicationNumberGenerator.class);
        return applicationnumber.generateApplicationNumber(
                currentInstallmentYear, application.getServiceType(),
                application.getSiteDetail() != null && application.getSiteDetail().get(0).getAdminBoundary() != null
                        ? application.getSiteDetail().get(0).getAdminBoundary() : null);
    }

    private String getCurrentInstallmentYear() {
        final SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        String currentInstallmentYear = null;
        final Installment inst = getCurrentInstallment(
                BpaConstants.EGMODULE_NAME, BpaConstants.YEARLY, new Date());
        if (inst != null)
            currentInstallmentYear = formatYear.format(inst.getInstallmentYear());
        return currentInstallmentYear;
    }
}
