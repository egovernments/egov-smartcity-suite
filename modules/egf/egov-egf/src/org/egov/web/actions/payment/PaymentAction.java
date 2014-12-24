package org.egov.web.actions.payment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.payment.PaymentBean;
import org.egov.model.payment.Paymentheader;
import org.egov.pims.commons.DesignationMaster;
import org.egov.services.payment.PaymentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.egov.web.annotation.ValidationErrorPage;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;
import com.opensymphony.xwork2.validator.annotations.Validation;

@ParentPackage("egov")  
@Validation
public class PaymentAction extends BasePaymentAction{
	private String expType,fromDate,toDate,mode,voucherdate,paymentMode,contractorIds="",supplierIds="",vouchernumber,voucherNumberPrefix="",voucherNumberSuffix="";
	private String contingentIds = "";
	private String salaryIds = "";
	private int miscount=0;
	private boolean isDepartmentDefault;
	private BigDecimal balance;
	private CommonsService commonsService;
	private Paymentheader paymentheader;
	private PaymentService paymentService;
	private VoucherService voucherService;
	private Integer bankaccount,bankbranch; 
	private Integer departmentId;
	private Integer defaultDept;
	private SimpleWorkflowService<Paymentheader> paymentWorkflowService;
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	private static final Logger LOGGER = Logger.getLogger(PaymentAction.class);
	private static final String PAYMENTID="paymentid";
	private static final String VIEW="view";
	private static final String LIST="list";
	private static final String MODIFY="modify";
	private String wfitemstate;
	private String type;
	private String billNumber;
	private String typeOfAccount;
	private List<EgBillregister> contractorBillList = null;
	private List<EgBillregister> supplierBillList = null;
	private List<EgBillregister> contingentBillList = null;
	private List<EgBillregister> salaryBillList = new ArrayList<EgBillregister>();
	private List<EgBillregister> totalBillList = new ArrayList<EgBillregister>();
	private List<Bankaccount> bankaccountList=null;
	private List<Miscbilldetail> miscBillList=null;
	private List<PaymentBean> billList;
	private List<PaymentBean> contractorList = null;
	private List<PaymentBean> supplierList = null;
	private List<PaymentBean> contingentList = null;
	private List<PaymentBean> salaryList = new ArrayList<PaymentBean>();
	private List<InstrumentHeader> instrumentHeaderList;
	private List<Paymentheader> paymentheaderList;
	private EgBillregister billregister;
	private boolean disableExpenditureType = false;
	
	private Map<String,String> payeeMap = new HashMap<String,String>();
	private Map<Long,BigDecimal> deductionAmtMap = new HashMap<Long,BigDecimal>();
	private Map<Long,BigDecimal> paidAmtMap = new HashMap<Long,BigDecimal>();
	private List<EgAdvanceRequisition> advanceRequisitionList = new ArrayList<EgAdvanceRequisition>();
	private VoucherHelper voucherHelper;
	private  ScriptService scriptExecutionService;
	public PaymentAction(){
		addRelatedEntity("paymentheader", Paymentheader.class);
	}
	
	public void prepare(){
		super.prepare();
		mandatoryFields = new ArrayList<String>();
		if(parameters.containsKey("salaryType"))
			setDisableExpenditureType(true);
		if(parameters.get("fundId")!=null && !parameters.get("fundId")[0].equals("-1")){
		   	Fund fund  = (Fund) persistenceService.find("from Fund where id=?", Integer.parseInt(parameters.get("fundId")[0]));
			addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? ) and br.isactive='1' order by br.bank.name asc",fund));
		}
		else
			addDropdownData("bankbranchList",Collections.EMPTY_LIST);

