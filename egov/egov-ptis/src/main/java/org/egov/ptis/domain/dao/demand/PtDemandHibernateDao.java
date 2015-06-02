/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.dao.demand;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
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
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "ptDemandDAO")
@Transactional(readOnly = true)
public class PtDemandHibernateDao implements PtDemandDao {
	private static final String BILLID_PARAM = "billid";
	private static final String PROPERTY = "property";
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleService moduleDao;
	@Autowired
	private InstallmentDao installmentDao;
	@Autowired
	@Qualifier(value = "demandGenericDAO")
	private DemandGenericDao demandGenericDAO;

	@Autowired
	private PropertyDAO propertyDAO;

	@PersistenceContext
	private EntityManager entityManager;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	/**
	 * This method called getCurrentDemandforProperty gets Total Current Demand
	 * Amount .
	 * 
	 * <p>
	 * This method returns Total Current Demand for given property.
	 * </p>
	 * 
	 * @param org
	 *            .egov.ptis.property.model.Property property
	 * 
	 * @return a BigDecimal.
	 */

	@Override
	public BigDecimal getCurrentDemandforProperty(Property property) {

		BigDecimal currentDemand = BigDecimal.ZERO;
		Query qry = null;
		if (property != null) {
			qry = getCurrentSession()
					.createQuery(
							" select sum(DmdDetails.amount) from  EgptPtdemand egDemand left join  egDemand.egDemandDetails DmdDetails where "
									+ " egDemand.egptProperty =:property  and egDemand.isHistory='N'   ");
			qry.setEntity(PROPERTY, property);
			currentDemand = (BigDecimal) qry.uniqueResult();
		}

		return currentDemand;

	}

	/**
	 * This method called WhetherBillExistsForProperty gets Character .
	 * 
	 * <p>
	 * This method returns Character for given Property , billnum and Module.
	 * </p>
	 * 
	 * @param org
	 *            .egov.ptis.property.model.Property property
	 * 
	 * @param java
	 *            .lang.Integer billnum
	 * 
	 * @param org
	 *            .egov.infstr.commons.Module module
	 * 
	 * @return Character of 'Y' or 'N'.
	 */

	@Override
	public Character whetherBillExistsForProperty(Property property, String billnum, Module module) {
		Character status = null;
		Query qry = null;
		List<EgBill> list;
		if (property != null && billnum != null) {
			List<EgBill> egBillList = demandGenericDAO.getBillsByBillNumber(billnum, module);
			if (egBillList == null || egBillList.isEmpty()) {
				status = 'N';
			} else {
				EgBill egBill = egBillList.get(0);
				if (egBill == null) {
					status = 'N';
				} else {
					qry = getCurrentSession()
							.createQuery(
									"select egBill from  EgptPtdemand egptDem , EgBill egBill  where egptDem.egptProperty =:property and :egBill in elements(egptDem.egBills)   ");
					qry.setEntity(PROPERTY, property);
					qry.setEntity("egBill", egBill);
					list = qry.list();
					if (list.isEmpty()) {
						status = 'N';
					} else {
						status = 'Y';
					}
				}
			}
		}
		return status;
	}

	/**
	 * This method called getNonHistoryDemandForProperty gets EgptPtdemand
	 * Object which is NonHistory.
	 * 
	 * <p>
	 * This method returns EgptPtdemand Object for given property .
	 * </p>
	 * 
	 * @param org
	 *            .egov.ptis.property.model.Property property
	 * 
	 * @return EgptPtdemand Object.
	 * 
	 */

	@Override
	public Ptdemand getNonHistoryDemandForProperty(Property property) {
		Query qry = null;
		Ptdemand egptPtdemand = null;

		if (property != null) {
			qry = getCurrentSession().createQuery(
					"from  Ptdemand egptDem where egptDem.egptProperty =:property   ");
			qry.setEntity(PROPERTY, property);
			if (qry.list().size() == 1) {
				egptPtdemand = (Ptdemand) qry.uniqueResult();
			} else {
				egptPtdemand = (Ptdemand) qry.list().get(0);
			}
		}
		return egptPtdemand;
	}

	/**
	 * This method called getDmdDetailsByPropertyIdBoundary gets DemandDetails
	 * List .
	 * 
	 * <p>
	 * This method returns DemandDetails List for given BasicProperty Object &
	 * Boundary Object(Optional) .
	 * </p>
	 * 
	 * @param org
	 *            .egov.ptis.property.model.BasicProperty basicProperty
	 * 
	 * @param org
	 *            .egov.lib.admbndry.Boundary divBoundary
	 * 
	 * @return DemandDetails List.
	 * 
	 */

