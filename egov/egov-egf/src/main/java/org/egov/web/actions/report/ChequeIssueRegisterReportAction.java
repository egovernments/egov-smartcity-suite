package org.egov.web.actions.report;

import org.apache.struts2.convention.annotation.Action;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.payment.ChequeAssignmentAction;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

@Results(value={
		@Result(name="PDF",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=ChequeIssueRegister.pdf"}),
		@Result(name="XLS",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=ChequeIssueRegister.xls"})
	})

@ParentPackage("egov")
public class ChequeIssueRegisterReportAction extends BaseFormAction{
	private static final String MULTIPLE = "Multiple";
	String jasperpath = "/org/egov/web/actions/report/chequeIssueRegisterReport.jasper";
	private List<ChequeIssueRegisterDisplay> chequeIssueRegisterList = new ArrayList<ChequeIssueRegisterDisplay>();
	PersistenceService persistenceService;
	private Date fromDate;
	private Date toDate;
	private String chequeFromNumber;
	private String chequeToNumber;
	private Department department;
	private Bankaccount accountNumber;
	ReportHelper reportHelper;
	private InputStream inputStream;
	private EgovCommon egovCommon;
	private GenericHibernateDaoFactory genericDao;
	private String ulbName = "";
	private String bank;
	private static final Logger LOGGER = Logger.getLogger(ChequeIssueRegisterReportAction.class);
	

	public ChequeIssueRegisterReportAction() {
		addRelatedEntity(Constants.EXECUTING_DEPARTMENT, Department.class);
	}

	@Override
	public void prepare() {
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		super.prepare();
		if(!parameters.containsKey("showDropDown")){
			EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
			addDropdownData("bankList", egovCommon.getBankBranchForActiveBanks());
			addDropdownData("bankAccountList", Collections.EMPTY_LIST);
			dropdownData.put("executingDepartmentList", masterCache.get("egi-department"));
		}
		populateUlbName();
	}
	@Override
	public String execute() throws Exception {
		return "form";
	}
	public void setReportHelper(final ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}

	public void generateReport() throws JRException, IOException{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("----Inside generateReport---- ");
		accountNumber = (Bankaccount) persistenceService.find("from Bankaccount where id=?",accountNumber.getId());
		validateDates(fromDate,toDate);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Querying to date range "+getFormattedDate(fromDate)+"to date "+ getFormattedDate(getNextDate(toDate)));         
		persistenceService.setType(InstrumentHeader.class);
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("select ih.instrumentnumber as chequeNumber,ih.instrumentdate as chequeDate," +
				"ih.instrumentamount as chequeAmount,vh.vouchernumber as voucherNumber,vh.id as vhId,ih.serialno as serialNo,vh.voucherdate as voucherDate,vh.name as voucherName,ih.payto as payTo,mbd.billnumber as billNumber," +
				"mbd.billDate as billDate,vh.type as type,es.DESCRIPTION as chequeStatus from egf_instrumentHeader ih,egf_instrumentvoucher iv,EGW_STATUS es," +
				"voucherheader vh left outer join miscbilldetail mbd on  vh.id=mbd.PAYVHID ,vouchermis vmis where ih.instrumentDate <'"
				+getFormattedDate(getNextDate(toDate))+"' and ih.instrumentDate>='"+getFormattedDate(fromDate)+"' and ih.isPayCheque='1' " +
						"and ih.INSTRUMENTTYPE=(select id from egf_instrumenttype where TYPE='cheque' ) and vh.status not in ("+getExcludeVoucherStatues()+") and vh.id=iv.voucherheaderid and  bankAccountId="
				+accountNumber.getId()+" and ih.id=iv.instrumentheaderid and ih.id_status=es.id " +
				" and vmis.voucherheaderid=vh.id "+createQuery()+" order by ih.instrumentDate,ih.instrumentNumber ")
				.addScalar("chequeNumber")
				.addScalar("chequeDate")
				.addScalar("chequeAmount")
				.addScalar("voucherNumber")
				.addScalar("voucherDate")
				.addScalar("voucherName")
				.addScalar("payTo")
				.addScalar("billNumber")
				.addScalar("billDate")
				.addScalar("type")
				.addScalar("vhId")
				.addScalar("serialNo")
				.addScalar("chequeStatus")
				.setResultTransformer(Transformers.aliasToBean(ChequeIssueRegisterDisplay.class));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Search query"+query.getQueryString());
		chequeIssueRegisterList = query.list();
		if(chequeIssueRegisterList==null)
			chequeIssueRegisterList = new ArrayList<ChequeIssueRegisterDisplay>();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Got Cheque list| Size of list is"+chequeIssueRegisterList.size());
		updateBillNumber();
		updateVoucherNumber();
		removeDuplicates();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("--End  generateReport--");
	}
	

	private void removeDuplicates() {
		Map<String,ChequeIssueRegisterDisplay> map = new HashMap<String, ChequeIssueRegisterDisplay>();
		for (Iterator<ChequeIssueRegisterDisplay> row = chequeIssueRegisterList.iterator(); row.hasNext();) {
			ChequeIssueRegisterDisplay next = row.next();
			if(map.get(next.getChequeNumber()+"-"+next.getSerialNo())==null)
				map.put(next.getChequeNumber()+"-"+next.getSerialNo(), next);
			else
				row.remove();
		}
	}

	String createQuery() {
		String query = "";
		if(department!=null && department.getId()!=0)
			query = query.concat(" and vmis.departmentid="+department.getId());
		return query;
	}

	private void updateBillNumber(){
		Map<String,ChequeIssueRegisterDisplay> map = new HashMap<String, ChequeIssueRegisterDisplay>();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside updateBillNumber ");
		for (ChequeIssueRegisterDisplay row : chequeIssueRegisterList) {
			if(map.get(row.getChequeNumber())==null)
				map.put(row.getChequeNumber(), row);
			else if(row.getBillNumber() != null && row.getBillNumber().equalsIgnoreCase(map.get(row.getChequeNumber()).getBillNumber()))
				continue;
			else{
				map.get(row.getChequeNumber()).setBillNumber("MULTIPLE");
				row.setBillNumber(MULTIPLE);
			}
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("End updateBillNumber ");
	}
	
	private void updateVoucherNumber(){
		Map<String,ChequeIssueRegisterDisplay> map = new HashMap<String, ChequeIssueRegisterDisplay>();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("End updateVoucherNumber ");
		for (ChequeIssueRegisterDisplay row : chequeIssueRegisterList)
		{
			if(map.get(row.getChequeNumber())==null)  
				map.put(row.getChequeNumber(), row);
			else if(row.getVoucherNumber() != null && row.getVoucherNumber().equalsIgnoreCase(map.get(row.getChequeNumber()).getVoucherNumber()))
				continue;
			else if (map.get(row.getChequeNumber()).getChequeStatus().equalsIgnoreCase(FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS)
					||map.get(row.getChequeNumber()).getChequeStatus().equalsIgnoreCase(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS)
					||map.get(row.getChequeNumber()).getChequeStatus().equalsIgnoreCase(FinancialConstants.INSTRUMENT_CANCELLED_STATUS)
					||row.getChequeStatus().equalsIgnoreCase(FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS)
					||row.getChequeStatus().equalsIgnoreCase(FinancialConstants.INSTRUMENT_SURRENDERED_STATUS)
					||row.getChequeStatus().equalsIgnoreCase(FinancialConstants.INSTRUMENT_CANCELLED_STATUS)){
				continue;
			}else{
				map.get(row.getChequeNumber()).setVoucherNumber("MULTIPLE");
				row.setVoucherNumber(MULTIPLE);
			}
		}if(LOGGER.isDebugEnabled())     LOGGER.debug("End updateVoucherNumber ");
	}

	private void validateDates(Date fromDate, Date toDate) {
		if(fromDate.compareTo(toDate) == 1){
			throw new ValidationException(Arrays.asList(new ValidationError("invalid.from.date", "invalid.from.date")));
		}
	}

@Action(value="/report/chequeIssueRegisterReport-generatePdf")
	public String generatePdf() throws JRException, IOException{
		generateReport();
		List<Object> data = new ArrayList<Object>();
		data.addAll(getChequeIssueRegisterList());
		inputStream = reportHelper.exportPdf(getInputStream(), jasperpath, getParamMap(), data);
		return "PDF";
	}

@Action(value="/report/chequeIssueRegisterReport-generateXls")
	public String generateXls() throws JRException, IOException{
		generateReport();
		List<Object> data = new ArrayList<Object>();
		data.addAll(getChequeIssueRegisterList());
		inputStream = reportHelper.exportXls(getInputStream(), jasperpath, getParamMap(), data);
		return "XLS";
	}

@Action(value="/report/chequeIssueRegisterReport-ajaxPrint")
	public String ajaxPrint() throws JRException, IOException{
		generateReport();
		return "result";
	}
	
	@Override
	public Object getModel() {
		return null;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public String getFormattedDate(Date date){
		SimpleDateFormat formatter = Constants.DDMMYYYYFORMAT1;
		return formatter.format(date);
	}
	private Date getNextDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}
	protected Map<String, Object> getParamMap() {
		accountNumber = (Bankaccount) persistenceService.find("from Bankaccount where id=?",accountNumber.getId());
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("bank", getFormattedBankName());
		paramMap.put("accountNumber", accountNumber.getAccountnumber());
		paramMap.put("fromDate", Constants.DDMMYYYYFORMAT1.format(fromDate));
		paramMap.put("toDate", Constants.DDMMYYYYFORMAT1.format(toDate));
		paramMap.put("ulbName", ulbName);
		if(department!=null && department.getId()!=null && department.getId()!=0){
			Department dept = (Department) persistenceService.find("from Department where id=?",department.getId());
			paramMap.put("departmentName", dept.getDeptName());
		}
		return paramMap;
	}
	
	public String getFormattedBankName(){
		String[] bankData = bank.split("-");
		Bank bank = (Bank) persistenceService.find("from Bank where id=?",Integer.valueOf(bankData[0]));
		Bankbranch bankBranch = (Bankbranch) persistenceService.find("from Bankbranch where id=?",Integer.valueOf(bankData[1]));
		String name = "";
		if(bank!=null && bankBranch!=null){
			name = bank.getName().concat(" - ").concat(bankBranch.getBranchname());
		}
		return name;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public List<ChequeIssueRegisterDisplay> getChequeIssueRegisterList() {
		return chequeIssueRegisterList;
	}
	public void setChequeIssueRegisterList(List<ChequeIssueRegisterDisplay> chequeIssueRegisterList) {
		this.chequeIssueRegisterList = chequeIssueRegisterList;
	}
	public void setAccountNumber(Bankaccount bankAccount) {
		this.accountNumber = bankAccount;
	}
	public Bankaccount getAccountNumber() {
		return accountNumber;
	}
	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}
	
	private String getExcludeVoucherStatues(){
		List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("finance","statusexcludeReport");
		String statusExclude = "-1"; 
		statusExclude = appList.get(0).getValue();
		return statusExclude;
	}
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	
	private void populateUlbName(){
		SQLQuery query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
		List<String> result = query.list();
		if(result!=null)
			setUlbName(result.get(0));
	}
	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;
	}
	public String getUlbName() {
		return ulbName;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBank() {
		return bank;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Department getDepartment() {
		return department;
	}
	public void setChequeToNumber(String chequeToNumber) {
		this.chequeToNumber = chequeToNumber;
	}
	public String getChequeToNumber() {
		return chequeToNumber;
	}
	public void setChequeFromNumber(String chequeFromNumber) {
		this.chequeFromNumber = chequeFromNumber;
	}
	public String getChequeFromNumber() {
		return chequeFromNumber;
	}
}
