/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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
package org.egov.stms.elasticSearch.entity;

import org.jboss.logging.Logger;

import javax.validation.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailySTCollectionReportSearch {
    private static final Logger logger = Logger.getLogger(DailySTCollectionReportSearch.class);
    private String fromDate;
    private String toDate;
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dtft = new SimpleDateFormat("dd/MM/yyyy");
    private List<String> consumerCode = new ArrayList<String>();
    private String collectionMode;
    private String collectionOperator;
    private String revenueWard;
    private String searchText;
    private String ulbName;
    private String consumerNumber;
    private String shscNumber;
    private String applicantName;
    private String mobileNumber;
    private String doorNumber;
    private String applicationDate;


    private String status;

    public List<String> getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final List<String> consumerCode) {
        this.consumerCode = consumerCode;
    }

    public void setFromDate(final String fromDate) {
        if (null != fromDate)
            try {
                if (logger.isDebugEnabled())
                    logger.debug("Date Range From start.. :" + ft.format(dtft.parse(fromDate)));
                this.fromDate = ft.format(dtft.parse(fromDate));
            } catch (final ParseException e) {
                throw new ValidationException(e.getMessage());
            }
    }

    public void setToDate(final String toDate) {
        final Calendar cal = Calendar.getInstance();
        if (null != toDate)
            try {
                cal.setTime(dtft.parse(toDate));
                cal.add(Calendar.DAY_OF_YEAR, 1);
                if (logger.isDebugEnabled())
                    logger.debug("Date Range Till .. :" + ft.format(cal.getTime()));
                this.toDate = ft.format(cal.getTime());
            } catch (final ParseException e) {
                throw new ValidationException(e.getMessage());
            }
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getCollectionMode() {
        return collectionMode;
    }

    public void setCollectionMode(final String collectionMode) {
        this.collectionMode = collectionMode;
    }

    public String getCollectionOperator() {
        return collectionOperator;
    }

    public void setCollectionOperator(final String collectionOperator) {
        this.collectionOperator = collectionOperator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(final String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(final String searchText) {
        this.searchText = searchText;
    }

    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(final String ulbName) {
        this.ulbName = ulbName;
    }

   /* public Filters searchCollectionFilters() {
        final List<Filter> andFilters = new ArrayList<>(0);
        andFilters.add(termsStringFilter(SewerageTaxConstants.CLAUSES_CITYNAME, ulbName));
        andFilters.add(termsStringFilter(SewerageTaxConstants.CLAUSES_CHANNEL, collectionMode));
        andFilters.add(termsStringFilter(SewerageTaxConstants.CLAUSES_RECEIPT, collectionOperator));
        andFilters.add(termsStringFilter(SewerageTaxConstants.CLAUSES_BILLING_SERVICE,
                SewerageTaxConstants.APPL_INDEX_MODULE_NAME));
        andFilters.add(termsStringFilter(SewerageTaxConstants.CLAUSES_STATUS, status));
        andFilters.add(rangeFilter(SewerageTaxConstants.SEARCHABLE_RECEIPT_DATE, fromDate, toDate));
        if (logger.isDebugEnabled())
            logger.debug("finished filters");
        logger.info("$$$$$$$$$$$$$$$$ Filters : " + andFilters);
        return Filters.withAndFilters(andFilters);
    }
    
    public Filters searchFilters() { 
        final List<Filter> andFilters = new ArrayList<>(0);
        andFilters.add(termsStringFilter(SewerageTaxConstants.SEARCHABLE_SHSCNO, shscNumber));
        andFilters.add(termsStringFilter(SewerageTaxConstants.CLAUSES_CITYNAME, ulbName));
        andFilters.add(queryStringFilter(SewerageTaxConstants.SEARCHABLE_CONSUMER_NAME, applicantName));
        andFilters.add(queryStringFilter(SewerageTaxConstants.CLAUSES_MOBILENO , mobileNumber));
        andFilters.add(queryStringFilter(SewerageTaxConstants.CLAUSES_DOORNO, doorNumber));
        andFilters.add(termsStringFilter(SewerageTaxConstants.CLAUSES_REVWARD_NAME, revenueWard));
        andFilters.add(queryStringFilter(SewerageTaxConstants.CLAUSES_APPLICATION_DATE,applicationDate));
        andFilters.add(termsStringFilter(SewerageTaxConstants.CLAUSES_APPLICATIONNO,consumerNumber));
        if (logger.isDebugEnabled())
            logger.debug("finished filters");
        return Filters.withAndFilters(andFilters);
    }

    public Filters searchConnectionForWardFilters() {
        final List<Filter> andFilters = new ArrayList<>(0);
        andFilters.add(termsStringFilter(SewerageTaxConstants.CLAUSES_CITYNAME, ulbName));
        andFilters.add(termsStringFilter(SewerageTaxConstants.CLAUSES_REVWARD_NAME, revenueWard));
        return Filters.withAndFilters(andFilters);
    }*/

    public String searchQuery() {
        return searchText;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getShscNumber() {
        return shscNumber;
    }

    public void setShscNumber(String shscNumber) {
        this.shscNumber = shscNumber;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

}