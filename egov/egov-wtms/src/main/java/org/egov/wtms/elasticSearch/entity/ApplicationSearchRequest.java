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
package org.egov.wtms.elasticSearch.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ApplicationSearchRequest {
    private String searchText;
    private String moduleName;
    private String applicationType;
    private String applicationNumber;
    private String consumerCode;
    private String applicantName;
    private String mobileNumber;
    private String fromDate;
    private String toDate;
    private String cityName;
    private String applicationStatus;
    private String source;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(final String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(final String fromDate) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dtft = new SimpleDateFormat("dd/MM/yyyy");
        if (null != fromDate)
            try {
                this.fromDate = ft.format(dtft.parse(fromDate));
            } catch (final ParseException e) {

            }
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(final String toDate) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dtft = new SimpleDateFormat("dd/MM/yyyy");
        final Calendar cal = Calendar.getInstance();
        if (null != toDate)
            try {
                cal.setTime(dtft.parse(toDate));
                cal.add(Calendar.DAY_OF_YEAR, 1);
                this.toDate = ft.format(cal.getTime());
            } catch (final ParseException e) {

            }
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
    }

    public void setSearchText(final String searchText) {
        this.searchText = searchText;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    /*public Filters searchFilters() {
        final List<Filter> andFilters = new ArrayList<>(0);
        andFilters.add(termsStringFilter("clauses.cityname", cityName));
        andFilters.add(queryStringFilter("searchable.applicationnumber", applicationNumber != null
                && !applicationNumber.trim().isEmpty() ? "\"" + applicationNumber + "\"" : applicationNumber));
        andFilters.add(termsStringFilter("clauses.modulename", moduleName));
        andFilters.add(termsStringFilter("clauses.applicationtype", applicationType));
        andFilters.add(termsStringFilter("clauses.channel", source));
        andFilters.add(queryStringFilter("searchable.applicantname", applicantName));
        andFilters.add(queryStringFilter("searchable.consumercode", consumerCode));
        if(applicationStatus !=null && applicationStatus.equals(WaterTaxConstants.APPLICATIONSTATUSOPEN))
            andFilters.add(queryStringFilter("searchable.isclosed",Integer.toString(1) ));
        else   if(applicationStatus !=null && applicationStatus.equals(WaterTaxConstants.APPLICATIONSTATUSCLOSED))
            andFilters.add(queryStringFilter("searchable.isclosed",Integer.toString(0) ));
            
        andFilters.add(queryStringFilter("searchable.mobilenumber", mobileNumber));
        andFilters.add(rangeFilter("searchable.applicationdate", fromDate, toDate));
        if (logger.isDebugEnabled())
            logger.debug("finished filters");
        logger.info("$$$$$$$$$$$$$$$$ Filters : " + andFilters);
        return Filters.withAndFilters(andFilters);
    }*/

    public String searchQuery() {
        return searchText;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }


}
