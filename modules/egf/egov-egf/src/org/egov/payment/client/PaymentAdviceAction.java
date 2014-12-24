/*
 * PaymentAdviceAction.java Created on Mar 20, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payment.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Chequedetail;
import org.egov.commons.service.CommonsService;
import org.egov.deduction.dao.DeductionDAOFactory;
import org.egov.deduction.dao.GeneralledgerdetailHibernateDAO;
import org.egov.deduction.model.Generalledgerdetail;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payment.model.Subledgerpaymentheader;
import org.egov.services.payment.PaymentService;
import org.egov.utils.Constants;
import org.egov.utils.GetEgfManagers;
import org.egov.utils.PDFGenerator;
import org.hibernate.jdbc.ReturningWork;

import com.exilant.eGov.src.common.EGovernCommon;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish P
 * @version 1.00
 */

public class PaymentAdviceAction extends DispatchAction {
	private static final String MODE = "mode";
	private final static String alert = "alertMessage";
	private static final String HEAVY_LOAD = "Heavy Load";
	private static final String ERROR="error";
	private PaymentService paymentService;
	public final static Logger LOGGER = Logger
			.getLogger(PaymentAdviceAction.class);
	
	// ERP 2.0 - commented to resolve compilation problems - will be fixed later
	/*CbillManager cbm = null;*/
	CommonsService commonsService = null;
	EGovernCommon egc = new EGovernCommon();
	NumberFormat nf = new DecimalFormat("##############.00");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	
	public ActionForward cancelPaymentVouchers(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest req,
			final HttpServletResponse res) throws IOException, ServletException {
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<ActionForward>() {

			@Override
			public ActionForward execute(Connection con) throws SQLException {
				

				String target = "";
				String alertMessage = null;
				try {
					PaymentAdviceAction.LOGGER.debug("inside cancelPaymentVoucher ");
					final PaymentAdviceForm paf = (PaymentAdviceForm) form;
					
					final int userId = ((Integer) req.getSession().getAttribute(
							"com.egov.user.LoginUserId")).intValue();
					if(paf.getIsCancel() != null && paf.getIsCancel().length > 0) {
						for (int i = 0; i < paf.getIsCancel().length; i++) {
							if(paf.getIsCancel()[i].equalsIgnoreCase("yes")) {
								egc.cancelVouchers(paf.getPymntVhId()[i], con);
								//egc.cancelDetails(paf.getPymntVhId()[i], userId, con);
							}
						}
					}
					alertMessage = "Transaction executed successfully for Cancel Voucher";
					target = "search";
				} catch (final Exception ex) {
					target = "error";
					PaymentAdviceAction.LOGGER.error("Exception Encountered!!!"
							+ ex.getMessage());
					HibernateUtil.rollbackTransaction();
				}
				req.setAttribute(PaymentAdviceAction.alert, alertMessage);
				return mapping.findForward(target);
			
				
			}
		});
		
	}
	
	// ERP 2.0 - commented to resolve compilation problems - will be fixed later
	
	/*public ActionForward generatePaymentAdvice(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest req,
			final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		
		try {
			final String paymentType = req.getParameter("paymentType");
			final PaymentAdviceForm paf = (PaymentAdviceForm) form;
			PaymentAdviceAction.LOGGER.debug("inside generatePaymentAdvice ");
			cbm = GetEgfManagers.getCbillManager();
			final GeneralledgerdetailHibernateDAO glDtlDAO = DeductionDAOFactory
					.getDAOFactory().getGeneralledgerdetailDAO();
			final RemitRecoveryDelegate rrd = new RemitRecoveryDelegate();
			PaymentAdviceAction.LOGGER.debug("Payment vh Id::::"
					+ req.getParameter("pymntVhId"));
			final String pymntVhId = req.getParameter("pymntVhId");
			String[] chequeNo = null;
			String[] chequeDate = null;
			String[] bankAccountNo = null;
			ArrayList glDtlList = null;
			CVoucherHeader jvVoucherheader = null;
			final CVoucherHeader pymntVoucherheader = commonsService
					.findVoucherHeaderById(Long.parseLong(pymntVhId));
			final ArrayList cBillList = (ArrayList) cbm
					.getOtherBillDetailByPymntVoucherheader(pymntVoucherheader);
			if(pymntVoucherheader.getRefcgNo() == null) 
			{
				glDtlList = (ArrayList) glDtlDAO.getGeneralledgerdetailByVhId(Integer.parseInt(pymntVhId));
			} 
			else 
			{
				jvVoucherheader = commonsService.getVoucherHeadersByCGN(pymntVoucherheader.getRefcgNo());
				glDtlList = (ArrayList) glDtlDAO.getGeneralledgerdetailByVhId(Integer.parseInt(jvVoucherheader.getId().toString()));
			}
			
			final ArrayList chequeDtlList = (ArrayList) commonsService
					.getChequedetailByVoucherheader(pymntVoucherheader);
			int i = 0;
			if(!chequeDtlList.isEmpty() && chequeDtlList.size()!=0) {
				chequeNo = new String[chequeDtlList.size()];
				chequeDate = new String[chequeDtlList.size()];
				bankAccountNo = new String[chequeDtlList.size()];
				for (final Iterator it = chequeDtlList.iterator(); it.hasNext();) {
					final Chequedetail cd = (Chequedetail) it.next();
					chequeNo[i] = cd.getChequenumber();
					bankAccountNo[i] = cd.getBankaccount().getAccountnumber();
					chequeDate[i] = sdf.format(cd.getChequedate());
					i++;
				}
			}
			
			PaymentAdviceAction.LOGGER.debug("glDtlList-->" + glDtlList);
			int size = 0;
			if(!cBillList.isEmpty() && cBillList.size()!=0) {
				size = size + cBillList.size();
			}
			if(!glDtlList.isEmpty() && glDtlList.size()!=0) {
				size = size + glDtlList.size();
			}
			
			String[] chqNo = null;
			String[] chqDate = null;
			String[] partyName = null;
			String[] partyAddress = null;
			String[] accountNo = null;
			String[] amount = null;
			String[] bankAccountType = null;
			String[] ifscCode = null;
			String[] code = null;
			String[] bankName = null;
			int count = 0;
			chqNo = new String[size];
			chqDate = new String[size];
			partyName = new String[size];
			partyAddress = new String[size];
			accountNo = new String[size];
			amount = new String[size];
			bankAccountType = new String[size];
			ifscCode = new String[size];
			code = new String[size];
			bankName = new String[size];
			
			if(!cBillList.isEmpty() && cBillList.size()!=0) {
				for (final Iterator it = cBillList.iterator(); it.hasNext();) {
					final OtherBillDetail obd = (OtherBillDetail) it.next();
					chqNo[count] = chequeNo[0];
					chqDate[count] = chequeDate[0];
					String paidTo = "";
					if(obd.getEgBillregister().getEgBillregistermis() != null
							&& obd.getEgBillregister().getEgBillregistermis()
									.getPayto() != null) {
						paidTo = obd.getEgBillregister().getEgBillregistermis()
								.getPayto() == null ? "" : obd
								.getEgBillregister().getEgBillregistermis()
								.getPayto();
					}
					partyName[count] = paidTo;
					accountNo[count] = "";
					final String dedAmt = commonsService
							.getCBillDeductionAmtByVhId(obd
									.getVoucherheaderByVoucherheaderid()
									.getId());
					final BigDecimal netAmt = obd.getEgBillregister()
							.getPassedamount().subtract(new BigDecimal(dedAmt));
					amount[count] = nf.format(netAmt);
					bankAccountType[count] = "";
					ifscCode[count] = "";
					code[count] = "";
					bankName[count] = "";
					partyAddress[count] = "";
					count++;
				}
			}
			if(!glDtlList.isEmpty() && glDtlList.size()!=0) {
				PaymentAdviceAction.LOGGER.debug("glDtlList.size()-->"
						+ glDtlList.size());
				for (final Iterator it = glDtlList.iterator(); it.hasNext();) {
					final Generalledgerdetail gld = (Generalledgerdetail) it
							.next();
					chqNo[count] = chequeNo[0];
					chqDate[count] = chequeDate[0];
					final String partyDetail = rrd.getPartyDetails(gld
							.getDetailkeyid().toString(), gld
							.getAccountdetailtype().getTablename());
					if(partyDetail == null) 
					{
						partyName[count] = "";
						accountNo[count] = "";
						bankAccountType[count] = "";
						ifscCode[count] = "";
						code[count] = "";
						bankName[count] = "";
						partyAddress[count] = "";
					} else {
						final String[] partyDetails = partyDetail.split(" ~ ");
						partyName[count] = partyDetails[1];
						accountNo[count] = partyDetails[10];
						bankAccountType[count] = partyDetails[5];
						ifscCode[count] = partyDetails[6];
						code[count] = partyDetails[7];
						bankName[count] = partyDetails[8];
						partyAddress[count] = partyDetails[3] + " "+ partyDetails[9];
					}
					amount[count] = nf.format(gld.getAmount());
					count++;
				}
			}
			
			if("RTGS Payment".equals(paymentType)) {
				
				BankAdviceForm baf;
				final ArrayList al = new ArrayList();
				for (int k = 0; k < amount.length; k++) {
					baf = new BankAdviceForm();
					baf.setContractorCode(code[k]);
					baf.setContractorName(partyName[k] + " " + partyAddress[k]);
					baf.setAccountType(bankAccountType[k]);
					baf.setBankName(bankName[k]);
					baf.setBankAccountNo(accountNo[k]);
					baf.setIfscCode(ifscCode[k]);
					baf.setNetAmount(new BigDecimal(amount[k]));
					al.add(baf);
				}
				
				if(al.size()!=0) {
					final HashMap paramMap = new HashMap();
					paramMap.put("chequeNo", chqNo[0]);
					paramMap.put("chequeDate", chqDate[0]);
					paramMap.put("pymtVhNo", pymntVoucherheader
							.getVoucherNumber());
					paramMap.put("inFavourOf", EGovConfig.getProperty(
							"egf_config.xml", "infavourof", "",
							"BankAdviceReport"));
					paramMap.put("pymtAcNo", bankAccountNo[0]);
					paramMap.put("jasperpath", "org/egov/payment/client/");
					req.setAttribute("filename", "/bankAdviceReport.pdf");
					new PDFGenerator().generateReport(req, res,
							"bankAdviceReport", al, paramMap);
				}
				target = "reports";
			} else {
				paf.setChqNo(chqNo);
				paf.setChqDate(chqDate);
				paf.setPartyName(partyName);
				paf.setAccountNo(accountNo);
				paf.setAmount(amount);
				target = "paymentAdvice";
			}
		} catch (final Exception ex) {
			target = "error";
			PaymentAdviceAction.LOGGER.error("Exception Encountered!!!"
					+ ex.getMessage());
			HibernateUtil.rollbackTransaction();
		}
		return mapping.findForward(target);
	}
	*/
	private java.util.Date getDate(final String dateString) {
		java.util.Date d = new java.util.Date();
	
			try {
				d = sdf.parse(dateString);
			} catch (ParseException e) {
				LOGGER.error("Exception while Parsing the Date...");
				throw new EGOVRuntimeException("Unable to Parse the Date ...");
			}
		return d;
	}
	
	// ERP 2.0 - commented to resolve compilation problems - will be fixed later
	
	/*public ActionForward searchPaymentVouchers(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest req,
			final HttpServletResponse res) throws IOException, ServletException {
		String target = "";
		ArrayList<Chequedetail> chqList = new ArrayList();
		try {
			PaymentAdviceAction.LOGGER.debug("inside searchPaymentVouchers ");
			final PaymentAdviceForm paf = (PaymentAdviceForm) form;
			cbm = GetEgfManagers.getCbillManager();
			final String pymntVhNo = paf.getPymntVhNo() == null || paf.getPymntVhNo().equals("") ? null:  paf.getPymntVhNo();
			final String chqNo = paf.getChequeNo() == null || paf.getChequeNo().equals("") ? null : paf.getChequeNo();
			final String dept = paf.getDepartmentId() == null || paf.getDepartmentId().equals("0") ? null : paf.getDepartmentId();
			final String functionary = paf.getFunctionaryId() == null || paf.getFunctionaryId().equals("0") ? null :  paf.getFunctionaryId();
			final Date pymntVhDateFrom = paf.getPymntVhDateFrom() == null || paf.getPymntVhDateFrom().equals("") ? null : getDate(paf.getPymntVhDateFrom());
			final Date pymntVhDateTo = paf.getPymntVhDateTo() == null || paf.getPymntVhDateTo().equals("") ? null : getDate(paf.getPymntVhDateTo());
			
			
			final String pType = paf.getPymntType();
			PaymentAdviceAction.LOGGER.debug("pymntVhNo-->" + pymntVhNo);
			PaymentAdviceAction.LOGGER
					.debug("pymntVhDate-->" + pymntVhDateFrom);
			PaymentAdviceAction.LOGGER.debug("pymntVhDate-->" + pymntVhDateTo);
			PaymentAdviceAction.LOGGER.debug("chqNo-->" + chqNo);
			PaymentAdviceAction.LOGGER.debug("pymntVhDate-->" + dept);
			PaymentAdviceAction.LOGGER.debug("chqNo-->" + functionary);
			ArrayList chqList1 = null;
			if(req.getParameter(MODE).equalsIgnoreCase("view")) {
				chqList1 = (ArrayList) commonsService.getChequedetailFilterBy(
						pymntVhNo, pymntVhDateFrom, pymntVhDateTo, chqNo,
						"view", pType, dept, functionary);
			} else if(req.getParameter(MODE).equalsIgnoreCase("reverse")) {
				chqList1 = (ArrayList) commonsService.getChequedetailFilterBy(
						pymntVhNo, pymntVhDateFrom, pymntVhDateTo, chqNo,
						"reverse", pType, dept, functionary);
			} else {
				chqList1 = (ArrayList) commonsService.getChequedetailFilterBy(
						pymntVhNo, pymntVhDateFrom, pymntVhDateTo, chqNo, "",
						pType, dept, functionary);
			}
			
			boolean addToList = false;
			getAll: if(!chqList1.isEmpty() && chqList1.size()!=0) {
				PaymentAdviceAction.LOGGER
						.info("From Advice" + chqList1.size());
				
				for (final Iterator it = chqList1.iterator(); it.hasNext();) {
					addToList = false;
					final Chequedetail cd = (Chequedetail) it.next();
					final ArrayList otherBillList = (ArrayList) cbm.getOtherBillDetailByPymntVoucherheader(cd.getVoucherheader());
					final ArrayList subLedPymntHeaderList = (ArrayList) paymentService.getSubledgerpaymentheaderByVoucherHeader(cd.getVoucherheader());
					if("Bill Payment".equalsIgnoreCase(pType)) 
					{
						if(!subLedPymntHeaderList.isEmpty()	&& subLedPymntHeaderList.size()!=0) {
							for (int k = 0; k < subLedPymntHeaderList.size(); k++) {
								final Subledgerpaymentheader subLedgerPaymentheader = (Subledgerpaymentheader) subLedPymntHeaderList
										.get(k);
								if(!subLedgerPaymentheader.getTypeofpayment()
										.equals("RTGS/NEFT")) {
									addToList = true;
								}
							}
							if(addToList) {
								chqList.add(cd);
								if(chqList.size() >= 1001) {
									req.setAttribute(PaymentAdviceAction.alert,
											PaymentAdviceAction.HEAVY_LOAD);
									chqList = new ArrayList();
									break getAll;
								}
							}
						}
						if(!otherBillList.isEmpty() && otherBillList.size()!=0 && !addToList) {
							chqList.add(cd);
							if(chqList.size() >= 1001) {
								req.setAttribute(PaymentAdviceAction.alert,
										PaymentAdviceAction.HEAVY_LOAD);
								chqList = new ArrayList();
								break getAll;
							}
						}
					} 
					else if("RTGS Payment".equalsIgnoreCase(pType)) 
					{
						if(!subLedPymntHeaderList.isEmpty()) {
							for (int k = 0; k < subLedPymntHeaderList.size(); k++) {
								final Subledgerpaymentheader subLedgerPaymentheader = (Subledgerpaymentheader) subLedPymntHeaderList
										.get(k);
								if(subLedgerPaymentheader.getTypeofpayment()
										.equals("RTGS/NEFT")) {
									addToList = true;
								}
							}
						}
						if(addToList) {
							chqList.add(cd);
							if(chqList.size() >= 1001) {
								req.setAttribute(PaymentAdviceAction.alert,
										PaymentAdviceAction.HEAVY_LOAD);
								chqList = new ArrayList();
								break getAll;
							}
						}
					} else {
						chqList.add(cd);
						if(chqList.size() >= 1001) {
							req.setAttribute(PaymentAdviceAction.alert,
									PaymentAdviceAction.HEAVY_LOAD);
							chqList = new ArrayList();
							break getAll;
						}
					}
				} // for loop
			}
			req.getSession().setAttribute("chqList", chqList);
			req.setAttribute(MODE, req.getParameter(MODE));
			
			target = "search";
			PaymentAdviceAction.LOGGER
					.info("From Advice Just Returning response");
		} catch (final Exception e) {
			target=ERROR;
			throw new EGOVRuntimeException(e.getMessage(),e);
		} 
		return mapping.findForward(target);
	}*/

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
}
