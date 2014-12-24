/*
 * Created on Dec 21, 2005
 * @author Sumit
 */
package com.exilant.eGov.src.reports;

import org.apache.log4j.Logger;

public class GeneralLedgerReportBean
{
	private static final Logger LOGGER = Logger.getLogger(GeneralLedgerReportBean.class);
	private String glCode1;
	private String glCode2;
	private String snapShotDateTime;
	private String forRevEntry;
	private String fund_id;
	private String fundSource_id;
	private String startDate;
	private String endDate;
	private String totalCount;
	private String isConfirmedCount;
	private String reportType;
	private String forRunningBalance;
	private String boundary;
	private String ulbName;
	private String mode;
	private String accEntityKey;
	 private String accEntityId;
	 
	
	private String rcptVchrDate;
	private String rcptVchrNo;
	private String rcptAccCode;
	private String rcptFuncCode;
	private String rcptBgtCode;
	private String rcptParticulars;
	private String rcptcashInHandAmt;
	private String rcptChqInHandAmt;
	private String rcptSrcOfFinance;
	private String pmtVchrDate;
	private String pmtVchrNo;
	private String pmtAccCode;
	private String pmtFuncCode;
	private String pmtBgtCode;
	private String pmtParticulars;
	private String pmtCashInHandAmt;
	private String pmtChqInHandAmt;
	private String pmtSrcOfFinance;
	private String CGN;	
	private String Cheque;
    private String accountCode;
    private String fundName;
	
    private String netRcptAmount;
    private String netPymntAmount;
    
    private String departmentId;
    private String functionaryId;
    private String fieldId;
	public	String functionCodeId;
	public 	String  functionCode;
	
	public GeneralLedgerReportBean(String str)
    {
		this.glCode1 = "";
		this.glCode2 = "";
		this.snapShotDateTime = "";
		this.forRevEntry = "";
		this.fund_id = "";
		this.fundSource_id = "";
		this.startDate = "";
		this.endDate = "";
		this.totalCount="";
		this.isConfirmedCount="";
		this.reportType="";
		this.forRunningBalance="";
		this.boundary="";
		this.ulbName="";
		this.mode = "";
		this.accEntityKey="";
		this.accEntityId="";

		 this.rcptVchrDate=str;
		 this.rcptVchrNo=str; 
		 this.rcptAccCode=str;
		 this.rcptFuncCode=str;
		 this.rcptBgtCode=str;
		 this.rcptParticulars=str;
		 this.rcptcashInHandAmt=str;
		 this.rcptChqInHandAmt=str;
		 this.rcptSrcOfFinance=str;
		 this.pmtVchrDate=str;
		 this.pmtVchrNo=str;
		 this.pmtAccCode=str;
		 this.pmtFuncCode=str;
		 this.pmtBgtCode=str;
		 this.pmtParticulars=str;
		 this.pmtCashInHandAmt=str;
		 this.pmtChqInHandAmt=str;
		 this.pmtSrcOfFinance=str;
		 this.CGN="";
		 this.Cheque=str;
         this.accountCode="";
         this.fundName="";
         this.netRcptAmount=str;
         this.netPymntAmount=str;
         this.functionCodeId = null;
 		 this.functionCode = null;
		
	}
	public GeneralLedgerReportBean()
    {
		this.glCode1 = "";
		this.glCode2 = "";
		this.snapShotDateTime = "";
		this.forRevEntry = "";
		this.fund_id = "";
		this.fundSource_id = "";
		this.startDate = "";
		this.endDate = "";
		this.totalCount="";
		this.isConfirmedCount="";
		this.reportType="";
		this.forRunningBalance="";
		this.boundary="";
		this.ulbName="";
		this.accEntityKey="";
		this.accEntityId="";
		

		 this.rcptVchrDate="&nbsp;";
		 this.rcptVchrNo="&nbsp;";
		 this.rcptAccCode="&nbsp;";
		 this.rcptFuncCode="&nbsp;";
		 this.rcptBgtCode="&nbsp;";
		 this.rcptParticulars="&nbsp;";
		 this.rcptcashInHandAmt="&nbsp;";
		 this.rcptChqInHandAmt="&nbsp;";
		 this.rcptSrcOfFinance="&nbsp;";
		 this.pmtVchrDate="&nbsp;";
		 this.pmtVchrNo="&nbsp;";
		 this.pmtAccCode="&nbsp;";
		 this.pmtFuncCode="&nbsp;";
		 this.pmtBgtCode="&nbsp;";
		 this.pmtParticulars="&nbsp;";
		 this.pmtCashInHandAmt="&nbsp;";
		 this.pmtChqInHandAmt="&nbsp;";
		 this.pmtSrcOfFinance="&nbsp;";
		 this.CGN="";
		 this.Cheque="";
		 this.mode="";
         this.accountCode="";
         this.fundName="";
         this.netRcptAmount="&nbsp;";
         this.netPymntAmount="&nbsp;";
		
	}

	/**
	 * @return Returns the endDate.
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	/**
	 * @return Returns the accEntityId.
	 */
	public String getAccEntityId() {
		return accEntityId;
	}
	/**
	 * @param accEntityId The accEntityId to set.
	 */
	public void setAccEntityId(String accEntityId) {
		this.accEntityId = accEntityId;
	}
	