	// Here divBoundary is used because in some Property Tax Applications(like
	// COC) propertyId is not unique ,It is unique within the Division Boundary
	// check the demandReasonMaster list . It is not working
	@Override
	public List getDmdDetailsByPropertyIdBoundary(BasicProperty basicProperty, Boundary divBoundary) {
		String divStatus = "N";
		List list = new ArrayList();
		StringBuffer qry = new StringBuffer(50);
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
			if ("Y".equals(divStatus)) {
				qry.append(" and ppid.wardId =:divBoundary  ");
			}

			Query query = getCurrentSession().createQuery(qry.toString());
			query.setEntity("basicProperty", basicProperty);
			if ("Y".equals(divStatus)) {
				query.setEntity("divBoundary", divBoundary);
			}
			list = query.list();
		}
		return list;
	}

	public List getTransactionByBasicProperty(BasicProperty basicProperty, Installment installment,
			String is_cancelled) {
		Query qry = null;
		List list = new ArrayList(0);
		if (basicProperty != null && installment != null && is_cancelled != null
				&& !is_cancelled.equals("")) {
			qry = getCurrentSession()
					.createQuery(
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
	 * 
	 * <p>
	 * This method returns Map of DemandReason Objects and DemandDetails amount
	 * for given BasicProperty & divBoundary .
	 * </p>
	 * 
	 * @param org
	 *            .egov.ptis.property.model.BasicProperty BasicProperty
	 * 
	 * @param org
	 *            .egov.lib.admbndry.Boundary divBoundary
	 * 
	 * @return Map<EgDemandReason,Amount>.
	 */

	@Override
	public Map getAllDemands(BasicProperty basicProperty, Boundary divBoundary) {
		List<EgDemandDetails> demandDetailsList;
		BigDecimal amount = BigDecimal.ZERO;
		demandDetailsList = getDmdDetailsByPropertyIdBoundary(basicProperty, divBoundary);
		Map<EgDemandReason, BigDecimal> dmdMap = new HashMap<EgDemandReason, BigDecimal>();
		Iterator iter = demandDetailsList.iterator();
		while (iter.hasNext()) {
			EgDemandDetails egDemandDetails = (EgDemandDetails) iter.next();
			if (egDemandDetails.getEgDemandReason() != null) {
				if (dmdMap.containsKey(egDemandDetails.getEgDemandReason())) {
					dmdMap.put(egDemandDetails.getEgDemandReason(), egDemandDetails.getAmount());
				} else {
					amount = amount.add(egDemandDetails.getAmount());
					dmdMap.put(egDemandDetails.getEgDemandReason(), amount);
				}
			}
		}
		return dmdMap;
	}

	/**
	 * Gets the current 1) demand amount, 2) collected amount, 3) rebate amount
	 * for the demand associated with the given bill ID.
	 */
	@Override
	public List<BigDecimal> getCurrentAmountsFromBill(Long billId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT").append("    SUM(det.amount),").append("    SUM(det.amt_collected),")
				.append("    SUM(det.amt_rebate)").append(" FROM ").append("    eg_bill bill,")
				.append("    eg_demand_details det,").append("    eg_demand_reason reas,")
				.append("    eg_demand_reason_master reasm,")
				.append("    eg_reason_category reascat,")
				.append("    eg_installment_master inst,").append("    eg_module module")
				.append(" WHERE ").append("    bill.id = :").append(BILLID_PARAM).append(" AND")
				.append("    det.id_demand = bill.id_demand AND")
				.append("    det.id_demand_reason = reas.id AND")
				.append("    reas.id_installment = inst.id_installment AND")
				.append("    reas.id_demand_reason_master = reasm.id AND")
				.append("    reasm.id_category = reascat.id_type AND")
				.append("    reascat.code = 'TAX' AND")
				.append("    inst.start_date <= SYSDATE AND")
				.append("    inst.end_date   >= SYSDATE AND")
				.append("    inst.id_module = module.id_module AND")
				.append("    module.module_name = 'Property Tax'");
		Query query = getCurrentSession().createSQLQuery(sb.toString());
		query.setLong(BILLID_PARAM, billId);
		Object[] results = (Object[]) query.uniqueResult();
		List<BigDecimal> amounts = new ArrayList<BigDecimal>();
		amounts.add((BigDecimal) results[0]); // demand
		amounts.add((BigDecimal) results[1]); // collection
		amounts.add((BigDecimal) results[2]); // rebate
		return amounts;
	}

	@Override
	public Map<String, BigDecimal> getDemandCollMap(Property property) {
		Ptdemand currDemand = getNonHistoryCurrDmdForProperty(property);
		Installment installment = null;
		Installment currInst = null;
		Integer instId = null;
		BigDecimal currDmd = BigDecimal.ZERO;
		BigDecimal arrDmd = BigDecimal.ZERO;
		BigDecimal currCollection = BigDecimal.ZERO;
		BigDecimal arrColelection = BigDecimal.ZERO;
		Map<String, BigDecimal> retMap = new HashMap<String, BigDecimal>();
		List dmdCollList = propertyDAO.getDmdCollAmtInstWise(currDemand);
		Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		currInst = installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());
		for (Object object : dmdCollList) {
			Object[] listObj = (Object[]) object;
			instId = Integer.valueOf(((BigDecimal) listObj[0]).toString());
			installment = (Installment) installmentDao.findById(instId, false);
			if (currInst.equals(installment)) {
				if (listObj[2] != null && !listObj[2].equals(BigDecimal.ZERO)) {
					currCollection = currCollection.add((BigDecimal) listObj[2]);
				}
				/*
				 * adding rebate to collection (commenting this code because,
				 * the rebate amt is been added to collection amt and is shown
				 * as a negative amt in 'Current Tax Due' in search results and
				 * view property screen)
				 */
				if (listObj[3] != null && !listObj[3].equals(BigDecimal.ZERO)) {
					currCollection = currCollection.add((BigDecimal) listObj[3]);
				}
				currDmd = currDmd.add((BigDecimal) listObj[1]);
			} else {
				arrDmd = arrDmd.add((BigDecimal) listObj[1]);
				if (listObj[2] != null && !listObj[2].equals(BigDecimal.ZERO)) {
					arrColelection = arrColelection.add((BigDecimal) listObj[2]);
				}
				/*
				 * adding rebate to collection (commenting this code because,
				 * the rebate amt is been added to collection amt and is shown
				 * as a negative amt in search results and view property screen)
				 */
				if (listObj[3] != null && !listObj[3].equals(BigDecimal.ZERO)) {
					arrColelection = arrColelection.add((BigDecimal) listObj[3]);
				}
			}
		}
		retMap.put(PropertyTaxConstants.CURR_DMD_STR, currDmd);
		retMap.put(PropertyTaxConstants.ARR_DMD_STR, arrDmd);
		retMap.put(PropertyTaxConstants.CURR_COLL_STR, currCollection);
		retMap.put(PropertyTaxConstants.ARR_COLL_STR, arrColelection);
		return retMap;
	}

	/**
	 * This method returns current installment non-history EgptPtdemand
	 * 
	 * <p>
	 * This method returns EgptPtdemand Object for given property .
	 * </p>
	 * 
	 * @param org
	 *            .egov.ptis.property.model.Property property
	 * 
	 * @return EgptPtdemand Object.
	 * 
	 */
	@Override
	public Ptdemand getNonHistoryCurrDmdForProperty(Property property) {
		Query qry = null;
		Ptdemand egptPtdemand = null;

		if (property != null) {
			qry = getCurrentSession()
					.createQuery(
							"from  Ptdemand egptDem where egptDem.egptProperty =:property and (egptDem.egInstallmentMaster.fromDate <= :fromYear and egptDem.egInstallmentMaster.toDate >=:toYear) ");
			qry.setEntity(PROPERTY, property);
			qry.setDate("fromYear", new Date());
			qry.setDate("toYear", new Date());
			if (qry.list().size() == 1) {
				egptPtdemand = (Ptdemand) qry.uniqueResult();
			} else {
				egptPtdemand = (Ptdemand) qry.list().get(0);
			}
		}
		return egptPtdemand;
	}

	@Override
	public List findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ptdemand findById(Integer id, boolean lock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ptdemand create(Ptdemand ptdemand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Ptdemand ptdemand) {
		// TODO Auto-generated method stub

	}

	@Override
	public Ptdemand update(Ptdemand ptdemand) {
		// TODO Auto-generated method stub
		return null;
	}

}
