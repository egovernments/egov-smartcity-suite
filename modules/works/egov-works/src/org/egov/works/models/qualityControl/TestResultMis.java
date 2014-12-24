package org.egov.works.models.qualityControl;

import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.model.instrument.InstrumentType;
import org.hibernate.validator.constraints.Length;

public class TestResultMis extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	private TestResultHeader testResultHeader;
	private Long chequeNumber;
	private Date chequeDate;

	@Required(message = "test.result.instrument.type.null")
	private InstrumentType instrumentType;

	@Required(message = "test.result.amount.received.null")
	private Double amountReceived;

	@Length(max=1024,message="test.result.remarks.length")
	private String remarks;
	
	public TestResultHeader getTestResultHeader() {
		return testResultHeader;
	}
	public void setTestResultHeader(TestResultHeader testResultHeader) {
		this.testResultHeader = testResultHeader;
	}
	public Long getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(Long chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public Date getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}
	public InstrumentType getInstrumentType() {
		return instrumentType;
	}
	public void setInstrumentType(InstrumentType instrumentType) {
		this.instrumentType = instrumentType;
	}
	public Double getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(Double amountReceived) {
		this.amountReceived = amountReceived;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	} 
}

	