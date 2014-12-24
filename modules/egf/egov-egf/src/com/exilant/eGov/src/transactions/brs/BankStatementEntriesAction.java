/*
 * BankStatementEntriesAction.java Created on Aug 25, 2006
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;
/**
 *
 * @author Tilak
 * @version 1.00
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankreconciliation;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.Fundsource;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.model.instrument.InstrumentType;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.services.instrument.InstrumentService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.BankBranch;
import com.exilant.eGov.src.domain.BankEntries;
import com.exilant.eGov.src.domain.Fund;
import com.exilant.eGov.src.transactions.CommonMethodsI;
import com.exilant.eGov.src.transactions.CommonMethodsImpl;
import com.exilant.eGov.src.transactions.InsufficientAmountException;
import com.exilant.exility.common.TaskFailedException;

public class BankStatementEntriesAction extends DispatchAction {
	public BankStatementEntriesAction() {
		super();

	}
	private static  final Logger LOGGER = Logger.getLogger(BankStatementEntriesAction.class);
	private static final String SUCCESS="success";
	private static final String ERROR="error";
	private static final String EXCEPTION="Exception Encountered!!!";
	private static final String ACCID="accId";
	private CommonsService commonsService;
	private DepartmentService departmentService;
	String target = "";
	SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	Date dt;
	EGovernCommon cm = new EGovernCommon();
	String alertMessage=null;
	InstrumentService instrumentService;
	PersistenceService persistenceService;
	/*
		  get list of bankbranches
	*/
	public ActionForward toLoad(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws Exception
	{

		try{
				LOGGER.info(">>> inside toLoad");
				BankBranch bb=new BankBranch();
				HashMap hm=bb.getBankBranch();
				req.getSession().setAttribute("bankBranchList", hm);
				req.getSession().setAttribute("accountCodeListForDetails",(ArrayList)commonsService.getDetailedAccountCodeList());
				LOGGER.info(">>> before ending BankStatementEntriesAction");
				target = SUCCESS;

		   }
		catch(Exception ex)
		{
		   target = ERROR;
		   LOGGER.debug(EXCEPTION+ex.getMessage());
		   
   		}
		return mapping.findForward(target);

	}
	/*
			  get list of accountnumber for given bankbranch
	*/
	public ActionForward getAccountNumbers(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
		throws Exception
		{

			try{
					LOGGER.info(">>> inside getAccountNumbers");
					org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
					BankBranch bb=new BankBranch();
					LOGGER.info("bank id  "+bankRecForm.get("bankId"));
					HashMap hm=bb.getAccNumber(((String)bankRecForm.get("bankId")));
					req.getSession().setAttribute("accNumberList2", hm);
					LOGGER.debug(">>> before ending BankStatementEntriesAction");
					target = SUCCESS;

			   }
			catch(Exception ex)
			{
			   target = ERROR;
			   LOGGER.debug(EXCEPTION+ex.getMessage());
			
	   		}
			return mapping.findForward(target);

	}
/*
	get list of bank entries for which vouchers has to be passed
*/
	public ActionForward getDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws Exception
	{

		try{
				LOGGER.info(">>> inside getDetails");
				org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
				LOGGER.info("bankacc id  "+bankRecForm.get(ACCID));
				BankEntries be=new BankEntries();
				ArrayList al=be.getRecords((String)bankRecForm.get(ACCID));
				//List instrumentList=getInstrumentListByAccountId(Integer.valueOf((String)bankRecForm.get(ACCID)));
				Fund fu=new Fund();
				HashMap fudList=fu.getFundList();
				List departmentList = departmentService.getAllDepartments();
				LOGGER.debug("Department List Size " + departmentList.size());
				req.setAttribute("departmentList", departmentList);
				
				List functionaryList =commonsService.getActiveFunctionaries();
				LOGGER.debug("Functionary List Size " + functionaryList.size());
				req.setAttribute("functionaryList", functionaryList);
				
				
				HashMap fudSourceList=fu.getFundSourceList();
				String defaultFund=fu.getFundForAcc((String)bankRecForm.get(ACCID));
				LOGGER.debug(">>> after list creation BankStatementEntriesAction");
				req.setAttribute("fudList", fudList);
				req.setAttribute("fudSourceList", fudSourceList);
				req.setAttribute("defaultFund", defaultFund);
				req.setAttribute("brsEntries", al);
			//	req.setAttribute("instrumentHeaderList", instrumentList);
				LOGGER.debug(">>> after list creation BankStatementEntriesAction");
				target = SUCCESS;
		   }
		catch(Exception ex)
		{
		   target = ERROR;
		   LOGGER.debug(EXCEPTION+ex.getMessage());
		   HibernateUtil.rollbackTransaction();
   		}
		return mapping.findForward(target);

	}
	/**
 * @param valueOf
 * @return
 */
private List getInstrumentListByAccountId(Integer bankAccId) {

	List instrumentList = instrumentService.persistenceService.findAll(" select ih from  InstrumentHeader ih,InstrumentVoucher iv where bankAccountId.id=?  and iv.instrumentHeaderId is null ");
	return instrumentList;
}
	/*
		pass voucher for bankentry
	*/
	public ActionForward saveDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
		throws TaskFailedException,Exception
		{
				

			try{
					LOGGER.info(">>> inside saveDetails");
					org.apache.struts.action.DynaActionForm bankRecForm = (org.apache.struts.action.DynaActionForm)form;
					int accId=Integer.parseInt((String)bankRecForm.get(ACCID));
					int fundId=Integer.parseInt((String)bankRecForm.get("fundId"));
					int fundSourceId=Integer.parseInt((String)bankRecForm.get("fundSourceId"));
					String departmentId="0";
					if(!"".equalsIgnoreCase((String)bankRecForm.get("departmentId"))){
						departmentId = (String)bankRecForm.get("departmentId");
					}
					int functionaryId=0;
					if(!"".equalsIgnoreCase((String)bankRecForm.get("functionaryId"))){
						functionaryId = Integer.parseInt((String)bankRecForm.get("functionaryId"));
					}
					
					LOGGER.debug("department id >>>>>>>>"+ departmentId);
					LOGGER.debug("functionary id >>>>>>>>"+ functionaryId);
					
					LOGGER.debug("bankacc id  "+accId);
					LOGGER.debug("fundId id  "+fundId);
					LOGGER.debug("fundSourceId id  "+fundSourceId);
					LOGGER.debug("functionary id >>>>>>>>"+ functionaryId);
					String[] bankEntryId = (String[])bankRecForm.get("bankEntryId"); 
					String[] refNo = (String[])bankRecForm.get("refNo");
					String[] type = (String[])bankRecForm.get("type");
					String[] entrydate = (String[])bankRecForm.get("entrydate");
					String[] amount = (String[])bankRecForm.get("amount");
					String[] remarks = (String[])bankRecForm.get("remarks");
					String accountCodeId[]= (String[])bankRecForm.get("accountCodeId");
					String isUpdated[]= (String[])bankRecForm.get("isUpdated");
					String passVoucher[]=(String[])bankRecForm.get("passVoucher");
					String instruments[]=(String[])bankRecForm.get("instrumentHeaderId");
					BankEntries be;
					List<InstrumentHeader> instrumentList=null;
					setInstrumentRelatedServices();
					
					int userId=((Integer)req.getSession().getAttribute("com.egov.user.LoginUserId")).intValue();
					for(int i=0; i < refNo.length; i++)
					{
						LOGGER.debug(">... bankEntryId[i] >....."+bankEntryId[i]);
						LOGGER.debug(">... accountCodeId[i] >....."+accountCodeId[i]);
						LOGGER.debug(">... passVoucher[i] >....."+passVoucher[i]);
						LOGGER.debug(">... refNo[i] >....."+refNo[i]);
						LOGGER.debug(">... passVoucher[i] >....."+passVoucher[i]);
						if(bankEntryId[i] == null || bankEntryId[i].equalsIgnoreCase(""))
						{
							be=new BankEntries();
							be.setBankAccountId(accId);
							be.setRefNo(refNo[i]);
							be.setTxnAmount(amount[i]);
							be.setType(type[i]);
							be.setRemarks(remarks[i]);
							dt=new Date();
							dt = sdf.parse(entrydate[i]);
							String txnDate=formatter.format(dt);
							be.setTxnDate(txnDate);
							if(!accountCodeId[i].equalsIgnoreCase("0"))
							be.setGlcodeId(accountCodeId[i]);
							if(type[i].equalsIgnoreCase("R"))
							{
								instrumentList = addToInstrument(refNo[i], amount[i], accId, FinancialConstants.IS_PAYCHECK_ZERO, dt);	
							}else if(type[i].equalsIgnoreCase("P"))
								{
								instrumentList=	addToInstrument(refNo[i], amount[i], accId, FinancialConstants.IS_PAYCHECK_ONE, dt);		
								}
							depositInstrument(instrumentList.get(0).getId().toString());
							be.setInstrumentHeaderId(instrumentList.get(0).getId());
							if(passVoucher[i].equalsIgnoreCase("yes") && type[i].equalsIgnoreCase("P"))
							{
								CVoucherHeader paymentVoucher = saveVoucher(refNo[i],amount[i],remarks[i],txnDate,accountCodeId[i],
										accId,userId,fundId,fundSourceId,departmentId,functionaryId,instrumentList.get(i),"Payment");
//								String VoucherHeaderId=bed.createPaymentVoucher(refNo[i],amount[i],remarks[i],txnDate,accountCodeId[i],
//											accId,userId,fundId,fundSourceId,departmentId,functionaryId,HibernateUtil.getCurrentSession().connection());
								be.setVoucherheaderId(paymentVoucher.getId().toString());
								updateInstrumentReference(instrumentList.get(0),paymentVoucher.getId());
							}
							else if(passVoucher[i].equalsIgnoreCase("yes") && type[i].equalsIgnoreCase("R"))
							{
								CVoucherHeader receiptVoucher = saveVoucher(refNo[i],amount[i],remarks[i],txnDate,accountCodeId[i],
										accId,userId,fundId,fundSourceId,departmentId,functionaryId,instrumentList.get(i),"Receipt");
//								String VoucherHeaderId=bed.createReceiptVoucher(refNo[i],amount[i],remarks[i],txnDate,
//										accountCodeId[i],accId,userId,fundId,fundSourceId,departmentId,functionaryId,HibernateUtil.getCurrentSession().connection());
								be.setVoucherheaderId(receiptVoucher.getId().toString());
								updateInstrumentReference(instrumentList.get(0),receiptVoucher.getId());
							}

							be.insert();
						}
						if(bankEntryId[i] != null && bankEntryId[i].length()>0 && isUpdated[i].equalsIgnoreCase("yes"))
						{
						
							be=new BankEntries();
							InstrumentHeader instrumentHeader=null;
							be.setId(bankEntryId[i]);
							be.setBankAccountId(accId);
							be.setRefNo(refNo[i]);
							be.setTxnAmount(amount[i]);
							be.setType(type[i]);
							be.setRemarks(remarks[i]);
							dt=new Date();
							dt = sdf.parse(entrydate[i]);
							String txnDate=formatter.format(dt);
							be.setTxnDate(txnDate);
							if(!accountCodeId[i].equalsIgnoreCase("0"))
							be.setGlcodeId(accountCodeId[i]);
							if(type[i].equalsIgnoreCase("R"))
							{
								instrumentHeader = updateInstrumentHeader(instruments[i],refNo[i], amount[i], accId, FinancialConstants.IS_PAYCHECK_ZERO, dt);	
							}else if(type[i].equalsIgnoreCase("P"))
								{
								instrumentHeader=updateInstrumentHeader(instruments[i],refNo[i], amount[i], accId, FinancialConstants.IS_PAYCHECK_ONE, dt);		
								}
							if(passVoucher[i].equalsIgnoreCase("yes") && type[i].equalsIgnoreCase("P"))
							{
								CVoucherHeader paymentVoucher = saveVoucher(refNo[i],amount[i],remarks[i],txnDate,accountCodeId[i],
										accId,userId,fundId,fundSourceId,departmentId,functionaryId,instrumentHeader,"Payment");
//								String VoucherHeaderId=bed.createPaymentVoucher(refNo[i],amount[i],remarks[i],txnDate,
//										accountCodeId[i],accId,userId,fundId,fundSourceId,departmentId,functionaryId,HibernateUtil.getCurrentSession().connection());
								be.setVoucherheaderId(paymentVoucher.getId().toString());
								updateInstrumentReference(instrumentHeader,paymentVoucher.getId());
							}
							else if(passVoucher[i].equalsIgnoreCase("yes") && type[i].equalsIgnoreCase("R"))
							{
								CVoucherHeader receiptVoucher = saveVoucher(refNo[i],amount[i],remarks[i],txnDate,accountCodeId[i],
										accId,userId,fundId,fundSourceId,departmentId,functionaryId,instrumentHeader,"Receipt");
//								String VoucherHeaderId=bed.createReceiptVoucher(refNo[i],amount[i],remarks[i],txnDate,
//										accountCodeId[i],accId,userId,fundId,fundSourceId,departmentId,functionaryId,HibernateUtil.getCurrentSession().connection());
								be.setVoucherheaderId(receiptVoucher.getId().toString());
								updateInstrumentReference(instrumentHeader,receiptVoucher.getId());
							}
							be.update();
						}
					}
					target = SUCCESS;
					alertMessage="Executed successfully";
					req.setAttribute("alertMessage", alertMessage);
			   }

	   		catch(InsufficientAmountException ex)
			{
			   target = SUCCESS;
			   alertMessage=ex.getMessage();
			   LOGGER.debug(EXCEPTION+ex.getMessage());
			   HibernateUtil.rollbackTransaction();
	   		}
	   		catch(TaskFailedException tx)
	   		{
	   			target = SUCCESS;
	   			alertMessage="Transaction Failed="+tx.getMessage();
	   			LOGGER.debug("Task failed Exception Encountered!!!"+tx.getMessage());
	   			HibernateUtil.rollbackTransaction();
	   		}
	   		catch(Exception ex)
			{
			   target = ERROR;
			   LOGGER.debug(EXCEPTION+ex.getMessage());
			   HibernateUtil.rollbackTransaction();
	   		}
	   		req.setAttribute("alertMessage", alertMessage);
			return mapping.findForward(target);

	}
	
	private CVoucherHeader saveVoucher(String chqNumber,String chqAmount,String narration,String voucherDate,String glCodeId,
			int accId,int userId,int fundId,int fundSourceId,String departmentId,int functionaryId,InstrumentHeader instrument,String voucherType) throws Exception{
		
		CommonMethodsI cm=new CommonMethodsImpl();
		EGovernCommon eGovComn = new EGovernCommon();
		String vType = FinancialConstants.RECEIPT_VOUCHERNO_TYPE;
		String fiscalPeriodId=cm.getFiscalPeriod(voucherDate);
		String cgnNo = "DBP"+eGovComn.getCGNumber(); 
		String vNum = eGovComn.maxVoucherNumber(vType,Integer.valueOf(fundId).toString());
		String cgvn= eGovComn.getEg_Voucher(vType, fiscalPeriodId);
		for(int i=cgvn.length();i<5;i++)
		{
			cgvn="0"+cgvn;
		}
		cgvn=vType+cgvn;
		
		CVoucherHeader voucherHeader = createVoucherHeader(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT,voucherDate, narration, vNum);
		instrumentService.updateInstrumentOtherDetailsStatus(instrument,formatter.parse(voucherDate),new BigDecimal(chqAmount));
		voucherHeader.setDescription("Bank Reconciliation Entry - "+narration);
		voucherHeader.setName("Bank Entry");
		voucherHeader.setType(voucherType);
		voucherHeader.setCgvn(cgvn);
		voucherHeader.setCgn(cgnNo);
		voucherHeader.setDescription(narration);
		HashMap<String, Object> headerDetails = createHeaderAndMisDetails(voucherHeader,departmentId,fundId,fundSourceId,functionaryId);
		String bankAccCodeAndName=cm.getBankCode(accId);
		String bankAccCodeName[]=bankAccCodeAndName.split("#");
		String bankGlCode=bankAccCodeName[0];								
		if("Receipt".equalsIgnoreCase(voucherType))
			return createVoucher(voucherHeader,headerDetails,cm.getGlCode(glCodeId),"",bankGlCode,voucherType,chqAmount);
		else
			return createVoucher(voucherHeader,headerDetails,"",cm.getGlCode(glCodeId),bankGlCode,voucherType,chqAmount);
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

	CVoucherHeader createVoucher(CVoucherHeader voucher,HashMap<String, Object> headerDetails,String glCode,String glCodeP,String bankAccountGlCode,String voucherType, String chqTotalAmt) {
		CVoucherHeader voucherHeader = null;
		try {
			headerDetails.put(VoucherConstant.SOURCEPATH,"");
			List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
			List<HashMap<String, Object>> accountdetails = populateAccountDetails(glCode,glCodeP,bankAccountGlCode,voucherType,chqTotalAmt);
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
	List<HashMap<String, Object>> populateAccountDetails(String coaId,String coaPId,String bankAccountGlCode,String voucherType, String chqTotalAmt) {
		List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
		BigDecimal totalAmount = BigDecimal.ZERO;
		if("Receipt".equals(voucherType)){
				BigDecimal amount = new BigDecimal(chqTotalAmt);
				accountdetails.add(populateDetailMap(coaId,BigDecimal.ZERO,amount));
				totalAmount = totalAmount.add(amount);
			accountdetails.add(populateDetailMap(bankAccountGlCode,totalAmount,BigDecimal.ZERO));
		}else{
				BigDecimal amount = new BigDecimal(chqTotalAmt);
				accountdetails.add(populateDetailMap(coaPId,amount,BigDecimal.ZERO));
				totalAmount = totalAmount.add(amount);
			accountdetails.add(populateDetailMap(bankAccountGlCode,BigDecimal.ZERO,totalAmount));
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
	
	HashMap<String, Object> createHeaderAndMisDetails(CVoucherHeader voucherHeader, String departmentId,int fundId,int fundSourceId,int functionaryId) throws ValidationException{
		HashMap<String, Object> headerdetails = new HashMap<String, Object>();
		headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeader.getName());
		headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeader.getType());
		headerdetails.put(VoucherConstant.VOUCHERNUMBER, voucherHeader.getVoucherNumber());
		headerdetails.put(VoucherConstant.VOUCHERDATE, voucherHeader.getVoucherDate());
		headerdetails.put(VoucherConstant.DESCRIPTION, voucherHeader.getDescription());
		if(departmentId!=null && !departmentId.isEmpty())
			headerdetails.put(VoucherConstant.DEPARTMENTCODE, ((DepartmentImpl)persistenceService.find("from DepartmentImpl where id=?",new Integer(departmentId))).getDeptCode());
		if(fundId>0)
			headerdetails.put(VoucherConstant.FUNDCODE, ((org.egov.commons.Fund)persistenceService.find("from Fund where id=?",new Integer(fundId))).getCode());
		if(fundSourceId>0)
			headerdetails.put(VoucherConstant.FUNDSOURCECODE, ((Fundsource)persistenceService.find("from Fundsource where id=?",new Integer(fundSourceId))).getCode());
		if(functionaryId>0)
			headerdetails.put(VoucherConstant.FUNCTIONARYCODE, ((Functionary)persistenceService.find("from Functionary where id=?",new Integer(functionaryId))).getCode());
		return headerdetails;
	}

	/**
	 * @param string
	 * @param string2
	 * @param string3
	 * @param accId2
	 * @param isPaycheckOne
	 * @param dt2
	 * @return
	 */
	private InstrumentHeader updateInstrumentHeader(String ihId, String refNo, String amount, int accId2, String isPaycheck, Date dt2) {
		InstrumentHeader ih=instrumentService.instrumentHeaderService.find("from InstrumentHeader where id=?",Long.valueOf(ihId));
		ih.setInstrumentNumber(refNo);
		ih.setInstrumentDate(dt2);
		Bankaccount bankacc=(Bankaccount)instrumentService.persistenceService.find("from Bankaccount where id=?",accId2);
		ih.setBankAccountId(bankacc);
		ih.setIsPayCheque(isPaycheck);
		instrumentService.instrumentHeaderService.persist(ih);
		HibernateUtil.getCurrentSession().flush();
		return ih;
		
		
	
	}
	private InstrumentHeader depositInstrument(String ihId) {
		InstrumentHeader ih=instrumentService.instrumentHeaderService.find("from InstrumentHeader where id=?",Long.valueOf(ihId));
		EgwStatus status = (EgwStatus)persistenceService.find("from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);
		ih.setStatusId(status);
		instrumentService.instrumentHeaderService.persist(ih);
		InstrumentVoucher iv=new InstrumentVoucher();
		iv.setInstrumentHeaderId(ih);
		instrumentService.instrumentVouherService.persist(iv);
		InstrumentOtherDetails io=new InstrumentOtherDetails();
		io.setInstrumentStatusDate(ih.getInstrumentDate());
		io.setInstrumentHeaderId(ih);
		instrumentService.instrumentOtherDetailsService.persist(io);
		HibernateUtil.getCurrentSession().flush();
		return ih;
		
		
	
	}
	/**
	 * @param instrumentHeader
	 * @param voucherheaderId
	 */
	private void updateInstrumentReference(InstrumentHeader instrumentHeader, Long voucherheaderId) {
		CVoucherHeader voucherHeader =(CVoucherHeader) instrumentService.persistenceService.find("from CVoucherHeader where id=?",voucherheaderId);
		InstrumentVoucher iv = instrumentService.instrumentVouherService.find("from InstrumentVoucher where instrumentHeaderId=?",instrumentHeader);
		iv.setVoucherHeaderId(voucherHeader);
		instrumentService.instrumentVouherService.persist(iv);
		instrumentService.addToBankReconcilation(voucherHeader, instrumentHeader);
		HibernateUtil.getCurrentSession().flush();
	
	}
	/**
	 * @param form
	 */
	private List<InstrumentHeader>  addToInstrument(String refNo,String amount,Integer accId,String isPayChq,Date dt) throws ParseException {

		
		Map<String,Object> iMap=new HashMap<String,Object>();
		List<Map<String,Object>> iList=new ArrayList<Map<String,Object>>();
		
	   	iMap.put("Transaction number",refNo);
	  	iMap.put("Instrument amount",Double.valueOf(amount) );
		iMap.put("Instrument type", FinancialConstants.INSTRUMENT_TYPE_BANK);
		iMap.put("Bank account id",accId);
		iMap.put("Is pay cheque",isPayChq );
		iMap.put("Transaction date",dt);
		iMap.put("Status id", FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);
		iList.add(iMap);
		List<InstrumentHeader> instrumentList = instrumentService.addToInstrument(iList);
		HibernateUtil.getCurrentSession().flush();
		
		
		return instrumentList;
	}
	/**
	 * 
	 */
	private void setInstrumentRelatedServices() {
		instrumentService=new InstrumentService();
		persistenceService=new PersistenceService();
		persistenceService.setSessionFactory(new SessionFactory());
		instrumentService.setPersistenceService( persistenceService);
		
		PersistenceService<InstrumentHeader, Long> iHeaderService= new PersistenceService<InstrumentHeader, Long>();
		iHeaderService.setType(InstrumentHeader.class);
		iHeaderService.setSessionFactory(new SessionFactory());
		instrumentService.setInstrumentHeaderService(iHeaderService);
	
		PersistenceService<InstrumentType, Long> iTypeService= new PersistenceService<InstrumentType, Long>();
		iTypeService.setType(InstrumentType.class);
		iTypeService.setSessionFactory(new SessionFactory());
		instrumentService.setInstrumentTypeService(iTypeService);
		PersistenceService<InstrumentVoucher, Long> iVoucherService= new PersistenceService<InstrumentVoucher, Long>();
		iVoucherService.setType(InstrumentVoucher.class);
		iVoucherService.setSessionFactory(new SessionFactory());
		instrumentService.setInstrumentVouherService(iVoucherService);
		PersistenceService<Bankreconciliation, Integer> bankreconciliationService= new PersistenceService<Bankreconciliation, Integer>();
		bankreconciliationService.setType(Bankreconciliation.class);
		bankreconciliationService.setSessionFactory(new SessionFactory());
		instrumentService.setBankreconciliationService(bankreconciliationService);
		PersistenceService<InstrumentOtherDetails, Long> iOtherDetailsService= new PersistenceService<InstrumentOtherDetails, Long>();
		iOtherDetailsService.setType(InstrumentOtherDetails.class);
		iOtherDetailsService.setSessionFactory(new SessionFactory());
		instrumentService.setInstrumentOtherDetailsService(iOtherDetailsService);
	}
	
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	



}
