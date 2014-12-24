package org.egov.web.actions.bill;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptContext;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.bills.EgSalaryCodes;
import org.egov.model.voucher.PreApprovedVoucher;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.jdbc.ReturningWork;

import com.exilant.eGov.src.transactions.CommonMethodsImpl;

@ParentPackage("egov")
public class SalaryBillRegisterAction extends BaseFormAction{
	private EgBillregister billregister = new EgBillregister();
	private EgBillregistermis billregistermis = new EgBillregistermis();
	private PersistenceService<EgBillregister, Long> billRegisterService;
	private PersistenceService<EgBillregistermis, Long> billRegisterMisService;
	private PersistenceService<EgBilldetails, Long> billDetailsService;
	private PersistenceService<EgBillPayeedetails, Long> billPayeeDetailsService;
	private List<EgBilldetails> earningsList = new ArrayList<EgBilldetails>(); 
	private List<EgBilldetails> deductionsList = new ArrayList<EgBilldetails>();
	private List<EgBilldetails> netPayList = new ArrayList<EgBilldetails>();
	private List<PreApprovedVoucher> subledgerList = new ArrayList<PreApprovedVoucher>();
	private Map<BigDecimal, CChartOfAccounts> coaAndIds = new HashMap<BigDecimal, CChartOfAccounts>();
	private Map<BigDecimal, String> coaIdAndHead = new HashMap<BigDecimal, String>();
	private List<CChartOfAccounts> glcodesList = new ArrayList<CChartOfAccounts>();
	private CommonMethodsImpl commonMethodsImpl;
	private ScriptService scriptExecutionService;
	private boolean close = false;
	private String message="";
	private Long billregisterId;
	private List<EgSalaryCodes> earningsCodes = new ArrayList<EgSalaryCodes>();
	private List<EgSalaryCodes> deductionsCodes = new ArrayList<EgSalaryCodes>();
	private GenericHibernateDaoFactory genericDao;
	private CChartOfAccounts	defaultNetPayCode;
	
	public SalaryBillRegisterAction() {
		addRelatedEntity("fieldList", BoundaryImpl.class);
		addRelatedEntity("functionaryList", Functionary.class);
		addRelatedEntity("departmentList", DepartmentImpl.class);
		addRelatedEntity("financialYearList", CFinancialYear.class);
	}
	@Override
	public Object getModel() {
		return getBillregister();
	}
	
	@Override
	public void prepare() {
		super.prepare();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		addDropdownData("fieldList", masterCache.get("egi-ward"));
		addDropdownData("departmentList", masterCache.get("egi-department"));
		addDropdownData("functionaryList", masterCache.get("egi-functionary"));
		addDropdownData("financialYearList", persistenceService.findAllBy("from CFinancialYear where isActive=1 order by finYearRange desc "));
		addDropdownData("detailTypeList", Collections.EMPTY_LIST);
		populateSalaryCode();
		populateEarningCodes();
		populateDeductionCodes();
		addDropdownData("glcodeList", getGlcodesList());
	}
	private void populateEarningCodes() {
		earningsList = new ArrayList<EgBilldetails>();
		earningsCodes = persistenceService.findAllBy("from EgSalaryCodes where salType='Earnings' order by chartOfAccount.glcode");
		for (EgSalaryCodes row : earningsCodes) {
			EgBilldetails billdetails = new EgBilldetails();
			billdetails.setGlcodeid(BigDecimal.valueOf(row.getChartOfAccount().getId()));
			billdetails.setDebitamount(BigDecimal.ZERO);
			earningsList.add(billdetails);
			coaAndIds.put(BigDecimal.valueOf(row.getChartOfAccount().getId()), row.getChartOfAccount());
			coaIdAndHead.put(BigDecimal.valueOf(row.getChartOfAccount().getId()), row.getHead());
			glcodesList.add(row.getChartOfAccount());
		}
	}
	private void populateDeductionCodes() {
		deductionsList = new ArrayList<EgBilldetails>();
		deductionsCodes = persistenceService.findAllBy("from EgSalaryCodes where salType='Deduction' order by chartOfAccount.glcode");
		for (EgSalaryCodes row : deductionsCodes) {
			EgBilldetails billdetails = new EgBilldetails();
			billdetails.setCreditamount(BigDecimal.ZERO);
			billdetails.setGlcodeid(BigDecimal.valueOf(row.getChartOfAccount().getId()));
			deductionsList.add(billdetails);
			coaAndIds.put(BigDecimal.valueOf(row.getChartOfAccount().getId()), row.getChartOfAccount());
			coaIdAndHead.put(BigDecimal.valueOf(row.getChartOfAccount().getId()), row.getHead());
			glcodesList.add(row.getChartOfAccount());
		}
	}

