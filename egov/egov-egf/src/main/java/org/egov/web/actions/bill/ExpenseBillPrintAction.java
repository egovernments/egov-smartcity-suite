package org.egov.web.actions.bill;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.service.CommonsService;
import org.egov.commons.utils.EntityType;
import org.egov.dao.budget.BudgetDetailsHibernateDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.NumberToWord;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.voucher.PreApprovedVoucher;
import org.egov.model.voucher.VoucherDetails;
import org.egov.pims.commons.Position;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.SQLQuery;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Results(value={
	@Result(name="PDF",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=ExpenseJournalVoucherReport.pdf"}),
	@Result(name="XLS",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=ExpenseJournalVoucherReport.xls"}),
	@Result(name="HTML",type="stream",location="inputStream", params={"inputName","inputStream","contentType","text/html"})
})

@org.apache.struts2.convention.annotation.ParentPackage("egov")
public class ExpenseBillPrintAction extends BaseFormAction{
	final static private Logger LOGGER=Logger.getLogger(ExpenseBillPrintAction.class);
	String jasperpath = "/org/egov/web/actions/report/expenseBillReport.jasper";
	String subReportPath="/org/egov/web/actions/report/budgetAppropriationDetail.jasper";
	private static final long serialVersionUID = 1L;
	private static final String PRINT = "print";
	String functionName;
	private GenericHibernateDaoFactory genericDao;
	public GenericHibernateDaoFactory getGenericDao() {     
		return genericDao;
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}


	private  BudgetDetailsHibernateDAO budgetDetailsDAO;
	private CommonsService commonsService;
	private SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	Map<String,Object> budgetDataMap=new HashMap<String, Object>();
	Map<String,Object> paramMap = new HashMap<String,Object>();
	
	public CommonsService getCommonsService() {
		return commonsService;  
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public BudgetDetailsHibernateDAO getBudgetDetailsDAO() {
		return budgetDetailsDAO;
	}

	public void setBudgetDetailsDAO(BudgetDetailsHibernateDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

	public String getFunctionName() {
		return functionName;
	}


	private CVoucherHeader voucher = new CVoucherHeader();
	public List<Object> getBillReportList() {
		return billReportList;
	}

	public void setBillReportList(List<Object> billReportList) {
		this.billReportList = billReportList;
	}


	List<Object> billReportList = new ArrayList<Object>();
	InputStream inputStream;
	ReportHelper reportHelper;
	Long id;
	EgBillregistermis billRegistermis;
	List<EgBillPayeedetails> billPayeeDetails = new ArrayList<EgBillPayeedetails>();
	private static final String ACCDETAILTYPEQUERY=" from Accountdetailtype where id=?";
	EgBillregister cbill=new EgBillregister();
	//private InboxService inboxService;

	public Long getId() {
		return id;
	}
	
	public void setReportHelper(ReportHelper helper) {
		this.reportHelper = helper;
	}
	
	public void setId(Long id) {
		this.id = id;
	}



	public InputStream getInputStream() {
		return inputStream;
	}
	
	public String execute(){
		return print();
	}
	
@Action(value="/bill/expenseBillPrint-ajaxPrint")
	public String ajaxPrint(){
		return exportHtml();
	}
	
	@Override
	public Object getModel() {
		return voucher;
	}
	
@Action(value="/bill/expenseBillPrint-print")
	public String print() {
		return PRINT;
	}
	private void populateBill()
	{
		if(parameters.get("id")!=null && !parameters.get("id")[0].isEmpty())
		{
			cbill=(EgBillregister)persistenceService.find("from EgBillregister where id=?",Long.valueOf(parameters.get("id")[0]));
			billRegistermis=cbill.getEgBillregistermis();
		}
	
		generateVoucherReportList();
		
	}

	private void populateVoucher() {
		if(!StringUtils.isBlank(parameters.get("id")[0])){
			Long id = Long.valueOf(parameters.get("id")[0]);
			CVoucherHeader voucherHeader = (CVoucherHeader) HibernateUtil.getCurrentSession().get(CVoucherHeader.class,id);
			if(voucherHeader != null){
				voucher = voucherHeader;
				billRegistermis = (EgBillregistermis) persistenceService.find("from EgBillregistermis where voucherHeader.id=?",voucherHeader.getId());
				if(billRegistermis!=null){
					billPayeeDetails = persistenceService.findAllBy("from EgBillPayeedetails where egBilldetailsId.egBillregister.id=?",billRegistermis.getEgBillregister().getId());
				}
				generateVoucherReportList();
			}
		}
	}

	private void generateVoucherReportList()  {
	prepareForPrint();
	}
	
	private String getUlbName(){
		SQLQuery query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
		List<String> result = query.list();
		if(result!=null)
			return result.get(0);
		return "";
	}

	public String exportPdf() throws JRException, IOException{
		populateBill();
		inputStream = reportHelper.exportPdf(inputStream, jasperpath, getParamMap(), billReportList);
	    return "PDF";
	}
	
	public String exportHtml() {
		populateBill();
	   inputStream = reportHelper.exportHtml(inputStream, jasperpath, getParamMap(), billReportList,"px");
	   return "HTML";
	}
	
	public String exportXls() throws JRException, IOException{
		populateBill();
		inputStream = reportHelper.exportXls(inputStream, jasperpath, getParamMap(), billReportList);
	    return "XLS";
	}

	protected Map<String, Object> getParamMap() {
		
		
		paramMap.put("billNumber", cbill.getBillnumber());
		if(cbill.getBilldate()!=null)
		{
		paramMap.put("billDate", sdf.format(cbill.getBilldate()));
		}
		paramMap.put("voucherDescription", getVoucherDescription());
		if(cbill!=null && cbill.getState()!=null){
			//coment for phoenix migration fix once api is ready
			//loadInboxHistoryData(inboxService.getStateById(cbill.getState().getId()),paramMap);
		}
		
		if(billRegistermis != null){
			paramMap.put("billDate", Constants.DDMMYYYYFORMAT2.format(billRegistermis.getEgBillregister().getBilldate()));
			paramMap.put("partyBillNumber", billRegistermis.getPartyBillNumber());
			paramMap.put("serviceOrder", billRegistermis.getNarration());
			paramMap.put("partyName", billRegistermis.getPayto());
			if(billRegistermis.getPartyBillDate()!=null)
			{
			paramMap.put("partyBillDate",sdf.format(billRegistermis.getPartyBillDate()));
			}
			paramMap.put("netAmount",cbill.getPassedamount());
			BigDecimal amt = cbill.getPassedamount().setScale(2);
			String amountInWords=NumberToWord.convertToWord(amt.toString());
			amountInWords="("+amountInWords+" )";
			amountInWords="Bill is in order. Sanction is accorded for Rs."+amt+"/-"+amountInWords;
			paramMap.put("netAmountText",amountInWords);
			if(LOGGER.isInfoEnabled())     LOGGER.info("amountInWords"+amountInWords);
			paramMap.put("netAmountInWords",amountInWords);
			paramMap.put("billNumber", billRegistermis.getEgBillregister().getBillnumber());
			paramMap.put("functionName",getFunctionName());   
			paramMap.put("departmentName",billRegistermis.getEgDepartment().getName());
			paramMap.put("fundName",billRegistermis.getFund().getName());
			BigDecimal billamount = billRegistermis.getEgBillregister().getBillamount();
			paramMap.put("budgetApprNumber",billRegistermis.getBudgetaryAppnumber());
			paramMap.put("budgetAppropriationDetailJasper", reportHelper.getClass().getResourceAsStream(subReportPath)); 
			//Stritng amountInFigures = billamount==null?" ":billamount.toPlainString();
			//String amountInWords = billamount==null?" ":NumberToWord.convertToWord(billamount.toPlainString());
			//paramMap.put("certificate", getText("ejv.report.text", new String[]{amountInFigures,amountInWords}));
		}
		paramMap.put("ulbName", getUlbName());
		return paramMap;
	}                          

	/**
	 * @param paramMap
	 * @return
	 */
	private Map<String,Object>  getBudgetDetails(CChartOfAccounts coa, EgBilldetails billDetail,String functionName) {
		budgetDataMap.put(Constants.FUNCTIONID,Long.valueOf(billDetail.getFunctionid().toString()));
		if(cbill.getEgBillregistermis().getVoucherHeader()!=null)
		{
		budgetDataMap.put(Constants.ASONDATE, cbill.getEgBillregistermis().getVoucherHeader().getVoucherDate());//this date plays important roles
		}else
		{
			budgetDataMap.put(Constants.ASONDATE, cbill.getBilldate());
		}
		CFinancialYear financialYearById = commonsService.getFinancialYearById((Long)budgetDataMap.get("financialyearid"));
		Map<String,Object> budgetApprDetailsMap=new HashMap<String, Object>();
		budgetApprDetailsMap.put("financialYear", "BE-"+financialYearById.getFinYearRange()+" & Addl Funds(Rs)"); 
		budgetDataMap.put("fromdate", financialYearById.getStartingDate());  
		budgetDataMap.put("glcode", coa.getGlcode());
		budgetDataMap.put("glcodeid", coa.getId());  
		List<BudgetGroup> budgetHeadByGlcode = budgetDetailsDAO.getBudgetHeadByGlcode(coa);
		budgetDataMap.put("budgetheadid",budgetHeadByGlcode);
		BigDecimal budgetedAmtForYear = budgetDetailsDAO.getBudgetedAmtForYear(budgetDataMap);
		paramMap.put("budgetedAmtForYear",budgetedAmtForYear);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("budgetedAmtForYear .......... "+budgetedAmtForYear);
		
		budgetDataMap.put("budgetApprNumber",cbill.getEgBillregistermis().getBudgetaryAppnumber()); 
		//budgetDetailsDAO.getActualBudgetUtilized(budgetDataMap);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Getting actuals .............................. for print");
		
		BigDecimal actualAmtFromVoucher = budgetDetailsDAO.getActualBudgetUtilizedForBudgetaryCheck(budgetDataMap); // get actual amount
		if(LOGGER.isDebugEnabled())     LOGGER.debug("actualAmtFromVoucher .............................. "+actualAmtFromVoucher);
		budgetDataMap.put(Constants.ASONDATE, cbill.getBilldate());
		BigDecimal actualAmtFromBill = budgetDetailsDAO.getBillAmountForBudgetCheck(budgetDataMap); // get actual amount
		if(LOGGER.isDebugEnabled())     LOGGER.debug("actualAmtFromBill .............................. "+actualAmtFromBill);
		
		BigDecimal	 currentBillAmount= BigDecimal.ZERO;
		BigDecimal soFarAppropriated= BigDecimal.ZERO;
		BigDecimal actualAmount=BigDecimal.ZERO;
		actualAmount=actualAmtFromVoucher!=null?actualAmtFromVoucher:BigDecimal.ZERO;
		actualAmount=actualAmtFromBill!=null?actualAmount.add(actualAmtFromBill):actualAmount;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("actualAmount ...actualAmtFromVoucher+actualAmtFromBill........ "+actualAmount);
		
		if(billDetail.getDebitamount()!=null && billDetail.getDebitamount().compareTo(BigDecimal.ZERO)!=0)
		{
			actualAmount=actualAmount.subtract(billDetail.getDebitamount());
			currentBillAmount=billDetail.getDebitamount();
			
		}
		else
		{
			actualAmount=actualAmount.subtract(billDetail.getCreditamount());
			currentBillAmount=billDetail.getCreditamount();
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("actualAmount ...actualAmount-billamount........ "+actualAmount);
		BigDecimal balance=budgetedAmtForYear;
		
		balance = balance.subtract(actualAmount);
			soFarAppropriated=actualAmount;
	    if(LOGGER.isDebugEnabled())     LOGGER.debug("soFarAppropriated ...actualAmount==soFarAppropriated........ "+soFarAppropriated);
	    if(LOGGER.isDebugEnabled())     LOGGER.debug("balance ...budgetedAmtForYear-actualAmount........ "+balance);	
		BigDecimal cumilativeIncludingCurrentBill=soFarAppropriated.add(currentBillAmount);
		BigDecimal currentBalanceAvailable=balance.subtract(currentBillAmount);
		budgetApprDetailsMap.put("budgetApprNumber",cbill.getEgBillregistermis().getBudgetaryAppnumber());
        budgetApprDetailsMap.put("budgetedAmtForYear",budgetedAmtForYear);
        budgetApprDetailsMap.put("soFarAppropriated",soFarAppropriated);
        budgetApprDetailsMap.put("balance",balance);
        budgetApprDetailsMap.put("cumilativeIncludingCurrentBill",cumilativeIncludingCurrentBill);
        budgetApprDetailsMap.put("currentBalanceAvailable",currentBalanceAvailable);
        budgetApprDetailsMap.put("currentBillAmount",currentBillAmount);
        budgetApprDetailsMap.put("AccountCode",coa.getGlcode());
        
        budgetApprDetailsMap.put("departmentName", cbill.getEgBillregistermis().getEgDepartment().getName());
        budgetApprDetailsMap.put("functionName", functionName);
        budgetApprDetailsMap.put("fundName", cbill.getEgBillregistermis().getFund().getName());       
        
           
        
        
        return budgetApprDetailsMap;
        
	}

	/**
	 *  @param cbill
	 *  will set data in budgetDataMap 
	 *  will be called only once per bill
	 */
	private void getRequiredDataForBudget(EgBillregister cbill) {
		String financialYearId =null;// commonsService.getFinancialYearId(cbill.getBilldate().getTime());
		budgetDataMap.put("financialyearid", Long.valueOf(financialYearId));
		budgetDataMap.put(Constants.DEPTID,cbill.getEgBillregistermis().getEgDepartment().getId());
		if(cbill.getEgBillregistermis().getFunctionaryid()!=null)
		budgetDataMap.put(Constants.FUNCTIONARYID,cbill.getEgBillregistermis().getFunctionaryid().getId());
		if(cbill.getEgBillregistermis().getScheme()!=null)
		budgetDataMap.put(Constants.SCHEMEID,cbill.getEgBillregistermis().getScheme().getId());
		if(cbill.getEgBillregistermis().getSubScheme()!=null)
		budgetDataMap.put(Constants.SUBSCHEMEID,cbill.getEgBillregistermis().getSubScheme().getId());
		budgetDataMap.put(Constants.FUNDID,cbill.getEgBillregistermis().getFund().getId());
		budgetDataMap.put(Constants.BOUNDARYID,cbill.getDivision());
		
	}

	private List<PreApprovedVoucher> populateBillPayeeDetails(){
		List<PreApprovedVoucher> payeeDetailsList = new ArrayList<PreApprovedVoucher>();
		if(billPayeeDetails!=null){
			for (EgBillPayeedetails payeeDetails : billPayeeDetails) {
				PreApprovedVoucher subledger = new PreApprovedVoucher();
				subledger = new PreApprovedVoucher();
				CChartOfAccounts coa=(CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id='"+payeeDetails.getEgBilldetailsId().getGlcodeid()+"'");
				subledger.setGlcode(coa);
				Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,payeeDetails.getAccountDetailTypeId());
				subledger.setDetailType(detailtype);
				Map<String,Object> payeeMap = new HashMap<String,Object>();
				try {
					payeeMap = getAccountDetails(payeeDetails.getAccountDetailTypeId(),payeeDetails.getAccountDetailKeyId(),payeeMap);
				} catch (EGOVException e) {
					LOGGER.error("Error"+ e.getMessage(),e);
				}
				subledger.setDetailKey(payeeMap.get(Constants.DETAILKEY)+"");
				subledger.setDetailCode(payeeMap.get(Constants.DETAILCODE)+"");
				subledger.setDetailKeyId(payeeDetails.getAccountDetailKeyId());
				if(payeeDetails.getDebitAmount()==null || BigDecimal.ZERO.equals(payeeDetails.getDebitAmount())){
					subledger.setDebitAmount(BigDecimal.ZERO);
					subledger.setCreditAmount(payeeDetails.getCreditAmount());
				}
				else{
					subledger.setDebitAmount(payeeDetails.getDebitAmount());
					subledger.setCreditAmount(BigDecimal.ZERO);
				}
				payeeDetailsList.add(subledger);
			}
		}
		return payeeDetailsList;
	}

	public Map<String,Object> getAccountDetails(final Integer detailtypeid,final Integer detailkeyid,Map<String,Object> tempMap) throws EGOVException{
		Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,detailtypeid);
		tempMap.put("detailtype", detailtype.getName());
		tempMap.put("detailtypeid", detailtype.getId());
		tempMap.put("detailkeyid", detailkeyid);
		
		EgovCommon common = new EgovCommon();
		common.setPersistenceService(persistenceService);
		EntityType entityType = common.getEntityType(detailtype,detailkeyid);
		tempMap.put(Constants.DETAILKEY,entityType.getName());
		tempMap.put(Constants.DETAILCODE,entityType.getCode());		
		return tempMap;
	}

	private String getVoucherNumber() {
		return voucher == null || voucher.getVoucherNumber() == null?"" : voucher.getVoucherNumber();
	}
	
	private String getVoucherDescription() {
		return voucher == null || voucher.getDescription() == null?"" : voucher.getDescription();
	}
	
	private String getVoucherDate() {
		return voucher == null || voucher.getVoucherDate() == null ?"" : DateUtils.getDefaultFormattedDate(voucher.getVoucherDate());
	}
	
	/*private void loadInboxHistoryData(State states, Map<String, Object> paramMap) throws EGOVRuntimeException {
		List<String> history = new ArrayList<String>();
		List<String> workFlowDate = new ArrayList<String>();
		String approverDesignation = "";
		String approvalDate = "";
		String stateValue = "";
    	if (states != null) {
    	    List<StateHistory> stateHistory = states.getHistory();
    	    Collections.reverse(stateHistory);
    	    for (StateHistory state : stateHistory) {
    	    	stateValue = state.getValue();
	    		Position position = getStateUser(state);
	    		if(!"NEW".equalsIgnoreCase(stateValue)){
	    			if(null !=position.getDeptDesigId())
	    				history.add(position.getDeptDesigId().getDesigId().getDesignationName());
	    			else
	    				history.add("Invalid mapping could not get designation");
	    			workFlowDate.add(Constants.DDMMYYYYFORMAT2.format(state.getLastModifiedDate()()));
	    			
	    	    	if(stateValue != null && !stateValue.equalsIgnoreCase("") && stateValue.toLowerCase().contains("approved"))
	    	    	{
	    	    		approverDesignation = position.getDeptDesigId().getDesigId().getDesignationName() ;
	    	    		approvalDate = Constants.DDMMYYYYFORMAT2.format(state.getLastModifiedDate()());
	    	    	}
	    		}
    	    }
        }
    	paramMap.put("workFlow_approver", approverDesignation);
    	paramMap.put("workFlow_approval_date", approvalDate);
    	for (int i = 0; i<history.size();i++) {
    		paramMap.put("workFlow_"+i, history.get(i));
    		paramMap.put("workFlowDate_"+i, workFlowDate.get(i));
		}
    }*/
	
	//coment for phoenix migration 
	
	/*private Position getStateUser(State state) {
    	if (state.getPrevious() != null)
    	    return state.getPrevious().getOwner();
    	else
    	    return inboxService.getPrimaryPositionForUser(state.getCreatedBy().getId(), state.getCreatedDate());
    }*/
	
	
	/*public void setInboxService(InboxService inboxService) {
		this.inboxService = inboxService;
	}*/
	
	  
	private void prepareForPrint()  {

		Set<EgBilldetails> egBilldetailes = cbill.getEgBilldetailes();
		boolean budgetcheck=false;
		List<AppConfigValues> list =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","budgetCheckRequired");
		if(!list.isEmpty())
		{
			String value = list.get(0).getValue();
			if(value.equalsIgnoreCase("Y"))
			{
				budgetcheck=true;      
			}
			getRequiredDataForBudget(cbill);
		}
		List<Map<String,Object>> budget=new ArrayList<Map<String,Object>>();
		for(EgBilldetails detail:egBilldetailes)
		{
			if(detail.getDebitamount()!=null && detail.getDebitamount().compareTo(BigDecimal.ZERO)!=0) 
			{
				CFunction functionById=null;
				Map<String, Object> budgetApprDetails=null;
				
				VoucherDetails vd=new VoucherDetails();
				BigDecimal glcodeid = detail.getGlcodeid();
				if(detail.getFunctionid()!=null)  
				{
				functionById =(CFunction) persistenceService.find("from CFunction where id=?",Long.valueOf(detail.getFunctionid().toString()));
				setFunctionName(functionById.getName());
				paramMap.put("functionName", functionById.getName());
				}
				CChartOfAccounts coa =(CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?",Long.valueOf(glcodeid.toString()));
				if(budgetcheck &&  coa.getBudgetCheckReq()!=null && coa.getBudgetCheckReq()==1)
				{
					budgetApprDetails = getBudgetDetails(coa,detail,functionById.getName());
					budget.add(budgetApprDetails);
				}
				vd.setGlcodeDetail(coa.getGlcode());
				vd.setGlcodeIdDetail(coa.getId());
				vd.setAccounthead(coa.getName());
				vd.setCreditAmountDetail(BigDecimal.ZERO);
				vd.setDebitAmountDetail(detail.getDebitamount());
				Set<EgBillPayeedetails> egBillPaydetailes = detail.getEgBillPaydetailes();
				for(EgBillPayeedetails payeedetail:egBillPaydetailes)
				{
					Accountdetailtype detailType = (Accountdetailtype)persistenceService.find("from Accountdetailtype where id=? order by name",payeedetail.getAccountDetailTypeId());
					vd.setDetailTypeName(detailType.getName());
					String table=detailType.getFullQualifiedName();
					Class<?> service;  
					try {
						service = Class.forName(table);
					} catch (ClassNotFoundException e1) {
						LOGGER.error(e1.getMessage(), e1);
						throw new ValidationException(Arrays.asList(new ValidationError("","")));
					}
					String simpleName = service.getSimpleName();
					String tableName=simpleName;
					//simpleName=simpleName.toLowerCase()+"Service";
					simpleName = simpleName.substring(0,1).toLowerCase()+simpleName.substring(1)+"Service";
					WebApplicationContext wac= WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
				//	EntityTypeService entityService=	(EntityTypeService)wac.getBean(simpleName);
					PersistenceService entityPersistenceService=(PersistenceService)wac.getBean(simpleName);
					//it may give error since it is finding from session
				//	entityPersistenceService.
					String dataType = "";
					try {
						Class aClass = Class.forName(table);
						java.lang.reflect.Method method = aClass.getMethod("getId");
						dataType = method.getReturnType().getSimpleName();
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
						throw new EGOVRuntimeException(e.getMessage());
					}
					EntityType entity = null;
					if ( dataType.equals("Long") ){
						entity=(EntityType)entityPersistenceService.findById(Long.valueOf(
								payeedetail.getAccountDetailKeyId().toString()),false);
					}else{
						entity=(EntityType)entityPersistenceService.findById(payeedetail.getAccountDetailKeyId(),false);
					}
					vd.setDetailKey(entity.getCode());
					vd.setDetailName(entity.getName());
				}
				
				BillReport billReport = new BillReport(persistenceService,vd,cbill,budgetApprDetails);
				billReportList.add(billReport);   
			}
			}
		for(EgBilldetails detail:egBilldetailes)
		{
			if(detail.getCreditamount()!=null && detail.getCreditamount().compareTo(BigDecimal.ZERO)!=0) 
			{
				CFunction functionById=null;
				Map<String, Object> budgetApprDetails=null;
				
				VoucherDetails vd=new VoucherDetails();
				BigDecimal glcodeid = detail.getGlcodeid();
				if(detail.getFunctionid()!=null)  
				{
				functionById =(CFunction) persistenceService.find("from CFunction where id=?",Long.valueOf(detail.getFunctionid().toString()));
				setFunctionName(functionById.getName());
				paramMap.put("functionName", functionById.getName());
				}
				CChartOfAccounts coa =(CChartOfAccounts) persistenceService.find("from CChartOfAccounts where id=?",Long.valueOf(glcodeid.toString()));
				if(budgetcheck &&  coa.getBudgetCheckReq()!=null && coa.getBudgetCheckReq()==1)
				{
					budgetApprDetails = getBudgetDetails(coa,detail,functionName);
					budget.add(budgetApprDetails);
				}
				vd.setGlcodeDetail(coa.getGlcode());
				vd.setGlcodeIdDetail(coa.getId());
				vd.setAccounthead(coa.getName());
				vd.setCreditAmountDetail(detail.getCreditamount());
				vd.setDebitAmountDetail(BigDecimal.ZERO);
				Set<EgBillPayeedetails> egBillPaydetailes = detail.getEgBillPaydetailes();
				for(EgBillPayeedetails payeedetail:egBillPaydetailes)
				{
					Accountdetailtype detailType = (Accountdetailtype)persistenceService.find("from Accountdetailtype where id=? order by name",payeedetail.getAccountDetailTypeId());
					vd.setDetailTypeName(detailType.getName());
					String table=detailType.getFullQualifiedName();
					Class<?> service;
					try {
						service = Class.forName(table);
					} catch (ClassNotFoundException e1) {
						LOGGER.error(e1.getMessage(), e1);
						throw new ValidationException(Arrays.asList(new ValidationError("","")));
					}
					String simpleName = service.getSimpleName();
					String tableName=simpleName;
					//simpleName=simpleName.toLowerCase()+"Service";
					simpleName = simpleName.substring(0,1).toLowerCase()+simpleName.substring(1)+"Service";
					WebApplicationContext wac= WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
				//	EntityTypeService entityService=	(EntityTypeService)wac.getBean(simpleName);
					PersistenceService entityPersistenceService=(PersistenceService)wac.getBean(simpleName);
					//it may give error since it is finding from session
				//	entityPersistenceService.
					String dataType = "";
					try {
						Class aClass = Class.forName(table);
						java.lang.reflect.Method method = aClass.getMethod("getId");
						dataType = method.getReturnType().getSimpleName();
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
						throw new EGOVRuntimeException(e.getMessage());
					}
					EntityType entity = null;
					if ( dataType.equals("Long") ){
						entity=(EntityType)entityPersistenceService.findById(Long.valueOf(
								payeedetail.getAccountDetailKeyId().toString()),false);
					}else{
						entity=(EntityType)entityPersistenceService.findById(payeedetail.getAccountDetailKeyId(),false);
					}
					vd.setDetailKey(entity.getCode());
					vd.setDetailName(entity.getName());
				}
				
				BillReport billReport = new BillReport(persistenceService,vd,cbill,budgetApprDetails);
				billReportList.add(billReport);   
			}
			}
		paramMap.put("budgetDetail", budget);
		
		
	}		
				
			
	
	
	


	/**
	 * @param name
	 */
	private void setFunctionName(String name) {
		this.functionName=name;
		
	}

}
