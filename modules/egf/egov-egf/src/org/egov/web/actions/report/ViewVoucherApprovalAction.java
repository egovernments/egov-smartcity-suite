package org.egov.web.actions.report;
/**
 * 
 */

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.Query;
import org.hibernate.Session;
/**
  *
 */
@ParentPackage("egov")
public class ViewVoucherApprovalAction extends SearchFormAction{

	private static final long serialVersionUID = 1L;
	private static final Logger	LOGGER	= Logger.getLogger(ViewVoucherApprovalAction.class);
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
	private final List<String> headerFields = new ArrayList<String>();
	private final List<String> mandatoryFields = new ArrayList<String>();
	private CVoucherHeader voucherHeader = new CVoucherHeader();
	private static Map<String,List<String>> netAccountCode =  new HashMap<String, List<String>>(); // have list of all net payable accounts codes based on the expenditure type.
	private Date fromDate;
	private Date toDate;
	private String vortype;
	private String vorname;
	private List<String> nameMap;
	private List<Map<String, String>> voucherNameList;
	private List<String> voucherTypeList;
	private List<ViewVoucherApprovalBean> reportList;
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String query = null;
		
		if(vortype.equalsIgnoreCase("Journal Voucher")){
			 query = getQueryForBillVoucher();
		}else if(vortype.equalsIgnoreCase("Payment")){
			 query = paymentQuery();
		}
		LOGGER.debug("BillRegisterReportAction | prepare | query >> "+ query);
		return new SearchQuerySQL(query,"select count(*) from ( "+query+" )",null);
	}
	
	@Override
	public Object getModel() {
		
		return null;
	}
	@SkipValidation
	public String newform(){
		loadDropdownData();
		return "new";
	}
	
	
	@ValidationErrorPage(value="new")
	public String list() throws Exception{
		LOGGER.debug("ViewVoucherApproval| list | start");
		setPageSize(50);
		loadDropdownData();
		validateData();
		search();
		formatResult();
		LOGGER.debug("ViewVoucherApproval | list | End");
		return "new";
	}
	
	
	 private void validateData()  throws Exception
	 {
		 if(null == fromDate){
			 throw new ValidationException(Arrays.asList(new ValidationError("date",
			 "from date can not empty")));
		 }
		 else if(null == toDate){
			 throw new ValidationException(Arrays.asList(new ValidationError("date",
			 "to date can not empty")));      
		 }
		 else if(fromDate.after(toDate)){
			 throw new ValidationException(Arrays.asList(new ValidationError("date",
			 "from date should fall before to date")));        
		 }
		 else{
    	 		CFinancialYear finYear1 = getFinancialYear(DDMMYYYYFORMATS.format(fromDate));
    	 		CFinancialYear finYear2 = getFinancialYear(DDMMYYYYFORMATS.format(toDate));
             
    	 		if(null == finYear1){
    	 			throw new ValidationException(Arrays.asList(new ValidationError("fromdate",
    				 "from date not defined")));            
    	 		}else if (null == finYear2){
    	 			throw new ValidationException(Arrays.asList(new ValidationError("fromdate",
   				 "to date not defined")));    
    	 		}else if(! finYear1.getId().equals(finYear2.getId())){
    	 			throw new ValidationException(Arrays.asList(new ValidationError("fromdate",
      				 "Date should be in same financial year")));    
             }
		 }   
		 if(null == vortype || StringUtils.isEmpty(vortype) ){
			 throw new ValidationException(Arrays.asList(new ValidationError("vortype",
			 "Please select voucher type")));  
		 }
	
     }
	 
	 
	public boolean shouldShowHeaderField(String field){
		return  headerFields.contains(field);
	}
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}
	
	protected String paymentQuery(){
		
		StringBuffer query = new StringBuffer(1000);
		StringBuffer whereQuery = new StringBuffer(200); 
		String fDate = DDMMYYYYFORMATS.format(fromDate);
		fDate = fDate+" 12:00:00 AM ";
		String tDate = DDMMYYYYFORMATS.format(toDate);
		tDate = tDate+" 11:59:59 PM ";
		
		whereQuery.append(" and wf.created_date >= to_date('"+fDate+"','dd/MM/yyyy HH:MI:SS PM')");
		whereQuery.append(" and wf.created_date <= to_date('"+tDate+"','dd/MM/yyyy HH:MI:SS PM')");
		whereQuery.append(" and vh.type='"+vortype+"'"); 
		if(null != vorname && !StringUtils.isEmpty(vorname)){
			whereQuery.append(" and vh.name='"+vorname+"'"); 
		}
		query.append(" select vh.vouchernumber,vh.type,misc.paidamount,wf.created_date,d.dept_name,vh.status").
		append(" from voucherheader vh, vouchermis mis, eg_wf_states wf , paymentheader ph,eg_department d,miscbilldetail misc").
		append(" where ph.state_id=wf.id and vh.id= ph.voucherheaderid and vh.id=mis.voucherheaderid and  d.id_dept(+)=mis.departmentid").
		append(" and misc.payvhid=vh.id and  wf.created_by="+EGOVThreadLocals.getUserId()).append(whereQuery);
		return query.toString();
	}
	 
	@SuppressWarnings("unchecked")
	protected String getQueryForBillVoucher(){
			String fDate = DDMMYYYYFORMATS.format(fromDate);
			fDate = fDate+" 12:00:00 AM ";
			String tDate = DDMMYYYYFORMATS.format(toDate);
			tDate = tDate+" 11:59:59 PM ";
			StringBuffer query = new StringBuffer(1000);
			StringBuffer whereQuery = new StringBuffer(200); 
			whereQuery.append(" and wf.created_date >= to_date('"+fDate+"','dd/MM/yyyy HH:MI:SS PM')");
			whereQuery.append(" and wf.created_date <= to_date('"+tDate+"','dd/MM/yyyy HH:MI:SS PM')");
			whereQuery.append(" and vh.type='"+vortype+"'"); 
			if(null ==vorname || StringUtils.isEmpty(vorname)){
					query.append(getQueryByVoucherName("Expense Journal", whereQuery.toString()));
					query.append(" UNION ");
					query.append(getQueryByVoucherName("Supplier Journal", whereQuery.toString()));
					query.append(" UNION ");
					query.append(getQueryByVoucherName("Salary Journal", whereQuery.toString()));
					query.append(" UNION ");
					query.append(getQueryByVoucherName("Contractor Journal", whereQuery.toString()));
					
			}else{
				query.append(getQueryByVoucherName(vorname, whereQuery.toString()));
			}
		  return query.toString(); 

	}
		  
