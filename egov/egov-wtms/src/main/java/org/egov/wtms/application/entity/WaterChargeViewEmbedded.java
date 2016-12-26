package org.egov.wtms.application.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class WaterChargeViewEmbedded implements Serializable {

    private static final long serialVersionUID = 8432025289061135445L;
    @ManyToOne
    @JoinColumn(name = "connectiondetailsid")
    private WaterConnectionDetails waterConnectionDetails;

    public WaterConnectionDetails getWaterConnectionDetails() {
        return waterConnectionDetails;
    }

    public void setWaterConnectionDetails(final WaterConnectionDetails waterConnectionDetails) {
        this.waterConnectionDetails = waterConnectionDetails;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (waterConnectionDetails == null ? 0 : waterConnectionDetails.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final WaterChargeViewEmbedded other = (WaterChargeViewEmbedded) obj;
        if (waterConnectionDetails == null) {
            if (other.waterConnectionDetails != null)
                return false;
        } else if (!waterConnectionDetails.equals(other.waterConnectionDetails))
            return false;
        return true;
    }

}
