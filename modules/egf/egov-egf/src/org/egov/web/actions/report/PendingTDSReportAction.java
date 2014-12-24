package org.egov.web.actions.report;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.deduction.model.EgRemittanceDetail;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.deduction.RemittanceBean;
import org.egov.model.instrument.InstrumentVoucher;
import org.egov.model.recoveries.Recovery;
import org.egov.services.deduction.RemitRecoveryService;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;

@Results(value={
		@Result(name="PDF",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=PendingTDSReport.pdf"}),
		@Result(name="XLS",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=PendingTDSReport.xls"}),
		@Result(name="summary-PDF",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=TdsSummaryReport.pdf"}),
		@Result(name="summary-XLS",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=TdsSummaryReport.xls"})
	})

@ParentPackage("egov")
public class PendingTDSReportAction extends BaseFormAction{
	String jasperpath = "pendingTDSReport";
	String summaryJasperpath = "summaryTDSReport";
	private Date asOnDate = new Date();
	private InputStream inputStream;
	private ReportService reportService;
	private String partyName="";
	private Integer detailKey;
	private boolean showRemittedEntries = false;
	private List<RemittanceBean> pendingTDS = new ArrayList<RemittanceBean>();
	private List<TDSEntry> remittedTDS = new ArrayList<TDSEntry>();
	private Recovery recovery = new Recovery();
	private Fund fund = new Fund();
	private DepartmentImpl department = new DepartmentImpl();
	private EgovCommon egovCommon;
	private List<EntityType> entitiesList = new ArrayList<EntityType>();
	private RemitRecoveryService remitRecoveryService;
	private FinancialYearHibernateDAO financialYearDAO;	
	private String message = ""; 
	
	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public void setRemitRecoveryService(RemitRecoveryService remitRecoveryService) {
		this.remitRecoveryService = remitRecoveryService;
	}

	@Override
	public String execute() throws Exception {
		return "reportForm";
	}
	
	public String summaryReport() throws Exception {
		return "summaryForm";
	}
	
	@Override
	public void prepare() {
		super.prepare();
		addDropdownData("departmentList", persistenceService.findAllBy("from DepartmentImpl order by deptName"));
		addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive='1' and isnotleaf='0' order by name"));
		addDropdownData("recoveryList", persistenceService.findAllBy(" from Recovery where isactive='1' order by recoveryName"));
	}
	
	public String ajaxLoadData(){
		populateData();
		return "results";
	}
	
	public String ajaxLoadSummaryData(){
		populateSummaryData();
		return "summaryResults";
	}

	public void setAsOnDate(Date startDate) {
		this.asOnDate = startDate;
	}

	public Date getAsOnDate() {
		return asOnDate;
	}
	
	public String getFormattedDate(Date date){
		return Constants.DDMMYYYYFORMAT2.format(date);
	}

	public String exportPdf() throws JRException, IOException{
		generateReport();
	    return "PDF";
	}
	public String exportSummaryPdf() throws JRException, IOException{
		generateSummaryReport();
	    return "summary-PDF";
	}

	private void generateReport() {
		populateData();
		ReportRequest reportInput = new ReportRequest(jasperpath, pendingTDS, getParamMap());
		ReportOutput reportOutput = reportService.createReport(reportInput);
		inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
	}
	
	private void generateSummaryReport() {
		populateSummaryData();
		ReportRequest reportInput = new ReportRequest(summaryJasperpath, remittedTDS, getParamMap());
		ReportOutput reportOutput = reportService.createReport(reportInput);
		inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
	}
	
	Map<String, Object> getParamMap() {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("remittedTDSJasper", this.getClass().getResourceAsStream("/reports/templates/remittedTDSReport.jasper"));
		if(showRemittedEntries)
			paramMap.put("remittedTDS", remittedTDS);
		else 
			paramMap.put("remittedTDS", null);
		paramMap.put("asOnDate", Constants.DDMMYYYYFORMAT2.format(asOnDate));
		fund = (Fund) persistenceService.find("from Fund where id=?",fund.getId());
		paramMap.put("fundName", fund.getName());
		paramMap.put("partyName", partyName);
		if(department.getId()!=null && department.getId()!=-1){
			department = (DepartmentImpl) persistenceService.find("from DepartmentImpl where id=?",department.getId());
			paramMap.put("departmentName", department.getDeptName());
		}
		paramMap.put("recoveryName", recovery.getRecoveryName());
		return paramMap;
	}


	private void populateData() {
		List<InstrumentVoucher> instrumentList = persistenceService.findAllBy("from InstrumentVoucher");
		Map<Long,InstrumentVoucher> instrumentVouchers = new HashMap<Long,InstrumentVoucher>(); 
		for (InstrumentVoucher instrumentVoucher : instrumentList) {
			instrumentVouchers.put(instrumentVoucher.getVoucherHeaderId().getId(), instrumentVoucher);
		}
		recovery = (Recovery) persistenceService.find("from Recovery where id=?",recovery.getId());
		String deptQuery="";
		String partyNameQuery="";
		if(department.getId()!=null && department.getId()!=-1){
			deptQuery = " and egRemittanceGldtl.generalledgerdetail.generalledger.voucherHeaderId.vouchermis.departmentid.id="+department.getId();
		}
		if(detailKey!=null && detailKey!=-1){
			partyNameQuery = " and egRemittanceGldtl.generalledgerdetail.detailkeyid="+detailKey;
		}
		RemittanceBean remittanceBean = new RemittanceBean();
		remittanceBean.setRecoveryId(recovery.getId());
		pendingTDS = remitRecoveryService.getPendingRecoveryDetails(remittanceBean, getVoucherHeader(),detailKey);
		if(showRemittedEntries){
			List<EgRemittanceDetail> result = persistenceService.findAllBy("from EgRemittanceDetail where egRemittanceGldtl is not null " +
					"and egRemittanceGldtl.gldtlamt-egRemittanceGldtl.remittedamt=0 and egRemittanceGldtl.generalledgerdetail.generalledger.glcodeId.id=? " +
					"and egRemittance.fund.id=? and egRemittance.voucherheader.status=0 and egRemittanceGldtl.generalledgerdetail.generalledger.voucherHeaderId.status=0 and " +
					"egRemittanceGldtl.generalledgerdetail.generalledger.voucherHeaderId.voucherDate <= ? "+deptQuery+partyNameQuery,
					recovery.getChartofaccounts().getId(),fund.getId(),asOnDate);
			for (EgRemittanceDetail entry : result) {
				TDSEntry tds = createTds(entry);
				tds.setRemittedOn(Constants.DDMMYYYYFORMAT2.format(entry.getLastmodifieddate()));
				if(entry.getEgRemittance().getVoucherheader()!=null){
					tds.setPaymentVoucherNumber(entry.getEgRemittance().getVoucherheader().getVoucherNumber());
				}
				InstrumentVoucher instrumentVoucher = instrumentVouchers.get(entry.getEgRemittance().getVoucherheader().getId());
				if(instrumentVoucher!=null){
					tds.setChequeNumber(instrumentVoucher.getInstrumentHeaderId().getInstrumentNumber());
					tds.setChequeAmount(instrumentVoucher.getInstrumentHeaderId().getInstrumentAmount());
					if(instrumentVoucher.getInstrumentHeaderId().getInstrumentDate()!=null)
						tds.setDrawnOn(Constants.DDMMYYYYFORMAT2.format(instrumentVoucher.getInstrumentHeaderId().getInstrumentDate()));
				}
				remittedTDS.add(tds);
			}
		}
	}

	private void populateSummaryData() {
		recovery = (Recovery) persistenceService.find("from Recovery where id=?",recovery.getId());
		String deptQuery="";
		String partyNameQuery="";
		if(department.getId()!=null && department.getId()!=-1){
			deptQuery = " and mis.departmentid="+department.getId();
		}
		if(detailKey!=null && detailKey!=-1){
			partyNameQuery = " and gld.detailkeyid="+detailKey;
		}
		List<Object[]> result = new ArrayList<Object[]>();
		try {
			result = persistenceService.getSession().createSQLQuery("select vh.name,sum(ergl.remittedamt),er.month,sum(ergl.gldtlamt) from eg_remittance_detail erd," +
					"voucherheader vh1 right outer join eg_remittance er on vh1.id=er.paymentvhid,voucherheader vh,vouchermis mis,generalledger gl,generalledgerdetail gld,fund f,eg_remittance_gldtl ergl where " +
					" erd.remittancegldtlid= ergl.id and erd.remittanceid=er.id and gl.glcodeid="+recovery.getChartofaccounts().getId()+" and vh.id=mis.voucherheaderid and " +
					"  vh1.status=0 and ergl.gldtlid=gld.id and gl.id=gld.generalledgerid and gl.voucherheaderid=vh.id and er.fundid=f.id and f.id="+fund.getId()+
					" and vh.status=0 and vh.voucherDate <= '"+Constants.DDMMYYYYFORMAT2.format(asOnDate)+"' and "+"vh.voucherDate >= '"+Constants.DDMMYYYYFORMAT2.format(financialYearDAO.getFinancialYearByDate(asOnDate).getStartingDate())+"' "+deptQuery+partyNameQuery+" group by er.month,vh.name order by er.month,vh.name").list();
		}catch(EGOVRuntimeException e) {
			message = e.getMessage();
			return;
		}catch (Exception e) {
			message = e.getMessage();
			return;
		}
		for (Object[] entry : result) {
			TDSEntry tds = new TDSEntry();
			tds.setNatureOfDeduction(entry[0].toString());
			tds.setTotalRemitted(new BigDecimal(entry[1].toString()));
			tds.setMonth(DateUtils.getAllMonthsWithFullNames().get(Integer.valueOf(entry[2].toString())+1));
			tds.setTotalDeduction(new BigDecimal(entry[3].toString()));
			remittedTDS.add(tds);
		}
	}

	private CVoucherHeader getVoucherHeader() {
		CVoucherHeader voucherHeader = new CVoucherHeader();
		voucherHeader.setFundId(fund);
		voucherHeader.setDepartmentId(department.getId());
		voucherHeader.setVoucherDate(asOnDate);
		return voucherHeader;
	}
	
	public String ajaxLoadEntites() throws ClassNotFoundException{
		if(parameters.containsKey("recoveryId") && parameters.get("recoveryId")[0] !=null && !"".equals(parameters.get("recoveryId")[0])){
			recovery = (Recovery) persistenceService.find("from Recovery where id=?",Long.valueOf(parameters.get("recoveryId")[0]));
			for (CChartOfAccountDetail detail : recovery.getChartofaccounts().getChartOfAccountDetails()) {
				entitiesList.addAll(egovCommon.loadEntitesFor(detail.getDetailTypeId()));
			}
		}
		return "entities";
	}

	private TDSEntry createTds(EgRemittanceDetail entry) {
		TDSEntry tds = new TDSEntry();
		if(entry.getEgRemittanceGldtl().getRecovery()!=null){
			tds.setPartyCode(entry.getEgRemittanceGldtl().getRecovery().getEgPartytype().getCode());
		}
		tds.setNatureOfDeduction(entry.getEgRemittanceGldtl().getGeneralledgerdetail().getGeneralledger().getVoucherHeaderId().getName());
		tds.setVoucherNumber(entry.getEgRemittanceGldtl().getGeneralledgerdetail().getGeneralledger().getVoucherHeaderId().getVoucherNumber());
		tds.setVoucherDate(Constants.DDMMYYYYFORMAT2.format(entry.getEgRemittanceGldtl().getGeneralledgerdetail().getGeneralledger().getVoucherHeaderId().getVoucherDate()));
		EntityType entityType = getEntity(entry);
		if(entityType!=null){
			tds.setPartyName(entityType.getName());
			tds.setPartyCode(entityType.getCode());
			tds.setPanNo(entityType.getPanno());
		}
		tds.setAmount(entry.getEgRemittanceGldtl().getGldtlamt());
		return tds;
	}

	private EntityType getEntity(EgRemittanceDetail entry) {
		EgovCommon common = new EgovCommon();
		common.setPersistenceService(persistenceService);
		Integer detailKeyId = entry.getEgRemittanceGldtl().getGeneralledgerdetail().getDetailkeyid().intValue();
		EntityType entityType = null;
		try {
			entityType = common.getEntityType(entry.getEgRemittanceGldtl().getGeneralledgerdetail().getAccountdetailtype(),detailKeyId);
		} catch (EGOVException e) {
			e.printStackTrace();
		}
		return entityType;
	}
	
	public String exportXls() throws JRException, IOException{
		populateData();
		ReportRequest reportInput = new ReportRequest(jasperpath, pendingTDS, getParamMap());
		reportInput.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportInput);
		inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
	    return "XLS";
	}
	
	public String exportSummaryXls() throws JRException, IOException{
		populateSummaryData();
		ReportRequest reportInput = new ReportRequest(summaryJasperpath, remittedTDS, getParamMap());
		reportInput.setReportFormat(FileFormat.XLS);
		ReportOutput reportOutput = reportService.createReport(reportInput);
		inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
	    return "summary-XLS";
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public Object getModel() {
		return null;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setShowRemittedEntries(boolean showRemittedEntries) {
		this.showRemittedEntries = showRemittedEntries;
	}
	
	public boolean getShowRemittedEntries() {
		return showRemittedEntries;
	}

	public boolean isShowRemittedEntries() {
		return showRemittedEntries;
	}

	public void setPendingTDS(List<RemittanceBean> pendingTDS) {
		this.pendingTDS = pendingTDS;
	}

	public List<RemittanceBean> getPendingTDS() {
		return pendingTDS;
	}

	public void setRemittedTDS(List<TDSEntry> remittedTDS) {
		this.remittedTDS = remittedTDS;
	}

	public List<TDSEntry> getRemittedTDS() {
		return remittedTDS;
	}

	public void setRecovery(Recovery recovery) {
		this.recovery = recovery;
	}

	public Recovery getRecovery() {
		return recovery;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public Fund getFund() {
		return fund;
	}
	
	public List<EntityType> getEntitiesList() {
		return entitiesList;
	}

	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}

	public DepartmentImpl getDepartment() {
		return department;
	}

	public void setDetailKey(Integer detailKey) {
		this.detailKey = detailKey;
	}

	public Integer getDetailKey() {
		return detailKey;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
