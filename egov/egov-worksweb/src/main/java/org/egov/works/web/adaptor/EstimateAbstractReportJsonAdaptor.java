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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.egov.infra.utils.StringUtils;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.reports.entity.EstimateAbstractReport;
import org.egov.works.reports.service.WorkProgressRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class EstimateAbstractReportJsonAdaptor implements JsonSerializer<EstimateAbstractReport> {
    final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

    private static final String LINEESTIMATES = "lineEstimates";
    @Autowired
    private WorkProgressRegisterService workProgressRegisterService;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @Override
    public JsonElement serialize(final EstimateAbstractReport estimateAbstractReport, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (estimateAbstractReport != null) {
            if (estimateAbstractReport.getDepartmentName() != null)
                jsonObject.addProperty("departmentName", estimateAbstractReport.getDepartmentName());
            else
                jsonObject.addProperty("departmentName", StringUtils.EMPTY);

            setTOWJsonValues(estimateAbstractReport, jsonObject);

            setLEJsonValues(estimateAbstractReport, jsonObject);

            if (estimateAbstractReport.getAdminSanctionedEstimates() != null)
                jsonObject.addProperty("adminSanctionedEstimates",
                        estimateAbstractReport.getAdminSanctionedEstimates());
            else
                jsonObject.addProperty("adminSanctionedEstimates", StringUtils.EMPTY);

            setAdminSanctionedAmountInCrores(estimateAbstractReport, jsonObject);

            if (estimateAbstractReport.getTechnicalSanctionedEstimates() != null)
                jsonObject.addProperty("technicalSanctionedEstimates",
                        estimateAbstractReport.getTechnicalSanctionedEstimates());
            else
                jsonObject.addProperty("technicalSanctionedEstimates", StringUtils.EMPTY);

            setLOAJsonObjectValues(estimateAbstractReport, jsonObject);

            if (estimateAbstractReport.getWorkNotCommenced() != null)
                jsonObject.addProperty("workNotCommenced", estimateAbstractReport.getWorkNotCommenced());
            else
                jsonObject.addProperty("workNotCommenced", StringUtils.EMPTY);

            setJsonObjectValues(estimateAbstractReport, jsonObject);

            jsonObject.addProperty("createdDate",
                    formatter.format(workProgressRegisterService.getReportSchedulerRunDate()));

        }
        return jsonObject;
    }

    private void setLEJsonValues(final EstimateAbstractReport estimateAbstractReport, final JsonObject jsonObject) {
        if (worksApplicationProperties.lineEstimateRequired() && estimateAbstractReport.getLineEstimates() != null)
            jsonObject.addProperty(LINEESTIMATES, estimateAbstractReport.getLineEstimates());
        else if (estimateAbstractReport.getAbstractEstimates() != null)
            jsonObject.addProperty(LINEESTIMATES, estimateAbstractReport.getAbstractEstimates());
        else
            jsonObject.addProperty(LINEESTIMATES, StringUtils.EMPTY);
    }

    private void setLOAJsonObjectValues(final EstimateAbstractReport estimateAbstractReport,
            final JsonObject jsonObject) {
        if (estimateAbstractReport.getLoaCreated() != null)
            jsonObject.addProperty("loaCreated", estimateAbstractReport.getLoaCreated());
        else
            jsonObject.addProperty("loaCreated", StringUtils.EMPTY);

        if (estimateAbstractReport.getLoaNotCreated() != null)
            jsonObject.addProperty("loaNotCreated", estimateAbstractReport.getLoaNotCreated());
        else
            jsonObject.addProperty("loaNotCreated", StringUtils.EMPTY);
    }

    private void setTOWJsonValues(final EstimateAbstractReport estimateAbstractReport, final JsonObject jsonObject) {
        if (estimateAbstractReport.getTypeOfWorkName() != null)
            jsonObject.addProperty("typeOfWorkName", estimateAbstractReport.getTypeOfWorkName());
        else
            jsonObject.addProperty("typeOfWorkName", StringUtils.EMPTY);

        if (estimateAbstractReport.getSubTypeOfWorkName() != null)
            jsonObject.addProperty("subTypeOfWorkName", estimateAbstractReport.getSubTypeOfWorkName());
        else
            jsonObject.addProperty("subTypeOfWorkName", StringUtils.EMPTY);
    }

    private void setJsonObjectValues(final EstimateAbstractReport estimateAbstractReport, final JsonObject jsonObject) {
        if (estimateAbstractReport.getAgreementValueInCrores() != null)
            jsonObject.addProperty("agreementValueInCrores",
                    new BigDecimal(estimateAbstractReport.getAgreementValueInCrores())
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty("agreementValueInCrores", StringUtils.EMPTY);

        if (estimateAbstractReport.getWorkInProgress() != null)
            jsonObject.addProperty("workInProgress", estimateAbstractReport.getWorkInProgress());
        else
            jsonObject.addProperty("workInProgress", StringUtils.EMPTY);

        if (estimateAbstractReport.getWorkCompleted() != null)
            jsonObject.addProperty("workCompleted", estimateAbstractReport.getWorkCompleted());
        else
            jsonObject.addProperty("workCompleted", StringUtils.EMPTY);

        if (estimateAbstractReport.getBillsCreated() != null)
            jsonObject.addProperty("billsCreated", estimateAbstractReport.getBillsCreated());
        else
            jsonObject.addProperty("billsCreated", StringUtils.EMPTY);
    }

    private void setAdminSanctionedAmountInCrores(final EstimateAbstractReport estimateAbstractReport,
            final JsonObject jsonObject) {
        if (estimateAbstractReport.getLeAdminSanctionedAmountInCrores() != null)
            jsonObject.addProperty("leAdminSanctionedAmountInCrores",
                    new BigDecimal(estimateAbstractReport.getLeAdminSanctionedAmountInCrores())
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty("leAdminSanctionedAmountInCrores", StringUtils.EMPTY);
        if (estimateAbstractReport.getAeAdminSanctionedAmountInCrores() != null)
            jsonObject.addProperty("aeAdminSanctionedAmountInCrores",
                    new BigDecimal(estimateAbstractReport.getAeAdminSanctionedAmountInCrores())
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty("aeAdminSanctionedAmountInCrores", StringUtils.EMPTY);

        if (estimateAbstractReport.getWorkValueOfAdminSanctionedAEInCrores() != null)
            jsonObject.addProperty("workValueOfAdminSanctionedAEInCrores",
                    new BigDecimal(estimateAbstractReport.getWorkValueOfAdminSanctionedAEInCrores())
                            .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty("workValueOfAdminSanctionedAEInCrores", StringUtils.EMPTY);

        if (estimateAbstractReport.getBillValueInCrores() != null)
            jsonObject.addProperty("billValueInCrores", new BigDecimal(estimateAbstractReport.getBillValueInCrores())
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty("billValueInCrores", StringUtils.EMPTY);
    }
}