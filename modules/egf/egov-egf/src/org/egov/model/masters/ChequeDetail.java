/**
 * 
 */
package org.egov.model.masters;

/**
 * @author manoranjan
 *
 */
public class ChequeDetail {
	
	private String fromChqNo;
	private String toChqNo;
	private String deptName;
	private Integer deptId;
	private String receivedDate;
	private String isExhusted;
	private String nextChqPresent;
	private Long accountChequeId;
	private Long chequeDeptId;
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getFromChqNo() {
		return fromChqNo;
	}
	public void setFromChqNo(String fromChqNo) {
		this.fromChqNo = fromChqNo;
	}
	public String getToChqNo() {
		return toChqNo;
	}
	public void setToChqNo(String toChqNo) {
		this.toChqNo = toChqNo;
	}
	
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getIsExhusted() {
		return isExhusted;
	}
	public void setIsExhusted(String isExhusted) {
		this.isExhusted = isExhusted;
	}
	
	public Long getAccountChequeId() {
		return accountChequeId;
	}
	public void setAccountChequeId(Long accountChequeId) {
		this.accountChequeId = accountChequeId;
	}
	public Long getChequeDeptId() {
		return chequeDeptId;
	}
	public void setChequeDeptId(Long chequeDeptId) {
		this.chequeDeptId = chequeDeptId;
	}
	public String getNextChqPresent() {
		return nextChqPresent;
	}
	public void setNextChqPresent(String nextChqPresent) {
		this.nextChqPresent = nextChqPresent;
	}
	

}
