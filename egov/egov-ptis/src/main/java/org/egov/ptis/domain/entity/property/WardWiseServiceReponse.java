package org.egov.ptis.domain.entity.property;

import java.util.List;
import java.util.Map;

public class WardWiseServiceReponse {
    private String revenueWard;
    private Long applicationsApproved;
    private Long applicationsRejected;
    private Long applicationsPending;
    private Long taxBeforeAffctd;
    private Long taxAfterAffctd;

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public Long getApplicationsApproved() {
        return applicationsApproved;
    }

    public void setApplicationApproved(Map<String, List<Long>> countMapApproved) {
        List<Long> valuesList = countMapApproved.get("APPROVED");
        this.applicationsApproved = valuesList == null ? Long.valueOf(0) : valuesList.get(0);
        this.taxBeforeAffctd = valuesList == null ? Long.valueOf(0) : valuesList.get(1);
        this.taxAfterAffctd = valuesList == null ? Long.valueOf(0) : valuesList.get(2);
    }

    public Long getApplicationsRejected() {
        return applicationsRejected;
    }

    public void setApplicationsRejected(Map<String, List<Long>> countMapRejected) {
        List<Long> valuesList = countMapRejected.get("REJECTED");
        this.applicationsRejected = valuesList == null ? Long.valueOf(0) : valuesList.get(0);
    }

    public Long getApplicationsPending() {
        return applicationsPending;
    }

    public void setApplicationsPending(Map<String, List<Long>> countMapPending) {
        List<Long> valuesList = countMapPending.get("INPROGRESS");
        this.applicationsPending = valuesList == null ? Long.valueOf(0) : valuesList.get(0);
    }

    public void setCountFieldValues(Map<String, List<Long>> countMap) {
        setApplicationApproved(countMap);
        setApplicationsRejected(countMap);
        setApplicationsPending(countMap);
    }

    public Long getTaxBeforeAffctd() {
        return taxBeforeAffctd;
    }

    public void setTaxBeforeAffctd(Long taxBeforeAffctd) {
        this.taxBeforeAffctd = taxBeforeAffctd;
    }

    public Long getTaxAfterAffctd() {
        return taxAfterAffctd;
    }

    public void setTaxAfterAffctd(Long taxAfterAffctd) {
        this.taxAfterAffctd = taxAfterAffctd;
    }

    
}
