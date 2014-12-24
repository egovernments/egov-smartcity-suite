package org.egov.billsaccounting.client;

import org.apache.struts.action.ActionForm;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

public class WorksBillForm extends ActionForm{
	public	String expenditure_Type;
	public String billId;
	public 	String billDate;
	public	String bill_Amount;
	public	String bill_Type;
	public String adjustmentAmount;
	public String 	passedAmount;
	public	String codeList; 
	public	String worksName;  
	public	String codeName;     
	public	String dept;
	public String userName;  
	public 	String CSId;
	public 	String CSName;          
	public	String MBrefNo;     
	public	String debitTotal;
	public String functionaryId;
	public	String creditTotal;
	public	String totalAmount;   
	public String advanceAmount;
	public String buttonType;
	public String billNo;
	public String narration;
	public String voucherHeader_narration;
	public	String[] deb_cv_fromFunctionCodeId=null;
	public 	String[] deb_function_code=null;
	public	String[] deb_chartOfAccounts_glCode=null;
	public	String[] deb_chartOfAccounts_glCodeId=null;
	public	String[] deb_chartOfAccounts_name=null;
	public	String[] debitAmount=null;
	public	String[] ded_cv_fromFunctionCodeId=null;
	public	String[] ded_function_code=null;
	public 	String[] tds_code=null;
	public	String[] ded_chartOfAccounts_glCode=null;
	public	String[] ded_chartOfAccounts_glCodeId=null;
	public	String[] ded_chartOfAccounts_name=null; 
	public	String[] creditAmount=null;
	public  String net_cv_fromFunctionCodeId;
	//public	String[] net_function_code;
	public  String net_chartOfAccounts_glCode;
	public  String net_chartOfAccounts_name;
	public	String net_Amount;
	public int userId; 
	public List tdsList;
	public String VoucherHeader_voucherDate;
	public String	voucherHeader_voucherNumber;
	public String totalWorkOrder;
	public String totalBilledAmount;
	public String sanctionNo;
	public String subPartyTYpe;
	public String workType;
	public String workSubType;
	public String workOrderDate;
	
	public String billAprvalDate;