	private void populateSalaryCode() {
		netPayList = new ArrayList<EgBilldetails>();
//		String salaryBillDefaultPurposeId = EGovConfig.getProperty("egf_config.xml","salaryBillDefaultPurposeId","","defaultValues");
//		String salaryBillPurposeIds = EGovConfig.getProperty("egf_config.xml","salaryBillPurposeIds","","defaultValues");
		List<AppConfigValues> configValuesByModuleAndKey = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","salaryBillPurposeIds");
		List<AppConfigValues> defaultConfigValuesByModuleAndKey = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","salaryBillDefaultPurposeId");
		String cBillDefaulPurposeId = defaultConfigValuesByModuleAndKey.get(0).getValue();
		List<CChartOfAccounts> salaryPayableCoa = persistenceService.findAllBy("FROM CChartOfAccounts WHERE purposeid in ("+cBillDefaulPurposeId+") and isactiveforposting = 1 and classification=4");
		for (CChartOfAccounts chartOfAccounts : salaryPayableCoa) {
			EgBilldetails billdetails = new EgBilldetails();
			billdetails.setGlcodeid(BigDecimal.valueOf(chartOfAccounts.getId()));
			netPayList.add(billdetails);
			coaAndIds.put(BigDecimal.valueOf(chartOfAccounts.getId()), chartOfAccounts);
			if(cBillDefaulPurposeId.equals(chartOfAccounts.getPurposeId())){
				defaultNetPayCode = chartOfAccounts;
			}
		}
	}
	
	@Override
	public String execute() throws Exception {
		return NEW;
	}
	
	private void save(){
		saveBillRegister();
		billregistermis.setEgBillregister(getBillregister());
		setValuesOnBillRegisterMis();
		billregistermis = billRegisterMisService.persist(billregistermis);
		saveBilldetails();
		saveBillPayeeDetails();
		populateSalaryCode();
		populateEarningCodes();
		populateDeductionCodes();
	}
	private void saveBillRegister() {
		billregister.setBillnumber(generateBillNumber());
		billregister.setExpendituretype("Salary");
		billregister.setBillstatus("Created");
		billregister.setBillamount(netPayList.get(0).getCreditamount());
		billregister.setStatus((EgwStatus) persistenceService.find("from EgwStatus where moduletype='SALBILL' and description='Created'"));
		billregister.setEgBillregistermis(null);
		setBillregister(billRegisterService.persist(billregister));
	}
	private void saveBilldetails() {
		for (EgBilldetails row : earningsList) {
			row.setEgBillregister(getBillregister());
			if(row.getFunctionid()!=null && BigDecimal.ZERO.equals(row.getFunctionid())){
				row.setFunctionid(null);
			}
			billDetailsService.persist(row);
		}
		for (EgBilldetails row : deductionsList) {
			row.setEgBillregister(getBillregister());
			if(row.getFunctionid()!=null && BigDecimal.ZERO.equals(row.getFunctionid())){
				row.setFunctionid(null);
			}
			billDetailsService.persist(row);
		}
	}
	
