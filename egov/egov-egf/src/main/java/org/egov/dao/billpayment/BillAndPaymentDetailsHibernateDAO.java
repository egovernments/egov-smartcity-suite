package org.egov.dao.billpayment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.egf.model.BillPayment.BillVoucherInfo;
import org.egov.egf.model.BillPayment.BillInfo;
import org.egov.egf.model.BillPayment.BillPaymentDetails;
import org.egov.egf.model.BillPayment.PaymentStatus;
import org.egov.egf.model.BillPayment.PaymentVoucherInfo;
import org.egov.egf.model.BillPayment.PaymentsInfo;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.utils.FinancialConstants;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BillAndPaymentDetailsHibernateDAO implements BillAndPaymentDetailsDAO {

	private static final Logger LOG = Logger.getLogger(BillAndPaymentDetailsDAO.class);

	@Autowired
	CityService cityService;

	@PersistenceContext
	private EntityManager entityManager;

	public Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	public static final String APPROVED = "APPROVED";
	public static final String CANCELLED = "CANCELLED";
	public static final String IN_PROGRESS = "IN_PROGRESS";

	@Override
	public BillPaymentDetails getBillAndPaymentDetails(String billNo) throws Exception {

		/** setting up return object **/
		BillPaymentDetails bpd = new BillPaymentDetails();
		bpd.setCityCode(ApplicationThreadLocals.getCityCode());
		bpd.setCityname(ApplicationThreadLocals.getCityName());

		String status = "", statusMessage = "";

		StringBuilder queryString = new StringBuilder(
				"select br.billnumber, br.billdate, br.billamount as grossAmount, br.billstatus, brm.narration from eg_billregister br join eg_billregistermis brm on brm.billid = br.id where br.billnumber = :billNo");

		LOG.debug(queryString);

		SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
		query.setParameter("billNo", billNo);
		Object[] row = (Object[]) query.uniqueResult();

		if (row != null && ((String) row[3]).equalsIgnoreCase(FinancialConstants.CONTRACTORBILL_APPROVED_STATUS)) {

			status = APPROVED;
			statusMessage = "The bill is approved";

			String tpBillNo = "";
			Date billDate = (Date) row[1];
			BigDecimal grossAmount = (BigDecimal) row[2];

			StringBuilder queryString1 = new StringBuilder("select sum(bd.creditamount) as netAmount from eg_billregister br join eg_billdetails bd on br.id = bd.billid join chartofaccounts coa on coa.id = bd.glcodeid join eg_appconfig_values cvalues on cvalues.value = cast(coa.purposeid as text) join eg_appconfig config on config.id = cvalues.key_id where br.billnumber = :billNo and config.key_name = :keyName and bd.creditamount > 0");

			LOG.debug(queryString1);
			SQLQuery query1 = getCurrentSession().createSQLQuery(queryString1.toString());
			query1.setParameter("billNo", billNo);
			query1.setParameter("keyName", "worksBillPurposeIds");

			BigDecimal netAmount, deductionAmount;
			netAmount = (BigDecimal) query1.uniqueResult();
			if (netAmount != null) {
				deductionAmount = grossAmount.subtract(netAmount);
			} else {
				return null;
			}

			/** setting up return object **/
			BillInfo bi = new BillInfo();
			bi.setBillDate(billDate);
			bi.setBillNumber(billNo);
			bi.setTpBillNo(tpBillNo);
			bi.setGrossAmount(grossAmount);
			bi.setDeduction(deductionAmount);
			bi.setNetAmount(netAmount);
			bpd.setBillInfo(bi);

			StringBuilder queryString2 = new StringBuilder("select vh.vouchernumber, vh.voucherdate, case vh.status when 5 then 'CREATED' ")
					.append("when 4 then 'CANCELLED' when 0 then 'APPROVED' end as voucherStatus ")
					.append("from voucherheader vh ") 
					.append("join eg_billregistermis egbmis on egbmis.voucherheaderid = vh.id ")
					.append("join eg_billregister egbireg on egbireg.id = egbmis.billid ")
					.append("where egbireg.billnumber=:billNo ");

			LOG.debug(queryString2);
			SQLQuery query2 = getCurrentSession().createSQLQuery(queryString2.toString());
			query2.setParameter("billNo", billNo);
			Object[] row2 = (Object[]) query2.uniqueResult();

			if (row2 != null) {
				LOG.debug(Arrays.toString(row2));
			} else {
				LOG.debug("row2 is null!");
			}

			if (row2 != null
					&& ((String) row2[2]).equalsIgnoreCase(FinancialConstants.CONTRACTORBILL_APPROVED_STATUS)) {

				status = APPROVED;
				statusMessage = "The bill voucher is approved";

				String voucherNumber = (String) row2[0];
				Date voucherDate = (Date) row2[1];
				String voucherStatus = (String) row2[2];

				/** setting up return object **/
				BillVoucherInfo bv = new BillVoucherInfo();
				bv.setBillVoucherDate(voucherDate);
				bv.setBillVoucherNumber(voucherNumber);
				bv.setBillVoucherStatus(voucherStatus);
				bpd.setBillVoucher(bv);

				StringBuilder queryString3 = new StringBuilder("select vh.vouchernumber, vh.voucherdate, case vh.status when 5 then 'CREATED' when 4 then 'CANCELLED' when 0 then 'APPROVED' end as voucherStatus, paymentamount, ih.instrumentnumber chequeNumber, ih.instrumentdate chequeDate, ih.instrumentamount chequeAmount from miscbilldetail mbd join voucherheader vh on vh.id = mbd.payvhid left join paymentheader ph on ph.voucherheaderid = vh.id left join egf_instrumentvoucher iv on iv.voucherheaderid = vh.id left join egf_instrumentheader ih on ih.id = iv.instrumentheaderid left join egf_instrumenttype it on it.id = ih.instrumenttype and it.type = 'cheque' where billnumber=:billNo");

				LOG.debug(queryString3);

				SQLQuery query3 = getCurrentSession().createSQLQuery(queryString3.toString());
				query3.setParameter("billNo", billNo);
				List<Object[]> rows = (List<Object[]>) query3.list();

				PaymentsInfo pi = new PaymentsInfo();
				bpd.setPaymentsInfo(pi);

				BigDecimal paidAmount = BigDecimal.ZERO;
				ArrayList<PaymentVoucherInfo> alPvs = new ArrayList<>();

				for (int i = 0; i < rows.size(); i++) {
					Object[] row3 = rows.get(i);
					String paymentVoucherNumber = (String) row3[0];
					Date paymentVoucherDate = (Date) row3[1];
					String paymentVoucherStatus = (String) row3[2];
					BigDecimal paymentAmount = (BigDecimal) row3[3];
					String chequeNumber = (String) row3[4];
					Date chequeDate = (Date) row3[5];

					paidAmount = paidAmount.add(paymentAmount);

					/** setting up return object **/

					PaymentVoucherInfo pv = new PaymentVoucherInfo();
					pv.setPaymentAmount(paymentAmount);
					pv.setPaymentVoucherDate(paymentVoucherDate);
					pv.setPaymentVoucherNumber(paymentVoucherNumber);
					pv.setPaymentVoucherStatus(paymentVoucherStatus);
					pv.setChequeRefNumber(chequeNumber);
					pv.setChequeDate(chequeDate);
					alPvs.add(pv);
				}

				/** setting up return object **/
				pi.setBillAmountPaid(paidAmount);
				pi.setPendingBillAmount(grossAmount.subtract(paidAmount));
				pi.setPaymentVouchers(alPvs);

			}
			if (row2 != null
					&& ((String) row2[2]).equalsIgnoreCase(FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS)) {
				status = CANCELLED;
				statusMessage = "The bill is cancelled";
			} else {
				status = IN_PROGRESS;
				statusMessage = "In progress";
			}

		} else {
			if (row[3] != null
					&& ((String) row[3]).equalsIgnoreCase(FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS)) {
				status = CANCELLED;
				statusMessage = "The bill is cancelled";
			} else {
				status = IN_PROGRESS;
				statusMessage = "In progress";
			}
		}

		/** setting up return object **/
		PaymentStatus st = new PaymentStatus();
		st.setStatusCode(status);
		st.setStatusMessage(statusMessage);
		bpd.setStatus(st);

		return bpd;
	}

}
