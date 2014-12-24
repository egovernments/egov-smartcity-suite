package org.egov.web.actions.payment;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVException;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.payment.ChequeAssignment;
import org.egov.model.recoveries.Recovery;
import org.egov.payment.client.BankAdviceForm;
import org.egov.services.cheque.ChequeService;
import org.egov.services.contra.ContraService;
import org.egov.services.instrument.InstrumentService;
import org.egov.services.payment.PaymentService;
import org.egov.services.recoveries.RecoveryService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.voucher.BaseVoucherAction;
import org.egov.web.actions.voucher.CommonAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.Validation;
@Results(value={
		@Result(name="bankAdvice-PDF",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=BandAdvice.pdf"}),
		@Result(name="bankAdvice-XLS",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=BandAdvice.xls"}),
		@Result(name="bankAdvice-HTML",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"text/html"})
	})

@ParentPackage("egov")
@Validation
public class ChequeAssignmentAction extends BaseVoucherAction 
{
	private static final String	SURRENDERSEARCH	= "surrendersearch";
	private String paymentMode,inFavourOf;
	private PaymentService paymentService;
	private Integer bankaccount,selectedRows=0,bankbranch; 
	private String bank_branch;
	private Integer department;
	public Map<String, String>						modeOfPaymentMap;
	private Date chequeDt;
	private boolean chequeNoGenerationAuto;
	private InstrumentService instrumentService;
	private List<ChequeAssignment> chequeAssignmentList;
	private List<InstrumentHeader> instHeaderList=null;
	List<InstrumentVoucher> instVoucherList;
	private static final Logger LOGGER = Logger.getLogger(ChequeAssignmentAction.class);
	private GenericHibernateDaoFactory genericDao;
	private static final String JASPER_PATH="/org/egov/payment/client/bankAdviceReport.jasper";
	InputStream inputStream;
	ReportHelper reportHelper;
	Map<String,Object> paramMap = new HashMap<String,Object>();
	List<Object> adviceList = new ArrayList<Object>();
	String fromDate;
	String toDate;
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	private List<InstrumentVoucher>	instrumentVoucherList;
	private List <InstrumentHeader>  instrumentHeaderList;
	private CommonAction common;
	String[] surrender;
	String instrumentNumber;
	private ContraService	contraService;
	Long recoveryId;
	
	private String button;
	String[] newInstrumentNumber;
	String[] newInstrumentDate;
	String[] surrendarReasons;
	private EgovCommon egovCommon;
	private ChequeService chequeService;
	String bank_account_dept;
	private String typeOfAccount;
	private Date currentDate;
	Map<String, String> bankBranchMap=Collections.EMPTY_MAP;
	Map<String, String> bankAccountMap=Collections.EMPTY_MAP;
	private RecoveryService recoveryService;
	Map<String, String> surrendarReasonMap=Collections.EMPTY_MAP;
	private boolean reassignSurrenderChq=false;  
	private  ScriptService scriptExecutionService;
	
	
	public boolean getReassignSurrenderChq() {
		return reassignSurrenderChq;
	}

	public void setReassignSurrenderChq(boolean reassignSurrenderChq) {
		this.reassignSurrenderChq = reassignSurrenderChq;
	}

	public Map<String, String> getSurrendarReasonMap() {
		return surrendarReasonMap;
	}

	public void setSurrendarReasonMap(Map<String, String> surrendarReasonMap) {
		this.surrendarReasonMap = surrendarReasonMap;
	}

	public void prepare()
	{
		super.prepare();
		addDropdownData("bankaccountList", Collections.EMPTY_LIST);
		chequeNoGenerationAuto = paymentService.isChequeNoGenerationAuto();
		typeOfAccount = FinancialConstants.TYPEOFACCOUNT_PAYMENTS+","+FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS;
		currentDate=new Date();
	}
	
	@SkipValidation
	public String beforeSearch()
	{
		paymentMode=FinancialConstants.MODEOFPAYMENT_CHEQUE;
		//typeOfAccount = FinancialConstants.TYPEOFACCOUNT_PAYMENTS+","+FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS;
		return "search";
	}
	public void prepareBeforeSearchForRemittance()
	{
		paymentMode=FinancialConstants.MODEOFPAYMENT_CASH;
	
	if( getSession().get("recoveryList")==null)
	{
		List<Recovery> listRecovery = recoveryService.getAllActiveTds();
		getSession().put("RecoveryList", listRecovery);
	}
		addDropdownData("recoveryList" ,(List) getSession().get("recoveryList"));
	}
	@SkipValidation
	public String beforeSearchForRemittance()
	{
		paymentMode=FinancialConstants.MODEOFPAYMENT_CASH;
		modeOfPaymentMap = new LinkedHashMap<String, String>();
		modeOfPaymentMap.put(FinancialConstants.MODEOFPAYMENT_CASH, getText("cash.consolidated.cheque"));                       
		List<Recovery> listRecovery = recoveryService.getAllActiveTds();
		LOGGER.debug("RemitRecoveryAction | Tds list size : "+ listRecovery.size());
		addDropdownData("recoveryList" , listRecovery);
		//typeOfAccount = FinancialConstants.TYPEOFACCOUNT_PAYMENTS+","+FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS;
		return "before_remittance_search";                                        
	}
	@ValidationErrorPage(value="search")
	public String search() throws EGOVException,ParseException
	{
		chequeAssignmentList = paymentService.getPaymentVoucherNotInInstrument(parameters,voucherHeader); 
		if(!paymentMode.equals(FinancialConstants.MODEOFPAYMENT_CHEQUE))
		{
			chequeDt = new Date();
			List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"cheque.assignment.infavourof");
			inFavourOf = appList.get(0).getValue();
		}
		loadBankAndAccount();
		LOGGER.info(""+reassignSurrenderChq);
		return "searchpayment";      
	}
	public void prepareSearchChequesOfRemittance()
	{
		beforeSearchForRemittance();
		mandatoryFields.add(Constants.RECOVERY);
	}
	
	@ValidationErrorPage(value="before_remittance_search")
	public String searchChequesOfRemittance() throws EGOVException,ParseException
	{
		Recovery recovery=(Recovery)persistenceService.find("from Recovery where id=?",recoveryId);
		voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
		voucherHeader.setName(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE);
		chequeAssignmentList = paymentService.getPaymentVoucherNotInInstrument(parameters,voucherHeader); 
		inFavourOf=recovery.getRemitted();
		loadBankAndAccount();
		return "searchremittance";      
	}
	private void loadBankAndAccount() {
		if(voucherHeader.getFundId()!=null)
		{
			common.setTypeOfAccount(typeOfAccount);
			common.setFundId(voucherHeader.getFundId().getId());
			common.setAsOnDate(currentDate);
			common.ajaxLoadBanksWithApprovedPayments();
			addDropdownData("bankbranchList", common.getBankBranchList());
			bankBranchMap=new LinkedHashMap<String, String>();
			for(Map mp:common.getBankBranchList())
			{
				bankBranchMap.put((String)mp.get("bankBranchId"),(String)mp.get("bankBranchName"));	
			}
			
			
		}
		if(getBankbranch()!=null) 
		{
			common.setTypeOfAccount(typeOfAccount);
			common.setFundId(voucherHeader.getFundId().getId());
			common.ajaxLoadBankAccounts();
			common.setBranchId(getBankbranch());			
			common.setAsOnDate(currentDate);
			common.ajaxLoadBankAccountsWithApprovedPayments();
			addDropdownData("bankaccountList",common.getAccNumList());
		}
	}
	@ValidationErrorPage(value="searchpayment")
	@SkipValidation
	public String createInstrument()throws EGOVException,ParseException
	{
	//	loadBankAndAccount();
		try
		{
			validateData();
			if(reassignSurrenderChq ||!isChequeNoGenerationAuto())
				validateDataForManual();
			if(getFieldErrors().isEmpty())
			{
				if(reassignSurrenderChq)
				{
				instHeaderList = paymentService.reassignInstrument(chequeAssignmentList, paymentMode,bankaccount, parameters,voucherHeader.getVouchermis().getDepartmentid());
				}
				else
				{
					instHeaderList = paymentService.createInstrument(chequeAssignmentList, paymentMode,bankaccount, parameters,voucherHeader.getVouchermis().getDepartmentid());	
				}
				selectedRows = paymentService.selectedRows;
				addActionMessage(getMessage("chq.assignment.transaction.success"));
				instVoucherList = paymentService.getInstVoucherList();
				//if(paymentMode.equals(FinancialConstants.MODEOFPAYMENT_RTGS))
				//	return "bankadvice";
			}
			else
				return "searchpayment";
		}catch(ValidationException e) 
		{  
			throw new ValidationException(e.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error(e.getMessage());
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exp",e.getMessage()));
			throw new ValidationException(errors);
		}
		
		return "view";
	}
	
	public void generateAdvice()throws Exception
	{
		BankAdviceForm baf;
		EntityType entity=null;
		String voucherno="";
		InstrumentHeader instrumentHeader = (InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?", Long.valueOf(parameters.get("instHeaderId")[0]));
		for (InstrumentVoucher instrumentVoucher : instrumentHeader.getInstrumentVouchers()) 
		{
			Object[] obj = (Object[])persistenceService.find(" select gld.detailTypeId,gld.detailKeyId,gld.amount from CGeneralLedgerDetail gld,CGeneralLedger gl where gl.id=gld.generalLedgerId and gl.voucherHeaderId=?",instrumentVoucher.getVoucherHeaderId());
			if(obj!=null)
			{
				entity =  paymentService.getEntity((Integer)obj[0], (Serializable)obj[1]);
				baf = new BankAdviceForm();
				baf.setContractorCode(entity.getCode());
				baf.setContractorName(entity.getName());
				baf.setBankName(entity.getBankname());
				baf.setBankAccountNo(entity.getBankaccount());
				baf.setIfscCode(entity.getIfsccode());
				baf.setNetAmount((BigDecimal)obj[2]);
				voucherno = voucherno+instrumentVoucher.getVoucherHeaderId().getVoucherNumber()+",";
				adviceList.add(baf);
			}
		}
		
		if(adviceList.size()!=0) 
		{
			voucherno = voucherno.substring(0,voucherno.length()-1);
			paramMap.put("chequeNo", instrumentHeader.getInstrumentNumber());
			paramMap.put("chequeDate", paymentService.formatter.format(instrumentHeader.getInstrumentDate()));
			paramMap.put("pymtVhNo", voucherno);
			paramMap.put("inFavourOf", instrumentHeader.getPayTo());
			paramMap.put("pymtBank", instrumentHeader.getBankAccountId().getBankbranch().getBank().getName()+"-"+instrumentHeader.getBankAccountId().getBankbranch().getBranchname());
			paramMap.put("pymtAcNo", instrumentHeader.getBankAccountId().getAccountnumber());
		}
	}
	public void validateData()
	{
		checkMandatory("vouchermis.departmentid",Constants.DEPARTMENT,voucherHeader.getVouchermis().getDepartmentid(),"voucher.department.mandatory");
	}
	@SkipValidation
	public String beforeSearchForSurrender()
	{
		addDropdownData("bankaccountList", Collections.EMPTY_LIST);
		loadBankAndAccounForSurender();
		return SURRENDERSEARCH;
	}
	
	@SkipValidation
	@ValidationErrorPage(value=SURRENDERSEARCH)
	public String searchChequesForSurrender()
	{
		
		validateForSuurenderSearch();
		if(getFieldErrors().size()>0)
		{
			if(bank_branch!=null && !bank_branch.equals("-1"))
				addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive='1' ",Integer.valueOf(bank_branch.split("-")[1])));
			loadReasonsForSurrendaring();	
			return beforeSearchForSurrender();	
		}
		
		StringBuffer sql =new StringBuffer();
		try {
			if(!"".equals(fromDate))
				sql.append(" and iv.voucherHeaderId.voucherDate>='"+sdf.format(formatter.parse(fromDate))+"' ");
			if(!"".equals(toDate))
				sql.append(" and iv.voucherHeaderId.voucherDate<='"+sdf.format(formatter.parse(toDate))+"'");
			if(bankaccount!=null && bankaccount!=-1)
				sql.append(" and  iv.instrumentHeaderId.bankAccountId.id="+bankaccount);
			if(instrumentNumber!=null && !instrumentNumber.isEmpty())
				sql.append(" and  iv.instrumentHeaderId.instrumentNumber="+instrumentNumber);
			if(department!=null && department!=-1 )
				{
				sql.append(" and  iv.voucherHeaderId.vouchermis.departmentid.id="+department);
				}
			if(voucherHeader.getVoucherNumber()!=null && !voucherHeader.getVoucherNumber().isEmpty() )
			{
			sql.append(" and  iv.voucherHeaderId.voucherNumber='"+voucherHeader.getVoucherNumber()+"'");
			}
			String mainquery = "select iv.instrumentHeaderId from  InstrumentVoucher iv  where   iv.voucherHeaderId.status=0  and iv.voucherHeaderId.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"'  "+sql+" "
				
			+" and iv.instrumentHeaderId.statusId.id in (?)  order by iv.voucherHeaderId.voucherDate";
			EgwStatus created = instrumentService.getStatusId(FinancialConstants.INSTRUMENT_CREATED_STATUS);
			instrumentHeaderList = persistenceService.findAllBy(mainquery,created.getId());
			LinkedHashSet  lhs=new LinkedHashSet();
			lhs.addAll(instrumentHeaderList);
			instrumentHeaderList.clear();
			instrumentHeaderList.addAll(lhs);
			instrumentVoucherList=new ArrayList<InstrumentVoucher>();
			for(InstrumentHeader ih:instrumentHeaderList)
			  {
				  instrumentVoucherList.addAll(ih.getInstrumentVouchers());
			  }  
			  getSession().put("instrumentVoucherList",instrumentVoucherList);
			  getSession().put("instrumentHeaderList", instrumentHeaderList);
			
			if(instrumentVoucherList.size()>0)
			{
				loadReasonsForSurrendaring();  
			}
			  
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError("Unparsable Date", "Unparsable Date")));
		}
		getheader();
		
		return "surrendercheques";
	}
	/**
	 * 
	 */
	private void loadReasonsForSurrendaring() {

		List<AppConfigValues> appConfigValuesList;
		surrendarReasonMap=new LinkedHashMap<String, String>();
		appConfigValuesList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF", "Reason For Cheque Surrendaring");
		for(AppConfigValues app:appConfigValuesList)
		{
			String value = app.getValue();
			if(app.getValue().indexOf('|')!=-1)
			{
				surrendarReasonMap.put(app.getValue(),value.substring(0, app.getValue().indexOf('|')));
			}
			else
			{
			surrendarReasonMap.put(app.getValue(),app.getValue());
			}
		}
		//return surrendarReasonMap;
	}

	/**
	 * 
	 */
	private void loadBankAndAccounForSurender() {
			common.setTypeOfAccount(typeOfAccount);
			common.ajaxLoadBankAccounts();
			common.setAsOnDate(currentDate);
			common.ajaxLoadBanksWithAssignedCheques();
			addDropdownData("bankbranchList",common.getBankBranchList());
			if(getBankbranch()!=null)
			{
			common.setBranchId(getBankbranch());
			common.ajaxLoadBankAccounts();  
			common.setAsOnDate(currentDate);
			common.ajaxLoadBanksAccountsWithAssignedCheques();
			addDropdownData("bankaccountList",common.getAccNumList());
			}
	}

	/**
	 * 
	 */
	private void getheader() {
		Bankaccount	account=(Bankaccount)persistenceService.find("from Bankaccount where id=?",bankaccount);
		bank_account_dept=account.getBankbranch().getBank().getName()+"-"+account.getBankbranch().getBranchname()
		+"-"+account.getAccountnumber();
		if(department!=null && department!=-1 )
		{
			DepartmentImpl dept=(DepartmentImpl)persistenceService.find("from DepartmentImpl where id=?",department);
			bank_account_dept=bank_account_dept+"-"+dept.getDeptName();
		}
	}
	@SkipValidation
	private void validateForSuurenderSearch() {
	
		if(bankaccount==null || bankaccount==-1)
		{
			addFieldError("bankaccount", getMessage("bankaccount.empty"));
			
		}
		if(bank_branch==null || bank_branch.equals("-1"))
		{
			addFieldError("bankbranch", getMessage("bankbranch.empty"));	
			
		}
		if(isFieldMandatory("department") && (null == department || department == -1)){
			addFieldError("department", getMessage("validate.department.null"));	
		}
		
		
	}
@ValidationErrorPage(value="surrendercheques")
	@SkipValidation
	public String surrenderCheques()
	{
	StringBuffer reasonMissingRows=new StringBuffer(50);  
	loadBankAndAccount();	
	loadReasonsForSurrendaring();
	try {
			
		if(surrender==null)
		{
			throw new ValidationException(Arrays.asList(new ValidationError("Exception while surrender Cheque ",
			"Please select the atleast one Cheque for Surrendering ")));	
		}
			List<InstrumentHeader> suurenderChequelist=new ArrayList<InstrumentHeader>();
			List<String> chequeNoList=new ArrayList<String>();
			List<Date> chequeDateList=new ArrayList<Date>();
			InstrumentHeader instrumentHeader=null;
			if(null == department || -1 == department){
				throw new ValidationException(Arrays.asList(new ValidationError("Exception while surrender Cheque ",
				"please select department")));
			}
			int j=0;
			instrumentHeaderList=(List<InstrumentHeader>)getSession().get("instrumentHeaderList");
			String[] newSurrender=new String[instrumentHeaderList.size()];
			for (InstrumentHeader iheader:instrumentHeaderList)
			{
					newSurrender[j]=null;
						
			 for(String ih:surrender)
				{
				if(ih.equalsIgnoreCase(iheader.getId().toString()))
				{
					newSurrender[j]=ih;
				}
				}
			 j++;
			}
			surrender=newSurrender;
			if(surrender!=null && surrender.length>0)
			{
			for( int i=0;i<surrender.length;i++)
			{   
				if(surrender[i]==null)
					instrumentHeader=null;
				else
					instrumentHeader=(InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?",Long.valueOf(surrender[i]));
				if(instrumentHeader!=null && (surrendarReasons[i]==null || surrendarReasons[i].equalsIgnoreCase("-1")))
				{
					reasonMissingRows.append(i+1);
					reasonMissingRows.append(",");
				}
				if(instrumentHeader!=null)  
				{
				instrumentHeader.setSurrendarReason(surrendarReasons[i]);
				suurenderChequelist.add(instrumentHeader);
				}
				
			}
			if(!reasonMissingRows.toString().isEmpty())
			{
				throw new ValidationException(Arrays.asList(new ValidationError("Exception while surrender Cheque ",
						"please select the Reason for Surrendering the cheque for selected  rows")));	
			}
			instrumentService.surrenderCheques(suurenderChequelist);
			if(button.equalsIgnoreCase("surrenderAndReassign"))
			{
			for( int i=0;i<surrender.length;i++)
			{
				if(surrender[i]!=null)
				{
					if(!isChequeNoGenerationAuto())
					{
						chequeNoList.add(newInstrumentNumber[i]);
						try {
							chequeDateList.add(formatter.parse(newInstrumentDate[i]));  
							
						} catch (ParseException e) {
							throw new ValidationException(Arrays.asList(new ValidationError("Exception while formatting ChequeDate ", "TRANSACTION_FAILED")));
						}
					}
					else
					{
						chequeNoList.add(getNewChequenumbers(instrumentHeader,department))	;
						chequeDateList.add(new Date());
						
					}
				}
				
			}
			
			
			 if(!isChequeNoGenerationAuto())
			 {
				 validateNewChequeNumbers(suurenderChequelist,chequeNoList,department);
				 if(getFieldErrors().size()>0)
				 {
					 throw new ValidationException(Arrays.asList(new ValidationError("TRANSACTION FAILED", "TRANSACTION FAILED")));
				 }
			 }
			//instrumentService.surrenderCheques(suurenderChequelist);
			 paymentMode="cheque";
			instHeaderList=addNewInstruments(suurenderChequelist,chequeNoList,chequeDateList);
			addActionMessage(getMessage("surrender.reassign.succesful"));
			}else
			{
				instHeaderList=suurenderChequelist;
				paymentMode="cheque";
				addActionMessage(getMessage("surrender.succesful"));	
			}
			
			}else {
				throw new ValidationException(Arrays.asList(new ValidationError("Exception while surrender ChequeDate ",
						"please select at least one cheque")));
			}
		} catch (ValidationException e) {
			instrumentVoucherList=(List<InstrumentVoucher>) getSession().get("instrumentVoucherList");
			instrumentHeaderList=(List<InstrumentHeader>)getSession().get("instrumentHeaderList");
			getheader();
			
			throw e;
		}
	
	return "view";
	}
	
	/**
	 * @param suurenderChequelist
	 * @param chequeNoList
	 */
	private String getNewChequenumbers(InstrumentHeader instrumentHeader,Integer department ) {
		/*int i=0;
		Integer dept=null;
		Set<InstrumentVoucher> instrumentVouchers = instrumentHeader.getInstrumentVouchers();
		for(InstrumentVoucher iv:instrumentVouchers)
			{
				dept=iv.getVoucherHeaderId().getVouchermis().getDepartmentid().getId();
				break;
			}*/
		return	chequeService.nextChequeNumber(instrumentHeader.getBankAccountId().getId().toString(), 1, department);
			
		
	}

	/**
	 * @param suurenderChequelist
	 */
	private List<InstrumentHeader> addNewInstruments(List<InstrumentHeader> suurenderChequelist, List<String> chequeNoList,List<Date> chequeDatelist)
	{
		Map<String,Object> instrumentVoucherMap=null;
		List <Map<String,Object>> instrumentVoucherList=new ArrayList<Map<String,Object>>();
		instHeaderList=new ArrayList<InstrumentHeader>();
		
			int i=0;
			for(InstrumentHeader instrumentHeader:suurenderChequelist)
			{
			InstrumentHeader newInstrumentHeader = instrumentHeader.clone();
			newInstrumentHeader.setInstrumentNumber(chequeNoList.get(i).toString());
			newInstrumentHeader.setStatusId(instrumentService.getStatusId(FinancialConstants.INSTRUMENT_CREATED_STATUS));
			newInstrumentHeader.setInstrumentDate((Date)chequeDatelist.get(i));
			i++;
			instHeaderList.add(instrumentService.instrumentHeaderService.persist(newInstrumentHeader));
			Set<InstrumentVoucher> instrumentVouchers = instrumentHeader.getInstrumentVouchers();
			
			for(InstrumentVoucher iv:instrumentVouchers)
			{
				instrumentVoucherMap=new HashMap<String,Object>();
				instrumentVoucherMap.put(VoucherConstant.VOUCHER_HEADER, iv.getVoucherHeaderId());
				instrumentVoucherMap.put(VoucherConstant.INSTRUMENT_HEADER, newInstrumentHeader);
				instrumentVoucherList.add(instrumentVoucherMap);
								
			}
			
			}
			instVoucherList = instrumentService.updateInstrumentVoucherReference(instrumentVoucherList);
			
		return instHeaderList;
	}

	

	private void validateNewChequeNumbers(List<InstrumentHeader> suurenderChequelist, List<String> chequeNoList,Integer department) {
	if(newInstrumentNumber!=null && newInstrumentNumber.length>0)
	{
		int i=0;
		Integer dept=null;
		InstrumentHeader instrumentHeader=null;
		for(i=0;i<suurenderChequelist.size();i++)
		{
			instrumentHeader= suurenderChequelist.get(i);
			/*Set<InstrumentVoucher> instrumentVouchers = instrumentHeader.getInstrumentVouchers();
			
			for(InstrumentVoucher iv:instrumentVouchers)
			{
				dept=iv.getVoucherHeaderId().getVouchermis().getDepartmentid().getId();
				if(dept!=null)
				{break;
				}
			}*/
			if(!instrumentService.isChequeNumberValid(chequeNoList.get(i),instrumentHeader.getBankAccountId().getId(),department))
					addFieldError("newInstrumentNumber["+i+"]",getMessage("payment.chequenumber.invalid")+" "+chequeNoList.get(i).toString());
			}
	}
	}

	public void validateDataForManual()
	{
		int i=0;
		Map<String,String> chqNoMap = new HashMap<String,String>();
		if(paymentMode.equals(FinancialConstants.MODEOFPAYMENT_CHEQUE))
		{
			for(ChequeAssignment assignment : chequeAssignmentList)
			{
				if(assignment.getIsSelected())
				{
					if(null ==assignment.getChequeNumber() || "".equals(assignment.getChequeNumber()))
					{
						addFieldError("chequeAssignmentList["+i+"].chequeNumber",getMessage("payment.chequeno.empty"));
						break;
					}
					else
					{
					if(reassignSurrenderChq)
					{
						if(!instrumentService.isReassigningChequeNumberValid(assignment.getChequeNumber(), bankaccount,voucherHeader.getVouchermis().getDepartmentid().getId()))
							addFieldError("chequeAssignmentList["+i+"].chequeNumber",getMessage("payment.chequenumber.invalid"));
					}
					else{
						if(!instrumentService.isChequeNumberValid(assignment.getChequeNumber(), bankaccount,voucherHeader.getVouchermis().getDepartmentid().getId()))
							addFieldError("chequeAssignmentList["+i+"].chequeNumber",getMessage("payment.chequenumber.invalid"));
					}
					}
					if(null ==assignment.getChequeDate())
					{
						addFieldError("chequeAssignmentList["+i+"].chequeNumber",getMessage("payment.chequeno.empty"));
						break;
					}
					if(chqNoMap.containsKey(assignment.getChequeNumber()))
					{
						if(!chqNoMap.get(assignment.getChequeNumber()).equals(assignment.getPaidTo())) // can't give the same cheque to different parties.
							addFieldError("chequeAssignmentList["+i+"].chequeNumber",getMessage("payment.duplicate.chequeno",new String[]{assignment.getChequeNumber(),chqNoMap.get(assignment.getChequeNumber()),assignment.getPaidTo()}));
					}
					else
						chqNoMap.put(assignment.getChequeNumber(),assignment.getPaidTo());
					i++;
				}
			}
		}
		else  // cash or RTGS
		{
			if(StringUtils.isEmpty(parameters.get("inFavourOf")[0] ))
				addFieldError("inFavourOf", getMessage("inFavourOf.is.empty"));
			if(StringUtils.isEmpty(parameters.get("chequeNo")[0] ))
				addFieldError("chequeNo", getMessage("payment.chequeno.empty"));
			else{
				if(reassignSurrenderChq)
				{
					if(!instrumentService.isReassigningChequeNumberValid(parameters.get("chequeNo")[0], bankaccount,voucherHeader.getVouchermis().getDepartmentid().getId()))
						addFieldError("chequeAssignmentList["+i+"].chequeNumber",getMessage("payment.chequenumber.invalid"));
				}else{
				if(!instrumentService.isChequeNumberValid(parameters.get("chequeNo")[0], bankaccount,voucherHeader.getVouchermis().getDepartmentid().getId()))
					addFieldError("chequeN0",getMessage("payment.chequenumber.invalid"));
				}
			}
			if(null==getChequeDt())
				addFieldError("chequeDt", getMessage("payment.chequedate.empty"));
		}
		
	}
	@SkipValidation
	public boolean validateUser(String purpose)throws ParseException
	{
		Script validScript = scriptExecutionService.findAllByNamedQuery(Script.BY_NAME,"Paymentheader.show.bankbalance").get(0);
		List<String> list = (List<String>) scriptExecutionService.executeScript(validScript,scriptExecutionService.createContext("persistenceService",paymentService,"purpose",purpose));
		if(list.get(0).equals("true"))
			return true;
		else 
			return false;
	}
	
	/*public String getAdminNumber(AbstractEstimate estimate) {
		String date = DateUtils.getFormattedDate(new Date(), "dd-MM-yyyy");
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "works.adminnumber.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), 
				ScriptService.createContext("estimate", estimate, "date", date, "sequenceGenerator", sequenceGenerator));
	}*/
	public void validate()
	{
		checkMandatory("fundId",Constants.FUND,voucherHeader.getFundId(),"voucher.fund.mandatory");
		checkMandatory("vouchermis.departmentid",Constants.DEPARTMENT,voucherHeader.getVouchermis().getDepartmentid(),"voucher.department.mandatory");
		checkMandatory("vouchermis.schemeid",Constants.SCHEME,voucherHeader.getVouchermis().getSchemeid(),"voucher.scheme.mandatory");
		checkMandatory("vouchermis.subschemeid",Constants.SUBSCHEME,voucherHeader.getVouchermis().getSubschemeid(),"voucher.subscheme.mandatory");
		checkMandatory("vouchermis.functionary",Constants.FUNCTIONARY,voucherHeader.getVouchermis().getFunctionary(),"voucher.functionary.mandatory");
		checkMandatory("fundsourceId",Constants.FUNDSOURCE,voucherHeader.getVouchermis().getFundsource(),"voucher.fundsource.mandatory");
		checkMandatory("vouchermis.divisionId",Constants.FIELD,voucherHeader.getVouchermis().getDivisionid(),"voucher.field.mandatory");
		checkMandatory("Recovery Code",Constants.RECOVERY,recoveryId,"recovery.mandatory");	
		if(getBankbranch()==null || getBankbranch()==-1)
			addFieldError("bankbranch", getMessage("bankbranch.empty"));
		if(getBankaccount()==null || getBankaccount()==-1)
			addFieldError("bankaccount", getMessage("bankaccount.empty"));
		loadBankAndAccount();
		
	}

	private void checkMandatory(String objectName,String fieldName,Object value,String errorKey)
	{
		if(mandatoryFields.contains(fieldName) && value == null)
			addFieldError(objectName, getMessage(errorKey));
	}
	@SkipValidation
	public String ajaxGenerateAdviceHtml() throws Exception{ 
		generateAdvice();
		inputStream = reportHelper.exportHtml(inputStream, JASPER_PATH, getParamMap(),getAdviceList(),"pt");
		return "bankAdvice-HTML";
	}
	@SkipValidation
	public String generateAdvicePdf() throws Exception{
		generateAdvice();
		inputStream = reportHelper.exportPdf(inputStream, JASPER_PATH, getParamMap(), getAdviceList());
		return "bankAdvice-PDF";
	}
	public Map<String, String> getBankBranchMap() {
		return bankBranchMap;
	}

	public void setBankBranchMap(Map<String, String> bankBranchMap) {
		this.bankBranchMap = bankBranchMap;
	}

	public Map<String, String> getBankAccountMap() {
		return bankAccountMap;
	}

	public void setBankAccountMap(Map<String, String> bankAccountMap) {
		this.bankAccountMap = bankAccountMap;
	}

	@SkipValidation
	public String generateAdviceXls() throws Exception{
		generateAdvice();
		inputStream = reportHelper.exportXls(inputStream, JASPER_PATH, getParamMap(), getAdviceList());
		return "bankAdvice-XLS";
	}
	protected String getMessage(String key) {
		return getText(key);
	}
	protected String getMessage(String key,String[] value) {
		return getText(key,value);
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	public Integer getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(Integer bankaccount) {
		this.bankaccount = bankaccount;
	}
	public List<InstrumentHeader> getInstHeaderList() {
		return instHeaderList;
	}
	public void setInstHeaderList(List<InstrumentHeader> instHeaderList) {
		this.instHeaderList = instHeaderList;
	}
	public ScriptService getScriptExecutionService() {
		return scriptExecutionService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

	public Integer getSelectedRows() {
		return selectedRows;
	}
	public void setSelectedRows(Integer selectedRows) {
		this.selectedRows = selectedRows;
	}
	public List<ChequeAssignment> getChequeAssignmentList() {
		return chequeAssignmentList;
	}
	public void setChequeAssignmentList(List<ChequeAssignment> chequeAssignmentList) {
		this.chequeAssignmentList = chequeAssignmentList;
	}
	public boolean isChequeNoGenerationAuto() {
		LOGGER.info(chequeNoGenerationAuto);
		return chequeNoGenerationAuto;
	}

	public void setChequeNoGenerationAuto(boolean chequeNoGenerationAuto) {
		this.chequeNoGenerationAuto = chequeNoGenerationAuto;
	}
	public void setInstrumentService(InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
	}

	public Integer getBankbranch() {
		return bankbranch;
	}

	public void setBankbranch(Integer bankbranch) {
		this.bankbranch = bankbranch;
	}

	public Date getChequeDt() {
		return chequeDt;
	}

	public void setChequeDt(Date chequeDt) {
		this.chequeDt = chequeDt;
	}

	public String getInFavourOf() {
		return inFavourOf;
	}

	public void setInFavourOf(String inFavourOf) {
		this.inFavourOf = inFavourOf;
	}
	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setReportHelper(final ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}

	public Map<String,Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String,Object> paramMap) {
		this.paramMap = paramMap;
	}

	public List<Object> getAdviceList() {
		return adviceList;
	}

	public List<InstrumentVoucher> getInstVoucherList() {
		return instVoucherList;
	}

	public void setInstVoucherList(List<InstrumentVoucher> instVoucherList) {
		this.instVoucherList = instVoucherList;
	}
	
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public List<InstrumentVoucher> getInstrumentVoucherList() {
		return instrumentVoucherList;
	}
	public void setInstrumentVoucherList(List<InstrumentVoucher> instrumentVoucherList) {
		this.instrumentVoucherList = instrumentVoucherList;
	}
	public String getInstrumentNumber() {
		return instrumentNumber;
	}
	public void setInstrumentNumber(String instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}
	public String[] getSurrender() {
		return surrender;
	}
	public void setSurrender(String[] surrender) {
		this.surrender = surrender;
	}
	public void setContraService(ContraService contraService) {
		this.contraService = contraService;
	}
	public String getBank_branch() {
		return bank_branch;
	}
	public void setBank_branch(String bank_branch) {
		this.bank_branch = bank_branch;
	}
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}
	public void setChequeService(ChequeService chequeService) {
		this.chequeService = chequeService;
	}
	public String[] getNewInstrumentNumber() {
		return newInstrumentNumber;
	}
		public void setNewInstrumentNumber(String[] newInstrumentNumber) {
		this.newInstrumentNumber = newInstrumentNumber;
	}
	public Integer getDepartment() {
			return department;
		}
	public void setDepartment(Integer department) {
			this.department = department;
		}
	public String getBank_account_dept() {
		return bank_account_dept;
	}
	public void setBank_account_dept(String bank_account_dept) {
		this.bank_account_dept = bank_account_dept;
	}
	public String[] getNewInstrumentDate() {
		return newInstrumentDate;
	}
	public void setNewInstrumentDate(String[] newInstrumentDate) {
		this.newInstrumentDate = newInstrumentDate;
	}

	public List<InstrumentHeader> getInstrumentHeaderList() {
		return instrumentHeaderList;
	}

	public void setInstrumentHeaderList(List<InstrumentHeader> instrumentHeaderList) {
		this.instrumentHeaderList = instrumentHeaderList;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public String getTypeOfAccount() {
		return typeOfAccount;
	}

	public void setTypeOfAccount(String typeOfAccount) {
		this.typeOfAccount = typeOfAccount;
	}
	public CommonAction getCommon() {
		return common;
	}

	public void setCommon(CommonAction commonAction) {
		this.common = commonAction;
	}
	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	public RecoveryService getRecoveryService() {
		return recoveryService;              
	}
	public void setRecoveryService(RecoveryService recoveryService) {
		this.recoveryService = recoveryService;
	}
	public Long getRecoveryId() {
		return recoveryId;
	}
	public void setRecoveryId(Long recoveryId) {
		this.recoveryId = recoveryId;
	}
	public Map<String, String> getModeOfPaymentMap() {
		return modeOfPaymentMap;
	}
	public void setModeOfPaymentMap(Map<String, String> modeOfPaymentMap) {
		this.modeOfPaymentMap = modeOfPaymentMap;
	}
	public String[] getSurrendarReasons() {
		return surrendarReasons;
	}

	public void setSurrendarReasons(String[] surrendarReasons) {
		this.surrendarReasons = surrendarReasons;
	}

}
