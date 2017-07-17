/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.stms.web.adapter;

import java.lang.reflect.Type;
import java.util.List;

import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.stms.entity.es.SewerageIndex;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class SewerageBaseRegisterAdaptor implements DataTableJsonAdapter<SewerageIndex> {

    @Override
    public JsonElement serialize(final DataTable<SewerageIndex> baseRegisterResponse, final Type type,
            final JsonSerializationContext jsc) {
        final List<SewerageIndex> baseRegisterResult = baseRegisterResponse.getData();
        final JsonArray baseRegisterResultData = new JsonArray();
        baseRegisterResult.forEach(baseForm -> {
            final JsonObject baseRegisterJson = new JsonObject();
            baseRegisterJson.addProperty("shscNumber", baseForm.getShscNumber());
            baseRegisterJson.addProperty("assementNo", baseForm.getPropertyIdentifier());
            baseRegisterJson.addProperty("ownerName", baseForm.getConsumerName());
            baseRegisterJson.addProperty("revenueWard", baseForm.getWard());
            baseRegisterJson.addProperty("doorNo", baseForm.getDoorNo());
            baseRegisterJson.addProperty("propertyType", baseForm.getPropertyType());
            baseRegisterJson.addProperty("residentialClosets", baseForm.getNoOfClosets_residential());
            baseRegisterJson.addProperty("nonResidentialClosets", baseForm.getNoOfClosets_nonResidential());
            baseRegisterJson.addProperty("period", baseForm.getPeriod());
            baseRegisterJson.addProperty("arrears", baseForm.getArrearAmount());
            baseRegisterJson.addProperty("currentDemand", baseForm.getDemandAmount());
            baseRegisterJson.addProperty("totalDemand", baseForm.getTotalAmount());
            baseRegisterJson.addProperty("arrearsCollected", baseForm.getCollectedArrearAmount());
            baseRegisterJson.addProperty("currentTaxCollected", baseForm.getCollectedDemandAmount());
            baseRegisterJson.addProperty("totalTaxCollected",
                    baseForm.getCollectedArrearAmount().add(baseForm.getCollectedDemandAmount()));
            baseRegisterJson.addProperty("advanceAmount", baseForm.getExtraAdvanceAmount());

            baseRegisterResultData.add(baseRegisterJson);
        });
        return enhance(baseRegisterResultData, baseRegisterResponse);
    }

}
