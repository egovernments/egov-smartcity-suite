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
package org.egov.wtms.application.entity;

public class BaseRegisterResult {

    private String consumerNo;
    private String oldConsumerNo;
    private String doorNo;
    private String assementNo;
    private String ownerName;
    private String categoryType;
    private String period;
    private String connectionType;
    private Double arrears;
    private Double current;
    private Double arrearsCollection;
    private Double currentCollection;
    private Double totalDemand;
    private Double totalCollection;
    private String usageType;
    private String pipeSize;
    private String waterSource;
    private Double monthlyRate;

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(final String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(final String doorNo) {
        this.doorNo = doorNo;
    }

    public String getAssementNo() {
        return assementNo;
    }

    public void setAssementNo(final String assementNo) {
        this.assementNo = assementNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(final String categoryType) {
        this.categoryType = categoryType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(final String period) {
        this.period = period;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(final Double current) {
        this.current = current;
    }

    public Double getArrears() {
        return arrears;
    }

    public void setArrears(final Double arrears) {
        this.arrears = arrears;
    }

    public Double getTotalDemand() {
        return totalDemand;
    }

    public void setTotalDemand(final Double totalDemand) {
        this.totalDemand = totalDemand;
    }

    public Double getArrearsCollection() {
        return arrearsCollection;
    }

    public void setArrearsCollection(final Double arrearsCollection) {
        this.arrearsCollection = arrearsCollection;
    }

    public Double getCurrentCollection() {
        return currentCollection;
    }

    public void setCurrentCollection(final Double currentCollection) {
        this.currentCollection = currentCollection;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(final String connectionType) {
        this.connectionType = connectionType;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(final String usageType) {
        this.usageType = usageType;
    }

    public String getPipeSize() {
        return pipeSize;
    }

    public void setPipeSize(final String pipeSize) {
        this.pipeSize = pipeSize;
    }

    public Double getTotalCollection() {
        return totalCollection;
    }

    public void setTotalCollection(final Double totalCollection) {
        this.totalCollection = totalCollection;
    }

    public Double getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(final Double monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(final String waterSource) {
        this.waterSource = waterSource;
    }

    public String getOldConsumerNo() {
        return oldConsumerNo;
    }

    public void setOldConsumerNo(final String oldConsumerNo) {
        this.oldConsumerNo = oldConsumerNo;
    }

}
