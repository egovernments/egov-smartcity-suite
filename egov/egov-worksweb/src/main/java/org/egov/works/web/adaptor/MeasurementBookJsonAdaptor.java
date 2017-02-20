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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.infra.utils.StringUtils;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.service.MeasurementSheetService;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.offlinestatus.service.OfflineStatusService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrder.OfflineStatuses;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class MeasurementBookJsonAdaptor implements JsonSerializer<WorkOrderEstimate> {

    @Autowired
    private OfflineStatusService offlineStatusService;

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private MeasurementSheetService measurementSheetService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private CityService cityService;

    @Override
    public JsonElement serialize(final WorkOrderEstimate workOrderEstimate, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        jsonObject.addProperty("showHistory", false);
        if (workOrderEstimate != null) {
            setMBJsonValues(workOrderEstimate, jsonObject, sdf);
            final List<MBHeader> previousMBHeaders = mbHeaderService.getPreviousMBHeaders(-1L,
                    workOrderEstimate.getId());

            jsonObject.addProperty("previousMBDate", !previousMBHeaders.isEmpty()
                    ? sdf.format(previousMBHeaders.get(previousMBHeaders.size() - 1).getMbDate()) : StringUtils.EMPTY);
            jsonObject.addProperty("workOrderEstimateId", workOrderEstimate.getId());

            final AbstractEstimate ae = workOrderEstimate.getEstimate();
            if (ae != null && ae.isSpillOverFlag())
                jsonObject.addProperty("spillOverFlag", ae.isSpillOverFlag());
        }
        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_MB_QUANTITY_TOLERANCE_LEVEL);
        final AppConfigValues value = values.get(0);
        jsonObject.addProperty("quantityTolerance", value.getValue());
        final SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
        final Date cutOffDate = worksUtils.getCutOffDate();
        if (cutOffDate != null) {
            jsonObject.addProperty("cutOffDate", sdf.format(worksUtils.getCutOffDate()));
            jsonObject.addProperty("cutOffDateDisplay", fmt.format(worksUtils.getCutOffDate()));
        } else {
            jsonObject.addProperty("cutOffDate", StringUtils.EMPTY);
            jsonObject.addProperty("cutOffDateDisplay", StringUtils.EMPTY);
        }
        jsonObject.addProperty("additionalRule",
                (String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));

        return jsonObject;
    }

    private void setMBJsonValues(final WorkOrderEstimate workOrderEstimate, final JsonObject jsonObject,
            final SimpleDateFormat sdf) {
        final AbstractEstimate estimate = workOrderEstimate.getEstimate();
        final WorkOrder workOrder = workOrderEstimate.getWorkOrder();
        final Double totalMBAmountOfMBs = mbHeaderService.getTotalMBAmountOfMBs(null, workOrderEstimate.getId(),
                MBHeader.MeasurementBookStatus.CANCELLED.toString());
        jsonObject.addProperty("workOrderEstimateId", workOrderEstimate.getId());
        if (estimate != null) {
            jsonObject.addProperty("estimateNumber", estimate.getEstimateNumber());
            jsonObject.addProperty("estimateId", estimate.getId().toString());
            jsonObject.addProperty("projectCode", estimate.getProjectCode().getCode());
            jsonObject.addProperty("nameOfWork", estimate.getName());
            jsonObject.addProperty("isMeasurementsExist", measurementSheetService.existsByEstimate(estimate.getId()));
        } else {
            jsonObject.addProperty("estimateNumber", StringUtils.EMPTY);
            jsonObject.addProperty("estimateId", StringUtils.EMPTY);
            jsonObject.addProperty("projectCode", StringUtils.EMPTY);
            jsonObject.addProperty("nameOfWork", StringUtils.EMPTY);
        }

        setWorkOrderJsonValues(jsonObject, sdf, workOrder);

        jsonObject.addProperty("totalMBAmountOfMBs",
                totalMBAmountOfMBs != null ? Double.toString(totalMBAmountOfMBs) : StringUtils.EMPTY);
    }

    private void setWorkOrderJsonValues(final JsonObject jsonObject, final SimpleDateFormat sdf,
            final WorkOrder workOrder) {
        final DecimalFormat df = new DecimalFormat("0.00");

        if (workOrder != null) {
            jsonObject.addProperty("workOrderNumber", workOrder.getWorkOrderNumber());
            jsonObject.addProperty("workOrderId", workOrder.getId().toString());
            jsonObject.addProperty("workOrderAmount", df.format(workOrder.getWorkOrderAmount()));
            jsonObject.addProperty("contractorName", workOrder.getContractor().getName());
            jsonObject.addProperty("workOrderAssignedTo", workOrder.getEngineerIncharge().getName());
            jsonObject.addProperty("tenderFinalisedPercentage",
                    Double.toString(workOrder.getTenderFinalizedPercentage()));
            final OfflineStatus offlineStatus = offlineStatusService.getOfflineStatusByObjectIdAndObjectTypeAndStatus(
                    workOrder.getId(), WorksConstants.WORKORDER,
                    OfflineStatuses.WORK_COMMENCED.toString().toUpperCase());
            jsonObject.addProperty("workCommencedDate",
                    offlineStatus != null ? sdf.format(offlineStatus.getStatusDate()) : StringUtils.EMPTY);
        } else {
            jsonObject.addProperty("workOrderNumber", StringUtils.EMPTY);
            jsonObject.addProperty("workOrderId", StringUtils.EMPTY);
            jsonObject.addProperty("workOrderAmount", StringUtils.EMPTY);
            jsonObject.addProperty("contractorName", StringUtils.EMPTY);
            jsonObject.addProperty("workOrderAssignedTo", StringUtils.EMPTY);
            jsonObject.addProperty("tenderFinalisedPercentage", StringUtils.EMPTY);
        }
    }
}
