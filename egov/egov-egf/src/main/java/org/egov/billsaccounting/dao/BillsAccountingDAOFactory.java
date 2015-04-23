
package org.egov.billsaccounting.dao;


public abstract class BillsAccountingDAOFactory {
	//private static final BillsAccountingDAOFactory EJB3_PERSISTENCE = null;
	private static final BillsAccountingDAOFactory HIBERNATE = new BillsAccountingHibernateDAOFactory();

	public static BillsAccountingDAOFactory getDAOFactory()
	{
		return HIBERNATE;
	}
	public abstract WorksDetailHibernateDAO getWorksDetailDAO();
    public abstract SupplierBillHibernateDAO getSupplierBillDAO();
    public abstract ContractorBillHibernateDAO getContractorBillDAO();
    public abstract EgwWorksMisHibernateDAO getEgwWorksMisDAO();
    public abstract EgwWorksDeductionsHibernateDAO getEgwWorksDeductionsDAO();
    public abstract SalarybilldetailHibernateDAO getSalarybilldetailDAO();
 
    }