package org.egov.web.actions.voucher;

public class WorkFlowHistoryItem {
	private String createdDate = "";
	private String sender= "";
	private String task= "";
	private String status= "";
	private String description= "";
	public WorkFlowHistoryItem(String date, String sender, String task,String status, String description) {
		this.createdDate = date;
		this.sender = sender;
		this.task = task;
		this.status = status;
		this.description = description;
	}
	public void setCreatedDate(String date) {
		this.createdDate = date;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSender() {
		return sender;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getTask() {
		return task;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public void setDescription(String details) {
		this.description = details;
	}
	public String getDescription() {
		return description;
	}
}
