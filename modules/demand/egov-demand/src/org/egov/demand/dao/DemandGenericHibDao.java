package org.egov.demand.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.infstr.commons.Module;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class DemandGenericHibDao implements DemandGenericDao{

	/**
	 * This method called getDemandReasonMasterByModule gets  List<EgDemandReasonMaster> objects.
	 *
	 * <p>This method returns  List<EgDemandReasonMaster> objects for given  module.</p>
	 *
	 * @param org.egov.infstr.commons.Module module.
	 *
	 * @return  List<EgDemandReasonMaster> objects.
	 *
	 * 
	 */

	public List<EgDemandReasonMaster> getDemandReasonMasterByModule(Module module)
	{
		List<EgDemandReasonMaster> list=new ArrayList<EgDemandReasonMaster>();
		Query qry=null;
		if(module!=null)
		{
			qry=HibernateUtil.getCurrentSession().createQuery(" from EgDemandReasonMaster where egModule =:module ");
			qry.setEntity("module", module);
			list=qry.list();
		}

		return list;
	}

	/**
	 * This method called getDemandReasonMasterByCategoryAndModule gets  List<EgDemandReasonMaster> objects.
	 *
	 * <p>This method returns  List<EgDemandReasonMaster> objects for given egReasonCategory and  module.</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgReasonCategory egReasonCategory.
	 *
	 * @param org.egov.infstr.commons.Module module.
	 *
	 * @return  List<EgDemandReasonMaster> objects.
	 *
	 * 
	 */

	public List<EgDemandReasonMaster> getDemandReasonMasterByCategoryAndModule(EgReasonCategory egReasonCategory,Module module)
	{
		List<EgDemandReasonMaster> list=new ArrayList<EgDemandReasonMaster>();
		Query qry=null;
		if(egReasonCategory!=null && module!=null)
		{
			qry=HibernateUtil.getCurrentSession().createQuery(" from EgDemandReasonMaster where egModule =:module and egReasonCategory =:egReasonCategory   ");
			qry.setEntity("module", module);
			qry.setEntity("egReasonCategory", egReasonCategory);
			list=qry.list();
		}
		return list;
	}

	/**
	 * This method called getDemandReasonMasterByCode gets  EgDemandReasonMaster object.
	 *
	 * <p>This method returns  EgDemandReasonMaster object for given code and  module.</p>
	 *
	 * @param java.lang.Integer code.
	 *
	 * @param org.egov.infstr.commons.Module module.
	 *
	 * @return  EgDemandReasonMaster object.
	 *
	 * 
	 */

	public  EgDemandReasonMaster getDemandReasonMasterByCode(String code,Module module)
	{
		EgDemandReasonMaster egDemandReasonMaster=null;
		Query qry=null;
		if(code!=null && !code.equals("") && module!=null)
		{
			qry=HibernateUtil.getCurrentSession().createQuery(" from EgDemandReasonMaster ReasonMaster where  ReasonMaster.egModule =:module and  ReasonMaster.code =:code  ");
			qry.setEntity("module", module);
			qry.setString("code", code);
			egDemandReasonMaster=(EgDemandReasonMaster)qry.uniqueResult();
		}
		return egDemandReasonMaster;
	}

	/**
	 * This method called getDemandReasonByInstallmentAndModule gets  List<EgDemandReason> objects.
	 *
	 * <p>This method returns  List<EgDemandReason> objects for given egReasonCategory and  module.</p>
	 *
	 * @param org.egov.commons.Installment installment.
	 *
	 * @param org.egov.infstr.commons.Module module.
	 *
	 * @return  List<EgDemandReason> objects.
	 *
	 * 
	 */

	public List<EgDemandReason> getDemandReasonByInstallmentAndModule(Installment installment,Module module)
	{
		List<EgDemandReason> list=new ArrayList<EgDemandReason>();
		Query qry=null;
		if(installment!=null && module!=null)
		{
			qry=HibernateUtil.getCurrentSession().createQuery(" select DmdReason from EgDemandReason DmdReason , EgDemandReasonMaster master  where DmdReason.egDemandReasonMaster.id = master.id and master.egModule =:module and DmdReason.egInstallmentMaster =:installment   ");
			qry.setEntity("module", module);
			qry.setEntity("installment", installment);
			list=qry.list();
		}
		return list;
	}

	/**
	 * This method called getDemandReasonByDemandReasonMaster gets  List<EgDemandReason> objects.
	 *
	 * <p>This method returns  List<EgDemandReason> objects for given code and  module.</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgDemandReasonMaster egDemandReasonMaster.
	 *
	 * @return  List<EgDemandReason> objects.
	 *
	 * 
	 */

	public  List<EgDemandReason> getDemandReasonByDemandReasonMaster(EgDemandReasonMaster egDemandReasonMaster)
	{
		List<EgDemandReason> list=new ArrayList<EgDemandReason>();
		Query qry=null;
		if(egDemandReasonMaster!=null )
		{
			qry=HibernateUtil.getCurrentSession().createQuery(" from EgDemandReason DmdReason  where  DmdReason.egDemandReasonMaster =:egDemandReasonMaster  ");
			qry.setEntity("egDemandReasonMaster", egDemandReasonMaster);
			list=qry.list();
		}
		return list;
	}

	/**
	 * This method called getEgReasonCategoryByCode gets  List<EgReasonCategory> objects.
	 *
	 * <p>This method returns  List<EgReasonCategory> objects for given code and  module.</p>
	 *
	 * @param java.lang.String code.
	 *
	 * @return  List<EgReasonCategory> objects.
	 *
	 * 
	 */

	public  List<EgReasonCategory> getEgReasonCategoryByCode(String code)
	{
		List<EgReasonCategory> list=new ArrayList<EgReasonCategory>();
		Query qry=null;
		if(code!=null && !code.equals(""))
		{
			qry=HibernateUtil.getCurrentSession().createQuery(" from EgReasonCategory  where code =:code ");
			qry.setString("code", code);
			list=qry.list();
		}
		return list;
	}

	/**
	 * This method called getDmdReasonByDmdReasonMsterInstallAndMod gets  EgDemandReason object.
	 *
	 * <p>This method returns  EgDemandReason object for given demandReasonMaster , installment  and  module.</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgDemandReasonMaster egDemandReasonMaster.
	 * 
	 * @param org.egov.commons.Installment installment.
	 * 
	 * @param org.egov.infstr.commons.Module module.
	 *
	 * @return  EgDemandReason object.
	 *
	 * 
	 */

	public EgDemandReason getDmdReasonByDmdReasonMsterInstallAndMod(EgDemandReasonMaster demandReasonMaster,Installment installment,Module module)
	{
		EgDemandReason egDemandReason=null;
		Query qry=null;
		if(demandReasonMaster!=null && installment!=null &&  module!=null)
		{
			qry=HibernateUtil.getCurrentSession().createQuery(" select DmdReason from EgDemandReason DmdReason , EgDemandReasonMaster master  where DmdReason.egDemandReasonMaster =:demandReasonMaster and master.egModule =:module and DmdReason.egInstallmentMaster =:installment   ");
			qry.setEntity("demandReasonMaster", demandReasonMaster);
			qry.setEntity("module", module);
			qry.setEntity("installment", installment);
			if(qry.list().size()==1)
				egDemandReason=(EgDemandReason)qry.uniqueResult();
			else if(qry.list().size()>1)
				egDemandReason=(EgDemandReason)qry.list().get(0);

		}
		return egDemandReason;
	}

	/**
	 * This method called getDemandDetailsForDemand gets  List<EgDemandDetails> objects.
	 *
	 * <p>This method returns  List<EgDemandDetails> objects for given EgDemand and  EgwStatus.</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgDemand demand.
	 * 
	 * @param org.egov.commons.EgwStatus status.
	 *
	 * @return  List<EgDemandDetails> objects.
	 *
	 * 
	 */

	public List<EgDemandDetails> getDemandDetailsForDemand(EgDemand demand, EgwStatus status) 
	{
		StringBuffer qryStr = new StringBuffer(2000);
		List<EgDemandDetails> list=new ArrayList<EgDemandDetails>();
		Query qry=null;
		if(demand!=null)
		{
			qryStr.append(" select DmdDetails from EgDemand egdemand left join egdemand.egDemandDetails DmdDetails  where egdemand =:demand  ");
			if(status!=null)
			{
				qryStr.append(" and DmdDetails.egwStatus =:status ");
			}
			qry=HibernateUtil.getCurrentSession().createQuery(qryStr.toString());
			qry.setEntity("demand", demand);
			if(status!=null)
			{
				qry.setEntity("status", status);
			}
			list=qry.list();
		}

		return list;
	}

	/**
	 * This method called getDemandDetailsForDemandAndReasons gets  List<EgDemandDetails> objects.
	 *
	 * <p>This method returns  List<EgDemandDetails> objects for given EgDemand and List<EgDemandReason> .</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgDemand demand.
	 * 
	 * @param java.util.List<org.egov.infstr.DCB.model.EgDemandReason> list.
	 *
	 * @return  List<EgDemandDetails> objects.
	 *
	 * 
	 */

	public List<EgDemandDetails>  getDemandDetailsForDemandAndReasons(EgDemand  demand ,List<EgDemandReason> demandReasonList )
	{
		List<EgDemandDetails> list=new ArrayList<EgDemandDetails>();
		Query qry=null;
		if(demand!=null && demandReasonList!=null && demandReasonList.size()>0)
		{
			qry=HibernateUtil.getCurrentSession().createQuery("select DmdDetails from EgDemand egdemand left join egdemand.egDemandDetails DmdDetails  where egdemand =:demand and DmdDetails.egDemandReason in (:demandReasonList) ");
			qry.setEntity("demand", demand);
			qry.setParameterList("demandReasonList", demandReasonList);
			list=qry.list();
		}
		return list;

	}

	/**
	 * This method called getAllBillsForDemand gets  List<EgBill> objects .
	 *
	 * <p>This method returns  List<EgBill> objects for given EgDemand , includeHistory and includeCancelled .</p>
	 *
	 * @param org.egov.infstr.DCB.model.EgDemand demand.
	 * 
	 * @param java.lang.String includeHistory.
	 * 
	 * @param java.lang.String includeCancelled.
	 *
	 * @return  List<EgBill> objects.
	 *
	 * 
	 */

	public List<EgBill> getAllBillsForDemand(EgDemand demand, String includeHistory, String includeCancelled)
	{
		List<EgBill> list=new ArrayList<EgBill>();
		Query qry=null;
		StringBuffer qryStr = new StringBuffer(2000);
		if(includeHistory!=null && !includeHistory.equals("") && includeHistory!=null && !includeCancelled.equals(""))
		{
			qryStr.append("from EgBill egBill  where  egBill.is_History =:includeHistory and egBill.is_Cancelled =:includeCancelled  ");
			if(demand !=null)
			{
				qryStr.append(" and egBill" +
				".egDemand =:demand " );
			}
			qry=HibernateUtil.getCurrentSession().createQuery(qryStr.toString());
			if(demand !=null)
			{
				qry.setEntity("demand", demand);
			}
			qry.setString("includeHistory", includeHistory);
			qry.setString("includeCancelled", includeCancelled);

			list=qry.list();
		}
		return list;

	}

	/**
	 * This method called getBillsByBillNumber gets  List<EgBill> objects .
	 *
	 * <p>This method returns  List<EgBill> objects for given BillNo and Module .</p>
	 *
	 * @param org.egov.infstr.commons.Module module.
	 * 
	 * @param java.lang.Long BillNo.
	 *
	 * @return  List<EgBill> objects.
	 *
	 * 
	 */

	public List getBillsByBillNumber(String BillNo,Module module)
	{
		Query qry=null;
		List<EgBill> list =new ArrayList<EgBill>();
		if(BillNo!=null && !BillNo.equals("0") && module!=null)
		{
			qry=HibernateUtil.getCurrentSession().createQuery("from EgBill egBill  where billNo =:BillNo and module =:module ");
			qry.setString("BillNo", BillNo);
			qry.setEntity("module", module);
			list=qry.list();
		}

		return list;
	}

	/**
	 * Called to get the Sum of Demand and Sum of Collection  with installment and EgDemand .
	 *
	 *Penalty amount is being excluded from the Demand and Collection 
	 *
	 * @param oorg.egov.demand.model.EgDemand.
	 * 
	 * @param org.egov.commons.installment
	 *
	 * @return  List of InstallmentId ,Demand and collection.
	 *
	 * 
	 */

	public List getDmdAmtAndCollAmt(EgDemand egDemand, Installment installment) {
		List list = new ArrayList();
		if (egDemand != null && installment != null) {
			StringBuffer strBuf = new StringBuffer(2000);
			strBuf.append(" select dmdRes.ID_INSTALLMENT, sum(dmdDet.amount), sum(dmdDet.amt_collected) ")
			.append(" from eg_demand_details dmdDet,eg_demand_reason dmdRes ,eg_demand_reason_master dmdresmas ") 
			.append(" where dmdDet.id_demand_reason=dmdRes.id and dmdDet.id_demand =:dmdId and dmdres.id_installment =:installId ") 
			.append(" and  dmdresmas.id= dmdres.id_demand_reason_master and dmdresmas.code not in('PENALTY','ADVANCE') ") 
			.append(" group by dmdRes.id_installment	 ");
			Query qry = HibernateUtil.getCurrentSession().createSQLQuery(
					strBuf.toString()).setLong("dmdId", egDemand.getId())
					.setInteger("installId", installment.getId());
			list = qry.list();
		}
		return list;
	}


	/**
	 * Called to get the EgDemandDetails Ids fron EG_DEMAND_DETAILS table .
	 *
	 * ex:- In COCPTIS there are four EgDemandDetails for every Installment 
	 *
	 *@param oorg.egov.demand.model.EgDemand.
	 * 
	 *@param org.egov.commons.installment
	 * 
	 *@param org.egov.infstr.commons.Module
	 *
	 *@param org.egov.demand.model.EgDemandReasonMaster
	 *
	 * @return  List EgDemandDetailIds.
	 *
	 * 
	 */

	public List<EgDemandDetails> getDmdDetailList(EgDemand egDemand,Installment installment, Module module,
			EgDemandReasonMaster dmdResMster) {
		List<EgDemandDetails> demandDetList = new ArrayList<EgDemandDetails>();
		if (egDemand != null && installment != null && module != null
				&& dmdResMster != null) {
			DemandGenericDao dmdDao = new DemandGenericHibDao();
			EgDemandReason dmdRes = dmdDao.getDmdReasonByDmdReasonMsterInstallAndMod(dmdResMster,
					installment, module);
			if (dmdRes == null) {
				throw new EGOVRuntimeException(
						"----EgDemand Reason  is null  For EgDemandID--"
						+ egDemand.getId() + "--with InstallmentID--"
						+ installment.getId());
			}
			List<EgDemandReason> demandReasonList = new ArrayList<EgDemandReason>();
			demandReasonList.add(dmdRes);
			demandDetList = dmdDao.getDemandDetailsForDemandAndReasons(egDemand, demandReasonList);
		}
		return demandDetList;

	}

	/**
	 * Method called  to get DCB(Demand and collection Totals with Some Basic Info). 
	 * 
	 *@param org.egov.demand.model.EgDemand
	 *@param org
	 *            .egov.infstr.commons.Module
	 * 
	 *@return java.util.List
	 * 
	 */

	public List getDCB(EgDemand egDemand, Module module) {
		List list = new ArrayList();
		if (egDemand != null && module!=null) {
			String query=" SELECT dmdres.ID_INSTALLMENT, " + 
			"  NVL(SUM(dmdDet.amount),0), " + 
			"  NVL(SUM(dmdDet.amt_collected),0), " + 
			"  master.id, " + 
			"  master.id_category, " + 
            "  NVL(SUM(dmdDet.amt_rebate),0) " + 
			"FROM eg_demand_details dmddet, " + 
			"  eg_demand_reason dmdres, " + 
			"  eg_demand_reason_master master, " + 
			"  eg_reason_category cate " + 
			"WHERE DMDDET.ID_DEMAND =:dmdId " +  
			"AND DMDDET.ID_DEMAND_REASON =dmdres.id " + 
			"AND DMDRES.ID_DEMAND_REASON_MASTER=master.id " + 
			"AND MASTER.CODE NOT  IN('BASE') " + 
			"AND master.module_id  =:moduleId " + 
			"AND cate.id_type = master.id_category " + 
			"GROUP BY dmdres.ID_INSTALLMENT, " + 
			"  master.id, " + 
			"  master.id_category " + 
			"ORDER BY dmdres.id_installment, " + 
			"  master.id_category  ";
			Query qry = HibernateUtil.getCurrentSession().createSQLQuery(query)
			.setLong("dmdId", egDemand.getId())
			.setLong("moduleId", module.getId());
			list = qry.list();
		}
		return list;
	}
	
	/**
	 * Called to get Reasonwise Demand & collection details
	 * 
	 *@param org.egov.demand.model.EgDemand
	 *@param org.egov.infstr.commons.Module
	 *
	 *@return java.util.List
	 * 
	 */
	
	public List getReasonWiseDCB(EgDemand egDemand, Module module){
		List list = new ArrayList();
		String query = " SELECT drm.code, " +
					   " i.description, " +
					   " dd.amount, " +
					   " dd.amt_collected, " +
					   " dd.amt_rebate " +
					   "FROM eg_demand_details dd, " +
					   " eg_demand_reason dr, " +
					   " eg_installment_master i, " +
					   " eg_demand_reason_master drm " +
					   "WHERE dd.id_demand_reason = dr.id " +
					   " AND dr.id_installment = i.id_installment " +
					   " AND dr.id_demand_reason_master = drm.id " +
					   " AND dd.id_demand =:dmdId " +
					   " AND drm.module_id  =:moduleId "+
					   "ORDER BY i.start_date ";			
		Query qry = HibernateUtil.getCurrentSession().createSQLQuery(query)
		.setLong("dmdId", egDemand.getId())
		.setLong("moduleId", module.getId());
		list = qry.list();
		return list;
	}

	/**
	 * Method called  to get ReasonMaster IDs of the Demand which is provided. 
	 * 
	 *@param org.egov.demand.model.EgDemand
	 * 
	 *@return java.util.List
	 * 
	 */

	public List getEgDemandReasonMasterIds(EgDemand egDemand) {
		List list = new ArrayList();
		if (egDemand != null) {
			String query ="SELECT id " + 
			"FROM eg_demand_reason_master " + 
			"WHERE code NOT LIKE 'BASE' " + 
			"AND id IN " + 
			"  (SELECT id_demand_reason_master " + 
			"  FROM eg_demand_reason " + 
			"  WHERE id IN " + 
			"    (SELECT id_demand_reason " + 
			"    FROM eg_demand_details dmddet " + 
			"    WHERE id_demand =:dmdId " + 
			"    )) " + 
			" ";
			Query qry = HibernateUtil.getCurrentSession().createSQLQuery(query)
			.setLong("dmdId", egDemand.getId());
			list = qry.list();
		}
		return list;
	}

	/**
	 * Method called to get Receipt Details. All the Receipt Details are stored
	 * in Bill Receipt Object i,e Receipt Number ,Receipt Date and Receipt Amount .
	 * 
	 *@param org
	 *            .egov.demand.model.EgDemand
	 * 
	 *@return java.util.List<BillReceipt>
	 * 
	 */
	public List<BillReceipt> getBillReceipts(EgDemand egDemand) {
		List<BillReceipt> billReceipts = null;
		EgBillReceiptDao billRctDao = DCBDaoFactory.getDaoFactory().getEgBillReceiptDao();
		if (egDemand != null) {
			List<EgBill> billList = getAllBillsForDemand(egDemand, "N", "N");
			if (billList != null && !billList.isEmpty()) {
				billReceipts = new ArrayList<BillReceipt>();
				for (EgBill egBill : billList) {
					BillReceipt billRct = billRctDao.getBillReceiptByEgBill(egBill);
					if (billRct != null) {
						billReceipts.add(billRct);
					}
				}
			}
		}
		return billReceipts;
	}

	/**
	 * Method called to get all the Bill Receipt Objects which exists for the Demands .
	 * Bill Receipts are one to one with EgBill.So Bill needs to be existed.
	 * 
	 *@return java.util.List<EgDemand>
	 * 
	 */

	public List<BillReceipt> getBillReceipts(List<EgDemand> egDemand) {
		List<BillReceipt> billReceipts = new ArrayList<BillReceipt>();
		Query qry = null;
		if (egDemand != null && !egDemand.isEmpty()) {
			Criteria criteria=HibernateUtil.getCurrentSession().createCriteria(BillReceipt.class,"billRct")
			.createAlias("billRct.billId", "egBill", CriteriaSpecification.LEFT_JOIN)
			.add(Restrictions.in("egBill.egDemand", egDemand))
			.add(Restrictions.eq("billRct.isCancelled",Boolean.FALSE))
			.addOrder(Order.asc("billRct.receiptDate"));

			billReceipts = criteria.list();
		}
		return billReceipts;
	}

	/**
	 * getReasonCategoryByCode gets  List<EgReasonCategory> objects.
	 *
	 * <p>This method returns  List<EgReasonCategory> objects for code.</p>
	 *
	 * @param code.
	 *
	 * @return  List<EgReasonCategory> objects.
	 *
	 * 
	 */

	public EgReasonCategory getReasonCategoryByCode(String code)
	{
		EgReasonCategory egReasonCategory=new EgReasonCategory();
		Query qry=null;
		if(code!=null && !code.equals(""))
		{
			qry=HibernateUtil.getCurrentSession().createQuery(" from EgReasonCategory where code =:codeStr ");
			qry.setString("codeStr", code);
			egReasonCategory=(EgReasonCategory)qry.uniqueResult();
		}
		return egReasonCategory;
	}

	public EgDemandReason getEgDemandReasonByCodeInstallmentModule(String demandReasonMasterCode,
			Installment installment, Module module,String egReasonCategoryCode) {
		EgDemandReasonMaster egDemandReasonMaster = getDemandReasonMasterByCode(demandReasonMasterCode, module);
		if (egDemandReasonMaster == null) { throw new EGOVRuntimeException(
				" EgDemandReasonMaster is null for the CODE" + demandReasonMasterCode); }

		EgDemandReason egDemandReason = getDmdReasonByDmdReasonMsterInstallAndMod(
				egDemandReasonMaster, installment, module);
		if (egDemandReason == null) { throw new EGOVRuntimeException(
				" EgDemandReason is null for the EgDemandReasonMaster"
				+ egDemandReasonMaster.getId() + "Installment ::"
				+ installment.getFromDate()); }

		return egDemandReason;
	}

	/**
     * Method called to get the balance Amount for the given Demand reason Master Code ,module and demand
     * Installment will be taken from EgDemand.
     *   
     *@param demand - EgDemand Object.
     *@param dmdReasonMasterCode - Code of the EgDemandReasonMaster 
     *@param module - EgModule object 
     *@return java.math.BigDecimal - returns the Balance(Demand - Collection)
     * 
     */
	
	public BigDecimal getBalanceByDmdMasterCode(EgDemand demand, String dmdReasonMasterCode,
			Module module) {
		DemandGenericDao dmdGenricDao = null;
		BigDecimal balance = BigDecimal.ZERO;
		if (demand != null && dmdReasonMasterCode != null && module != null) {
			dmdGenricDao = new DemandGenericHibDao();
			List<EgDemandDetails> dmdDetList = dmdGenricDao.getDmdDetailList(demand, demand
					.getEgInstallmentMaster(), module, dmdGenricDao.getDemandReasonMasterByCode(
					dmdReasonMasterCode, module));
			if (!dmdDetList.isEmpty()) {
				for (EgDemandDetails dmdDet : dmdDetList) {
					if (dmdDet.getAmount() != null) {
						balance = balance.add(dmdDet.getAmount());
					}
					if (dmdDet.getAmtCollected() != null
							&& dmdDet.getAmtCollected().compareTo(BigDecimal.ZERO) > 0) {
						balance = balance.subtract(dmdDet.getAmtCollected());
					}
				}
			}
		}
		return balance;
	}

	public List<EgdmCollectedReceipt> getAllEgdmCollectedReceipts(
			String receiptNo) {
		List<EgdmCollectedReceipt> CollectedReceipts = new ArrayList<EgdmCollectedReceipt>();
		Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(
				EgdmCollectedReceipt.class, "CollReceipt").add(
				Restrictions.eq("CollReceipt.receiptNumber", receiptNo));
		CollectedReceipts = criteria.list();
		return CollectedReceipts;
	}
}
