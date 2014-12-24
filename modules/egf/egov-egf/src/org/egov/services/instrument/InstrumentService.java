package org.egov.services.instrument;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankreconciliation;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.cheque.AccountCheques;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.instrument.InstrumentAccountCodes;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.egov.model.instrument.InstrumentType;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.log4j.Logger;


public class InstrumentService{

	public static final String STATUS_ID = "Status id";
	public static final String RECONCILED_AMOUNT = "Reconciled amount";
	public static final String INSTRUMENT_STATUS_DATE = "Instrument status date";
	public static final String PAYIN_SLIP_ID = "Payin slip id";
	public static final String VOUCHER_HEADER = "Voucher header";
	public static final String INSTRUMENT_HEADER = "Instrument header";
	public static final String BRANCH_NAME = "Bank branch name";
	public static final String PAYEE_NAME = "Payee name";
	public static final String PAY_TO = "Pay to";
	public static final String DETAIL_TYPE_ID = "Detail type id";
	public static final String DETAIL_KEY_ID = "Detail key id";
	public static final String BANK_CODE = "Bank code";
	public static final String INSTRUMENT_TYPE = "Instrument type";
	public static final String INSTRUMENT_AMOUNT = "Instrument amount";
	public static final String INSTRUMENT_DATE = "Instrument date";
	public static final String INSTRUMENT_NUMBER = "Instrument number";
	public static final String IS_NULL="is null";
	public static final String IS_PAYCHECK = "Is pay cheque";
	public static final String BANKACCOUNTID = "Bank account id";
	
	public static final String TRANSACTION_NUMBER = "Transaction number";
	public static final String TRANSACTION_DATE = "Transaction date";
	private static final Logger LOGGER =Logger.getLogger(InstrumentService.class);
	// Business methods
	/**
	 * Accepts the list of instruments and save the same to instrument object
	 * The values that needs to be passed are:<br><b>
	 * Instrument number, Instrument date, Instrument amount, Instrument type, payee name,Bank code,Bank account,Bank branch name,Is pay cheque </b>
	 * @param paramList
	 * for <I> receipts Is pay cheque will be '0' and for payments '1'  </I>
	 * @return List Of Saved Instrument Objects
	 * 1.check for receipt or payment with isPaycheque value
	 * 2.Find the instrument type and based on type check for  mandatory fields
	 * 3.set all non mandatory fields if they are provided
	 */

