package org.egov.web.actions.voucher;

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
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVException;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.VoucherDetail;
import org.egov.commons.Vouchermis;
import org.egov.egf.commons.VoucherSearchUtil;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;

import com.opensymphony.xwork2.validator.annotations.Validation;

@Result(name=com.opensymphony.xwork2.Action.SUCCESS, type="redirect", location = "voucherSearch.action")
@ParentPackage("egov")
@Validation
public class VoucherSearchAction extends BaseFormAction
{
	public Map<String, String> getNameList() {
		return nameList;
	}

	public void setNameList(Map<String, String> nameList) {
		this.nameList = nameList;
	}

	private static final Logger	LOGGER	= Logger.getLogger(VoucherSearchAction.class);
	private static final long serialVersionUID = 1L;
	public CVoucherHeader voucherHeader = new CVoucherHeader();
	public static final String SEARCH ="search";
	public List<Map<String,Object>> voucherList;
	private List<Object> schemeList;
	public  Map<String,String> nameList=new LinkedHashMap<String, String>();;
	
	public final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	public final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	public GenericHibernateDaoFactory genericDao;
	private final List<String> headerFields = new ArrayList<String>();
	private final List<String> mandatoryFields = new ArrayList<String>();
	public String fromDate;
	public String toDate;
	private String showMode;
	private VoucherSearchUtil voucherSearchUtil;
	
	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	
	@Override
	public Object getModel() {
		return voucherHeader;
	}
	
