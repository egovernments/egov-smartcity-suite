/*
 * DishonoredChequeAction.java Created on May 17, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.recoveries.Recovery;
import org.egov.services.instrument.InstrumentService;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.jdbc.ReturningWork;

import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.BankBranch;
import com.exilant.eGov.src.domain.BankEntries;
import com.exilant.eGov.src.transactions.CommonMethodsI;
import com.exilant.eGov.src.transactions.CommonMethodsImpl;
import com.exilant.eGov.src.transactions.InsufficientAmountException;

public class DishonoredChequeAction extends DispatchAction {
	private static final String PAYMENT = "Payment";
	private static final String RECEIPT = "Receipt";
	private static final String JOURNAL_VOUCHER = "Journal Voucher";
	private static final String ERROR = "error";
	private static final String SUCCESS = "success";
	private InstrumentService instrumentService;
	protected GenericHibernateDaoFactory genericDao;
	public PersistenceService<InstrumentHeader, Long> instrumentHeaderService ;
	PersistenceService persistenceService;
	private static  final Logger LOGGER = Logger.getLogger(DishonoredChequeAction.class);
	String target = "";
	String reversalVhIdValue="";
	String bankChargesVhIdValue="";
	String txnDateChq;
	SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	Date dt;
	EGovernCommon cm = new EGovernCommon();
	String alertMessage=null;
	public ActionForward toLoad(final ActionMapping mapping,final ActionForm form,final HttpServletRequest req,final HttpServletResponse res)throws IOException,ServletException{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<ActionForward>() {

			@Override
			public ActionForward execute(Connection connection) throws SQLException {

				try{
					LOGGER.info(">>> inside toLoad");
					String todayDt=cm.getCurrentDate(connection);
					LOGGER.info(">>> inside toLoad ********** getCurrentDate-->TODAY DATE IS"+todayDt);
					req.getSession().setAttribute("todayDate", todayDt);

					BankBranch bb=new BankBranch();
					//use persistence service
					Map hm=bb.getBankBranch(connection);
					req.getSession().setAttribute("bankBranchList", hm);
					LOGGER.info(">>> before ending DishonoredChequeAction");
					target = SUCCESS;
				   }catch(Exception ex){
				   target = ERROR;
				   LOGGER.error("Exception Encountered!!!"+ex.getMessage());
				}
				return mapping.findForward(target);
			
			}
		});
		
	}
	/*
			  get list of accountnumber for given bankbranch
	*/
	public ActionForward getAccountNumbers(final ActionMapping mapping,final ActionForm form,final HttpServletRequest req,final HttpServletResponse res)
		throws IOException,ServletException
		{
			return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<ActionForward>() {

				@Override
				public ActionForward execute(Connection conn)
						throws SQLException {

					try{
							LOGGER.info(">>> inside getAccountNumbers");
							DishonoredChequeForm dishonCheqForm=(DishonoredChequeForm)form;
							BankBranch bb=new BankBranch();
							LOGGER.info("bank id  "+dishonCheqForm.getBankId());
							HashMap hm=bb.getAccNumber(((String)dishonCheqForm.getBankId()),conn);
							req.getSession().setAttribute("accNumberList2", hm);
							LOGGER.debug(">>> before ending DishonoredChequeAction");
							target = SUCCESS;
					}catch(Exception ex){
					   target = ERROR;
					   LOGGER.error("Exception Encountered!!!"+ex.getMessage());
					   HibernateUtil.rollbackTransaction();
			   		}
					return mapping.findForward(target);
			
				}
			});
		
		}

