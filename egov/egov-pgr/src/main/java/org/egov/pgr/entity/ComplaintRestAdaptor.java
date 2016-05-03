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

package org.egov.pgr.entity;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class ComplaintRestAdaptor implements JsonSerializer<Complaint>, JsonDeserializer<Complaint> {
    private static final Logger LOG = LoggerFactory.getLogger(ComplaintRestAdaptor.class);

    @Override
    public JsonElement serialize(final Complaint complaint, final Type type, final JsonSerializationContext jsc) {
        final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy hh:mm a");
        String resolutionDate="";
        resolutionDate=formatter.print(complaint.getEscalationDate());
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("complaintnumber", complaint.getCrn());
        jsonObject.addProperty("timetoresolve", "" + resolutionDate);
        jsonObject.addProperty("message",
                "Thank you for your submission. We apologize for the inconvenience caused to you. We will resolve the complaint by :"
                        + resolutionDate);
        return jsonObject;
    }

    @Override
    public Complaint deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) {
        final Complaint complaint = new Complaint();
        LOG.info("json" + json);

        final JsonObject object = json.getAsJsonObject();
        LOG.info("object" + object);
        try {

            new Complainant();
        } catch (final Exception e) {
            // Log.w(Constants.TAG,"Error deserializing note " + object,e);
        }
        return complaint;
    }

}