	private void saveBillPayeeDetails() {
		for (PreApprovedVoucher row : subledgerList) {
			EgBillPayeedetails billPayeedetails = new EgBillPayeedetails();
			billPayeedetails.setAccountDetailKeyId(row.getDetailKeyId());
			billPayeedetails.setAccountDetailTypeId(row.getDetailType().getId());
			billPayeedetails.setCreditAmount(row.getCreditAmount());
			billPayeedetails.setDebitAmount(row.getDebitAmount());
			billPayeedetails.setEgBilldetailsId(getEgBillDetailsForGlCode(coaAndIds.get(new BigDecimal(row.getGlcode().getId()))));
			billPayeeDetailsService.persist(billPayeedetails);
		}
	}

	private EgBilldetails getEgBillDetailsForGlCode(CChartOfAccounts chartOfAccounts) {
		for (EgBilldetails row : earningsList) {
			if(chartOfAccounts!=null && chartOfAccounts.getId()!=null && chartOfAccounts.getId().equals(row.getGlcodeid().longValue())){
				return row;
			}
		}
		for (EgBilldetails row : deductionsList) {
			if(chartOfAccounts!=null && chartOfAccounts.getId()!=null && chartOfAccounts.getId().equals(row.getGlcodeid().longValue())){
				return row;
			}
		}
		return null;
	}
	private void setValuesOnBillRegisterMis() {
		if(billregistermis.getEgDepartment() != null && billregistermis.getEgDepartment().getId()!=null)
			billregistermis.setEgDepartment((DepartmentImpl) persistenceService.find("from DepartmentImpl where id=?",billregistermis.getEgDepartment().getId()));
		if(billregistermis.getFinancialyear() != null && billregistermis.getFinancialyear().getId()!=null)
			billregistermis.setFinancialyear((CFinancialYear) persistenceService.find("from CFinancialYear where id=?",billregistermis.getFinancialyear().getId()));
		if(billregistermis.getFieldid() != null && billregistermis.getFieldid().getId()!=null)
			billregistermis.setFieldid((BoundaryImpl) persistenceService.find("from BoundaryImpl where id=?",billregistermis.getFieldid().getId()));
		if(billregistermis.getFunctionaryid() != null && billregistermis.getFunctionaryid().getId()!=null)
			billregistermis.setFunctionaryid((Functionary) persistenceService.find("from Functionary where id=?",billregistermis.getFunctionaryid().getId()));
	}
	public String saveAndNew(){
		save();
		message = getText("salary.bill.saved.successfully")+" "+getBillregister().getBillnumber();
		addActionMessage(message);
		setBillregister(new EgBillregister());
		billregistermis = new EgBillregistermis();
		return NEW;
	}
	
	public String saveAndClose(){
		save();
		message = getText("salary.bill.saved.successfully")+" "+getBillregister().getBillnumber();
		addActionMessage(message);
		setClose(true);
		return NEW;
	}
	
