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

package org.egov.works.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.works.reports.entity.EstimateAbstractReport;
import org.egov.works.reports.service.WorkProgressRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Component
public class EstimateAbstractReportJsonAdaptor implements JsonSerializer<EstimateAbstractReport> {
    final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

    @Autowired
    private WorkProgressRegisterService workProgressRegisterService;

    @Override
    public JsonElement serialize(final EstimateAbstractReport estimateAbstractReport, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (estimateAbstractReport != null) {
            if (estimateAbstractReport.getDepartmentName() != null)
                jsonObject.addProperty("departmentName", estimateAbstractReport.getDepartmentName());
            else
                jsonObject.addProperty("departmentName", "");

            if (estimateAbstractReport.getTypeOfWorkName() != null)
                jsonObject.addProperty("typeOfWorkName", estimateAbstractReport.getTypeOfWorkName());
            else
                jsonObject.addProperty("typeOfWorkName", "");

            if (estimateAbstractReport.getSubTypeOfWorkName() != null)
                jsonObject.addProperty("subTypeOfWorkName", estimateAbstractReport.getSubTypeOfWorkName());
            else
                jsonObject.addProperty("subTypeOfWorkName", "");

            if (estimateAbstractReport.getLineEstimates() != null)
                jsonObject.addProperty("lineEstimates", estimateAbstractReport.getLineEstimates());
            else
                jsonObject.addProperty("lineEstimates", "");

            if (estimateAbstractReport.getAdminSanctionedEstimates() != null)
                jsonObject.addProperty("adminSanctionedEstimates", estimateAbstractReport.getAdminSanctionedEstimates());
            else
                jsonObject.addProperty("adminSanctionedEstimates", "");

            if (estimateAbstractReport.getAdminSanctionedAmountInCrores() != null)
                jsonObject.addProperty(
                        "adminSanctionedAmountInCrores",
                        new BigDecimal(estimateAbstractReport.getAdminSanctionedAmountInCrores()).setScale(2,
                                BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("adminSanctionedAmountInCrores", "");

            if (estimateAbstractReport.getTechnicalSanctionedEstimates() != null)
                jsonObject.addProperty("technicalSanctionedEstimates",
                        estimateAbstractReport.getTechnicalSanctionedEstimates());
            else
                jsonObject.addProperty("technicalSanctionedEstimates", "");

            if (estimateAbstractReport.getLoaCreated() != null)
                jsonObject.addProperty("loaCreated", estimateAbstractReport.getLoaCreated());
            else
                jsonObject.addProperty("loaCreated", "");

            if (estimateAbstractReport.getAgreementValueInCrores() != null)
                jsonObject.addProperty("agreementValueInCrores",
                        new BigDecimal(estimateAbstractReport.getAgreementValueInCrores())
                                .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("agreementValueInCrores", "");

            if (estimateAbstractReport.getWorkInProgress() != null)
                jsonObject.addProperty("workInProgress", estimateAbstractReport.getWorkInProgress());
            else
                jsonObject.addProperty("workInProgress", "");

            if (estimateAbstractReport.getWorkCompleted() != null)
                jsonObject.addProperty("workCompleted", estimateAbstractReport.getWorkCompleted());
            else
                jsonObject.addProperty("workCompleted", "");

            if (estimateAbstractReport.getBillsCreated() != null)
                jsonObject.addProperty("billsCreated", estimateAbstractReport.getBillsCreated());
            else
                jsonObject.addProperty("billsCreated", "");

            if (estimateAbstractReport.getBillValueInCrores() != null)
                jsonObject.addProperty("billValueInCrores", new BigDecimal(estimateAbstractReport.getBillValueInCrores())
                        .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("billValueInCrores", "");

            jsonObject.addProperty("createdDate", formatter.format(workProgressRegisterService.getReportSchedulerRunDate()));

        }
        return jsonObject;
    }
}