	public List<InstrumentHeader> addToInstrument(List<Map<String,Object>> paramList){
		List<InstrumentHeader> instrList = new ArrayList<InstrumentHeader>();
		InstrumentHeader instrHeader ;
		InstrumentType instrumentType ;
		for (Map<String, Object> instrMap : paramList) {
			instrHeader = new InstrumentHeader();
			
			//1.check for receipt or payment with isPaycheque value
			if(instrMap.get(IS_PAYCHECK)==null)
			{
				throw new IllegalArgumentException(IS_PAYCHECK+IS_NULL);
			}
			else if(!instrMap.get(IS_PAYCHECK).equals(FinancialConstants.IS_PAYCHECK_ZERO)&&!instrMap.get(IS_PAYCHECK).equals(FinancialConstants.IS_PAYCHECK_ONE))
			{
				throw new EGOVRuntimeException("value for "+IS_PAYCHECK+"should be either"+FinancialConstants.IS_PAYCHECK_ZERO+"or "+FinancialConstants.IS_PAYCHECK_ONE);
				
			}else
			{
				instrHeader.setIsPayCheque(instrMap.get(IS_PAYCHECK).toString());
			}
			
			
			//2.Find the instrument type and based on type check for  mandatory fields
			if(instrMap.get(INSTRUMENT_TYPE)==null )
			{
				throw new IllegalArgumentException(INSTRUMENT_TYPE+IS_NULL);	
			}
			else
			{
				instrumentType= getInstrumentTypeByType(instrMap.get(INSTRUMENT_TYPE).toString());
				if(instrumentType==null)
				{
					throw new EGOVRuntimeException(INSTRUMENT_TYPE+"'"+instrMap.get(INSTRUMENT_TYPE).toString()+"' is not defined in the system ");
				}
				else
				{
					instrHeader.setInstrumentType(instrumentType);
				}
				
			}
			if(instrMap.get(INSTRUMENT_AMOUNT)==null)
			{
				throw new IllegalArgumentException(INSTRUMENT_AMOUNT+IS_NULL);
			}
			else
			{
				try {
					instrHeader.setInstrumentAmount(new BigDecimal((instrMap.get(INSTRUMENT_AMOUNT).toString())));
				} catch (NumberFormatException e) {
					throw new EGOVRuntimeException(INSTRUMENT_AMOUNT+"is not numeric");
				}
				
			}
			
			if(instrumentType.getType().equals(FinancialConstants.INSTRUMENT_TYPE_CHEQUE) || instrumentType.getType().equals(FinancialConstants.INSTRUMENT_TYPE_DD))
			{
				
				if(instrMap.get(INSTRUMENT_NUMBER)==null )
				{
					throw new IllegalArgumentException(INSTRUMENT_NUMBER+IS_NULL);
				}
				else
				{	
					instrHeader.setInstrumentNumber(instrMap.get(INSTRUMENT_NUMBER).toString());
				}
				if(instrMap.get(INSTRUMENT_DATE)==null)
				{
					throw new IllegalArgumentException(INSTRUMENT_DATE+IS_NULL);
				}
				else if(new Date().compareTo((Date)instrMap.get(INSTRUMENT_DATE))==-1)
				{
					throw new IllegalArgumentException(INSTRUMENT_DATE+"cannot be future Date");
				}
				else
				{
					instrHeader.setInstrumentDate((Date)instrMap.get(INSTRUMENT_DATE));
				}
				//if it is type of cheque And DD add bank code
				if(instrMap.get(BANK_CODE)!=null)
				{
					Bank bank = getBank(instrMap.get(BANK_CODE).toString());
					if(bank==null)
					{
						throw new EGOVRuntimeException(BANK_CODE+"'"+instrMap.get(BANK_CODE).toString()+"' is not defined in the system ");
					}
					else
					{
						instrHeader.setBankId(bank);
					}
				}
				else
				{
					throw new EGOVRuntimeException(BANK_CODE+IS_NULL);
				}
				
				// applicable for payment
				if(instrMap.get(BANKACCOUNTID)!=null)
				{
					Bankaccount bankaccount = getBankaccount(instrMap.get(BANKACCOUNTID).toString());
					if(bankaccount==null)
					{
						throw new EGOVRuntimeException(BANKACCOUNTID+"'"+instrMap.get(BANKACCOUNTID).toString()+"' is not defined in the system ");
					}
					else
					{
						instrHeader.setBankAccountId(bankaccount);
					}
				}
			}
			/**
			 * Transaction Number,transaction date is mandatory
			 */
			else if(instrumentType.getType().equalsIgnoreCase(FinancialConstants.INSTRUMENT_TYPE_CARD)||instrumentType.getType().equalsIgnoreCase(FinancialConstants.INSTRUMENT_TYPE_ONLINE))
			{
				if(instrMap.get(TRANSACTION_NUMBER)!=null)
				{
					instrHeader.setTransactionNumber(instrMap.get(TRANSACTION_NUMBER).toString());
				}else
				{
					throw new IllegalArgumentException(TRANSACTION_NUMBER+IS_NULL);
				}
				if(instrMap.get(TRANSACTION_DATE)==null)
					{
						throw new IllegalArgumentException(TRANSACTION_DATE+IS_NULL);
					}
					else if(new Date().compareTo((Date)instrMap.get(TRANSACTION_DATE))==-1)
					{
						throw new IllegalArgumentException(TRANSACTION_DATE+"cannot be future Date");
					}
					else
					{
						instrHeader.setTransactionDate((Date)instrMap.get(TRANSACTION_DATE));
					}
				if(instrMap.get(INSTRUMENT_NUMBER)!=null)
				{
					instrHeader.setInstrumentNumber((String)instrMap.get(INSTRUMENT_NUMBER));
				}
			
			}
			/**
			 * Transaction Number,transaction date, bankaccountid is mandatory
			 */
			else if(instrumentType.getType().equalsIgnoreCase(FinancialConstants.INSTRUMENT_TYPE_ADVICE))
			{
				if(instrMap.get(TRANSACTION_NUMBER)!=null)
				{
					instrHeader.setTransactionNumber(instrMap.get(TRANSACTION_NUMBER).toString());
				}
				else
				{
					throw new IllegalArgumentException(TRANSACTION_NUMBER+IS_NULL);
				}
				if(instrMap.get(TRANSACTION_DATE)==null)
				{
					throw new IllegalArgumentException(TRANSACTION_DATE+IS_NULL);
				}
				else if(new Date().compareTo((Date)instrMap.get(TRANSACTION_DATE))==-1)
				{
					throw new IllegalArgumentException(TRANSACTION_DATE+"cannot be future Date");
				}
				else
				{
					instrHeader.setTransactionDate((Date)instrMap.get(TRANSACTION_DATE));
				}
				if(instrMap.get(BANKACCOUNTID)==null)
				{
					throw new IllegalArgumentException(BANKACCOUNTID+IS_NULL);
				}
				if(instrMap.get(BANKACCOUNTID)!=null)
				{
					Bankaccount bankaccount = getBankaccount(instrMap.get(BANKACCOUNTID).toString());
					if(bankaccount==null)
					{
						throw new EGOVRuntimeException(BANKACCOUNTID+"'"+instrMap.get(BANKACCOUNTID).toString()+"' is not defined in the system ");
					}
					else
					{
						instrHeader.setBankAccountId(bankaccount);
					}
				}	
				
			}
			/**
			 * Transaction Number,transaction date, bankaccountid is mandatory
			 */
			if(instrumentType.getType().equalsIgnoreCase(FinancialConstants.INSTRUMENT_TYPE_BANK))
			{
				if(instrMap.get(TRANSACTION_NUMBER)!=null)
				{
					instrHeader.setTransactionNumber(instrMap.get(TRANSACTION_NUMBER).toString());
				}
				else
				{
					throw new IllegalArgumentException(TRANSACTION_NUMBER+IS_NULL);
				}
				if(instrMap.get(TRANSACTION_DATE)==null)
				{
					throw new IllegalArgumentException(TRANSACTION_DATE+IS_NULL);
				}
				else if(new Date().compareTo((Date)instrMap.get(TRANSACTION_DATE))==-1)
				{
					throw new IllegalArgumentException(TRANSACTION_DATE+"cannot be future Date");
				}
				else
				{
					instrHeader.setTransactionDate((Date)instrMap.get(TRANSACTION_DATE));
				}
				if(instrMap.get(BANKACCOUNTID)==null)
				{
					throw new IllegalArgumentException(BANKACCOUNTID+IS_NULL);
				}
				if(instrMap.get(BANKACCOUNTID)!=null)
				{
					Bankaccount bankaccount = getBankaccount(instrMap.get(BANKACCOUNTID).toString());
					if(bankaccount==null)
					{
						throw new EGOVRuntimeException(BANKACCOUNTID+"'"+instrMap.get(BANKACCOUNTID).toString()+"' is not defined in the system ");
					}
					else
					{
						instrHeader.setBankAccountId(bankaccount);
					}
				}
			}
			if(instrumentType.getType().equalsIgnoreCase(FinancialConstants.INSTRUMENT_TYPE_CASH)){
				
				if(instrMap.get(BANKACCOUNTID)!=null)
				{
					Bankaccount bankaccount = getBankaccount(instrMap.get(BANKACCOUNTID).toString());
					if(bankaccount==null)
					{
						throw new EGOVRuntimeException(BANKACCOUNTID+"'"+instrMap.get(BANKACCOUNTID).toString()+"' is not defined in the system ");
					}
					else
					{
						instrHeader.setBankAccountId(bankaccount);
					}
				}
				if(instrMap.get(BANK_CODE)!=null)
				{
					Bank bank = getBank(instrMap.get(BANK_CODE).toString());
					if(bank==null)
					{
						throw new EGOVRuntimeException(BANK_CODE+"'"+instrMap.get(BANK_CODE).toString()+"' is not defined in the system ");
					}
					else
					{
						instrHeader.setBankId(bank);
					}
				}
				if(instrMap.get(TRANSACTION_NUMBER)!=null)
				{
					instrHeader.setTransactionNumber(instrMap.get(TRANSACTION_NUMBER).toString());
				}
				
				if(instrMap.get(TRANSACTION_DATE)!=null)
				{
					 if(new Date().compareTo((Date)instrMap.get(TRANSACTION_DATE))==-1)
						{
							throw new IllegalArgumentException(TRANSACTION_DATE+"cannot be future Date");
						}
						else
						{
							instrHeader.setTransactionDate((Date)instrMap.get(TRANSACTION_DATE));
						}	
				}
				
				
			}
			//set all non mandatory fields
			instrHeader.setPayee((instrMap.get(PAYEE_NAME)!=null ?instrMap.get(PAYEE_NAME).toString():null));
			instrHeader.setPayTo((instrMap.get(PAY_TO)!=null ?instrMap.get(PAY_TO).toString():null));
			if(instrMap.get(DETAIL_TYPE_ID)!=null)
			{
				Accountdetailtype detailType = (Accountdetailtype) persistenceService.find("from Accountdetailtype where id=?",Integer.parseInt(instrMap.get(DETAIL_TYPE_ID).toString()));
				instrHeader.setDetailTypeId(detailType);
			}
			instrHeader.setDetailKeyId((instrMap.get(DETAIL_KEY_ID)!=null ?(Long)instrMap.get(DETAIL_KEY_ID):null));
			instrHeader.setBankBranchName(instrMap.get(BRANCH_NAME)!=null? instrMap.get(BRANCH_NAME).toString():null);
			EgwStatus status = (EgwStatus)persistenceService.find("from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper('"+FinancialConstants.INSTRUMENT_CREATED_STATUS+"')");
			LOGGER.info("Created Status of Instrument"+status.getDescription());
			instrHeader.setStatusId(status);
			//in Transaction Number is present then Transaction Date is Mandatory
			instrHeader.setCreatedDate(new Date());
			instrumentHeaderService.persist(instrHeader);
			InstrumentOtherDetails io=new InstrumentOtherDetails();
			io.setInstrumentHeaderId(instrHeader);
			if(instrHeader.getTransactionDate()!=null)
			{
				io.setInstrumentStatusDate(instrHeader.getTransactionDate());
			}
			else 
			{
				io.setInstrumentStatusDate(instrHeader.getInstrumentDate());
			}
			instrumentOtherDetailsService.persist(io);
			instrList.add(instrHeader);
		}
		return instrList;
	}
	
