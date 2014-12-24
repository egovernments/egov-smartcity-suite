/**
 * 
 */
package org.egov.works.web.actions.serviceOrder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.script.ScriptContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.dao.voucher.VoucherHibernateDAO;
import org.egov.egf.bills.model.Cbill;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.masters.model.AccountEntity;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.masters.AccountCodePurpose;
import org.egov.model.voucher.VoucherDetails;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EisUtilService;
import org.egov.services.voucher.VoucherService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.OverheadValue;
import org.egov.works.models.serviceOrder.ServiceOrder;
import org.egov.works.models.serviceOrder.ServiceOrderDetails;
import org.egov.works.models.serviceOrder.ServiceOrderObjectDetail;
import org.egov.works.models.serviceOrder.SoMeasurementHeader;
import org.egov.works.models.serviceOrder.SoMeasurmentDetail;
import org.egov.works.services.serviceOrder.MeasurementHeaderService;
import org.egov.works.services.serviceOrder.ServiceOrderService;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class MeasurementBookAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(MeasurementBookAction.class);
	private ServiceOrderObjectDetail soObjDetail =  new ServiceOrderObjectDetail();
	private SoMeasurementHeader mHeader = new SoMeasurementHeader();
	private ServiceOrderService serviceOrderSer;
	private List<ServiceOrder> serviceOrderList;
	private List<AbstractEstimate> abstractEstimateList;
	private Date fromDate;
	private Date toDate;
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private EisUtilService eisService;
	private List<VoucherDetails> billDetailslist;
	private List<VoucherDetails> subLedgerlist;
	private ExpenseBillService cbillSer;
	private Cbill cbill = new Cbill() ;
	private MeasurementHeaderService measurementHService;
	protected List<Action> validButtons;
	protected String nextLevel;
	protected VoucherService voucherService;
	protected SimpleWorkflowService<Cbill> billRegisterWorkflowService;
	private VoucherHibernateDAO voucherHibDAO;
	private ScriptService scriptExecutionService;
	private SequenceGenerator sequenceGenerator;
	private Long functionId=null;
    public MeasurementBookAction(){
    	addRelatedEntity("preparedby", UserImpl.class);
    	addRelatedEntity("serviceOrderObjectDetail", ServiceOrderObjectDetail.class);
    }
	
	@Override 
	public Object getModel() {
		
		return mHeader;
	}
	@Override
	public void prepare() {
		super.prepare();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("departmentList", masterCache.get("egi-department"));
		addDropdownData("prepareByList", Collections.EMPTY_LIST);
		addDropdownData("estimateList", Collections.EMPTY_LIST);
		addDropdownData("billSubTypeList", Collections.EMPTY_LIST);
		
	}
	
	public String beforeSearch(){
		
		return "search";
	}
	
	public String  ajaxFilterSOBydept(){
			EgwStatus status = (EgwStatus)persistenceService.find(" from EgwStatus where moduletype='"+"SERVICEORDER"+"' and description='"+"ServiceOrderCreated'");
			serviceOrderList = serviceOrderSer.findAllBy("from  ServiceOrder where  departmentId.id=? and serviceordernumber like '"+
			parameters.get("serviceordernumber")[0]+"%' and status=?", Integer.valueOf(parameters.get("deptId")[0]),status);
		
		return "soNum";
	}
	
	public String ajaxFilterAbsByDeptAndSOydept(){
		
		getEstimateListByDeptAndSo(parameters.get("serviceordernumber")[0],Integer.valueOf(parameters.get("deptId")[0]) );
		return "absNum";
	}
	
	@SuppressWarnings("unchecked")
	private List<AbstractEstimate> getEstimateListByDeptAndSo(String soNum , Integer deptId){
		
		StringBuffer query = new StringBuffer(200);
		query.append("select soobj.abstractEstimate from ServiceOrderObjectDetail soobj where soobj.serviceOrder.departmentId.id=?");
		if(StringUtils.isNotEmpty(soNum)  ){
			query.append(" and  soobj.serviceOrder.serviceordernumber='"+soNum+"'");
		}
		
		abstractEstimateList = (List<AbstractEstimate> ) persistenceService.findAllBy(query.toString(),
												deptId);
		
		return abstractEstimateList;
	}
	
	public String list(){
		StringBuffer query = new StringBuffer(100);
		query.append(" from ServiceOrderObjectDetail soobj where soobj.serviceOrder.departmentId.id="+soObjDetail.getServiceOrder().getDepartmentId().getId());
		query.append(" and soobj.serviceOrder.serviceordernumber='"+soObjDetail.getServiceOrder().getServiceordernumber()+"'").
		append(" and soobj.abstractEstimate.estimateNumber='"+soObjDetail.getAbstractEstimate().getEstimateNumber()+"'");
		/*if(null != fromDate){
			query.append(" and soobj.serviceOrder.serviceorderdate >= to_date('"+DDMMYYYYFORMATS.format(fromDate)+"','dd/MM/yyyy')");
		}
		if(null != toDate){
			query.append(" and soobj.serviceOrder.serviceorderdate <= to_date('"+DDMMYYYYFORMATS.format(toDate)+"','dd/MM/yyyy')");
		}*/
		
		LOGGER.debug("MeasurementBookAction | prepareQuery | query >>>>> "+ query.toString());
		
		soObjDetail = (ServiceOrderObjectDetail) persistenceService.find(query.toString());
		
		addDropdownData("estimateList", getEstimateListByDeptAndSo(soObjDetail.getServiceOrder().getServiceordernumber(),soObjDetail.getServiceOrder().getDepartmentId().getId()));
		return "search";
	}
	
	@SuppressWarnings("unchecked")
	public String beforeCreate(){
		
		LOGGER.debug("MeasurementBookAction | beforeBill | Start");
		Predicate soDtlsPredicate = new ServiceOrderDetails();
		CollectionUtils.filter(soObjDetail.getServiceOrderDetails(), soDtlsPredicate);
		setPreparedByList(soObjDetail.getServiceOrder().getDepartmentId().getId());
		billDetailslist = new ArrayList<VoucherDetails>();
		subLedgerlist = new ArrayList<VoucherDetails>();
		VoucherDetails detbitDetails = new VoucherDetails(); 
		
		AccountCodePurpose purpose = (AccountCodePurpose)persistenceService.findAllBy(" from AccountCodePurpose where name=?", "Architect Fee Code").get(0);
		OverheadValue overheadValue =(OverheadValue) persistenceService.find(" from OverheadValue where abstractEstimate.estimateNumber='"+soObjDetail.getAbstractEstimate().getEstimateNumber() +"' and overhead.account.purposeId="+purpose.getId());
		functionId=overheadValue.getAbstractEstimate().getFinancialDetails().get(0).getFunction().getId();
		detbitDetails.setGlcodeDetail(overheadValue.getOverhead().getAccount().getGlcode());
		detbitDetails.setGlcodeIdDetail(overheadValue.getOverhead().getAccount().getId());
		detbitDetails.setAccounthead(overheadValue.getOverhead().getAccount().getName());
		detbitDetails.setFunctionIdDetail(functionId);
		BigDecimal totalDbAmt = BigDecimal.ZERO;
		for (ServiceOrderDetails sodetls : soObjDetail.getServiceOrderDetails()) {
			totalDbAmt = totalDbAmt.add((soObjDetail.getObjectamount().multiply(sodetls.getRatePercentage()))
										.divide(BigDecimal.valueOf(100)));
		}
		detbitDetails.setDebitAmountDetail(totalDbAmt.setScale(2, BigDecimal.ROUND_HALF_UP));
		detbitDetails.setCreditAmountDetail(BigDecimal.ZERO.setScale(2));
		billDetailslist.add(detbitDetails);
		
		VoucherDetails creditDetails = new VoucherDetails(); 
		
		List<AppConfigValues> appConfigVals = persistenceService.findAllBy("from AppConfigValues where key.keyName =? and key.module =?","contingencyBillPurposeIds", "EGF");
		
		List<CChartOfAccounts> coaList= (List<CChartOfAccounts>)persistenceService.findAllBy(" from CChartOfAccounts where purposeId=?",appConfigVals.get(0).getValue());
		CChartOfAccounts coa = coaList.get(0);
		creditDetails.setGlcodeDetail(coa.getGlcode());
		creditDetails.setGlcodeIdDetail(coa.getId());
		creditDetails.setAccounthead(coa.getName());
		creditDetails.setDebitAmountDetail(BigDecimal.ZERO.setScale(2));
		creditDetails.setCreditAmountDetail(totalDbAmt.setScale(2, BigDecimal.ROUND_HALF_UP));
		creditDetails.setFunctionIdDetail(functionId);
		billDetailslist.add(creditDetails);
		
		VoucherDetails subLedgerDetail = new VoucherDetails();
		subLedgerDetail.setAmount(totalDbAmt.setScale(2, BigDecimal.ROUND_HALF_UP));
		subLedgerDetail.setGlcode(overheadValue.getOverhead().getAccount());
		subLedgerDetail.setSubledgerCode(overheadValue.getOverhead().getAccount().getGlcode());
		Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService.find(" from Accountdetailtype where name='"+"Architect'");
		subLedgerDetail.setDetailType(accountdetailtype);
		subLedgerDetail.setDetailTypeName(accountdetailtype.getName());
		ServiceOrder serviceOrder = (ServiceOrder)persistenceService.find(" select so.serviceOrder from ServiceOrderObjectDetail so where so.id="+soObjDetail.getId());
		AccountEntity entity= (AccountEntity) persistenceService.find("from AccountEntity where id="+serviceOrder.getDetailkeyid());
		
		subLedgerDetail.setDetailCode(entity.getCode());
		subLedgerDetail.setDetailKeyId(serviceOrder.getDetailkeyid());
		subLedgerDetail.setDetailKey(entity.getName());
		subLedgerDetail.setFunctionIdDetail(functionId);
		EgBillregistermis egBillregistermis = new EgBillregistermis();
		egBillregistermis.setPayto(entity.getName());
		cbill.setEgBillregistermis(egBillregistermis);
		subLedgerlist.add(subLedgerDetail);
		
		
		validButtons = cbillSer.getValidActions("authentication",null);
		if(validButtons.size()==0)
		{
			addActionMessage(getText("cbill.user.authenticate"));
		}
		else
		{
			Map<String, Object>  map = voucherService.getDesgBYPassingWfItem("cbill.nextUser",null,null);
			addDropdownData("designationList", (List<DesignationMaster>)map.get("designationList")); 
			addDropdownData("userList", Collections.EMPTY_LIST);
			nextLevel = map.get("wfitemstate")!=null?map.get("wfitemstate").toString():null;
		}
		
		addDropdownData("billSubTypeList",cbillSer.getBillSubTypes());
		return "create";
	}
	
	private void setPreparedByList(Integer deptId){
		HashMap<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("departmentId",deptId.toString());
		List<EmployeeView> empInfoList = eisService.getEmployeeInfoList(paramMap);
		List<EmployeeView> userList = new ArrayList<EmployeeView>();
		List<EmployeeView> finalList= new ArrayList<EmployeeView>();
		EmployeeView mainEmpViewObj,prevEmpView = new EmployeeView();
		Iterator iterator = empInfoList.iterator(); 
		while(iterator.hasNext())
		{
			mainEmpViewObj=(EmployeeView)iterator.next();
			if(!((mainEmpViewObj.getId().equals(prevEmpView.getId())) && (mainEmpViewObj.getDesigId().equals(prevEmpView.getDesigId())))){
				finalList.add(mainEmpViewObj); 
		}
			prevEmpView=mainEmpViewObj;
		}
		userList=Collections.EMPTY_LIST;
		userList=finalList;
		addDropdownData("prepareByList", userList);
	}
	
	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value="create")
	public String create(){
		EgBillregistermis egBillregistermis = cbill.getEgBillregistermis();
		Fund fund = (Fund)persistenceService.find("select f.fund from FinancialDetail f , AbstractEstimate abs , ServiceOrderObjectDetail so " +
				" where  f.abstractEstimate.id=abs.id and abs.id=so.abstractEstimate.id and so.id="+mHeader.getServiceOrderObjectDetail().getId());
		egBillregistermis.setFund(fund);
		DepartmentImpl egDepartment  = (DepartmentImpl)persistenceService.find("select s.departmentId from ServiceOrderObjectDetail so," +
				"ServiceOrder s  where so.serviceOrder.id=s.id and  so.id="+mHeader.getServiceOrderObjectDetail().getId());
		egBillregistermis.setEgDepartment(egDepartment);
		cbill.setEgBillregistermis(egBillregistermis);
		egBillregistermis.setEgBillregister(cbill);
		cbillSer.createExpenseBill(billDetailslist,subLedgerlist,cbill);
		mHeader.setEgBillregister(cbill);
		EgwStatus status = (EgwStatus)persistenceService.find(" from EgwStatus where moduletype='"+"SERVICEORDER"+"' and description='"+"MeasurementBookCreated'");
		mHeader.setStatus(status);
		mHeader.setMbNumber(getMBNum(egDepartment.getDeptCode()));
		measurementHService.persist(mHeader);
		billRegisterWorkflowService.start(cbill,cbillSer.getPosition());
		forwardBill(cbill);
		persistenceService.setType(ServiceOrderDetails.class);
		for (SoMeasurmentDetail mhDetails : mHeader.getSoMeasurmentDetails()) {
			ServiceOrderDetails soDetsils = (ServiceOrderDetails)persistenceService.findById(mhDetails.getServiceOrderDetails().getId(), false);
			
			soDetsils.setIscompleted(true);
			persistenceService.update(soDetsils);
		}
		
		ServiceOrder serviceOrder = (ServiceOrder)serviceOrderSer.find("select soObj.serviceOrder from ServiceOrderObjectDetail soObj where soObj.id="+mHeader.getServiceOrderObjectDetail().getId());
		List<ServiceOrderDetails> list = (List<ServiceOrderDetails>)persistenceService.findAllBy("from  ServiceOrderDetails where iscompleted is null or iscompleted=0" +
				"and  serviceOrderObjectDetail.serviceOrder.id=?",serviceOrder.getId());
		
		if(list.size() ==0){
			status = (EgwStatus)persistenceService.find(" from EgwStatus where moduletype='"+"SERVICEORDER"+"' and description='"+"ServiceOrderCompleted'");
			serviceOrder.setStatus(status);
			serviceOrderSer.update(serviceOrder);
		}
		
		addActionMessage(" Measurement book created sucessfully ,MB Number :"+mHeader.getMbNumber());
		addActionMessage("Expense bill Number : "+ cbill.getBillnumber());
		if(null != cbill.getEgBillregistermis().getBudgetaryAppnumber())
		{
			addActionMessage(getText("budget.recheck.sucessful",new String[]{cbill.getEgBillregistermis().getBudgetaryAppnumber()}));
		}
		return "message";
	}
	
	private String getMBNum(String deptCode){
		
		CFiscalPeriod fiscalPeriod = (CFiscalPeriod)persistenceService.find("from CFiscalPeriod where  to_date('"+DDMMYYYYFORMATS.format(mHeader.getMeasurementDate())+"','dd/MM/yyyy')"
				+"  between startingDate and endingDate");
		CFinancialYear financialYear =(CFinancialYear) persistenceService.find(" from CFinancialYear where  to_date('"+DDMMYYYYFORMATS.format(mHeader.getMeasurementDate())+"','dd/MM/yyyy')"+
				" between  startingDate and endingDate");
		ScriptContext scriptContext = ScriptService.createContext("deptCode",deptCode,"sequenceGenerator",sequenceGenerator,
				"fiscalPeriodName",fiscalPeriod.getName(),"financialYear",financialYear.getFinYearRange());  
		return  (String)scriptExecutionService.executeScript("so.mb.number", scriptContext);
	}
	
	public String getFundName(){
		
		LOGGER.debug(soObjDetail);
		String fundName = (String)persistenceService.find("select f.fund.name from FinancialDetail f where f.abstractEstimate.estimateNumber='"+soObjDetail.getAbstractEstimate().getEstimateNumber()+"'");
		
		return fundName;
	}
	
	public String getDeptName(){
		
		String deptName  = (String)persistenceService.find("select executingDepartment.deptName from AbstractEstimate  abs where abs.estimateNumber='"+soObjDetail.getAbstractEstimate().getEstimateNumber()+"'");
		return deptName;
	}
	
	
	private void forwardBill(Cbill cbill )
	{
		Integer userId = null;
		if(null != parameters.get("approverUserId") &&  Integer.valueOf(parameters.get("approverUserId")[0])!=-1)
		{
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);
		}else 
		{
			userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
		}
		
		LOGGER.debug("User selected id is : "+userId);
		billRegisterWorkflowService.transition(parameters.get("actionName")[0]+"|"+userId, cbill,parameters.get("comments")[0]);
		addActionMessage(getText("pjv.voucher.approved",new String[]{voucherService.getEmployeeNameForPositionId(cbill.getState().getOwner())}));
	}
	
	
	public String beforeSearchMB(){
		return "searchMB";
	}
	@ValidationErrorPage(value="searchMB")
	public String searchMB(){
		search();
		return "searchMB";
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		
		StringBuffer query = new StringBuffer();
		EgwStatus billStatus =(EgwStatus) persistenceService.find("from EgwStatus where moduletype='EXPENSEBILL' and description='Approved'");
		query.append(" from SoMeasurementHeader mb where mb.egBillregister.status.id="+billStatus.getId());
	
		return new SearchQueryHQL(query.toString(),"select count(*) "+query.toString(),null);
	}
	
	public String view(){
		mHeader = measurementHService.findById(mHeader.getId(), false);
		
		setPreparedByList(mHeader.getServiceOrderObjectDetail().getServiceOrder().getDepartmentId().getId());
		addDropdownData("billSubTypeList",cbillSer.getBillSubTypes());
		
		soObjDetail.setAbstractEstimate(mHeader.getServiceOrderObjectDetail().getAbstractEstimate());
		soObjDetail.setObjectamount(mHeader.getServiceOrderObjectDetail().getObjectamount());
		List<ServiceOrderDetails> serviceOrderDetails = new ArrayList<ServiceOrderDetails>();
		for (SoMeasurmentDetail mbDetails :  mHeader.getSoMeasurmentDetails()) {
			serviceOrderDetails.add(mbDetails.getServiceOrderDetails());
		}
		soObjDetail.setServiceOrderDetails(serviceOrderDetails);
		
		cbill = (Cbill) persistenceService.find(" from Cbill where id="+mHeader.getEgBillregister().getId());
		billDetailslist = new ArrayList<VoucherDetails>();
		subLedgerlist = new ArrayList<VoucherDetails>();
		for (EgBilldetails billDetail : cbill.getEgBilldetailes()) {
			VoucherDetails account= new VoucherDetails(); 
			if(null != billDetail.getFunctionid()){
				CFunction function = (CFunction) persistenceService.find(" from CFunction where id="+Long.valueOf(billDetail.getFunctionid().toString()));
				account.setFunctionDetail(function.getName());
			}
			
			CChartOfAccounts coa =(CChartOfAccounts)persistenceService.find(" from CChartOfAccounts where id="+billDetail.getGlcodeid());
			account.setGlcodeDetail(coa.getGlcode());
			account.setAccounthead(coa.getName());
			account.setDebitAmountDetail(null ==billDetail.getDebitamount()?BigDecimal.ZERO.setScale(2):billDetail.getDebitamount());
			account.setCreditAmountDetail(null ==billDetail.getCreditamount()?BigDecimal.ZERO.setScale(2):billDetail.getCreditamount());
			billDetailslist.add(account);
			for (EgBillPayeedetails payee : billDetail.getEgBillPaydetailes()) {
				VoucherDetails subledger= new VoucherDetails();
				subledger.setAmount(null == payee.getDebitAmount()?payee.getCreditAmount():payee.getCreditAmount());
				subledger.setGlcode(coa);
				subledger.setSubledgerCode(coa.getGlcode());
				Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService.find(" from Accountdetailtype where id="+payee.getAccountDetailTypeId());
				subledger.setDetailType(accountdetailtype);
				subledger.setDetailTypeName(accountdetailtype.getName());
				AccountEntity entity= (AccountEntity) persistenceService.find("from AccountEntity where id="+payee.getAccountDetailKeyId());
				subledger.setDetailCode(entity.getCode());
				subledger.setDetailKeyId(payee.getAccountDetailKeyId());
				subledger.setDetailKey(entity.getName());
				subledger.setAmount(null == payee.getDebitAmount()?payee.getCreditAmount():payee.getDebitAmount());
				subLedgerlist.add(subledger);
			}
		}
		
		nextLevel = "END";
		
		
		return "view";
	}
	
	public BigDecimal getAmount(BigDecimal objAmount , BigDecimal rate){
		
		return (objAmount.multiply(rate)).divide(BigDecimal.valueOf(100), 2,BigDecimal.ROUND_HALF_UP);
		
	}
	
	public List<ServiceOrder> getServiceOrderList() {
		return serviceOrderList;
	}

	public void setServiceOrderList(List<ServiceOrder> serviceOrderList) {
		this.serviceOrderList = serviceOrderList;
	}

	public void setServiceOrderSer(ServiceOrderService serviceOrderSer) {
		this.serviceOrderSer = serviceOrderSer;
	}
	public ServiceOrderObjectDetail getSoObjDetail() {
		return soObjDetail;
	}
	public void setSoObjDetail(ServiceOrderObjectDetail soObjDetail) {
		this.soObjDetail = soObjDetail;
	}
	public List<AbstractEstimate> getAbstractEstimateList() {
		return abstractEstimateList;
	}
	public void setAbstractEstimateList(List<AbstractEstimate> abstractEstimateList) {
		this.abstractEstimateList = abstractEstimateList;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public SoMeasurementHeader getMHeader() {
		return mHeader;
	}
	public void setMHeader(SoMeasurementHeader header) {
		mHeader = header;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public List<VoucherDetails> getBillDetailslist() {
		return billDetailslist;
	}

	public List<VoucherDetails> getSubLedgerlist() {
		return subLedgerlist;
	}

	public void setBillDetailslist(List<VoucherDetails> billDetailslist) {
		this.billDetailslist = billDetailslist;
	}

	public void setSubLedgerlist(List<VoucherDetails> subLedgerlist) {
		this.subLedgerlist = subLedgerlist;
	}

	public Cbill getCbill() {
		return cbill;
	}

	public void setCbill(Cbill cbill) {
		this.cbill = cbill;
	}

	public void setMeasurementHService(MeasurementHeaderService measurementHService) {
		this.measurementHService = measurementHService;
	}

	public List<Action> getValidButtons() {
		return validButtons;
	}

	public void setValidButtons(List<Action> validButtons) {
		this.validButtons = validButtons;
	}

	public String getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(String nextLevel) {
		this.nextLevel = nextLevel;
	}

	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}

	public void setBillRegisterWorkflowService(
			SimpleWorkflowService<Cbill> billRegisterWorkflowService) {
		this.billRegisterWorkflowService = billRegisterWorkflowService;
	}

	public void setCbillSer(ExpenseBillService cbillSer) {
		this.cbillSer = cbillSer;
	}

	public VoucherHibernateDAO getVoucherHibDAO() {
		return voucherHibDAO;
	}

	public void setVoucherHibDAO(VoucherHibernateDAO voucherHibDAO) {
		this.voucherHibDAO = voucherHibDAO;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
}
