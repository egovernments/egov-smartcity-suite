/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.adtax.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AgencyWiseCollectionSearch {
    private String agencyName;
    private BigDecimal pendingDemandAmount;
    private BigDecimal penaltyAmount;
    private BigDecimal additionalTaxAmount;
    private BigDecimal totalAmount;
    private String ownerDetail;
    private String advertisementNumber;
    private String applicationNumber;
    private Long advertisementPermitId;
    private List<AgencyWiseCollectionSearch> agencyWiseCollectionList = new ArrayList<AgencyWiseCollectionSearch>();
    private boolean selectedForCollection = false;

    public String getOwnerDetail() {
        return ownerDetail;
    }

    public void setOwnerDetail(final String ownerDetail) {
        this.ownerDetail = ownerDetail;
    }

    public BigDecimal getTotalAmount() {
        final BigDecimal additionalAmt = additionalTaxAmount != null ? additionalTaxAmount : BigDecimal.ZERO;
        final BigDecimal penaltyAmt = penaltyAmount != null ? penaltyAmount : BigDecimal.ZERO;

        // return (penaltyAmount!=null? (pendingDemandAmount!=null? penaltyAmount.add(pendingDemandAmount):penaltyAmount):
        return pendingDemandAmount != null ? pendingDemandAmount.add(additionalAmt).add(penaltyAmt)
                : additionalAmt.add(penaltyAmt);
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isSelectedForCollection() {
        return selectedForCollection;
    }

    public void setSelectedForCollection(final boolean selectedForCollection) {
        this.selectedForCollection = selectedForCollection;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(final String agencyName) {
        this.agencyName = agencyName;
    }

    public BigDecimal getPendingDemandAmount() {
        return pendingDemandAmount;
    }

    public void setPendingDemandAmount(final BigDecimal pendingDemandAmount) {
        this.pendingDemandAmount = pendingDemandAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(final BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public String getAdvertisementNumber() {
        return advertisementNumber;
    }

    public void setAdvertisementNumber(final String advertisementNumber) {
        this.advertisementNumber = advertisementNumber;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Long getAdvertisementPermitId() {
        return advertisementPermitId;
    }

    public void setAdvertisementPermitId(final Long advertisementPermitId) {
        this.advertisementPermitId = advertisementPermitId;
    }

    public List<AgencyWiseCollectionSearch> getAgencyWiseCollectionList() {
        return agencyWiseCollectionList;
    }

    public void setAgencyWiseCollectionList(final List<AgencyWiseCollectionSearch> agencyWiseCollectionList) {
        this.agencyWiseCollectionList = agencyWiseCollectionList;
    }

    public BigDecimal getAdditionalTaxAmount() {
        return additionalTaxAmount;
    }

    public void setAdditionalTaxAmount(final BigDecimal additionalTaxAmount) {
        this.additionalTaxAmount = additionalTaxAmount;
    }

}
