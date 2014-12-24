package org.egov.model.instrument;

import org.egov.commons.CVoucherHeader;
import org.egov.infstr.models.BaseModel;
/**
 * 
 * @author Mani
 *
 */

public class InstrumentVoucher  extends BaseModel{
	
	InstrumentHeader instrumentHeaderId;
	CVoucherHeader voucherHeaderId ;
	public InstrumentHeader getInstrumentHeaderId() {
		return instrumentHeaderId;
	}
	public void setInstrumentHeaderId(InstrumentHeader instrumentHeaderId) {
		this.instrumentHeaderId = instrumentHeaderId;
	}
	public CVoucherHeader getVoucherHeaderId() {
		return voucherHeaderId;
	}
	public void setVoucherHeaderId(CVoucherHeader voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}
	


}
