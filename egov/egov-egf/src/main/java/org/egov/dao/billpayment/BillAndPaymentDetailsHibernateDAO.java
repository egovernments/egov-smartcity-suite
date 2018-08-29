package org.egov.dao.billpayment;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.model.BillPaymentDetails;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

@Repository
public class BillAndPaymentDetailsHibernateDAO implements BillAndPaymentDetailsDAO{

	@PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
	
   @Override
	public List<BillPaymentDetails> getBillAndPaymentDetails(String billNo) {
		StringBuilder queryString = new StringBuilder("SELECT DISTINCT mbd.billVoucherHeader.voucherNumber AS billVoucherNo,"
    			+ " mbd.payVoucherHeader.voucherNumber AS paymentVoucherNo,mbd.paidamount AS paymentAmount,"
    			+ " mbd.payVoucherHeader.voucherDate AS voucherDate,iv.instrumentHeaderId.instrumentNumber AS chequRefNo"
    			+ " FROM Miscbilldetail AS mbd,InstrumentVoucher AS iv,EgBillregister AS egbr,InstrumentHeader AS instrumentHeader"
    			+ " WHERE mbd.payVoucherHeader.id= iv.voucherHeaderId.id and iv.instrumentHeaderId.id=instrumentHeader.id and "
    			+ " egbr.billnumber=mbd.billnumber and "
    			+ " mbd.billnumber =:billNo AND egbr.status.code =:billStatus");
    		Query query = getCurrentSession().createQuery(queryString.toString());  
    		query.setParameter("billNo", billNo);
    		query.setParameter("billStatus", "Approved");
    		return query.setResultTransformer(Transformers.aliasToBean(BillPaymentDetails.class)).list();
	}

}
