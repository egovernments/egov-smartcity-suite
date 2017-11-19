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
package org.egov.ptis.domain.dao.demand;

import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.PropertyDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository(value = "ptDemandDAO")
public class PtDemandHibernateDao implements PtDemandDao {
    private static final String BILLID_PARAM = "billid";
    private static final String PROPERTY = "property";
    @SuppressWarnings("rawtypes")
    @Autowired
    private InstallmentHibDao installmentDao;
    @Autowired
    @Qualifier(value = "demandGenericDAO")
    private DemandGenericDao demandGenericDAO;

    @Autowired
    private PropertyDAO propertyDAO;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * This method called getCurrentDemandforProperty gets Total Current Demand Amount .
     * <p>
     * This method returns Total Current Demand for given property.
     * </p>
     *
     * @param org .egov.ptis.property.model.Property property
     * @return a BigDecimal.
     */

    @Override
    public BigDecimal getCurrentDemandforProperty(final Property property) {

        BigDecimal currentDemand = BigDecimal.ZERO;
        Query qry = null;
        if (property != null) {
            qry = getCurrentSession().createQuery(
                    " select sum(DmdDetails.amount) from  EgptPtdemand egDemand left join  egDemand.egDemandDetails DmdDetails where "
                            + " egDemand.egptProperty =:property  and egDemand.isHistory='N'   ");
            qry.setEntity(PROPERTY, property);
            currentDemand = (BigDecimal) qry.uniqueResult();
        }

        return currentDemand;

    }

    /**
     * This method called WhetherBillExistsForProperty gets Character .
     * <p>
     * This method returns Character for given Property , billnum and Module.
     * </p>
     *
     * @param org .egov.ptis.property.model.Property property
     * @param java .lang.Integer billnum
     * @param org .egov.infstr.commons.Module module
     * @return Character of 'Y' or 'N'.
     */

    @SuppressWarnings("unchecked")
    @Override
    public Character whetherBillExistsForProperty(final Property property, final String billnum, final Module module) {
        Character status = null;
        Query qry = null;
        List<EgBill> list;
        if (property != null && billnum != null) {
            final List<EgBill> egBillList = demandGenericDAO.getBillsByBillNumber(billnum, module);
            if (egBillList == null || egBillList.isEmpty())
                status = 'N';
            else {
                final EgBill egBill = egBillList.get(0);
                if (egBill == null)
                    status = 'N';
                else {
                    qry = getCurrentSession()
                            .createQuery(
                                    "select egBill from  EgptPtdemand egptDem , EgBill egBill  where egptDem.egptProperty =:property and :egBill in elements(egptDem.egBills)   ");
                    qry.setEntity(PROPERTY, property);
                    qry.setEntity("egBill", egBill);
                    list = qry.list();
                    if (list.isEmpty())
                        status = 'N';
                    else
                        status = 'Y';
                }
            }
        }
        return status;
    }

    /**
     * This method called getNonHistoryDemandForProperty gets EgptPtdemand Object which is NonHistory.
     * <p>
     * This method returns EgptPtdemand Object for given property .
     * </p>
     *
     * @param org .egov.ptis.property.model.Property property
     * @return EgptPtdemand Object.
     */

    @Override
    public Ptdemand getNonHistoryDemandForProperty(final Property property) {
        Query qry = null;
        Ptdemand egptPtdemand = null;

        if (property != null) {
            qry = getCurrentSession().createQuery("from  Ptdemand egptDem where egptDem.egptProperty =:property   ");
            qry.setEntity(PROPERTY, property);
            if (qry.list().size() == 1)
                egptPtdemand = (Ptdemand) qry.uniqueResult();
            else
                egptPtdemand = (Ptdemand) qry.list().get(0);
        }
        return egptPtdemand;
    }

    /**
     * This method called getDmdDetailsByPropertyIdBoundary gets DemandDetails List .
     * <p>
     * This method returns DemandDetails List for given BasicProperty Object & Boundary Object(Optional) .
     * </p>
     *
     * @param org .egov.ptis.property.model.BasicProperty basicProperty
     * @param org .egov.lib.admbndry.Boundary divBoundary
     * @return DemandDetails List.
     */

