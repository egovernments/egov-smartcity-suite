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
package org.egov.works.web.controller.contractorbill;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.contractorbill.entity.ContractorBillCertificateInfo;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.masters.entity.Contractor;
import org.egov.works.mb.entity.MBDetails;
import org.egov.works.mb.service.MBDetailsService;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.offlinestatus.service.OfflineStatusService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrder.OfflineStatuses;
import org.egov.works.workorder.entity.WorkOrderActivity;
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
@RequestMapping(value = "/contractorbill")
public class CompletionCertificatePDFController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Autowired
    private OfflineStatusService offlineStatusService;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private MBDetailsService mbDetailsService;

    @Autowired
    private WorksUtils worksUtils;

    public static final String CONTRACTORCOMPLETIONBILLPDF = "completionCertificate";

    @RequestMapping(value = "/completioncertificatepdf/{contractorBillId}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> generateContractorBillPDF(final HttpServletRequest request,
            @PathVariable("contractorBillId") final Long id, final HttpSession session) throws IOException {
        final ContractorBillRegister contractorBillRegister = contractorBillRegisterService.getContractorBillById(id);

        return generateReport(contractorBillRegister, request);
    }

    private ResponseEntity<byte[]> generateReport(final ContractorBillRegister contractorBillRegister,
            final HttpServletRequest request) {
        final Map<String, Object> reportParams = new HashMap<>();
        ReportRequest reportInput = null;
        ReportOutput reportOutput;
        if (contractorBillRegister != null) {

            final DecimalFormat df = new DecimalFormat("0.00");

            final String url = WebUtils.extractRequestDomainURL(request, false);

            final AbstractEstimate ae = contractorBillRegister.getWorkOrderEstimate().getEstimate();
            final WorkOrder workOrder = contractorBillRegister.getWorkOrderEstimate().getWorkOrder();
            final Contractor contractor = workOrder.getContractor();
            final OfflineStatus offlineStatusses = offlineStatusService
                    .getOfflineStatusByObjectIdAndObjectTypeAndStatus(workOrder.getId(), WorksConstants.WORKORDER,
                            OfflineStatuses.WORK_COMMENCED.toString().toUpperCase());
            if (ae != null) {
                reportParams.put("nameOfWork", ae.getName());
                reportParams.put("estimateNumber", ae.getEstimateNumber());
                reportParams.put("winCode", ae.getProjectCode().getCode());
                reportParams.put("estimateValue", df.format(ae.getEstimateValue()));
                reportParams.put("ward", ae.getWard().getName());
            } else {
                reportParams.put("estimateNumber", "NA");
                reportParams.put("winCode", "NA");
            }
            if (contractor != null) {
                reportParams.put("contractorName", contractor.getName());
                reportParams.put("contractorCode", contractor.getCode());
            } else {
                reportParams.put("contractorName", StringUtils.EMPTY);
                reportParams.put("contractorCode", StringUtils.EMPTY);
            }
            if (workOrder != null) {
                reportParams.put("workOrderAmount", df.format(workOrder.getWorkOrderAmount()));
                reportParams.put("workOrderNumber", workOrder.getWorkOrderNumber());
            }
            reportParams.put("workCommencedOn",
                    DateUtils.getFormattedDate(offlineStatusses.getStatusDate(), "dd/MM/yyyy"));
            reportParams.put("workCompletedDate", DateUtils.getFormattedDate(
                    contractorBillRegister.getWorkOrderEstimate().getWorkCompletionDate(), "dd/MM/yyyy"));

            reportParams.put("crearedBy", worksUtils.getUserDesignation(contractorBillRegister.getCreatedBy()));
            reportParams.put("approvedBy", worksUtils.getUserDesignation(contractorBillRegister.getApprovedBy()));
            reportParams.put("cityLogo", url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
                    .concat((String) request.getSession().getAttribute("citylogo")));

            reportParams.put("cityName", ApplicationThreadLocals.getMunicipalityName());
            reportParams.put("reportRunDate", DateUtils.getFormattedDate(new Date(), "dd/MM/yyyy hh:mm a"));

            double totalAsPerTender = 0.0;
            double totalAsPerExecution = 0.0;
            double totalDifference;
            final List<ContractorBillCertificateInfo> contractorBillCertificateInfoList = new ArrayList<>();
            for (final WorkOrderActivity woa : contractorBillRegister.getWorkOrderEstimate().getWorkOrderActivities()) {
                final Activity act = woa.getActivity();
                final List<Activity> activities = estimateService.getActivitiesByParent(act.getId());
                activities.add(act);

                double quantity = 0.0;

                for (final Activity activity : activities) {
                    final ContractorBillCertificateInfo contractorBillCertificateInfo = new ContractorBillCertificateInfo();
                    final List<MBDetails> detailsList = mbDetailsService.getMBDetailsByWorkOrderActivity(woa.getId());
                    for (final MBDetails mbDetails : detailsList)
                        quantity += mbDetails.getQuantity();
                    contractorBillCertificateInfo.setExecutionQuantity(quantity);
                    contractorBillCertificateInfo.setTenderQuantity(activity.getQuantity());
                    contractorBillCertificateInfo.setTenderAmount(activity.getAmount().getValue());
                    contractorBillCertificateInfo.setTenderRate(activity.getEstimateRate());
                    contractorBillCertificateInfo.setExecutionRate(activity.getEstimateRate());
                    contractorBillCertificateInfo.setExecutionAmount(quantity * activity.getRate());
                    contractorBillCertificateInfo.setWorkOrderActivity(woa);
                    totalAsPerTender += activity.getEstimateRate() * activity.getQuantity();
                    totalAsPerExecution += activity.getEstimateRate() * quantity;
                    contractorBillCertificateInfo.setTotalAsPerTender(totalAsPerTender);
                    contractorBillCertificateInfo.setTotalAsPerExecution(totalAsPerExecution);
                    totalDifference = totalAsPerTender - totalAsPerExecution;
                    contractorBillCertificateInfo.setTotalDifference(totalDifference);
                    contractorBillCertificateInfoList.add(contractorBillCertificateInfo);
                }
            }

            reportInput = new ReportRequest(CONTRACTORCOMPLETIONBILLPDF, contractorBillCertificateInfoList, reportParams);

        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition",
                "inline;filename=CompletionCertificate_" + contractorBillRegister.getBillnumber() + ".pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);

    }

}