		/**
	 * @return the sanctionNo
	 */
	public String getSanctionNo() {
		return sanctionNo;
	}
	/**
	 * @param sanctionNo the sanctionNo to set
	 */
	public void setSanctionNo(String sanctionNo) {
		this.sanctionNo = sanctionNo;
	}
		/**
	 * @return the voucherHeader_voucherDate
	 */
	public String getVoucherHeader_voucherDate() {
		return VoucherHeader_voucherDate;
	}
	/**
	 * @param voucherHeader_voucherDate the voucherHeader_voucherDate to set
	 */
	public void setVoucherHeader_voucherDate(String voucherHeader_voucherDate) {
		VoucherHeader_voucherDate = voucherHeader_voucherDate;
	}
	/**
	 * @return the voucherHeader_voucherNumber
	 */
	public String getVoucherHeader_voucherNumber() {
		return voucherHeader_voucherNumber;
	}
	/**
	 * @param voucherHeader_voucherNumber the voucherHeader_voucherNumber to set
	 */
	public void setVoucherHeader_voucherNumber(String voucherHeader_voucherNumber) {
		this.voucherHeader_voucherNumber = voucherHeader_voucherNumber;
	}
		public void reset(ActionMapping mapping,HttpServletRequest req) {
		this.expenditure_Type ="";
		this.billDate = "";
		this.bill_Amount = "";
		this.bill_Type = "";
		this.adjustmentAmount = "";
		this.passedAmount = "";
		this.codeList = "";
		this.worksName = "";
		this.codeName = "";
		this.dept = "";
		this.CSId = "";
		this.CSName = "";
		this.MBrefNo = "";
		this.debitTotal = "";
		this.creditTotal = "";
		this.totalAmount = "";
		this.advanceAmount = "";
		this.buttonType = "";
		this.billNo = "";
		this.deb_cv_fromFunctionCodeId = null;
		this.deb_function_code = null;
		this.deb_chartOfAccounts_glCode = null;
		this.deb_chartOfAccounts_glCodeId = null;
		this.deb_chartOfAccounts_name = null;
		this.debitAmount = null;
		this.ded_cv_fromFunctionCodeId = null;
		this.ded_function_code = null;
		this.tds_code = null;
		this.ded_chartOfAccounts_glCode = null;
		this.ded_chartOfAccounts_glCodeId = null;
		this.ded_chartOfAccounts_name = null;
		this.creditAmount = null;
		this.net_cv_fromFunctionCodeId = null;
		this.net_chartOfAccounts_glCode = null;
		this.net_chartOfAccounts_name = null;
		this.net_Amount = null;
		this.tdsList = null;
		this.totalWorkOrder="";
		this.totalBilledAmount="";
		this.sanctionNo="";
		this.narration="";
		this.voucherHeader_narration="";  
		
		
	}
		/**
	 * @return the tdsList
	 */
	public List getTdsList() {
		return tdsList;
	}
	/**
	 * @param tdsList the tdsList to set
	 */
	public void setTdsList(List tdsList) {
		this.tdsList = tdsList;
	}
		/**
	 * @return the net_Amount
	 */
	public String getNet_Amount() {
		return net_Amount;
	}
	/**
	 * @return the net_chartOfAccounts_glCode
	 */
	public String getNet_chartOfAccounts_glCode() {
		return net_chartOfAccounts_glCode;
	}
	/**
	 * @return the net_chartOfAccounts_name
	 */
	public String getNet_chartOfAccounts_name() {
		return net_chartOfAccounts_name;
	}
	/**
	 * @return the net_cv_fromFunctionCodeId
	 */
	public String getNet_cv_fromFunctionCodeId() {
		return net_cv_fromFunctionCodeId;
	}
		/**
	 * @param net_Amount the net_Amount to set
	 */
	public void setNet_Amount(String net_Amount) {
		this.net_Amount = net_Amount;
	}
	/**
	 * @param net_chartOfAccounts_glCode the net_chartOfAccounts_glCode to set
	 */
	public void setNet_chartOfAccounts_glCode(String net_chartOfAccounts_glCode) {
		this.net_chartOfAccounts_glCode = net_chartOfAccounts_glCode;
	}
	/**
	 * @param net_chartOfAccounts_name the net_chartOfAccounts_name to set
	 */
	public void setNet_chartOfAccounts_name(String net_chartOfAccounts_name) {
		this.net_chartOfAccounts_name = net_chartOfAccounts_name;
	}
	/**
	 * @param net_cv_fromFunctionCodeId the net_cv_fromFunctionCodeId to set
	 */
	public void setNet_cv_fromFunctionCodeId(String net_cv_fromFunctionCodeId) {
		this.net_cv_fromFunctionCodeId = net_cv_fromFunctionCodeId;
	}
		/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
		/**
	 * @return the worksName
	 */
	public String getWorksName() {
		return worksName;
	}
	/**
	 * @param worksName the worksName to set
	 */
	public void setWorksName(String worksName) {
		this.worksName = worksName;
	}
		/**
	 * @return the cSId
	 */
	public String getCSId() {
		return CSId;
	}
	/**
	 * @param id the cSId to set
	 */
	public void setCSId(String id) {
		CSId = id;
	}
	/**
	 * @return the cSName
	 */
	public String getCSName() {     
		return CSName;
	}
	/**
	 * @param name the cSName to set
	 */
	public void setCSName(String name) {
		CSName = name;
	}
		/**
	 * @return the codeList
	 */
	public String getCodeList() {
		return codeList;
	}
	/**
	 * @param codeList the codeList to set
	 */
	public void setCodeList(String codeList) {
		this.codeList = codeList;
	}
	/**
	 * @return the codeName
	 */
	public String getCodeName() {
		return codeName;
	}
	/**
	 * @param codeName the codeName to set
	 */
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
		public WorksBillForm()
		{    
		 
		}
	
		
		/**
		 * @return the bill_Amount
		 */
		public String getBill_Amount() {
			return bill_Amount;
		}
		/**
		 * @param bill_Amount the bill_Amount to set
		 */
		public void setBill_Amount(String bill_Amount) {
			this.bill_Amount = bill_Amount;
		}
		/**
		 * @return the bill_Type
		 */
		public String getBill_Type() {
			return bill_Type;
		}
		/**
		 * @param bill_Type the bill_Type to set
		 */
		public void setBill_Type(String bill_Type) {
			this.bill_Type = bill_Type;
		}
		/**
		 * @return the billDate
		 */
		public String getBillDate() {
			return billDate;
		}
		/**
		 * @param billDate the billDate to set
		 */
		public void setBillDate(String billDate) {
			this.billDate = billDate;
		}
	
		/**
		 * @return the creditAmount
		 */
		public String[] getCreditAmount() {
			return creditAmount;
		}
		/**
		 * @param creditAmount the creditAmount to set
		 */
		public void setCreditAmount(String[] creditAmount) {
			this.creditAmount = creditAmount;
		}
	
