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
package org.egov.ptis.domain.entity.property;

import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;
import org.jboss.logging.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.egov.search.domain.Filter.rangeFilter;
import static org.egov.search.domain.Filter.termsStringFilter;

public class DailyCollectionReportSearch {

    private String fromDate;
    private String toDate;
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dtft = new SimpleDateFormat("dd/MM/yyyy");
    private String collectionMode;
    private String collectionOperator;
    private String status;
    private String revenueWard;
    private String searchText;
    private String ulbName;
    private List<String> consumerCode = new ArrayList<String>();

    private static final Logger logger = Logger.getLogger(DailyCollectionReportSearch.class);

    public void setFromDate(final String fromDate) {
        if (null != fromDate)
            try {
                if (logger.isDebugEnabled())
                    logger.debug("Date Range From start.. :" + ft.format(dtft.parse(fromDate)));
                this.fromDate = ft.format(dtft.parse(fromDate));
            } catch (final ParseException e) {
                e.printStackTrace();
            }
    }

    public void setToDate(final String toDate) {
        if (null != toDate)
            try {
                if (logger.isDebugEnabled())
                    logger.debug("Date Range Till .. :" + ft.format(dtft.parse(toDate)));
                this.toDate = ft.format(dtft.parse(toDate));
            } catch (final ParseException e) {
                e.printStackTrace();
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

    public void setCollectionMode(String collectionMode) {
        this.collectionMode = collectionMode;
    }

    public String getCollectionOperator() {
        return collectionOperator;
    }

    public void setCollectionOperator(String collectionOperator) {
        this.collectionOperator = collectionOperator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRevenueWard() {
        return revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<String> getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(List<String> consumerCode) {
        this.consumerCode = consumerCode;
    }
    
    public String getUlbName() {
        return ulbName;
    }

    public void setUlbName(String ulbName) {
        this.ulbName = ulbName;
    }

    public Filters searchCollectionFilters() {
        final List<Filter> andFilters = new ArrayList<>(0);
        andFilters.add(termsStringFilter("clauses.cityname", ulbName));
        andFilters.add(termsStringFilter("clauses.channel", collectionMode));
        andFilters.add(termsStringFilter("clauses.status", status));
        andFilters.add(termsStringFilter("clauses.receiptCreator", collectionOperator));
        andFilters.add(termsStringFilter("clauses.billingservice", "Property Tax"));
        if (!consumerCode.isEmpty()) {
            String[] consumerCodes = consumerCode.toArray(new String[consumerCode.size()]);
            andFilters.add(termsStringFilter("common.consumercode", consumerCodes));
        }
        andFilters.add(rangeFilter("searchable.receiptdate", fromDate, toDate));
        if (logger.isDebugEnabled())
            logger.debug("finished filters");
        logger.info("$$$$$$$$$$$$$$$$ Filters : " + andFilters);
        return Filters.withAndFilters(andFilters);
    }

    public Filters searchProperyForWardFilters() {
        final List<Filter> andFilters = new ArrayList<>(0);
        andFilters.add(termsStringFilter("clauses.cityname", ulbName));
        andFilters.add(termsStringFilter("clauses.revwardname", revenueWard));
        if (logger.isDebugEnabled())
            logger.debug("finished property tax filters");
        logger.info("$$$$$$$$$$$$$$$$ Filters : " + andFilters);
        return Filters.withAndFilters(andFilters);
    }

    public String searchQuery() {
        return searchText;
    }

}
