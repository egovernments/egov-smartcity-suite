package org.egov.web.actions.budget;

import java.math.BigDecimal;

public class BudgetReportView {

	private String deptCode = "";
	private String functionCode = "";
	private String glCode = "";
	private String narration = "";
	private String reference;
	private BigDecimal amount;
	private BigDecimal appropriationAmount;
	private BigDecimal totalAmount;
	private String rowStyle; 
	private BigDecimal reProposalAmount;
	private BigDecimal beProposalAmount;
	private BigDecimal reRecomAmount;
	private BigDecimal beRecomAmount;
	
	private BigDecimal reProposalTotalAmount;
	private BigDecimal beProposalTotalAmount;
	private BigDecimal reRecomTotalAmount;
	private BigDecimal beRecomTotalAmount;
	
	
	public BigDecimal getReProposalTotalAmount() {
		return reProposalTotalAmount;
	}
	public void setReProposalTotalAmount(BigDecimal reProposalTotalAmount) {
		this.reProposalTotalAmount = reProposalTotalAmount;
	}
	public BigDecimal getBeProposalTotalAmount() {
		return beProposalTotalAmount;
	}
	public void setBeProposalTotalAmount(BigDecimal beProposalTotalAmount) {
		this.beProposalTotalAmount = beProposalTotalAmount;
	}
	public BigDecimal getReRecomTotalAmount() {
		return reRecomTotalAmount;
	}
	public void setReRecomTotalAmount(BigDecimal reRecomTotalAmount) {
		this.reRecomTotalAmount = reRecomTotalAmount;
	}
	public BigDecimal getBeRecomTotalAmount() {
		return beRecomTotalAmount;
	}
	public void setBeRecomTotalAmount(BigDecimal beRecomTotalAmount) {
		this.beRecomTotalAmount = beRecomTotalAmount;
	}
	public BigDecimal getReProposalAmount() {
		return reProposalAmount;
	}
	public void setReProposalAmount(BigDecimal reProposalAmount) {
		this.reProposalAmount = reProposalAmount;
	}
	public BigDecimal getBeProposalAmount() {
		return beProposalAmount;
	}
	public void setBeProposalAmount(BigDecimal beProposalAmount) {
		this.beProposalAmount = beProposalAmount;
	}
	public BigDecimal getReRecomAmount() {
		return reRecomAmount;
	}
	public void setReRecomAmount(BigDecimal reRecomAmount) {
		this.reRecomAmount = reRecomAmount;
	}
	public BigDecimal getBeRecomAmount() {
		return beRecomAmount;
	}
	public void setBeRecomAmount(BigDecimal beRecomAmount) {
		this.beRecomAmount = beRecomAmount;
	}
	private Integer deptId;
	private Long functionId;
	private String type = "";
	private String majorCode = "";
	private BigDecimal tempamount=BigDecimal.ZERO;
	private Long detailId;
	
	
	public BudgetReportView(final String glCode,final String narration,final  String reference,final  BigDecimal amount,BigDecimal appropriationAmount,BigDecimal totalAmount) {
		this.glCode = glCode;
		this.narration = narration;
		this.reference = reference;
		this.amount = amount;
		this.appropriationAmount = appropriationAmount;
		this.totalAmount = totalAmount;
	}
	public BudgetReportView(final String deptCode,final String functionCode,final String glCode,final String narration,final String reference,
			final BigDecimal amount,BigDecimal appropriationAmount,BigDecimal totalAmount,String rowstyle) {
		this.deptCode=deptCode;
		this.functionCode =functionCode;
		this.glCode = glCode;
		this.narration = narration;
		this.reference = reference;
		this.amount = amount;
		this.rowStyle=rowstyle;
		this.appropriationAmount = appropriationAmount;
		this.totalAmount = totalAmount;
	}
	public BudgetReportView(final String deptCode,final String functionCode,final String glCode,final String narration,final String reference,
			final BigDecimal reProposalAmount,BigDecimal reRecomAmount,BigDecimal beProposalAmount,BigDecimal beRecomAmount, String rowstyle) {
		this.deptCode=deptCode;
		this.functionCode =functionCode;
		this.glCode = glCode;
		this.narration = narration;
		this.reference = reference;
		this.beProposalAmount=beProposalAmount;
		this.reProposalAmount=reProposalAmount;
		this.beRecomAmount=beRecomAmount;
		this.reRecomAmount=reRecomAmount;
		this.rowStyle=rowstyle;
		
		
	}
	public BudgetReportView(Integer deptId,Long functionId,String type,String majorcode,BigDecimal tempamount,BigDecimal appropriationAmount,
			BigDecimal totalAmount){
		this.deptId = deptId;
		this.functionId = functionId;
		this.type = type;
		this.majorCode = majorcode;
		this.tempamount = tempamount;
		this.appropriationAmount = appropriationAmount;
		this.totalAmount = totalAmount;
	}
	public BudgetReportView()
	{
		
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(final String deptCode) {
		this.deptCode = deptCode;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(final String functionCode) {
		this.functionCode = functionCode;
	}
	
	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(final String glCode) {
		this.glCode = glCode;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(final String narration) {
		this.narration = narration;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(final String reference) {
		this.reference = reference;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public String getAmountAsString() {
		return format(amount);
	}
	private String format(BigDecimal value) {
		return value==null?"":value.setScale(2).toPlainString();
	}
	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}
	public String getRowStyle() {
		return rowStyle;
	}
	public void setRowStyle(final String rowStyle) {
		this.rowStyle = rowStyle;
	}
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	public Long getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMajorCode() {
		return majorCode;
	}
	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}
	public BigDecimal getTempamount() {
		return tempamount;
	}
	public void setTempamount(BigDecimal tempamount) {
		this.tempamount = tempamount;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((functionCode == null) ? 0 : functionCode.hashCode());
		result = prime * result + ((glCode == null) ? 0 : glCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BudgetReportView other = (BudgetReportView) obj;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (functionCode == null) {
			if (other.functionCode != null)
				return false;
		} else if (!functionCode.equals(other.functionCode))
			return false;
		if (glCode == null) {
			if (other.glCode != null)
				return false;
		} else if (!glCode.equals(other.glCode))
			return false;
		return true;
	}
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	public Long getDetailId() {
		return detailId;
	}
	public void setAppropriationAmount(BigDecimal appropriationAmount) {
		this.appropriationAmount = appropriationAmount;
	}
	public BigDecimal getAppropriationAmount() {
		return appropriationAmount;
	}
	public String getAppropriationAmountAsString() {
		return format(appropriationAmount);
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public String getTotalAmountAsString() {
		return format(totalAmount);
	}
	
}
