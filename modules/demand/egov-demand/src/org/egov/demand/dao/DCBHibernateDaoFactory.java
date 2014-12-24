/*
 * Created on Oct 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.demand.dao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.DepreciationMaster;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Session;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DCBHibernateDaoFactory extends DCBDaoFactory {

	/**
	 *
	 */
	public DCBHibernateDaoFactory() {
		super();
	}


    protected Session getCurrentSession() {
        // Get a Session and begin a database transaction. If the current
        // thread/EJB already has an open Sessio n and an ongoing Transaction,
        // this is a no-op and only returns a reference to the current Session.
        HibernateUtil.beginTransaction();
        return HibernateUtil.getCurrentSession();
    }


	/* (non-Javadoc)
	 * @see org.egov.infstr.DCB.dao.DCBDaoFactory#getDepreciationMasterDao()
	 */
	public DepreciationMasterDao getDepreciationMasterDao()
	{
		return new DepreciationMasterHibDao(DepreciationMaster.class,getCurrentSession());
	}


	public EgReasonCategoryDao getEgReasonCategoryDao()
	{
		return new EgReasonCategoryHibernateDao(EgReasonCategory.class,getCurrentSession());
	}

	public EgDemandDao getEgDemandDao()
	{
		return new EgDemandHibernateDao(EgDemand.class,getCurrentSession());
	}

	public EgBillDao getEgBillDao()
	{
		return new EgBillHibernateDao(EgBill.class,getCurrentSession());
	}
	
	public EgDemandReasonDao getEgDemandReasonDao()
	{
		return new EgDemandReasonHibDao(EgDemandReason.class,getCurrentSession());
	}
	
	public EgBillReceiptDao getEgBillReceiptDao()
	{
		return new EgBillReceiptHibDao(BillReceipt.class,getCurrentSession());
	}
	
	public EgBillDetailsDao getEgBillDetailsDao()
	{
		return new EgBillDetailsHibDao(EgBillDetails.class,getCurrentSession());
	}
	
	public EgDemandDetailsDao getEgDemandDetailsDao()
	{
		return new EgDemandDetailsHibDao(EgDemandDetails.class,getCurrentSession());
	}
	
	public EgDemandReasonMasterDao getEgDemandReasonMasterDao()
	{
		return new EgDemandReasonMasterHibDao(EgDemandReasonMaster.class,getCurrentSession());
	}
	
	public EgdmCollectedReceiptDao getEgdmCollectedReceiptsDao() {
		return new EgdmCollectedReceiptHibDao(EgdmCollectedReceipt.class, getCurrentSession());
	}
	
}
