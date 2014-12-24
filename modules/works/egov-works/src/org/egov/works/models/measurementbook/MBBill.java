package org.egov.works.models.measurementbook;

import org.egov.infstr.models.BaseModel;
import org.egov.model.bills.EgBillregister;

public class MBBill extends BaseModel{
	
	private MBHeader mbHeader;
	private EgBillregister egBillregister;
		
	public MBHeader getMbHeader() {
		return mbHeader;
	}

	public void setMbHeader(MBHeader mbHeader) {
		this.mbHeader = mbHeader;
	}

	public EgBillregister getEgBillregister() {
		return egBillregister;
	}

	public void setEgBillregister(EgBillregister egBillregister) {
		this.egBillregister = egBillregister;
	}

}
