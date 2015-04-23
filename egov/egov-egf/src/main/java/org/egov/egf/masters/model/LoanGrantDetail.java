/**
 * 
 */
package org.egov.egf.masters.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.infstr.models.BaseModel;

/**
 * @author mani
 * Is used to store the funding patterns
 */
public class LoanGrantDetail extends BaseModel {
	private static final long	serialVersionUID	= -5584173572438673662L;
	
	final static Logger LOGGER=Logger.getLogger(LoanGrantReceiptDetail.class);
	private LoanGrantHeader header;
	private FundingAgency fundingAgency;
	private BigDecimal loanAmount;
	private BigDecimal grantAmount;
	private Double percentage;
	private String agencySchemeNo;//This field will store no and date
	private String councilResNo;//This field will store no and date
	private String loanSanctionNo;//This field will store no and date
	private Date agreementDate;
	private String commOrderNo;
	private String docId;
	private String patternType;// to type of pattern used 1.sactioned 2.unsanctioned 3.revised
	public LoanGrantDetail() { }
	public LoanGrantDetail(String patternType) {
		this.patternType=patternType;
	}
	public LoanGrantHeader getHeader() {
		return header;
	}
	public void setHeader(LoanGrantHeader header) {
		this.header = header;
	}
	public FundingAgency getFundingAgency() {
		return fundingAgency;
	}
	public void setFundingAgency(FundingAgency fundingAgency) {
		this.fundingAgency = fundingAgency;
	}
	public BigDecimal getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}
	public BigDecimal getGrantAmount() {
		return grantAmount;
	}
	public void setGrantAmount(BigDecimal grantAmount) {
		this.grantAmount = grantAmount;
	}
	
	public String getAgencySchemeNo() {
		return agencySchemeNo;
	}
	public void setAgencySchemeNo(String agencySchemeNo) {
		this.agencySchemeNo = agencySchemeNo;
	}
	public String getCouncilResNo() {
		return councilResNo;
	}
	public void setCouncilResNo(String councilResNo) {
		this.councilResNo = councilResNo;
	}
	public String getLoanSanctionNo() {
		return loanSanctionNo;
	}
	public void setLoanSanctionNo(String loanSanctionNo) {
		this.loanSanctionNo = loanSanctionNo;
	}
	public Date getAgreementDate() {
		return agreementDate;
	}
	public void setAgreementDate(Date agreementDate) {
		this.agreementDate = agreementDate;
	}
	public String getCommOrderNo() {
		return commOrderNo;
	}
	public void setCommOrderNo(String commOrderNo) {
		this.commOrderNo = commOrderNo;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
		public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	public String getPatternType() {
		return patternType;
	}
	public void setPatternType(String patternType) {
		this.patternType = patternType;
	}
	
}
