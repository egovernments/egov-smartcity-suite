package org.egov.web.actions.voucher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptContext;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.billsaccounting.services.BillsAccountingService;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Relation;
import org.egov.commons.VoucherDetail;
import org.egov.commons.service.CommonsService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.masters.model.AccountEntity;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.receipt.ReceiptVoucher;
import org.egov.model.voucher.PreApprovedVoucher;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.services.bills.BillsService;
import org.egov.services.contra.ContraService;
import org.egov.services.receipt.ReceiptService;
import org.egov.services.voucher.VoucherService;
//import org.egov.dms.services.FileManagementService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.utils.ERPWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


//@Result(name=Action.SUCCESS, type=ServletRedirectResult.class, value = "preApprovedVoucher.action")
@ParentPackage("egov")
public class PreApprovedVoucherAction extends BaseFormAction
{
	private static final String EGF = "EGF";
	private static final String EMPTY_STRING="";
	private static final long serialVersionUID = 1L;
	private VoucherService voucherService;
	private CVoucherHeader voucherHeader = new CVoucherHeader();
	private EgBillregister egBillregister = new EgBillregister();
	private SimpleWorkflowService<CVoucherHeader> voucherWorkflowService;
	protected EmployeeService employeeService;
	private CommonsService commonsService;
	private EisCommonsService eisCommonsService;
	//private FileManagementService fileManagementService;
	private List<EgBillregister> preApprovedVoucherList;
	protected List<String> headerFields = new ArrayList<String>();
	protected List<String> mandatoryFields = new ArrayList<String>();
	protected ScriptService scriptExecutionService;
	private ApplicationContextBeanProvider beanProvider;
	public ScriptService getScriptExecutionService() {
		return scriptExecutionService;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

	private GenericHibernateDaoFactory genericDao;
	private BillsAccountingService billsAccountingService;
	private BillsService billsService;
	private static final Logger LOGGER = Logger.getLogger(PreApprovedVoucherAction.class);

	private final PreApprovedVoucher preApprovedVoucher = new PreApprovedVoucher();
	private List<PreApprovedVoucher> billDetailslist;
	private List<PreApprovedVoucher> subLedgerlist;
	private ReceiptVoucher receiptVoucher;
	private ContraJournalVoucher contraVoucher;
	private static final String ERROR="error";

	private static final String BILLID="billid";
	private static final String VOUCHEREDIT="voucheredit";
	private static final String VHID="vhid";
	private static final String CGN="cgn";
	private static final String VOUCHERQUERY=" from CVoucherHeader where id=?";
	private static final String VOUCHERQUERYBYCGN=" from CVoucherHeader where cgn=?";
	private static final String ACCDETAILTYPEQUERY=" from Accountdetailtype where id=?";
	private static final String ACTIONNAME="actionName";
	private String values="",from="";
	private String methodName="";
	private Integer departmentId;
	private String type;
	private String wfitemstate;
	private String finConstExpendTypeContingency;
	SimpleWorkflowService<ReceiptVoucher> receiptWorkflowService;
	SimpleWorkflowService<ContraJournalVoucher> contraWorkflowService;
	private SequenceGenerator sequenceGenerator;
	private Map<String, Object> billDetails;
	private VoucherHelper voucherHelper;
	private String fileSummary;
	private String userName;
	private boolean notifyOnCancel;
	public void setContraWorkflowService(SimpleWorkflowService<ContraJournalVoucher> contraWorkflowService) {
		this.contraWorkflowService = contraWorkflowService;
	}

	public void setReceiptWorkflowService(SimpleWorkflowService<ReceiptVoucher> receiptWorkflowService) {
		this.receiptWorkflowService = receiptWorkflowService;
	}

	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public PreApprovedVoucherAction(){
		addRelatedEntity(VoucherConstant.GLCODE, CChartOfAccounts.class);
		addRelatedEntity("detailType", Accountdetailtype.class);
	}

	@Override
	public Object getModel() {
		return preApprovedVoucher;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void prepare() {
		super.prepare();
		addDropdownData("departmentList", Collections.EMPTY_LIST);
		addDropdownData("designationList", Collections.EMPTY_LIST);
		addDropdownData("userList", Collections.EMPTY_LIST);
		if(parameters.containsKey("vhid")){
			if(!parameters.get("vhid")[0].isEmpty()){
			egBillregister = (EgBillregister) getPersistenceService().find(" from EgBillregister br where br.egBillregistermis.voucherHeader.id=?", Long.valueOf(parameters.get("vhid")[0]));
			if(egBillregister!=null){
				setUserName(egBillregister.getCreatedBy().getUserName());
			}
			}
		}
	}




	@SkipValidation
	public String list()
	{
		if(getValidActions("designation").size()==0)
		{
			addActionError(getText("pjv.designation.notmatching"));
		}
		else
		{
			final EgwStatus egwStatus = commonsService.getStatusByModuleAndCode("SBILL", "Approved");
			getHeaderMandateFields();
			if(isFieldMandatory("department"))
				preApprovedVoucherList = getPersistenceService().findAllBy(" from EgBillregister br where br.status=? and br.egBillregistermis.egDepartment.id=? and ( br.egBillregistermis.voucherHeader is null or br.egBillregistermis.voucherHeader in (from CVoucherHeader vh where vh.status =4 )) ", egwStatus,getCurrentDepartment().getId());
			else
				preApprovedVoucherList = getPersistenceService().findAllBy(" from EgBillregister br where br.status=? and ( br.egBillregistermis.voucherHeader is null or br.egBillregistermis.voucherHeader in (from CVoucherHeader vh where vh.status =4 )) ", egwStatus);

			LOGGER.debug(preApprovedVoucherList);
		}
		return "list";
	}
	@SkipValidation
	public String voucher()
	{
		egBillregister = (EgBillregister) getPersistenceService().find(" from EgBillregister where id=?", Long.valueOf(parameters.get(BILLID)[0]));
		LOGGER.debug("egBillregister=="+egBillregister);
		final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","pjv_saveasworkingcopy_enabled");
		final String pjv_wc_enabled = appList.get(0).getValue();
		// loading aprover user info
		type = egBillregister.getExpendituretype();
		getHeaderMandateFields();
		loadApproverUser(type);
		if("Y".equals(pjv_wc_enabled)){
			try {
				// loading the bill detail info.
				getMasterDataForBillVoucher();
			} catch (Exception e) {
				 List<ValidationError> errors=new ArrayList<ValidationError>();
				 errors.add(new ValidationError("exp",e.getMessage()));
				 throw new ValidationException(errors);
			}
			return VOUCHEREDIT;
		}
		else{
			try {
				// loading the bill detail info.
				getMasterDataForBill();
			} catch (Exception e) {
				 List<ValidationError> errors=new ArrayList<ValidationError>();
				 errors.add(new ValidationError("exp",e.getMessage()));
				 throw new ValidationException(errors);
			}
			return "billview";
		}

	}

	@SkipValidation
	public String loadvoucher()
	{
		voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
		LOGGER.debug("voucherHeader=="+voucherHeader);
		List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","pjv_saveasworkingcopy_enabled");
		String pjv_wc_enabled = appList.get(0).getValue();
		type = billsService.getBillTypeforVoucher(voucherHeader);
		try {
			// loading the bill detail info.
			getMasterDataForBillVoucher();
		} catch (Exception e) {
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getMessage()));
			 throw new ValidationException(errors);
		}
		getHeaderMandateFields();
		if(type==null)
		{
			return "view";
		}
		else
		{
			loadApproverUser(type);
			if("Y".equals(pjv_wc_enabled))
				return VOUCHEREDIT;
			else
				return "voucherview";
		}
	}

