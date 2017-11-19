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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.validation.ValidationException;

import org.jboss.logging.Logger;

public class DailyWTCollectionReportSearch {
    private static final Logger logger = Logger.getLogger(DailyWTCollectionReportSearch.class);
    private String fromDate;
    private String toDate;
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat dtft = new SimpleDateFormat("dd/MM/yyyy");
    private String collectionMode;
    private String collectionOperator;
    private String revenueWard;
    private String searchText;
    private String status;
    private String ulbName;
    private String receiptnumber;
   
    
    public String getFromDate() {
        return fromDate;
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

    public String getToDate() {
        return toDate;
    }

    public void setToDate(final String toDate) {
        final Calendar cal = Calendar.getInstance();
        if (null != toDate)
            try {
                cal.setTime(dtft.parse(toDate));
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                if (logger.isDebugEnabled())
                    logger.debug("Date Range Till .. :" + ft.format(cal.getTime()));
                this.toDate = ft.format(cal.getTime());
            } catch (final ParseException e) {
                throw new ValidationException(e.getMessage());
            }
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

    public String searchQuery() {
        return searchText;
    }

    public String getReceiptnumber() {
        return receiptnumber;
    }

    public void setReceiptnumber(String receiptnumber) {
        this.receiptnumber = receiptnumber;
    }
   

  
}
