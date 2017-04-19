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
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.persistence.entity.component.Period;
import org.egov.works.masters.entity.SORRate;
import org.egov.works.masters.entity.ScheduleOfRate;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class ScheduleOfRateSearchJsonAdaptor implements JsonSerializer<ScheduleOfRate> {
    @Override
    public JsonElement serialize(final ScheduleOfRate scheduleOfRate, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        if (scheduleOfRate != null) {
            jsonObject.addProperty("sorCode", scheduleOfRate.getCode());
            jsonObject.addProperty("unitOfMeasure", scheduleOfRate.getUom().getUom());
            jsonObject.addProperty("sorDescription", scheduleOfRate.getDescription());
            final List<SORRate> sorRates = scheduleOfRate.getSorRates();
            if (!sorRates.isEmpty()) {
                jsonObject.addProperty("rate", sorRates.get(sorRates.size() - 1).getRate().getValue());
                final Period validity = scheduleOfRate.getSorRates().get(0).getValidity();
                if (validity != null)
                    jsonObject.addProperty("fromDate", formatter.format(validity.getStartDate()));
                else
                    jsonObject.addProperty("fromDate", StringUtils.EMPTY);
                if (validity != null && validity.getEndDate() != null)
                    jsonObject.addProperty("toDate", formatter.format(validity.getEndDate()));
                else
                    jsonObject.addProperty("toDate", StringUtils.EMPTY);
            } else
                jsonObject.addProperty("rate", StringUtils.EMPTY);
            jsonObject.addProperty("scheduleOfRateId", scheduleOfRate.getId());
        }
        return jsonObject;
    }
}
