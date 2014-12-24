package org.egov.dao.bills;



import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.bills.EgBillregister;
import org.hibernate.Query;
import org.hibernate.Session;

@SuppressWarnings("unchecked")
public class EgBillRegisterHibernateDAO extends GenericHibernateDAO{
	private final Logger LOGGER = Logger.getLogger(getClass());
	private  PersistenceService<EgBillregister, Long> egBillRegisterService;
	private Session session;
	public EgBillRegisterHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
		 egBillRegisterService = new PersistenceService<EgBillregister, Long>();
		 egBillRegisterService.setSessionFactory(new SessionFactory());
		 egBillRegisterService.setType(EgBillregister.class);
	}
	
	
	public List<String> getDistinctEXpType(){
		session = HibernateUtil.getCurrentSession();
      
		List<String> list = (List<String>)session.createQuery("select DISTINCT (expendituretype) from EgBillregister egbills" ).list();
        return list;
       
		
	}

	//shoud get called only for other t Fixed asset
	public String getBillTypeforVoucher(CVoucherHeader voucherHeader) throws ValidationException{
		LOGGER.debug("EgBillRegisterHibernateDAO | getBillTypeforVoucher");
		if(null == voucherHeader){
			 throw new ValidationException(Arrays.asList(new ValidationError("voucher header null","VoucherHeader supplied is null")));
		}
		session = HibernateUtil.getCurrentSession();
		Query qry =  session.createQuery("from  EgBillregister br where br.egBillregistermis.voucherHeader.id=:voucherId");
		qry.setLong("voucherId",voucherHeader.getId());
		EgBillregister billRegister=(EgBillregister)qry.uniqueResult(); 
		return (billRegister==null?null:billRegister.getExpendituretype());
	}
	
	//shoud get called only for Fixed asset
	public String getBillSubTypeforVoucher(CVoucherHeader voucherHeader) throws ValidationException{
		LOGGER.debug("EgBillRegisterHibernateDAO | getBillTypeforVoucher");
		if(null == voucherHeader){
			 throw new ValidationException(Arrays.asList(new ValidationError("voucher header null","VoucherHeader supplied is null")));
		}
		session = HibernateUtil.getCurrentSession();
		Query qry =  session.createQuery("from  EgBillregister br where br.egBillregistermis.voucherHeader.id=:voucherId");
		qry.setLong("voucherId",voucherHeader.getId());
		EgBillregister billRegister=(EgBillregister)qry.uniqueResult(); 
		return (billRegister==null?"General":billRegister.getEgBillregistermis().getEgBillSubType()==null?billRegister.getExpendituretype():billRegister.getEgBillregistermis().getEgBillSubType().getName());
	}
}
