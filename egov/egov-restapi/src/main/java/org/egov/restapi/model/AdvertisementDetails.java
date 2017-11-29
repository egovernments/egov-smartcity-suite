/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.restapi.model;

import org.egov.adtax.entity.AdvertisementPermitDetail;

public class AdvertisementDetails {

    private String permissionNumber;
    private String advertisementNo;
    private String advertisementType;
    private String agencyAdvertiser;
    private String advertisementStatus;
    private String locality;
    private String revenueWard;
    private String adminWard;
    private String address;
    private String structure;
    private String category;
    private String subCategory;
    private Double measurement;
    private Double length;
    private Double width;
    private Double heightOfStructure;
    private String TPBORInumber;
    private String advertisementDuration;
    private String electricityServiceNo;

    public AdvertisementDetails(AdvertisementPermitDetail advPermitDtl) {

        this.permissionNumber = advPermitDtl.getPermissionNumber();
        this.length = advPermitDtl.getLength();
        this.width = advPermitDtl.getWidth();
        this.measurement = advPermitDtl.getMeasurement();
        this.heightOfStructure = advPermitDtl.getTotalHeight();

        this.advertisementDuration = advPermitDtl.getAdvertisementDuration() != null
                ? advPermitDtl.getAdvertisementDuration().name() : null;
        if (advPermitDtl.getAgency() != null)
            this.agencyAdvertiser = advPermitDtl.getAgency().getName();
        else
            this.agencyAdvertiser = advPermitDtl.getAdvertiser();

        this.advertisementNo = advPermitDtl.getAdvertisement().getAdvertisementNumber();
        this.advertisementType = advPermitDtl.getAdvertisement().getType() != null
                ? advPermitDtl.getAdvertisement().getType().name() : null;
        this.advertisementStatus = advPermitDtl.getAdvertisement().getStatus() != null
                ? advPermitDtl.getAdvertisement().getStatus().name() : null;

        this.locality = advPermitDtl.getAdvertisement().getLocality() != null
                ? advPermitDtl.getAdvertisement().getLocality().getName() : null;
        this.revenueWard = advPermitDtl.getAdvertisement().getWard() != null ? advPermitDtl.getAdvertisement().getWard().getName()
                : null;
        this.adminWard = advPermitDtl.getAdvertisement().getElectionWard() != null
                ? advPermitDtl.getAdvertisement().getElectionWard().getName() : null;
        this.address = advPermitDtl.getAdvertisement().getAddress();
        this.structure = advPermitDtl.getAdvertisement().getType() != null ? advPermitDtl.getAdvertisement().getType().name()
                : null;
        this.category = advPermitDtl.getAdvertisement().getCategory() != null
                ? advPermitDtl.getAdvertisement().getCategory().getName() : null;
        this.subCategory = advPermitDtl.getAdvertisement().getSubCategory() != null
                ? advPermitDtl.getAdvertisement().getSubCategory().getDescription() : null;
        this.TPBORInumber = advPermitDtl.getAdvertisement().getRevenueInspector() != null
                ? advPermitDtl.getAdvertisement().getRevenueInspector().getName() : null;
        this.electricityServiceNo = advPermitDtl.getAdvertisement().getElectricityServiceNumber();

    }

    public String getAdvertisementNo() {
        return advertisementNo;
    }

    public void setAdvertisementNo(String advertisementNo) {
        this.advertisementNo = advertisementNo;
    }

    public String getAdvertisementType() {
        return advertisementType;
    }

    public void setAdvertisementType(String advertisementType) {
        this.advertisementType = advertisementType;
    }

    public String getAgencyAdvertiser() {
        return agencyAdvertiser;
    }

    public void setAgencyAdvertiser(String agencyAdvertiser) {
        this.agencyAdvertiser = agencyAdvertiser;
    }

    public String getAdvertisementStatus() {
        return advertisementStatus;
    }

    public void setAdvertisementStatus(String advertisementStatus) {
        this.advertisementStatus = advertisementStatus;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getAdminWard() {
        return adminWard;
    }

    public void setAdminWard(String adminWard) {
        this.adminWard = adminWard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public Double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Double measurement) {
        this.measurement = measurement;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeightOfStructure() {
        return heightOfStructure;
    }

    public void setHeightOfStructure(Double heightOfStructure) {
        this.heightOfStructure = heightOfStructure;
    }

    public String getTPBORInumber() {
        return TPBORInumber;
    }

    public void setTPBORInumber(String tPBORInumber) {
        TPBORInumber = tPBORInumber;
    }

    public String getAdvertisementDuration() {
        return advertisementDuration;
    }

    public void setAdvertisementDuration(String advertisementDuration) {
        this.advertisementDuration = advertisementDuration;
    }

    public String getElectricityServiceNo() {
        return electricityServiceNo;
    }

    public void setElectricityServiceNo(String electricityServiceNo) {
        this.electricityServiceNo = electricityServiceNo;
    }

    public String getPermissionNumber() {
        return permissionNumber;
    }

    public void setPermissionNumber(String permissionNumber) {
        this.permissionNumber = permissionNumber;
    }

}
