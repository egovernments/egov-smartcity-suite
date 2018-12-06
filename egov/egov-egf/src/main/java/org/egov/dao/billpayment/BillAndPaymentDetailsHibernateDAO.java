package org.egov.dao.billpayment;

import org.egov.egf.model.BillPaymentDetails;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BillAndPaymentDetailsHibernateDAO implements BillAndPaymentDetailsDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public List<BillPaymentDetails> getBillAndPaymentDetails(String billNo) {
        StringBuilder queryString = new StringBuilder("SELECT DISTINCT mbd.billVoucherHeader.voucherNumber AS billVoucherNo,")
                .append(" mbd.payVoucherHeader.voucherNumber AS paymentVoucherNo, mbd.paidamount AS paymentAmount,")
                .append(" mbd.payVoucherHeader.voucherDate AS voucherDate, iv.instrumentHeaderId.instrumentNumber AS chequRefNo")
                .append(" FROM Miscbilldetail AS mbd, InstrumentVoucher AS iv,EgBillregister AS egbr, InstrumentHeader AS instrumentHeader")
                .append(" WHERE mbd.payVoucherHeader.id = iv.voucherHeaderId.id and iv.instrumentHeaderId.id = instrumentHeader.id and ")
                .append(" egbr.billnumber = mbd.billnumber and ")
                .append(" mbd.billnumber = :billNo AND egbr.status.code = :billStatus");
        Query query = getCurrentSession().createQuery(queryString.toString())
                .setParameter("billNo", billNo, StringType.INSTANCE)
                .setParameter("billStatus", "Approved", StringType.INSTANCE);
        return query.setResultTransformer(Transformers.aliasToBean(BillPaymentDetails.class)).list();
    }

}
