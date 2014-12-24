/**
 * 
 */
package org.egov.works.web.actions.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.services.recoveries.RecoveryService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.utils.EgovPaginatedList;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.WorkOrderService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
@Results({
@Result(name=TaxDeductionStatementAction.EXPORTPDF_42,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache;filename=Form42-Report.pdf"}),
@Result(name=TaxDeductionStatementAction.EXPORTEXCEL_42,type="stream",location="ExcelInputStream", params={"inputName","ExcelInputStream","contentType","application/msexcel","contentDisposition","no-cache;filename=Form42-Report.xls"}),
@Result(name=TaxDeductionStatementAction.EXPORTPDF_26C,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache;filename=Form26C-Report.pdf"}),
@Result(name=TaxDeductionStatementAction.EXPORTEXCEL_26C,type="stream",location="ExcelInputStream", params={"inputName","ExcelInputStream","contentType","application/msexcel","contentDisposition","no-cache;filename=Form26C-Report.xls"})
})
public class TaxDeductionStatementAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TaxDeductionStatementAction.class);
	private Date fromDate;
	private Date toDate;
	private RecoveryService recoveryService;
	private WorkOrderService workOrderService;
	private List<TaxDeductionStmntBean> reportList;
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private Long recoveryglcodeId;
	private static Map<String,List<String>> netAccountCode =  new HashMap<String, List<String>>();
	private String rptNo;
	private ReportService reportService;
	private InputStream pdfInputStream;
	private InputStream excelInputStream;
	private EgovCommon egovCommon;
	
	private static final String REPORT_42="42";
	private static final String REPORT_26C="26C";
	public static final String  EXPORTPDF_42 = "exportPdf42";
	public static final String  EXPORTEXCEL_42 = "exportExcel42";
	public static final String  EXPORTPDF_26C = "exportPdf26C";
	public static final String  EXPORTEXCEL_26C = "exportExcel26C";
	
	@Override
	public Object getModel() {
		
		return null;
	}
	
	@Override
	public void prepare() {
		super.prepare();
		addDropdownData("recoveryList" , recoveryService.getAllTdsByPartyType("Contractor"));
		
	}
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String query = getQuery();
		
		return new SearchQuerySQL(query,"select count(*) from ( "+query+" )",null);
	}

	public String newform(){
		return "new";
	}
	@ValidationErrorPage(value="new")
	public String list(){
		validateDate();
		LOGGER.debug("TaxDeductionStatementAction | list | start");
		setPageSize(30);
		search();
		formatSearchResult();
		LOGGER.debug("TaxDeductionStatementAction | list | End");
		return "new";
	}
	
	public String exportToPdf() throws JRException,Exception{
		ReportRequest reportRequest; 
		reportRequest = new ReportRequest("TaxDeductionStatement",getReportData(), createHeaderParams());
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		if(rptNo!=null && rptNo.equalsIgnoreCase(REPORT_26C))
			return EXPORTPDF_26C; 
		else
			return EXPORTPDF_42;
	}
	
	public String exportToExcel() throws JRException,Exception{
		ReportRequest reportRequest; 
		reportRequest = new ReportRequest("TaxDeductionStatement",getReportData(), createHeaderParams());
		reportRequest.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			excelInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
		if(rptNo!=null && rptNo.equalsIgnoreCase(REPORT_26C))
			return EXPORTEXCEL_26C; 
		else
			return EXPORTEXCEL_42;
	}
	
	private Map<String,Object> createHeaderParams()
	{
		Map<String,Object> reportParams = new HashMap<String,Object>();
		if(rptNo!=null && rptNo.equalsIgnoreCase(REPORT_26C))
		{
			reportParams.put("reportTitle", "FORM No. 26 C");
			reportParams.put("reportSubTitle", "SEE RULE 37 (2-C)");
			reportParams.put("reportHeading", "Statement of deduction of tax under section 194-C of the Income tax act 1961 from payment made to contractors or sub-contractors Name and Address of person responsible for making the payment : NAGPUR MUNICIPAL CORPORATION, NAGPUR.");
		}
		if(rptNo!=null && rptNo.equalsIgnoreCase(REPORT_42))
		{
			reportParams.put("reportTitle", "FORM No XXXXII");
			reportParams.put("reportSubTitle", "SEE Section6(3) Under work Contract Act");
			reportParams.put("reportHeading", "Statement of deduction of tax under section 14 of the Maharashtra tax laws(levy,Amendment and validation) act 19 from payment made to contractors or sub-contractors"+" \n "+"Name and Address of person responsible for making the payment : NAGPUR MUNICIPAL CORPORATION, NAGPUR.");
		}
		return reportParams;
	}
	
	@SuppressWarnings("unchecked")
	private List<TaxDeductionStmntBean> getReportData()
	{
		String query = getQuery();
		List<Object[]> reportData = persistenceService.getSession().createSQLQuery(query).list();
		return formatSearchResultForExport(reportData);
	}
	
	public void validateDate(){
		
		if(null == fromDate){
			throw new ValidationException(Arrays.asList(new ValidationError("fromDate","from date can not be blank")));
		}
		else if(null == toDate){
			throw new ValidationException(Arrays.asList(new ValidationError("toDate","to date can not be blank")));
		}
		else if(fromDate.after(toDate)){
			throw new ValidationException(Arrays.asList(new ValidationError("fromDate.toDate","From date should be lesser than or equal to to date")));
		}

		CFinancialYear finYear1 = getFinancialYear(DDMMYYYYFORMATS.format(fromDate));
		CFinancialYear finYear2 = getFinancialYear(DDMMYYYYFORMATS.format(toDate));
		
		if(null == finYear1){
			throw new ValidationException(Arrays.asList(new ValidationError("date.finyear","financial year is not defined for "+DDMMYYYYFORMATS.format(fromDate))));	
		}else if (null == finYear2){
			throw new ValidationException(Arrays.asList(new ValidationError("date.finyear","financial year is not defined for "+DDMMYYYYFORMATS.format(toDate))));
		}else if(! finYear1.getId().equals(finYear2.getId())){
			throw new ValidationException(Arrays.asList(new ValidationError("date.finyear","from date and to date should fall in same financial year")));
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private CFinancialYear getFinancialYear(String date){
		Session session  = HibernateUtil.getCurrentSession();
		StringBuffer query1 = new StringBuffer(100);
		query1.append("from CFinancialYear where startingDate <= to_date('").append(date).
		append("','dd/MM/yyyy') and endingDate >= to_date('").append(date).append("','dd/MM/yyyy')");
		List<CFinancialYear> list1 =(List<CFinancialYear> ) session.createQuery(query1.toString()).list();
		return list1 != null?list1.get(0):null;
	}
	private String getQuery(){
		
		StringBuffer query = new StringBuffer(100);
		query.append(" select b.id,bd.creditamount,vh.voucherdate,b.billnumber,rgld.id as rmitgldtlid, gl.glcodeid from eg_billregister b, eg_billregistermis mis,").
		append(" eg_billdetails bd,  voucherheader vh,generalledger gl,generalledgerdetail gld,eg_remittance_gldtl rgld ,Tds tds,EG_PARTYTYPE party ").
		append(" where b.id= bd.billid and b.id=mis.billid  and mis.voucherheaderid=vh.id and tds.glcodeid = bd.glcodeid and ").
		append(" gl.voucherheaderid = vh.id and gld.generalledgerid = gl.id and rgld.gldtlid = gld.id and b.expendituretype='Works' ").
		append(" and party.id= tds.partytypeid and bd.creditamount >0 AND vh.status=0 and party.code='Contractor'");
		if(null!=fromDate){
			query.append(" and b.billdate >= to_date('"+DDMMYYYYFORMATS.format(fromDate)+"','dd/MM/yyyy')");
		}
		if(null!=toDate){
			query.append(" and b.billdate <= to_date('"+DDMMYYYYFORMATS.format(toDate)+"','dd/MM/yyyy')");
		}
		if(null != recoveryglcodeId){
			query.append(" and bd.glcodeid="+recoveryglcodeId);
			query.append(" and gl.glcodeid="+recoveryglcodeId);
		}
		query.append(" order by gld.detailtypeid,gld.detailkeyid");
		LOGGER.debug("TaxDeductionStatementAction | query >> "+ query.toString());
		return query.toString();
		
	}
	
	@SuppressWarnings("unchecked")
	protected void formatSearchResult() {
		LOGGER.debug("TaxDeductionStatementAction | formatSearchResult | start");
		reportList = new ArrayList<TaxDeductionStmntBean>();
		EgovPaginatedList egovPaginatedList = (EgovPaginatedList)searchResult;
		List<Object[]> list = egovPaginatedList.getList();
		if(list==null || list.isEmpty())
			return;
		LOGGER.debug("TaxDeductionStatementAction | formatSearchResult | list size : "+list.size() );
		List<String> uniqueEntity = new ArrayList<String>(); // used to display the entity (contractor) name once.
		
		//Create BillId List for financials API
		List<Long> billIdList = new ArrayList<Long>();
		Map<Long,Map<String,String>> billPaymentDetailMap = null;
		Map<String,String> paymentSumAndDateMap = null;
		Map<Long, String> remittanceMap = null;
		for(Object[] object : list) {
			billIdList.add(Long.valueOf(object[0].toString()));
		}
		billPaymentDetailMap = egovCommon.getTotalPaymentAmountAndPaymentDates(billIdList);
		Long remittanceGlCodeId;
		for(Object[] object : list) {
			
			EgBillPayeedetails payee = getEntityDetails(Long.valueOf(object[0].toString()));
			
			if(null != payee){ // ignore the bills for which the net payable code is not correct
				TaxDeductionStmntBean taxDeductionStmntBean = new TaxDeductionStmntBean();
				WorkOrder workOrder = workOrderService.getWOForBillId(Long.valueOf(object[0].toString()));
				if(null != workOrder){
					DecimalFormat df = new DecimalFormat("###.##");
					taxDeductionStmntBean.setGrossValue(new BigDecimal(df.format(workOrderService.getRevisionEstimateWOAmount(workOrder))).setScale(2));
				}
				
				taxDeductionStmntBean.setTaxDeductedAmt(new BigDecimal(object[1].toString()).setScale(2));
				taxDeductionStmntBean.setVoucherDate(DDMMYYYYFORMATS.format((Date)object[2]));
				
				if(!uniqueEntity.contains(payee.getAccountDetailTypeId()+"~"+payee.getAccountDetailKeyId())){
					EntityType entity = getEntityInfo(payee.getAccountDetailKeyId(), payee.getAccountDetailTypeId());
					//Contractor con = (Contractor) entity;
					String nameAndAddr = entity.getName();
					/*if(null!=con &&  null != con.getPaymentAddress()){
						nameAndAddr = nameAndAddr +" , " +con.getPaymentAddress();
					}*/
					taxDeductionStmntBean.setName(nameAndAddr); 
					uniqueEntity.add(payee.getAccountDetailTypeId()+"~"+payee.getAccountDetailKeyId());
				}
				
				taxDeductionStmntBean.setNetPayble(payee.getEgBilldetailsId().getCreditamount().setScale(2));
				
				if(billPaymentDetailMap != null)
					paymentSumAndDateMap = billPaymentDetailMap.get(Long.valueOf(object[0].toString()));
				if(paymentSumAndDateMap != null && paymentSumAndDateMap.get("date") != null) {
					taxDeductionStmntBean.setPaymentDate(paymentSumAndDateMap.get("date"));  
				}	
				remittanceGlCodeId = new Long(object[5].toString());
				if(remittanceGlCodeId!=null)
				{
					remittanceMap = egovCommon.getDeductionDateList(billIdList, remittanceGlCodeId);
					if(remittanceMap !=null && remittanceMap.get(Long.valueOf(object[0].toString()))!=null)
						taxDeductionStmntBean.setRemittedDate(remittanceMap.get(Long.valueOf(object[0].toString())));
				}
				
				reportList.add(taxDeductionStmntBean);
			}
			
		}
		egovPaginatedList.setList(reportList);
	}

	private List<TaxDeductionStmntBean> formatSearchResultForExport(List<Object[]> list ) {
		LOGGER.debug("TaxDeductionStatementAction | formatSearchResultForExport | list size : "+list.size() );
		List<String> uniqueEntity = new ArrayList<String>(); // used to display the entity (contractor) name once.
		List<TaxDeductionStmntBean> resultBeanList = new ArrayList<TaxDeductionStmntBean>();
		
		List<Long> billIdList = new ArrayList<Long>();
		Map<Long,Map<String,String>> billPaymentDetailMap = null;
		Map<String,String> paymentSumAndDateMap = null;
		Map<Long, String> remittanceMap = null;
		if(list==null || list.isEmpty())
			return null;
		for(Object[] object : list) {
			billIdList.add(Long.valueOf(object[0].toString()));
		}
		billPaymentDetailMap = egovCommon.getTotalPaymentAmountAndPaymentDates(billIdList);
		Long remittanceGlCodeId;
		
		for(Object[] object : list) {
			
			EgBillPayeedetails payee = getEntityDetails(Long.valueOf(object[0].toString()));
			
			if(null != payee){ // ignore the bills for which the net payable code is not correct
				TaxDeductionStmntBean taxDeductionStmntBean = new TaxDeductionStmntBean();
				WorkOrder workOrder = workOrderService.getWOForBillId(Long.valueOf(object[0].toString()));
				if(null != workOrder){
					DecimalFormat df = new DecimalFormat("###.##");
					taxDeductionStmntBean.setGrossValue(new BigDecimal(df.format(workOrderService.getRevisionEstimateWOAmount(workOrder))));
				}
				
				taxDeductionStmntBean.setTaxDeductedAmt(new BigDecimal(object[1].toString()));
				taxDeductionStmntBean.setVoucherDate(DDMMYYYYFORMATS.format((Date)object[2]));
				
				if(!uniqueEntity.contains(payee.getAccountDetailTypeId()+"~"+payee.getAccountDetailKeyId())){
					EntityType entity = getEntityInfo(payee.getAccountDetailKeyId(), payee.getAccountDetailTypeId());
					//Contractor con = (Contractor) entity;
					String nameAndAddr = entity.getName();
					/*if(null!=con &&  null != con.getPaymentAddress()){
						nameAndAddr = nameAndAddr +" , " +con.getPaymentAddress();
					}*/
					taxDeductionStmntBean.setName(nameAndAddr); 
					uniqueEntity.add(payee.getAccountDetailTypeId()+"~"+payee.getAccountDetailKeyId());
				}
				
				taxDeductionStmntBean.setNetPayble(payee.getEgBilldetailsId().getCreditamount());
				
				if(billPaymentDetailMap != null)
					paymentSumAndDateMap = billPaymentDetailMap.get(Long.valueOf(object[0].toString()));
				if(paymentSumAndDateMap != null && paymentSumAndDateMap.get("date") != null) {
					taxDeductionStmntBean.setPaymentDate(paymentSumAndDateMap.get("date"));  
				}	
				
				remittanceGlCodeId = new Long(object[5].toString());
				if(remittanceGlCodeId!=null)
				{
					remittanceMap = egovCommon.getDeductionDateList(billIdList, remittanceGlCodeId);
					if(remittanceMap !=null && remittanceMap.get(Long.valueOf(object[0].toString()))!=null)
						taxDeductionStmntBean.setRemittedDate(remittanceMap.get(Long.valueOf(object[0].toString())));
				}
				
				resultBeanList.add(taxDeductionStmntBean);
			}
			
		}
		return resultBeanList;
	}
	/*
	@SuppressWarnings("unchecked")
	private String getRemittedDate(Integer remitGlDtlId){
		StringBuffer remitedDate =new StringBuffer("");
		
		List<EgRemittanceDetail> egRemitDetailList = (List<EgRemittanceDetail>)persistenceService.findAllBy(" from EgRemittanceDetail where egRemittanceGldtl.id=?",remitGlDtlId);
		if(null != egRemitDetailList && egRemitDetailList.size() >0){
			for (EgRemittanceDetail egRemittanceDetail : egRemitDetailList) {
				if(StringUtils.isEmpty(remitedDate.toString())){
					remitedDate.append(DDMMYYYYFORMATS.format(egRemittanceDetail.getEgRemittance().getVoucherheader().getVoucherDate()));
				}else{
					remitedDate.append(" | ").append(DDMMYYYYFORMATS.format(egRemittanceDetail.getEgRemittance().getVoucherheader().getVoucherDate()));
				}
				
			}
			
		}
		
		
		return remitedDate.toString();
	}
	
	@SuppressWarnings("unchecked")
	private String getPaymentDate(String billNumber){
		
		StringBuffer paymentDate =new StringBuffer("");
		
		List<Miscbilldetail> miscBillList =(List<Miscbilldetail>)persistenceService.findAllBy(" from Miscbilldetail where billnumber=?", billNumber);
		if(null!= miscBillList && miscBillList.size()>0){
			for (Miscbilldetail miscbilldetail : miscBillList) {
				if(null != miscbilldetail.getPayVoucherHeader() && miscbilldetail.getPayVoucherHeader().getStatus().
						equals(Integer.valueOf(FinancialConstants.CREATEDVOUCHERSTATUS))){
					if(StringUtils.isEmpty(paymentDate.toString())){
						paymentDate.append(DDMMYYYYFORMATS.format(miscbilldetail.getPayVoucherHeader().getVoucherDate()));
					}else{
						paymentDate.append(" | ").append(DDMMYYYYFORMATS.format(miscbilldetail.getPayVoucherHeader().getVoucherDate()));
					}
					
				}
			}
		}
		return paymentDate.toString();
	}*/
	private EgBillPayeedetails getEntityDetails(Long billId){
		List<String> listOfNetPayGlIds = netAccountCode.get("Works");
		StringBuffer netPayCodes = new StringBuffer(30);
		for (String netCode : listOfNetPayGlIds) {
			if(!StringUtils.isEmpty(netPayCodes.toString()))
				netPayCodes.append(",").append(netCode);
			else
				netPayCodes.append(netCode);
			
		}
		StringBuffer query = new StringBuffer(160);
		query.append("select payee from EgBillregister b , EgBillPayeedetails payee , EgBilldetails bd where payee.egBilldetailsId.id = bd.id ").
		append(" and b.id=bd.egBillregister.id and bd.creditamount >0  and bd.glcodeid in(").append(netPayCodes.toString()).append(")").
		append(" and b.id="+billId);
		EgBillPayeedetails payee = (EgBillPayeedetails)persistenceService.find(query.toString());
		
		return payee;
	}
	
	public EntityType getEntityInfo(final Integer detailKeyId,final Integer detailtypeId) throws ValidationException
	{	
		EntityType entity = null;
		try {
				Accountdetailtype accountdetailtype= (Accountdetailtype)persistenceService.find(" from Accountdetailtype where id="+detailtypeId);
				Class<?> service = Class.forName(accountdetailtype.getFullQualifiedName());
				// getting the entity type service.
				String detailTypeName = service.getSimpleName();
				String detailTypeService =  detailTypeName.substring(0,1).toLowerCase()+detailTypeName.substring(1)+"Service";
				WebApplicationContext wac= WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
				PersistenceService entityPersistenceService=(PersistenceService)wac.getBean(detailTypeService);
				String dataType = "";
				// required to know data type of the id of the detail  type object.
				java.lang.reflect.Method method = service.getMethod("getId");
				dataType = method.getReturnType().getSimpleName();
				if ( dataType.equals("Long") ){
					entity=(EntityType)entityPersistenceService.findById(Long.valueOf(detailKeyId.toString()),false);
				}else{
					entity=(EntityType)entityPersistenceService.findById(detailKeyId,false);
				}
		} catch (Exception e) {
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getMessage()));
			 throw new ValidationException(errors);
		}
		return entity;
		
	}
	static {
		
		// setting net pay account codes for works type.
		String query = "from AppConfigValues where key.module=:module and key.keyName=:keyName";
		Session session = HibernateUtil.getCurrentSession();
		List<String> wBillNetPayCodeList = new ArrayList<String>();
		Query query4 = session.createQuery(query);
		query4.setString("module", "EGF");
		query4.setString("keyName", "worksBillPurposeIds");
		List<AppConfigValues> wBillNetPurpose =(List<AppConfigValues>)query4.list();
		for (AppConfigValues appConfigValues : wBillNetPurpose) {
				List<CChartOfAccounts> coaList = (List<CChartOfAccounts>) session.createQuery("from CChartOfAccounts where purposeId="+appConfigValues.getValue()).list();
				for (CChartOfAccounts chartOfAccounts : coaList) {
					wBillNetPayCodeList.add(chartOfAccounts.getId().toString());
				}
		}
		netAccountCode.put("Works", wBillNetPayCodeList);
	}
	
	public void setRecoveryService(RecoveryService recoveryService) {
		this.recoveryService = recoveryService;
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

	public Long getRecoveryglcodeId() {
		return recoveryglcodeId;
	}

	public void setRecoveryglcodeId(Long recoveryglcodeId) {
		this.recoveryglcodeId = recoveryglcodeId;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public String getRptNo() {
		return rptNo;
	}

	public void setRptNo(String rptNo) {
		this.rptNo = rptNo;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public InputStream getPdfInputStream() {
		return pdfInputStream;
	}

	public InputStream getExcelInputStream() {
		return excelInputStream;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

}
