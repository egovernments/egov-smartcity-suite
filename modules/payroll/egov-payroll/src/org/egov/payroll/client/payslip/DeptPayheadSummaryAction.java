package org.egov.payroll.client.payslip;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage ;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.services.EISServeable;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;

//@Result(name=Action.SUCCESS, type=ServletRedirectResult.class, value = "preApprovedVoucher.action")
@Results({ 
	@Result(name = "reportView", type = "stream", location = "fileStream", params = { "contentType", "${contentType}", "contentDisposition", "attachment; filename=${fileName}" })	
	})
@ParentPackage("egov")
public class DeptPayheadSummaryAction extends BaseFormAction 
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(DeptPayheadSummaryAction.class);
	private PayRollService payRollService;
	private Integer month;
	private Integer year;
	Integer deptGroupId[];
	private List<HashMap> payslipSet;
	private PayrollExternalInterface payrollExternalInterface;
	private EISServeable eisService;
	private String billNumber;
	private Integer billNumberId;
	//private List payheadList;
	private static final String DEPT_PAYHEADSUMMARY_TEMPLATE = "DeptPayheadSummary";
	private ReportService reportService;
	private ReportOutput ro;
	private String contentType;
	private InputStream fileStream;
	private String fileName;
	private String fileFormat;	
	private String noRecordsFound=""; 
	private Boolean groupByDept = true;
	private ReportHelper reportHelper; 
	String jasperpath = "/reports/templates/DeptPayheadSummary.jasper";
	
	
	/*public List getPayheadList() {
		payheadList = new ArrayList();
		if(getPayslipSet()!=null && getPayslipSet().size()>0){								      	
			Map payslipMap = (LinkedHashMap) getPayslipSet().get(0);
			payheadList.addAll(payslipMap.keySet());
		}
		return payheadList;
	}
	public void setPayheadList(List payheadList) {
		this.payheadList = payheadList;
	}*/
	public List<HashMap> getPayslipSet() {
		return payslipSet;
	}
	public void setPayslipSet(List<HashMap> payslipSet) {
		this.payslipSet = payslipSet;
	}
	public Integer[] getDeptGroupId() {
		return deptGroupId;
	}
	public void setDeptGroupId(Integer[] deptGroupId) {
		this.deptGroupId = deptGroupId;
	}
	
	
	public void setMonth(Integer month) {
		this.month= month;
	}
	
	public Integer getMonth() {
		return month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year= year;
	}
	
	public PayRollService getPayRollService() {
		return payRollService;
	}
	public void setPayRollService(PayRollService payRollService) {
		this.payRollService = payRollService;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public Integer getBillNumberId() {
		return billNumberId;
	}
	public void setBillNumberId(Integer billNumberId) {
		this.billNumberId = billNumberId;
	}
	/*private PersistenceService<CVoucherHeader, Long> voucherService;
	private CVoucherHeader voucherHeader = new CVoucherHeader();
	private EgBillregister egBillregister = new EgBillregister();
	private SimpleWorkflowService<CVoucherHeader> voucherWorkflowService;
	protected EisManager eisManager;
	
	private EisCommonsManager eisCommonsManager;
	private List<EgBillregister> preApprovedVoucherList;
	
	private GenericHibernateDaoFactory genericDao;
	private BillsAccountingManager billsAccountingManager;
	
	
	private PreApprovedVoucher preApprovedVoucher = new PreApprovedVoucher();
	private List<PreApprovedVoucher> billDetailslist;
	private List<PreApprovedVoucher> subLedgerlist;
	private static final String ERROR="error";
	private static final String BILLID="billid";
	private static final String VOUCHEREDIT="voucheredit";
	private static final String VHID="vhid";
	private String values="";*/
	
	/*public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}*/
	
	/*public DeptPayheadSummaryAction(){
		addRelatedEntity("payslip", EmpPayroll.class);
		//addRelatedEntity("detailType", Accountdetailtype.class);
	}*/
	
	

	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public InputStream getFileStream() {
		return new ByteArrayInputStream(ro.getReportOutputData());
	}
	public void setFileStream(InputStream fileStream) {
		this.fileStream = fileStream;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	
	public ReportService getReportService() {
		return reportService;
	}
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	public Boolean getGroupByDept() {
		return groupByDept;
	}
	public void setGroupByDept(Boolean groupByDept) {
		this.groupByDept = groupByDept;
	}
	@Override
	public Object getModel() {
		return null;
	}
	
	@Override
	public void prepare(){ 
		addDropdownData("deptList", getEisService().getDeptsForUser());
		addDropdownData("deptDummyList", new ArrayList<DepartmentImpl>());
		addDropdownData("finYearList", payrollExternalInterface.getAllActivePostingFinancialYear());
	}
	
	@Override
	public String execute() throws Exception {
		request.put("deptList", getEisService().getDeptsForUser());
		return "list";
	}
			
	public String showPayslipList() throws Exception{
		LOGGER.info("Inside DeptPayheadSummaryAction----------");
		LOGGER.info("month/year---"+month+"/"+year);
		noRecordsFound="";
		payslipSet = payRollService.getDeptPayheadSummary(month, year, deptGroupId,billNumberId);		
		if(fileFormat != null && !payslipSet.isEmpty())
		{
			Map<String, Object> reportParams = new HashMap<String, Object>();		
			reportParams.put("EGOV_CROUPBY_DEPT", (groupByDept==null ? false: groupByDept));
			ReportRequest reportInput = new ReportRequest(DEPT_PAYHEADSUMMARY_TEMPLATE, payslipSet,reportParams);
			reportInput.setReportFormat(FileFormat.valueOf(fileFormat));	
			this.contentType = ReportViewerUtil.getContentType(FileFormat.valueOf(fileFormat));
			this.fileName = "deptPayheadSummary." + fileFormat.toLowerCase();
			ro = reportService.createReport(reportInput);
			if (FileFormat.HTM.name().equals(fileFormat) )
			{									
				fileStream=reportHelper.exportHtml(fileStream,jasperpath,reportParams,payslipSet,"px");
				return "reportView";
			}
			else if(!FileFormat.HTM.name().equals(fileFormat))
			{									
				return "reportView";
			}
		}
		else
		{
		noRecordsFound="No Records Found";
		}
		return "list";
	}
	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}
	public EISServeable getEisService() {
		return eisService;
	}
	public void setEisService(EISServeable eisService) {
		this.eisService = eisService;
	}
	public void setReportHelper(ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}
	public String getNoRecordsFound() {
		return noRecordsFound;
	}
	public void setNoRecordsFound(String noRecordsFound) {
		this.noRecordsFound = noRecordsFound;
	}
	
	
}
