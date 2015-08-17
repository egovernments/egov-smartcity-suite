package org.egov.wtms.application.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
@Entity
@Table(name="egwtr_existing_connection_details")
@SequenceGenerator(name = ExistingConnectionDetails.SEQ, sequenceName = ExistingConnectionDetails.SEQ, allocationSize = 1)
public class ExistingConnectionDetails extends AbstractAuditable {

	private static final long serialVersionUID = 25508399800297413L;

	public static final String SEQ = "seq_egwtr_existing_connection_details";

	@Id
	@GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
	private Long id;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connectiondetailsid", nullable = false)
    private WaterConnectionDetails waterConnectionDetails;
	private Double monthlyFee;
	private Double donationCharges;
	private Double arrears;
	private Double meterCost;
	@SafeHtml
    @Length(max = 20)
	private String meterName;
	@SafeHtml
    @Length(max = 20)
	private String meterNo;
	private Long previousReading;
	private Long currentReading;
	@Temporal(value = TemporalType.DATE)
	private Date readingDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public WaterConnectionDetails getWaterConnectionDetails() {
		return waterConnectionDetails;
	}
	public void setWaterConnectionDetails(
			WaterConnectionDetails waterConnectionDetails) {
		this.waterConnectionDetails = waterConnectionDetails;
	}
	public Double getMonthlyFee() {
		return monthlyFee;
	}
	public void setMonthlyFee(Double monthlyFee) {
		this.monthlyFee = monthlyFee;
	}
	public Double getDonationCharges() {
		return donationCharges;
	}
	public void setDonationCharges(Double donationCharges) {
		this.donationCharges = donationCharges;
	}
	public Double getArrears() {
		return arrears;
	}
	public void setArrears(Double arrears) {
		this.arrears = arrears;
	}
	public Double getMeterCost() {
		return meterCost;
	}
	public void setMeterCost(Double meterCost) {
		this.meterCost = meterCost;
	}
	public String getMeterName() {
		return meterName;
	}
	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	public String getMeterNo() {
		return meterNo;
	}
	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}
	public Long getPreviousReading() {
		return previousReading;
	}
	public void setPreviousReading(Long previousReading) {
		this.previousReading = previousReading;
	}
	public Long getCurrentReading() {
		return currentReading;
	}
	public void setCurrentReading(Long currentReading) {
		this.currentReading = currentReading;
	}
	public Date getReadingDate() {
		return readingDate;
	}
	public void setReadingDate(Date readingDate) {
		this.readingDate = readingDate;
	}
	
	
	


}