protected String getQueryByVoucherName(String journalName,String whereQuery){
		
		List<String> listOfNetPayGlIds = netAccountCode.get(journalName);
		StringBuffer netPayCodes = new StringBuffer(30);
		for (String netCode : listOfNetPayGlIds) {
			if(!StringUtils.isEmpty(netPayCodes.toString()))
				netPayCodes.append(",").append(netCode);
			else
				netPayCodes.append(netCode);
			
		}
		StringBuffer query = new StringBuffer(500);
		query.append(" select vh.vouchernumber,vh.type,sum(gl.creditamount),wf.created_date,d.dept_name,vh.status").
		append(" from voucherheader vh, vouchermis mis, eg_wf_states wf , generalledger gl,eg_department d").
		append(" where vh.state_id=wf.id and vh.id= gl.voucherheaderid and vh.id=mis.voucherheaderid and  d.id_dept(+)=mis.departmentid").
		append("  and  wf.created_by="+EGOVThreadLocals.getUserId()).append(" and vh.name='").append(journalName).append("'"). 
		append(" and gl.glcodeid in("+netPayCodes+")").append(whereQuery).
		append(" group by vh.vouchernumber,vh.type,wf.created_date,d.dept_name,vh.status");
		
		
		return query.toString();
}

private void formatResult(){
	LOGGER.debug("ViewVoucherApprovalAction | formatResult | start");
	reportList = new ArrayList<ViewVoucherApprovalBean>();
	EgovPaginatedList egovPaginatedList = (EgovPaginatedList)searchResult;
	List<Object[]> list = egovPaginatedList.getList();
	LOGGER.debug("ViewVoucherApprovalAction | formatResult | list size : "+list.size() );
	for(Object[] object : list) {
		
		ViewVoucherApprovalBean bean= new ViewVoucherApprovalBean();
		bean.setVoucherNumber(object[0].toString());
		bean.setVoucherType(object[1].toString());
		bean.setNetAmount(new BigDecimal(object[2].toString()).setScale(2));
		bean.setApprovedDate(DDMMYYYYFORMATS.format((Date)object[3]));
		bean.setDepartment(null!=object[4]?object[4].toString():"");
		if(object[5].toString().equalsIgnoreCase(FinancialConstants.CREATEDVOUCHERSTATUS.toString())){
			bean.setStatus("Approved");
		}
		else if(object[5].toString().equalsIgnoreCase(FinancialConstants.PREAPPROVEDVOUCHERSTATUS.toString())){
			bean.setStatus("Preapproved");
		}
		else if(object[5].toString().equalsIgnoreCase(FinancialConstants.CANCELLEDVOUCHERSTATUS.toString())){
			bean.setStatus("Cancelled");
		}
		else if(object[5].toString().equalsIgnoreCase(FinancialConstants.REVERSEDVOUCHERSTATUS.toString())){
			bean.setStatus("Reversed");
		}
		else if(object[5].toString().equalsIgnoreCase(FinancialConstants.REVERSALVOUCHERSTATUS.toString())){
			bean.setStatus("Reversal");
		}
		
		
		reportList.add(bean); 
		
	}

	egovPaginatedList.setList(reportList); 
}
	protected void loadDropdownData(){
	    List<String> voucherTypeList = new ArrayList<String>();
	    voucherTypeList.add("Journal Voucher");
	    voucherTypeList.add("Payment");
	    addDropdownData("voucherTypeList", voucherTypeList);
	    if(null != vortype && !StringUtils.isEmpty(vortype)){
	    	addDropdownData("voucherNameList",getListOfVoucherName(vortype));
	    }else{
	    	addDropdownData("voucherNameList",Collections.EMPTY_LIST);
	    }
		
		
	}
		
   @SuppressWarnings("unchecked")
   public String  ajaxLoadVoucherNames(){
	   
	   	voucherNameList = new ArrayList<Map<String,String>>();
		Map <String,String> voucherNameMap;
		List<String> nameList = getListOfVoucherName(vortype);
	   	for (String voucherName : nameList) {
			LOGGER.info("..................................................................."+(String)voucherName);
			voucherNameMap=new LinkedHashMap<String,String>();
			voucherNameMap.put("key",(String)voucherName);
			voucherNameMap.put("val",(String)voucherName);
			voucherNameList.add(voucherNameMap);
		}
	   return "voucherName";
   }
   
   @SuppressWarnings("unchecked")
