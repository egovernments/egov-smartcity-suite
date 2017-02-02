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
package org.egov.lcms.web.adaptor;

import java.lang.reflect.Type;

import org.egov.infra.utils.DateUtils;
import org.egov.lcms.reports.entity.DailyBoardReportResults;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DailyBoardReportJsonAdapter implements JsonSerializer<DailyBoardReportResults> {

    @Override
    public JsonElement serialize(final DailyBoardReportResults dailyboardresult, final Type typeOfSrc,
            final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("caseNumber", dailyboardresult.getLegalCase().getCaseNumber());
        jsonObject.addProperty("caseTitle", dailyboardresult.getLegalCase().getCaseTitle());
        jsonObject.addProperty("courtName", dailyboardresult.getCourtName());
        jsonObject.addProperty("petitionType", dailyboardresult.getPetitionType());
        jsonObject.addProperty("petitionerName", dailyboardresult.getLegalCase().getPetitionersNames());
        jsonObject.addProperty("respondantName", dailyboardresult.getLegalCase().getRespondantNames());
        jsonObject.addProperty("standingCouncil", dailyboardresult.getLegalCase().getOppPartyAdvocate());
        jsonObject.addProperty("officerIncharge", dailyboardresult.getLegalCase().getOfficerIncharge().getName());
        jsonObject.addProperty("caseStatus", dailyboardresult.getCaseStatus());
        jsonObject.addProperty("nextDate", dailyboardresult.getLegalCase().getNextDate() == null ? ""
                : DateUtils.getDefaultFormattedDate(dailyboardresult.getLegalCase().getNextDate()));
        jsonObject.addProperty("lcNumber", dailyboardresult.getLegalCase().getLcNumber());
        return jsonObject;
    }

}