	@SuppressWarnings("unchecked")
	private void loadApproverUser(String type){
		String scriptName = "billvoucher.nextDesg";
		departmentId = voucherService.getCurrentDepartment().getId();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		Map<String, Object>  map = voucherService.getDesgByDeptAndType(type, scriptName);
		if(null == map.get("wfitemstate")){
		//  If the department is mandatory show the logged in users assigned department only.
			if(mandatoryFields.contains("department")){
				addDropdownData("departmentList", voucherHelper.getAllAssgnDeptforUser());
			}else{
				addDropdownData("departmentList", masterCache.get("egi-department"));
			}
			addDropdownData("designationList", (List<DesignationMaster>)map.get("designationList"));
			wfitemstate="";
		}else{
			wfitemstate = map.get("wfitemstate").toString();
		}


	}
	
	private void loadApproverUserForContra(String type){
		String scriptName = "ContraJournalVoucher.nextDesg";
		DepartmentImpl dept= voucherService.getCurrentDepartment();
		Script depScript=(Script)persistenceService.findAllByNamedQuery(Script.BY_NAME, "ContraJournalVoucher.nextDept").get(0);
		ScriptContext scriptContext = scriptExecutionService.createContext("wfItem",contraVoucher,"dept",dept,"persistenceService",persistenceService);
		List<DepartmentImpl> depts =(List<DepartmentImpl>)scriptExecutionService.executeScript(depScript.getName(), scriptContext);
			addDropdownData("departmentList", depts);
			addDropdownData("designationList", Collections.EMPTY_LIST);
			if(contraVoucher.getState().getValue().equalsIgnoreCase("CHECKED"))
					wfitemstate="END";
			else
			  wfitemstate="" ;       
				
	}


