package org.egov.tender.model;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infstr.commonMasters.EgUom;


@SuppressWarnings("serial")
public class TenderResponseLine implements java.io.Serializable{
	
	private Long id;
	private BigDecimal quantity;
	private EgUom uom;
	private BigDecimal bidRate;
	private TenderableEntity tenderableEntity;
	private GenericTenderResponse tenderResponse;
	private Date modifiedDate;
	
	
	public GenericTenderResponse getTenderResponse() {
		return tenderResponse;
	}
	public void setTenderResponse(GenericTenderResponse tenderResponse) {
		this.tenderResponse = tenderResponse;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public EgUom getUom() {
		return uom;
	}
	public void setUom(EgUom uom) {
		this.uom = uom;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getBidRate() {
		return bidRate;
	}
	public void setBidRate(BigDecimal bidRate) {
		this.bidRate = bidRate;
	}
	public TenderableEntity getTenderableEntity() {
		return tenderableEntity;
	}
	public void setTenderableEntity(TenderableEntity tenderableEntity) {
		this.tenderableEntity = tenderableEntity;
	}
	
	public BigDecimal getBidRateByUom()
	{
		return (bidRate==null)?BigDecimal.ZERO:(uom==null)?bidRate:bidRate.multiply(uom.getConvFactor()).setScale(4, BigDecimal.ROUND_HALF_UP);
	}
	
	public BigDecimal getQuantityByUom()
	{
		return (quantity==null)?BigDecimal.ZERO:(uom==null)?quantity:quantity.divide(uom.getConvFactor(),4,BigDecimal.ROUND_HALF_UP);
	}
	
	public BigDecimal getEstimatedRate()
	{
		return this.tenderableEntity.getRequestedValue();
	}
	
	public BigDecimal getEstimatedRateByUom()
	{
		return (tenderableEntity.getRequestedValue()==null)?BigDecimal.ZERO:(uom==null)?tenderableEntity.getRequestedValue():tenderableEntity.getRequestedValue().multiply(uom.getConvFactor()).setScale(4, BigDecimal.ROUND_HALF_UP);
	}
	
	public BigDecimal getTotalEstimatedRate()
	{
		return getQuantityByUom().multiply(getEstimatedRateByUom()).setScale(4, BigDecimal.ROUND_HALF_UP);
	}
	
	public BigDecimal getTotalBidRateByUom()
	{
		return getQuantityByUom().multiply(getBidRateByUom()).setScale(4, BigDecimal.ROUND_HALF_UP);
	}
	
	
}
