package org.egov.eb.web.action.reports;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.eb.domain.master.bean.RtgsPaymentReportBean;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.FlushMode;


@ParentPackage("egov")   
public class RtgsPaymentReportAction extends SearchFormAction{

	private static final Logger	LOGGER = Logger.getLogger(RtgsPaymentReportAction.class);

	private EgovPaginatedList paginatedList;
	private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private String year ;
	private String month;
	private String fromDate;
	private String toDate;
	private String region;
	private String heading;


	private FinancialYearHibernateDAO financialYearDAO;	
	
	private List<RtgsPaymentReportBean> rtgsPaymentDisplayList = new ArrayList<RtgsPaymentReportBean>();
	private Map<Integer, String> monthMap = new LinkedHashMap<Integer, String> ();
	private Map<Integer, String> shortMonthMap = new LinkedHashMap<Integer, String> ();

	public RtgsPaymentReportAction() {
		super();
	}
	
	@Override
	public Object getModel() {
		return null;
	}
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("RtgsPaymentReportAction | prepare | start");
		String query = getQueryString().toString();
		StringBuffer srchQry = new StringBuffer(query);
		StringBuffer countQry = new StringBuffer("select count(*) from ("+query+")");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("RtgsPaymentReportAction | prepare | srchQry >> "+ srchQry);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("RtgsPaymentReportAction | prepare | countQry >> "+ countQry);
		
		return new SearchQuerySQL(srchQry.toString(),countQry.toString(),null);
	}
	public void prepareNewForm() {
		super.prepare();
	
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside  Prepare ........");
	
		addDropdownData("regionsList", VoucherHelper.TNEB_REGIONS);
		addDropdownData("financialYearsList", financialYearDAO.getAllActiveFinancialYearList());
		monthMap = DateUtils.getAllMonthsWithFullNames();
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug(parameters);
		
	}

	@SkipValidation
