package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

/**
 *
 * @author nayeem
 *
 */

public class UnitAreaCalculationDetail {

	@NotNull
	private Long id;

	@NotNull
	private BigDecimal taxableArea = BigDecimal.ZERO;

	@NotNull
	private BigDecimal monthlyBaseRent = BigDecimal.ZERO;

	@NotNull
	private BigDecimal monthlyRentalValue = BigDecimal.ZERO;

	@NotNull
	private UnitCalculationDetail unitCalculationDetail;

	private String floorNumber;
	private String unitOccupation;

	private String unitUsage;

	@NotNull
	private BigDecimal baseRentPerSqMtr = BigDecimal.ZERO;

	@NotNull
	private BigDecimal manualALV = BigDecimal.ZERO;

	@NotNull
	private BigDecimal monthlyRentPaidByTenanted = BigDecimal.ZERO;

	@NotNull
	private String unitIdentifier;


	public UnitAreaCalculationDetail() {}

	public UnitAreaCalculationDetail(UnitAreaCalculationDetail other) {
		this.taxableArea = other.taxableArea;
		this.monthlyBaseRent = other.monthlyBaseRent;
		this.monthlyRentalValue = other.monthlyRentalValue;
		this.floorNumber = other.floorNumber;
		this.unitOccupation = other.unitOccupation;
		this.unitUsage = other.unitUsage;
		this.baseRentPerSqMtr = other.baseRentPerSqMtr;
		this.manualALV = other.manualALV;
		this.monthlyRentPaidByTenanted = other.monthlyRentPaidByTenanted;
		this.unitIdentifier = other.unitIdentifier;
	}


	/*@Override
	public int hashCode() {

		int seedValue = HashCodeUtil.SEED;

		seedValue = HashCodeUtil.hash(seedValue, this.monthlyBaseRent);
		seedValue = HashCodeUtil.hash(seedValue, this.monthlyRentalValue);
		seedValue = HashCodeUtil.hash(seedValue, this.taxableArea);
		seedValue = HashCodeUtil.hash(seedValue, this.unitCalculationDetail == null ? 0 : this.unitCalculationDetail);
		seedValue = HashCodeUtil.hash(seedValue, this.floorNumber);
		seedValue = HashCodeUtil.hash(seedValue, this.unitOccupation);
		seedValue = HashCodeUtil.hash(seedValue, this.unitUsage);
		seedValue = HashCodeUtil.hash(seedValue, this.baseRentPerSqMtr);
		seedValue = HashCodeUtil.hash(seedValue, this.unitIdentifier);

		return seedValue;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (!(obj instanceof UnitAreaCalculationDetail)) {
			return false;
		}

		UnitAreaCalculationDetail other = (UnitAreaCalculationDetail) obj;

		if (monthlyBaseRent == null) {
			if (other.monthlyBaseRent != null) {
				return false;
			}
		} else if (!monthlyBaseRent.equals(other.monthlyBaseRent)) {
			return false;
		}

		if (taxableArea == null) {
			if (other.taxableArea != null) {
				return false;
			}
		} else if (!taxableArea.equals(other.taxableArea)) {
			return false;
		}

		if (floorNumber == null) {
			if (other.floorNumber != null) {
				return false;
			}
		} else if (!floorNumber.equals(other.floorNumber)) {
			return false;
		}

		if (unitOccupation == null) {
			if (other.unitOccupation != null) {
				return false;
			}
		} else if (!unitOccupation.equals(other.unitOccupation)) {
			return false;
		}

		if (unitUsage == null) {
			if (other.unitUsage != null) {
				return false;
			}
		} else if (!unitUsage.equals(other.unitUsage)) {
			return false;
		}

		if (baseRentPerSqMtr == null) {
			if (other.baseRentPerSqMtr != null) {
				return false;
			}
		} else if (!baseRentPerSqMtr.equals(other.baseRentPerSqMtr)) {
			return false;
		}

		if (unitIdentifier == null) {
			if (other.unitIdentifier != null) {
				return false;
			}
		} else if (!unitIdentifier.equals(other.unitIdentifier)) {
			return false;
		}

		return true;
	}*/

	@Override
	public String toString() {
		return new StringBuilder()
			.append("UnitAreaCalculationDetail [")
			.append("id=").append(id)
			.append(", baseRentPerSqMtr=").append(baseRentPerSqMtr)
			.append(", taxableArea=").append(taxableArea)
			.append(", monthlyRent").append(monthlyBaseRent)
			.append(", calculatedTax").append(monthlyRentalValue)
			.append(", floorNumber").append(floorNumber)
			.append(", unitOccupation").append(unitOccupation)
			.append(", unitUsage").append(unitUsage)
			.append(", manualALV").append(manualALV)
			.append(", monthlyRentPaidByTenanted").append(monthlyRentPaidByTenanted)
			.append(", unitIdentifier").append(unitIdentifier)
			.append("]").toString();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public UnitCalculationDetail getUnitCalculationDetail() {
		return unitCalculationDetail;
	}

	public void setUnitCalculationDetail(UnitCalculationDetail unitCalculationDetail) {
		this.unitCalculationDetail = unitCalculationDetail;
	}

	public BigDecimal getTaxableArea() {
		return taxableArea;
	}

	public void setTaxableArea(BigDecimal taxableArea) {
		this.taxableArea = taxableArea;
	}

	public BigDecimal getMonthlyBaseRent() {
		return monthlyBaseRent;
	}

	public void setMonthlyBaseRent(BigDecimal monthlyBaseRent) {
		this.monthlyBaseRent = monthlyBaseRent;
	}

	public BigDecimal getMonthlyRentalValue() {
		return monthlyRentalValue;
	}

	public void setMonthlyRentalValue(BigDecimal monthlyRentalValue) {
		this.monthlyRentalValue = monthlyRentalValue;
	}

	public String getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(String floorNumber) {
		this.floorNumber = floorNumber;
	}

	public String getUnitUsage() {
		return unitUsage;
	}

	public void setUnitUsage(String unitUsage) {
		this.unitUsage = unitUsage;
	}

	public String getUnitOccupation() {
		return unitOccupation;
	}

	public void setUnitOccupation(String unitOccupation) {
		this.unitOccupation = unitOccupation;
	}

	public BigDecimal getBaseRentPerSqMtr() {
		return baseRentPerSqMtr;
	}

	public void setBaseRentPerSqMtr(BigDecimal baseRentPerSqMtr) {
		this.baseRentPerSqMtr = baseRentPerSqMtr;
	}

	public BigDecimal getManualALV() {
		return manualALV;
	}

	public void setManualALV(BigDecimal manualALV) {
		this.manualALV = manualALV;
	}

	public BigDecimal getMonthlyRentPaidByTenanted() {
		return monthlyRentPaidByTenanted;
	}

	public void setMonthlyRentPaidByTenanted(BigDecimal monthlyRentPaidByTenanted) {
		this.monthlyRentPaidByTenanted = monthlyRentPaidByTenanted;
	}

	public String getUnitIdentifier() {
		return unitIdentifier;
	}

	public void setUnitIdentifier(String unitIdentifier) {
		this.unitIdentifier = unitIdentifier;
	}
}
