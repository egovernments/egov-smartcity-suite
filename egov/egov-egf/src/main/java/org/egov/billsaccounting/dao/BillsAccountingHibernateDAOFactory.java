package org.egov.billsaccounting.dao;

import org.egov.billsaccounting.model.Contractorbilldetail;
import org.egov.billsaccounting.model.EgwWorksDeductions;
import org.egov.billsaccounting.model.EgwWorksMis;
import org.egov.billsaccounting.model.OtherBillDetail;
import org.egov.billsaccounting.model.Salarybilldetail;
import org.egov.billsaccounting.model.Supplierbilldetail;
import org.egov.billsaccounting.model.Worksdetail;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Session;

public class BillsAccountingHibernateDAOFactory extends BillsAccountingDAOFactory {

	protected Session getCurrentSession()
    {
		// returns a reference to the current Session.	        
		return HibernateUtil.getCurrentSession();
    }
	public  WorksDetailHibernateDAO getWorksDetailDAO()
	{
		return new WorksDetailHibernateDAO(Worksdetail.class,getCurrentSession());
	}
	public 	SupplierBillHibernateDAO getSupplierBillDAO()
	{	
		return new SupplierBillHibernateDAO(Supplierbilldetail.class,getCurrentSession());
	}
	public 	ContractorBillHibernateDAO getContractorBillDAO()
	{
		return new ContractorBillHibernateDAO(Contractorbilldetail.class,getCurrentSession());
	}
	public EgwWorksMisHibernateDAO getEgwWorksMisDAO()
	{
		return new EgwWorksMisHibernateDAO(EgwWorksMis.class,getCurrentSession());
	}
	public EgwWorksDeductionsHibernateDAO getEgwWorksDeductionsDAO()
	{
		return new EgwWorksDeductionsHibernateDAO(EgwWorksDeductions.class,getCurrentSession());
	}
	

	public SalarybilldetailHibernateDAO getSalarybilldetailDAO()
	{
		return new SalarybilldetailHibernateDAO(Salarybilldetail.class,getCurrentSession());
	}
	
}

 