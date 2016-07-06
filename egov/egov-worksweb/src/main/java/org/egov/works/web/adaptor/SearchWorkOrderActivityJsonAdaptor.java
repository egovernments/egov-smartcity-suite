
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
package org.egov.works.web.adaptor;

import java.lang.reflect.Type;
import java.util.List;

import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchWorkOrderActivityJsonAdaptor implements JsonSerializer<WorkOrderActivity> {

    @Autowired
    private MBHeaderService mbHeaderService;

    @Override
    public JsonElement serialize(final WorkOrderActivity workOrderActivity, final Type typeOfSrc,
            final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        if (workOrderActivity.getActivity().getSchedule() != null) {
            jsonObject.addProperty("description", workOrderActivity.getActivity().getSchedule().getDescription());
            jsonObject.addProperty("summary", workOrderActivity.getActivity().getSchedule().getSummary());
            jsonObject.addProperty("sorNonSorType", "SOR");
            jsonObject.addProperty("sorCode", workOrderActivity.getActivity().getSchedule().getCode());
            jsonObject.addProperty("categoryType", workOrderActivity.getActivity().getSchedule().getScheduleCategory().getCode());
        } else {
            if (workOrderActivity.getActivity().getNonSor() != null) {
                jsonObject.addProperty("description", workOrderActivity.getActivity().getNonSor().getDescription());
                jsonObject.addProperty("summary", workOrderActivity.getActivity().getNonSor().getDescription());
            } else {
                jsonObject.addProperty("description", "");
                jsonObject.addProperty("summary", "");
            }
            jsonObject.addProperty("sorNonSorType", "Non SOR");
            jsonObject.addProperty("sorCode", "");
            jsonObject.addProperty("categoryType", "");
        }
        if (workOrderActivity.getActivity().getUom() != null)
            jsonObject.addProperty("uom", workOrderActivity.getActivity().getUom().getUom());
        else
            jsonObject.addProperty("uom", "");
        jsonObject.addProperty("approvedQuantity", workOrderActivity.getApprovedQuantity());
        jsonObject.addProperty("estimateRate", workOrderActivity.getActivity().getEstimateRate());
        jsonObject.addProperty("approvedRate", workOrderActivity.getApprovedRate());
        jsonObject.addProperty("approvedAmount", workOrderActivity.getApprovedAmount());
        jsonObject.addProperty("activityAmount", workOrderActivity.getActivity().getAmount().getValue());
        jsonObject.addProperty("unitRate", workOrderActivity.getActivity().getRate());
        jsonObject.addProperty("conversionFactor", workOrderActivity.getActivity().getConversionFactor());
        Double cumulativePreviousEntry = 0.0;
        cumulativePreviousEntry = mbHeaderService.getPreviousCumulativeQuantity(workOrderActivity.getMbHeaderId(),
                workOrderActivity.getId());

        if (cumulativePreviousEntry == null)
            jsonObject.addProperty("cumulativePreviousEntry", 0);
        else
            jsonObject.addProperty("cumulativePreviousEntry", cumulativePreviousEntry);

        jsonObject.addProperty("id", workOrderActivity.getId());
        return jsonObject;
    }

}