private 	List<String> getListOfVoucherName(String voucherType){
	   
		List<String> nameList = new ArrayList<String>();
	   
	   	if (null != vortype && vortype.equalsIgnoreCase("Journal Voucher")){ 
	   		nameList = getPersistenceService().findAllBy(
					" select  distinct vh.name from CVoucherHeader vh, org.egov.infstr.models.State wf where vh.state.id= wf.id  and wf.createdBy.id=? and vh.type='"+vortype+"'",Integer.valueOf(EGOVThreadLocals.getUserId()));
	
		} else if (null != vortype && vortype.equalsIgnoreCase("Payment")){ 
	   		nameList = getPersistenceService().findAllBy(
					" select  distinct vh.name from CVoucherHeader vh, Paymentheader ph, org.egov.infstr.models.State wf where ph.state.id= wf.id  " +
					" and vh.id=ph.voucherheader.id  and wf.createdBy.id=? and vh.type='"+vortype+"'",Integer.valueOf(EGOVThreadLocals.getUserId()));
	
	} 
	   	return nameList;
   }
   
   	static {
		String query = "from AppConfigValues where key.module=:module and key.keyName=:keyName";
		// setting net pay account codes for expense type.
		Session session = HibernateUtil.getCurrentSession();
		Query query1  = session.createQuery(query);
		query1.setString("module", "EGF");
		query1.setString("keyName", "contingencyBillPurposeIds");
		List<String> cBillNetPayCodeList = new ArrayList<String>();
		List<AppConfigValues> cBillNetPurpose =(List<AppConfigValues>)query1.list();
		for (AppConfigValues appConfigValues : cBillNetPurpose) {
				List<CChartOfAccounts> coaList = (List<CChartOfAccounts>) session.createQuery("from CChartOfAccounts where purposeId="+appConfigValues.getValue()).list();
				for (CChartOfAccounts chartOfAccounts : coaList) {
					cBillNetPayCodeList.add(chartOfAccounts.getId().toString());
				}
		}
		netAccountCode.put("Expense Journal", cBillNetPayCodeList);
		
		// setting net pay account codes for purchase type.
		List<String> pBillNetPayCodeList = new ArrayList<String>();
		Query query2  = session.createQuery(query);
		query2.setString("module", "EGF");
		query2.setString("keyName", "purchaseBillPurposeIds");
		List<AppConfigValues> purchBillNetPurpose =(List<AppConfigValues>)query2.list();
			for (AppConfigValues appConfigValues : purchBillNetPurpose) {
				List<CChartOfAccounts> coaList = (List<CChartOfAccounts>) session.createQuery("from CChartOfAccounts where purposeId="+appConfigValues.getValue()).list();
				for (CChartOfAccounts chartOfAccounts : coaList) {
					pBillNetPayCodeList.add(chartOfAccounts.getId().toString());
				}
		}
		netAccountCode.put("Supplier Journal", pBillNetPayCodeList);
		
		// setting net pay account codes for salary type.
		List<String> sBillNetPayCodeList = new ArrayList<String>();
		Query query3  = session.createQuery(query);
		query3.setString("module", "EGF");
		query3.setString("keyName", "salaryBillPurposeIds");
		List<AppConfigValues> sBillNetPurpose =(List<AppConfigValues>)query3.list();
		for (AppConfigValues appConfigValues : sBillNetPurpose) {
			
				List<CChartOfAccounts> coaList = (List<CChartOfAccounts>) session.createQuery("from CChartOfAccounts where purposeId="+appConfigValues.getValue()).list();
				for (CChartOfAccounts chartOfAccounts : coaList) {
					sBillNetPayCodeList.add(chartOfAccounts.getId().toString());
				}
			
		}
		netAccountCode.put("Salary Journal", sBillNetPayCodeList);
		
		// setting net pay account codes for works type.
		
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
		netAccountCode.put("Contractor Journal", wBillNetPayCodeList);
	}


