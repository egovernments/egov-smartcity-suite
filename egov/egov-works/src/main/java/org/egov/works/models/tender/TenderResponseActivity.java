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

import org.apache.commons.collections.CollectionUtils;
import org.egov.infstr.models.BaseModel;
import org.egov.works.abstractestimate.entity.Activity;

import javax.validation.Valid;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TenderResponseActivity extends BaseModel {

    private static final long serialVersionUID = -8405385609284605838L;

    private TenderResponse tenderResponse;

    private Activity activity;

    // @Required(message="tenderResponseActivity.negotiatedRate.not.null")
    // @GreaterThan(value=0,message="tenderResponseActivity.negotiatedRate.non.negative")
    private double negotiatedRate;

    // @Required(message="tenderResponseActivity.negotiatedQuantity.not.null")
    // @GreaterThan(value=0,message="tenderResponseActivity.negotiatedQuantity.non.negative")
    private double negotiatedQuantity;

    private String schCode;
    private double assignedQty;

    private double estimatedQty;

    @Valid
    private List<TenderResponseQuotes> tenderResponseQuotes = new LinkedList<TenderResponseQuotes>();

    public TenderResponse getTenderResponse() {
        return tenderResponse;
    }

    public void setTenderResponse(final TenderResponse tenderResponse) {
        this.tenderResponse = tenderResponse;
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

    public double getNegotiatedQuantity() {
        return negotiatedQuantity;
    }

    public void setNegotiatedQuantity(final double negotiatedQuantity) {
        this.negotiatedQuantity = negotiatedQuantity;
    }

    public String getSchCode() {
        return schCode;
    }

    public void setSchCode(final String schCode) {
        this.schCode = schCode;
    }

    public List<TenderResponseQuotes> getTenderResponseQuotes() {
        return tenderResponseQuotes;
    }

    public Collection<TenderResponseQuotes> getTenderResponseQuotesList() {
        return CollectionUtils.select(tenderResponseQuotes,
                tenderReponseQuote -> (TenderResponseQuotes) tenderReponseQuote != null);

    }

    public void setTenderResponseQuotes(final List<TenderResponseQuotes> tenderResponseQuotes) {
        this.tenderResponseQuotes = tenderResponseQuotes;
    }

    public void addTenderResponseQuotes(final TenderResponseQuotes tenderResponseQuotes) {
        this.tenderResponseQuotes.add(tenderResponseQuotes);
    }

    public double getAssignedQty() {
        return assignedQty;
    }

    public void setAssignedQty(final double assignedQty) {
        this.assignedQty = assignedQty;
    }

    public double getEstimatedQty() {
        return estimatedQty;
    }

    public void setEstimatedQty(final double estimatedQty) {
        this.estimatedQty = estimatedQty;
    }

}
