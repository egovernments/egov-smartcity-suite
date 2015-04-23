package org.egov.eb.domain.transaction.entity;

import org.egov.eb.domain.master.entity.EBConsumer;
import org.egov.infstr.models.BaseModel;

public class EbSchedulerLogDetails extends BaseModel {
	
	//if bill is created/already exists it is success else it is failure
	public static final String STATUS_FAILURE="Failure";
	public static final String STATUS_SUCCESS="Success";

	private EbSchedulerLog ebSchedulerLog;
	private EBConsumer ebConsumer;
	//Below 4 are fetched from TNEB so saving as string as we dont know what data it returns (may be no or string)
	private String consumerNo;
	private String dueDate;
	private String amount;
	private String message;
	
    private String status;
	public EbSchedulerLog getEbSchedulerLog() {
		return ebSchedulerLog;
	}
	public void setEbSchedulerLog(EbSchedulerLog ebSchedulerLog) {
		this.ebSchedulerLog = ebSchedulerLog;
	}
	
	public EBConsumer getEbConsumer() {
		return ebConsumer;
	}
	public void setEbConsumer(EBConsumer ebConsumer) {
		this.ebConsumer = ebConsumer;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getConsumerNo() {
		return consumerNo;
	}
	public void setConsumerNo(String consumerNo) {
		this.consumerNo = consumerNo;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
    
}
