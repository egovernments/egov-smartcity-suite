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
import java.util.List;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.utils.ApplicationConstant;
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
        final DecimalFormat df = new DecimalFormat("0.00");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        jsonObject.addProperty("showHistory", false);
        if (workOrderEstimate != null) {
            jsonObject.addProperty("workOrderEstimateId", workOrderEstimate.getId());
            if (workOrderEstimate.getEstimate() != null) {
                final AbstractEstimate estimate = workOrderEstimate.getEstimate();
                jsonObject.addProperty("estimateNumber", estimate.getEstimateNumber());
                jsonObject.addProperty("estimateId", estimate.getId());
                jsonObject.addProperty("projectCode", estimate.getProjectCode().getCode());
                jsonObject.addProperty("nameOfWork", estimate.getName());
            } else {
                jsonObject.addProperty("estimateNumber", "");
                jsonObject.addProperty("estimateId", "");
                jsonObject.addProperty("projectCode", "");
                jsonObject.addProperty("nameOfWork", "");
            }
            if (workOrderEstimate.getWorkOrder() != null) {
                final WorkOrder workOrder = workOrderEstimate.getWorkOrder();
                jsonObject.addProperty("workOrderNumber", workOrder.getWorkOrderNumber());
                jsonObject.addProperty("workOrderId", workOrder.getId());
                jsonObject.addProperty("workOrderAmount", df.format(workOrder.getWorkOrderAmount()));
                jsonObject.addProperty("contractorName", workOrder.getContractor().getName());
                jsonObject.addProperty("workOrderAssignedTo", workOrder.getEngineerIncharge().getName());
                jsonObject.addProperty("tenderFinalisedPercentage", workOrder.getTenderFinalizedPercentage());
                final OfflineStatus offlineStatus = offlineStatusService
                        .getOfflineStatusByObjectIdAndObjectTypeAndStatus(workOrder.getId(), WorksConstants.WORKORDER,
                                OfflineStatuses.WORK_COMMENCED.toString().toUpperCase());
                if (offlineStatus != null)
                    jsonObject.addProperty("workCommencedDate", sdf.format(offlineStatus.getStatusDate()));
                else
                    jsonObject.addProperty("workCommencedDate", "");
                final Double totalMBAmountOfMBs = mbHeaderService.getTotalMBAmountOfMBs(null, workOrderEstimate.getId(),
                        MBHeader.MeasurementBookStatus.CANCELLED.toString());
                if (totalMBAmountOfMBs != null)
                    jsonObject.addProperty("totalMBAmountOfMBs", totalMBAmountOfMBs);
                else
                    jsonObject.addProperty("totalMBAmountOfMBs", "");
            } else {
                jsonObject.addProperty("workOrderNumber", "");
                jsonObject.addProperty("workOrderId", "");
                jsonObject.addProperty("workOrderAmount", "");
                jsonObject.addProperty("contractorName", "");
                jsonObject.addProperty("workOrderAssignedTo", "");
                jsonObject.addProperty("tenderFinalisedPercentage", "");
                jsonObject.addProperty("workCommencedDate", "");
                jsonObject.addProperty("totalMBAmountOfMBs", "");
            }

            final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                    WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_MB_QUANTITY_TOLERANCE_LEVEL);
            final AppConfigValues value = values.get(0);

            jsonObject.addProperty("quantityTolerance", value.getValue());

            final List<MBHeader> previousMBHeaders = mbHeaderService.getPreviousMBHeaders(-1L,
                    workOrderEstimate.getId());

            if (!previousMBHeaders.isEmpty())
                jsonObject.addProperty("previousMBDate",
                        sdf.format(previousMBHeaders.get(previousMBHeaders.size() - 1).getMbDate()));
            else
                jsonObject.addProperty("previousMBDate", "");

            jsonObject.addProperty("isMeasurementsExist",
                    measurementSheetService.existsByEstimate(workOrderEstimate.getEstimate().getId()));
            jsonObject.addProperty("workOrderEstimateId", workOrderEstimate.getId());
            if (workOrderEstimate.getEstimate().getLineEstimateDetails() != null
                    && workOrderEstimate.getEstimate().getLineEstimateDetails().getLineEstimate().isSpillOverFlag()) {
                final SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
                jsonObject.addProperty("cutOffDate",
                        worksUtils.getCutOffDate() != null ? sdf.format(worksUtils.getCutOffDate()) : "");
                jsonObject.addProperty("cutOffDateDisplay",
                        worksUtils.getCutOffDate() != null ? fmt.format(worksUtils.getCutOffDate()) : "");
            }
            jsonObject.addProperty("spillOverFlag",
                    workOrderEstimate.getEstimate().getLineEstimateDetails().getLineEstimate().isSpillOverFlag());
            
            jsonObject.addProperty("additionalRule",
                    (String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));

        }
        return jsonObject;
    }
}
