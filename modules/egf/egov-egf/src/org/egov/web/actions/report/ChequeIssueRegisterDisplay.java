package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.egov.utils.Constants;


public class ChequeIssueRegisterDisplay {
	private Date chequeDate;
	private String chequeNumber = "";
	private BigDecimal chequeAmount;
	private String voucherNumber = "";
	private String voucherName = "";
	private Date voucherDate;
	private String chequeStatus = "";
	private String payTo = "";
	private String billNumber = "";
	private Date billDate;
	private String type = "";
	private List<Long> voucherheaderId = new ArrayList<Long>();

	public String getChequeDate() {
		return chequeDate==null?"":Constants.DDMMYYYYFORMAT1.format(chequeDate);
	}
	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public BigDecimal getChequeAmount() {
		return chequeAmount;
	}
	public void setChequeAmount(BigDecimal chequeAmount) {
		this.chequeAmount = chequeAmount;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public Date getVoucherDate() {
		return voucherDate;
	}
	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}
	public String getChequeStatus() {
		return chequeStatus;
	}
	public void setChequeStatus(String chequeStatus) {
		this.chequeStatus = chequeStatus;
	}
	public String getPayTo() {
		return payTo;
	}
	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setVoucherheaderId(List<Long> voucherheaderId) {
		this.voucherheaderId = voucherheaderId;
	}
	public List<Long> getVoucherheaderId() {
		return voucherheaderId;
	}
	public String getBillNumberAndDate() {
		if("MULTIPLE".equalsIgnoreCase(billNumber))
			return "MULTIPLE"; 
		if(!"".equals(billNumber) && billDate!=null)
			billNumber = billNumber.concat(" , ").concat(Constants.DDMMYYYYFORMAT1.format(billDate));
		return billNumber;
	}
	public String getVoucherNumberAndDate() {
		if("MULTIPLE".equalsIgnoreCase(voucherNumber))
			return "MULTIPLE"; 
		if(!"".equals(voucherNumber) && voucherDate!=null)
			voucherNumber = voucherNumber.concat(" , ").concat(Constants.DDMMYYYYFORMAT1.format(voucherDate));
		return voucherNumber;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getVoucherName() {
		return voucherName;
	}
}
