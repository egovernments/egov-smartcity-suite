package org.egov.pims.empLeave.model;

public class ViewLeaveTxns  implements java.io.Serializable{
	String  san = null ;
	String  type= null;
	Integer  total= Integer.valueOf(0);
	java.sql.Date  from= null;
	java.sql.Date  to= null;
	String  appNo= null;
	String  desname= null;
	Integer statusId= null;
	private Character payElegible= null;
	public java.sql.Date getFrom() {
		return from;
	}
	public void setFrom(java.sql.Date from) {
		this.from = from;
	}
	public Character getPayElegible() {
		return payElegible;
	}
	public void setPayElegible(Character payElegible) {
		this.payElegible = payElegible;
	}
	public String getSan() {
		return san;
	}
	public void setSan(String san) {
		this.san = san;
	}
	public java.sql.Date getTo() {
		return to;
	}
	public void setTo(java.sql.Date to) {
		this.to = to;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public String getDesname() {
		return desname;
	}
	public void setDesname(String desname) {
		this.desname = desname;
	}
	public String getAppNo() {
		return appNo;
	}
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}
	

}
