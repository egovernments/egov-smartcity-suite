package org.egov.model.recoveries;

import org.egov.commons.CVoucherHeader;

public class RemittanceSchedulePayment implements java.io.Serializable
{
	private Long id;
	private RemittanceSchedulerLog schId;
	private CVoucherHeader voucherheaderId;
	
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public RemittanceSchedulerLog getSchId() {
		return schId;
	}
	public void setSchId(RemittanceSchedulerLog schId) {
		this.schId = schId;
	}
	
	public CVoucherHeader getVoucherheaderId() {
		return voucherheaderId;
	}
	public void setVoucherheaderId(CVoucherHeader voucherheaderId) {
		this.voucherheaderId = voucherheaderId;
	}
     
}


