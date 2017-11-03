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

package org.egov.api.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.Position;

import java.lang.reflect.Type;

public class ComplaintStatusAdapter extends DataAdapter<StateHistory<Position>> {

    @Override
    public JsonElement serialize(StateHistory<Position> stateHistory, Type type, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        jo.addProperty("stateId", stateHistory.getState().getId());
        jo.addProperty("value", stateHistory.getValue());
        jo.addProperty("ownerPosotion", stateHistory.getOwnerPosition().getName());
        jo.addProperty("ownerUser", stateHistory.getOwnerPosition().getName());
        if (stateHistory.getOwnerUser() != null)
            jo.addProperty("createdBy", stateHistory.getOwnerUser().getUsername());
        jo.addProperty("createdDate", stateHistory.getCreatedDate().toString());
        jo.addProperty("lastModifiedBy", stateHistory.getLastModifiedBy().getUsername());
        jo.addProperty("lastModifiedDate", stateHistory.getLastModifiedDate().toString());
        jo.addProperty("comments", stateHistory.getComments());
        return jo;
    }

}
