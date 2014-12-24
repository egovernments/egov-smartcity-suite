package org.egov.model.contra;

import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.models.StateAware;
import org.egov.model.instrument.InstrumentHeader;

public class ContraJournalVoucher extends StateAware {

	private CVoucherHeader voucherHeaderId;
	private Bankaccount fromBankAccountId;
	private Bankaccount toBankAccountId;
	private InstrumentHeader instrumentHeaderId;
	public CVoucherHeader getVoucherHeaderId() {
		return voucherHeaderId;
	}
	public void setVoucherHeaderId(CVoucherHeader voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}
	public Bankaccount getFromBankAccountId() {
		return fromBankAccountId;
	}
	public void setFromBankAccountId(Bankaccount fromBankAccountId) {
		this.fromBankAccountId = fromBankAccountId;
	}
	public Bankaccount getToBankAccountId() {
		return toBankAccountId;
	}
	public void setToBankAccountId(Bankaccount toBankAccountId) {
		this.toBankAccountId = toBankAccountId;
	}
	public void setInstrumentHeaderId(InstrumentHeader instrumentHeaderId) {
		this.instrumentHeaderId = instrumentHeaderId;
	}
	public InstrumentHeader getInstrumentHeaderId() {
		return instrumentHeaderId;
	}
	@Override
	public String getStateDetails() {
		return voucherHeaderId.getVoucherNumber();
	}
	
}