@Action(value="/reports/rtgsPaymentReport-newForm")
	public String newForm() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside NewForm method..");
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	@SkipValidation
	public String Search(){
		
		/*HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);*/
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBBillReportAction | Search | start");
		super.search();
		prepareResults();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("EBBillReportAction | list | End");
		prepareNewForm();	
			
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	private StringBuffer getQueryString() {
		
		StringBuffer queryString = new StringBuffer();
		shortMonthMap = DateUtils.getAllMonths();
		String monthYearQry="";
		String fromDateTodateQry="";
		heading = "RTGS report for TNEB bill payment";
		
		if(null!=parameters.get("month")[0] && !parameters.get("month")[0].equalsIgnoreCase("")){
			heading = heading +" for Bill Month - "+ shortMonthMap.get(Integer.parseInt(parameters.get("month")[0]));
			monthYearQry=" and eb.month = "+Integer.parseInt(parameters.get("month")[0])+"";
		}
		if(null!=parameters.get("year")[0] && !parameters.get("year")[0].equalsIgnoreCase("")){
			
			monthYearQry=monthYearQry + " and eb.financialyearid = "+	Long.parseLong(parameters.get("year")[0])+" ";
			
			CFinancialYear financialYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id = ?",
					Long.parseLong(parameters.get("year")[0]));
			
			heading = heading +" and Year - "+ financialYear.getFinYearRange();
		}
		if(null!=parameters.get("fromDate")[0] && !parameters.get("fromDate")[0].equalsIgnoreCase("")){
			fromDateTodateQry=" AND ih.transactionDate >= '"+parameters.get("fromDate")[0]+"'";
			heading = heading +" and FromDate - "+ parameters.get("fromDate")[0];
		}
		if(null!=parameters.get("toDate")[0] && !parameters.get("toDate")[0].equalsIgnoreCase("")){
			fromDateTodateQry= fromDateTodateQry + " AND ih.transactionDate <= '"+parameters.get("toDate")[0]+"'";
			heading = heading +" ToDate - "+ parameters.get("toDate")[0];
		}
		if(null!=parameters.get("region")[0] && !parameters.get("region")[0].equals("")){
			heading = heading +" and Region - "+ parameters.get("region")[0];
			monthYearQry= monthYearQry + " AND eb.region = '"+parameters.get("region")[0]+"'";
		}
				           
		queryString = queryString
				.append(" SELECT ih.TRANSACTIONNUMBER AS rtgsNumber,ih.TRANSACTIONDATE AS rtgsDate,ph.PAYMENTAMOUNT AS paymentAmount, eb.REGION AS region,eb.month AS MONTH,f.FINANCIALYEAR AS finYearRange," +
						" SUM(DECODE(eb.receiptno,NULL,eb.billamount,0)) AS UnPaidAmount,SUM(DECODE(eb.receiptno, NULL,0, eb.billamount)) AS PaidAmount,SUM(DECODE(eb.receiptno,NULL,1,0)) AS unpaidcount  " +
						" FROM EGF_EBDETAILS eb,FINANCIALYEAR f,EG_BILLREGISTER bill,MISCBILLDETAIL misc,EGF_InstrumentVoucher iv,EGF_INSTRUMENTHEADER ih,PAYMENTHEADER ph WHERE eb.FINANCIALYEARID = f.ID " +
						" AND eb.status not in (select id from egw_status where moduletype = 'TNEB Bill' and code = 'CANCELLED') AND ih.id_status not in (select id from egw_status where moduletype = 'Instrument' and description in ('Surrendered','Cancelled','Surrender_For_Reassign'))"  +
						" AND iv.instrumentheaderId = ih.ID AND ph.VOUCHERHEADERID = misc.PAYVHID AND eb.BILLID = bill.ID AND bill.BILLNUMBER = misc.BILLNUMBER AND misc.PAYVHID = iv.voucherheaderId "+monthYearQry+" "+fromDateTodateQry+" " +
						" GROUP BY ih.TRANSACTIONNUMBER ,ih.TRANSACTIONDATE ,ph.PAYMENTAMOUNT , eb.REGION ,eb.MONTH ,f.FINANCIALYEAR ORDER BY ih.TRANSACTIONNUMBER,ih.TRANSACTIONDATE,eb.month");
 
	return queryString;
	 
	}

	private void prepareResults() {
		
		LOGGER.debug("Entering into prepareResults");
		paginatedList = (EgovPaginatedList) searchResult;
		List<Object[]> list = paginatedList.getList();
		StringBuffer queryString = new StringBuffer();
		List<RtgsPaymentReportBean> rtgsDisplayList = new ArrayList<RtgsPaymentReportBean>();
		shortMonthMap = DateUtils.getAllMonths();
		for(Object[] object : list) {
			RtgsPaymentReportBean reportBean = new RtgsPaymentReportBean();
			reportBean.setRtgsNumber(getStringValue(object[0]));
			reportBean.setRtgsDate(getDateValue(object[1]));
			reportBean.setPaymentAmount(getBigDecimalValue(object[2]));
			reportBean.setRegion(getStringValue(object[3]));
			reportBean.setMonth(shortMonthMap.get(getIntegerValue(object[4]))+"-"+getStringValue(object[5]).substring(0,4));
			reportBean.setUnPaidAmount(getBigDecimalValue(object[6]));
			reportBean.setIOBPaidAmount(getBigDecimalValue(object[7]));
			reportBean.setNumOfBillsUnpaid(getIntegerValue(object[8]));
			rtgsPaymentDisplayList.add(reportBean);
		}
		paginatedList.setList(rtgsPaymentDisplayList);
		LOGGER.debug("Exiting from prepareResults");
	}

	private String getStringValue(Object object) {
		return object != null?object.toString():"";
	}
	private String getDateValue(Object object) {
		
		return object != null?formatter.format((Date) object):"";
	}
	private BigDecimal getBigDecimalValue(Object object) {
		return object!= null? new BigDecimal(object.toString()).setScale(2):BigDecimal.ZERO.setScale(2);
	}
	private Integer getIntegerValue(Object object) {
		return object!= null? new Integer(object.toString()):0;
	}
	public FinancialYearHibernateDAO getFinancialYearDAO() {
		return financialYearDAO;
	}

	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public Map<Integer, String> getMonthMap() {
		return monthMap;
	}

	public void setMonthMap(Map<Integer, String> monthMap) {
		this.monthMap = monthMap;
	}
	public EgovPaginatedList getPaginatedList() {
		return paginatedList;
	}

	public void setPaginatedList(EgovPaginatedList paginatedList) {
		this.paginatedList = paginatedList;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
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

	public List<RtgsPaymentReportBean> getRtgsPaymentDisplayList() {
		return rtgsPaymentDisplayList;
	}

	public void setRtgsPaymentDisplayList(
			List<RtgsPaymentReportBean> rtgsPaymentDisplayList) {
		this.rtgsPaymentDisplayList = rtgsPaymentDisplayList;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}



}
