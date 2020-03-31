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

package org.egov.ptis.report.bean;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.domain.entity.property.view.FloorDetailsInfo;
import org.egov.ptis.domain.entity.property.view.InstDmdCollInfo;
import org.egov.ptis.domain.entity.property.view.PropertyMVInfo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class BuidingAgeWiseReportHelperAdaptor implements DataTableJsonAdapter<PropertyMVInfo> {

    @Override
    public JsonElement serialize(DataTable<PropertyMVInfo> buildingAgeWiseResponse, Type typeOfSrc,
            JsonSerializationContext context) {
        final List<PropertyMVInfo> buildingAgeWiseInfo = buildingAgeWiseResponse.getData();
        final JsonArray buildingAgeWiseDetails = new JsonArray();
        buildingAgeWiseInfo.forEach(buildingAgeWiseInfoObj -> {
            final JsonObject jsonObject = new JsonObject();
            boolean duplicateConstrDate = validateConstructiveDate(buildingAgeWiseInfoObj.getFloorDetails().stream().collect(Collectors.toList()));
            if (duplicateConstrDate)
                jsonObject.addProperty("assessmentNo", buildingAgeWiseInfoObj.getPropertyId());
            else
                jsonObject.addProperty("assessmentNo", buildingAgeWiseInfoObj.getPropertyId() + "[P]");
            jsonObject.addProperty("doorNo", buildingAgeWiseInfoObj.getHouseNo());
            jsonObject.addProperty("name", buildingAgeWiseInfoObj.getOwnerName());
            jsonObject.addProperty("zone", buildingAgeWiseInfoObj.getZone().getName());
            jsonObject.addProperty("ward", buildingAgeWiseInfoObj.getWard().getName());
            jsonObject.addProperty("block", buildingAgeWiseInfoObj.getBlock().getName());
            jsonObject.addProperty("locality", buildingAgeWiseInfoObj.getLocality().getName());
            for (InstDmdCollInfo demand : buildingAgeWiseInfoObj.getInstDmdColl()) {
                jsonObject.addProperty("propertyTax",
                        demand.getGeneralTax().add(demand.getLightTax()).add(demand.getVacantLandTax())
                                .add(demand.getDrainageTax()).add(demand.getWaterTax()).add(demand.getScavengeTax()));
                jsonObject.addProperty("eduTax", demand.getEduCessTax());
                jsonObject.addProperty("libCess", demand.getLibCessTax());
                jsonObject.addProperty("ucPenalty", demand.getUnauthPenaltyTax());
                jsonObject.addProperty("total", demand.getGeneralTax().add(demand.getLibCessTax()).add(demand.getEduCessTax())
                        .add(demand.getUnauthPenaltyTax()));

            }
            buildingAgeWiseDetails.add(jsonObject);
        });
        return enhance(buildingAgeWiseDetails, buildingAgeWiseResponse);
    }
    
    public Boolean validateConstructiveDate(List<FloorDetailsInfo> floorList) {
        Date firstFloorconstructionDate = floorList.get(0).getConstructionDate();
        return floorList.stream()
                .filter(floor -> floor != null)
                .filter(floor -> floor.getConstructionDate() != null)
                .allMatch(floor -> floor.getConstructionDate().equals(firstFloorconstructionDate));
    }
}