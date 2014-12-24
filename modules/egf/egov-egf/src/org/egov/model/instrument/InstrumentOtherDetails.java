/**
 * 
 */
package org.egov.model.instrument;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.CVoucherHeader;
import org.egov.infstr.models.BaseModel;

/**
 * @author Mani
 *
 */
public class InstrumentOtherDetails extends BaseModel{ 
	
	 
	private	 InstrumentHeader instrumentHeaderId ;
	private  CVoucherHeader  payinslipId ;
	private	 Date  instrumentStatusDate ;
	private	 BigDecimal  reconciledAmount ;	
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
	
	
	}
