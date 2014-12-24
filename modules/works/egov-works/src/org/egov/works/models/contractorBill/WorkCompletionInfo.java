package org.egov.works.models.contractorBill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.infstr.models.State;
import org.egov.infstr.utils.DateUtils;
import org.egov.works.models.workorder.WorkOrderEstimate;

public class WorkCompletionInfo {

	private WorkOrderEstimate workOrderEstimate;
	private String mbNumbers;
	private Date workCommencedOnDate;
	private List<State> workflowHistory;
	private List<Map<String, Object>> mbHeaderMapList;
	private ContractorBillRegister contractorBillReg;
	private List<Map<String, Object>> deductionsMapList;
	private BigDecimal totalWorkValue;
	private BigDecimal netPayAmount;
	private Date prevBilldate;
	private String prevCCNumber;
	

	/**
	 * @param workOrderEstimate
	 */
	public WorkCompletionInfo(WorkOrderEstimate workOrderEstimate) {
		this.workOrderEstimate = workOrderEstimate;
	}

	public WorkCompletionInfo() {
		
	}
	
	public WorkCompletionInfo(ContractorBillRegister contractorBillReg, WorkOrderEstimate workOrderEstimate) {
		this.contractorBillReg = contractorBillReg;
		this.workOrderEstimate = workOrderEstimate;
	}
	
	/**
	 * @param contractorBillregister
	 * @param workOrderEstimate
	 */
	public WorkCompletionInfo(WorkOrderEstimate workOrderEstimate,String mbNumbers) {
		this.workOrderEstimate = workOrderEstimate;
		this.mbNumbers=mbNumbers;
	}
	
	
	/**
	 * @return name of work
	 */
	public String getWorkName() {
		return workOrderEstimate.getEstimate().getName();
	}

	/**
	 * @return estimate number
	 */
	public String getEstimateNo() {
		return workOrderEstimate.getEstimate().getEstimateNumber();
	}

	/**
	 * @return estimate amount
	 */
	public String getEstimateAmount() {
		return workOrderEstimate.getEstimate().getTotalAmount()
				.getFormattedString();
	}
	
	/**
	 * @return estimate amount value
	 */
	public Double getEstimateAmountValue() {
		return workOrderEstimate.getEstimate().getTotalAmount().getValue();
				
	}

	/**
	 * @return estimate name
	 */
	public String getEstimateName() {
		return workOrderEstimate.getEstimate().getName();
	}
	
	/**
	 * @return budgetheader
	 */
	public String getBudgetHeader() {
		return workOrderEstimate.getEstimate().getFinancialDetails()==null ? null :
				workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup() == null ? null :
				workOrderEstimate.getEstimate().getFinancialDetails().get(0).getBudgetGroup().getName();
	}

	/**
	 * @return appr no
	 */
	public String getApprNo() {
		return workOrderEstimate.getEstimate().getBudgetApprNo();
	}

	/**
	 * @return project code
	 */
	public String getProjectCode() {
		return workOrderEstimate.getEstimate().getProjectCode()==null? null : workOrderEstimate.getEstimate().getProjectCode().getCode();
	}

	/**
	 * @return contractor name
	 */
	public String getContractorName() {
		return workOrderEstimate.getWorkOrder().getContractor().getName();
	}

	/**
	 * @return list of mbno as string
	 */
	public String getAllMBNO() {
		return mbNumbers;
	}

	/**
	 * @return work commenced date
	 */
	public Date getWorkCommencedOn() {
		return workCommencedOnDate;
	}
	
	/**
	 * @set the work commeced date
	 */
	public void setWorkCommencedOn(Date workCommencedOnDate){
		this.workCommencedOnDate=workCommencedOnDate;
	}
	
	/**
	 * @return work completed date
	 */
	public Date getWorkCompletedDate() {
		return workOrderEstimate.getWorkCompletionDate();
	}
	
	/**
	 * @set the history
	 */
	public void setWorkflowHistory(List<State> workflowHistory){
		this.workflowHistory=workflowHistory;
	}
	
	/**
	 * @return workflow history
	 */
	public List<State> getWorkflowHistory() {
		return workflowHistory;
	}

	public WorkOrderEstimate getWorkOrderEstimate() {
		return workOrderEstimate;
	}

	public String getCCNumber()
	{
		return contractorBillReg.getContractCertificateNumber();
	}
	
	public String getCCNumberAndDate()
	{
		if(contractorBillReg.getPartbillNo()==1)
		{
			return "";
		}
		else
		{
			if(getPrevCCNumber()==null)
				return DateUtils.getFormattedDate(getPrevBilldate(),"dd/MM/yyyy");
			else
				return getPrevCCNumber() + " , " + DateUtils.getFormattedDate(getPrevBilldate(),"dd/MM/yyyy");
		}
	}
	
	public BigDecimal getBillAmount()
	{
		return contractorBillReg.getBillamount();
	}

	public String getBillDate()
	{
		return DateUtils.getFormattedDate(contractorBillReg.getBilldate(),"dd/MM/yyyy");
	}
	
	public Integer getPartBillNo()
	{
		return contractorBillReg.getPartbillNo();
	}
	public BigDecimal getTotalWorkValue() {
		return totalWorkValue;
	}
	public void setTotalWorkValue(BigDecimal totalWorkValue) {
		this.totalWorkValue = totalWorkValue;
	}
	public List<Map<String, Object>> getDeductionsMapList() {
		return deductionsMapList;
	}
	public void setDeductionsMapList(List<Map<String, Object>> deductionsMapList) {
		this.deductionsMapList = deductionsMapList;
	}
	public ContractorBillRegister getContractorBillReg() {
		return contractorBillReg;
	}
	public void setContractorBillReg(ContractorBillRegister contractorBillReg) {
		this.contractorBillReg = contractorBillReg;
	}
	public List<Map<String, Object>> getMbHeaderMapList() {
		return mbHeaderMapList;
	}
	public void setMbHeaderMapList(List<Map<String, Object>> mbHeaderMapList) {
		this.mbHeaderMapList = mbHeaderMapList;
	}

	public Date getPrevBilldate() {
		return prevBilldate;
	}

	public void setPrevBilldate(Date prevBilldate) {
		this.prevBilldate = prevBilldate;
	}

	public String getPrevCCNumber() {
		return prevCCNumber;
	}

	public void setPrevCCNumber(String prevCCNumber) {
		this.prevCCNumber = prevCCNumber;
	}

	public BigDecimal getNetPayAmount() {
		return netPayAmount;
	}

	public void setNetPayAmount(BigDecimal netPayAmount) {
		this.netPayAmount = netPayAmount;
	}
}
