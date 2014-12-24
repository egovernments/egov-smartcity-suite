package org.egov.web.actions.report;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVException;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgModules;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.VoucherDetail;
import org.egov.commons.Vouchermis;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.voucher.VoucherSearchAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.Validation;
@Results(value={
		@Result(name="PDF",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=VoucherStatusReport.pdf"}),
		@Result(name="XLS",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=VoucherStatusReport.xls"})
	})
@ParentPackage("egov")
@Validation
 public class VoucherStatusReportAction extends BaseFormAction
 {
	private static final Logger	LOGGER	= Logger.getLogger(VoucherSearchAction.class);
	public List<Map<String,Object>> voucherList;
	private static final long serialVersionUID = 1L;
	public CVoucherHeader voucherHeader = new CVoucherHeader();
	public final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	public String fromDate=null;
	public String toDate=null;
	private final List<String> headerFields = new ArrayList<String>();
	private final List<String> mandatoryFields = new ArrayList<String>();
	InputStream inputStream;
	private static final String JASPERPATH = "/reports/templates/voucherStatusReport.jasper";
	ReportHelper reportHelper;
	List<Object> voucherReportList = new ArrayList<Object>(); 
	List<CVoucherHeader> voucherDisplayList = new ArrayList<CVoucherHeader>();
	private Map<Integer,String> statusMap;
	private Map<String,Object> paramMap=new HashMap<String,Object>();
	private Map<String, String> nameMap;
	
	@Override
	public Object getModel() {
		return voucherHeader;
	}
	
	
	public Map<String, String> getNameMap() {
		return nameMap;
	}

	public void setNameMap(Map<String, String> nameMap) {
		this.nameMap = nameMap;
	}
	public VoucherStatusReportAction()
	{
		voucherHeader.setVouchermis(new Vouchermis());
		addRelatedEntity("vouchermis.departmentid", DepartmentImpl.class);
		addRelatedEntity("fundId", Fund.class);
		addRelatedEntity("vouchermis.schemeid", Scheme.class);
		addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
		addRelatedEntity("vouchermis.functionary", Functionary.class);
		addRelatedEntity("vouchermis.divisionid", BoundaryImpl.class);  
		addRelatedEntity("fundsourceId", Fundsource.class);  
		
	}
	
	public void prepare()
	{
		super.prepare();
		getHeaderFields();
		
		loadDropDowns();
		
		LOGGER.debug("Number of  MIS attributes are :"+headerFields.size());
		LOGGER.debug("Number of mandate MIS attributes are :"+mandatoryFields.size());
		statusMap = new HashMap<Integer,String>();
		statusMap.put(FinancialConstants.CREATEDVOUCHERSTATUS, "Approved");
		statusMap.put(FinancialConstants.REVERSEDVOUCHERSTATUS, "Reversed");
		statusMap.put(FinancialConstants.REVERSALVOUCHERSTATUS, "Reversal");
		statusMap.put(FinancialConstants.CANCELLEDVOUCHERSTATUS, "Cancelled");
		statusMap.put(FinancialConstants.PREAPPROVEDVOUCHERSTATUS, "Preapproved");  
		
	}
	
	private void loadDropDowns() {

		if(headerFields.contains("department")){
			addDropdownData("departmentList", persistenceService.findAllBy("from DepartmentImpl order by deptName"));
		}
		if(headerFields.contains("functionary")){
			addDropdownData("functionaryList", persistenceService.findAllBy(" from Functionary where isactive='1' order by name"));
		}
		if(headerFields.contains("fund")){
			addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive='1' and isnotleaf='0' order by name"));
		}
		if(headerFields.contains("fundsource")){
			addDropdownData("fundsourceList", persistenceService.findAllBy(" from Fundsource where isactive='1' and isnotleaf='0' order by name"));
		}
		if(headerFields.contains("field")){
			addDropdownData("fieldList", persistenceService.findAllBy(" from BoundaryImpl b where lower(b.boundaryType.name)='ward' "));
		}
		if(headerFields.contains("scheme")){
			addDropdownData("schemeList",  Collections.EMPTY_LIST );
		}
		if(headerFields.contains("subscheme")){
			addDropdownData("subschemeList", Collections.EMPTY_LIST);
		}
		
		addDropdownData("typeList", persistenceService.findAllBy(" select distinct vh.type from CVoucherHeader vh  order by vh.type")); //where vh.status!=4
		nameMap=new LinkedHashMap<String, String> ();
		
		
	}


	protected void getHeaderFields() 
	{
		List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'DEFAULT_SEARCH_MISATTRRIBUTES'");
		for (AppConfig appConfig : appConfigList) 
		{
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) 
			{
				String value = appConfigVal.getValue();
				String header=value.substring(0, value.indexOf('|'));
				headerFields.add(header);
				String mandate = value.substring(value.indexOf('|')+1);
				if(mandate.equalsIgnoreCase("M")){
					mandatoryFields.add(header);
				}
			}
		}
		
	}
	public boolean shouldShowHeaderField(String field){
		return  headerFields.contains(field);
	}
	
	public Map<Integer, String> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<Integer, String> statusMap) {
		this.statusMap = statusMap;
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
	@SkipValidation
	public String beforeSearch()
	{
		loadDropDowns();
		fromDate=toDate="";
		
		voucherHeader.reset();
		 
		return "search";
	}
	@ValidationErrorPage(value="search") 
	public String search() throws EGOVException,ParseException
	{
		
		Date fDate = null;
		if(!fromDate.equals(""))
			fDate = formatter.parse(fromDate);
		Date tDate = null;
		if(!toDate.equals(""))
			tDate = formatter.parse(toDate);
		validate();
		List<CVoucherHeader> list ;
		
		list=voucherSearch(fDate,tDate);
		voucherList = new ArrayList<Map<String,Object>>(); 
		Map<String,Object> voucherMap = null;
		for(CVoucherHeader voucherheader :list)
		{
			voucherMap = new HashMap<String,Object>(); 
			BigDecimal amt =BigDecimal.ZERO;
			voucherMap.put("id", voucherheader.getId());
			voucherMap.put("cgn", voucherheader.getCgn());
			voucherMap.put("vouchernumber", voucherheader.getVoucherNumber());
			voucherMap.put("type", voucherheader.getType());
			voucherMap.put("name", voucherheader.getName());		
			voucherMap.put("voucherdate", voucherheader.getVoucherDate());
			voucherMap.put("fundname", voucherheader.getFundId().getName());
			voucherMap.put("deptName", voucherheader.getVouchermis().getDepartmentid().getDeptName());
			for(VoucherDetail detail:voucherheader.getVoucherDetail())
			{
				amt = amt.add(detail.getDebitAmount());
			}
			voucherMap.put("amount", amt);
			voucherMap.put("status",getVoucherStatus(voucherheader.getStatus()));
			voucherMap.put("source", getVoucherModule(voucherheader.getModuleId()));
			voucherList.add(voucherMap);
		}
		loadAjaxedData();
		
		getSession().put("voucherDisplayList", list);
		return "search";
	}

	private void loadAjaxedData() {
		getVoucherNameMap(voucherHeader.getType());
		if (headerFields.contains("scheme")) {
			if (voucherHeader.getFundId() != null
					&& voucherHeader.getFundId().getId() != -1) {
				StringBuffer st = new StringBuffer();
				st.append("from Scheme where isactive=1 and fund.id=");
				st.append(voucherHeader.getFundId().getId());
				dropdownData.put("schemeList", persistenceService.findAllBy(st
						.toString()));
				st.delete(0, st.length() - 1);

			} else
				dropdownData.put("schemeList", Collections.emptyList());
		}
		if (headerFields.contains("subscheme")) {
			if (voucherHeader.getVouchermis() != null
					&& voucherHeader.getVouchermis().getSchemeid() != null
					&& voucherHeader.getVouchermis().getSchemeid().getId() != -1) {

				dropdownData.put("subSchemeList", persistenceService.findAllBy(
						"from SubScheme where isactive=1 and scheme.id=?",
						voucherHeader.getVouchermis().getSchemeid().getId()));

			} else
				dropdownData.put("subSchemeList", Collections.emptyList());
		}
	}


public Map<String, String> getVoucherNameMap(String type) {
	List<Object> voucherNameList = getPersistenceService().findAllBy("select  distinct name from  CVoucherHeader where type=?",type);
	nameMap=new LinkedHashMap<String,String>();

	for(Object voucherName: voucherNameList )
	{
		nameMap.put((String)voucherName,(String)voucherName);
	}
	return nameMap;
}
	
	private List<CVoucherHeader> voucherSearch(Date fDate, Date tDate) {

		String sql = "";
		if (voucherHeader.getFundId() != null
				&& voucherHeader.getFundId().getId() != -1) {
			sql = sql + "  vh.fundId=" + voucherHeader.getFundId().getId();
		}

		if (voucherHeader.getType() != null
				&& !voucherHeader.getType().equals("-1")) {
			sql = sql + " and vh.type='" + voucherHeader.getType() + "'";
		}
		if (voucherHeader.getName() != null
				&& !voucherHeader.getName().equalsIgnoreCase("-1")) {
			sql = sql + " and vh.name='" + voucherHeader.getName() + "'";
		}
		if (fDate != null) {
			sql = sql + " and vh.voucherDate>='"
					+ Constants.DDMMYYYYFORMAT1.format(fDate) + "'";
		}
		if (tDate != null) {
			sql = sql + " and vh.voucherDate<='"
					+ Constants.DDMMYYYYFORMAT1.format(tDate) + "'";
		}
		if (voucherHeader.getStatus() != -1) {
			sql = sql + " and vh.status=" + voucherHeader.getStatus();
		}

		if (voucherHeader.getVouchermis().getDepartmentid() != null
				&& voucherHeader.getVouchermis().getDepartmentid().getId() != -1) {
			sql = sql + " and vh.vouchermis.departmentid="
					+ voucherHeader.getVouchermis().getDepartmentid().getId();
		}

		if (voucherHeader.getVouchermis().getSchemeid() != null) {
			sql = sql + " and vh.vouchermis.schemeid="
					+ voucherHeader.getVouchermis().getSchemeid().getId();
		}
		if (voucherHeader.getVouchermis().getSubschemeid() != null) {
			sql = sql + " and vh.vouchermis.subschemeid="
					+ voucherHeader.getVouchermis().getSubschemeid().getId();
		}
		if (voucherHeader.getVouchermis().getFunctionary() != null) {
			sql = sql + " and vh.vouchermis.functionary="
					+ voucherHeader.getVouchermis().getFunctionary().getId();
		}
		if (voucherHeader.getVouchermis().getDivisionid() != null) {
			sql = sql + " and vh.vouchermis.divisionid="
					+ voucherHeader.getVouchermis().getDivisionid().getId();
		}

		return (List<CVoucherHeader>) persistenceService
				.findAllBy(" from CVoucherHeader vh where  " + sql
						+ " order by vh.vouchermis.departmentid.deptName , vh.voucherNumber ");

	}

	private String getVoucherModule(Integer vchrModuleId) throws EGOVException
	{
		if(vchrModuleId==null)
			return "Internal";
		else
		{  
			EgModules egModuleObj;
			egModuleObj=(EgModules)persistenceService.find("from EgModules m where m.id=?",vchrModuleId);
			if(egModuleObj==null)
				throw new EGOVException("INCORRECT MODULE ID");
			else
				return egModuleObj.getName();
		}
	}
	
	public void validate() {
		checkMandatoryField("fundId","fund",voucherHeader.getFundId(),"voucher.fund.mandatory");
		checkMandatoryField("vouchermis.departmentid","department",voucherHeader.getVouchermis().getDepartmentid(),"voucher.department.mandatory");
		checkMandatoryField("vouchermis.schemeid","scheme",voucherHeader.getVouchermis().getSchemeid(),"voucher.scheme.mandatory");
		checkMandatoryField("vouchermis.subschemeid","subscheme",voucherHeader.getVouchermis().getSubschemeid(),"voucher.subscheme.mandatory");
		checkMandatoryField("vouchermis.functionary","functionary",voucherHeader.getVouchermis().getFunctionary(),"voucher.functionary.mandatory");
		checkMandatoryField("fundsourceId","fundsource",voucherHeader.getVouchermis().getFundsource(),"voucher.fundsource.mandatory");
		checkMandatoryField("vouchermis.divisionId","field",voucherHeader.getVouchermis().getDivisionid(),"voucher.field.mandatory");
	}

	public void setReportHelper(final ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}
	public String generatePdf() throws Exception{
		
		populateData();
		inputStream = reportHelper.exportPdf(inputStream, JASPERPATH, getParamMap(),voucherReportList);
		return "PDF";
	}
	public String generateXls() throws Exception{
		
		populateData();
		inputStream = reportHelper.exportXls(inputStream, JASPERPATH, getParamMap(),voucherReportList);
		return "XLS";
	}

	@SuppressWarnings("unchecked")
	private void populateData() throws ParseException, EGOVException {
		voucherDisplayList=(List<CVoucherHeader>) getSession().get("voucherDisplayList");
		List<CVoucherHeader> list=new ArrayList();
		if(voucherDisplayList==null || voucherDisplayList.size()==0)
		{
			Date fDate = null;
			if(!fromDate.equals(""))
				fDate = formatter.parse(fromDate);
			Date tDate = null;
			if(!toDate.equals(""))
				tDate = formatter.parse(toDate);
			validate();
			list = voucherSearch( fDate, tDate);
		}
		else
			list.addAll( voucherDisplayList);
		int sno=1;
		for(CVoucherHeader cVchrHdr:list)
		{
			VoucherReportView vhcrRptView = new VoucherReportView();
			vhcrRptView.setSerialNumber(sno);
			Date vchrDate=cVchrHdr.getVoucherDate();
			String strVchrDate=new String(vchrDate.toString());
			String datesplit[]=new String[3];
			datesplit=strVchrDate.split("-");
			
			StringBuffer strBufferDate=new StringBuffer();
			strBufferDate.append(datesplit[2]);
			strBufferDate.append("/");
			strBufferDate.append(datesplit[1]);
			strBufferDate.append("/");
			strBufferDate.append(datesplit[0]);
			
			vhcrRptView.setVoucherDate(strBufferDate.toString());
			vhcrRptView.setVoucherNumber(cVchrHdr.getVoucherNumber());
			vhcrRptView.setDeptName(cVchrHdr.getVouchermis().getDepartmentid().getDeptName());
			vhcrRptView.setVoucherType(cVchrHdr.getType());
			vhcrRptView.setVoucherName(cVchrHdr.getName());
			vhcrRptView.setSource(getVoucherModule(cVchrHdr.getModuleId()));
			vhcrRptView.setAmount(cVchrHdr.getTotalAmount());
			vhcrRptView.setStatus(getVoucherStatus(cVchrHdr.getStatus())); 
			voucherReportList.add(vhcrRptView);
			sno++;
		}
		
		setParamMap();
	}
	protected void checkMandatoryField(String objectName,String fieldName,Object value,String errorKey) 
	{
		if(mandatoryFields.contains(fieldName) && ( value == null || value.equals(-1) ))
		{
			addFieldError(objectName, getText(errorKey));
		}
	}
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}
	
	private String getVoucherStatus(int status)
	{
		if(FinancialConstants.CREATEDVOUCHERSTATUS.equals(status))
			return("Approved");
		if(FinancialConstants.REVERSEDVOUCHERSTATUS.equals(status))
			return("Reversed");
		if(FinancialConstants.REVERSALVOUCHERSTATUS.equals(status))
			return("Reversal");
		if(FinancialConstants.CANCELLEDVOUCHERSTATUS.equals(status))
			return("Cancelled");
		if(FinancialConstants.PREAPPROVEDVOUCHERSTATUS.equals(status))
			return("Preapproved");
		return "";
	}

	public void setParamMap()
	{
			paramMap.put("fund", voucherHeader.getFundId().getName());
			if(voucherHeader.getVouchermis()!=null && voucherHeader.getVouchermis().getDepartmentid()!=null )
				paramMap.put("deptName", voucherHeader.getVouchermis().getDepartmentid().getDeptName());
			paramMap.put("status", getVoucherStatus(voucherHeader.getStatus()));
			paramMap.put("toDate", getToDate());
			paramMap.put("fromDate", getFromDate());
			paramMap.put("voucherName",voucherHeader.getName() );
			paramMap.put("voucherType",voucherHeader.getType() );
	}

	protected Map<String, Object> getParamMap() {
		return paramMap;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	

 }
