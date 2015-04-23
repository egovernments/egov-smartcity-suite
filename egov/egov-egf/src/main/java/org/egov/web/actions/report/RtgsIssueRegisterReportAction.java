package org.egov.web.actions.report;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Results(value={
		@Result(name="PDF",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=RtgsIssueRegisterReport.pdf"}),
		@Result(name="XLS",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=RtgsIssueRegisterReport.xls"}),
		@Result(name="HTML",type="stream",location="inputStream", params={"inputName","inputStream","contentType","text/html"})
	})
@ParentPackage("egov")

public class RtgsIssueRegisterReportAction  extends ReportAction {

	private static final long serialVersionUID = 1L;
	private static final Logger	LOGGER	= Logger.getLogger(RtgsIssueRegisterReportAction.class);
	private Date fromDate = new Date() ;
	private Date toDate = new Date() ;
	private Bankaccount bankaccount;
	BankAdviceReportInfo bankAdviceInfo;
	private InputStream inputStream;
	private ReportHelper reportHelper;
	String jasperpath= "/org/egov/web/actions/report/rtgsIssueRegisterReportAction.jasper";
	private StringBuffer header=new StringBuffer(); 
	List<BankAdviceReportInfo> rtgsDisplayList = new ArrayList<BankAdviceReportInfo>();
	List<Object> rtgsReportList = new ArrayList<Object>();
	Map<String,Object> paramMap = new HashMap<String,Object>();
	Boolean searchResult = Boolean.FALSE;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void prepare(){
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		super.prepare();
			
				addDropdownData("bankList", persistenceService.findAllBy("from Bank where isactive=1 order by upper(name)"));
				addDropdownData("bankBranchList",  Collections.EMPTY_LIST); 
				addDropdownData("bankAccountList", Collections.EMPTY_LIST);
				addDropdownData("accNumList", Collections.EMPTY_LIST);
				addDropdownData("chequeNumberList",  Collections.EMPTY_LIST); 
				finYearDate();
				mandatoryFields.clear();
				
               
	}
	
	@ValidationErrorPage("search")
@Action(value="/report/rtgsIssueRegisterReport-newForm")
	public String newForm() {
		if(LOGGER.isInfoEnabled())  {
			LOGGER.info(fromDate);   
			LOGGER.info(toDate);          
		}
	      return "search";
	}
	private String getUlbName(){
		SQLQuery query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
		List<String> result = query.list();
		if(result!=null)
			return result.get(0);
		return "";
	}
	public String exportPdf() throws JRException, IOException{
		search();
		if(rtgsDisplayList.size()>0){
		inputStream = reportHelper.exportPdf(inputStream, jasperpath, getParamMap(), rtgsReportList);
	    return "PDF";
		}
		 prepare();
		return "search";
	}
	
	public String exportHtml() {
		search();
		if(rtgsDisplayList.size()>0){
			inputStream = reportHelper.exportHtml(inputStream, jasperpath, getParamMap(), rtgsReportList,JRHtmlExporterParameter.SIZE_UNIT_POINT);
			return "HTML";
		}
		 prepare();
		 return "search";
	}
	
	public String exportXls() throws JRException, IOException{
		search();
		if(rtgsDisplayList.size()>0){
		inputStream = reportHelper.exportXls(inputStream, jasperpath, getParamMap(), rtgsReportList);
		return "XLS";
		}
		 prepare();
		return "search";
	    
	}
protected Map<String, Object> getParamMap() {
	
	String fundAndBankHeading = "";
	String dateRange="";
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	Date date = new Date();
	String newFromDate = "";
	String newToDate = "";
	String reportRundate ="";
	fundAndBankHeading = "RTGS Register for "+(persistenceService.find("select name from Fund where id = ?",Integer.parseInt(parameters.get("fundId")[0]))).toString().split("-")[1];
	
	if(null!=parameters.get("rtgsAssignedFromDate")[0] && !parameters.get("rtgsAssignedFromDate")[0].equalsIgnoreCase("")){
				dateRange = "from "+parameters.get("rtgsAssignedFromDate")[0];
				
	}else {
				 newFromDate=dateFormat.format(fromDate);
				 dateRange = "from "+newFromDate;
	}
	if(null!=parameters.get("rtgsAssignedToDate")[0] && !parameters.get("rtgsAssignedToDate")[0].equalsIgnoreCase("")){
				dateRange = dateRange + " to "+parameters.get("rtgsAssignedToDate")[0];
		     
	}else{
				newToDate=dateFormat.format(toDate);
				dateRange = dateRange + " to "+newToDate;
	}
			reportRundate = dateFormat.format(date);
			paramMap.put("fundAndBankHeading", fundAndBankHeading);
			paramMap.put("dateRange",dateRange);
			paramMap.put("reportRundate",reportRundate);
			paramMap.put("ulbName", getUlbName());
			paramMap.put("rtgsDetailsList",rtgsDisplayList);
			paramMap.put("rtgsReportList",rtgsReportList);
		return paramMap;
	}     

	public void finYearDate(){
		if(LOGGER.isDebugEnabled())   LOGGER.debug(" Getting Starting date of financial year ");
		FinancialYearDAO financialYearDAO= CommonsDaoFactory.getDAOFactory().getFinancialYearDAO();
		CFinancialYear date=financialYearDAO.getFinancialYearByDate(new Date());
		HibernateUtil.getCurrentSession().setReadOnly(date, true);
		fromDate=date.getStartingDate();
	}
	
	@SuppressWarnings("unchecked")
	@ValidationErrorPage(NEW)
	public String search() {
		searchResult = Boolean.TRUE;
		if(LOGGER.isDebugEnabled())   LOGGER.debug(" Seraching RTGS result for given criteria ");
		
		Query query = HibernateUtil.getCurrentSession().createSQLQuery(getQueryString().toString())
				.addScalar("ihId") 
				.addScalar("rtgsNumber")
				.addScalar("rtgsDate")
				.addScalar("vhId")
				.addScalar("paymentNumber")     
				.addScalar("paymentDate")             
				.addScalar("paymentAmount")
				.addScalar("department")
				.addScalar("status")
				.addScalar("bank")
				.addScalar("bankBranch")
				.addScalar("dtId")
				.addScalar("dkId")
		        .addScalar("accountNumber");
		if(null==parameters.get("rtgsAssignedFromDate")[0] || parameters.get("rtgsAssignedFromDate")[0].equalsIgnoreCase("")){
			
			query.setDate("finStartDate", new java.sql.Date(fromDate.getTime()));
		}
		if(LOGGER.isInfoEnabled()) LOGGER.info("Search Query ------------>"+query);
		
		query.setResultTransformer(Transformers.aliasToBean(BankAdviceReportInfo.class));
			rtgsDisplayList = query.list();
			populateSubLedgerDetails();
			rtgsReportList.addAll(rtgsDisplayList);
        return "search";
	}

	private StringBuffer getQueryString() {
		StringBuffer queryString = new StringBuffer();
		String deptQry="";
		String fundQry="";
		String phQry="";
		StringBuffer bankQry= new StringBuffer("");
		
		StringBuffer instrumentHeaderQry= new StringBuffer("");
		
		if(null!=parameters.get("departmentid")[0] && !parameters.get("departmentid")[0].equalsIgnoreCase("-1")){
			deptQry=" AND vmis.departmentid     ="+parameters.get("departmentid")[0];
		}
		if(null!=parameters.get("rtgsAssignedFromDate")[0] && !parameters.get("rtgsAssignedFromDate")[0].equalsIgnoreCase("")){
			instrumentHeaderQry=instrumentHeaderQry.append(" and   ih.transactiondate >='"+parameters.get("rtgsAssignedFromDate")[0]+"'");
		}else{
			instrumentHeaderQry=instrumentHeaderQry.append(" and   ih.transactiondate >=:finStartDate");
		}
		if(null!=parameters.get("rtgsAssignedToDate")[0] && !parameters.get("rtgsAssignedToDate")[0].equalsIgnoreCase("")){
			instrumentHeaderQry=instrumentHeaderQry.append(" and   ih.transactiondate  <='"+parameters.get("rtgsAssignedToDate")[0]+"'");
		}
		if(null!=parameters.get("bank")[0] && !parameters.get("bank")[0].equals("-1") && !parameters.get("bank")[0].equalsIgnoreCase("")){
			bankQry=bankQry.append(" AND b.id = "+parameters.get("bank")[0]); 
		}
		if(null!=parameters.get("bankbranch.id")[0] && !parameters.get("bankbranch.id")[0].equals("-1") && !parameters.get("bankbranch.id")[0].equalsIgnoreCase("")){
			bankQry = bankQry.append(" AND branch.id="+parameters.get("bankbranch.id")[0]); 
		}
		if(null!=parameters.get("bankaccount.id")[0] && !parameters.get("bankaccount.id")[0].equals("-1") && !parameters.get("bankaccount.id")[0].equalsIgnoreCase("")){
			phQry=" AND ph.bankaccountnumberid="+parameters.get("bankaccount.id")[0]; 
			instrumentHeaderQry=instrumentHeaderQry.append(" and   ih.bankaccountid ="+parameters.get("bankaccount.id")[0]);
		}
		if(null!=parameters.get("instrumentnumber")[0] && !parameters.get("instrumentnumber")[0].equalsIgnoreCase("")){
			instrumentHeaderQry=instrumentHeaderQry.append(" and   ih.transactionnumber ='"+parameters.get("instrumentnumber")[0]+"'");
		}
		if(null!=parameters.get("fundId")[0] && !parameters.get("fundId")[0].equalsIgnoreCase("")){
			fundQry=" AND vh.fundId            ="+parameters.get("fundId")[0];
		}          
				           
		queryString = queryString
				.append(" SELECT ih.id as ihId , ih.transactionnumber as rtgsNumber,  ih.transactiondate as rtgsDate, vh.id as vhId,  vh.vouchernumber as paymentNumber," +
						" to_char(vh.voucherdate,'dd/mm/yyyy') as paymentDate,   gld.detailtypeid as dtId,  gld.detailkeyid as dkId,   gld.amount as paymentAmount,"+
						" dept.dept_name as department,   stat.description as status,b.name as bank,branch.branchname as bankBranch, ba.accountnumber as accountNumber FROM Paymentheader ph, voucherheader vh,vouchermis vmis,bankaccount ba,bankbranch branch,bank b,generalledger gl,generalledgerdetail gld,"+
						" egf_instrumentvoucher iv,  egf_instrumentheader ih,  eg_department dept ,egw_status stat WHERE "+
						" ph.voucherheaderid   =vh.id AND vmis.voucherheaderid = vh.id "+bankQry.toString()+"  AND ih.bankaccountid = ba.id and branch.id = ba.branchid and branch.bankid = b.id and vh.status = 0 "+
						  fundQry+phQry+ " and stat.id= ih.id_status "+
						" AND dept.id_dept = vmis.departmentid "+deptQry+"  and lower(ph.type)=lower('rtgs') "+instrumentHeaderQry.toString()+
						" AND IV.VOUCHERHEADERID  IS NOT NULL AND iv.voucherheaderid   =vh.id AND ih.instrumentnumber IS NULL " +
						" AND ih.id = iv.instrumentheaderid " +              
						" AND vh.type   = 'Payment' and gl.voucherheaderid = vh.id and gld.generalledgerid = gl.id GROUP BY ih.id , ih.transactionnumber,"+
						" ih.transactiondate, vh.id,  vh.vouchernumber,vh.voucherDate, vmis.departmentid,  dept.dept_name, b.name,branch.branchname,ba.accountnumber,stat.description,gld.detailtypeid,gld.detailkeyid,gld.amount ORDER BY b.name,branch.branchname,ba.accountnumber,ih.transactiondate,ih.transactionnumber,dept.dept_name");
		
		
		 
	return queryString;
	}
	private void populateSubLedgerDetails(){

		Map<Integer,List<EntityType>> subLedgerList =new HashMap<Integer, List<EntityType>>();
		Map<Integer,List<Long>> detailTypeMapForGetEntitys =new HashMap<Integer, List<Long>>();
		for(BankAdviceReportInfo bankAdviceReportInfo:rtgsDisplayList ){
			if(detailTypeMapForGetEntitys.get(bankAdviceReportInfo.getDtId().intValue()) == null)
			{
				detailTypeMapForGetEntitys.put((bankAdviceReportInfo.getDtId().intValue()), new ArrayList<Long>());
				detailTypeMapForGetEntitys.get(bankAdviceReportInfo.getDtId().intValue()).add(bankAdviceReportInfo.getDkId().longValue());	
			}else
			{
				detailTypeMapForGetEntitys.get(bankAdviceReportInfo.getDtId().intValue()).add(bankAdviceReportInfo.getDkId().longValue());
			}

		}

		for(Integer keyGroup:detailTypeMapForGetEntitys.keySet())
		{

			try
			{
				List<EntityType> subDetail = new ArrayList<EntityType>();
				Accountdetailtype detailType = (Accountdetailtype)persistenceService.find("from Accountdetailtype where id=? order by name",keyGroup);
				String table=detailType.getFullQualifiedName();
				Class<?> service = Class.forName(table);
				String simpleName = service.getSimpleName();
				simpleName = simpleName.substring(0,1).toLowerCase()+simpleName.substring(1)+"Service";

				WebApplicationContext wac= WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
				EntityTypeService entityService=(EntityTypeService)wac.getBean(simpleName);
				List<Long> entityIds=new ArrayList<Long>(detailTypeMapForGetEntitys.get(keyGroup));
				int size = entityIds.size();
				if(LOGGER.isInfoEnabled())  LOGGER.info( entityService + " size "+ size);
				if(size>999)
				{
					int fromIndex = 0;
					int toIndex = 0; 
					int step = 1000;

					while(size-step>=0)
					{
						toIndex += step;  
						List<EntityType> returnList=(List<EntityType>)entityService.getEntitiesById(entityIds.subList(fromIndex, toIndex));

						if(returnList!=null)
						{
							subDetail.addAll(returnList);
						}

						fromIndex = toIndex;
						size-=step;


					}

					if(size>0)
					{
						fromIndex = toIndex;
						toIndex = fromIndex+size;
						List<EntityType> returnList = (List<EntityType>)entityService.getEntitiesById(entityIds.subList(fromIndex, toIndex));
						if(returnList!=null)
						{
							subDetail.addAll(returnList);
						}
					}

					subLedgerList.put(keyGroup,subDetail);
				}else
				{
					subDetail.addAll((List<EntityType>)entityService.getEntitiesById(entityIds));
					subLedgerList.put(keyGroup,subDetail);
				}

			}catch (ClassCastException e) {
				LOGGER.error(e);
			}
			catch(Exception e)
			{
				LOGGER.error("Exception to get EntityType="+e.getMessage());
			}

		}
		List<EntityType> subDetail = new ArrayList<EntityType>();

		for(Integer keyGroup:subLedgerList.keySet()){

			for(BankAdviceReportInfo bankAdviceReportInfo:rtgsDisplayList ){
				if(bankAdviceReportInfo.getDtId()!=null){
					if(keyGroup.equals(bankAdviceReportInfo.getDtId().intValue())){

						subDetail = subLedgerList.get(keyGroup);

						for(EntityType entityType:subDetail){
							if(bankAdviceReportInfo.getDtId()!=null){
								if(entityType.getEntityId().equals(bankAdviceReportInfo.getDkId().intValue()))
								{
									if(entityType!=null){
										bankAdviceReportInfo.setPartyName(entityType.getName().toUpperCase());
									}

								}
							}
						}
					}
				}
			}
		}
		for(BankAdviceReportInfo bankAdviceReportInfo:rtgsDisplayList ){
    		
    		if(bankAdviceReportInfo.getStatus().equalsIgnoreCase("new")){
				bankAdviceReportInfo.setStatus("Assigned");
			}
		}
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
	public Bankaccount getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(Bankaccount bankaccount) {
		this.bankaccount = bankaccount;
	}
	public void setHeader(StringBuffer header) {
		this.header = header;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public ReportHelper getReportHelper() {
		return reportHelper;
	}
	public StringBuffer getHeader() {
		return header;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public void setReportHelper(ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}
	public List<BankAdviceReportInfo> getRtgsDisplayList() {
		return rtgsDisplayList;
	}

	public void setRtgsDisplayList(List<BankAdviceReportInfo> rtgsDisplayList) {
		this.rtgsDisplayList = rtgsDisplayList;
	}

	public List<Object> getRtgsReportList() {
		return rtgsReportList;
	}

	public void setRtgsReportList(List<Object> rtgsReportList) {
		this.rtgsReportList = rtgsReportList;
	}
	public String getFormattedDate(Date date){
		return Constants.DDMMYYYYFORMAT2.format(date);
	}
	public Boolean getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(Boolean searchResult) {
		this.searchResult = searchResult;
	}
	
	
}