    // Here divBoundary is used because in some Property Tax Applications(like
    // COC) propertyId is not unique ,It is unique within the Division Boundary
    // check the demandReasonMaster list . It is not working
    @SuppressWarnings("rawtypes")
    @Override
    public List getDmdDetailsByPropertyIdBoundary(final BasicProperty basicProperty, final Boundary divBoundary) {
        String divStatus = "N";
        List list = new ArrayList();
        final StringBuffer qry = new StringBuffer(50);
        if (basicProperty != null) {
            // should check for active
            qry.append(" select demdet From EgptPtdemand ptdem left join ptdem.egDemandDetails demdet left join "
                    + "ptdem.egptProperty prop left join  prop.basicProperty bp");
            if (divBoundary != null) {
                qry.append(" left join bp.propertyID ppid  ");
                divStatus = "Y";
            }

            qry.append(" where ptdem.isHistory='N'  and prop.status='N' and  prop.isDefaultProperty='Y' and bp.active='Y' "
                    + "and bp =:basicProperty ");
            if ("Y".equals(divStatus))
                qry.append(" and ppid.wardId =:divBoundary  ");

            final Query query = getCurrentSession().createQuery(qry.toString());
            query.setEntity("basicProperty", basicProperty);
            if ("Y".equals(divStatus))
                query.setEntity("divBoundary", divBoundary);
            list = query.list();
        }
        return list;
    }

    @SuppressWarnings("rawtypes")
    public List getTransactionByBasicProperty(final BasicProperty basicProperty, final Installment installment,
            final String is_cancelled) {
        Query qry = null;
        List list = new ArrayList(0);
        if (basicProperty != null && installment != null && is_cancelled != null && !is_cancelled.equals("")) {
            qry = getCurrentSession().createQuery(
                    " select TD from PropertyTaxTxAgent txAgent left join txAgent.myTransactions header "
                            + " left join header.transactionDetails TD where  header.isCancelled =:is_cancelled and "
                            + " header.installment =:installment and txAgent.basicProperty =:basicProperty  ");
            qry.setString("is_cancelled", is_cancelled);
            qry.setEntity("basicProperty", basicProperty);
            qry.setEntity("installment", installment);
            list = qry.list();
        }
        return list;
    }

    /**
     * This method called getAllDemands gets Map<EgDemandReason,Amount> .
     * <p>
     * This method returns Map of DemandReason Objects and DemandDetails amount for given BasicProperty & divBoundary .
     * </p>
     *
     * @param org .egov.ptis.property.model.BasicProperty BasicProperty
     * @param org .egov.lib.admbndry.Boundary divBoundary
     * @return Map<EgDemandReason,Amount>.
     */

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Map<EgDemandReason, BigDecimal> getAllDemands(final BasicProperty basicProperty, final Boundary divBoundary) {
        List<EgDemandDetails> demandDetailsList;
        BigDecimal amount = BigDecimal.ZERO;
        demandDetailsList = getDmdDetailsByPropertyIdBoundary(basicProperty, divBoundary);
        final Map<EgDemandReason, BigDecimal> dmdMap = new HashMap<EgDemandReason, BigDecimal>();
        final Iterator iter = demandDetailsList.iterator();
        while (iter.hasNext()) {
            final EgDemandDetails egDemandDetails = (EgDemandDetails) iter.next();
            if (egDemandDetails.getEgDemandReason() != null)
                if (dmdMap.containsKey(egDemandDetails.getEgDemandReason()))
                    dmdMap.put(egDemandDetails.getEgDemandReason(), egDemandDetails.getAmount());
                else {
                    amount = amount.add(egDemandDetails.getAmount());
                    dmdMap.put(egDemandDetails.getEgDemandReason(), amount);
                }
        }
        return dmdMap;
    }