public List<String> getNameMap() {
	return nameMap;
}

public void setNameMap(List<String> nameMap) {
	this.nameMap = nameMap;
}

public List<Map<String, String>> getVoucherNameList() {
	return voucherNameList;
}

public void setVoucherNameList(List<Map<String, String>> voucherNameList) {
	this.voucherNameList = voucherNameList;
}

public List<String> getVoucherTypeList() {
	return voucherTypeList;
}

public void setVoucherTypeList(List<String> voucherTypeList) {
	this.voucherTypeList = voucherTypeList;
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

	public CVoucherHeader getVoucherHeader() {
		return voucherHeader;
	}

	public void setVoucherHeader(CVoucherHeader voucherHeader) {
		this.voucherHeader = voucherHeader;
	}

	
	public String getVortype() {
		return vortype;
	}

	public void setVortype(String vortype) {
		this.vortype = vortype;
	}

	 private CFinancialYear getFinancialYear(String date)throws Exception {
		 try{
			Session session  = HibernateUtil.getCurrentSession();
			StringBuffer query1 = new StringBuffer(100);
			query1.append("from CFinancialYear where startingDate <= to_date('").append(date).
			append("','dd/MM/yyyy') and endingDate >= to_date('").append(date).append("','dd/MM/yyyy')");
			List<CFinancialYear> list1 =(List<CFinancialYear> ) session.createQuery(query1.toString()).list();
			return list1 != null?list1.get(0):null;
	  } 
		 catch (Exception e) {
		 LOGGER.debug( "Failed while processing bill number :"+ e.toString());
		 throw e; }
		}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public List<ViewVoucherApprovalBean> getReportList() {
		return reportList;
	}

	public void setReportList(List<ViewVoucherApprovalBean> reportList) {
		this.reportList = reportList;
	}
		
}




