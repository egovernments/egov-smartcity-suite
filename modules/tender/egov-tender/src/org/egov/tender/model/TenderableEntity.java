package org.egov.tender.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.utils.StringUtils;
import org.egov.tender.TenderableType;
import org.egov.tender.interfaces.Tenderable;

     
public class TenderableEntity implements Tenderable, java.io.Serializable {
	private Long id; 
	private String description;
	private String name;
	private String number;
	private Date requestedByDate; 
	private BigDecimal requestedQty;
	private BigDecimal requestedValue; 
	private EgUom requestedUOM;
	private TenderableType tenderableType;
	private Date modifiedDate;
	private TenderUnit tenderUnit;
	private TenderableEntityGroup tenderableEntityGroup;
	private String tempEntityGroupName;
	private Long indexNo;
	
	// Variable used for comparision statement.
	private BigDecimal estimatedRateByUom;
	
	public BigDecimal getEstimatedRateByUom() {
		return (requestedValue==null)?BigDecimal.ZERO:(getRequestedUOM()==null)?getRequestedValue():getRequestedValue().multiply(getRequestedUOM().getConvFactor()).setScale(2, BigDecimal.ROUND_HALF_UP);
		
	}
	
	public String getTempEntityGroupName() {
		return tempEntityGroupName;
	}
	public void setTempEntityGroupName(String tempEntityGroupName) {
		this.tempEntityGroupName = tempEntityGroupName;
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
	public TenderUnit getTenderUnit() {
		return tenderUnit;
	}
	public void setTenderUnit(TenderUnit tenderUnit) {
		this.tenderUnit = tenderUnit;
	}
	public TenderableEntityGroup getTenderableEntityGroup() {
		return tenderableEntityGroup;
	}
	public void setTenderableEntityGroup(TenderableEntityGroup tenderableEntityGroup) {
		this.tenderableEntityGroup = tenderableEntityGroup;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = StringEscapeUtils.unescapeHtml(description);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = StringEscapeUtils.unescapeHtml(name);
	}
	public String getNumber() {
		return StringUtils.escapeSpecialChars(number);
	}
	public void setNumber(String number) {
		this.number = StringEscapeUtils.unescapeHtml(number);
	}
	public Date getRequestedByDate() {
		return requestedByDate;
	}
	public void setRequestedByDate(Date requestedByDate) {
		this.requestedByDate = requestedByDate;
	}
	
	public BigDecimal getRequestedQty() {
		return requestedQty;
	}
	public void setRequestedQty(BigDecimal requestedQty) {
		this.requestedQty = requestedQty;
	}
	
	public BigDecimal getRequestedValue() {
		return requestedValue;
	}
	public void setRequestedValue(BigDecimal requestedValue) {
		this.requestedValue = requestedValue;
	}
	public EgUom getRequestedUOM() {
		return requestedUOM;
	}
	public void setRequestedUOM(EgUom requestedUOM) {
		this.requestedUOM = requestedUOM;
	}
	public TenderableType getTenderableType() {
		return tenderableType;
	}
	public void setTenderableType(TenderableType tenderableType) {
		this.tenderableType = tenderableType;
	}
	
	public String getDescriptionJS() {
		return StringUtils.escapeJavaScript(description);
	}
	
	public String getNameJS() {
        return StringUtils.escapeSpecialChars(name);
    }

	public Long getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(Long indexNo) {
		this.indexNo = indexNo;
	}

}
