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

package org.egov.ptis.elasticSearch;

import org.egov.infra.search.elastic.Indexable;
import org.egov.search.domain.Searchable;

public class PropertyTaxIndex implements Indexable {

    @Searchable(name = "builtuparea", group = Searchable.Group.CLAUSES)
    public Double builtupArea;

    @Searchable(name = "citygrade", group = Searchable.Group.CLAUSES)
    public String cityGrade;

    @Searchable(name = "cityname", group = Searchable.Group.CLAUSES)
    public String cityName;

    @Searchable(name = "districtname", group = Searchable.Group.CLAUSES)
    public String districtName;

    @Searchable(name = "adminwardname", group = Searchable.Group.CLAUSES)
    public String adminWardName;

    @Searchable(name = "propertyType", group = Searchable.Group.CLAUSES)
    public String propertyType;

    @Searchable(name = "regionname", group = Searchable.Group.CLAUSES)
    public String regionName;

    @Searchable(name = "revwardname", group = Searchable.Group.CLAUSES)
    public String revWardName;

    @Searchable(name = "revzonename", group = Searchable.Group.CLAUSES)
    public String revZoneName;

    @Searchable(name = "sitalarea", group = Searchable.Group.CLAUSES)
    public String sitalArea;

    @Searchable(name = "propertyusage", group = Searchable.Group.CLAUSES)
    public String propertyUsage;

    @Searchable(name = "locationname", group = Searchable.Group.CLAUSES)
    public String locationName;

    @Searchable(name = "aadhaarnumber", group = Searchable.Group.CLAUSES)
    public String aadhaarNumber;

    @Searchable(name = "ulbCode", group = Searchable.Group.CLAUSES)
    public String ulbcode;

    @Searchable(name = "consumercode", group = Searchable.Group.COMMON)
    public String consumerCode;

    @Searchable(name = "propertygeo", group = Searchable.Group.COMMON)
    public String propertyGeo;

    @Searchable(name = "boundarygeo", group = Searchable.Group.COMMON)
    public String boundaryGeo;

    @Searchable(name = "ptassesmentno", group = Searchable.Group.COMMON)
    public String ptAssesmentNo;

    @Searchable(name = "propertyaddress", group = Searchable.Group.SEARCHABLE)
    public String propertyAddress;

    @Searchable(name = "annualcollection", group = Searchable.Group.SEARCHABLE)
    public Double annualCollection;

    @Searchable(name = "annualdemand", group = Searchable.Group.SEARCHABLE)
    public Double annualDemand;

    @Searchable(name = "arrearbalance", group = Searchable.Group.SEARCHABLE)
    public Double arrearBalance;

    @Searchable(name = "arrearcollection", group = Searchable.Group.SEARCHABLE)
    public Double arrearCollection;

    @Searchable(name = "arreardemand", group = Searchable.Group.SEARCHABLE)
    public Double arrearDemand;

    @Searchable(name = "arvamount", group = Searchable.Group.SEARCHABLE)
    public Double arvAmount;

    @Searchable(name = "firstinstallmentdemand", group = Searchable.Group.SEARCHABLE)
    public Double firstInstallmentDemand;

    @Searchable(name = "firstinstallmentcollection", group = Searchable.Group.SEARCHABLE)
    public Double firstInstallmentCollection;

    @Searchable(name = "secondinstallmentdemand", group = Searchable.Group.SEARCHABLE)
    public Double secondInstallmentDemand;

    @Searchable(name = "secondinstallmentcollection", group = Searchable.Group.SEARCHABLE)
    public Double secondInstallmentCollection;

    @Searchable(name = "consumername", group = Searchable.Group.SEARCHABLE)
    public String consumerName;

    @Searchable(name = "totalbalance", group = Searchable.Group.SEARCHABLE)
    public Double totalBalance;

    @Searchable(name = "annualbalance", group = Searchable.Group.SEARCHABLE)
    public Double annualBalance;

    @Searchable(name = "totaldemand", group = Searchable.Group.SEARCHABLE)
    public Double totalDemand;