	public String view() {
		setBillregister((EgBillregister) persistenceService.find("from EgBillregister where id=?", billregisterId));
		billregistermis = getBillregister().getEgBillregistermis();
		earningsList = persistenceService.findAllBy("from EgBilldetails where egBillregister.id=? and glcodeid in ("+getGlCodeIds(earningsCodes)+")", billregisterId);
		deductionsList = persistenceService.findAllBy("from EgBilldetails where egBillregister.id=? and glcodeid in ("+getGlCodeIds(deductionsCodes)+")", billregisterId);
		subledgerList = persistenceService.findAllBy("from EgBillPayeedetails where egBilldetailsId.id in ("+getBillDetailsId(earningsList,deductionsList)+")");
		return Constants.VIEW;
	}
	private String getBillDetailsId(List<EgBilldetails> earningsList,List<EgBilldetails> deductionsList) {
		String billDetailIds = "0,";
		for (EgBilldetails egBilldetails : earningsList) {
			billDetailIds = billDetailIds.concat(egBilldetails.getId().toString()).concat(",");
		}
		for (EgBilldetails egBilldetails : deductionsList) {
			billDetailIds = billDetailIds.concat(egBilldetails.getId().toString()).concat(",");
		}
		billDetailIds = billDetailIds.substring(0, billDetailIds.length()-2);
		return billDetailIds;
	}
	private String getGlCodeIds(List<EgSalaryCodes> earningsCodes) {
		String glcodeIds = "0";
		for (EgSalaryCodes egSalaryCodes : earningsCodes) {
			glcodeIds = glcodeIds.concat(",").concat(egSalaryCodes.getChartOfAccount().getId().toString());
		}
		if(glcodeIds.length()>1)
			glcodeIds = glcodeIds.substring(0, glcodeIds.length()-2);
		return glcodeIds;
	}
	public void setBillRegisterService(PersistenceService<EgBillregister, Long> billRegisterService) {
		this.billRegisterService = billRegisterService;
	}
	public void setEarningsList(List<EgBilldetails> earningsList) {
		this.earningsList = earningsList;
	}
	public List<EgBilldetails> getEarningsList() {
		return earningsList;
	}
	public void setDeductionsList(List<EgBilldetails> deductionsList) {
		this.deductionsList = deductionsList;
	}
	public List<EgBilldetails> getDeductionsList() {
		return deductionsList;
	}
	public void setSubledgerList(List<PreApprovedVoucher> earningsSubledgerList) {
		this.subledgerList = earningsSubledgerList;
	}
	public List<PreApprovedVoucher> getSubledgerList() {
		return subledgerList;
	}
	public Map<BigDecimal, CChartOfAccounts> getCoaAndIds() {
		return coaAndIds;
	}
	public void setNetPayList(List<EgBilldetails> netPayList) {
		this.netPayList = netPayList;
	}
	public List<EgBilldetails> getNetPayList() {
		return netPayList;
	}
	public Map<BigDecimal, String> getCoaIdAndHead() {
		return coaIdAndHead;
	}
	public void setBillRegisterMisService(PersistenceService<EgBillregistermis, Long> billRegisterMisService) {
		this.billRegisterMisService = billRegisterMisService;
	}
	public void setBillDetailsService(PersistenceService<EgBilldetails, Long> billDetailsService) {
		this.billDetailsService = billDetailsService;
	}
	public EgBillregistermis getBillregistermis() {
		return billregistermis;
	}
	
	protected String generateBillNumber(){
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<String>() {			
			@Override
			public String execute(Connection connection) throws SQLException {
				Script billNumberScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME, "salary.billnumber").get(0);
				return (String) scriptExecutionService.executeScript(billNumberScript, (ScriptContext) scriptExecutionService.createContext("commonMethodsImpl",commonMethodsImpl,"connection",connection,"wfItem",getBillregister()));
			}
		});
	}
	public void setCommonMethodsImpl(CommonMethodsImpl commonMethodsImpl) {
		this.commonMethodsImpl = commonMethodsImpl;
	}
	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	public void setGlcodesList(List<CChartOfAccounts> glcodesList) {
		this.glcodesList = glcodesList;
	}
	public List<CChartOfAccounts> getGlcodesList() {
		return glcodesList;
	}
	public void setBillPayeeDetailsService(PersistenceService<EgBillPayeedetails, Long> billPayeeDetailsService) {
		this.billPayeeDetailsService = billPayeeDetailsService;
	}
	public void setClose(boolean close) {
		this.close = close;
	}
	public boolean isClose() {
		return close;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setBillregisterId(Long billregisterId) {
		this.billregisterId = billregisterId;
	}
	public Long getBillregisterId() {
		return billregisterId;
	}
	public void setBillregister(EgBillregister billregister) {
		this.billregister = billregister;
	}
	public EgBillregister getBillregister() {
		return billregister;
	}
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	public void setDefaultNetPayCode(CChartOfAccounts defaultNetPayCode) {
		this.defaultNetPayCode = defaultNetPayCode;
	}
	public CChartOfAccounts getDefaultNetPayCode() {
		return defaultNetPayCode;
	}
}
