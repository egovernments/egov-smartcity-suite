
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

import org.egov.works.mb.entity.MBHeader;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchMBHeaderJsonAdaptor implements JsonSerializer<MBHeader> {

    @Autowired
    private WorksUtils worksUtils;

    @Override
    public JsonElement serialize(final MBHeader mBHeader, final Type typeOfSrc,
            final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final WorkOrderEstimate workOrderEstimate = mBHeader.getWorkOrderEstimate();
        jsonObject.addProperty("mbheaderId", mBHeader.getId());
        jsonObject.addProperty("workOrderId", workOrderEstimate.getWorkOrder().getId());
        jsonObject.addProperty("estimateId", workOrderEstimate.getEstimate().getId());
        jsonObject.addProperty("estimateNumber", workOrderEstimate.getEstimate().getEstimateNumber());
        jsonObject.addProperty("workOrderNumber", workOrderEstimate.getWorkOrder().getWorkOrderNumber());
        jsonObject.addProperty("department", workOrderEstimate.getEstimate().getExecutingDepartment().getName());
        jsonObject.addProperty("contractor", workOrderEstimate.getWorkOrder().getContractor().getName());
        jsonObject.addProperty("agreemantAmount", workOrderEstimate.getWorkOrder().getWorkOrderAmount());
        // mbRefNo and mbDate are used in reloading mb's while creating contractor bill
        jsonObject.addProperty("mbRefNo", mBHeader.getMbRefNo());
        jsonObject.addProperty("mbDate", sdf.format(mBHeader.getMbDate()));
        jsonObject.addProperty("mbrefnumberdate", mBHeader.getMbRefNo() + "--" + sdf.format(mBHeader.getMbDate()));
        jsonObject.addProperty("mbamount", mBHeader.getMbAmount());
        jsonObject.addProperty("mbpageno", mBHeader.getFromPageNo() + "-" + mBHeader.getToPageNo());
        jsonObject.addProperty("status", mBHeader.getEgwStatus().getDescription());
        jsonObject.addProperty("createdBy", mBHeader.getCreatedBy().getName());
        if (mBHeader.getState() != null && mBHeader.getState().getOwnerPosition() != null)
            jsonObject.addProperty("currentOwner",
                    worksUtils.getApproverName(mBHeader.getState().getOwnerPosition().getId()));
        else
            jsonObject.addProperty("currentOwner", "N/A");
        return jsonObject;
    }

}