    /**
     * Gets the current 1) demand amount, 2) collected amount, 3) rebate amount for the demand associated with the given bill ID.
     */
    @Override
    public List<BigDecimal> getCurrentAmountsFromBill(final Long billId) {
        final StringBuilder sb = new StringBuilder();
        sb.append("SELECT").append("    SUM(det.amount),").append("    SUM(det.amt_collected),")
                .append("    SUM(det.amt_rebate)").append(" FROM ").append("    eg_bill bill,")
                .append("    eg_demand_details det,").append("    eg_demand_reason reas,")
                .append("    eg_demand_reason_master reasm,").append("    eg_reason_category reascat,")
                .append("    eg_installment_master inst,").append("    eg_module module").append(" WHERE ")
                .append("    bill.id = :").append(BILLID_PARAM).append(" AND")
                .append("    det.id_demand = bill.id_demand AND").append("    det.id_demand_reason = reas.id AND")
                .append("    reas.id_installment = inst.id_installment AND")
                .append("    reas.id_demand_reason_master = reasm.id AND")
                .append("    reasm.id_category = reascat.id_type AND").append("    reascat.code = 'TAX' AND")
                .append("    inst.start_date <= SYSDATE AND").append("    inst.end_date   >= SYSDATE AND")
                .append("    inst.id_module = module.id_module AND").append("    module.module_name = 'Property Tax'");
        final Query query = getCurrentSession().createSQLQuery(sb.toString());
        query.setLong(BILLID_PARAM, billId);
        final Object[] results = (Object[]) query.uniqueResult();
        final List<BigDecimal> amounts = new ArrayList<BigDecimal>();
        amounts.add((BigDecimal) results[0]); // demand
        amounts.add((BigDecimal) results[1]); // collection
        amounts.add((BigDecimal) results[2]); // rebate
        return amounts;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, BigDecimal> getDemandCollMap(final Property property) {
        final Ptdemand currDemand = getNonHistoryCurrDmdForProperty(property);
        Installment installment = null;
        List dmdCollList = new ArrayList();
        Integer instId = null;
        BigDecimal currFirstHalfDmd = BigDecimal.ZERO;
        BigDecimal currSecondHalfDmd = BigDecimal.ZERO;
        BigDecimal arrDmd = BigDecimal.ZERO;
        BigDecimal currFirstHalfCollection = BigDecimal.ZERO;
        BigDecimal currSecondHalfCollection = BigDecimal.ZERO;
        BigDecimal arrColelection = BigDecimal.ZERO;
        BigDecimal demand = BigDecimal.ZERO;
        BigDecimal collection = BigDecimal.ZERO;
        final Map<String, BigDecimal> retMap = new HashMap<String, BigDecimal>();

        if (currDemand != null)
            dmdCollList = propertyDAO.getDmdCollAmtInstWise(currDemand);

        final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());

        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            instId = Integer.valueOf(listObj[0].toString());
            demand = listObj[1] != null ? new BigDecimal((Double) listObj[1]) : BigDecimal.ZERO;
            collection = listObj[2] != null ? new BigDecimal((Double) listObj[2]) : BigDecimal.ZERO;

            installment = installmentDao.findById(instId, false);
            if (currYearInstMap.get(CURRENTYEAR_FIRST_HALF).equals(installment)) {
                if (collection.compareTo(BigDecimal.ZERO) == 1)
                    currFirstHalfCollection = currFirstHalfCollection.add(collection);
                currFirstHalfDmd = currFirstHalfDmd.add(demand);
            } else if (currYearInstMap.get(CURRENTYEAR_SECOND_HALF).equals(installment)) {
                if (collection.compareTo(BigDecimal.ZERO) == 1)
                    currSecondHalfCollection = currSecondHalfCollection.add(collection);
                currSecondHalfDmd = currSecondHalfDmd.add(demand);
            } else {
                arrDmd = arrDmd.add(demand);
                if (collection.compareTo(BigDecimal.ZERO) == 1)
                    arrColelection = arrColelection.add(collection);
            }
        }
        retMap.put(PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR, currFirstHalfDmd);
        retMap.put(PropertyTaxConstants.CURR_SECONDHALF_DMD_STR, currSecondHalfDmd);
        retMap.put(PropertyTaxConstants.ARR_DMD_STR, arrDmd);
        retMap.put(PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR, currFirstHalfCollection);
        retMap.put(PropertyTaxConstants.CURR_SECONDHALF_COLL_STR, currSecondHalfCollection);
        retMap.put(PropertyTaxConstants.ARR_COLL_STR, arrColelection);
        return retMap;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, BigDecimal> getDemandCollMapIncludingPenaltyAndAdvance(final Property property) {
        final Ptdemand currDemand = getNonHistoryCurrDmdForProperty(property);
        Installment installment = null;
        List dmdCollList = new ArrayList();
        Integer instId = null;
        String code = "";
        BigDecimal currFirstHalfDmd = BigDecimal.ZERO;
        BigDecimal currSecondHalfDmd = BigDecimal.ZERO;
        BigDecimal arrDmd = BigDecimal.ZERO;
        BigDecimal currFirstHalfCollection = BigDecimal.ZERO;
        BigDecimal currSecondHalfCollection = BigDecimal.ZERO;
        BigDecimal arrColelection = BigDecimal.ZERO;
        BigDecimal demand;
        BigDecimal collection;
        BigDecimal currFirstHalfPenalty = BigDecimal.ZERO;
        BigDecimal currSecondHalfPenalty = BigDecimal.ZERO;
        final BigDecimal currFirstHalfPenaltyColllection = BigDecimal.ZERO;
        final BigDecimal currSecondHalfPenaltyCollection = BigDecimal.ZERO;
        final BigDecimal arrPenaltyCollection = BigDecimal.ZERO;
        final BigDecimal arrPenalty = BigDecimal.ZERO;
        BigDecimal advance = BigDecimal.ZERO;

        final Map<String, BigDecimal> retMap = new HashMap<>();

        if (currDemand != null)
            dmdCollList = propertyDAO.getInstallmentAndReasonWiseDemandDetails(currDemand);

        final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());

        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            instId = Integer.valueOf(listObj[0].toString());
            code = listObj[1].toString();
            demand = listObj[2] != null ? new BigDecimal((Double) listObj[2]) : BigDecimal.ZERO;
            collection = listObj[3] != null ? new BigDecimal((Double) listObj[3]) : BigDecimal.ZERO;
            installment = installmentDao.findById(instId, false);

