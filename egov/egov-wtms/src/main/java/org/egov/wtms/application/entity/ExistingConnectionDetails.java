package org.egov.wtms.application.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

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
import java.util.Date;

@Entity
@Table(name = "egwtr_existing_connection_details")
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public WaterConnectionDetails getWaterConnectionDetails() {
        return waterConnectionDetails;
    }

    public void setWaterConnectionDetails(
            final WaterConnectionDetails waterConnectionDetails) {
        this.waterConnectionDetails = waterConnectionDetails;
    }

    public Double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(final Double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public Double getDonationCharges() {
        return donationCharges;
    }

    public void setDonationCharges(final Double donationCharges) {
        this.donationCharges = donationCharges;
    }

    public Double getArrears() {
        return arrears;
    }

    public void setArrears(final Double arrears) {
        this.arrears = arrears;
    }

    public Double getMeterCost() {
        return meterCost;
    }

    public void setMeterCost(final Double meterCost) {
        this.meterCost = meterCost;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(final String meterName) {
        this.meterName = meterName;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(final String meterNo) {
        this.meterNo = meterNo;
    }

    public Long getPreviousReading() {
        return previousReading;
    }

    public void setPreviousReading(final Long previousReading) {
        this.previousReading = previousReading;
    }

    public Long getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(final Long currentReading) {
        this.currentReading = currentReading;
    }

    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(final Date readingDate) {
        this.readingDate = readingDate;
    }

}
