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
package org.egov.wtms.es.entity;

public class ConnectionSearchRequest {
    private String searchText;
    private String consumerCode;
    private String applicantName;
    private String locality;
    private String mobileNumber;
    private String ulbName;
    private String revenueWard;
    private String doorNumber;
private String watersource;
private Long propertyTaxDue;
 private boolean islegacy;
 private String address;
 private String mobilenumber;
    private Long numberofperson;
 private Long totaldue;
private String usage;
private String propertytype;
 private String ulbname;
private String consumercode;
 private String ward;
private String applicationcode;
 private String districtname;
 private String zone;
 private String adminward;
 private String grade;
 private String bpaid;
 private String regionname;
 private String pipesize;
 private String doorno;
 private String category;
 private String connectiontype;
 private String propertyid;
private String status;
private Long monthlyRate;
 private String aadhaarnumber;
 private Long waterTaxDue;
    private Long arrearsDue;
 private String consumername;
 private Long currentDue;
private Long arrearsDemand;
private Long currentDemand;



    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(final String locality) {
        this.locality = locality;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

    public void setSearchText(final String searchText) {
        this.searchText = searchText;
    }

   /* public Filters searchFilters() {
        final List<Filter> andFilters = new ArrayList<>(0);
        andFilters.add(termsStringFilter("clauses.ulbname", ulbName));
        andFilters.add(queryStringFilter("searchable.consumername", applicantName));
        andFilters.add(queryStringFilter("clauses.consumercode", consumerCode));
        andFilters.add(queryStringFilter("searchable.locality", locality));
        andFilters.add(queryStringFilter("clauses.mobilenumber", mobileNumber));
        andFilters.add(termsStringFilter("clauses.doorno", doorNumber));
        andFilters.add(termsStringFilter("clauses.ward", revenueWard));
        if (logger.isDebugEnabled())
            logger.debug("finished filters");
        return Filters.withAndFilters(andFilters);
    }
*/
    public String searchQuery() {
        return searchText;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getWatersource() {
        return watersource;
    }

    public void setWatersource(String watersource) {
        this.watersource = watersource;
    }

    public boolean isIslegacy() {
        return islegacy;
    }

    public void setIslegacy(boolean islegacy) {
        this.islegacy = islegacy;
    }

    

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public Long getNumberofperson() {
        return numberofperson;
    }

    public void setNumberofperson(Long numberofperson) {
        this.numberofperson = numberofperson;
    }

    public Long getTotaldue() {
        return totaldue;
    }

    public void setTotaldue(Long totaldue) {
        this.totaldue = totaldue;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getPropertytype() {
        return propertytype;
    }

    public void setPropertytype(String propertytype) {
        this.propertytype = propertytype;
    }

    public String getUlbname() {
        return ulbname;
    }

    public void setUlbname(String ulbname) {
        this.ulbname = ulbname;
    }

    public String getConsumercode() {
        return consumercode;
    }

    public void setConsumercode(String consumercode) {
        this.consumercode = consumercode;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getApplicationcode() {
        return applicationcode;
    }

    public void setApplicationcode(String applicationcode) {
        this.applicationcode = applicationcode;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getAdminward() {
        return adminward;
    }

    public void setAdminward(String adminward) {
        this.adminward = adminward;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getBpaid() {
        return bpaid;
    }

    public void setBpaid(String bpaid) {
        this.bpaid = bpaid;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public String getPipesize() {
        return pipesize;
    }

    public void setPipesize(String pipesize) {
        this.pipesize = pipesize;
    }

    public String getDoorno() {
        return doorno;
    }

    public void setDoorno(String doorno) {
        this.doorno = doorno;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getConnectiontype() {
        return connectiontype;
    }

    public void setConnectiontype(String connectiontype) {
        this.connectiontype = connectiontype;
    }

    public String getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(String propertyid) {
        this.propertyid = propertyid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(Long monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public String getAadhaarnumber() {
        return aadhaarnumber;
    }

    public void setAadhaarnumber(String aadhaarnumber) {
        this.aadhaarnumber = aadhaarnumber;
    }

    public Long getWaterTaxDue() {
        return waterTaxDue;
    }

    public void setWaterTaxDue(Long waterTaxDue) {
        this.waterTaxDue = waterTaxDue;
    }

    public Long getArrearsDue() {
        return arrearsDue;
    }

    public void setArrearsDue(Long arrearsDue) {
        this.arrearsDue = arrearsDue;
    }

    public String getConsumername() {
        return consumername;
    }

    public void setConsumername(String consumername) {
        this.consumername = consumername;
    }

    public Long getCurrentDue() {
        return currentDue;
    }

    public void setCurrentDue(Long currentDue) {
        this.currentDue = currentDue;
    }

    public Long getArrearsDemand() {
        return arrearsDemand;
    }

    public void setArrearsDemand(Long arrearsDemand) {
        this.arrearsDemand = arrearsDemand;
    }

    public Long getCurrentDemand() {
        return currentDemand;
    }

    public void setCurrentDemand(Long currentDemand) {
        this.currentDemand = currentDemand;
    }

    public String getSearchText() {
        return searchText;
    }

    public Long getPropertyTaxDue() {
        return propertyTaxDue;
    }

    public void setPropertyTaxDue(Long propertyTaxDue) {
        this.propertyTaxDue = propertyTaxDue;
    }
    

}
