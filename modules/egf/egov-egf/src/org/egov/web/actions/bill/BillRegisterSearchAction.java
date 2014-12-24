/**
 * 
 */
package org.egov.web.actions.bill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.egov.web.actions.BaseFormAction;

/**
 * @author manoranjan
 *
 */
public class BillRegisterSearchAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private static final Logger	LOGGER	= Logger.getLogger(BillRegisterSearchAction.class);
	private  List<String> headerFields = new ArrayList<String>();
	private  List<String> mandatoryFields = new ArrayList<String>();
	private EgBillregister billregister;
	private String billDateFrom;
	private  String billDateTo;
	private String expType;
	private List<Map<String, Object>> billList;

	public BillRegisterSearchAction()
	{
		billregister = new EgBillregister();
		billregister.setEgBillregistermis(new EgBillregistermis());
		addRelatedEntity("egBillregistermis.egDepartment", DepartmentImpl.class);
		addRelatedEntity("egBillregistermis.fund", Fund.class);
		addRelatedEntity("egBillregistermis.scheme", Scheme.class);
		addRelatedEntity("egBillregistermis.subScheme", SubScheme.class);
		addRelatedEntity("egBillregistermis.functionaryid", Functionary.class);
		addRelatedEntity("egBillregistermis.fundsource", Fundsource.class);
		addRelatedEntity("egBillregistermis.fieldid", BoundaryImpl.class);
		
	}
	@Override
	public Object getModel() {
		
		return billregister;
		
	}
	@Override
	public void prepare() {
		super.prepare();
		LOGGER.debug("BillRegisterSearchAction | prepare | Start");
		List<String> expTypeList =  new ArrayList<String>();
		expTypeList.add(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
		addDropdownData("expType",expTypeList);
		getHeaderFields();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		if(headerFields.contains("department")){
			addDropdownData("departmentList", masterCache.get("egi-department"));
		}
		if(headerFields.contains("functionary")){
			addDropdownData("functionaryList", masterCache.get("egi-functionary"));
		}
		if(headerFields.contains("fund")){
			addDropdownData("fundList",  masterCache.get("egi-fund"));
		}
		if(headerFields.contains("fundsource")){
			addDropdownData("fundsourceList", masterCache.get("egi-fundSource"));
		}
		if(headerFields.contains("field")){
			addDropdownData("fieldList", masterCache.get("egi-ward"));
		}
		if(headerFields.contains("scheme")){
			addDropdownData("schemeList",  Collections.EMPTY_LIST );
		}
		if(headerFields.contains("subscheme")){
			addDropdownData("subschemeList", Collections.EMPTY_LIST);
		}
		LOGGER.debug("BillRegisterSearchAction | prepare | End");
	}
	
	public String newform(){
		LOGGER.debug("BillRegisterSearchAction | newform | Start");
		return NEW;
	}
	
	public String search(){
		
		LOGGER.debug("BillRegisterSearchAction | search | Start");
		StringBuffer query = new StringBuffer(500);
		query.append("select br.expendituretype , br.billtype ,br.billnumber , br.billdate , br.billamount , br.passedamount ,egwstatus.description,billmis.sourcePath,br.id ").
		append("from EgBillregister br, EgBillregistermis billmis , EgwStatus egwstatus where   billmis.egBillregister.id = br.id and egwstatus.id = br.status.id  ").
		append(" and br.expendituretype=?").append(VoucherHelper.getBillDateQuery(billDateFrom, billDateTo))
		.append(VoucherHelper.getBillMisQuery(billregister));
		List<Object[]>  list = persistenceService.findAllBy(query.toString(),expType);
		LOGGER.debug("Total number of bills found =: "+ list.size());
	    
		if(list.size() != 0){
			billList = new ArrayList<Map<String,Object>>();
			Map<String,Object> billMap;
			for (Object[] object : list) {
				billMap = new HashMap<String, Object>();
				billMap.put("expendituretype", object[0].toString());
				billMap.put("billtype", object[1].toString());
				billMap.put("billnumber", object[2].toString());
				billMap.put("billdate", object[3]);
				billMap.put("billamount", object[4]);
				billMap.put("passedamount", object[5]);
				billMap.put("billstatus", object[6].toString());
				if(null !=object[7]){
					billMap.put("sourcepath", object[7].toString());   
				}else{
					billMap.put("sourcepath", "/EGF/bill/contingentBill!beforeView.action?billRegisterId="+object[8].toString());  
				}
				
				billList.add(billMap);
			}
		}
		return NEW;
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
	
	public void setMandatoryFields(List<String> mandatoryFields) {
		this.mandatoryFields = mandatoryFields;
	}
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}
	public boolean shouldShowHeaderField(String field){
		return  headerFields.contains(field);
	}
	public void setBillregister(EgBillregister billregister) {
		this.billregister = billregister;
	}
	public void setExpType(String expType) {
		this.expType = expType;
	}
	public void setBillDateFrom(String billDateFrom) {
		this.billDateFrom = billDateFrom;
	}
	public void setBillDateTo(String billDateTo) {
		this.billDateTo = billDateTo;
	}
	public void setBillList(List<Map<String, Object>> billList) {
		this.billList = billList;
	}
	public String getBillDateFrom() {
		return billDateFrom;
	}
	public String getBillDateTo() {
		return billDateTo;
	}
	public String getExpType() {
		return expType;
	}
	public List<Map<String, Object>> getBillList() {
		return billList;
	}
	public void setHeaderFields(List<String> headerFields) {
		this.headerFields = headerFields;
	}
}
