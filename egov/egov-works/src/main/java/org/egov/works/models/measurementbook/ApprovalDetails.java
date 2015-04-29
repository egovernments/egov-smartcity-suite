package org.egov.works.models.measurementbook;

import java.util.Date;

public class ApprovalDetails {
	private String statusDesc;
	private String emplName;
	private String desgName;
	private Date date;
	private String text;
	
	public String getStatusDesc() {
		return statusDesc;
	}
	public String getEmplName() {
		return emplName;
	}
	public String getDesgName() {
		return desgName;
	}
	public Date getDate() {
		return date;
	}
	public String getText() {
		return text;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public void setEmplName(String emplName) {
		this.emplName = emplName;
	}
	public void setDesgName(String desgName) {
		this.desgName = desgName;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setText(String text) {
		this.text = text;
	}
}
