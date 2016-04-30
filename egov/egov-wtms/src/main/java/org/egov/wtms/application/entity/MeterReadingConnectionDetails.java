package org.egov.wtms.application.entity;

import org.egov.infra.persistence.entity.AbstractAuditable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "egwtr_meter_connection_details")
@SequenceGenerator(name = MeterReadingConnectionDetails.SEQ, sequenceName = MeterReadingConnectionDetails.SEQ, allocationSize = 1)
public class MeterReadingConnectionDetails extends AbstractAuditable {

    private static final long serialVersionUID = 25508399800297413L;

    public static final String SEQ = "seq_egwtr_meter_connection_details";

    @Id
    @GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connectiondetailsid", nullable = false)
    private WaterConnectionDetails waterConnectionDetails;

    private Long currentReading;

    private Date currentReadingDate;

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

    public void setWaterConnectionDetails(final WaterConnectionDetails waterConnectionDetails) {
        this.waterConnectionDetails = waterConnectionDetails;
    }

    public Long getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(final Long currentReading) {
        this.currentReading = currentReading;
    }

    public Date getCurrentReadingDate() {
        return currentReadingDate;
    }

    public void setCurrentReadingDate(final Date currentReadingDate) {
        this.currentReadingDate = currentReadingDate;
    }

}
