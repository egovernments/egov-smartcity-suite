package org.egov.web.actions.report;


import org.apache.struts2.convention.annotation.Action;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.VoucherDetail;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.models.State;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.NumberToWord;
import org.egov.infstr.workflow.inbox.InboxService;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBillregistermis;
import org.egov.pims.commons.Position;
import org.egov.services.budget.BudgetAppropriationService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.actions.voucher.VoucherReport;
import org.hibernate.FlushMode;
import org.hibernate.SQLQuery;

@Results(value={
	@Result(name="PDF",type=StreamResult.class,value="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=ExpenseJournalVoucherReport.pdf"}),
	@Result(name="XLS",type=StreamResult.class,value="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=ExpenseJournalVoucherReport.xls"}),
	@Result(name="HTML",type=StreamResult.class,value="inputStream", params={"inputName","inputStream","contentType","text/html"})
})

@ParentPackage("egov")
public class ExpenseJournalVoucherPrintAction extends BaseFormAction{
	String jasperpath = "/org/egov/web/actions/report/expenseJournalVoucherReport.jasper";
	private static final long serialVersionUID = 1L;
	private static final String PRINT = "print";
	private CVoucherHeader voucher = new CVoucherHeader();
	List<Object> voucherReportList = new ArrayList<Object>();
	InputStream inputStream;
	ReportHelper reportHelper;
	Long id;
	EgBillregistermis billRegistermis;
	List<EgBillPayeedetails> billPayeeDetails = new ArrayList<EgBillPayeedetails>();
	private static final String ACCDETAILTYPEQUERY=" from Accountdetailtype where id=?";
	private InboxService inboxService;
	private BudgetAppropriationService budgetAppropriationService;

	public Long getId() {
		return id;
	}

	public void setBudgetAppropriationService(BudgetAppropriationService budgetAppropriationService) {
		this.budgetAppropriationService = budgetAppropriationService;
	}

