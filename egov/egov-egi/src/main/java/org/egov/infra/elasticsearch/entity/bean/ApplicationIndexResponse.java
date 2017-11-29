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

package org.egov.infra.elasticsearch.entity.bean;

import org.egov.infra.utils.StringUtils;

import java.util.List;

public class ApplicationIndexResponse {

    private long totalReceived = 0;
    private long totalClosed = 0;
    private long totalOpen = 0;
    private long todaysReceived = 0;
    private long todaysClosed = 0;
    private long totalBeyondSLA = 0;
    private long totalWithinSLA = 0;
    private long openBeyondSLA = 0;
    private long closedBeyondSLA = 0;
    private long totalCsc = 0;
    private long totalMeeseva = 0;
    private long totalOnline = 0;
    private long totalUlb = 0;
    private long totalOthers = 0;
    private String url=StringUtils.EMPTY;
    private String cityCode=StringUtils.EMPTY;
    private List<Trend> trend;
    private List<ApplicationDetails> details;
    private List<ServiceGroupDetails> serviceGroupDetails;
    private List<ServiceGroupTrend> serviceGroupTrend;
    private List<ServiceDetails> serviceDetails;
    private List<SourceTrend> sourceTrend;

    public long getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(final long totalReceived) {
        this.totalReceived = totalReceived;
    }

    public long getTotalClosed() {
        return totalClosed;
    }

    public void setTotalClosed(final long totalClosed) {
        this.totalClosed = totalClosed;
    }

    public long getTotalOpen() {
        return totalOpen;
    }

    public void setTotalOpen(final long totalOpen) {
        this.totalOpen = totalOpen;
    }

    public long getTodaysReceived() {
        return todaysReceived;
    }

    public void setTodaysReceived(final long todaysReceived) {
        this.todaysReceived = todaysReceived;
    }

    public long getTodaysClosed() {
        return todaysClosed;
    }

    public void setTodaysClosed(final long todaysClosed) {
        this.todaysClosed = todaysClosed;
    }

    public List<Trend> getTrend() {
        return trend;
    }

    public void setTrend(final List<Trend> trend) {
        this.trend = trend;
    }

    public List<ApplicationDetails> getDetails() {
        return details;
    }

    public void setDetails(final List<ApplicationDetails> details) {
        this.details = details;
    }

    public List<ServiceGroupDetails> getServiceGroupDetails() {
        return serviceGroupDetails;
    }

    public void setServiceGroupDetails(final List<ServiceGroupDetails> serviceGroupDetails) {
        this.serviceGroupDetails = serviceGroupDetails;
    }

    public List<ServiceGroupTrend> getServiceGroupTrend() {
        return serviceGroupTrend;
    }

    public void setServiceGroupTrend(final List<ServiceGroupTrend> serviceGroupTrend) {
        this.serviceGroupTrend = serviceGroupTrend;
    }

    public long getTotalBeyondSLA() {
        return totalBeyondSLA;
    }

    public void setTotalBeyondSLA(final long totalBeyondSLA) {
        this.totalBeyondSLA = totalBeyondSLA;
    }

    public long getTotalWithinSLA() {
        return totalWithinSLA;
    }

    public void setTotalWithinSLA(final long totalWithinSLA) {
        this.totalWithinSLA = totalWithinSLA;
    }

    public long getOpenBeyondSLA() {
        return openBeyondSLA;
    }

    public void setOpenBeyondSLA(final long openBeyondSLA) {
        this.openBeyondSLA = openBeyondSLA;
    }

    public long getClosedBeyondSLA() {
        return closedBeyondSLA;
    }

    public void setClosedBeyondSLA(final long closedBeyondSLA) {
        this.closedBeyondSLA = closedBeyondSLA;
    }

    public List<ServiceDetails> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(final List<ServiceDetails> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public long getTotalCsc() {
        return totalCsc;
    }

    public void setTotalCsc(final long totalCsc) {
        this.totalCsc = totalCsc;
    }

    public long getTotalMeeseva() {
        return totalMeeseva;
    }

    public void setTotalMeeseva(final long totalMeeseva) {
        this.totalMeeseva = totalMeeseva;
    }

    public long getTotalOnline() {
        return totalOnline;
    }

    public void setTotalOnline(final long totalOnline) {
        this.totalOnline = totalOnline;
    }

    public long getTotalUlb() {
        return totalUlb;
    }

    public void setTotalUlb(final long totalUlb) {
        this.totalUlb = totalUlb;
    }

    public long getTotalOthers() {
        return totalOthers;
    }

    public void setTotalOthers(final long totalOthers) {
        this.totalOthers = totalOthers;
    }

    public List<SourceTrend> getSourceTrend() {
        return sourceTrend;
    }

    public void setSourceTrend(final List<SourceTrend> sourceTrend) {
        this.sourceTrend = sourceTrend;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