		if(getBankbranch()!=null)
			addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive='1' ",getBankbranch()));
		else if(parameters.get("paymentheader.bankaccount.bankbranch.id")!=null && !parameters.get("paymentheader.bankaccount.bankbranch.id")[0].equals("-1"))
			addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive='1' ",Integer.valueOf(parameters.get("paymentheader.bankaccount.bankbranch.id")[0])));
		else
			addDropdownData("bankaccountList", Collections.EMPTY_LIST);			
		if(getBillregister()!=null && getBillregister().getId()!=null){
			billregister=(EgBillregister) persistenceService.find(" from EgBillregister where id=?",getBillregister().getId());
			addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? ) and br.isactive='1' order by br.bank.name asc",billregister.getEgBillregistermis().getFund()));
		}
		addDropdownData("designationList", Collections.EMPTY_LIST);
		addDropdownData("userList", Collections.EMPTY_LIST);
		typeOfAccount = FinancialConstants.TYPEOFACCOUNT_PAYMENTS+","+FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS;
	}
	
	private void loadbankBranch(Fund fund){
		if(typeOfAccount != null && !typeOfAccount.equals("")) {
			if(typeOfAccount.indexOf(",") !=  -1 ) {
				String [] strArray = typeOfAccount.split(",");
				addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and isactive = 1 and type in (?,?) ) and br.isactive='1' and br.bank.isactive = 1 order by br.bank.name asc",fund, (String)strArray[0], (String)strArray[1] ));
			} else {
				addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and isactive = 1 and type in (?) ) and br.isactive='1' and br.bank.isactive = 1 order by br.bank.name asc",fund, typeOfAccount ));
			}
		} else {
			addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and isactive = 1) and br.isactive='1' and br.bank.isactive = 1 order by br.bank.name asc",fund));
		}
	}
	
	@Override
	public Object getModel() {
		voucherHeader=(CVoucherHeader)super.getModel();
		voucherHeader.setType("Payment");
		return voucherHeader;
	}
	
	@SkipValidation
	public String beforeSearch()throws Exception{
		if(validateUser("deptcheck"))
			voucherHeader.getVouchermis().setDepartmentid((DepartmentImpl)paymentService.getAssignment().getDeptId());
		return "search";
	}
	@ValidationErrorPage(value="search")
	public String search() throws Exception{  
		StringBuffer sql =new StringBuffer();
		if(!"".equals(billNumber))
			sql.append(" and bill.billnumber = '"+billNumber+"' ");
		if(!"".equals(fromDate))
			sql.append(" and bill.billdate>='"+sdf.format(formatter.parse(fromDate))+"' ");
		if(!"".equals(toDate))
			sql.append(" and bill.billdate<='"+sdf.format(formatter.parse(toDate))+"'");
		if(voucherHeader.getFundId()!=null)
			sql.append(" and bill.egBillregistermis.fund.id="+voucherHeader.getFundId().getId());
		if(voucherHeader.getVouchermis().getFundsource()!=null)
			sql.append(" and bill.egBillregistermis.fundsource.id="+voucherHeader.getVouchermis().getFundsource().getId());
		if(voucherHeader.getVouchermis().getDepartmentid()!=null)
			sql.append(" and bill.egBillregistermis.egDepartment.id="+voucherHeader.getVouchermis().getDepartmentid().getId());
		if(voucherHeader.getVouchermis().getSchemeid()!=null)
			sql.append(" and bill.egBillregistermis.scheme.id="+voucherHeader.getVouchermis().getSchemeid().getId());
		if(voucherHeader.getVouchermis().getSubschemeid()!=null)
			sql.append(" and bill.egBillregistermis.subScheme.id="+voucherHeader.getVouchermis().getSubschemeid().getId());
		if(voucherHeader.getVouchermis().getFunctionary()!=null)
			sql.append(" and bill.egBillregistermis.functionaryid.id="+voucherHeader.getVouchermis().getFunctionary().getId());
		if(voucherHeader.getVouchermis().getDivisionid()!=null)
			sql.append(" and bill.egBillregistermis.fieldid="+voucherHeader.getVouchermis().getDivisionid().getId());
			
		EgwStatus egwStatus=null;
		String mainquery = "from EgBillregister bill where bill.egBillregistermis.voucherHeader is not null and bill.egBillregistermis.voucherHeader.status=0 and bill.expendituretype=? and ( ( bill.passedamount >(select sum(paidamount) from Miscbilldetail where  billVoucherHeader in (select voucherHeader from EgBillregistermis where egBillregister.id = bill.id ) and (payVoucherHeader is null or payVoucherHeader.status not in (1,2,4) )   group by billVoucherHeader)) " +
					" or(select count(*) from Miscbilldetail where payVoucherHeader.status!=4 and billVoucherHeader in (select voucherHeader from EgBillregistermis where egBillregister.id = bill.id ) )=0 ) ";
		if(disableExpenditureType==true || (expType!=null && !expType.equals("-1") && expType.equals("Salary"))){
			return salaryBills(sql,mainquery);
		}
		if(expType==null || expType.equals("-1") || expType.equals("Purchase"))
		{
			egwStatus = commonsService.getStatusByModuleAndCode("SBILL", "Approved");
			EgwStatus egwStatus1 = commonsService.getStatusByModuleAndCode("PURCHBILL", "Passed");
			String supplierBillSql=mainquery+" and bill.status in (?,?) "+sql.toString() +" order by bill.billdate desc";
			supplierBillList = getPersistenceService().findPageBy(supplierBillSql,1,500,"Purchase",egwStatus,egwStatus1).getList();
		}
		if(expType==null || expType.equals("-1") || expType.equals("Works"))
		{
			// right not we dont know, the EGW-Status for works bill, passed from external system
			egwStatus = commonsService.getStatusByModuleAndCode("WORKSBILL", "Passed");
			EgwStatus egwStatus1 = commonsService.getStatusByModuleAndCode("CONTRACTORBILL", "APPROVED"); // for external systems
			String contractorBillSql=mainquery+" and bill.status in (?,?) "+sql.toString() + " order by bill.billdate desc"; 
			contractorBillList = getPersistenceService().findPageBy(contractorBillSql,1,500,"Works",egwStatus,egwStatus1).getList(); 
		}
		if(expType==null || expType.equals("-1") || expType.equals(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT))
		{
			egwStatus = commonsService.getStatusByModuleAndCode("EXPENSEBILL", "Approved"); // for financial expense bills
			EgwStatus egwStatus1 = commonsService.getStatusByModuleAndCode("CBILL", "APPROVED"); // for external systems
			String cBillSql=mainquery+" and bill.status in (?,?) "+sql.toString() + " order by bill.billdate desc"; 
			contingentBillList = getPersistenceService().findPageBy(cBillSql,1,500,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT,egwStatus,egwStatus1).getList(); 
			LOGGER.info("cBillSql  ===> "+cBillSql);
		}
		paymentService.getGlcodeIds();
		deductionAmtMap = paymentService.getDeductionAmt(contractorBillList, "Works");
		deductionAmtMap.putAll(paymentService.getDeductionAmt(supplierBillList, "Purchase"));
		deductionAmtMap.putAll(paymentService.getDeductionAmt(contingentBillList,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT));
		paidAmtMap = paymentService.getEarlierPaymentAmt(contractorBillList, "Works");
		paidAmtMap.putAll(paymentService.getEarlierPaymentAmt(supplierBillList, "Purchase"));
		paidAmtMap.putAll(paymentService.getEarlierPaymentAmt( contingentBillList,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT));
		contractorList = paymentService.getCSList(contractorBillList, deductionAmtMap, paidAmtMap);
		supplierList = paymentService.getCSList(supplierBillList, deductionAmtMap, paidAmtMap);
		
		contingentList = paymentService.getCSList(contingentBillList, deductionAmtMap, paidAmtMap);
		LOGGER.info("contingentList size ==="+contingentList.size());
		
		setMode("search");
		paymentMode=FinancialConstants.MODEOFCOLLECTION_CHEQUE;
		loadSchemeSubscheme();
		loadFundSource();
		return "searchbills";
	}
	
	private String salaryBills(StringBuffer sql,String mainquery) {
		EgwStatus egwStatus = commonsService.getStatusByModuleAndCode("SALBILL", "Approved"); // for financial expense bills
		EgwStatus egwStatus1 = commonsService.getStatusByModuleAndCode("SBILL", "Approved"); // for external systems
		String sBillSql=mainquery+" and bill.status in (?,?) "+sql.toString()+" order by bill.billdate desc"; 
		salaryBillList = getPersistenceService().findAllBy(sBillSql,FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY,egwStatus,egwStatus1);
		paymentService.getGlcodeIds();
		deductionAmtMap = paymentService.getDeductionAmt(salaryBillList, "Salary");
		paidAmtMap = paymentService.getEarlierPaymentAmt(salaryBillList, "Salary");
		salaryList = paymentService.getCSList(salaryBillList, deductionAmtMap, paidAmtMap);
		setMode("search");
		paymentMode=FinancialConstants.MODEOFPAYMENT_CASH;
		return "salaryBills";
	}

	public String generatePayment() throws ValidationException{
		try{
			billList = new ArrayList<PaymentBean>();
			contractorIds = contractorIds+populateBillListFor(contractorList,contractorIds);
			supplierIds = supplierIds+populateBillListFor(supplierList,supplierIds);
			contingentIds = contingentIds+populateBillListFor(contingentList,contingentIds);
			salaryIds = salaryIds+populateBillListFor(salaryList,salaryIds);
			if(salaryIds!=null && salaryIds.length()>0){
				disableExpenditureType = true;
			}
			
			billregister = (EgBillregister) persistenceService.find(" from EgBillregister where id=?", (billList.get(0)).getBillId()) ;
			loadbankBranch(billregister.getEgBillregistermis().getFund());
			miscount = billList.size();
			if(parameters.get("paymentMode")[0].equalsIgnoreCase("RTGS")){
				paymentService.validateForRTGSPayment(contractorList, "Contractor");
				paymentService.validateForRTGSPayment(supplierList, "Supplier");
				paymentService.validateForRTGSPayment(contingentList, FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
			}

			if(! "Auto".equalsIgnoreCase(new VoucherTypeForULB().readVoucherTypes("Payment"))){
				headerFields.add("vouchernumber");
				mandatoryFields.add("vouchernumber");
			}
			voucherdate =formatter.format(new Date());
		}catch(ValidationException e){
			LOGGER.error(e.getErrors());
			throw new ValidationException(e.getErrors());
		}catch(EGOVException e){
			LOGGER.error(e.getMessage());
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getMessage()));
			throw new ValidationException(errors);
		}
		loadApproverUser(voucherHeader.getType());
		return "form";
		
	}
	private String populateBillListFor(List<PaymentBean> list,String ids) {
		if(list!=null){
			for(PaymentBean bean :list){
				if(bean.getIsSelected()){
					billList.add(bean);
					ids = ids+bean.getBillId()+",";
				}
			}
			if(ids.length()>0)
				ids = ids.substring(0,ids.length()-1);
		}
		return ids;
	}
	
	@ValidationErrorPage(value="form")
	@SkipValidation
	public String createPayment(){
		try{
			paymentheader = paymentService.createPayment(parameters,billList,billregister);
			paymentheader.getVoucherheader().getVouchermis().setSourcePath("/EGF/payment/payment!view.action?"+PAYMENTID+"="+paymentheader.getId());
			getPaymentBills();
			paymentWorkflowService.start(paymentheader, paymentService.getPosition(),"");
			sendForApproval();
			addActionMessage(getMessage("payment.transaction.success",new String[]{paymentheader.getVoucherheader().getVoucherNumber()}));
			loadApproverUser(voucherHeader.getType());
		}catch(ValidationException e){
			loadApproverUser(voucherHeader.getType());
			throw new ValidationException(e.getErrors());
		}catch(EGOVRuntimeException e){
			LOGGER.error(e.getMessage());
			loadApproverUser(voucherHeader.getType());
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getMessage()));
			throw new ValidationException(errors);
		}
		return VIEW;
	}

	@ValidationErrorPage(value=VIEW)
	@SkipValidation
	public String sendForApproval() 
	{
		paymentheader = getPayment();
		getPaymentBills();
		LOGGER.debug("Paymentheader=="+paymentheader.getId()+", actionname="+parameters.get(ACTIONNAME)[0]);
		action=parameters.get(ACTIONNAME)[0];
		
		Integer userId = null;
		if( parameters.get(ACTIONNAME)[0] != null && parameters.get(ACTIONNAME)[0].contains("reject")){
			userId = paymentheader.getCreatedBy().getId();
		}
		else if (null != parameters.get("approverUserId") &&  Integer.valueOf(parameters.get("approverUserId")[0])!=-1 ) {
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);
		}else {
			userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
		}
		
		LOGGER.debug("Paymentheader=="+paymentheader.getStateType());
		if(parameters.get("comments")!=null){
			paymentWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, paymentheader, parameters.get("comments")[0]);
		}else{
			paymentWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, paymentheader, paymentheader.getVoucherheader().getDescription());	
		}
		HibernateUtil.getCurrentSession().refresh(paymentheader.getVoucherheader());
		LOGGER.info(paymentheader.getVoucherheader().getStatus()+"and vouchernumber is "+paymentheader.getVoucherheader().getVoucherNumber());
		paymentService.persist(paymentheader);
		if(parameters.get(ACTIONNAME)[0].contains("approve"))
			if("END".equals(paymentheader.getState().getValue()))
				addActionMessage(getMessage("payment.voucher.final.approval"));
			else
				addActionMessage(getMessage("payment.voucher.approved",new String[]{paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwner())}));
		else
			addActionMessage(getMessage("payment.voucher.rejected",new String[]{paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwner())}));
		if(Constants.ADVANCE_PAYMENT.equalsIgnoreCase(paymentheader.getVoucherheader().getName())){
			getAdvanceRequisitionDetails();
			return "advancePaymentView";
		}
		return VIEW;
	}
	
	public String getComments(){
		return getText("payment.comments", new String[]{paymentheader.getPaymentAmount().toPlainString()});
	}
	
	@SkipValidation
	public List<Action> getValidActions(){
		
		return paymentWorkflowService.getValidActions(getPayment());
 	}
	@SkipValidation
	public String view() 
	{
		paymentheader = getPayment();
		if(paymentheader.getState().getValue()!=null && !paymentheader.getState().getValue().isEmpty()  && paymentheader.getState().getValue().contains("Rejected"))
		{
			return modify();
		}
		//billList = paymentService.getMiscBillList(paymentheader);
		//getPaymentBills();
		List<PaymentBean> miscBillList = paymentService.getMiscBillList(paymentheader);
		paymentService.getGlcodeIds();
		for(PaymentBean paymentObj:miscBillList){
			EgBillregister billregister = (EgBillregister)persistenceService.find("from EgBillregister where billnumber = ?", paymentObj.getBillNumber());
			totalBillList.add(billregister);
			deductionAmtMap.putAll(paymentService.getDeductionAmt(totalBillList, paymentObj.getExpType()));
			paidAmtMap.putAll(paymentService.getEarlierPaymentAmt(totalBillList, paymentObj.getExpType()));
			Iterator itr = paidAmtMap.keySet().iterator();
			long key;
			while (itr.hasNext()) {
				key = Long.valueOf(itr.next().toString());
				paidAmtMap.put(key, paidAmtMap.get(key).subtract(paymentObj.getPaymentAmt()));	
			}
		}
		billList = paymentService.getCSListForModify(totalBillList, deductionAmtMap, paidAmtMap, miscBillList);
		getChequeInfo();
		if(null != parameters.get("showMode") && parameters.get("showMode")[0].equalsIgnoreCase("view") ){
			// if user is  drilling down form source , parameter showMode is passed with value view, in this case we do not show the
			 // approver drop down in the view screen .
			wfitemstate = "END";
		}else{
			loadApproverUser(voucherHeader.getType());
		}
		
		LOGGER.info("defaultDept in vew : "+getDefaultDept());
		return VIEW;
	}
	
	@SkipValidation
	public String advanceView(){
		paymentheader = getPayment();
		if(paymentheader.getState().getValue()!=null && !paymentheader.getState().getValue().isEmpty()  
				&& paymentheader.getState().getValue().contains("Rejected")){
			return modifyAdvancePayment();
		}
		getAdvanceRequisitionDetails();
		getChequeInfo();
		loadApproverUser(voucherHeader.getType());
		return "advancePaymentView";
	}
	
	private void getAdvanceRequisitionDetails() {
		advanceRequisitionList.addAll(persistenceService.findAllBy("from EgAdvanceRequisition where egAdvanceReqMises.voucherheader.id=?", paymentheader.getVoucherheader().getId()));
	}
	
	public void getChequeInfo()
	{
		paymentheader = getPayment();
		instrumentHeaderList = getPersistenceService().findAllBy(" from InstrumentHeader ih where ih.id in (select iv.instrumentHeaderId.id from InstrumentVoucher iv where iv.voucherHeaderId.id=?) order by instrumentNumber", paymentheader.getVoucherheader().getId());
	}
	
	@SkipValidation
	public void getPaymentBills()
	{
		miscBillList = getPersistenceService().findAllBy(" from Miscbilldetail where payVoucherHeader.id = ? order by paidto", paymentheader.getVoucherheader().getId());
	}
	@SkipValidation
	public boolean validateUser(String purpose)throws ParseException
	{
		LOGGER.info("-------------------------------------------------------------------------------------------------");
		LOGGER.info("Calling Validate User "+purpose);
		LOGGER.info("-------------------------------------------------------------------------------------------------");
		Script validScript = scriptExecutionService.findAllByNamedQuery(Script.BY_NAME,"Paymentheader.show.bankbalance").get(0);
		List<String> list = (List<String>) scriptExecutionService.executeScript(validScript,scriptExecutionService.createContext("persistenceService",paymentService,"purpose",purpose));
		if(list.get(0).equals("true"))
		{
			if(purpose.equals("balancecheck"))
			{
				paymentheader = getPayment(); 
				try {
					getBankBalance(paymentheader.getBankaccount().getId().toString(),formatter.format(new Date()),paymentheader.getPaymentAmount(),paymentheader.getId());
				} catch (ValidationException e) {
					balance=BigDecimal.valueOf(-1);
				}
			}
			return true;
		}
		else 
			return false;
	}
	
	public ScriptService getScriptExecutionService() {
		return scriptExecutionService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

	@SkipValidation
	public String ajaxLoadBankAccounts()
	{
		LOGGER.info("Bankbranch id = "+parameters.get("bankbranch")[0]);
		Bankbranch bankbranch = (Bankbranch) persistenceService.find("from Bankbranch where id = ?", Integer.parseInt(parameters.get("bankbranch")[0]));
		bankaccountList = getPersistenceService().findAllBy(" FROM Bankaccount where bankbranch=? and isactive='1' ", bankbranch);
		return "bankaccount";
	}
	
	@SkipValidation
	public String ajaxGetAccountBalance() throws ParseException
	{
		getBankBalance(parameters.get("bankaccount")[0],parameters.get("voucherDate")[0],null,null);
		
		return "balance";
	}
	@SkipValidation
	public void getBankBalance(String accountId,String vdate,BigDecimal amount,Long paymentId)throws ParseException
	{
		
		try {
			balance = paymentService.getAccountBalance(accountId,vdate,amount,paymentId);
		} catch (Exception e) {
			balance=BigDecimal.valueOf(-1);
		}
		
	}
	
	@SkipValidation
	public String beforeModify()throws Exception
	{
		if(validateUser("deptcheck"))
			voucherHeader.getVouchermis().setDepartmentid((DepartmentImpl)paymentService.getAssignment().getDeptId());
		action="search";
		return LIST;
	}
	@ValidationErrorPage(value=LIST)
	public String list() throws Exception
	{
		List<String> descriptionList = new ArrayList<String>();
		descriptionList.add("New");
		descriptionList.add("Deposited");
		descriptionList.add("Reconciled");
		List<EgwStatus> egwStatusList = commonsService.getStatusListByModuleAndCodeList("Instrument", descriptionList);
		String statusId="";
		for(EgwStatus egwStatus : egwStatusList)
			statusId = statusId+egwStatus.getId()+",";
		statusId = statusId.substring(0, statusId.length()-1);
		
		StringBuffer sql =new StringBuffer();
		if(!StringUtils.isBlank(fromDate))
			sql.append(" and ph.voucherheader.voucherDate>='"+sdf.format(formatter.parse(fromDate))+"' ");
		if(!StringUtils.isBlank(toDate))
			sql.append(" and ph.voucherheader.voucherDate<='"+sdf.format(formatter.parse(toDate))+"'");
		if(!StringUtils.isBlank(voucherHeader.getVoucherNumber()))
			sql.append(" and ph.voucherheader.voucherNumber like '%"+voucherHeader.getVoucherNumber()+"%'");
		if(voucherHeader.getFundId()!=null)
			sql.append(" and ph.voucherheader.fundId.id="+voucherHeader.getFundId().getId());
		if(voucherHeader.getFundsourceId()!=null)
			sql.append(" and ph.voucherheader.vouchermis.fundsource.id="+voucherHeader.getFundsourceId().getId());
		if(voucherHeader.getVouchermis().getDepartmentid()!=null)
			sql.append(" and ph.voucherheader.vouchermis.departmentid.id="+voucherHeader.getVouchermis().getDepartmentid().getId());
		if(voucherHeader.getVouchermis().getSchemeid()!=null)
			sql.append(" and ph.voucherheader.vouchermis.schemeid.id="+voucherHeader.getVouchermis().getSchemeid().getId());
		if(voucherHeader.getVouchermis().getSubschemeid()!=null)
			sql.append(" and ph.voucherheader.vouchermis.subschemeid.id="+voucherHeader.getVouchermis().getSubschemeid().getId());
		if(voucherHeader.getVouchermis().getFunctionary()!=null)
			sql.append(" and ph.voucherheader.vouchermis.functionary.id="+voucherHeader.getVouchermis().getFunctionary().getId());
		if(voucherHeader.getVouchermis().getDivisionid()!=null)
			sql.append(" and ph.voucherheader.vouchermis.divisionid.id="+voucherHeader.getVouchermis().getDivisionid().getId());


		paymentheaderList = getPersistenceService().findAllBy(" from Paymentheader ph where ph.voucherheader.status=0 and (ph.voucherheader.isConfirmed=null or ph.voucherheader.isConfirmed=0) "+sql.toString()+
				"  and ph.voucherheader.id not in (select iv.voucherHeaderId.id from InstrumentVoucher iv where iv.instrumentHeaderId in (from InstrumentHeader ih where ih.statusId.id in ("+statusId+") ))");
		action=LIST;
		return LIST;
	}
	@ValidationErrorPage(value=LIST)
	@SkipValidation
	public String modify() 
	{
		paymentheader = getPayment();
		List<PaymentBean> miscBillList = paymentService.getMiscBillList(paymentheader);
		paymentService.getGlcodeIds();
		for(PaymentBean paymentObj:miscBillList){
			
			EgBillregister billregister = (EgBillregister)persistenceService.find("from EgBillregister where billnumber = ?", paymentObj.getBillNumber());
			totalBillList.add(billregister);
			deductionAmtMap.putAll(paymentService.getDeductionAmt(totalBillList, paymentObj.getExpType()));
			paidAmtMap.putAll(paymentService.getEarlierPaymentAmt(totalBillList, paymentObj.getExpType()));
			Iterator itr = paidAmtMap.keySet().iterator();
			long key;
			while (itr.hasNext()) {
				key = Long.valueOf(itr.next().toString());
				paidAmtMap.put(key, paidAmtMap.get(key).subtract(paymentObj.getPaymentAmt()));	
			}
		}
		billList = paymentService.getCSListForModify(totalBillList, deductionAmtMap, paidAmtMap, miscBillList);
		String  vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Payment");
		if (!"Auto".equalsIgnoreCase(vNumGenMode))
		{
			voucherNumberPrefix=paymentheader.getVoucherheader().getVoucherNumber().substring(0,Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
			voucherNumberSuffix=paymentheader.getVoucherheader().getVoucherNumber().substring(Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH,paymentheader.getVoucherheader().getVoucherNumber().length()));
		}
		addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive='1' ",paymentheader.getBankaccount().getBankbranch().getId()));
		//addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? ) and br.isactive='1' order by br.bank.name asc",paymentheader.getVoucherheader().getFundId()));
		loadbankBranch(paymentheader.getVoucherheader().getFundId());
		
		try {
			balance = paymentService.getAccountBalance(paymentheader.getBankaccount().getId().toString(), formatter.format(new Date()),paymentheader.getPaymentAmount(),paymentheader.getId());
		} catch (ParseException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("Error While formatting date","Error While formatting date")));
		}
		loadApproverUser(paymentheader.getVoucherheader().getType());
		return MODIFY;
		
	}
	
	@ValidationErrorPage(value=LIST)
	@SkipValidation
	public String modifyAdvancePayment(){
		paymentheader = (Paymentheader) persistenceService.find(" from Paymentheader where id=? ",paymentheader.getId());
		addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive='1' and fund.id=?",paymentheader.getBankaccount().getBankbranch().getId(),paymentheader.getBankaccount().getFund().getId()));
		loadbankBranch(paymentheader.getVoucherheader().getFundId());
		getAdvanceRequisitionDetails();
		String  vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Payment");
		if (!"Auto".equalsIgnoreCase(vNumGenMode)){
			voucherNumberPrefix=paymentheader.getVoucherheader().getVoucherNumber().substring(0,Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
			voucherNumberSuffix=paymentheader.getVoucherheader().getVoucherNumber().substring(Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH,paymentheader.getVoucherheader().getVoucherNumber().length()));
		}
		try {
			balance = paymentService.getAccountBalance(paymentheader.getBankaccount().getId().toString(), formatter.format(new Date()),paymentheader.getPaymentAmount(),paymentheader.getId());
		} catch (ParseException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("Error While formatting date","Error While formatting date")));
		}
		loadApproverUser(paymentheader.getVoucherheader().getType());
		return "advancePaymentModify";
	}

	@ValidationErrorPage(value=MODIFY)
	@SkipValidation
	public String cancelPayment()
	{
		paymentheader = (Paymentheader) persistenceService.find(" from Paymentheader where id=? ",paymentheader.getId());
		voucherHeader=paymentheader.getVoucherheader();
		voucherHeader.setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
		persistenceService.setType(CVoucherHeader.class);
		paymentWorkflowService.end(paymentheader, paymentService.getPosition());
		persistenceService.persist(voucherHeader);
		addActionMessage(getMessage("payment.cancel.success"));  
		action=parameters.get(ACTIONNAME)[0];
		return VIEW;      
	}          
	@ValidationErrorPage(value=MODIFY)
	@SkipValidation
	public String update()throws Exception
	{
		try
		{
			validateForUpdate();
			if(getFieldErrors().isEmpty())
			{
				paymentheader =paymentService.updatePayment(parameters, billList,paymentheader);
				getPaymentBills();
				sendForApproval();
				addActionMessage(getMessage("payment.transaction.success",new String[]{paymentheader.getVoucherheader().getVoucherNumber()}));
				//System.out.println("The retun value is :VIEW");
				loadApproverUser(voucherHeader.getType());
			}
			else
				return MODIFY;
		}
		catch(ValidationException e) 
		{
			addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? ) and br.isactive='1' order by br.bank.name asc",paymentheader.getVoucherheader().getFundId()));
			throw new ValidationException(e.getErrors());
		}
		catch(EGOVRuntimeException e) 
		{
			LOGGER.error(e.getMessage());
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getMessage()));
			throw new ValidationException(errors);
		}
		catch(Exception e) 
		{
			LOGGER.error(e.getMessage());
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getMessage()));
			throw new ValidationException(errors);
		}
   
		//action=MODIFY;
		return VIEW;
	}
	@ValidationErrorPage(value="advancePaymentModify")
	@SkipValidation
	public String updateAdvancePayment()throws Exception{
		paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where id=?",paymentheader.getId());
		getAdvanceRequisitionDetails();
		try{
			validateAdvancePayment();
			paymentheader.setBankaccount((Bankaccount) persistenceService.find("from Bankaccount where id=?",Integer.valueOf(parameters.get("paymentheader.bankaccount.id")[0])));
			addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive='1' and fund.id=?",paymentheader.getBankaccount().getBankbranch().getId(),paymentheader.getBankaccount().getFund().getId()));
			loadbankBranch(paymentheader.getBankaccount().getFund());
			if(getFieldErrors().isEmpty()){
				Integer userId = null;
				if (null != parameters.get("approverUserId") &&  Integer.valueOf(parameters.get("approverUserId")[0])!=-1 ) {
					userId = Integer.valueOf(parameters.get("approverUserId")[0]);
				}else {
					userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
				}
				paymentWorkflowService.transition(getValidActions().get(0).getName()+"|"+userId, paymentheader, paymentheader.getVoucherheader().getDescription());
				addActionMessage(getMessage("payment.voucher.approved",new String[]{paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwner())}));
				loadApproverUser(voucherHeader.getType());
			}else
				return "advancePaymentModify";
		}catch(ValidationException e){
			addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive='1' and fund.id=?",paymentheader.getBankaccount().getBankbranch().getId(),paymentheader.getBankaccount().getFund().getId()));
			loadbankBranch(paymentheader.getBankaccount().getFund());
			throw new ValidationException(e.getErrors());
		}catch(Exception e){
			addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive='1' and fund.id=?",paymentheader.getBankaccount().getBankbranch().getId(),paymentheader.getBankaccount().getFund().getId()));
			loadbankBranch(paymentheader.getBankaccount().getFund());
			LOGGER.error(e.getMessage());
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getMessage()));
			throw new ValidationException(errors);
		}
		return "advancePaymentView";
	}
	
	private void validateAdvancePayment()throws ValidationException,EGOVException,ParseException{
		if(paymentheader.getVoucherheader().getVoucherDate()==null || paymentheader.getVoucherheader().getVoucherDate().equals(""))
			throw new ValidationException(Arrays.asList(new ValidationError("payment.voucherdate.empty","payment.voucherdate.empty")));
		String  vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Payment");
		if(!"Auto".equalsIgnoreCase(vNumGenMode) &&( voucherNumberSuffix==null || voucherNumberSuffix.equals("")))
			throw new ValidationException(Arrays.asList(new ValidationError("payment.vouchernumber.empty","payment.vouchernumber.empty")));
		if(parameters.get("paymentheader.bankaccount.bankbranch.id")[0].equals("-1"))
			throw new ValidationException(Arrays.asList(new ValidationError("bankbranch.empty","bankbranch.empty")));
		if(parameters.get("paymentheader.bankaccount.id")[0].equals("-1"))
			throw new ValidationException(Arrays.asList(new ValidationError("bankaccount.empty","bankaccount.empty")));
	}


	private void validateForUpdate()throws ValidationException,EGOVException,ParseException
	{
		if(paymentheader.getVoucherheader().getVoucherDate()==null || paymentheader.getVoucherheader().getVoucherDate().equals(""))
			addFieldError("paymentheader.voucherheader.voucherDate",getMessage("payment.voucherdate.empty"));
		String  vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Payment");
		if(!"Auto".equalsIgnoreCase(vNumGenMode) &&( voucherNumberSuffix==null || voucherNumberSuffix.equals("")))
			addFieldError("paymentheader.voucherheader.voucherNumber",getMessage("payment.vouchernumber.empty"));
		if(parameters.get("paymentheader.bankaccount.bankbranch.id")[0].equals("-1"))
			addFieldError("paymentheader.bankaccount.bankbranch.id",getMessage("bankbranch.empty"));
		if(parameters.get("paymentheader.bankaccount.id")[0].equals("-1"))
			addFieldError("paymentheader.bankaccount.id",getMessage("bankaccount.empty"));
		if(billList==null)
			addFieldError("paymentheader.bankaccount.id",getMessage("bill.details.empty"));
		
		/*if(!parameters.get("paymentheader.bankaccount.id")[0].equals("-1") && paymentheader.getVoucherheader().getVoucherDate()!=null)
		{
			balance = paymentService.getAccountBalance(parameters.get("paymentheader.bankaccount.id")[0], formatter.format(paymentheader.getVoucherheader().getVoucherDate()),paymentheader.getPaymentAmount(),paymentheader.getId()); 
			if(BigDecimal.valueOf(Long.valueOf(parameters.get("grandTotal")[0])).compareTo(balance)==1)
				addFieldError("balance",getMessage("insufficient.bank.balance"));
		}*/
		int i=0;
		for(PaymentBean bean : billList)
		{
			if(bean.getIsSelected())
			{
				i++;
				if(bean.getPaymentAmt().compareTo(BigDecimal.ZERO)<=0)
					addFieldError("billList["+i+"].paymentAmt",getMessage("payment.amount.null"));
			}
		}
		if(i==0)
			addFieldError("paymentheader.bankaccount.id",getMessage("bill.details.empty"));
		if(paymentheader.getType().equalsIgnoreCase("RTGS"))
			paymentService.validateRTGSPaymentForModify(billList);
	}
	public void validate()
	{
		checkMandatory("fundId",Constants.FUND,voucherHeader.getFundId(),"voucher.fund.mandatory");
		checkMandatory("vouchermis.departmentid",Constants.DEPARTMENT,voucherHeader.getVouchermis().getDepartmentid(),"voucher.department.mandatory");
		checkMandatory("vouchermis.schemeid",Constants.SCHEME,voucherHeader.getVouchermis().getSchemeid(),"voucher.scheme.mandatory");
		checkMandatory("vouchermis.subschemeid",Constants.SUBSCHEME,voucherHeader.getVouchermis().getSubschemeid(),"voucher.subscheme.mandatory");
		checkMandatory("vouchermis.functionary",Constants.FUNCTIONARY,voucherHeader.getVouchermis().getFunctionary(),"voucher.functionary.mandatory");
		checkMandatory("fundsourceId",Constants.FUNDSOURCE,voucherHeader.getVouchermis().getFundsource(),"voucher.fundsource.mandatory");
		checkMandatory("vouchermis.divisionId",Constants.FIELD,voucherHeader.getVouchermis().getDivisionid(),"voucher.field.mandatory");
	}
	private void checkMandatory(String objectName,String fieldName,Object value,String errorKey) 
	{
		if(mandatoryFields.contains(fieldName) && value == null)
			addFieldError(objectName, getMessage(errorKey));
	}
	
	@SuppressWarnings("unchecked")
	@SkipValidation
	private void loadApproverUser(String type){
		String scriptName = "paymentHeader.nextDesg";
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		//defaultDept = voucherService.getCurrentDepartment().getId();
		//LOGGER.info("defaultDept :"+defaultDept);
		Map<String, Object>  map = voucherService.getDesgByDeptAndType("",scriptName);
		boolean isDeptMandate=false;
		List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'DEFAULTTXNMISATTRRIBUTES'");
		for (AppConfig appConfig : appConfigList) {
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
				String value = appConfigVal.getValue();
				String header=value.substring(0, value.indexOf("|"));
				String mandate = value.substring(value.indexOf("|")+1);
				if(header.equalsIgnoreCase("department")){
					if(mandate.equalsIgnoreCase("M")){
						isDeptMandate = true;
					}
					break;
				}
			}
		}
		if(isDeptMandate){
			//  If the department is mandatory show the logged in users assigned department only.
			addDropdownData("departmentList", voucherHelper.getAllAssgnDeptforUser());
		}else{
			addDropdownData("departmentList", masterCache.get("egi-department"));
		}
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
			defaultDept = dept.getId();
		}
		wfitemstate = map.get("wfitemstate")!=null?map.get("wfitemstate").toString():"";
		
		
	}
	
	protected String getMessage(String key) {
		return getText(key);
	}
	protected String getMessage(String key,String[] value) {
		return getText(key,value);
	}
	public String getExpType() {
		return expType;
	}
	public void setExpType(String expType) {
		this.expType = expType;
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
	public List<EgBillregister> getContractorBillList() {
		return contractorBillList;
	}
	public void setContractorBillList(List<EgBillregister> contractorBillList) {
		this.contractorBillList = contractorBillList;
	}
	public List<EgBillregister> getSupplierBillList() {
		return supplierBillList;
	}
	public void setSupplierBillList(List<EgBillregister> supplierBillList) {
		this.supplierBillList = supplierBillList;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Map<String, String> getPayeeMap() {
		return payeeMap;
	}
	public void setPayeeMap(Map<String, String> payeeMap) {
		this.payeeMap = payeeMap;
	}
	public List<EgBillregister> getTotalBillList() {
		return totalBillList;
	}
	public void setTotalBillList(List<EgBillregister> totalBillList) {
		this.totalBillList = totalBillList;
	}
	public List<Bankaccount> getBankaccountList() {
		return bankaccountList;
	}
	public void setBankaccountList(List<Bankaccount> bankaccountList) {
		this.bankaccountList = bankaccountList;
	}
	public Paymentheader getPaymentheader() {
		return paymentheader;
	}
	/**
	 * 
	 * @return
	 */
	public Paymentheader getPayment(){
		String paymentid=null;
		//System.out.println("Payment id is"+parameters.get(PAYMENTID));
		if(parameters.get(PAYMENTID)==null || "".equals(parameters.get(PAYMENTID)))
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
		if(paymentheader==null && paymentid!=null && !"".equals(paymentid)) 
		{
			paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where id=?", Long.valueOf(paymentid));
		}
		if(paymentheader==null)
		{
			paymentheader=new Paymentheader();
		}
		
		return paymentheader;
	}
	public void setPaymentheader(Paymentheader paymentheader) {
		this.paymentheader = paymentheader;
	}
	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public Map<Long,BigDecimal> getDeductionAmtMap() {
		return deductionAmtMap;
	}
	public void setDeductionAmtMap(Map<Long,BigDecimal> deductionAmtMap) {
		this.deductionAmtMap = deductionAmtMap;
	}
	public Map<Long,BigDecimal> getPaidAmtMap() {
		return paidAmtMap;
	}
	public void setPaidAmtMap(Map<Long,BigDecimal> paidAmtMap) {
		this.paidAmtMap = paidAmtMap;
	}
	public String getVoucherdate() {
		return voucherdate;
	}
	public void setVoucherdate(String voucherdate) {
		this.voucherdate = voucherdate;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public List<Miscbilldetail> getMiscBillList() {
		return miscBillList;
	}
	public void setMiscBillList(List<Miscbilldetail> miscBillList) {
		this.miscBillList = miscBillList;
	}
	public int getMiscount() {
		return miscount;
	}
	public void setMiscount(int miscount) {
		this.miscount = miscount;
	}
	public Integer getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(Integer bankaccount) {
		this.bankaccount = bankaccount;
	}
	public Integer getBankbranch() {
		return bankbranch;
	}
	public void setBankbranch(Integer bankbranch) {
		this.bankbranch = bankbranch;
	}
	public List<PaymentBean> getBillList() {
		return billList;
	}
	public void setBillList(List<PaymentBean> billList) {
		this.billList = billList;
	}
	public String getContractorIds() {
		return contractorIds;
	}
	public void setContractorIds(String contractorIds) {
		this.contractorIds = contractorIds;
	}
	public String getSalaryIds() {
		return salaryIds;
	}
	public void setSalaryIds(String salaryIds) {
		this.salaryIds = salaryIds;
	}
	public String getSupplierIds() {
		return supplierIds;
	}
	public void setSupplierIds(String supplierIds) {
		this.supplierIds = supplierIds;
	}
	public String getVouchernumber() {
		return vouchernumber;
	}
	public void setVouchernumber(String vouchernumber) {
		this.vouchernumber = vouchernumber;
	}
	public EgBillregister getBillregister() {
		return billregister;
	}
	public void setBillregister(EgBillregister billregister) {
		this.billregister = billregister;
	}
	public void setPaymentWorkflowService(SimpleWorkflowService<Paymentheader> paymentWorkflowService) {
		this.paymentWorkflowService = paymentWorkflowService;
	}

	public boolean isDepartmentDefault() {
		return isDepartmentDefault;
	}
	public void setDepartmentDefault(boolean isDepartmentDefault) {
		this.isDepartmentDefault = isDepartmentDefault;
	}
	public List<InstrumentHeader> getInstrumentHeaderList() {
		return instrumentHeaderList;
	}
	public void setInstrumentHeaderList(List<InstrumentHeader> instrumentHeaderList) {
		this.instrumentHeaderList = instrumentHeaderList;
	}
	public List<PaymentBean> getContractorList() {
		return contractorList;
	}
	public void setContractorList(List<PaymentBean> contractorList) {
		this.contractorList = contractorList;
	}
	public List<PaymentBean> getSupplierList() {
		return supplierList;
	}
	public void setSupplierList(List<PaymentBean> supplierList) {
		this.supplierList = supplierList;
	}
	public List<Paymentheader> getPaymentheaderList() {
		return paymentheaderList;
	}
	public void setPaymentheaderList(List<Paymentheader> paymentheaderList) {
		this.paymentheaderList = paymentheaderList;
	}
	public String getVoucherNumberPrefix() {
		return voucherNumberPrefix;
	}
	public void setVoucherNumberPrefix(String voucherNumberPrefix) {
		this.voucherNumberPrefix = voucherNumberPrefix;
	}
	public String getVoucherNumberSuffix() {
		return voucherNumberSuffix;
	}
	public void setVoucherNumberSuffix(String voucherNumberSuffix) {
		this.voucherNumberSuffix = voucherNumberSuffix;
	}
	public List<EgBillregister> getContingentBillList() {
		return contingentBillList;
	}
	public void setContingentBillList(List<EgBillregister> contingentBillList) {
		this.contingentBillList = contingentBillList;
	}
	public List<EgBillregister> getSalaryBillList() {
		return salaryBillList;
	}
	public void setSalaryBillList(List<EgBillregister> salaryBillList) {
		this.salaryBillList = salaryBillList;
	}
	public List<PaymentBean> getContingentList() {
		return contingentList;
	}
	public void setContingentList(List<PaymentBean> contingentList) {
		this.contingentList = contingentList;
	}
	public List<PaymentBean> getSalaryList() {
		return salaryList;
	}
	public void setSalaryList(List<PaymentBean> salaryList) {
		this.salaryList = salaryList;
	}
	public String getContingentIds() {
		return contingentIds;
	}
	public void setContingentIds(String contingentIds) {
		this.contingentIds = contingentIds;
	}
	public String getWfitemstate() {
		return wfitemstate;
	}
	public void setWfitemstate(String wfitemstate) {
		this.wfitemstate = wfitemstate;
	}
	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getDefaultDept() {
		return defaultDept;
	}
	public void setDefaultDept(Integer defaultDept) {
		this.defaultDept = defaultDept;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getTypeOfAccount() {
		return typeOfAccount;
	}
	public void setTypeOfAccount(String typeOfAccount) {
		this.typeOfAccount = typeOfAccount;
	}
	public void setAdvanceRequisition(List<EgAdvanceRequisition> advanceRequisition) {
		this.advanceRequisitionList = advanceRequisition;
	}
	public List<EgAdvanceRequisition> getAdvanceRequisitionList() {
		return advanceRequisitionList;
	}

	public void setDisableExpenditureType(boolean disableExpenditureType) {
		this.disableExpenditureType = disableExpenditureType;
	}

	public boolean isDisableExpenditureType() {
		return disableExpenditureType;
	}

	public void setVoucherHelper(VoucherHelper voucherHelper) {
		this.voucherHelper = voucherHelper;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
}
