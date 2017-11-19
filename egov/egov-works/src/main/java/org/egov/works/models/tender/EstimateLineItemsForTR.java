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
package org.egov.works.models.tender;

import org.egov.works.abstractestimate.entity.Activity;

import java.util.Date;
import java.util.List;

public class EstimateLineItemsForTR {

    public List<TenderResponseQuotes> getTenderResponseQuotes() {
        return tenderResponseQuotes;
    }

    public void setTenderResponseQuotes(final List<TenderResponseQuotes> tenderResponseQuotes) {
        this.tenderResponseQuotes = tenderResponseQuotes;
    }

    private Integer srlNo;
    private String code;
    private double quantity;
    private String description;
    private String summary;
    private double rate;
    private String uom;
    private double conversionFactor;
    private double amt;
    private Date estimateDate;
    private double marketRate;
    private Activity activity;
    private double negotiatedRate;
    private List<TenderResponseQuotes> tenderResponseQuotes;

    public Integer getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(final Integer srlNo) {
        this.srlNo = srlNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(final double quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(final double rate) {
        this.rate = rate;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(final String uom) {
        this.uom = uom;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(final double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(final double amt) {
        this.amt = amt;
    }

    public Date getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(final Date estimateDate) {
        this.estimateDate = estimateDate;
    }

    public double getMarketRate() {
        return marketRate;
    }

    public void setMarketRate(final double marketRate) {
        this.marketRate = marketRate;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(final Activity activity) {
        this.activity = activity;
    }

    public double getNegotiatedRate() {
        return negotiatedRate;
    }

    public void setNegotiatedRate(final double negotiatedRate) {
        this.negotiatedRate = negotiatedRate;
    }

}