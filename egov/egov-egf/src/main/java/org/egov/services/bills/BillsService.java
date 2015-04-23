package org.egov.services.bills;

import java.util.List;

import org.egov.commons.CVoucherHeader;
import org.egov.dao.bills.BillsDaoFactory;
import org.egov.dao.bills.EgBillRegisterHibernateDAO;
import org.egov.model.bills.EgBillregister;

public class BillsService   {
	public EgBillregister createBillRegister(EgBillregister billregister)
    {
    	EgBillRegisterHibernateDAO obj = BillsDaoFactory.getDAOFactory().getEgBillRegisterHibernateDAO();
    	return (EgBillregister)obj.create( billregister );
    }
	public EgBillregister updateBillRegister(EgBillregister billregister)
    {
    	EgBillRegisterHibernateDAO obj = BillsDaoFactory.getDAOFactory().getEgBillRegisterHibernateDAO();
    	return (EgBillregister)obj.update( billregister );
    }
	public EgBillregister getBillRegisterById(Integer billid)
    {
    	EgBillRegisterHibernateDAO obj = BillsDaoFactory.getDAOFactory().getEgBillRegisterHibernateDAO();
    	return (EgBillregister)obj.findById(new Long(billid), false);
    }
	
	public List<String> getDistExpType()
    {
    	EgBillRegisterHibernateDAO obj = BillsDaoFactory.getDAOFactory().getEgBillRegisterHibernateDAO();
    	return obj.getDistinctEXpType();
    }
	public String getBillTypeforVoucher(CVoucherHeader voucherHeader)
    {
    	EgBillRegisterHibernateDAO obj = BillsDaoFactory.getDAOFactory().getEgBillRegisterHibernateDAO();
    	return obj.getBillTypeforVoucher(voucherHeader);
    }
	public String getBillSubTypeforVoucher(CVoucherHeader voucherHeader){
    	EgBillRegisterHibernateDAO obj = BillsDaoFactory.getDAOFactory().getEgBillRegisterHibernateDAO();
    	return obj.getBillSubTypeforVoucher(voucherHeader);
    }
}
