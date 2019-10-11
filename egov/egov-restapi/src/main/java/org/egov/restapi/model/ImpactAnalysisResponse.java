/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

public class ImpactAnalysisResponse {
    private Long date;
    private double revenueCollected;
    private double receiptcount;
    private double complaintcount;
    private double noOfCitizenRegistered;
    private double employeeRegistered;
    private double servicesApplied;
    private double vouchercount;
    private double agreementcount;
    private String revenueModule;
    private Integer ulbsCovered;

    public ImpactAnalysisResponse() {

    }

    public ImpactAnalysisResponse(String revenueModule, Integer ulbsCovered) {
        this.revenueModule = revenueModule;
        this.ulbsCovered = ulbsCovered;
    }

    public double getReceiptcount() {
        return receiptcount;
    }

    public void setReceiptcount(double receiptcount) {
        this.receiptcount = receiptcount;
    }

    public double getComplaintcount() {
        return complaintcount;
    }

    public void setComplaintcount(double complaintcount) {
        this.complaintcount = complaintcount;
    }

    public double getEmployeeRegistered() {
        return employeeRegistered;
    }

    public void setEmployeeRegistered(double employeeRegistered) {
        this.employeeRegistered = employeeRegistered;
    }

    public double getVouchercount() {
        return vouchercount;
    }

    public void setVouchercount(double vouchercount) {
        this.vouchercount = vouchercount;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public double getAgreementcount() {
        return agreementcount;
    }

    public void setAgreementcount(double agreementcount) {
        this.agreementcount = agreementcount;
    }

    public double getRevenueCollected() {
        return revenueCollected;
    }

    public void setRevenueCollected(double revenueCollected) {
        this.revenueCollected = revenueCollected;
    }

    public double getNoOfCitizenRegistered() {
        return noOfCitizenRegistered;
    }

    public void setNoOfCitizenRegistered(double noOfCitizenRegistered) {
        this.noOfCitizenRegistered = noOfCitizenRegistered;
    }

    public double getServicesApplied() {
        return servicesApplied;
    }

    public void setServicesApplied(double servicesApplied) {
        this.servicesApplied = servicesApplied;
    }

    public String getRevenueModule() {
        return revenueModule;
    }

    public void setRevenueModule(String revenueModule) {
        this.revenueModule = revenueModule;
    }

    public Integer getUlbsCovered() {
        return ulbsCovered;
    }

    public void setUlbsCovered(Integer ulbsCovered) {
        this.ulbsCovered = ulbsCovered;
    }

}
