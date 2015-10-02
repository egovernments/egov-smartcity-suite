/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
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

    @Searchable(name = "closureType", group = Searchable.Group.SEARCHABLE)
    private Character closureType;

    @Searchable(name = "waterTaxDue", group = Searchable.Group.CLAUSES)
    private BigDecimal waterTaxDue;

    @Searchable(name = "ulbname", group = Searchable.Group.CLAUSES)
    private final String ulbName;

    public ConsumerSearch(final String consumerCode, final String mobileNumber, final String usageType, final String ulbName,
            final Date createdDate) {
        this.consumerCode = consumerCode;
        this.mobileNumber = mobileNumber;
        this.usageType = usageType;
        this.ulbName = ulbName;
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public String getIndexId() {
        return consumerCode;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(final String zone) {
        this.zone = zone;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(final String ward) {
        this.ward = ward;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(final String propertyId) {
        this.propertyId = propertyId;
    }

    public String getBpaId() {
        return bpaId;
    }

    public void setBpaId(final String bpaId) {
        this.bpaId = bpaId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(final String consumerName) {
        this.consumerName = consumerName;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(final String locality) {
        this.locality = locality;
    }

    public String getUsageType() {
        return usageType;
    }

    public BigDecimal getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(final BigDecimal totalDue) {
        this.totalDue = totalDue;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(final String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final String connectionType) {
        this.connectionType = connectionType;
    }

    public BigDecimal getWaterTaxDue() {
        return waterTaxDue;
    }

    public void setWaterTaxDue(final BigDecimal waterTaxDue) {
        this.waterTaxDue = waterTaxDue;
    }

    public Character getClosureType() {
        return closureType;
    }

    public void setClosureType(final Character closureType) {
        this.closureType = closureType;
    }
    
    public String getUlbName() {
        return ulbName;
    }


}
