/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.ptis.domain.entity.property;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class UnitAreaCalculationDetail implements Serializable {

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


    public UnitAreaCalculationDetail() {
    }

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
