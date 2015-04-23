package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;


/**
 *
 *
 * @author nayeem
 *
 */
public class MigratedPropertyFloor {

	@NotNull
	private Long id;
	@NotNull
	private String propertyId;
	@NotNull
	private Long floorId;
	@NotNull
	private BigDecimal migratedAlv = BigDecimal.ZERO;
	@NotNull
	private BigDecimal calculatedAlv  = BigDecimal.ZERO;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}
	public Long getFloorId() {
		return floorId;
	}
	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}

	public BigDecimal getMigratedAlv() {
		return migratedAlv;
	}

	public void setMigratedAlv(BigDecimal migratedAlv) {
		this.migratedAlv = migratedAlv;
	}

	public BigDecimal getCalculatedAlv() {
		return calculatedAlv;
	}

	public void setCalculatedAlv(BigDecimal calculatedAlv) {
		this.calculatedAlv = calculatedAlv;
	}

	@Override
	public String toString() {
		return new StringBuilder(250).append("MigratedPropertyFloor [").append("id=").append(id)
				.append(", propertyId=").append(propertyId).append(", floorId=").append(floorId)
				.append(", migratedAlv=").append(migratedAlv).append(", calculatedAlv=").append(calculatedAlv)
				.toString();
	}

}
