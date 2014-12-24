package org.egov.tender.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import org.egov.tender.TenderableGroupType;
import org.egov.tender.interfaces.TenderableGroup;

public class TenderableEntityGroup implements TenderableGroup, java.io.Serializable {

	private Set<TenderableEntity> entities = new TreeSet<TenderableEntity>(new TenderableEntityComparator());
	private String number;
	private TenderableGroupType tenderableGroupType;
	private TenderUnit tenderUnit;
	private Date modifiedDate;
	private Long id;
	private String description;
	private BigDecimal estimatedCost;
	private String tenderUnitRefNumber;
	
	public String getDescription() {
		return description;
	}

	public String getTenderUnitRefNumber() {
		return tenderUnitRefNumber;
	}

	public void setTenderUnitRefNumber(String tenderUnitRefNumber) {
		this.tenderUnitRefNumber = tenderUnitRefNumber;
	}

	public BigDecimal getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(BigDecimal estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TenderUnit getTenderUnit() {
		return tenderUnit;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setTenderUnit(TenderUnit tenderUnit) {
		this.tenderUnit = tenderUnit;
	}

	public Set<TenderableEntity> getEntities() {
		return entities;
	}
	
	public void setEntities(Set<TenderableEntity> entities) {
		this.entities = entities;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public TenderableGroupType getTenderableGroupType() {
		return tenderableGroupType;
	}
	public void setTenderableGroupType(TenderableGroupType tenderableGroupType) {
		this.tenderableGroupType = tenderableGroupType;
	}

	public void addTenderEntities(TenderableEntity tenderEntity) {
		if(this.entities!=null){
			this.entities.add(tenderEntity);
			}
		
	}



}