		/**
		 * @return the deb_chartOfAccounts_glCode
		 */
		public String[] getDeb_chartOfAccounts_glCode() {
			return deb_chartOfAccounts_glCode;
		}
		/**
		 * @param deb_chartOfAccounts_glCode the deb_chartOfAccounts_glCode to set
		 */
		public void setDeb_chartOfAccounts_glCode(String[] deb_chartOfAccounts_glCode) {
			this.deb_chartOfAccounts_glCode = deb_chartOfAccounts_glCode;
		}
		/**
		 * @return the deb_chartOfAccounts_name
		 */
		public String[] getDeb_chartOfAccounts_name() {
			return deb_chartOfAccounts_name;
		}
		/**
		 * @param deb_chartOfAccounts_name the deb_chartOfAccounts_name to set
		 */
		public void setDeb_chartOfAccounts_name(String[] deb_chartOfAccounts_name) {
			this.deb_chartOfAccounts_name = deb_chartOfAccounts_name;
		}
		/**
		 * @return the deb_cv_fromFunctionCodeId
		 */
		public String[] getDeb_cv_fromFunctionCodeId() {
			return deb_cv_fromFunctionCodeId;
		}
		/**
		 * @param deb_cv_fromFunctionCodeId the deb_cv_fromFunctionCodeId to set
		 */
		public void setDeb_cv_fromFunctionCodeId(String[] deb_cv_fromFunctionCodeId) {
			this.deb_cv_fromFunctionCodeId = deb_cv_fromFunctionCodeId;
		}
		/**
		 * @return the debitAmount
		 */
		public String[] getDebitAmount() {
			return debitAmount;
		}
		/**
		 * @param debitAmount the debitAmount to set
		 */
		public void setDebitAmount(String[] debitAmount) {
			this.debitAmount = debitAmount;
		}
		/**
		 * @return the ded_chartOfAccounts_glCode 
		 */
		public String[] getDed_chartOfAccounts_glCode() {
			return ded_chartOfAccounts_glCode;
		}
		/**
		 * @param ded_chartOfAccounts_glCode the ded_chartOfAccounts_glCode to set
		 */
		public void setDed_chartOfAccounts_glCode(String[] ded_chartOfAccounts_glCode) {
			this.ded_chartOfAccounts_glCode = ded_chartOfAccounts_glCode;
		}
		/**
		 * @return the ded_chartOfAccounts_name
		 */
		public String[] getDed_chartOfAccounts_name() {
			return ded_chartOfAccounts_name;
		}
		/**
		 * @param ded_chartOfAccounts_name the ded_chartOfAccounts_name to set
		 */
		public void setDed_chartOfAccounts_name(String[] ded_chartOfAccounts_name) {
			this.ded_chartOfAccounts_name = ded_chartOfAccounts_name;
		}
		/**
		 * @return the ded_cv_fromFunctionCodeId
		 */
		public String[] getDed_cv_fromFunctionCodeId() {
			return ded_cv_fromFunctionCodeId;
		}
		/**
		 * @param ded_cv_fromFunctionCodeId the ded_cv_fromFunctionCodeId to set
		 */
		public void setDed_cv_fromFunctionCodeId(String[] ded_cv_fromFunctionCodeId) {
			this.ded_cv_fromFunctionCodeId = ded_cv_fromFunctionCodeId;
		}
		/**
		 * @return the dept
		 */
		public String getDept() {
			return dept;
		}
		/**
		 * @param dept the dept to set
		 */
		public void setDept(String dept) {
			this.dept = dept;
		}
	
	
		/**
		 * @return the creditTotal
		 */
		public String getCreditTotal() {
			return creditTotal;
		}
		/**
		 * @param creditTotal the creditTotal to set
		 */
		public void setCreditTotal(String creditTotal) {
			this.creditTotal = creditTotal;
		}
		/**
		 * @return the debitTotal
		 */
		public String getDebitTotal() {
			return debitTotal;
		}
		/**
		 * @param debitTotal the debitTotal to set
		 */
		public void setDebitTotal(String debitTotal) {
			this.debitTotal = debitTotal;
		}
		/**
		 * @return the net_Amount
		 */
	
		/**
		 * @return the totalAmount
		 */
		public String getTotalAmount() {
			return totalAmount;
		}
		/**
		 * @param totalAmount the totalAmount to set
		 */
		public void setTotalAmount(String totalAmount) {
			this.totalAmount = totalAmount;
		}
		/**
		 * @return the expenditure_Type
		 */
		public String getExpenditure_Type() {
			return expenditure_Type;
		}
		/**
		 * @param expenditure_Type the expenditure_Type to set
		 */
		public void setExpenditure_Type(String expenditure_Type) {
			this.expenditure_Type = expenditure_Type;
		}
		/**
		 * @return the mBrefNo
		 */
		public String getMBrefNo() {
			return MBrefNo;
		}
		/**
		 * @param brefNo the mBrefNo to set
		 */
		public void setMBrefNo(String brefNo) {
			MBrefNo = brefNo;
		}
		
