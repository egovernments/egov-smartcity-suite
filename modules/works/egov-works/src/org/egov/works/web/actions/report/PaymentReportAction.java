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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.StringUtils;
import org.egov.model.bills.EgBillregister;
import org.egov.model.budget.BudgetGroup;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.utils.EgovPaginatedList;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.ContractorService;
import org.egov.works.services.WorkOrderService;
import org.hibernate.Session;
import org.joda.time.LocalDate;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Results({
@Result(name=PaymentReportAction.EXPORTPDF,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache;filename=WO_PaymentReport.pdf"}),
@Result(name=PaymentReportAction.EXPORTEXCEL,type="stream",location="ExcelInputStream", params={"inputName","ExcelInputStream","contentType","application/msexcel","contentDisposition","no-cache;filename=WO_PaymentReport.xls"})
})
   
public class PaymentReportAction extends SearchFormAction{
	
	private static final Logger logger = Logger.getLogger(PaymentReportAction.class);
	private static final String  SEARCH="search";
	
	private Long contractorId;
	private Date  fromDate;
	private Date  toDate;
	private String jurisdictionName;
	private Integer jurisdictionId;
	private String budgetHeadName;
	private Long budgetHeadId;
	private String billType;
	private String reportSubTitle;
	private List<Object> paramList;
	private List<WOReportBean> reportList;
	private InputStream pdfInputStream;
	private InputStream excelInputStream;
	
	private WorkOrderService workOrderService;
	private ContractorService contractorService;
	private static final String COMMA_SEPARATOR = ", ";
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	
	public static final String  EXPORTPDF = "exportPdf";
	public static final String  EXPORTEXCEL = "exportExcel";
	private ReportService reportService;
	private EgBillregister egBill;
	private EgovCommon egovCommon;
	private BigDecimal totalAmount = new BigDecimal(0);
	

	@Override
	public Object getModel() {
		return null;
	}

	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) 
	{
		String query = getSearchQuery();
		String countQry =  " select count(distinct mb.mbHeader.workOrder) " + query +" ";
		String query2 = "select distinct mb.mbHeader.workOrder " + query + " ";  
		setPageSize(30);
		return new SearchQueryHQL(query2,countQry,paramList);
	}

	public String search()
	{
		return SEARCH;
	}
	
	public void prepare()
	{
		return;
	}
	
	@ValidationErrorPage(value=SEARCH)
	public String searchList()
	{
		validateDate();
		validateDateFinYr();
		super.search();
		formatSearchResult();
		return SEARCH;
	}

	public void validateDate(){
		
		if(fromDate!=null && toDate==null){
			throw new ValidationException(Arrays.asList(new ValidationError("enddate",getText("search.billendDate.null"))));
			
		}
		if(toDate!=null && fromDate==null){
			throw new ValidationException(Arrays.asList(new ValidationError("startdate",getText("search.billstartDate.null"))));		
			
		}
		
		if(!DateUtils.compareDates(getToDate(),getFromDate())){
			throw new ValidationException(Arrays.asList(new ValidationError("enddate",getText("greaterthan.endDate.fromDate"))));
		}
	}
	
	public void validateDateFinYr(){
	
		
		if(fromDate!=null && toDate!=null)
		{
			if(fromDate.after(toDate)){
				throw new ValidationException(Arrays.asList(new ValidationError("fromDate.toDate",getText("billfFromDate.greater.ToDate"))));
			}
	
			CFinancialYear finYear1 = getFinancialYear(DDMMYYYYFORMATS.format(fromDate));
			CFinancialYear finYear2 = getFinancialYear(DDMMYYYYFORMATS.format(toDate));
			
			if(! finYear1.getId().equals(finYear2.getId())){
				throw new ValidationException(Arrays.asList(new ValidationError("date.finyear",getText("billdates.same.finYear"))));
			}
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

	private String getSearchQuery()
	{
		logger.debug("---------Beginning of getSearchQuery---------");
		paramList = new ArrayList<Object>();
		StringBuilder dynQuery = new StringBuilder(800);
		StringBuffer titleBuffer = new StringBuffer();
		String queryBeginning =" ";
		
		queryBeginning = "from MBBill mb where mb.id is not null and mb.egBillregister.billstatus='APPROVED'";
		
		titleBuffer.append("List of payments made ");
		
		if(contractorId!=null && contractorId!=-1) 
		{
			dynQuery.append("and mb.mbHeader.workOrder.contractor.id = ?"); 
			paramList.add(contractorId);
			Contractor cs = contractorService.findById(contractorId, false);
			titleBuffer.append("for contractor "+cs.getName().toString());
		}
		if(fromDate!=null && toDate!=null){
			dynQuery.append(" and trunc(mb.egBillregister.billdate) >= ?");
			dynQuery.append(" and trunc(mb.egBillregister.billdate) <= ?");
			paramList.add(new Date(DateUtils.getFormattedDate(fromDate,"dd-MMM-yyyy"))); 
			paramList.add(new Date(DateUtils.getFormattedDate(toDate,"dd-MMM-yyyy")));
	//		dynQuery.append(" and trunc(mb.egBillregister.billdate) >= '"+DateUtils.getFormattedDate(fromDate,"dd-MMM-yyyy")+"' ");
	//		dynQuery.append(" and trunc(mb.egBillregister.billdate) <= '"+DateUtils.getFormattedDate(toDate,"dd-MMM-yyyy")+"' ");
			
			titleBuffer.append(" with date range - "+DateUtils.getFormattedDate(fromDate,"dd/MM/yyyy")+" to "+DateUtils.getFormattedDate(toDate,"dd/MM/yyyy"));
		}
		if(jurisdictionId != null && jurisdictionId != -1) { 
			dynQuery.append(" and mb.mbHeader.workOrderEstimate.estimate.ward.id = ? "); 
			paramList.add(jurisdictionId);
			titleBuffer.append(" in Jurisdiction - "+jurisdictionName);
		}
	
		if(budgetHeadId != null && budgetHeadId != -1) {			
			dynQuery.append(" and mb.mbHeader.workOrderEstimate.estimate.financialDetails[0].budgetGroup is not null and mb.mbHeader.workOrderEstimate.estimate.financialDetails[0].budgetGroup.id=? ");
			paramList.add(budgetHeadId);
			titleBuffer.append(" with budget head - "+budgetHeadName);
		}
		
		if(billType!=null && !"".equals(billType) && !"-1".equals(billType)) 
		{
			dynQuery.append(" and mb.egBillregister.billtype = ? ");
			paramList.add(billType);
			titleBuffer.append(" for the bill type - "+billType);
		}

		if(fromDate!=null && toDate!=null && !DateUtils.compareDates(getToDate(),getFromDate()))
			addFieldError("fromDate",getText("greaterthan.endDate.fromDate"));
		if(toDate!=null && !DateUtils.compareDates(new Date(),getToDate()))
			addFieldError("toDate",getText("greaterthan.endDate.currentdate"));
		
		//dynQuery.append(" and mb.egBillregister.billdate in (select max(egb.billdate) from EgBillregister egb where egb.workordernumber = mb.egBillregister.workordernumber )");
		reportSubTitle = titleBuffer.toString();
		logger.debug("---------reportSubTitle--------->>"+reportSubTitle);
		logger.debug("---------End of getSearchQuery---------");
		logger.debug("---------SearchQuery Returned---------"+queryBeginning+dynQuery.toString());

		return queryBeginning+dynQuery.toString();
	}
	
	private boolean isWithin(Date startDate, Date endDate, Date dateTime){
		LocalDate start = new LocalDate(startDate); 
		LocalDate end = new LocalDate(endDate);
		LocalDate date=new LocalDate(dateTime);		
		return start.compareTo(date)<=0 && end.compareTo(date)>=0;
	}
	
	@SuppressWarnings("unchecked")
	protected void formatSearchResult() {		
		
		reportList = new ArrayList<WOReportBean>();
		EgovPaginatedList egovPaginatedList = (EgovPaginatedList)searchResult;
		
		List<WorkOrder> list = egovPaginatedList.getList();
		WOReportBean repBean;
		
		for(WorkOrder wo : list)
		{
			repBean = new WOReportBean();
			for(WorkOrderEstimate woe : wo.getWorkOrderEstimates())
			{
				repBean.setEstNumAndDate(woe.getEstimate().getEstimateNumber());
				repBean.setNameOfWork(woe.getEstimate().getName());
				repBean.setJurisdiction(woe.getEstimate().getWard().getName());
				BudgetGroup bgtgrp = woe.getEstimate().getFinancialDetails().get(0).getBudgetGroup();
				if(bgtgrp!=null)
				{
					repBean.setBudgetHead(bgtgrp.getMaxCode().getGlcode()+" ~ "+bgtgrp.getName());
				}
				repBean.setProjectCode(woe.getEstimate().getProjectCode().getCode());
				repBean.setWONumAndDate(woe.getWorkOrder().getWorkOrderNumber());
				repBean.setContractorName(woe.getWorkOrder().getContractor().getName());
			}
			Date maxBillDate = null;
			List<Long> billIdList = new ArrayList<Long>();
			
			String lastBillType = "";
			String billNoDate = "";
			BigDecimal paymentAmt = new BigDecimal(0);
			for(MBHeader mbh : wo.getMbHeaders())
			{
				for(MBBill mb : mbh.getMbBills())
				{
					if(mb.getEgBillregister().getBillstatus().equals("APPROVED")) 
					{
						/* all 3 selected */
						if((billType!=null && !"".equals(billType) && !"-1".equals(billType) && billType.equals(mb.getEgBillregister().getBilltype())) &&
								(fromDate!=null && toDate!=null && isWithin(fromDate,toDate,mb.getEgBillregister().getBilldate()))) 
						{
							billIdList.add(mb.getEgBillregister().getId());
							if(maxBillDate == null)
							{
								maxBillDate = mb.getEgBillregister().getBilldate();
								lastBillType = mb.getEgBillregister().getBilltype();
								billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
							}
							else
							{
								if(DateUtils.compareDates(mb.getEgBillregister().getBilldate(), maxBillDate))
								{
									maxBillDate = mb.getEgBillregister().getBilldate();
									lastBillType = mb.getEgBillregister().getBilltype();
									billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
									
								}
							}
						}
						/* only dates */
						else if((billType==null || "".equals(billType) || "-1".equals(billType)) && 
								(fromDate!=null && toDate!=null && isWithin(fromDate,toDate,mb.getEgBillregister().getBilldate())))
						{
							billIdList.add(mb.getEgBillregister().getId());
							if(maxBillDate == null)
							{
								maxBillDate = mb.getEgBillregister().getBilldate();
								lastBillType = mb.getEgBillregister().getBilltype();
								billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
							}
							else
							{
								if(DateUtils.compareDates(mb.getEgBillregister().getBilldate(), maxBillDate))
								{
									maxBillDate = mb.getEgBillregister().getBilldate();
									lastBillType = mb.getEgBillregister().getBilltype();
									billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
								}
							}
						}
						/* only bill type */
						else if ((billType!=null && !"".equals(billType) && !"-1".equals(billType)  && fromDate == null && toDate == null))
						{
							billIdList.add(mb.getEgBillregister().getId());
							if(maxBillDate == null)
							{
								maxBillDate = mb.getEgBillregister().getBilldate();
								lastBillType = mb.getEgBillregister().getBilltype();
								billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
							}
							else
							{
								if(DateUtils.compareDates(mb.getEgBillregister().getBilldate(), maxBillDate))
								{
									maxBillDate = mb.getEgBillregister().getBilldate();
									lastBillType = mb.getEgBillregister().getBilltype();
									billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
								}
							}
						}
						/* none of 3 selected */
						else if ((billType==null || "".equals(billType) || "-1".equals(billType))  && fromDate == null && toDate == null)
						{
							billIdList.add(mb.getEgBillregister().getId());
							if(maxBillDate == null)
							{
								maxBillDate = mb.getEgBillregister().getBilldate();
								lastBillType = mb.getEgBillregister().getBilltype();
								billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
							}
							else
							{
								if(DateUtils.compareDates(mb.getEgBillregister().getBilldate(), maxBillDate))
								{
									maxBillDate = mb.getEgBillregister().getBilldate();
									lastBillType = mb.getEgBillregister().getBilltype();
									billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
								}
							}
						}
					}
				}
			}
			repBean.setTypeOfBill(lastBillType);
			repBean.setBillNumAndDate(billNoDate);
			billIdList.addAll(getRetentionMoneyBillIds(wo.getId()));
			billIdList.addAll(getSecurityDepositBillIds(wo.getId()));
			
			DecimalFormat df = new DecimalFormat("###.##");
			if(!billIdList.isEmpty())
				paymentAmt = egovCommon.getTotalPaymentAmountForBills(billIdList, "contractor", wo.getContractor().getId());
			
			if(paymentAmt.equals(BigDecimal.ZERO))
				repBean.setBillAmount(null);
			else
				repBean.setBillAmount(new BigDecimal(df.format(paymentAmt)).setScale(2));
			reportList.add(repBean);  
		}
		egovPaginatedList.setList(reportList);
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> getRetentionMoneyBillIds(Long woId)
	{
		List<Long> rmBillIdList= new ArrayList<Long>();
		StringBuilder dynQuery = new StringBuilder(800);
		String queryBeginning =" ";
		
		queryBeginning = "select rmr.egBillregister.id from RetentionMoneyRefund rmr where rmr.egwStatus.code = 'APPROVED' and rmr.egBillregister.billstatus = 'APPROVED' and rmr.workOrder.id = " + woId;
		
		if(fromDate!=null && toDate!=null){
			dynQuery.append(" and trunc(rmr.egBillregister.billdate) >= '"+DateUtils.getFormattedDate(fromDate,"dd-MMM-yyyy")+"' ");
			dynQuery.append(" and trunc(rmr.egBillregister.billdate) <= '"+DateUtils.getFormattedDate(toDate,"dd-MMM-yyyy")+"' ");
		}	
		rmBillIdList = persistenceService.findAllBy(queryBeginning+dynQuery.toString());
		return rmBillIdList;
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> getSecurityDepositBillIds(Long woId)
	{
		List<Long> sdBillIdList= new ArrayList<Long>();
		StringBuilder dynQuery = new StringBuilder(800);
		String queryBeginning =" ";
		
		queryBeginning = "select rsd.egBillregister.id from ReturnSecurityDeposit rsd where rsd.egwStatus.code = 'APPROVED' and rsd.egBillregister.billstatus = 'APPROVED' and rsd.workOrder.id = " + woId;
		
		if(fromDate!=null && toDate!=null){
			
			dynQuery.append(" and trunc(rsd.egBillregister.billdate) >= '"+DateUtils.getFormattedDate(fromDate,"dd-MMM-yyyy")+"' ");
			dynQuery.append(" and trunc(rsd.egBillregister.billdate) <= '"+DateUtils.getFormattedDate(toDate,"dd-MMM-yyyy")+"' ");
		}	
		sdBillIdList = persistenceService.findAllBy(queryBeginning+dynQuery.toString());
		return sdBillIdList;
	}
	
	public Map<String,Object> getContractorForApprovedWorkOrder() {
		Map<String,Object> contractorsWithWOList = new LinkedHashMap<String, Object>();		
		if(workOrderService.getContractorsWithWO()!=null) {
			for(Contractor contractor :workOrderService.getContractorsWithWO()){
				contractorsWithWOList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
			}			
		}
		return contractorsWithWOList; 
	}
	
	protected List<WOReportBean> formatSearchResultForExport(List<WorkOrder> searchResult) {		
		reportList = new ArrayList<WOReportBean>();
		WOReportBean repBean;
		for(WorkOrder wo : searchResult)
		{
			repBean = new WOReportBean();
			for(WorkOrderEstimate woe : wo.getWorkOrderEstimates())
			{
				
				repBean.setEstNumAndDate(woe.getEstimate().getEstimateNumber());
				repBean.setNameOfWork(woe.getEstimate().getName());
				repBean.setJurisdiction(woe.getEstimate().getWard().getName());
				BudgetGroup bgtgrp = woe.getEstimate().getFinancialDetails().get(0).getBudgetGroup();
				if(bgtgrp!=null)
				{
					repBean.setBudgetHead(bgtgrp.getMaxCode().getGlcode()+" ~ "+bgtgrp.getName());
				}
				repBean.setProjectCode(woe.getEstimate().getProjectCode().getCode());
				repBean.setWONumAndDate(woe.getWorkOrder().getWorkOrderNumber());
				repBean.setContractorName(woe.getWorkOrder().getContractor().getName());
				
			}
			Date maxBillDate = null;
			List<Long> billIdList = new ArrayList<Long>();
			String lastBillType = "";
			String billNoDate = "";
			BigDecimal paymentAmt = new BigDecimal(0);
			for(MBHeader mbh : wo.getMbHeaders())
			{
				for(MBBill mb : mbh.getMbBills())
				{

					if(mb.getEgBillregister().getBillstatus().equals("APPROVED"))
					{
						/* all 3 selected */
						if((billType!=null && !"".equals(billType) && !"-1".equals(billType) && billType.equals(mb.getEgBillregister().getBilltype())) &&
								(fromDate!=null && toDate!=null && isWithin(fromDate,toDate,mb.getEgBillregister().getBilldate()))) 
						{
							billIdList.add(mb.getEgBillregister().getId());
							if(maxBillDate == null)
							{
								maxBillDate = mb.getEgBillregister().getBilldate();
								lastBillType = mb.getEgBillregister().getBilltype();
								billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
							}
							else
							{
								if(DateUtils.compareDates(mb.getEgBillregister().getBilldate(), maxBillDate))
								{
									maxBillDate = mb.getEgBillregister().getBilldate();
									lastBillType = mb.getEgBillregister().getBilltype();
									billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
									
								}
							}
						}
						/* only dates */
						else if((billType==null || "".equals(billType) || "-1".equals(billType)) && 
								(fromDate!=null && toDate!=null && isWithin(fromDate,toDate,mb.getEgBillregister().getBilldate())))
						{
							billIdList.add(mb.getEgBillregister().getId());
							if(maxBillDate == null)
							{
								maxBillDate = mb.getEgBillregister().getBilldate();
								lastBillType = mb.getEgBillregister().getBilltype();
								billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
							}
							else
							{
								if(DateUtils.compareDates(mb.getEgBillregister().getBilldate(), maxBillDate))
								{
									maxBillDate = mb.getEgBillregister().getBilldate();
									lastBillType = mb.getEgBillregister().getBilltype();
									billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
								}
							}
						}
						/* only bill type */
						else if ((billType!=null && !"".equals(billType) && !"-1".equals(billType)  && fromDate == null && toDate == null))
						{
							billIdList.add(mb.getEgBillregister().getId());
							if(maxBillDate == null)
							{
								maxBillDate = mb.getEgBillregister().getBilldate();
								lastBillType = mb.getEgBillregister().getBilltype();
								billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
							}
							else
							{
								if(DateUtils.compareDates(mb.getEgBillregister().getBilldate(), maxBillDate))
								{
									maxBillDate = mb.getEgBillregister().getBilldate();
									lastBillType = mb.getEgBillregister().getBilltype();
									billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
								}
							}
						}
						/* none of 3 selected */
						else if ((billType==null || "".equals(billType) || "-1".equals(billType))  && fromDate == null && toDate == null)
						{
							billIdList.add(mb.getEgBillregister().getId());
							if(maxBillDate == null)
							{
								maxBillDate = mb.getEgBillregister().getBilldate();
								lastBillType = mb.getEgBillregister().getBilltype();
								billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
							}
							else
							{
								if(DateUtils.compareDates(mb.getEgBillregister().getBilldate(), maxBillDate))
								{
									maxBillDate = mb.getEgBillregister().getBilldate();
									lastBillType = mb.getEgBillregister().getBilltype();
									billNoDate = mb.getEgBillregister().getBillnumber() + COMMA_SEPARATOR + DateUtils.getFormattedDate(maxBillDate,"dd/MM/yyyy");
								}
							}
						}
					}
				}
			}
			repBean.setTypeOfBill(lastBillType);
			repBean.setBillNumAndDate(billNoDate);
			billIdList.addAll(getRetentionMoneyBillIds(wo.getId()));
			billIdList.addAll(getSecurityDepositBillIds(wo.getId()));
			
			DecimalFormat df = new DecimalFormat("###.##");
			paymentAmt = egovCommon.getTotalPaymentAmountForBills(billIdList, "contractor", wo.getContractor().getId());
			totalAmount = totalAmount.add(paymentAmt);
			if(paymentAmt.equals(BigDecimal.ZERO))
				repBean.setBillAmount(null);
			else
				repBean.setBillAmount(new BigDecimal(df.format(paymentAmt)).setScale(2));
			reportList.add(repBean);  
		}
		return reportList;
	}

	@SuppressWarnings("unchecked")
	private List<WOReportBean> getReportData() { 
		String reportQuery ="select distinct mb.mbHeader.workOrder "+  getSearchQuery();
		List searchResult = persistenceService.findAllBy(reportQuery, paramList.toArray());
		formatSearchResultForExport(searchResult);
		return reportList;
	}

	public String exportToPdf() throws JRException,Exception{
		Map<String,Object> reportParams = new HashMap<String,Object>();
		List<WOReportBean> reportData = getReportData();
		reportParams.put("reportSubTitle", reportSubTitle);
		reportParams.put("totalAmount", totalAmount);
		ReportRequest reportRequest = new ReportRequest("WO_PaymentReport",reportData, reportParams);
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
		return EXPORTPDF;
	}
	
	public String exportToExcel() throws JRException,Exception{
		Map<String,Object> reportParams = new HashMap<String,Object>();
		List<WOReportBean> reportData = getReportData();
		reportParams.put("reportSubTitle", reportSubTitle);
		reportParams.put("totalAmount", totalAmount);
		ReportRequest reportRequest = new ReportRequest("WO_PaymentReport",reportData, reportParams);
		reportRequest.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportRequest); 
		if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
			excelInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
		return EXPORTEXCEL; 
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}


	public WorkOrderService getWorkOrderService() {
		return workOrderService;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public List<WOReportBean> getReportList() {
		return reportList;
	}

	public void setReportList(List<WOReportBean> reportList) {
		this.reportList = reportList;
	}
    
	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public String getJurisdictionName() {
		return jurisdictionName;
	}

	public void setJurisdictionName(String jurisdictionName) {
		this.jurisdictionName = jurisdictionName;
	}

	public Integer getJurisdictionId() {
		return jurisdictionId;
	}

	public void setJurisdictionId(Integer jurisdictionId) {
		this.jurisdictionId = jurisdictionId;
	}

	public String getBudgetHeadName() {
		return StringUtils.escapeSpecialChars(budgetHeadName);
	}

	public void setBudgetHeadName(String budgetHeadName) {
		this.budgetHeadName = StringEscapeUtils.unescapeHtml(budgetHeadName);
	}

	public Long getBudgetHeadId() {
		return budgetHeadId;
	}

	public void setBudgetHeadId(Long budgetHeadId) {
		this.budgetHeadId = budgetHeadId;
	}

	public String getReportSubTitle() {
		return reportSubTitle;
	}
	
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public ContractorService getContractorService() {
		return contractorService;
	}

	public void setContractorService(ContractorService contractorService) {
		this.contractorService = contractorService;
	}

	public void setReportSubTitle(String reportSubTitle) {
		this.reportSubTitle = reportSubTitle;
	}

	public InputStream getPdfInputStream() {
		return pdfInputStream;
	}

	public void setPdfInputStream(InputStream pdfInputStream) {
		this.pdfInputStream = pdfInputStream;
	}

	public InputStream getExcelInputStream() {
		return excelInputStream;
	}

	public void setExcelInputStream(InputStream excelInputStream) {
		this.excelInputStream = excelInputStream;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public EgBillregister getEgBill() {
		return egBill;
	}

	public void setEgBill(EgBillregister egBill) {
		this.egBill = egBill;
	}

	public EgovCommon getEgovCommon() {
		return egovCommon;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

}
