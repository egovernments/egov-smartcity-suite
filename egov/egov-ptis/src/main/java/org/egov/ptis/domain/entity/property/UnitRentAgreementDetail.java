package org.egov.ptis.domain.entity.property;

import java.util.Date;

import org.hibernate.validator.constraints.Range;

/**
 * Represents the rent agreement details for the unit
 * 
 * @author nayeem
 *
 */
public class UnitRentAgreementDetail{
	
	private Long id;
	private FloorIF floor;
	
	/**
	 * The period the rent agreement is valid
	 */
	@Range(min=1, max=12)
	private Byte agreementPeriod;
	
	/**
	 * The Date on which rent agreement was made
	 */
	private Date agreementDate;
	
	/**
	 * The percentage in which the rent has to increased
	 */
	@Range(min=1, max=100)
	private Byte incrementInRent;
	
	public String toString() {
		return new StringBuilder(250).append("UnitRentAgreementDetail [")
				.append("id=").append(id)
				.append(", agreementPeriod=").append(agreementPeriod)
				.append(", agreementDate=").append(agreementDate)
				.append(", incrementInRent=").append(incrementInRent).toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FloorIF getFloor() {
		return floor;
	}

	public void setFloor(FloorIF floor) {
		this.floor = floor;
	}

	public Byte getAgreementPeriod() {
		return agreementPeriod;
	}

	public void setAgreementPeriod(Byte agreementPeriod) {
		this.agreementPeriod = agreementPeriod;
	}

	public Date getAgreementDate() {
		return agreementDate;
	}

	public void setAgreementDate(Date agreementDate) {
		this.agreementDate = agreementDate;
	}

	public Byte getIncrementInRent() {
		return incrementInRent;
	}

	public void setIncrementInRent(Byte incrementInRent) {
		this.incrementInRent = incrementInRent;
	}
}

