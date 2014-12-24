package org.egov.model.instrument;

import org.egov.commons.CChartOfAccounts;
import org.egov.infstr.models.BaseModel;

public class InstrumentAccountCodes extends BaseModel {
	private Long id;
	private InstrumentType instrumentType;
	private CChartOfAccounts accountCode;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public InstrumentType getInstrumentType() {
		return instrumentType;
	}
	public void setInstrumentType(InstrumentType instrumentType) {
		this.instrumentType = instrumentType;
	}
	public CChartOfAccounts getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(CChartOfAccounts accountCode) {
		this.accountCode = accountCode;
	}

}
