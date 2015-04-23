/**
 * 
 */
package org.egov.model.instrument;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.CVoucherHeader;
import org.egov.infstr.annotation.Introspection;
import org.egov.infstr.models.BaseModel;

/**
 * @author Mani
 * 
 */
public class InstrumentOtherDetails extends BaseModel {

	@Introspection
	private InstrumentHeader instrumentHeaderId;
	@Introspection
	private CVoucherHeader payinslipId;
	private Date instrumentStatusDate;
	@Introspection
	private BigDecimal reconciledAmount;
	private Date reconciledOn;
	private String  dishonorBankRefNo;

	public Date getReconciledOn() {
		return reconciledOn;
	}

	public void setReconciledOn(Date reconciledOn) {
		this.reconciledOn = reconciledOn;
	}

	public BigDecimal getReconciledAmount() {
		return reconciledAmount;
	}

	public void setReconciledAmount(BigDecimal reconciledAmount) {
		this.reconciledAmount = reconciledAmount;
	}

	public InstrumentHeader getInstrumentHeaderId() {
		return instrumentHeaderId;
	}

	public void setInstrumentHeaderId(InstrumentHeader instrumentHeaderId) {
		this.instrumentHeaderId = instrumentHeaderId;
	}

	public CVoucherHeader getPayinslipId() {
		return payinslipId;
	}

	public void setPayinslipId(CVoucherHeader payinslipId) {
		this.payinslipId = payinslipId;
	}

	public void setInstrumentStatusDate(Date instrumentStatusDate) {
		this.instrumentStatusDate = instrumentStatusDate;
	}

	public Date getInstrumentStatusDate() {
		return instrumentStatusDate;
	}

	public String getDishonorBankRefNo() {
		return dishonorBankRefNo;
	}

	public void setDishonorBankRefNo(String dishonorBankRefNo) {
		this.dishonorBankRefNo = dishonorBankRefNo;
	}

	

	

}