		/**
		 * @return the deb_function_code
		 */
		public String[] getDeb_function_code() {
			return deb_function_code;
		}
		/**
		 * @param deb_function_code the deb_function_code to set
		 */
		public void setDeb_function_code(String[] deb_function_code) {
			this.deb_function_code = deb_function_code;
		}
		/**
		 * @return the ded_function_code
		 */
		public String[] getDed_function_code() {
			return ded_function_code;
		}
		/**
		 * @param ded_function_code the ded_function_code to set
		 */
		public void setDed_function_code(String[] ded_function_code) {
			this.ded_function_code = ded_function_code;
		}
		/**
		 * @return the tds_code
		 */
		public String[] getTds_code() {
			return tds_code;
		}
		/**
		 * @param tds_code the tds_code to set
		 */
		public void setTds_code(String[] tds_code) {
			this.tds_code = tds_code;
		}
		/**
		 * @return the adjustmentAmount
		 */
		public String getAdjustmentAmount() {
			return adjustmentAmount;
		}
		/**
		 * @param adjustmentAmount the adjustmentAmount to set
		 */
		public void setAdjustmentAmount(String adjustmentAmount) {
			this.adjustmentAmount = adjustmentAmount;
		}
		/**
		 * @return the advanceAmount
		 */
		public String getPassedAmount() {
			return passedAmount;
		}
		/**
		 * @param advanceAmount the advanceAmount to set
		 */
		public void setPassedAmount(String advanceAmount) {
			this.passedAmount = advanceAmount;
		}
		/**
		 * @return the advanceAmount
		 */
		public String getAdvanceAmount() {
			return advanceAmount;
		}
		/**
		 * @param advanceAmount the advanceAmount to set
		 */
		public void setAdvanceAmount(String advanceAmount) {
			this.advanceAmount = advanceAmount;
		}
		/**
		 * @return the buttonType
		 */
		public String getButtonType() {
			return buttonType;
		}
		/**
		 * @param buttonType the buttonType to set
		 */
		public void setButtonType(String buttonType) {
			this.buttonType = buttonType;
		}
		/**
		 * @return the billNo
		 */
		public String getBillNo() {
			return billNo;
		}
		/**
		 * @param billNo the billNo to set
		 */
		public void setBillNo(String billNo) {
			this.billNo = billNo;
		}
		/**
		 * @return the billId
		 */
		public String getBillId() {
			return billId;
		}
		/**
		 * @param billId the billId to set
		 */
		public void setBillId(String billId) {
			this.billId = billId;
		}
		/**
		 * @return the totalBilledAmount
		 */
		public String getTotalBilledAmount() {
			return totalBilledAmount;
		}
		/**
		 * @param totalBilledAmount the totalBilledAmount to set
		 */
		public void setTotalBilledAmount(String totalBilledAmount) {
			this.totalBilledAmount = totalBilledAmount;
		}
		/**
		 * @return the totalWorkOrder
		 */
		public String getTotalWorkOrder() {
			return totalWorkOrder;
		}
		/**
		 * @param totalWorkOrder the totalWorkOrder to set
		 */
		public void setTotalWorkOrder(String totalWorkOrder) {
			this.totalWorkOrder = totalWorkOrder;
		}
		/**
		 * @return the userName
		 */
		public String getUserName() {
			return userName;
		}
		/**
		 * @param userName the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}
		/**
		 * @return the narration
		 */
		public String getNarration() {
			return narration;
		}
		/**
		 * @param narration the narration to set
		 */
		public void setNarration(String narration) {
			this.narration = narration;
		}
		/**
		 * @return the voucherHeader_narration
		 */
		public String getVoucherHeader_narration() {
			return voucherHeader_narration;
		}
		/**
		 * @param voucherHeader_narration the voucherHeader_narration to set
		 */
		public void setVoucherHeader_narration(String voucherHeader_narration) {
			this.voucherHeader_narration = voucherHeader_narration;
		}
		public String getSubPartyTYpe() {
			return subPartyTYpe;
		}
		public void setSubPartyTYpe(String subPartyTYpe) {
			this.subPartyTYpe = subPartyTYpe;
			
		}
		public String getWorkType() {
			return workType;
		}
		public void setWorkType(String workType) {
			this.workType = workType;
			
		}
		public String getWorkSubType() {
			return workSubType;
		}
		public void setWorkSubType(String workSubType) {
			this.workSubType = workSubType;
			
		}
		public String getFunctionaryId() {
			return functionaryId;
		}
		public void setFunctionaryId(String functionaryId) {
			this.functionaryId = functionaryId;
		}
		public String getBillAprvalDate() {
			return billAprvalDate;
		}
		public void setBillAprvalDate(String billAprvalDate) {
			this.billAprvalDate = billAprvalDate;
		}
		public String getWorkOrderDate() {
			return workOrderDate;
		}
		public void setWorkOrderDate(String workOrderDate) {
			this.workOrderDate = workOrderDate;
		}
	
		  
  
}
