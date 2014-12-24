/**
 * 
 */
package org.egov.web.actions.payment;  

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Relation;
import org.egov.commons.Vouchermis;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.payment.Paymentheader;
import org.egov.model.voucher.CommonBean;
import org.egov.model.voucher.VoucherDetails;
import org.egov.pims.commons.DesignationMaster;
import org.egov.services.contra.ContraService;
import org.egov.services.payment.PaymentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.HibernateException;
import org.hibernate.jdbc.Work;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * @author mani
 */

public class DirectBankPaymentAction extends BasePaymentAction {
	private static final String						FAILED_WHILE_REVERSING		= "Failed while Reversing";
	private static final String						FAILED						= "Transaction failed";
	private static final String						EXCEPTION_WHILE_SAVING_DATA	= "Exception while saving data";
	private static final long						serialVersionUID			= 1L;
	private PaymentService							paymentService;
	private static final String						DD_MMM_YYYY					= "dd-MMM-yyyy";
	public Map<String, String>						modeOfPaymentMap;
	private static final String						MDP_CHEQUE					= FinancialConstants.MODEOFPAYMENT_CHEQUE;
	private static final String						MDP_RTGS					= FinancialConstants.MODEOFPAYMENT_RTGS;
	private static final String						MDP_CASH					= FinancialConstants.MODEOFPAYMENT_CASH;
	private String									button;
	private VoucherService							voucherService;
	private SimpleWorkflowService<Paymentheader>	paymentWorkflowService;
	private static final Logger		LOGGER		= Logger.getLogger(DirectBankPaymentAction.class);
	public static final String		ZERO		= "0";
	private static final String		VIEW		= "view";
	private static final String		REVERSE		= "reverse";
	private static final String		REQUIRED	= "required";
	private static final String		PAYMENTID	= "paymentid";
	private List<VoucherDetails>	billDetailslist;
	private List<VoucherDetails>	subLedgerlist;
	boolean							showChequeNumber;
	private CommonBean				commonBean;
	private EgovCommon egovCommon;
	
	private Paymentheader			paymentheader;
	public boolean					showApprove	= false;
	private ScriptService scriptExecutionService;
	public ScriptService getScriptExecutionService() {
		return scriptExecutionService;
	}


	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}


	private Integer departmentId;
	private String wfitemstate;
	private String typeOfAccount;
	private List<InstrumentHeader> instrumentHeaderList;
	private BigDecimal balance;
	


	public BigDecimal getBalance() {
		return balance;
	}


	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}


	@Override
	public Object getModel() {
		voucherHeader = (CVoucherHeader) super.getModel();
		return voucherHeader;
		
	}
	
	
	@Override
	public void prepare() {  
		super.prepare();
		voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
		voucherHeader.setName(FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK);
		modeOfPaymentMap = new LinkedHashMap<String, String>();
		modeOfPaymentMap.put(MDP_CHEQUE, getText(MDP_CHEQUE));
		modeOfPaymentMap.put(MDP_CASH, getText(MDP_CASH));
		modeOfPaymentMap.put(MDP_RTGS, getText(MDP_RTGS));
		
		addDropdownData("designationList", Collections.EMPTY_LIST);
		addDropdownData("userList", Collections.EMPTY_LIST);
		typeOfAccount = FinancialConstants.TYPEOFACCOUNT_PAYMENTS+","+FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS;
	}
	
	public void prepareNewform() {
		addDropdownData("bankList", Collections.EMPTY_LIST);
		addDropdownData("accNumList", Collections.EMPTY_LIST);
		
	}
	
	@SkipValidation
	public String newform() {
		LOGGER.info("Resetting all........................... ");
		voucherHeader.reset();
		commonBean.reset();
		commonBean.setModeOfPayment(MDP_CHEQUE);
		voucherHeader.setVouchermis(new Vouchermis());
		voucherHeader.getVouchermis().setDepartmentid((DepartmentImpl)paymentService.getAssignment().getDeptId());
		billDetailslist = new ArrayList<VoucherDetails>();
		billDetailslist.add(new VoucherDetails());
		subLedgerlist = new ArrayList<VoucherDetails>();
		subLedgerlist.add(new VoucherDetails());
		loadDefalutDates();
		loadApproverUser(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
		return NEW;
	}
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "fundId", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "voucherNumber", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "commonBean.bankId", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "commonBean.accountNumberId", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "commonBean.amount", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "voucherDate", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "commonBean.documentNumber", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "commonBean.documentDate", message = "", key = REQUIRED),
			@RequiredFieldValidator(fieldName = "commonBean.paidTo", message = "", key = REQUIRED) })
	@SkipValidation
	@ValidationErrorPage(value=NEW)
	public String create() {
		CVoucherHeader billVhId=null;
		voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
		loadAjaxedDropDowns();
		removeEmptyRowsAccoutDetail(billDetailslist);
		removeEmptyRowsSubledger(subLedgerlist);
		
		try {
			if (!validateDBPData(billDetailslist, subLedgerlist)) {
				if (commonBean.getModeOfPayment().equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS)) {
					LOGGER.info("calling Validate RTGS");
					validateRTGS();
				}
				  
				if(showMode!=null && showMode.equalsIgnoreCase("nonbillPayment"))
				{
				if(	voucherHeader.getId()!=null)
					billVhId=(CVoucherHeader)persistenceService.find("from CVoucherHeader where id=?",voucherHeader.getId());
					voucherHeader.setId(null);
				}
				voucherHeader = createVoucherAndledger();
				paymentheader = paymentService.createPaymentHeader(voucherHeader, Integer.valueOf(commonBean.getAccountNumberId()), commonBean
						.getModeOfPayment(), commonBean.getAmount());
				createMiscBillDetail(billVhId);
				paymentWorkflowService.start(paymentheader, paymentService.getPosition(), "");
				sendForApproval();
				addActionMessage(getText("directbankpayment.transaction.success") + voucherHeader.getVoucherNumber()) ;
			}
			else
			{
				throw  new ValidationException(Arrays.asList(new ValidationError("engine.validation.failed", "Validation Faild")));
			}  
		} catch (NumberFormatException e) {
			LOGGER.error(e.getMessage(),e);
			throw e;
		} catch (EGOVRuntimeException e) {
			LOGGER.error(e.getMessage(),e);
			throw e;
			
		}
		finally{
		if (subLedgerlist.size() == 0) {
			subLedgerlist.add(new VoucherDetails());
		}
		loadApproverUser(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
		}
		return VIEW;
	}

	//@ValidationErrorPage(value="/error/error,jsp")
	
