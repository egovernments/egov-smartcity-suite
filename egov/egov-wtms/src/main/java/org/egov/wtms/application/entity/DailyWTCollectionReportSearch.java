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
package org.egov.wtms.application.entity;

import org.jboss.logging.Logger;

import javax.validation.ValidationException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyWTCollectionReportSearch {
    private static final Logger logger = Logger.getLogger(DailyWTCollectionReportSearch.class);
    private String fromDate;
    private String toDate;
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat dtft = new SimpleDateFormat("dd/MM/yyyy");
    private List<String> consumerCode = new ArrayList<String>();
    private String collectionMode;
    private String collectionOperator;
    private String revenueWard;
    private String searchText;
    private String ulbName;
    private String receiptnumber;
    private String consumercode;
    private String consumername;
    private String channel;
    private String paymentmode;
    private String status;
    private String installmentfrom;
    private String installmentto;
    private Double arrearamount;
    private Double currentamount;
    private Double advanceamount;
    private Double totalamount;


    public List<String> getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(final List<String> consumerCode) {
        this.consumerCode = consumerCode;
    }

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

    /*public Filters searchCollectionFilters() {
        final List<Filter> andFilters = new ArrayList<>(0);
        andFilters.add(termsStringFilter("clauses.cityname", ulbName));
        andFilters.add(termsStringFilter("clauses.channel", collectionMode));
        andFilters.add(termsStringFilter("clauses.receiptcreator", collectionOperator));
        andFilters.add(termsStringFilter("clauses.billingservice", "Water Tax"));
        andFilters.add(termsStringFilter("clauses.status", status));
        if (!consumerCode.isEmpty()) {
            final String[] consumerCodes = consumerCode.toArray(new String[consumerCode.size()]);
            andFilters.add(termsStringFilter("common.consumercode", consumerCodes));
        }
        andFilters.add(rangeFilter("searchable.receiptdate", fromDate, toDate));
        if (logger.isDebugEnabled())
            logger.debug("finished filters");
        logger.info("$$$$$$$$$$$$$$$$ Filters : " + andFilters);
        return Filters.withAndFilters(andFilters);
    }

    public Filters searchConnectionForWardFilters() {
        final List<Filter> andFilters = new ArrayList<>(0);
        andFilters.add(termsStringFilter("clauses.ulbname", ulbName));
        andFilters.add(termsStringFilter("clauses.ward", revenueWard));
        return Filters.withAndFilters(andFilters);
    }*/

    public String searchQuery() {
        return searchText;
    }

    public String getReceiptnumber() {
        return receiptnumber;
    }

    public void setReceiptnumber(String receiptnumber) {
        this.receiptnumber = receiptnumber;
    }

    public String getConsumercode() {
        return consumercode;
    }

    public void setConsumercode(String consumercode) {
        this.consumercode = consumercode;
    }

    public String getConsumername() {
        return consumername;
    }

    public void setConsumername(String consumername) {
        this.consumername = consumername;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getInstallmentfrom() {
        return installmentfrom;
    }

    public void setInstallmentfrom(String installmentfrom) {
        this.installmentfrom = installmentfrom;
    }

    public String getInstallmentto() {
        return installmentto;
    }

    public void setInstallmentto(String installmentto) {
        this.installmentto = installmentto;
    }

    public Double getArrearamount() {
        return arrearamount;
    }

    public void setArrearamount(Double arrearamount) {
        this.arrearamount = arrearamount;
    }

    public Double getCurrentamount() {
        return currentamount;
    }

    public void setCurrentamount(Double currentamount) {
        this.currentamount = currentamount;
    }

    public Double getAdvanceamount() {
        return advanceamount;
    }

    public void setAdvanceamount(Double advanceamount) {
        this.advanceamount = advanceamount;
    }

    public Double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(Double totalamount) {
        this.totalamount = totalamount;
    }

  
}