	/**
	 * Accepts List of Instrument and Voucher objects and updates the same in InstrumentVoucher Object .<br>
	 * List of Values that can be Passed are<br>
	 * <B>Instrument header -InstrumentHeader Object(Mandatory), 
	 * Voucher header - Voucher Header Object (Mandatory) </B><br>
	 *  @param paramList
	 * @return List<InstrumentVoucher> 
	 */
	public List<InstrumentVoucher> updateInstrumentVoucherReference(List<Map<String,Object>> paramList)
	{
		List<InstrumentVoucher> iVouherList=new ArrayList<InstrumentVoucher>();
		for(Map<String,Object> iVoucherMap:paramList)
		{
			InstrumentVoucher iVoucher=new InstrumentVoucher();
			if(iVoucherMap.get(INSTRUMENT_HEADER)==null)
			{
			throw new EGOVRuntimeException(INSTRUMENT_HEADER+IS_NULL);
			}
			else
			{
			iVoucher.setInstrumentHeaderId((InstrumentHeader)iVoucherMap.get(INSTRUMENT_HEADER));
			}
			if(iVoucherMap.get(VOUCHER_HEADER)==null)
			{
				throw new EGOVRuntimeException(VOUCHER_HEADER+IS_NULL);
			}
			else
			{
				iVoucher.setVoucherHeaderId((CVoucherHeader)iVoucherMap.get(VOUCHER_HEADER));
			}
			
		instrumentVouherService.persist(iVoucher);
		if(iVoucher.getInstrumentHeaderId().getIsPayCheque().equals(FinancialConstants.IS_PAYCHECK_ONE)||
				iVoucher.getInstrumentHeaderId().getIsPayCheque().equals(FinancialConstants.IS_PAYCHECK_ZERO)&&iVoucher.getInstrumentHeaderId().getInstrumentType().getType().equals(FinancialConstants.INSTRUMENT_TYPE_BANK))
					{
			addToBankReconcilation(iVoucher.getVoucherHeaderId(),iVoucher.getInstrumentHeaderId());
			}
		
			iVouherList.add(iVoucher);
		}

		return iVouherList;

	}
	