	@SkipValidation
	public String loadvoucherview() throws EGOVException
	{
		billDetails = new HashMap<String, Object>();
		if(parameters.get("from")!=null && FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT.equals(parameters.get("from")[0]))
		{
			receiptVoucher= (ReceiptVoucher) persistenceService.find("from ReceiptVoucher where id=?", Long.valueOf(parameters.get(VHID)[0]));
			voucherHeader = receiptVoucher.getVoucherHeader();
			from=FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT;
		}
		else if(parameters.get("from")!=null && FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA.equals(parameters.get("from")[0]))
		{
			contraVoucher = (ContraJournalVoucher) persistenceService.find(" from ContraJournalVoucher where id=?",Long.valueOf(parameters.get(VHID)[0]));
			voucherHeader = contraVoucher.getVoucherHeaderId();
			from=FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA;
			loadApproverUserForContra("contra");
		}
		else
		{
			voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
			from=FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL;
		}
		/*List<VoucherDetail> voucherDetailList=new ArrayList<VoucherDetail>();
		voucherDetailList = persistenceService.findAllBy("from VoucherDetail where voucherHeaderId.id=? order by decode(debitAmount,null,0, debitAmount) desc ,decode(creditAmount,null,0, creditAmount) asc",voucherHeader.getId());
		billDetails.put("voucherDetailList", voucherDetailList);*/
		getMasterDataForBillVoucher();
		getHeaderMandateFields();
		return "view";
	}
	@SkipValidation
	public List<Action> getValidActions(){
		if(FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT.equals(parameters.get("from")[0]))
			return receiptWorkflowService.getValidActions(receiptVoucher);
		else if(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA.equals(parameters.get("from")[0]))
			return contraWorkflowService.getValidActions(contraVoucher);
		else
			return null;
 	}
	@SkipValidation
	public String approval() throws EGOVException
	{    
		System.out.println("inside Appproval +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
				 "classpath:org/serviceconfig-Bean.xml","classpath:org/egov/infstr/beanfactory/globalApplicationContext.xml" });
		voucherHeader = (CVoucherHeader) persistenceService.find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
		if(voucherHeader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT))
		{
			receiptVoucher = (ReceiptVoucher) persistenceService.find("from ReceiptVoucher where id=?", Long.valueOf(parameters.get(VHID)[0]));
			ReceiptService receiptService = (ReceiptService) applicationContext.getBean("receiptService");
			receiptWorkflowService.transition(parameters.get(ACTIONNAME)[0], receiptVoucher, parameters.get("comments")[0]);
			receiptService.persist(receiptVoucher);
			addActionMsg(receiptVoucher.getState().getValue(), receiptVoucher.getState().getOwner());
		}
		else if(voucherHeader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA))
		{
			System.out.println("inside Appproval +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+voucherHeader.getType());
			contraVoucher = (ContraJournalVoucher) persistenceService.find("from ContraJournalVoucher where id=?", Long.valueOf(parameters.get("contraId")[0]));
			ContraService contraService = (ContraService) applicationContext.getBean("contraService");
			Integer	 userId=null;
				 userId = parameters.get("approverUserId")!=null?Integer.valueOf(parameters.get("approverUserId")[0]):
					 											Integer.valueOf( EGOVThreadLocals.getUserId());
			System.out.println("-----------------------------------parameters.get(ACTIONNAME)[0]+|+userId"+parameters.get(ACTIONNAME)[0]+"|"+userId);
			contraWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, contraVoucher, parameters.get("comments")[0]);
			contraService.persist(contraVoucher);
			addActionMsgForContra(contraVoucher.getState().getValue(), contraVoucher.getState().getOwner());
		}
		
		else if(voucherHeader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL) || voucherHeader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA))
		{
			voucherWorkflowService.transition(parameters.get(ACTIONNAME)[0], voucherHeader,parameters.get("comments")[0]);
			voucherService.persist(voucherHeader);
			addActionMsg(voucherHeader.getState().getValue(), voucherHeader.getState().getOwner());
		}
		methodName="approval";
		getMasterDataForBillVoucher();
		return "view";
	}
	private void addActionMsg(final String stateValue,final Position pos)
	{
		if(parameters.get(ACTIONNAME)[0].contains("approve"))
			if("END".equals(stateValue))
				addActionMessage(getText("pjv.voucher.final.approval",new String[]{"The File has been approved"}));
			else
				addActionMessage(getText("pjv.voucher.approved",new String[]{voucherService.getEmployeeNameForPositionId(pos)}));
		else if(parameters.get(ACTIONNAME)[0].contains("co_reject") || parameters.get(ACTIONNAME)[0].contains("aa_reject") || "END".equals(stateValue) )
			addActionMessage(getText("voucher.cancelled"));
		else
			addActionMessage(getText("pjv.voucher.rejected",new String[]{voucherService.getEmployeeNameForPositionId(pos)}));

	}
	private void addActionMsgForContra(final String stateValue,final Position pos)
	{
		if(parameters.get(ACTIONNAME)[0].contains("Approve"))
			if("END".equals(stateValue))
				addActionMessage(getText("pjv.voucher.final.approval",new String[]{"The File has been approved"}));
			else
				addActionMessage(getText("pjv.voucher.approved",new String[]{voucherService.getEmployeeNameForPositionId(pos)}));
		else if(parameters.get(ACTIONNAME)[0].contains("Reject") || parameters.get(ACTIONNAME)[0].contains("reject") || "END".equals(stateValue) )
			addActionMessage(getText("voucher.cancelled"));
		else if(parameters.get(ACTIONNAME)[0].contains("Check")||parameters.get(ACTIONNAME)[0].contains("Create"))
				addActionMessage(getText("pjv.voucher.approved",new String[]{voucherService.getEmployeeNameForPositionId(pos)}));

	}

	@SkipValidation
	public String loadvoucherviewByCGN() throws EGOVException
	{
		billDetails = new HashMap<String, Object>();
		voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERYBYCGN, parameters.get(CGN)[0]);
		/*List<VoucherDetail> voucherDetailList=new ArrayList<VoucherDetail>();
		voucherDetailList = persistenceService.findAllBy("from VoucherDetail where voucherHeaderId.id=? order by decode(debitAmount,null,0, debitAmount) desc ,decode(creditAmount,null,0, creditAmount) asc",voucherHeader.getId());
		billDetails.put("voucherDetailList", voucherDetailList);*/
		getHeaderMandateFields();
		getMasterDataForBillVoucher();
		return "view";
	}

	public String save() throws ValidationException
	{
		try
		{
			LOGGER.debug("bill id======="+parameters.get(BILLID)[0]);
			methodName="save";
			// check budgetary check
			egBillregister = (EgBillregister) getPersistenceService().find(" from EgBillregister where id=?", Long.valueOf(parameters.get(BILLID)[0]));
			getMasterDataForBill();
			//Check if budgetary Appropriation is enabled for the application. Only if required we need to do the check.
			List<AppConfigValues> list =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"budgetCheckRequired");
			Long vhid = null;
			if(list.isEmpty())
				throw new ValidationException(EMPTY_STRING,"budgetCheckRequired is not defined in AppConfig");
			boolean checkReq=false;
				vhid =billsAccountingService.createPreApprovedVoucherFromBill(Integer.parseInt(parameters.get(BILLID)[0]));
				LOGGER.debug("voucher id======="+vhid);
			voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, vhid);
			voucherHeader.setModifiedDate(new Date());
			voucherWorkflowService.start(voucherHeader, getPosition(), parameters.get("comments")[0]);
			sendForApproval();
			addActionMessage(getText(egBillregister.getExpendituretype()+".voucher.created",new String[]{voucherHeader.getVoucherNumber(),voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwner())}));
			//If 'Y' then  need for budget checking and budgetary number generation.
			if("Y".equalsIgnoreCase(((AppConfigValues)list.get(0)).getValue()) && null == egBillregister.getEgBillregistermis().getBudgetaryAppnumber())
			{
				Set<VoucherDetail> vDetailSet =voucherHeader.getVoucherDetail();
				for(VoucherDetail detail:vDetailSet)
				{
					CChartOfAccounts coa=(CChartOfAccounts) persistenceService.find("from CChartOfAccounts where glcode='"+detail.getGlCode()+"'");
					if(null!=coa &&  null!= coa.getBudgetCheckReq() &&  coa.getBudgetCheckReq()==1){
						checkReq=true;
						break;
					}
				}
				if(checkReq){
					Script sequenceNumberScript = scriptExecutionService.findAllByNamedQuery(Script.BY_NAME, "egf.reappropriation.sequence.generator").get(0);
					StringBuffer finyearQuery = new StringBuffer();
					finyearQuery.append("from CFinancialYear where  startingDate <= '").append(Constants.DDMMYYYYFORMAT1.format(voucherHeader.getVoucherDate())).append("' AND endingDate >='").append(Constants.DDMMYYYYFORMAT1.format(voucherHeader.getVoucherDate())).append("'");
					CFinancialYear financialyear =(CFinancialYear) persistenceService.find(finyearQuery.toString());
					String budgetNumber=(String)scriptExecutionService.executeScript(sequenceNumberScript,scriptExecutionService.createContext("wfItem",financialyear,"sequenceGenerator",sequenceGenerator));
					voucherHeader.getVouchermis().setBudgetaryAppnumber(budgetNumber);
					addActionMessage(getText("budget.recheck.sucessful",new String[]{budgetNumber}));
				}
			}


		}catch(ValidationException e)
		{
			LOGGER.error(e.getErrors());
			throw new ValidationException(e.getErrors());
		}
		catch(Exception e)
		{
			if(e.getCause().getClass().equals(ValidationException.class))
			{
				ValidationException s = (ValidationException)e;
				throw new ValidationException(s.getErrors());
			}
			LOGGER.error(e.getMessage());
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getCause().getMessage()));
			throw new ValidationException(errors);
		}
		return "billview";
	}

	public String update() throws ValidationException
	{
		LOGGER.debug("voucher id======="+parameters.get(VHID)[0]);
		methodName="update";
		sendForApproval();
		if(notifyOnCancel){
			notifyAndCancel();
		}
		if(parameters.get(ACTIONNAME)[0].contains("aa_reject")){
			addActionMessage(getText("billVoucher.file.canceled"));
		}else{
			if(parameters.get(ACTIONNAME)[0].contains("approve")){
				if("END".equals(voucherHeader.getState().getValue()))
					addActionMessage(getText("pjv.voucher.final.approval",new String[]{"The File has been approved"}));
				else
					addActionMessage(getText("pjv.voucher.approved",new String[]{voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwner())}));
			}
			else{
				addActionMessage(getText("pjv.voucher.rejected",new String[]{voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwner())}));
			}
		}


		return "message";
	}
	
	public void notifyAndCancel() throws ValidationException
	{
		egBillregister = (EgBillregister) getPersistenceService().find(" from EgBillregister br where br.egBillregistermis.voucherHeader.id=?", Long.valueOf(parameters.get(VHID)[0]));
		if(voucherHeader.getId()==null)
			voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
		final HashMap<String, String> fileDetails = new HashMap<String, String>();
		fileDetails.put("fileCategory", "INTER DEPARTMENT");
		fileDetails.put("filePriority", "MEDIUM");
		fileDetails.put("fileHeading", "Cancel of Bill Voucher");
		fileDetails.put("fileSummary", getFileSummary());
		fileDetails.put("fileSource", "INTER DEPARTMENT");
		fileDetails.put("senderAddress", "");
		fileDetails.put("senderName", voucherHeader.getCreatedBy().getUserName());
		fileDetails.put("senderPhone", "");
		fileDetails.put("senderEmail", "");
		//fileManagementService=(FileManagementService)beanProvider.getBean("fileMgmtService", ERPWebApplicationContext.ContextName.dms, true);
		//fileManagementService.generateFileNotification(fileDetails, egBillregister.getCreatedBy().getId().toString());
		addActionMessage(getText("billVoucher.file.canceled.notified", new String[]{egBillregister.getCreatedBy().getFirstName().toString()}));
	}

	public String saveAsWorkingCopy() throws ValidationException
	{
		methodName="saveAsWorkingCopy";
		if(parameters.get(VHID)[0].equals(""))
		{
			egBillregister = (EgBillregister) getPersistenceService().find(" from EgBillregister where id=?", Long.valueOf(parameters.get(BILLID)[0]));
			final boolean budgetcheck = voucherService.budgetaryCheck(egBillregister);
			if(budgetcheck)
			{
				LOGGER.debug("saveAsWorkingCopy==========="+billDetailslist+subLedgerlist);
				final Long vhid =billsAccountingService.createPreApprovedVoucherFromBillForPJV(Integer.valueOf(parameters.get(BILLID)[0]),billDetailslist,subLedgerlist);
				LOGGER.debug("voucher id======="+vhid);
				voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, vhid);
				voucherWorkflowService.start(voucherHeader, getPosition());
				addActionMessage(getText("pjv.voucher.wc.created",new String[]{voucherHeader.getVoucherNumber()}));
			}
			else
				addActionError(getText("pjv.budgetcheck.failed"));
		}
		else
		{
			final Long vhid = Long.valueOf(parameters.get(VHID)[0]);
			voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, vhid);
			billsAccountingService.updatePJV(voucherHeader, billDetailslist, subLedgerlist);
			addActionMessage(getText("pjv.voucher.updated"));
		}
		return "voucheredit";
	}
	public void sendForApproval()
	{
		LOGGER.debug("PreApprovedVoucherAction | sendForApproval | Start");
		if(voucherHeader.getId()==null)
			voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
		LOGGER.debug("Voucherheader=="+voucherHeader.getId()+", actionname="+parameters.get(ACTIONNAME)[0]);
		Integer userId = null;
		if(parameters.get("actionName")[0].contains("approve")){
			 userId = parameters.get("approverUserId")!=null?Integer.valueOf(parameters.get("approverUserId")[0]):
				 											Integer.valueOf( EGOVThreadLocals.getUserId());
		}
		else{
			userId = voucherHeader.getCreatedBy().getId();
		}

		LOGGER.debug("User selected id is : "+userId);
		voucherWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, voucherHeader,parameters.get("comments")[0]);
		voucherService.persist(voucherHeader);
	}

	public String sendForApprovalForWC() throws ValidationException
	{
		methodName="sendForApprovalForWC";
		sendForApproval();
		if(parameters.get(ACTIONNAME)[0].contains("approve"))
			if("END".equals(voucherHeader.getState().getValue()))
				addActionMessage(getText("pjv.voucher.final.approval",new String[]{"The File has been approved"}));
			else
				addActionMessage(getText("pjv.voucher.approved",new String[]{voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwner())}));
		else
			addActionMessage(getText("pjv.voucher.rejected",new String[]{voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwner())}));
		return "message";
	}

	public List<EgBillregister> getPreApprovedVoucherList() {
		return preApprovedVoucherList;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public void validate()
	{
		LOGGER.debug("validate==============");
	/*	if(parameters.get("comments")[0]!=null && parameters.get("comments")[0].length()>1024)
			addFieldError("comments", "Max. length exceeded");*/
	}

	public void setEgBillregister(final EgBillregister egBillregister) {
		this.egBillregister = egBillregister;
	}
	public DepartmentImpl getCurrentDepartment()
	{
		return voucherService.getCurrentDepartment();
	}
	public String getMasterName(final String name)
	{
		String val="";
		if(voucherHeader.getVoucherDetail().size()>0)
		{
			if(name.equals("fund") && voucherHeader.getFundId()!=null)
				val = voucherHeader.getFundId().getName();
			else if(name.equals("fundsource") && voucherHeader.getVouchermis().getFundsource()!=null)
				val = voucherHeader.getVouchermis().getFundsource().getName();
			else if(name.equals("department") && voucherHeader.getVouchermis().getDepartmentid()!=null)
				val = voucherHeader.getVouchermis().getDepartmentid().getDeptName();
			else if(name.equals("scheme") && voucherHeader.getVouchermis().getSchemeid()!=null)
				val = voucherHeader.getVouchermis().getSchemeid().getName();
			else if(name.equals("subscheme") && voucherHeader.getVouchermis().getSubschemeid()!=null)
				val = voucherHeader.getVouchermis().getSubschemeid().getName();
			else if(name.equals("field") && voucherHeader.getVouchermis().getDivisionid()!=null)
				val = voucherHeader.getVouchermis().getDivisionid().getName();
			else if(name.equals("functionary") && voucherHeader.getVouchermis().getFunctionary()!=null)
				val = voucherHeader.getVouchermis().getFunctionary().getName();
			else if("narration".equals(name))
				val = voucherHeader.getDescription();
			else if("billnumber".equals(name))
				val = ((EgBillregister)getPersistenceService().find(" from EgBillregister br where br.egBillregistermis.voucherHeader=?",voucherHeader)).getBillnumber();
		}
		else
		{
			if(name.equals("fund") && egBillregister.getEgBillregistermis().getFund()!=null)
				val = egBillregister.getEgBillregistermis().getFund().getName();
			else if(name.equals("fundsource") && egBillregister.getEgBillregistermis().getFundsource()!=null)
				val = egBillregister.getEgBillregistermis().getFundsource().getName();
			else if(name.equals("department") && egBillregister.getEgBillregistermis().getEgDepartment()!=null)
				val = egBillregister.getEgBillregistermis().getEgDepartment().getDeptName();
			else if(name.equals("scheme") && egBillregister.getEgBillregistermis().getScheme()!=null)
				val = egBillregister.getEgBillregistermis().getScheme().getName();
			else if(name.equals("subscheme") &&  egBillregister.getEgBillregistermis().getSubScheme()!=null)
				val = egBillregister.getEgBillregistermis().getSubScheme().getName();
			else if(name.equals("field") && egBillregister.getEgBillregistermis().getFieldid()!=null)
				val = egBillregister.getEgBillregistermis().getFieldid().getName();
			else if(name.equals("functionary") && egBillregister.getEgBillregistermis().getFunctionaryid()!=null)
				val = egBillregister.getEgBillregistermis().getFunctionaryid().getName();
			else if("narration".equals(name))
				val = egBillregister.getEgBillregistermis().getNarration();
			else if("billnumber".equals(name))
				val = egBillregister.getBillnumber();
		}
		return val;
	}
	public String getSourcePath(){
		String sourcePath;
		if(voucherHeader.getVoucherDetail().size()>0)
		{
			sourcePath = voucherHeader.getVouchermis().getSourcePath();
		}else{
			sourcePath = egBillregister.getEgBillregistermis().getSourcePath();
		}
		LOGGER.debug("Source Path = "+ sourcePath);
		return sourcePath;
	}
	public Map<String, Object> getMasterName() throws EGOVException{
		LOGGER.debug("getmastername===============");
		final Map<String, Object> names = new HashMap<String, Object>();
		// to get the ledger.
		List<CGeneralLedger> gllist = getPersistenceService().findAllBy(" from CGeneralLedger where voucherHeaderId.id=? order by voucherlineId", Long.valueOf(voucherHeader.getId()+""));
		Map<String,Object> temp = null;
		List<Map<String,Object>> tempList=new ArrayList<Map<String,Object>>();
		for(CGeneralLedger gl :gllist)
		{
			if(gl.getFunctionId()!=null)
				names.put(gl.getGlcodeId().getGlcode(), ((CFunction)getPersistenceService().find(" from CFunction where id=?",Long.valueOf(gl.getFunctionId()))).getName());

			// get subledger.
			List<CGeneralLedgerDetail> gldetailList = getPersistenceService().findAllBy("from CGeneralLedgerDetail where generalLedgerId=?", Integer.valueOf(gl.getId()+""));
			if(gldetailList!=null && !gldetailList.isEmpty())
			{
				for(CGeneralLedgerDetail gldetail:gldetailList)
				{
					temp = new HashMap<String,Object>();
					temp = getAccountDetails(gldetail.getDetailTypeId(),gldetail.getDetailKeyId(),temp);
					temp.put(Constants.GLCODE, gl.getGlcode());
					temp.put("amount", gldetail.getAmount());
					tempList.add(temp);
				}
				names.put("tempList", tempList);
			}
		}
		LOGGER.debug("voucher details =="+names);
		return names;
	}

	public void getMasterDataForBill() throws EGOVException
	{
		billDetails = new HashMap<String, Object>();
		CChartOfAccounts coa=null;
		Map<String,Object> temp = null;
		Map<String,Object> payeeMap= null;
		List<Map<String,Object>> tempList=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> payeeList=new ArrayList<Map<String,Object>>();

		List<EgBilldetails> egBillDetails = persistenceService.findAllBy("from EgBilldetails where  egBillregister.id=? order by  decode(debitamount,null,0, debitamount) desc ,decode(creditamount,null,0, creditamount) asc", egBillregister.getId());

		for(EgBilldetails billdetails:egBillDetails)
		{
			temp = new HashMap<String,Object>();
			if(billdetails.getFunctionid()!=null)
				temp.put(Constants.FUNCTION, ((CFunction)getPersistenceService().find("from CFunction where id=?", Long.valueOf(billdetails.getFunctionid()+""))).getName());
			coa = (CChartOfAccounts) getPersistenceService().find("from CChartOfAccounts where id=?", Long.valueOf(billdetails.getGlcodeid()+""));
			temp.put(Constants.GLCODE, coa.getGlcode());
			temp.put("accounthead", coa.getName());
			temp.put(Constants.DEBITAMOUNT, billdetails.getDebitamount()==null?0:billdetails.getDebitamount());
			temp.put(Constants.CREDITAMOUNT, billdetails.getCreditamount()==null?0:billdetails.getCreditamount());
			temp.put("billdetailid", billdetails.getId());
			tempList.add(temp);

			for(EgBillPayeedetails payeeDetails:billdetails.getEgBillPaydetailes())
			{
				payeeMap = new HashMap<String,Object>();
				payeeMap = getAccountDetails(payeeDetails.getAccountDetailTypeId(),payeeDetails.getAccountDetailKeyId(),payeeMap);
				payeeMap.put(Constants.GLCODE, coa.getGlcode());
				payeeMap.put(Constants.DEBITAMOUNT, payeeDetails.getDebitAmount()==null?0:payeeDetails.getDebitAmount());
				payeeMap.put(Constants.CREDITAMOUNT, payeeDetails.getCreditAmount()==null?0:payeeDetails.getCreditAmount());
				payeeList.add(payeeMap);
			}
		}
		billDetails.put("tempList", tempList);
		billDetails.put("payeeList", payeeList);
	}

	public void getMasterDataForBillVoucher() throws EGOVException
	{
		billDetails = new HashMap<String, Object>();
		CChartOfAccounts coa=null;
		Map<String,Object> temp = null;
		Map<String,Object> payeeMap= null;
		List<Map<String,Object>> tempList=new ArrayList<Map<String,Object>>();
		List<PreApprovedVoucher> payeeList=new ArrayList<PreApprovedVoucher>();
		List<Long> glcodeIdList=new ArrayList<Long>();
		List<Accountdetailtype> detailtypeIdList=new ArrayList<Accountdetailtype>();
		PreApprovedVoucher subledger = null;

		if(voucherHeader.getVoucherDetail().size()>0)
		{
			List<CGeneralLedger> gllist = getPersistenceService().findAllBy(" from CGeneralLedger where voucherHeaderId.id=? order by decode(debitAmount,null,0, debitAmount) desc ,decode(creditAmount,null,0, creditAmount) asc", Long.valueOf(voucherHeader.getId()+""));

			for(CGeneralLedger gl:gllist)
			{
				temp = new HashMap<String,Object>();
				if(gl.getFunctionId()!=null)
				{
					temp.put(Constants.FUNCTION, ((CFunction)getPersistenceService().find("from CFunction where id=?", Long.valueOf(gl.getFunctionId()))).getName());
					temp.put("functionid",gl.getFunctionId());
				}
				coa = (CChartOfAccounts) getPersistenceService().find("from CChartOfAccounts where glcode=?",gl.getGlcode());
				temp.put("glcodeid", coa.getId());
				glcodeIdList.add(coa.getId());
				temp.put(Constants.GLCODE, coa.getGlcode());
				temp.put("accounthead", coa.getName());
				temp.put(Constants.DEBITAMOUNT, gl.getDebitAmount()==null?0:gl.getDebitAmount());
				temp.put(Constants.CREDITAMOUNT, gl.getCreditAmount()==null?0:gl.getCreditAmount());
				temp.put("billdetailid", gl.getId());
				tempList.add(temp);

				List<CGeneralLedgerDetail> gldetailList = getPersistenceService().findAllBy("from CGeneralLedgerDetail where generalLedgerId=?", Integer.valueOf(gl.getId()+""));
				for(CGeneralLedgerDetail gldetail:gldetailList)
				{
					subledger = new PreApprovedVoucher();
					subledger.setGlcode(coa);
					Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,gldetail.getDetailTypeId());
					detailtypeIdList.add(detailtype);
					subledger.setDetailType(detailtype);
					payeeMap = new HashMap<String,Object>();
					payeeMap = getAccountDetails(gldetail.getDetailTypeId(),gldetail.getDetailKeyId(),payeeMap);
					subledger.setDetailKey(payeeMap.get(Constants.DETAILKEY)+"");
					subledger.setDetailCode(payeeMap.get(Constants.DETAILCODE)+"");
					subledger.setDetailKeyId(gldetail.getDetailKeyId());
					subledger.setAmount(gldetail.getAmount());
					subledger.setFunctionDetail(temp.get("function")!=null?temp.get("function").toString():"");
					if(gl.getDebitAmount()==null || gl.getDebitAmount()==0)
					{
						subledger.setDebitAmount(BigDecimal.ZERO);
						subledger.setCreditAmount(gldetail.getAmount());
					}
					else
					{
						subledger.setDebitAmount(gldetail.getAmount());
						subledger.setCreditAmount(BigDecimal.ZERO);
					}
					payeeList.add(subledger);
				}
			}
			/*// this is for only vouchers, which do not have the bills
			List<VoucherDetail> voucherDetailList=new ArrayList<VoucherDetail>();
			voucherDetailList = persistenceService.findAllBy("from VoucherDetail where voucherHeaderId.id=? order by decode(debitAmount,null,0, debitAmount) desc ,decode(creditAmount,null,0, creditAmount) asc",voucherHeader.getId());
			billDetails.put("voucherDetailList", voucherDetailList);*/
		}
		else
		{
			for(EgBilldetails billdetails:egBillregister.getEgBilldetailes())
			{
				temp = new HashMap<String,Object>();
				if(billdetails.getFunctionid()!=null)
				{
					temp.put(Constants.FUNCTION, ((CFunction)getPersistenceService().find("from CFunction where id=?", Long.valueOf(billdetails.getFunctionid()+""))).getName());
					temp.put("functionid",billdetails.getFunctionid());
				}
				coa = (CChartOfAccounts) getPersistenceService().find("from CChartOfAccounts where id=?", Long.valueOf(billdetails.getGlcodeid()+""));
				temp.put("glcodeid", coa.getId());
				glcodeIdList.add(coa.getId());
				temp.put(Constants.GLCODE, coa.getGlcode());
				temp.put("accounthead", coa.getName());
				temp.put(Constants.DEBITAMOUNT, billdetails.getDebitamount()==null?0:billdetails.getDebitamount());
				temp.put(Constants.CREDITAMOUNT, billdetails.getCreditamount()==null?0:billdetails.getCreditamount());
				temp.put("billdetailid", billdetails.getId());
				tempList.add(temp);

				for(EgBillPayeedetails payeeDetails:billdetails.getEgBillPaydetailes())
				{
					subledger = new PreApprovedVoucher();
					subledger.setGlcode(coa);
					Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,payeeDetails.getAccountDetailTypeId());
					detailtypeIdList.add(detailtype);
					subledger.setDetailType(detailtype);
					payeeMap = new HashMap<String,Object>();
					payeeMap = getAccountDetails(payeeDetails.getAccountDetailTypeId(),payeeDetails.getAccountDetailKeyId(),payeeMap);
					subledger.setDetailKey(payeeMap.get(Constants.DETAILKEY)+"");
					subledger.setDetailCode(payeeMap.get(Constants.DETAILCODE)+"");
					subledger.setDetailKeyId(payeeDetails.getAccountDetailKeyId());
					if(payeeDetails.getDebitAmount()==null)
					{
						subledger.setDebitAmount(BigDecimal.ZERO);
						subledger.setCreditAmount(payeeDetails.getCreditAmount());
					}
					else
					{
						subledger.setDebitAmount(payeeDetails.getDebitAmount());
						subledger.setCreditAmount(BigDecimal.ZERO);
					}
					payeeList.add(subledger);
				}
			}
		}
		billDetails.put("tempList", tempList);
		billDetails.put("subLedgerlist", payeeList);
		setupDropDownForSL(glcodeIdList);
		setupDropDownForSLDetailtype(detailtypeIdList);
	}

	public Map<String,Object> getAccountDetails(final Integer detailtypeid,final Integer detailkeyid,Map<String,Object> tempMap) throws EGOVException
	{
		Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,detailtypeid);
		tempMap.put("detailtype", detailtype.getDescription());
		tempMap.put("detailtypeid", detailtype.getId());
		tempMap.put("detailkeyid", detailkeyid);

		EgovCommon common = new EgovCommon();
		common.setPersistenceService(persistenceService);
		EntityType entityType = common.getEntityType(detailtype,detailkeyid);
		tempMap.put(Constants.DETAILKEY,entityType.getName());
		tempMap.put(Constants.DETAILCODE,entityType.getCode());
		return tempMap;
	}

	public void setupDropDownForSL(final List<Long> glcodeIdList)
	{
		if(glcodeIdList.isEmpty())
			dropdownData.put("glcodeList",Collections.EMPTY_LIST);
		else
			dropdownData.put("glcodeList", getPersistenceService().findAllByNamedQuery("getSubLedgerCodes", glcodeIdList));
	}

	public void setupDropDownForSLDetailtype(final List<Accountdetailtype> detailtypeIdList)
	{
		dropdownData.put("detailTypeList",detailtypeIdList);
	}

	public EgBillregister getEgBillregister() {
		return egBillregister;
	}
	protected void getHeaderMandateFields()
	{
		List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'DEFAULTTXNMISATTRRIBUTES'");
		for (AppConfig appConfig : appConfigList)
		{
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues())
			{
				String value = appConfigVal.getValue();
				String header=value.substring(0, value.indexOf("|"));
				headerFields.add(header);
				String mandate = value.substring(value.indexOf("|")+1);
				if(mandate.equalsIgnoreCase("M")){
					mandatoryFields.add(header);
				}
			}
		}
	}
	public boolean shouldShowHeaderField(String field)
	{
		return  headerFields.contains(field);
	}
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}
	public Position getPosition()throws EGOVRuntimeException
	{
		Position pos;
			LOGGER.debug("getPosition===="+Integer.valueOf(EGOVThreadLocals.getUserId()));
			pos = eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			LOGGER.debug("position==="+pos.getId());
		return pos;
	}

	public List<Action> getValidActions(String purpose){
		List<Action> validButtons = new ArrayList<Action>();
		Script validScript = (Script) scriptExecutionService.findAllByNamedQuery(Script.BY_NAME,"pjv.validbuttons").get(0);
		List<String> list = (List<String>) scriptExecutionService.executeScript(validScript,scriptExecutionService.createContext("eisManagerBean", employeeService,"userId",Integer.valueOf(EGOVThreadLocals.getUserId().trim()),"date",new Date(),"purpose",purpose));
		for(Object s:list)
		{
			if("invalid".equals(s))
				break;
			Action action = (Action) getPersistenceService().find(" from org.egov.infstr.workflow.Action where type='CVoucherHeader' and name=?", s.toString());
			validButtons.add(action);
		}
		return validButtons;
	}

	public String ajaxValidateDetailCode()
	{
		String code = parameters.get("code")[0];
		String index = parameters.get("index")[0];
		Accountdetailtype adt = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,Integer.valueOf(parameters.get("detailtypeid")[0]));
		if(adt==null)
		{
			values=index+"~"+ERROR;
			return "result";
		}
		if(adt.getTablename().equalsIgnoreCase("EG_EMPLOYEE"))
		{
			PersonalInformation information = (PersonalInformation) getPersistenceService().find(" from PersonalInformation where employeeCode=? and isActive=1", code);
			if(information==null)
				values=index+"~"+ERROR;
			else
				values=index+"~"+information.getIdPersonalInformation()+"~"+information.getEmployeeFirstName();
		}
		else if(adt.getTablename().equalsIgnoreCase("RELATION"))
		{
			Relation relation = (Relation) getPersistenceService().find(" from Relation where code=? and isactive='1'", code);
			if(relation==null)
				values=index+"~"+ERROR;
			else
				values=index+"~"+relation.getId()+"~"+relation.getName();
		}
		else if(adt.getTablename().equalsIgnoreCase("ACCOUNTENTITYMASTER"))
		{
			AccountEntity accountEntity = (AccountEntity) getPersistenceService().find(" from AccountEntity where code=? and isactive='1' ", code);
			if(accountEntity==null)
				values=index+"~"+ERROR;
			else
				values=index+"~"+accountEntity.getId()+"~"+accountEntity.getCode();
		}
		return "result";
	}

	public void setVoucherService(final VoucherService voucherService) {
		this.voucherService = voucherService;
	}

	public void setVoucherWorkflowService(final SimpleWorkflowService<CVoucherHeader> voucherWorkflowService) {
		this.voucherWorkflowService = voucherWorkflowService;
	}
	public CVoucherHeader getVoucherHeader() {
		return voucherHeader;
	}
	public void setVoucherHeader(final CVoucherHeader voucherHeader) {
		this.voucherHeader = voucherHeader;
	}
	
	public void setBillsAccountingService(
			BillsAccountingService billsAccountingService) {
		this.billsAccountingService = billsAccountingService;
	}

	public List<PreApprovedVoucher> getSubLedgerlist() {
		return subLedgerlist;
	}
	public void setSubLedgerlist(final List<PreApprovedVoucher> subLedgerlist) {
		this.subLedgerlist = subLedgerlist;
	}
	public List<PreApprovedVoucher> getBillDetailslist() {
		return billDetailslist;
	}
	public void setBillDetailslist(final List<PreApprovedVoucher> billDetailslist) {
		this.billDetailslist = billDetailslist;
	}

	public String getValues() {
		return values;
	}

	public void setValues(final String values) {
		this.values = values;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<String> getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(List<String> headerFields) {
		this.headerFields = headerFields;
	}

	public List<String> getMandatoryFields() {
		return mandatoryFields;
	}

	public void setMandatoryFields(List<String> mandatoryFields) {
		this.mandatoryFields = mandatoryFields;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWfitemstate() {
		return wfitemstate;
	}

	public void setWfitemstate(String wfitemstate) {
		this.wfitemstate = wfitemstate;
	}
	public ReceiptVoucher getReceiptVoucher() {
		return receiptVoucher;
	}

	public void setReceiptVoucher(ReceiptVoucher receiptVoucher) {
		this.receiptVoucher = receiptVoucher;
	}
	public ContraJournalVoucher getContraVoucher() {
		return contraVoucher;
	}

	public void setContraVoucher(ContraJournalVoucher contraVoucher) {
		this.contraVoucher = contraVoucher;
	}
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Map<String, Object> getBillDetails() {
		return billDetails;
	}

	public void setBillDetails(Map<String, Object> billDetails) {
		this.billDetails = billDetails;
	}

	public String getFinConstExpendTypeContingency() {
		return finConstExpendTypeContingency = FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT;
	}

	public VoucherHelper getVoucherHelper() {
		return voucherHelper;
	}

	public void setVoucherHelper(VoucherHelper voucherHelper) {
		this.voucherHelper = voucherHelper;
	}

	public String getFileSummary() {
		return fileSummary;
	}

	public void setFileSummary(String fileSummary) {
		this.fileSummary = fileSummary;
	}

	/*public void setFileManagementService(FileManagementService fileManagementService) {
		this.fileManagementService = fileManagementService;
	}*/

	public void setBeanProvider(ApplicationContextBeanProvider beanProvider) {
		this.beanProvider = beanProvider;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isNotifyOnCancel() {
		return notifyOnCancel;
	}

	public void setNotifyOnCancel(boolean notifyOnCancel) {
		this.notifyOnCancel = notifyOnCancel;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public void setBillsService(BillsService billsService) {
		this.billsService = billsService;
	}
	

}