	/**
	 * @return Returns the accEntityKey.
	 */
	public String getAccEntityKey() {
		return accEntityKey;
	}
	/**
	 * @param accEntityKey The accEntityKey to set.
	 */
	public void setAccEntityKey(String accEntityKey) {
		this.accEntityKey = accEntityKey;
	}
	
	
	
	
	
	/**
	 * @return Returns the forRevEntry.
	 */
	public String getForRevEntry() {
		return forRevEntry;
	}
	/**
	 * @param forRevEntry The forRevEntry to set.
	 */
	public void setForRevEntry(String forRevEntry) {
		this.forRevEntry = forRevEntry;
	}
	/**
	 * @return Returns the fund_id.
	 */
	public String getFund_id() {
		return fund_id;
	}
	/**
	 * @param fund_id The fund_id to set.
	 */
	public void setFund_id(String fund_id) {
		this.fund_id = fund_id;
	}
	/**
	 * @return Returns the fundSource_id.
	 */
	public String getFundSource_id() {
		return fundSource_id;
	}
	/**
	 * @param fundSource_id The fundSource_id to set.
	 */
	public void setFundSource_id(String fundSource_id) {
		this.fundSource_id = fundSource_id;
	}
	/**
	 * @return Returns the glCode1.
	 */
	public String getGlCode1() {
		return glCode1;
	}
	/**
	 * @param glCode1 The glCode1 to set.
	 */
	public void setGlCode1(String glCode1) {
		this.glCode1 = glCode1;
	}
	/**
	 * @return Returns the glCode2.
	 */
	public String getGlCode2() {
		return glCode2;
	}
	/**
	 * @param glCode2 The glCode2 to set.
	 */
	public void setGlCode2(String glCode2) {
		this.glCode2 = glCode2;
	}
	/**
	 * @return Returns the snapShotDateTime.
	 */
	public String getSnapShotDateTime() {
		return snapShotDateTime;
	}
	/**
	 * @param snapShotDateTime The snapShotDateTime to set.
	 */
	public void setSnapShotDateTime(String snapShotDateTime) {
		this.snapShotDateTime = snapShotDateTime;
	}
	/**
	 * @return Returns the startDate.
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return Returns the totalCount.
	 */
	public String getTotalCount() {
		return totalCount;
	}
	/**
	 * @param totalCount The totalCount to set.
	 */
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * @return Returns the isConfirmedCount.
	 */
	public String getIsConfirmedCount() {
		return isConfirmedCount;
	}
	/**
	 * @param isConfirmedCount The isConfirmedCount to set.
	 */
	public void setIsConfirmedCount(String isConfirmedCount) {
		this.isConfirmedCount = isConfirmedCount;
	}
	/**
	 * @return Returns the cGN.
	 */

	public void setReportType(String rptType) {
		this.reportType = rptType;

	}
	/**
	 * @param chequeInHand  to set.
	 */
	public String getReportType() {
		return reportType;
	}
	
	public void setUlbName(String ulbName) {
		this.ulbName = ulbName;

	}
	/**
	 * @param chequeInHand  to set.
	 */
	public String getUlbName() {
		return ulbName;
	}
	


	 public void setForRunningBalance(String forRunningBalance) {
	 		this.forRunningBalance = forRunningBalance;

	 	}

	 	public String getForRunningBalance() {
	 		return forRunningBalance;
	 	}
	 	/**
	 	 * 
	 	 * @param rptType
	 	 */
	 	public void setRcptVchrNo(String vchrNo) {
			this.rcptVchrNo = vchrNo;
	 	}
	 	
	 	public String getRcptVchrNo() {
			return rcptVchrNo;
		}
	 
