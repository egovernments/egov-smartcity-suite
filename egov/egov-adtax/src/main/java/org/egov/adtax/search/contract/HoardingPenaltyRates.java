package org.egov.adtax.search.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.adtax.entity.AdvertisementPenaltyRates;

public class HoardingPenaltyRates {
    
    private Long id;
    
    
    List<AdvertisementPenaltyRates> advtPenaltyRatesList = new ArrayList<AdvertisementPenaltyRates>();

    public List<AdvertisementPenaltyRates> getAdvtPenaltyRatesList() {
        return advtPenaltyRatesList;
    }

    public void setAdvtPenaltyRatesList(List<AdvertisementPenaltyRates> advtPenaltyRatesList) {
        this.advtPenaltyRatesList = advtPenaltyRatesList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
