package org.egov.web.actions.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.FlushMode;

public class ReportAction extends BaseFormAction
{
	private static final long serialVersionUID = 1L;
	protected ReportSearch reportSearch = new ReportSearch();
	protected List<String> headerFields = new ArrayList<String>();
	protected List<String> mandatoryFields = new ArrayList<String>();
	protected List<Object> schemeList = new ArrayList<Object>();
	protected static final String REPORT ="report";
	protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
	protected SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.US);

	@Override
	public Object getModel() {
		return reportSearch;
	}
	public ReportAction()
	{
		addRelatedEntity(Constants.DEPARTMENT, Department.class);
		addRelatedEntity(Constants.FUNCTION, CFunction.class);
		addRelatedEntity(Constants.FUND, Fund.class);
		addRelatedEntity(Constants.SCHEME, Scheme.class);
		addRelatedEntity(Constants.SUBSCHEME, SubScheme.class);
		addRelatedEntity(Constants.FUNCTIONARY, Functionary.class);
		addRelatedEntity(Constants.FUNDSOURCE, Fundsource.class);
		addRelatedEntity(Constants.FIELD, Boundary.class);
	}
	public void prepare()
	{
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		super.prepare();
		getHeaderFields();
		if(headerFields.contains(Constants.DEPARTMENT)){
			addDropdownData("departmentList", persistenceService.findAllBy("from Department order by deptName"));
		}
		if(headerFields.contains(Constants.FUNCTION)){
			addDropdownData("functionList", persistenceService.findAllBy("from CFunction where isactive=1 and isnotleaf=0  order by name"));
		}
		if(headerFields.contains(Constants.FUNCTIONARY)){
			addDropdownData("functionaryList", persistenceService.findAllBy(" from Functionary where isactive=1 order by name"));
		}
		if(headerFields.contains(Constants.FUND)){
			addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=1 and isnotleaf=0 order by name"));
		}
		if(headerFields.contains(Constants.FUNDSOURCE)){
			addDropdownData("fundsourceList", persistenceService.findAllBy(" from Fundsource where isactive=1 and isnotleaf=0 order by name"));
		}
		if(headerFields.contains(Constants.FIELD)){
			addDropdownData("fieldList", persistenceService.findAllBy(" from Boundary b where lower(b.boundaryType.name)='ward' "));
		}
		if(headerFields.contains(Constants.SCHEME)){
			addDropdownData("schemeList",  Collections.EMPTY_LIST );
		}
		if(headerFields.contains(Constants.SUBSCHEME)){
			addDropdownData("subschemeList", Collections.EMPTY_LIST);
		}
	}
	protected void getHeaderFields() 
	{
		List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'REPORT_SEARCH_MISATTRRIBUTES'");
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
		checkMandatoryField(Constants.FUND,Constants.FUND,reportSearch.getFund(),"voucher.fund.mandatory");
		checkMandatoryField(Constants.DEPARTMENT,Constants.DEPARTMENT,reportSearch.getDepartment(),"voucher.department.mandatory");
		checkMandatoryField(Constants.SCHEME,Constants.SCHEME,reportSearch.getScheme(),"voucher.scheme.mandatory");
		checkMandatoryField(Constants.SUBSCHEME,Constants.SUBSCHEME,reportSearch.getSubScheme(),"voucher.subscheme.mandatory");
		checkMandatoryField(Constants.FUNCTIONARY,Constants.FUNCTIONARY,reportSearch.getFunctionary(),"voucher.functionary.mandatory");
		checkMandatoryField(Constants.FUNDSOURCE,Constants.FUNDSOURCE,reportSearch.getFundsource(),"voucher.fundsource.mandatory");
		checkMandatoryField(Constants.FUNCTION,Constants.FUNCTION,reportSearch.getFunction(),"voucher.function.mandatory");
		checkMandatoryField(Constants.FIELD,Constants.FIELD,reportSearch.getField(),"voucher.field.mandatory");
	}
	protected void checkMandatoryField(String objectName,String fieldName,Object value,String errorKey) 
	{
		if(mandatoryFields.contains(fieldName) && value == null)
			addFieldError(objectName, getText(errorKey));
	}
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}
	public boolean shouldShowHeaderField(String field){
		return  headerFields.contains(field);
	}
	public String ajaxLoadSchemes()
	{
		schemeList = (List<Object>)persistenceService.findAllBy(" from Scheme where fund=?", reportSearch.getFund());
		return "schemes";
	}
	
	public String ajaxLoadSubSchemes()
	{
		schemeList = (List<Object>)persistenceService.findAllBy(" from SubScheme where scheme=?",reportSearch.getScheme());
		return "schemes";
	}

	public List<Object> getSchemeList() {
		return schemeList;
	}
	public void setReportSearch(ReportSearch reportSearch) {
		this.reportSearch = reportSearch;
	}
	public ReportSearch getReportSearch() {
		return reportSearch;
	}
}
