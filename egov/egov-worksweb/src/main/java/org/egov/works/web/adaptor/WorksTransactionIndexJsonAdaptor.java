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

package org.egov.works.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.infra.utils.DateUtils;
import org.egov.works.elasticsearch.model.WorksMilestoneIndexResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.math.BigDecimal;

@Component
public class WorksTransactionIndexJsonAdaptor implements JsonSerializer<WorksMilestoneIndexResponse> {

    @Autowired
    private WorksReportJsonAdaptorHelper worksReportJsonAdaptorHelper;

    @Override
    public JsonElement serialize(final WorksMilestoneIndexResponse response, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (response != null) {
            jsonObject.addProperty("Fund", response.getFund());
            if (response.getScheme() != null)
                jsonObject.addProperty("Scheme", response.getScheme());
            else
                jsonObject.addProperty("Scheme", "NA");
            if (response.getSubscheme() != null)
                jsonObject.addProperty("Sub Scheme", response.getSubscheme());
            else
                jsonObject.addProperty("Sub Scheme", "NA");
            jsonObject.addProperty("Ward", response.getWard());
            jsonObject.addProperty("Estimate Number", response.getEstimatenumber());
            jsonObject.addProperty("Work Identification Number", response.getWin());
            jsonObject.addProperty("Name of the work", response.getNameofthework());
            if (response.getTotalestimatedcostinlakhs() != null)
                jsonObject.addProperty("Estimated cost in lakhs",
                        BigDecimal.valueOf(response.getTotalestimatedcostinlakhs() / 100000)
                                .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("Estimated cost in lakhs", "0.00");
            jsonObject.addProperty("Agreement Number", response.getAgreementnumber());
            jsonObject.addProperty("Agreement Date",
                    response.getAgreementdate() != null ? DateUtils.getDefaultFormattedDate(response.getAgreementdate()) : "");
            jsonObject.addProperty("Contract Period(in days)", response.getContractperiod());
            if (response.getTotalworkordervalueinlakhs() != null)
                jsonObject.addProperty("Work order value in lakhs",
                        BigDecimal.valueOf(response.getTotalworkordervalueinlakhs() / 100000)
                                .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("Work order value in lakhs", "0.00");
            if (response.getTotalbillamountinlakhs() != null)
                jsonObject.addProperty("Total bill amount in lakhs",
                        BigDecimal.valueOf(response.getTotalbillamountinlakhs() / 100000).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty("Total bill amount in lakhs", "0.00");
            if (response.getTotalpaidamountinlakhs() != null)
                jsonObject.addProperty("Total paid amount in lakhs",
                        BigDecimal.valueOf(response.getTotalpaidamountinlakhs() / 100000).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty("Total paid amount in lakhs", "0.00");

            final DateTime currentDate = new DateTime();
            switch (currentDate.getMonthOfYear()) {
            case 1:
                worksReportJsonAdaptorHelper.showJanuaryData(response, jsonObject, currentDate);
                break;
            case 2:
                worksReportJsonAdaptorHelper.showFebruaryData(response, jsonObject, currentDate);

                break;
            case 3:
                worksReportJsonAdaptorHelper.showMarchData(response, jsonObject, currentDate);

                break;
            case 4:
                worksReportJsonAdaptorHelper.showAprilData(response, jsonObject, currentDate);
                break;
            case 5:
                worksReportJsonAdaptorHelper.showMayData(response, jsonObject, currentDate);

                break;
            case 6:
                worksReportJsonAdaptorHelper.showJuneData(response, jsonObject, currentDate);
                break;
            case 7:
                worksReportJsonAdaptorHelper.showJulyData(response, jsonObject, currentDate);

                break;
            case 8:
                worksReportJsonAdaptorHelper.showAugustData(response, jsonObject, currentDate);

                break;
            case 9:
                worksReportJsonAdaptorHelper.showSeptemberData(response, jsonObject, currentDate);

                break;
            case 10:
                worksReportJsonAdaptorHelper.showOctoberData(response, jsonObject, currentDate);
                break;
            case 11:
                worksReportJsonAdaptorHelper.showNovemberData(response, jsonObject, currentDate);

                break;
            case 12:
                worksReportJsonAdaptorHelper.showDecemberData(response, jsonObject, currentDate);

                break;
            default:
                break;
            }

            jsonObject.addProperty("Financial progress %",
                    BigDecimal.valueOf(response.getFinancialprogress()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        return jsonObject;
    }

}