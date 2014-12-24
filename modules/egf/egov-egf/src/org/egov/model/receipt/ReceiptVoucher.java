package org.egov.model.receipt;

import org.egov.commons.CVoucherHeader;
import org.egov.infstr.models.StateAware;

public class ReceiptVoucher  extends StateAware {
	private CVoucherHeader voucherHeader;
	public CVoucherHeader getVoucherHeader() {
		return voucherHeader;
	}
	public void setVoucherHeader(CVoucherHeader voucherHeader) {
		this.voucherHeader = voucherHeader;
	}
	@Override
	public String getStateDetails() {
		return voucherHeader.getVoucherNumber();
	}
}
