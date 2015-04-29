package org.egov.works.web.actions.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class CommonDrillDownReportBean {

	private Long estimateId;
	private String estNumber;
	private String estName;
	private Date adminSanctionDate;
	private Date woDate;
	private String workOrderNo;
	private Date siteHandedDate;
	private Date workCommencedDate;
	private BigDecimal woValue = new BigDecimal(0.00);
	private String contractorName;
	private int contractPeriod;
	private Date expectedCompletionDate;
	private Date completionDate;
	private BigDecimal paymentReleasedAmount = new BigDecimal(0.00);
	private Long woId;
	private BigDecimal milestonePerc;
	private BigDecimal paymentReleasedPerc;
	private BigDecimal estAmount = new BigDecimal(0.00);
	private BigDecimal spillOverWorkValue = new BigDecimal(0.00);
	private Date estCreatedDate;
	private Date estDate;
	private String jurisdiction;
	private String wardName;
	private String estPreparedBy;
	private Date estApprovedDate;
	private String tenderNegotiationNum;
	private String typeOfWork;
	private String subTypeOfWork;
	private Date tenderNegotiationDate;
	private Date worksPackageDate;
	private Long worksPackageId;
	private String worksPackageNo;
	private Date tenderDocReleaseDate;
	private String tenderType;
	private String finalBillStatus = "In Progress";
	private BigDecimal tenderValueAfterNegotiation;
	private BigDecimal negotiationPerc;
	private BigDecimal savings;
	private Date workOrderApprovedDate;
	
	public String getJurisdiction() {
		return jurisdiction;
	}
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}
	public Date getEstDate() {
		return estDate;
	}
	public void setEstDate(Date estDate) {
		this.estDate = estDate;
	}
	public BigDecimal getEstAmount() {
		return estAmount;
	}
	public void setEstAmount(BigDecimal estAmount) {
		this.estAmount = estAmount;
	}
	public BigDecimal getSpillOverWorkValue() {
		return spillOverWorkValue;
	}
	public void setSpillOverWorkValue(BigDecimal spillOverWorkValue) {
		this.spillOverWorkValue = spillOverWorkValue;
	}
	public Date getEstCreatedDate() {
		return estCreatedDate;
	}
	public void setEstCreatedDate(Date estCreatedDate) {
		this.estCreatedDate = estCreatedDate;
	}
	public Long getWoId() {
		return woId;
	}
	public void setWoId(Long woId) {
		this.woId = woId;
	}
	public String getEstNumber() {
		return estNumber;
	}
	public void setEstNumber(String estNumber) {
		this.estNumber = estNumber;
	}
	public String getEstName() {
		return estName;
	}
	public void setEstName(String estName) {
		this.estName = estName;
	}
	public Date getAdminSanctionDate() {
		return adminSanctionDate;
	}
	public void setAdminSanctionDate(Date adminSanctionDate) {
		this.adminSanctionDate = adminSanctionDate;
	}
	public Date getWoDate() {
		return woDate;
	}
	public void setWoDate(Date woDate) {
		this.woDate = woDate;
	}
	public Date getSiteHandedDate() {
		return siteHandedDate;
	}
	public void setSiteHandedDate(Date siteHandedDate) {
		this.siteHandedDate = siteHandedDate;
	}
	public Date getWorkCommencedDate() {
		return workCommencedDate;
	}
	public void setWorkCommencedDate(Date workCommencedDate) {
		this.workCommencedDate = workCommencedDate;
	}
	public BigDecimal getWoValue() {
		return woValue;
	}
	public void setWoValue(BigDecimal woValue) {
		this.woValue = woValue;
	}
	public String getContractorName() {
		return contractorName;
	}
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}
	public int getContractPeriod() {
		return contractPeriod;
	}
	public void setContractPeriod(int contractPeriod) {
		this.contractPeriod = contractPeriod;
	}
	public Date getExpectedCompletionDate() {
		return expectedCompletionDate;
	}
	public void setExpectedCompletionDate(Date expectedCompletionDate) {
		this.expectedCompletionDate = expectedCompletionDate;
	}
	public Date getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	public BigDecimal getPaymentReleasedAmount() {
		return paymentReleasedAmount;
	}
	public void setPaymentReleasedAmount(BigDecimal paymentReleasedAmount) {
		this.paymentReleasedAmount = paymentReleasedAmount;
	}
	public BigDecimal getMilestonePerc() {
		return milestonePerc;
	}
	public void setMilestonePerc(BigDecimal milestonePerc) {
		this.milestonePerc = milestonePerc;
	}
	public BigDecimal getPaymentReleasedPerc() {
		return paymentReleasedPerc;
	}
	public void setPaymentReleasedPerc(BigDecimal paymentReleasedPerc) {
		this.paymentReleasedPerc = paymentReleasedPerc;
	}
	public Long getEstimateId() {
		return estimateId;
	}
	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public String getEstPreparedBy() {
		return estPreparedBy;
	}
	public void setEstPreparedBy(String estPreparedBy) {
		this.estPreparedBy = estPreparedBy;
	}
	public Date getEstApprovedDate() {
		return estApprovedDate;
	}
	public void setEstApprovedDate(Date estApprovedDate) {
		this.estApprovedDate = estApprovedDate;
	}
	public String getTenderNegotiationNum() {
		return tenderNegotiationNum;
	}
	public void setTenderNegotiationNum(String tenderNegotiationNum) {
		this.tenderNegotiationNum = tenderNegotiationNum;
	}
	public Date getTenderNegotiationDate() {
		return tenderNegotiationDate;
	}
	public void setTenderNegotiationDate(Date tenderNegotiationDate) {
		this.tenderNegotiationDate = tenderNegotiationDate;
	}
	public Date getWorksPackageDate() {
		return worksPackageDate;
	}
	public void setWorksPackageDate(Date worksPackageDate) {
		this.worksPackageDate = worksPackageDate;
	}
	public String getTenderType() {
		return tenderType;
	}
	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}
	public BigDecimal getTenderValueAfterNegotiation() {
		return tenderValueAfterNegotiation;
	}
	public void setTenderValueAfterNegotiation(
			BigDecimal tenderValueAfterNegotiation) {
		this.tenderValueAfterNegotiation = tenderValueAfterNegotiation;
	}
	public BigDecimal getNegotiationPerc() {
		return negotiationPerc;
	}
	public void setNegotiationPerc(BigDecimal negotiationPerc) {
		this.negotiationPerc = negotiationPerc;
	}
	public String getWorksPackageNo() {
		return worksPackageNo;
	}
	public void setWorksPackageNo(String worksPackageNo) {
		this.worksPackageNo = worksPackageNo;
	}
	public Date getTenderDocReleaseDate() {
		return tenderDocReleaseDate;
	}
	public void setTenderDocReleaseDate(Date tenderDocReleaseDate) {
		this.tenderDocReleaseDate = tenderDocReleaseDate;
	}
	public String getFinalBillStatus() {
		return finalBillStatus;
	}
	public void setFinalBillStatus(String finalBillStatus) {
		this.finalBillStatus = finalBillStatus;
	}
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public Long getWorksPackageId() {
		return worksPackageId;
	}
	public void setWorksPackageId(Long worksPackageId) {
		this.worksPackageId = worksPackageId;
	}
	public String getTypeOfWork() {
		return typeOfWork;
	}
	public String getSubTypeOfWork() {
		return subTypeOfWork;
	}
	public void setTypeOfWork(String typeOfWork) {
		this.typeOfWork = typeOfWork;
	}
	public void setSubTypeOfWork(String subTypeOfWork) {
		this.subTypeOfWork = subTypeOfWork;
	}
	public BigDecimal getSavings() {
		return savings;
	}
	public void setSavings(BigDecimal savings) {
		this.savings = savings;
	}
	public BigDecimal getRoundedOffEstAmount() {
		return estAmount==null?null:(estAmount).setScale(0, RoundingMode.HALF_UP);
	}
	public Date getWorkOrderApprovedDate() {
		return workOrderApprovedDate;
	}
	public void setWorkOrderApprovedDate(Date workOrderApprovedDate) {
		this.workOrderApprovedDate = workOrderApprovedDate;
	}
	
}