	public void setReportHelper(ReportHelper helper) {
		this.reportHelper = helper;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public List<Object> getVoucherReportList() {
		return voucherReportList;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	
	public String execute(){
		return print();
	}
	
@Action(value="/report/expenseJournalVoucherPrint-ajaxPrint")
	public String ajaxPrint(){
		return exportHtml();
	}
	
	@Override
	public Object getModel() {
		return voucher;
	}
	
@Action(value="/report/expenseJournalVoucherPrint-print")
	public String print() {
		return PRINT;
	}

	private void populateVoucher() {
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		if(!StringUtils.isBlank(parameters.get("id")[0])){
			Long id = Long.valueOf(parameters.get("id")[0]);
			CVoucherHeader voucherHeader = (CVoucherHeader) HibernateUtil.getCurrentSession().get(CVoucherHeader.class,id);
			if(voucherHeader != null){
				voucher = voucherHeader;
				billRegistermis = (EgBillregistermis) persistenceService.find("from EgBillregistermis where voucherHeader.id=?",voucherHeader.getId());
				if(billRegistermis!=null){
					billPayeeDetails = persistenceService.findAllBy("from EgBillPayeedetails where egBilldetailsId.egBillregister.id=?",billRegistermis.getEgBillregister().getId());
				}
				generateVoucherReportList();
			}
		}
	}

	private void generateVoucherReportList() {
		if(voucher != null){
			for (VoucherDetail vd : voucher.getVoucherDetail()) {
				if(BigDecimal.ZERO.equals(vd.getCreditAmount())){
					VoucherReport voucherReport = new VoucherReport(persistenceService,Integer.valueOf(voucher.getId().toString()),vd);
					if(billRegistermis!=null){
						voucherReport.setDepartment(billRegistermis.getEgDepartment());
					}
					voucherReportList.add(voucherReport);
				}
			}
			for (VoucherDetail vd : voucher.getVoucherDetail()) {
				if(BigDecimal.ZERO.equals(vd.getDebitAmount())){
					VoucherReport voucherReport = new VoucherReport(persistenceService,Integer.valueOf(voucher.getId().toString()),vd);
					if(billRegistermis!=null){
						voucherReport.setDepartment(billRegistermis.getEgDepartment());
					}
					voucherReportList.add(voucherReport);
				}
			}
		}
	}
	
	private String getUlbName(){
		SQLQuery query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
		List<String> result = query.list();
		if(result!=null)
			return result.get(0);
		return "";
	}

	public String exportPdf() throws JRException, IOException{
		populateVoucher();
		inputStream = reportHelper.exportPdf(inputStream, jasperpath, getParamMap(), voucherReportList);
	    return "PDF";
	}
	
	public String exportHtml() {
		populateVoucher();
	   inputStream = reportHelper.exportHtml(inputStream, jasperpath, getParamMap(), voucherReportList,"px");
	   return "HTML";
	}
	
	public String exportXls() throws JRException, IOException{
		populateVoucher();
		inputStream = reportHelper.exportXls(inputStream, jasperpath, getParamMap(), voucherReportList);
	    return "XLS";
	}

	protected Map<String, Object> getParamMap() {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("voucherNumber", getVoucherNumber());
		paramMap.put("voucherDate", getVoucherDate());
		paramMap.put("voucherDescription", getVoucherDescription());
		if(voucher!=null && voucher.getState()!=null){
			loadInboxHistoryData(inboxService.getStateById(voucher.getState().getId()),paramMap);
		}
		if(billRegistermis != null){
			paramMap.put("billDate", Constants.DDMMYYYYFORMAT2.format(billRegistermis.getEgBillregister().getBilldate()));
			paramMap.put("partyBillNumber", billRegistermis.getPartyBillNumber());
			paramMap.put("serviceOrder", billRegistermis.getNarration());
			paramMap.put("partyName", billRegistermis.getPayto());
			paramMap.put("billNumber", billRegistermis.getEgBillregister().getBillnumber());
			BigDecimal billamount = billRegistermis.getEgBillregister().getBillamount();
			String amountInFigures = billamount==null?" ":billamount.setScale(2).toPlainString();
			String amountInWords = billamount==null?" ":NumberToWord.convertToWord(billamount.toPlainString());
			paramMap.put("certificate", getText("ejv.report.text", new String[]{amountInFigures,amountInWords}));
		}
		paramMap.put("ulbName", getUlbName());
	
		return paramMap;
	}

	public Map<String,Object> getAccountDetails(final Integer detailtypeid,final Integer detailkeyid,Map<String,Object> tempMap) throws EGOVException{
		Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,detailtypeid);
		tempMap.put("detailtype", detailtype.getName());
		tempMap.put("detailtypeid", detailtype.getId());
		tempMap.put("detailkeyid", detailkeyid);
		
		EgovCommon common = new EgovCommon();
		common.setPersistenceService(persistenceService);
		EntityType entityType = common.getEntityType(detailtype,detailkeyid);
		tempMap.put(Constants.DETAILKEY,entityType.getName());
		tempMap.put(Constants.DETAILCODE,entityType.getCode());		
		return tempMap;
	}

	private String getVoucherNumber() {
		return voucher == null || voucher.getVoucherNumber() == null?"" : voucher.getVoucherNumber();
	}
	
	private String getVoucherDescription() {
		return voucher == null || voucher.getDescription() == null?"" : voucher.getDescription();
	}
	
	private String getVoucherDate() {
		return voucher == null || voucher.getVoucherDate() == null ?"" : DateUtils.getDefaultFormattedDate(voucher.getVoucherDate());
	}
	
	private void loadInboxHistoryData(State states, Map<String, Object> paramMap) throws EGOVRuntimeException {
		List<String> history = new ArrayList<String>();
		List<String> workFlowDate = new ArrayList<String>();
    	if (states != null) {
    	    List<State> stateHistory = states.getHistory();
    	    Collections.reverse(stateHistory);
    	    for (State state : stateHistory) {
	    		Position position = getStateUser(state);
	    		if(!"NEW".equalsIgnoreCase(state.getValue())){
	    			history.add(position.getDesigId().getDesignationName());
	    			workFlowDate.add(Constants.DDMMYYYYFORMAT2.format(state.getModifiedDate()));
	    		}
    	    }
        }
    	for (int i = 0; i<history.size();i++) {
    		paramMap.put("workFlow_"+i, history.get(i));
    		paramMap.put("workFlowDate_"+i, workFlowDate.get(i));
		}
    }
	
	private Position getStateUser(State state) {
    	if (state.getPrevious() != null)
    	    return state.getPrevious().getOwner();
    	else
    	    return inboxService.getPrimaryPositionForUser(state.getCreatedBy().getId(), state.getCreatedDate());
    }
	
	
	public void setInboxService(InboxService inboxService) {
		this.inboxService = inboxService;
	}
	
}