/*
	get list of cheques which have not been reconciled
*/

	public ActionForward getDetails(final ActionMapping mapping,final ActionForm form,final HttpServletRequest req,final HttpServletResponse res)
	throws IOException,ServletException{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<ActionForward>() {

			@Override
			public ActionForward execute(Connection conn) throws SQLException {
				

				try{
						LOGGER.info(">>> inside getDetails------>SEARCH QUERY");
						DishonoredChequeForm dishonCheqForm=(DishonoredChequeForm)form;
						LOGGER.info("bankacc id (accId)-->from drop down list "+dishonCheqForm.getAccId());
						LOGGER.info("CHEQUE NO IS  "+dishonCheqForm.getChequeNo());

						BankEntries be=new BankEntries();
						String chqFromDate ="";
						String chqToDate ="";

						dt=new Date();
						if(!((String)dishonCheqForm.getBankFromDate()).equalsIgnoreCase("")){
							dt = sdf.parse((String)dishonCheqForm.getBankFromDate());
							chqFromDate = formatter.format(getNextDate(dt, -1));
						}
						chqToDate = formatter.format(getNextDate(dt, 1));
						Long bankId = Long.valueOf(0);
						if(dishonCheqForm.getBankId()!=null)
							bankId = Long.valueOf(dishonCheqForm.getBankId().toString().split("-")[0]);
						List al=be.getChequeDetails(Long.parseLong(dishonCheqForm.getAccId().toString()) ,
								bankId,(String)dishonCheqForm.getChequeNo(),chqFromDate,chqToDate,conn);
						getreversalGlCodes(al,dishonCheqForm);
						if(al!=null && al.size()>1){
							List chequeDetails = new ArrayList();
							BrsEntries brs = (BrsEntries) al.get(0);
							brs.setVoucherNumber("MULTIPLE");
							chequeDetails.add(brs);
							al = chequeDetails;
						}
						req.setAttribute("dishonCheqDetails", al);
						target = SUCCESS;
				}catch(Exception ex){
				   target = ERROR;
				   LOGGER.error("Exception Encountered!!!"+ex.getMessage());
		   		}
				return mapping.findForward(target);
			
			}
		});
		
	}

	/**
	 * Populates all the glcodes for which reversal gl entries  have to be made
	 * fetches all glcodes with creditamount > 0 for receipt and
	 * fetches all glcodes with debitamount > 0 for all others(payment,contra)
	 */
	@SuppressWarnings("unchecked")
	private void getreversalGlCodes(List al, DishonoredChequeForm dishonCheqForm) {
		if(al==null || al.size()==0)
			return;
		String voucherNumber = getVoucherNumbers(al);
		List<Object[]> glCodes = new ArrayList<Object[]>();
		List<Object[]> glCodescredit = new ArrayList<Object[]>();
		List<Object[]> glCodesdebit = new ArrayList<Object[]>();
		List<Object[]> slDetailsCredit = new ArrayList<Object[]>();
		List<Object[]> slDetailsDebit = new ArrayList<Object[]>();
		StringBuffer reversalGlCodes = new StringBuffer();
		if(RECEIPT.equalsIgnoreCase(((BrsEntries)al.get(0)).getVoucherType()) || JOURNAL_VOUCHER.equalsIgnoreCase(((BrsEntries)al.get(0)).getVoucherType()))
		{
			glCodescredit = persistenceService.findAllBy("select gl.glcode,gl.glcodeId.name, sum(gl.creditAmount),sum(gl.debitAmount) from CGeneralLedger gl where gl.voucherHeaderId in("+voucherNumber+") and gl.debitAmount<>0 and gl.creditAmount=0 and gl.glcode not in (select glcode from CChartOfAccounts where purposeId in (select id from AccountCodePurpose where name='Cheque In Hand')) group by gl.glcode,gl.glcodeId.name order by gl.glcode"); 
			glCodes = persistenceService.findAllBy("select gl.glcode,gl.glcodeId.name,sum(gl.creditAmount),sum(gl.debitAmount) from CGeneralLedger gl where gl.voucherHeaderId in("+voucherNumber+") and gl.creditAmount<>0 and gl.debitAmount=0 group by gl.glcode,gl.glcodeId.name order by gl.glcode");
			glCodes.addAll(glCodescredit);
		}	
		else
			glCodes = persistenceService.findAllBy("select distinct gl.glcode,gl.glcodeId.name, sum(gl.creditAmount) ,sum(gl.debitAmount) from CGeneralLedger gl where gl.voucherHeaderId in("+voucherNumber+") and gl.creditAmount=0 and gl.debitAmount<>0 group by gl.glcode,gl.glcodeId.name order by gl.glcode");
		String glCode = "";
		for (Object[] generalLedger : glCodes) {
			glCode = glCode.concat(generalLedger[0].toString()).concat("~").concat(generalLedger[1].toString()).concat("~").concat(generalLedger[2].toString()).concat("~").concat(generalLedger[3].toString()).concat(",");
			reversalGlCodes = reversalGlCodes .append(generalLedger[0].toString()).append(',');
		}
		String reversalGlCodesStr=reversalGlCodes.substring(0, reversalGlCodes.length()-1);
		 StringBuffer slCodes = new StringBuffer();
		dishonCheqForm.setGlcodeChList(glCode);
		slDetailsCredit = persistenceService.findAllBy("select distinct gl.glcode, gd.detailTypeId, gd.detailKeyId,SUM(gd.amount)" +
				" from CGeneralLedger gl, CGeneralLedgerDetail gd where gl.voucherHeaderId in("+voucherNumber+")" +
						" and gl.id = gd.generalLedgerId and gl.debitAmount >0 and gl.glcode in ("+reversalGlCodesStr+") group by gl.glcode, gd.detailTypeId, gd.detailKeyId");
		for (Object[] objects : slDetailsCredit) {
			if(slCodes.length()>0){
				slCodes.append("~").append(objects[0]).append("-").append(objects[1]).append("-").append(objects[2]).append("-").append("credit")
				.append("-").append(objects[3]);
			}else{
				slCodes.append(objects[0]).append("-").append(objects[1]).append("-").append(objects[2]).append("-").append("credit")
				.append("-").append(objects[3]);
			
			}
			
		}
		
		slDetailsDebit = persistenceService.findAllBy("select distinct gl.glcode, gd.detailTypeId, gd.detailKeyId,SUM(gd.amount)" +
				" from CGeneralLedger gl, CGeneralLedgerDetail gd where gl.voucherHeaderId in("+voucherNumber+")" +
						" and gl.id = gd.generalLedgerId and gl.creditAmount >0 and gl.glcode in ("+reversalGlCodesStr+") group by gl.glcode, gd.detailTypeId, gd.detailKeyId");
		for (Object[] objects : slDetailsDebit) {
			if(slCodes.length()>0){
				slCodes.append("~").append(objects[0]).append("-").append(objects[1]).append("-").append(objects[2]).append("-").append("debit")
				.append("-").append(objects[3]);
			}else{
				slCodes.append(objects[0]).append("-").append(objects[1]).append("-").append(objects[2]).append("-").append("debit")
				.append("-").append(objects[3]);
			
			}
			
		}
		
		dishonCheqForm.setSubledgerDetails(slCodes.toString());
	}
	private String getVoucherNumbers(List al) {
		String voucherNumber = "-1";
		for (Object object : al) {
			voucherNumber = voucherNumber.concat(",").concat(((BrsEntries)object).getVoucherHeaderId());
		}
		return voucherNumber;
	}
	/*
		pass voucher for bankentry
	*/
	public ActionForward saveDetails(final ActionMapping mapping,final ActionForm form,final HttpServletRequest req,
			final HttpServletResponse res)throws IOException,ServletException{
		
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<ActionForward>() {

			@Override
			public ActionForward execute(Connection conn) throws SQLException {
			

				int fundId=0;
				int fundSourceId=0;
				int fieldId=0;
				String departmentId=null;
				String appConfigKey = "GJV_FOR_RCPT_CHQ_DISHON";
				AppConfigValues appConfigValues = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG, appConfigKey).get(0);
				String gjvForRcpt=appConfigValues.getValue();
				DishonoredEntriesDelegate delegate = new DishonoredEntriesDelegate();
				try{
					LOGGER.info(">>> inside saveDetails");
					DishonoredChequeForm dishonCheqForm=(DishonoredChequeForm)form;
					String vTypeParam=(String)dishonCheqForm.getVoucherTypeParam();
					LOGGER.info("vTypeParam --->"+vTypeParam);
					String pTxnDate ="";
					LOGGER.info("pTxnDate BEFORE CONVERT"+(String)dishonCheqForm.getVoucherTxnDate());
					Date dt = new Date();

					if(!("".equals(dishonCheqForm.getVoucherTxnDate()))){
						dt = sdf.parse((String)dishonCheqForm.getVoucherTxnDate());
						pTxnDate = formatter.format(dt);
					}
					int passAccId=Integer.parseInt((String)dishonCheqForm.getPassAccId());
					if((String)dishonCheqForm.getPassFundId()!=null && !((String)dishonCheqForm.getPassFundId()).equals("")){
						fundId=Integer.parseInt((String)dishonCheqForm.getPassFundId());
					}
					if((String)dishonCheqForm.getPassFundSrcId()!=null && !((String)dishonCheqForm.getPassFundSrcId()).equals("")){
						fundSourceId=Integer.parseInt((String)dishonCheqForm.getPassFundSrcId());
			        }
					if(null !=dishonCheqForm.getDepartmentId() && dishonCheqForm.getDepartmentId().trim().length()!=0 ){
						departmentId = (String)dishonCheqForm.getDepartmentId();
					}
		            int vHeadId=Integer.parseInt((String)dishonCheqForm.getPassVoucherId());
		            int instrumentHeadId=Integer.parseInt((String)dishonCheqForm.getInstrumentHeaderId());

				    String passRefNo = (String)dishonCheqForm.getPassRefNo();
				    int payinVHeadId=Integer.parseInt((String)dishonCheqForm.getPassPayinVHId());
				    String passVoucher[]=(String[])dishonCheqForm.getPassVoucher();
//			        String passChqNo[]=(String[])dishonCheqForm.getPassChqNo();
			        String chqDate[]=(String[])dishonCheqForm.getPassChqDate();
					String passedAmount = (String)dishonCheqForm.getPassedAmount();
					String debitAmount[] = new String[dishonCheqForm.getDebitAmount().length]  ;
					int debitCount = 0;
					for (int i = 0; i < dishonCheqForm.getDebitAmount().length; i++) {
						if(!StringUtils.isEmpty(dishonCheqForm.getDebitAmount()[i])){
							debitAmount[debitCount] = dishonCheqForm.getDebitAmount()[i];
							debitCount++;
						}
						 
						
					}
					
					
					String[] creditAmount = new String[dishonCheqForm.getCreditAmount().length]  ;
					int creditCount = 0;
					for (int i = 0; i < dishonCheqForm.getCreditAmount().length; i++) {
						if(!StringUtils.isEmpty(dishonCheqForm.getCreditAmount()[i])){
							creditAmount[creditCount] = dishonCheqForm.getCreditAmount()[i];
							creditCount++;
						}
						 
						
					}
					
					String bankTotalAmt = (String)dishonCheqForm.getBankTotalAmt();
					Double bankChargesAmt=Double.parseDouble(bankTotalAmt);

					String glcodeChId[]= (String[])dishonCheqForm.getGlcodeChId();
					String glcodeChIdP[]= (String[])dishonCheqForm.getGlcodeChIdP();
					for (int i = 0; i < glcodeChIdP.length; i++) {
						glcodeChIdP[i] = glcodeChIdP[i].split("~")[0];
					}
					String glcodeBkId= (String)dishonCheqForm.getGlcodeBkId();
					String chqReason = (String)dishonCheqForm.getChqReason();
					String chqReasonP = (String)dishonCheqForm.getChqReasonP();

					String bankChReason = (String)dishonCheqForm.getBankChReason();
					BankEntries be;

		/* 	 Create Payment Voucher for Bank Charges	*/
					Bankaccount bankAccount = getBankAccount(passAccId);
					
					if(bankChargesAmt >0 && !bankTotalAmt.equals("") && !glcodeBkId.equals("")){
						CVoucherHeader voucherHeader=null;
						String voucherNumber = dishonCheqForm.getBankChargesVoucherNumber();
						String[] glCode = new String[]{bankAccount.getChartofaccounts().getGlcode()};
						be = createBankEntry(pTxnDate, passAccId, passRefNo,bankTotalAmt, glcodeBkId, bankChReason); 

						voucherHeader = createVoucherHeader(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT,pTxnDate, chqReason, voucherNumber);
						
						//bankChargesVhIdValue=bankEntriesDelegate.createPaymtVouForDishonBankCharges(vHeadId,passRefNo,bankTotalAmt,narration,pTxnDate,glcodeBkId,passAccId,userId,fundId,fundSourceId,fieldId,departmentId,functionaryId,HibernateUtil.getCurrentSession().connection());
						InstrumentHeader instrument = instrumentService.addToInstrument(createInstruments(voucherNumber,formatter.parse(pTxnDate),new BigDecimal(passedAmount),bankAccount,"1", "bank")).get(0);
						instrumentService.updateInstrumentOtherDetailsStatus(instrument,formatter.parse(pTxnDate),BigDecimal.ZERO);
						voucherHeader.setName("Bank Entry");
						HashMap<String, Object> headerDetails = createHeaderAndMisDetails(voucherHeader,vHeadId,departmentId,fundId,fundSourceId,fieldId);
						
							voucherHeader = createVoucher(voucherHeader,headerDetails,new String[]{},glCode,getGlCodeForId(glcodeBkId),"",new String[]{bankTotalAmt},null,dishonCheqForm.getSubledgerDetails());
						
						updateInstrumentVoucherReference(Arrays.asList(instrument), voucherHeader);
						be.setVoucherheaderId(voucherHeader.getId().toString());
						be.setInstrumentHeaderId(instrument.getId());
						be.insert(conn);
						bankChargesVhIdValue = voucherHeader.getId().toString();
					}
					
			/*  Create Payment Voucher for -------------->Receipt Reversal  */
					   
					Date chequeDate = sdf.parse(chqDate[0]);
					if(vTypeParam.equalsIgnoreCase(RECEIPT) || JOURNAL_VOUCHER.equalsIgnoreCase(vTypeParam)){
						if(debitAmount != null){
							CVoucherHeader voucherHeader=null;
							String voucherNumber = dishonCheqForm.getReversalVoucherNumber();
							//If reversal for receipt, then according to appconfig value get the prefix for voucher.
							if (vTypeParam.equalsIgnoreCase(RECEIPT) && gjvForRcpt.equalsIgnoreCase("Y")){
								voucherHeader = createVoucherHeader(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL,pTxnDate, chqReason, voucherNumber);
							}
							else {
								voucherHeader = createVoucherHeader(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT,pTxnDate, chqReason, voucherNumber);
							}
							List<InstrumentHeader> instrument = instrumentService.addToInstrument(createInstruments(voucherNumber,chequeDate,new BigDecimal(passedAmount),bankAccount,"1", "bank"));
							instrument.get(0).setStatusId(getReconciledStatus());
							instrumentHeaderService.persist(instrument.get(0));
							LOGGER.info("---------------------------"+debitAmount.toString());
							instrumentService.updateInstrumentOtherDetailsStatus(instrument.get(0),formatter.parse(pTxnDate),new BigDecimal(getTotalAmount(debitAmount)));
							voucherHeader.setName("Receipt Reversal");
							voucherHeader.setDescription(chqReason);
							HashMap<String, Object> headerDetails = createHeaderAndMisDetails(voucherHeader,vHeadId,departmentId,fundId,fundSourceId,fieldId);
							CVoucherHeader paymentVoucher = createVoucher(voucherHeader,headerDetails,glcodeChId,glcodeChIdP,bankAccount.getChartofaccounts().getGlcode(),vTypeParam,debitAmount,creditAmount,dishonCheqForm.getSubledgerDetails());
							reversalVhIdValue = paymentVoucher.getId().toString();
							//instrumentService.addToBankReconcilation(voucherHeader,instrument.get(0));
							updateInstrumentVoucherReference(instrument, paymentVoucher);
							//reversalVhIdValue=dishonoredEntriesDelegate.createPaymtVouForReversalDishonChq(passedAmount,vHeadId,passRefNo,chqTotalAmt,chqReason,pTxnDate,glcodeChId,passAccId,userId,fundId,fundSourceId,fieldId,departmentId,functionaryId,HibernateUtil.getCurrentSession().connection());
							LOGGER.info("After calling createPaymtVouForReversalDishonChq VoucherHeaderId is:"+reversalVhIdValue);
						}
					}
			/*  Create Receipt Voucher for ---------------->Payment Reversal  */
					if(vTypeParam.equalsIgnoreCase(PAYMENT)){
						if(creditAmount != null){
							String voucherNumber =dishonCheqForm.getReversalVoucherNumber();
							CVoucherHeader voucherHeader = createVoucherHeader(FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT,pTxnDate, chqReason, voucherNumber);
							List<InstrumentHeader> instrument = instrumentService.addToInstrument(createInstruments(voucherNumber,chequeDate,new BigDecimal(passedAmount),bankAccount,"0", "bank"));
							instrument.get(0).setStatusId(getReconciledStatus());
							instrumentHeaderService.persist(instrument.get(0));
							instrumentService.updateInstrumentOtherDetailsStatus(instrument.get(0),formatter.parse(pTxnDate),new BigDecimal(getTotalAmount(creditAmount)));
							voucherHeader.setName("Payment Reversal"); 
							voucherHeader.setDescription(chqReasonP);
							HashMap<String, Object> headerDetails = createHeaderAndMisDetails(voucherHeader,payinVHeadId,departmentId,fundId,fundSourceId,fieldId);
							CVoucherHeader receiptVoucher = createVoucher(voucherHeader, headerDetails,glcodeChId,glcodeChIdP,bankAccount.getChartofaccounts().getGlcode(),vTypeParam,creditAmount,null,dishonCheqForm.getSubledgerDetails());
							reversalVhIdValue = receiptVoucher.getId().toString();
							updateInstrumentVoucherReference(instrument, receiptVoucher);
							//reversalVhIdValue=dishonoredEntriesDelegate.createReceiptVoucherForPaymentReversal(passedAmount,vHeadId,passRefNo,creditAmount,chqReasonP,pTxnDate,glcodeChIdP,passAccId,userId,fundId,fundSourceId,fieldId,departmentId,functionaryId,HibernateUtil.getCurrentSession().connection());
							LOGGER.info("After calling createReceiptVoucherForPaymentReversal VoucherHeaderId is:"+reversalVhIdValue);
						}
					}
					
					for(int i=0; i < passVoucher.length; i++){
						if(passVoucher[i].equalsIgnoreCase("yes")){
							delegate.updateInstrumentHeader(instrumentHeadId,persistenceService);
						}
					}
					target = SUCCESS;
					alertMessage="Executed successfully";
					req.setAttribute("alertMessage", alertMessage);
					req.setAttribute("buttonType",req.getParameter("buttonValue"));
					req.setAttribute("reversalVhId",reversalVhIdValue);
					req.setAttribute("bankChargesVhId",bankChargesVhIdValue);
					req.setAttribute("reversalAmount",passedAmount);
					req.setAttribute("bankChargesAmount",bankTotalAmt);
			   }catch(InsufficientAmountException ex){
				   target = SUCCESS;
				   alertMessage=ex.getMessage();
				   LOGGER.error("Exception Encountered!!!"+ex.getMessage());
				   HibernateUtil.rollbackTransaction();
		   		}
			   catch(ValidationException ex){
				   target = SUCCESS;
				   List<ValidationError> errors=ex.getErrors();
				   errors.get(0).getMessage();
				   alertMessage=errors.get(0).getMessage();
				   LOGGER.error("Exception Encountered!!!"+errors.get(0).getMessage());
				   HibernateUtil.rollbackTransaction();
		   		}
			   
		   		catch(Exception ex){
				   target = ERROR;
				   LOGGER.error("Exception Encountered!!!"+ex.getMessage());
				   HibernateUtil.rollbackTransaction();
		   		}
		   		req.setAttribute("alertMessage", alertMessage);
				return mapping.findForward(target);
			
				
			}
		});
	}
	
	private Double getTotalAmount(String[] chqTotalAmt) {
		Double total = Double.valueOf("0");
		for (String amt : chqTotalAmt) {
			if(!StringUtils.isEmpty(amt)){
				total = total+Double.valueOf(amt);
			}
			
		}
		return total;
	}
	private EgwStatus getReconciledStatus(){
		return (EgwStatus)persistenceService.find("from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper('"+FinancialConstants.INSTRUMENT_RECONCILED_STATUS+"')");
	}
	BankEntries createBankEntry(String pTxnDate, int passAccId,String passRefNo, String bankTotalAmt, String glcodeBkId,String bankChReason) {
		BankEntries be = new BankEntries();
		be.setBankAccountId(passAccId);
		be.setRefNo(passRefNo);
		be.setTxnAmount(bankTotalAmt);
		be.setType("P");
		be.setRemarks(bankChReason);
		be.setTxnDate(pTxnDate);
		be.setGlcodeId(glcodeBkId);
		return be;
	}
	
	private String getGlCodeForId(String id) {
		String glCode = "";
		if(id!=null && !id.isEmpty())
			glCode = ((CChartOfAccounts)persistenceService.find("from CChartOfAccounts where id=?",Long.valueOf(id))).getGlcode();
		return glCode;
	}
	private String getVoucherNumber(final String type,final int fundId, final int fieldId, final String pTxnDate) throws Exception {
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {
			String voucherNumber;
			@Override
			public String execute(Connection conn) throws SQLException {
				try {
					CommonMethodsI cmImpl = new CommonMethodsImpl();
					voucherNumber= cmImpl.getTxnNumber(String.valueOf(fundId), type, sdf.format(formatter.parse(pTxnDate)), conn);
				} catch (Exception e) {
					// TODO: handle exception
				}
				return voucherNumber;
			
			}
		});
	}
	private CVoucherHeader createVoucherHeader(String type,String voucherDate, String description, String voucherNumber)throws ParseException {
		CVoucherHeader voucherHeader = new CVoucherHeader();
		voucherHeader.setType(type);
		if(voucherNumber!=null && !voucherNumber.isEmpty())
		{
		voucherHeader.setVoucherNumber(voucherNumber);
		}
		voucherHeader.setVoucherDate(formatter.parse(voucherDate));
		voucherHeader.setDescription(description);
		return voucherHeader;
	}
	
	public ActionForward beforePrintDishonoredCheque(final ActionMapping mapping,final ActionForm form,final HttpServletRequest req,
			final HttpServletResponse res)
		throws IOException,ServletException{
		
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<ActionForward>() {

			@Override
			public ActionForward execute(Connection conn) throws SQLException {
				

				String target ="";
				try{
					LOGGER.info("Inside beforePrintDishonoredCheque");
					String reversalVhId=req.getParameter("reversalVhId");
					String bankChargesVhId=req.getParameter("bankChargesVhId");
					LOGGER.info("reversalVhId>>"+reversalVhId);
					LOGGER.info("bankChargesVhId>>"+bankChargesVhId);
					String reversalAmount=req.getParameter("reversalAmount");
					String bankChargesAmount=req.getParameter("bankChargesAmount");
					LOGGER.info("reversalAmount>>"+reversalAmount);
					LOGGER.info("bankChargesAmount>>"+bankChargesAmount);
					DishonoredChequeForm disForm=(DishonoredChequeForm)form;
					DishonoredEntriesDelegate dised=new DishonoredEntriesDelegate();

					if(!reversalVhId.equals("") && reversalVhId!=null && reversalVhId.length()>0){
						ArrayList alAccDetailsForRevVoucher=dised.getDishonoredRevVoucherAccCodeDetails(Long.parseLong(reversalVhId),conn);
						if(!alAccDetailsForRevVoucher.isEmpty()){
							String[] reversalAccCode=new String[alAccDetailsForRevVoucher.size()] ;
							String[] reversalDescn=new String[alAccDetailsForRevVoucher.size()] ;
							String[] reversalDebitAmount=new String[alAccDetailsForRevVoucher.size()] ;
							String[] reversalCreditAmount=new String[alAccDetailsForRevVoucher.size()] ;

							for (int n = 0; n < alAccDetailsForRevVoucher.size(); n++){
								DishonoredViewEntries dve=(DishonoredViewEntries)alAccDetailsForRevVoucher.get(n);
							 	reversalAccCode[n]=dve.getReversalAccCode();
							 	reversalDescn[n]=dve.getReversalDescn();
							 	reversalDebitAmount[n]=dve.getReversalDebitAmount();
							 	reversalCreditAmount[n]=dve.getReversalCreditAmount();
							}// for
							disForm.setReversalAccCode(reversalAccCode);
							disForm.setReversalDescn(reversalDescn);
							disForm.setReversalDebitAmount(reversalDebitAmount);
							disForm.setReversalCreditAmount(reversalCreditAmount);
						}//if
						ArrayList alVHeaderDetailsForRev=dised.getDishonoredRevVoucherHeaderDetails(Long.parseLong(reversalVhId),conn);
						for (Iterator itr = alVHeaderDetailsForRev.iterator(); itr.hasNext(); )
			            {
							DishonoredViewEntries dve=(DishonoredViewEntries)itr.next();
							disForm.setVouHName(dve.getVouHName());
							disForm.setReason(StringUtils.isEmpty(dve.getReason())?"":dve.getReason());
							disForm.setVoucherNumber(dve.getVoucherNumber());
							disForm.setVouDate(dve.getVouDate());

							disForm.setFund(dve.getFund());
			            }
						disForm.setPassedAmount(reversalAmount);
					}
						if(!bankChargesVhId.equals("") && bankChargesVhId!=null && bankChargesVhId.length()>0){
							LOGGER.info("Inside Bank charges entry details -getting query");
							ArrayList alAccDetailsForBkChgs=dised.getDishonoredRevVoucherAccCodeDetails(Long.parseLong(bankChargesVhId),conn);
							if(!alAccDetailsForBkChgs.isEmpty()){
								String[] reversalAccCode=new String[alAccDetailsForBkChgs.size()] ;
								String[] reversalDescn=new String[alAccDetailsForBkChgs.size()] ;
								String[] reversalDebitAmount=new String[alAccDetailsForBkChgs.size()] ;
								String[] reversalCreditAmount=new String[alAccDetailsForBkChgs.size()] ;
								for (int n = 0; n < alAccDetailsForBkChgs.size(); n++){
									DishonoredViewEntries dve=(DishonoredViewEntries)alAccDetailsForBkChgs.get(n);
								 	reversalAccCode[n]=dve.getReversalAccCode();
								 	reversalDescn[n]=dve.getReversalDescn();
								 	reversalDebitAmount[n]=dve.getReversalDebitAmount();
								 	reversalCreditAmount[n]=dve.getReversalCreditAmount();
								}
								List<Object[]> list = getBankChargesDetails(bankChargesVhId);
								disForm.setReversalAccCodeBC(reversalAccCode);
								disForm.setReversalDescnBC(reversalDescn);
								disForm.setReversalDebitAmountBC(reversalDebitAmount);
								disForm.setReversalCreditAmountBC(reversalCreditAmount);
								if(list!=null){
									disForm.setRefNo((list.get(0)[0]==null?"":list.get(0)[0].toString()));
									disForm.setRefDate(list.get(0)[1]==null?"":list.get(0)[1].toString());
								}
							}
							ArrayList alVHeaderDetailsForBkChgs=dised.getDishonoredRevVoucherHeaderDetails(Long.parseLong(bankChargesVhId),conn);

							for (Iterator itr = alVHeaderDetailsForBkChgs.iterator(); itr.hasNext(); ){
								DishonoredViewEntries dve=(DishonoredViewEntries)itr.next();
								disForm.setVouHNameBC(dve.getVouHName());
								disForm.setReasonBC(dve.getReason());
								disForm.setVoucherNumberBC(dve.getVoucherNumber());
								disForm.setVouDateBC(dve.getVouDate());
								}
							disForm.setBankTotalAmt(bankChargesAmount);
						}
						target="printDishonoredChequeDetail";
						req.setAttribute("DishonoredChequeForm", disForm);  
				}
				catch(Exception ex){
				   target = ERROR;
				   LOGGER.debug("Exception Encountered!!!"+ex.getMessage());
				}
			return mapping.findForward(target);
		
				
			}
		});
	}

	private List<Object[]> getBankChargesDetails(String bankChargesVhId) {
		SQLQuery query = persistenceService.getSession().createSQLQuery("select refno,to_char(txndate, 'dd/mm/yyyy') from bankentries where voucherheaderid="+bankChargesVhId);
		return query.list();
	}
	List<Map<String, Object>> createInstruments(String instrumentNumber,	Date instrumentDate, BigDecimal instrumentAmount,
			Bankaccount bankAccount, String isPayCheque, String instrumentType) {
		final Map<String, Object> iMap = new HashMap<String, Object>();
		final List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
		iMap.put("Transaction number", instrumentNumber);
		iMap.put("Transaction date", instrumentDate);
		iMap.put("Instrument amount", instrumentAmount.doubleValue());
		iMap.put("Instrument type", instrumentType);
		iMap.put("Bank code",  bankAccount.getBankbranch().getBank().getCode());
		iMap.put("Bank branch name", bankAccount.getBankbranch().getBranchaddress1());
		iMap.put("Bank account id", bankAccount.getId());
		iMap.put("Is pay cheque", isPayCheque);
		iList.add(iMap);
		return iList;
	}
	
	private Bankaccount getBankAccount(Integer id) {
		return (Bankaccount) persistenceService.find("from Bankaccount where id=?",id);
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setInstrumentService(InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
	}
	
	CVoucherHeader createVoucher(CVoucherHeader voucher,HashMap<String, Object> headerDetails,String[] glCode,String[] glCodeP,String bankAccountGlCode,String voucherType, String[] debitAmount,String[] creditAmount,String subLedger) {
		CVoucherHeader voucherHeader = null;
		try {
			headerDetails.put(VoucherConstant.SOURCEPATH,"");
			List<HashMap<String, Object>> subledgerDetails;
			if(null != subLedger && !StringUtils.isEmpty(subLedger)){
				 subledgerDetails = populateSubledgerDetails(subLedger);
			}else{
				subledgerDetails = new ArrayList<HashMap<String,Object>>();
			}
			
			List<HashMap<String, Object>> accountdetails = populateAccountDetails(glCode,glCodeP,bankAccountGlCode,voucherType,debitAmount,creditAmount);
			CreateVoucher cv = new CreateVoucher();
			voucherHeader = cv.createVoucher(headerDetails, accountdetails, subledgerDetails);
			voucherHeader.getVouchermis().setSourcePath("");
		} catch (final HibernateException e) { 
			throw new ValidationException(Arrays.asList(new ValidationError("", "")));
		} 
		catch (ValidationException e) {
			throw e;
		}
		catch (final Exception e) {
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),e.getMessage())));
		}
		return voucherHeader;
	}
	
	HashMap<String, Object> createHeaderAndMisDetails(CVoucherHeader voucherHeader,int originalVoucherId, String departmentId,int fundId,int fundSourceId,int fieldId) throws ValidationException{
		HashMap<String, Object> headerdetails = new HashMap<String, Object>();
		headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeader.getName());
		headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeader.getType());
		headerdetails.put(VoucherConstant.VOUCHERNUMBER, voucherHeader.getVoucherNumber());
		headerdetails.put(VoucherConstant.VOUCHERDATE, voucherHeader.getVoucherDate());
		headerdetails.put(VoucherConstant.DESCRIPTION, voucherHeader.getDescription());
		headerdetails.put(VoucherConstant.ORIGIONALVOUCHER, String.valueOf(originalVoucherId));
		headerdetails.put(VoucherConstant.STATUS, 2);
		if(departmentId!=null && !departmentId.isEmpty())
			headerdetails.put(VoucherConstant.DEPARTMENTCODE, ((DepartmentImpl)persistenceService.find("from DepartmentImpl where id=?",new Integer(departmentId))).getDeptCode());
		if(fundId>0)
			headerdetails.put(VoucherConstant.FUNDCODE, ((Fund)persistenceService.find("from Fund where id=?",new Integer(fundId))).getCode());
		if(fundSourceId>0)
			headerdetails.put(VoucherConstant.FUNDSOURCECODE, ((Fundsource)persistenceService.find("from Fundsource where id=?",new Integer(fundSourceId))).getCode());
		if(fieldId>0)
			headerdetails.put(VoucherConstant.DIVISIONID, new Integer(fieldId));
		return headerdetails;
	}
	
	List<HashMap<String, Object>> populateSubledgerDetails(String subLedgerDetails) {
		List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
		
		LOGGER.debug(subLedgerDetails);
		
		String[] subledgerToken = subLedgerDetails.split("~");
		for(int i =0; i < subledgerToken.length; i++){
			HashMap<String, Object> subledgerMap = new HashMap<String, Object>();
			String[] token = subledgerToken[i].split("-");
			subledgerMap.put(VoucherConstant.GLCODE, token[0].toString());
			subledgerMap.put(VoucherConstant.DETAILTYPEID, token[1].toString());
			subledgerMap.put(VoucherConstant.DETAILKEYID, token[2].toString());
			if("debit".equalsIgnoreCase(token[3].toString())){
				subledgerMap.put(VoucherConstant.DEBITAMOUNT, token[4].toString());
			}else if ("credit".equalsIgnoreCase(token[3].toString())){
				subledgerMap.put(VoucherConstant.CREDITAMOUNT, token[4].toString());
				List<Recovery> tdslist = new ArrayList<Recovery>();
				//persistenceService.findAllBy(query, params)
				//persistenceService.find(query)
				tdslist= persistenceService.findAllBy(" from Recovery where chartofaccounts.glcode="+token[0].toString());
				if (!tdslist.isEmpty()){
					for (Recovery tds : tdslist){
						if(tds.getType().equals(token[0].toString())){
							subledgerMap.put(VoucherConstant.TDSID, tds.getId());							
						}	
					}
				}
			}else{
				throw new EGOVRuntimeException("DishonoredChequeAction |  populatesubledgerDetails | not able to find either debit or " +
						"credit amount");
			}
			
			
			subledgerDetails.add(subledgerMap);
		}
		
		return subledgerDetails;
	}
	
	List<HashMap<String, Object>> populateAccountDetails(String[] coaId,String[] coaPId,String bankAccountGlCode,String voucherType, String[] debitAmount,String[] creditAmount) {
		List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
		BigDecimal totalAmountDbt = BigDecimal.ZERO;
		BigDecimal totalAmountCrd = BigDecimal.ZERO;
		if(RECEIPT.equals(voucherType) || JOURNAL_VOUCHER.equals(voucherType)){
			for (int i = 0; i < debitAmount.length; i++) {
				if(!StringUtils.isEmpty(debitAmount[i])){
					BigDecimal amount = new BigDecimal(debitAmount[i]);
					String code = coaId[i];
					if(!StringUtils.equals(debitAmount[i], "0.0")){
						accountdetails.add(populateDetailMap(code,BigDecimal.ZERO,amount));	
					}
					totalAmountDbt = totalAmountDbt.add(amount);
				}
			}
			for (int i = 0; i < creditAmount.length; i++) {
				if(!StringUtils.isEmpty(creditAmount[i])){
					BigDecimal amount = new BigDecimal(creditAmount[i]);
					String code = coaId[i];
					if(!StringUtils.equals(creditAmount[i], "0.0")){
						accountdetails.add(populateDetailMap(code,amount,BigDecimal.ZERO));	
					}
					totalAmountCrd = totalAmountCrd.add(amount);
				}
				
			}
			accountdetails.add(populateDetailMap(bankAccountGlCode,totalAmountDbt.subtract(totalAmountCrd),BigDecimal.ZERO));
		}else{
			for (int i = 0; i < debitAmount.length; i++) {
				if(!StringUtils.isEmpty(debitAmount[i])){
					String code = coaPId[i];
					BigDecimal amount = new BigDecimal(debitAmount[i]);
					accountdetails.add(populateDetailMap(code,amount,BigDecimal.ZERO));
					totalAmountDbt = totalAmountDbt.add(amount);
				}
				
			}
			accountdetails.add(populateDetailMap(bankAccountGlCode,BigDecimal.ZERO,totalAmountDbt));
		}
		return accountdetails;
	}

	HashMap<String, Object> populateDetailMap(String glCode,BigDecimal creditAmount,BigDecimal debitAmount){
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		detailMap.put(VoucherConstant.CREDITAMOUNT, creditAmount.toString());
		detailMap.put(VoucherConstant.DEBITAMOUNT, debitAmount.toString());
		detailMap.put(VoucherConstant.GLCODE, glCode);
		return detailMap;
	}
	void updateInstrumentVoucherReference(List<InstrumentHeader> instrumentList,CVoucherHeader voucherHeader) {
		Map<String, Object> iMap = new HashMap<String, Object>();
		List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
		iMap.put("Instrument header", instrumentList.get(0));
		iMap.put("Voucher header", voucherHeader);
		iList.add(iMap);
		instrumentService.updateInstrumentVoucherReference(iList);
	}
	private Date getNextDate(Date date,int amount){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, amount);
		return calendar.getTime();
	}
	public void setInstrumentHeaderService(PersistenceService<InstrumentHeader, Long> instrumentHeaderService) {
		this.instrumentHeaderService = instrumentHeaderService;
	}
	public GenericHibernateDaoFactory getGenericDao() {
		return genericDao;
	}
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
}

