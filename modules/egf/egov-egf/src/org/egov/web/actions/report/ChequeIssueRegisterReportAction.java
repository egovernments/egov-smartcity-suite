package org.egov.web.actions.report;

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

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
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
	private DepartmentImpl department;
	private Bankaccount accountNumber;
	ReportHelper reportHelper;
	private InputStream inputStream;
	private EgovCommon egovCommon;
	private GenericHibernateDaoFactory genericDao;
	private String ulbName = "";
	private String bank;

	public ChequeIssueRegisterReportAction() {
		addRelatedEntity(Constants.EXECUTING_DEPARTMENT, DepartmentImpl.class);
	}

	@Override
	public void prepare() {
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
		accountNumber = (Bankaccount) persistenceService.find("from Bankaccount where id=?",accountNumber.getId());
		validateDates(fromDate,toDate);
		persistenceService.setType(InstrumentHeader.class);
		Query query = persistenceService.getSession().createSQLQuery("select ih.instrumentnumber as chequeNumber,ih.instrumentdate as chequeDate," +
				"ih.instrumentamount as chequeAmount,vh.vouchernumber as voucherNumber,vh.voucherdate as voucherDate,vh.name as voucherName,ih.payto as payTo,mbd.billnumber as billNumber," +
				"mbd.billDate as billDate,vh.type as type,es.DESCRIPTION as chequeStatus from egf_instrumentHeader ih,egf_instrumentvoucher iv,EGW_STATUS es," +
				"voucherheader vh left outer join miscbilldetail mbd on  vh.id=mbd.PAYVHID ,vouchermis vmis where ih.instrumentDate <'"
				+getFormattedDate(getNextDate(toDate))+"' and ih.instrumentDate>='"+getFormattedDate(fromDate)+"' and ih.isPayCheque='1' " +
						"and ih.INSTRUMENTTYPE=(select id from egf_instrumenttype where TYPE='cheque' ) and vh.status not in ("+getExcludeVoucherStatues()+") and vh.id=iv.voucherheaderid and  bankAccountId="
				+accountNumber.getId()+" and ih.id=iv.instrumentheaderid and ih.id_status=es.id and vmis.voucherheaderid=vh.id "+createQuery()+" order by ih.instrumentDate,ih.instrumentNumber")
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
				.addScalar("chequeStatus")
				.setResultTransformer(Transformers.aliasToBean(ChequeIssueRegisterDisplay.class));
		chequeIssueRegisterList = query.list();
		if(chequeIssueRegisterList==null)
			chequeIssueRegisterList = new ArrayList<ChequeIssueRegisterDisplay>();
		updateBillNumber();
		updateVoucherNumber();
		removeDuplicates();
	}
	

	private void removeDuplicates() {
		Map<String,ChequeIssueRegisterDisplay> map = new HashMap<String, ChequeIssueRegisterDisplay>();
		for (Iterator<ChequeIssueRegisterDisplay> row = chequeIssueRegisterList.iterator(); row.hasNext();) {
			ChequeIssueRegisterDisplay next = row.next();
			if(map.get(next.getChequeNumber())==null)
				map.put(next.getChequeNumber(), next);
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
	}
	
	private void updateVoucherNumber(){
		Map<String,ChequeIssueRegisterDisplay> map = new HashMap<String, ChequeIssueRegisterDisplay>();
		for (ChequeIssueRegisterDisplay row : chequeIssueRegisterList) {
			if(map.get(row.getChequeNumber())==null)
				map.put(row.getChequeNumber(), row);
			else if(row.getVoucherNumber() != null && row.getVoucherNumber().equalsIgnoreCase(map.get(row.getChequeNumber()).getVoucherNumber()))
				continue;
			else{
				map.get(row.getChequeNumber()).setVoucherNumber("MULTIPLE");
				row.setVoucherNumber(MULTIPLE);
			}
		}
	}

	private void validateDates(Date fromDate, Date toDate) {
		if(fromDate.compareTo(toDate) == 1){
			throw new ValidationException(Arrays.asList(new ValidationError("invalid.from.date", "invalid.from.date")));
		}
	}

	public String generatePdf() throws JRException, IOException{
		generateReport();
		List<Object> data = new ArrayList<Object>();
		data.addAll(getChequeIssueRegisterList());
		inputStream = reportHelper.exportPdf(getInputStream(), jasperpath, getParamMap(), data);
		return "PDF";
	}

	public String generateXls() throws JRException, IOException{
		generateReport();
		List<Object> data = new ArrayList<Object>();
		data.addAll(getChequeIssueRegisterList());
		inputStream = reportHelper.exportXls(getInputStream(), jasperpath, getParamMap(), data);
		return "XLS";
	}

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
			DepartmentImpl dept = (DepartmentImpl) persistenceService.find("from DepartmentImpl where id=?",department.getId());
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
		SQLQuery query = persistenceService.getSession().createSQLQuery("select name from companydetail");
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
	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}
	public DepartmentImpl getDepartment() {
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
