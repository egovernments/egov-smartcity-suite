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
package org.egov.wtms.reports.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.wtms.application.entity.DataEntryConnectionReport;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

public class DataEntryConnectionReportAdaptor implements JsonSerializer<DataEntryConnectionReport> {

    @Override
    public JsonElement serialize(final DataEntryConnectionReport dataEntryConnectionReport, final Type typeOfSrc,
            final JsonSerializationContext context) {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("hscNo", dataEntryConnectionReport.getHscNo());
        jsonObject.addProperty("assessmentNo", dataEntryConnectionReport.getAssessmentNo());
        jsonObject.addProperty("ownerName", dataEntryConnectionReport.getOwnerName());
        jsonObject.addProperty("wardName", dataEntryConnectionReport.getZone()+"/"+dataEntryConnectionReport.getRevenueWard()+"/"+dataEntryConnectionReport.getBlock());
        jsonObject.addProperty("locality", dataEntryConnectionReport.getLocality());
        jsonObject.addProperty("address", dataEntryConnectionReport.getAddress());
        jsonObject.addProperty("mobileNumber", dataEntryConnectionReport.getMobileNumber());
        jsonObject.addProperty("email", dataEntryConnectionReport.getEmail());
        jsonObject.addProperty("waterSource", dataEntryConnectionReport.getWaterSource());
        jsonObject.addProperty("propertyType", dataEntryConnectionReport.getPropertyType());
        jsonObject.addProperty("applicationType", dataEntryConnectionReport.getApplicationType());
        jsonObject.addProperty("connectionType", dataEntryConnectionReport.getConnectionType());
        jsonObject.addProperty("usageType", dataEntryConnectionReport.getUsageType());
        jsonObject.addProperty("category", dataEntryConnectionReport.getCategory());
        jsonObject.addProperty("pipeSizeInInch", dataEntryConnectionReport.getPipeSizeInInch());
        jsonObject.addProperty("aadharNumber", dataEntryConnectionReport.getAadharNumber());
        jsonObject.addProperty("noOfPersons", dataEntryConnectionReport.getNoOfPersons());
        jsonObject.addProperty("noOfRooms", dataEntryConnectionReport.getNoOfRooms());
        jsonObject.addProperty("sumpCapacity", dataEntryConnectionReport.getSumpCapacity());
        jsonObject.addProperty("donationCharges", dataEntryConnectionReport.getDonationCharges());
        jsonObject.addProperty("connectionDate", dateformat.format(dataEntryConnectionReport.getConnectionDate())
                .toString());
        jsonObject.addProperty("monthlyFee", dataEntryConnectionReport.getMonthlyFee());
        jsonObject.addProperty("waterTaxDue", dataEntryConnectionReport.getWaterTaxDue());
        jsonObject.addProperty("propertyTaxDue", dataEntryConnectionReport.getPropertyTaxDue());
        return jsonObject;
    }

}