	 	public void setRcptVchrDate(String rcptVchrDate) {
			this.rcptVchrDate = rcptVchrDate;
	 	}
	 	public String getRcptVchrDate() {
			return rcptVchrDate;
		}
	 	public void setRcptAccCode(String rcptAccCode) {
			this.rcptAccCode = rcptAccCode;
	 	}
	 	public String getRcptAccCode() {
			return rcptAccCode;
		}
	 	public void setRcptFuncCode(String rcptFuncCode) {
			this.rcptFuncCode = rcptFuncCode;
	 	}
	 	public String getRcptFuncCode() {
			return rcptFuncCode;
		}
	 	public void setRcptBgtCode(String rcptBgtCode) {
			this.rcptBgtCode = rcptBgtCode;
	 	}
	 	public String getRcptBgtCode() {
			return rcptBgtCode;
		}public void setRcptParticulars(String rcptParticulars) {
			this.rcptParticulars = rcptParticulars;
	 	}
	 	public String getRcptParticulars() {
			return rcptParticulars;
		}
	 	public void setRcptcashInHandAmt(String rcptcashInHandAmt) {
			this.rcptcashInHandAmt = rcptcashInHandAmt;
	 	}
	 	public String getRcptcashInHandAmt() {
			return rcptcashInHandAmt;
		}public void setRcptChqInHandAmt(String rcptChqInHandAmt) {
			this.rcptChqInHandAmt = rcptChqInHandAmt;
	 	}
	 	public String getRcptChqInHandAmt() {
			return rcptChqInHandAmt;
		}public void setRcptSrcOfFinance(String rcptSrcOfFinance) {
			this.rcptSrcOfFinance = rcptSrcOfFinance;
	 	}
	 	public String getRcptSrcOfFinance() {
			return rcptSrcOfFinance;
		}public void setpmtVchrDate(String pmtVchrDate) {
			this.pmtVchrDate = pmtVchrDate;
	 	}
	 	public String getpmtVchrDate() {
			return pmtVchrDate;
		}
	 	public void setPmtVchrNo(String pmtVchrNo) {
			this.pmtVchrNo = pmtVchrNo;
	 	}
	 	public String getPmtVchrNo() {
			return pmtVchrNo;
		}public void setPmtAccCode(String pmtAccCode) {
			this.pmtAccCode = pmtAccCode;
	 	}
	 	public String getPmtAccCode() {
			return pmtAccCode;
		}
	 	public void setPmtFuncCode(String pmtFuncCode) {
			this.pmtFuncCode = pmtFuncCode;
	 	}
	 	public String getPmtFuncCode() {
			return pmtFuncCode;
		}public void setPmtBgtCode(String pmtBgtCode) {
			this.pmtBgtCode = pmtBgtCode;
	 	}
	 	public String getPmtBgtCode() {
			return pmtBgtCode;
		}public void setPmtParticulars(String pmtParticulars) {
			this.pmtParticulars = pmtParticulars;
	 	}
	 	public String getPmtParticulars() {
			return pmtParticulars;
		}public void setPmtCashInHandAmt(String pmtCashInHandAmt) {
			this.pmtCashInHandAmt = pmtCashInHandAmt;
	 	}
	 	public String getPmtCashInHandAmt() {
			return pmtCashInHandAmt;
		}public void setPmtChqInHandAmt(String pmtChqInHandAmt) {
			this.pmtChqInHandAmt = pmtChqInHandAmt;
	 	}
	 	public String getPmtChqInHandAmt() {
			return pmtChqInHandAmt;
		}
	 	public void setPmtSrcOfFinance(String pmtSrcOfFinance) {
			this.pmtSrcOfFinance = pmtSrcOfFinance;
	 	}
	 	public String getPmtSrcOfFinance() {
			return pmtSrcOfFinance;
		}
	 	public String getCGN() {
			return CGN;
		}
		/**
		 * @param cgn The cGN to set.
		 */
		public void setCGN(String cgn) {
			CGN = cgn;
		}
		/**
		 * @return Returns the endDate.
		 */
		public String getBoundary() {
			return boundary;
		}
		/**
		 * @param endDate The endDate to set.
		 */
		public void setBoundary(String boundary) {
			LOGGER.debug("after set boundary:"+boundary);
			this.boundary = boundary;
			LOGGER.debug("after set this.boundary:"+this.boundary);
		}
	 	
	/**
	 * @return Returns the cheque.
	 */
	public String getCheque() {
		return Cheque;
	}
	/**
	 * @param cheque The cheque to set.
	 */
	public void setCheque(String cheque) {
		Cheque = cheque;
	}
	/**
	 * @return Returns the mode.
	 */
	public String getMode() {
		LOGGER.debug("111111111111111111111111after get mode:" +mode);
		return this.mode;
	}
	/**
	 * @param mode The mode to set.
	 */
	public void setMode(String mode) {
		LOGGER.debug("111111111111111111111111after get mode:" +mode);
		this.mode = mode;
		LOGGER.debug("111111111111111111111111after get mode:" +this.mode);
	}
    /**
     * @return Returns the accountCode.
     */
    public String getAccountCode()
    {
        return accountCode;
    }
    /**
     * @param accountCode The accountCode to set.
     */
    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }
    /**
     * @return Returns the fundName.
     */
    public String getFundName()
    {
        return fundName;
    }
    /**
     * @param fundName The fundName to set.
     */
    public void setFundName(String fundName)
    {
        this.fundName = fundName;
    }
	/**
	 * @return the netPymntAmount
	 */
	public String getNetPymntAmount()
	{
		return netPymntAmount;
	}
	/**
	 * @param netPymntAmount the netPymntAmount to set
	 */
	public void setNetPymntAmount(String netPymntAmount)
	{
		this.netPymntAmount = netPymntAmount;
	}
	/**
	 * @return the netRcptAmount
	 */
	public String getNetRcptAmount()
	{
		return netRcptAmount;
	}
	/**
	 * @param netRcptAmount the netRcptAmount to set
	 */
	public void setNetRcptAmount(String netRcptAmount)
	{
		this.netRcptAmount = netRcptAmount;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getFunctionaryId() {
		return functionaryId;
	}
	public void setFunctionaryId(String functionaryId) {
		this.functionaryId = functionaryId;
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public String getFunctionCodeId() {
		return functionCodeId;
	}
	public void setFunctionCodeId(String functionCodeId) {
		this.functionCodeId = functionCodeId;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

}
