package org.egov.pims.empLeave.model;

public class LeaveCard implements java.io.Serializable
{
	String  type= null;
	String  appNo= null;
	Integer statusId= null;
	String  san = null ;
	Float  max= new Float(0);
	Float  ear= new Float(0);
	Float  availed= new Float(0);
	Float  bal= new Float(0);
	Float  balPreFy= new Float(0);
	java.sql.Date  from= null;
	java.sql.Date  to= null;
	
	
	public Float getAvailed() {
		return availed;
	}
	public void setAvailed(Float availed) {
		this.availed = availed;
	}
	public Float getBal() {
		return bal;
	}
	public void setBal(Float bal) {
		this.bal = bal;
	}
	public Float getEar() {
		return ear;
	}
	public void setEar(Float ear) {
		this.ear = ear;
	}
	public Float getMax() {
		return max;
	}
	public void setMax(Float max) {
		this.max = max;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSan() {
		return san;
	}
	public void setSan(String san) {
		this.san = san;
	}
	public Float getBalPreFy() {
		return balPreFy;
	}
	public void setBalPreFy(Float balPreFy) {
		this.balPreFy = balPreFy;
	}
	public String getAppNo() {
		return appNo;
	}
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}
	public java.sql.Date getFrom() {
		return from;
	}
	public void setFrom(java.sql.Date from) {
		this.from = from;
	}
	public java.sql.Date getTo() {
		return to;
	}
	public void setTo(java.sql.Date to) {
		this.to = to;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	
}
