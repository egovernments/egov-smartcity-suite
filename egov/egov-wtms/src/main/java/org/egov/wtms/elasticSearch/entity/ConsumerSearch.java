package org.egov.wtms.elasticSearch.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infra.search.elastic.Indexable;
import org.egov.search.domain.Searchable;

public class ConsumerSearch implements Indexable {

    @Searchable(name = "zone", group = Searchable.Group.CLAUSES)
    private String zone;

    @Searchable(name = "ward", group = Searchable.Group.CLAUSES)
    private String ward;

    @Searchable(name = "consumercode", group = Searchable.Group.CLAUSES)
    private final String consumerCode;

    @Searchable(name = "propertyid", group = Searchable.Group.CLAUSES)
    private String propertyId;

    @Searchable(name = "bpaid", group = Searchable.Group.CLAUSES)
    private String bpaId;

    @Searchable(name = "mobilenumber", group = Searchable.Group.CLAUSES)
    private final String mobileNumber;

    @Searchable(name = "consumername", group = Searchable.Group.SEARCHABLE)
    private String consumerName;

    @Searchable(name = "locality", group = Searchable.Group.SEARCHABLE)
    private String locality;

    @Searchable(name = "usage", group = Searchable.Group.CLAUSES)
    private final String usageType;

    @Searchable(name = "totaldue", group = Searchable.Group.CLAUSES)
    private BigDecimal totalDue;

    @Searchable(name = "createdDate", group = Searchable.Group.COMMON)
    private final Date createdDate;

    @Searchable(name = "applicationcode", group = Searchable.Group.CLAUSES)
    private String applicationCode;

    @Searchable(name = "status", group = Searchable.Group.CLAUSES)
    private String status;

    @Searchable(name = "connectiontype", group = Searchable.Group.CLAUSES)
    private String connectionType;

    public ConsumerSearch(final String consumerCode, final String mobileNumber, final String usageType,
            final Date createdDate) {
        this.consumerCode = consumerCode;
        this.mobileNumber = mobileNumber;
        this.usageType = usageType;
        this.createdDate = createdDate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public String getIndexId() {
        return consumerCode;
    }

    /**
     * @return the zone
     */
    public String getZone() {
        return zone;
    }

    /**
     * @param zone
     *            the zone to set
     */
    public void setZone(final String zone) {
        this.zone = zone;
    }

    /**
     * @return the ward
     */
    public String getWard() {
        return ward;
    }

    /**
     * @param ward
     *            the ward to set
     */
    public void setWard(final String ward) {
        this.ward = ward;
    }

    /**
     * @return the consumerCode
     */
    public String getConsumerCode() {
        return consumerCode;
    }

    /**
     * @return the propertyId
     */
    public String getPropertyId() {
        return propertyId;
    }

    /**
     * @param propertyId
     *            the propertyId to set
     */
    public void setPropertyId(final String propertyId) {
        this.propertyId = propertyId;
    }

    /**
     * @return the bpaId
     */
    public String getBpaId() {
        return bpaId;
    }

    /**
     * @param bpaId
     *            the bpaId to set
     */
    public void setBpaId(final String bpaId) {
        this.bpaId = bpaId;
    }

    /**
     * @return the mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * @return the consumerName
     */
    public String getConsumerName() {
        return consumerName;
    }

    /**
     * @param consumerName
     *            the consumerName to set
     */
    public void setConsumerName(final String consumerName) {
        this.consumerName = consumerName;
    }

    /**
     * @return the locality
     */
    public String getLocality() {
        return locality;
    }

    /**
     * @param locality
     *            the locality to set
     */
    public void setLocality(final String locality) {
        this.locality = locality;
    }

    /**
     * @return the usageType
     */
    public String getUsageType() {
        return usageType;
    }

    /**
     * @return the totalDue
     */
    public BigDecimal getTotalDue() {
        return totalDue;
    }

    /**
     * @param totalDue
     *            the totalDue to set
     */
    public void setTotalDue(final BigDecimal totalDue) {
        this.totalDue = totalDue;
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @return the applicationCode
     */
    public String getApplicationCode() {
        return applicationCode;
    }

    /**
     * @param applicationCode
     *            the applicationCode to set
     */
    public void setApplicationCode(final String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final String connectionType) {
        this.connectionType = connectionType;
    }

}