public void prepareNonBillPayment()
	{
		addDropdownData("bankList", Collections.EMPTY_LIST);
		addDropdownData("accNumList", Collections.EMPTY_LIST);
		commonBean.setModeOfPayment(MDP_CHEQUE);
	}
@ValidationErrorPage(value=NEW)  
@SkipValidation
public String nonBillPayment()  
{
	voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", voucherHeader.getId());
	//String bankGlcode = paymentheader.getBankaccount().getChartofaccounts().getGlcode();
	List<VoucherDetails>  debitAndNonpaybleCreditsList=new ArrayList<VoucherDetails>();
	//VoucherDetails bankdetail = null;
	Map<String, Object> vhInfoMap = voucherService.getVoucherInfo(voucherHeader.getId());
	
	// voucherHeader =
	// (CVoucherHeader)vhInfoMap.get(Constants.VOUCHERHEADER);
	String vName=voucherHeader.getName();
	String appconfigKey="";
	if(vName.equalsIgnoreCase(FinancialConstants.JOURNALVOUCHER_NAME_CONTRACTORJOURNAL))
	{
		appconfigKey="worksBillPurposeIds";
	}
	else if(vName.equalsIgnoreCase(FinancialConstants.JOURNALVOUCHER_NAME_SUPPLIERJOURNAL))
	{
		appconfigKey="purchaseBillPurposeIds";
	}
	else if(vName.equalsIgnoreCase(FinancialConstants.JOURNALVOUCHER_NAME_SALARYJOURNAL))
	{
		appconfigKey="salaryBillPurposeIds";
	}
	AppConfigValues appConfigValues = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,appconfigKey).get(0);
	String purposeValue = appConfigValues.getValue();
	CGeneralLedger netPay = (CGeneralLedger)persistenceService.find("from CGeneralLedger where voucherHeaderId.id=? and glcodeId.purposeId=?",voucherHeader.getId(),purposeValue);
	if(netPay==null)
	{
		throw new ValidationException(Arrays.asList(new ValidationError("net.payable.not.selected.or.selected.wrongly","Either Net payable code is not selected or wrongly selected in voucher .Payment creation Failed")));
	}
	billDetailslist=new ArrayList<VoucherDetails>();
	subLedgerlist=new ArrayList<VoucherDetails>();
	VoucherDetails vd =new VoucherDetails();
	vd.setGlcodeDetail(netPay.getGlcode());
	vd.setGlcodeIdDetail(netPay.getGlcodeId().getId());
	vd.setAccounthead(netPay.getGlcodeId().getName());
	vd.setDebitAmountDetail(BigDecimal.valueOf(netPay.getCreditAmount()));
	if(netPay.getFunctionId()!=null)
		{
		vd.setFunctionIdDetail(Long.valueOf(netPay.getFunctionId()));
		String function =(String) persistenceService.find("select name from CFunction where id=?",Long.valueOf(netPay.getFunctionId()));
		vd.setFunctionDetail(function);
		}
	commonBean.setAmount(BigDecimal.valueOf(netPay.getCreditAmount()));
	billDetailslist.add(vd);
	Set<CGeneralLedgerDetail> generalLedgerDetails = netPay.getGeneralLedgerDetails();
	int i=0;
	for(CGeneralLedgerDetail gldetail:generalLedgerDetails)
	{
		VoucherDetails vdetails=new VoucherDetails();
		vdetails.setSubledgerCode(netPay.getGlcode());
		vdetails.setAmount(gldetail.getAmount());    
		//vdetails.setDebitAmountDetail(vdetails.getAmount());
		vdetails.setGlcodeDetail(netPay.getGlcode());
		vdetails.setGlcode(netPay.getGlcodeId());
		vdetails.setSubledgerCode(netPay.getGlcode());
		vdetails.setAccounthead(netPay.getGlcodeId().getName());
			Accountdetailtype detailType =(Accountdetailtype) persistenceService.find("from Accountdetailtype where id=?",gldetail.getDetailTypeId());
		vdetails.setDetailTypeName(detailType.getName());
		vdetails.setDetailType(detailType);
		vdetails.setDetailKey(gldetail.getDetailKeyId().toString());
		vdetails.setDetailKeyId(gldetail.getDetailKeyId());
		
		String table=detailType.getFullQualifiedName();
		Class<?> service;
		try {
			service = Class.forName(table);
		} catch (ClassNotFoundException e1) {
			throw new ValidationException(Arrays.asList(new ValidationError("application.error","application.error")));
		}
		String simpleName = service.getSimpleName();
		String tableName=simpleName;
		//simpleName=simpleName.toLowerCase()+"Service";
		simpleName = simpleName.substring(0,1).toLowerCase()+simpleName.substring(1)+"Service";
		WebApplicationContext wac= WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
		PersistenceService entityPersistenceService=(PersistenceService)wac.getBean(simpleName);
		String dataType = "";
		try {
			Class aClass = Class.forName(table);
			java.lang.reflect.Method method = aClass.getMethod("getId");
			dataType = method.getReturnType().getSimpleName();
		} catch (Exception e) {
			throw new EGOVRuntimeException(e.getMessage());
		}
		EntityType entity = null;
		if ( dataType.equals("Long") ){
			entity=(EntityType)entityPersistenceService.findById(Long.valueOf(
					gldetail.getDetailKeyId().toString()),false);
		}else{
			entity=(EntityType)entityPersistenceService.findById(gldetail.getDetailKeyId(),false);
		}
		vdetails.setDetailCode(entity.getCode());
		vdetails.setDetailName(entity.getName());
		vdetails.setDetailKey(entity.getName());
		if(i==0)
		{
			commonBean.setPaidTo(entity.getName());
		}
		subLedgerlist.add(vdetails);
		

	}
	if (subLedgerlist.size() == 0) {
		subLedgerlist.add(new VoucherDetails());  
	}
	loadAjaxedDropDowns();
	loadApproverUser(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
	return NEW;
}
	
	private void validateRTGS() {
		{
			EntityType entity = null;
			List<ValidationError> errors = new ArrayList<ValidationError>();
			Relation rel = null;
			String type = null;
			//handle null 
			if (subLedgerlist != null && !subLedgerlist.isEmpty() ) {
				for (VoucherDetails voucherDetail : subLedgerlist) {
					try {
						entity = paymentService.getEntity(voucherDetail.getDetailType().getId(), (Serializable) voucherDetail.getDetailKeyId());
					if(entity==null)
					{
						throw new ValidationException(Arrays.asList(new ValidationError("No.entity.for.detailkey", "There is no entity defined for"+ voucherDetail.getDetailCode(),new String [] {voucherDetail.getDetailCode()})));
					}
					} catch (EGOVException e) {
						throw new ValidationException(Arrays.asList(new ValidationError("Exception to get EntityType  ", e.getMessage())));
					}
					voucherDetail.setDetailType((Accountdetailtype) persistenceService.find("from Accountdetailtype where id=?", voucherDetail.getDetailType()
							.getId()));
					if (voucherDetail.getDetailType().getName().equalsIgnoreCase("creditor")) {
						rel = (Relation) entity;
						type = rel.getRelationtype().getName();
					}
					if (type.equalsIgnoreCase("Contractor")
							&& (StringUtils.isBlank(entity.getPanno()) || StringUtils.isBlank(entity.getBankname())
									|| StringUtils.isBlank(entity.getBankaccount()) || StringUtils.isBlank(entity.getIfsccode()))) {
						LOGGER.error("BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for " + entity.getName());
						errors.add(new ValidationError("paymentMode", "BankName, BankAccount,IFSC Code, Pan number is mandatory for RTGS Payment for "
								+ entity.getName()));
						throw new ValidationException(errors);
					}
					if (type.equalsIgnoreCase("Supplier")
							&& (StringUtils.isBlank(entity.getTinno()) || StringUtils.isBlank(entity.getBankname())
									|| StringUtils.isBlank(entity.getBankaccount()) || StringUtils.isBlank(entity.getIfsccode()))) {
						LOGGER.error("BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for " + entity.getName());
						errors.add(new ValidationError("paymentMode", "BankName, BankAccount,IFSC Code, Tin number is mandatory for RTGS Payment for "
								+ entity.getName()));
						throw new ValidationException(errors);
					}
				}
			}else
			{
				throw new ValidationException(Arrays.asList(new ValidationError("no.subledger.cannot.create.rtgs.payment", "There is no subledger selected cannot create RTGS Payment")));
			}
		}
		
	}
	
	private void createMiscBillDetail(CVoucherHeader billVhId) {
		Miscbilldetail miscbillDetail = new Miscbilldetail();
		miscbillDetail.setBillnumber(commonBean.getDocumentNumber());
		miscbillDetail.setBilldate(commonBean.getDocumentDate());
		miscbillDetail.setBillamount(commonBean.getAmount());
		miscbillDetail.setPaidamount(commonBean.getAmount());
		miscbillDetail.setPassedamount(commonBean.getAmount());
		miscbillDetail.setPayVoucherHeader(voucherHeader);
		miscbillDetail.setBillVoucherHeader(billVhId);
		miscbillDetail.setPaidto(commonBean.getPaidTo());
		persistenceService.setType(Miscbilldetail.class);
		persistenceService.persist(miscbillDetail);
		
	}
	
	private void updateMiscBillDetail() {
		Miscbilldetail miscbillDetail = (Miscbilldetail) persistenceService.find(" from Miscbilldetail where payVoucherHeader=?", voucherHeader);
		miscbillDetail.setBillnumber(commonBean.getDocumentNumber());
		miscbillDetail.setBilldate(commonBean.getDocumentDate());
		miscbillDetail.setBillamount(commonBean.getAmount());
		miscbillDetail.setPayVoucherHeader(voucherHeader);
		miscbillDetail.setPaidamount(commonBean.getAmount());
		miscbillDetail.setPassedamount(commonBean.getAmount());
		miscbillDetail.setPaidamount(commonBean.getAmount());
		miscbillDetail.setPaidto(commonBean.getPaidTo());
		persistenceService.setType(Miscbilldetail.class);
		persistenceService.persist(miscbillDetail);
	}
	
	@SkipValidation
	public String beforeView() {
		prepareForViewModifyReverse();
		wfitemstate = "END"; // requird to hide the approver drop down when view is form source
		return VIEW;
	}
	
	@SkipValidation
	public String beforeEdit() {
		prepareForViewModifyReverse();
		loadApproverUser(voucherHeader.getType());
		return EDIT;
	}
	
	@SkipValidation
	public String beforeReverse() {  
		prepareForViewModifyReverse();
		return REVERSE;
	}
	
	@SuppressWarnings("unchecked")
	private void prepareForViewModifyReverse() {
		voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", voucherHeader.getId());
		paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where voucherheader=?", voucherHeader);
		commonBean.setAmount(paymentheader.getPaymentAmount());
		commonBean.setAccountNumberId(paymentheader.getBankaccount().getId().toString());
		commonBean.setAccnumnar(paymentheader.getBankaccount().getNarration());
		
		String bankBranchId = paymentheader.getBankaccount().getBankbranch().getBank().getId() + "-" + paymentheader.getBankaccount().getBankbranch().getId();
		commonBean.setBankId(bankBranchId);
		commonBean.setModeOfPayment(paymentheader.getType());
		Miscbilldetail miscbillDetail = (Miscbilldetail) persistenceService.find(" from Miscbilldetail where payVoucherHeader=?", voucherHeader);
		commonBean.setDocumentNumber(miscbillDetail.getBillnumber());
		commonBean.setDocumentDate(miscbillDetail.getBilldate());
		commonBean.setPaidTo(miscbillDetail.getPaidto());
		String bankGlcode = paymentheader.getBankaccount().getChartofaccounts().getGlcode();
		VoucherDetails bankdetail = null;
		Map<String, Object> vhInfoMap = voucherService.getVoucherInfo(voucherHeader.getId());
		
		// voucherHeader =
		// (CVoucherHeader)vhInfoMap.get(Constants.VOUCHERHEADER);
		billDetailslist = (List<VoucherDetails>) vhInfoMap.get(Constants.GLDEATILLIST);
		subLedgerlist = (List<VoucherDetails>) vhInfoMap.get("subLedgerDetail");
		
		for (VoucherDetails vd : billDetailslist) {
			if (vd.getGlcodeDetail().equalsIgnoreCase(bankGlcode)) {
				bankdetail = vd;
			}
		}
		if (bankdetail != null) {
			billDetailslist.remove(bankdetail);
		}
		instrumentHeaderList = getPersistenceService().findAllBy(" from InstrumentHeader ih where ih.id in (select iv.instrumentHeaderId.id from InstrumentVoucher iv where iv.voucherHeaderId.id=?) order by instrumentNumber", paymentheader.getVoucherheader().getId());
		loadAjaxedDropDowns();
		//find it last so that rest of the data loaded
		if(!"view".equalsIgnoreCase(showMode))
		{
		validateUser("balancecheck");
		}
	}
	
	private void loadAjaxedDropDowns() {
		loadSchemeSubscheme();
		loadBankBranchForFund();
		loadBankAccountNumber(commonBean.getBankId());
		loadFundSource();
	}
	
	@SuppressWarnings("deprecation")
	public String edit() throws SQLException {
		voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
		removeEmptyRowsAccoutDetail(billDetailslist);
		removeEmptyRowsSubledger(subLedgerlist);
		validateFields();
		voucherHeader = voucherService.updateVoucherHeader(voucherHeader);
		
		try {
			if (!validateDBPData(billDetailslist, subLedgerlist)) {
				if (commonBean.getModeOfPayment().equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS)) {
					validateRTGS();
				}
				
				reCreateLedger();
				paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where voucherheader=?", voucherHeader);
				paymentService.updatePaymentHeader(paymentheader, voucherHeader, Integer.valueOf(commonBean.getAccountNumberId()), commonBean
						.getModeOfPayment(), commonBean.getAmount());
				updateMiscBillDetail();
				sendForApproval();
				addActionMessage(getText("directbankpayment.transaction.success") + voucherHeader.getVoucherNumber());
			}
			else
			{
				throw  new ValidationException(Arrays.asList(new ValidationError("engine.validation.failed", "Validation Faild")));
			}
		} catch (NumberFormatException e) {
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		}

		finally {
			if (subLedgerlist.size() == 0) {
				subLedgerlist.add(new VoucherDetails());
			}
			loadAjaxedDropDowns();
			loadApproverUser(voucherHeader.getType());
		}
		
		return VIEW;
	}
	
	@ValidationErrorPage("reverse")
	public String reverse() {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		final CreateVoucher cv = new CreateVoucher();
		CVoucherHeader reversalVoucher = null;
		final HashMap<String, Object> reversalVoucherMap = new HashMap<String, Object>();
		reversalVoucherMap.put("Original voucher header id", voucherHeader.getId());
		reversalVoucherMap.put("Reversal voucher type", "Receipt");
		// what should be the name
		reversalVoucherMap.put("Reversal voucher name", "Direct");
		try {
			reversalVoucherMap.put("Reversal voucher date", sdf.parse(getReversalVoucherDate()));
		} catch (final ParseException e1) {
			throw new ValidationException(Arrays.asList(new ValidationError("reversalVocuherDate", "reversalVocuherDate.notinproperformat")));
		}
		reversalVoucherMap.put("Reversal voucher number", getReversalVoucherNumber());
		final List<HashMap<String, Object>> reversalList = new ArrayList<HashMap<String, Object>>();
		reversalList.add(reversalVoucherMap);
		try {
			reversalVoucher = cv.reverseVoucher(reversalList);
		} catch (final EGOVRuntimeException e) {
			throw new ValidationException(Arrays.asList(new ValidationError(FAILED_WHILE_REVERSING, FAILED_WHILE_REVERSING)));
		} catch (final ParseException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("Date is not in proper Format", "Date is not in proper Format")));
		}
		loadAjaxedDropDowns();
		addActionMessage(getText("directbankpayment.reverse.transaction.success") + reversalVoucher.getVoucherNumber());
		voucherHeader.setId(reversalVoucher.getId());
		return REVERSE;
	}
	
	private CVoucherHeader createVoucherAndledger() {
		
		try {
			final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
			// update DirectBankPayment source path
			headerDetails.put(VoucherConstant.SOURCEPATH, "/EGF/payment/directBankPayment!beforeView.action?voucherHeader.id=");
			HashMap<String, Object> detailMap = null;
			HashMap<String, Object> subledgertDetailMap = null;
			final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
			final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
			
			detailMap = new HashMap<String, Object>();
			detailMap.put(VoucherConstant.CREDITAMOUNT, commonBean.getAmount().toString());
			detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
			Bankaccount account = (Bankaccount) persistenceService.find("from Bankaccount where id=?", Integer.valueOf(commonBean.getAccountNumberId()));
			detailMap.put(VoucherConstant.GLCODE, account.getChartofaccounts().getGlcode());
			accountdetails.add(detailMap);
			Map<String, Object> glcodeMap = new HashMap<String, Object>();
			for (VoucherDetails voucherDetail : billDetailslist)

			{
				detailMap = new HashMap<String, Object>();
				if (voucherDetail.getFunctionIdDetail() != null) {
					CFunction function = (CFunction) persistenceService.find("from CFunction where id=?", voucherDetail.getFunctionIdDetail());
					detailMap.put(VoucherConstant.FUNCTIONCODE, function.getCode());
				}
				if (voucherDetail.getCreditAmountDetail().compareTo(BigDecimal.ZERO) ==0) {
					
					detailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getDebitAmountDetail().toString());
					detailMap.put(VoucherConstant.CREDITAMOUNT, ZERO);
					detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
					accountdetails.add(detailMap);
					glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.DEBIT);
				}
				else {
					detailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getCreditAmountDetail().toString());
					detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
					detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
					accountdetails.add(detailMap);
					glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.CREDIT);
				}
				
			}
			
			for (VoucherDetails voucherDetail : subLedgerlist) {
				subledgertDetailMap = new HashMap<String, Object>();
				String amountType = glcodeMap.get(voucherDetail.getSubledgerCode())!=null?glcodeMap.get(voucherDetail.getSubledgerCode()).toString():null; // Debit or Credit.
				if(null != amountType && amountType.equalsIgnoreCase(VoucherConstant.DEBIT)){
					subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getAmount());
				}else if(null != amountType ){
					subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getAmount());
				}
				subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, voucherDetail.getDetailType().getId());
				subledgertDetailMap.put(VoucherConstant.DETAILKEYID, voucherDetail.getDetailKeyId());
				subledgertDetailMap.put(VoucherConstant.GLCODE, voucherDetail.getSubledgerCode());
				subledgerDetails.add(subledgertDetailMap);
			}
			
			final CreateVoucher cv = new CreateVoucher();
			voucherHeader = cv.createPreApprovedVoucher(headerDetails, accountdetails, subledgerDetails);
			
		} catch (final HibernateException e) {
			LOGGER.error(e.getMessage(),e);
			throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
		} catch (final EGOVRuntimeException e) {
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		} catch (final ValidationException e) {
			LOGGER.error(e.getMessage(),e);
			throw e;
		} catch (final Exception e) {
			// handle engine exception
			LOGGER.error(e.getMessage(),e);
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		}
		LOGGER.debug("Posted to Ledger " + voucherHeader.getId());
		return voucherHeader;
			}
	
	private void reCreateLedger() {
		HibernateUtil.getCurrentSession().doWork(new Work() {			
			@Override
			public void execute(Connection conn) throws SQLException {
				final CreateVoucher createVoucher = new CreateVoucher();
				try {
					createVoucher.deleteVoucherdetailAndGL(conn, voucherHeader);
					HibernateUtil.getCurrentSession().flush();
					HashMap<String, Object> detailMap = null;
					HashMap<String, Object> subledgertDetailMap = null;
					final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
					final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
					
					detailMap = new HashMap<String, Object>();
					detailMap.put(VoucherConstant.CREDITAMOUNT, commonBean.getAmount().toString());
					detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
					Bankaccount account = (Bankaccount) persistenceService.find("from Bankaccount where id=?", Integer.valueOf(commonBean.getAccountNumberId()));
					detailMap.put(VoucherConstant.GLCODE, account.getChartofaccounts().getGlcode());
					accountdetails.add(detailMap);
					Map<String, Object> glcodeMap = new HashMap<String, Object>();
					for (VoucherDetails voucherDetail : billDetailslist) {
						detailMap = new HashMap<String, Object>();
						if (voucherDetail.getFunctionIdDetail() != null) {
							CFunction function = (CFunction) persistenceService.find("from CFunction where id=?", voucherDetail.getFunctionIdDetail());
							detailMap.put(VoucherConstant.FUNCTIONCODE, function.getCode());
						}
						if (voucherDetail.getCreditAmountDetail().compareTo(BigDecimal.ZERO)==0) {
							
							detailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getDebitAmountDetail().toString());
							detailMap.put(VoucherConstant.CREDITAMOUNT, ZERO);
							detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
							accountdetails.add(detailMap);
							glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.DEBIT);
						}
						else {
							detailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getCreditAmountDetail().toString());
							detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
							detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
							accountdetails.add(detailMap);
							glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.CREDIT);
						}
					}
					
					for (VoucherDetails voucherDetail : subLedgerlist) {
						subledgertDetailMap = new HashMap<String, Object>();
						String amountType = glcodeMap.get(voucherDetail.getSubledgerCode())!=null?glcodeMap.get(voucherDetail.getSubledgerCode()).toString():null; // Debit or Credit.
						if(null != amountType && amountType.equalsIgnoreCase(VoucherConstant.DEBIT)){
							subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getAmount());
						}else if(null != amountType ){
							subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getAmount());
						}
						subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, voucherDetail.getDetailType().getId());
						subledgertDetailMap.put(VoucherConstant.DETAILKEYID, voucherDetail.getDetailKeyId());
						subledgertDetailMap.put(VoucherConstant.GLCODE, voucherDetail.getSubledgerCode());
						subledgerDetails.add(subledgertDetailMap);
					}
					
					final List<Transaxtion> transactions = createVoucher.createTransaction(null,accountdetails, subledgerDetails, voucherHeader);
					HibernateUtil.getCurrentSession().flush();
					
					final ChartOfAccounts engine = ChartOfAccounts.getInstance();
					Transaxtion txnList[] = new Transaxtion[transactions.size()];
					txnList = transactions.toArray(txnList);
					final SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
					if (!engine.postTransaxtions(txnList, conn, formatter.format(voucherHeader.getVoucherDate()))) {
						throw new ValidationException(Arrays.asList(new ValidationError("Exception While Saving Data", "Transaction Failed")));
					}
				} catch (HibernateException e) {
					LOGGER.error(e.getMessage(),e);
					throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
				} catch (NumberFormatException e) {
					LOGGER.error(e.getMessage(),e);
					throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
				} catch (EGOVRuntimeException e) {
					LOGGER.error(e.getMessage(),e);
					throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
				} catch (SQLException e) {
					LOGGER.error(e.getMessage(),e);
					throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
				} catch (TaskFailedException e) {
					LOGGER.error(e.getMessage(),e);
					throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
				} catch (Exception e) {
					LOGGER.error(e.getMessage(),e);
					throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
				}
			}
		});
		
	}
	
	protected boolean validateDBPData(final List<VoucherDetails> billDetailslist, final List<VoucherDetails> subLedgerList) {
		BigDecimal totalDrAmt = BigDecimal.ZERO;
		BigDecimal totalCrAmt = BigDecimal.ZERO;
		totalCrAmt = totalCrAmt.add(commonBean.getAmount());
		int index = 0;
		boolean isValFailed = false;
		for (VoucherDetails voucherDetails : billDetailslist) {
			index = index + 1;
			totalDrAmt = totalDrAmt.add(voucherDetails.getDebitAmountDetail());
			totalCrAmt = totalCrAmt.add(voucherDetails.getCreditAmountDetail());
			if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0 && voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0
					&& voucherDetails.getGlcodeDetail().trim().length() == 0) {
				
				addActionError(getText("journalvoucher.accdetail.emptyaccrow", new String[] { "" + index }));
				isValFailed = true;
			}
			else if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
					&& voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0 && voucherDetails.getGlcodeDetail().trim().length() != 0) {
				addActionError(getText("journalvoucher.accdetail.amountZero", new String[] { voucherDetails.getGlcodeDetail() }));
				isValFailed = true;
			}
			else if (voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0
					&& voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0) {
				addActionError(getText("journalvoucher.accdetail.amount", new String[] { voucherDetails.getGlcodeDetail() }));
				isValFailed = true;
			}
			else if ((voucherDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0 || voucherDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0)
					&& voucherDetails.getGlcodeDetail().trim().length() == 0) {
				addActionError(getText("journalvoucher.accdetail.accmissing", new String[] { "" + index }));
				isValFailed = true;
			}
			
		}
		if (totalDrAmt.compareTo(totalCrAmt) != 0 && !isValFailed) {
			addActionError(getText("journalvoucher.accdetail.drcrmatch"));
			isValFailed = true;
		}
		else if (!isValFailed) {
			isValFailed = validateSubledgerDetails(billDetailslist, subLedgerList);
		}
		return isValFailed;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@SkipValidation
	public boolean validateUser(String purpose)  {
		Script validScript = scriptExecutionService.findAllByNamedQuery(Script.BY_NAME, "Paymentheader.show.bankbalance").get(0);
		List<String> list = (List<String>) scriptExecutionService.executeScript(validScript,scriptExecutionService.createContext("persistenceService", paymentService, "purpose", purpose));
		if (list.get(0).equals("true")) {
			try {
				commonBean.setAvailableBalance(egovCommon.getAccountBalance(new Date(), paymentheader.getBankaccount().getId(),paymentheader.getPaymentAmount(),paymentheader.getId()));
				balance=commonBean.getAvailableBalance();
				return true;
			} catch (Exception e) {
				balance=BigDecimal.valueOf(-1);
				return true;
			}
		}
		else {
			return false;
		}
	}
	
	@ValidationErrorPage(value = VIEW)
	@SkipValidation
	public String approve() {
		paymentheader = getPayment();
		if(paymentheader.getState().getValue()!=null && !paymentheader.getState().getValue().isEmpty()  && paymentheader.getState().getValue().contains("Rejected"))
		{
		    voucherHeader.setId(paymentheader.getVoucherheader().getId());
			return beforeEdit();
		}
		showApprove = true;
		voucherHeader.setId(paymentheader.getVoucherheader().getId());
		prepareForViewModifyReverse();
		loadApproverUser(voucherHeader.getType());
		return VIEW;
		
	}
	

	
	@ValidationErrorPage(value = VIEW)
	@SkipValidation
	public String sendForApproval() {
		paymentheader = getPayment();
	//	LOGGER.debug("Paymentheader==" + paymentheader.getId() + ", actionname=" + parameters.get(ACTIONNAME)[0]);
		action = parameters.get(ACTIONNAME)[0];
		
		Integer userId = null;
		if( parameters.get(ACTIONNAME)[0] != null && parameters.get(ACTIONNAME)[0].contains("reject")){
			userId = paymentheader.getCreatedBy().getId();
		}
		else if(null != parameters.get("approverUserId") &&  Integer.valueOf(parameters.get("approverUserId")[0])!=-1  ){
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);
		}else {
			userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
		}
		
		LOGGER.debug("userId  ==" + userId);
		paymentWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, paymentheader, parameters.get("comments")[0]);
		paymentService.persist(paymentheader);
		if (parameters.get(ACTIONNAME)[0].contains("approve")){
			if ("END".equals(paymentheader.getState().getValue())) {
				addActionMessage(getText("payment.voucher.final.approval"));
			}
			else
			{
				addActionMessage(getText("payment.voucher.approved", new String[] { paymentService.getEmployeeNameForPositionId(paymentheader.getState()
								.getOwner()) }));
			}
			setAction(parameters.get(ACTIONNAME)[0]);
			
		}else
		{
			addActionMessage(getText("payment.voucher.rejected", new String[] { paymentService
					.getEmployeeNameForPositionId(paymentheader.getState().getOwner()) }));
		}
		return approve();
	}
	
	@SuppressWarnings("unchecked")
	private void loadApproverUser(String atype){
		String scriptName = "paymentHeader.nextDesg";
		if(paymentheader!=null && paymentheader.getPaymentAmount()!=null)
		{
		LOGGER.info("paymentheader.getPaymentAmount() >>>>>>>>>>>>>>>>>>> :"+paymentheader.getPaymentAmount());
		atype = atype + "|"+paymentheader.getPaymentAmount();
		}
		else
		{
			atype = atype + "|";  
		}
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		departmentId = voucherService.getCurrentDepartment().getId();
		LOGGER.info("departmentId :"+departmentId);
		Map<String, Object>  map = voucherService.getDesgByDeptAndType(atype, scriptName);
		addDropdownData("departmentList", masterCache.get("egi-department"));
		
		List<Map<String,Object>> desgList  =  (List<Map<String,Object>>) map.get("designationList");
		String  strDesgId = "", dName = "";
		boolean bDefaultDeptId = false;
		List< Map<String , Object>> designationList = new ArrayList<Map<String,Object>>();
		Map<String, Object> desgFuncryMap;
		for(Map<String,Object> desgIdAndName : desgList) {  
			desgFuncryMap = new HashMap<String, Object>();
			
			if(desgIdAndName.get("designationName") != null ) {
				desgFuncryMap.put("designationName",(String) desgIdAndName.get("designationName"));
			}
			
			if(desgIdAndName.get("designationId") != null ) {
				strDesgId = (String) desgIdAndName.get("designationId");
				if(strDesgId.indexOf("~") != -1) {
					strDesgId = strDesgId.substring(0, strDesgId.indexOf('~'));
					dName = (String) desgIdAndName.get("designationId");
					dName = dName.substring(dName.indexOf('~')+1);
					bDefaultDeptId = true;
				}
				desgFuncryMap.put("designationId",strDesgId);
			}
			designationList.add(desgFuncryMap);
		}
		map.put("designationList", designationList);
		
		addDropdownData("designationList", (List<DesignationMaster>)map.get("designationList")); 
		
		if(bDefaultDeptId && !dName.equals("")) {
			DepartmentImpl dept = (DepartmentImpl) persistenceService.find("from DepartmentImpl where deptName like '%"+dName+"' ");
			departmentId = dept.getId();
		}
		wfitemstate = map.get("wfitemstate")!=null?map.get("wfitemstate").toString():"";
	}
	
	@SkipValidation
	public List<Action> getValidActions() {
		return paymentWorkflowService.getValidActions(getPayment());
	}
	
	public Paymentheader getPayment() {
		String paymentid=null;
		//System.out.println("Payment id is"+parameters.get(PAYMENTID));
		if(parameters.get(PAYMENTID)==null )
		{
			Object obj = getSession().get(PAYMENTID);
			if(obj!=null)
			{
				paymentid=(String)obj;	
			}
		}else
		{
			paymentid=parameters.get(PAYMENTID)[0];
		}
		if(paymentheader==null && paymentid!=null) 
		{
			paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where id=?", Long.valueOf(paymentid));
		}
		if(paymentheader==null)
		{
			paymentheader=new Paymentheader();
		}
		
		return paymentheader;
	}

	@ValidationErrorPage(value="beforeEdit")
	@SkipValidation
	public String cancelPayment()
	{
		voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", voucherHeader.getId());
		paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where voucherheader=?", voucherHeader);
		voucherHeader.setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
		persistenceService.setType(CVoucherHeader.class);
		paymentWorkflowService.end(paymentheader, paymentService.getPosition());
		persistenceService.persist(voucherHeader);
		addActionMessage(getText("payment.cancel.success"));  
		action=parameters.get(ACTIONNAME)[0];
		return beforeView();      
	}          
	
	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	public void setContraService(ContraService contraService) {}
	
	public boolean isShowChequeNumber() {
		return showChequeNumber;
	}
	
	public void setShowChequeNumber(boolean showChequeNumber) {
		this.showChequeNumber = showChequeNumber;
	}
	
	public List<VoucherDetails> getSubLedgerlist() {
		return subLedgerlist;
	}
	
	public void setSubLedgerlist(List<VoucherDetails> subLedgerlist) {
		this.subLedgerlist = subLedgerlist;
	}
	
	public List<VoucherDetails> getBillDetailslist() {
		return billDetailslist;
	}
	
	public void setBillDetailslist(List<VoucherDetails> billDetailslist) {
		this.billDetailslist = billDetailslist;
	}
	
	public void setCommonBean(CommonBean commonBean) {
		this.commonBean = commonBean;
	}
	
	public CommonBean getCommonBean() {
		return commonBean;
	}
	
	public Map<String, String> getModeOfPaymentMap() {
		return modeOfPaymentMap;
	}
	
	public void setModeOfPaymentMap(Map<String, String> modeOfPaymentMap) {
		this.modeOfPaymentMap = modeOfPaymentMap;
	}
	
	public String getButton() {
		return button;
	}
	
	public void setButton(String button) {
		this.button = button;
	}
	
	public VoucherService getVoucherService() {
		return voucherService;
	}
	
	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	
	public Paymentheader getPaymentheader() {
		return paymentheader;
	}
	
	public void setPaymentheader(Paymentheader paymentheader) {
		this.paymentheader = paymentheader;
	}

	public void setPaymentWorkflowService(SimpleWorkflowService<Paymentheader> paymentWorkflowService) {
		this.paymentWorkflowService = paymentWorkflowService;
	}
	
	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}


	public Integer getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}


	public String getWfitemstate() {
		return wfitemstate;
	}


	public void setWfitemstate(String wfitemstate) {
		this.wfitemstate = wfitemstate;
	}

	public String getComments(){
		return getText("payment.comments", new String[]{paymentheader.getPaymentAmount().toPlainString()});
	}


	public String getTypeOfAccount() {
		return typeOfAccount;
	}


	public void setTypeOfAccount(String typeOfAccount) {
		this.typeOfAccount = typeOfAccount;
	}


	public List<InstrumentHeader> getInstrumentHeaderList() {
		return instrumentHeaderList;
	}


	public void setInstrumentHeaderList(List<InstrumentHeader> instrumentHeaderList) {
		this.instrumentHeaderList = instrumentHeaderList;
	}
	
	
}