	public List<InstrumentVoucher> modifyInstrumentVoucher(List<Map<String,Object>> paramList)
	{
		List<InstrumentVoucher> iVouherList=new ArrayList<InstrumentVoucher>();
		for(Map<String,Object> iVoucherMap:paramList)
		{
			InstrumentVoucher iVoucher=new InstrumentVoucher();
			if(iVoucherMap.get(INSTRUMENT_HEADER)==null)
			{
			throw new EGOVRuntimeException(INSTRUMENT_HEADER+IS_NULL);
			}
			else
			{
			iVoucher.setInstrumentHeaderId((InstrumentHeader)iVoucherMap.get(INSTRUMENT_HEADER));
			}
			if(iVoucherMap.get(VOUCHER_HEADER)==null)
			{
				throw new EGOVRuntimeException(VOUCHER_HEADER+IS_NULL);
			}
			else
			{
				iVoucher.setVoucherHeaderId((CVoucherHeader)iVoucherMap.get(VOUCHER_HEADER));
			}
			instrumentVouherService.update(iVoucher);
			iVouherList.add(iVoucher);
		}

		return iVouherList;

	}
	
	
	/**
	 * populates following fields in Bankreconciliation<br>
	 * <B>bankaccount,chequenumber,chequedate,amount,voucherheader,isreconciled,transactiontype,type<B><br>
	 * @param vouherHeader
	 * @param instrumentHeader
	 * @return Bankreconciliation
	 * @throws EGOVRuntimeException
	 */
	public  Bankreconciliation addToBankReconcilation(CVoucherHeader vouherHeader,InstrumentHeader instrumentHeader) throws EGOVRuntimeException {
		
		if(vouherHeader==null)
		{
			throw new IllegalArgumentException("voucherHeader"+IS_NULL);
		}
		if(instrumentHeader==null)
		{
			throw new IllegalArgumentException("instrumentHeader"+IS_NULL);
		}
		Bankreconciliation bankreconciliation=new Bankreconciliation();
		InstrumentOtherDetails iOtherdetails;
	    iOtherdetails = instrumentOtherDetailsService.find("from InstrumentOtherDetails where instrumentHeaderId=?",instrumentHeader);
		if(iOtherdetails==null)
		{
			 iOtherdetails=new InstrumentOtherDetails();
		}
		
		if(instrumentHeader.getInstrumentType().getType().equalsIgnoreCase(FinancialConstants.INSTRUMENT_TYPE_BANK))
		{
	
		iOtherdetails.setReconciledAmount(instrumentHeader.getInstrumentAmount());
		iOtherdetails.setInstrumentStatusDate(instrumentHeader.getInstrumentDate());
		EgwStatus status = (EgwStatus)persistenceService.find("from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
		instrumentHeader.setStatusId(status);
		}
		else
		{
		bankreconciliation.setBankaccount(instrumentHeader.getBankAccountId());
		iOtherdetails.setInstrumentStatusDate(instrumentHeader.getInstrumentDate());
		//EgwStatus status = (EgwStatus)persistenceService.find("from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);
		//instrumentHeader.setStatusId(status);
		}
		if(instrumentHeader.getIsPayCheque()==null)
		{
			throw new EGOVRuntimeException(IS_PAYCHECK +IS_NULL +" in Instrument Header cannot update Transactiontype in BankReconciliation ");
		}
		else if(instrumentHeader.getIsPayCheque().equals(FinancialConstants.IS_PAYCHECK_ONE))
		{
			bankreconciliation.setTransactiontype("Cr");
		}
		else if(instrumentHeader.getIsPayCheque().equals(FinancialConstants.IS_PAYCHECK_ZERO))
		{
			bankreconciliation.setTransactiontype("Dr");
		}
		bankreconciliation.setInstrumentHeaderId(instrumentHeader.getId());
		bankreconciliation.setAmount(instrumentHeader.getInstrumentAmount());
		instrumentOtherDetailsService.persist(iOtherdetails);
		bankreconciliationService.persist(bankreconciliation);
		return bankreconciliation;
		 
	}
	/**
	 * Accepts list of following Objects in a Map and updates them into InstrumnetOtherdetails and statusid in InstrumentHeader.
	 * accepted values are<br>
	 * <b>
	 * Instrument Header,payinSlipId,instrumentStatusDate,reconciledAmount,statusId,BankAccount</b><br>
	 * status values are<br>
	 * <b>New,Deposited,Reconciled,Dishonored,Cancelled</b><br>
	 * Based on status instrumentStatusDate will have deposited date,reconciled date and dishonored date.
	 * @param paramList
	 * @return
	 */

	public List<InstrumentOtherDetails> updateInstrumentOtherDetails(List<Map<String,Object>> paramList)throws EGOVRuntimeException
	{
		InstrumentHeader iHeader=null;
		InstrumentOtherDetails iOtherDetails=null;
		List<InstrumentOtherDetails> iOtherDetailsList=new ArrayList<InstrumentOtherDetails>();
		for(Map<String,Object> iOtherDetailsMap:paramList)
		{
			
			
			if(iOtherDetailsMap.get(INSTRUMENT_HEADER)==null)
			{
				throw new EGOVRuntimeException(INSTRUMENT_HEADER+IS_NULL);
			}
			else
			{
				iHeader=(InstrumentHeader)iOtherDetailsMap.get(INSTRUMENT_HEADER);
				iOtherDetails = instrumentOtherDetailsService.find("from InstrumentOtherDetails where instrumentHeaderId=?",iHeader);
				if(iOtherDetails==null)
				{
				iOtherDetails=new InstrumentOtherDetails();
				iOtherDetails.setInstrumentHeaderId(iHeader);
				}
				if(iOtherDetailsMap.get(PAYIN_SLIP_ID)==null)
				{
					LOGGER.debug("PayinSlip Id is null");
					//throw new EGOVRuntimeException(PAYIN_SLIP_ID+IS_NULL);
				}
				else
				{
					iOtherDetails.setPayinslipId((CVoucherHeader)(iOtherDetailsMap.get(PAYIN_SLIP_ID)));
				}

				if(iOtherDetailsMap.get(INSTRUMENT_STATUS_DATE)!=null)
				{
					new java.sql.Date(((Date)iOtherDetailsMap.get(INSTRUMENT_STATUS_DATE)).getTime());
					iOtherDetails.setInstrumentStatusDate(new java.sql.Date(((Date)iOtherDetailsMap.get(INSTRUMENT_STATUS_DATE)).getTime()));
				} 
				if(iOtherDetailsMap.get(RECONCILED_AMOUNT)!=null)
				{
					iOtherDetails.setReconciledAmount((BigDecimal)(iOtherDetailsMap.get(RECONCILED_AMOUNT)));
				} 
				if(iOtherDetailsMap.get(STATUS_ID)==null)
				{
					throw new EGOVRuntimeException("Required Object Status is not Found in the Map ");

				} 
				else
				{
					EgwStatus status = (EgwStatus)persistenceService.find("from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",iOtherDetailsMap.get(STATUS_ID));
					iHeader.setStatusId(status);	
				}
				if(iOtherDetailsMap.get(TRANSACTION_NUMBER)!=null)
				{
					iHeader.setTransactionNumber((String)iOtherDetailsMap.get(TRANSACTION_NUMBER));
				}
				if(iOtherDetailsMap.get(TRANSACTION_DATE)!=null)
				{
					iHeader.setTransactionDate((Date)iOtherDetailsMap.get(TRANSACTION_DATE));
				}
				if(iOtherDetailsMap.get(BANKACCOUNTID)!=null)
				{
					iHeader.setBankAccountId((Bankaccount)iOtherDetailsMap.get(BANKACCOUNTID));
				}
			}
			instrumentOtherDetailsService.persist(iOtherDetails);
			instrumentHeaderService.persist(iHeader);
			iOtherDetailsList.add(iOtherDetails);
		}

		return iOtherDetailsList;
	}

	public boolean cancelInstrument(InstrumentHeader ih)throws EGOVRuntimeException
	{
		boolean result=false;
		try {
			String cancelStatusQuiery="from EgwStatus where upper(moduletype)=upper('instrument') and  upper(description)=upper('"+FinancialConstants.INSTRUMENT_CANCELLED_STATUS+"')";
			EgwStatus cancelStatus=(EgwStatus)persistenceService.find(cancelStatusQuiery);
			ih.setStatusId(cancelStatus);
			instrumentHeaderService.update(ih);
			result=true;
		} catch (HibernateException e) {
			throw new EGOVRuntimeException(e.getMessage());
		}
		catch (Exception e) {
			throw new EGOVRuntimeException(e.getMessage());
		}
		return result;
	}
	
	
		
	private Bank getBank(String bankCode){
			return (Bank)persistenceService.find("from Bank where code=?",bankCode);
	}

	private Bankaccount getBankaccount(String bankAccountId) {
		return (Bankaccount)persistenceService.find("from Bankaccount where id=?", Integer.valueOf(bankAccountId));
		
	}
	
	/**
	 * returns list of InstrumentVouchers  reconciled from reconcilationFromDate  to reconcilationToDate
	 * to get list of InstrumentVouchers  reconciled on a perticular date pass both as same Date
	 * @param reconcilationFromDate('dd/MM/yyyy'),reconcilationToDate('dd/MM/yyyy')
	 * @return List of InstrumentVouchers
	 */
	public List<InstrumentVoucher> getReconciledCheques(Date reconcilationFromDate,Date reconcilationToDate)throws EGOVRuntimeException
	{
		if(reconcilationFromDate == null  || reconcilationToDate == null)
		{
			throw new EGOVRuntimeException("reconcilationFromDate and reconcilationToDate should not be null");
		}
		Query qry=persistenceService.getSession().createQuery("select iv from InstrumentVoucher iv inner join iv.instrumentHeaderId as  ih   where ih.statusId.description=:status"
				+" and ih in (select iih from InstrumentOtherDetails io inner join io.instrumentHeaderId as iih where io.instrumentStatusDate>=:startDate and io.instrumentStatusDate<=:endDate )");
		qry.setString("status", FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
		qry.setDate("startDate", reconcilationFromDate);
		qry.setDate("endDate", reconcilationToDate);
		return  qry.list();
	}
	/**
	 * returns List of InstrumentVouhcers dishonored from dishonoredFromDate to  dishonoredToDate
	 * to get list of InstrumentVouchers  dishonored on a perticular date pass both as same Date
	 * @param dishonoredFromDate,dishonoredToDate
	 * @return  List of InstrumentVouchers
	 */
	public List<InstrumentVoucher> getBouncedCheques(Date dishonoredFromDate,Date dishonoredToDate) throws EGOVRuntimeException
	{
		if(dishonoredFromDate == null  || dishonoredToDate == null)
		{
			throw new EGOVRuntimeException("dishonoredFromDate and dishonoredToDate should not be null");
		}	
		Query qry=persistenceService.getSession().createQuery("select iv from InstrumentVoucher iv inner join iv.instrumentHeaderId as  ih   where ih.statusId.description=:status"
				+" and ih in (select iih from InstrumentOtherDetails io inner join io.instrumentHeaderId as iih where io.instrumentStatusDate>=:startDate and io.instrumentStatusDate<=:endDate )");
		qry.setString("status", FinancialConstants.INSTRUMENT_DISHONORED_STATUS);
		qry.setDate("startDate",dishonoredFromDate);
		qry.setDate("endDate", dishonoredToDate);
		return qry.list();
	}

	
	public InstrumentType getInstrumentTypeById(Long id)throws EGOVRuntimeException
	{
		InstrumentType iType=null;
		if(id==null)
		{
			throw new EGOVRuntimeException(INSTRUMENT_TYPE+" id "+IS_NULL);
		}
		else
		{
			iType=instrumentTypeService.findById(id,false);
		}
		return iType;
	}

	public InstrumentType getInstrumentTypeByType(String type)	throws EGOVRuntimeException
	{
		InstrumentType iType=null;
		if(type==null)
		{
			throw new EGOVRuntimeException(INSTRUMENT_TYPE+IS_NULL);
		}
		else if(type.isEmpty())
		{
			throw new EGOVRuntimeException(INSTRUMENT_TYPE+"is empty");
		}
		else
		{
			String qry="";
			try {
				qry = "from InstrumentType  where type=? and isActive=1";
				iType=instrumentTypeService.find(qry,type);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		return iType;
	}
	
	
	public EgwStatus getStatusId(String statusString)
	{
		String statusQury="from EgwStatus where upper(moduletype)=upper('instrument') and  upper(description)=upper('"+statusString+"')";
		EgwStatus egwStatus=(EgwStatus)persistenceService.find(statusQury);
		return egwStatus;
		
	}
	public InstrumentHeader getInstrumentHeader(Integer bankaccountId,String instrumentNo,String payTo)
	{
		return instrumentHeaderService.find(" from InstrumentHeader where bankAccountId.id=? and instrumentNumber=? and payTo=? ", bankaccountId,instrumentNo,payTo);
	}

	//persisters
	public InstrumentType createInstrumentType(InstrumentType iType )
	{
		instrumentTypeService.persist(iType);
		return iType;
	}

	public InstrumentAccountCodes createInstrumentAccountCodes(InstrumentAccountCodes iAccCodes)
	{
		instrumentAccountCodesService.persist(iAccCodes);
		return iAccCodes;
	}
	//setters for Spring injection

	public void setInstrumentHeaderService(
			PersistenceService<InstrumentHeader, Long> instrumentHeaderService) {
		this.instrumentHeaderService = instrumentHeaderService;
	}
	public void setInstrumentVouherService(PersistenceService<InstrumentVoucher, Long> instrumentVouherService) {
		this.instrumentVouherService = instrumentVouherService;
	}
	public void setInstrumentAccountCodesService(PersistenceService<InstrumentAccountCodes, Long> instrumentAccountCodesService) {
		this.instrumentAccountCodesService = instrumentAccountCodesService;
	}
	public void setInstrumentTypeService(PersistenceService<InstrumentType, Long> instrumentTypeService) {
		this.instrumentTypeService = instrumentTypeService;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	public void setInstrumentOtherDetailsService(PersistenceService<InstrumentOtherDetails, Long> instrumentOtherDetailsService) {
		this.instrumentOtherDetailsService = instrumentOtherDetailsService;
	}
	
	public void setBankreconciliationService(PersistenceService<Bankreconciliation, Integer> bankreconciliationService) {
		this.bankreconciliationService = bankreconciliationService;
	}
	
	public boolean isChequeNumberWithinRange(String chequeNumber,Integer bankAccountId,Integer departmentId){
		AccountCheques accountCheques = (AccountCheques) persistenceService.find("select ac from AccountCheques ac, ChequeDeptMapping cd where ac.id = cd.accountCheque.id and " +
				" ac.bankAccountId.id=? and cd.allotedTo.id=? and to_number(?) between ac.fromChequeNumber and ac.toChequeNumber ", bankAccountId,departmentId,chequeNumber);
		if(accountCheques==null)
			return false;
		return true;
	}
	boolean isChequeNumberUnique(String chequeNumber,Integer bankAccountId){
		InstrumentType instrumentType = getInstrumentTypeByType("cheque");
		List<InstrumentHeader> list = instrumentHeaderService.findAllBy("from InstrumentHeader where instrumentNumber=? and instrumentType.id=? and bankAccountId.id=?", chequeNumber,instrumentType.getId(),bankAccountId);
		if(list!=null && list.size()>0)
			return false;
		return true;
	}
	
	boolean isChequeIsSurrenderdForReassign(String chequeNumber,Integer bankAccountId){
		InstrumentType instrumentType = getInstrumentTypeByType("cheque");
		List<InstrumentHeader> list = instrumentHeaderService.findAllBy("from InstrumentHeader where instrumentNumber=? and instrumentType.id=? and bankAccountId.id=? and statusId in (?)", chequeNumber,instrumentType.getId(),bankAccountId,getStatusId(FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS));
		if(list!=null && list.size()>0)
			return true;
		return false;
	}
	public boolean isChequeNumberValid(String chequeNumber,Integer bankAccountId,Integer departmentId){
		if(!isChequeNumberWithinRange(chequeNumber,bankAccountId,departmentId))
			return false;
		if(!isChequeNumberUnique(chequeNumber,bankAccountId))
			return false;
		return true;
	}
	//fields
	public PersistenceService<InstrumentHeader, Long> instrumentHeaderService ;
	public PersistenceService<InstrumentVoucher, Long> instrumentVouherService;
	public PersistenceService<InstrumentAccountCodes, Long> instrumentAccountCodesService;
	public PersistenceService<InstrumentType, Long> instrumentTypeService;    
	public PersistenceService<InstrumentOtherDetails, Long> instrumentOtherDetailsService;
	public PersistenceService<Bankreconciliation, Integer> bankreconciliationService;
	public PersistenceService persistenceService;
	/**
	 * 
	 */
	public void setServicesForLegacyClasses() {
		
		PersistenceService<InstrumentHeader, Long> iHeaderService= new PersistenceService<InstrumentHeader, Long>();
		iHeaderService.setType(InstrumentHeader.class);
		iHeaderService.setSessionFactory(new SessionFactory());
		this.setInstrumentHeaderService(iHeaderService);
		PersistenceService<InstrumentVoucher, Long> iVoucherService= new PersistenceService<InstrumentVoucher, Long>();
		iVoucherService.setType(InstrumentVoucher.class);
		iVoucherService.setSessionFactory(new SessionFactory());
		this.setInstrumentVouherService(iVoucherService);
		PersistenceService<InstrumentOtherDetails, Long> iOtherDetailsService= new PersistenceService<InstrumentOtherDetails, Long>();
		iOtherDetailsService.setType(InstrumentOtherDetails.class);
		iOtherDetailsService.setSessionFactory(new SessionFactory());
		this.setInstrumentOtherDetailsService(iOtherDetailsService);
		PersistenceService persistenceService=new PersistenceService();
		persistenceService.setSessionFactory(new SessionFactory());
		this.setPersistenceService(persistenceService);
		PersistenceService<InstrumentType, Long> iTypeService= new PersistenceService<InstrumentType, Long>();
		iTypeService.setType(InstrumentType.class);
		iTypeService.setSessionFactory(new SessionFactory());
		this.setInstrumentTypeService(iTypeService);
		PersistenceService<InstrumentAccountCodes, Long> iAccountCodeService= new PersistenceService<InstrumentAccountCodes, Long>();
		iAccountCodeService.setType(InstrumentAccountCodes.class);
		iAccountCodeService.setSessionFactory(new SessionFactory());
		this.setInstrumentAccountCodesService(iAccountCodeService);
		PersistenceService<Bankreconciliation, Integer> bankreconciliationService= new PersistenceService<Bankreconciliation, Integer>();
		bankreconciliationService.setType(Bankreconciliation.class);
		bankreconciliationService.setSessionFactory(new SessionFactory());
		this.setBankreconciliationService(bankreconciliationService);
		
	}

	/**
	 * @param vhId
	 */
	public void unDeposit(Long payinslipId) {
	 InstrumentOtherDetails iOtherdetails = instrumentOtherDetailsService.find("from InstrumentOtherDetails  io where payinslipId.id=?",payinslipId);
	 InstrumentHeader iHeader = iOtherdetails.getInstrumentHeaderId();
	 iHeader.setStatusId(getStatusId(FinancialConstants.INSTRUMENT_CREATED_STATUS));
	 instrumentHeaderService.persist(iHeader);
	 iOtherdetails.setPayinslipId(null);
	 instrumentOtherDetailsService.persist(iOtherdetails);
	}

	/**
	 * @param suurenderChequelist
	 */
	public void surrenderCheques(List<InstrumentHeader> suurenderChequelist) {
	
	for(InstrumentHeader instrumentHeader:suurenderChequelist)
	{
		String reason=instrumentHeader.getSurrendarReason();
		//when reason is there 
		if(reason!=null)
		{
		//if it contain reassign=Y OR N remove it and save
			if(  reason.indexOf('|')!=-1 )  
			{	
				if(reason.substring(reason.indexOf('|')+1,(reason.length())).equalsIgnoreCase("Y"))
				{
					instrumentHeader.setStatusId(getStatusId(FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS));
					instrumentHeader.setSurrendarReason(reason.substring(0,reason.indexOf('|')));
				}
				else
				{
					instrumentHeader.setSurrendarReason(reason.substring(0,reason.indexOf('|')));
					instrumentHeader.setStatusId(getStatusId(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS));
				}
			}
			else
			{
				instrumentHeader.setStatusId(getStatusId(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS));
			}
		}else
		{
			instrumentHeader.setStatusId(getStatusId(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS));
		}
		InstrumentOtherDetails instrumentOtherDetails = instrumentOtherDetailsService.find("from InstrumentOtherDetails where instrumentHeaderId=?",instrumentHeader);
		if(instrumentOtherDetails!=null)
		{
			instrumentOtherDetails.setInstrumentStatusDate(new Date());
		}
		else
		{
			instrumentOtherDetails=new InstrumentOtherDetails();
			instrumentOtherDetails.setInstrumentHeaderId(instrumentHeader);
			instrumentOtherDetails.setInstrumentStatusDate(new Date());
		}
		instrumentHeaderService.persist(instrumentHeader);
		instrumentOtherDetailsService.persist(instrumentOtherDetails);
	}
		
	}

	public void updateInstrumentOtherDetailsStatus(InstrumentHeader instrumentHeader,Date statusDate,BigDecimal reconciledAmount) {
		InstrumentOtherDetails instrumentOtherDetails = instrumentOtherDetailsService.find("from InstrumentOtherDetails where instrumentHeaderId.id=?",instrumentHeader.getId());
		instrumentOtherDetails.setInstrumentStatusDate(statusDate);
		instrumentOtherDetails.setReconciledAmount(reconciledAmount);
		instrumentOtherDetailsService.persist(instrumentOtherDetails);
	}
	
	public void editInstruments(InstrumentOtherDetails instrumentOtherDetails){
	
			InstrumentHeader instrumentHeader = instrumentOtherDetails.getInstrumentHeaderId();
			instrumentHeader.setStatusId(getStatusId(FinancialConstants.INSTRUMENT_CREATED_STATUS));
			instrumentHeaderService.update(instrumentHeader);
			instrumentOtherDetailsService.delete(instrumentOtherDetails);
			Bankreconciliation bankreconciliation=(Bankreconciliation) persistenceService.find("from Bankreconciliation where instrumentHeaderId=?", instrumentHeader.getId());
			bankreconciliationService.delete(bankreconciliation);
			ContraJournalVoucher contraJournalVoucher=(ContraJournalVoucher) persistenceService.find("from ContraJournalVoucher where instrumentHeaderId=?", instrumentHeader);
			persistenceService.delete(contraJournalVoucher);
	
	}

	/**
	 * @param chequeNumber
	 * @param bankaccountId
	 * @param departmentId
	 * @return
	 */
	public boolean isReassigningChequeNumberValid(String chequeNumber, Integer bankAccountId, Integer departmentId) {
		if(!isChequeNumberWithinRange(chequeNumber,bankAccountId,departmentId))
			return false;
		if(!isChequeIsSurrenderdForReassign(chequeNumber,bankAccountId))
			return false;
		return true;
	}
}
