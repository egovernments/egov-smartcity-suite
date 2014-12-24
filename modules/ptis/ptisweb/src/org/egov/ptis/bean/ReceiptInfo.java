package org.egov.ptis.bean;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author subhash
 *
 */

public class ReceiptInfo {

	private String indexNo;
	private String houseNo;
	private String wardNo;
	private String payeeName;
	private String receiptNo;
	private String paymentMode;
	private String chequeNo;
	private Date chequeDate;
	private String bankName;
	private List<TaxCollectionInfo> collInfoList;
	public String getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getWardNo() {
		return wardNo;
	}
	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public Date getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public List<TaxCollectionInfo> getCollInfoList() {
		return collInfoList;
	}
	public void setCollInfoList(List<TaxCollectionInfo> collInfoList) {
		this.collInfoList = collInfoList;
	}
	
}