	public VoucherSearchAction()
	{
		voucherHeader.setVouchermis(new Vouchermis());
		addRelatedEntity("vouchermis.departmentid", DepartmentImpl.class);
		addRelatedEntity("fundId", Fund.class);
		addRelatedEntity("vouchermis.schemeid", Scheme.class);
		addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
		addRelatedEntity("vouchermis.functionary", Functionary.class);
		addRelatedEntity("fundsourceId", Fundsource.class);
		addRelatedEntity("vouchermis.divisionid", BoundaryImpl.class);
		
	}
	public void prepare()
	{
		super.prepare();
		getHeaderFields();
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
		
		if(null != parameters.get("showMode"))
		{
			showMode = parameters.get("showMode")[0];
			LOGGER.debug("show mode  :"+ showMode);
			if(showMode.equalsIgnoreCase("nonBillPayment"))
			{
				List<String> typeList=new ArrayList<String>();
				typeList.add(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
				addDropdownData("typeList",typeList);
				voucherHeader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
				nameList=new LinkedHashMap<String,String>();
				nameList.put(FinancialConstants.JOURNALVOUCHER_NAME_CONTRACTORJOURNAL,FinancialConstants.JOURNALVOUCHER_NAME_CONTRACTORJOURNAL);
				nameList.put(FinancialConstants.JOURNALVOUCHER_NAME_SUPPLIERJOURNAL,FinancialConstants.JOURNALVOUCHER_NAME_SUPPLIERJOURNAL);
				nameList.put(FinancialConstants.JOURNALVOUCHER_NAME_SALARYJOURNAL,FinancialConstants.JOURNALVOUCHER_NAME_SALARYJOURNAL);
			}
			else
			{
				addDropdownData("typeList", persistenceService.findAllBy(" select distinct vh.type from CVoucherHeader vh where vh.status!=4 order by vh.type"));		
			}
		}else
		{
			addDropdownData("typeList", persistenceService.findAllBy(" select distinct vh.type from CVoucherHeader vh where vh.status!=4 order by vh.type"));
		}
		LOGGER.debug("Number of  MIS attributes are :"+headerFields.size());
		LOGGER.debug("Number of mandate MIS attributes are :"+mandatoryFields.size());
		
		
	}
	public Map<String, String> getVoucherNameMap(String type) {
		List<Object> voucherNameList = getPersistenceService().findAllBy("select  distinct name from  CVoucherHeader where type=?",type);
		nameList=new LinkedHashMap<String,String>();
	
		for(Object voucherName: voucherNameList )
		{
			nameList.put((String)voucherName,(String)voucherName);
		}
		return nameList;
	}

	@SkipValidation
	public String beforesearch()
	{ 
		if(showMode!=null && showMode.equalsIgnoreCase("nonBillPayment"))
		{
		LOGGER.debug("nonBillPayment");	  
		}
		else
		{
			if(voucherHeader.getType()!=null && !voucherHeader.getType().equals("-1") )	
			{
				getVoucherNameMap(voucherHeader.getType());
			}
		}
		
		return SEARCH;
		
	}
	public void prepareSearch(){
		
		LOGGER.debug("Voucher Search Action | prepareSearch");
		if(!showMode.equalsIgnoreCase("nonbillPayment"))
		{
			if(null != parameters.get("type") && ! parameters.get("type")[0].equalsIgnoreCase("-1")){
				nameList=getVoucherNameMap(parameters.get("type")[0]);	
			}
		}
		
	}
	public String search() throws EGOVException,ParseException
	{
		if(null != parameters.get("showMode")){
			showMode = parameters.get("showMode")[0];
		}
		
		Date fDate = null;
		if(!fromDate.equals(""))
			fDate = formatter.parse(fromDate);
		Date tDate = null;
		if(!toDate.equals(""))
			tDate = formatter.parse(toDate);
		List<CVoucherHeader> list ;
        if(showMode.equalsIgnoreCase("nonbillPayment"))
        {
        	list = voucherSearchUtil.searchNonBillVouchers(voucherHeader, fDate, tDate, showMode);
        }
        else
        {
		list = voucherSearchUtil.search(voucherHeader, fDate, tDate, showMode);
        }
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
			for(VoucherDetail detail:voucherheader.getVoucherDetail())
			{
				amt = amt.add(detail.getDebitAmount());
			}
			voucherMap.put("amount", amt);
			voucherMap.put("status","voucherstatus"+voucherheader.getStatus());//read the value from properties file.
			//voucherMap.put("status", (voucherheader.getStatus()==0?(voucherheader.getIsConfirmed()==0?"UnConfirmed":"Confirmed"):(voucherheader.getStatus()==1?"Reversed":(voucherheader.getStatus()==2?"Reversal":""))));
			voucherList.add(voucherMap);
		}
		//is for voucher name drop down which is ajax call in jsp
		/*if(!showMode.equalsIgnoreCase("nonbillPayment"))
		{
			if(voucherHeader.getType()!=null && !voucherHeader.getType().equals("-1"))
			{
				nameList=getVoucherNameMap(voucherHeader.getType());	
			}
		}*/
		return SEARCH;
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
	public void validate() {
		LOGGER.debug("Inside Validate Method");
		checkMandatoryField("fundId","fund",voucherHeader.getFundId(),"voucher.fund.mandatory");
		checkMandatoryField("vouchermis.departmentid","department",voucherHeader.getVouchermis().getDepartmentid(),"voucher.department.mandatory");
		checkMandatoryField("vouchermis.schemeid","scheme",voucherHeader.getVouchermis().getSchemeid(),"voucher.scheme.mandatory");
		checkMandatoryField("vouchermis.subschemeid","subscheme",voucherHeader.getVouchermis().getSubschemeid(),"voucher.subscheme.mandatory");
		checkMandatoryField("vouchermis.functionary","functionary",voucherHeader.getVouchermis().getFunctionary(),"voucher.functionary.mandatory");
		checkMandatoryField("fundsourceId","fundsource",voucherHeader.getVouchermis().getFundsource(),"voucher.fundsource.mandatory");
		checkMandatoryField("vouchermis.divisionId","field",voucherHeader.getVouchermis().getDivisionid(),"voucher.field.mandatory");
	}
	protected void checkMandatoryField(String objectName,String fieldName,Object value,String errorKey) 
	{
		if(mandatoryFields.contains(fieldName) && value == null)
		{
			addFieldError(objectName, getText(errorKey));
		}
	}
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}
	public boolean shouldShowHeaderField(String field){
		return  headerFields.contains(field);
	}
	
	public List<Map<String,Object>> getVoucherList() {
		return voucherList;
	}
	
	public String ajaxLoadSchemes()
	{
		schemeList = (List<Object>)persistenceService.findAllBy(" from Scheme where fund=?", voucherHeader.getFundId());
		return "schemes";
	}
	
	public String ajaxLoadSubSchemes()
	{
		schemeList = (List<Object>)persistenceService.findAllBy(" from SubScheme where scheme=?",voucherHeader.getVouchermis().getSchemeid());
		return "schemes";
	}

	public List<Object> getSchemeList() {
		return schemeList;
	}

	public void setSchemeId(Integer schemeId) {
		voucherHeader.getVouchermis().setSchemeid((Scheme)persistenceService.find(" from Scheme where id=?",schemeId));
	}

	public void setVoucherHeader(CVoucherHeader voucherHeader) {
		this.voucherHeader = voucherHeader;
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

	public void setVoucherSearchUtil(VoucherSearchUtil voucherSearchUtil) {
		this.voucherSearchUtil = voucherSearchUtil;
	}

}