    public Double getBuiltupArea() {
        return builtupArea;
    }

    public void setBuiltupArea(Double builtupArea) {
        this.builtupArea = builtupArea;
    }

    public String getCityGrade() {
        return cityGrade;
    }

    public void setCityGrade(String cityGrade) {
        this.cityGrade = cityGrade;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAdminWardName() {
        return adminWardName;
    }

    public void setAdminWardName(String adminWardName) {
        this.adminWardName = adminWardName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRevWardName() {
        return revWardName;
    }

    public void setRevWardName(String revWardName) {
        this.revWardName = revWardName;
    }

    public String getRevZoneName() {
        return revZoneName;
    }

    public void setRevZoneName(String revZoneName) {
        this.revZoneName = revZoneName;
    }

    public String getSitalArea() {
        return sitalArea;
    }

    public void setSitalArea(String sitalArea) {
        this.sitalArea = sitalArea;
    }

    public String getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(String propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getUlbcode() {
        return ulbcode;
    }

    public void setUlbcode(String ulbcode) {
        this.ulbcode = ulbcode;
    }

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getPropertyGeo() {
        return propertyGeo;
    }

    public void setPropertyGeo(String propertyGeo) {
        this.propertyGeo = propertyGeo;
    }

    public String getBoundaryGeo() {
        return boundaryGeo;
    }

    public void setBoundaryGeo(String boundaryGeo) {
        this.boundaryGeo = boundaryGeo;
    }

    public String getPtAssesmentNo() {
        return ptAssesmentNo;
    }

    public void setPtAssesmentNo(String ptAssesmentNo) {
        this.ptAssesmentNo = ptAssesmentNo;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public Double getAnnualCollection() {
        return annualCollection;
    }

    public void setAnnualCollection(Double annualCollection) {
        this.annualCollection = annualCollection;
    }

    public Double getAnnualDemand() {
        return annualDemand;
    }

    public void setAnnualDemand(Double annualDemand) {
        this.annualDemand = annualDemand;
    }

    public Double getArrearBalance() {
        return arrearBalance;
    }

    public void setArrearBalance(Double arrearBalance) {
        this.arrearBalance = arrearBalance;
    }

    public Double getArrearCollection() {
        return arrearCollection;
    }

    public void setArrearCollection(Double arrearCollection) {
        this.arrearCollection = arrearCollection;
    }

    public Double getArrearDemand() {
        return arrearDemand;
    }

    public void setArrearDemand(Double arrearDemand) {
        this.arrearDemand = arrearDemand;
    }

    public Double getArvAmount() {
        return arvAmount;
    }

    public void setArvAmount(Double arvAmount) {
        this.arvAmount = arvAmount;
    }

    public Double getFirstInstallmentDemand() {
        return firstInstallmentDemand;
    }

    public void setFirstInstallmentDemand(Double firstInstallmentDemand) {
        this.firstInstallmentDemand = firstInstallmentDemand;
    }

    public Double getFirstInstallmentCollection() {
        return firstInstallmentCollection;
    }

    public void setFirstInstallmentCollection(Double firstInstallmentCollection) {
        this.firstInstallmentCollection = firstInstallmentCollection;
    }

    public Double getSecondInstallmentDemand() {
        return secondInstallmentDemand;
    }

    public void setSecondInstallmentDemand(Double secondInstallmentDemand) {
        this.secondInstallmentDemand = secondInstallmentDemand;
    }

    public Double getSecondInstallmentCollection() {
        return secondInstallmentCollection;
    }

    public void setSecondInstallmentCollection(Double secondInstallmentCollection) {
        this.secondInstallmentCollection = secondInstallmentCollection;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Double getAnnualBalance() {
        return annualBalance;
    }

    public void setAnnualBalance(Double annualBalance) {
        this.annualBalance = annualBalance;
    }

    public Double getTotalDemand() {
        return totalDemand;
    }

    public void setTotalDemand(Double totalDemand) {
        this.totalDemand = totalDemand;
    }

    @Override
    public String getIndexId() {
        return ptAssesmentNo + "-" + consumerCode;
    }

}
