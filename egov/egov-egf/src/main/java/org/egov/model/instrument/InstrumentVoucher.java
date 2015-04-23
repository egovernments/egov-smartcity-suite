package org.egov.model.instrument;

import org.egov.commons.CVoucherHeader;
import org.egov.infstr.models.BaseModel;

/**
 * 
 * @author Mani
 * 
 */

public class InstrumentVoucher extends BaseModel {

	InstrumentHeader instrumentHeaderId;
	CVoucherHeader voucherHeaderId;

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

	public String toString() {
		StringBuffer ivBuffer = new StringBuffer();
		ivBuffer.append("[id=" + id).append(
				"instrumentHeader=" + instrumentHeaderId).append(
				"voucherHeader=" + voucherHeaderId).append("]");
		return ivBuffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((instrumentHeaderId == null) ? 0 : instrumentHeaderId
						.hashCode());
		result = prime * result
				+ ((voucherHeaderId == null) ? 0 : voucherHeaderId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstrumentVoucher other = (InstrumentVoucher) obj;
		if (instrumentHeaderId == null) {
			if (other.instrumentHeaderId != null)
				return false;
		} else if (!instrumentHeaderId.equals(other.instrumentHeaderId))
			return false;
		if (voucherHeaderId == null) {
			if (other.voucherHeaderId != null)
				return false;
		} else if (!voucherHeaderId.equals(other.voucherHeaderId))
			return false;
		return true;
	}

}
