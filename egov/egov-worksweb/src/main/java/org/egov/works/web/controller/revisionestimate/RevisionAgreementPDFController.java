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
package org.egov.works.web.controller.revisionestimate;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.letterofacceptance.service.WorkOrderActivityService;
import org.egov.works.masters.entity.Contractor;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.revisionestimate.service.RevisionEstimateService;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/revisionestimate")
public class RevisionAgreementPDFController {

    public static final String REVISIONAGREEMENTPDF = "RevisionAgreementPDF";

    @Autowired
    private RevisionEstimateService revisionEstimateService;

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private ReportService reportService;

    @Autowired
    private WorkOrderActivityService workOrderActivityService;

    @RequestMapping(value = "/revisionagreementPDF/{revisionEstimateId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> generateContractorBillPDF(final HttpServletRequest request,
            @PathVariable("revisionEstimateId") final Long revisionEstimateId) throws IOException {
        final RevisionAbstractEstimate revisionEstimate = revisionEstimateService
                .getRevisionEstimateById(revisionEstimateId);
        return generateReport(revisionEstimate, request);
    }

    private ResponseEntity<byte[]> generateReport(final RevisionAbstractEstimate revisionEstimate,
            final HttpServletRequest request) {

        final Map<String, Object> reportParams = new HashMap<String, Object>();
        final DecimalFormat df = new DecimalFormat("0.00");
        ReportRequest reportInput = null;
        ReportOutput reportOutput;
        if (revisionEstimate != null) {
            final WorkOrderEstimate revisionWorkOrderEstimate = workOrderEstimateService
                    .getWorkOrderEstimateByAbstractEstimateId(revisionEstimate.getId());
            final WorkOrder revisionworkOrder = revisionWorkOrderEstimate.getWorkOrder();
            final AbstractEstimate originalEstimate = revisionEstimate.getParent();
            final WorkOrderEstimate workOrderEstimate = workOrderEstimateService
                    .getWorkOrderEstimateByAbstractEstimateId(originalEstimate.getId());
            final String url = WebUtils.extractRequestDomainURL(request, false);
            reportParams.put("cityLogo", url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
                    .concat((String) request.getSession().getAttribute("citylogo")));

            final String cityName = ApplicationThreadLocals.getMunicipalityName();
            reportParams.put("cityName", cityName);
            reportParams.put("revisionEstimate", revisionEstimate);
            reportParams.put("suplimentAgreementAmount", BigDecimal.valueOf(revisionworkOrder.getWorkOrderAmount())
                    .setScale(2, BigDecimal.ROUND_UP).toString());
            reportParams.put("workOrderNumber", workOrderEstimate.getWorkOrder().getWorkOrderNumber() != null
                    ? workOrderEstimate.getWorkOrder().getWorkOrderNumber() : StringUtils.EMPTY);
            reportParams.put("workOrderDate", revisionworkOrder.getWorkOrderDate() != null
                    ? DateUtils.getDefaultFormattedDate(revisionworkOrder.getWorkOrderDate()) : StringUtils.EMPTY);

            setReportParamsForContractor(reportParams, revisionworkOrder);
            reportParams.put("subject", originalEstimate.getName());
            reportParams.put("modeOfAllotment", !StringUtils.isBlank(originalEstimate.getModeOfAllotment())
                    ? originalEstimate.getModeOfAllotment() : StringUtils.EMPTY);
            reportParams.put("agreementAmount", df.format(revisionworkOrder.getWorkOrderAmount()));
            reportParams.put("WINCode", originalEstimate.getProjectCode().getCode());
            reportParams.put("amountOfEstimate",
                    revisionEstimate.getEstimateValue().setScale(2, BigDecimal.ROUND_HALF_EVEN));
            reportParams.put("ward", originalEstimate.getWard().getName());
            revisionEstimateService.prepareNonTenderedAndLumpSumActivities(revisionEstimate);
            reportParams.put("nonTenderedActivities", revisionEstimate.getNonTenderedActivities());
            reportParams.put("LumpSumActivities", revisionEstimate.getLumpSumActivities());
            revisionEstimateService.prepareChangeQuantityActivities(revisionEstimate);
            reportParams.put("changeQuatityActivities", revisionEstimate.getChangeQuantityActivities());
            setReportParamsForRevisionEstimate(revisionEstimate, reportParams);
            setTenderedAndNonTenderedAmountAndTenderFinalizedPercentage(revisionEstimate, reportParams,
                    revisionworkOrder);
            reportParams.put("currDate", DateUtils.getFormattedDateWithTimeStamp(new DateTime()));
            reportParams.put("workValue", df.format(revisionEstimate.getWorkValue()));
            reportParams.put("reportRunDate", DateUtils.getFormattedDateWithTimeStamp(new DateTime()));

            String technicalSanctionByDesignation;
            if (!originalEstimate.getEstimateTechnicalSanctions().isEmpty() && originalEstimate
                    .getEstimateTechnicalSanctions().get(originalEstimate.getEstimateTechnicalSanctions().size() - 1)
                    .getTechnicalSanctionBy() != null)
                technicalSanctionByDesignation = worksUtils.getUserDesignation(originalEstimate
                        .getEstimateTechnicalSanctions()
                        .get(originalEstimate.getEstimateTechnicalSanctions().size() - 1).getTechnicalSanctionBy());
            else
                technicalSanctionByDesignation = worksUtils.getUserDesignation(originalEstimate.getCreatedBy());
            reportParams.put("technicalSanctionByDesignation", technicalSanctionByDesignation);
            reportInput = new ReportRequest(REVISIONAGREEMENTPDF, revisionEstimate, reportParams);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        if (revisionEstimate != null)
            headers.add("content-disposition",
                    "inline;filename=RevisionAgreement_" + revisionEstimate.getEstimateNumber() + ".pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    private void setReportParamsForRevisionEstimate(final RevisionAbstractEstimate revisionEstimate,
            final Map<String, Object> reportParams) {
        if (!revisionEstimate.getNonTenderedActivities().isEmpty())
            reportParams.put("nonTenderedActivitiesFlag", Boolean.FALSE);
        else
            reportParams.put("nonTenderedActivitiesFlag", Boolean.TRUE);
        if (!revisionEstimate.getLumpSumActivities().isEmpty())
            reportParams.put("LumpSumActivitiesFlag", Boolean.FALSE);
        else
            reportParams.put("LumpSumActivitiesFlag", Boolean.TRUE);
        if (!revisionEstimate.getChangeQuantityActivities().isEmpty())
            reportParams.put("changeQuatityActivitiesFlag", Boolean.FALSE);
        else
            reportParams.put("changeQuatityActivitiesFlag", Boolean.TRUE);
    }

    private void setTenderedAndNonTenderedAmountAndTenderFinalizedPercentage(
            final RevisionAbstractEstimate revisionEstimate, final Map<String, Object> reportParams,
            final WorkOrder revisionworkOrder) {
        double tenderAmount = 0;
        double nonTenderAmount = 0;
        double approvedAmount;
        final Double tenderFinalizedPercentage = revisionworkOrder.getParent().getTenderFinalizedPercentage();
        for (final Activity activity : revisionEstimate.getChangeQuantityActivities()) {
            final WorkOrderActivity workOrderActivity = workOrderActivityService
                    .getWorkOrderActivityByActivity(activity.getId());
            approvedAmount = new Money(activity.getRate() * workOrderActivity.getApprovedQuantity()).getValue();
            if (activity.getRevisionType() != null && RevisionType.REDUCED_QUANTITY.equals(activity.getRevisionType()))
                approvedAmount = approvedAmount * -1;
            if (activity != null && activity.getParent() != null && activity.getParent().getRevisionType() == null
                    && (RevisionType.ADDITIONAL_QUANTITY.toString()
                            .equalsIgnoreCase(activity.getRevisionType().toString())
                            || RevisionType.REDUCED_QUANTITY.toString()
                                    .equalsIgnoreCase(activity.getRevisionType().toString())))
                tenderAmount = tenderAmount + approvedAmount;
            else
                nonTenderAmount = nonTenderAmount + approvedAmount;
        }
        // Apply percentage for change quantity items for tender items in
        // case of percentage
        // tender
        if (!tenderFinalizedPercentage.equals(Double.valueOf(0)))
            tenderAmount = tenderAmount + tenderAmount * tenderFinalizedPercentage / 100;

        reportParams.put("tenderFinalizedPercentage", tenderFinalizedPercentage);
        reportParams.put("changeQtyTenderAmount", tenderAmount);
        reportParams.put("changeQtyNonTenderAmount", nonTenderAmount);
    }

    private void setReportParamsForContractor(final Map<String, Object> reportParams,
            final WorkOrder revisionworkOrder) {
        final Contractor contractor = revisionworkOrder.getContractor();
        if (contractor != null) {
            reportParams.put("contractorName", contractor.getName());
            if (contractor.getCorrespondenceAddress() != null)
                reportParams.put("contractorAddress", contractor.getCorrespondenceAddress());
            else
                reportParams.put("contractorAddress", StringUtils.EMPTY);
            if (contractor.getPanNumber() != null)
                reportParams.put("panNo", contractor.getPanNumber());
            else
                reportParams.put("panNo", StringUtils.EMPTY);
            if (contractor.getBank() != null)
                reportParams.put("bank", contractor.getBank().getName());
            else
                reportParams.put("bank", StringUtils.EMPTY);
            if (contractor.getBankAccount() != null)
                reportParams.put("accountNo", contractor.getBankAccount());
            else
                reportParams.put("accountNo", StringUtils.EMPTY);
        }
    }

}