            if (currYearInstMap.get(CURRENTYEAR_FIRST_HALF).equals(installment) && installment.getId().equals(instId)) {
                if (!code.equals(PropertyTaxConstants.ADVANCE_DMD_RSN_CODE)
                        && !code.equals(PropertyTaxConstants.PENALTY_DMD_RSN_CODE)) {
                    currFirstHalfDmd = currFirstHalfDmd.add(demand);
                    if (collection.compareTo(BigDecimal.ZERO) > 0)
                        currFirstHalfCollection = currFirstHalfCollection.add(collection);
                } else if (!code.equals(PropertyTaxConstants.ADVANCE_DMD_RSN_CODE)
                        && code.equals(PropertyTaxConstants.PENALTY_DMD_RSN_CODE)) {
                    currFirstHalfPenalty = currFirstHalfPenalty.add(demand);
                    if (collection.compareTo(BigDecimal.ZERO) > 0)
                        currFirstHalfCollection = currFirstHalfCollection.add(collection);
                }
            } else if (currYearInstMap.get(CURRENTYEAR_SECOND_HALF).equals(installment) && installment.getId().equals(instId)) {

                if (!code.equals(PropertyTaxConstants.ADVANCE_DMD_RSN_CODE)
                        && !code.equals(PropertyTaxConstants.PENALTY_DMD_RSN_CODE)) {
                    currSecondHalfDmd = currSecondHalfDmd.add(demand);
                    if (collection.compareTo(BigDecimal.ZERO) > 0)
                        currSecondHalfCollection = currSecondHalfCollection.add(collection);
                    else if (!code.equals(PropertyTaxConstants.ADVANCE_DMD_RSN_CODE)
                            && code.equals(PropertyTaxConstants.PENALTY_DMD_RSN_CODE)) {
                        currSecondHalfPenalty = currSecondHalfPenalty.add(demand);
                        if (collection.compareTo(BigDecimal.ZERO) > 0)
                            currSecondHalfCollection = currSecondHalfCollection.add(collection);
                    }
                }
            } else if (!code.equals(PropertyTaxConstants.ADVANCE_DMD_RSN_CODE)
                    && !code.equals(PropertyTaxConstants.PENALTY_DMD_RSN_CODE)) {
                arrDmd = arrDmd.add(demand);
                if (collection.compareTo(BigDecimal.ZERO) > 0)
                    arrColelection = arrColelection.add(collection);
            } else if (!code.equals(PropertyTaxConstants.ADVANCE_DMD_RSN_CODE)
                    && code.equals(PropertyTaxConstants.PENALTY_DMD_RSN_CODE)) {
            }
            if (code.equals(PropertyTaxConstants.ADVANCE_DMD_RSN_CODE))
                advance = advance.add(collection);
        }

        retMap.put(PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR, currFirstHalfDmd);
        retMap.put(PropertyTaxConstants.CURR_SECONDHALF_DMD_STR, currSecondHalfDmd);
        retMap.put(PropertyTaxConstants.ARR_DMD_STR, arrDmd);
        retMap.put(PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR, currFirstHalfCollection);
        retMap.put(PropertyTaxConstants.CURR_SECONDHALF_COLL_STR, currSecondHalfCollection);
        retMap.put(PropertyTaxConstants.ARR_COLL_STR, arrColelection);
        retMap.put(PropertyTaxConstants.CURR_FIRSTHALF_PENALTY_DMD_STR, currFirstHalfPenalty);
        retMap.put(PropertyTaxConstants.CURR_SECONDHALF_PENALTY_DMD_STR, currSecondHalfPenalty);
        retMap.put(PropertyTaxConstants.ARR_PENALTY_DMD_STR, arrPenalty);
        retMap.put(PropertyTaxConstants.CURR_FIRSTHALF_PENALTY_COLL_STR, currFirstHalfPenaltyColllection);
        retMap.put(PropertyTaxConstants.CURR_SECONDHALF_PENALTY_COLL_STR, currSecondHalfPenaltyCollection);
        retMap.put(PropertyTaxConstants.ARR_PENALTY_COLL_STR, arrPenaltyCollection);
        retMap.put(PropertyTaxConstants.ADVANCE_DMD_RSN_CODE, advance);

        return retMap;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, BigDecimal> getPenaltyDemandCollMap(final Property property) {
        final Ptdemand currDemand = getNonHistoryCurrDmdForProperty(property);
        Installment installment = null;
        List penaltyDmdCollList = new ArrayList();
        final Installment currInst = null;
        Integer instId = null;
        BigDecimal currPenalty = BigDecimal.ZERO;
        BigDecimal arrPenalty = BigDecimal.ZERO;
        BigDecimal currPenaltyColl = BigDecimal.ZERO;
        BigDecimal arrPenaltyColl = BigDecimal.ZERO;
        final Map<String, BigDecimal> retMap = new HashMap<String, BigDecimal>();

        if (currDemand != null)
            penaltyDmdCollList = propertyDAO.getPenaltyDmdCollAmtInstWise(currDemand);
        for (final Object object : penaltyDmdCollList) {
            final Object[] listObj = (Object[]) object;
            instId = Integer.valueOf(listObj[0].toString());
            installment = installmentDao.findById(instId, false);
            if (installment.equals(currInst)) {
                if (listObj[2] != null && !new BigDecimal((Double) listObj[2]).equals(BigDecimal.ZERO))
                    currPenaltyColl = currPenaltyColl.add(new BigDecimal((Double) listObj[2]));
                if (listObj[3] != null && !new BigDecimal((Double) listObj[3]).equals(BigDecimal.ZERO))
                    currPenaltyColl = currPenaltyColl.add(new BigDecimal((Double) listObj[3]));
                currPenalty = currPenalty.add(new BigDecimal((Double) listObj[1]));
            } else {
                arrPenalty = arrPenalty.add(new BigDecimal((Double) listObj[1]));
                if (listObj[2] != null && !new BigDecimal((Double) listObj[2]).equals(BigDecimal.ZERO))
                    arrPenaltyColl = arrPenaltyColl.add(new BigDecimal((Double) listObj[2]));
                if (listObj[3] != null && !new BigDecimal((Double) listObj[3]).equals(BigDecimal.ZERO))
                    arrPenaltyColl = arrPenaltyColl.add(new BigDecimal((Double) listObj[3]));
            }
        }
        retMap.put(PropertyTaxConstants.CURR_PENALTY_DMD_STR, currPenalty);
        retMap.put(PropertyTaxConstants.ARR_PENALTY_DMD_STR, arrPenalty);
        retMap.put(PropertyTaxConstants.CURR_PENALTY_COLL_STR, currPenaltyColl);
        retMap.put(PropertyTaxConstants.ARR_PENALTY_COLL_STR, arrPenaltyColl);
        return retMap;
    }

    /**
     * This method returns current installment non-history EgptPtdemand
     * <p>
     * This method returns EgptPtdemand Object for given property .
     * </p>
     *
     * @param org .egov.ptis.property.model.Property property
     * @return EgptPtdemand Object.
     */
    @Override
    public Ptdemand getNonHistoryCurrDmdForProperty(final Property property) {
        Query qry = null;
        Ptdemand egptPtdemand = null;

        if (property != null) {
            final CFinancialYear currentFinancialYear = financialYearDAO.getFinancialYearByDate(new Date());
            qry = getCurrentSession()
                    .createQuery(
                            "from  Ptdemand egptDem left join fetch egptDem.egDemandDetails dd left join fetch dd.egDemandReason dr "
                                    + "where egptDem.egptProperty =:property "
                                    + "and (egptDem.egInstallmentMaster.fromDate <= :fromYear and egptDem.egInstallmentMaster.toDate >=:toYear) and egptDem.isHistory='N' ");
            qry.setEntity(PROPERTY, property);
            qry.setDate("fromYear", currentFinancialYear.getStartingDate());
            qry.setDate("toYear", currentFinancialYear.getStartingDate());
            final List<Ptdemand> ptDemandResult = qry.list();
            if (ptDemandResult != null && ptDemandResult.size() > 0)
                egptPtdemand = ptDemandResult.get(0);
        }
        return egptPtdemand;
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public Ptdemand findById(final Integer id, final boolean lock) {
        return null;
    }

    @Override
    public Ptdemand create(final Ptdemand ptdemand) {
        return null;
    }

    @Override
    public void delete(final Ptdemand ptdemand) {

    }

    @Override
    public Ptdemand update(final Ptdemand ptdemand) {
        getCurrentSession().update(ptdemand);
        return ptdemand;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getPropertyTaxDetails(final String assessmentNo) {
        List<Object> list = new ArrayList<Object>();
        final CFinancialYear currentFinancialYear = financialYearDAO.getFinancialYearByDate(new Date());
        String selectQuery = " select drm.code, inst.description, dd.amount, dd.amt_collected "
                + " from egpt_basic_property bp, egpt_property prop, egpt_ptdemand ptd, eg_demand d, eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm, eg_installment_master inst "
                + " where bp.id = prop.id_basic_property and prop.status  in ('A','I') "
                + " and prop.id = ptd.id_property and ptd.id_demand = d.id "
                + " and d.id = dd.id_demand "
                + " and dd.id_demand_reason = dr.id and drm.id = dr.id_demand_reason_master "
                + " and dr.id_installment = inst.id and bp.propertyid =:assessmentNo"
                + " and dd.amount > dd.amt_collected  "
                + " and d.id_installment =(select id from eg_installment_master "
                + " where start_date <= :fromYear and end_date >=:toYear and id_module=(select id from eg_module where name='Property Tax' ))  ";
        selectQuery = selectQuery + " order by inst.description desc ";

        final Query qry = getCurrentSession().createSQLQuery(selectQuery).setString("assessmentNo", assessmentNo)
                .setDate("fromYear", currentFinancialYear.getStartingDate())
                .setDate("toYear", currentFinancialYear.getStartingDate());
        list = qry.list();
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getTaxDetailsForWaterConnection(final String consumerNo, final String connectionType) {
        List<Object> list = new ArrayList<Object>();
        String selectQuery = "";
        if (connectionType.equals("METERED"))
            selectQuery = " select drm.code, inst.description, dd.amount, dd.amt_collected "
                    + " from  egwtr_connection conn,egwtr_connectiondetails bp, egwtr_demand_connection demconn ,eg_demand d, eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm, eg_installment_master inst "
                    + " where conn.id =bp.connection "
                    + " and demconn.connectiondetails = bp.id "
                    + " and demconn.demand = d.id "
                    + " and d.id = dd.id_demand "
                    + " and dd.id_demand_reason = dr.id and drm.id = dr.id_demand_reason_master "
                    + " and dr.id_installment = inst.id and conn.consumercode =:consumerNo"
                    + " and dd.amount > dd.amt_collected  "
                    + " and d.id_installment =(select id from eg_installment_master where now() between start_date and end_date and id_module=(select id from eg_module where name='Water Tax Management' ) and installment_type='Monthly' )  ";
        else
            selectQuery = " select drm.code, inst.description, dd.amount, dd.amt_collected "
                    + " from  egwtr_connection conn,egwtr_connectiondetails bp,egwtr_demand_connection demconn , eg_demand d, eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm, eg_installment_master inst "
                    + " where conn.id =bp.connection " + " and demconn.connectiondetails = bp.id " + " and demconn.demand = d.id "
                    + " and d.id = dd.id_demand "
                    + " and dd.id_demand_reason = dr.id and drm.id = dr.id_demand_reason_master "
                    + " and d.is_history='N' "
                    + " and dr.id_installment = inst.id and conn.consumercode =:consumerNo"
                    + " and dd.amount > dd.amt_collected  ";
        // +
        // " and d.id_installment =(select id from eg_installment_master where now() between start_date and end_date and
        // id_module=(select id from eg_module where name='Property Tax' ) ) ";
        selectQuery = selectQuery + " order by inst.description desc ";

        final Query qry = getCurrentSession().createSQLQuery(selectQuery).setString("consumerNo", consumerNo);
        list = qry.list();
        return list;
    }

    @Override
    @Deprecated
    public Set<String> getDemandYears(final String assessmentNo) {
        final Set<String> demandYears = new LinkedHashSet<String>();
        final String selectQuery = " select inst.description "
                + " from egpt_basic_property bp, egpt_property prop, egpt_ptdemand ptd, eg_demand d, eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm, eg_installment_master inst "
                + " where bp.id = prop.id_basic_property and prop.status = 'A' "
                + " and prop.id = ptd.id_property and ptd.id_demand = d.id "
                + " and d.id = dd.id_demand "
                + " and dd.id_demand_reason = dr.id and drm.id = dr.id_demand_reason_master "
                + " and dr.id_installment = inst.id and bp.propertyid =:assessmentNo "
                + " and d.id_installment =(select id from eg_installment_master where now() between start_date and end_date and id_module=(select id from eg_module where name='Property Tax' )) order by inst.start_date ";
        final Query qry = getCurrentSession().createSQLQuery(selectQuery).setString("assessmentNo", assessmentNo);
        for (final Object record : qry.list())
            demandYears.add((String) record);
        return demandYears;
    }

    @Override
    public Map<String, BigDecimal> getDemandIncludingPenaltyCollMap(final Property property) {
        final Ptdemand currDemand = getNonHistoryCurrDmdForProperty(property);
        Installment installment = null;
        List dmdCollList = new ArrayList();
        Integer instId = null;
        BigDecimal currFirstHalfDmd = BigDecimal.ZERO;
        BigDecimal currSecondHalfDmd = BigDecimal.ZERO;
        BigDecimal arrDmd = BigDecimal.ZERO;
        BigDecimal currFirstHalfCollection = BigDecimal.ZERO;
        BigDecimal currSecondHalfCollection = BigDecimal.ZERO;
        BigDecimal arrColelection = BigDecimal.ZERO;
        BigDecimal demand = BigDecimal.ZERO;
        BigDecimal collection = BigDecimal.ZERO;
        final Map<String, BigDecimal> retMap = new HashMap<String, BigDecimal>();

        if (currDemand != null)
            dmdCollList = propertyDAO.getTotalDemandDetailsIncludingPenalty(currDemand);

        final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());

        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            instId = Integer.valueOf(listObj[0].toString());
            demand = listObj[1] != null ? new BigDecimal((Double) listObj[1]) : BigDecimal.ZERO;
            collection = listObj[2] != null ? new BigDecimal((Double) listObj[2]) : BigDecimal.ZERO;

            installment = installmentDao.findById(instId, false);
            if (currYearInstMap.get(CURRENTYEAR_FIRST_HALF).equals(installment)) {
                if (collection.compareTo(BigDecimal.ZERO) == 1)
                    currFirstHalfCollection = currFirstHalfCollection.add(collection);
                currFirstHalfDmd = currFirstHalfDmd.add(demand);
            } else if (currYearInstMap.get(CURRENTYEAR_SECOND_HALF).equals(installment)) {
                if (collection.compareTo(BigDecimal.ZERO) == 1)
                    currSecondHalfCollection = currSecondHalfCollection.add(collection);
                currSecondHalfDmd = currSecondHalfDmd.add(demand);
            } else {
                arrDmd = arrDmd.add(demand);
                if (collection.compareTo(BigDecimal.ZERO) == 1)
                    arrColelection = arrColelection.add(collection);
            }
        }
        retMap.put(PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR, currFirstHalfDmd);
        retMap.put(PropertyTaxConstants.CURR_SECONDHALF_DMD_STR, currSecondHalfDmd);
        retMap.put(PropertyTaxConstants.ARR_DMD_STR, arrDmd);
        retMap.put(PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR, currFirstHalfCollection);
        retMap.put(PropertyTaxConstants.CURR_SECONDHALF_COLL_STR, currSecondHalfCollection);
        retMap.put(PropertyTaxConstants.ARR_COLL_STR, arrColelection);
        return retMap;
    }

}
